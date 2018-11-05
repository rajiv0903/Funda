/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 *
 */

package com.fanniemae.mbsportal.pu.constants;

/**
 * 
 * 
 * @author g8uaxt Created on 6/11/2018.
 */

/**
 * Enum to handle supported event types
 * @author g8uaxt
 *
 */
public enum MessageEventType {
        TBA("TBA");
        
    /**
     * 
     * eventType String
     */
     private String eventType;
     
    /**
     *  
     * @param eventType
     */
    MessageEventType(String eventType) {
            this.eventType=eventType;
    }
        
    /**
     *
     * getter method
     *
     * @return String
     */
    public String getEventType() {
            return this.eventType;
    }
    
    /**
     * create enum based on status
     *
     * @param type
     * @return
     */
    public static MessageEventType getEnum(String type) {
                
                for(MessageEventType eventType : MessageEventType.values()) {
                        if(eventType.toString().equals(type)) {
                                return eventType;
                        }
                }
                return null;
        }
}
