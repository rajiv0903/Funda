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

package com.fanniemae.mbsportal.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.geode.cache.query.SelectResults;
import org.apache.geode.cache.query.internal.ResultsSet;
import org.apache.geode.pdx.PdxInstance;
import org.apache.geode.pdx.PdxInstanceFactory;
import org.apache.geode.pdx.internal.PdxInstanceFactoryImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.fanniemae.mbsportal.model.MBSProfile;
import com.fanniemae.mbsportal.model.MBSProfileRole;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Dec 11, 2017
 * @Time 4:14:29 PM com.fanniemae.mbsportal.dao MBSProfileDaoTest.java
 * @Description: Revision 1.1 - Avoid direct hitting the Gemfire for query
 */
public class MBSProfileDaoTest extends BaseDaoTest {

    private static String regionName = "MBSProfile";

    @Autowired
    private MBSProfileDao mBSProfileDao;

    @Mock
    MBSBaseDao<MBSProfile> mBSBaseDao;

    MBSProfile mBSProfileMock;
    MBSProfileRole mBSProfileRoleMock;
    String userName;
    String[] brsUserNames = new String[] {"w8mmbstr"};

    @Before
    public void setUp() throws Exception {

        when(mockRegion.getRegionService()).thenReturn(mockRegionService);
        when(mockRegion.getName()).thenReturn(regionName);
        when(mockRegionService.getRegion(regionName)).thenReturn(mockRegion);

        userName = "t3user";
        // MBS Profile Role Mock
        mBSProfileRoleMock = new MBSProfileRole();
        mBSProfileRoleMock.setName("FM Trader - Execute");
        // MBS Profile Mock
        mBSProfileMock = new MBSProfile();
        mBSProfileMock.setUserName(userName);
        mBSProfileMock.setFirstName("fn");
        mBSProfileMock.setLastName("ln");
        mBSProfileMock.setEmailAddress("test@gmail.com");
        List<MBSProfileRole> mbsRoles = new ArrayList<>();
        mbsRoles.add(mBSProfileRoleMock);
        mBSProfileMock.setRoles(mbsRoles);
    }

    @Test
    public void saveOrUpdate() throws MBSBaseException {

        try {

            mBSProfileDao.saveOrUpdate(mBSProfileMock);
            assertEquals(userName, mBSProfileMock.getUserName());

        } catch (Exception exe) {
            fail("Should not have thrown any error");
        }
    }

    @Test
    public void getProfile() throws MBSBaseException {
        MBSProfile mBSProfileActual = mBSProfileDao.getProfile(userName);
        assertNull(mBSProfileActual);
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void getMBSProfile() throws MBSBaseException {
        SelectResults selectResults = new ResultsSet();
        selectResults.add(mBSProfileMock);
        when(((BaseDaoWrapper) baseDaoWrapper).query(any(), any())).thenReturn(selectResults);
        List<MBSProfile> results = mBSProfileDao.getMBSProfile(userName);
        assertEquals(userName, results.get(0).getUserName());
    }
    
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Ignore
    public void getMBSProfile_PdxInstance() throws MBSBaseException {
        
        PdxInstanceFactory pdxInstanceFactory = PdxInstanceFactoryImpl.newCreator("MBSProfile", true);
        pdxInstanceFactory.writeString("userName", userName);
        PdxInstance pdxInstance = pdxInstanceFactory.create();
        
        SelectResults selectResults = new ResultsSet();
        selectResults.add(pdxInstance);
        when(((BaseDaoWrapper) baseDaoWrapper).query(any(), any())).thenReturn(selectResults);
        List<MBSProfile> results = mBSProfileDao.getMBSProfile(userName);
        Assert.assertNotNull(results);
    }
    
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void getProfileByBRSNames() throws MBSBaseException {
    	SelectResults selectResults = new ResultsSet();
    	selectResults.add(mBSProfileMock);
    	when(((BaseDaoWrapper) baseDaoWrapper).query(any())).thenReturn(selectResults);
        List<MBSProfile> results = mBSProfileDao.getProfileByBRSNames(brsUserNames);
        assertEquals(userName, results.get(0).getUserName());
    }
    
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void getUserNamesForBRSNames() throws MBSBaseException {
    	SelectResults selectResults = new ResultsSet();
    	selectResults.add(mBSProfileMock);
    	when(((BaseDaoWrapper) baseDaoWrapper).query(any())).thenReturn(selectResults);
        List<String> results = mBSProfileDao.getUserNamesForBRSNames(brsUserNames);
        assertEquals(userName, results.get(0));
    }
    
    
    
}
