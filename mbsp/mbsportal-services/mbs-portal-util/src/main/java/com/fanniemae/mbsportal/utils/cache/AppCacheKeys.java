package com.fanniemae.mbsportal.utils.cache;/**
 * @author g8uaxt Created on 6/18/2018.
 */

/**
 * Define the cache key to be used later for application wise to use cache
 * @author g8uaxt
 *
 */
public enum AppCacheKeys {
        
        PRODUCT_CACHE_KEY("MBSP_CACHE_KEY_PRODUCTS");
        private String key;
        AppCacheKeys(String key) {
                this.key=key;
        }
}
