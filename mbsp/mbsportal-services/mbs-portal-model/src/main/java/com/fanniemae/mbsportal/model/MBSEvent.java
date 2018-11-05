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

public class MBSEvent extends MBSBaseEntity {

    private static final long serialVersionUID = -687991492884005033L;

    private String userName;
    private Long eventTimeStamp;

    /**
     * @see com.fanniemae.fnmpfj.gemfire.dao.BaseEntity#getId()
     */
    @Override
    public Serializable getId() {
        return this.userName;
    }

    public MBSEvent() {
    }

    public MBSEvent(String userName, long eventTimeStamp) {
        this.userName = userName;
        this.eventTimeStamp = eventTimeStamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getEventTimeStamp() {
        return eventTimeStamp;
    }

    public void setEventTimeStamp(Long eventTimeStamp) {
        this.eventTimeStamp = eventTimeStamp;
    }

    @Override
    public String toString() {
        return "MBSEvent [userName=" + userName + ", eventTimeStamp=" + eventTimeStamp + ", createdOn=" + createdOn
                + ", lastUpdated=" + lastUpdated + ", lastUpdatedBy=" + lastUpdatedBy + ", createdBy=" + createdBy
                + "]";
    }

    

}
