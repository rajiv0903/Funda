/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.pu.adapter.config;

import com.fanniemae.mbsportal.pu.listener.PUEsbMessageListener;
import com.fanniemae.mbsportal.utils.cache.ProductCacheLoader;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

import org.apache.geode.cache.query.CqException;
import org.apache.geode.cache.query.CqExistsException;
import org.apache.geode.cache.query.RegionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * Created by g8uaxt on 8/24/2017.
 */
/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Dec 28, 2017
 * @Time 11:07:59 AM com.fanniemae.mbsportal.api.config PUBootstrapConfig.java
 * @Description: For Blue/ Green - Added Profile
 */
@Configuration
public class PUBootstrapConfig {
    
    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PUBootstrapConfig.class);
    
    /**
     * Loader for product ref data into cache
     */
    @Autowired
    private ProductCacheLoader productCacheLoader;
    /**
     * 
     * esbMessageListener PUEsbMessageListener
     */
    @Autowired
    private PUEsbMessageListener puEsbMessageListener;
    
    /**
     * 
     * 
     * @throws MBSBaseException
     * @throws CqException
     * @throws CqExistsException
     * @throws RegionNotFoundException
     * @throws InterruptedException
     */
    @EventListener(ApplicationReadyEvent.class)
    public void startESBClientAsyncConsumer()
            throws MBSBaseException, CqException, CqExistsException, RegionNotFoundException, InterruptedException, Exception  {
        LOGGER.info("startESBClientAsyncConsumer starting..");
        puEsbMessageListener.startESBClientAsyncConsumer();
    }

}
