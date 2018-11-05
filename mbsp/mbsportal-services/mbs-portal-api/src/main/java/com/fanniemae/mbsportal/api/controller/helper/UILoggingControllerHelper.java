/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.api.controller.helper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.UILoggingMessagePO;
import com.fanniemae.mbsportal.api.service.UILogginService;
import com.fanniemae.mbsportal.aspect.exception.ExceptionTracingAnnotation;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 *
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 3, 2018
 * @Time 3:54:09 PM com.fanniemae.mbsportal.api.controller.helper
 *       UILoggingControllerHelper.java
 * @Description: CMMBSSTA01-941 : (Tech) Logging API to capture UI logs
 */
@Component
public class UILoggingControllerHelper extends BaseControllerHelper {

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UILoggingControllerHelper.class);

    /**
     *
     * uiLogginService UILogginService
     */
    @Autowired
    private UILogginService uiLogginService;

    /**
     *
     * Select the version based on the media type
     *
     * @param mediaType
     *            the mediaType
     * @return BaseProcessor
     */
    @SuppressWarnings("rawtypes")
    private BaseProcessor selectDataServiceByMediaType(String mediaType) {
        LOGGER.debug("Entering selectDataServiceByMediaType method in UILoggingControllerHelper " + mediaType);
        if (mediaType.equals(MediaType.APPLICATION_JSON_VALUE) || mediaType.equals(getVersion1json().toString())) {
            return uiLogginService;
        } else {
            return null;
        }
    }

    /**
     *
     *
     * @param uiLoggingMessagePOs
     *            the List of uiLoggingMessagePO
     * @throws MBSBaseException
     * @Description: Method to log service to log UI message
     *
     */
    @ExceptionTracingAnnotation
    public void logUIMsg(List<UILoggingMessagePO> uiLoggingMessagePOs) throws MBSBaseException {
        LOGGER.debug("Entering logUIMsg method in UILoggingControllerHelper");
        String mediaType = MediaType.APPLICATION_JSON_VALUE;
        UILogginService uiLogginService = (UILogginService) selectDataServiceByMediaType(mediaType.trim());
        uiLogginService.logUIMsg(uiLoggingMessagePOs);
        LOGGER.debug("Exiting logUIMsg method in UILoggingControllerHelper");

    }
}
