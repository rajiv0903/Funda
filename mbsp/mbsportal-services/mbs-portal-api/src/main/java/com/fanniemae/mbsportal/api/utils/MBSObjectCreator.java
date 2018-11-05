/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */


package com.fanniemae.mbsportal.api.utils;

import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.pojo.TransformationObject;

/**
 * 
 * This class is the centralized utility for creating new objects.
 * @author g8upjv
 * @date 19th Jan 2018
 * 
 *
 */
@Component
public class MBSObjectCreator {

    /**
     * 
     * This method returns new TransformationObject
     * 
     * @return TransformationObject
     */
    public TransformationObject getTransformationObject() {
        return new TransformationObject();
    }
    
}
