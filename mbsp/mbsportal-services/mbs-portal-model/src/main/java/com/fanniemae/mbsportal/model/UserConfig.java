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

package com.fanniemae.mbsportal.model;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 25, 2018
 * @File: com.fanniemae.mbsportal.model.UserConfig.java 
 * @Revision: 
 * @Description: UserConfig.java
 */
public class UserConfig {

    /**
     * 
     * directApiUrl String
     */
    private String directApiUrl;

    /**
     * 
     * webSocketUrl String
     */
    private String webSocketUrl;

    public UserConfig() {
    }

    /**
     * 
     * @param directApiUrl
     * @param webSocketUrl
     */
    public UserConfig(String directApiUrl, String webSocketUrl) {
        super();
        this.directApiUrl = directApiUrl;
        this.webSocketUrl = webSocketUrl;
    }

    /**
     * 
     * @return directApiUrl
     */
    public String getDirectApiUrl() {
        return directApiUrl;
    }

    /**
     * 
     * @param directApiUrl directApiUrl
     */
    public void setDirectApiUrl(String directApiUrl) {
        this.directApiUrl = directApiUrl;
    }

    /**
     * 
     * @return webSocketUrl
     */
    public String getWebSocketUrl() {
        return webSocketUrl;
    }

    /**
     * 
     * @param webSocketUrl webSocketUrl
     */
    public void setWebSocketUrl(String webSocketUrl) {
        this.webSocketUrl = webSocketUrl;
    }

    @Override
    public String toString() {
        return "UserConfig [directApiUrl=" + directApiUrl + ", webSocketUrl=" + webSocketUrl + "]";
    }

}
