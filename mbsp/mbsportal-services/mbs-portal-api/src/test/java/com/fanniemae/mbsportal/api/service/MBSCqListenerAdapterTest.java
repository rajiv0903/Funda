/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */


package com.fanniemae.mbsportal.api.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.geode.cache.Operation;
import org.apache.geode.cache.query.CqEvent;
import org.apache.geode.cache.query.CqException;
import org.apache.geode.cache.query.CqExistsException;
import org.apache.geode.cache.query.CqQuery;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.RegionNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.fanniemae.mbsportal.api.config.TradeServiceProperties;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author g8uaxt Created on 12/8/2017.
 */

public class MBSCqListenerAdapterTest extends BaseServiceTest {
    @Spy
    MBSCqListenerAdapter mbsCqListenerAdapter;
    @Mock
    TransactionEventService transactionEventService;
    @Mock
    CqQuery cqQuery;
    @Mock
    QueryService queryService;

    @Mock
    CqEvent aCqEvent;

    @Mock
    TradeServiceProperties tradeServiceProperties;

    @Before
    public void setUp() throws Exception {
        when(queryService.newCq(any(), any(), any())).thenReturn(cqQuery);
        doNothing().when(cqQuery).execute();
        when(tradeServiceProperties.getSourceSystem()).thenReturn("MBSP");
        when(mbsCqListenerAdapter.getQueryService()).thenReturn(queryService);
        mbsCqListenerAdapter.setTransactionEventService(transactionEventService);
        mbsCqListenerAdapter.setTradeServiceProperties(tradeServiceProperties);
        doNothing().when(transactionEventService).scanStatusAndTimedOut();
    }

    @Test
    public void startCqListener() throws InterruptedException, CqExistsException, RegionNotFoundException, CqException, Exception {
        mbsCqListenerAdapter.startCqListener();
    }

    @Test
    public void onEventAddForTRADER_REPRICED() {
        when(aCqEvent.getBaseOperation()).thenReturn(Operation.UPDATE);
        when(aCqEvent.getNewValue()).thenReturn(getMBSTransactionRequest(StateType.TRADER_REPRICED.name()));
        when(aCqEvent.getKey()).thenReturn("key-1");
        mbsCqListenerAdapter.onEvent(aCqEvent);
    }

    @Test
    public void onEventAddForLENDER_OPEN() {
        when(aCqEvent.getBaseOperation()).thenReturn(Operation.CREATE);
        when(aCqEvent.getNewValue()).thenReturn(getMBSTransactionRequest(StateType.LENDER_OPEN.name()));
        when(aCqEvent.getKey()).thenReturn("key-1");
        mbsCqListenerAdapter.onEvent(aCqEvent);
    }

    @Test
    public void onEventRemoveForTRADER_CONFIRMED() {
        when(aCqEvent.getBaseOperation()).thenReturn(Operation.UPDATE);
        when(aCqEvent.getNewValue()).thenReturn(getMBSTransactionRequest(StateType.TRADER_CONFIRMED.name()));
        when(aCqEvent.getKey()).thenReturn("key-2");
        mbsCqListenerAdapter.onEvent(aCqEvent);
    }

    @Test
    public void onEventRemoveForLENDER_REJECTED() {
        when(aCqEvent.getBaseOperation()).thenReturn(Operation.UPDATE);
        when(aCqEvent.getNewValue()).thenReturn(getMBSTransactionRequest(StateType.LENDER_REJECTED.name()));
        when(aCqEvent.getKey()).thenReturn("key-3");
        mbsCqListenerAdapter.onEvent(aCqEvent);
    }

    @Test
    public void onEventRemoveForTRADER_REJECTED() {
        when(aCqEvent.getBaseOperation()).thenReturn(Operation.UPDATE);
        when(aCqEvent.getNewValue()).thenReturn(getMBSTransactionRequest(StateType.TRADER_REJECTED.name()));
        when(aCqEvent.getKey()).thenReturn("key-3");
        mbsCqListenerAdapter.onEvent(aCqEvent);
    }

    @Test
    public void onEventRemoveForTRADER_PASSED() {
        when(aCqEvent.getBaseOperation()).thenReturn(Operation.UPDATE);
        when(aCqEvent.getNewValue()).thenReturn(getMBSTransactionRequest(StateType.TRADER_PASSED.name()));
        when(aCqEvent.getKey()).thenReturn("key-3");
        mbsCqListenerAdapter.onEvent(aCqEvent);
    }

    @Test
    public void onEventRemoveForPENDING_EXECUTION() {
        when(aCqEvent.getBaseOperation()).thenReturn(Operation.UPDATE);
        when(aCqEvent.getNewValue()).thenReturn(getMBSTransactionRequest(StateType.PENDING_EXECUTION.name()));
        when(aCqEvent.getKey()).thenReturn("key-3");
        mbsCqListenerAdapter.onEvent(aCqEvent);
    }

    @Test
    public void onEventRemoveForEXECUTED() {
        when(aCqEvent.getBaseOperation()).thenReturn(Operation.UPDATE);
        when(aCqEvent.getNewValue()).thenReturn(getMBSTransactionRequest(StateType.EXECUTED.name()));
        when(aCqEvent.getKey()).thenReturn("key-3");
        mbsCqListenerAdapter.onEvent(aCqEvent);
    }

    @Test
    public void onEventRemoveForLENDER_TIMEOUT() {
        when(aCqEvent.getBaseOperation()).thenReturn(Operation.UPDATE);
        when(aCqEvent.getNewValue()).thenReturn(getMBSTransactionRequest(StateType.LENDER_TIMEOUT.name()));
        when(aCqEvent.getKey()).thenReturn("key-3");
        mbsCqListenerAdapter.onEvent(aCqEvent);
    }

    @Test
    public void onEventRemoveForTRADER_TIMEOUT() {
        when(aCqEvent.getBaseOperation()).thenReturn(Operation.UPDATE);
        when(aCqEvent.getNewValue()).thenReturn(getMBSTransactionRequest(StateType.TRADER_TIMEOUT.name()));
        when(aCqEvent.getKey()).thenReturn("key-3");
        mbsCqListenerAdapter.onEvent(aCqEvent);
    }

    @Test
    public void onEventRemoveForERROR() {
        when(aCqEvent.getBaseOperation()).thenReturn(Operation.UPDATE);
        when(aCqEvent.getNewValue()).thenReturn(getMBSTransactionRequest(StateType.ERROR.name()));
        when(aCqEvent.getKey()).thenReturn("key-3");
        mbsCqListenerAdapter.onEvent(aCqEvent);
    }

    @Test
    public void onEventRemoveForNET_LOAD_UPDATE() {
        when(aCqEvent.getBaseOperation()).thenReturn(Operation.NET_LOAD_UPDATE);
        when(aCqEvent.getNewValue()).thenReturn(getMBSTransactionRequest(StateType.ERROR.name()));
        when(aCqEvent.getKey()).thenReturn("key-3");
        mbsCqListenerAdapter.onEvent(aCqEvent);
    }

    @Test
    public void onError() {
        when(aCqEvent.getQueryOperation()).thenReturn(Operation.UPDATE);
        mbsCqListenerAdapter.onError(aCqEvent);
    }
    
    @Test
    public void refreshActiveTrades_success() throws MBSBaseException{
        
        when(transactionEventService.getActiveTransactions(Mockito.any())).thenReturn(createTransList());
        doNothing().when(transactionEventService).add(Mockito.any(), Mockito.any());
        mbsCqListenerAdapter.refreshActiveTrades();
        
    }

    // @After
    // @AfterClass//TODO: call it once after all the test cases
    public void close() {
        mbsCqListenerAdapter.close();
    }

    /**
     * utility method to get setup data
     * 
     * @param stateType
     * @return
     */
    private MBSTransactionRequest getMBSTransactionRequest(String stateType) {
        MBSTransactionRequest mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(stateType);
        return mbsTransactionRequest;
    }
    
    private List<MBSTransactionRequest> createTransList(){
        List<MBSTransactionRequest> mbsTransLst = new ArrayList();
        MBSTransactionRequest mBSTransactionRequest = new MBSTransactionRequest();
        mBSTransactionRequest.setTransReqNumber("17I00001");
        mBSTransactionRequest.setTradeSettlementDate(new Date());
        mBSTransactionRequest.setStateType(StateType.LENDER_OPEN.name());
        mbsTransLst.add(mBSTransactionRequest);
        return mbsTransLst;
    }
    


}
