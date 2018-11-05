/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.listener;

import org.apache.geode.cache.query.CqException;
import org.apache.geode.cache.query.CqExistsException;
import org.apache.geode.cache.query.RegionNotFoundException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.service.MBSCqListenerAdapter;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * @author g8uaxt Created on 12/4/2017.
 */
@Component
public class TransactionListener {

    /**
     * 
     * LOGGER Logger variable
     */
    @InjectLog
    Logger LOGGER;

    /**
     * 
     * mbsCqListenerAdapter MBSCqListenerAdapter
     */
    @Autowired
    MBSCqListenerAdapter mbsCqListenerAdapter;

    
    /**
     * 
     * startCqListener
     * 
     * 
     * @throws CqException
     * @throws CqExistsException
     * @throws RegionNotFoundException
     * @throws InterruptedException
     */
    public void startCqListener() throws CqException, CqExistsException, RegionNotFoundException, InterruptedException, Exception{
        LOGGER.info("TransactionListener startCqListener called");
        mbsCqListenerAdapter.refreshActiveTrades();
        mbsCqListenerAdapter.startCqListener();
        LOGGER.debug("TransactionListener startCqListener started");
        LOGGER.info("Async Method : scanStatusAndTimedOut started");
    }
}
