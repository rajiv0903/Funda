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

import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.model.MBSExceptionLookup;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * 
 * This class conects to the Exception Lookup region for querying and updating
 * 
 * @author g8upjv
 */
@Service
public class MBSExceptionLookupDao extends MBSBaseDao<MBSExceptionLookup> {
    
    /**
     * 
     * LOGGER Logger variable
     */
    @InjectLog
    private Logger LOGGER;

    /**
     * 
     * cache RegionService
     */
    @Autowired
    RegionService cache;
    
    /**
     * 
     * regionName String
     */
    private String regionName = "MBSExceptionLookup";

    
    /**
     * 
     * This is post construct method where the region name is initialized
     */
    @PostConstruct
    public void initConfig() {
        LOGGER.debug("init called..MBSExceptionLookup" + cache + regionName);
        getBaseDaoWrapper().setCache(cache);
        getBaseDaoWrapper().setRegionName(regionName);
    }

    /**
     * 
     * @return Region<String, MBSProduct>
     */
    @Override
    public Region<String, MBSExceptionLookup> getStorageRegion() {
        LOGGER.debug("Storage region for Exception request in MBSExceptionLookupDao class");
        return getBaseDaoWrapper().getCache().getRegion("MBSExceptionLookup");
    }

    /**
     * 
     * Save or Update 
     * @param obj MBSExceptionLookup
     * @throws MBSBaseException
     */
    @Override
    public void saveOrUpdate(MBSExceptionLookup obj) throws MBSBaseException {
        super.saveOrUpdate(obj);
        LOGGER.debug("saved in gemfire obj id {} in MBSExceptionLookupDao class", obj.getId());
    }
}
