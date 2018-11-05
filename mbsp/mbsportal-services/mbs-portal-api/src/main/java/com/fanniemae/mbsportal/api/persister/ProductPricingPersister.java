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

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fanniemae.fnmpfj.gemfire.dao.BaseDaoImpl;
import com.fanniemae.mbsportal.dao.MBSBaseDao;
import com.fanniemae.mbsportal.dao.MBSProductPricingDao;
import com.fanniemae.mbsportal.model.MBSProductPricingRequest;
import com.fanniemae.mbsportal.persist.BasePersister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * @author g8uaxt Created on 9/13/2017.
 */
@SuppressWarnings("rawtypes")
@Component
@Qualifier("productPricingPersister")
public class ProductPricingPersister extends BasePersister {
        
        /**
         * 
         * LOGGER Logger variable
         */
        @InjectLog
        private Logger LOGGER;
        
        /**
         * 
         * mbsProductPricingDao MBSProductPricingDao
         */
        @Autowired
        private MBSProductPricingDao mbsProductPricingDao;
        
        /**
         * 
         * 
         * @param transformationObject TransformationObject
         * @throws MBSBaseException
         */
        @SuppressWarnings("unchecked")
        @Override
        public void persist(TransformationObject transformationObject) throws MBSBaseException {
                List<MBSProductPricingRequest> mbsProductPricingRequestList = (List<MBSProductPricingRequest>)
                        transformationObject
                        .getTargetPojo();
                for(MBSProductPricingRequest mbsProductPricingRequest : mbsProductPricingRequestList) {
                        mbsProductPricingDao.saveOrUpdate(mbsProductPricingRequest);
                }
        }
        
        /**
         * 
         * Clear All of Product Pricing objects in Gemfire
         * 
         * @throws MBSBaseException
         */
        public void clearAll() throws MBSBaseException {
                // clear transaction requests
                Set<?> keySets = mbsProductPricingDao.getStorageRegion().keySetOnServer();
                if(keySets.isEmpty())
                        LOGGER.debug("No productPricing to clear");
                for(final Object key : keySets) {
                        mbsProductPricingDao.getStorageRegion().destroy(key);
                }
                LOGGER.debug("cleared all productPricing  ...");
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
                mbsProductPricingDao.getStorageRegion().destroy(key);
            }
            LOGGER.debug("cleared records for key  ..."+key);
        }
        
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
                return (MBSBaseDao) mbsProductPricingDao;
        }
        
}
