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

import java.util.List;


/**
 * @author gaur5c
 * @Description - Profile Role for right access
 */
public class ProfileEntitlementRolePO {
    /**
     * 
     *  serialVersionUID long
     */
    private static final long serialVersionUID = -3553433810461830541L;
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
     * @return String
     */
    public String getName() {
        return name;
    }
    
    /**
     * 
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 
     * @return String
     */
    public String getAppCode() {
        return appCode;
    }
    
    /**
     * 
     * @param appCode the appCode
     */
    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }
    
    /**
     * 
     * @return List<String>
     */
    public List<String> getPermissions() {
        return permissions;
    }
    
    /**
     * 
     * @param permissions the permissions
     */
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
    
    /**
     * 
     * @return String
     */
    @Override
    public String toString() {
        return "ProfileEntitlementRolePO [name=" + name + ", permissions=" + permissions + "]";
    }
    
    /**
     * 
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ProfileEntitlementRolePO)) {
            return false;
        }
        ProfileEntitlementRolePO c = (ProfileEntitlementRolePO) o;
        
        return name.equalsIgnoreCase(c.getName());
    }
    
    /**
     * 
     * @return int
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + name.hashCode();
        return result;
    }
    
    

}
