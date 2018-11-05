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
 * @Date: Mar 15, 2018
 * @File: com.fanniemae.mbsportal.api.transformation.ProfileSessionPOTransformerTest.java
 * @Revision : 
 * @Description: ProfileSessionPOTransformerTest.java
 */
public class ProfileSessionPOTransformerTest {

    private ProfileSessionPOTransformer<TransformationObject> profileSessionPOTransformer;
    private MBSProfileSession mBSProfileSession;
    
    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        
        profileSessionPOTransformer = new ProfileSessionPOTransformer<>();
        
        mBSProfileSession = new MBSProfileSession();
        mBSProfileSession.setUserName("userName");
        mBSProfileSession.setSessionId("sessionId");
    }
    
    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void transform_Success() throws MBSBaseException {
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSProfileSession);
        profileSessionPOTransformer.transform(transformationObject);
        ProfileSessionPO profileSessionPO = (ProfileSessionPO) transformationObject.getSourcePojo();
        assertTrue(profileSessionPO.getSessionId().equals(mBSProfileSession.getSessionId()));
    }
}
