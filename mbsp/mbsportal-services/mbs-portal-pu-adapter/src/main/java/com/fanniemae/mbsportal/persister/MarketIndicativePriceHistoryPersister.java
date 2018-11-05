/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.persister;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.geode.cache.CommitConflictException;
import org.apache.geode.cache.TransactionDataNodeHasDepartedException;
import org.apache.geode.cache.TransactionDataRebalancedException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fanniemae.fnmpfj.gemfire.dao.BaseDaoImpl;
import com.fanniemae.fnmpfj.gemfire.utils.client.GemfireUtil;
import com.fanniemae.mbsportal.dao.MBSBaseDao;
import com.fanniemae.mbsportal.dao.MBSMarketIndicativePriceHistoryDao;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePriceHistory;
import com.fanniemae.mbsportal.persist.BasePersister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSDataAccessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 2, 2018
 * @File: com.fanniemae.mbsportal.persister.TransactionPersister.java
 * @Revision:
 * @Description: TransactionPersister.java
 */
@SuppressWarnings("rawtypes")
@Component
public class MarketIndicativePriceHistoryPersister extends BasePersister {

    /**
     * 
     * LOGGER Logger variable
     */
    @InjectLog
    private Logger LOGGER;

    /**
     * 
     * mBSMarketIndicativePriceHistoryDao MBSMarketIndicativePriceHistoryDao
     */
    @Autowired
    private MBSMarketIndicativePriceHistoryDao mBSMarketIndicativePriceHistoryDao;

    /**
     * 
     * Overriding the base method to persist the record(s)
     * 
     * @param transformationObject
     *            TransformationObject
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    @Override
    public void persist(TransformationObject transformationObject) throws MBSBaseException {

        LOGGER.debug("Entering persist method in MBSMarketIndicativePriceHistoryPersister");
        
        List<MBSMarketIndicativePriceHistory> mBSMarketIndicativePriceHistoryLst = (List<MBSMarketIndicativePriceHistory>) transformationObject
                .getTargetPojo();

        try {
            BaseProcessor.beginTransaction(GemfireUtil.getTransactionManager());

            mBSMarketIndicativePriceHistoryDao.putAll(mBSMarketIndicativePriceHistoryLst);

            BaseProcessor.commitTransaction(GemfireUtil.getTransactionManager());

        } catch (CommitConflictException | TransactionDataRebalancedException
                | TransactionDataNodeHasDepartedException cce) {
            LOGGER.error("CommitConflict GF: {}", cce);
            BaseProcessor.rollbackTransaction(GemfireUtil.getTransactionManager());
            throw new MBSDataAccessException("CommitConflict GF",
                    MBSExceptionConstants.SYSTEM_EXCEPTION, cce);
            
        } catch (Exception e) {
            LOGGER.error("persist: {}", e);
            BaseProcessor.handleTransactionException(GemfireUtil.getTransactionManager(), e);
            throw new MBSDataAccessException("Error while persist history data",
                    MBSExceptionConstants.SYSTEM_EXCEPTION, e);
            
        }
        LOGGER.debug("Exiting persist method in MBSMarketIndicativePriceHistoryPersister");

    }

    /**
     * 
     * Overriding the clearAll() method
     * 
     * @throws MBSBaseException
     */
    @Override
    public void clearAll() throws MBSBaseException {
        // clear transaction requests
        Set<Long> keySets = mBSMarketIndicativePriceHistoryDao.getStorageRegion().keySetOnServer();
        for (final Long key : keySets) {
            mBSMarketIndicativePriceHistoryDao.getStorageRegion().destroy(key);
        }
        LOGGER.debug("cleared all history  ...");
    }

    /**
     * 
     * Overriding the clear() method
     * 
     * @param key
     *            String
     * @throws MBSBaseException
     */
    @Override
    public void clear(String key) throws MBSBaseException {
        if (!StringUtils.isEmpty(key) && StringUtils.isNumeric(key)) {
            mBSMarketIndicativePriceHistoryDao.getStorageRegion().destroy(Long.valueOf(key));
        }
        LOGGER.debug("cleared records for key  ..." + key);
    }

    /**
     * This method is not being used
     * 
     * @return BaseDaoImpl
     * @throws MBSBaseException
     */
    @Override
    public BaseDaoImpl getDao() throws MBSBaseException {
        return null;
    }

    /**
     * This method returns the MBSBaseDao version of
     * MBSMarketIndicativePriceHistoryDao
     * 
     * @throws MBSBaseException
     * @return MBSBaseDao
     */
    @Override
    public MBSBaseDao getBaseDao() throws MBSBaseException {
        return mBSMarketIndicativePriceHistoryDao;
    }
}
