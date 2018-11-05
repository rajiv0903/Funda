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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.persister.MarketIndicativePriceHistoryPersister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.pu.transformer.MarketIndicativePriceHistoryMessageTransformer;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author: Rajiv Chaudhuri
 * @Date: Jul 9, 2018
 * @File: com.fanniemae.mbsportal.pu.service.impl.MarketIndicativePriceHistoryServiceImplTest.java
 * @Revision:
 * @Description: MarketIndicativePriceHistoryServiceImplTest.java
 */
@SuppressWarnings("rawtypes")
@RunWith(MockitoJUnitRunner.class)
public class MarketIndicativePriceHistoryServiceImplTest {

    @Mock
    private MarketIndicativePriceHistoryMessageTransformer marketIndicativePriceHistoryMessageTransformer;

    @Mock
    private MarketIndicativePriceHistoryPersister marketIndicativePriceHistoryPersister;

    @InjectMocks
    private MarketIndicativePriceHistoryServiceImpl marketIndicativePriceHistoryServiceImpl;

    private TransformationObject transformationObj;

    @Before
    public void setUp() throws Exception {

        transformationObj = new TransformationObject();

        marketIndicativePriceHistoryServiceImpl = new MarketIndicativePriceHistoryServiceImpl(
                marketIndicativePriceHistoryMessageTransformer, marketIndicativePriceHistoryPersister);
    }

    @Test
    public void saveMarketIndicativePriceHistory_Success() throws Exception {

        doReturn(transformationObj).when(marketIndicativePriceHistoryMessageTransformer).transform(anyObject());
        doNothing().when(marketIndicativePriceHistoryPersister).persist(anyObject());
        boolean status = marketIndicativePriceHistoryServiceImpl.saveMarketIndicativePriceHistory(transformationObj);
        assertTrue(status);
    }

    @Test(expected = MBSBaseException.class)
    public void saveMarketIndicativePriceHistory_Failure() throws Exception {

        doReturn(transformationObj).when(marketIndicativePriceHistoryMessageTransformer).transform(anyObject());
        doThrow(MBSBaseException.class).when(marketIndicativePriceHistoryPersister).persist(anyObject());
        boolean status = marketIndicativePriceHistoryServiceImpl.saveMarketIndicativePriceHistory(transformationObj);
        assertFalse(status);
    }

    @Test
    public void saveMarketIndicativePriceHistoryAsync_Success() throws Exception {

        doReturn(transformationObj).when(marketIndicativePriceHistoryMessageTransformer).transform(anyObject());
        doNothing().when(marketIndicativePriceHistoryPersister).persist(anyObject());
        marketIndicativePriceHistoryServiceImpl.saveMarketIndicativePriceHistoryAsync(transformationObj);
    }

    @Test(expected = MBSBaseException.class)
    public void saveMarketIndicativePriceHistoryAsync_Failure() throws Exception {

        doReturn(transformationObj).when(marketIndicativePriceHistoryMessageTransformer).transform(anyObject());
        doThrow(MBSBaseException.class).when(marketIndicativePriceHistoryPersister).persist(anyObject());
        marketIndicativePriceHistoryServiceImpl.saveMarketIndicativePriceHistoryAsync(transformationObj);
    }
}
