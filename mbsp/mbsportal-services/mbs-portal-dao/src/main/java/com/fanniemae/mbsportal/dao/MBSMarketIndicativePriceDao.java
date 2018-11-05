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

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionService;
import org.apache.geode.cache.query.SelectResults;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * 
 * @author Pradeep
 * @Version:
 * @Date Jun 05 2018
 * @Description: To interact with MBSMarketIndicativePrice Region
 */

@Service
public class MBSMarketIndicativePriceDao extends MBSBaseDao<MBSMarketIndicativePrice> {

    @InjectLog
    private Logger LOGGER;

    @Autowired
    RegionService cache;
    private String regionName = "MBSMarketIndicativePrice";

    /**
     * Get the event storage region
     * @return Region<String, MBSMarketIndicativePrice>
     */
    @Override
    public Region<String, MBSMarketIndicativePrice> getStorageRegion() {
        return getBaseDaoWrapper().getCache().getRegion("MBSMarketIndicativePrice");
    }

    /**
     * This is post construct method where the region name is initialized
     */
    @PostConstruct
    public void initConfig() {
        LOGGER.debug("init called..MBSMarketIndicativePrice" + cache + regionName);
        getBaseDaoWrapper().setCache(cache);
        getBaseDaoWrapper().setRegionName(regionName);
    }
    
    /**
     * @param obj
     */
    @Override
    public void saveOrUpdate(MBSMarketIndicativePrice obj) throws MBSBaseException {
            super.saveOrUpdate(obj);
            LOGGER.debug("saved in gemfire obj id {} in MBSMarketIndicativePrice class", obj.getId());
    }
    
    /**
     * Return last updated record 
     * 
     * @return lastUpdatedRecord  MBSMarketIndicativePrice
     * @throws MBSBaseException
     */
    public MBSMarketIndicativePrice getLastUpdatedRecord() throws MBSBaseException {
        
        MBSMarketIndicativePrice lastUpdatedRecord = null;
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("1=1 order by lastUpdated desc limit 1 ");
        SelectResults<MBSMarketIndicativePrice> mBSMarketIndicativePriceLst = (SelectResults<MBSMarketIndicativePrice>)super.query(queryBuilder.toString());
        if (CollectionUtils.isNotEmpty(mBSMarketIndicativePriceLst)) {
            lastUpdatedRecord =  mBSMarketIndicativePriceLst.asList().get(0);
        }
        return lastUpdatedRecord;
        
    }

}
