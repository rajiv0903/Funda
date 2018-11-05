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

package com.fanniemae.mbsportal.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.fanniemae.mbsportal.api.controller.PollingController;
import com.fanniemae.mbsportal.api.controller.helper.PollingControllerHelper;
import com.fanniemae.mbsportal.api.po.PollingPO;
import com.fanniemae.mbsportal.api.service.ExceptionLookupService;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;

/**
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Dec 4, 2017
 * @Time 3:02:46 PM com.fanniemae.mbsportal.api.controller
 *       PollingControllerTest.java
 * @Description:
 */
@RunWith(MockitoJUnitRunner.class)
public class PollingControllerTest {

    @InjectMocks
    PollingController pollingController;

    @Mock
    PollingControllerHelper pollingControllerHelper;
    
    @Mock
    ExceptionLookupService exceptionLookupService;

    private String reqDateTimestamp;
    private String currentServerTime;
    private PollingPO poolingPO;

    @Before
    public void setUp() throws Exception {

        reqDateTimestamp = MBSPortalUtils.getLocalZonedDateTimeFromEpochMilli(
                MBSPortalUtils.getLocalDateCurrentTimeStamp(), MBSPortalUtils.DATE_TIME_FORMAT_UUUU_MM_DD_HH_MM_SS);

        currentServerTime = MBSPortalUtils.getLocalZonedDateTimeFromEpochMilli(
                MBSPortalUtils.getLocalDateCurrentTimeStamp(), MBSPortalUtils.DATE_TIME_FORMAT_UUUU_MM_DD_HH_MM_SS);
        Mockito.when(exceptionLookupService.getExceptionLookupData(Mockito.any())).thenReturn(createData());
        poolingPO = new PollingPO();
        poolingPO.setEventsAvailable(true);
        poolingPO.setServerTime(currentServerTime);
    }

    @Test
    public void lenderPollingRequestTest() throws MBSBaseException {

        Mockito.when(pollingControllerHelper.lenderPollingRequest(Mockito.any(), Mockito.any())).thenReturn(poolingPO);
        Map<String, String> headers = new HashMap<>();
        ResponseEntity<Object> responseObj = pollingController.lenderPollingRequest(Optional.of(reqDateTimestamp),
                headers);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
    }

    @Test
    public void lenderPollingEmptyRequestTime() throws MBSBaseException {

        Mockito.when(pollingControllerHelper.lenderPollingRequest(Mockito.any(), Mockito.any())).thenReturn(poolingPO);
        Map<String, String> headers = new HashMap<>();
        ResponseEntity<Object> responseObj = pollingController.lenderPollingRequest(Optional.empty(), headers);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
    }

    @Test
    public void lenderPollingRequestMBSBaseExceptionTest() throws MBSBaseException {

        Mockito.when(pollingControllerHelper.lenderPollingRequest(Mockito.any(), Mockito.any()))
                .thenThrow(new MBSBusinessException("Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
        Mockito.when(exceptionLookupService.getExceptionLookupData(Mockito.any())).thenThrow(new MBSBaseException("test"));
        Map<String, String> headers = new HashMap<>();
        ResponseEntity<Object> responseObj = pollingController.lenderPollingRequest(Optional.of(reqDateTimestamp),
                headers);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("400"));
    }

    @Test
    public void lenderPollingRequesExceptionTest() throws MBSBaseException {

        Mockito.when(pollingControllerHelper.lenderPollingRequest(Mockito.any(), Mockito.any()))
                .thenThrow(new RuntimeException("Runtine Exception!"));
        Map<String, String> headers = new HashMap<>();
        ResponseEntity<Object> responseObj = pollingController.lenderPollingRequest(Optional.of(reqDateTimestamp),
                headers);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }
    
    @Test
    public void lenderPollingRequesExceptionTestRetrieveError() throws MBSBaseException {

        Mockito.when(pollingControllerHelper.lenderPollingRequest(Mockito.any(), Mockito.any()))
                .thenThrow(new RuntimeException("Runtine Exception!"));
        Map<String, String> headers = new HashMap<>();
        ResponseEntity<Object> responseObj = pollingController.lenderPollingRequest(Optional.of(reqDateTimestamp),
                headers);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }

    @Test
    public void traderPollingRequestTest() throws MBSBaseException {

        Mockito.when(pollingControllerHelper.traderPollingRequest(Mockito.any(), Mockito.any())).thenReturn(poolingPO);
        Map<String, String> headers = new HashMap<>();
        ResponseEntity<Object> responseObj = pollingController.traderPollingRequest(Optional.of(reqDateTimestamp),
                headers);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
    }

    @Test
    public void traderPollingEmptyRequestTime() throws MBSBaseException {

        Mockito.when(pollingControllerHelper.traderPollingRequest(Mockito.any(), Mockito.any())).thenReturn(poolingPO);
        Map<String, String> headers = new HashMap<>();
        ResponseEntity<Object> responseObj = pollingController.traderPollingRequest(Optional.empty(), headers);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
    }

    @Test
    public void traderPollingRequestMBSBaseExceptionTest() throws MBSBaseException {

        Mockito.when(pollingControllerHelper.traderPollingRequest(Mockito.any(), Mockito.any()))
                .thenThrow(new MBSBusinessException("Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
        Map<String, String> headers = new HashMap<>();
        ResponseEntity<Object> responseObj = pollingController.traderPollingRequest(Optional.of(reqDateTimestamp),
                headers);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("400"));
    }

    @Test
    public void traderPollingRequestExceptionTest() throws MBSBaseException {

        Mockito.when(pollingControllerHelper.traderPollingRequest(Mockito.any(), Mockito.any()))
                .thenThrow(new RuntimeException("Runtine Exception!"));
        Map<String, String> headers = new HashMap<>();
        ResponseEntity<Object> responseObj = pollingController.traderPollingRequest(Optional.of(reqDateTimestamp),
                headers);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }

    @Test
    public void clearAllEventsTest() throws MBSBaseException {

        doNothing().when(pollingControllerHelper).clearAllEvents();
        pollingController.clearAllEvents();
    }
    
    public List<ExceptionLookupPO> createData() {
        List<ExceptionLookupPO> exceptionLookupPOLst = new ArrayList<>();
        ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        exceptionLookupPO.setErrorCategory("API");
        exceptionLookupPO.setErrorCode("TRANS_00001");
        exceptionLookupPO.setMessageType("DISP_ERROR");
        exceptionLookupPO.setErrorMessage("Test Message");
        exceptionLookupPOLst.add(exceptionLookupPO);
        return exceptionLookupPOLst;
    }
}
