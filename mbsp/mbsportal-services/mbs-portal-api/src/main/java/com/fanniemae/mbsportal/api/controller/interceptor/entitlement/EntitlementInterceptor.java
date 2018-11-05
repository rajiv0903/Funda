/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.controller.interceptor.entitlement;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fanniemae.mbsportal.api.cdx.CDXClientApi;
import com.fanniemae.mbsportal.api.controller.interceptor.entitlement.service.EntitlementInterceptorService;
import com.fanniemae.mbsportal.api.service.ExceptionLookupService;
import com.fanniemae.mbsportal.cdx.config.CDXApiClientConfig;
import com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.ExceptionResponsePO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Rajiv Chaudhuri
 * @Date: Mar 13, 2018
 * @File: com.fanniemae.mbsportal.api.controller.interceptor.entitlement.
 *        EntitlementInterceptor.java
 * @Revision :
 * @Description: CMMBSSTA01-1212 : (Tech) Trader user login calls MBSP API with
 *               valid Session ID
 */
@Component
public class EntitlementInterceptor extends HandlerInterceptorAdapter {

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EntitlementInterceptor.class);

    /**
     *
     * cDXClientApi CDXClientApi
     */
    @Autowired
    private CDXClientApi cDXClientApi;

    /**
     *
     * cDXApiClientConfig CDXApiClientConfig
     */
    @Autowired
    CDXApiClientConfig cDXApiClientConfig;

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
     * entitlementInterceptorService EntitlementInterceptorService
     */
    @Autowired
    EntitlementInterceptorService entitlementInterceptorService;

    /**
     *
     *
     * @param request
     *            the HttpServletRequest
     * @param response
     *            the HttpServletResponse
     * @param handler
     *            the handler
     * @return boolean
     * @throws Exception
     *
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        LOGGER.debug("Entering preHandle method in EntitlementInterceptor ");
        if (handler instanceof HandlerMethod) {

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // EntitlementRequired entitlementRequired =
            // handlerMethod.getMethod().getAnnotation(EntitlementRequired.class);
            EntitlementRequired entitlementRequired = handlerMethod.getMethodAnnotation(EntitlementRequired.class);

            if (entitlementRequired != null) {

                String signedToken = request.getHeader(CDXHeaderMap.JWS_TOKEN.getValue());
                String sessionId = request.getHeader(CDXHeaderMap.SESSION_ID.getValue());

                /*
                 * CMMBSSTA01-1212 : (Tech) Trader user login calls MBSP API
                 * with valid Session ID if Signed Token or Session ID none of
                 * them presents and pass through is true then by pass
                 */
                if (cDXApiClientConfig.isEntitlementpassthrough()
                        && (StringUtils.isBlank(signedToken) && StringUtils.isBlank(sessionId))) {
                    LOGGER.debug("Exiting preHandle method in EntitlementInterceptor");
                    LOGGER.debug("EntitlementInterceptor : preHandle:" + cDXApiClientConfig.isEntitlementpassthrough()
                            + ", passthrough the request since " + " token and session none of them presents");
                    return super.preHandle(request, response, handler);
                }

                LOGGER.debug("EntitlementInterceptor : preHandle: For Passthrough: "
                        + cDXApiClientConfig.isEntitlementpassthrough() + ", Token:" + signedToken + ", Session ID:"
                        + sessionId);

                /*
                 * CMMBSSTA01-1212 : (Tech) Trader user login calls MBSP API
                 * with valid Session ID if Signed Token or Session ID none of
                 * them presents then throw error
                 */
                if (StringUtils.isBlank(signedToken) && StringUtils.isBlank(sessionId)) {
                    // TODO: Change exception format
                    response.reset();
                    response.sendError(HttpStatus.BAD_REQUEST.value(),
                            "Token for Authorization or Session ID is missing!");
                    LOGGER.debug("Exiting preHandle method in EntitlementInterceptor");
                    return false;
                }

                List<String> authorizedRoles = Arrays.asList(entitlementRequired.roles());
                authorizedRoles = authorizedRoles.stream().map(String::toLowerCase).collect(Collectors.toList());

                LOGGER.debug(entitlementRequired.toString() + ", Authorized for Roles:" + authorizedRoles);

                /*
                 * CMMBSSTA01-1212 : (Tech) Trader user login calls MBSP API
                 * with valid Session ID if Signed Token Presents then existing
                 * rule
                 */
                if (!StringUtils.isBlank(signedToken)) {
                    List<String> userRoles = cDXClientApi.getRoles(signedToken);
                    if (userRoles == null || userRoles.isEmpty()) {
                        response.reset();
                        String json;
                        // CMMBSSTA01-882 - Read Only User access Exception -
                        // Start
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
                            LOGGER.error("Exiting preHandle method in EntitlementInterceptor");
                            return false;
                        }
                        ExceptionResponsePO exceptionResponsePO = MBSPortalUtils
                                .getExceptionMessage(exceptionLookupPOLst);
                        json = mapper.writeValueAsString(exceptionResponsePO);
                        response.setContentType("application/json; charset=UTF-8");
                        response.getWriter().print(json);
                        if (exceptionResponsePO.getMessages().get(0).getMessageCode()
                                .equals(MBSExceptionConstants.SYSM_0002)) {
                            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                        } else {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        }
                        LOGGER.debug("Exiting preHandle method in EntitlementInterceptor");
                        // CMMBSSTA01-882 - Read Only User access Exception -
                        // End
                        return false;
                    }
                    userRoles = userRoles.stream().map(String::toLowerCase).collect(Collectors.toList());
                    LOGGER.debug(entitlementRequired.toString() + ", Token Roles:" + userRoles);

                    if (!CollectionUtils.containsAny(authorizedRoles, userRoles)) {
                        response.reset();
                        String json;
                        LOGGER.error("User is not authorized to perform this activity with roles" + userRoles);
                        // CMMBSSTA01-882 - Read Only User access Exception -
                        // Start
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
                            return false;
                        }
                        ExceptionResponsePO exceptionResponsePO = MBSPortalUtils
                                .getExceptionMessage(exceptionLookupPOLst);
                        json = mapper.writeValueAsString(exceptionResponsePO);
                        response.setContentType("application/json; charset=UTF-8");
                        response.getWriter().print(json);
                        if (exceptionResponsePO.getMessages().get(0).getMessageCode()
                                .equals(MBSExceptionConstants.SYSM_0002)) {
                            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                        } else {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        }
                        // CMMBSSTA01-882 - Read Only User access Exception -
                        // End
                        return false;
                    }
                }

                else if (!StringUtils.isBlank(sessionId)) {
                    LOGGER.debug("Request with Session ID with Request URI: {}", request.getRequestURI());
                    // The method will return whether to proceed further or not
                    // - true means proceed further
                    if (!entitlementInterceptorService.performAuthorizationUsingSessionID(sessionId, authorizedRoles,
                            request, response)) {
                        LOGGER.debug("Exiting preHandle method in EntitlementInterceptor");
                        return false;
                    }
                }
                LOGGER.debug("Exiting preHandle method in EntitlementInterceptor");
                return super.preHandle(request, response, handler);
            }
        }
        return super.preHandle(request, response, handler);
    }

}
