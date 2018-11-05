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

package com.fanniemae.mbsportal.utils.cache;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.model.MBSBaseEntity;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;

/**
 * @author g8uaxt Created on 6/14/2018.
 */
@Component
public class MBSPAppCache {
        @Value("${mbsp.cacheProp}")
        private String cacheProp;
        private static String spec;// "maximumSize=1000,expireAfterWrite=5h"; //refreshAfterWrite,maximumSize=0 to off
        @InjectLog
        private Logger LOGGER;
        private LoadingCache<String, Collection<? extends MBSBaseEntity>> cache;
        
        /**
         * our custom method to have control on cache props
         * @param loader
         * @return
         */
        public static LoadingCache<String, Collection<? extends MBSBaseEntity>> build(
                CacheLoader<String, Collection<? extends MBSBaseEntity>> loader) {
                return mbspBuilder().build(loader);
        }
        
        /**
         * method to override in case
         * @return
         */
        private static CacheBuilder<Object, Object> mbspBuilder() {
                return CacheBuilder.from(CacheBuilderSpec.parse(spec)).newBuilder().recordStats(); //for testing
               // return CacheBuilder.from(CacheBuilderSpec.parse(spec)).newBuilder(); //for prod
        }
        
        /**
         * Place holder init method to load data if any
         */
        @PostConstruct
        public void init() {
                LOGGER.info("MBSPAppCache creating..");
                spec=cacheProp;
        }
        
        /**
         * getting products from cache
         *
         * @return
         * @throws MBSBaseException
         * 
         */
        public Collection<? extends MBSBaseEntity> get(AppCacheKeys cachekey) throws MBSBaseException {
                return cache.getUnchecked(cachekey.name()); //AppCacheKeys.PRODUCT_CACHE_KEY.name()
        }
        
        public void setCache(LoadingCache<String, Collection<? extends MBSBaseEntity>> cache) {
                LOGGER.debug("MBSPAppCache created for use");
                this.cache = cache;
        }
        
        /**
         * print stat if enabled
         */
        public void printStats() {
                CacheStats stats = this.cache.stats();
                //TODO: change it later to debug
                LOGGER.debug("CacheStats-->  Size={}, hitCount={}, loadCount={},totalLoadTime={}ns ",
                        this.cache.size(), stats.hitCount(), stats.loadCount(), stats.totalLoadTime());
        }
        
        /**
         * it clears all the objects stored in cache. can be used for admin util function kind
         */
        public void clearAll() {
                this.cache.invalidateAll();
                LOGGER.debug("ALL Cache cleared");
        }
}
