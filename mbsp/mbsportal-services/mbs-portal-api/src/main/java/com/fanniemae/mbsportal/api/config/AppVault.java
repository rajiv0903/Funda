/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.config;

/**
 * @author g8uaxt Created on 10/25/2017.
 */

public class AppVault {
        
        /**
         * tsAdminToken Admin Token
         */
        private String tsAdminToken=null;
        
        /**
         * 
         * Get TS admin token
         * 
         * @return String
         */
        public String getTsAdminToken() {
                return tsAdminToken;
        }
        
        /**
         * 
         * Set the admin token
         * 
         * @param tsAdminToken Admin token
         */
        public void setTsAdminToken(String tsAdminToken) {
                this.tsAdminToken = tsAdminToken;
        }
  
        
}
