/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.persist;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fanniemae.fnmpfj.gemfire.dao.BaseDaoImpl;
import com.fanniemae.mbsportal.dao.MBSBaseDao;
import com.fanniemae.mbsportal.dao.MBSExceptionLookupDao;
import com.fanniemae.mbsportal.model.MBSExceptionLookup;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * 
 * 
 * @author g8upjv
 */
@SuppressWarnings("rawtypes")
@Component
public class ExceptionPersister extends BasePersister {
    
        /**
         * 
         * LOGGER Logger variable
         */
        @InjectLog
        private Logger LOGGER;

	/**
	 * 
	 * mbsExceptionLookupDao MBSExceptionLookupDao
	 */
        @Autowired
	private MBSExceptionLookupDao mbsExceptionLookupDao;

	/**
	 * 
	 * Overriding the base method to persist the record(s)
	 * 
	 * @param transformationObject TransformationObject
	 * @throws MBSBaseException
	 */
	@Override
	public void persist(TransformationObject transformationObject) throws MBSBaseException {
	    List<MBSExceptionLookup> mbsExceptionLookupLst = (List<MBSExceptionLookup>)
                    transformationObject
                    .getTargetPojo();
            for(MBSExceptionLookup mbsExceptionLookup : mbsExceptionLookupLst) {
                mbsExceptionLookupDao.saveOrUpdate(mbsExceptionLookup);
            }
	}

	/**
	 * 
	 * Overriding the clearAll() method
	 * 
	 * 
	 * @throws MBSBaseException
	 */
	@Override
	public void clearAll() throws MBSBaseException {
		// clear transaction requests
		Set<?> keySets = mbsExceptionLookupDao.getStorageRegion().keySetOnServer();
		for (final Object key : keySets) {
		    mbsExceptionLookupDao.getStorageRegion().destroy(key);
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
                mbsExceptionLookupDao.getStorageRegion().destroy(key);
            }
            LOGGER.debug("cleared records for key  ..."+key);
        }

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
	 * This method returns the MBSBaseDao version of ProductDao
	 * 
	 * @throws MBSBaseDao
	 * @throws MBSBaseException
	 */
	@Override
	public MBSBaseDao getBaseDao() throws MBSBaseException {
		return mbsExceptionLookupDao;
	}
}
