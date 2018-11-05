/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.utils.cdx.token;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import com.fanniemae.mbsportal.cdx.config.CDXApiClientConfig;
import com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap;
import com.fanniemae.mbsportal.cdx.template.GatewayProxyTemplate;
import com.fanniemae.mbsportal.utils.epv.EpvConnector;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jun 21, 2018
 * @File: com.fanniemae.mbsportal.utils.cdx.token.CDXTokenRefresherTest.java 
 * @Revision: 
 * @Description: CDXTokenRefresherTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class CDXTokenRefresherTest {

    @Mock
    CDXApiClientConfig cDXApiClientConfig;
    @Mock
    private GatewayProxyTemplate gatewayProxyTemplate;
    @Mock
    private EpvConnector epvConnector;

    @InjectMocks
    private CDXTokenRefresher cDXTokenRefresher;
    
    ResponseEntity<String> loginResponse;
    String responseBody = "responseBody";
    private String userId= "mbspdevl";
    private String valutPwd= "valutPwd";
    private String sessionId= "sessionId";
    private String baseurl= "https://d2o5ku9ymibe1l.cloudfront.net/cdxapi/";
    private String loginapi = "cdxlogin";

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        
        doReturn(CDXHeaderMap.CHANNEL_VALUE.getValue()).when(cDXApiClientConfig).getHeaderChannel();
        doReturn(CDXHeaderMap.SUB_CHANNEL_VALUE.getValue()).when(cDXApiClientConfig).getHeaderSubChannel();
        doReturn(userId).when(cDXApiClientConfig).getUserId();
        doReturn(valutPwd).when(epvConnector).getValutPwd();
        doReturn(userId).when(cDXApiClientConfig).getUserId();
        doReturn(userId).when(cDXApiClientConfig).getUserId();
        doReturn(baseurl).when(cDXApiClientConfig).getBaseurl();
        doReturn(loginapi).when(cDXApiClientConfig).getLoginapi();
        
    }
    
    @Test
    public void encrypt_success() {
        Assert.assertNotNull(cDXTokenRefresher.encrypt("abc"));
    }
    
    @Test
    public void decrypt_success() {
        Assert.assertNotNull(cDXTokenRefresher.decrypt("abc"));
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test
    public void refreshLogin_Success() throws Exception{
        
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(CDXHeaderMap.SESSION_ID.getValue(), sessionId);
        loginResponse = new ResponseEntity<String>(responseBody, headers, HttpStatus.OK);
        
        doReturn(loginResponse).when(gatewayProxyTemplate).exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any());
        
        String sessionIdRet = cDXTokenRefresher.refreshLogin();
        assertEquals(sessionId, sessionIdRet);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected=MBSSystemException.class)
    public void refreshLogin_Bad_Request_Failure() throws Exception{
        
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(CDXHeaderMap.SESSION_ID.getValue(), sessionId);
        loginResponse = new ResponseEntity<String>(responseBody, headers, HttpStatus.BAD_REQUEST);
        
        doReturn(loginResponse).when(gatewayProxyTemplate).exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any());
        
        String sessionIdRet = cDXTokenRefresher.refreshLogin();
        assertEquals(sessionId, sessionIdRet);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected=MBSSystemException.class)
    public void refreshLogin_Empty_Password_Failure() throws Exception{
        
        doReturn(null).when(epvConnector).getValutPwd();
        
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(CDXHeaderMap.SESSION_ID.getValue(), sessionId);
        loginResponse = new ResponseEntity<String>(responseBody, headers, HttpStatus.OK);
        
        doThrow(RestClientException.class).when(gatewayProxyTemplate).exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any());
        
        String sessionIdRet = cDXTokenRefresher.refreshLogin();
        assertEquals(sessionId, sessionIdRet);
    }
    
    /**
     * 
     * @throws Exception
     */
    @Test(expected=MBSSystemException.class)
    public void refreshLogin_Failure() throws Exception{
        
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(CDXHeaderMap.SESSION_ID.getValue(), sessionId);
        loginResponse = new ResponseEntity<String>(responseBody, headers, HttpStatus.OK);
        
        doThrow(RestClientException.class).when(gatewayProxyTemplate).exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any());
        
        String sessionIdRet = cDXTokenRefresher.refreshLogin();
        assertEquals(sessionId, sessionIdRet);
    }
}
