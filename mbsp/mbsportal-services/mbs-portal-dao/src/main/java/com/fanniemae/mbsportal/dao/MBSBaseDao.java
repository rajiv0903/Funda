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

import java.util.Collection;
import java.util.List;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionService;
import org.springframework.beans.factory.annotation.Autowired;

import com.fanniemae.fnmpfj.gemfire.dao.BaseEntity;
import com.fanniemae.fnmpfj.gemfire.dao.CallBackArgument;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSDataAccessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;

/**
 * @author g8uaxt Created on 9/25/2017.
 */
// @Service //TODO: check it later whether it is required?
// @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MBSBaseDao<T extends BaseEntity> {
    @SuppressWarnings("rawtypes")
    @Autowired
    BaseDaoWrapper baseDaoWrapper;

    /**
     * get storage region
     * 
     * @return Region
     */
    @SuppressWarnings("rawtypes")
    public Region getStorageRegion() {
        return baseDaoWrapper.getStorageRegion();
    }

    /**
     * get basedaowrapper
     * 
     * @return BaseDaoWrapper
     */
    @SuppressWarnings("rawtypes")
    public BaseDaoWrapper getBaseDaoWrapper() {
        return baseDaoWrapper;
    }

    /**
     * set timestamp
     * 
     * @param useTimeStamps
     */
    public void setUseTimeStamps(boolean useTimeStamps) {
        baseDaoWrapper.setUseTimeStamps(useTimeStamps);
    }

    /**
     * set region name
     * 
     * @param regionName
     */
    public void setRegionName(String regionName) {
        baseDaoWrapper.setRegionName(regionName);
    }

    /**
     * get details by id
     * 
     * @param id
     * @return BaseEntity
     * @throws MBSBaseException
     */
    public BaseEntity getById(Object id) throws MBSBaseException {
        try {
            return baseDaoWrapper.getById(id);
        } catch (Exception e) {
            throw new MBSDataAccessException("Failed to get the Object by ID" +id, MBSExceptionConstants.SYSTEM_EXCEPTION, e);
        }
    }

    /**
     * remove details by id
     * 
     * @param id
     * @return BaseEntity
     * @throws MBSBaseException
     */
    public BaseEntity removeById(Object id) throws MBSBaseException {
        try {
            return baseDaoWrapper.removeById(id);
        } catch (Exception e) {
            throw new MBSDataAccessException("Failed to remove the Object by ID", MBSExceptionConstants.SYSTEM_EXCEPTION, e);
        }
    }

    /**
     * remove all
     * 
     * @return int
     * @throws MBSBaseException
     */
    public int removeAll() throws MBSBaseException {
        try {
            return baseDaoWrapper.removeAll();
        } catch (Exception e) {
            throw new MBSDataAccessException("Failed to remove all Objects", MBSExceptionConstants.SYSTEM_EXCEPTION, e);
        }
    }

    /**
     * remove all with persister param
     * 
     * @param persist
     * @return int
     */
    public int removeAll(boolean persist) {
        return baseDaoWrapper.removeAll(persist);
    }

    /**
     * update all in the list
     * 
     * @param batch
     */
    public void putAll(List<T> batch) throws MBSBaseException {
        try {
            baseDaoWrapper.putAll(batch);
        } catch (Exception e) {
            throw new MBSDataAccessException("Failed to save batch Object", MBSExceptionConstants.SYSTEM_EXCEPTION, e);
        }
    }

    /**
     * get all from gemfire
     * 
     * @return List<T>
     * @throws MBSBaseException
     */
    public List<T> getAll() throws MBSBaseException {
        try {
            return baseDaoWrapper.getAll();
        } catch (Exception e) {
            throw new MBSDataAccessException("Failed to Get all Objects", MBSExceptionConstants.SYSTEM_EXCEPTION, e);
        }
    }

    /**
     * query gemfire based on params
     * 
     * @param whereClause
     * @param params
     * @return Collection<T>
     * @throws MBSBaseException
     */
    public Collection<T> query(String whereClause, Object... params) throws MBSBaseException {
        try {
            return baseDaoWrapper.query(whereClause, params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MBSDataAccessException("Failed to Query whereClause:" + whereClause, MBSExceptionConstants.SYSTEM_EXCEPTION, e);
        }
    }

    /**
     * get cache
     * 
     * @return RegionService
     */
    public RegionService getCache() {
        return baseDaoWrapper.getCache();
    }

    /**
     * set cache
     * 
     * @param cache
     */
    public void setCache(RegionService cache) {
        baseDaoWrapper.setCache(cache);
    }

    /**
     * init method
     * 
     * @param locators
     * @param serverGroup
     * @param regionName
     * @param poolName
     */
    public void init(String locators, String serverGroup, String regionName, String poolName) {
        baseDaoWrapper.init(locators, serverGroup, regionName, poolName);
    }

    /**
     * close method
     */
    public void close() {
        baseDaoWrapper.close();
    }

    /**
     * create or update in gemfire with callback
     * 
     * @param obj
     * @param callBackArgument
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    public void saveOrUpdate(T obj, CallBackArgument callBackArgument) throws MBSBaseException {
        try {
            baseDaoWrapper.saveOrUpdate(obj, callBackArgument);
        } catch (Exception e) {
            throw new MBSDataAccessException("Failed to save the Object");
        }
    }

    /**
     * create or update in gemfire without callback
     * 
     * @param obj
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    public void saveOrUpdate(T obj) throws MBSBaseException {
        try {
            baseDaoWrapper.saveOrUpdate(obj);
        } catch (Exception e) {
            throw new MBSDataAccessException("Failed to save the Object");
        }
    }

}
