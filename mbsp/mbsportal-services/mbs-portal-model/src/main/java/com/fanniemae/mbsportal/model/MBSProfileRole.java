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

import java.util.List;

/**
 * 
 * @author g8upjv
 *
 */
public class MBSProfileRole {

    private String name;
    private String appCode;
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
     * @param name
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
     * @param appCode
     */
    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    /**
     * 
     * @return List
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

    /**
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MBSProfileRole)) {
            return false;
        }
        MBSProfileRole c = (MBSProfileRole) o;

        return name.equalsIgnoreCase(c.getName());
    }

    /**
     * @return int
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + name.hashCode();
        return result;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MBSProfileRole [name=" + name + ", appCode=" + appCode + ", permissions=" + permissions + "]";
    }
    
    

}
