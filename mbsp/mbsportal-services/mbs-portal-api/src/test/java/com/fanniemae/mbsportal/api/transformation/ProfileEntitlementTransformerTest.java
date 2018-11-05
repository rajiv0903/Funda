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
 * @File: com.fanniemae.mbsportal.api.transformation.ProfileEntitlementTransformerTest.java
 * @Revision : 
 * @Description: ProfileEntitlementTransformerTest.java
 */
public class ProfileEntitlementTransformerTest {

    private ProfileEntitlementTransformer<TransformationObject> profileEntitlementTransformer;
    
    private ProfileEntitlementPO profileEntitlementPO;
    private ProfileEntitlementRolePO profileEntitlementRolePO;
    private MBSProfile mBSProfile;
    private MBSProfileRole mBSProfileRole;

    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Before
    public void setUp() throws Exception {
        
        profileEntitlementTransformer = new ProfileEntitlementTransformer<TransformationObject>();
    }
    
    /**
     * 
     * 
     * @throws Exception
     */
    @Test
    public void transform_Success() throws Exception {
        
        createProfile();
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(profileEntitlementPO);
        transformationObject.setTargetPojo(mBSProfile);
        profileEntitlementTransformer.transform(transformationObject);
        MBSProfile mBSProfileRet = (MBSProfile) transformationObject.getTargetPojo();
        assertTrue(mBSProfile.getUserName().equals(mBSProfileRet.getUserName()));
        assertTrue(mBSProfileRet.getRoles().size() == 1 );
        
    }
    
    @Test
    public void transform_Existing_Roles_With_Merge_Success() throws Exception {
        
        createProfile();
        TransformationObject transformationObject = new TransformationObject();
        List<ProfileEntitlementRolePO> roles = new ArrayList<>();
        profileEntitlementPO.setRoles(roles);
        transformationObject.setSourcePojo(profileEntitlementPO);
        transformationObject.setTargetPojo(mBSProfile);
        profileEntitlementTransformer.transform(transformationObject);
        MBSProfile mBSProfileRet = (MBSProfile) transformationObject.getTargetPojo();
        assertTrue(mBSProfile.getUserName().equals(mBSProfileRet.getUserName()));
        assertTrue(mBSProfileRet.getRoles().size() == 1 );
        
    }
    
    /*
     * CMMBSSTA01-1382: Automated way to clean up stale Role
     */
    @Test
    public void transform_no_merge() throws Exception {
        
        createProfile();
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(profileEntitlementPO);
        transformationObject.setTargetPojo(mBSProfile);
        transformationObject.setMergedProfile(false);
        profileEntitlementTransformer.transform(transformationObject);
        MBSProfile mBSProfileRet = (MBSProfile) transformationObject.getTargetPojo();
        assertTrue(mBSProfile.getUserName().equals(mBSProfileRet.getUserName()));
        assertTrue(mBSProfileRet.getRoles().size() == 1 );
        assertFalse(mBSProfileRet.isTspUser());
        
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
        profileEntitlementPO.setBrsUserName("brsUserName");
        
        List<ProfileEntitlementRolePO> roles = new ArrayList<>();
        profileEntitlementRolePO = new  ProfileEntitlementRolePO();
        profileEntitlementRolePO.setName(EntitlementRole.LENDER_TRADE_EXECUTE);
        roles.add(profileEntitlementRolePO);
        
        profileEntitlementPO.setRoles(roles);
        
        mBSProfile = new MBSProfile();
        mBSProfile.setUserName("UN");
        mBSProfile.setFirstName("FN");
        mBSProfile.setLastName("LN");
        
        List<MBSProfileRole> mbsRoles = new ArrayList<>();
        mBSProfileRole = new MBSProfileRole();
        mBSProfileRole.setName(EntitlementRole.LENDER_TRADE_EXECUTE);
        mbsRoles.add(mBSProfileRole);
        
        mBSProfile.setRoles(mbsRoles);
    }
    
}