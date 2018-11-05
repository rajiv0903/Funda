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

package com.fanniemae.mbsportal.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fanniemae.all.messaging.api.ESBClient;
import com.fanniemae.all.messaging.api.ESBClientException;
import com.fanniemae.all.messaging.api.ESBMessageProcessor;
import com.fanniemae.all.messaging.message.ESBMessage;
import com.fanniemae.mbsportal.constants.MBSAdapterConstants;
import com.fanniemae.mbsportal.service.TradeMessageProcessor;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;

/**
 * @author g8upjv
 *
 *         ESBMessage Listener class to listen and store messages in gemfire
 *         coming on ESB Queue
 *
 */
@Component
public class ESBMessageListener implements ESBMessageProcessor, MBSAdapterConstants {

    private static final Logger LOGGER = LoggerFactory.getLogger(ESBMessageListener.class);

    @Autowired
    private ESBClient esbClient;

    @Autowired
    private TradeMessageProcessor tradeMessageProcessor;

    /**
     * Method to start the consumer
     *
     * @throws MBSBaseException
     */
    public void startESBClientAsyncConsumer() throws MBSBaseException {
        LOGGER.debug("Starting ESB Client  APP CD {}", System.getProperty("APP_CD"));
        LOGGER.debug("System Properties: ENV_CD {}", System.getProperty("ENV_CD"));
        try {
            esbClient.startMessageConsumer(this);
            LOGGER.debug("ESBMessageListner Registered and Started.");
        } catch (Exception ex) {
            LOGGER.error("Runtime exception in MBS ESB Listener listening to BlackRock messages ", ex);
        }
    }

    /**
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
            tradeMessageProcessor.prepareAndProcess(payLoad);
            handleMsgReturn = true;
        }
        catch (Throwable t) {
            MBSExceptionConstants.logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER, "", "", "processMessage",
                    "", "");

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
