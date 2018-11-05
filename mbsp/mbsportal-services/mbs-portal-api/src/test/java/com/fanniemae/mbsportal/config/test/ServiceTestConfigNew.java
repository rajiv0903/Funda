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
package com.fanniemae.mbsportal.config.test;

import com.fanniemae.mbsportal.api.cdx.CDXClientApi;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import com.fanniemae.all.messaging.api.ESBClient;
import com.fanniemae.mbsportal.api.config.AppVault;
import com.fanniemae.mbsportal.api.config.TradeServiceProperties;
import com.fanniemae.mbsportal.cdx.template.GatewayProxyTemplate;
import com.fanniemae.mbsportal.streaming.prop.StreamingClientProperties;
import com.fanniemae.mbsportal.streaming.stomp.client.MBSStompClient;
import com.fanniemae.mbsportal.utils.cache.MBSPAppCache;
import com.fanniemae.mbsportal.utils.cdx.token.CDXTokenRefresher;
import com.fanniemae.mbsportal.utils.epv.EpvConnector;
import com.fanniemae.mbsportal.utils.template.MBSRestInternalTemplate;

/**
 * @author g8uaxt Created on 10/9/2017.
 */

@Configuration
@ActiveProfiles("test")
public class ServiceTestConfigNew {

    
    @Bean
    @Primary
    public GatewayProxyTemplate gatewayProxyTemplate() {
        return Mockito.mock(GatewayProxyTemplate.class);
    }

    @Bean
    @Primary
    public MBSRestInternalTemplate mbsRestInternalTemplate() {
        return Mockito.mock(MBSRestInternalTemplate.class);
    }
    

    @Bean
    @Primary
    public AppVault appVault() {
        return Mockito.mock(AppVault.class);
    }

    @Bean
    @Primary
    public EpvConnector epvConnector() {
        return Mockito.mock(EpvConnector.class);
    }

    @Bean
    @Primary
    public CDXTokenRefresher tokenRefresher() {
        return Mockito.mock(CDXTokenRefresher.class);
    }

    @Bean
    @Primary
    public TradeServiceProperties tradeServiceProperties() {
        return Mockito.mock(TradeServiceProperties.class);
    }
    
    @Bean
    @Primary
    public StreamingClientProperties streamingClientProperties() {
        StreamingClientProperties streamingClientProperties = new StreamingClientProperties();
        streamingClientProperties.setTransactionDestinationTopic("/mbsp/topic/transaction/");
        streamingClientProperties.setPricingDestinationTopic("/mbsp/topic/tbapricing");
        streamingClientProperties.setEndPoint("/mbsp-streaming");
        streamingClientProperties.setInBoundCorePool(10);
        streamingClientProperties.setInBoundMaxPool(20);
        streamingClientProperties.setOutBoundCorePool(10);
        streamingClientProperties.setOutBoundMaxPool(20);
        return streamingClientProperties;
    }

    @Bean
    @Primary
    public ESBClient esbClient() {
        return Mockito.mock(ESBClient.class);
    }
    
    @Bean
    @Primary
    public MBSPAppCache mbspAppCache() {
        return Mockito.mock(MBSPAppCache.class);
    }
    
    @Bean
    @Primary
    public MBSStompClient mbsStompClient() {
        return Mockito.mock(MBSStompClient.class);
    }
    
    @Bean
    @Primary
    public CDXClientApi cdxClientApi() {
        return Mockito.mock(CDXClientApi.class);
    }

    
}