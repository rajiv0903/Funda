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

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import com.fanniemae.mbsportal.api.config.AppVault;
import com.fanniemae.mbsportal.api.config.TradeServiceProperties;

/**
 * Created by g8uaxt on 6/28/2017.
 */

@Configuration
@ActiveProfiles("test")
@ImportResource(value = { "classpath*:*-client-gf-context-test.xml" })
public class TestContextConfig {
       /* @Bean
        @Primary
        public TradeServicePoller tradeServicePoller() {
                return Mockito.mock(TradeServicePoller.class);
        }*/

       @Bean
       @Primary
       public RestTemplate restTemplate() {
               return Mockito.mock(RestTemplate.class);
       }
        
        @Bean
        @Primary
        public AppVault appVault() {
                return Mockito.mock(AppVault.class);
        }
        @Bean
        @Primary
        public TradeServiceProperties tradeServiceProperties() {
                return Mockito.mock(TradeServiceProperties.class);
        }
        
}
