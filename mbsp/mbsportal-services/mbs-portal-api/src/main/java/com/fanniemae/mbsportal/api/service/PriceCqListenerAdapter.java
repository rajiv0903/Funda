/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.service;

import java.rmi.dgc.VMID;

import org.apache.geode.cache.Operation;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.query.CqAttributes;
import org.apache.geode.cache.query.CqAttributesFactory;
import org.apache.geode.cache.query.CqEvent;
import org.apache.geode.cache.query.CqException;
import org.apache.geode.cache.query.CqExistsException;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.RegionNotFoundException;
import org.apache.geode.cache.util.CqListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;

/**
 * @author g8uaxt Created on 12/5/2017.
 */
@Component
public class PriceCqListenerAdapter extends CqListenerAdapter {

    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PriceCqListenerAdapter.class);

    /**
     * 
     * priceEventService PriceEventService
     */
    @Autowired
    PriceEventService priceEventService;

    /**
     * 
     * added for Junit set //TODO: find way to mock it
     * 
     * @param priceEventService
     *            priceEventService
     * 
     */
    public void setPriceEventService(PriceEventService priceEventService) {
        this.priceEventService = priceEventService;
    }

    /**
     * 
     * startCqListener
     * 
     * @throws CqException
     * @throws CqExistsException
     * @throws RegionNotFoundException
     * @throws InterruptedException
     */
    public void startCqListener()
            throws CqException, CqExistsException, RegionNotFoundException, InterruptedException, Exception {
        LOGGER.info("PriceCqListenerAdapter startCqListener called");
        String query = "Select * from /MBSMarketIndicativePrice";
        CqAttributesFactory cqAttributesFactory = new CqAttributesFactory();
        cqAttributesFactory.addCqListener(this);
        CqAttributes cqAttributes = cqAttributesFactory.create();
        String cqName = "PriceListener" + new VMID().toString();
        QueryService queryService = getQueryService();
        queryService.newCq(cqName, query, cqAttributes).execute();
        LOGGER.info("PriceCqListenerAdapter startCqListener readyForEvents: " + cqName);
    }

    /**
     * 
     * Added for jUnit mock
     * 
     * @return QueryService
     */
    public QueryService getQueryService() {
        return ClientCacheFactory.getAnyInstance().getQueryService();
    }

    /**
     * 
     * onEvent
     * 
     * @param aCqEvent
     *            CqEvent
     */
    public void onEvent(CqEvent aCqEvent) {
        if (aCqEvent.getBaseOperation() == Operation.NET_LOAD_UPDATE || aCqEvent.getBaseOperation() == Operation.UPDATE
                || aCqEvent.getBaseOperation() == Operation.CREATE) {
            LOGGER.debug("PriceCqListenerAdapter onEvent called for key:" + aCqEvent.getKey() + " NewVal:"
                    + aCqEvent.getNewValue());
            MBSMarketIndicativePrice obj = (MBSMarketIndicativePrice) aCqEvent.getNewValue();
            try {
                priceEventService.processEvent(obj);
            } catch (Exception ex) {
                LOGGER.error("PriceCqListenerAdapter onEvent: with Id#" + obj.getId(), ex);
            }
        }
    }
    
    /**
     * 
     * onError
     * 
     * @param aCqEvent
     *            CqEvent
     * 
     */
    public void onError(CqEvent aCqEvent) {
        LOGGER.info("PriceListener onError called" + aCqEvent.getQueryOperation());
    }

    /**
     * 
     * close method
     */
    public void close() {
        LOGGER.info("PriceListener close called");
        ClientCacheFactory.getAnyInstance().close();
    }

}
