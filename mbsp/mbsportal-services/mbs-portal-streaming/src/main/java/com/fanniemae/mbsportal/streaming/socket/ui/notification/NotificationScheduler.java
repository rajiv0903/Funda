/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.streaming.socket.ui.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 27, 2018
 * @File: com.fanniemae.mbsportal.streaming.socket.ui.notification.NotificationScheduler.java 
 * @Revision: 
 * @Description: NotificationScheduler.java
 */
@Configuration
@EnableScheduling
public class NotificationScheduler {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationScheduler.class);

    @Autowired
    private SubscriberNotificationImpl subscriberNotificationImpl;

    @Scheduled(fixedDelay = 10000)
    public void publish() throws Exception {

        subscriberNotificationImpl.publishAll();
    }
}
