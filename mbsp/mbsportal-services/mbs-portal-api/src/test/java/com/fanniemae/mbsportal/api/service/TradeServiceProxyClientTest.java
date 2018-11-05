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

package com.fanniemae.mbsportal.api.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.fanniemae.mbsportal.api.config.TradeServiceProperties;
import com.fanniemae.mbsportal.cdx.config.CDXApiClientConfig;
import com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap;
import com.fanniemae.mbsportal.cdx.template.GatewayProxyTemplate;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jun 21, 2018
 * @File: com.fanniemae.mbsportal.api.service.TradeServiceProxyClientTest.java 
 * @Revision: 
 * @Description: TradeServiceProxyClientTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class TradeServiceProxyClientTest {

    @Mock
    private TradeServiceProperties tradeServiceProperties;
    @Mock
    private GatewayProxyTemplate gatewayProxyTemplate;
    @Mock
    private CDXApiClientConfig cDXApiClientConfig;
    
    @InjectMocks
    private TradeServiceProxyClient tradeServiceProxyClient;
    
    private ResponseEntity<String> proxyResponse;
    private String responseBody = "responseBody";
    
    private String hostUrl= "https://d2o5ku9ymibe1l.cloudfront.net";
    private String refreshUrl = "/tradeservice";
    private String sessionId= "sessionId";
    private List<String> traderIds= null;
    
    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        
        traderIds = new ArrayList<>();
        traderIds.add("TRADE1");
        
        doReturn(CDXHeaderMap.CHANNEL_VALUE.getValue()).when(cDXApiClientConfig).getHeaderChannel();
        doReturn(CDXHeaderMap.SUB_CHANNEL_VALUE.getValue()).when(cDXApiClientConfig).getHeaderSubChannel();
        doReturn(hostUrl).when(cDXApiClientConfig).getHostUrl();
        doReturn(refreshUrl).when(tradeServiceProperties).getRefreshUrl();
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test
    public void callTradeServiceProxy_Success() throws Exception {
        
        proxyResponse = new ResponseEntity<String>(responseBody, HttpStatus.OK);        
        doReturn(proxyResponse).when(gatewayProxyTemplate).exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any());
        
        boolean status = tradeServiceProxyClient.callTradeServiceProxy(sessionId, traderIds);
        assertTrue(status);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test
    public void callTradeServiceProxy_Multi_Status_Failure() throws Exception {
        
        proxyResponse = new ResponseEntity<String>(responseBody, HttpStatus.MULTI_STATUS);        
        doReturn(proxyResponse).when(gatewayProxyTemplate).exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any());
        
        boolean status = tradeServiceProxyClient.callTradeServiceProxy(sessionId, traderIds);
        assertTrue(status);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected=MBSSystemException.class)
    public void callTradeServiceProxy_Un_Authorized_Failure() throws Exception {
        
        proxyResponse = new ResponseEntity<String>(responseBody, HttpStatus.UNAUTHORIZED);        
        doReturn(proxyResponse).when(gatewayProxyTemplate).exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any());
        
        boolean status = tradeServiceProxyClient.callTradeServiceProxy(sessionId, traderIds);
        assertFalse(status);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected=MBSSystemException.class)
    public void callTradeServiceProxy_Internal_Server_Error_Failure() throws Exception {
        
        proxyResponse = new ResponseEntity<String>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);        
        doReturn(proxyResponse).when(gatewayProxyTemplate).exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any());
        
        boolean status = tradeServiceProxyClient.callTradeServiceProxy(sessionId, traderIds);
        assertFalse(status);
    }
    
   
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected=MBSSystemException.class)
    public void callTradeServiceProxy_Not_OK_Failure() throws Exception {
        
        proxyResponse = new ResponseEntity<String>(responseBody, HttpStatus.MULTIPLE_CHOICES);        
        doReturn(proxyResponse).when(gatewayProxyTemplate).exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any());
        
        boolean status = tradeServiceProxyClient.callTradeServiceProxy(sessionId, traderIds);
        assertFalse(status);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected=HttpServerErrorException.class)
    public void callTradeServiceProxy_Http_Server_Exception_Failure() throws Exception {
        
        HttpServerErrorException httpServerErrorException = new HttpServerErrorException(HttpStatus.UNAUTHORIZED);
        proxyResponse = new ResponseEntity<String>(responseBody, HttpStatus.UNAUTHORIZED);        
        doThrow(httpServerErrorException).when(gatewayProxyTemplate).exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any());
        
        boolean status = tradeServiceProxyClient.callTradeServiceProxy(sessionId, traderIds);
        assertFalse(status);
    }
    
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected=HttpClientErrorException.class)
    public void callTradeServiceProxy_Http_Client_Exception_Failure() throws Exception {
        
        HttpClientErrorException httpClientErrorException = new HttpClientErrorException(HttpStatus.BAD_GATEWAY);
        proxyResponse = new ResponseEntity<String>(responseBody, HttpStatus.BAD_GATEWAY);        
        doThrow(httpClientErrorException).when(gatewayProxyTemplate).exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any());
        
        boolean status = tradeServiceProxyClient.callTradeServiceProxy(sessionId, traderIds);
        assertFalse(status);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected=Exception.class)
    public void callTradeServiceProxy_Fatal_Error_Failure() throws Exception {
        
        proxyResponse = new ResponseEntity<String>(responseBody, HttpStatus.OK);        
        doThrow(Exception.class).when(gatewayProxyTemplate).exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any());
        
        boolean status = tradeServiceProxyClient.callTradeServiceProxy(sessionId, traderIds);
        assertFalse(status);
    }
}
