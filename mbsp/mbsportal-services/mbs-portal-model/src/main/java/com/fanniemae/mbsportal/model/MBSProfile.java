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

import java.io.Serializable;
import java.util.List;

/**
 * @author gaur5c
 * @Description- Persistance Object for MBSProfileEntitlement Region
 */
public class MBSProfile extends MBSBaseEntity {

    private static final long serialVersionUID = -687991492884005033L;

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String mobileNumber;
    private String workNumber;
    private String customerName;
    private String userName;
    private String sellerServicerNumber;
    private String dealerOrgName;
    private String dealerOrgId;
    private List<MBSProfileRole> roles;
    private List<String> sellerServicerDetails;
    private String institutionId;
    private String defaultSellerServicerNumber;
    private String lenderDetails;
    private boolean fannieMaeUser;
    private String brsUserName;
    private boolean tspUser;   
    private UserConfig userConfig;
   


    /**
     * @see com.fanniemae.fnmpfj.gemfire.dao.BaseEntity#getId()
     */
    @Override
    public Serializable getId() {
        return this.userName;
    }

    /**
     * 
     * @return long
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * 
     * @return String
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * 
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * 
     * @return String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * 
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * 
     * @return String
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * 
     * @param emailAddress
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * 
     * @return String
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * 
     * @param mobileNumber
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * 
     * @return String
     */
    public String getWorkNumber() {
        return workNumber;
    }

    /**
     * 
     * @param workNumber
     */
    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }
    
    /**
     * 
     * @return String
     */
    public String getCustomerName() {
        return customerName;
    }
    /**
     * 
     * @param cutstomerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

   

    /**
     * 
     * @return String
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    
    /**
     * 
     * @return sellerServicerNumber
     */
    public String getSellerServicerNumber() {
        return sellerServicerNumber;
    }

    /**
     * 
     * @param sellerServicerNumber the sellerServicerNumber
     */
    public void setSellerServicerNumber(String sellerServicerNumber) {
        this.sellerServicerNumber = sellerServicerNumber;
    }

    /**
     * 
     * @return String
     */
    public String getDealerOrgName() {
        return dealerOrgName;
    }

    /**
     * 
     * @param dealerOrgName
     */
    public void setDealerOrgName(String dealerOrgName) {
        this.dealerOrgName = dealerOrgName;
    }

    /**
     * 
     * @return String
     */
    public String getDealerOrgId() {
        return dealerOrgId;
    }

    /**
     * 
     * @param dealerOrgId
     */
    public void setDealerOrgId(String dealerOrgId) {
        this.dealerOrgId = dealerOrgId;
    }

    /**
     * 
     * @return List
     */
    public List<MBSProfileRole> getRoles() {
        return roles;
    }

    /**
     * 
     * @param roles
     */
    public void setRoles(List<MBSProfileRole> roles) {
        this.roles = roles;
    }

    /**
     * 
     * @return List
     */
    public List<String> getSellerServicerDetails() {
        return sellerServicerDetails;
    }

    /**
     * 
     * @param sellerServiceDetails
     */
    public void setSellerServicerDetails(List<String> sellerServicerDetails) {
        this.sellerServicerDetails = sellerServicerDetails;
    }
    /**
     * 
     * @return String
     */
    public String getInstitutionId() {
        return institutionId;
    }

    /**
     * 
     * @param institutionId
     */
    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    /**
     * 
     * @return String
     */
    public String getDefaultSellerServicerNumber() {
        return defaultSellerServicerNumber;
    }

    /**
     * 
     * @param defaultSellerServicerNo
     */
    public void setDefaultSellerServicerNumber(String defaultSellerServicerNumber) {
        this.defaultSellerServicerNumber = defaultSellerServicerNumber;
    }

    /**
     * 
     * @return String
     */
    public String getLenderDetails() {
        return lenderDetails;
    }

    /**
     * 
     * @param lenderDetails
     */
    public void setLenderDetails(String lenderDetails) {
        this.lenderDetails = lenderDetails;
    }

    /**
     * 
     * @return boolean
     */
    public boolean isFannieMaeUser() {
        return fannieMaeUser;
    }

    /**
     * 
     * @param fannieMaeUser
     */
    public void setFannieMaeUser(boolean fannieMaeUser) {
        this.fannieMaeUser = fannieMaeUser;
    }


    /**
     * 
     * @return String
     */
    public String getBrsUserName() {
        return brsUserName;
    }

    /**
     * 
     * @param brsUserName
     */
    public void setBrsUserName(String brsUserName) {
        this.brsUserName = brsUserName;
    }
   

    /**
     * @return the tspUser
     */
    public boolean isTspUser() {
        return tspUser;
    }

    /**
     * @param tspUser the tspUser to set
     */
    public void setTspUser(boolean tspUser) {
        this.tspUser = tspUser;
    }
    
    /**
     * 
     * @return the userConfig
     */
    public UserConfig getUserConfig() {
        return userConfig;
    }

    /**
     * 
     * @param userConfig the userConfig
     */
    public void setUserConfig(UserConfig userConfig) {
        this.userConfig = userConfig;
    }

    /**
     * 
     * @return String
     */
    @Override
    public String toString() {
        return "MBSProfile [firstName=" + firstName + ", lastName=" + lastName + ", emailAddress=" + emailAddress
                + ", mobileNumber=" + mobileNumber + ", workNumber=" + workNumber + ", customerName=" + customerName
                + ", userName=" + userName + ", sellerServicerNumber=" + sellerServicerNumber + ", dealerOrgName="
                + dealerOrgName + ", dealerOrgId=" + dealerOrgId + ", roles=" + roles + ", sellerServicerDetails="
                + sellerServicerDetails + ", institutionId=" + institutionId + ", defaultSellerServicerNumber="
                + defaultSellerServicerNumber + ", lenderDetails=" + lenderDetails + ", fannieMaeUser=" + fannieMaeUser
                + ", brsUserName=" + brsUserName + ", userConfig=" + userConfig + ", tspUser=" + tspUser + "]";
    }
    
    
}
