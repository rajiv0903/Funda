/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.controller.interceptor.header;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fanniemae.mbsportal.cdx.config.CDXApiClientConfig;
import com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap;

/**
 *
 * @author Rajiv Chaudhuri
 * @Date Nov 3, 2017
 * @Time: 3:39:06 PM
 * @Description: com.fanniemae.mbsportal.api.controller.interceptor.header
 *               HeaderInterceptor.java
 * @Purpose: To validate whether mandatory header is presence at API Client
 *           Request
 *
 */
@Component
public class RequestHeaderInterceptor extends HandlerInterceptorAdapter {
    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHeaderInterceptor.class);

    /**
     *
     * cDXApiClientConfig CDXApiClientConfig
     */
    @Autowired
    CDXApiClientConfig cDXApiClientConfig;

    /**
     *
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
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        LOGGER.debug("Entering preHandle method in RequestHeaderInterceptor");
        if (handler instanceof HandlerMethod) {

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // RequestHeaderRequired requestHeaderRequired =
            // handlerMethod.getMethod().getAnnotation(RequestHeaderRequired.class);
            RequestHeaderRequired requestHeaderRequired = handlerMethod
                    .getMethodAnnotation(RequestHeaderRequired.class);

            if (requestHeaderRequired != null) {

                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String headerName = headerNames.nextElement();
                        LOGGER.debug("Key:" + headerName);
                        LOGGER.debug("Value:" + request.getHeader(headerName));
                    }
                }

                /*
                 * If entitlement Pass through is enabled and no token presence
                 * then by pass the check
                 */
                if (cDXApiClientConfig.isEntitlementpassthrough()) {
                    LOGGER.debug("Exiting performAuthorizationUsingSessionID method in EntitlementInterceptorService");
                    LOGGER.debug(
                            "RequestHeaderInterceptor : preHandle:" + cDXApiClientConfig.isEntitlementpassthrough());
                    return super.preHandle(request, response, handler);
                }

                boolean headerPresent = true;

                /*
                 * CMMBSSTA01-1212 : (Tech) Trader user login calls MBSP API
                 * with valid Session ID Either Token or Session ID has to be
                 * there
                 */
                if (StringUtils.isBlank(request.getHeader(CDXHeaderMap.JWS_TOKEN.getValue()))
                        && StringUtils.isBlank(request.getHeader(CDXHeaderMap.SESSION_ID.getValue()))) {
                    headerPresent = false;
                }
                // if CDX header is missing
                if (!headerPresent) {

                    response.reset();
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "Mandatory Header is missing!");
                    LOGGER.debug("Exiting preHandle method in RequestHeaderInterceptor ");
                    return false;

                } else {
                    LOGGER.debug("Exiting preHandle method in RequestHeaderInterceptor");
                    return super.preHandle(request, response, handler);
                }
            }
        }
        LOGGER.debug("Exiting preHandle method in RequestHeaderInterceptor");
        return super.preHandle(request, response, handler);
    }

}
