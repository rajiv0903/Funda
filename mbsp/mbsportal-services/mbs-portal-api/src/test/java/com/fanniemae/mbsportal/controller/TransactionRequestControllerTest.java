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

package com.fanniemae.mbsportal.controller;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.fanniemae.mbsportal.api.controller.TransactionRequestController;
import com.fanniemae.mbsportal.api.controller.helper.TransactionRequestControllerHelper;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.api.service.ExceptionLookupService;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;

/**
 * Class Name: TransactionRequestControllerTest Purpose : This class is the test
 * class for the Transaction controller
 * 
 * @author g8upjv
 *
 */
/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Nov 22, 2017
 * @Time 10:53:07 AM
 *      com.fanniemae.mbsportal.api.controller
 *      ProductControllerTest.java
 * @Description: Incorporate the Internal Server Error - Status Code 500
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionRequestControllerTest {

	@InjectMocks
	TransactionRequestController transactionRequestController;

	@Mock
	TransactionRequestControllerHelper transactionRequestControllerHelper;
	
	    @Mock
	    ExceptionLookupService exceptionLookupService;


	TransactionRequestPO transactionRequestPO;

	ProductPO prodPO;

	ProductIdPO productId;

	List<TransactionRequestPO> lstMbsTrans;

	Map<String, String> headerMap;

	ProfileEntitlementPO profileEntitlementPO;

	public void createData() {
		headerMap = new HashMap<String, String>();
		transactionRequestPO = new TransactionRequestPO();
		prodPO = new ProductPO();
		productId = new ProductIdPO();
		productId.setIdentifier(new Long(1));
		productId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
		productId.setType(PRODUCT_TYPE.MBS);
		prodPO.setProductId(productId);
		transactionRequestPO.setProduct(prodPO);
		transactionRequestPO.setTradeAmount("500000");
		transactionRequestPO.setTradeBuySellType("SELL");
		transactionRequestPO.setTradeCouponRate("2.0");
		transactionRequestPO.setTradeSettlementDate("2017-10-12");
		transactionRequestPO.setLenderId("testLender");
		transactionRequestPO.setStateType(StateType.LENDER_OPEN);

		lstMbsTrans = new ArrayList<TransactionRequestPO>();
		TransactionRequestPO mbsTrans = new TransactionRequestPO();
		mbsTrans.setTransReqId("17I" + String.valueOf(new Random().nextInt(90000) + 10000));
		mbsTrans.setTradeAmount("10101010");
		mbsTrans.setTradeBuySellType("SELL");
		mbsTrans.setTradeCouponRate("10.1");
		mbsTrans.setTradeSettlementDate("2018-03-04");
		lstMbsTrans.add(mbsTrans);

		profileEntitlementPO = new ProfileEntitlementPO();
		profileEntitlementPO.setUserName("username");
		profileEntitlementPO.setFirstName("firstName");
		profileEntitlementPO.setLastName("lastName");
	}
	
	public List<ExceptionLookupPO> createExceptionData() {
	        List<ExceptionLookupPO> exceptionLookupPOLst = new ArrayList<>();
	        ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
	        exceptionLookupPO.setErrorCategory("API");
	        exceptionLookupPO.setErrorCode("TRANS_00001");
	        exceptionLookupPO.setMessageType("DISP_ERROR");
	        exceptionLookupPO.setErrorMessage("Test Message");
	        exceptionLookupPOLst.add(exceptionLookupPO);
	        return exceptionLookupPOLst;
	    }

	/**
	 * Purpose: Creates the transactionRequestPO data model
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
	    Mockito.when(exceptionLookupService.getExceptionLookupData(Mockito.any())).thenReturn(createExceptionData());
	}

	/**
	 * Purpose: Test for case when media type is empty
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	@Ignore("Content/Media Type is ignored as of now")
	public void testCreateTransactionRequestNoMediaType() throws MBSBaseException {
		Mockito.when(transactionRequestControllerHelper.createTransactionRequestLender(Mockito.any(), Mockito.any()))
				.thenReturn(transactionRequestPO);
		ResponseEntity<Object> responseObj = transactionRequestController
				.createTransactionRequestLender(transactionRequestPO, headerMap);
		assertTrue(responseObj.getStatusCode().toString().equals("400"));
	}

	/**
	 * Purpose: Test for case when the request body is null
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	public void testCreateTransactionRequestNoTransactionPO() throws MBSBaseException {
		createData();
		Mockito.when(transactionRequestControllerHelper.createTransactionRequestLender(Mockito.any(), Mockito.any()))
				.thenThrow(new MBSBusinessException("Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
		ResponseEntity<Object> responseObj = transactionRequestController.createTransactionRequestLender(null,
				headerMap);
		assertTrue(responseObj.getStatusCode().toString().equals("400"));
	}

	/**
	 * Purpose: Test for case for successful call
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	public void testCreateTransactionRequestException() throws MBSBaseException {
		createData();
		Mockito.when(transactionRequestControllerHelper.createTransactionRequestLender(Mockito.any(), Mockito.any()))
				.thenThrow(new MBSBusinessException("test"));
		ResponseEntity<Object> responseObj = transactionRequestController
				.createTransactionRequestLender(transactionRequestPO, headerMap);
		assertTrue(responseObj.hasBody());
		assertTrue(responseObj.getStatusCode().toString().equals("400"));
	}
	
	/**
	 * Purpose: Test for case for successful call
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	public void testCreateTransactionRequestEmptyProfile() throws MBSBaseException {
		createData();
		Mockito.when(transactionRequestControllerHelper.createTransactionRequestLender(Mockito.any(), Mockito.any()))
				.thenThrow(new MBSBusinessException("test",  MBSExceptionConstants.BUSINESS_EXCEPTION));
		ResponseEntity<Object> responseObj = transactionRequestController
				.createTransactionRequestLender(transactionRequestPO, headerMap);
		assertTrue(responseObj.hasBody());
		assertTrue(responseObj.getStatusCode().toString().equals("400"));
	}
	
	/**
	 * @author Rajiv
	 * @throws MBSBaseException
	 */
	@Test
        public void testCreateTransactionRequestInternalServerError() throws MBSBaseException {
                createData();
                Mockito.when(transactionRequestControllerHelper.createTransactionRequestLender(Mockito.any(), Mockito.any()))
                            .thenThrow(new RuntimeException("Runtine Exception!"));
                ResponseEntity<Object> responseObj = transactionRequestController
                                .createTransactionRequestLender(transactionRequestPO, headerMap);
                assertTrue(responseObj.hasBody());
                assertTrue(responseObj.getStatusCode().toString().equals("500"));
        }
	
	@Test
        public void testCreateTransactionRequestInternalServerErrorExceptionMsgRetrieval() throws MBSBaseException {
                createData();
                Mockito.when(transactionRequestControllerHelper.createTransactionRequestLender(Mockito.any(), Mockito.any()))
                            .thenThrow(new RuntimeException("Runtine Exception!"));
                Mockito.when(exceptionLookupService.getExceptionLookupData(Mockito.any())).thenThrow(new MBSBaseException("test"));
                ResponseEntity<Object> responseObj = transactionRequestController
                                .createTransactionRequestLender(transactionRequestPO, headerMap);
                assertTrue(responseObj.hasBody());
                assertTrue(responseObj.getStatusCode().toString().equals("500"));
        }

	/**
	 * Purpose: Test for case for successful call
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	public void testCreateTransactionRequest() throws MBSBaseException {
		createData();
		Mockito.when(transactionRequestControllerHelper.createTransactionRequestLender(Mockito.any(), Mockito.any()))
				.thenReturn(transactionRequestPO);
		ResponseEntity<Object> responseObj = transactionRequestController
				.createTransactionRequestLender(transactionRequestPO, headerMap);
		assertTrue(responseObj.hasBody());
		assertTrue(responseObj.getStatusCode().toString().equals("200"));
	}

	/**
	 * Purpose: Test for case for successful call to get list of transactions
	 * for lender
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	public void testGetTransactionRequest() throws MBSBaseException {
		createData();
		Mockito.when(
		        transactionRequestControllerHelper.getTransactionRequestsLender(Mockito.any(), Mockito.any()))
				.thenReturn(lstMbsTrans);
		Optional<String> transReqid = Optional.of("");
		ResponseEntity<Object> responseObj = transactionRequestController.getTransactionRequestsLender(transReqid,
				headerMap);
		assertTrue(responseObj.hasBody());
		assertTrue(responseObj.getStatusCode().toString().equals("200"));
	}
	
	/**
	 * Purpose: Test for case for successful call to get list of transactions
	 * for lender
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	public void testGetTransactionRequestTransId() throws MBSBaseException {
		createData();
		Mockito.when(
		        transactionRequestControllerHelper.getTransactionRequestsLender(Mockito.any(), Mockito.any()))
				.thenReturn(lstMbsTrans);
		Optional<String> transReqid = Optional.of("17I00001");
		ResponseEntity<Object> responseObj = transactionRequestController.getTransactionRequestsLender(transReqid,
				headerMap);
		assertTrue(responseObj.hasBody());
		assertTrue(responseObj.getStatusCode().toString().equals("200"));
	}

	/**
	 * Purpose: Test for case for successful call to get list of transactions
	 * for lender
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	public void testGetTransactionRequestEmptyProfile() throws MBSBaseException {
		createData();
		Mockito.when(
		        transactionRequestControllerHelper.getTransactionRequestsLender(Mockito.any(), Mockito.any()))
		                .thenThrow(new MBSBusinessException("test",  MBSExceptionConstants.BUSINESS_EXCEPTION));
		Optional<String> transReqid = Optional.of("");
		ResponseEntity<Object> responseObj = transactionRequestController.getTransactionRequestsLender(transReqid,
				headerMap);
		assertTrue(responseObj.hasBody());
		assertTrue(responseObj.getStatusCode().toString().equals("400"));
	}

	/**
	 * Purpose: Test for case for successful call to get list of transactions
	 * for lender
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	public void testGetTransactionRequestException() throws MBSBaseException {
		createData();
		Mockito.when(
		        transactionRequestControllerHelper.getTransactionRequestsLender(Mockito.any(), Mockito.any()))
				.thenThrow(new MBSBusinessException("test"));
		Optional<String> transReqid = Optional.of("");
		ResponseEntity<Object> responseObj = transactionRequestController.getTransactionRequestsLender(transReqid,
				headerMap);
		assertTrue(responseObj.hasBody());
		assertTrue(responseObj.getStatusCode().toString().equals("400"));
	}
	
	/**
	 * @author Rajiv
	 * @throws MBSBaseException
	 */
	@Test
        public void testGetTransactionRequestInternalServerError() throws MBSBaseException {
                createData();
                Mockito.when(
                        transactionRequestControllerHelper.getTransactionRequestsLender(Mockito.any(), Mockito.any()))
                .thenThrow(new RuntimeException("Runtine Exception!"));
                Optional<String> transReqid = Optional.of("");
                ResponseEntity<Object> responseObj = transactionRequestController.getTransactionRequestsLender(transReqid,
                                headerMap);
                assertTrue(responseObj.hasBody());
                assertTrue(responseObj.getStatusCode().toString().equals("500"));
        }

	/**
	 * Purpose: Test for case when media type is empty to get list of
	 * transaction for lender
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	@Ignore("Content/Media Type is ignored as of now")
	public void testGetTransactionRequestNoMediaType() throws MBSBaseException {
		createData();
		Mockito.when(
		        transactionRequestControllerHelper.getTransactionRequestsLender(Mockito.any(), Mockito.any()))
		                    .thenThrow(new MBSBaseException("test"));
		Optional<String> transReqid = Optional.of("17I00018");
		ResponseEntity<Object> responseObj = transactionRequestController.getTransactionRequestsLender(transReqid,
				headerMap);
		assertTrue(responseObj.getStatusCode().toString().equals("400"));
	}

	/**
	 * Purpose: Test for case for successful call to get list of transactions
	 * for trader
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	public void testGetTransactionRequestTrader() throws MBSBaseException {
		createData();
		Mockito.when(
		        transactionRequestControllerHelper.getTransactionRequestsTrader(Mockito.any()))
				.thenReturn(lstMbsTrans);
		Optional<String> transReqid = Optional.of("");
		ResponseEntity<Object> responseObj = transactionRequestController.getTransactionRequestsTrader(transReqid);
		assertTrue(responseObj.hasBody());
		assertTrue(responseObj.getStatusCode().toString().equals("200"));
	}

	/**
	 * Purpose: Test for case for successful call to get list of transactions
	 * for trader
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	public void testGetTransactionRequestTraderException() throws MBSBaseException {
		createData();
		Mockito.when(
		        transactionRequestControllerHelper.getTransactionRequestsTrader(Mockito.any()))
				.thenThrow(new MBSBusinessException("test"));
		Optional<String> transReqid = Optional.of("");
		ResponseEntity<Object> responseObj = transactionRequestController.getTransactionRequestsTrader(transReqid);
		assertTrue(responseObj.hasBody());
		assertTrue(responseObj.getStatusCode().toString().equals("400"));
	}
	
	/**
	 * @author Rajiv
	 * @throws MBSBaseException
	 */
	@Test
        public void testGetTransactionRequestTraderInternalServerError() throws MBSBaseException {
                createData();
                Mockito.when(
                        transactionRequestControllerHelper.getTransactionRequestsTrader(Mockito.any()))
                                .thenThrow(new RuntimeException("Runtine Exception!"));
                Optional<String> transReqid = Optional.of("");
                ResponseEntity<Object> responseObj = transactionRequestController.getTransactionRequestsTrader(transReqid);
                assertTrue(responseObj.hasBody());
                assertTrue(responseObj.getStatusCode().toString().equals("500"));
        }

	/**
	 * Purpose: Test for case when media type is empty to get list of
	 * transaction for trader
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	@Ignore("Content Type is ignored as of now")
	public void testGetTransactionRequestTraderNoMediaType() throws MBSBaseException {
		Mockito.when(
		        transactionRequestControllerHelper.getTransactionRequestsTrader(Mockito.any()))
		                    .thenThrow(new MBSBaseException("test"));
		Optional<String> transReqid = Optional.of("17I00018");
		ResponseEntity<Object> responseObj = transactionRequestController.getTransactionRequestsTrader(transReqid);
		assertTrue(responseObj.getStatusCode().toString().equals("400"));
	}

	/**
	 * Purpose: Test for case to put/update transaction for trader
	 * 
	 * @throws MBSBaseException
	 */
	@Test
	public void testPutTransactionRequestLender() throws MBSBaseException {
		createData();
		Mockito.when(transactionRequestControllerHelper.submitPriceRequest(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(transactionRequestPO);
		ResponseEntity<Object> responseObjTraderPut = transactionRequestController
				.submitPriceRequest(transactionRequestPO.getTransReqId(), transactionRequestPO, headerMap);
		assertTrue(responseObjTraderPut.getStatusCode().toString().equals("200"));

	}
	
	/**
	 * Purpose: Test for case to put/update transaction for trader
	 * 
	 * @throws MBSBaseException
	 */
	@Test
	public void testPutTransactionRequestLenderEmptyProfile() throws MBSBaseException {
		createData();
		Mockito.when(transactionRequestControllerHelper.submitPriceRequest(Mockito.any(), Mockito.any(), Mockito.any()))
		                        .thenThrow(new MBSBusinessException("test"));
		ResponseEntity<Object> responseObjTraderPut = transactionRequestController
				.submitPriceRequest(transactionRequestPO.getTransReqId(), transactionRequestPO, headerMap);
		assertTrue(responseObjTraderPut.getStatusCode().toString().equals("400"));

	}

	

	/**
	 * Purpose: Test for case to put/update transaction for trader
	 * 
	 * @throws MBSBaseException
	 */
	@Test
	public void testPutTransactionRequestLenderException() throws MBSBaseException {
		createData();
		Mockito.when(transactionRequestControllerHelper.submitPriceRequest(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenThrow(new MBSBusinessException("test"));
		ResponseEntity<Object> responseObjTraderPut = transactionRequestController
				.submitPriceRequest(transactionRequestPO.getTransReqId(), transactionRequestPO, headerMap);
		assertTrue(responseObjTraderPut.getStatusCode().toString().equals("400"));

	}
	
	@Test
        public void testPutTransactionRequestLenderInternalServerError() throws MBSBaseException {
                createData();
                Mockito.when(transactionRequestControllerHelper.submitPriceRequest(Mockito.any(), Mockito.any(), Mockito.any()))
                        .thenThrow(new RuntimeException("Runtine Exception!"));
                ResponseEntity<Object> responseObjTraderPut = transactionRequestController
                                .submitPriceRequest(transactionRequestPO.getTransReqId(), transactionRequestPO, headerMap);
                assertTrue(responseObjTraderPut.getStatusCode().toString().equals("500"));

        }

	/**
	 * Purpose: Test for case to put/update transaction for trader
	 * 
	 * @throws MBSBaseException
	 */
	@Test
	public void testPutTransactionRequestTrader() throws MBSBaseException {
		createData();
		Mockito.when(transactionRequestControllerHelper.acceptRejectPriceLender(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(transactionRequestPO);
		ResponseEntity<Object> responseObjTraderPut = transactionRequestController
				.acceptRejectPriceLender(transactionRequestPO.getTransReqId(), transactionRequestPO, headerMap);
		assertTrue(responseObjTraderPut.getStatusCode().toString().equals("200"));

	}
	
	

	/**
	 * Purpose: Test for case to put/update transaction for trader
	 * 
	 * @throws MBSBaseException
	 */
	@Test
	public void testPutTransactionRequestTraderException() throws MBSBaseException {
		createData();
		Mockito.when(transactionRequestControllerHelper.acceptRejectPriceLender(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenThrow(new MBSBusinessException("test"));
		ResponseEntity<Object> responseObjTraderPut = transactionRequestController
				.acceptRejectPriceLender(transactionRequestPO.getTransReqId(), transactionRequestPO, headerMap);
		assertTrue(responseObjTraderPut.getStatusCode().toString().equals("400"));

	}
	
	@Test
        public void testPutTransactionRequestTraderInternalServerError() throws MBSBaseException {
                createData();
                Mockito.when(transactionRequestControllerHelper.acceptRejectPriceLender(Mockito.any(), Mockito.any(), Mockito.any()))
                    .thenThrow(new RuntimeException("Runtine Exception!"));
                ResponseEntity<Object> responseObjTraderPut = transactionRequestController
                                .acceptRejectPriceLender(transactionRequestPO.getTransReqId(), transactionRequestPO, headerMap);
                assertTrue(responseObjTraderPut.getStatusCode().toString().equals("500"));

        }

	/**
	 * Purpose: This method is to clear all transaction requests - temp use
	 * only.
	 *
	 * @throws Exception
	 */

	@Test
	public void clearAllMBSTransactionRequests() throws Exception {
		Mockito.doNothing().when(transactionRequestControllerHelper).clearAllTransactionRequests();
		transactionRequestController.clearAllTransactionRequests();
	}

}
