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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;

/**
 *
 * @author Rajiv Chaudhuri
 * @Date: Mar 22, 2018
 * @File: com.fanniemae.mbsportal.api.controller.interceptor.header.
 *        ResponseHeaderInterceptor.java
 * @Revision :
 * @Description: ResponseHeaderInterceptor.java
 */
@Component
public class ResponseHeaderInterceptor extends HandlerInterceptorAdapter {

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseHeaderInterceptor.class);

    /**
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

        LOGGER.debug("Entering preHandle method in ResponseHeaderInterceptor");
        if (handler instanceof HandlerMethod) {

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ResponseHeaderRequired responseHeaderRequired = handlerMethod
                    .getMethodAnnotation(ResponseHeaderRequired.class);

            if (responseHeaderRequired != null) {
                String serverTime = MBSPortalUtils.convertDateToString(MBSPortalUtils.getCurrentDate(),
                        DateFormats.DATE_FORMAT_WITH_TIMESTAMP);
                LOGGER.debug("ResponseHeaderInterceptor : preHandle: serverTime: {}", serverTime);
                response.setHeader(MBSPServiceConstants.RESPONSE_HEADER_SERVER_TIME_NAME, serverTime);
                response.setHeader(MBSPServiceConstants.RESPONSE_ACCESS_CONTROL_EXPOSE_HEADER_NAME,
                        MBSPServiceConstants.RESPONSE_HEADER_SERVER_TIME_NAME);
            }
        }
        LOGGER.debug("Exiting preHandle method in ResponseHeaderInterceptor");
        return super.preHandle(request, response, handler);
    }
}
