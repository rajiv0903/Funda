/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.api.po;

import java.io.Serializable;

/**
 * 
 * @author g8upjv
 *
 */
@SuppressWarnings("serial")
public class PollingPO implements Serializable {
    
    /**
     * 
     * eventsAvailable boolean
     */
    private boolean eventsAvailable;
    /**
     * 
     * serverTime String
     */
    private String serverTime;
    
    /**
     * 
     * @return boolean
     */
    public boolean isEventsAvailable() {
        return eventsAvailable;
    }
    
    /**
     * 
     * @param eventsAvailable the eventsAvailable
     */
    public void setEventsAvailable(boolean eventsAvailable) {
        this.eventsAvailable = eventsAvailable;
    }
    
    /**
     * 
     * @return String
     */
    public String getServerTime() {
        return serverTime;
    }
    
    /**
     * 
     * @param serverTime the serverTime
     */
    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }
    
    /**
     * 
     * @return String
     */
    @Override
    public String toString() {
        return "PollingPO [eventsAvailable=" + eventsAvailable + ", serverTime=" + serverTime + "]";
    }
    
    

}
