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

import com.fanniemae.mbsportal.model.MBSParty;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * 
 * @author g8upjv
 */
@Service
public class MBSPartyDao extends MBSBaseDao<MBSParty> {
    
    @InjectLog
    private Logger LOGGER;

    @Autowired
    RegionService cache;
    private String regionName = "MBSParty";

    /**
     * This is post construct method where the region name is initialized
     */
    @PostConstruct
    public void initConfig() {
        LOGGER.debug("init called..MBSParty" + cache + regionName);
        getBaseDaoWrapper().setCache(cache);
        getBaseDaoWrapper().setRegionName(regionName);
    }

    /**
     * @return Region<String, MBSParty>
     */
    @Override
    public Region<String, MBSParty> getStorageRegion() {
        LOGGER.debug("Storage region for Party request in MBSPartyDao class");
        return getBaseDaoWrapper().getCache().getRegion("MBSParty");
    }
    
    /**
     * 
     * @param sellerSerivcerNo
     * @return MBSParty
     * @throws MBSBaseException
     */
    public MBSParty getParty(String sellerSerivcerNo) throws MBSBaseException {
        return (MBSParty) super.getById(sellerSerivcerNo);
    }
    
    /**
     * 
     * @param sellerSerivcerNo the sellerSerivcerNo
     * @return MBSParty
     * @throws MBSBaseException
     */
    public MBSParty removeParty(String sellerSerivcerNo) throws MBSBaseException {
        return (MBSParty) super.removeById(sellerSerivcerNo);
    }
   
}
