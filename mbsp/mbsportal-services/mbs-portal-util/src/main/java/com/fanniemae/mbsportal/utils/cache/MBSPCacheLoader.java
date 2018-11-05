package com.fanniemae.mbsportal.utils.cache;/**
 * @author g8uaxt Created on 6/19/2018.
 */

import com.fanniemae.mbsportal.model.MBSBaseEntity;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.Collection;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author g8uaxt
 */
public abstract class MBSPCacheLoader {
        @InjectLog
        private Logger LOGGER;
        @Autowired
        private MBSPAppCache mbspAppCache;
        
        /**
         * It is loading cache meant for generic loading of data for caching
         *
         * @param key
         * @param data
         * @throws MBSBaseException
         */
        public void load(String key, Collection<? extends MBSBaseEntity> data) throws MBSBaseException {
                LOGGER.debug("Loading Data for caching...");
                CacheLoader<String, Collection<? extends MBSBaseEntity>> loader; //TODO: verify for multiple type of obj
                loader = new CacheLoader<String, Collection<? extends MBSBaseEntity>>() {
                        @Override
                        public Collection<? extends MBSBaseEntity> load(String key) throws MBSBaseException {
                                return data;
                        }
                };
                LoadingCache<String, Collection<? extends MBSBaseEntity>> cache = MBSPAppCache.build(loader);
                if(cache != null) {
                        mbspAppCache.setCache(cache);
                        LOGGER.debug("Loaded Data for caching");
                } else {
                        LOGGER.error("Problem Loading Caching data");
                }
                
        }
        
}
