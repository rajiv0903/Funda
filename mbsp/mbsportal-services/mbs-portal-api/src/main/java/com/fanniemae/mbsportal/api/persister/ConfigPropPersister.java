/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.api.persister;

import java.util.Set;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fanniemae.fnmpfj.gemfire.dao.BaseDaoImpl;
import com.fanniemae.mbsportal.dao.MBSBaseDao;
import com.fanniemae.mbsportal.dao.MBSConfigPropDao;
import com.fanniemae.mbsportal.model.MBSConfigProp;
import com.fanniemae.mbsportal.persist.BasePersister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 6, 2018
 * @File: com.fanniemae.mbsportal.api.persister.ConfigPropPersister.java 
 * @Revision: 
 * @Description: ConfigPropPersister.java
 */
@SuppressWarnings("rawtypes")
@Component
public class ConfigPropPersister extends BasePersister {

    /**
     * 
     * LOGGER Logger variable
     */
    @InjectLog
    private Logger LOGGER;

    /**
     * 
     * mBSConfigPropDao MBSConfigPropDao
     */
    @Autowired
    private MBSConfigPropDao mBSConfigPropDao;

    /**
     * 
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
     * 
     * This method returns the MBSBaseDao version of MBSConfigPropDao
     * 
     * @throws MBSBaseDao
     * @throws MBSBaseException
     */
    @Override
    public MBSBaseDao getBaseDao() throws MBSBaseException {
        return mBSConfigPropDao;
    }

    /**
     * 
     * @param transformationObject
     *            the TransformationObject
     * @throws MBSBaseException
     */
    @Override
    public void persist(TransformationObject transformationObject) throws MBSBaseException {
        MBSConfigProp mBSConfigProp = (MBSConfigProp) transformationObject.getTargetPojo();
        mBSConfigPropDao.saveOrUpdate(mBSConfigProp);

    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Override
    public void clearAll() throws MBSBaseException {
        // clear transaction requests
        Set<?> keySets = mBSConfigPropDao.getStorageRegion().keySetOnServer();
        for (final Object key : keySets) {
            mBSConfigPropDao.getStorageRegion().destroy(key);
        }
        LOGGER.debug("cleared all parties  ...");

    }

    /**
     * 
     * Overriding the clear() method
     * 
     * @param key String
     * @throws MBSBaseException
     */
    @Override
    public void clear(String key) throws MBSBaseException {
        removeKey(key);
    }
    
    /**
     * 
     * @param key
     * @throws MBSBaseException
     */
    public MBSConfigProp removeKey(String key) throws MBSBaseException {
        return mBSConfigPropDao.removeKey(key);

    }    
    
    
   
}
