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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.util.HashSet;
import java.util.Set;

import org.apache.geode.cache.Region;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.fanniemae.fnmpfj.gemfire.dao.BaseDaoImpl;
import com.fanniemae.mbsportal.api.persister.TradePersister;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.dao.MBSBaseDao;
import com.fanniemae.mbsportal.dao.MBSTradeDao;
import com.fanniemae.mbsportal.model.MBSTrade;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author Rajiv Chaudhuri
 * @Date Nov 16, 2017
 * @Time 4:20:18 PM
 * 	com.fanniemae.mbsportal.api.persister
 * 	TradePersisterTest.java
 */
public class TradePersisterTest extends BaseServiceTest{

    @InjectMocks
    TradePersister tradePersister;
    
    @Mock
    MBSTradeDao mBSTradeDao;
    
    @Mock
    Region<String, MBSTrade> region;
    
    MBSTrade mBSTrade;
    Set<String> keySets;
    
    @Before
    public void setUp() throws Exception {
        
        mBSTrade = new MBSTrade();
        keySets = new HashSet<>();
        keySets.add("TRANSID");
    }
    
    @Test
    public void persistTest() throws MBSBaseException {
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSTrade);
        doNothing().when(mBSTradeDao).saveOrUpdate(any());
        tradePersister.persist(transformationObject);
    }
    
    @Test
    public void clearAllTest() throws MBSBaseException {
        
        doReturn(region).when(mBSTradeDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSTrade).when(region).destroy(any());
        tradePersister.clearAll();
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void getDaoTest() throws MBSBaseException {
        BaseDaoImpl baseDao = tradePersister.getDao();
        assertTrue(baseDao == null);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void getBaseDaoTest() throws MBSBaseException {
        MBSBaseDao<MBSTrade> baseDao = tradePersister.getBaseDao();
        assertTrue(baseDao instanceof MBSTradeDao);
    }
    
    /**
     * @author gaur5c
     * @throws MBSBaseException
     */
    @Test
    public void clear() throws MBSBaseException {
        
        doReturn(region).when(mBSTradeDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSTrade).when(region).destroy(any());
        tradePersister.clear("key");
    }
}
