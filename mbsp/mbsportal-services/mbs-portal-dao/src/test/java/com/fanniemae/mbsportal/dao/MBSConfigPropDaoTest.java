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

package com.fanniemae.mbsportal.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fanniemae.mbsportal.model.MBSConfigProp;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Aug 6, 2018
 * @File: com.fanniemae.mbsportal.dao.MBSConfigPropDaoTest.java 
 * @Revision: 
 * @Description: MBSConfigPropDaoTest.java
 */
public class MBSConfigPropDaoTest extends BaseDaoTest {
    
    private static String regionName = "MBSPConfigProp";

    
    @Autowired
    private MBSConfigPropDao mbsConfigPropDao;

    @Mock
    MBSBaseDao<MBSConfigProp> mBSBaseDao;
    
    @InjectLog
    private Logger LOGGER;
    
    MBSConfigProp mBSConfigProp;

    @Before
    public void setUp() throws Exception {

        when(mockRegion.getRegionService()).thenReturn(mockRegionService);
        when(mockRegion.getName()).thenReturn(regionName);
        when(mockRegionService.getRegion(regionName)).thenReturn(mockRegion);
        
        mBSConfigProp = new MBSConfigProp();
        mBSConfigProp.setKey("key");
        mBSConfigProp.setValue("value");
    }
    
    @Test
    public void saveOrUpdate() throws MBSBaseException {
        MBSConfigProp mbsConfigProp = new MBSConfigProp();
        mbsConfigProp.setKey("ts.authKey");
        mbsConfigProp.setValue("NewKeyxadsfetwerpw3842dfs");
        mbsConfigProp.setParent("TS");
        mbsConfigProp.setDataType("String");
        mbsConfigPropDao.saveOrUpdate(mbsConfigProp);
    }

    @Test
    public void get() throws MBSBaseException {
        
        doReturn(mBSConfigProp).when((BaseDaoWrapper) baseDaoWrapper).getById(anyString());
        MBSConfigProp mBSConfigPropActual = (MBSConfigProp) mbsConfigPropDao.getById("key");
        assertEquals(mBSConfigProp.getValue(), mBSConfigPropActual.getValue());

    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void removeKey_Success() throws MBSBaseException {
        
        doReturn(mBSConfigProp).when((BaseDaoWrapper) baseDaoWrapper).removeById(anyString());
        MBSConfigProp mBSConfigPropActual = mbsConfigPropDao.removeKey("key");
        assertEquals(mBSConfigProp.getKey(), mBSConfigPropActual.getKey());
    }
}
