/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.streaming.socket.ui.notification;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 27, 2018
 * @File: com.fanniemae.mbsportal.streaming.socket.ui.notification.SubscriptionMessage.java 
 * @Revision: 
 * @Description: SubscriptionMessage.java
 */
public abstract class SubscriptionMessage {

    protected String destination;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public abstract String getContent();

    public abstract void setContent(String content);

}
