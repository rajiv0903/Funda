/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.cdx.constants;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Mar 22, 2018
 * @File: com.fanniemae.mbsportal.api.cdx.CDXHeaderMap.java
 * @Revision : 
 * @Description: CDXHeaderMap.java
 */
public enum CDXHeaderMap {

    ACCEPT("Accept"),
    CHANNEL("x-fnma-channel"),
    SUB_CHANNEL("x-fnma-sub-channel"),
    SESSION_ID("x-fnma-sessionid"),
    JWS_TOKEN("x-fnma-jws-token"),

    CHANNEL_VALUE("web"),
    SUB_CHANNEL_VALUE("MBSP"),
    
    MESSAGE_ID("message-id"),
    TRANS_STATE("trans-state");
    
    /**
     * 
     * value Value
     */
    private final String value;

    /**
     * 
     * Constructor
     * 
     * @param value Value to be set in constructor
     */
    private CDXHeaderMap(String value) {
        this.value = value;
    }

    /**
     * 
     * getter method
     * 
     * @return String
     */
    public String getValue() {
        return value;
    }

}
