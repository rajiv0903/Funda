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

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Mar 12, 2018
 * @File: com.fanniemae.mbsportal.model.MBSProfileSession.java
 * @Revision :
 * @Description: MBSProfileSession.java
 * CMMBSSTA01-1212 : (Tech) Trader user login calls MBSP API with valid Session ID
 */
public class MBSProfileSession extends MBSBaseEntity {

    private static final long serialVersionUID = -787991492884005033L;

    private String sessionId;
    private String userName;

    /**
     * @see com.fanniemae.fnmpfj.gemfire.dao.BaseEntity#getId()
     */
    @Override
    public Serializable getId() {
        return this.sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
