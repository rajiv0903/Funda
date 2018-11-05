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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.config.TradeServiceProperties;
import com.fanniemae.mbsportal.api.persister.TransactionPersister;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.po.ProductPricingPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.api.po.TspPartyLenderPO;
import com.fanniemae.mbsportal.api.transformation.TransactionRequestPOTransformer;
import com.fanniemae.mbsportal.api.transformation.TransactionRequestTransformer;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.api.validator.TransactionRequestPOValidator;
import com.fanniemae.mbsportal.api.validator.TransactionRequestValidator;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.dao.MBSMarketIndicativePriceDao;
import com.fanniemae.mbsportal.dao.MBSQueryFunctionDao;
import com.fanniemae.mbsportal.dao.MBSTransactionRequestDao;
import com.fanniemae.mbsportal.enrichment.Enricher;
import com.fanniemae.mbsportal.gf.pojo.SortBy;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.publisher.MessagePublisher;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 29, 2018
 * @Time 12:10:35 PM
 * 	com.fanniemae.mbsportal.api.service
 * 	TransactionRequestServiceTest.java
 * @Description: CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
 */

public class TransactionRequestServiceTest extends BaseServiceTest {

    @Autowired
    TradeServiceProperties tradeServiceProperties;
    
    @Mock
    MbspProperties mbspProperties;
    

    @Mock
    TransactionRequestTransformer<TransformationObject> transactionRequestTransformer;
    @Mock
    TransactionRequestPOValidator<TransformationObject> transactionRequestPOValidator;
    @Mock
    TransactionRequestValidator<TransformationObject> transactionRequestValidator;
    @Mock
    TransactionPersister transactionPersister;
    @Mock
    Enricher<TransformationObject> transactionRequestEnrichment;
    @Mock
    MessagePublisher<TransformationObject>  transactionRequestMessagePublisher;
    @Mock
    TransactionRequestPOTransformer<TransformationObject> transactionRequestPOTransformer;

    @Mock
    ProductService productService;
    @Mock
    ProductPricingService productPricingService;
    
    @Mock
    MBSMarketIndicativePriceDao mbsMarketIndicativePriceDao;
    
    @Mock
    ProfileEntitlementService profileEntitlementService;

    @Mock
    MBSTransactionRequestDao mbsTransactionDao;
    @Mock
    MBSQueryFunctionDao mbsQueryFunctionDao;
    @Mock
    MBSObjectCreator mbsObjectCreator;
    @Mock
    ExceptionLookupService exceptionLookupService;

    @InjectMocks
    TransactionRequestService transactionRequestService;

    TransactionRequestPO transactionRequestPO;
    ProductPO prodPO;
    ProductIdPO productId;
    List<MBSTransactionRequest> lstMbsTrans;
    List<ProductPO> prodPOLst;
    List<String> stateTypeLst;
    TransformationObject transformationObject;
    ProfileEntitlementPO profileEntitlementPO;
    List<String> lenderLst;
    ProductPricingPO productPricingPO;
    List<ProductPricingPO> productPricingPORequest;
    MBSMarketIndicativePrice mbsMarketIndicativePrice;
    
    Map<String, String> reqHeaderMap;
    

    @Before
    public void setUp() throws Exception {
        reqHeaderMap = new HashMap<>();
        Mockito.when(exceptionLookupService.getExceptionLookupDataMap(Mockito.any())).thenReturn(createExData());
    }
    
    public Map<String, ExceptionLookupPO> createExData() {
        Map<String, ExceptionLookupPO> exceptionLookupPOLst = new HashMap<String, ExceptionLookupPO>();
        ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        exceptionLookupPO.setErrorCategory("API");
        exceptionLookupPO.setErrorCode("TRAN_0001");
        exceptionLookupPO.setMessageType("DISP_ERROR");
        exceptionLookupPO.setErrorMessage("Test Message");
        exceptionLookupPO.setLoggerMessage("Test Message");
        exceptionLookupPOLst.put("TRAN_0001",exceptionLookupPO);
        return exceptionLookupPOLst;
    }

    /**
     * Initialize the mocks
     *
     * @throws MBSBaseException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void initMocks() throws MBSBaseException {
        transactionRequestService = new TransactionRequestService(transactionRequestTransformer,
                transactionRequestPOValidator, transactionRequestValidator, transactionPersister,
                transactionRequestPOTransformer, transactionRequestEnrichment, transactionRequestMessagePublisher);
        MockitoAnnotations.initMocks(this);
        doNothing().when(transactionPersister).clearAll();
        doNothing().when((Persister) transactionPersister).persist(any());
        doNothing().when(transactionRequestPOValidator).validate(any());
        doNothing().when(transactionRequestValidator).validate(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        when(transactionPersister.getBaseDao()).thenReturn(mbsTransactionDao);
        when(tradeServiceProperties.getSourceSystem()).thenReturn("MBSP");
        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
    }

    /**
     * Purpose: This method test call to save the data to gemfire
     *
     * @throws Exception
     */
    @Test
    public void createMBSTransReq_Success() throws MBSBaseException {
        initMocks();
        createData();
        when(transactionRequestTransformer.transform(any())).thenReturn(transformationObject);
        when(transactionRequestPOTransformer.transform(any())).thenReturn(transformationObject);
        when(productService.getByProductId(any())).thenReturn(prodPO);
        when(productPricingService.getProductPricing(any(), Mockito.anyBoolean())).thenReturn(productPricingPORequest);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        when(profileEntitlementService.getProfile(any())).thenReturn(profileEntitlementPO);
        Mockito.when(exceptionLookupService.getExceptionLookupDataMap(Mockito.any())).thenReturn(createExData());
        TransactionRequestPO transactionRequestPORet = transactionRequestService.createMBSTransReq(transactionRequestPO,
                MBSRoleType.LENDER, reqHeaderMap);
        assertNotNull(transactionRequestPORet);
        assertEquals("900000", transactionRequestPORet.getTradeAmount());
    }
    
    /**
     * Purpose: This method test call to save the data to gemfire
     *
     * @throws Exception
     */
    @Test
    public void createMBSTransReq_Success_tsp() throws MBSBaseException {
        initMocks();
        createData();
        transactionRequestPO.setOboLenderSellerServicerNumber("112233");
        transformationObject.setMBSRoleType(MBSRoleType.TSP);
        when(transactionRequestTransformer.transform(any())).thenReturn(transformationObject);
        when(transactionRequestPOTransformer.transform(any())).thenReturn(transformationObject);
        when(productService.getByProductId(any())).thenReturn(prodPO);
        when(productPricingService.getProductPricing(any(), Mockito.anyBoolean())).thenReturn(productPricingPORequest);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        when(profileEntitlementService.getProfile(any())).thenReturn(profileEntitlementPO);
        Mockito.when(exceptionLookupService.getExceptionLookupDataMap(Mockito.any())).thenReturn(createExData());
        TransactionRequestPO transactionRequestPORet = transactionRequestService.createMBSTransReq(transactionRequestPO,
                MBSRoleType.LENDER, reqHeaderMap);
        assertNotNull(transactionRequestPORet);
        assertEquals("900000", transactionRequestPORet.getTradeAmount());
    }

    /**
     * Purpose: This method test call to save the data to gemfire
     *
     * @throws Exception
     */
    @Test(expected = MBSBaseException.class)
    public void createMBSTransReq_Invalid_State_Type_Failure() throws MBSBaseException {
        initMocks();
        createUpdateData();
        when(transactionRequestTransformer.transform(any())).thenReturn(transformationObject);
        when(transactionRequestPOTransformer.transform(any())).thenReturn(transformationObject);
        when(productService.getByProductId(any())).thenReturn(prodPO);
        when(productPricingService.getProductPricing(any(), Mockito.anyBoolean())).thenReturn(productPricingPORequest);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        when(profileEntitlementService.getProfile(any())).thenReturn(profileEntitlementPO);
        TransactionRequestPO transactionRequestPORet = transactionRequestService.createMBSTransReq(transactionRequestPO,
                MBSRoleType.LENDER, reqHeaderMap);
        assertNotNull(transactionRequestPORet);
        assertEquals("900000", transactionRequestPORet.getTradeAmount());
    }

    /**
     * Purpose: This method test call to save the data to gemfire
     *
     * @throws Exception
     */
    @Test(expected = MBSBaseException.class)
    public void createMBSTransReq_No_Product_From_User_Input_Failure() throws MBSBaseException {
        initMocks();
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setProduct(null);
        TransactionRequestPO transactionRequestPORet = transactionRequestService.createMBSTransReq(transactionRequestPO,
                MBSRoleType.LENDER, reqHeaderMap);
        assertNotNull(transactionRequestPORet);
        assertEquals("900000", transactionRequestPORet.getTradeAmount());
    }

    /**
     * Purpose: This method test call to save the data to gemfire
     *
     * @throws Exception
     */
    @Test(expected = MBSBaseException.class)
    public void createMBSTransReq_No_Product_Presence_Failure() throws MBSBaseException {
        initMocks();
        createData();
        when(transactionRequestTransformer.transform(any())).thenReturn(transformationObject);
        when(transactionRequestPOTransformer.transform(any())).thenReturn(transformationObject);
        when(profileEntitlementService.getProfile(any())).thenReturn(profileEntitlementPO);
        when(productService.getByProductId(any())).thenReturn(null);
        TransactionRequestPO transactionRequestPORet = transactionRequestService.createMBSTransReq(transactionRequestPO,
                MBSRoleType.LENDER, reqHeaderMap);
        assertNotNull(transactionRequestPORet);
        assertEquals("900000", transactionRequestPORet.getTradeAmount());
    }

    /**
     * Purpose: This method is for testing PUT service for Trader
     *
     * @throws MBSBaseException
     */
    @Test
    public void updateMBSTransReq_Trader_Activity_Success() throws MBSBaseException {
        initMocks();
        createUpdateData();
        when(mbsTransactionDao.getTransReqByTransReqNumber(any())).thenReturn(lstMbsTrans.get(0));
        when(transactionRequestTransformer.transform(any())).thenReturn(transformationObject);
        when(transactionRequestPOTransformer.transform(any())).thenReturn(transformationObject);
        when(productService.getByProductId(any())).thenReturn(prodPO);
        when(profileEntitlementService.getProfile(any())).thenReturn(profileEntitlementPO);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        Mockito.when(exceptionLookupService.getExceptionLookupDataMap(Mockito.any())).thenReturn(createExData());
        TransactionRequestPO transactionRequestPOTrader = transactionRequestService
                .updateMBSTransReq(transactionRequestPO, MBSRoleType.TRADER, reqHeaderMap);
        assertNotNull(transactionRequestPOTrader);
        assertEquals(StateType.TRADER_PRICED, transactionRequestPOTrader.getStateType());
    }
    
    /**
     * Purpose: This method is for testing PUT service for Trader
     *
     * @throws MBSBaseException
     */
    @Test
    public void updateMBSTransReq_Trader_Activity_No_Stream_price() throws MBSBaseException {
        initMocks();
        createUpdateData();
        when(mbsTransactionDao.getTransReqByTransReqNumber(any())).thenReturn(lstMbsTrans.get(0));
        when(transactionRequestTransformer.transform(any())).thenReturn(transformationObject);
        when(transactionRequestPOTransformer.transform(any())).thenReturn(transformationObject);
        when(productService.getByProductId(any())).thenReturn(prodPO);
        when(profileEntitlementService.getProfile(any())).thenReturn(profileEntitlementPO);
        when(mbsMarketIndicativePriceDao.getById(any())).thenThrow(Exception.class);
        Mockito.when(exceptionLookupService.getExceptionLookupDataMap(Mockito.any())).thenReturn(createExData());
        TransactionRequestPO transactionRequestPOTrader = transactionRequestService
                .updateMBSTransReq(transactionRequestPO, MBSRoleType.TRADER, reqHeaderMap);
        assertNotNull(transactionRequestPOTrader);
        assertEquals(StateType.TRADER_PRICED, transactionRequestPOTrader.getStateType());
    }
    
    @Test
    public void updateMBSTransReq_Trader_Activity_Soft_Lock_Success() throws MBSBaseException {
        initMocks();
        createUpdateData();
        when(mbsTransactionDao.getTransReqByTransReqNumber(any())).thenReturn(lstMbsTrans.get(0));
        transactionRequestPO = (TransactionRequestPO) transformationObject.getSourcePojo();
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        when(transactionRequestTransformer.transform(any())).thenReturn(transformationObject);
        when(transactionRequestPOTransformer.transform(any())).thenReturn(transformationObject);
        when(productService.getByProductId(any())).thenReturn(prodPO);
        when(profileEntitlementService.getProfile(any())).thenReturn(profileEntitlementPO);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        Mockito.when(exceptionLookupService.getExceptionLookupDataMap(Mockito.any())).thenReturn(createExData());
        TransactionRequestPO transactionRequestPOTrader = transactionRequestService
                .updateMBSTransReq(transactionRequestPO, MBSRoleType.TRADER, reqHeaderMap);
        assertNotNull(transactionRequestPOTrader);
        assertEquals(StateType.LENDER_OPEN, transactionRequestPOTrader.getStateType());
    }
    
    @Test(expected=MBSBusinessException.class)
    public void updateMBSTransReq_Trader_Activity_Soft_Lock_Invalid_Lender_Profile_Failure() throws MBSBaseException {
        initMocks();
        createUpdateData();
        when(mbsTransactionDao.getTransReqByTransReqNumber(any())).thenReturn(lstMbsTrans.get(0));
        transactionRequestPO = (TransactionRequestPO) transformationObject.getSourcePojo();
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        when(transactionRequestTransformer.transform(any())).thenReturn(transformationObject);
        when(transactionRequestPOTransformer.transform(any())).thenReturn(transformationObject);
        when(productService.getByProductId(any())).thenReturn(prodPO);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        doThrow(MBSBusinessException.class).when(profileEntitlementService).getProfile(any());
        Mockito.when(exceptionLookupService.getExceptionLookupDataMap(Mockito.any())).thenReturn(createExData());
        TransactionRequestPO transactionRequestPOTrader = transactionRequestService
                .updateMBSTransReq(transactionRequestPO, MBSRoleType.TRADER, reqHeaderMap);
        assertNotNull(transactionRequestPOTrader);
        assertEquals(StateType.LENDER_OPEN, transactionRequestPOTrader.getStateType());
    }
    
//    @Test(expected=MBSBusinessException.class)
//    public void updateMBSTransReq_Trader_Activity_Soft_Lock_Empty_Lender_Profile_Failure() throws MBSBaseException {
//        initMocks();
//        createUpdateData();
//        when(mbsTransactionDao.getById(any())).thenReturn(lstMbsTrans.get(0));
//        transactionRequestPO = (TransactionRequestPO) transformationObject.getSourcePojo();
//        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
//        when(transactionRequestTransformer.transform(any())).thenReturn(transformationObject);
//        when(transactionRequestPOTransformer.transform(any())).thenReturn(transformationObject);
//        when(productService.getByProductId(any())).thenReturn(prodPO);
//        when(profileEntitlementService.getProfile(any())).thenReturn(null);
//        TransactionRequestPO transactionRequestPOTrader = transactionRequestService
//                .updateMBSTransReq(transactionRequestPO, MBSRoleType.TRADER, reqHeaderMap);
//        assertNotNull(transactionRequestPOTrader);
//        assertEquals(StateType.LENDER_OPEN, transactionRequestPOTrader.getStateType());
//    }
    
    @Test
    public void updateMBSTransReq_Trader_Activity_Soft_Lock_Release_Success() throws MBSBaseException {
        initMocks();
        createUpdateData();
        when(mbsTransactionDao.getTransReqByTransReqNumber(any())).thenReturn(lstMbsTrans.get(0));
        transactionRequestPO = (TransactionRequestPO) transformationObject.getSourcePojo();
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setTraderId(null);
        when(transactionRequestTransformer.transform(any())).thenReturn(transformationObject);
        when(transactionRequestPOTransformer.transform(any())).thenReturn(transformationObject);
        when(productService.getByProductId(any())).thenReturn(prodPO);
        when(profileEntitlementService.getProfile(any())).thenReturn(profileEntitlementPO);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        Mockito.when(exceptionLookupService.getExceptionLookupDataMap(Mockito.any())).thenReturn(createExData());
        TransactionRequestPO transactionRequestPOTrader = transactionRequestService
                .updateMBSTransReq(transactionRequestPO, MBSRoleType.TRADER, reqHeaderMap);
        assertNotNull(transactionRequestPOTrader);
        assertEquals(StateType.LENDER_OPEN, transactionRequestPOTrader.getStateType());
    }
    
    
    @SuppressWarnings("unused")
    @Test(expected = MBSBusinessException.class)
    public void updateMBSTransReq_Lender_Activity_Empty_Profile_Failure() throws MBSBaseException {
        initMocks();
        createUpdateData();
        when(mbsTransactionDao.getTransReqByTransReqNumber(any())).thenReturn(lstMbsTrans.get(0));
        when(transactionRequestTransformer.transform(any())).thenReturn(transformationObject);
        when(transactionRequestPOTransformer.transform(any())).thenReturn(transformationObject);
        when(productService.getByProductId(any())).thenReturn(prodPO);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        when(profileEntitlementService.getProfile(any())).thenReturn(null);
        Mockito.when(exceptionLookupService.getExceptionLookupDataMap(Mockito.any())).thenReturn(createExData());
        TransactionRequestPO transactionRequestPOTrader = transactionRequestService
                .updateMBSTransReq(transactionRequestPO, MBSRoleType.LENDER, reqHeaderMap);
    }
    
    @Test
    public void updateMBSTransReq_Lender_Activity_Success() throws MBSBaseException {
        initMocks();
        createUpdateData();
        when(mbsTransactionDao.getTransReqByTransReqNumber(any())).thenReturn(lstMbsTrans.get(0));
        transactionRequestPO = (TransactionRequestPO)transformationObject.getSourcePojo();
        transactionRequestPO.setTraderId(null);
        when(transactionRequestTransformer.transform(any())).thenReturn(transformationObject);
        when(transactionRequestPOTransformer.transform(any())).thenReturn(transformationObject);
        when(productService.getByProductId(any())).thenReturn(prodPO);
        when(profileEntitlementService.getProfile(any())).thenReturn(profileEntitlementPO);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        Mockito.when(exceptionLookupService.getExceptionLookupDataMap(Mockito.any())).thenReturn(createExData());
        TransactionRequestPO transactionRequestPOTrader = transactionRequestService
                .updateMBSTransReq(transactionRequestPO, MBSRoleType.LENDER, reqHeaderMap);
        assertNotNull(transactionRequestPOTrader);
        assertEquals(StateType.TRADER_PRICED, transactionRequestPOTrader.getStateType());
    }

    /**
     * Purpose: This method is for testing PUT service for Trader
     *
     * @throws MBSBaseException
     */
    @Test(expected = MBSBaseException.class)
    public void updateMBSTransReq_Trader_Empty_Profile_Failure() throws MBSBaseException {
        initMocks();
        createUpdateData();
        when(mbsTransactionDao.getTransReqByTransReqNumber(any())).thenReturn(lstMbsTrans.get(0));
        when(transactionRequestTransformer.transform(any())).thenReturn(transformationObject);
        when(transactionRequestPOTransformer.transform(any())).thenReturn(transformationObject);
        when(productService.getByProductId(any())).thenReturn(prodPO);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        when(profileEntitlementService.getProfile(any())).thenReturn(null);
        Mockito.when(exceptionLookupService.getExceptionLookupDataMap(Mockito.any())).thenReturn(createExData());
        TransactionRequestPO transactionRequestPOTrader = transactionRequestService
                .updateMBSTransReq(transactionRequestPO, MBSRoleType.TRADER, reqHeaderMap);
        assertNotNull(transactionRequestPOTrader);
        assertEquals(StateType.TRADER_PRICED, transactionRequestPOTrader.getStateType());
    }

    /**
     * Purpose: This method is to clear all transaction requests - temp use
     * only.
     *
     * @throws Exception
     */
    @Test
    public void clearAllMBSTransactionRequests() throws Exception {
        initMocks();
        when(transactionPersister.getBaseDao()).thenReturn(mbsTransactionDao);
        transactionRequestService.clearAll();
    }

    /**
     * Purpose: This method test call to get the data from gemfire
     *
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testGetMBSTransTrader() throws Exception {
        TransactionRequestPOTransformer transactionRequestPOTransformer = Mockito
                .spy(TransactionRequestPOTransformer.class);
        transactionRequestService = new TransactionRequestService(transactionRequestTransformer,
                transactionRequestPOValidator, transactionRequestValidator, transactionPersister,
                transactionRequestPOTransformer, transactionRequestEnrichment, transactionRequestMessagePublisher);
        MockitoAnnotations.initMocks(this);
        when(tradeServiceProperties.getSourceSystem()).thenReturn("MBSP");
        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
        doNothing().when(transactionPersister).clearAll();
        doNothing().when((Persister) transactionPersister).persist(any());
        doNothing().when(transactionRequestPOValidator).validate(any());
        doNothing().when(transactionRequestValidator).validate(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        createData();
        lstMbsTrans.get(0).setTransReqNumber("17I00509");
        when(((Persister) transactionPersister).getBaseDao()).thenReturn(mbsTransactionDao);
        when(mbsTransactionDao.getAll()).thenReturn(lstMbsTrans);
        when(mbsTransactionDao.getTransReqByTransReqNumber(any())).thenReturn(lstMbsTrans.get(0));
        Mockito.doReturn(transformationObject).when(((Transformer) transactionRequestPOTransformer)).transform(any());
        when(productService.getByProductId(any())).thenReturn(prodPO);
        when(profileEntitlementService.getProfile(any())).thenReturn(profileEntitlementPO);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        List<String> stateType = new ArrayList<>();
        stateType.add("LENDER_OPEN");
        stateType.add("LENDER_ACCEPTED");
        stateType.add("LENDER_REJECTED");
        stateType.add("TRADER_PRICED");
        stateType.add("TRADER_PASSED");
        Optional<String> transReqid = Optional.of("17I00509");
        String traderId = "";
        List<TransactionRequestPO> lstTransPOResult = transactionRequestService.getMBSTransReq(traderId, transReqid,
                stateType, MBSRoleType.TRADER);
        assertNotNull(lstTransPOResult);
    }

    /**
     * Purpose: This method test call to get the data from gemfire
     *
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @Test(expected = MBSBaseException.class)
    public void testGetMBSTransTraderExceptionSourceSystem() throws Exception {
        TransactionRequestPOTransformer transactionRequestPOTransformer = Mockito
                .spy(TransactionRequestPOTransformer.class);
        transactionRequestService = new TransactionRequestService(transactionRequestTransformer,
                transactionRequestPOValidator, transactionRequestValidator, transactionPersister,
                transactionRequestPOTransformer, transactionRequestEnrichment,transactionRequestMessagePublisher);
        MockitoAnnotations.initMocks(this);
        when(tradeServiceProperties.getSourceSystem()).thenReturn("MB");
        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
        doNothing().when(transactionPersister).clearAll();
        doNothing().when((Persister) transactionPersister).persist(any());
        doNothing().when(transactionRequestPOValidator).validate(any());
        doNothing().when(transactionRequestValidator).validate(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        createData();
        lstMbsTrans.get(0).setTransReqNumber("17I00509");
        when(((Persister) transactionPersister).getBaseDao()).thenReturn(mbsTransactionDao);
        when(mbsTransactionDao.getAll()).thenReturn(lstMbsTrans);
        when(mbsTransactionDao.getTransReqByTransReqNumber(any())).thenReturn(lstMbsTrans.get(0));
        Mockito.doReturn(transformationObject).when(((Transformer) transactionRequestPOTransformer)).transform(any());
        when(productService.getByProductId(any())).thenReturn(prodPO);
        when(profileEntitlementService.getProfile(any())).thenReturn(profileEntitlementPO);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        List<String> stateType = new ArrayList<>();
        stateType.add("LENDER_OPEN");
        stateType.add("LENDER_ACCEPTED");
        stateType.add("LENDER_REJECTED");
        stateType.add("TRADER_PRICED");
        stateType.add("TRADER_PASSED");
        Optional<String> transReqid = Optional.of("17I00509");
        String traderId = "";
        List<TransactionRequestPO> lstTransPOResult = transactionRequestService.getMBSTransReq(traderId, transReqid,
                stateType, MBSRoleType.TRADER);
        assertNotNull(lstTransPOResult);
    }

    /**
     * Purpose: This method test call to get the data from gemfire
     *
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testGetMBSTransTraderNoTransId() throws Exception {
        TransactionRequestPOTransformer transactionRequestPOTransformer = Mockito
                .spy(TransactionRequestPOTransformer.class);
        transactionRequestService = new TransactionRequestService(transactionRequestTransformer,
                transactionRequestPOValidator, transactionRequestValidator, transactionPersister,
                transactionRequestPOTransformer, transactionRequestEnrichment,transactionRequestMessagePublisher);
        MockitoAnnotations.initMocks(this);
        when(tradeServiceProperties.getSourceSystem()).thenReturn("MBSP");
        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
        doNothing().when(transactionPersister).clearAll();
        doNothing().when((Persister) transactionPersister).persist(any());
        doNothing().when(transactionRequestPOValidator).validate(any());
        doNothing().when(transactionRequestValidator).validate(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        createData();
        lstMbsTrans.get(0).setTransReqNumber("17I00509");
        when(((Persister) transactionPersister).getBaseDao()).thenReturn(mbsTransactionDao);
        when(mbsTransactionDao.getAll()).thenReturn(lstMbsTrans);
        when(mbsTransactionDao.getTransReqStateType(any(), any(), any(), any())).thenReturn(lstMbsTrans);
        when(mbsTransactionDao.getTransReqByTransReqNumber(any())).thenReturn(lstMbsTrans.get(0));
        Mockito.doReturn(transformationObject).when(((Transformer) transactionRequestPOTransformer)).transform(any());
        when(productService.getByProductId(any())).thenReturn(prodPO);
        when(profileEntitlementService.getProfile(any())).thenReturn(profileEntitlementPO);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        List<String> stateType = new ArrayList<>();
        stateType.add("LENDER_OPEN");
        stateType.add("LENDER_ACCEPTED");
        stateType.add("LENDER_REJECTED");
        stateType.add("TRADER_PRICED");
        stateType.add("TRADER_PASSED");
        Optional<String> transReqid = Optional.of("");
        String traderId = "";
        List<TransactionRequestPO> lstTransPOResult = transactionRequestService.getMBSTransReq(traderId, transReqid,
                stateType, MBSRoleType.TRADER);
        assertNotNull(lstTransPOResult);
    }

    /**
     * Purpose: This method test call to get the data from gemfire
     *
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testGetMBSTransLender() throws Exception {
        TransactionRequestPOTransformer transactionRequestPOTransformer = Mockito
                .spy(TransactionRequestPOTransformer.class);
        transactionRequestService = new TransactionRequestService(transactionRequestTransformer,
                transactionRequestPOValidator, transactionRequestValidator, transactionPersister,
                transactionRequestPOTransformer, transactionRequestEnrichment,transactionRequestMessagePublisher);
        MockitoAnnotations.initMocks(this);
        when(tradeServiceProperties.getSourceSystem()).thenReturn("MBSP");
        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
        doNothing().when(transactionPersister).clearAll();
        doNothing().when((Persister) transactionPersister).persist(any());
        doNothing().when(transactionRequestPOValidator).validate(any());
        doNothing().when(transactionRequestValidator).validate(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        createData();
        lstMbsTrans.get(0).setTransReqNumber("17I00509");
        when(((Persister) transactionPersister).getBaseDao()).thenReturn(mbsTransactionDao);
        when(mbsTransactionDao.getAll()).thenReturn(lstMbsTrans);
        when(mbsTransactionDao.getTransReqStateType(any(), any(), any(), any())).thenReturn(lstMbsTrans);
        when(mbsTransactionDao.getTransReqByTransReqNumber(any())).thenReturn(lstMbsTrans.get(0));
        Mockito.doReturn(transformationObject).when(((Transformer) transactionRequestPOTransformer)).transform(any());
        when(productService.getByProductId(any())).thenReturn(prodPO);
        when(profileEntitlementService.getProfile(any())).thenReturn(profileEntitlementPO);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        List<String> stateType = new ArrayList<>();
        stateType.add("LENDER_OPEN");
        stateType.add("LENDER_ACCEPTED");
        stateType.add("LENDER_REJECTED");
        stateType.add("TRADER_PRICED");
        stateType.add("TRADER_PASSED");
        Optional<String> transReqid = Optional.of("17I00509");
        String lenderId = "testLender";
        List<TransactionRequestPO> lstTransPOResult = transactionRequestService.getMBSTransReq(lenderId, transReqid,
                stateType, MBSRoleType.LENDER);
        assertNotNull(lstTransPOResult);
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testGetMBSTransTsp() throws Exception {
        TransactionRequestPOTransformer transactionRequestPOTransformer = Mockito
                .spy(TransactionRequestPOTransformer.class);
        transactionRequestService = new TransactionRequestService(transactionRequestTransformer,
                transactionRequestPOValidator, transactionRequestValidator, transactionPersister,
                transactionRequestPOTransformer, transactionRequestEnrichment,transactionRequestMessagePublisher);
        MockitoAnnotations.initMocks(this);
        when(tradeServiceProperties.getSourceSystem()).thenReturn("MBSP");
        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
        doNothing().when(transactionPersister).clearAll();
        doNothing().when((Persister) transactionPersister).persist(any());
        doNothing().when(transactionRequestPOValidator).validate(any());
        doNothing().when(transactionRequestValidator).validate(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        createData();
        lstMbsTrans.get(0).setTransReqNumber("17I00509");
        ((TransactionRequestPO) transformationObject.getSourcePojo()).setOboLenderSellerServicerNumber("121212");
        profileEntitlementPO.setTspUser(true);
        List<TspPartyLenderPO> tspLenders = new ArrayList<>();
        TspPartyLenderPO tspLender = new TspPartyLenderPO();
        tspLender.setName("TEST COMPANY");
        tspLender.setShortName("TC");
        tspLender.setLenderSellerServicerNumber("121212");
        tspLenders.add(tspLender);
        profileEntitlementPO.setTspLenders(tspLenders);
        when(((Persister) transactionPersister).getBaseDao()).thenReturn(mbsTransactionDao);
        when(mbsTransactionDao.getAll()).thenReturn(lstMbsTrans);
        when(mbsTransactionDao.getTransReqStateType(any(), any(), any(), any())).thenReturn(lstMbsTrans);
        when(mbsTransactionDao.getTransReqByTransReqNumber(any())).thenReturn(lstMbsTrans.get(0));
        Mockito.doReturn(transformationObject).when(((Transformer) transactionRequestPOTransformer)).transform(any());
        when(productService.getByProductId(any())).thenReturn(prodPO);
        when(profileEntitlementService.getProfile(any())).thenReturn(profileEntitlementPO);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        List<String> stateType = new ArrayList<>();
        stateType.add("LENDER_OPEN");
        stateType.add("LENDER_ACCEPTED");
        stateType.add("LENDER_REJECTED");
        stateType.add("TRADER_PRICED");
        stateType.add("TRADER_PASSED");
        Optional<String> transReqid = Optional.of("17I00509");
        String lenderId = "testLender";
        List<TransactionRequestPO> lstTransPOResult = transactionRequestService.getMBSTransReq(lenderId, transReqid,
                stateType, MBSRoleType.TSP);
        assertNotNull(lstTransPOResult);
    }

    /**
     * Purpose: This method test call to get the data from gemfire
     *Rent
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testGetMBSTransLenderNoTransId() throws Exception {
        TransactionRequestPOTransformer transactionRequestPOTransformer = Mockito
                .spy(TransactionRequestPOTransformer.class);
        transactionRequestService = new TransactionRequestService(transactionRequestTransformer,
                transactionRequestPOValidator, transactionRequestValidator, transactionPersister,
                transactionRequestPOTransformer, transactionRequestEnrichment, transactionRequestMessagePublisher);
        MockitoAnnotations.initMocks(this);
        when(tradeServiceProperties.getSourceSystem()).thenReturn("MBSP");
        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
        doNothing().when(transactionPersister).clearAll();
        doNothing().when((Persister) transactionPersister).persist(any());
        doNothing().when(transactionRequestPOValidator).validate(any());
        doNothing().when(transactionRequestValidator).validate(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        createData();
        lstMbsTrans.get(0).setTransReqNumber("17I00509");
        when(((Persister) transactionPersister).getBaseDao()).thenReturn(mbsTransactionDao);
        when(mbsTransactionDao.getAll()).thenReturn(lstMbsTrans);
        when(mbsTransactionDao.getTransReqStateType(any(), any(), any(), any())).thenReturn(lstMbsTrans);
        when(mbsTransactionDao.getTransReqByTransReqNumber(any())).thenReturn(lstMbsTrans.get(0));
        Mockito.doReturn(transformationObject).when(((Transformer) transactionRequestPOTransformer)).transform(any());
        when(productService.getByProductId(any())).thenReturn(prodPO);
        when(profileEntitlementService.getProfile(any())).thenReturn(profileEntitlementPO);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        List<String> stateType = new ArrayList<>();
        stateType.add("LENDER_OPEN");
        stateType.add("LENDER_ACCEPTED");
        stateType.add("LENDER_REJECTED");
        stateType.add("TRADER_PRICED");
        stateType.add("TRADER_PASSED");
        Optional<String> transReqid = Optional.of("");
        String lenderId = "testLender";
        List<TransactionRequestPO> lstTransPOResult = transactionRequestService.getMBSTransReq(lenderId, transReqid,
                stateType, MBSRoleType.LENDER);
        assertNotNull(lstTransPOResult);
    }

    /**
     * Purpose: This method test call to get the data from gemfire
     *
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void getMBSTransReqHistorySortedLender() throws Exception {
        TransactionRequestPOTransformer transactionRequestPOTransformer = Mockito
                .spy(TransactionRequestPOTransformer.class);
        transactionRequestService = new TransactionRequestService(transactionRequestTransformer,
                transactionRequestPOValidator, transactionRequestValidator, transactionPersister,
                transactionRequestPOTransformer, transactionRequestEnrichment, transactionRequestMessagePublisher);
        MockitoAnnotations.initMocks(this);
        when(tradeServiceProperties.getSourceSystem()).thenReturn("MBSP");
        when(mbspProperties.getTransHistActiveDays()).thenReturn("-90");
        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
        doNothing().when(transactionPersister).clearAll();
        doNothing().when((Persister) transactionPersister).persist(any());
        doNothing().when(transactionRequestPOValidator).validate(any());
        doNothing().when(transactionRequestValidator).validate(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        createData();
        lstMbsTrans.get(0).setTransReqNumber("17I00509");
        // lstMbsTrans.get(0).setTradeBuySellType();
        when(((Persister) transactionPersister).getBaseDao()).thenReturn(mbsTransactionDao);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        when(mbsTransactionDao.getTransReqStateType(any(), any(), any(), any())).thenReturn(lstMbsTrans);
        Mockito.doReturn(transformationObject).when(((Transformer) transactionRequestPOTransformer)).transform(any());
        when(productService.getByProductId(any())).thenReturn(prodPO);
        List<StateType> lstStateType = new ArrayList<StateType>();
        lstStateType.add(StateType.LENDER_OPEN);
        String sellerServiceNumber = "121212";
        String sortBy = "tradeBuySellType";
        String sortOrder = SortBy.asc.getName();
        Integer pageIndex = 1;
        Integer pageSize = 100;
        // Map<String, List<String>> filters, Page page, Sort sort, Integer
        // limit
        when(mbsQueryFunctionDao.getTransactionHistoryDetails(any(), any(), any(), anyInt())).thenReturn(lstMbsTrans);
        List<TransactionRequestPO> lstTransPOResult = transactionRequestService.getMBSTransReqHistory(
                MBSRoleType.LENDER, lstStateType, sellerServiceNumber, sortBy, sortOrder, pageIndex, pageSize);
        assertNotNull(lstTransPOResult);
    }
    
    /**
     * Purpose: This method test call to get the data from gemfire
     *
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void getMBSTransReqHistorySortedTSP() throws Exception {
        TransactionRequestPOTransformer transactionRequestPOTransformer = Mockito
                .spy(TransactionRequestPOTransformer.class);
        transactionRequestService = new TransactionRequestService(transactionRequestTransformer,
                transactionRequestPOValidator, transactionRequestValidator, transactionPersister,
                transactionRequestPOTransformer, transactionRequestEnrichment, transactionRequestMessagePublisher);
        MockitoAnnotations.initMocks(this);
        when(tradeServiceProperties.getSourceSystem()).thenReturn("MBSP");
        when(mbspProperties.getTransHistActiveDays()).thenReturn("-90");
        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
        doNothing().when(transactionPersister).clearAll();
        doNothing().when((Persister) transactionPersister).persist(any());
        doNothing().when(transactionRequestPOValidator).validate(any());
        doNothing().when(transactionRequestValidator).validate(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        createData();
        lstMbsTrans.get(0).setTransReqNumber("17I00509");
        // lstMbsTrans.get(0).setTradeBuySellType();
        when(((Persister) transactionPersister).getBaseDao()).thenReturn(mbsTransactionDao);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        when(mbsTransactionDao.getTransReqStateType(any(), any(), any(), any())).thenReturn(lstMbsTrans);
        Mockito.doReturn(transformationObject).when(((Transformer) transactionRequestPOTransformer)).transform(any());
        when(productService.getByProductId(any())).thenReturn(prodPO);
        List<StateType> lstStateType = new ArrayList<StateType>();
        lstStateType.add(StateType.LENDER_OPEN);
        String sellerServiceNumber = "121212";
        String sortBy = "tradeBuySellType";
        String sortOrder = SortBy.asc.getName();
        Integer pageIndex = 1;
        Integer pageSize = 100;
        // Map<String, List<String>> filters, Page page, Sort sort, Integer
        // limit
        when(mbsQueryFunctionDao.getTransactionHistoryDetails(any(), any(), any(), anyInt())).thenReturn(lstMbsTrans);
        List<TransactionRequestPO> lstTransPOResult = transactionRequestService.getMBSTransReqHistory(
                MBSRoleType.TSP, lstStateType, sellerServiceNumber, sortBy, sortOrder, pageIndex, pageSize);
        assertNotNull(lstTransPOResult);
    }
    
    @SuppressWarnings("rawtypes")
    @Test(expected=MBSBaseException.class)
    public void getMBSTransReqHistorySortedLender_ExceptionWithStateType() throws Exception {
        TransactionRequestPOTransformer transactionRequestPOTransformer = Mockito
                .spy(TransactionRequestPOTransformer.class);
        transactionRequestService = new TransactionRequestService(transactionRequestTransformer,
                transactionRequestPOValidator, transactionRequestValidator, transactionPersister,
                transactionRequestPOTransformer, transactionRequestEnrichment, transactionRequestMessagePublisher);
        MockitoAnnotations.initMocks(this);
        when(tradeServiceProperties.getSourceSystem()).thenReturn("MBSP");
        when(mbspProperties.getTransHistActiveDays()).thenReturn("-90");
        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
        doNothing().when(transactionPersister).clearAll();
        doNothing().when((Persister) transactionPersister).persist(any());
        doNothing().when(transactionRequestPOValidator).validate(any());
        doNothing().when(transactionRequestValidator).validate(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        createData();
        String sellerServiceNumber = "121212";
        lstMbsTrans.get(0).setTransReqNumber("17I00509");
        // lstMbsTrans.get(0).setTradeBuySellType();
        when(((Persister) transactionPersister).getBaseDao()).thenReturn(mbsTransactionDao);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        when(mbsTransactionDao.getTransReqStateType(any(), any(), any(), any())).thenReturn(lstMbsTrans);
        Mockito.doReturn(transformationObject).when(((Transformer) transactionRequestPOTransformer)).transform(any());
        when(productService.getByProductId(any())).thenReturn(prodPO);
        String sortBy = "tradeBuySellType";
        String sortOrder = SortBy.asc.getName();
        Integer pageIndex = 1;
        Integer pageSize = 100;
        // Map<String, List<String>> filters, Page page, Sort sort, Integer
        // limit
        when(mbsQueryFunctionDao.getTransactionHistoryDetails(any(), any(), any(), anyInt())).thenReturn(lstMbsTrans);
        List<TransactionRequestPO> lstTransPOResult = transactionRequestService.getMBSTransReqHistory(
                MBSRoleType.LENDER, null, sellerServiceNumber, sortBy, sortOrder, pageIndex, pageSize);
        assertNotNull(lstTransPOResult);
    }

    /**
     * Purpose: This method test call to get the data from gemfire
     *
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void getMBSTransReqHistorySortedTrader() throws Exception {
        TransactionRequestPOTransformer transactionRequestPOTransformer = Mockito
                .spy(TransactionRequestPOTransformer.class);
        transactionRequestService = new TransactionRequestService(transactionRequestTransformer,
                transactionRequestPOValidator, transactionRequestValidator, transactionPersister,
                transactionRequestPOTransformer, transactionRequestEnrichment, transactionRequestMessagePublisher);
        MockitoAnnotations.initMocks(this);
        when(tradeServiceProperties.getSourceSystem()).thenReturn("MBSP");
        when(mbspProperties.getTransHistActiveDays()).thenReturn("-90");
        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
        doNothing().when(transactionPersister).clearAll();
        doNothing().when((Persister) transactionPersister).persist(any());
        doNothing().when(transactionRequestPOValidator).validate(any());
        doNothing().when(transactionRequestValidator).validate(any());
        doNothing().when(transactionRequestMessagePublisher).publish(any());
        createData();
        String sellerServiceNumber = null;
        lstMbsTrans.get(0).setTransReqNumber("17I00509");
        when(((Persister) transactionPersister).getBaseDao()).thenReturn(mbsTransactionDao);
        when(mbsMarketIndicativePriceDao.getById(any())).thenReturn(mbsMarketIndicativePrice);
        when(mbsTransactionDao.getTransReqStateType(any(), any(), any(), any())).thenReturn(lstMbsTrans);
        Mockito.doReturn(transformationObject).when(((Transformer) transactionRequestPOTransformer)).transform(any());
        when(productService.getByProductId(any())).thenReturn(prodPO);
        List<StateType> lstStateType = new ArrayList<StateType>();
        lstStateType.add(StateType.LENDER_OPEN);
        String sortBy = "tradeBuySellType";
        String sortOrder = SortBy.asc.getName();
        Integer pageIndex = 1;
        Integer pageSize = 100;
        // Map<String, List<String>> filters, Page page, Sort sort, Integer
        // limit
        when(mbsQueryFunctionDao.getTransactionHistoryDetails(any(), any(), any(), anyInt())).thenReturn(lstMbsTrans);
        List<TransactionRequestPO> lstTransPOResult = transactionRequestService.getMBSTransReqHistory(
                MBSRoleType.TRADER, lstStateType, sellerServiceNumber, sortBy, sortOrder, pageIndex, pageSize);
        assertNotNull(lstTransPOResult);
    }

    /**
     * Method to create data for the test cases
     */
    public void createData() {

        transactionRequestPO = new TransactionRequestPO();
        prodPO = new ProductPO();
        lenderLst = new ArrayList<String>();
        lenderLst.add("testlocalLenderUser");
        productId = new ProductIdPO();
        prodPOLst = new ArrayList<ProductPO>();
        productId.setIdentifier(new Long(1));
        productId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
        productId.setType(PRODUCT_TYPE.MBS);
        prodPO.setProductId(productId);
        prodPOLst.add(prodPO);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount("900000");
        transactionRequestPO.setTradeBuySellType("SELL");
        transactionRequestPO.setTradeCouponRate("2.0");
        transactionRequestPO.setTradeSettlementDate("2017-10-12");
        transactionRequestPO.setLenderId("testLender");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setPricePercentTicksText("101");
        transactionRequestPO.setPricePercentHandleText("101");
        productPricingPORequest = new ArrayList<>();
        productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("BUY");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        lstMbsTrans = new ArrayList<MBSTransactionRequest>();
        MBSTransactionRequest mbsTransactionRequest = new MBSTransactionRequest();
        ProductId prodId = new ProductId();
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
        mbsTransactionRequest.setSourceSystem("MBSP");
        mbsTransactionRequest.setPricePercent(new BigDecimal(104.000000000).setScale(9).round(MathContext.DECIMAL64));
        lstMbsTrans.add(mbsTransactionRequest);
        stateTypeLst = new ArrayList<>();
        stateTypeLst.add(StateType.LENDER_OPEN.toString());
        transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setTargetPojo(mbsTransactionRequest);
        
        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementPO.setFirstName("testLenderFirstName");
        profileEntitlementPO.setUserName("testLender");
        profileEntitlementPO.setLastName("testLenderLastName");
        profileEntitlementPO.setDealerOrgId("Full Name");
        profileEntitlementPO.setPartyShortName("Party short full Name");
        createStreamPricingData();

    }
    
    public void createStreamPricingData(){
        mbsMarketIndicativePrice = new MBSMarketIndicativePrice();
        mbsMarketIndicativePrice.setAskPricePercent(new BigDecimal("101.25"));
        mbsMarketIndicativePrice.setAskPriceText("101-101");
        mbsMarketIndicativePrice.setBidPricePercent(new BigDecimal("99.25"));
        mbsMarketIndicativePrice.setBidPriceText("99-101");
        
    }

    /**
     * Method to create data for the test cases
     */
    public void createUpdateData() {
        transactionRequestPO = new TransactionRequestPO();
        prodPO = new ProductPO();
        productId = new ProductIdPO();
        prodPOLst = new ArrayList<ProductPO>();
        productId.setIdentifier(new Long(1));
        productId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
        productId.setType(PRODUCT_TYPE.MBS);
        prodPO.setProductId(productId);
        prodPOLst.add(prodPO);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount("900000");
        transactionRequestPO.setTradeBuySellType("SELL");
        transactionRequestPO.setTradeCouponRate("2.0");
        transactionRequestPO.setTradeSettlementDate("2017-11-13");
        transactionRequestPO.setLenderId("testLender");
        transactionRequestPO.setStateType(StateType.TRADER_PRICED);
        transactionRequestPO.setPricePercentTicksText("101");
        transactionRequestPO.setPricePercentHandleText("101");
        transactionRequestPO.setTraderId("AC003");

        lstMbsTrans = new ArrayList<MBSTransactionRequest>();
        MBSTransactionRequest mbsTrans = new MBSTransactionRequest();
        mbsTrans.setTradeAmount(new BigDecimal(10101010));
        mbsTrans.setTradeBuySellType("SELL");
        mbsTrans.setTradeCouponRate(new BigDecimal(10.1));
        mbsTrans.setTradeSettlementDate(new Date());
        mbsTrans.setSubmissionDate(new Date());
        mbsTrans.setStateType(StateType.LENDER_ACCEPTED.toString());
        mbsTrans.setCounterpartyTraderIdentifier("testLender");
        mbsTrans.setSourceSystem("MBSP");
        lstMbsTrans.add(mbsTrans);
        stateTypeLst = new ArrayList<>();
        stateTypeLst.add(StateType.LENDER_OPEN.toString());
        transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setTargetPojo(mbsTrans);

        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementPO.setFirstName("testLenderFirstName");
        profileEntitlementPO.setUserName("testLender");
        profileEntitlementPO.setLastName("testLenderLastName");
        profileEntitlementPO.setDealerOrgId("Full Name");
        profileEntitlementPO.setPartyShortName("Party short full Name");
        profileEntitlementPO.setBrsUserName("Brs User Name");
        createStreamPricingData();

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
