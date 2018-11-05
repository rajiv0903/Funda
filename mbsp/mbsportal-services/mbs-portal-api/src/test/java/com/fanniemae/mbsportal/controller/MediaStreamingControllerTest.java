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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fanniemae.mbsportal.api.controller.MediaStreamingController;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Mar 23, 2018
 * @File: com.fanniemae.mbsportal.controller.MediaStreamingControllerTest.java
 * @Revision : 
 * @Description: MediaStreamingControllerTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class MediaStreamingControllerTest {

    @InjectMocks
    MediaStreamingController mediaStreamingController;
    
    MockHttpServletRequest mockedRequest;
    MockHttpServletResponse mockedResponse;
    
    String fileName;
    
    /**
     * 
     */
    @Before
    public void setUp() {
        
        mockedRequest = new MockHttpServletRequest();
        mockedResponse = new MockHttpServletResponse();
        
        fileName = "LENDER_OPEN.mp3";
    }
    
    /**
     * 
     */
    @Test
    public void downloadAudioMedia_Success(){
        
        mediaStreamingController.downloadAudioMedia(mockedRequest, mockedResponse, fileName);
        Assert.assertEquals(HttpStatus.OK.value() , mockedResponse.getStatus());
    }
    
    /**
     * 
     */
    @Test
    public void downloadAudioMedia_Content_Length_Success(){
        
        mediaStreamingController.downloadAudioMedia(mockedRequest, mockedResponse, fileName);
        Assert.assertEquals(HttpStatus.OK.value() , mockedResponse.getStatus());
        Assert.assertTrue(mockedResponse.getContentLength() > 0);
    }
    
    /**
     * 
     */
    @Test
    public void downloadAudioMedia_Failure(){
        
        mediaStreamingController.downloadAudioMedia(mockedRequest, mockedResponse, fileName+".pdf");
        Assert.assertEquals(HttpStatus.OK.value() , mockedResponse.getStatus());
    }
       
}
