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
/**
 */

import com.fanniemae.mbsportal.api.cdx.CDXClientApi;
import com.fanniemae.mbsportal.api.service.TradeServicePoller;
import com.fanniemae.mbsportal.service.TokenService;
import com.fanniemae.mbsportal.streaming.stomp.client.MBSStompClient;
import com.fanniemae.mbsportal.utils.cache.MBSPAppCache;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author g8uaxt
 */

@Configuration
@EnableScheduling
public class MBSScheduler {
        
        /**
         * LOGGER Logger variable
         */
        @InjectLog
        private Logger LOGGER;
        
        /**
         * tradeServicePoller TradeServicePoller
         */
        @Autowired
        private TradeServicePoller tradeServicePoller;
        /**
         * MBSP product app cache component
         */
        @Autowired
        private MBSPAppCache mbspAppCache;
        @Autowired
        private MBSStompClient mbsStompClient;
        @Autowired
        private TokenService tokenService;
        @Autowired
        private CDXClientApi cdxClientApi;
/*    @Autowired
    private MBSThreadPoolTaskExecutor mbsThreadPoolTaskExecutor;*/
        
        /**
         * initialization method
         */
        @PostConstruct
        public void init() {
                // refreshToken();
                LOGGER.info("Started TradeService poller");
        }
        
        /**
         * scheduler for trade service
         */
        // @Scheduled(cron = "*/30 * * * * *")//${ts.schedule.delay.milli})
        @Scheduled(fixedDelay = 1000, initialDelay = 30000)
        public void scheduleFixedDelayTask() {
                try {
                        LOGGER.debug("Started to post TradeService");
            /*
             * if(null == tokenRefresher.getSessionId()) {
             * tokenRefresher.refreshLogin(); }
             */
                        tradeServicePoller.processTrades();
                        LOGGER.debug("Ending to post TradeService");
                } catch(MBSBaseException e) {
                        LOGGER.error("Error while posting Trade", e);
                } catch(Exception ex) {
                        LOGGER.error("Error while posting Trade", ex);
                }
        }
        
        /**
         * print cache stat for information
         */
        @Scheduled(fixedDelay = 30 * 60 * 1000, initialDelay = 30000)
        public void printCacheStats() {
                LOGGER.debug("Start print stats");
                mbspAppCache.printStats();
                LOGGER.debug("End print stats");
                //mbsThreadPoolTaskExecutor.printStats();
        }
        
        /**
         * job to re-establish connection to WS for publishing
         */
        @Scheduled(fixedDelay = 2 * 60 * 1000, initialDelay = 30000)
        public void wsConnectionManager() {
                LOGGER.debug("Start wsConnectionManager");
                try {
                        boolean status = mbsStompClient.getConnectedSession().get().isConnected();
                        LOGGER.debug("wsConnectionManager ws status {}", status);
                        if(status) {
                                LOGGER.debug("wsConnectionManager connection active");
                        } else {
                                LOGGER.info("wsConnectionManager trying to re-connection from job");
                                reConnectOnFailOver(mbsStompClient); //it should be successful ?
                                if(mbsStompClient
                                        .getConnectedSession().get().isConnected()){
                                        LOGGER.info("wsConnectionManager connected back again!");
                                }else{
                                        LOGGER.error("wsConnectionManager Some issue. Not connected");
                                }
                         }
                      } catch(Throwable t) {
                        LOGGER.error("Error while re-connect WS Throwable handling here ", t);
                        reConnectOnFailOver(mbsStompClient);
                }
                LOGGER.debug("End wsConnectionManager");
        }
        
        /**
         * reConnectOnFailOver //TODO: move it to connection manager later
         * @param mbsStompClient
         */
        private void reConnectOnFailOver(MBSStompClient mbsStompClient) {
                //try connect again here
                String sessionId = null;
                try {
                        sessionId = tokenService.getCurrentSessionId();
                        if(!cdxClientApi.isSessionValid(sessionId)) {
                                //let reconnect take care of session id
                                mbsStompClient.reConnectToWsServer(null);
                        }else{
                                mbsStompClient.reConnectToWsServer(sessionId);
                        }
                        LOGGER.debug("re-connected to WS from wsConnectionManager and isConnected {}",mbsStompClient
                                .getConnectedSession().get().isConnected());
                } catch(MBSBaseException e) {
                        LOGGER.error("Error while re-connect WS MBSBaseException ", e);
                } catch(InterruptedException e) {
                        LOGGER.error("Error while re-connect WS InterruptedException ", e);
                } catch(ExecutionException e) {
                        LOGGER.error("Error while re-connect WS ExecutionException  ", e);
                        // EMM alert already raised at this point?
                } catch(Throwable th) {
                        LOGGER.error("Error while re-connect WS Throwable  ", th);
                }
        }
}