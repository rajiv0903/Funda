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

package com.fanniemae.mbsportal.enrichment;

import static junit.framework.TestCase.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;

import com.fanniemae.mbsportal.api.enrichment.TransactionHistoryEnrichment;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.TradePO;
import com.fanniemae.mbsportal.api.po.TransactionHistoryPO;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * @author g8upjv Created on 9/26/2017.
 */

public class TransactionHistoryEnrichmentTest {
	
	@InjectLog
    private Logger LOGGER;
	
    @SuppressWarnings("rawtypes")
    TransactionHistoryEnrichment transactionHistoryEnrichment = new TransactionHistoryEnrichment();
	
	TransformationObject transformationObject;


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    TransactionHistoryPO mbsTransHist;
    
    List<TransactionRequestPO> lstMbsTransReq;
    
    TradePO tradePO;
    
    ProfileEntitlementPO lenderProfileEntitlementPO;
    ProfileEntitlementPO traderProfileEntitlementPO;

    
    /**
     * Purpose: Test case for successful call for enriching object to to get list of transactions
     * for trader Sell
     * 
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test
    public void enrichSell() throws MBSBaseException {
    	createSellData();
    	transformationObject.setMBSRoleType(MBSRoleType.TRADER);
    	transactionHistoryEnrichment.enrich(transformationObject);
    	mbsTransHist = (TransactionHistoryPO) transformationObject.getTargetPojo();
    	assertNotNull(mbsTransHist);
        
    }
    
    /**
     * Purpose: Test case for successful call for enriching object to to get list of transactions
     * for trader Buy
     *  
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test
    public void enrichBuy() throws MBSBaseException {
    	createBuyData();
    	transformationObject.setMBSRoleType(MBSRoleType.TRADER);
    	transactionHistoryEnrichment.enrich(transformationObject);
    	mbsTransHist = (TransactionHistoryPO) transformationObject.getTargetPojo();
    	assertNotNull(mbsTransHist);
        
    }
    
    /**
     * When lender, trader profile is throwing exception
     * @throws MBSBaseException
     */
    @Test
    public void enrichBuyEmptyLenderTrader() throws MBSBaseException {
    	createBuyData();
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.LENDER_PROFILE, null);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.TRADER_PROFILE, null);
    	transactionHistoryEnrichment.enrich(transformationObject);
    	mbsTransHist = (TransactionHistoryPO) transformationObject.getTargetPojo();
    	assertNotNull(mbsTransHist);
        
    }
    
    /**
     * Purpose: Test case when exception is thrown
     * 
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test(expected=MBSBaseException.class)
    public void enrichException() throws MBSBaseException {
    	createSellData();
    	transformationObject.getTransformationDataMap().put(MBSPServiceConstants.TRANSACTION_REQUEST, null);
    	transactionHistoryEnrichment.enrich(transformationObject);
    	mbsTransHist = (TransactionHistoryPO) transformationObject.getTargetPojo();
    	assertNotNull(mbsTransHist);
        
    }
    
    /**
     * Create buy Data
     */
    public void createBuyData(){
    	transformationObject = new TransformationObject();
    	TransactionRequestPO transactionRequestPO = new TransactionRequestPO();
    	ProductPO prodPO = new ProductPO();
    	ProductIdPO productId = new ProductIdPO();
    	lstMbsTransReq = new ArrayList<TransactionRequestPO>();
        productId.setIdentifier(new Long(21));
        productId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
        productId.setType(PRODUCT_TYPE.MBS);
        prodPO.setProductId(productId);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount("500000");
        transactionRequestPO.setTradeBuySellType("BID");
        transactionRequestPO.setTradeCouponRate("2.0");
        transactionRequestPO.setTradeSettlementDate("2017-10-12");
        transactionRequestPO.setLenderId("98765432");
        transactionRequestPO.setStateType(StateType.TRADER_CONFIRMED);
        transactionRequestPO.setPricePercent("123");
        transactionRequestPO.setPricePercentHandleText("101");
        transactionRequestPO.setPricePercentTicksText("101");
        transactionRequestPO.setSubmissionDate("2017-09-26");
        transactionRequestPO.setTraderId("testTrader");
        transactionRequestPO.setTransReqId("17J00001");
        transactionRequestPO.setLenderName("testLenderLastName,testLenderFirstName");
        transactionRequestPO.setLenderName("testLenderLastName,testLenderFirstName");
        lstMbsTransReq.add(transactionRequestPO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.TRANSACTION_REQUEST, transactionRequestPO);
        tradePO = new TradePO();
        tradePO.setLenderEntityName("TEST-C");
        tradePO.setTradeDate("2017-10-12");
        tradePO.setTradeSourceId("BRS");
        tradePO.setTradeSourcePrimaryId(1234);
        tradePO.setTradeSubPortfolioId(12);
        tradePO.setTradeSubPortfolioShortName("STS-OPS");
        tradePO.setTransactionRequestId("17J00001");
        lenderProfileEntitlementPO = new ProfileEntitlementPO();
        traderProfileEntitlementPO = new ProfileEntitlementPO();
        lenderProfileEntitlementPO.setFirstName("testLenderFirstName");
        lenderProfileEntitlementPO.setUserName("testLender");
        traderProfileEntitlementPO.setUserName("testTrader");
        lenderProfileEntitlementPO.setLastName("testLenderLastName");
        traderProfileEntitlementPO.setFirstName("testTraderFirstName");
        traderProfileEntitlementPO.setLastName("testTraderLastName");
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.TRADE_REQUEST, tradePO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.LENDER_PROFILE, lenderProfileEntitlementPO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.TRADER_PROFILE, traderProfileEntitlementPO);
    }
    
    /**
     * Create Sell Data
     */
    public void createSellData(){
    	transformationObject = new TransformationObject();
    	TransactionRequestPO transactionRequestPO = new TransactionRequestPO();
    	ProductPO prodPO = new ProductPO();
    	ProductIdPO productId = new ProductIdPO();
    	lstMbsTransReq = new ArrayList<TransactionRequestPO>();
        productId.setIdentifier(new Long(21));
        productId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
        productId.setType(PRODUCT_TYPE.MBS);
        prodPO.setProductId(productId);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount("500000");
        transactionRequestPO.setTradeBuySellType("OFFER");
        transactionRequestPO.setTradeCouponRate("2.0");
        transactionRequestPO.setTradeSettlementDate("2017-10-12");
        transactionRequestPO.setLenderId("98765432");
        transactionRequestPO.setStateType(StateType.TRADER_CONFIRMED);
        transactionRequestPO.setPricePercent("123");
        transactionRequestPO.setPricePercentHandleText("101");
        transactionRequestPO.setPricePercentTicksText("101");
        transactionRequestPO.setSubmissionDate("2017-09-26");
        transactionRequestPO.setTraderId("testTrader");
        transactionRequestPO.setTransReqId("17J00001");
        lstMbsTransReq.add(transactionRequestPO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.TRANSACTION_REQUEST, transactionRequestPO);
        tradePO = new TradePO();
        tradePO.setLenderEntityName("TEST-C");
        tradePO.setTradeDate("2017-10-12");
        tradePO.setTradeSourceId("BRS");
        tradePO.setTradeSourcePrimaryId(1234);
        tradePO.setTradeSubPortfolioId(12);
        tradePO.setTradeSubPortfolioShortName("STS-OPS");
        tradePO.setTransactionRequestId("17J00001");
        lenderProfileEntitlementPO = new ProfileEntitlementPO();
        traderProfileEntitlementPO = new ProfileEntitlementPO();
        lenderProfileEntitlementPO.setFirstName("testLenderFirstName");
        lenderProfileEntitlementPO.setUserName("testLender");
        traderProfileEntitlementPO.setUserName("testTrader");
        lenderProfileEntitlementPO.setLastName("testLenderLastName");
        traderProfileEntitlementPO.setFirstName("testTraderFirstName");
        traderProfileEntitlementPO.setLastName("testTraderLastName");
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.TRADE_REQUEST, tradePO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.LENDER_PROFILE, lenderProfileEntitlementPO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.TRADER_PROFILE, traderProfileEntitlementPO);
    }
    /**
     * Purpose: Creates the transactionRequestPO data model
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

    }
}
