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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import com.fanniemae.mbsportal.api.controller.TradeServiceProxy;
import com.fanniemae.mbsportal.api.service.ExceptionLookupService;
import com.fanniemae.mbsportal.api.service.TradeServicePoller;
import com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author g8upjv
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TradeServiceProxyTest {

    @InjectMocks
    TradeServiceProxy tradeServiceProxy;

    @Mock
    TradeServicePoller tradeServicePoller;
    
    @Mock
    ExceptionLookupService exceptionLookupService;
    
    @Mock
    Logger LOGGER;

    /**
     * Purpose: Creates the productPO data model
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        Mockito.when(exceptionLookupService.getExceptionLookupData(Mockito.any())).thenReturn(createExceptionData());
    }

    /**
     * Purpose: Test for case when media type is empty
     * 
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test
    public void testProxyPostToTS() throws MBSBaseException {

        Mockito.when(tradeServicePoller.disptachAndUpdateStatus(Mockito.any(), Mockito.any())).thenReturn(true);
        MultiValueMap<String, String> headers = new HttpHeaders();
        List<String> token = new ArrayList<>();
        token.add("test");
        token.add("token");
        headers.put(CDXHeaderMap.JWS_TOKEN.getValue(), token);
        String body = "18C00001, 18C00002";
        ResponseEntity<Object> responseObj = tradeServiceProxy.proxyPostToTradeService(body, headers, HttpMethod.GET);
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
    }
    
    /**
     * Purpose: Test for case when media type is empty
     * 
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test
    public void testProxyPostToTS_EmptyToken() throws MBSBaseException {

        Mockito.when(tradeServicePoller.disptachAndUpdateStatus(Mockito.any(), Mockito.any())).thenReturn(true);
        MultiValueMap<String, String> headers = new HttpHeaders();
        List<String> token = new ArrayList<>();
        headers.put(CDXHeaderMap.JWS_TOKEN.getValue(), token);
        String body = "18C00001, 18C00002";
        ResponseEntity<Object> responseObj = tradeServiceProxy.proxyPostToTradeService(body, headers, HttpMethod.GET);
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }
    
    @Test
    public void testProxyPostToTS_EmptyBody() throws MBSBaseException {

        Mockito.when(tradeServicePoller.disptachAndUpdateStatus(Mockito.any(), Mockito.any())).thenReturn(true);
        MultiValueMap<String, String> headers = new HttpHeaders();
        List<String> token = new ArrayList<>();
        token.add("test");
        token.add("token");
        headers.put(CDXHeaderMap.JWS_TOKEN.getValue(), token);
        String body = "";
        ResponseEntity<Object> responseObj = tradeServiceProxy.proxyPostToTradeService(body, headers, HttpMethod.GET);
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }
    
    @Test
    public void testProxyPostToTS_PollerReturnsFalse() throws MBSBaseException {

        Mockito.when(tradeServicePoller.disptachAndUpdateStatus(Mockito.any(), Mockito.any())).thenReturn(false);
        MultiValueMap<String, String> headers = new HttpHeaders();
        List<String> token = new ArrayList<>();
        token.add("test");
        token.add("token");
        headers.put(CDXHeaderMap.JWS_TOKEN.getValue(), token);
        String body = "[]";
        ResponseEntity<Object> responseObj = tradeServiceProxy.proxyPostToTradeService(body, headers, HttpMethod.GET);
        assertTrue(responseObj.getStatusCode().toString().equals("207"));
    }
    
    @Test
    public void testProxyPostToTS_Exception() throws MBSBaseException {

        Mockito.when(tradeServicePoller.disptachAndUpdateStatus(Mockito.any(), Mockito.any())).thenThrow(Exception.class);
        MultiValueMap<String, String> headers = new HttpHeaders();
        List<String> token = new ArrayList<>();
        token.add("test");
        token.add("token");
        headers.put(CDXHeaderMap.JWS_TOKEN.getValue(), token);
        String body = "18C00001, 18C00002";
        ResponseEntity<Object> responseObj = tradeServiceProxy.proxyPostToTradeService(body, headers, HttpMethod.GET);
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }
    
    @Test
    public void testProxyPostToTS_ExceptionMessageRetrieval() throws MBSBaseException {

        Mockito.when(tradeServicePoller.disptachAndUpdateStatus(Mockito.any(), Mockito.any())).thenThrow(Exception.class);
        Mockito.when(exceptionLookupService.getExceptionLookupData(Mockito.any())).thenThrow(new MBSBaseException("test"));
        MultiValueMap<String, String> headers = new HttpHeaders();
        List<String> token = new ArrayList<>();
        token.add("test");
        token.add("token");
        headers.put(CDXHeaderMap.JWS_TOKEN.getValue(), token);
        String body = "18C00001, 18C00002";
        ResponseEntity<Object> responseObj = tradeServiceProxy.proxyPostToTradeService(body, headers, HttpMethod.GET);
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }
    
    @Test
    public void testTradeResponse() throws MBSBaseException {

        MultiValueMap<String, String> headers = new HttpHeaders();
        List<String> token = new ArrayList<>();
        token.add("test");
        token.add("token");
        headers.put(CDXHeaderMap.JWS_TOKEN.getValue(), token);
        String body = "18C00001, 18C00002";
        ResponseEntity<Object> responseObj = tradeServiceProxy.traderesponse(body, headers, HttpMethod.GET);
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
    }
    
    @Test
    public void testTradeResponse_EmptyToken() throws MBSBaseException {

        MultiValueMap<String, String> headers = new HttpHeaders();
        List<String> token = new ArrayList<>();
        headers.put(CDXHeaderMap.JWS_TOKEN.getValue(), token);
        String body = "18C00001, 18C00002";
        ResponseEntity<Object> responseObj = tradeServiceProxy.traderesponse(body, headers, HttpMethod.GET);
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }
    
    public List<ExceptionLookupPO> createExceptionData() {
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
