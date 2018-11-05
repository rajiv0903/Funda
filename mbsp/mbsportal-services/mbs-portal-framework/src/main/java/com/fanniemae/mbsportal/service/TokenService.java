/*
 * Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 * reserved under the copyright laws of the United States and international
 * conventions. Use of a copyright notice is precautionary only and does not
 * imply publication or disclosure. This software contains confidential
 * information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 * is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.service;

import com.fanniemae.fnmpfj.gemfire.utils.client.GemfireUtil;
import com.fanniemae.mbsportal.constants.DAOConstants;
import com.fanniemae.mbsportal.util.TransactionManagerUtil;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.cdx.token.CDXTokenRefresher;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.geode.cache.CommitConflictException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Single service to interact with DC for handling session id
 * it is synchronized to make it more optimized in a predictive way
 *
 * @author Anbu Thiru Created on 7/27/2018.
 */
@Component
public class TokenService {
        private final ReentrantLock lock = new ReentrantLock();
        @InjectLog
        private Logger LOGGER;
        @Autowired
        private CDXTokenRefresher tokenRefresher;
        @Autowired
        private ConfigService configService;
        
        /**
         * Get the session ID from ConfigProp - If not null then send it; if null go
         * for token refresher
         * will tell about the DC session and refresh it if invalid
         *
         * @return sessionId String
         * @throws MBSBaseException
         */
        public String getValidSessionId() throws MBSBaseException {
                LOGGER.debug("Entering getValidSessionId method in TokenService");
                String sessionId = null;
                String paddedId = null;
                try {
                        // TODO: create distributed lock at app/frame work level
                        lock.lock(); // node level lock
                        LOGGER.debug("locked by me for session renew");
                        sessionId = getCurrentSessionId();
                        if(sessionId != null) {
                                // go to unlock
                                LOGGER.debug("lock.getQueueLength():" + lock.getQueueLength());
                                LOGGER.debug("Unlocked it since re-using renewed session ");
                        } else {
                                TransactionManagerUtil.beginTransaction(GemfireUtil.getTransactionManager()); // cluster
                                // lock
                                sessionId = getCurrentSessionId();
                                if(sessionId == null) {
                                        LOGGER.debug("Going to get new SESSION_ID");
                                        sessionId = tokenRefresher.refreshLogin();
                                        paddedId = MBSPortalUtils.getLeftPaddedString(sessionId);
                                        LOGGER.debug("Padded Id {} saved in GF", paddedId);
//                                        LOGGER.debug("SESSION_ID {} saved in GF", sessionId);
                                        configService.saveConfigInfo(DAOConstants.SESSION_ID,
                                                tokenRefresher.encrypt(sessionId), DAOConstants.SESSION_ID_PARENT_KEY);
                                        TransactionManagerUtil.commitTransaction(GemfireUtil.getTransactionManager());
                                }
                        }
                        LOGGER.debug("lock.getQueueLength():" + lock.getQueueLength());
                } catch(CommitConflictException cce) {
                        LOGGER.error("CommitConflictException: getValidSessionId#", cce);
                        TransactionManagerUtil.rollbackTransaction(GemfireUtil.getTransactionManager());
                        LOGGER.debug("lock.getQueueLength():" + lock.getQueueLength());
                        // get one more time in case
                        sessionId = getCurrentSessionId();
                } catch(Exception e) {
                        paddedId = MBSPortalUtils.getLeftPaddedString(sessionId);
                        LOGGER.warn("Bad session/login. will re-try again for valid Padded Id: {}" , paddedId, e);
//                        LOGGER.warn("Bad session/login. will re-try again for valid SESSION_ID:" + sessionId, e);
                        TransactionManagerUtil.handleTransactionException(GemfireUtil.getTransactionManager(), e);
                        LOGGER.debug("lock.getQueueLength():" + lock.getQueueLength());
                        // get one more time in case
                        sessionId = getCurrentSessionId();
                } finally {
                        // no need now. let us test it
                        unlock();
                }
                LOGGER.debug("Exiting getValidSessionId method in TokenService");
                return sessionId;
        }
        
        /**
         * unlock utility
         */
        private void unlock() {
                LOGGER.debug("lock.getQueueLength():" + lock.getQueueLength());
                if(lock != null) { // lock.isHeldByCurrentThread()
                        lock.unlock();
                        LOGGER.debug("Unlocked it in Finally");
                }
        }
        
        /**
         * reset token in GF
         *
         * @throws MBSBaseException
         */
        public void resetSessionAndToken() throws MBSBaseException {
                LOGGER.debug("resetSessionAndToken called");
                configService.saveConfigInfo(DAOConstants.SESSION_ID, null, DAOConstants.SESSION_ID_PARENT_KEY);
                configService.saveConfigInfo(DAOConstants.AUTH_TOKEN, null, DAOConstants.SESSION_ID_PARENT_KEY);
        }
        
        /**
         * wrapper method to get session id
         *
         * @return
         * @throws MBSBaseException
         */
        public String getCurrentSessionId() throws MBSBaseException {
                return tokenRefresher.decrypt(configService.getPropValueAsString(DAOConstants.SESSION_ID));
        }
        
}
