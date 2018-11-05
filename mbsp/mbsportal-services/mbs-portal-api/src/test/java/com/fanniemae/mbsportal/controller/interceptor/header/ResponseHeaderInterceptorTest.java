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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

import com.fanniemae.mbsportal.api.controller.interceptor.header.ResponseHeaderInterceptor;
import com.fanniemae.mbsportal.api.controller.interceptor.header.ResponseHeaderRequired;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Mar 22, 2018
 * @File: com.fanniemae.mbsportal.controller.interceptor.header.ResponseHeaderInterceptorTest.java
 * @Revision : 
 * @Description: ResponseHeaderInterceptorTest.java
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ResponseHeaderInterceptorTest {

    @InjectMocks
    ResponseHeaderInterceptor responseHeaderInterceptor;
    
    @Mock
    HandlerMethod handlerMethod;
    @Mock
    ResponseHeaderRequired responseHeaderRequired;

    MockHttpServletRequest mockedRequest;
    MockHttpServletResponse mockedResponse;

    @Before
    public void setUp() {
        
        mockedRequest = new MockHttpServletRequest();
        mockedResponse = new MockHttpServletResponse();
    }
    
    @Test
    public void preHandle_Success() throws Exception {

        doReturn(responseHeaderRequired).when(handlerMethod).getMethodAnnotation(ResponseHeaderRequired.class);
        responseHeaderInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod);
        Assert.assertEquals(HttpStatus.OK.value() , mockedResponse.getStatus());
        assertNotNull(mockedResponse.getHeader(MBSPServiceConstants.RESPONSE_HEADER_SERVER_TIME_NAME));
        assertEquals(MBSPServiceConstants.RESPONSE_HEADER_SERVER_TIME_NAME, mockedResponse.getHeader(MBSPServiceConstants.RESPONSE_ACCESS_CONTROL_EXPOSE_HEADER_NAME));
    }
}
