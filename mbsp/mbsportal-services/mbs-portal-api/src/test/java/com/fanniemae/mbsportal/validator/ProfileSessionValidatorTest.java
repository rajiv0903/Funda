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

package com.fanniemae.mbsportal.validator;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.api.po.ProfileSessionPO;
import com.fanniemae.mbsportal.api.validator.ProfileSessionValidator;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Mar 15, 2018
 * @File: com.fanniemae.mbsportal.validator.ProfileSessionValidatorTest.java
 * @Revision :
 * @Description: ProfileSessionValidatorTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class ProfileSessionValidatorTest {

    private ProfileSessionValidator<TransformationObject> profileSessionValidator;

    private ProfileSessionPO profileSessionPO;

    @Before
    public void setUp() throws Exception {

        profileSessionValidator = new ProfileSessionValidator<TransformationObject>();

        profileSessionPO = new ProfileSessionPO();
        profileSessionPO.setUserName("username");
        profileSessionPO.setSessionId("sessionId");
    }

    @Test
    public void validate() throws Exception {

        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(profileSessionPO);
        profileSessionValidator.validate(transformationObject);
        ProfileSessionPO profileSessionPORet = (ProfileSessionPO) transformationObject.getSourcePojo();
        assertTrue(profileSessionPO.getUserName().equals(profileSessionPORet.getUserName()));
    }

    @Test(expected = MBSBusinessException.class)
    public void validate_For_Empty_SessionID_Failure() throws Exception {

        profileSessionPO.setSessionId(null);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(profileSessionPO);
        profileSessionValidator.validate(transformationObject);
    }

    @Test(expected = MBSBusinessException.class)
    public void validate_For_Empty_UserName_Failure() throws Exception {

        profileSessionPO.setUserName(null);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(profileSessionPO);
        profileSessionValidator.validate(transformationObject);
    }
}
