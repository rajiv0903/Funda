/*
 * Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 * reserved under the copyright laws of the United States and international
 * conventions. Use of a copyright notice is precautionary only and does not
 * imply publication or disclosure. This software contains confidential
 * information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 * is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.streaming.stomp.client;

import com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap;
import com.fanniemae.mbsportal.service.TokenService;
import com.fanniemae.mbsportal.streaming.stomp.MBSStompUtil;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import java.net.ConnectException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import javax.websocket.DeploymentException;
import org.apache.commons.lang.StringUtils;
import org.apache.geode.cache.query.CqException;
import org.apache.geode.cache.query.CqExistsException;
import org.apache.geode.cache.query.RegionNotFoundException;
import org.slf4j.Logger;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.stomp.ConnectionLostException;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

/**
 * This is stomp client publishing messages directly to ws broker
 *
 * @author g8uaxt Created on 7/20/2018.
 */

public class MBSStompClient extends WebSocketStompClient {
        ListenableFuture<StompSession> connectedSession = null;
        /**
         * LOGGER Logger variable
         */
        @InjectLog
        private Logger LOGGER;
        //private MessageConverter messageConverter;
        private StompSessionHandler stompSessionHandler;
        private String wsUrl;
        private String sessionId;
        private TokenService tokenService;
        
        public MBSStompClient(WebSocketClient webSocketClient) {
                super(webSocketClient);
        }
        
        public MBSStompClient(WebSocketClient webSocketClient, MessageConverter messageConverter,
                StompSessionHandler stompSessionHandler, String wsUrl) {
                super(webSocketClient);
                this.setMessageConverter(messageConverter);
                this.stompSessionHandler = stompSessionHandler;
                this.wsUrl = wsUrl;
        }
        
        /**
         * getWsUrl
         *
         * @return
         */
        public String getWsUrl() {
                return wsUrl;
        }
        
        /**
         * setTokenService for later use
         *
         * @param tokenService
         */
        public void setTokenService(TokenService tokenService) {
                this.tokenService = tokenService;
        }
        
        /**
         * Actually connecting to websocket server here
         *
         * @return
         * @throws ExecutionException
         * @throws InterruptedException
         */
        public ListenableFuture<StompSession> connect(String sessionId)
                throws ExecutionException, InterruptedException {
                LOGGER.info("WS Client connecting to {}....", wsUrl);
                WebSocketHttpHeaders handshakeHeaders = setSecurityHeaderValues(sessionId);
                StompHeaders stompHeaders = setStompSecurityHeaderValues(sessionId, "", "");
                //StompSessionHandler sessionHandler = new MBSStompSessionHandler();
                try {
                        this.connectedSession = this
                                .connect(wsUrl, handshakeHeaders, stompHeaders, this.stompSessionHandler);
                } catch(Throwable throwable) {
                        if(throwable instanceof DeploymentException) {
                                //Socket server not available
                                alertForServerNotAvailable(throwable);
                        } else if(throwable instanceof ConnectException) {
                                LOGGER.error("WS stream server ConnectException. will retry again", throwable);
                                alertForServerNotAvailable(throwable);
                        }else if(throwable instanceof ConnectionLostException) {
                                LOGGER.warn("WS stream server closed connection. will retry again",
                                        throwable);
                                //alertForServerNotAvailable(throwable);
                        } else {
                                LOGGER.info("Unknown case to cover for WS re-connect", throwable);
                        }
                }
                if(this.connectedSession != null && this.connectedSession.get().isConnected()) {
                        //connectedSession.get().setAutoReceipt(true);
                        this.sessionId = sessionId; //save it now for further use
                        LOGGER.info("WS Client connected " + connectedSession);
                } else {
                        LOGGER.info("WS Client connecting issue " + connectedSession);
                }
                return this.connectedSession;
        }
        
        /**
         * alertForServerNotAvailable
         * @param throwable
         */
        private void alertForServerNotAvailable(Throwable throwable) {
                LOGGER.warn("WS stream server not available. will retry again", throwable);
                MBSExceptionConstants.logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER,
                        MBSExceptionConstants.SYSTEM_EXCEPTION.toString(), throwable.getMessage(),
                        "MBSStompSessionHandler:handleExceptions", "",
                        "WS stream server not available. " + "will retry again");
                MBSExceptionConstants.resetLogAlert();
        }
        
        /**
         * Set security header values
         *
         * @return
         */
        private WebSocketHttpHeaders setSecurityHeaderValues(String sessionId) {
                WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
                handshakeHeaders
                        .put(CDXHeaderMap.CHANNEL.getValue(), Arrays.asList(CDXHeaderMap.CHANNEL_VALUE.getValue()));
                handshakeHeaders.put(CDXHeaderMap.SUB_CHANNEL.getValue(),
                        Arrays.asList(CDXHeaderMap.SUB_CHANNEL_VALUE.getValue()));
                handshakeHeaders.put(CDXHeaderMap.SESSION_ID.getValue(), Arrays.asList(sessionId));
                LOGGER.debug("Setting headers {} --> {}, {} -> {}, {} --> {} ", CDXHeaderMap.CHANNEL.getValue(),
                        handshakeHeaders.getFirst(CDXHeaderMap.CHANNEL.getValue()), CDXHeaderMap.SUB_CHANNEL.getValue(),
                        handshakeHeaders.getFirst(CDXHeaderMap.SUB_CHANNEL.getValue()),
                        CDXHeaderMap.SESSION_ID.getValue(), StringUtils
                                .leftPad(handshakeHeaders.getFirst(CDXHeaderMap.SESSION_ID.getValue()),
                                        handshakeHeaders.getFirst(CDXHeaderMap.SESSION_ID.getValue()).length(), 'X'));
                return handshakeHeaders;
        }
        
        /**
         * Set security header values
         * @param transStateType TODO
         *
         * @return
         */
        private StompHeaders setStompSecurityHeaderValues(String sessionId, String messageId, String transStateType) {
                StompHeaders handshakeHeaders = new StompHeaders();
                handshakeHeaders
                        .put(CDXHeaderMap.CHANNEL.getValue(), Arrays.asList(CDXHeaderMap.CHANNEL_VALUE.getValue()));
                handshakeHeaders.put(CDXHeaderMap.SUB_CHANNEL.getValue(),
                        Arrays.asList(CDXHeaderMap.SUB_CHANNEL_VALUE.getValue()));
                handshakeHeaders.put(CDXHeaderMap.SESSION_ID.getValue(), Arrays.asList(sessionId));
                handshakeHeaders.put(CDXHeaderMap.MESSAGE_ID.getValue(), Arrays.asList(messageId));
                handshakeHeaders.put(CDXHeaderMap.TRANS_STATE.getValue(), Arrays.asList(transStateType));
                LOGGER.debug("Setting Stomp headers {} --> {}, {} -> {}, {} --> {} ", CDXHeaderMap.CHANNEL.getValue(),
                        handshakeHeaders.getFirst(CDXHeaderMap.CHANNEL.getValue()), CDXHeaderMap.SUB_CHANNEL.getValue(),
                        handshakeHeaders.getFirst(CDXHeaderMap.SUB_CHANNEL.getValue()),
                        CDXHeaderMap.SESSION_ID.getValue(), StringUtils
                                .leftPad(handshakeHeaders.getFirst(CDXHeaderMap.SESSION_ID.getValue()),
                                        handshakeHeaders.getFirst(CDXHeaderMap.SESSION_ID.getValue()).length(), 'X'));
                return handshakeHeaders;
        }
        
        /**
         * get connected session
         *
         * @return
         */
        public ListenableFuture<StompSession> getConnectedSession() {
                return connectedSession;
        }
        
        /**
         * Initialize connection to websocket server. this is the startup method to be called
         * when application initialized fully
         *
         * @throws MBSBaseException
         * @throws CqException
         * @throws CqExistsException
         * @throws RegionNotFoundException
         * @throws InterruptedException
         * @throws Exception
         */
        public void connectToWsServer(String sessionId)
                throws MBSBaseException, CqException, CqExistsException, RegionNotFoundException, InterruptedException,
                Exception {
                LOGGER.info("mbsStompClient connecting...");
                this.connect(sessionId);
                LOGGER.info("mbsStompClient connected!");
        }
        
        /**
         * reConnectToWsServer  //TODO: later move it to connection manager
         *
         * @param sessionId
         * @return
         * @throws MBSBaseException
         * @throws CqException
         * @throws CqExistsException
         * @throws RegionNotFoundException
         * @throws InterruptedException
         * @throws Exception
         */
        public boolean reConnectToWsServer(String sessionId)
                throws MBSBaseException, CqException, CqExistsException, RegionNotFoundException, InterruptedException,
                Exception {
                LOGGER.info("mbsStompClient re-connecting");
                boolean isSuccessful = false;
                //based on cause, re-connect. as of now, it is assumed that it is due to sessionId
                //TODO: move CDXClientApi to framework service
                if(sessionId == null) {
                        LOGGER.debug("mbsStompClient re-connecting using new session id");
                        sessionId = MBSStompUtil.getValidCdxSession(this.tokenService);
                }
                this.sessionId = sessionId;
                //TODO: try for (few) 3 times ??
                connectToWsServer(sessionId);
                isSuccessful = true;
                LOGGER.info("mbsStompClient re-connected!");
                return isSuccessful;
        }
        
        /**
         * Method to publish directly to ws
         *
         * @param topic
         * @param msg
         * @throws ExecutionException
         * @throws InterruptedException
         */
        public void publish(String topic, Object msg, String messageId, String transStateType)
                throws ExecutionException, InterruptedException, MBSBaseException {
                LOGGER.info("WS publishing to topic: {} and payload {} and session {}", topic, msg.toString(),
                        connectedSession);
                if(connectedSession == null || !connectedSession.get().isConnected()) {
                        LOGGER.warn(
                                "WS publishing ERROR. wsConnection in-valid; connectedSession: {} and status " + "{} ",
                                connectedSession, connectedSession.get().isConnected());
                        //TODO: should we retry?
                        //String sessionId = tokenService.getValidSessionId();
                        //LOGGER.info("Got session id for connecting ws {} and is not null",!isNull(sessionId));
                        //throw new MBSBaseException("WS Session is null/invalid " + connectedSession);
                } else {
                        //final StompSession.Receiptable send = connectedSession.get().send(topic, msg.toString()
                        // .getBytes());
                        StompHeaders stompHeaders = setStompSecurityHeaderValues(this.sessionId, messageId, transStateType);
                        stompHeaders.setDestination(topic);
                        final StompSession.Receiptable send = connectedSession.get()
                                .send(stompHeaders, msg.toString().getBytes());
                        //TODO: handle the ack and exceptions
                        LOGGER.debug("WS receipt {} from {}", send, topic); //send.getReceiptId()
                }
        }
        
        @Override
        public void start() {
                super.start();
                LOGGER.info("Stomp Client Started");
        }
        
        @Override
        public void stop() {
                super.stop();
                LOGGER.info("Stomp Client Stopped");
        }
        
        @Override
        public boolean isRunning() {
                return super.isRunning();
        }
        
        @Override
        public boolean isDefaultHeartbeatEnabled() {
                return super.isDefaultHeartbeatEnabled();
        }
        
        @Override
        public void setDefaultHeartbeat(long[] heartbeat) {
                super.setDefaultHeartbeat(heartbeat);
        }
        
        @Override
        public void setReceiptTimeLimit(long receiptTimeLimit) {
                super.setReceiptTimeLimit(receiptTimeLimit);
        }
        
}
