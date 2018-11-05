/*
 * Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 * reserved under the copyright laws of the United States and international
 * conventions. Use of a copyright notice is precautionary only and does not
 * imply publication or disclosure. This software contains confidential
 * information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 * is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.streaming.stomp.client;

import java.util.concurrent.ExecutionException;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import com.fanniemae.mbsportal.streaming.stomp.session.MBSStompSessionHandler;

/**
 * @author g8uaxt Created on 7/20/2018.
 */

public class MBSWebSocketClientUtil {
        
        public void subscribe(StompSession stompSession, String topic, MBSStompSessionHandler mbsStompSessionHandler)
                throws ExecutionException, InterruptedException {
                stompSession.subscribe(topic, mbsStompSessionHandler);
        }
        
        public void subscribe(StompSession stompSession, StompHeaders headers,
                MBSStompSessionHandler mbsStompSessionHandler) throws ExecutionException, InterruptedException {
                //stompSession.subscribe(topic,mbsStompSessionHandler );
        }
}
