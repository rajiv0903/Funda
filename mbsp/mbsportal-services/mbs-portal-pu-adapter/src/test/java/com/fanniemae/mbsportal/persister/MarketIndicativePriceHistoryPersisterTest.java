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

package com.fanniemae.mbsportal.persister;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import com.fanniemae.fnmpfj.gemfire.dao.BaseDaoImpl;
import com.fanniemae.mbsportal.dao.MBSMarketIndicativePriceHistoryDao;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePriceHistory;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author: Rajiv Chaudhuri
 * @Date: Jul 9, 2018
 * @File: com.fanniemae.mbsportal.persister.MarketIndicativePriceHistoryPersisterTest.java
 * @Revision:
 * @Description: MarketIndicativePriceHistoryPersisterTest.java
 */

@RunWith(MockitoJUnitRunner.class)
public class MarketIndicativePriceHistoryPersisterTest {

    @Mock
    protected Logger LOGGER;

    @Mock
    ClientCacheFactory clientCacheFactory;

    @InjectMocks
    private MarketIndicativePriceHistoryPersister marketIndicativePriceHistoryPersister;

    @Mock
    private MBSMarketIndicativePriceHistoryDao mBSMarketIndicativePriceHistoryDao;

    @Mock
    private Region<Long, MBSMarketIndicativePriceHistory> region;

    private String historyId;
    private MBSMarketIndicativePriceHistory mBSMarketIndicativePriceHistoryMock;
    Set<Long> keySets;

    @Before
    public void setUp() throws Exception {

        createMbsMarketIndicativePriceHistory();
        keySets = new HashSet<>();
        keySets.add(1234L);
        historyId = "1234";

    }

    @Test
    @Ignore
    public void persist_Success() throws MBSBaseException {

        TransformationObject transformationObject = new TransformationObject();
        List<MBSMarketIndicativePriceHistory> mBSMarketIndicativePriceHistoryLst = new ArrayList<>();
        mBSMarketIndicativePriceHistoryLst.add(mBSMarketIndicativePriceHistoryMock);
        transformationObject.setTargetPojo(mBSMarketIndicativePriceHistoryLst);
        doNothing().when(mBSMarketIndicativePriceHistoryDao).putAll(anyObject());
        marketIndicativePriceHistoryPersister.persist(transformationObject);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void getDao_Success() throws MBSBaseException {

        BaseDaoImpl baseDaoImpl = marketIndicativePriceHistoryPersister.getDao();
        Assert.assertNull(baseDaoImpl);
    }

    @Test
    public void getBaseDao_Success() throws MBSBaseException {

        MBSMarketIndicativePriceHistoryDao mBSMarketIndicativePriceHistoryDaoRet = (MBSMarketIndicativePriceHistoryDao) marketIndicativePriceHistoryPersister
                .getBaseDao();
        Assert.assertNotNull(mBSMarketIndicativePriceHistoryDaoRet);
    }

    @Test
    public void clearAll_Success() throws MBSBaseException {

        doReturn(region).when(mBSMarketIndicativePriceHistoryDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSMarketIndicativePriceHistoryMock).when(region).destroy(any());
        marketIndicativePriceHistoryPersister.clearAll();
    }

    @Test
    public void clear_Success() throws MBSBaseException {

        doReturn(region).when(mBSMarketIndicativePriceHistoryDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSMarketIndicativePriceHistoryMock).when(region).destroy(any());
        marketIndicativePriceHistoryPersister.clear(historyId);
    }

    private void createMbsMarketIndicativePriceHistory() throws MBSBaseException {

        mBSMarketIndicativePriceHistoryMock = new MBSMarketIndicativePriceHistory(null, "FN15", "FN15", 1528898940858L,
                new BigDecimal("4.5"), 0,
                MBSPortalUtils.convertToDateWithFormatter("2018-06-18", DateFormats.DATE_FORMAT_NO_TIMESTAMP), null,
                new BigDecimal("102.578125"), "102-18+", new BigDecimal("102.578125"), "102-18+", null,
                new BigDecimal("102.578125"), "102-18+",
                MBSPortalUtils.convertToDateWithFormatter("2018-06-18", DateFormats.DATE_FORMAT_NO_TIMESTAMP));

    }
}
