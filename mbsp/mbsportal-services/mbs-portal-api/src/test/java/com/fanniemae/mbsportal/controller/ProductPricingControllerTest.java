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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.fanniemae.mbsportal.api.controller.ProductPricingController;
import com.fanniemae.mbsportal.api.controller.helper.ProductPricingControllerHelper;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPricingPO;
import com.fanniemae.mbsportal.api.service.ExceptionLookupService;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;

/**
 * Class Name: ProductPricingControllerTest Purpose : This class is the test
 * class for the ProductPricing controller
 * 
 * @author g8upjv
 *
 */
/**
 * 
 * @author g8upjv
 * @Version: Initial
 * @Date Dec 4, 2017
 * @Description: Incorporate the Internal Server Error - Status Code 500
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductPricingControllerTest {

	@InjectMocks
	ProductPricingController productPricingController;

	@Mock
	ProductPricingControllerHelper productPricingControllerHelper;
	
	    @Mock
	    ExceptionLookupService exceptionLookupService;

	List<ProductPricingPO> productPricingPOLst;

	Map<String, String> headerMap;

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
    
    /**
     * utility method to create stub data
     */
    private ProductId createProductId(Long identifier) {
            ProductId productId = new ProductId();
            productId.setIdentifier(Long.valueOf(identifier));
            productId.setSourceType("PU");
            productId.setType("MBS");
            return productId;
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
     * utility method to create stub data
     */
	public void createData() {
		headerMap = new HashMap<String, String>();
		productPricingPOLst =  new ArrayList<>();
		ProductIdPO productId = new ProductIdPO();
        productId.setIdentifier(Long.valueOf(8));
        productId.setType(TradeConstants.PRODUCT_TYPE.MBS);
        productId.setSourceType(TradeConstants.PRODUCT_SOURCE_TYPE.PU);
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(productId);
		productPricingPOLst.add(productPricingPO);
		
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
	 * Purpose: Test for case for success
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	public void testCreateProductPricing() throws MBSBaseException {
		createData();
		Mockito.when(productPricingControllerHelper.createProductPricing(Mockito.any()))
				.thenReturn(null);
		ResponseEntity<Object> responseObj = productPricingController
				.createProductPricing(productPricingPOLst);
		assertTrue(responseObj.getStatusCode().toString().equals("200"));
	}
	
	/**
	 * Purpose: Test for case for exception
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	public void testCreateProductPricingException() throws MBSBaseException {
		createData();
		Mockito.when(productPricingControllerHelper.createProductPricing(Mockito.any()))
				.thenThrow(Exception.class);
		ResponseEntity<Object> responseObj = productPricingController
				.createProductPricing(productPricingPOLst);
		assertTrue(responseObj.getStatusCode().toString().equals("500"));
	}
	
	/**
         * Purpose: Test for case for exception
         * 
         * @throws MBSBaseException
         * @throws Exception
         */
        @Test
        public void testCreateProductPricingExceptionMessageRetrieval() throws MBSBaseException {
                createData();
                Mockito.when(productPricingControllerHelper.createProductPricing(Mockito.any()))
                                .thenThrow(Exception.class);
                Mockito.when(exceptionLookupService.getExceptionLookupData(Mockito.any())).thenThrow(new MBSBaseException("test"));
                ResponseEntity<Object> responseObj = productPricingController
                                .createProductPricing(productPricingPOLst);
                assertTrue(responseObj.getStatusCode().toString().equals("500"));
        }
	
	/**
	 * Purpose: Test for case for MBS exception
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	public void testCreateProductPricingMBSException() throws MBSBaseException {
		createData();
		Mockito.when(productPricingControllerHelper.createProductPricing(Mockito.any()))
				.thenThrow(new MBSBusinessException("error"));
		ResponseEntity<Object> responseObj = productPricingController
				.createProductPricing(productPricingPOLst);
		assertTrue(responseObj.getStatusCode().toString().equals("400"));
	}

	/**
	 * Purpose: Test for case for successful call to get list of product pricing
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	public void testGetAllProductPricing() throws MBSBaseException {
		createData();
		Mockito.when(productPricingControllerHelper.getAllProductPricing(Mockito.anyBoolean()))
		.thenReturn(productPricingPOLst);
		Optional<Long> identifier = Optional.empty();
		Optional<TradeConstants.PRODUCT_SOURCE_TYPE> sourceType = Optional.empty();
		Optional<TradeConstants.PRODUCT_TYPE> type = Optional.empty();
		ResponseEntity<Object> responseObj = productPricingController.getAllProductPricing(identifier, sourceType, type);
		assertTrue(responseObj.hasBody());
		assertTrue(responseObj.getStatusCode().toString().equals("200"));
	}
	
	/**
	 * Purpose: Test for case for successful call to get list of product pricing
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	public void testGetAllProductPricingException() throws MBSBaseException {
		createData();
		Mockito.when(productPricingControllerHelper.getAllProductPricing(Mockito.anyBoolean()))
		.thenThrow(Exception.class);
		Optional<Long> identifier = Optional.empty();
		Optional<TradeConstants.PRODUCT_SOURCE_TYPE> sourceType = Optional.empty();
		Optional<TradeConstants.PRODUCT_TYPE> type = Optional.empty();
		ResponseEntity<Object> responseObj = productPricingController.getAllProductPricing(identifier, sourceType, type);
		assertTrue(responseObj.hasBody());
		assertTrue(responseObj.getStatusCode().toString().equals("500"));
	}
	
	/**
	 * Purpose: Test for case for successful call to get list of product pricing
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	public void testGetAllProductPricingMBSException() throws MBSBaseException {
		createData();
		Mockito.when(productPricingControllerHelper.getAllProductPricing(Mockito.anyBoolean()))
		.thenThrow(new MBSBusinessException("error"));
		Optional<Long> identifier = Optional.empty();
		Optional<TradeConstants.PRODUCT_SOURCE_TYPE> sourceType = Optional.empty();
		Optional<TradeConstants.PRODUCT_TYPE> type = Optional.empty();
		ResponseEntity<Object> responseObj = productPricingController.getAllProductPricing(identifier, sourceType, type);
		assertTrue(responseObj.hasBody());
		assertTrue(responseObj.getStatusCode().toString().equals("400"));
	}
	
	/**
	 * Purpose: Test for case for successful call to get list of product pricing
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	public void testGetProductPricing() throws MBSBaseException {
		createData();
		Mockito.when(productPricingControllerHelper.getProductPricing(Mockito.any(), Mockito.any(), Mockito.any(),  Mockito.anyBoolean()))
		.thenReturn(productPricingPOLst);
		ResponseEntity<Object> responseObj = productPricingController.getProductPricing(new Long(1), TradeConstants.PRODUCT_SOURCE_TYPE.PU, TradeConstants.PRODUCT_TYPE.MBS);
		assertTrue(responseObj.hasBody());
		assertTrue(responseObj.getStatusCode().toString().equals("200"));
	}
	
	/**
	 * Purpose: Test for case for successful call to get list of product pricing
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	public void testGetProductPricingException() throws MBSBaseException {
		createData();
		Mockito.when(productPricingControllerHelper.getProductPricing(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyBoolean()))
		.thenThrow(Exception.class);
		ResponseEntity<Object> responseObj = productPricingController.getProductPricing(new Long(1), TradeConstants.PRODUCT_SOURCE_TYPE.PU, TradeConstants.PRODUCT_TYPE.MBS);
		assertTrue(responseObj.hasBody());
		assertTrue(responseObj.getStatusCode().toString().equals("500"));
	}
	
	/**
	 * Purpose: Test for case for successful call to get list of product pricing
	 * 
	 * @throws MBSBaseException
	 * @throws Exception
	 */
	@Test
	public void testGetProductPricingMBSException() throws MBSBaseException {
		createData();
		Mockito.when(productPricingControllerHelper.getProductPricing(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyBoolean()))
		.thenThrow(new MBSBusinessException("error"));
		ResponseEntity<Object> responseObj = productPricingController.getProductPricing(new Long(1), TradeConstants.PRODUCT_SOURCE_TYPE.PU, TradeConstants.PRODUCT_TYPE.MBS);
		assertTrue(responseObj.hasBody());
		assertTrue(responseObj.getStatusCode().toString().equals("400"));
	}

	@Test
	public void clearAllMBSTransactionRequests() throws Exception {
		Mockito.doNothing().when(productPricingControllerHelper).clearAllProductPricing();
		productPricingController.clearAllProductPricing();
	}

}
