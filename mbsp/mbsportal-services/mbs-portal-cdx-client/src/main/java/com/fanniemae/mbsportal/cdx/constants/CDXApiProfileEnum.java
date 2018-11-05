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
 * Enum class for the profile entries
 * @author g8upjv
 * Date - Dec 28 2017
 * Added comments
 *
 */
public enum CDXApiProfileEnum {

    FIRSTNAME("FIRSTNAME"),
    LASTNAME("LASTNAME"),
    USER_NAME("USER_NAME"),
    EMAIL("EMAIL"),
    CUSTOMER_NAME("CUSTOMER_NAME"),
    WORK_NUMBER("WORK_NUMBER"),
    MOBILE_NUMBER("MOBILE_NUMBER"),
    
    DEFAULT_SELLER_SERVICE_NUMBER("DEFAULT_SELLER_SERVICE_NUMBER"),
    SELLER_SERVICE_NUMBER("SELLER_SERVICE_NUMBER"),
    
    DEALER_ORG_NAME("DEALER_ORG_NAME"),
    DEALER_ORG_ID("DEALER_ORG_ID"),
    
    INSTITUTION_ID("INSTITUTION_ID"),
    FANNIEMAE_USER("FANNIEMAE_USER"),
    
    ROLES("ROLES"),
    
    SELLER_SERVICE_DETAILS("SELLER_SERVICE_DETAILS"),
    LENDER_DETAILS("LENDER_DETAILS");
    
    /**
     * 
     * value the value of the enum
     */
    private final String value;

    /**
     * 
     * Constructor
     * 
     * @param value the value being passed to set it
     */
    private CDXApiProfileEnum(String value) {
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
