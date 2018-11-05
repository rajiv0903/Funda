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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.fanniemae.mbsportal.api.controller.UILoggingController;
import com.fanniemae.mbsportal.api.controller.helper.UILoggingControllerHelper;
import com.fanniemae.mbsportal.api.po.UILoggingMessagePO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 4, 2018
 * @Time 4:26:32 PM
 * 	com.fanniemae.mbsportal.api.controller
 * 	UILoggingControllerTest.java
 * @Description: JUnit Test cases for UI Logging Controller
 */
@RunWith(MockitoJUnitRunner.class)
public class UILoggingControllerTest {

    
    @InjectMocks
    private UILoggingController uiLoggingController;

    @Mock
    private UILoggingControllerHelper uiLoggingControllerHelper;
    
    private List<UILoggingMessagePO> uiLoggingMessagePOs;
    private UILoggingMessagePO uiLoggingMessagePO;
    
    @Before
    public void setUp() throws Exception {
        
        uiLoggingMessagePOs = new ArrayList<>();
        
        uiLoggingMessagePO = new UILoggingMessagePO();
        uiLoggingMessagePO.setUserName("userid");
        uiLoggingMessagePO.setModule("TRANSACTION");
        
        uiLoggingMessagePOs.add(uiLoggingMessagePO);

    }
    
    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void logUIMsgTest() throws MBSBaseException {
        
        doNothing().when(uiLoggingControllerHelper).logUIMsg(any());
        ResponseEntity<Void> responseObj = uiLoggingController.logUIMsg(uiLoggingMessagePOs);
        assertFalse(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
    }
    
    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void logUIMsgMBSBaseExceptionTest() throws MBSBaseException {

        doThrow(MBSBaseException.class).when(uiLoggingControllerHelper).logUIMsg(any());
        ResponseEntity<Void> responseObj = uiLoggingController.logUIMsg(uiLoggingMessagePOs);
        assertFalse(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("400"));
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void logUIMsgExceptionTest() throws MBSBaseException {

        doThrow(RuntimeException.class).when(uiLoggingControllerHelper).logUIMsg(any());
        ResponseEntity<Void> responseObj = uiLoggingController.logUIMsg(uiLoggingMessagePOs);
        assertFalse(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }
}
