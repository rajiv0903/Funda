/*
 * Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 * reserved under the copyright laws of the United States and international
 * conventions. Use of a copyright notice is precautionary only and does not
 * imply publication or disclosure. This software contains confidential
 * information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 * is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.streaming.stomp.client;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

/**
 * @author g8uaxt Created on 7/20/2018.
 */

public class MBSWebSocketClient extends StandardWebSocketClient { // StandardWebSocketClient
        
        //StandardWebSocketClient
        // No implementation yet. it just give flexibility to switch to SockJs client later
        Map<String, Object> userProperties = null;
        
        public MBSWebSocketClient() {
                userProperties = new HashMap<>();
              /*userProperties.put("connection","Upgrade");
              userProperties.put("upgrade","websocket");*/
                userProperties.put("myval", "test");
                
        }
        
        @Override
        public Map<String, Object> getUserProperties() {
                return this.userProperties;
        }
        
        @Override
        protected ListenableFuture<WebSocketSession> doHandshakeInternal(WebSocketHandler webSocketHandler,
                HttpHeaders headers, final URI uri, List<String> protocols, List<WebSocketExtension> extensions,
                Map<String, Object> attributes) {
                if(headers != null) {
                        headers.put("connection", Arrays.asList("Upgrade"));
                        headers.put("upgrade", Arrays.asList("websocket"));
                }
                return super.doHandshakeInternal(webSocketHandler, headers, uri, protocols, extensions, attributes);
        }
}