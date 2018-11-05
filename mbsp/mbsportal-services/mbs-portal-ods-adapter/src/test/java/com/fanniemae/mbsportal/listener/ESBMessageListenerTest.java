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

package com.fanniemae.mbsportal.listener;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.all.messaging.api.ESBClient;
import com.fanniemae.all.messaging.api.ESBClientException;
import com.fanniemae.all.messaging.message.ESBMessage;
import com.fanniemae.mbsportal.service.TradeMessageProcessor;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Dec 29, 2017
 * @Time 10:15:09 AM com.fanniemae.mbsportal.listener
 *       ESBMessageListenerTest.java
 * @Description: Test Cases for ESBMessageListener
 */
@RunWith(MockitoJUnitRunner.class)
public class ESBMessageListenerTest {

    @InjectMocks
    ESBMessageListener eSBMessageListener;

    @Mock
    ESBClient eSBClient;

    @Mock
    TradeMessageProcessor tradeMessageProcessor;

    @Mock
    ESBMessage eSBMessage;

    private String messageId;
    private String payLoad;

    @Before
    public void setUp() throws Exception {
        messageId = "messageId";
        payLoad = "Sample XML Payload";
    }

    @Test
    public void startESBClientAsyncConsumer_Success() throws Exception {

        doNothing().when(eSBClient).startMessageConsumer(any());
        eSBMessageListener.startESBClientAsyncConsumer();
    }

    @Test
    public void startESBClientAsyncConsumer_Throw_Error() throws Exception {

        doThrow(Exception.class).when(eSBClient).startMessageConsumer(any());
        eSBMessageListener.startESBClientAsyncConsumer();
    }

    @Test
    public void processMessage_Success() throws Exception {

        doReturn(messageId).when(eSBMessage).getMessageId();
        doReturn(payLoad).when(eSBMessage).getPayload();
        doNothing().when(tradeMessageProcessor).prepareAndProcess(any());
        boolean handleMsgReturn  = eSBMessageListener.processMessage(eSBMessage);
        assertTrue(handleMsgReturn);
    }
    
    @Test
    public void processMessage_Suspend_Message() throws Exception {

        doReturn(messageId).when(eSBMessage).getMessageId();
        doReturn(payLoad).when(eSBMessage).getPayload();
        doThrow(Exception.class).when(tradeMessageProcessor).prepareAndProcess(any());
        doNothing().when(eSBClient).suspendMessage(any());
        boolean handleMsgReturn  = eSBMessageListener.processMessage(eSBMessage);
        assertTrue(handleMsgReturn);
    }
    
    @Test
    public void processMessage_Failed_To_Suspend_Message() throws Exception {

        doReturn(messageId).when(eSBMessage).getMessageId();
        doReturn(payLoad).when(eSBMessage).getPayload();
        doThrow(Exception.class).when(tradeMessageProcessor).prepareAndProcess(any());
        doThrow(ESBClientException.class).when(eSBClient).suspendMessage(any());
        boolean handleMsgReturn  = eSBMessageListener.processMessage(eSBMessage);
        assertFalse(handleMsgReturn);
    }
}
