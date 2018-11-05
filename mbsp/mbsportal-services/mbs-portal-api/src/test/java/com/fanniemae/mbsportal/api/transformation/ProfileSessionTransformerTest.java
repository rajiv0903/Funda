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

package com.fanniemae.mbsportal.api.transformation;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.fanniemae.mbsportal.api.po.ProfileSessionPO;
import com.fanniemae.mbsportal.model.MBSProfileSession;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Apr 2, 2018
 * @File: com.fanniemae.mbsportal.api.transformation.ProfileSessionTransformerTest.java
 * @Revision : 
 * @Description: ProfileSessionTransformerTest.java
 */
public class ProfileSessionTransformerTest {

    
    private ProfileSessionTransformer<TransformationObject> profileSessionTransformer;
    private ProfileSessionPO profileSessionPO;
    private MBSProfileSession mBSProfileSession;
    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        
        profileSessionTransformer = new ProfileSessionTransformer<>();
        
        profileSessionPO = new ProfileSessionPO();
        profileSessionPO.setUserName("userName");
        profileSessionPO.setSessionId("sessionId");
        
        mBSProfileSession = new MBSProfileSession();
    }
    
    
    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void transform_Success() throws MBSBaseException {
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(profileSessionPO);
        transformationObject.setTargetPojo(mBSProfileSession);
        profileSessionTransformer.transform(transformationObject);
        MBSProfileSession mBSProfileSession = (MBSProfileSession) transformationObject.getTargetPojo();
        assertTrue(profileSessionPO.getSessionId().equals(mBSProfileSession.getSessionId()));
    }
}

