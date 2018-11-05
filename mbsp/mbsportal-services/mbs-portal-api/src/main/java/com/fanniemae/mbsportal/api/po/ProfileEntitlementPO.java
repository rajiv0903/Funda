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
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author gaur5c
 * @Description - Presentation Object for Profile Entitlement Serialized to JSON
 *              Object
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ProfileEntitlementPO implements Serializable {
    /**
     * 
     * serialVersionUID long
     */
    private static final long serialVersionUID = -7391732330415321183L;
    /**
     * 
     * firstName String
     */
    private String firstName;
    /**
     * 
     * lastName String
     */
    private String lastName;
    /**
     * 
     * emailAddress String
     */
    private String emailAddress;
    /**
     * 
     * mobileNumber String
     */
    private String mobileNumber;
    /**
     * 
     * workNumber String
     */
    private String workNumber;
    /**
     * 
     * customerName String
     */
    private String customerName;
    /**
     * 
     * userName String
     */
    private String userName;
    /**
     * 
     * sellerServicerNumber String
     */
    private String sellerServicerNumber;
    /**
     * 
     * dealerOrgName String
     */
    private String dealerOrgName;
    /**
     * 
     * dealerOrgId String
     */
    private String dealerOrgId;
    /**
     * 
     * roles List<ProfileEntitlementRolePO>
     */
    private List<ProfileEntitlementRolePO> roles;
    /**
     * 
     * sellerServicerDetails List<String>
     */
    private List<String> sellerServicerDetails;
    /**
     * 
     * institutionId String
     */
    private String institutionId;
    /**
     * 
     * defaultSellerServicerNumber String
     */
    private String defaultSellerServicerNumber;
    /**
     * 
     * lenderDetails String
     */
    private String lenderDetails;
    /**
     * 
     * fannieMaeUser boolean
     */
    private boolean fannieMaeUser;
    /**
     * 
     * partyShortName String
     */
    private String partyShortName;
    /**
     * 
     * brsUserName String
     */
    private String brsUserName;
    
    /**
     * 
     * tspLenders - List of Lenders
     */
    private List<TspPartyLenderPO> tspLenders;
    
    /**
     * tspUser boolean
     */
    private boolean tspUser;
    
    /**
     * userConfigPO UserConfigPO
     */
    private UserConfigPO userConfig;
    
    
    
    
    /**
     * 
     * Custom method to add role
     * @param role the role
     */
    public void addRole(ProfileEntitlementRolePO role) {
        if(roles == null || roles.size() <= 0){
            roles = new ArrayList<>();
        }
        roles.add(role);
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
     * @param firstName the firstName
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
     * @param lastName the lastName
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
     * @param emailAddress the emailAddress
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
     * @param mobileNumber the mobileNumber
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
     * @param workNumber the workNumber
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
     * @param cutstomerName the cutstomerName
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
     * @param userName the userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
   
    /**
     * 
     * @return String
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
     * @param dealerOrgName the dealerOrgName
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
     * @param dealerOrgId the dealerOrgId
     */
    public void setDealerOrgId(String dealerOrgId) {
        this.dealerOrgId = dealerOrgId;
    }
    
    /**
     * 
     * @return List<ProfileEntitlementRolePO>
     */
    public List<ProfileEntitlementRolePO> getRoles() {
        return roles;
    }
    
    /**
     * 
     * @param roles the roles
     */
    public void setRoles(List<ProfileEntitlementRolePO> roles) {
        this.roles = roles;
    }
    
    /**
     * 
     * @return List<String>
     */
    public List<String> getSellerServicerDetails() {
        return sellerServicerDetails;
    }

    /**
     * 
     * @param sellerServiceDetails the sellerServiceDetails
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
     * @param institutionId the institutionId
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
     * @param defaultSellerServicerNumber the defaultSellerServicerNumber
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
     * @param lenderDetails the lenderDetails
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
     * @param fannieMaeUser the fannieMaeUser
     */
    public void setFannieMaeUser(boolean fannieMaeUser) {
        this.fannieMaeUser = fannieMaeUser;
    }
    
    /**
     * 
     * @return String
     */
    public String getPartyShortName() {
        return partyShortName;
    }
    
    /**
     * 
     * @param partyShortName the partyShortName
     */
    public void setPartyShortName(String partyShortName) {
        this.partyShortName = partyShortName;
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
     * @param brsUserName the brsUserName
     */
    public void setBrsUserName(String brsUserName) {
        this.brsUserName = brsUserName;
    }
    
    
    /**
     * 
     * @return list of tsp lenders
     */
    public List<TspPartyLenderPO> getTspLenders() {
        return tspLenders;
    }

    /**
     * 
     * @param tspLenders the tsp lenders
     */
    public void setTspLenders(List<TspPartyLenderPO> tspLenders) {
        this.tspLenders = tspLenders;
    }
    
    /**
     * 
     * Custom method to add tsp lenders
     * @param tspPartyLenderPO the tspPartyLenderPO
     */
    public void addTspPartyLender(TspPartyLenderPO tspPartyLenderPO) {
        if(tspLenders == null || tspLenders.size() <= 0){
            tspLenders = new ArrayList<>();
        }
        tspLenders.add(tspPartyLenderPO);
    }

    /**
     * 
     * @return boolean
     */
    public boolean isTspUser() {
        return tspUser;
    }

    /**
     * 
     * @param tspUser the tsp profile flag
     */
    public void setTspUser(boolean tspUser) {
        this.tspUser = tspUser;
    }
    
    
   
    /**
     * 
     * @return userConfig
     */
    public UserConfigPO getUserConfig() {
        return userConfig;
    }

    /**
     * 
     * @param userConfig the userConfig
     */
    public void setUserConfig(UserConfigPO userConfig) {
        this.userConfig = userConfig;
    }

    /**
     * 
     * @return String
     */
    @Override
    public String toString() {
        return "ProfileEntitlementPO [firstName=" + firstName + ", lastName=" + lastName + ", emailAddress="
                + emailAddress + ", mobileNumber=" + mobileNumber + ", workNumber=" + workNumber + ", customerName="
                + customerName + ", userName=" + userName + ", sellerServicerNumber=" + sellerServicerNumber
                + ", dealerOrgName=" + dealerOrgName + ", dealerOrgId=" + dealerOrgId + ", roles=" + roles
                + ", sellerServicerDetails=" + sellerServicerDetails + ", institutionId=" + institutionId
                + ", defaultSellerServicerNumber=" + defaultSellerServicerNumber + ", lenderDetails=" + lenderDetails
                + ", fannieMaeUser=" + fannieMaeUser + ", partyShortName=" + partyShortName + ", brsUserName="
                + brsUserName + ", tspLenders=" + tspLenders + ", tspUser=" + tspUser + ", userConfig="
                + userConfig + "]";
    }

  
   

}
