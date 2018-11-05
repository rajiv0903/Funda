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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.fanniemae.mbsportal.model.MBSEvent;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Nov 21, 2017
 * @Time 12:11:26 PM com.fanniemae.mbsportal.dao MBSEventDaoTest.java
 * @Description:
 */
public class MBSEventDaoTest extends BaseDaoTest {

    private static String regionName = "MBSEvent";

    @Autowired
    private MBSEventDao mBSEventDao;

    @Mock
    MBSBaseDao<MBSEvent> mBSBaseDao;

    MBSEvent mBSEvent;
    String userName;
    long eventTimeStamp;

    @Before
    public void setUp() throws Exception {

        eventTimeStamp = System.currentTimeMillis() + 10 * 1000;
        userName = TradeConstants.TRADE_EVENT_USRE_NAME;
        mBSEvent = new MBSEvent(userName, eventTimeStamp);
        mBSEvent.setEventTimeStamp(eventTimeStamp);

        when(mockRegion.getRegionService()).thenReturn(mockRegionService);
        when(mockRegion.getName()).thenReturn(regionName);
        when(mockRegionService.getRegion(regionName)).thenReturn(mockRegion);

    }
    
    @Test
    @Ignore
    public void getStorageRegion() throws MBSBaseException {
            assertNotNull(mBSEventDao.getStorageRegion());
    }

    @Test
    public void saveOrUpdate() throws MBSBaseException {
        try {
            mBSEventDao.saveOrUpdate(mBSEvent);
            assertEquals(userName, mBSEvent.getUserName());

        } catch (Exception exe) {
            fail("Should not have thrown any error");
        }
    }
    
    @Test
    public void saveOrUpdate_Not_Passing_TimeStamp() throws MBSBaseException {
        mBSEvent.setEventTimeStamp(null);
        mBSEventDao.saveOrUpdate(mBSEvent);
        assertEquals(userName, mBSEvent.getUserName());
    }

    @Test
    public void saveOrUpdate_Empty_UserName() throws MBSBaseException {

        mBSEvent.setUserName(null);
        mBSEventDao.saveOrUpdate(mBSEvent);
        assertNull(mBSEvent.getUserName());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void eventsAvailableTrader() throws MBSBaseException {

        mBSEvent.setEventTimeStamp(eventTimeStamp);
        doReturn(mBSEvent).when((BaseDaoWrapper) baseDaoWrapper).getById(Mockito.any());
        MBSEvent mBSEventRet = mBSEventDao.getEvent(userName);
        assertTrue(mBSEventRet.getUserName().equals(mBSEvent.getUserName()));
        assertTrue(mBSEventRet.getEventTimeStamp() == mBSEvent.getEventTimeStamp());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void eventsAvailableLender() throws MBSBaseException {

        mBSEvent.setEventTimeStamp(eventTimeStamp);
        doReturn(mBSEvent).when((BaseDaoWrapper) baseDaoWrapper).getById(Mockito.any());
        MBSEvent mBSEventRet = mBSEventDao.getEvent(userName);
        assertTrue(mBSEventRet.getUserName().equals(mBSEvent.getUserName()));
        assertTrue(mBSEventRet.getEventTimeStamp() == mBSEvent.getEventTimeStamp());
    }




}
