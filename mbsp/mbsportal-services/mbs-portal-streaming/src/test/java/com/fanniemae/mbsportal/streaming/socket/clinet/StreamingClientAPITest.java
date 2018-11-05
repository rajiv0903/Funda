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

package com.fanniemae.mbsportal.streaming.socket.clinet;

import com.fanniemae.mbsportal.service.TokenService;
import com.fanniemae.mbsportal.streaming.prop.StreamingClientProperties;
import com.fanniemae.mbsportal.streaming.socket.client.StreamingClientAPI;
import com.fanniemae.mbsportal.streaming.socket.model.StreamingMessage;
import com.fanniemae.mbsportal.streaming.socket.model.StreamingMessageDetails;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.template.MBSRestInternalTemplate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

/**
 * @author: Rajiv Chaudhuri
 * @Date: Jun 8, 2018
 * @File: com.fanniemae.mbsportal.streaming.clinet.StreamingClientAPITest.java
 * @Revision:
 * @Description: StreamingClientAPITest.java
 */
@RunWith(MockitoJUnitRunner.class)
@Ignore
public class StreamingClientAPITest {
        
        private static final String TRANSACTION_CONTEXT_PATH = "transaction";
        /**
         * streamingClientProperties StreamingClientProperties
         */
        @Mock
        private StreamingClientProperties streamingClientProperties;
        /**
         * mbsRestInternalTemplate MBSRestInternalTemplate
         */
        @Mock
        private MBSRestInternalTemplate mbsRestInternalTemplate;
        @Mock
        private TokenService tokenService;
        /**
         * simpMessagingTemplate SimpMessagingTemplate
         */
        @Mock
        private SimpMessagingTemplate simpMessagingTemplate;
        /**
         * streamingClientAPI StreamingClientAPI
         */
        @InjectMocks
        private StreamingClientAPI streamingClientAPI;
        private StreamingMessage streamingMessage;
        private String defaultServiceUrl;
        private String sessionId;
        
        /**
         * @throws Exception
         */
        @Before
        public void setUp() throws Exception {
                defaultServiceUrl = "https://localhost:8443/mbsp-streaming/publish/";
                StreamingMessageDetails streamingMessageDetails = getstreamingMessageDetails();
                streamingMessage = new StreamingMessage();
                streamingMessage.addMessages(streamingMessageDetails);
                doReturn("valid-session-id").when(tokenService).getCurrentSessionId();
                doReturn("valid-session-id").when(tokenService).getValidSessionId();
                doNothing().when(tokenService).resetSessionAndToken();
        }
        
        /**
         * @throws Exception
         */
        @SuppressWarnings("unchecked")
        @Test
        public void sendTransactionMsg_Existing_Session_Success() throws Exception {
                
                doReturn(TRANSACTION_CONTEXT_PATH).when(streamingClientProperties).getPublishMsgContextPath();
                doReturn(1).when(streamingClientProperties).getRetryCount();
                
                ResponseEntity<Void> mockResponse = new ResponseEntity<Void>(HttpStatus.OK);
                doReturn(mockResponse).when(mbsRestInternalTemplate)
                        .exchange(anyList(), anyInt(), Mockito.<HttpMethod>any(),
                                Mockito.<HttpEntity<StreamingMessage>>any(), Matchers.any(Class.class));
                
                streamingClientAPI.sendMsgByHttp(streamingMessage, defaultServiceUrl);
        }
        
        /**
         * @throws Exception
         */
        @SuppressWarnings("unchecked")
        @Test
        public void sendTransactionMsg_New_Session_Success() throws Exception {
                doReturn(TRANSACTION_CONTEXT_PATH).when(streamingClientProperties).getPublishMsgContextPath();
                doReturn(1).when(streamingClientProperties).getRetryCount();
                doReturn(null).when(tokenService).getCurrentSessionId();
                ResponseEntity<Void> mockResponse = new ResponseEntity<Void>(HttpStatus.OK);
                doReturn(mockResponse).when(mbsRestInternalTemplate)
                        .exchange(anyList(), anyInt(), Mockito.<HttpMethod>any(),
                                Mockito.<HttpEntity<StreamingMessage>>any(), Matchers.any(Class.class));
                
                streamingClientAPI.sendMsgByHttp(streamingMessage, defaultServiceUrl);
        }
        
        @SuppressWarnings("unchecked")
        @Test(expected = HttpClientErrorException.class)
        public void sendTransactionMsg_Throw_HttpClient_Error_Exception_Failure() throws Exception {
                doReturn(TRANSACTION_CONTEXT_PATH).when(streamingClientProperties).getPublishMsgContextPath();
                doReturn(1).when(streamingClientProperties).getRetryCount();
                doThrow(HttpClientErrorException.class).when(mbsRestInternalTemplate)
                        .exchange(anyList(), anyInt(), Mockito.<HttpMethod>any(),
                                Mockito.<HttpEntity<StreamingMessage>>any(), Matchers.any(Class.class));
                
                streamingClientAPI.sendMsgByHttp(streamingMessage, defaultServiceUrl);
        }
        
        /**
         * @throws Exception
         */
        @SuppressWarnings("unchecked")
        @Test(expected = RestClientException.class)
        public void sendTransactionMsg_Throw_Rest_Client_Exception_Failure() throws Exception {
                doReturn(TRANSACTION_CONTEXT_PATH).when(streamingClientProperties).getPublishMsgContextPath();
                doReturn(1).when(streamingClientProperties).getRetryCount();
                
                doThrow(RestClientException.class).when(mbsRestInternalTemplate)
                        .exchange(anyList(), anyInt(), Mockito.<HttpMethod>any(),
                                Mockito.<HttpEntity<StreamingMessage>>any(), Matchers.any(Class.class));
                
                streamingClientAPI.sendMsgByHttp(streamingMessage, defaultServiceUrl);
        }
        
        /**
         * @throws Exception
         */
        @SuppressWarnings("unchecked")
        @Test(expected = MBSBaseException.class)
        @Ignore
        public void sendTransactionMsg_Throw_MBSBaseException_Failure() throws Exception {
                doReturn(TRANSACTION_CONTEXT_PATH).when(streamingClientProperties).getPublishMsgContextPath();
                doReturn(1).when(streamingClientProperties).getRetryCount();
                ResponseEntity<Void> mockResponse = new ResponseEntity<Void>(HttpStatus.OK);
                doReturn(mockResponse).when(mbsRestInternalTemplate)
                        .exchange(anyList(), anyInt(), Mockito.<HttpMethod>any(),
                                Mockito.<HttpEntity<StreamingMessage>>any(), Matchers.any(Class.class));
                
                streamingClientAPI.sendMsgByHttp(streamingMessage, defaultServiceUrl);
        }
        
        /**
         * @throws Exception
         */
        @SuppressWarnings("unchecked")
        @Test(expected = Exception.class)
        @Ignore
        public void sendTransactionMsg_Throw_Exception_Failure() throws Exception {
                doReturn(TRANSACTION_CONTEXT_PATH).when(streamingClientProperties).getPublishMsgContextPath();
                doReturn(1).when(streamingClientProperties).getRetryCount();
                ResponseEntity<Void> mockResponse = new ResponseEntity<Void>(HttpStatus.OK);
                doReturn(mockResponse).when(mbsRestInternalTemplate)
                        .exchange(anyList(), anyInt(), Mockito.<HttpMethod>any(),
                                Mockito.<HttpEntity<StreamingMessage>>any(), Matchers.any(Class.class));
                
                streamingClientAPI.sendMsgByHttp(streamingMessage, defaultServiceUrl);
        }
        
        /**
         * @return streamingMessageDetails the StreamingMessageDetails
         */
        private StreamingMessageDetails getstreamingMessageDetails() {
                String jsonInString = "jsonInString";
                // Create Trader Message
                StreamingMessageDetails streamingMessageDetails = new StreamingMessageDetails();
                streamingMessageDetails.setMessage(jsonInString);
                streamingMessageDetails.addTopic("topic");
                return streamingMessageDetails;
        }
}
