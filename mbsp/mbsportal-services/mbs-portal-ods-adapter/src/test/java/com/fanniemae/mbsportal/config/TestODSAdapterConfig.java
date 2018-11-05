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

package com.fanniemae.mbsportal.config;

import com.fanniemae.all.messaging.api.ESBClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author g8uaxt Created on 11/22/2017.
 */
@Configuration
@ActiveProfiles("test")
public class TestODSAdapterConfig {
        
        @Bean
        @Primary
        public ESBClient esbClient() {
                return Mockito.mock(ESBClient.class);
        }
}
