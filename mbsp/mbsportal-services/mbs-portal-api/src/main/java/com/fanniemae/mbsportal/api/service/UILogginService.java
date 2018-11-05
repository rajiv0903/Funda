/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.service;

import java.util.List;

import org.slf4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.api.po.UILoggingMessagePO;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 3, 2018
 * @Time 4:28:32 PM com.fanniemae.mbsportal.api.service UILogginService.java
 * @Description: CMMBSSTA01-941 : (Tech) Logging API to capture UI logs
 */
@SuppressWarnings("rawtypes")
@Service
public class UILogginService extends BaseProcessor {
    
    /**
     * 
     * LOGGER Logger variable
     */
    @InjectLog
    private Logger LOGGER;
    
    /**
     * 
     * LOGGER_UI Logger variable
     */
    private static final Logger LOGGER_UI = LoggerFactory.getLogger("c.f.m.c.UILoggingController");

    /**
     * 
     * @param uiLoggingMessagePOs List<UILoggingMessagePO>
     * @throws MBSBaseException
     * @Description: Method which takes care of setting MDC variable to log at
     *               UI logger for Audit/Troubleshoot
     */
    public void logUIMsg(List<UILoggingMessagePO> uiLoggingMessagePOs) throws MBSBaseException {

        LOGGER.debug("Entering logUIMsg method in UILogginService");

        for (UILoggingMessagePO uiLoggingMessagePO : uiLoggingMessagePOs)
        {
            MDC.put(MBSExceptionConstants.UI_LOGGING_USER_ID, uiLoggingMessagePO.getUserName());
            MDC.put(MBSExceptionConstants.UI_LOGGING_HTTP_CODE, uiLoggingMessagePO.getCode());
            MDC.put(MBSExceptionConstants.UI_LOGGING_CHANNEL, uiLoggingMessagePO.getChannel());
            MDC.put(MBSExceptionConstants.UI_LOGGING_TRANSACTION_ID, uiLoggingMessagePO.getTransReqId());
            MDC.put(MBSExceptionConstants.UI_LOGGING_MSG_TYPE, uiLoggingMessagePO.getType());
            MDC.put(MBSExceptionConstants.UI_LOGGING_MODULE, uiLoggingMessagePO.getModule());
            MDC.put(MBSExceptionConstants.UI_LOGGING_PAGE, uiLoggingMessagePO.getPage());
            MDC.put(MBSExceptionConstants.UI_LOGGING_FUNCTIONALITY, uiLoggingMessagePO.getFunctionality());
            MDC.put(MBSExceptionConstants.UI_LOGGING_TIME_STAMP, uiLoggingMessagePO.getTimeStamp());
            MDC.put(MBSExceptionConstants.UI_LOGGING_MSG, uiLoggingMessagePO.getMessage());
    
            LOGGER_UI.info(uiLoggingMessagePO.getMessageDescription());
        }

        LOGGER.debug("Exiting logUIMsg method in UILogginService");
    }

}
