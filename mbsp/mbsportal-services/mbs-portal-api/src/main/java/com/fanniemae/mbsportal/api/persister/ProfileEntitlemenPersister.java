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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fanniemae.fnmpfj.gemfire.dao.BaseDaoImpl;
import com.fanniemae.mbsportal.dao.MBSBaseDao;
import com.fanniemae.mbsportal.dao.MBSProfileDao;
import com.fanniemae.mbsportal.model.MBSProfile;
import com.fanniemae.mbsportal.persist.BasePersister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * @author Rajiv Chaudhuri
 * @Date Nov 16, 2017
 * @Time 4:01:39 PM
 * 	com.fanniemae.mbsportal.api.persister
 * 	ProfileEntitlemenPersister.java
 */
@SuppressWarnings("rawtypes")
@Component
public class ProfileEntitlemenPersister extends BasePersister {
    
    /**
     * 
     * LOGGER Logger variable
     */
    @InjectLog
    private Logger LOGGER;
    
    /**
     * 
     * mBSProfileDao MBSProfileDao
     */
    @Autowired
    private MBSProfileDao mBSProfileDao;

    /**
     * 
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
     * 
     * @return MBSBaseDao
     * @throws MBSBaseException
     */
    @Override
    public MBSBaseDao getBaseDao() throws MBSBaseException {
        return mBSProfileDao;
    }
    
    /**
     * 
     * 
     * @param transformationObject TransformationObject
     * @throws MBSBaseException
     */
    @Override
    public void persist(TransformationObject transformationObject) throws MBSBaseException 
    {
        MBSProfile mBSProfile = (MBSProfile) transformationObject.getTargetPojo();        
        mBSProfileDao.saveOrUpdate(mBSProfile);
    }

    
    /**
     * 
     * This methods clears all the data in the profile
     * 
     * @throws MBSBaseException
     */
    @Override
    public void clearAll() throws MBSBaseException {
            // clear transaction requests
            Set<?> keySets = mBSProfileDao.getStorageRegion().keySetOnServer();
            for (final Object key : keySets) {
                mBSProfileDao.getStorageRegion().destroy(key);
            }
            LOGGER.debug("cleared all transactions  ...");
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
        if(!StringUtils.isEmpty(key)){
            mBSProfileDao.getStorageRegion().destroy(key);
        }
        LOGGER.debug("cleared records for key  ..."+key);
    }


}
