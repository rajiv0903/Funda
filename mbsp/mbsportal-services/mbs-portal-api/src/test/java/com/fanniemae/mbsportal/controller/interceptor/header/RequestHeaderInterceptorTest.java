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

package com.fanniemae.mbsportal.controller.interceptor.header;

import static org.mockito.Mockito.doReturn;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.method.HandlerMethod;

import com.fanniemae.mbsportal.api.controller.interceptor.header.RequestHeaderInterceptor;
import com.fanniemae.mbsportal.api.controller.interceptor.header.RequestHeaderRequired;
import com.fanniemae.mbsportal.cdx.config.CDXApiClientConfig;
import com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap;

/**
 * @Author Rajiv Chaudhuri
 * @Date Nov 3, 2017
 * @Time: 4:45:00 PM
 * @Description:
 * 	com.fanniemae.mbsportal.api.controller.interceptor.header
 * 	HeaderInterceptorTest.java
 * @Purpose: JUnit Test Cases for HeaderInterceptor
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class RequestHeaderInterceptorTest {

    @InjectMocks
    RequestHeaderInterceptor requestHeaderInterceptor;

    @Mock
    CDXApiClientConfig cDXApiClientConfig;
    @Mock
    HandlerMethod handlerMethod;
    @Mock
    RequestHeaderRequired requestHeaderRequired;

    MockHttpServletRequest mockedRequest;
    MockHttpServletResponse mockedResponse;

    @Before
    public void setUp() {
        
        mockedRequest = new MockHttpServletRequest();
        mockedResponse = new MockHttpServletResponse();
    }

    @Test
    public void preHandlePassThrough() throws Exception {

        doReturn(true).when(cDXApiClientConfig).isEntitlementpassthrough();
        doReturn(requestHeaderRequired).when(handlerMethod).getMethodAnnotation(RequestHeaderRequired.class);
        requestHeaderInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod);
        Assert.assertEquals(HttpStatus.OK.value() , mockedResponse.getStatus());
    }

    @Test
    public void preHandle() throws Exception {
        
        doReturn(false).when(cDXApiClientConfig).isEntitlementpassthrough();
        mockedRequest.addHeader(CDXHeaderMap.JWS_TOKEN.getValue(), "token");
        doReturn(requestHeaderRequired).when(handlerMethod).getMethodAnnotation(RequestHeaderRequired.class);
        requestHeaderInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod);
        Assert.assertEquals(HttpStatus.OK.value() , mockedResponse.getStatus());
    }
    @Test
    public void preHandleNotPassThroughAndNoToken() throws Exception {
        
        doReturn(false).when(cDXApiClientConfig).isEntitlementpassthrough();
        doReturn(requestHeaderRequired).when(handlerMethod).getMethodAnnotation(RequestHeaderRequired.class);
        requestHeaderInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod);
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value() , mockedResponse.getStatus());
    }
}
