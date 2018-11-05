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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.fanniemae.mbsportal.api.enrichment.TransactionRequestEnrichment;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.api.po.TspPartyLenderPO;
import com.fanniemae.mbsportal.api.service.ProfileEntitlementService;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.constants.TradeBuySell;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author g8uaxt Created on 12/29/2017.
 */
public class TransactionRequestEnrichmentTest extends BaseServiceTest {
        
        @Autowired
        TransactionRequestEnrichment transactionRequestEnrichment;// = new TransactionRequestEnrichment();
        @Mock
        private ProfileEntitlementService profileEntitlementService;
        
        private ProfileEntitlementPO lenderProfileEntitlementPO;
        private ProfileEntitlementPO traderProfileEntitlementPO;
        
        @Before
        public void setUp() throws Exception {
                transactionRequestEnrichment.setProfileEntitlementService(profileEntitlementService);
                traderProfileEntitlementPO = createTraderProfileEntitlementPO();
                lenderProfileEntitlementPO = createLenderProfileEntitlementPO();
                when(profileEntitlementService.getProfile("testLender")).thenReturn(lenderProfileEntitlementPO);
        }
        
        @Test
        public void enrichForLenderBuy() throws MBSBaseException {
                TransformationObject transformationObject = new TransformationObject();
                TransactionRequestPO transactionRequestPO = createTransactionRequestPO();
                transactionRequestPO.setTradeBuySellType(TradeBuySell.BUY.name());
                transactionRequestPO.setLenderId("testLender");
                //transactionRequestPO.setTraderId(null);
                MBSTransactionRequest mbsTransactionRequest = createMBSTransactionRequest();
                mbsTransactionRequest.setTransReqNumber(null);
                transformationObject.setSourcePojo(transactionRequestPO);
                transformationObject.setTargetPojo(mbsTransactionRequest);
                transformationObject.setMBSRoleType(MBSRoleType.LENDER);
                transactionRequestEnrichment.enrich(transformationObject);
                MBSTransactionRequest mbsTransactionRequestRet = (MBSTransactionRequest) transformationObject
                        .getTargetPojo();
                assertNotNull(mbsTransactionRequestRet);
                assertNotNull(mbsTransactionRequestRet.getDealerOrgName());
                assertSame("Not Matching Trader", TradeBuySell.SELL.name(),
                        mbsTransactionRequestRet.getTradeBuySellType());
                assertSame("Not Matching Lender", TradeBuySell.BUY.name(),
                        mbsTransactionRequestRet.getCounterPartyBuySellType());
        }
        
        
        @Test
        public void enrichForTspBuy() throws MBSBaseException {
                TransformationObject transformationObject = new TransformationObject();
                TransactionRequestPO transactionRequestPO = createTransactionRequestPO();
                transactionRequestPO.setTradeBuySellType(TradeBuySell.BUY.name());
                transactionRequestPO.setLenderId("testLender");
                transactionRequestPO.setOboLenderSellerServicerNumber("121212");
                when(profileEntitlementService.getProfile("testLender")).thenReturn(createTspProfileEntitlementPO());
                //transactionRequestPO.setTraderId(null);
                MBSTransactionRequest mbsTransactionRequest = createMBSTransactionRequest();
                mbsTransactionRequest.setTransReqNumber(null);
                transformationObject.setSourcePojo(transactionRequestPO);
                transformationObject.setTargetPojo(mbsTransactionRequest);
                transformationObject.setMBSRoleType(MBSRoleType.TSP);
                transactionRequestEnrichment.enrich(transformationObject);
                MBSTransactionRequest mbsTransactionRequestRet = (MBSTransactionRequest) transformationObject
                        .getTargetPojo();
                assertNotNull(mbsTransactionRequestRet);
                assertNotNull(mbsTransactionRequestRet.getDealerOrgName());
                assertSame("Not Matching Trader", TradeBuySell.SELL.name(),
                        mbsTransactionRequestRet.getTradeBuySellType());
                assertSame("Not Matching Lender", TradeBuySell.BUY.name(),
                        mbsTransactionRequestRet.getCounterPartyBuySellType());
        }
        
        @Test
        public void enrichForTspSell() throws MBSBaseException {
                TransformationObject transformationObject = new TransformationObject();
                TransactionRequestPO transactionRequestPO = createTransactionRequestPO();
                transactionRequestPO.setTradeBuySellType(TradeBuySell.SELL.name());
                transactionRequestPO.setLenderId("testLender");
                transactionRequestPO.setOboLenderSellerServicerNumber("121212");
                when(profileEntitlementService.getProfile("testLender")).thenReturn(createTspProfileEntitlementPO());
                //transactionRequestPO.setTraderId(null);
                MBSTransactionRequest mbsTransactionRequest = createMBSTransactionRequest();
                mbsTransactionRequest.setTransReqNumber(null);
                transformationObject.setSourcePojo(transactionRequestPO);
                transformationObject.setTargetPojo(mbsTransactionRequest);
                transformationObject.setMBSRoleType(MBSRoleType.TSP);
                transactionRequestEnrichment.enrich(transformationObject);
                MBSTransactionRequest mbsTransactionRequestRet = (MBSTransactionRequest) transformationObject
                        .getTargetPojo();
                assertNotNull(mbsTransactionRequestRet);
                assertNotNull(mbsTransactionRequestRet.getDealerOrgName());
                assertSame("Not Matching Trader", TradeBuySell.BUY.name(),
                        mbsTransactionRequestRet.getTradeBuySellType());
                assertSame("Not Matching Lender", TradeBuySell.SELL.name(),
                        mbsTransactionRequestRet.getCounterPartyBuySellType());
        }
        
        @Test
        public void enrichForLenderSell() throws MBSBaseException {
                TransformationObject transformationObject = new TransformationObject();
                TransactionRequestPO transactionRequestPO = createTransactionRequestPO();
                transactionRequestPO.setTradeBuySellType(TradeBuySell.SELL.name());
                transactionRequestPO.setLenderId("testLender");
                //transactionRequestPO.setTraderId(null);
                MBSTransactionRequest mbsTransactionRequest = createMBSTransactionRequest();
                mbsTransactionRequest.setTransReqNumber(null);
                transformationObject.setSourcePojo(transactionRequestPO);
                transformationObject.setTargetPojo(mbsTransactionRequest);
                transformationObject.setMBSRoleType(MBSRoleType.LENDER);
                transactionRequestEnrichment.enrich(transformationObject);
                MBSTransactionRequest mbsTransactionRequestRet = (MBSTransactionRequest) transformationObject
                        .getTargetPojo();
                assertNotNull(mbsTransactionRequestRet);
                assertNotNull(mbsTransactionRequestRet.getDealerOrgName());
                assertSame("Not Matching Trader", TradeBuySell.BUY.name(),
                        mbsTransactionRequestRet.getTradeBuySellType());
                assertSame("Not Matching Lender", TradeBuySell.SELL.name(),
                        mbsTransactionRequestRet.getCounterPartyBuySellType());
        }
        
        @Test
        public void enrichForTraderBuy_LenderOpen_Lock() throws MBSBaseException {
                when(profileEntitlementService.getProfile("testTrader")).thenReturn(traderProfileEntitlementPO);
                
                TransformationObject transformationObject = new TransformationObject();
                TransactionRequestPO transactionRequestPO = createTransactionRequestPO();
                transactionRequestPO.setStateType(StateType.LENDER_OPEN);
                transactionRequestPO.setTradeBuySellType(TradeBuySell.BUY.name());
                transactionRequestPO.setLenderId(null);
                transactionRequestPO.setTraderId("testTrader");
                MBSTransactionRequest mbsTransactionRequest = createMBSTransactionRequest();
                mbsTransactionRequest.setTransReqNumber("TA0001234");
                //mbsTransactionRequest.setTradeTraderIdentifierText(null);
                mbsTransactionRequest.setTraderName(null);
                transformationObject.setSourcePojo(transactionRequestPO);
                transformationObject.setTargetPojo(mbsTransactionRequest);
                transactionRequestEnrichment.enrich(transformationObject);
                MBSTransactionRequest mbsTransactionRequestRet = (MBSTransactionRequest) transformationObject
                        .getTargetPojo();
                assertNotNull(mbsTransactionRequestRet);
                assertNotNull(mbsTransactionRequestRet.getTraderName());
                assertEquals("Not Matching Trader Name", "testTraderFirstName,testTraderLastName",
                        mbsTransactionRequestRet
                        .getTraderName());
        }
        
        @Test
        public void enrichForTraderBuy_LenderOpen_UnLock() throws MBSBaseException {
                when(profileEntitlementService.getProfile("testTrader")).thenReturn(traderProfileEntitlementPO);
                
                TransformationObject transformationObject = new TransformationObject();
                TransactionRequestPO transactionRequestPO = createTransactionRequestPO();
                transactionRequestPO.setStateType(StateType.LENDER_OPEN);
                transactionRequestPO.setTradeBuySellType(TradeBuySell.BUY.name());
                transactionRequestPO.setLenderId(null);
                transactionRequestPO.setTraderId(null);
                MBSTransactionRequest mbsTransactionRequest = createMBSTransactionRequest();
                mbsTransactionRequest.setTransReqNumber("TA0001234");
               // mbsTransactionRequest.setTradeTraderIdentifierText("testTrader");
                mbsTransactionRequest.setTraderName("Test Trader Name");
                transformationObject.setSourcePojo(transactionRequestPO);
                transformationObject.setTargetPojo(mbsTransactionRequest);
                transactionRequestEnrichment.enrich(transformationObject);
                MBSTransactionRequest mbsTransactionRequestRet = (MBSTransactionRequest) transformationObject
                        .getTargetPojo();
                assertNotNull(mbsTransactionRequestRet);
                assertNull(mbsTransactionRequestRet.getTraderName());
                //assertNull(mbsTransactionRequestRet.getTradeTraderIdentifierText());
        }
        
        @Test
        public void enrichForTraderSell() throws MBSBaseException {
                when(profileEntitlementService.getProfile("testTrader")).thenReturn(traderProfileEntitlementPO);
                
                TransformationObject transformationObject = new TransformationObject();
                TransactionRequestPO transactionRequestPO = createTransactionRequestPO();
                transactionRequestPO.setLenderId(null);
                transactionRequestPO.setTraderId("testTrader");
                transactionRequestPO.setTradeBuySellType(TradeBuySell.SELL.name());
                MBSTransactionRequest mbsTransactionRequest = createMBSTransactionRequest();
                mbsTransactionRequest.setTransReqNumber(null);
                //mbsTransactionRequest.setTraderName("Test Trader Name");
                transformationObject.setSourcePojo(transactionRequestPO);
                transformationObject.setTargetPojo(mbsTransactionRequest);
                transactionRequestEnrichment.enrich(transformationObject);
                MBSTransactionRequest mbsTransactionRequestRet = (MBSTransactionRequest) transformationObject
                        .getTargetPojo();
                assertNotNull(mbsTransactionRequestRet);
                assertEquals(TradeBuySell.SELL.name(), mbsTransactionRequestRet.getTradeBuySellType());
        }
        
        /**
         * Purpose: Test case when exception is thrown
         *
         * @throws MBSBaseException
         * @throws Exception
         */
        @Test(expected = MBSBaseException.class)
        public void enrichBaseException() throws MBSBaseException {
                doThrow(new MBSBaseException("Test case Exception")).when(profileEntitlementService)
                        .getProfile("testTrader");
                TransformationObject transformationObject = new TransformationObject();
                TransactionRequestPO transactionRequestPO = createTransactionRequestPO();
                transactionRequestPO.setLenderId(null);
                transactionRequestPO.setTraderId("testTrader");
                transactionRequestPO.setTradeBuySellType(TradeBuySell.SELL.name());
                MBSTransactionRequest mbsTransactionRequest = createMBSTransactionRequest();
                mbsTransactionRequest.setTransReqNumber(null);
                //mbsTransactionRequest.setTraderName("Test Trader Name");
                transformationObject.setSourcePojo(transactionRequestPO);
                transformationObject.setTargetPojo(mbsTransactionRequest);
                transactionRequestEnrichment.enrich(transformationObject);
                MBSTransactionRequest mbsTransactionRequestRet = (MBSTransactionRequest) transformationObject
                        .getTargetPojo();
                assertNotNull(mbsTransactionRequestRet);
                
        }
        
        /**
         * Purpose: Test case when exception is thrown
         *
         * @throws Exception
         * @throws Exception
         */
        @Test(expected = Exception.class)
        public void enrichException() throws MBSBaseException {
                doThrow(new Exception("Test case Exception")).when(profileEntitlementService).getProfile("testTrader");
                TransformationObject transformationObject = new TransformationObject();
                TransactionRequestPO transactionRequestPO = createTransactionRequestPO();
                transactionRequestPO.setLenderId(null);
                transactionRequestPO.setTraderId("testTrader");
                transactionRequestPO.setTradeBuySellType(TradeBuySell.SELL.name());
                MBSTransactionRequest mbsTransactionRequest = createMBSTransactionRequest();
                mbsTransactionRequest.setTransReqNumber(null);
                //mbsTransactionRequest.setTraderName("Test Trader Name");
                transformationObject.setSourcePojo(transactionRequestPO);
                transformationObject.setTargetPojo(mbsTransactionRequest);
                transactionRequestEnrichment.enrich(transformationObject);
                MBSTransactionRequest mbsTransactionRequestRet = (MBSTransactionRequest) transformationObject
                        .getTargetPojo();
                assertNotNull(mbsTransactionRequestRet);
                
        }
        
        /**
         * util method - createMBSTransactionRequest
         * @return
         */
        private MBSTransactionRequest createMBSTransactionRequest() {
                MBSTransactionRequest mbsTransactionRequest = new MBSTransactionRequest();
                ProductId prodId = new ProductId();
                prodId.setIdentifier(new Long(1));
                prodId.setSourceType(TradeConstants.PRODUCT_SOURCE_TYPE.PU.toString());
                prodId.setType(TradeConstants.PRODUCT_TYPE.MBS.toString());
                mbsTransactionRequest.setProductId(prodId);
                mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
                mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
                mbsTransactionRequest.setSubmissionDate(new Date());
                mbsTransactionRequest.setTradeAmount(new BigDecimal(9000000));
                mbsTransactionRequest.setTradeBuySellType("SELL");
                mbsTransactionRequest.setTradeCouponRate(new BigDecimal(5.5));
                mbsTransactionRequest.setTradeSettlementDate(new Date());
                mbsTransactionRequest.setTransReqNumber("10001");
                mbsTransactionRequest
                        .setPricePercent(new BigDecimal(104.000000000).setScale(9).round(MathContext.DECIMAL64));
                return mbsTransactionRequest;
        }
        
        /**
         * util method - createTransactionRequestPO
         * @return
         */
        private TransactionRequestPO createTransactionRequestPO() {
                TransactionRequestPO transactionRequestPO = new TransactionRequestPO();
                ProductPO prodPO = new ProductPO();
                ProductIdPO productId = new ProductIdPO();
                productId.setIdentifier(new Long(21));
                productId.setSourceType(TradeConstants.PRODUCT_SOURCE_TYPE.PU);
                productId.setType(TradeConstants.PRODUCT_TYPE.MBS);
                prodPO.setProductId(productId);
                prodPO.setNameCode("FN30");
                transactionRequestPO.setProduct(prodPO);
                transactionRequestPO.setLenderId("testLender");
                transactionRequestPO.setStateType(StateType.EXECUTED);
                transactionRequestPO.setTransReqId("17J00004");
                return transactionRequestPO;
        }
        
        /**
         * util method -createTraderProfileEntitlementPO
         * @return
         */
        private ProfileEntitlementPO createTraderProfileEntitlementPO() {
                ProfileEntitlementPO traderProfileEntitlementPO = new ProfileEntitlementPO();
                traderProfileEntitlementPO = new ProfileEntitlementPO();
                traderProfileEntitlementPO.setUserName("testTrader");
                traderProfileEntitlementPO.setFirstName("testTraderFirstName");
                traderProfileEntitlementPO.setLastName("testTraderLastName");
                return traderProfileEntitlementPO;
        }
        
        /**
         * util method -createLenderProfileEntitlementPO
         * @return
         */
        private ProfileEntitlementPO createLenderProfileEntitlementPO() {
                ProfileEntitlementPO lenderProfileEntitlementPO = new ProfileEntitlementPO();
                lenderProfileEntitlementPO = new ProfileEntitlementPO();
                lenderProfileEntitlementPO.setFirstName("testLenderFirstName");
                lenderProfileEntitlementPO.setUserName("testLender");
                lenderProfileEntitlementPO.setLastName("testLenderLastName");
                lenderProfileEntitlementPO.setDealerOrgName("TEST COMPANY");
                return lenderProfileEntitlementPO;
        }
        
        /**
         * util method -createLenderProfileEntitlementPO
         * @return
         */
        private ProfileEntitlementPO createTspProfileEntitlementPO() {
                ProfileEntitlementPO tspProfileEntitlementPO = new ProfileEntitlementPO();
                tspProfileEntitlementPO = new ProfileEntitlementPO();
                tspProfileEntitlementPO.setFirstName("testLenderFirstName");
                tspProfileEntitlementPO.setUserName("testLender");
                tspProfileEntitlementPO.setLastName("testLenderLastName");
                tspProfileEntitlementPO.setDealerOrgName("TEST COMPANY");
                List<TspPartyLenderPO> tspLenders = new ArrayList<>();
                TspPartyLenderPO tspLender = new TspPartyLenderPO();
                tspLender.setName("TEST COMPANY");
                tspLender.setShortName("TC");
                tspLender.setLenderSellerServicerNumber("121212");
                tspLenders.add(tspLender);
                tspProfileEntitlementPO.setTspLenders(tspLenders);
                return tspProfileEntitlementPO;
        }
        
}
