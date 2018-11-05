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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.apache.geode.cache.Operation;
import org.apache.geode.cache.query.CqEvent;
import org.apache.geode.cache.query.CqException;
import org.apache.geode.cache.query.CqExistsException;
import org.apache.geode.cache.query.CqQuery;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.RegionNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;

/**
 * @author g8upjv Created on 12/8/2017.
 */

public class PriceCqListenerAdapterTest extends BaseServiceTest {

    @Spy
    PriceCqListenerAdapter priceCqListenerAdapter;

    @Mock
    PriceEventService priceEventService;

    @Mock
    CqQuery cqQuery;

    @Mock
    QueryService queryService;

    @Mock
    CqEvent aCqEvent;

    @Before
    public void setUp() throws Exception {
        when(queryService.newCq(any(), any(), any())).thenReturn(cqQuery);
        doNothing().when(cqQuery).execute();
        when(priceCqListenerAdapter.getQueryService()).thenReturn(queryService);
        priceCqListenerAdapter.setPriceEventService(priceEventService);
        //doNothing().when(priceEventService).scanStatusAndTimedOut();
    }

    @Test
    public void startCqListener() throws InterruptedException, CqExistsException, RegionNotFoundException, CqException, Exception {
        priceCqListenerAdapter.startCqListener();
    }

    @Test
    public void onEventUpdate() {
        when(aCqEvent.getBaseOperation()).thenReturn(Operation.UPDATE);
        when(aCqEvent.getNewValue()).thenReturn(getMBSPrice("101-101"));
        when(aCqEvent.getKey()).thenReturn("key-1");
        priceCqListenerAdapter.onEvent(aCqEvent);
    }

    @Test
    public void onEventCreate() {
        when(aCqEvent.getBaseOperation()).thenReturn(Operation.CREATE);
        when(aCqEvent.getNewValue()).thenReturn(getMBSPrice("100-100"));
        when(aCqEvent.getKey()).thenReturn("key-1");
        priceCqListenerAdapter.onEvent(aCqEvent);
    }

    @Test
    public void onEventNET_LOAD_UPDATE() {
        when(aCqEvent.getBaseOperation()).thenReturn(Operation.NET_LOAD_UPDATE);
        when(aCqEvent.getNewValue()).thenReturn(getMBSPrice("102-102"));
        when(aCqEvent.getKey()).thenReturn("key-3");
        priceCqListenerAdapter.onEvent(aCqEvent);
    }

    @Test
    public void onError() {
        when(aCqEvent.getQueryOperation()).thenReturn(Operation.UPDATE);
        priceCqListenerAdapter.onError(aCqEvent);
    }

    // @After
    // @AfterClass//TODO: call it once after all the test cases
    public void close() {
        priceCqListenerAdapter.close();
    }

    /**
     * utility method to get setup data
     * 
     * @param stateType
     * @return
     */
    private MBSMarketIndicativePrice getMBSPrice(String price) {
        MBSMarketIndicativePrice mbsMarketIndicativePrice = new MBSMarketIndicativePrice();
        mbsMarketIndicativePrice.setAskPriceText(price);
        mbsMarketIndicativePrice.setBidPriceText(price);
        return mbsMarketIndicativePrice;
    }

}
