/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */


package com.fanniemae.mbsportal.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.po.ProductPricingPO;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.api.validator.TransactionRequestValidator;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.service.MBSExceptionService;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessWarning;

/**
 * Created by g8uaxt on 8/23/2017.
 * @author g8upjv
 */
/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 16, 2018
 * @Time 2:28:42 PM com.fanniemae.mbsportal.api.validator
 *       TransactionRequestValidatorTest.java
 * @Description: Update Existing Test Cases for - CMMBSSTA01-1022: (Tech)
 *               Maintain versions for transaction request
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionRequestValidatorTest {
    
    @InjectMocks
    TransactionRequestValidator transactionRequestValidator;
    
    private TransactionRequestPO transactionRequestPO;
    private MBSTransactionRequest mbsTransactionRequest;
    ProductPO prodPO;
    ProductIdPO productId;
    
    @Mock
    MbspProperties mbspProperties;
    
//    @Mock
//    MBSExceptionService mbsExceptionService;

    @Test
    public void validateForLenderRejected() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        prodPO = new ProductPO();
        productId = new ProductIdPO();
        productId.setIdentifier(new Long(1));
        productId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
        productId.setType(PRODUCT_TYPE.MBS);
        prodPO.setProductId(productId);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount("100000");
        transactionRequestPO.setTradeBuySellType("SELL");
        transactionRequestPO.setTradeCouponRate("2.0");
        transactionRequestPO.setTradeSettlementDate("2001-07-04T12:08:56.235-0700");
        transactionRequestPO.setSubmissionDate("2001-07-04T12:08:56.235-0700");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.LENDER_REJECTED);
        transactionRequestPO.setPricePercentHandleText("105");
        transactionRequestPO.setPricePercentTicksText("193");
        transactionRequestPO.setActiveVersion(3L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setTradeAmount(new BigDecimal(9000000));
        mbsTransactionRequest.setTradeBuySellType("SELL");
        mbsTransactionRequest.setTradeCouponRate(new BigDecimal(5.5));
        mbsTransactionRequest.setTradeSettlementDate(new Date());
        mbsTransactionRequest.setTransReqNumber("10001");
        mbsTransactionRequest.setPricePercent(new BigDecimal(104.000000000).setScale(9).round(MathContext.DECIMAL64));
        mbsTransactionRequest.setActiveVersion(3L);
        mbsTransactionRequest.setLenderShortName("TEST-C");
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertEquals(StateType.LENDER_REJECTED, StateType.getEnum(mbsTransactionExpected.getStateType()));

    }

    @Test
    public void validateForLenderOpenTrader() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setActiveVersion(1L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        //
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(1L);
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transformationObject.setMBSRoleType(MBSRoleType.TRADER);
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        // assert status
        assertEquals(StateType.LENDER_OPEN, StateType.getEnum(mbsTransactionExpected.getStateType()));
    }
    
    @Test
    public void validateForLenderOpenLender() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setActiveVersion(1L);
        transactionRequestPO.setTradeSettlementDate("2020-09-21");
        transactionRequestPO.setTradeCouponRate("4.0");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        //
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(1L);
        mbsTransactionRequest.setTradeCouponRate(new BigDecimal(4.0));
        Date tradeSettleDate = MBSPortalUtils.convertToDateWithFormatter("2020-09-21", DateFormats.DATE_FORMAT_NO_TIMESTAMP);
        mbsTransactionRequest.setTradeSettlementDate(tradeSettleDate);
        mbsTransactionRequest.setLenderShortName("TEST-C");
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("SELL");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setCutOffDate("2020-09-19");
        productPricingPO.setPassThroughRate("4.00");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        
        when(mbspProperties.getCutOffHour()).thenReturn("14");
        when(mbspProperties.getCutOffMinute()).thenReturn("00");
        when(mbspProperties.getCutOffSecond()).thenReturn("00");
        when(mbspProperties.getCutOffMillisecond()).thenReturn("00");
        
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        // assert status
        assertEquals(mbsTransactionExpected.getTradeSettlementDate(), mbsTransactionRequest.getTradeSettlementDate());
    }
    
    @Test(expected = MBSBaseException.class)
    public void validateForLenderOpenLender_EmptyPartyShortName() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setActiveVersion(1L);
        transactionRequestPO.setTradeSettlementDate("2020-09-21");
        transactionRequestPO.setTradeCouponRate("4.0");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        //
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(1L);
        mbsTransactionRequest.setTradeCouponRate(new BigDecimal(4.0));
        Date tradeSettleDate = MBSPortalUtils.convertToDateWithFormatter("2020-09-21", DateFormats.DATE_FORMAT_NO_TIMESTAMP);
        mbsTransactionRequest.setTradeSettlementDate(tradeSettleDate);
        
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("SELL");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setCutOffDate("2020-09-19");
        productPricingPO.setPassThroughRate("4.00");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        
        when(mbspProperties.getCutOffHour()).thenReturn("14");
        when(mbspProperties.getCutOffMinute()).thenReturn("00");
        when(mbspProperties.getCutOffSecond()).thenReturn("00");
        when(mbspProperties.getCutOffMillisecond()).thenReturn("00");
        
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        // assert status
        assertEquals(mbsTransactionExpected.getTradeSettlementDate(), mbsTransactionRequest.getTradeSettlementDate());
    }
    
    @Test
    public void validateForLenderOpenTSP() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setActiveVersion(1L);
        transactionRequestPO.setTradeSettlementDate("2020-09-21");
        transactionRequestPO.setTradeCouponRate("4.0");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        //
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(1L);
        mbsTransactionRequest.setTradeCouponRate(new BigDecimal(4.0));
        mbsTransactionRequest.setLenderShortName("TEST-C");
        Date tradeSettleDate = MBSPortalUtils.convertToDateWithFormatter("2020-09-21", DateFormats.DATE_FORMAT_NO_TIMESTAMP);
        mbsTransactionRequest.setTradeSettlementDate(tradeSettleDate);
        mbsTransactionRequest.setLenderShortName("TEST-C");
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("SELL");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setCutOffDate("2020-09-19");
        productPricingPO.setPassThroughRate("4.00");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transformationObject.setMBSRoleType(MBSRoleType.TSP);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        
        when(mbspProperties.getCutOffHour()).thenReturn("14");
        when(mbspProperties.getCutOffMinute()).thenReturn("00");
        when(mbspProperties.getCutOffSecond()).thenReturn("00");
        when(mbspProperties.getCutOffMillisecond()).thenReturn("00");
        
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        // assert status
        assertEquals(mbsTransactionExpected.getTradeSettlementDate(), mbsTransactionRequest.getTradeSettlementDate());
    }
    
    @Test(expected = MBSBaseException.class)
    public void validateForLenderOpenTSP_EmptyPartyShortName() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setActiveVersion(1L);
        transactionRequestPO.setTradeSettlementDate("2020-09-21");
        transactionRequestPO.setTradeCouponRate("4.0");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        //
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(1L);
        mbsTransactionRequest.setTradeCouponRate(new BigDecimal(4.0));
        Date tradeSettleDate = MBSPortalUtils.convertToDateWithFormatter("2020-09-21", DateFormats.DATE_FORMAT_NO_TIMESTAMP);
        mbsTransactionRequest.setTradeSettlementDate(tradeSettleDate);
        
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("SELL");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setCutOffDate("2020-09-19");
        productPricingPO.setPassThroughRate("4.00");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transformationObject.setMBSRoleType(MBSRoleType.TSP);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        
        when(mbspProperties.getCutOffHour()).thenReturn("14");
        when(mbspProperties.getCutOffMinute()).thenReturn("00");
        when(mbspProperties.getCutOffSecond()).thenReturn("00");
        when(mbspProperties.getCutOffMillisecond()).thenReturn("00");
        
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        // assert status
        assertEquals(mbsTransactionExpected.getTradeSettlementDate(), mbsTransactionRequest.getTradeSettlementDate());
    }
    
    @Test(expected = MBSBaseException.class)
    public void validateForLenderOpenLenderCouponException() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setActiveVersion(1L);
        transactionRequestPO.setTradeSettlementDate("2020-09-21");
        transactionRequestPO.setTradeCouponRate("3.0");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        //
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(1L);
        mbsTransactionRequest.setTradeCouponRate(new BigDecimal(3.0));
        Date tradeSettleDate = MBSPortalUtils.convertToDateWithFormatter("2020-09-21", DateFormats.DATE_FORMAT_NO_TIMESTAMP);
        mbsTransactionRequest.setTradeSettlementDate(tradeSettleDate);
        
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("SELL");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setCutOffDate("2020-09-19");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        
        when(mbspProperties.getCutOffHour()).thenReturn("14");
        when(mbspProperties.getCutOffMinute()).thenReturn("00");
        when(mbspProperties.getCutOffSecond()).thenReturn("00");
        when(mbspProperties.getCutOffMillisecond()).thenReturn("00");
        
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        // assert status
        assertEquals(mbsTransactionExpected.getTradeSettlementDate(), mbsTransactionRequest.getTradeSettlementDate());
    }
    
    @Test(expected = MBSBaseException.class)
    public void validateForLenderOpenLenderInvalidSettlementDate() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setActiveVersion(1L);
        transactionRequestPO.setTradeSettlementDate("2020-09-21");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        //
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(1L);
        Date tradeSettleDate = MBSPortalUtils.convertToDateWithFormatter("2020-10-21", DateFormats.DATE_FORMAT_NO_TIMESTAMP);
        mbsTransactionRequest.setTradeSettlementDate(tradeSettleDate);
        
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("SELL");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setCutOffDate("2020-09-19");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        
        when(mbspProperties.getCutOffHour()).thenReturn("14");
        when(mbspProperties.getCutOffMinute()).thenReturn("00");
        when(mbspProperties.getCutOffSecond()).thenReturn("00");
        when(mbspProperties.getCutOffMillisecond()).thenReturn("00");
        
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        // assert status
        assertEquals(mbsTransactionExpected.getTradeSettlementDate(), mbsTransactionRequest.getTradeSettlementDate());
    }
    
    @Test(expected = MBSBaseException.class)
    public void validateForLenderOpenLenderCutOffDateException() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setActiveVersion(1L);
        transactionRequestPO.setTradeSettlementDate("2020-09-21");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        //
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(1L);
        Date tradeSettleDate = MBSPortalUtils.convertToDateWithFormatter("2020-09-21", DateFormats.DATE_FORMAT_NO_TIMESTAMP);
        mbsTransactionRequest.setTradeSettlementDate(tradeSettleDate);
        
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("SELL");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setCutOffDate("");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        
        when(mbspProperties.getCutOffHour()).thenReturn("14");
        when(mbspProperties.getCutOffMinute()).thenReturn("00");
        when(mbspProperties.getCutOffSecond()).thenReturn("00");
        when(mbspProperties.getCutOffMillisecond()).thenReturn("00");
        
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        // assert status
        assertEquals(mbsTransactionExpected.getTradeSettlementDate(), mbsTransactionRequest.getTradeSettlementDate());
    }
    
    @Test(expected = MBSBaseException.class)
    public void validateForLenderOpenLenderPastCutoffDate() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setActiveVersion(1L);
        transactionRequestPO.setTradeSettlementDate("2020-09-21");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        //
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(1L);
        Date tradeSettleDate = MBSPortalUtils.convertToDateWithFormatter("2020-09-21", DateFormats.DATE_FORMAT_NO_TIMESTAMP);
        mbsTransactionRequest.setTradeSettlementDate(tradeSettleDate);
        
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("SELL");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setCutOffDate("2017-09-19");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        
        when(mbspProperties.getCutOffHour()).thenReturn("14");
        when(mbspProperties.getCutOffMinute()).thenReturn("00");
        when(mbspProperties.getCutOffSecond()).thenReturn("00");
        when(mbspProperties.getCutOffMillisecond()).thenReturn("00");
        
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        // assert status
        assertEquals(mbsTransactionExpected.getTradeSettlementDate(), mbsTransactionRequest.getTradeSettlementDate());
    }
    
    @Test(expected = MBSBaseException.class)
    public void validateForLenderOpenLenderCutOffDate() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setActiveVersion(1L);
        transactionRequestPO.setTradeSettlementDate("2020-09-21");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        //
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(1L);
        Date tradeSettleDate = MBSPortalUtils.convertToDateWithFormatter("2020-09-21", DateFormats.DATE_FORMAT_NO_TIMESTAMP);
        mbsTransactionRequest.setTradeSettlementDate(tradeSettleDate);
        
        
        Date currDate = MBSPortalUtils.getCurrentDate();
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("SELL");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setCutOffDate(MBSPortalUtils.convertDateToString(currDate, DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        
        when(mbspProperties.getCutOffHour()).thenReturn("00");
        when(mbspProperties.getCutOffMinute()).thenReturn("00");
        when(mbspProperties.getCutOffSecond()).thenReturn("00");
        when(mbspProperties.getCutOffMillisecond()).thenReturn("001");
        
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        // assert status
        assertEquals(mbsTransactionExpected.getTradeSettlementDate(), mbsTransactionRequest.getTradeSettlementDate());
    }
    
    @Test(expected = MBSBaseException.class)
    public void validateForLenderOpenLenderException() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setActiveVersion(1L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setTargetPojo(null);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        // assert status
        assertEquals(StateType.LENDER_OPEN, StateType.getEnum(mbsTransactionExpected.getStateType()));
    }

    @Test(expected = MBSBaseException.class)
    public void validateForLenderOpen() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.TRADER_CONFIRMED);
        transactionRequestPO.setPricePercentHandleText("105");
        transactionRequestPO.setPricePercentTicksText("193");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        //
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        // assert statsus
        assertEquals(StateType.TRADER_PRICED, StateType.getEnum(mbsTransactionExpected.getStateType()));
    }

    @Test
    public void validateForTraderPassed() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.TRADER_PASSED);
        transactionRequestPO.setActiveVersion(4L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        //
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(4L);
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        // assert statsus
        assertEquals(StateType.TRADER_PASSED, StateType.getEnum(mbsTransactionExpected.getStateType()));
    }

    /**
     * Throws exception when invalid status is passed
     * 
     * @throws MBSBaseException
     */
    @Test(expected = MBSBaseException.class)
    public void validateForTraderRejectedException() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.TRADER_REJECTED);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        //
        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        // assert statsus
        assertEquals(StateType.TRADER_PASSED, StateType.getEnum(mbsTransactionExpected.getStateType()));
    }

    @Test(expected = MBSBaseException.class)
    public void validateForLenderRequestException() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.LENDER_ACCEPTED);
        transactionRequestPO.setPricePercentHandleText("105");
        transactionRequestPO.setPricePercentTicksText("193");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setTargetPojo(null);
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        // assert statsus
        assertNull("state type not matched", mbsTransactionExpected.getStateType());
    }
    
    @Test
    public void validate_Version_Should_Match_For_TraderPassed_Success() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.TRADER_PASSED);
        transactionRequestPO.setActiveVersion(3L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);

        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(3L);
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertEquals(StateType.TRADER_PASSED, StateType.getEnum(mbsTransactionExpected.getStateType()));
    }
    
    @Test
    public void validate_Version_Should_Match_For_TraderPriced_Success() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.TRADER_PRICED);
        transactionRequestPO.setActiveVersion(3L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);

        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(3L);
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertEquals(StateType.TRADER_PRICED, StateType.getEnum(mbsTransactionExpected.getStateType()));
    }
    
    @Test
    public void validate_Version_Lagging_For_Trader_RePriced_Success() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.TRADER_REPRICED);
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);

        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.TRADER_PRICED.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(3L);
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertEquals(StateType.TRADER_REPRICED, StateType.getEnum(mbsTransactionExpected.getStateType()));
    }
    
    @Test(expected=MBSBusinessWarning.class)
    public void validate_Version_Aheading_For_TraderPassed_Failure() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.TRADER_PASSED);
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);

        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(3L);
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertEquals(StateType.TRADER_PASSED, StateType.getEnum(mbsTransactionExpected.getStateType()));
    }
    
    @Test(expected=MBSBusinessWarning.class)
    public void validate_Version_Existing_Record_With_No_Version_info_TraderPassed_Failure() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.TRADER_PASSED);
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);

        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertEquals(StateType.TRADER_PASSED, StateType.getEnum(mbsTransactionExpected.getStateType()));
    }
    
    @Test(expected=MBSBusinessException.class)
    public void validate_Not_Eligible_State_Failure() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.TRADER_CONFIRMED);
        transactionRequestPO.setActiveVersion(3L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);

        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(3L);
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertEquals(StateType.TRADER_PASSED, StateType.getEnum(mbsTransactionExpected.getStateType()));
    }
    
    /**
     * 
     * Lender/ TSP should be able to cancel if Trader did not take any action on it
     * 
     * @throws MBSBaseException
     */
    @Test
    public void validate_Lender_Cancel_For_Trader_No_Action_On_Trade() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.LENDER_REJECTED);
        transactionRequestPO.setActiveVersion(1L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);

        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(1L);
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertEquals(StateType.LENDER_REJECTED, StateType.getEnum(mbsTransactionExpected.getStateType()));
    }
    
    
    /**
     * 
     * Lender should be able to cancel the trader if Trader Priced it
     * 
     * @throws MBSBaseException
     */
    @Test
    public void validate_Lender_Cancel_For_Trader_Priced() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.LENDER_REJECTED);
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);

        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.TRADER_PRICED.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(2L);
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertEquals(StateType.LENDER_REJECTED, StateType.getEnum(mbsTransactionExpected.getStateType()));
    }
    
    /**
     * 
     * Lender should be able to cancel the trade if Trader Re-Priced it 
     * 
     * @throws MBSBaseException
     */
    @Test
    public void validate_Lender_Cancel_For_Trader_Re_Priced() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.LENDER_REJECTED);
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);

        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.TRADER_REPRICED.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(2L);
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertEquals(StateType.LENDER_REJECTED, StateType.getEnum(mbsTransactionExpected.getStateType()));
    }
    
    /**
     * 
     * Lender should be able to cancel the trade even if Lender Accepted it
     * 
     * @throws MBSBaseException
     */
    @Test
    public void validate_Lender_Cancel_For_Lender_Accepted_Quote() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.LENDER_REJECTED);
        transactionRequestPO.setActiveVersion(3L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);

        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.LENDER_ACCEPTED.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(3L);
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestValidator.validate(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertEquals(StateType.LENDER_REJECTED, StateType.getEnum(mbsTransactionExpected.getStateType()));
    }
    
    /**
     * 
     * Lender should not be able to cancel the trade if Trader Confirmed it 
     * 
     * @throws MBSBaseException
     */
    @Test(expected=MBSBusinessException.class)
    public void validate_Lender_Cancel_For_Trader_Confirmed_Trade() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setStateType(StateType.LENDER_REJECTED);
        transactionRequestPO.setActiveVersion(3L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);

        mbsTransactionRequest = new MBSTransactionRequest();
        mbsTransactionRequest.setStateType(StateType.TRADER_CONFIRMED.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setActiveVersion(3L);
        transformationObject.setTargetPojo(mbsTransactionRequest);
        transactionRequestValidator.validate(transformationObject);
    }
    
    
    /**
     * utility method to create stub data
     */
    private ProductIdPO createProductIdPO(Long identifier) {
        ProductIdPO productId = new ProductIdPO();
        productId.setIdentifier(Long.valueOf(identifier));
        productId.setType(TradeConstants.PRODUCT_TYPE.MBS);
        productId.setSourceType(TradeConstants.PRODUCT_SOURCE_TYPE.PU);
        return productId;
    }
}
