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

package com.fanniemae.mbsportal.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.geode.cache.CommitConflictException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.fanniemae.mbsportal.api.publisher.TransactionRequestMessagePublisher;
import com.fanniemae.mbsportal.api.transformation.TransactionRequestPOTransformer;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.dao.MBSTransactionRequestDao;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Dec 7, 2017
 * @Time 1:42:27 PM com.fanniemae.mbsportal.api.utils TransactionServiceTest.java
 * @Description:
 */
public class TransactionServiceTest extends BaseServiceTest {

    @InjectMocks
    TransactionEventService transactionEventService;

    @Mock
    MBSTransactionRequestDao mBSTransactionRequestDao;
    
    @Mock
    MBSObjectCreator mbsObjectCreator;
    
    @Mock
    TransactionRequestPOTransformer<TransformationObject> transactionRequestPOTransformer;
    
    @Mock
    TransactionRequestMessagePublisher transactionRequestMessagePublisher;

    MBSTransactionRequest mBSTransactionRequest;
    String transReqNumber;
    
    TransformationObject transformationObject;

    @Before
    public void setUp() throws Exception {

        transReqNumber = "17L1000";

        mBSTransactionRequest = new MBSTransactionRequest();
        mBSTransactionRequest.setTransReqNumber(transReqNumber);
        mBSTransactionRequest.setTradeSettlementDate(new Date());
        mBSTransactionRequest.setStateType(StateType.LENDER_OPEN.name());
        
        transformationObject = new TransformationObject();
        transformationObject.setMBSRoleType(MBSRoleType.TRADER);
        transformationObject.setTargetPojo(mBSTransactionRequest);
    }

    @Test
    public void persistTradeAsTimedout_Lender_Open_test() throws MBSBaseException {

        doReturn(mBSTransactionRequest).when(mBSTransactionRequestDao).getById(any());
        doNothing().when(mBSTransactionRequestDao).saveOrUpdate(any());
        doNothing().when(mBSTransactionRequestDao).saveEvents(any(), any());
        
        doReturn(transformationObject).when(mbsObjectCreator).getTransformationObject();
        doReturn(transformationObject).when(transactionRequestPOTransformer).transform(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        
        boolean result = transactionEventService.persistTradeAsTimedout(mBSTransactionRequest);
        assertTrue(result == true);
    }

    @Test
    public void persistTradeAsTimedout_Trader_Priced_test() throws MBSBaseException {

        mBSTransactionRequest.setStateType(StateType.TRADER_PRICED.name());

        doReturn(mBSTransactionRequest).when(mBSTransactionRequestDao).getById(any());
        doNothing().when(mBSTransactionRequestDao).saveOrUpdate(any());
        doNothing().when(mBSTransactionRequestDao).saveEvents(any(), any());
        
        doReturn(transformationObject).when(mbsObjectCreator).getTransformationObject();
        doReturn(transformationObject).when(transactionRequestPOTransformer).transform(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        
        boolean result = transactionEventService.persistTradeAsTimedout(mBSTransactionRequest);
        assertTrue(result == true);
    }

    @Test
    public void persistTradeAsTimedout_Pass_InValid_Object_test() throws MBSBaseException {

        mBSTransactionRequest.setStateType(StateType.TRADER_PRICED.name());

        doReturn(null).when(mBSTransactionRequestDao).getById(any());
        doNothing().when(mBSTransactionRequestDao).saveOrUpdate(any());
        doNothing().when(mBSTransactionRequestDao).saveEvents(any(), any());
        
        doReturn(transformationObject).when(mbsObjectCreator).getTransformationObject();
        doReturn(transformationObject).when(transactionRequestPOTransformer).transform(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        
        boolean result = transactionEventService.persistTradeAsTimedout(mBSTransactionRequest);
        assertTrue(result == true);
    }

    @Test
    public void persistTradeAsTimedout_Lender_TimeOut_test() throws MBSBaseException {

        mBSTransactionRequest.setStateType(StateType.LENDER_TIMEOUT.name());

        doReturn(mBSTransactionRequest).when(mBSTransactionRequestDao).getById(any());
        doNothing().when(mBSTransactionRequestDao).saveOrUpdate(any());
        doNothing().when(mBSTransactionRequestDao).saveEvents(any(), any());
        
        doReturn(transformationObject).when(mbsObjectCreator).getTransformationObject();
        doReturn(transformationObject).when(transactionRequestPOTransformer).transform(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        
        boolean result = transactionEventService.persistTradeAsTimedout(mBSTransactionRequest);
        assertTrue(result == true);
    }

    @Test
    public void persistTradeAsTimedout_Trader_TimeOut_test() throws MBSBaseException {

        mBSTransactionRequest.setStateType(StateType.TRADER_TIMEOUT.name());

        doReturn(mBSTransactionRequest).when(mBSTransactionRequestDao).getById(any());
        doNothing().when(mBSTransactionRequestDao).saveOrUpdate(any());
        doNothing().when(mBSTransactionRequestDao).saveEvents(any(), any());
        
        doReturn(transformationObject).when(mbsObjectCreator).getTransformationObject();
        doReturn(transformationObject).when(transactionRequestPOTransformer).transform(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        
        boolean result = transactionEventService.persistTradeAsTimedout(mBSTransactionRequest);
        assertTrue(result == true);
    }

    @Test(expected = MBSBaseException.class)
    public void persistTradeAsTimedout_Throw_Exception_test() throws MBSBaseException {

        mBSTransactionRequest.setStateType(StateType.TRADER_PRICED.name());

        doThrow(RuntimeException.class).when(mBSTransactionRequestDao).getById(any());
        doNothing().when(mBSTransactionRequestDao).saveOrUpdate(any());
        doNothing().when(mBSTransactionRequestDao).saveEvents(any(), any());        
        
        doReturn(transformationObject).when(mbsObjectCreator).getTransformationObject();
        doReturn(transformationObject).when(transactionRequestPOTransformer).transform(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        
        boolean result = transactionEventService.persistTradeAsTimedout(mBSTransactionRequest);
        assertTrue(result == true);
    }

    @Test
    public void persistTradeAsTimedout_Throw_CommitConflictException_Exception_test() throws MBSBaseException {

        mBSTransactionRequest.setStateType(StateType.LENDER_OPEN.name());

        doReturn(mBSTransactionRequest).when(mBSTransactionRequestDao).getById(any());
        doThrow(CommitConflictException.class).when(mBSTransactionRequestDao).saveOrUpdate(any());
        doNothing().when(mBSTransactionRequestDao).saveEvents(any(), any());        
        
        doReturn(transformationObject).when(mbsObjectCreator).getTransformationObject();
        doReturn(transformationObject).when(transactionRequestPOTransformer).transform(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        
        boolean result = transactionEventService.persistTradeAsTimedout(mBSTransactionRequest);
        assertTrue(result == false);
    }
    
    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void persistTradeAsTimedout_Already_Trader_Timed_Out__Publish_Success() throws MBSBaseException {

        mBSTransactionRequest.setStateType(StateType.TRADER_TIMEOUT.name());
        doReturn(mBSTransactionRequest).when(mBSTransactionRequestDao).getById(any());
        doNothing().when(mBSTransactionRequestDao).saveOrUpdate(any());
        doNothing().when(mBSTransactionRequestDao).saveEvents(any(), any());
        
        doReturn(transformationObject).when(mbsObjectCreator).getTransformationObject();
        doReturn(transformationObject).when(transactionRequestPOTransformer).transform(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        
        boolean result = transactionEventService.persistTradeAsTimedout(mBSTransactionRequest);
        assertTrue(result == true);
    }
    
    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void persistTradeAsTimedout_Already_Lender_Timed_Out__Publish_Success() throws MBSBaseException {

        mBSTransactionRequest.setStateType(StateType.TRADER_TIMEOUT.name());
        doReturn(mBSTransactionRequest).when(mBSTransactionRequestDao).getById(any());
        doNothing().when(mBSTransactionRequestDao).saveOrUpdate(any());
        doNothing().when(mBSTransactionRequestDao).saveEvents(any(), any());
        
        doReturn(transformationObject).when(mbsObjectCreator).getTransformationObject();
        doReturn(transformationObject).when(transactionRequestPOTransformer).transform(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        
        boolean result = transactionEventService.persistTradeAsTimedout(mBSTransactionRequest);
        assertTrue(result == true);
    }

    @Test
    public void getWhoTimedOut_Lender_test() {

        String stateType = transactionEventService.getWhoTimedOut("lender");
        assertEquals(StateType.TRADER_TIMEOUT.name(), stateType);
    }

    @Test
    public void getWhoTimedOut_TRader_test() {

        String stateType = transactionEventService.getWhoTimedOut("trader");
        assertEquals(StateType.LENDER_TIMEOUT.name(), stateType);
    }

    @Test
    public void scanStatusAndTimedOut_Add_Remove_To_Scan_Test() throws Exception {
        transactionEventService.add(mBSTransactionRequest.getTransReqNumber(), mBSTransactionRequest);
        assertEquals(1, transactionEventService.tradesToScan.size());
        transactionEventService.remove(mBSTransactionRequest.getTransReqNumber());
        assertEquals(0, transactionEventService.tradesToScan.size());
    }

    @Test
    public void scanStatusAndTimedOut_Test() throws Exception {

        mBSTransactionRequest.setSubmissionDate(new Date(System.currentTimeMillis() - 150000));
        transactionEventService.add(mBSTransactionRequest.getTransReqNumber(), mBSTransactionRequest);
        doNothing().when(mBSTransactionRequestDao).saveOrUpdate(any());
        doNothing().when(mBSTransactionRequestDao).saveEvents(any(), any());

        assertEquals(1, transactionEventService.tradesToScan.size());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    transactionEventService.scanStatusAndTimedOut();
                } catch (Throwable throwable) {
                } finally {
                }
            }
        }).start();
        Thread.sleep(3000);
        transactionEventService.isActive = false;
        assertEquals(0, transactionEventService.tradesToScan.size());
    }
    
    @Test
    public void getActiveTransactions_Success() throws MBSBaseException{
        
        List<MBSTransactionRequest> mbsTransReq = new ArrayList();
        mbsTransReq.add(mBSTransactionRequest);
        Mockito.when(mBSTransactionRequestDao.getTransReqStateType(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(mbsTransReq);
        List<MBSTransactionRequest> mbsTransactionRequestLst = transactionEventService.getActiveTransactions("MBSPDEV");
        assertEquals(1, mbsTransactionRequestLst.size());
        
    }
    
    @Test
    public void getActiveTransactions_Exception() throws MBSBaseException{
        
        List<MBSTransactionRequest> mbsTransReq = new ArrayList();
        mbsTransReq.add(mBSTransactionRequest);
        Mockito.when(mBSTransactionRequestDao.getTransReqStateType(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(new MBSBaseException("test"));
        List<MBSTransactionRequest> mbsTransactionRequestLst = transactionEventService.getActiveTransactions("MBSPDEV");
        assertEquals(0, mbsTransactionRequestLst.size());
        
    }
}
