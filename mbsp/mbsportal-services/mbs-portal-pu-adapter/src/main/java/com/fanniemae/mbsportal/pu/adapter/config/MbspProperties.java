/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.pu.adapter.config;



import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by g8uaxt on 6/19/2017. Simple bean for prop injection
 * 
 * @author g8upjv
 */
//TODO: properties needs to be cleaned/synched with MBS
@Component
@ConfigurationProperties("mbsp")
public class MbspProperties {

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
     * systemUserId the systemUserId
     */
    @NotEmpty
    private String systemUserId;

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
