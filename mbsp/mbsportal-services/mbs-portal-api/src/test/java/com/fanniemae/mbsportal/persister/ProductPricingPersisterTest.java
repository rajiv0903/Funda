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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.geode.cache.Region;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.fanniemae.fnmpfj.gemfire.dao.BaseDaoImpl;
import com.fanniemae.mbsportal.api.persister.ProductPricingPersister;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.dao.MBSBaseDao;
import com.fanniemae.mbsportal.dao.MBSProductPricingDao;
import com.fanniemae.mbsportal.model.MBSProductPricingRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author Rajiv Chaudhuri
 * @Date Nov 16, 2017
 * @Time 4:53:08 PM
 * 	com.fanniemae.mbsportal.api.persister
 * 	ProductPricingPersisterTest.java
 */
public class ProductPricingPersisterTest extends BaseServiceTest{

    @InjectMocks
    ProductPricingPersister productPricingPersister;
    
    @Mock
    MBSProductPricingDao mbsProductPricingDao;
    
    @Mock
    Region<String, MBSProductPricingRequest> region;
    
    MBSProductPricingRequest mBSProductPricingRequest;
    List<MBSProductPricingRequest> mbsProductPricingRequestList;
    Set<Long> keySets;
    
    @Before
    public void setUp() throws Exception {
        
        mBSProductPricingRequest = new MBSProductPricingRequest();
        mbsProductPricingRequestList = new ArrayList<>();
        mbsProductPricingRequestList.add(mBSProductPricingRequest);
        keySets = new HashSet<>();
        keySets.add(1L);
    }
    
    @Test
    public void persistTest() throws MBSBaseException {
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mbsProductPricingRequestList);
        doNothing().when(mbsProductPricingDao).saveOrUpdate(any());
        productPricingPersister.persist(transformationObject);
    }
    
    @Test
    public void clearAllTest() throws MBSBaseException {
        
        doReturn(region).when(mbsProductPricingDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSProductPricingRequest).when(region).destroy(any());
        productPricingPersister.clearAll();
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void getDaoTest() throws MBSBaseException {
        BaseDaoImpl baseDao = productPricingPersister.getDao();
        assertTrue(baseDao == null);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void getBaseDaoTest() throws MBSBaseException {
        MBSBaseDao<MBSProductPricingRequest> baseDao = productPricingPersister.getBaseDao();
        assertTrue(baseDao instanceof MBSProductPricingDao);
    }
    
    /**
     * @author gaur5c
     * @throws MBSBaseException
     */
    @Test
    public void clear() throws MBSBaseException {
        
        doReturn(region).when(mbsProductPricingDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSProductPricingRequest).when(region).destroy(any());
        productPricingPersister.clear("key");
    }
}
