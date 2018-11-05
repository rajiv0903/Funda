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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.fnmpfj.gemfire.dao.BaseDaoImpl;
import com.fanniemae.mbsportal.dao.MBSBaseDao;
import com.fanniemae.mbsportal.dao.MBSExceptionLookupDao;
import com.fanniemae.mbsportal.model.MBSExceptionLookup;
import com.fanniemae.mbsportal.persist.ExceptionPersister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author g8upjv
 * @Date Mar 6, 2018
 * @Time 4:46:56 PM
 *      com.fanniemae.mbsportal.api.persister
 *      
 */
@RunWith(MockitoJUnitRunner.class)
public class ExceptionPersisterTest{
    
    @InjectMocks
    ExceptionPersister exceptionLookupPersister;
    
    @Mock
    MBSExceptionLookupDao mbsExceptionLookupDao;
    
    @Mock
    Region<String, MBSExceptionLookup> region;
    
    MBSExceptionLookup mbsExceptionLookup;
    List<MBSExceptionLookup> mbsExceptionLookupLst;
    Set<String> keySets;
    
    @Before
    public void setUp() throws Exception {
        mbsExceptionLookupLst = new ArrayList<>();
        mbsExceptionLookup = new MBSExceptionLookup();
        mbsExceptionLookupLst.add(mbsExceptionLookup);
        keySets = new HashSet<>();
        keySets.add("TRANSID");
    }
    
    @Test
    public void persistTest() throws MBSBaseException {
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mbsExceptionLookupLst);
        doNothing().when(mbsExceptionLookupDao).saveOrUpdate(any());
        exceptionLookupPersister.persist(transformationObject);
    }
    
    @Test
    @Ignore
    public void clearAllTest() throws MBSBaseException {
        
        doReturn(region).when(mbsExceptionLookupDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mbsExceptionLookup).when(region).destroy(any());
        exceptionLookupPersister.clearAll();
    }
    
    @Test
    @Ignore
    public void clearTest() throws MBSBaseException {
        
        doReturn(region).when(mbsExceptionLookupDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mbsExceptionLookup).when(region).destroy(any());
        exceptionLookupPersister.clear("TRANSID");
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void getDaoTest() throws MBSBaseException {
        BaseDaoImpl baseDao = exceptionLookupPersister.getDao();
        assertTrue(baseDao == null);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void getBaseDaoTest() throws MBSBaseException {
        MBSBaseDao<MBSExceptionLookup> baseDao = exceptionLookupPersister.getBaseDao();
        assertTrue(baseDao instanceof MBSExceptionLookupDao);
    }

}
