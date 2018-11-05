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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.util.HashSet;
import java.util.Set;

import org.apache.geode.cache.Region;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.fnmpfj.gemfire.dao.BaseDaoImpl;
import com.fanniemae.mbsportal.api.persister.ConfigPropPersister;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.dao.MBSConfigPropDao;
import com.fanniemae.mbsportal.model.MBSConfigProp;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Aug 6, 2018
 * @File: com.fanniemae.mbsportal.persister.ConfigPropPersisterTest.java 
 * @Revision: 
 * @Description: ConfigPropPersisterTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigPropPersisterTest extends BaseServiceTest{

    @InjectMocks
    ConfigPropPersister configPropPersister;
    
    @Mock
    MBSConfigPropDao mBSConfigPropDao;
    
    @Mock
    Region<String, MBSConfigProp> region;
    
    private MBSConfigProp mBSConfigProp;
    private Set<String> keySets;
    
    @Before
    public void setUp() throws Exception {        
        createConfigProp();
        keySets = new HashSet<>();
        keySets.add("1234");
        
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void getDao_Success() throws MBSBaseException {
        
        BaseDaoImpl baseDaoImpl =  configPropPersister.getDao();
        Assert.assertNull(baseDaoImpl);
    }
   
    
    @Test
    public void getBaseDao_Success() throws MBSBaseException {
        
        MBSConfigPropDao mBSConfigPropDaoRet =  (MBSConfigPropDao) configPropPersister.getBaseDao();
        Assert.assertNotNull(mBSConfigPropDaoRet);
    }
    
    
    @Test
    public void persist_Success() throws MBSBaseException {
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSConfigProp);
        doNothing().when(mBSConfigPropDao).saveOrUpdate(any());
        configPropPersister.persist(transformationObject);
    }
    
    @Test
    public void clearAll_Success() throws MBSBaseException {
        
        doReturn(region).when(mBSConfigPropDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSConfigProp).when(region).destroy(any());
        configPropPersister.clearAll();
    }
    
    @Test
    public void clear_Success() throws MBSBaseException {
        
        doReturn(region).when(mBSConfigPropDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSConfigProp).when(region).destroy(any());
        configPropPersister.clear("key");
    }
    
    private void createConfigProp(){
        
        mBSConfigProp = new MBSConfigProp();
        mBSConfigProp.setKey("key");
        mBSConfigProp.setValue("value");
        mBSConfigProp.setParent("C");
        mBSConfigProp.setDataType("String");
    }
    
}
