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
import com.fanniemae.mbsportal.api.persister.ProductPersister;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.dao.MBSBaseDao;
import com.fanniemae.mbsportal.dao.MBSProductDao;
import com.fanniemae.mbsportal.model.MBSProduct;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author Rajiv Chaudhuri
 * @Date Nov 16, 2017
 * @Time 4:46:56 PM
 * 	com.fanniemae.mbsportal.api.persister
 * 	ProductPersisterTest.java
 */
public class ProductPersisterTest extends BaseServiceTest{
    
    @InjectMocks
    ProductPersister productPersister;
    
    @Mock
    MBSProductDao mBSProductDao;
    
    @Mock
    Region<String, MBSProduct> region;
    
    MBSProduct mBSProduct;
    Set<String> keySets;
    
    @Before
    public void setUp() throws Exception {
        
        mBSProduct = new MBSProduct();
        keySets = new HashSet<>();
        keySets.add("TRANSID");
    }
    
    @Test
    public void persistTest() throws MBSBaseException {
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSProduct);
        doNothing().when(mBSProductDao).saveOrUpdate(any());
        productPersister.persist(transformationObject);
    }
    
    @Test
    public void clearAllTest() throws MBSBaseException {
        
        doReturn(region).when(mBSProductDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSProduct).when(region).destroy(any());
        productPersister.clearAll();
    }
    
    /**
     * @author gaur5c
     * @throws MBSBaseException
     */
    @Test
    public void clear() throws MBSBaseException {
        
        doReturn(region).when(mBSProductDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSProduct).when(region).destroy(any());
        productPersister.clear("key");
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void getDaoTest() throws MBSBaseException {
        BaseDaoImpl baseDao = productPersister.getDao();
        assertTrue(baseDao == null);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void getBaseDaoTest() throws MBSBaseException {
        MBSBaseDao<MBSProduct> baseDao = productPersister.getBaseDao();
        assertTrue(baseDao instanceof MBSProductDao);
    }

}
