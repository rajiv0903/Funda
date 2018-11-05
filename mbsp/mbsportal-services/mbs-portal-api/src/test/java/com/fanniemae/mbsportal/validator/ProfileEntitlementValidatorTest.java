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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementRolePO;
import com.fanniemae.mbsportal.api.validator.ProfileEntitlementValidator;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 22, 2018
 * @Time 12:20:37 PM com.fanniemae.mbsportal.api.validator
 *       ProfileEntitlementValidatorTest.java
 * @Description: CMMBSSTA01-962: (Tech) Improve CAST for MBSP
 */
@RunWith(MockitoJUnitRunner.class)
public class ProfileEntitlementValidatorTest {

    private ProfileEntitlementValidator<TransformationObject> profileEntitlementValidator;

    private ProfileEntitlementPO profileEntitlementPO;
    private List<ProfileEntitlementRolePO> roles;


    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        profileEntitlementValidator = new ProfileEntitlementValidator<TransformationObject>();
        createProfile();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void validate_Success() throws Exception {

        profileEntitlementPO.setRoles(roles);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(profileEntitlementPO);
        profileEntitlementValidator.validate(transformationObject);
        ProfileEntitlementPO profileEntitlementPORet = (ProfileEntitlementPO) transformationObject.getSourcePojo();
        assertTrue(profileEntitlementPO.getEmailAddress().equals(profileEntitlementPORet.getEmailAddress()));
    }


  

    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSBusinessException.class)
    public void validate_For_Empty_UserName_Failure() throws Exception {

        profileEntitlementPO.setRoles(roles);
        profileEntitlementPO.setUserName(null);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(profileEntitlementPO);
        profileEntitlementValidator.validate(transformationObject);
    }

    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSBusinessException.class)
    public void validate_For_Empty_FirstName_Failure() throws Exception {

        profileEntitlementPO.setRoles(roles);
        profileEntitlementPO.setFirstName(null);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(profileEntitlementPO);
        profileEntitlementValidator.validate(transformationObject);
    }

    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSBusinessException.class)
    public void validate_For_Empty_LastName_Failure() throws Exception {

        profileEntitlementPO.setRoles(roles);
        profileEntitlementPO.setLastName(null);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(profileEntitlementPO);
        profileEntitlementValidator.validate(transformationObject);
    }

    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSBusinessException.class)
    public void validate_For_Empty_Role_Failure() throws Exception {

        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(profileEntitlementPO);
        profileEntitlementValidator.validate(transformationObject);
    }


    /**
     * 
     * Create Profile
     */
    private void createProfile() {

        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementPO.setUserName("username");
        profileEntitlementPO.setFirstName("FN");
        profileEntitlementPO.setLastName("LN");
        profileEntitlementPO.setEmailAddress("username@dummy.com");

        roles = new ArrayList<>();
        ProfileEntitlementRolePO role = new ProfileEntitlementRolePO();
        role.setName(EntitlementRole.LENDER_TRADE_EXECUTE);
        roles.add(role);
    }

  
}
