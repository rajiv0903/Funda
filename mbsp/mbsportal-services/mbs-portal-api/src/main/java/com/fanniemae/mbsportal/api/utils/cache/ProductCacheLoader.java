package com.fanniemae.mbsportal.api.utils.cache;

import com.fanniemae.mbsportal.dao.MBSProductDao;
import com.fanniemae.mbsportal.utils.cache.AppCacheKeys;
import com.fanniemae.mbsportal.utils.cache.MBSPCacheLoader;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author g8uaxt Created on 6/19/2018.
 */

@Component
public class ProductCacheLoader extends MBSPCacheLoader {
        @InjectLog
        private Logger LOGGER;
        @Autowired
        private MBSProductDao mbsProductDao;
        
        
        @EventListener(ApplicationReadyEvent.class)
        public void load() throws MBSBaseException {
                LOGGER.info("Loading Product Data for caching...");
                super.load(AppCacheKeys.PRODUCT_CACHE_KEY.name(),mbsProductDao.getAll());
                LOGGER.info("Loaded Product Data for caching");
        }
 }
