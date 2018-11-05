/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.cdx.config;


import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jun 19, 2018
 * @File: com.fanniemae.mbsportal.utils.config.CDXApiClientConfig.java 
 * @Revision: 
 * @Description: CDXApiClientConfig.java
 */
@Component
@ConfigurationProperties("cdx.api.client")
public class CDXApiClientConfig {
    /**
     * 
     * certlocation Location path of the certificate
     */
    @NotEmpty
    private String[] certlocation;
    /**
     * 
     * entitlementpassthrough entitlement pass through flag
     */
    private boolean entitlementpassthrough;

    /**
     * 
     * proxyHost CDX Proxy Host
     */
    private String proxyHost;

    /**
     * 
     * proxyPort CDX Proxy Port
     */
    private int proxyPort;
    
    /**
     * 
     * hostUrl CDX Host URL
     */
    @NotEmpty
    private String hostUrl;
    
    /**
     * 
     * baseurl CDX Base URL
     */
    @NotEmpty
    private String baseurl;
    
    /**
     * 
     * loginapi CDX Login API Url
     */
    
    @NotEmpty
    private String loginapi;
    
    /**
     * 
     * profileapi CDX profile API Url
     */
    @NotEmpty
    private String profileapi;
    
    /**
     * 
     * sessionapi CDX session API URL
     */
    @NotEmpty
    private String sessionapi;
    
    /**
     * 
     * headerChannel String
     */
    private String headerChannel;
    /**
     * 
     * headerSubChannel String
     */
    private String headerSubChannel;
    
    /**
     * 
     * userId String
     */
    private String userId;

    /**
     * 
     * @return String[]
     */
    public String[] getCertlocation() {
        return certlocation;
    }

    /**
     * 
     * @param certlocation the certificate location being set
     */
    public void setCertlocation(String[] certlocation) {
        this.certlocation = certlocation;
    }

    /**
     * 
     * getter method for entitlement pass through field
     * 
     * @return boolean
     */
    public boolean isEntitlementpassthrough() {
        return entitlementpassthrough;
    }

    /**
     * 
     * setter method for entitlement pass through field
     * 
     * @param entitlementpassthrough pass through entitlement flag being set
     */
    public void setEntitlementpassthrough(boolean entitlementpassthrough) {
        this.entitlementpassthrough = entitlementpassthrough;
    }

    /**
     * 
     * Getter method for proxyHost
     * 
     * @return String
     */
    public String getProxyHost() {
        return proxyHost;
    }

    /**
     * 
     * Setter method for proxyHost
     * 
     * @param proxyHost Proxy Host for DC 
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
     * 
     * Getter method for proxyPort
     * 
     * @return int proxyPort
     */
    public int getProxyPort() {
        return proxyPort;
    }

    /**
     * 
     * Setter method for proxyPort
     * 
     * @param proxyPort Proxy Port for DC 
     */
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    /**
     * 
     * Getter method for hostUrl
     * 
     * @return String hostUrl
     */
    public String getHostUrl() {
        return hostUrl;
    }

    /**
     * 
     * Setter method for hostUrl
     * 
     * @param hostUrl Host URL for DC 
     */
    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    /**
     * 
     * Getter method for baseurl
     * 
     * @return String baseurl
     */
    public String getBaseurl() {
        return baseurl;
    }

    /**
     * 
     * Setter method for baseurl
     * 
     * @param baseurl Base URL for DC 
     */
    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }

    /**
     * 
     * Getter method for loginapi
     * 
     * @return String loginapi
     */
    public String getLoginapi() {
        return loginapi;
    }

    /**
     * 
     * Setter method for loginapi
     * 
     * @param loginapi endpoint for login API 
     */
    public void setLoginapi(String loginapi) {
        this.loginapi = loginapi;
    }

    /**
     * 
     * Getter method for profileapi
     * 
     * @return String profileapi
     */
    public String getProfileapi() {
        return profileapi;
    }

    /**
     * 
     * Setter method for loginapi
     * 
     * @param profileapi endpoint for Profile API 
     */
    public void setProfileapi(String profileapi) {
        this.profileapi = profileapi;
    }

    /**
     * 
     * Getter method for sessionapi
     * 
     * @return String sessionapi
     */
    public String getSessionapi() {
        return sessionapi;
    }

    /**
     * 
     * Setter method for sessionapi
     * 
     * @param sessionapi endpoint for Validity Session API 
     */
    public void setSessionapi(String sessionapi) {
        this.sessionapi = sessionapi;
    }

    /**
     * 
     * Getter method for headerChannel
     * 
     * @return String headerChannel
     */
    public String getHeaderChannel() {
        return headerChannel;
    }

    /**
     * 
     * Setter method for headerChannel
     * 
     * @param headerChannel CDX Header Channel 
     */
    public void setHeaderChannel(String headerChannel) {
        this.headerChannel = headerChannel;
    }

    /**
     * 
     * Getter method for headerSubChannel
     * 
     * @return String headerSubChannel
     */
    public String getHeaderSubChannel() {
        return headerSubChannel;
    }

    /**
     * 
     * Setter method for headerSubChannel
     * 
     * @param headerSubChannel CDX Header Sub Channel 
     */
    public void setHeaderSubChannel(String headerSubChannel) {
        this.headerSubChannel = headerSubChannel;
    }

    /**
     * 
     * Getter method for userId
     * 
     * @return String userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 
     * Setter method for userId
     * 
     * @param userId CDX User ID to generate token
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    

}
