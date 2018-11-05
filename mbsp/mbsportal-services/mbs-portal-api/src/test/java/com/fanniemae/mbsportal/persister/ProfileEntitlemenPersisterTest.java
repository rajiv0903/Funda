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

package com.fanniemae.mbsportal.persister;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.util.HashSet;
import java.util.Set;

import org.apache.geode.cache.Region;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.fnmpfj.gemfire.dao.BaseDaoImpl;
import com.fanniemae.mbsportal.api.persister.ProfileEntitlemenPersister;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.dao.MBSProfileDao;
import com.fanniemae.mbsportal.model.MBSProfile;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Mar 15, 2018
 * @File: com.fanniemae.mbsportal.persister.ProfileEntitlemenPersisterTest.java
 * @Revision : 
 * @Description: ProfileEntitlemenPersisterTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class ProfileEntitlemenPersisterTest extends BaseServiceTest{
    
    @InjectMocks
    ProfileEntitlemenPersister profileEntitlemenPersister;
    
    @Mock
    MBSProfileDao mBSProfileDao;
    
    @Mock
    Region<String, MBSProfile> region;
    
    
    MBSProfile mBSProfile;
    Set<String> keySets;
    
    @Before
    public void setUp() throws Exception {
        
        mBSProfile = new MBSProfile();
        keySets = new HashSet<>();
        keySets.add("test");
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testGetDao() throws MBSBaseException {
        
        BaseDaoImpl baseDaoImpl =  profileEntitlemenPersister.getDao();
        Assert.assertNull(baseDaoImpl);
    }
   
    
    @Test
    public void testGetBaseDao() throws MBSBaseException {
        
        MBSProfileDao mBSProfileDaoRet =  (MBSProfileDao) profileEntitlemenPersister.getBaseDao();
        Assert.assertNotNull(mBSProfileDaoRet);
    }
    
    
    @Test
    public void testPersist() throws MBSBaseException {
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSProfile);
        doNothing().when(mBSProfileDao).saveOrUpdate(any());
        profileEntitlemenPersister.persist(transformationObject);
    }
    
    @Test
    public void clearAllTest() throws MBSBaseException {
        
        doReturn(region).when(mBSProfileDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSProfile).when(region).destroy(any());
        profileEntitlemenPersister.clearAll();
    }
    
    /**
     * @author gaur5c
     * @throws MBSBaseException
     */
    @Test
    public void clear() throws MBSBaseException {
        
        doReturn(region).when(mBSProfileDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSProfile).when(region).destroy(any());
        profileEntitlemenPersister.clear("key");
    }

}
