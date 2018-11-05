/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.api.config;


import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by g8uaxt on 6/19/2017. Simple bean for prop injection
 * 
 * @author g8upjv
 */
@Component
@ConfigurationProperties("mbsp")
public class MbspProperties {

    /**
     * 
     * cutOffHour the cutOffHour
     */
    @NotEmpty
    private String cutOffHour;

    /**
     * 
     * cutOffMinute the cutOffMinute
     */
    @NotEmpty
    private String cutOffMinute;

    /**
     * 
     * cutOffSecond the cutOffSecond
     */
    @NotEmpty
    private String cutOffSecond;

    /**
     * 
     * cutOffMillisecond the cutOffMillisecond
     */
    @NotEmpty
    private String cutOffMillisecond;

    /**
     * 
     * numberOfDisplayMonths the numberOfDisplayMonths
     */
    @NotEmpty
    private String numberOfDisplayMonths;

    /**
     * 
     * corsHostUrl the corsHostUrl
     */
    @NotEmpty
    private String corsHostUrl;

    /**
     * 
     * awsVref the awsVref
     */
    @NotEmpty
    private String awsVref;

    /**
     * 
     * envCd the environment code
     */
    @NotEmpty
    private String envCd;

    /**
     * 
     * epvRefId the epvRefId
     */
    @NotEmpty
    private String epvRefId;
    
    /**
     * 
     * transHistActiveDays the transHistActiveDays
     */
    @NotEmpty
    private String transHistActiveDays;
    
    
    /**
     * 
     * systemUserId the systemUserId
     */
    @NotEmpty
    private String systemUserId;
    
    
    /**
     * 
     * directApiUrl the directApiUrl
     */
    @NotEmpty
    private String directApiUrl;
    
    
    /**
     * 
     * fnmSellerSerivcerNo the fnmSellerSerivcerNo
     */
    @NotNull
    private Integer fnmSellerSerivcerNo;
    
  

    /**
     * 
     * @return String
     */
    public String getEpvRefId() {
        return epvRefId;
    }

    /**
     * 
     * @param epvRefId the epvRefId
     */
    public void setEpvRefId(String epvRefId) {
        this.epvRefId = epvRefId;
    }

    /**
     * 
     * @return String
     */
    public String getEnvCd() {
        return envCd;
    }

    /**
     * 
     * @param envCd the envCd
     */
    public void setEnvCd(String envCd) {
        this.envCd = envCd;
    }

    /**
     * 
     * @return String
     */
    public String getCorsHostUrl() {
        return corsHostUrl;
    }

    /**
     * 
     * @param corsHostUrl the corsHostUrl
     */
    public void setCorsHostUrl(String corsHostUrl) {
        this.corsHostUrl = corsHostUrl;
    }

    /**
     * 
     * @return String
     */
    public String getAwsVref() {
        return awsVref;
    }

    /**
     * 
     * @param awsVref the awsVref
     */
    public void setAwsVref(String awsVref) {
        this.awsVref = awsVref;
    }

    /**
     * 
     * @return cutOffHour
     */
    public String getCutOffHour() {
        return cutOffHour;
    }

    /**
     * 
     * @param cutOffHour
     *            the cutOffHour to set
     */
    public void setCutOffHour(String cutOffHour) {
        this.cutOffHour = cutOffHour;
    }

    /**
     * 
     * @return cutOffMinute
     */
    public String getCutOffMinute() {
        return cutOffMinute;
    }

    /**
     * 
     * @param cutOffMinute
     *            the cutOffMinute to set
     */
    public void setCutOffMinute(String cutOffMinute) {
        this.cutOffMinute = cutOffMinute;
    }

    /**
     * 
     * @return the cutOffSecond
     */
    public String getCutOffSecond() {
        return cutOffSecond;
    }

    /**
     * 
     * @param cutOffSecond
     *            the cutOffSecond to set
     */
    public void setCutOffSecond(String cutOffSecond) {
        this.cutOffSecond = cutOffSecond;
    }

    /**
     * 
     * @return cutOffMillisecond
     */
    public String getCutOffMillisecond() {
        return cutOffMillisecond;
    }

    /**
     * 
     * @param cutOffMillisecond
     *            the cutOffMillisecond to set
     */
    public void setCutOffMillisecond(String cutOffMillisecond) {
        this.cutOffMillisecond = cutOffMillisecond;
    }

    /**
     * 
     * @return numberOfDisplayMonths
     */
    public String getNumberOfDisplayMonths() {
        return numberOfDisplayMonths;
    }

    /**
     * 
     * @param numberOfDisplayMonths
     *            the numberOfDisplayMonths to set
     */
    public void setNumberOfDisplayMonths(String numberOfDisplayMonths) {
        this.numberOfDisplayMonths = numberOfDisplayMonths;
    }

    /**
     * @return the transHistActiveDays
     */
    public String getTransHistActiveDays() {
        return transHistActiveDays;
    }

    /**
     * @param transHistActiveDays the transHistActiveDays to set
     */
    public void setTransHistActiveDays(String transHistActiveDays) {
        this.transHistActiveDays = transHistActiveDays;
    }

    /**
     * @return the systemUserId
     */
    public String getSystemUserId() {
        return systemUserId;
    }

    /**
     * @param systemUserId the systemUserId to set
     */
    public void setSystemUserId(String systemUserId) {
        this.systemUserId = systemUserId;
    }

    /**
     * @return the directApiUrl
     */
    public String getDirectApiUrl() {
        return directApiUrl;
    }
    /**
     * @param directApiUrl the directApiUrl to set
     */
    public void setDirectApiUrl(String directApiUrl) {
        this.directApiUrl = directApiUrl;
    }

    /**
     * 
     * @return fnmSellerSerivcerNo the FannieMae Seller Service Number
     */
    public Integer getFnmSellerSerivcerNo() {
        return fnmSellerSerivcerNo;
    }

    /**
     * 
     * @param fnmSellerSerivcerNo the FannieMae Seller Service Number
     */
    public void setFnmSellerSerivcerNo(Integer fnmSellerSerivcerNo) {
        this.fnmSellerSerivcerNo = fnmSellerSerivcerNo;
    }

    
    
}
