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

package com.fanniemae.mbsportal.streaming.socket.client;

import com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap;
import com.fanniemae.mbsportal.service.TokenService;
import com.fanniemae.mbsportal.streaming.prop.StreamingClientProperties;
import com.fanniemae.mbsportal.streaming.socket.model.StreamingMessage;
import com.fanniemae.mbsportal.streaming.socket.model.StreamingMessageDetails;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.template.MBSRestInternalTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

/**
 * @author: Rajiv Chaudhuri
 * @Date: Jul 27, 2018
 * @File: com.fanniemae.mbsportal.streaming.socket.client.StreamingClientAPI.java
 * @Revision:
 * @Description: StreamingClientAPI.java
 */

@Component
@EnableAsync
@EnableRetry
public class StreamingClientAPI {
        /**
         * LOGGER Logger variable
         */
        private static final Logger LOGGER = LoggerFactory.getLogger(StreamingClientAPI.class);
        
        @Autowired
        private TokenService tokenService;
        /**
         * streamingClientProperties StreamingClientProperties
         */
        @Autowired
        private StreamingClientProperties streamingClientProperties;
        /**
         * mbsRestInternalTemplate MBSRestInternalTemplate
         */
        @Autowired
        private MBSRestInternalTemplate mbsRestInternalTemplate;
        /**
         * simpMessagingTemplate SimpMessagingTemplate
         */
        @Autowired
        private SimpMessagingTemplate simpMessagingTemplate;
        
        /**
         * initialization method
         */
        @PostConstruct
        public void init() {
                LOGGER.info("Started Websocket client");
        }
        
        public void sendMsgByTemplate(StreamingMessage streamingMessage) throws MBSBaseException {
                
                LOGGER.debug("Entering sendMsgByTemplate method in StreamingClientAPI");
                
                for(StreamingMessageDetails messages : streamingMessage.getMessages()) {
                        
                        for(String topic : messages.getTopics()) {
                                LOGGER.info("Topic: {}, Message Identifier: {}, Message: {}", topic,
                                        messages.getMessageIdentifierMap(), messages.getMessage());
                                simpMessagingTemplate.convertAndSend(topic, messages.getMessage());
                                
                        }
                        
                }
                LOGGER.debug("Exiting sendMsgByTemplate method in StreamingClientAPI");
        }
        
        /**
         * Publish the message to Streaming Server
         *
         * @param streamingMessage    the streamingMessage
         * @param msgPublishServerUrl the msgPublishServerUrl
         * @throws MBSBaseException
         */
        @Retryable(value = { HttpClientErrorException.class, RestClientException.class, MBSBaseException.class,
                Exception.class }, maxAttemptsExpression = "#{${mbs.messaging.client.retryMaxAttempts}}", backoff =
        @Backoff(delayExpression = "#{${mbs.messaging.client.retryBackoff}}"))
        @Async
        public void sendMsgByHttp(StreamingMessage streamingMessage, String msgPublishServerUrl)
                throws MBSBaseException {
                // TODO: add async name to it to track
                LOGGER.debug("Entering sendMsgByHttp method in StreamingClientAPI");
                try {
                        
                        HttpHeaders headers = new HttpHeaders();
                        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add(CDXHeaderMap.CHANNEL.getValue(), CDXHeaderMap.CHANNEL_VALUE.getValue());
                        headers.add(CDXHeaderMap.SUB_CHANNEL.getValue(), CDXHeaderMap.SUB_CHANNEL_VALUE.getValue());
                        String sessionId = tokenService.getCurrentSessionId();
                        // Get Session ID from Config
                        if(sessionId == null) {
                                sessionId = tokenService.getValidSessionId();
                        }
                        if(sessionId == null) {
                                throw new MBSBaseException("Session Null, need to go for another re-try: ");
                        }
                        // set the header for security handshake check
                        headers.add(CDXHeaderMap.SESSION_ID.getValue(), sessionId);
                        
                        HttpEntity<StreamingMessage> request = new HttpEntity<>(streamingMessage, headers);
                        
                        String publishMsgContextPath = streamingClientProperties.getPublishMsgContextPath();
                        
                        LOGGER.info("headers : {}, msgPublishServerUrl: {}, publishMsgContextPath: {}, Topics: {}, "
                                        + "Message Identifier: {}", headers, msgPublishServerUrl, publishMsgContextPath,
                                streamingMessage.getMessages().get(0).getTopics(),
                                streamingMessage.getMessages().get(0).getMessageIdentifierMap());
                        
                        LOGGER.debug("streamingMessage : {}", streamingMessage);
                        
                        List<String> urlList = Arrays.asList(msgPublishServerUrl);
                        urlList = urlList.stream().map(url -> url + publishMsgContextPath).collect(Collectors.toList());
                        
                        StopWatch sw = new StopWatch();
                        sw.start();
                        
                        ResponseEntity<Void> response = mbsRestInternalTemplate
                                .exchange(urlList, streamingClientProperties.getRetryCount(), HttpMethod.POST, request,
                                        Void.class);
                        
                        sw.stop();
                        
                        LOGGER.debug("streamingMessage : {}", streamingMessage);
                        LOGGER.debug("StatusCodeValue : {}, Time Taken to Publish: {} ms",
                                response.getStatusCodeValue(), sw.getTotalTimeMillis());
                        
                        if(HttpStatus.UNAUTHORIZED.value() == response.getStatusCodeValue()) {
                                // reset the token for token refresh
                                tokenService.resetSessionAndToken();
                                throw new MBSBaseException(
                                        "Failed to perform handshake, status code: " + response.getStatusCodeValue());
                        }
                        if(HttpStatus.OK.value() != response.getStatusCodeValue()) {
                                throw new MBSBaseException(
                                        "Failed to perform handshake, status code: " + response.getStatusCodeValue());
                        }
                } catch(HttpClientErrorException exe) {
                        LOGGER.error("HttpClientErrorException Exception: {}", exe);
            /*
             * Reset the token for any 4xx error send by Streaming Server
             */
                        tokenService.resetSessionAndToken();
                        throw exe;
                        
                } catch(RestClientException exe) {
                        LOGGER.error("RestClientException Exception: {}", exe);
                        throw exe;
                } catch(MBSBaseException exe) {
                        LOGGER.error("MBSBaseException Exception: {}", exe);
                        throw exe;
                } catch(Exception exe) {
                        LOGGER.error("Exception: {}", exe);
                        throw exe;
                }
                LOGGER.debug("Exiting sendMsgByHttp method in StreamingClientAPI");
        }
        
}
