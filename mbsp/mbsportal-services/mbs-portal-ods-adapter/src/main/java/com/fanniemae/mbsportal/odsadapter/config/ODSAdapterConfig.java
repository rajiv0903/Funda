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

package com.fanniemae.mbsportal.odsadapter.config;

import com.fanniemae.all.messaging.api.ESBClient;
import com.fanniemae.all.messaging.api.ESBClientCredentials;
import com.fanniemae.all.messaging.api.ESBClientException;
import com.fanniemae.all.messaging.api.ESBClientFactory;
import com.fanniemae.mbsportal.utils.epv.EpvConnector;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author g8uaxt Created on 11/21/2017.
 */

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Dec 28, 2017
 * @Time 11:07:59 AM com.fanniemae.mbsportal.odsadapter.config BootstrapConfig.java
 * @Description: For Blue/ Green - Added Profile
 */
@Configuration
public class ODSAdapterConfig {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ODSAdapterConfig.class);
    @Value("${mbsp.systemUserId}")
    private String esbUserId;

    private String esbUserPwd;
    private String EFSSTrustStorePassword;

    @Value("${mbsp.appCd}")
    private String appCode;
    @Value("${mbsp.envCd}")
    private String env;
    @Value("${mbsp.epvRefId}")
    private String objRef;

    @Bean
    @Profile({ "DEV1", "DEV2", "ACPT1", "ACPT2", "PERF", "TEST1", "PROD1", "PROD2" })
    public ESBClient esbClient() throws ESBClientException {
        ESBClient esbClient = ESBClientFactory.createESBClient(esbClientCredentials());
        LOGGER.info("esbClient bean initialized");
        return esbClient;
    }

    @Bean
    @Profile({ "DEV1", "DEV2", "ACPT1", "ACPT2", "PERF","TEST1", "PROD1", "PROD2" })
    ESBClientCredentials esbClientCredentials() {
        esbUserPwd = epvConnector().getValutPwd();
        LOGGER.debug("esbClientCredentials with esbUserId: " + esbUserId);
        ESBClientCredentials esbClientCredentials = new MBSEsbClientCredentials(esbUserId, esbUserPwd,
                EFSSTrustStorePassword);
        return esbClientCredentials;
    }

    @Bean
    @Profile({ "DEV1", "DEV2", "ACPT1", "ACPT2", "PERF", "TEST1", "PROD1", "PROD2" })
    public EpvConnector epvConnector() {
        LOGGER.info("epvConnector bean initializing");
        EpvConnector epvConnector = new EpvConnector();
        try {
            epvConnector.initialize(appCode, env, objRef);
        } catch (MBSBaseException e) {
            LOGGER.error("epvConnector bean initialization failed", e.getRootException());
        }
        LOGGER.info("epvConnector bean initialized");
        return epvConnector;
    }

    // local config beans
    @Bean
    @Profile({ "default", "test", "perf", "LOCAL" })
    public ESBClient esbClientLocal() throws ESBClientException {
        // is it threads safe?
        ESBClient esbClient = null;
        LOGGER.debug("esbClientLocal bean initialized");
        return esbClient;
    }

    @Bean
    @Profile({ "default", "test", "perf", "LOCAL" })
    ESBClientCredentials esbClientCredentialsLocal() {
        ESBClientCredentials esbClientCredentials = null;
        LOGGER.debug("esbClientCredentialsLocal bean initialized");
        return esbClientCredentials;
    }
}
