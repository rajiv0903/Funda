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

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Mar 13, 2018
 * @File: com.fanniemae.mbsportal.api.po.ProfileSessionPO.java
 * @Revision : 
 * @Description: CMMBSSTA01-1212 : (Tech) Trader user login calls MBSP API with
 *               valid Session ID
 */
@SuppressWarnings("serial")
public class ProfileSessionPO implements Serializable {

    /**
     * 
     * sessionId String
     */
    private String sessionId;
    /**
     * 
     * userName String
     */
    private String userName;
    
    /**
     * 
     * @return sessionId
     */
    public String getSessionId() {
        return sessionId;
    }
    /**
     * 
     * @param sessionId the sessionId
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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
     * @param userName the userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    
}
