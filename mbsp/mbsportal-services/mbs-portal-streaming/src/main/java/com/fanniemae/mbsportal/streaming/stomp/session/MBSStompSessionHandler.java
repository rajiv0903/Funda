/*
 * Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 * reserved under the copyright laws of the United States and international
 * conventions. Use of a copyright notice is precautionary only and does not
 * imply publication or disclosure. This software contains confidential
 * information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 * is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.streaming.stomp.session;

import com.fanniemae.mbsportal.streaming.stomp.MBSStompUtil;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import java.lang.reflect.Type;
import java.net.ConnectException;
import javax.websocket.DeploymentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.ConnectionLostException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

/**
 * @author g8uaxt Created on 7/20/2018.
 */

public class MBSStompSessionHandler implements StompSessionHandler {
        /**
         * LOGGER Logger variable
         */
        private static final Logger LOGGER = LoggerFactory.getLogger(MBSStompSessionHandler.class);
        
        @Override
        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
                LOGGER.info("WS client connected handler");
        }
        
        @Override
        public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders,
                byte[] bytes, Throwable throwable) {
                LOGGER.info("WS client handleException {}", throwable);
        }
        
        @Override
        public void handleTransportError(StompSession stompSession, Throwable throwable) {
                LOGGER.info("WS client handleTransportError {}", throwable);
                //all runtime exceptions
                //javax.websocket.DeploymentException: The HTTP request to initiate the WebSocket connection failed
                handleExceptions(throwable, stompSession);
        }
        
        /**
         * handleExceptions
         *
         * @param throwable
         * @param stompSession
         */
        public void handleExceptions(Throwable throwable, StompSession stompSession) {
                
                if(throwable instanceof DeploymentException) {
                        //Socket server not available
                        LOGGER.warn("WS stream server not available. will retry again", throwable);
                        MBSExceptionConstants.logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER,
                                MBSExceptionConstants.SYSTEM_EXCEPTION.toString(), throwable.getMessage(),
                                "MBSStompSessionHandler:handleExceptions", "",
                                "WS stream server not available. will retry again");
                        MBSExceptionConstants.resetLogAlert();
                } else if(throwable instanceof ConnectException) {
                        LOGGER.error("WS stream server ConnectException. will retry again", throwable);
                } else if(throwable instanceof ConnectionLostException) {
                        LOGGER.warn("WS stream server closed connection. may be bad session. will re-connect by job",
                                throwable);
                        //TODO: retry with session id? will revisit here later
                        LOGGER.info("Retry connecting here.:");
                        try {
                                //TODO: use keys later for multiple clients. Retry once otherwise, will be
                                // aggressive re-connect
                                MBSStompUtil.getClient("default").reConnectToWsServer(null);
                                LOGGER.info("handleExceptions: connected back again");
                        } catch(Exception e) {
                                //fail over case
                                LOGGER.error("handleExceptions: Need to handle in reconnect?", e);
                        }
                } else {
                        LOGGER.warn("Unknown case to cover for WS re-connect");
                }
        }
        
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
                return null;
        }
        
        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
                //TODO: handle frame types like ACK/ERROR etc.
                System.out.println("handleFrame LOGGER " + LOGGER);
                if(o != null) {
                        LOGGER.debug("WS Received Frame: " + new String((byte[]) o));
                }
                
        }
        
}
