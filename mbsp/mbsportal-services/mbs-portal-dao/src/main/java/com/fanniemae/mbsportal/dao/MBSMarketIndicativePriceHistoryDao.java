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

package com.fanniemae.mbsportal.dao;

import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.constants.DAOConstants;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePriceHistory;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSDataAccessException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * @author: Rajiv Chaudhuri
 * @Date: Jul 2, 2018
 * @File: com.fanniemae.mbsportal.dao.MBSMarketIndicativePriceHistoryDao.java
 * @Revision:
 * @Description: MBSMarketIndicativePriceHistoryDao.java
 */
@Service
public class MBSMarketIndicativePriceHistoryDao extends MBSBaseDao<MBSMarketIndicativePriceHistory> {

    @InjectLog
    private Logger LOGGER;

    /**
     * ID service dao initialised for
     */
    @Autowired
    private IDServiceDao idServiceDao;

    @Autowired
    RegionService cache;
    private String regionName = "MBSMarketIndicativePriceHistory";

    /**
     * Get the event storage region
     * 
     * @return Region<Long, MBSMarketIndicativePriceHistory>
     */
    @Override
    public Region<Long, MBSMarketIndicativePriceHistory> getStorageRegion() {
        return getBaseDaoWrapper().getCache().getRegion("MBSMarketIndicativePriceHistory");
    }

    /**
     * This is post construct method where the region name is initialized
     */
    @PostConstruct
    public void initConfig() {
        LOGGER.debug("init called..MBSMarketIndicativePriceHistory" + cache + regionName);
        getBaseDaoWrapper().setCache(cache);
        getBaseDaoWrapper().setRegionName(regionName);
    }

    /**
     * @param mBSMarketIndicativePriceHistory
     */
    @Override
    public void saveOrUpdate(MBSMarketIndicativePriceHistory mBSMarketIndicativePriceHistory) throws MBSBaseException {

        LOGGER.debug("Entering saveOrUpdate method in MBSMarketIndicativePriceHistoryDao");
        
        if (Objects.equals(mBSMarketIndicativePriceHistory.getHistoryId(), null)
                || mBSMarketIndicativePriceHistory.getHistoryId().longValue() < 0) {
            
            String seqId = idServiceDao.getSeqId(DAOConstants.IDTypes.MARKET_INDICATIVE_PRICE_HISTORY_ID,
                    DAOConstants.SEQUENCE_TWO_MILLIONS);
            
            if (null == seqId || "".equals(seqId)) {
                LOGGER.error("seqId from gemfire {} in MBSMarketIndicativePriceHistoryDao class", seqId);
                throw new MBSDataAccessException("Seq Id is null for " + mBSMarketIndicativePriceHistory);
                
            }
            mBSMarketIndicativePriceHistory.setHistoryId(Long.valueOf(seqId));
            
        }
        super.saveOrUpdate(mBSMarketIndicativePriceHistory);
        LOGGER.debug("saved in gemfire obj id {} in MBSMarketIndicativePriceHistory class",
                mBSMarketIndicativePriceHistory.getId());
        
        LOGGER.debug("Exiting saveOrUpdate method in MBSMarketIndicativePriceHistoryDao");
    }

    /**
     * @param mBSMarketIndicativePriceHistoryLst
     *            the list of MBSMarketIndicativePriceHistory
     */
    @Override
    public void putAll(List<MBSMarketIndicativePriceHistory> mBSMarketIndicativePriceHistoryLst)
            throws MBSBaseException {

        LOGGER.debug("Entering putAll method in MBSMarketIndicativePriceHistoryDao");
        
        for (MBSMarketIndicativePriceHistory mBSMarketIndicativePriceHistory : mBSMarketIndicativePriceHistoryLst) {
            
            if (Objects.equals(mBSMarketIndicativePriceHistory.getHistoryId(), null)
                    || mBSMarketIndicativePriceHistory.getHistoryId().longValue() < 0) {
                
                String seqId = idServiceDao.getSeqId(DAOConstants.IDTypes.MARKET_INDICATIVE_PRICE_HISTORY_ID,
                        DAOConstants.SEQUENCE_TWO_MILLIONS);
                
                if (null == seqId || "".equals(seqId)) {
                    LOGGER.error("seqId from gemfire {} in MBSMarketIndicativePriceHistoryDao class", seqId);
                    throw new MBSDataAccessException("Seq Id is null for " + mBSMarketIndicativePriceHistory);
                    
                }
                mBSMarketIndicativePriceHistory.setHistoryId(Long.valueOf(seqId));
            }
        }
        super.putAll(mBSMarketIndicativePriceHistoryLst);
        
        LOGGER.debug("Exiting putAll method in MBSMarketIndicativePriceHistoryDao");
    }

}
