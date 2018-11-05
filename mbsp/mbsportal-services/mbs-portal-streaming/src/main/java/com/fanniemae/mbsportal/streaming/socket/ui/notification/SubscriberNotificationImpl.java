/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.streaming.socket.ui.notification;

import java.util.LinkedHashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 27, 2018
 * @File: com.fanniemae.mbsportal.streaming.socket.ui.notification.SubscriberNotificationImpl.java 
 * @Revision: 
 * @Description: SubscriberNotificationImpl.java
 */

@Component
public class SubscriberNotificationImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriberNotificationImpl.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    LinkedHashSet<SubscriberInfo> subscribers = new LinkedHashSet<>();

    public void publishAll() throws Exception {
        for (SubscriberInfo subscriberInfo : subscribers) {
            try {
                this.simpMessagingTemplate.convertAndSend(subscriberInfo.getDestination(), subscriberInfo.getSubscriptionMessage().getContent());
            } catch (MessagingException exe) {
                LOGGER.error("Failed to publish message at {}", subscriberInfo.getDestination());
            }
        }
    }

    public void publish(SubscriberInfo subscriberInfo) throws Exception {
        try {
            this.simpMessagingTemplate.convertAndSend(subscriberInfo.getDestination(), subscriberInfo.getSubscriptionMessage().getContent());
        } catch (MessagingException exe) {
            LOGGER.error("Failed to publish message at {}", subscriberInfo.getDestination());
        }
    }

    public LinkedHashSet<SubscriberInfo> getSubscribers() {
        if (this.subscribers == null)
            this.subscribers = new LinkedHashSet<SubscriberInfo>();
        return this.subscribers;
    }

    public void setSubscribers(LinkedHashSet<SubscriberInfo> subscribers) {
        this.subscribers = subscribers;
    }

    public boolean removeRegisteredDestinations(String destination) {
        return this.subscribers.remove(destination);
    }
}
