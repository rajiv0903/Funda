/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.streaming.socket.ui.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fanniemae.mbsportal.streaming.socket.ui.notification.SubscriberInfo;
import com.fanniemae.mbsportal.streaming.socket.ui.notification.SubscriberNotificationImpl;
import com.fanniemae.mbsportal.streaming.socket.ui.notification.SubscriptionMessage;
import com.fanniemae.mbsportal.streaming.socket.ui.notification.SubscriptionMessageImpl;


/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 27, 2018
 * @File: com.fanniemae.mbsportal.streaming.socket.ui.controller.SubscriberController.java 
 * @Revision: 
 * @Description: SubscriberController.java
 */

@RestController
public class SubscriberController  {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriberController.class);

    @Autowired
    private SubscriberNotificationImpl subscriberNotificationImpl;
    
    private static final String DESTINATION_PREFIX= "/mbsp/topic/";

    @RequestMapping(value = "/mbsp-streaming/register/{topic}/{username}", method = RequestMethod.POST, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    public ResponseEntity<String> registerUser(@PathVariable("topic") String topic, @PathVariable("username") String username) {

        String topicDestination = DESTINATION_PREFIX +topic +"/"+ username;
        LOGGER.debug("Username : {}", username);
        
        SubscriberInfo subscriberInfo = new SubscriberInfo();
        subscriberInfo.setDestination(topicDestination);
        SubscriptionMessage subscriptionMessage = new SubscriptionMessageImpl();
        subscriptionMessage.setDestination(topicDestination);
        subscriptionMessage.setContent("{ Hello, " + username + "!}");
        subscriberInfo.setSubscriptionMessage(subscriptionMessage);

        subscriberNotificationImpl.getSubscribers().add(subscriberInfo);
        return ResponseEntity.ok("SUCCESS");
    }

    @RequestMapping(value = "/mbsp-streaming/unregister/{topic}/{username}", method = RequestMethod.POST, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    public ResponseEntity<String> unRegisterUser(@PathVariable("topic") String topic, @PathVariable("username") String username) {
        
        String topicDestination = DESTINATION_PREFIX  +topic +"/"+ username;
        LOGGER.debug("Username : {}", username);
        
        SubscriberInfo subscriberInfo = new SubscriberInfo();
        subscriberInfo.setDestination(topicDestination);

        subscriberNotificationImpl.getSubscribers().remove(subscriberInfo);

        return ResponseEntity.ok("SUCCESS");
    }
}
