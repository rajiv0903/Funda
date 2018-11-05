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
import com.fanniemae.mbsportal.api.persister.TransactionPersister;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.dao.MBSBaseDao;
import com.fanniemae.mbsportal.dao.MBSEventDao;
import com.fanniemae.mbsportal.dao.MBSTransactionRequestDao;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;

/**
 * @author Rajiv Chaudhuri
 * @Date Nov 16, 2017
 * @Time 4:01:57 PM
 * 	com.fanniemae.mbsportal.api.persister
 * 	TransactionPersisterTest.java
 */
/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 30, 2018
 * @Time 3:38:21 PM
 * 	com.fanniemae.mbsportal.api.persister
 * 	TransactionPersisterTest.java
 * @Description: CMMBSSTA01-1108: API- Prevent Concurrent Request from Same Lender
 */
public class TransactionPersisterTest extends BaseServiceTest{

    @InjectMocks
    TransactionPersister transactionPersister;
    
    @Mock
    MBSTransactionRequestDao mBSTransactionRequestDao;
    
    @Mock
    MBSEventDao mBSEventDao;
    
    @Mock
    Region<String, MBSTransactionRequest> region;
    
    MBSTransactionRequest mBSTransactionRequest;
    Set<String> keySets;
    
    @Before
    public void setUp() throws Exception {
        
        mBSTransactionRequest = new MBSTransactionRequest();
        keySets = new HashSet<>();
        keySets.add("TRANSID");
    }
    
    @Test
    public void persistTest() throws MBSBaseException {
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSTransactionRequest);
        doNothing().when(mBSTransactionRequestDao).saveOrUpdate(any());
        transactionPersister.persist(transformationObject);
    }
    
    @Test
    public void persist_Concurrent_Lender_Trade_Request_Success() throws MBSBaseException {
        
        List<MBSTransactionRequest> transReqLst  = new ArrayList<MBSTransactionRequest>();
        mBSTransactionRequest.setStateType(StateType.LENDER_OPEN.name());
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transformationObject.setTargetPojo(mBSTransactionRequest);
        doReturn(transReqLst).when(mBSTransactionRequestDao).getConcurrentLenderTradeRequest(any());
        doNothing().when(mBSTransactionRequestDao).saveOrUpdate(any());
        transactionPersister.persist(transformationObject);
    }
    
    @Test(expected=MBSBusinessException.class)
    public void persist_Concurrent_Lender_Trade_Request_Failure() throws MBSBaseException {
        
        mBSTransactionRequest.setStateType(StateType.LENDER_OPEN.name());
        
        List<MBSTransactionRequest> transReqLst  = new ArrayList<MBSTransactionRequest>();
        transReqLst.add(mBSTransactionRequest);
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transformationObject.setTargetPojo(mBSTransactionRequest);
        doReturn(transReqLst).when(mBSTransactionRequestDao).getConcurrentLenderTradeRequest(any());
        doNothing().when(mBSTransactionRequestDao).saveOrUpdate(any());
        transactionPersister.persist(transformationObject);
    }
    
    public void persist_Concurrent_Trader_Price_Request_Failure() throws MBSBaseException {
        
        mBSTransactionRequest.setStateType(StateType.LENDER_OPEN.name());
        
        List<MBSTransactionRequest> transReqLst  = new ArrayList<MBSTransactionRequest>();
        transReqLst.add(mBSTransactionRequest);
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setMBSRoleType(MBSRoleType.TRADER);
        transformationObject.setTargetPojo(mBSTransactionRequest);
        doReturn(transReqLst).when(mBSTransactionRequestDao).getConcurrentLenderTradeRequest(any());
        doNothing().when(mBSTransactionRequestDao).saveOrUpdate(any());
        transactionPersister.persist(transformationObject);
    }
    
    @Test
    public void persistSaveEventsTest() throws MBSBaseException {
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setMBSRoleType(MBSRoleType.TRADER);
        transformationObject.setTargetPojo(mBSTransactionRequest);
        doNothing().when(mBSTransactionRequestDao).saveOrUpdate(any());
        doNothing().when(mBSTransactionRequestDao).saveEvents(any(), any());
        transactionPersister.persist(transformationObject);
    }
    
    @Test
    public void clearAllTest() throws MBSBaseException {
        
        doReturn(region).when(mBSTransactionRequestDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSTransactionRequest).when(region).destroy(any());
        transactionPersister.clearAll();
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void getDaoTest() throws MBSBaseException {
        BaseDaoImpl baseDao = transactionPersister.getDao();
        assertTrue(baseDao == null);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void getBaseDaoTest() throws MBSBaseException {
        MBSBaseDao<MBSTransactionRequest> baseDao = transactionPersister.getBaseDao();
        assertTrue(baseDao instanceof MBSTransactionRequestDao);
    }
    
    /**
     * @author gaur5c
     * @throws MBSBaseException
     */
    @Test
    public void clear() throws MBSBaseException {
        
        doReturn(region).when(mBSTransactionRequestDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSTransactionRequest).when(region).destroy(any());
        transactionPersister.clear("key");
    }
}
