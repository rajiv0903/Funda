/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.scheduler;

import com.fanniemae.mbsportal.api.cdx.CDXClientApi;
import com.fanniemae.mbsportal.api.listener.PriceListener;
import com.fanniemae.mbsportal.api.listener.TransactionListener;
import com.fanniemae.mbsportal.api.utils.cache.ProductCacheLoader;
import com.fanniemae.mbsportal.listener.ESBMessageListener;
import com.fanniemae.mbsportal.service.TokenService;
import com.fanniemae.mbsportal.streaming.prop.StreamingClientProperties;
import com.fanniemae.mbsportal.streaming.stomp.MBSStompUtil;
import com.fanniemae.mbsportal.streaming.stomp.client.MBSStompClient;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import org.apache.geode.cache.query.CqException;
import org.apache.geode.cache.query.CqExistsException;
import org.apache.geode.cache.query.RegionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

/**
 * Created by g8uaxt on 8/24/2017.
 */

/**
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Dec 28, 2017
 * @Time 11:07:59 AM com.fanniemae.mbsportal.api.config BootstrapConfig.java
 * @Description: For Blue/ Green - Added Profile
 */
@Configuration
@Profile({ "DEV1", "DEV2", "TEST1", "PERF", "ACPT1", "ACPT2", "PROD1", "PROD2"})
public class BootstrapConfig {
        
        /**
         * LOGGER Logger variable
         */
        private static final Logger LOGGER = LoggerFactory.getLogger(BootstrapConfig.class);
        
        /**
         * Loader for product ref data into cache
         */
        @Autowired
        private ProductCacheLoader productCacheLoader;
        
        /**
         * esbMessageListener ESBMessageListener
         */
        @Autowired
        private ESBMessageListener esbMessageListener;
        
        /**
         * transactionTimedOutListener TransactionTimedOutListener
         */
        @Autowired
        private TransactionListener transactionListener;
        
        /**
         * priceListener PriceListener
         */
        @Autowired
        private PriceListener priceListener;
        
        @Autowired
        private MBSStompClient mbsStompClient;
        
        @Autowired
        private TokenService tokenService;
        @Autowired
        private CDXClientApi cdxClientApi;
        @Autowired
        private StreamingClientProperties streamingClientProperties;
        
        /**
         * @throws MBSBaseException
         * @throws CqException
         * @throws CqExistsException
         * @throws RegionNotFoundException
         * @throws InterruptedException
         */
        @EventListener(ApplicationReadyEvent.class)
        public void startESBClientAsyncConsumer()
                throws MBSBaseException, CqException, CqExistsException, RegionNotFoundException, InterruptedException,
                Exception {
                LOGGER.info("From BootstrapConfig: esbMessageListener starting..");
                esbMessageListener.startESBClientAsyncConsumer();
        }
        
        /**
         * @throws MBSBaseException
         * @throws CqException
         * @throws CqExistsException
         * @throws RegionNotFoundException
         * @throws InterruptedException
         */
        @EventListener(ApplicationReadyEvent.class)
        public void startTransactionListener()
                throws MBSBaseException, CqException, CqExistsException, RegionNotFoundException, InterruptedException,
                Exception {
                LOGGER.info("From BootstrapConfig: transactionListener starting..");
                transactionListener.startCqListener();
                LOGGER.info("From BootstrapConfig: transactionListener started.");
        }
        
        /**
         * @throws MBSBaseException
         * @throws CqException
         * @throws CqExistsException
         * @throws RegionNotFoundException
         * @throws InterruptedException
         */
        @EventListener(ApplicationReadyEvent.class)
        public void startPriceListener()
                throws MBSBaseException, CqException, CqExistsException, RegionNotFoundException, InterruptedException,
                Exception {
                LOGGER.info("From BootstrapConfig: PriceListener starting..");
                priceListener.startCqListener();
                LOGGER.info("From BootstrapConfig: PriceListener started.");
        }
        
        /**
         * startStompClient
         *
         * @throws CqException
         * @throws CqExistsException
         * @throws RegionNotFoundException
         * @throws InterruptedException
         * @throws Exception
         */
        @EventListener(ApplicationReadyEvent.class)
        @DependsOn({"tokenService","streamingClientProperties"})
        public void startStompClient() {
                LOGGER.info("BootstrapConfig startStompClient called");
                String sessionId = null;
                try {
                        LOGGER.info("Trying again for connecting to stream server " + mbsStompClient.getWsUrl());
                        mbsStompClient.setTokenService(tokenService);
                        MBSStompUtil.MAX_RETRY= streamingClientProperties.getRetryMaxAttempts();
                        MBSStompUtil.SLEEP_INTERVAL= streamingClientProperties.getRetrySleep();
                        LOGGER.info("Stomp client initialized with {} {} " ,tokenService,streamingClientProperties);
                        LOGGER.info("Using RetryMaxAttempts {} and sleep between retry {}" , MBSStompUtil.MAX_RETRY,
                                MBSStompUtil.SLEEP_INTERVAL);
                        try {
                                sessionId = tokenService.getCurrentSessionId();
                                if(!cdxClientApi.isSessionValid(sessionId)) {
                                        sessionId = MBSStompUtil.getValidCdxSession(tokenService);
                                }
                                if(sessionId != null && !sessionId.isEmpty()) {
                                        LOGGER.info("Got session id for connecting ws {} and is not null",
                                                MBSPortalUtils.getLeftPaddedString(sessionId));
                                        mbsStompClient.connectToWsServer(sessionId);
                                        LOGGER.info("startStompClient started");
                                } else {
                                        LOGGER.error("startStompClient : Not connected after max retry ");
                                }
                        } catch(Exception e) {
                                LOGGER.error("startStompClient : Could not be initialized exception {}", e);
                        }
                } catch(Throwable t) {
                        LOGGER.error("startStompClient : Not connected ", t);
                }
        }
}
