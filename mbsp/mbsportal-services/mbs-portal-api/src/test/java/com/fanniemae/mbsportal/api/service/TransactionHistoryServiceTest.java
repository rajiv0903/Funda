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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.fanniemae.mbsportal.api.config.TradeServiceProperties;
import com.fanniemae.mbsportal.api.enrichment.TransactionHistoryEnrichment;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementRolePO;
import com.fanniemae.mbsportal.api.po.TradePO;
import com.fanniemae.mbsportal.api.po.TransactionHistoryPO;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.api.utils.ExportUtils;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.gf.pojo.SortBy;
import com.fanniemae.mbsportal.model.MBSTrade;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import com.fanniemae.securitiesods.ods_core.domain.TradeParty;

import junit.framework.Assert;

/**
 * This class handles the test case for the TransactionHistoryService class
 *
 * @author g8upjv
 *
 */

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 22, 2018
 * @Time 2:30:39 PM
 * 	com.fanniemae.mbsportal.api.service
 * 	TransactionHistoryServiceTest.java
 * @Description: CMMBSSTA01-962: (Tech) Improve CAST for MBSP
 */

public class TransactionHistoryServiceTest extends BaseServiceTest {

    @Mock
    ProfileEntitlementService profileEntitlementService;
    @Mock
    TransactionHistoryEnrichment<TransformationObject> transactionHistoryEnrichment;
    @InjectMocks
    TransactionHistoryService transactionHistoryService;
    @InjectLog
    private Logger LOGGER;
    @Mock
    private TransactionRequestService transactionRequestService;
    @Mock
    private TransactionExportService transactionExportService;
    @Mock
    private TradeService tradeService;
    @Mock
    MBSObjectCreator mbsObjectCreator;
    @Mock
    ExportUtils exportUtils;
    private TransactionRequestPO transactionRequestPO;
    private ProductPO prodPO;
    private ProductIdPO productId;
    private List<MBSTransactionRequest> lstMbsTrans;
    private List<TransactionRequestPO> lstTransPO;
    private List<ProductPO> prodPOLst;

    private MBSTrade mbsTrade;
    private TransformationObject transformationObject;
    private TradePO mbsTradePO;
    private List<TradePO> mbsTradePOLst;
    private List<MBSTrade> mbsMBSTradeLst;
    private List<ProfileEntitlementPO> profileEntitlementPOLst;
    private ProfileEntitlementPO profileEntitlementPO;
    private ProfileEntitlementRolePO profileEntitlementRolePO;

    @Mock
    TradeServiceProperties tradeServiceProperties;
    
    @Before
    public void setUp() throws Exception {
        transactionHistoryService = new TransactionHistoryService(transactionHistoryEnrichment);
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Purpose: This method test call to get the data from gemfire, when status
     * is EXECUTED
     *
     * @throws Exception
     */
    @Test
    public void getTransactionRequestSorted_Lender_Success() throws MBSBaseException {
        createData();
        when(transactionRequestService.getMBSTransReqHistory(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(lstTransPO);
        when(tradeService.getMBSTrades(Mockito.any())).thenReturn(mbsTradePOLst);
        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
        when(tradeServiceProperties.getShakeOutCPartyName()).thenReturn("T");
        Mockito.doNothing().when(transactionHistoryEnrichment).enrich(Mockito.any());
        String sellerServiceNumber = "121212";
        String sortBy = "tradeBuySellType";
        String sortOrder = SortBy.asc.getName();
        Integer pageIndex = 1;
        Integer pageSize = 100;
        List<TransactionHistoryPO> lstTransPOResult = transactionHistoryService
                .getTransactionRequestSorted(MBSRoleType.LENDER, sellerServiceNumber, false, sortBy, sortOrder, pageIndex, pageSize);
        assertNotNull(lstTransPOResult);
    }
    
    /**
     * Purpose: This method test call to get the data from gemfire, when status
     * is EXECUTED
     *
     * @throws Exception
     */
    @Test
    public void getTransactionRequestSorted_Trader_Success() throws MBSBaseException {
        createData();
        when(transactionRequestService.getMBSTransReqHistory(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(lstTransPO);
        when(tradeService.getMBSTrades(Mockito.any())).thenReturn(mbsTradePOLst);
        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
        when(tradeServiceProperties.getShakeOutCPartyName()).thenReturn("T");
        Mockito.doNothing().when(transactionHistoryEnrichment).enrich(Mockito.any());
        String sellerServiceNumber = "121212";
        String sortBy = "tradeBuySellType";
        String sortOrder = SortBy.asc.getName();
        Integer pageIndex = 1;
        Integer pageSize = 100;
        List<TransactionHistoryPO> lstTransPOResult = transactionHistoryService
                .getTransactionRequestSorted(MBSRoleType.TRADER, sellerServiceNumber, false, sortBy, sortOrder, pageIndex, pageSize);
        assertNotNull(lstTransPOResult);
    }

    @Test
    public void getTransactionRequestSorted_Trader_Success_Accepted() throws MBSBaseException {
        createData();
        when(transactionRequestService.getMBSTransReqHistory(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(lstTransPO);
        when(tradeService.getMBSTrades(Mockito.any())).thenReturn(mbsTradePOLst);
        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
        when(tradeServiceProperties.getShakeOutCPartyName()).thenReturn("T");
        Mockito.doNothing().when(transactionHistoryEnrichment).enrich(Mockito.any());
        String sellerServiceNumber = "121212";
        String sortBy = "tradeBuySellType";
        String sortOrder = SortBy.asc.getName();
        Integer pageIndex = 1;
        Integer pageSize = 100;
        List<TransactionHistoryPO> lstTransPOResult = transactionHistoryService
                .getTransactionRequestSorted(MBSRoleType.TRADER, sellerServiceNumber, true, sortBy, sortOrder, pageIndex, pageSize);
        assertNotNull(lstTransPOResult);
    }
    
    
    /**
     * Purpose: This method test call to get the data from gemfire, when status
     * is EXECUTED
     *
     * @throws Exception
     */
    @Test
    public void getTransactionRequestSorted_Trader_Empty_TransReq_Success() throws MBSBaseException {
        createData();
        when(transactionRequestService.getMBSTransReqHistory(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(null);
        when(tradeService.getMBSTrade(Mockito.any())).thenReturn(mbsTradePOLst);
        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
        when(tradeServiceProperties.getShakeOutCPartyName()).thenReturn("T");
        Mockito.doNothing().when(transactionHistoryEnrichment).enrich(Mockito.any());
        String sellerServiceNumber = "121212";
        String sortBy = "tradeBuySellType";
        String sortOrder = SortBy.asc.getName();
        Integer pageIndex = 1;
        Integer pageSize = 100;
        List<TransactionHistoryPO> lstTransPOResult = transactionHistoryService
                .getTransactionRequestSorted(MBSRoleType.TRADER, sellerServiceNumber, false, sortBy, sortOrder, pageIndex, pageSize);
        assertNotNull(lstTransPOResult);
    }

    /**
     * Purpose: This method test call to get the data from gemfire, when status
     * is not EXECUTED
     *
     * @throws Exception
     */
    @Test
    public void getTransactionRequestSorted_Trader_Non_Executed_Success() throws MBSBaseException {
        createNonExecutedData();
        when(transactionRequestService.getMBSTransReqHistory(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(lstTransPO);
        when(tradeService.getMBSTrades(Mockito.any())).thenReturn(mbsTradePOLst);
        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
        when(tradeServiceProperties.getShakeOutCPartyName()).thenReturn("T");
        Mockito.doNothing().when(transactionHistoryEnrichment).enrich(Mockito.any());
        String sellerServiceNumber = "121212";
        String sortBy = "tradeBuySellType";
        String sortOrder = SortBy.asc.getName();
        Integer pageIndex = 1;
        Integer pageSize = 100;
        List<TransactionHistoryPO> lstTransPOResult = transactionHistoryService
                .getTransactionRequestSorted(MBSRoleType.TRADER, sellerServiceNumber, false, sortBy, sortOrder, pageIndex, pageSize);
        assertNotNull(lstTransPOResult);
    }
    

    /**
     * Method to create data for the test cases
     */
    public void createData() {
        profileEntitlementPOLst = new ArrayList<ProfileEntitlementPO>();
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setLenderId("testLender");
        transactionRequestPO.setStateType(StateType.EXECUTED);
        transactionRequestPO.setTransReqId("17J00004");
        transactionRequestPO.setTraderId("testTrader");
        transactionRequestPO.setLenderId("testLender");
        transactionRequestPO.setLenderEntityShortName("TEST");
        lstTransPO = new ArrayList<TransactionRequestPO>();
        lstTransPO.add(transactionRequestPO);
        transformationObject = new TransformationObject();
        mbsTrade = new MBSTrade();
        TradeParty tradeParty = new TradeParty();
        tradeParty.setPartyShortName("TEST-C");
        mbsTrade.setTransReqNumber("17J00004");
        mbsTradePO = new TradePO();
        mbsTradePO.setTransactionRequestId(mbsTrade.getTransReqNumber());
        mbsTradePOLst = new ArrayList<>();
        mbsMBSTradeLst = new ArrayList<>();
        mbsTradePOLst.add(mbsTradePO);
        mbsMBSTradeLst.add(mbsTrade);
        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementRolePO = new ProfileEntitlementRolePO();
        profileEntitlementRolePO.setName("MBS Trade - Read Only");
        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementPO.setUserName("testLenderTrader");
        profileEntitlementPO.setFirstName("fn");
        profileEntitlementPO.setLastName("ln");
        profileEntitlementPO.setEmailAddress("test@gmail.com");
        profileEntitlementPO.setPartyShortName("TEST");
        profileEntitlementPO.addRole(profileEntitlementRolePO);
        profileEntitlementPOLst.add(profileEntitlementPO);
        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementPO.setUserName("testLender");
        profileEntitlementPO.setFirstName("fn");
        profileEntitlementPO.setLastName("ln");
        profileEntitlementPO.setEmailAddress("test@gmail.com");
        profileEntitlementPO.setPartyShortName("TEST");
        profileEntitlementPO.addRole(profileEntitlementRolePO);
        profileEntitlementPOLst.add(profileEntitlementPO);
        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementPO.setUserName("testTrader");
        profileEntitlementPO.setFirstName("fn");
        profileEntitlementPO.setLastName("ln");
        profileEntitlementPO.setEmailAddress("test@gmail.com");
        profileEntitlementPO.setPartyShortName("TEST");
        profileEntitlementPO.addRole(profileEntitlementRolePO);
        profileEntitlementPOLst.add(profileEntitlementPO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.TRADE_REQUEST, mbsTradePOLst.get(0));
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.TRANSACTION_REQUEST, transactionRequestPO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.LENDER_PROFILE, profileEntitlementPO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.TRADER_PROFILE, profileEntitlementPO);
    }

    /**
     * Method to create data for the test cases
     */
    public void createNonExecutedData() {

        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setLenderId("");
        transactionRequestPO.setStateType(StateType.TRADER_CONFIRMED);
        transactionRequestPO.setTransReqId("17J00004");
        transactionRequestPO.setTraderId("");
        lstTransPO = new ArrayList<TransactionRequestPO>();
        lstTransPO.add(transactionRequestPO);
        transformationObject = new TransformationObject();
        mbsTrade = new MBSTrade();
        TradeParty tradeParty = new TradeParty();
        tradeParty.setPartyShortName("TEST-C");
        mbsTrade.setTransReqNumber("17J00004");
        mbsTradePO = new TradePO();
        mbsTradePO.setTransactionRequestId(mbsTrade.getTransReqNumber());
        mbsTradePOLst = new ArrayList<>();
        mbsMBSTradeLst = new ArrayList<>();
        mbsTradePOLst.add(mbsTradePO);
        mbsMBSTradeLst.add(mbsTrade);
        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementRolePO = new ProfileEntitlementRolePO();
        profileEntitlementRolePO.setName("MBS Trade - Read Only");
        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementPO.setUserName("testLenderTrader");
        profileEntitlementPO.setFirstName("fn");
        profileEntitlementPO.setLastName("ln");
        profileEntitlementPO.setEmailAddress("test@gmail.com");
        profileEntitlementPO.addRole(profileEntitlementRolePO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.TRADE_REQUEST, mbsTradePOLst.get(0));
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.TRANSACTION_REQUEST, transactionRequestPO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.LENDER_PROFILE, profileEntitlementPO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.TRADER_PROFILE, profileEntitlementPO);

    }

    
    @Test
    public void testGetTransactionRequestSortedSize() throws MBSBaseException {
    	createData();
        String sellerServiceNumber = "121212";
        when(transactionExportService.getMBSTransReqHistorySize(Mockito.any(), Mockito.anyList(), Mockito.any())).thenReturn(1000);
    	Integer size = transactionHistoryService.getTransactionRequestSortedSize(MBSRoleType.LENDER, sellerServiceNumber, true);
    	assertEquals(size, new Integer(1000));
    	size = transactionHistoryService.getTransactionRequestSortedSize(MBSRoleType.LENDER, sellerServiceNumber, false);
    	assertEquals(size, new Integer(1000));
    }
}
