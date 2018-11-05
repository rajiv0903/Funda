/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.streaming.socket.broker;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import com.fanniemae.mbsportal.cdx.config.CDXApiClientConfig;
import com.fanniemae.mbsportal.streaming.cdx.CDXStreamingProfileService;
import com.fanniemae.mbsportal.streaming.prop.StreamingClientProperties;
import com.fanniemae.mbsportal.streaming.socket.decorator.WebSocketCustomHandlerDecorator;
import com.fanniemae.mbsportal.streaming.socket.interceptor.ChannelInterceptorAdapterImpl;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 25, 2018
 * @File: com.fanniemae.mbsportal.streaming.socket.broker.WebSocketCustomMessageBrokerConfigurer.java 
 * @Revision: 
 * @Description: WebSocketCustomMessageBrokerConfigurer.java
 */

@Configuration
@EnableWebSocketMessageBroker
@EnableScheduling
public class WebSocketCustomMessageBrokerConfigurer extends AbstractWebSocketMessageBrokerConfigurer
        implements WebSocketMessageBrokerConfigurer {

    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketCustomMessageBrokerConfigurer.class);

    /**
     * streamingClientProperties StreamingClientProperties
     */
    @Autowired
    private StreamingClientProperties streamingClientProperties;
    /**
     * cDXApiClientConfig CDXApiClientConfig
     */
    @Autowired
    private CDXApiClientConfig cDXApiClientConfig;
    /**
     * cDXProfileSessionService CDXProfileSessionService
     */
    @Autowired
    private CDXStreamingProfileService cDXStreamingProfileSessionService;
    

    /**
     * 
     * Configure the Message Broker with List of Supported Topic Destination
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        
        String[] destinationPrefixes = {streamingClientProperties.getTransactionDestinationTopic(), streamingClientProperties.getPricingDestinationTopic()};
        
        LOGGER.debug("Configuring  MessageBroker Destinations: {}", String.valueOf(destinationPrefixes));
        
        registry.enableSimpleBroker(destinationPrefixes);
        registry.setApplicationDestinationPrefixes(streamingClientProperties.getEndPoint());
        ThreadPoolTaskExecutor taskExecutorBrokerChannel = new ThreadPoolTaskExecutor();
        taskExecutorBrokerChannel.setMaxPoolSize(20);
        taskExecutorBrokerChannel.setCorePoolSize(10);
        registry.configureBrokerChannel().taskExecutor(taskExecutorBrokerChannel);

    }

    /**
     * 
     * Configure the Message Broker Endpoint
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(streamingClientProperties.getEndPoint()).setAllowedOrigins("*").withSockJS();

    }

    /**
     * 
     * Configure the Message Broker In-Bound Channel Interceptor
     */
    @Override
    public void configureClientInboundChannel(final ChannelRegistration registration) {
        
        registration.interceptors(new ChannelInterceptorAdapterImpl(streamingClientProperties, cDXApiClientConfig, cDXStreamingProfileSessionService));
        ThreadPoolTaskExecutor taskExecutorInboundChannel = new ThreadPoolTaskExecutor();
        taskExecutorInboundChannel.setMaxPoolSize(streamingClientProperties.getInBoundMaxPool());
        taskExecutorInboundChannel.setCorePoolSize(streamingClientProperties.getInBoundCorePool());
        registration.taskExecutor(taskExecutorInboundChannel);
    }

    /**
     * 
     * Configure the Message Broker Transport
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setDecoratorFactories(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandlerDecorator decorate(WebSocketHandler handler) {
                return new WebSocketCustomHandlerDecorator(handler);
            }
        });

    }

    /**
     * 
     * Configure the Message Broker Out-Bound Channel Interceptor
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {

        ThreadPoolTaskExecutor taskExecutorOutboundChannel = new ThreadPoolTaskExecutor();
        taskExecutorOutboundChannel.setMaxPoolSize(streamingClientProperties.getOutBoundMaxPool());
        taskExecutorOutboundChannel.setCorePoolSize(streamingClientProperties.getOutBoundCorePool());
        registration.taskExecutor(taskExecutorOutboundChannel);
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        return true;
    }

}
