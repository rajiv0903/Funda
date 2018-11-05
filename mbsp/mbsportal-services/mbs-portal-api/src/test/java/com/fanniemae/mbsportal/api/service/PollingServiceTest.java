/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */


package com.fanniemae.mbsportal.api.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.geode.cache.Region;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.api.po.PollingPO;
import com.fanniemae.mbsportal.api.service.PollingService;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.dao.MBSEventDao;
import com.fanniemae.mbsportal.model.MBSEvent;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Nov 22, 2017
 * @Time 10:58:22 AM com.fanniemae.mbsportal.api.service PollingServiceTest.java
 * @Description:
 */
@RunWith(MockitoJUnitRunner.class)
public class PollingServiceTest extends BaseServiceTest {

    @Mock
    Region<String, MBSEvent> region;

    @Mock
    MBSEventDao mBSEventDao;

    @InjectMocks
    private PollingService pollingService;

    private MBSEvent mBSEvent;
    private Set<String> keySets;

    private MBSRoleType roleTypeTrader;
    private MBSRoleType roleTypeLender;
    private String userName;
    private String reqDateTimestamp;
    boolean eventsAvalable;

    @Before
    public void setUp() throws Exception {

        keySets = new HashSet<>();
        keySets.add(TradeConstants.TRADE_EVENT_USRE_NAME);

        userName = TradeConstants.TRADE_EVENT_USRE_NAME;
        reqDateTimestamp = MBSPortalUtils.getLocalZonedDateTimeFromEpochMilli(
                MBSPortalUtils.getLocalDateCurrentTimeStamp(), MBSPortalUtils.DATE_TIME_FORMAT_UUUU_MM_DD_HH_MM_SS);
        roleTypeTrader = MBSRoleType.TRADER;
        roleTypeLender = MBSRoleType.LENDER;

        eventsAvalable = true;

        mBSEvent = new MBSEvent();
        mBSEvent.setUserName(userName);
        mBSEvent.setEventTimeStamp(MBSPortalUtils.getLocalDateCurrentTimeStamp());

    }

    @Test
    public void getPoollingReqTraderEventsAvailableForFirstTimeTest() throws MBSBaseException {

        mBSEvent.setEventTimeStamp(MBSPortalUtils.getLocalDateCurrentTimeStamp() + 1000);

        doReturn(mBSEvent).when(mBSEventDao).getEvent(any());
        PollingPO pollingPORet = pollingService.getPoollingReq(userName, Optional.empty(), roleTypeTrader);
        assertEquals(true, pollingPORet.isEventsAvailable());
    }

    @Test
    public void getPoollingReqTraderEventsAvailableTest() throws MBSBaseException {

        mBSEvent.setEventTimeStamp(MBSPortalUtils.getLocalDateCurrentTimeStamp() + 1000);

        doReturn(mBSEvent).when(mBSEventDao).getEvent(any());
        PollingPO pollingPORet = pollingService.getPoollingReq(userName, Optional.of(reqDateTimestamp), roleTypeTrader);
        assertEquals(true, pollingPORet.isEventsAvailable());
    }

    @Test
    public void getPoollingReqTraderNoEventsAvailableTest() throws MBSBaseException {

        mBSEvent.setEventTimeStamp(MBSPortalUtils.getLocalDateCurrentTimeStamp() - 1000);

        doReturn(mBSEvent).when(mBSEventDao).getEvent(any());
        PollingPO pollingPORet = pollingService.getPoollingReq(userName, Optional.of(reqDateTimestamp), roleTypeTrader);
        assertEquals(false, pollingPORet.isEventsAvailable());
    }

    @Test
    public void getPoollingReqTraderNoEventsAvailableAtGemfireTest() throws MBSBaseException {

        mBSEvent.setEventTimeStamp(MBSPortalUtils.getLocalDateCurrentTimeStamp() - 1000);

        doReturn(null).when(mBSEventDao).getEvent(any());
        PollingPO pollingPORet = pollingService.getPoollingReq(userName, Optional.of(reqDateTimestamp), roleTypeTrader);
        assertEquals(false, pollingPORet.isEventsAvailable());
    }

    @Test(expected = MBSBaseException.class)
    public void getPoollingReqTraderInvalidDateTest() throws MBSBaseException {

        mBSEvent.setEventTimeStamp(MBSPortalUtils.getLocalDateCurrentTimeStamp() - 1000);

        doReturn(null).when(mBSEventDao).getEvent(any());
        PollingPO pollingPORet = pollingService.getPoollingReq(userName, Optional.of("201-1-1:01:11:22"),
                roleTypeTrader);
        assertEquals(false, pollingPORet.isEventsAvailable());
    }

    @Test
    public void getPoollingReqLenderEventsAvailableForFirstTimeTest() throws MBSBaseException {

        mBSEvent.setEventTimeStamp(MBSPortalUtils.getLocalDateCurrentTimeStamp() + 1000);

        doReturn(mBSEvent).when(mBSEventDao).getEvent(any());
        PollingPO pollingPORet = pollingService.getPoollingReq(userName, Optional.empty(), roleTypeLender);
        assertEquals(true, pollingPORet.isEventsAvailable());
    }

    @Test
    public void getPoollingReqLenderEventsAvailableTest() throws MBSBaseException {

        mBSEvent.setEventTimeStamp(MBSPortalUtils.getLocalDateCurrentTimeStamp() + 1000);

        doReturn(mBSEvent).when(mBSEventDao).getEvent(any());
        PollingPO pollingPORet = pollingService.getPoollingReq(userName, Optional.of(reqDateTimestamp), roleTypeLender);
        assertEquals(true, pollingPORet.isEventsAvailable());
    }

    @Test
    public void getPoollingReqLenderNoEventsAvailableTest() throws MBSBaseException {

        mBSEvent.setEventTimeStamp(MBSPortalUtils.getLocalDateCurrentTimeStamp() - 1000);

        doReturn(mBSEvent).when(mBSEventDao).getEvent(any());
        PollingPO pollingPORet = pollingService.getPoollingReq(userName, Optional.of(reqDateTimestamp), roleTypeLender);
        assertEquals(false, pollingPORet.isEventsAvailable());
    }

    @Test
    public void getPoollingReqLenderNoEventsAvailableAtGemfireTest() throws MBSBaseException {

        mBSEvent.setEventTimeStamp(MBSPortalUtils.getLocalDateCurrentTimeStamp() - 1000);

        doReturn(null).when(mBSEventDao).getEvent(any());
        PollingPO pollingPORet = pollingService.getPoollingReq(userName, Optional.of(reqDateTimestamp), roleTypeLender);
        assertEquals(false, pollingPORet.isEventsAvailable());
    }

    @Test(expected = MBSBaseException.class)
    public void getPoollingReqLenderInvalidDateTest() throws MBSBaseException {

        mBSEvent.setEventTimeStamp(MBSPortalUtils.getLocalDateCurrentTimeStamp() - 1000);

        doReturn(null).when(mBSEventDao).getEvent(any());
        PollingPO pollingPORet = pollingService.getPoollingReq(userName, Optional.of("201-1-1:01:11:22"),
                roleTypeLender);
        assertEquals(false, pollingPORet.isEventsAvailable());
    }

    @Test
    public void clearAllTest() throws MBSBaseException {

        doReturn(region).when(mBSEventDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSEvent).when(region).destroy(any());
        pollingService.clearAll();

    }
}
