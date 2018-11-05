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

package com.fanniemae.mbsportal.controller.helper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.api.cdx.CDXClientApi;
import com.fanniemae.mbsportal.api.controller.helper.PollingControllerHelper;
import com.fanniemae.mbsportal.api.po.PollingPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.service.PollingService;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;

/**
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Dec 4, 2017
 * @Time 3:08:01 PM com.fanniemae.mbsportal.api.controller.helper
 *       PollingControllerHelperTest.java
 * @Description:
 */
@RunWith(MockitoJUnitRunner.class)
public class PollingControllerHelperTest {

    @InjectMocks
    PollingControllerHelper pollingControllerHelper;

    @Mock
    PollingService pollingService;

    @Mock
    CDXClientApi cDXClientApi;

    private String reqDateTimestamp;
    private ProfileEntitlementPO profileEntitlementPO;

    private String currentServerTime;
    private PollingPO poolingPO;

    @Before
    public void setUp() throws Exception {

        reqDateTimestamp = MBSPortalUtils.getLocalZonedDateTimeFromEpochMilli(
                MBSPortalUtils.getLocalDateCurrentTimeStamp(), MBSPortalUtils.DATE_TIME_FORMAT_UUUU_MM_DD_HH_MM_SS);

        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementPO.setUserName("username");

        currentServerTime = MBSPortalUtils.getLocalZonedDateTimeFromEpochMilli(
                MBSPortalUtils.getLocalDateCurrentTimeStamp(), MBSPortalUtils.DATE_TIME_FORMAT_UUUU_MM_DD_HH_MM_SS);

        poolingPO = new PollingPO();
        poolingPO.setEventsAvailable(true);
        poolingPO.setServerTime(currentServerTime);
    }

    @Test
    public void lenderPollingRequestTest() throws MBSBaseException {

        doReturn(profileEntitlementPO).when(cDXClientApi).getProfileEntitlementPO(any(), any());
        doReturn(poolingPO).when(pollingService).getPoollingReq(any(), any(), any());

        Map<String, String> headers = new HashMap<>();
        PollingPO poolingPORet = pollingControllerHelper.lenderPollingRequest(Optional.of(reqDateTimestamp), headers);
        assertEquals(poolingPO.isEventsAvailable(), poolingPORet.isEventsAvailable());
    }

    @Test
    public void lenderPollingEmptyRequestTest() throws MBSBaseException {

        doReturn(profileEntitlementPO).when(cDXClientApi).getProfileEntitlementPO(any(), any());
        doReturn(poolingPO).when(pollingService).getPoollingReq(any(), any(), any());

        Map<String, String> headers = new HashMap<>();
        PollingPO poolingPORet = pollingControllerHelper.lenderPollingRequest(Optional.empty(), headers);
        assertEquals(poolingPO.isEventsAvailable(), poolingPORet.isEventsAvailable());
    }

    @Test(expected = MBSBusinessException.class)
    public void lenderPollingRequestWithoutProfileTest() throws MBSBaseException {

        doReturn(null).when(cDXClientApi).getProfileEntitlementPO(any(), any());
        doReturn(poolingPO).when(pollingService).getPoollingReq(any(), any(), any());

        Map<String, String> headers = new HashMap<>();
        PollingPO poolingPORet = pollingControllerHelper.lenderPollingRequest(Optional.of(reqDateTimestamp), headers);
        assertEquals(poolingPO.isEventsAvailable(), poolingPORet.isEventsAvailable());
    }

    @Test(expected = MBSBusinessException.class)
    public void lenderPollingRequestWithoutReqDateTimeTest() throws MBSBaseException {

        doReturn(profileEntitlementPO).when(cDXClientApi).getProfileEntitlementPO(any(), any());
        doReturn(poolingPO).when(pollingService).getPoollingReq(any(), any(), any());

        Map<String, String> headers = new HashMap<>();
        PollingPO poolingPORet = pollingControllerHelper.lenderPollingRequest(null, headers);
        assertEquals(poolingPO.isEventsAvailable(), poolingPORet.isEventsAvailable());
    }

    @Test
    public void traderPollingRequestTest() throws MBSBaseException {

        doReturn(poolingPO).when(pollingService).getPoollingReq(any(), any(), any());

        Map<String, String> headers = new HashMap<>();
        PollingPO poolingPORet = pollingControllerHelper.traderPollingRequest(Optional.of(reqDateTimestamp), headers);
        assertEquals(poolingPO.isEventsAvailable(), poolingPORet.isEventsAvailable());
    }

    @Test
    public void traderPollingEmptyRequestTest() throws MBSBaseException {

        doReturn(poolingPO).when(pollingService).getPoollingReq(any(), any(), any());

        Map<String, String> headers = new HashMap<>();
        PollingPO poolingPORet = pollingControllerHelper.traderPollingRequest(Optional.empty(), headers);
        assertEquals(poolingPO.isEventsAvailable(), poolingPORet.isEventsAvailable());
    }

    @Test(expected = MBSBusinessException.class)
    public void traderPollingRequestWithoutReqDateTimeTest() throws MBSBaseException {

        doReturn(poolingPO).when(pollingService).getPoollingReq(any(), any(), any());

        Map<String, String> headers = new HashMap<>();
        PollingPO poolingPORet = pollingControllerHelper.traderPollingRequest(null, headers);
        assertEquals(poolingPO.isEventsAvailable(), poolingPORet.isEventsAvailable());
    }

    @Test
    public void clearAllEvents() throws MBSBaseException {

        doNothing().when(pollingService).clearAll();
        pollingControllerHelper.clearAllEvents();
    }

    @Test(expected = MBSBusinessException.class)
    public void clearAllEventsThrowsException() throws MBSBaseException {

        doThrow(MBSBusinessException.class).when(pollingService).clearAll();
        pollingControllerHelper.clearAllEvents();
    }
}
