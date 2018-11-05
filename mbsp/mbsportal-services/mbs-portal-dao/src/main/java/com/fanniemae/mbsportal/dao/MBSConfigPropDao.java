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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.model.MBSConfigProp;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * @author g8uaxt Created on 1/16/2018.
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MBSConfigPropDao extends MBSBaseDao<MBSConfigProp> {

    @Autowired
    RegionService cache;
    private String regionName = "MBSPConfigProp";
    @InjectLog
    private Logger LOGGER;

    @PostConstruct
    public void initConfig() {
        LOGGER.debug("init called..MBSConfigPropDao" + cache + regionName);
        getBaseDaoWrapper().setCache(cache);
        getBaseDaoWrapper().setRegionName(regionName);
    }

    @Override
    public Region<Long, MBSConfigProp> getStorageRegion() {
        return getBaseDaoWrapper().getCache().getRegion(regionName);
    }

    /**
     * saveOrUpdate saving key for prop
     * 
     * @param obj
     */
    @Override
    public void saveOrUpdate(MBSConfigProp obj) throws MBSBaseException {
        super.saveOrUpdate(obj);
        LOGGER.debug("saved in gemfire obj id {} in MBSConfigPropDao class", obj.getId());
    }

    /**
     * 
     * @param key
     *            the key
     * @return MBSConfigProp
     * @throws MBSBaseException
     */
    public MBSConfigProp removeKey(String key) throws MBSBaseException {
        return (MBSConfigProp) super.removeById(key);
    }
}
