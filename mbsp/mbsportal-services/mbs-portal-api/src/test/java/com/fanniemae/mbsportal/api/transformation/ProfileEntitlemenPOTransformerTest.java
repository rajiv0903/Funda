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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementRolePO;
import com.fanniemae.mbsportal.model.MBSProfile;
import com.fanniemae.mbsportal.model.MBSProfileRole;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Apr 6, 2018
 * @File: com.fanniemae.mbsportal.api.transformation.ProfileEntitlemenPOTransformerTest.java
 * @Revision : 
 * @Description: ProfileEntitlemenPOTransformerTest.java
 */

public class ProfileEntitlemenPOTransformerTest {

    private ProfileEntitlemenPOTransformer<TransformationObject> profileEntitlemenPOTransformer;

    private ProfileEntitlementPO profileEntitlementPO;
    private ProfileEntitlementRolePO profileEntitlementRolePO;
    private MBSProfile mBSProfile;
    private MBSProfileRole mBSProfileRole;


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {

        profileEntitlemenPOTransformer = new ProfileEntitlemenPOTransformer<TransformationObject>();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void transform_Success() throws Exception {
        
        createProfile();
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSProfile);
        profileEntitlemenPOTransformer.transform(transformationObject);
        ProfileEntitlementPO profileEntitlementPORet = (ProfileEntitlementPO) transformationObject.getSourcePojo();
        assertTrue(profileEntitlementPO.getUserName().equals(profileEntitlementPORet.getUserName()));
        assertTrue(profileEntitlementPO.getRoles().get(0).getName().equals(
                profileEntitlementPORet.getRoles().get(0).getName()));
        assertFalse(profileEntitlementPORet.isTspUser());
    }
    
 
    /**
     * 
     * Create the Profile
     */
    private void createProfile(){
        
        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementPO.setUserName("UN");
        profileEntitlementPO.setFirstName("FN");
        profileEntitlementPO.setLastName("LN");

        List<ProfileEntitlementRolePO> roles = new ArrayList<>();
        profileEntitlementRolePO = new ProfileEntitlementRolePO();
        profileEntitlementRolePO.setName(EntitlementRole.MBSP_FM_TRADER_EXECUTE_LE);
        roles.add(profileEntitlementRolePO);

        profileEntitlementPO.setRoles(roles);

        mBSProfile = new MBSProfile();
        mBSProfile.setUserName("UN");
        mBSProfile.setFirstName("FN");
        mBSProfile.setLastName("LN");

        List<MBSProfileRole> mbsRoles = new ArrayList<>();
        mBSProfileRole = new MBSProfileRole();
        mBSProfileRole.setName(EntitlementRole.MBSP_FM_TRADER_EXECUTE_LE);
        mbsRoles.add(mBSProfileRole);

        mBSProfile.setRoles(mbsRoles);
    }
    
   
}
