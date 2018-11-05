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

package com.fanniemae.mbsportal.pu.adapter.config;

import com.fanniemae.all.messaging.api.ESBClientCredentials;

/**
 * 
 * Client Credentials Implementation class used by ESB api.
 * 
 * @author g8upjv
 * 
 */
public class MBSEsbClientCredentials implements ESBClientCredentials {
    
    /**
     * 
     * ESBUserId String
     */
    private String ESBUserId;
    /**
     * 
     * ESBPassword String
     */
    private String ESBPassword;

    /**
     * 
     * EFSSTrustStorePassword String
     */
    private String EFSSTrustStorePassword;
    
    /**
     * 
     * @param ESBUserId String
     * @param ESBPassword String
     * @param EFSSTrustStorePassword String
     */
    public MBSEsbClientCredentials(String ESBUserId, String ESBPassword, String EFSSTrustStorePassword) {
        this.ESBUserId = ESBUserId;
        this.ESBPassword = ESBPassword;
        this.EFSSTrustStorePassword = EFSSTrustStorePassword;
    }
    
    /**
     * 
     * Setter for ESB password
     * 
     * @param eSBPassword
     *            to set
     */
    public void setESBPassword(String eSBPassword) {
        ESBPassword = eSBPassword;
    }

    /**
     * 
     * Getter for ESB password
     * 
     * @return String
     * @see com.fanniemae.all.messaging.api.ESBClientCredentials#getESBPassword()
     */
    @Override
    public String getESBPassword() {
        return ESBPassword;
    }

    /**
     * 
     * Setter for ESB User Id
     * 
     * @param esbUserId
     *            to set
     */
    public void setESBUserId(String esbUserId) {
        this.ESBUserId = esbUserId;
    }

    /**
     * 
     * Getter for ESB User Id
     * 
     * @return String
     * 
     * @see com.fanniemae.all.messaging.api.ESBClientCredentials#getESBUserId()
     */
    @Override
    public String getESBUserId() {
        return ESBUserId;
    }
    
    /**
     * 
     * @return String
     */
    @Override
    public String getEFSSTrustStorePassword() {
        return EFSSTrustStorePassword;
    }

    /**
     * 
     * @param eFSSTrustStorePassword
     *            the eFSSTrustStorePassword to set
     */
    public void setEFSSTrustStorePassword(String eFSSTrustStorePassword) {
        EFSSTrustStorePassword = eFSSTrustStorePassword;
    }

}
