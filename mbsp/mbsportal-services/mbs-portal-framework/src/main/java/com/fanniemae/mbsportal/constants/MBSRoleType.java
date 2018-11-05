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

package com.fanniemae.mbsportal.constants;

import java.io.Serializable;

/**
 * 
 * @author g8upjv
 *
 */
public enum MBSRoleType implements Serializable {

    LENDER ("LENDER"), TRADER ("TRADER"), TSP ("TSP"), LENDER_AND_TRADER ("LENDER_AND_TRADER");
    
    private String role;
    
    /**
     * Constructor 
     * @param role
     */
    MBSRoleType(String role) {
        this.role = role;
    }
    
    /**
     * 
     * @return String(role)
     */
    public String getRole() {
        return this.role;
    }
}
