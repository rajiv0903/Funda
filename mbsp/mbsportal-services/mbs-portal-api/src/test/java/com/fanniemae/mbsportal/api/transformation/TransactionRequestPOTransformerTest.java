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

package com.fanniemae.mbsportal.api.transformation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.constants.TradeBuySell;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * Class Name: TransactionRequestPOTransformerTest Purpose : This class handles
 * the test case for the TransactionRequestPOTransformer class
 * 
 * date 08/02/2017
 * 
 * @author g8upjv
 * 
 */

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 18, 2018
 * @Time 2:12:31 PM com.fanniemae.mbsportal.api.transformation
 *       TransactionRequestPOTransformerTest.java
 * @Description: CMMBSSTA01-1023: (Trader page) Services test: Price input
 *               validation errors
 */
/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 25, 2018
 * @Time 2:48:19 PM
 * 	com.fanniemae.mbsportal.api.transformation
 * 	TransactionRequestPOTransformerTest.java
 * @Description: CMMBSSTA01-1040: (Trader page) Allow '+' in third digit of ticks field and display in ticket and trx history
 */

public class TransactionRequestPOTransformerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionRequestPOTransformerTest.class);
    @SuppressWarnings("rawtypes")
    private TransactionRequestPOTransformer transactionRequestPOTransformer = new TransactionRequestPOTransformer();

    private TransactionRequestPO transactionRequestPO;

    private MBSTransactionRequest mbsTransactionRequest;

    private ProductId prodId;

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    MBSMarketIndicativePrice mbsMarketIndicativePrice;

    /**
     * Purpose: This method tests the call to transformation from
     * TransactionRequestPO to MBSTransaction for successful call
     * 
     * @throws Exception
     */
    @Test
    public void transform_sell() throws Exception {
        mbsTransactionRequest = new MBSTransactionRequest();
        prodId = new ProductId();
        prodId.setIdentifier(new Long(1));
        prodId.setSourceType(PRODUCT_SOURCE_TYPE.PU.toString());
        prodId.setType(PRODUCT_TYPE.MBS.toString());
        mbsTransactionRequest.setProductId(prodId);
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setTradeAmount(new BigDecimal(9000000));
        mbsTransactionRequest.setTradeBuySellType("BUY");
        mbsTransactionRequest.setTradeCouponRate(new BigDecimal(5.5));
        mbsTransactionRequest.setTradeSettlementDate(new Date());
        mbsTransactionRequest.setTransReqNumber("10001");
        mbsTransactionRequest.setPricePercent(new BigDecimal(104.000000000).setScale(9).round(MathContext.DECIMAL64));
        mbsTransactionRequest.setLastUpdated(MBSPortalUtils.getCurrentDate());
        mbsTransactionRequest.setDealerOrgName("Movement");
        mbsTransactionRequest.setLenderShortName("TEST-C");
        mbsTransactionRequest.setLenderSellerServiceNumber("121212");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE,
                createStreamPricingData());
        transactionRequestPOTransformer.transform(transformationObject);
        TransactionRequestPO transactionRequestPOExp = (TransactionRequestPO) transformationObject.getSourcePojo();
        assertTrue(transactionRequestPOExp.getStateType().toString().equals(StateType.LENDER_OPEN.toString()));
        assertTrue("TEST-C".equals(transactionRequestPOExp.getLenderEntityShortName()));
        assertTrue("Movement".equals(transactionRequestPOExp.getDealerOrgName()));
        assertTrue("121212".equals(transactionRequestPOExp.getOboLenderSellerServicerNumber()));
        assertTrue("101".equals(transactionRequestPOExp.getStreamPricePercentTicksText()));
        assertTrue("99".equals(transactionRequestPOExp.getStreamPricePercentHandleText()));
    }
    
    /**
     * Purpose: This method tests the call to transformation from
     * TransactionRequestPO to MBSTransaction for successful call
     * 
     * @throws Exception
     */
    @Test
    public void transform_buy() throws Exception {
        mbsTransactionRequest = new MBSTransactionRequest();
        prodId = new ProductId();
        prodId.setIdentifier(new Long(1));
        prodId.setSourceType(PRODUCT_SOURCE_TYPE.PU.toString());
        prodId.setType(PRODUCT_TYPE.MBS.toString());
        mbsTransactionRequest.setProductId(prodId);
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setTradeAmount(new BigDecimal(9000000));
        mbsTransactionRequest.setTradeBuySellType("SELL");
        mbsTransactionRequest.setTradeCouponRate(new BigDecimal(5.5));
        mbsTransactionRequest.setTradeSettlementDate(new Date());
        mbsTransactionRequest.setTransReqNumber("10001");
        mbsTransactionRequest.setPricePercent(new BigDecimal(104.000000000).setScale(9).round(MathContext.DECIMAL64));
        mbsTransactionRequest.setLastUpdated(MBSPortalUtils.getCurrentDate());
        mbsTransactionRequest.setDealerOrgName("Movement");
        mbsTransactionRequest.setLenderShortName("TEST-C");
        mbsTransactionRequest.setLenderSellerServiceNumber("121212");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE,
                createStreamPricingData());
        transactionRequestPOTransformer.transform(transformationObject);
        TransactionRequestPO transactionRequestPOExp = (TransactionRequestPO) transformationObject.getSourcePojo();
        assertTrue(transactionRequestPOExp.getStateType().toString().equals(StateType.LENDER_OPEN.toString()));
        assertTrue("TEST-C".equals(transactionRequestPOExp.getLenderEntityShortName()));
        assertTrue("Movement".equals(transactionRequestPOExp.getDealerOrgName()));
        assertTrue("121212".equals(transactionRequestPOExp.getOboLenderSellerServicerNumber()));
        assertTrue("101".equals(transactionRequestPOExp.getStreamPricePercentTicksText()));
        assertTrue("101".equals(transactionRequestPOExp.getStreamPricePercentHandleText()));
    }
    
    /**
     * Purpose: This method tests the call to transformation from
     * TransactionRequestPO to MBSTransaction for successful call
     * 
     * @throws Exception
     */
    @Test
    public void transform_streamprice_from_transaction() throws Exception {
        //Stream price set from transaction request instead of market stream price
        mbsTransactionRequest = new MBSTransactionRequest();
        prodId = new ProductId();
        prodId.setIdentifier(new Long(1));
        prodId.setSourceType(PRODUCT_SOURCE_TYPE.PU.toString());
        prodId.setType(PRODUCT_TYPE.MBS.toString());
        mbsTransactionRequest.setProductId(prodId);
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setTradeAmount(new BigDecimal(9000000));
        mbsTransactionRequest.setTradeBuySellType("SELL");
        mbsTransactionRequest.setTradeCouponRate(new BigDecimal(5.5));
        mbsTransactionRequest.setTradeSettlementDate(new Date());
        mbsTransactionRequest.setTransReqNumber("10001");
        mbsTransactionRequest.setPricePercent(new BigDecimal(104.000000000).setScale(9).round(MathContext.DECIMAL64));
        mbsTransactionRequest.setLastUpdated(MBSPortalUtils.getCurrentDate());
        mbsTransactionRequest.setDealerOrgName("Movement");
        mbsTransactionRequest.setLenderShortName("TEST-C");
        mbsTransactionRequest.setLenderSellerServiceNumber("121212");
        mbsTransactionRequest.setStreamPricePercentTicksText("102-102");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestPOTransformer.transform(transformationObject);
        TransactionRequestPO transactionRequestPOExp = (TransactionRequestPO) transformationObject.getSourcePojo();
        assertTrue(transactionRequestPOExp.getStateType().toString().equals(StateType.LENDER_OPEN.toString()));
        assertTrue("TEST-C".equals(transactionRequestPOExp.getLenderEntityShortName()));
        assertTrue("Movement".equals(transactionRequestPOExp.getDealerOrgName()));
        assertTrue("121212".equals(transactionRequestPOExp.getOboLenderSellerServicerNumber()));
        assertTrue("102".equals(transactionRequestPOExp.getStreamPricePercentTicksText()));
        assertTrue("102".equals(transactionRequestPOExp.getStreamPricePercentHandleText()));
    }

    /**
     * Purpose: This method tests the call to transformation from
     * TransactionRequestPO to MBSTransaction for invalid amount
     * 
     * @throws Exception
     */
    @Test(expected = MBSBaseException.class)
    public void transformAmountNoDate() throws Exception {
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        prodId = new ProductId();
        prodId.setIdentifier(new Long(1));
        prodId.setSourceType(PRODUCT_SOURCE_TYPE.PU.toString());
        prodId.setType(PRODUCT_TYPE.MBS.toString());
        mbsTransactionRequest.setProductId(prodId);

        mbsTransactionRequest.setStateType("PRICED");
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setTradeAmount(new BigDecimal(9000000));
        mbsTransactionRequest.setTradeBuySellType("SELL");
        mbsTransactionRequest.setTradeCouponRate(new BigDecimal(5.5));
        mbsTransactionRequest.setTradeSettlementDate(null);
        mbsTransactionRequest.setTransReqNumber("10001");
        mbsTransactionRequest.setPricePercent(new BigDecimal(104.000000000).setScale(9).round(MathContext.DECIMAL64));
        mbsTransactionRequest.setLastUpdated(MBSPortalUtils.getCurrentDate());
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestPOTransformer.transform(transformationObject);
        TransactionRequestPO transactionRequestPOExp = (TransactionRequestPO) transformationObject.getSourcePojo();
        assertTrue(transactionRequestPOExp.getStateType().toString().equals(StateType.LENDER_OPEN.toString()));
    }

    /**
     * Purpose: This method tests the call to transformation from
     * TransactionRequestPO to MBSTransaction for invalid amount
     * 
     * @throws Exception
     */
    @Test
    public void TransactionRequestPricePercentTicksNotNull() throws Exception {
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        prodId = new ProductId();
        prodId.setIdentifier(new Long(1));
        prodId.setSourceType(PRODUCT_SOURCE_TYPE.PU.toString());
        prodId.setType(PRODUCT_TYPE.MBS.toString());
        mbsTransactionRequest.setProductId(prodId);

        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setTradeSettlementDate(new Date());
        mbsTransactionRequest.setTradeAmount(new BigDecimal(9000000));
        mbsTransactionRequest.setTradeBuySellType("SELL");
        mbsTransactionRequest.setTradeTraderIdentifierText("12131");
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        mbsTransactionRequest.setTradeCouponRate(new BigDecimal(5.5));

        mbsTransactionRequest.setTransReqNumber("10001");
        mbsTransactionRequest.setPricePercentTicksText("101-102");
        mbsTransactionRequest.setPricePercent(new BigDecimal(104.000000000).setScale(9).round(MathContext.DECIMAL64));
        mbsTransactionRequest.setLastUpdated(MBSPortalUtils.getCurrentDate());
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestPOTransformer.transform(transformationObject);
        TransactionRequestPO transactionRequestPOExp = (TransactionRequestPO) transformationObject.getSourcePojo();
        assertTrue(transactionRequestPOExp.getPricePercentTicksText().toString().equals("101"));
        assertTrue(transactionRequestPOExp.getPricePercentHandleText().toString().equals("102"));
    }

    /**
     * Purpose: This method tests the call to transformation from
     * TransactionRequestPO to MBSTransaction for invalid amount
     * 
     * @throws Exception
     */
    @Test
    public void transformRoleTypeLENDERAndBuy() throws Exception {
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        prodId = new ProductId();
        prodId.setIdentifier(new Long(1));
        prodId.setSourceType(PRODUCT_SOURCE_TYPE.PU.toString());
        prodId.setType(PRODUCT_TYPE.MBS.toString());
        mbsTransactionRequest.setProductId(prodId);

        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setTradeSettlementDate(new Date());
        mbsTransactionRequest.setTradeAmount(new BigDecimal(9000000));
        mbsTransactionRequest.setTradeBuySellType(TradeBuySell.BUY.toString());// set
        mbsTransactionRequest.setCounterPartyBuySellType(TradeBuySell.BUY.toString());
        // buy
        mbsTransactionRequest.setTradeTraderIdentifierText("12131");
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        mbsTransactionRequest.setTradeCouponRate(new BigDecimal(5.5));

        mbsTransactionRequest.setTransReqNumber("10001");
        mbsTransactionRequest.setPricePercentTicksText("101-102");
        mbsTransactionRequest.setPricePercent(new BigDecimal(104.000000000).setScale(9).round(MathContext.DECIMAL64));
        mbsTransactionRequest.setLastUpdated(MBSPortalUtils.getCurrentDate());
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);// role
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestPOTransformer.transform(transformationObject);
        TransactionRequestPO transactionRequestPOExp = (TransactionRequestPO) transformationObject.getSourcePojo();
        assertTrue(transactionRequestPOExp.getTradeBuySellType().toString().equals(TradeBuySell.BUY.toString()));
    }

    @Test
    public void transformRoleTypeLENDERAndSell() throws Exception {
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        prodId = new ProductId();
        prodId.setIdentifier(new Long(1));
        prodId.setSourceType(PRODUCT_SOURCE_TYPE.PU.toString());
        prodId.setType(PRODUCT_TYPE.MBS.toString());
        mbsTransactionRequest.setProductId(prodId);

        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setTradeSettlementDate(new Date());
        mbsTransactionRequest.setTradeAmount(new BigDecimal(9000000));
        mbsTransactionRequest.setTradeBuySellType(TradeBuySell.SELL.toString());// set
        mbsTransactionRequest.setCounterPartyBuySellType(TradeBuySell.SELL.toString()); // sell
        mbsTransactionRequest.setTradeTraderIdentifierText("12131");
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        mbsTransactionRequest.setTradeCouponRate(new BigDecimal(5.5));

        mbsTransactionRequest.setTransReqNumber("10001");
        mbsTransactionRequest.setPricePercentTicksText("101-102");
        mbsTransactionRequest.setPricePercent(new BigDecimal(104.000000000).setScale(9).round(MathContext.DECIMAL64));
        mbsTransactionRequest.setLastUpdated(MBSPortalUtils.getCurrentDate());
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);// role
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestPOTransformer.transform(transformationObject);
        TransactionRequestPO transactionRequestPOExp = (TransactionRequestPO) transformationObject.getSourcePojo();
        assertTrue(transactionRequestPOExp.getTradeBuySellType().toString().equals(TradeBuySell.SELL.toString()));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void transformRoleTypeTRADERAndBuy() throws Exception {
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        prodId = new ProductId();
        prodId.setIdentifier(new Long(1));
        prodId.setSourceType(PRODUCT_SOURCE_TYPE.PU.toString());
        prodId.setType(PRODUCT_TYPE.MBS.toString());
        mbsTransactionRequest.setProductId(prodId);

        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setTradeSettlementDate(new Date());
        mbsTransactionRequest.setTradeAmount(new BigDecimal(9000000));
        mbsTransactionRequest.setTradeBuySellType(TradeBuySell.BUY.toString());// set
        mbsTransactionRequest.setCounterPartyBuySellType(TradeBuySell.SELL.toString());
        // buy
        mbsTransactionRequest.setTradeTraderIdentifierText("12131");
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        mbsTransactionRequest.setTradeCouponRate(new BigDecimal(5.5));

        mbsTransactionRequest.setTransReqNumber("10001");
        mbsTransactionRequest.setPricePercentTicksText("101-102");
        mbsTransactionRequest.setPricePercent(new BigDecimal(104.000000000).setScale(9).round(MathContext.DECIMAL64));
        mbsTransactionRequest.setLastUpdated(MBSPortalUtils.getCurrentDate());
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setMBSRoleType(MBSRoleType.TRADER);// role
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestPOTransformer.transform(transformationObject);
        TransactionRequestPO transactionRequestPOExp = (TransactionRequestPO) transformationObject.getSourcePojo();
        assertTrue(transactionRequestPOExp.getTradeBuySellType().toString().equals(TradeBuySell.BID.toString()));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void transformRoleTypeTRADERAndSell() throws Exception {
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        prodId = new ProductId();
        prodId.setIdentifier(new Long(1));
        prodId.setSourceType(PRODUCT_SOURCE_TYPE.PU.toString());
        prodId.setType(PRODUCT_TYPE.MBS.toString());
        mbsTransactionRequest.setProductId(prodId);

        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setTradeSettlementDate(new Date());
        mbsTransactionRequest.setTradeAmount(new BigDecimal(9000000));
        mbsTransactionRequest.setTradeBuySellType(TradeBuySell.SELL.toString());// set
                                                                                // sell
        mbsTransactionRequest.setTradeTraderIdentifierText("12131");
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        mbsTransactionRequest.setTradeCouponRate(new BigDecimal(5.5));

        mbsTransactionRequest.setTransReqNumber("10001");
        mbsTransactionRequest.setPricePercentTicksText("101-102");
        mbsTransactionRequest.setPricePercent(new BigDecimal(104.000000000).setScale(9).round(MathContext.DECIMAL64));
        mbsTransactionRequest.setLastUpdated(MBSPortalUtils.getCurrentDate());
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setMBSRoleType(MBSRoleType.TRADER);// role
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestPOTransformer.transform(transformationObject);
        TransactionRequestPO transactionRequestPOExp = (TransactionRequestPO) transformationObject.getSourcePojo();
        assertTrue(transactionRequestPOExp.getTradeBuySellType().toString().equals(TradeBuySell.OFFER.toString()));
    }
    
    @Test
    public void transform_Validate_Trader_Priced_Tick_Eights_Numeric_Success() throws Exception {
        
        mbsTransactionRequest = createMbsTransactionReq("testLender", 1L, StateType.TRADER_PRICED, TradeBuySell.BUY, "10001", "104-101");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setMBSRoleType(MBSRoleType.TRADER);// role
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestPOTransformer.transform(transformationObject);
        TransactionRequestPO transactionRequestPOExp = (TransactionRequestPO) transformationObject.getSourcePojo();
        assertEquals("10+", transactionRequestPOExp.getPricePercentTicksText());
    }
    
    @Test
    public void transform_Validate_Trader_Priced_Tick_Eights_Symbolic_Success() throws Exception {
        
        mbsTransactionRequest = createMbsTransactionReq("testLender", 1L, StateType.TRADER_PRICED, TradeBuySell.BUY, "10001", "10+-101");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setMBSRoleType(MBSRoleType.TRADER);// role
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestPOTransformer.transform(transformationObject);
        TransactionRequestPO transactionRequestPOExp = (TransactionRequestPO) transformationObject.getSourcePojo();
        assertEquals("10+", transactionRequestPOExp.getPricePercentTicksText());
    }
    
    
    @Test
    public void transform_Validate_Trader_Priced_Tick_No_Eights_Success() throws Exception {
        
        mbsTransactionRequest = createMbsTransactionReq("testLender", 1L, StateType.TRADER_PRICED, TradeBuySell.BUY, "10001", "10-101");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setMBSRoleType(MBSRoleType.TRADER);// role
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestPOTransformer.transform(transformationObject);
        TransactionRequestPO transactionRequestPOExp = (TransactionRequestPO) transformationObject.getSourcePojo();
        assertEquals("10", transactionRequestPOExp.getPricePercentTicksText());
    }
    
    @Test
    public void transform_Validate_Trader_Priced_Tick_Numeric_Eights_Failure() throws Exception {
        mbsTransactionRequest = createMbsTransactionReq("testLender", 1L, StateType.TRADER_PRICED, TradeBuySell.BUY, "10001", "104-101");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setMBSRoleType(MBSRoleType.TRADER);// role
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestPOTransformer.transform(transformationObject);
        TransactionRequestPO transactionRequestPOExp = (TransactionRequestPO) transformationObject.getSourcePojo();
        assertNotEquals("104", transactionRequestPOExp.getPricePercentTicksText());
    }
    
    
    @Test
    public void transform_Trader_Name_Success() throws Exception {
        mbsTransactionRequest = createMbsTransactionReq("testLender", 1L, StateType.TRADER_PRICED, TradeBuySell.BUY, "10001", "104-101");
        mbsTransactionRequest.setTraderName("FN"+MBSPServiceConstants.FIRSTNAME_LASTNAME_SEPERATOR+"LN");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setMBSRoleType(MBSRoleType.TRADER);// role
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestPOTransformer.transform(transformationObject);
        TransactionRequestPO transactionRequestPOExp = (TransactionRequestPO) transformationObject.getSourcePojo();
        assertEquals("LN"+MBSPServiceConstants.FIRSTNAME_LASTNAME_SEPERATOR+"FN", transactionRequestPOExp.getTraderName());
    }
    
    @Test
    public void transform_Trader_Name_Only_FN_Success() throws Exception {
        mbsTransactionRequest = createMbsTransactionReq("testLender", 1L, StateType.TRADER_PRICED, TradeBuySell.BUY, "10001", "104-101");
        mbsTransactionRequest.setTraderName("FN");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setMBSRoleType(MBSRoleType.TRADER);// role
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestPOTransformer.transform(transformationObject);
        TransactionRequestPO transactionRequestPOExp = (TransactionRequestPO) transformationObject.getSourcePojo();
        assertEquals("FN", transactionRequestPOExp.getTraderName());
    }
    
    @Test
    public void transform_No_Trader_Name_Success() throws Exception {
        mbsTransactionRequest = createMbsTransactionReq("testLender", 1L, StateType.TRADER_PRICED, TradeBuySell.BUY, "10001", "104-101");
        mbsTransactionRequest.setTraderName(null);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setMBSRoleType(MBSRoleType.TRADER);// role
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestPOTransformer.transform(transformationObject);
        TransactionRequestPO transactionRequestPOExp = (TransactionRequestPO) transformationObject.getSourcePojo();
        assertNull(transactionRequestPOExp.getTraderName());
    }


    @SuppressWarnings("unused")
    @Test
    @Ignore
    public void transformForCoupon() throws MBSBaseException {
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        prodId = new ProductId();
        prodId.setIdentifier(new Long(1));
        prodId.setSourceType(PRODUCT_SOURCE_TYPE.PU.toString());
        prodId.setType(PRODUCT_TYPE.MBS.toString());
        mbsTransactionRequest.setProductId(prodId);

        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setTradeSettlementDate(new Date());
        mbsTransactionRequest.setTradeAmount(new BigDecimal(9000000));
        mbsTransactionRequest.setTradeBuySellType("SELL");
        mbsTransactionRequest.setTradeTraderIdentifierText("12131");
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        mbsTransactionRequest.setTradeCouponRate(new BigDecimal(5.5));

        mbsTransactionRequest.setTransReqNumber("10001");
        mbsTransactionRequest.setPricePercentTicksText("101-102");
        mbsTransactionRequest.setPricePercent(new BigDecimal(104.000000000).setScale(9).round(MathContext.DECIMAL64));
        mbsTransactionRequest.setLastUpdated(MBSPortalUtils.getCurrentDate());
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestPOTransformer.transform(transformationObject);
        TransactionRequestPO transactionRequestPOExp = (TransactionRequestPO) transformationObject.getSourcePojo();

        assertNotNull(transactionRequestPO);
        LOGGER.debug("5.500");
        assertThat(transactionRequestPO.getTradeCouponRate(), is("5.500"));
    }
    
    
    /**
     * 
     * 
     * @param testLender
     * @param productId
     * @param stateType
     * @param tradeBuySell
     * @param transId
     * @param pricePercentTicksText
     * @return
     */
    private MBSTransactionRequest createMbsTransactionReq(String testLender, Long productId, StateType stateType, TradeBuySell tradeBuySell, String transId, String pricePercentTicksText) {
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setCounterpartyTraderIdentifier(testLender);
        prodId = new ProductId();
        prodId.setIdentifier(new Long(productId));
        prodId.setSourceType(PRODUCT_SOURCE_TYPE.PU.toString());
        prodId.setType(PRODUCT_TYPE.MBS.toString());
        mbsTransactionRequest.setProductId(prodId);

        mbsTransactionRequest.setStateType(stateType.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setTradeSettlementDate(new Date());
        mbsTransactionRequest.setTradeAmount(new BigDecimal(9000000));
        mbsTransactionRequest.setTradeBuySellType(tradeBuySell.toString());
        // buy
        mbsTransactionRequest.setTradeTraderIdentifierText("12131");
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        mbsTransactionRequest.setTradeCouponRate(new BigDecimal(5.5));

        mbsTransactionRequest.setTransReqNumber(transId);
        mbsTransactionRequest.setPricePercentTicksText(pricePercentTicksText);
        mbsTransactionRequest.setPricePercent(new BigDecimal(104.000000000).setScale(9).round(MathContext.DECIMAL64));
        mbsTransactionRequest.setLastUpdated(MBSPortalUtils.getCurrentDate());
        return mbsTransactionRequest;
    }
    
    public MBSMarketIndicativePrice createStreamPricingData(){
        mbsMarketIndicativePrice = new MBSMarketIndicativePrice();
        mbsMarketIndicativePrice.setAskPricePercent(new BigDecimal("101.25"));
        mbsMarketIndicativePrice.setAskPriceText("101-101");
        mbsMarketIndicativePrice.setBidPricePercent(new BigDecimal("99.25"));
        mbsMarketIndicativePrice.setBidPriceText("99-101");
        return mbsMarketIndicativePrice;
        
    }

}
