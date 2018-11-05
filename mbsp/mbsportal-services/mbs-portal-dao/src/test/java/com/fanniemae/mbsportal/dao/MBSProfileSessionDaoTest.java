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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.fnmpfj.gemfire.dao.BaseDaoImpl;
import com.fanniemae.mbsportal.model.MBSProfileSession;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Mar 14, 2018
 * @File: com.fanniemae.mbsportal.dao.MBSProfileSessionDaoTest.java
 * @Revision : 
 * @Description: MBSProfileSessionDaoTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class MBSProfileSessionDaoTest {

    @Mock
    private RegionService mockRegionService;
    @Mock
    private Region<Object, Object> mockRegion;
    @Spy
    MBSProfileSessionDao mBSProfileSessionDaoSpy;

    MBSProfileSession mbsProfileSessionMock;
    String cdxSessionId;
    String userName;

    /**
     * Set Up the Initial Mock
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        when(mockRegion.getRegionService()).thenReturn(mockRegionService);
        when(mockRegion.getName()).thenReturn("MBSSession");
        when(mockRegionService.getRegion("MBSSession")).thenReturn(mockRegion);
        when(mBSProfileSessionDaoSpy.getCache()).thenReturn(mockRegionService);
        mBSProfileSessionDaoSpy.setCache(mockRegionService);
        mBSProfileSessionDaoSpy.setRegionName("MBSSession");

        cdxSessionId = "12345";
        userName = "t3user";

        // Profile Session Mock
        mbsProfileSessionMock = new MBSProfileSession();
        mbsProfileSessionMock.setUserName(userName);
        mbsProfileSessionMock.setSessionId(cdxSessionId);
    }

    /**
     * Test for Successful Profile Session
     */
    @Test
    public void getProfileSession_Success() {
        doReturn(mbsProfileSessionMock).when(mBSProfileSessionDaoSpy).getProfileSession(cdxSessionId);
        MBSProfileSession mBSProfileSessionActual = mBSProfileSessionDaoSpy.getProfileSession(cdxSessionId);
        Assert.assertEquals(mbsProfileSessionMock.getUserName(), mBSProfileSessionActual.getUserName());
    }

    /**
     * Test for Successful Profile Save Or Update
     */
    @Test
    public void saveOrUpdate_Success() {
        try {

            doNothing().when((BaseDaoImpl<MBSProfileSession>) mBSProfileSessionDaoSpy)
                    .saveOrUpdate(mbsProfileSessionMock);
            mBSProfileSessionDaoSpy.saveOrUpdate(mbsProfileSessionMock);

        } catch (Exception e) {

            Assert.fail("Exception " + e);
        }
    }
    
    /**
     * Test for Successful Profile Save Or Update
     */
    @Test
    public void saveOrUpdate_Empty_User_ID_Success() {
        try {
            mbsProfileSessionMock.setUserName(null);
            doNothing().when((BaseDaoImpl<MBSProfileSession>) mBSProfileSessionDaoSpy)
                    .saveOrUpdate(mbsProfileSessionMock);
            mBSProfileSessionDaoSpy.saveOrUpdate(mbsProfileSessionMock);

        } catch (Exception e) {

            Assert.fail("Exception " + e);
        }
    }
}
