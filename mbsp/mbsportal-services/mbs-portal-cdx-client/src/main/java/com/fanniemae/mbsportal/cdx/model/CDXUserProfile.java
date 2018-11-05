package com.fanniemae.mbsportal.cdx.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 31, 2018
 * @File: com.fanniemae.mbsportal.cdx.model.CDXUserProfile.java 
 * @Revision: 
 * @Description: CDXUserProfile.java
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(Include.NON_NULL)
public class CDXUserProfile implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -5028980442824703355L;

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
     * roles List<CDXUserProfileRole>
     */
    private List<CDXUserProfileRole> roles;
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
     * authenticationLevel Integer
     */
    private Integer authenticationLevel;    
    /**
     * 
     * sellerServicerParentSubscriberId String
     */
    private String sellerServicerParentSubscriberId;    
    /**
     * 
     * sellerServicerCoNumber String
     */
    private String sellerServicerCoNumber;
    /**
     * 
     * clientId String
     */
    private String clientId;
    /**
     * 
     * mornetPlusId String
     */
    private String mornetPlusId;
    /**
     * 
     * userType Integer
     */
    private Integer userType;
    /**
     * 
     * userType String
     */
    private String accessibleApplication;
    /**
     * 
     * selfService selfService
     */
    private boolean selfService;
    /**
     * 
     * selfService selfService
     */
    private boolean fannieMaeUser;
    
    public CDXUserProfile(){}
    
    /**
     * 
     * @param firstName
     * @param lastName
     * @param emailAddress
     * @param mobileNumber
     * @param workNumber
     * @param customerName
     * @param userName
     * @param sellerServicerNumber
     * @param dealerOrgName
     * @param dealerOrgId
     * @param roles
     * @param sellerServicerDetails
     * @param institutionId
     * @param defaultSellerServicerNumber
     * @param lenderDetails
     * @param authenticationLevel
     * @param sellerServicerParentSubscriberId
     * @param sellerServicerCoNumber
     * @param clientId
     * @param mornetPlusId
     * @param userType
     * @param accessibleApplication
     * @param selfService
     * @param fannieMaeUser
     */
    public CDXUserProfile(String firstName, String lastName, String emailAddress, String mobileNumber,
            String workNumber, String customerName, String userName, String sellerServicerNumber, String dealerOrgName,
            String dealerOrgId, List<CDXUserProfileRole> roles, List<String> sellerServicerDetails,
            String institutionId, String defaultSellerServicerNumber, String lenderDetails, Integer authenticationLevel,
            String sellerServicerParentSubscriberId, String sellerServicerCoNumber, String clientId,
            String mornetPlusId, Integer userType, String accessibleApplication, boolean selfService,
            boolean fannieMaeUser) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.mobileNumber = mobileNumber;
        this.workNumber = workNumber;
        this.customerName = customerName;
        this.userName = userName;
        this.sellerServicerNumber = sellerServicerNumber;
        this.dealerOrgName = dealerOrgName;
        this.dealerOrgId = dealerOrgId;
        this.roles = roles;
        this.sellerServicerDetails = sellerServicerDetails;
        this.institutionId = institutionId;
        this.defaultSellerServicerNumber = defaultSellerServicerNumber;
        this.lenderDetails = lenderDetails;
        this.authenticationLevel = authenticationLevel;
        this.sellerServicerParentSubscriberId = sellerServicerParentSubscriberId;
        this.sellerServicerCoNumber = sellerServicerCoNumber;
        this.clientId = clientId;
        this.mornetPlusId = mornetPlusId;
        this.userType = userType;
        this.accessibleApplication = accessibleApplication;
        this.selfService = selfService;
        this.fannieMaeUser = fannieMaeUser;
    }
    /**
     * 
     * @return firstName
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
     * @return lastName
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
     * @return emailAddress
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
     * @return mobileNumber
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
     * @return workNumber
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
     * @return customerName
     */
    public String getCustomerName() {
        return customerName;
    }
    /**
     * 
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    /**
     * 
     * @return userName
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
     * @param sellerServicerNumber
     */
    public void setSellerServicerNumber(String sellerServicerNumber) {
        this.sellerServicerNumber = sellerServicerNumber;
    }
    /**
     * 
     * @return dealerOrgName
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
     * @return dealerOrgId
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
     * @return roles
     */
    public List<CDXUserProfileRole> getRoles() {
        return roles;
    }
    /**
     * 
     * @param roles
     */
    public void setRoles(List<CDXUserProfileRole> roles) {
        this.roles = roles;
    }
    /**
     * 
     * @return sellerServicerDetails
     */
    public List<String> getSellerServicerDetails() {
        return sellerServicerDetails;
    }
    /**
     * 
     * @param sellerServicerDetails
     */
    public void setSellerServicerDetails(List<String> sellerServicerDetails) {
        this.sellerServicerDetails = sellerServicerDetails;
    }
    /**
     * 
     * @return institutionId
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
     * @return defaultSellerServicerNumber
     */
    public String getDefaultSellerServicerNumber() {
        return defaultSellerServicerNumber;
    }
    /**
     * 
     * @param defaultSellerServicerNumber
     */
    public void setDefaultSellerServicerNumber(String defaultSellerServicerNumber) {
        this.defaultSellerServicerNumber = defaultSellerServicerNumber;
    }
    /**
     * 
     * @return lenderDetails
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
     * @return authenticationLevel
     */
    public Integer getAuthenticationLevel() {
        return authenticationLevel;
    }
    /**
     * 
     * @param authenticationLevel
     */
    public void setAuthenticationLevel(Integer authenticationLevel) {
        this.authenticationLevel = authenticationLevel;
    }
    /**
     * 
     * @return sellerServicerParentSubscriberId
     */
    public String getSellerServicerParentSubscriberId() {
        return sellerServicerParentSubscriberId;
    }
    /**
     * 
     * @param sellerServicerParentSubscriberId
     */
    public void setSellerServicerParentSubscriberId(String sellerServicerParentSubscriberId) {
        this.sellerServicerParentSubscriberId = sellerServicerParentSubscriberId;
    }
    /**
     * 
     * @return sellerServicerCoNumber
     */
    public String getSellerServicerCoNumber() {
        return sellerServicerCoNumber;
    }
    /**
     * 
     * @param sellerServicerCoNumber
     */
    public void setSellerServicerCoNumber(String sellerServicerCoNumber) {
        this.sellerServicerCoNumber = sellerServicerCoNumber;
    }
    /**
     * 
     * @return clientId
     */
    public String getClientId() {
        return clientId;
    }
    /**
     * 
     * @param clientId
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    /**
     * 
     * @return mornetPlusId
     */
    public String getMornetPlusId() {
        return mornetPlusId;
    }
    /**
     * 
     * @param mornetPlusId
     */
    public void setMornetPlusId(String mornetPlusId) {
        this.mornetPlusId = mornetPlusId;
    }
    /**
     * 
     * @return userType
     */
    public Integer getUserType() {
        return userType;
    }
    /**
     * 
     * @param userType
     */
    public void setUserType(Integer userType) {
        this.userType = userType;
    }
    /**
     * 
     * @return accessibleApplication
     */
    public String getAccessibleApplication() {
        return accessibleApplication;
    }
    /**
     * 
     * @param accessibleApplication
     */
    public void setAccessibleApplication(String accessibleApplication) {
        this.accessibleApplication = accessibleApplication;
    }
    /**
     * 
     * @return selfService
     */
    public boolean isSelfService() {
        return selfService;
    }
    /**
     * 
     * @param selfService
     */
    public void setSelfService(boolean selfService) {
        this.selfService = selfService;
    }
    /**
     * 
     * @return fannieMaeUser
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



    public static class CDXUserProfileRole{
        /**
         * 
         *  name String
         */
        private String name;
        /**
         * 
         * appCode String
         */
        private String appCode;
        /**
         * 
         * permissions List<String>
         */
        private List<String> permissions;
        
        /**
         * 
         * @return name
         */
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        /**
         * 
         * @return appCode
         */
        public String getAppCode() {
            return appCode;
        }
        public void setAppCode(String appCode) {
            this.appCode = appCode;
        }
        /**
         * 
         * @return permissions list of permission
         */
        public List<String> getPermissions() {
            return permissions;
        }
        /**
         * 
         * @param permissions
         */
        public void setPermissions(List<String> permissions) {
            this.permissions = permissions;
        }
        
        public CDXUserProfileRole(){}
        
        /**
         * 
         * @param name
         * @param appCode
         * @param permissions
         */
        public CDXUserProfileRole(String name, String appCode, List<String> permissions) {
            super();
            this.name = name;
            this.appCode = appCode;
            this.permissions = permissions;
        }
        
        @Override
        public String toString() {
            return "CDXUserProfileRole [name=" + name + ", appCode=" + appCode + ", permissions=" + permissions + "]";
        }
        
        
    }
}
