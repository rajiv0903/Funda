/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.streaming.socket.decorator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

import com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 25, 2018
 * @File: com.fanniemae.mbsportal.streaming.socket.decorator.WebSocketCustomHandlerDecorator.java 
 * @Revision: 
 * @Description: WebSocketCustomHandlerDecorator.java
 */

public class WebSocketCustomHandlerDecorator extends WebSocketHandlerDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketCustomHandlerDecorator.class);

    /**
     * @param delegate
     */
    public WebSocketCustomHandlerDecorator(WebSocketHandler delegate) {
        super(delegate);        
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        
        String cdxSessionID = session.getHandshakeHeaders().getFirst(CDXHeaderMap.SESSION_ID.getValue());
        String wsSessionId = session.getId();
        LOGGER.info("After Connection Has Been Established for Client IP: {}, CDX Padded ID: {}, WS Padded ID: {}", session.getRemoteAddress(), 
                MBSPortalUtils.getLeftPaddedString(cdxSessionID), 
                MBSPortalUtils.getLeftPaddedString(wsSessionId));
        super.afterConnectionEstablished(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        String wsSessionId = session.getId();
        LOGGER.debug("After Message Has Been Handled for WS Session: {}", wsSessionId);
        //To prevent connection time out - Broken Pipe Issue
        if(session.isOpen()){
            super.getDelegate().handleMessage(session, message);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        
        LOGGER.error("handleTransportError: Exception: {}", exception);
        super.getDelegate().handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

        String cdxSessionID = session.getHandshakeHeaders().getFirst(CDXHeaderMap.SESSION_ID.getValue());
        String wsSessionId = session.getId();
        LOGGER.info("After Connection Has Been Closed for Client IP: {}, CDX Session ID: {}, WS Session ID: {}", session.getRemoteAddress(), 
                MBSPortalUtils.getLeftPaddedString(cdxSessionID), 
                wsSessionId);
        
        super.getDelegate().afterConnectionClosed(session, closeStatus);

    }

}
