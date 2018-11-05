/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 *
 */

package com.fanniemae.mbsportal.api.controller.interceptor.entitlement.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.cdx.CDXClientApi;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementRolePO;
import com.fanniemae.mbsportal.api.po.ProfileSessionPO;
import com.fanniemae.mbsportal.api.service.ExceptionLookupService;
import com.fanniemae.mbsportal.api.service.ProfileEntitlementService;
import com.fanniemae.mbsportal.api.service.ProfileSessionService;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.ExceptionResponsePO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSRetryableException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author: Rajiv Chaudhuri
 * @Date: Jul 11, 2018
 * @File: com.fanniemae.mbsportal.api.controller.interceptor.entitlement.
 *        EntitlementInterceptorService.java
 * @Revision:
 * @Description: EntitlementInterceptorService.java
 */

@Component
@EnableRetry
public class EntitlementInterceptorService {

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EntitlementInterceptorService.class);

    /**
     *
     * profileSessionService ProfileSessionService
     */
    @Autowired
    ProfileSessionService profileSessionService;

    /**
     *
     * cDXClientApi CDXClientApi
     */
    @Autowired
    private CDXClientApi cDXClientApi;

    /**
     *
     * profileEntitlementService ProfileEntitlementService
     */
    @Autowired
    private ProfileEntitlementService profileEntitlementService;

    /**
     *
     * exceptionLookupService ExceptionLookupService
     */
    @Autowired
    private ExceptionLookupService exceptionLookupService;

    /**
     *
     * mapper ObjectMapper
     */
    @Autowired
    ObjectMapper mapper;

    /**
     *
     * @param sessionId
     *            the sessionId
     * @param authorizedRoles
     *            the authorizedRoles
     * @param request
     *            the request
     * @param response
     *            the response
     * @param handler
     *            the handler
     * @return boolean Whether to proceed further or not
     * @throws Exception
     */

    @Retryable(value = {
            MBSRetryableException.class }, maxAttemptsExpression = "#{${cdx.client.retryMaxAttempts}}", backoff = @Backoff(delayExpression = "#{${cdx.client.retryBackoff}}"))

    public boolean performAuthorizationUsingSessionID(final String sessionId, final List<String> authorizedRoles,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        /*
         * If Session ID Presence then - - Get the Profile - Get the user name
         * based on session ID from Local Cache & check user name - Does match
         * with CDX Profile and Local Cache Then - Get Profile Details for roles
         * - Else (Not Exist at Local Cache or Session was tied with different
         * users) -Save the session and user name details from CDX Profile at
         * Local Cache - Get Profile Details for roles
         */
        LOGGER.debug("Entering performAuthorizationUsingSessionID method in EntitlementInterceptorService");
        boolean proceedFurther = true;
        String userName = null;
        ProfileEntitlementPO profileEntitlementPO = null;
        ProfileSessionPO profileSessionPO = null;
        List<String> userRoles = new ArrayList<>();
        try {

            // Get Profile from Local Cache
            profileSessionPO = profileSessionService.getProfileSession(sessionId);
            /*
             * If session Profile does not exists
             */
            if (profileSessionPO == null) {
                LOGGER.debug("Session info does not exist, going to call Gateway to validate; session ID: {}",
                        sessionId);
                profileEntitlementPO = cDXClientApi.getProfileFromSessionID(getHeadersInfo(request));
                userName = profileEntitlementPO.getUserName();
                profileSessionPO = new ProfileSessionPO();
                profileSessionPO.setSessionId(sessionId);
                profileSessionPO.setUserName(userName);
                profileSessionPO = profileSessionService.saveProfileSession(profileSessionPO);
                LOGGER.debug("Session info completed; username: {}", userName);
            } else {
                LOGGER.debug("Session info exist, taking the user name from existing local cache; session ID: {}",
                        sessionId);
                userName = profileSessionPO.getUserName();
                LOGGER.debug("Session info completed; username: {}", userName);
            }
            /*
             * Set the User ID (For Logback MDC- Variable)
             */
            MDC.put(MBSExceptionConstants.INFO_USER_ID, userName);
            
            // Fetch Profile from
            profileEntitlementPO = profileEntitlementService.getProfile(userName);
            // Get the roles
            for (ProfileEntitlementRolePO profileEntitlementRolePO : profileEntitlementPO.getRoles()) {
                userRoles.add(profileEntitlementRolePO.getName());
            }
            // convert the roles to lower case
            userRoles = userRoles.stream().map(String::toLowerCase).collect(Collectors.toList());

            // Lender Role - Then does support
            List<String> supportedRoles = Arrays.asList(EntitlementRole.SESSION_ID_SUPPORTED_ROLES);
            supportedRoles = supportedRoles.stream().map(String::toLowerCase).collect(Collectors.toList());

            // check if supported role presence at user profile roles
            if (!CollectionUtils.containsAny(supportedRoles, userRoles)) {
                LOGGER.error("Supported roles not available for user roles" + userRoles);
                response.reset();
                String json;

                List<ExceptionLookupPO> exceptionLookupPOLst = null;
                try {
                    exceptionLookupPOLst = exceptionLookupService.getExceptionLookupData(Optional.of("SYSM_0001"));

                } catch (MBSBaseException ex) {
                    LOGGER.error("Failed to retieve error messages" + ex);
                    json = mapper.writeValueAsString(MBSPortalUtils.getExceptionInternalServer());
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().print(json);
                    LOGGER.debug("Exiting performAuthorizationUsingSessionID method in EntitlementInterceptorService");
                    return false;

                }
                ExceptionResponsePO exceptionResponsePO = MBSPortalUtils.getExceptionMessage(exceptionLookupPOLst);
                json = mapper.writeValueAsString(exceptionResponsePO);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().print(json);
                if (exceptionResponsePO.getMessages().get(0).getMessageCode().equals(MBSExceptionConstants.SYSM_0002)) {
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

                } else {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());

                }
                LOGGER.debug("Exiting performAuthorizationUsingSessionID method in EntitlementInterceptorService");
                return false;
            }
            // Final Authorization
            if (!CollectionUtils.containsAny(authorizedRoles, userRoles)) {
                LOGGER.error("Authorized roles not available for user roles" + userRoles);
                response.reset();
                String json;

                List<ExceptionLookupPO> exceptionLookupPOLst = null;
                try {
                    exceptionLookupPOLst = exceptionLookupService
                            .getExceptionLookupData(Optional.of(MBSExceptionConstants.SYSM_0001));
                } catch (MBSBaseException ex) {
                    LOGGER.error("Failed to retieve error messages" + ex);
                    json = mapper.writeValueAsString(MBSPortalUtils.getExceptionInternalServer());
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().print(json);
                    LOGGER.debug("Exiting performAuthorizationUsingSessionID method in EntitlementInterceptorService");
                    return false;

                }
                ExceptionResponsePO exceptionResponsePO = MBSPortalUtils.getExceptionMessage(exceptionLookupPOLst);
                json = mapper.writeValueAsString(exceptionResponsePO);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().print(json);
                if (exceptionResponsePO.getMessages().get(0).getMessageCode().equals(MBSExceptionConstants.SYSM_0002)) {
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

                } else {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());

                }
                LOGGER.debug("Exiting performAuthorizationUsingSessionID method in EntitlementInterceptorService");
                return false;
            }

        } catch (MBSRetryableException exe) {
            LOGGER.error("Exception in  method performAuthorizationUsingSessionID :", exe);
            throw exe;

        } catch (MBSBaseException exe) {

            LOGGER.error("Exception in  method performAuthorizationUsingSessionID :", exe);
            response.reset();
            String json;
            List<ExceptionLookupPO> exceptionLookupPOLst = null;
            try {
                exceptionLookupPOLst = exceptionLookupService.getExceptionLookupData(Optional.of("SYSM_0001"));
            } catch (MBSBaseException ex) {
                LOGGER.error("Failed to retieve error messages" + ex);
                json = mapper.writeValueAsString(MBSPortalUtils.getExceptionInternalServer());
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().print(json);
                LOGGER.debug("Exiting performAuthorizationUsingSessionID method in EntitlementInterceptorService");
                return false;
            }
            ExceptionResponsePO exceptionResponsePO = MBSPortalUtils.getExceptionMessage(exceptionLookupPOLst);
            json = mapper.writeValueAsString(exceptionResponsePO);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().print(json);
            if (exceptionResponsePO.getMessages().get(0).getMessageCode().equals(MBSExceptionConstants.SYSM_0002)) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
            LOGGER.debug("Exiting performAuthorizationUsingSessionID method in EntitlementInterceptorService");
            return false;
        }
        LOGGER.debug("Exiting performAuthorizationUsingSessionID method in EntitlementInterceptorService");
        return proceedFurther;
    }

    /**
     *
     * @param request
     *            the HttpServletRequest
     * @return nothing
     */
    private Map<String, String> getHeadersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<String, String>();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }
}
