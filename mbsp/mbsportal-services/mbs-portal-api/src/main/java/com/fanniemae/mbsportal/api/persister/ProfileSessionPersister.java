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
import com.fanniemae.mbsportal.dao.MBSProfileSessionDao;
import com.fanniemae.mbsportal.model.MBSProfileSession;
import com.fanniemae.mbsportal.persist.BasePersister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Mar 13, 2018
 * @File: com.fanniemae.mbsportal.api.persister.ProfileSessionPersister.java
 * @Revision :
 * @Description: CMMBSSTA01-1212 : (Tech) Trader user login calls MBSP API with
 *               valid Session ID
 */
@SuppressWarnings("rawtypes")
@Component
public class ProfileSessionPersister extends BasePersister {

    /**
     * 
     * LOGGER Logger variable
     */
    @InjectLog
    private Logger LOGGER;
    /**
     * 
     * mBSProfileSessionDao MBSProfileSessionDao
     */
    @Autowired
    private MBSProfileSessionDao mBSProfileSessionDao;

    /**
     * 
     * 
     * @param transformationObject
     *            TransformationObject
     * @throws MBSBaseException
     */
    @Override
    public void persist(TransformationObject transformationObject) throws MBSBaseException {

        MBSProfileSession mBSProfileSession = (MBSProfileSession) transformationObject.getTargetPojo();
        mBSProfileSessionDao.saveOrUpdate(mBSProfileSession);

    }

    /**
     * 
     * This methods clears all the data in the profile session local region
     * 
     * @throws MBSBaseException
     */
    @Override
    public void clearAll() throws MBSBaseException {

        Set<String> keySets = mBSProfileSessionDao.getStorageRegion().keySetOnServer();
        for (final String key : keySets) {
            mBSProfileSessionDao.getStorageRegion().destroy(key);
        }
        LOGGER.debug("cleared all profile sessions  ...");

    }

    /**
     * 
     * Overriding the clear() method
     * 
     * @param key
     *            String
     * @throws MBSBaseException
     */
    @Override
    public void clear(String key) throws MBSBaseException {
        if (!StringUtils.isEmpty(key)) {
            mBSProfileSessionDao.getStorageRegion().destroy(key);
        }
        LOGGER.debug("cleared records for key  ..." + key);

    }

    /**
     * 
     * 
     * @return BaseDaoImpl
     * @throws MBSBaseException
     */
    @Override
    public BaseDaoImpl getDao() throws MBSBaseException {
        return mBSProfileSessionDao;
    }

    /**
     * 
     * 
     * @return MBSBaseDao
     * @throws MBSBaseException
     */
    @Override
    public MBSBaseDao getBaseDao() throws MBSBaseException {
        return null;
    }

}
