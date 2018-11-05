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

import static org.assertj.core.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.api.po.UILoggingMessagePO;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;


/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 5, 2018
 * @Time 10:25:02 AM
 * 	com.fanniemae.mbsportal.api.service
 * 	UILogginServiceTest.java
 * @Description: JUnit test cases for UI Logging Service
 */
@RunWith(MockitoJUnitRunner.class)
public class UILogginServiceTest extends BaseServiceTest {

    @InjectMocks
    private UILogginService uiLogginService;

    private List<UILoggingMessagePO> uiLoggingMessagePOs;
    
    private UILoggingMessagePO uiLoggingMessagePO;

    @Before
    public void setUp() throws Exception {
        
        uiLoggingMessagePOs = new ArrayList<>();

        uiLoggingMessagePO = new UILoggingMessagePO();
        uiLoggingMessagePO.setUserName("userid");
        uiLoggingMessagePO.setCode("200");
        uiLoggingMessagePO.setTransReqId("18J12345");
        uiLoggingMessagePO.setType("ERROR");
        uiLoggingMessagePO.setModule("TRANSACTION");
        uiLoggingMessagePO.setPage("LENDER_TRANSACTION");
        uiLoggingMessagePO.setFunctionality("LENDER_TRANSACTION_TRADE_POST");
        uiLoggingMessagePO.setMessage("Issue with lender post");
        uiLoggingMessagePO.setMessageDescription("Internal Method error");
        
        uiLoggingMessagePOs.add(uiLoggingMessagePO);

    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void logUIMsgTest() throws MBSBaseException {

        try {
            uiLogginService.logUIMsg(uiLoggingMessagePOs);

        } catch (Exception exe) {
            fail("There should not be any exception", exe);
        }
    }
    
}
