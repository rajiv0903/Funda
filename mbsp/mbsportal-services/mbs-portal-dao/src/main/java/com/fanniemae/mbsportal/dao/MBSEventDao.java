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

import org.apache.commons.lang.StringUtils;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.model.MBSEvent;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Nov 20, 2017
 * @Time 3:32:12 PM
 * 	com.fanniemae.mbsportal.dao
 * 	MBSEventDao.java
 * @Description: To interact with MBSEvent Region
 */

@Service
public class MBSEventDao extends MBSBaseDao<MBSEvent> {

    @InjectLog
    private Logger LOGGER;

    @Autowired
    RegionService cache;
    private String regionName = "MBSEvent";

    /**
     * Get the event storage region
     * @return Region<String, MBSEvent>
     */
    @Override
    public Region<String, MBSEvent> getStorageRegion() {
        return getBaseDaoWrapper().getCache().getRegion("MBSEvent");
    }

    /**
     * This is post construct method where the region name is initialized
     */
    @PostConstruct
    public void initConfig() {
        LOGGER.debug("init called..MBSEvent" + cache + regionName);
        getBaseDaoWrapper().setCache(cache);
        getBaseDaoWrapper().setRegionName(regionName);
    }
    
    /**
     * @param obj
     */
    @Override
    public void saveOrUpdate(MBSEvent obj) throws MBSBaseException {

        if (StringUtils.isNotBlank(obj.getUserName())) {
            // set current time ms
            if(obj.getEventTimeStamp() == null || obj.getEventTimeStamp().longValue() < 0){
                obj.setEventTimeStamp(MBSPortalUtils.getLocalDateCurrentTimeStamp());
            }
            super.saveOrUpdate(obj);
            LOGGER.debug("saved in gemfire obj id {} in MBSEvent class", obj.getId());
        } else {
            LOGGER.debug("Failed to save in gemfire obj id {} in MBSEvent class", obj.getId());
        }
    }
    
    /**
     * 
     * @param userName
     * @return MBSEvent
     * @throws MBSBaseException
     */
    public MBSEvent getEvent(String userName) throws MBSBaseException {
        return (MBSEvent) super.getById(userName);
    }

}
