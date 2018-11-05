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

import static org.assertj.core.api.Assertions.fail;
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

import com.fanniemae.mbsportal.api.controller.helper.UILoggingControllerHelper;
import com.fanniemae.mbsportal.api.po.UILoggingMessagePO;
import com.fanniemae.mbsportal.api.service.UILogginService;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 5, 2018
 * @Time 10:05:28 AM com.fanniemae.mbsportal.api.controller.helper
 *       UILoggingControllerHelperTest.java
 * @Description: JUnit Test cases for UI Logging Controller Helper
 */
@RunWith(MockitoJUnitRunner.class)
public class UILoggingControllerHelperTest {

    @InjectMocks
    private UILoggingControllerHelper uiLoggingControllerHelper;

    @Mock
    private UILogginService uiLogginService;

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

        doNothing().when(uiLogginService).logUIMsg(any());
        try {
            uiLoggingControllerHelper.logUIMsg(uiLoggingMessagePOs);

        } catch (Exception exe) {
            fail("There should not be any exception", exe);
        }
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test(expected = MBSBaseException.class)
    public void logUIMsgTest_Errror() throws MBSBaseException {

        doThrow(MBSBusinessException.class).when(uiLogginService).logUIMsg(any());
        uiLoggingControllerHelper.logUIMsg(uiLoggingMessagePOs);
    }

}
