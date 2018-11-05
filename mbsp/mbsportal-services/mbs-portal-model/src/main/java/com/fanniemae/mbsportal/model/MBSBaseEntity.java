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

import com.fanniemae.fnmpfj.gemfire.dao.BaseEntity;

/**
 * Created by g8uaxt on 6/21/2017.
 */
public abstract class MBSBaseEntity extends BaseEntity {
	private static final long serialVersionUID = 3551952631538467394L;
    private String appId;

    //TODO: check with Sreenu
    private Integer mbsVersionNumber;

	public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
    /**
     * logicalDeleteIndicator declaration
     */
    private Character logicalDeleteIndicator = 'N';

	public Integer getMtaVersionNumber() {
		return mbsVersionNumber;
	}

	public void setMtaVersionNumber(Integer mbsVersionNumber) {
		this.mbsVersionNumber = mbsVersionNumber;
	}

	public Character getLogicalDeleteIndicator() {
		return logicalDeleteIndicator;
	}

	public void setLogicalDeleteIndicator(Character logicalDeleteIndicator) {
		this.logicalDeleteIndicator = logicalDeleteIndicator;
	}
}
