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

package com.fanniemae.mbsportal.pu.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.persister.MarketIndicativePriceHistoryPersister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.pu.service.MarketIndicativePriceHistoryService;
import com.fanniemae.mbsportal.pu.transformer.MarketIndicativePriceHistoryMessageTransformer;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author: Rajiv Chaudhuri
 * @Date: Jul 2, 2018
 * @File: com.fanniemae.mbsportal.pu.service.impl.MarketIndicativePriceHistoryServiceImpl.java
 * @Revision:
 * @Description: MarketIndicativePriceHistoryServiceImpl.java
 */
@SuppressWarnings("rawtypes")
@Component
@EnableAsync
public class MarketIndicativePriceHistoryServiceImpl implements MarketIndicativePriceHistoryService {

    /**
     * 
     * LOGGER Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketIndicativePriceHistoryServiceImpl.class);
    
    
    /**
     * 
     * marketIndicativePriceHistoryMessageTransformer MarketIndicativePriceHistoryMessageTransformer
     */
    private final MarketIndicativePriceHistoryMessageTransformer marketIndicativePriceHistoryMessageTransformer;
    
    /**
     * 
     * marketIndicativePriceHistoryPersister MarketIndicativePriceHistoryPersister
     */
    private final MarketIndicativePriceHistoryPersister marketIndicativePriceHistoryPersister;
    
    /**
     * 
     * @param marketIndicativePriceHistoryMessageTransformer
     * @param marketIndicativePriceHistoryPersister
     */
    @Autowired
    public MarketIndicativePriceHistoryServiceImpl(
            Transformer marketIndicativePriceHistoryMessageTransformer, 
            Persister marketIndicativePriceHistoryPersister) 
    {
        this.marketIndicativePriceHistoryMessageTransformer = (MarketIndicativePriceHistoryMessageTransformer) marketIndicativePriceHistoryMessageTransformer;
        this.marketIndicativePriceHistoryPersister = (MarketIndicativePriceHistoryPersister) marketIndicativePriceHistoryPersister;
    }
    
    @Override
    public boolean saveMarketIndicativePriceHistory(TransformationObject transformationObj) throws MBSBaseException {

        boolean status = true;
        LOGGER.debug("Entering saveMarketIndicativePriceHistory method in MarketIndicativePriceHistoryServiceImpl");
        
        try {
            saveMarketIndicativePriceHistoryAsync(transformationObj);
            
        } catch (MBSBaseException exe) {
            LOGGER.error("MBSBaseException: {}", exe);
            status =  false;    
            throw exe;
        }
        LOGGER.debug("Exiting saveMarketIndicativePriceHistory method in MarketIndicativePriceHistoryServiceImpl");
        return status;
    }

    @Override
    @Async
    public void saveMarketIndicativePriceHistoryAsync(TransformationObject transformationObj)
            throws MBSBaseException {
        //TODO: add async name to it to track
        LOGGER.debug("Entering saveMarketIndicativePriceHistoryAsync method in MarketIndicativePriceHistoryServiceImpl");
        
        try{
            transformationObj = marketIndicativePriceHistoryMessageTransformer.transform(transformationObj);
            marketIndicativePriceHistoryPersister.persist(transformationObj);
            
        }catch(MBSBaseException exe){            
            LOGGER.error("MBSBaseException: {}", exe);
            throw exe;
        }
        
        LOGGER.debug("Exiting saveMarketIndicativePriceHistoryAsync method in MarketIndicativePriceHistoryServiceImpl");
    }

}
