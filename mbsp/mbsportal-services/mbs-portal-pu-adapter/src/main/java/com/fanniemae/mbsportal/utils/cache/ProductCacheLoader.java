/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.utils.cache;

import com.fanniemae.mbsportal.dao.MBSProductDao;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 
 * @author g8uaxt Created on 6/18/2018.
 */
@Component
public class ProductCacheLoader extends MBSPCacheLoader { // TODO: refactor this duplicate class later
        
        /**
         * 
         * LOGGER Logger
         */
        @InjectLog
        private Logger LOGGER;
        
        /**
         * 
         * mbsProductDao MBSProductDao
         */
        @Autowired
        private MBSProductDao mbsProductDao;
        
        /**
         * 
         * 
         * @throws MBSBaseException
         */
        @EventListener(ApplicationReadyEvent.class)
        public void load() throws MBSBaseException {
                LOGGER.info("Loading Product Data for caching...");
                super.load(AppCacheKeys.PRODUCT_CACHE_KEY.name(),mbsProductDao.getAll());
                LOGGER.info("Loaded Product Data for caching");
        }
 }
