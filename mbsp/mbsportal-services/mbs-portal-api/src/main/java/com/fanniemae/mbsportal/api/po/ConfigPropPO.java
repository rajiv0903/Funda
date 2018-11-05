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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 6, 2018
 * @File: com.fanniemae.mbsportal.api.po.ConfigPropPO.java
 * @Revision:
 * @Description: ConfigPropPO.java
 */
@JsonInclude(Include.NON_NULL)
public class ConfigPropPO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -553851830288979919L;
    /**
     * 
     * key String
     */
    private String key;
    /**
     * 
     * value String
     */
    private String value;
    /**
     * 
     * parent String
     */
    private String parent;
    
    /**
     * 
     * dataType String
     */
    private String dataType;

    /**
     * 
     * @return String
     */
    public String getKey() {
        return key;
    }

    /**
     * 
     * @param key
     *            the key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 
     * @return String
     */
    public String getValue() {
        return value;
    }

    /**
     * 
     * @param key
     *            the key
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 
     * @return String
     */
    public String getParent() {
        return parent;
    }

    /**
     * 
     * @param key
     *            the key
     */
    public void setParent(String parent) {
        this.parent = parent;
    }
    
    
    /**
     * 
     * @return String
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * 
     * @param dataType
     *            the dataType
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return "ConfigPropPO [key=" + key + ", value=" + value + ", parent=" + parent + ", dataType=" + dataType + "]";
    }

    
    
}
