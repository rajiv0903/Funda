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

package com.fanniemae.mbsportal.publisher;

import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Apr 16, 2018
 * @File: com.fanniemae.mbsportal.publisher.Publisher.java
 * @Revision :
 * @Description: Publisher.java
 */
public interface MessagePublisher<T extends TransformationObject> {

    /**
     * 
     * @param obj
     * @throws MBSBaseException
     */
    public void publish(T obj) throws MBSBaseException;
}
