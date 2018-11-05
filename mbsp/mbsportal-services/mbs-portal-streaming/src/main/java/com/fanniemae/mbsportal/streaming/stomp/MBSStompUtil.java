/*
 * Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 * reserved under the copyright laws of the United States and international
 * conventions. Use of a copyright notice is precautionary only and does not
 * imply publication or disclosure. This software contains confidential
 * information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 * is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.streaming.stomp;

import com.fanniemae.mbsportal.service.TokenService;
import com.fanniemae.mbsportal.streaming.stomp.client.MBSStompClient;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author g8uaxt Created on 8/2/2018.
 */

public class MBSStompUtil {
        /**
         * LOGGER Logger variable
         */
        private static final Logger LOGGER = LoggerFactory.getLogger(MBSStompUtil.class);
        public static int SLEEP_INTERVAL = 3;
        public static int MAX_RETRY = 15;
        private static List<MBSStompClient> clientList = new ArrayList<>();
        
        /**
         * addClient
         *
         * @param key
         * @param mbsStompClient
         */
        public static void addClient(String key, MBSStompClient mbsStompClient) {
                clientList.add(mbsStompClient);
        }
        
        /**
         * getClient
         *
         * @param key
         * @return
         * @throws MBSBaseException
         */
        public static MBSStompClient getClient(String key) throws MBSBaseException {
                if(clientList.isEmpty()) {
                        throw new MBSBaseException("No Client instance available");
                }
                return clientList.get(0);
        }
        
        /**
         * print the infos
         */
        public static void printClientInfos() {
        
        }
        
        /**
         * Iterate through list of clients and pick by url
         *
         * @param msgPublishServerLenderUrl
         * @return
         */
        public static MBSStompClient getStompClientByUrl(String msgPublishServerLenderUrl) {
                //TODO: yet to be implemented since as of now, only one url supported for lender
                return clientList.get(0);
        }
        
        public static String getValidCdxSession(TokenService tokenService)
                throws MBSBaseException, InterruptedException {
                //thread sync needed?
                String sessionId = null;
                boolean isLastTry = false;
                tokenService.resetSessionAndToken();
                //TODO: move the prop beans into util folder
                for(int i = 0; i < MAX_RETRY; i++) {
                        try {
                                if(i + 1 == MAX_RETRY) {
                                        //alert EMM in case it fails
                                        LOGGER.info("Last try {} to get session id...", MAX_RETRY);
                                        isLastTry = true;
                                }
                                sessionId = tokenService.getValidSessionId();
                                if(sessionId != null) {
                                        LOGGER.info("got session from cdx {} after retry {}, moving forward",
                                                MBSPortalUtils.getLeftPaddedString(sessionId),i);
                                        break;
                                }
                                
                        } catch(Exception e) {
                                if(isLastTry) {
                                        LOGGER.error("All try {} times went un-successful and logging for alert...",
                                                MAX_RETRY, e);
                                        MBSExceptionConstants
                                                .logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER,
                                                        MBSExceptionConstants.SYSTEM_EXCEPTION.toString(),
                                                        e.getMessage(), "MBSStompClient:getValidCdxSession", sessionId,
                                                        "");
                                        MBSExceptionConstants.resetLogAlert();
                                } else {
                                        LOGGER.warn("Will try again...", e);
                                        Thread.sleep(SLEEP_INTERVAL * 1000);
                                }
                        }
                }
                return sessionId;
        }
}
