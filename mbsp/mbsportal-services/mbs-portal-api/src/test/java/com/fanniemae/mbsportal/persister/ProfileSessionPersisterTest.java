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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.util.HashSet;
import java.util.Set;

import org.apache.geode.cache.Region;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.fanniemae.fnmpfj.gemfire.dao.BaseDaoImpl;
import com.fanniemae.mbsportal.api.persister.ProfileSessionPersister;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.dao.MBSBaseDao;
import com.fanniemae.mbsportal.dao.MBSProfileSessionDao;
import com.fanniemae.mbsportal.model.MBSProfileSession;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Mar 15, 2018
 * @File: com.fanniemae.mbsportal.persister.ProfileSessionPersisterTest.java
 * @Revision :
 * @Description: ProfileSessionPersisterTest.java
 */
public class ProfileSessionPersisterTest extends BaseServiceTest {

    @InjectMocks
    ProfileSessionPersister profileSessionPersister;

    @Mock
    MBSProfileSessionDao mBSProfileSessionDao;

    @Mock
    Region<String, MBSProfileSession> region;

    MBSProfileSession mBSProfileSession;
    Set<String> keySets;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        mBSProfileSession = new MBSProfileSession();
        keySets = new HashSet<>();
        keySets.add("sessionid-1");
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void persistTest() throws MBSBaseException {

        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSProfileSession);
        doNothing().when(mBSProfileSessionDao).saveOrUpdate(any());
        profileSessionPersister.persist(transformationObject);
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void clearAllTest() throws MBSBaseException {

        doReturn(region).when(mBSProfileSessionDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSProfileSession).when(region).destroy(any());
        profileSessionPersister.clearAll();
    }
    
    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void clear() throws MBSBaseException {

        doReturn(region).when(mBSProfileSessionDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSProfileSession).when(region).destroy(any());
        profileSessionPersister.clear("key");
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void getDaoTest() throws MBSBaseException {
        BaseDaoImpl<MBSProfileSession> baseDao = profileSessionPersister.getDao();
        assertTrue(baseDao instanceof MBSProfileSessionDao);

    }

    /**
     * 
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void getBaseDaoTest() throws MBSBaseException {
        MBSBaseDao<MBSProfileSession> baseDao = profileSessionPersister.getBaseDao();
        assertTrue(baseDao == null);
    }
}
