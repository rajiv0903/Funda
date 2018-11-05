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

package com.fanniemae.mbsportal.discovery;

import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * Created by g8uaxt on 6/28/2017.
 * @author g8upjv
 */
public abstract class BaseDiscovery<T extends TransformationObject> implements Discovery<T> {

    /**
     * @param obj
     * @throws MBSBaseException
     */
	public abstract void discover(T obj) throws MBSBaseException;
}
