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

package com.fanniemae.mbsportal.pu.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fanniemae.all.messaging.api.ESBClient;
import com.fanniemae.all.messaging.api.ESBClientException;
import com.fanniemae.all.messaging.api.ESBMessageProcessor;
import com.fanniemae.all.messaging.message.ESBMessage;
import com.fanniemae.mbsportal.pu.service.PUPriceMessageProcessor;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;

/**
 * @author g8upjv
 *
 *         PUEsbMessageListener Listener class to listen and store messages in gemfire
 *         coming on ESB Queue for PU pricing
 *
 */
@Component
public class PUEsbMessageListener implements ESBMessageProcessor{
    
    /**
     * 
     * LOGGER Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PUEsbMessageListener.class);
    
    /**
     * 
     * esbClient ESBClient
     */
    @Autowired
    private ESBClient esbClient;
    
    /**
     * 
     * pUPriceMessageProcessor PUPriceMessageProcessor
     */
    @Autowired
    private PUPriceMessageProcessor pUPriceMessageProcessor;

    /**
     * 
     * Method to start the consumer
     *
     * @throws MBSBaseException
     */
    public void startESBClientAsyncConsumer() throws MBSBaseException {
        LOGGER.info("Starting ESB Client  APP CD {}", System.getProperty("APP_CD"));
        LOGGER.debug("System Properties: ENV_CD {}", System.getProperty("ENV_CD"));
        try {
            esbClient.startMessageConsumer(this);
            LOGGER.info("PUEsbMessageListener Registered and Started.");
        } catch (Exception ex) {
            LOGGER.error("Runtime exception in PU ESB Listener listening to Pricing messages ", ex);
        }
    }

    /**
     * 
     * This method process the message
     *
     * @return handleMsgReturn
     * @param esbMessage
     * @see com.fanniemae.all.messaging.api.ESBMessageProcessor#
     *      processMessage(com.fanniemae.all.messaging .message.ESBMessage)
     */
    @Override
    public boolean processMessage(ESBMessage esbMessage) {
        boolean handleMsgReturn = false;
        String messageId = null;
        try {
            messageId = esbMessage.getMessageId();
            LOGGER.debug("processMessage method: ESB message reecived with messaeg id: {} and Message Content " + "{}",
                    messageId, esbMessage);
            String payLoad = esbMessage.getPayload();
            LOGGER.debug("processMessage method: ESB message received with payload {} ", payLoad);
            pUPriceMessageProcessor.prepareAndProcess(esbMessage);
            handleMsgReturn = true;
        }
        catch (Throwable t) {
            LOGGER.error("Exception Occured while processing the message. message ID: {}", t, messageId);
            try {
                esbClient.suspendMessage(esbMessage);
                handleMsgReturn = true;
            } catch (ESBClientException e) {
                MBSExceptionConstants.logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER, "", "",
                        "processMessage", "", "");

                LOGGER.error("Exception Occured while moving the message to suspend queue: {}", e, messageId);
            }
        }
        LOGGER.debug("ESBMessagelistener processMessage exit " + esbMessage.getCorrelationId());
        return handleMsgReturn;
    }
}
