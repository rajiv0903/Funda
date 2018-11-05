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


package com.fanniemae.mbsportal.validator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.api.validator.TransactionRequestPOValidator;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.constants.TradeBuySell;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.service.MBSExceptionService;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessWarning;

/**
 * Class Name: TransactionRequestPOValidatorTest Purpose : This class handles
 * the test case for the TransactionRequestPOValidator class
 * 
 * @author g8upjv
 * 
 */

/**
 * 
 * @author g8upjv
 */

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 16, 2018
 * @Time 2:29:50 PM
 * 	com.fanniemae.mbsportal.api.validator
 * 	TransactionRequestPOValidatorTest.java
 * @Description: Update Existing Test Cases for - CMMBSSTA01-1022: (Tech)
 *               Maintain versions for transaction request
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionRequestPOValidatorTest {
	

    @SuppressWarnings("rawtypes")
    private TransactionRequestPOValidator transactionRequestPOValidator = new TransactionRequestPOValidator();
    private TransactionRequestPO transactionRequestPO;
    
    private String TRADER_MIN_AMOUNT = "25000";
    private String TRADER_MAX_AMOUNT = "250000000";
    private String TRADER_AMOUNT_NOT_INRANGE = "24000";
    
    private String TRADE_HANDLE_MDEFAULT_VALUE = "100";
    private String TRADE_HANDLE_MIN_VALUE = "90";
    private String TRADE_HANDLE_MAX_VALUE = "115";
    
    private String TRADE_TICK_MIN_VALUE = "00";
    private String TRADE_TICK_MAX_VALUE = "31";
    
    private String TRADE_EIGHTS_MAX_VALUE = "7";
    private String TRADE_EIGHTS_MIN_VALUE = "0";
    private String EIGHTS_MIDDLE_VALUE_SIGN ="+";
    
    private String TEST_TRADER_ID = "testtrader";
    
    //TODO:Validator mock class, which needs to be used going forward for all test cases. For now only few tests are using this.
    @InjectMocks
    TransactionRequestPOValidator transactionRequestPOValidatorMock;
    
    @Mock
    MBSExceptionService mbsExceptionService;

    
    public ExceptionLookupPO createData() {
        
        ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        exceptionLookupPO.setErrorCategory("API");
        exceptionLookupPO.setErrorCode("TRAN_5001");
        exceptionLookupPO.setMessageType("DISP_ERROR");
        exceptionLookupPO.setErrorMessage("Test Message");
        return exceptionLookupPO;
    }

    
    /**
     * Purpose: This method tests the call to validation for state type with
     * Correct status value
     *
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @Test(expected=MBSBaseException.class)
    public void validate_Lender_Open_Trader_With_Empty_Transaction_ID_Failure() throws Exception {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setLenderId("987654321");
        transactionRequestPO.setTradeAmount(TRADER_MIN_AMOUNT);
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.TRADER);
        Map<String, ExceptionLookupPO> exceptionLookupPOMap = new HashMap<String, ExceptionLookupPO>(); 
        exceptionLookupPOMap.put("TRAN_5001", createData());
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.EXCEPTION_MESSAGES, exceptionLookupPOMap);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getStateType().toString().equals("LENDER_OPEN"));
        
    }
    
    /**
     * Purpose: This method tests the call to validation for state type with
     * Correct status value
     *
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @Test(expected=MBSBaseException.class)
    public void validate_Lender_Open_Lender_With_Transaction_ID_Failure() throws Exception {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("1234");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setLenderId("987654321");
        transactionRequestPO.setTradeAmount(TRADER_MAX_AMOUNT);
        transactionRequestPO.setActiveVersion(1L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getStateType().toString().equals("LENDER_OPEN"));
        
    }
    
    @Test(expected=MBSBaseException.class)
    public void validate_Lender_Open_Tsp_With_Transaction_ID_Failure() throws Exception {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("1234");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setLenderId("987654321");
        transactionRequestPO.setTradeAmount(TRADER_MAX_AMOUNT);
        transactionRequestPO.setActiveVersion(1L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.TSP);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getStateType().toString().equals("LENDER_OPEN"));
        
    }
    
    /**
     * Purpose: This method tests the call to validation for state type with
     * trans req id
     *
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @Test(expected=MBSBaseException.class)
    public void validate_Lender_Open_Lender_No_Settlement_date_Failure() throws Exception {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setLenderId("987654321");
        transactionRequestPO.setTradeAmount(TRADER_MIN_AMOUNT);
        transactionRequestPO.setActiveVersion(1L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
                transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getStateType().toString().equals("LENDER_OPEN"));
    }
    
    @Test(expected=MBSBaseException.class)
    public void validate_Lender_Open_Tsp_No_Settlement_date_Failure() throws Exception {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setLenderId("987654321");
        transactionRequestPO.setTradeAmount(TRADER_MIN_AMOUNT);
        transactionRequestPO.setActiveVersion(1L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.TSP);
                transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getStateType().toString().equals("LENDER_OPEN"));
    }

    /**
     * Purpose: This method tests the call to validation for state type with
     * trans req id
     *
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @Test(expected=MBSBaseException.class)
    public void validate_Lender_Open_Lender_No_Product_Failure() throws Exception {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setTradeSettlementDate("2020-06-13");
        transactionRequestPO.setLenderId("987654321");
        transactionRequestPO.setTradeAmount(TRADER_MIN_AMOUNT);
        transactionRequestPO.setActiveVersion(1L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
		transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getStateType().toString().equals("LENDER_OPEN"));
    }
    
    @Test(expected=MBSBaseException.class)
    public void validate_Lender_Open_Tsp_No_Product_Failure() throws Exception {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setTradeSettlementDate("2020-06-13");
        transactionRequestPO.setLenderId("987654321");
        transactionRequestPO.setTradeAmount(TRADER_MIN_AMOUNT);
        transactionRequestPO.setActiveVersion(1L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.TSP);
                transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getStateType().toString().equals("LENDER_OPEN"));
    }
    
    /**
     * Purpose: This method tests the call to validation with no lender id
     *
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @Test(expected=MBSBaseException.class)
    public void testValidateTransformerRequestPONoStateType() throws Exception {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setLenderId("12345");
        transactionRequestPO.setTradeSettlementDate("2020-06-13");
        transactionRequestPO.setActiveVersion(1L);
        ProductPO prodPO = new ProductPO();
        ProductIdPO prodIdPo = new ProductIdPO();
        prodIdPo.setIdentifier(new Long(1));
        prodPO.setProductId(prodIdPo);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount(TRADER_MIN_AMOUNT);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
		transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getStateType().toString().equals("LENDER_OPEN"));
    }
    
    @Test(expected=MBSBaseException.class)
    public void testValidateTransformerRequestPOTspNoStateType() throws Exception {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setLenderId("12345");
        transactionRequestPO.setTradeSettlementDate("2020-06-13");
        transactionRequestPO.setActiveVersion(1L);
        ProductPO prodPO = new ProductPO();
        ProductIdPO prodIdPo = new ProductIdPO();
        prodIdPo.setIdentifier(new Long(1));
        prodPO.setProductId(prodIdPo);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount(TRADER_MIN_AMOUNT);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.TSP);
                transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getStateType().toString().equals("LENDER_OPEN"));
    }
    
    @Test(expected=MBSBaseException.class)
    public void testValidateTransformerRequestPONoOboParams() throws Exception {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setLenderId("12345");
        transactionRequestPO.setTradeSettlementDate("2020-06-13");
        transactionRequestPO.setTradeBuySellType("SELL");
        transactionRequestPO.setActiveVersion(1L);
        ProductPO prodPO = new ProductPO();
        ProductIdPO prodIdPo = new ProductIdPO();
        prodIdPo.setIdentifier(new Long(1));
        prodPO.setProductId(prodIdPo);
        transactionRequestPO.setProduct(prodPO);
        transactionRequestPO.setTradeAmount(TRADER_MIN_AMOUNT);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.TSP);
                transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getStateType().toString().equals("LENDER_OPEN"));
    }

    /**
     * Purpose: This method tests the call to validation for invalid state type
     *
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @Test(expected=MBSBaseException.class)
    public void testValidateTransformerRequestPOInvalidStateType() throws Exception {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("12345");
        transactionRequestPO.setStateType(StateType.LENDER_ACCEPTED);
        transactionRequestPO.setLenderId("");
        transactionRequestPO.setActiveVersion(3L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
		transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getStateType().toString().equals("LENDER_OPEN"));
    }
    
    /**
     * No trader id for Trader Passed
     * @throws MBSBaseException
     */
    @SuppressWarnings("unused")
    @Test(expected = MBSBaseException.class)
    public void transactionRequestPOValidatorNoTraderIdTraderPassed() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("12345");
        transactionRequestPO.setStateType(StateType.TRADER_PASSED);
        transactionRequestPO.setTraderId("");
        transactionRequestPO.setActiveVersion(4L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
		transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
    }
    
    /**
     * No trans req id for Trader Passed
     * @throws MBSBaseException
     */
    @SuppressWarnings("unused")
    @Test(expected = MBSBaseException.class)
    public void transactionRequestPOValidatorNoTransIdTraderPassed() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("");
        transactionRequestPO.setStateType(StateType.TRADER_PASSED);
        transactionRequestPO.setTraderId("testTrader");
        transactionRequestPO.setActiveVersion(4L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
		transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
    }
    
    /**
     * No trader id for Trader Rejected
     * @throws MBSBaseException
     */
    @SuppressWarnings("unused")
    @Test(expected = MBSBaseException.class)
    public void transactionRequestPOValidatorNoTraderIdTraderRejected() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("12345");
        transactionRequestPO.setStateType(StateType.TRADER_REJECTED);
        transactionRequestPO.setTraderId("");
        transactionRequestPO.setActiveVersion(4L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
		transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
    }
    
    /**
     * No trans req id for Trader Rejected
     * @throws MBSBaseException
     */
    @SuppressWarnings("unused")
    @Test(expected = MBSBaseException.class)
    public void transactionRequestPOValidatorNoTransIdTraderRejected() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("");
        transactionRequestPO.setStateType(StateType.TRADER_REJECTED);
        transactionRequestPO.setTraderId("testTrader");
        transactionRequestPO.setActiveVersion(4L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
		transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
    }

    @SuppressWarnings("unused")
    @Test
    public void TransactionRequestPOValidatorForTicksAndHandleValid() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("12345");
        transactionRequestPO.setStateType(StateType.TRADER_PRICED);
        transactionRequestPO.setLenderId("1234");
        transactionRequestPO.setPricePercentHandleText("100");
        transactionRequestPO.setPricePercentTicksText("1");
        transactionRequestPO.setTraderId("testTrader");
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
		transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getPricePercentHandleText().equals("100"));
        assertTrue(transactionRequestPO.getPricePercentTicksText().equals("1"));
    }
    
    @SuppressWarnings("unused")
    @Test(expected = MBSBaseException.class)
    @Ignore("This scenario is no longer valid")
    public void TransactionRequestPOValidatorNoTransId() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("");
        transactionRequestPO.setStateType(StateType.TRADER_PRICED);
        transactionRequestPO.setLenderId("1234");
        transactionRequestPO.setPricePercentHandleText("100");
        transactionRequestPO.setPricePercentTicksText("1");
        transactionRequestPO.setTraderId("testTrader");
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
		transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getPricePercentHandleText().equals("100"));
        assertTrue(transactionRequestPO.getPricePercentTicksText().equals("1"));
    }
    
    @SuppressWarnings("unused")
    @Test(expected = MBSBaseException.class)
    public void TransactionRequestPOValidatorNoTraderId() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("12345");
        transactionRequestPO.setStateType(StateType.TRADER_PRICED);
        transactionRequestPO.setLenderId("1234");
        transactionRequestPO.setPricePercentHandleText("100");
        transactionRequestPO.setPricePercentTicksText("1");
        transactionRequestPO.setTraderId("");
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
		transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getPricePercentHandleText().equals("100"));
        assertTrue(transactionRequestPO.getPricePercentTicksText().equals("1"));
    }

    @SuppressWarnings("unused")
    @Test(expected = MBSBaseException.class)
    public void TransactionRequestPOValidatorForTicksAndHandleInvalid() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("12345");
        transactionRequestPO.setStateType(StateType.TRADER_PRICED);
        transactionRequestPO.setLenderId("1234");
        transactionRequestPO.setPricePercentHandleText(null);
        transactionRequestPO.setPricePercentTicksText(null);
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        Map<String, ExceptionLookupPO> exceptionLookupPOMap = new HashMap<String, ExceptionLookupPO>();
        ExceptionLookupPO exceptionLookupPO = createData();
        exceptionLookupPO.setErrorCode("TRAN_5001");
        //exceptionLookupPOMap.put("TRAN_5001", exceptionLookupPO);
        //transformationObject.getTransformationDataMap().put(MBSPServiceConstants.EXCEPTION_MESSAGES, exceptionLookupPOMap);
        Mockito.when(mbsExceptionService.createBusinessExceptionAndLog(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new MBSBusinessException(exceptionLookupPO));
        transactionRequestPOValidatorMock.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
    }

    @SuppressWarnings("unused")
    @Test
    public void TransactionRequestPOValidatorForHandleInvalid() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("12345");
        transactionRequestPO.setStateType(StateType.TRADER_PRICED);
        transactionRequestPO.setLenderId("1234");
        transactionRequestPO.setPricePercentHandleText("100");
        transactionRequestPO.setPricePercentTicksText(null);
        transactionRequestPO.setTraderId("testTrader");
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
		transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getPricePercentHandleText().equals("100"));
        assertTrue(transactionRequestPO.getPricePercentTicksText().equals("00"));
    }

    @SuppressWarnings("unused")
    @Test(expected = MBSBaseException.class)
    public void TransactionRequestPOValidatorForHandleInvalidChar() throws MBSBaseException {
        
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("12345");
        transactionRequestPO.setStateType(StateType.TRADER_PRICED);
        transactionRequestPO.setLenderId("1234");
        transactionRequestPO.setPricePercentHandleText("100");
        transactionRequestPO.setPricePercentTicksText("ABC");
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        //Map<String, ExceptionLookupPO> exceptionLookupPOMap = new HashMap<String, ExceptionLookupPO>();
        ExceptionLookupPO exceptionLookupPO = createData();
        exceptionLookupPO.setErrorCode("TRAN_5003");
        //exceptionLookupPOMap.put("TRAN_5003", exceptionLookupPO);
        //transformationObject.getTransformationDataMap().put(MBSPServiceConstants.EXCEPTION_MESSAGES, exceptionLookupPOMap);
        Mockito.when(mbsExceptionService.createBusinessExceptionAndLog(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new MBSBusinessException(exceptionLookupPO));
        transactionRequestPOValidatorMock.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
    }

    @SuppressWarnings("unused")
    @Test
    public void TransactionRequestPOValidatorForHandleInvalidEmpty() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("12345");
        transactionRequestPO.setStateType(StateType.TRADER_PRICED);
        transactionRequestPO.setLenderId("1234");
        transactionRequestPO.setPricePercentHandleText("100");
        transactionRequestPO.setPricePercentTicksText(" ");
        transactionRequestPO.setTraderId("testTrader");
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
		transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getPricePercentHandleText().equals("100"));
        assertTrue(transactionRequestPO.getPricePercentTicksText().equals("00"));
    }

    @SuppressWarnings("unused")
    @Test(expected = MBSBaseException.class)
    public void TransactionRequestPOValidatorForHandleInvalidHandleEmpty() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("12345");
        transactionRequestPO.setStateType(StateType.TRADER_PRICED);
        transactionRequestPO.setLenderId("1234");
        transactionRequestPO.setPricePercentHandleText("");
        transactionRequestPO.setPricePercentTicksText("123");
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        Map<String, ExceptionLookupPO> exceptionLookupPOMap = new HashMap<String, ExceptionLookupPO>();
        ExceptionLookupPO exceptionLookupPO = createData();
        exceptionLookupPO.setErrorCode("TRAN_5001");
        //exceptionLookupPOMap.put("TRAN_5001", exceptionLookupPO);
        //transformationObject.getTransformationDataMap().put(MBSPServiceConstants.EXCEPTION_MESSAGES, exceptionLookupPOMap);
        
        Mockito.when(mbsExceptionService.createBusinessExceptionAndLog(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new MBSBusinessException(exceptionLookupPO));
        transactionRequestPOValidatorMock.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
    }
    
    @SuppressWarnings("unused")
    @Test(expected = MBSBaseException.class)
    public void TransactionRequestPOValidatorConfirmedNoTraderId() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("12345");
        transactionRequestPO.setStateType(StateType.TRADER_CONFIRMED);
        transactionRequestPO.setLenderId("1234");
        transactionRequestPO.setPricePercentHandleText("100");
        transactionRequestPO.setPricePercentTicksText("1");
        transactionRequestPO.setTraderId("");
        transactionRequestPO.setActiveVersion(4L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
		transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getPricePercentHandleText().equals("100"));
        assertTrue(transactionRequestPO.getPricePercentTicksText().equals("1"));
    }
    
    @SuppressWarnings("unused")
    @Test(expected = MBSBaseException.class)
    public void TransactionRequestPOValidatorPendingNoTransId() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("");
        transactionRequestPO.setStateType(StateType.PENDING_EXECUTION);
        transactionRequestPO.setLenderId("1234");
        transactionRequestPO.setPricePercentHandleText("100");
        transactionRequestPO.setPricePercentTicksText("1");
        transactionRequestPO.setTraderId("testTrader");
        transactionRequestPO.setActiveVersion(5L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
		transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getPricePercentHandleText().equals("100"));
        assertTrue(transactionRequestPO.getPricePercentTicksText().equals("1"));
    }
    
    @SuppressWarnings("unused")
    @Test(expected = MBSBaseException.class)
    public void TransactionRequestPOValidatorLenderRejectedNoTrans() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("");
        transactionRequestPO.setStateType(StateType.LENDER_REJECTED);
        transactionRequestPO.setLenderId("1234");
        transactionRequestPO.setPricePercentHandleText("100");
        transactionRequestPO.setPricePercentTicksText("1");
        transactionRequestPO.setTraderId("testTrader");
        transactionRequestPO.setActiveVersion(3L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
		transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getPricePercentHandleText().equals("100"));
        assertTrue(transactionRequestPO.getPricePercentTicksText().equals("1"));
    }
    
    @SuppressWarnings("unused")
    @Test(expected = MBSBaseException.class)
    public void TransactionRequestPOValidatorLenderRejectedNoLenderId() throws MBSBaseException {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("12345");
        transactionRequestPO.setStateType(StateType.LENDER_REJECTED);
        transactionRequestPO.setLenderId("");
        transactionRequestPO.setPricePercentHandleText("100");
        transactionRequestPO.setPricePercentTicksText("1");
        transactionRequestPO.setTraderId("testTrader");
        transactionRequestPO.setActiveVersion(3L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
		transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getPricePercentHandleText().equals("100"));
        assertTrue(transactionRequestPO.getPricePercentTicksText().equals("1"));
    }
    
    /**
     * 
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @Test
    public void validate_Trade_Min_Amount_Success() throws Exception {
        
        ProductIdPO productIdPO = new ProductIdPO();
        productIdPO.setIdentifier(1L);
        ProductPO productPO= new ProductPO();
        productPO.setProductId(productIdPO);
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setLenderId("987654321");
        transactionRequestPO.setTradeAmount(TRADER_MIN_AMOUNT);
        transactionRequestPO.setTradeBuySellType(TradeBuySell.BUY.toString());
        transactionRequestPO.setProduct(productPO);
        transactionRequestPO.setActiveVersion(1L);
        transactionRequestPO.setTradeSettlementDate("2020-09-12");
        transactionRequestPO.setLenderEntityShortName("TEST-C");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getStateType().toString().equals("LENDER_OPEN"));
        
    }
    
    /**
     * 
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @Test
    public void validate_Trade_Max_Amount_Success() throws Exception {
        
        ProductIdPO productIdPO = new ProductIdPO();
        productIdPO.setIdentifier(1L);
        ProductPO productPO= new ProductPO();
        productPO.setProductId(productIdPO);
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setLenderId("987654321");
        transactionRequestPO.setTradeAmount(TRADER_MAX_AMOUNT);
        transactionRequestPO.setTradeBuySellType(TradeBuySell.BUY.toString());
        transactionRequestPO.setProduct(productPO);
        transactionRequestPO.setActiveVersion(1L);
        transactionRequestPO.setTradeSettlementDate("2020-09-12");
        transactionRequestPO.setLenderEntityShortName("TEST-C");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getStateType().toString().equals("LENDER_OPEN"));
        
    }
    
    /**
     * 
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @Test(expected=MBSBaseException.class)
    public void validate_Trade_Amount_Not_In_Range() throws Exception {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("1234");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setLenderId("987654321");
        transactionRequestPO.setTradeAmount(TRADER_AMOUNT_NOT_INRANGE);
        transactionRequestPO.setActiveVersion(1L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getStateType().toString().equals("LENDER_OPEN"));
        
    }
    
    /**
     * 
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @Test(expected=MBSBaseException.class)
    public void validate_Trade_Amount_Invalid_Negative_Amount() throws Exception {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("1234");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setLenderId("987654321");
        transactionRequestPO.setTradeAmount("-"+TRADER_MIN_AMOUNT);
        transactionRequestPO.setActiveVersion(1L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getStateType().toString().equals("LENDER_OPEN"));
        
    }
    
    /**
     * 
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @Test(expected=MBSBaseException.class)
    public void validate_Trade_Amount_Invalid_Amount_Decimal() throws Exception {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("1234");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setLenderId("987654321");
        transactionRequestPO.setTradeAmount("250000.03");
        transactionRequestPO.setActiveVersion(1L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getStateType().toString().equals("LENDER_OPEN"));
        
    }
    
    @SuppressWarnings("unused")
    @Test(expected=MBSBaseException.class)
    public void validate_Empty_Trade_Amount_Failure() throws Exception {
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId("1234");
        transactionRequestPO.setStateType(StateType.LENDER_OPEN);
        transactionRequestPO.setLenderId("987654321");
        transactionRequestPO.setTradeAmount(null);
        transactionRequestPO.setActiveVersion(1L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPO.getStateType().toString().equals("LENDER_OPEN"));
        
    }
    

    
    @Test
    public void validate_Handle_Max_Value_Success() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentHandleText(TRADE_HANDLE_MAX_VALUE);
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test
    public void validate_Handle_Min_Value_Success() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentHandleText(TRADE_HANDLE_MIN_VALUE);
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test(expected=MBSBaseException.class)
    public void validate_Handle_Empty_Value_Failure() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentHandleText("");
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        Map<String, ExceptionLookupPO> exceptionLookupPOMap = new HashMap<String, ExceptionLookupPO>();
        ExceptionLookupPO exceptionLookupPO = createData();
        exceptionLookupPO.setErrorCode("TRAN_5001");
        //exceptionLookupPOMap.put("TRAN_5001", exceptionLookupPO);
        //transformationObject.getTransformationDataMap().put(MBSPServiceConstants.EXCEPTION_MESSAGES, exceptionLookupPOMap);
        Mockito.when(mbsExceptionService.createBusinessExceptionAndLog(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new MBSBusinessException(exceptionLookupPO));
        transactionRequestPOValidatorMock.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test(expected=MBSBaseException.class)
    public void validate_Handle_More_Than_3_Digit_Value_Failure() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentHandleText("1000");
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        Map<String, ExceptionLookupPO> exceptionLookupPOMap = new HashMap<String, ExceptionLookupPO>();
        ExceptionLookupPO exceptionLookupPO = createData();
        exceptionLookupPO.setErrorCode("TRAN_5002");
        //exceptionLookupPOMap.put("TRAN_5002", exceptionLookupPO);
        //transformationObject.getTransformationDataMap().put(MBSPServiceConstants.EXCEPTION_MESSAGES, exceptionLookupPOMap);
        Mockito.when(mbsExceptionService.createBusinessExceptionAndLog(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new MBSBusinessException(exceptionLookupPO));
        transactionRequestPOValidatorMock.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test(expected=MBSBaseException.class)
    public void validate_Handle_Started_With_Zero_Value_Failure() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentHandleText("095");
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        Map<String, ExceptionLookupPO> exceptionLookupPOMap = new HashMap<String, ExceptionLookupPO>();
        ExceptionLookupPO exceptionLookupPO = createData();
        exceptionLookupPO.setErrorCode("TRAN_5002");
        //exceptionLookupPOMap.put("TRAN_5002", exceptionLookupPO);
        //transformationObject.getTransformationDataMap().put(MBSPServiceConstants.EXCEPTION_MESSAGES, exceptionLookupPOMap);
        Mockito.when(mbsExceptionService.createBusinessExceptionAndLog(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new MBSBusinessException(exceptionLookupPO));
        transactionRequestPOValidatorMock.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test(expected=MBSBaseException.class)
    public void validate_Handle_Less_Than_Min_Value_Failure() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentHandleText("89");
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        Map<String, ExceptionLookupPO> exceptionLookupPOMap = new HashMap<String, ExceptionLookupPO>();
        ExceptionLookupPO exceptionLookupPO = createData();
        exceptionLookupPO.setErrorCode("TRAN_5002");
        //exceptionLookupPOMap.put("TRAN_5002", exceptionLookupPO);
        //transformationObject.getTransformationDataMap().put(MBSPServiceConstants.EXCEPTION_MESSAGES, exceptionLookupPOMap);
        Mockito.when(mbsExceptionService.createBusinessExceptionAndLog(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new MBSBusinessException(exceptionLookupPO));
        transactionRequestPOValidatorMock.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test(expected=MBSBaseException.class)
    public void validate_Handle_Greater_Than_Max_Value_Failure() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentHandleText("116");
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        Map<String, ExceptionLookupPO> exceptionLookupPOMap = new HashMap<String, ExceptionLookupPO>();
        ExceptionLookupPO exceptionLookupPO = createData();
        exceptionLookupPO.setErrorCode("TRAN_5002");
        //exceptionLookupPOMap.put("TRAN_5002", exceptionLookupPO);
        //transformationObject.getTransformationDataMap().put(MBSPServiceConstants.EXCEPTION_MESSAGES, exceptionLookupPOMap);
        Mockito.when(mbsExceptionService.createBusinessExceptionAndLog(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new MBSBusinessException(exceptionLookupPO));
        transactionRequestPOValidatorMock.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test
    public void validate_Tick_Max_Value_Success() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentTicksText(TRADE_TICK_MAX_VALUE);
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test
    public void validate_Tick_Min_Value_Success() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentTicksText(TRADE_TICK_MIN_VALUE);
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test
    public void validate_Tick_Empty_Value_Success() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentTicksText("");
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test(expected=MBSBaseException.class)
    public void validate_Tick_More_Than_3_Digit_Value_Failure() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentTicksText("0061");
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        Map<String, ExceptionLookupPO> exceptionLookupPOMap = new HashMap<String, ExceptionLookupPO>();
        ExceptionLookupPO exceptionLookupPO = createData();
        //exceptionLookupPOMap.put("TRAN_5003", createData());
        //transformationObject.getTransformationDataMap().put(MBSPServiceConstants.EXCEPTION_MESSAGES, exceptionLookupPOMap);
        Mockito.when(mbsExceptionService.createBusinessExceptionAndLog(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new MBSBusinessException(exceptionLookupPO));
        transactionRequestPOValidatorMock.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test(expected=MBSBaseException.class)
    public void validate_Tick_Greater_Than_Max_Value_Failure() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentTicksText("32");
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        Map<String, ExceptionLookupPO> exceptionLookupPOMap = new HashMap<String, ExceptionLookupPO>(); 
        ExceptionLookupPO exceptionLookupPO = createData();
        //exceptionLookupPOMap.put("TRAN_5003", createData());
        //transformationObject.getTransformationDataMap().put(MBSPServiceConstants.EXCEPTION_MESSAGES, exceptionLookupPOMap);
        Mockito.when(mbsExceptionService.createBusinessExceptionAndLog(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new MBSBusinessException(exceptionLookupPO));
        transactionRequestPOValidatorMock.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test
    public void validate_Eights_Max_Value_Success() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentTicksText(TRADE_TICK_MIN_VALUE+TRADE_EIGHTS_MAX_VALUE);
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test
    public void validate_Eights_Min_Value_Success() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentTicksText(TRADE_TICK_MIN_VALUE+TRADE_EIGHTS_MIN_VALUE);
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test(expected=MBSBaseException.class)
    public void validate_Eights_Greater_Than_Min_Value_Failure() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentTicksText(TRADE_TICK_MIN_VALUE+"8");
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        Map<String, ExceptionLookupPO> exceptionLookupPOMap = new HashMap<String, ExceptionLookupPO>();
        ExceptionLookupPO exceptionLookupPO = createData();
        exceptionLookupPO.setErrorCode("TRAN_5003");
        //exceptionLookupPOMap.put("TRAN_5003", exceptionLookupPO);
        //transformationObject.getTransformationDataMap().put(MBSPServiceConstants.EXCEPTION_MESSAGES, exceptionLookupPOMap);
        Mockito.when(mbsExceptionService.createBusinessExceptionAndLog(Mockito.any(), Mockito.any(), Mockito.anyVararg())).thenReturn(new MBSBusinessException(exceptionLookupPO));
        transactionRequestPOValidatorMock.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test
    public void validate_Max_Handle_With_No_Ticks_Success() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentHandleText(TRADE_HANDLE_MAX_VALUE);
        transactionRequestPO.setPricePercentTicksText("");
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test
    public void validate_Max_Handle_With_Zero_Ticks_Success() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentHandleText(TRADE_HANDLE_MAX_VALUE);
        transactionRequestPO.setPricePercentTicksText(TRADE_TICK_MIN_VALUE);
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test
    public void validate_Max_Handle_With_Zero_Ticks_Zero_Eights_Success() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentHandleText(TRADE_HANDLE_MAX_VALUE);
        transactionRequestPO.setPricePercentTicksText(TRADE_TICK_MIN_VALUE+TRADE_EIGHTS_MIN_VALUE);
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
 
    
    @Test(expected=MBSBusinessException.class)
    public void validate_Max_Handle_With_Ticks_Failure() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentHandleText(TRADE_HANDLE_MAX_VALUE);
        transactionRequestPO.setPricePercentTicksText("12");
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        Map<String, ExceptionLookupPO> exceptionLookupPOMap = new HashMap<String, ExceptionLookupPO>(); 
        ExceptionLookupPO exceptionLookupPO = createData();
        //exceptionLookupPOMap.put("TRAN_5003", createData());
        //transformationObject.getTransformationDataMap().put(MBSPServiceConstants.EXCEPTION_MESSAGES, exceptionLookupPOMap);
        Mockito.when(mbsExceptionService.createBusinessExceptionAndLog(Mockito.any(), Mockito.any(), Mockito.anyVararg())).thenReturn(new MBSBusinessException(exceptionLookupPO));
        transactionRequestPOValidatorMock.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test
    public void validate_With_Min_Ticks_Symbolic_Eights_Success() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentHandleText(TRADE_HANDLE_MDEFAULT_VALUE);
        transactionRequestPO.setPricePercentTicksText(TRADE_TICK_MIN_VALUE+EIGHTS_MIDDLE_VALUE_SIGN);
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test
    public void validate_With_Max_Ticks_Symbolic_Eights_Success() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentHandleText(TRADE_HANDLE_MDEFAULT_VALUE);
        transactionRequestPO.setPricePercentTicksText(TRADE_TICK_MAX_VALUE+EIGHTS_MIDDLE_VALUE_SIGN);
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test(expected=MBSBusinessException.class)
    public void validate_Max_Handle_With_Min_Ticks_Symbolic_Eights_Success() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("12345", StateType.TRADER_PRICED, TRADER_MIN_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentHandleText(TRADE_HANDLE_MAX_VALUE);
        transactionRequestPO.setPricePercentTicksText(TRADE_TICK_MIN_VALUE+EIGHTS_MIDDLE_VALUE_SIGN);
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        Map<String, ExceptionLookupPO> exceptionLookupPOMap = new HashMap<String, ExceptionLookupPO>(); 
        ExceptionLookupPO exceptionLookupPO = createData();
        //exceptionLookupPOMap.put("TRAN_5003", createData());
        //transformationObject.getTransformationDataMap().put(MBSPServiceConstants.EXCEPTION_MESSAGES, exceptionLookupPOMap);
        Mockito.when(mbsExceptionService.createBusinessExceptionAndLog(Mockito.any(), Mockito.any(), Mockito.anyVararg())).thenReturn(new MBSBusinessException(exceptionLookupPO));
        transactionRequestPOValidatorMock.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertNotNull(transactionRequestPORet.getPricePercentHandleText());
        assertNotNull(transactionRequestPORet.getPricePercentTicksText());
    }
    
    @Test
    public void validate_Active_Version_Lender_Open_Success() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("", StateType.LENDER_OPEN, TRADER_MAX_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentHandleText("");
        transactionRequestPO.setPricePercentTicksText("");
        transactionRequestPO.setActiveVersion(1L);
        transactionRequestPO.setTradeSettlementDate("2020-09-12");
        transactionRequestPO.setLenderEntityShortName("TEST-C");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPORet.getStateType().toString().equals("LENDER_OPEN"));
    }
    
    @Test(expected=MBSBusinessException.class)
    public void validate_Active_Version_Lender_Open_Failure_Settlement_date() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("", StateType.LENDER_OPEN, TRADER_MAX_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentHandleText("");
        transactionRequestPO.setPricePercentTicksText("");
        transactionRequestPO.setActiveVersion(1L);
        transactionRequestPO.setTradeSettlementDate("");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPORet.getStateType().toString().equals("LENDER_OPEN"));
    }
    
    @Test(expected=MBSBusinessWarning.class)
    public void validate_Active_Version_Greater_Than_One_Lender_Open_Failure() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("", StateType.LENDER_OPEN, TRADER_MAX_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentHandleText("");
        transactionRequestPO.setPricePercentTicksText("");
        transactionRequestPO.setActiveVersion(2L);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPORet.getStateType().toString().equals("LENDER_OPEN"));
    }
    
    @Test(expected=MBSBusinessWarning.class)
    public void validate_Active_Version_Empty_Lender_Open_Failure() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("", StateType.LENDER_OPEN, TRADER_MAX_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setPricePercentHandleText("");
        transactionRequestPO.setPricePercentTicksText("");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transactionRequestPOValidator.validate(transformationObject);
        TransactionRequestPO transactionRequestPORet =(TransactionRequestPO)transformationObject.getSourcePojo();
        assertTrue(transactionRequestPORet.getStateType().toString().equals("LENDER_OPEN"));
    }
    
    @Test(expected=MBSBusinessException.class)
    public void validate_No_State_Type_Failure() throws MBSBaseException {
        transactionRequestPO = createTransactionRequest("", StateType.LENDER_OPEN, TRADER_MAX_AMOUNT, TEST_TRADER_ID);
        transactionRequestPO.setActiveVersion(1L);
        transactionRequestPO.setStateType(null);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(MBSRoleType.LENDER);
        transactionRequestPOValidator.validate(transformationObject);
    }
    
    /**
     * 
     * @param transReqId
     * @param stateType
     * @param tradeAmount
     * @return
     */
    private TransactionRequestPO createTransactionRequest(String transReqId, StateType stateType, String tradeAmount, String traderId) {
        
        ProductIdPO productIdPO = new ProductIdPO();
        productIdPO.setIdentifier(1L);
        ProductPO productPO= new ProductPO();
        productPO.setProductId(productIdPO);
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setTransReqId(transReqId);
        transactionRequestPO.setStateType(stateType);
        transactionRequestPO.setLenderId("987654321");
        transactionRequestPO.setTradeAmount(tradeAmount);
        transactionRequestPO.setTradeBuySellType(TradeBuySell.BUY.toString());
        transactionRequestPO.setProduct(productPO);
        transactionRequestPO.setPricePercentHandleText(TRADE_HANDLE_MDEFAULT_VALUE);
        transactionRequestPO.setPricePercentTicksText("00");
        transactionRequestPO.setTraderId(traderId);
        return transactionRequestPO;
    }
}
