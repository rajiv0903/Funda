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

package com.fanniemae.mbsportal.pu.adapter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.fanniemae.all.messaging.api.ESBClient;
import com.fanniemae.all.messaging.api.ESBClientCredentials;
import com.fanniemae.all.messaging.api.ESBClientException;

/**
 * 
 * @author g8upjv
 *
 */
@Configuration
@Profile({ "default", "test", "LOCAL" })
public class PUAdapterLowerEnvConfig {

    /**
     * 
     * LOGGER Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PUAdapterLowerEnvConfig.class);
    
    /**
     * 
     * 
     * @return ESBClient
     * @throws ESBClientException
     */
    // local config beans
    @Bean
    public ESBClient esbClientLocal() throws ESBClientException {
        // is it threads safe?
        ESBClient esbClient = null;
        LOGGER.info("esbClientLocal bean initialized");
        return esbClient;
    }
    
    /**
     * 
     * 
     * @return ESBClientCredentials
     */
    @Bean
    ESBClientCredentials esbClientCredentialsLocal() {
        ESBClientCredentials esbClientCredentials = null;
        LOGGER.info("esbClientCredentialsLocal bean initialized");
        return esbClientCredentials;
    }
}
