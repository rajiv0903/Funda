package com.fanniemae.mbsportal.util;

import org.apache.geode.cache.CacheTransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * Dao Util for using to handle commit conflict situation
 * @author g8uaxt Created on 7/6/2018.
 */

public class TransactionManagerUtil {
        
        private static final Logger LOGGER = LoggerFactory.getLogger(TransactionManagerUtil.class);
        
        /**
         * Begin transaction
         *
         * @param cacheTransactionManager
         */
        public static void beginTransaction(CacheTransactionManager cacheTransactionManager) {
                if(null != cacheTransactionManager && cacheTransactionManager.exists()) {
                        cacheTransactionManager.begin();
                        LOGGER.debug("Gemfire Transaction begin for MBS pipeline");
                }
        }
        
        /**
         * Commit transation
         *
         * @param cacheTransactionManager
         */
        public static void commitTransaction(CacheTransactionManager cacheTransactionManager) {
                if(null != cacheTransactionManager && cacheTransactionManager.exists()) {
                        cacheTransactionManager.commit();
                        LOGGER.debug("Gemfire Transaction committed for ODS pipeline");
                }
                
        }
        
        /**
         * @param cacheTransactionManager
         */
        public static void rollbackTransaction(CacheTransactionManager cacheTransactionManager) {
                if(cacheTransactionManager != null && cacheTransactionManager.exists()) {
                        cacheTransactionManager.rollback();
                }
        }
        
        /**
         * Handle transaction exception
         *
         * @param cacheTransactionManager
         * @param e
         * @throws MBSBaseException
         */
        public static void handleTransactionException(CacheTransactionManager cacheTransactionManager, Exception e)
                throws MBSBaseException {
                
                if(cacheTransactionManager != null && cacheTransactionManager.exists()) {
                        cacheTransactionManager.rollback();
                        LOGGER.warn("Exception occurred - {} - Rolled back Gemfire Transaction for the MBS ");
                }
        }
        
        
}
