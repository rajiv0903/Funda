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

package com.fanniemae.mbsportal.utils;



import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author g8upjv
 */
public class MBSObjectCreatorTest {
     
        MBSObjectCreator mbsObjectCreator = new MBSObjectCreator();
        
        @Test
        public void getTransformationObject() throws MBSBaseException {
        	assertNotNull(mbsObjectCreator.getTransformationObject());
        }
        
}
