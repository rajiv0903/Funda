/*
 * Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 * reserved under the copyright laws of the United States and international
 * conventions. Use of a copyright notice is precautionary only and does not
 * imply publication or disclosure. This software contains confidential
 * information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 * is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.streaming.stomp.client;

import java.util.List;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

/**
 * Sock Js Client wrapper
 * @author g8uaxt Created on 7/20/2018.
 */

public class MBSWebSocketJsClient extends SockJsClient {
        
        public MBSWebSocketJsClient(List<Transport> transports) {
                super(transports);
                this.setMessageCodec(new Jackson2SockJsMessageCodec());
        }
 }
