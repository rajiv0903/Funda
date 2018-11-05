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

import java.util.Objects;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.model.MBSProductTransactionXref;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * 
 * @author g8upjv
 * @Version:
 * @Date Jul 19 2018
 * @Description: To interact with MBSProductTransactionXref Region
 */

@Service
public class MBSProductTransactionXrefDao extends MBSBaseDao<MBSProductTransactionXref> {

    @InjectLog
    private Logger LOGGER;

    @Autowired
    RegionService cache;
    private String regionName = "MBSProductTransactionXref";

    /**
     * Get the event storage region
//     * @return Region<String, MBSProductTransactionXref>
     */
    @Override
    public Region<String, MBSProductTransactionXref> getStorageRegion() {
        return getBaseDaoWrapper().getCache().getRegion("MBSProductTransactionXref");
    }

    /**
     * This is post construct method where the region name is initialized
     */
    @PostConstruct
    public void initConfig() {
        LOGGER.debug("init called..MBSProductTransactionXref" + cache + regionName);
        getBaseDaoWrapper().setCache(cache);
        getBaseDaoWrapper().setRegionName(regionName);
    }
    
    /**
     * @param obj
     */
    @Override
    public void saveOrUpdate(MBSProductTransactionXref obj) throws MBSBaseException {
            super.saveOrUpdate(obj);
            LOGGER.debug("saved in gemfire obj id {} in MBSProductTransactionXref class", obj.getId());
    }
    
    /**
     * 
     * 
     * @param key Object
     * @throws MBSBaseException
     */
    public void clear(Object key) throws MBSBaseException {
        if(Objects.nonNull(key)){
            this.getStorageRegion().destroy(key);
        }
        LOGGER.debug("cleared MBSProductTransactionXref records for key  ..."+key);
    }
    
    public void clearAll() throws MBSBaseException {
        // clear transaction requests
        Set<?> keySets = this.getStorageRegion().keySetOnServer();
        for (final Object key : keySets) {
                this.getStorageRegion().destroy(key);
        }
        LOGGER.debug("cleared all MBSProductTransactionXref transactions  ...");
}

}
