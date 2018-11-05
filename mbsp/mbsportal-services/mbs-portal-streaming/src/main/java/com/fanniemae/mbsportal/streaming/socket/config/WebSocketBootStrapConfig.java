/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.streaming.socket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 25, 2018
 * @File: com.fanniemae.mbsportal.streaming.socket.config.WebSocketBootStrapConfig.java 
 * @Revision: 
 * @Description: WebSocketBootStrapConfig.java
 */
@Component
public class WebSocketBootStrapConfig {
    
    @Autowired
    private WebSocketMessageBrokerStats webSocketMessageBrokerStats;
    
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        webSocketMessageBrokerStats.setLoggingPeriod(10 * 60 * 1000); // desired time in millis
    }
}
