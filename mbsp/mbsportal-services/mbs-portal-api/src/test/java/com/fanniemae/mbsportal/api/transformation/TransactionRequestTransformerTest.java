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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.config.TradeServiceProperties;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.po.ProductPricingPO;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * This class handles the test case for the TransactionRequestTransaformer class
 * 
 * date: 07/24/2017
 * 
 * @author g8upjv Initial version 08/02/2017 g8upjv Updated test cases for
 *         invalid date and prod identifier
 * 
 */

public class TransactionRequestTransformerTest extends BaseServiceTest {

    @SuppressWarnings("rawtypes")
    @InjectMocks
    private TransactionRequestTransformer transactionRequestTransformer;

    private TransactionRequestPO transactionRequestPO;

    ProductPO prodPO;

    ProductIdPO productId;
    
    MBSMarketIndicativePrice mbsMarketIndicativePrice;

    @Mock
    MbspProperties mbspProperties;

    @Mock
    TradeServiceProperties tradeServiceProperties;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    /**
     * Purpose: This method tests the call to transformation from
     * TransactionRequestPO to MBSTransaction for successful call
     * 
     * @throws Exception
     */
    @Test
    public void transformLender_LenderOpen() throws Exception {
        createStreamPricingData();
        when(mbspProperties.getCutOffHour()).thenReturn("14");
        when(mbspProperties.getCutOffMinute()).thenReturn("00");
        when(mbspProperties.getCutOffSecond()).thenReturn("00");
        when(mbspProperties.getCutOffMillisecond()).thenReturn("00");
        when(tradeServiceProperties.getSourceSystem()).thenReturn("MBSP");
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
        transactionRequestPO.setTradeSettlementDate("2020-09-11");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("SELL");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertNull("Submission Date is null", mbsTransactionExpected.getSubmissionDate());
        assertNull("AcceptanceDate is not null", mbsTransactionExpected.getAcceptanceDate());
        assertEquals(mbsMarketIndicativePrice.getBidPriceText(),mbsTransactionExpected.getStreamPricePercentTicksText());
    }
    
    /**
     * Purpose: This method tests the call to transformation from
     * TransactionRequestPO to MBSTransaction for successful call
     * 
     * @throws Exception
     */
    @Test
    public void transformLender_LenderOpen_AskStreamPrice() throws Exception {
        createStreamPricingData();
        when(mbspProperties.getCutOffHour()).thenReturn("14");
        when(mbspProperties.getCutOffMinute()).thenReturn("00");
        when(mbspProperties.getCutOffSecond()).thenReturn("00");
        when(mbspProperties.getCutOffMillisecond()).thenReturn("00");
        when(tradeServiceProperties.getSourceSystem()).thenReturn("MBSP");
        transactionRequestPO = new TransactionRequestPO();
        prodPO = new ProductPO();
        productId = new ProductIdPO();
        productId.setIdentifier(new Long(1));
        productId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
        productId.setType(PRODUCT_TYPE.MBS);
        prodPO.setProductId(productId);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount("100000");
        transactionRequestPO.setTradeBuySellType("BUY");
        transactionRequestPO.setTradeCouponRate("2.0");
        transactionRequestPO.setTradeSettlementDate("2020-09-11");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("SELL");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertNull("Submission Date is null", mbsTransactionExpected.getSubmissionDate());
        assertNull("AcceptanceDate is not null", mbsTransactionExpected.getAcceptanceDate());
        assertEquals(mbsMarketIndicativePrice.getAskPriceText(),mbsTransactionExpected.getStreamPricePercentTicksText());
    }
    
    @Test
    public void transformTSP_LenderOpen() throws Exception {
        createStreamPricingData();
        when(mbspProperties.getCutOffHour()).thenReturn("14");
        when(mbspProperties.getCutOffMinute()).thenReturn("00");
        when(mbspProperties.getCutOffSecond()).thenReturn("00");
        when(mbspProperties.getCutOffMillisecond()).thenReturn("00");
        when(tradeServiceProperties.getSourceSystem()).thenReturn("MBSP");
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
        transactionRequestPO.setTradeSettlementDate("2020-09-11");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setOboLenderSellerServicerNumber("121212");
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("SELL");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.TSP);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertNull("Submission Date is null", mbsTransactionExpected.getSubmissionDate());
        assertNull("AcceptanceDate is not null", mbsTransactionExpected.getAcceptanceDate());
        assertEquals(mbsMarketIndicativePrice.getBidPriceText(),mbsTransactionExpected.getStreamPricePercentTicksText());
    }
    
    @Test
    public void transformTSP_LenderOpen_AskStreamPrice() throws Exception {
        createStreamPricingData();
        when(mbspProperties.getCutOffHour()).thenReturn("14");
        when(mbspProperties.getCutOffMinute()).thenReturn("00");
        when(mbspProperties.getCutOffSecond()).thenReturn("00");
        when(mbspProperties.getCutOffMillisecond()).thenReturn("00");
        when(tradeServiceProperties.getSourceSystem()).thenReturn("MBSP");
        transactionRequestPO = new TransactionRequestPO();
        prodPO = new ProductPO();
        productId = new ProductIdPO();
        productId.setIdentifier(new Long(1));
        productId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
        productId.setType(PRODUCT_TYPE.MBS);
        prodPO.setProductId(productId);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount("100000");
        transactionRequestPO.setTradeBuySellType("BUY");
        transactionRequestPO.setTradeCouponRate("2.0");
        transactionRequestPO.setTradeSettlementDate("2020-09-11");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setOboLenderSellerServicerNumber("121212");
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("SELL");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.TSP);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertNull("Submission Date is null", mbsTransactionExpected.getSubmissionDate());
        assertNull("AcceptanceDate is not null", mbsTransactionExpected.getAcceptanceDate());
        assertEquals(mbsMarketIndicativePrice.getAskPriceText(),mbsTransactionExpected.getStreamPricePercentTicksText());
    }
   

    /**
     * Purpose: This method tests the call to transformation from
     * TransactionRequestPO to MBSTransaction for successful call
     * 
     * @throws Exception
     */
    @Test
    public void testTransformTraderLenderOpen() throws Exception {
        createStreamPricingData();
        when(mbspProperties.getCutOffHour()).thenReturn("14");
        when(mbspProperties.getCutOffMinute()).thenReturn("00");
        when(mbspProperties.getCutOffSecond()).thenReturn("00");
        when(mbspProperties.getCutOffMillisecond()).thenReturn("00");
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
        transactionRequestPO.setTradeSettlementDate("2020-09-11");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setTraderId("trader");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("SELL");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setTargetPojo(new MBSTransactionRequest());
        transformationObject.setMBSRoleType(MBSRoleType.TRADER);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertTrue(mbsTransactionExpected.getTradeTraderIdentifierText().equals("trader"));
    }
    
    

    /**
     * Purpose: This method tests the call to transformation from
     * TransactionRequestPO to MBSTransaction for invalid amount
     * 
     * @throws Exception
     */
    @Test(expected = MBSBaseException.class)

    public void testCreateTransformAmountInvalidAmount() throws Exception {
        createStreamPricingData();
        transactionRequestPO = new TransactionRequestPO();
        prodPO = new ProductPO();
        productId = new ProductIdPO();
        productId.setIdentifier(new Long(1));
        productId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
        productId.setType(PRODUCT_TYPE.MBS);
        prodPO.setProductId(productId);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount("ASD");
        transactionRequestPO.setTradeBuySellType("SELL");
        transactionRequestPO.setTradeCouponRate("2.0");
        transactionRequestPO.setTradeSettlementDate("2020-07-04");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("BUY");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertTrue(mbsTransactionExpected.getTradeAmount().intValue() == 0);
    }

    /**
     * Purpose: This method tests the call to transformation from
     * TransactionRequestPO to MBSTransaction for invalid amount
     * 
     * @throws Exception
     */
    @Test(expected = MBSBaseException.class)
    public void testTransformAmountNoAmount() throws Exception {
        createStreamPricingData();
        transactionRequestPO = new TransactionRequestPO();
        prodPO = new ProductPO();
        productId = new ProductIdPO();
        productId.setIdentifier(new Long(1));
        productId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
        productId.setType(PRODUCT_TYPE.MBS);
        prodPO.setProductId(productId);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount("");
        transactionRequestPO.setTradeBuySellType("SELL");
        transactionRequestPO.setTradeCouponRate("2.0");
        transactionRequestPO.setTradeSettlementDate("2020-07-04");
        transactionRequestPO.setSubmissionDate("2001-07-04T12:08:56.235-0700");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("BUY");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertTrue(mbsTransactionExpected.getTradeAmount().intValue() == 0);
    }

    /**
     * Purpose: This method tests the call to transformation from
     * TransactionRequestPO to MBSTransaction
     * 
     * @throws Exception
     */
    @Test
    public void testTransformAmountNoPrdIden() throws Exception {
        createStreamPricingData();
        when(mbspProperties.getCutOffHour()).thenReturn("14");
        when(mbspProperties.getCutOffMinute()).thenReturn("00");
        when(mbspProperties.getCutOffSecond()).thenReturn("00");
        when(mbspProperties.getCutOffMillisecond()).thenReturn("00");
        when(tradeServiceProperties.getSourceSystem()).thenReturn("MBSP");
        transactionRequestPO = new TransactionRequestPO();
        prodPO = new ProductPO();
        productId = new ProductIdPO();
        productId.setIdentifier(new Long(1));
        productId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
        productId.setType(PRODUCT_TYPE.MBS);
        prodPO.setProductId(productId);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount("123456");
        transactionRequestPO.setTradeBuySellType("SELL");
        transactionRequestPO.setTradeCouponRate("2.0");
        transactionRequestPO.setTradeSettlementDate("2020-07-04");
        transactionRequestPO.setSubmissionDate("2001-07-04T12:08:56.235-0700");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("BUY");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertTrue(mbsTransactionExpected.getTradeAmount().intValue() == 123456);
    }

    /**
     * Purpose: This method tests the call to transformation from
     * TransactionRequestPO to MBSTransaction for invalid date
     * 
     * @throws Exception
     */
    @Test
    public void testTransformLenderIdNull() throws Exception {
        createStreamPricingData();
        when(mbspProperties.getCutOffHour()).thenReturn("14");
        when(mbspProperties.getCutOffMinute()).thenReturn("00");
        when(mbspProperties.getCutOffSecond()).thenReturn("00");
        when(mbspProperties.getCutOffMillisecond()).thenReturn("00");
        when(tradeServiceProperties.getSourceSystem()).thenReturn("MBSP");
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
        transactionRequestPO.setTradeSettlementDate("2020-22-04");
        transactionRequestPO.setSubmissionDate("2001-07-04T12:08:56.235-0700");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setLenderId(null);
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("BUY");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertTrue(mbsTransactionExpected.getTradeAmount().intValue() == 100000);
    }

    

    /**
     * Purpose: This method tests the call to transformation from
     * TransactionRequestPO to MBSTransaction for empty date
     * 
     * @throws Exception
     */
    @Test
    public void testCreateMBSTransEmptyDate() throws Exception {
        createStreamPricingData();
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
        transactionRequestPO.setTradeSettlementDate("");
        transactionRequestPO.setSubmissionDate("2001-07-04T12:08:56.235-0700");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        thrown.expect(MBSBaseException.class);
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("BUY");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertTrue(mbsTransactionExpected.getTradeAmount().intValue() == 0);
    }

    /**
     * Purpose: This method tests the call to transformation from
     * TransactionRequestPO to MBSTransaction for empty date
     * 
     * @throws Exception
     */
    @Test(expected = MBSBaseException.class)
    public void testCreateMBSTransNoProdIden() throws Exception {
        createStreamPricingData();
        transactionRequestPO = new TransactionRequestPO();
        prodPO = new ProductPO();
        productId = new ProductIdPO();
        productId.setIdentifier(new Long(1));
        productId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
        productId.setType(PRODUCT_TYPE.MBS);
        prodPO.setProductId(null);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount("100000");
        transactionRequestPO.setTradeBuySellType("SELL");
        transactionRequestPO.setTradeCouponRate("2.0");
        transactionRequestPO.setTradeSettlementDate("2020-07-04");
        transactionRequestPO.setSubmissionDate("2001-07-04T12:08:56.235-0700");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("BUY");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertTrue(mbsTransactionExpected.getTradeAmount().intValue() == 0);
    }

    /**
     * Purpose: This method tests the call to transformation from
     * TransactionRequestPO to MBSTransaction for empty date
     * 
     * @throws Exception
     */
    @Test
    public void testTransTraderConfirmed() throws Exception {
        createStreamPricingData();
        when(mbspProperties.getCutOffHour()).thenReturn("14");
        when(mbspProperties.getCutOffMinute()).thenReturn("00");
        when(mbspProperties.getCutOffSecond()).thenReturn("00");
        when(mbspProperties.getCutOffMillisecond()).thenReturn("00");
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
        transactionRequestPO.setTradeSettlementDate("2020-07-04");
        transactionRequestPO.setSubmissionDate("2001-07-04T12:08:56.235-0700");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.TRADER_CONFIRMED);
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("BUY");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setTargetPojo(new MBSTransactionRequest());
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertNotNull(mbsTransactionExpected);
        assertNotNull(mbsTransactionExpected.getAcceptanceDate());
    }

    /**
     * Purpose: This method tests the call to transformation from
     * TransactionRequestPO to MBSTransaction for trader passed scenario
     * 
     * @throws Exception
     */
    @Test
    public void testTransTraderPassed() throws Exception {
        createStreamPricingData();
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
        transactionRequestPO.setTradeSettlementDate("2020-07-04");
        transactionRequestPO.setSubmissionDate("2001-07-04T12:08:56.235-0700");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.TRADER_PASSED);
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("BUY");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setTargetPojo(new MBSTransactionRequest());
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertNotNull(mbsTransactionExpected);
    }

    /**
     * Purpose: This method tests the call to transformation from
     * TransactionRequestPO to MBSTransaction for trader rejected scenario
     * 
     * @throws Exception
     */
    @Test
    public void testTransTraderRejected() throws Exception {
        createStreamPricingData();
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
        transactionRequestPO.setTradeSettlementDate("2020-07-04");
        transactionRequestPO.setSubmissionDate("2001-07-04T12:08:56.235-0700");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.TRADER_REJECTED);
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("BUY");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setTargetPojo(new MBSTransactionRequest());
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertNotNull(mbsTransactionExpected);
    }

    /**
     * Purpose: This method tests the call to transformation from
     * TransactionRequestPO to MBSTransaction for empty date
     * 
     * @throws Exception
     */
    @Test
    public void testTransLenderAccepted() throws Exception {
        createStreamPricingData();
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
        transactionRequestPO.setTradeSettlementDate("2020-07-04");
        transactionRequestPO.setSubmissionDate("2001-07-04T12:08:56.235-0700");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.LENDER_ACCEPTED);
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("BUY");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setTargetPojo(new MBSTransactionRequest());
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertNotNull(mbsTransactionExpected);
    }
    
    @Test
    public void testTransLenderRejected() throws Exception {
        createStreamPricingData();
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
        transactionRequestPO.setTradeSettlementDate("2020-07-04");
        transactionRequestPO.setSubmissionDate("2001-07-04T12:08:56.235-0700");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.LENDER_REJECTED);
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("BUY");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setTargetPojo(new MBSTransactionRequest());
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertNotNull(mbsTransactionExpected);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void validateTransactionAmount() throws Exception {
        createStreamPricingData();
        when(mbspProperties.getCutOffHour()).thenReturn("14");
        when(mbspProperties.getCutOffMinute()).thenReturn("00");
        when(mbspProperties.getCutOffSecond()).thenReturn("00");
        when(mbspProperties.getCutOffMillisecond()).thenReturn("00");
        when(tradeServiceProperties.getSourceSystem()).thenReturn("MBSP");
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
        transactionRequestPO.setTradeSettlementDate("2020-07-04");
        transactionRequestPO.setSubmissionDate("2001-07-04T12:08:56.235-0700");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("BUY");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertEquals(new BigDecimal(100000, new MathContext(13, RoundingMode.HALF_EVEN)).setScale(3),
                mbsTransactionExpected.getTradeAmount());
    }

    @Test
    public void validatePricePercent() throws Exception {
        createStreamPricingData();
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
        transactionRequestPO.setTradeSettlementDate("2020-07-04");
        transactionRequestPO.setSubmissionDate("2001-07-04T12:08:56.235-0700");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.TRADER_PRICED);
        transactionRequestPO.setPricePercentHandleText("105");
        transactionRequestPO.setPricePercentTicksText("193");
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("BUY");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        //
        MBSTransactionRequest mbsTransaction = new MBSTransactionRequest();
        mbsTransaction.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransaction.setPricePercent(new BigDecimal(7).setScale(9));
        transformationObject.setTargetPojo(mbsTransaction);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertEquals(new BigDecimal(105.605468750).setScale(9).round(MathContext.DECIMAL64),
                mbsTransaction.getPricePercent());
    }
    
    @Test
    public void validateStreamPrice_bid() throws Exception {
        createStreamPricingData();
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
        transactionRequestPO.setTradeSettlementDate("2020-07-04");
        transactionRequestPO.setSubmissionDate("2001-07-04T12:08:56.235-0700");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.TRADER_PRICED);
        transactionRequestPO.setPricePercentHandleText("105");
        transactionRequestPO.setPricePercentTicksText("193");
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("BUY");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        //
        MBSTransactionRequest mbsTransaction = new MBSTransactionRequest();
        mbsTransaction.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransaction.setTradeBuySellType("BUY");
        mbsTransaction.setPricePercent(new BigDecimal(7).setScale(9));
        transformationObject.setTargetPojo(mbsTransaction);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertEquals(mbsMarketIndicativePrice.getBidPriceText(),mbsTransactionExpected.getStreamPricePercentTicksText());
    }
    
    @Test
    public void validateStreamPrice_ask() throws Exception {
        createStreamPricingData();
        transactionRequestPO = new TransactionRequestPO();
        prodPO = new ProductPO();
        productId = new ProductIdPO();
        productId.setIdentifier(new Long(1));
        productId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
        productId.setType(PRODUCT_TYPE.MBS);
        prodPO.setProductId(productId);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount("100000");
        transactionRequestPO.setTradeBuySellType("BUY");
        transactionRequestPO.setTradeCouponRate("2.0");
        transactionRequestPO.setTradeSettlementDate("2020-07-04");
        transactionRequestPO.setSubmissionDate("2001-07-04T12:08:56.235-0700");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.TRADER_PRICED);
        transactionRequestPO.setPricePercentHandleText("105");
        transactionRequestPO.setPricePercentTicksText("193");
        List<ProductPricingPO> productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("BUY");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPORequest);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE, mbsMarketIndicativePrice);
        //
        MBSTransactionRequest mbsTransaction = new MBSTransactionRequest();
        mbsTransaction.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransaction.setTradeBuySellType("SELL");
        mbsTransaction.setPricePercent(new BigDecimal(7).setScale(9));
        transformationObject.setTargetPojo(mbsTransaction);
        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertEquals(mbsMarketIndicativePrice.getAskPriceText(),mbsTransactionExpected.getStreamPricePercentTicksText());
    }

    @Test
    public void calculatePricePercent() throws Exception {
        String pricePercentHandleText = "104";
        String pricePercentTicksText = "0";
        assertEquals("Not Matching price",
                transactionRequestTransformer.calculatePricePercent(pricePercentTicksText, pricePercentHandleText),
                new BigDecimal(104.000000000).setScale(9).round(MathContext.DECIMAL64));
    }

    @Test
    public void calculatePricePercent1() throws Exception {
        String pricePercentHandleText = "105";
        String pricePercentTicksText = "193";
        assertEquals("Not Matching price",
                transactionRequestTransformer.calculatePricePercent(pricePercentTicksText, pricePercentHandleText),
                new BigDecimal(105.60546875).setScale(9).round(MathContext.DECIMAL64));
    }

    @Test
    public void calculatePricePercent2() throws Exception {
        String pricePercentHandleText = "102";
        String pricePercentTicksText = "066";
        assertEquals("Not Matching price",
                transactionRequestTransformer.calculatePricePercent(pricePercentTicksText, pricePercentHandleText),
                new BigDecimal(102.210937500).setScale(9).round(MathContext.DECIMAL64));
    }

    @Test
    public void calculatePricePercent3() throws Exception {
        String pricePercentHandleText = "103";
        String pricePercentTicksText = "055";
        assertEquals("Not Matching price", new BigDecimal(103.175781250).setScale(9).round(MathContext.DECIMAL64),
                transactionRequestTransformer.calculatePricePercent(pricePercentTicksText, pricePercentHandleText));
    }

    @Test
    public void calculatePricePercent4() throws Exception {
        String pricePercentHandleText = "101";
        String pricePercentTicksText = "161";
        assertEquals("Not Matching price", new BigDecimal(101.5039062500).setScale(9).round(MathContext.DECIMAL64),
                transactionRequestTransformer.calculatePricePercent(pricePercentTicksText, pricePercentHandleText));
    }

    @Test
    public void calculatePricePercent5() throws Exception {
        String pricePercentHandleText = "101";
        String pricePercentTicksText = "193";
        assertEquals("Not Matching price", new BigDecimal(101.60546875).setScale(9).round(MathContext.DECIMAL64),
                transactionRequestTransformer.calculatePricePercent(pricePercentTicksText, pricePercentHandleText));
    }

    @Test
    public void calculatePricePercent6() throws Exception {
        String pricePercentHandleText = "101";
        String pricePercentTicksText = "075";
        assertEquals("Not Matching price", new BigDecimal(101.23828125).setScale(9).round(MathContext.DECIMAL64),
                transactionRequestTransformer.calculatePricePercent(pricePercentTicksText, pricePercentHandleText));
    }

    @Test
    public void calculatePricePercent7() throws Exception {
        String pricePercentHandleText = "101";
        String pricePercentTicksText = "007";
        assertEquals("Not Matching price", new BigDecimal(101.0273437500).setScale(9).round(MathContext.DECIMAL64),
                transactionRequestTransformer.calculatePricePercent(pricePercentTicksText, pricePercentHandleText));
    }

    @Test
    public void calculatePricePercent8() throws Exception {
        String pricePercentHandleText = "101";
        String pricePercentTicksText = "01";
        assertEquals("Not Matching price", new BigDecimal(101.0312500000).setScale(9).round(MathContext.DECIMAL64),
                transactionRequestTransformer.calculatePricePercent(pricePercentTicksText, pricePercentHandleText));
    }

    @Test
    public void transform_Eights_Symbol_And_Validate_Price_Percent_Success() throws Exception {
        transactionRequestPO = new TransactionRequestPO();
        prodPO = new ProductPO();
        productId = createProductIdPO(1L);
        prodPO.setProductId(productId);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount("100000");
        transactionRequestPO.setTradeBuySellType("SELL");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.TRADER_PRICED);
        transactionRequestPO.setPricePercentHandleText("101");
        transactionRequestPO.setPricePercentTicksText("06+");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);

        MBSTransactionRequest mbsTransaction = new MBSTransactionRequest();
        transformationObject.setTargetPojo(mbsTransaction);

        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertEquals(new BigDecimal(101.203125000).setScale(9).round(MathContext.DECIMAL64),
                mbsTransactionExpected.getPricePercent());
    }

    @Test
    public void transform_Eights_Symbol_And_Validate_Price_Percent_Tick_Text_Success() throws Exception {
        transactionRequestPO = new TransactionRequestPO();
        prodPO = new ProductPO();
        productId = createProductIdPO(1L);
        prodPO.setProductId(productId);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount("100000");
        transactionRequestPO.setTradeBuySellType("SELL");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.TRADER_PRICED);
        transactionRequestPO.setPricePercentHandleText("101");
        transactionRequestPO.setPricePercentTicksText("06+");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);

        MBSTransactionRequest mbsTransaction = new MBSTransactionRequest();
        transformationObject.setTargetPojo(mbsTransaction);

        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertEquals("06"+TradeConstants.EIGHTS_MIDDLE_VALUE_SIGN+"-101", mbsTransactionExpected.getPricePercentTicksText());
    }
    
    @Test
    public void transform_Eights_Numeric_And_Validate_Price_Percent_Tick_Text_Success() throws Exception {
        transactionRequestPO = new TransactionRequestPO();
        prodPO = new ProductPO();
        productId = createProductIdPO(1L);
        prodPO.setProductId(productId);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount("100000");
        transactionRequestPO.setTradeBuySellType("SELL");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.TRADER_PRICED);
        transactionRequestPO.setPricePercentHandleText("101");
        transactionRequestPO.setPricePercentTicksText("064");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);

        MBSTransactionRequest mbsTransaction = new MBSTransactionRequest();
        transformationObject.setTargetPojo(mbsTransaction);

        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertEquals("06+-101", mbsTransactionExpected.getPricePercentTicksText());
    }
    
    @Test
    public void transform_Eights_Numeric_And_Validate_Price_Percent_Tick_Text_Failure() throws Exception {
        transactionRequestPO = new TransactionRequestPO();
        prodPO = new ProductPO();
        productId = createProductIdPO(1L);
        prodPO.setProductId(productId);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount("100000");
        transactionRequestPO.setTradeBuySellType("SELL");
        transactionRequestPO.setLenderId("11223344");
        transactionRequestPO.setStateType(StateType.TRADER_PRICED);
        transactionRequestPO.setPricePercentHandleText("101");
        transactionRequestPO.setPricePercentTicksText("064");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);

        MBSTransactionRequest mbsTransaction = new MBSTransactionRequest();
        transformationObject.setTargetPojo(mbsTransaction);

        transactionRequestTransformer.transform(transformationObject);
        MBSTransactionRequest mbsTransactionExpected = (MBSTransactionRequest) transformationObject.getTargetPojo();
        assertNotEquals("064-101", mbsTransactionExpected.getPricePercentTicksText());
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
    public void createStreamPricingData(){
        mbsMarketIndicativePrice = new MBSMarketIndicativePrice();
        mbsMarketIndicativePrice.setAskPricePercent(new BigDecimal("101.25"));
        mbsMarketIndicativePrice.setAskPriceText("101-101");
        mbsMarketIndicativePrice.setBidPricePercent(new BigDecimal("99.25"));
        mbsMarketIndicativePrice.setBidPriceText("99-101");
        
    }

}