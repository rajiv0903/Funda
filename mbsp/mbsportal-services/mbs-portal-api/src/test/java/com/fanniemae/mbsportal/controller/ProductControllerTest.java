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
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.fanniemae.mbsportal.api.controller.ProductController;
import com.fanniemae.mbsportal.api.controller.helper.ProductControllerHelper;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.service.ExceptionLookupService;
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
 * @Time 10:53:07 AM com.fanniemae.mbsportal.api.controller
 *       ProductControllerTest.java
 * @Description: Incorporate the Internal Server Error - Status Code 500
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTest {

    @InjectMocks
    ProductController productController;

    @Mock
    ProductControllerHelper roductControllerHelper;
    
    @Mock
    ExceptionLookupService exceptionLookupService;

    ProductPO prodPO;

    ProductIdPO prodId;

    List<ProductPO> lstProdPO;

    /**
     * Create Product PO data
     */
    public void createProductData() {
        prodPO = new ProductPO();
        prodId = new ProductIdPO();
        lstProdPO = new ArrayList<ProductPO>();
        prodId.setIdentifier(new Long(1));
        prodId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
        prodId.setType(PRODUCT_TYPE.MBS);
        prodPO.setProductId(prodId);
        prodPO.setAgencyType("FN");
        prodPO.setDescription("FN30 Fixed");
        prodPO.setNameCode("FN30");
        prodPO.setNameCodeSortOrder(1);
        prodPO.setSecurityTerm(360);
        lstProdPO.add(prodPO);
    }
    
    public List<ExceptionLookupPO> createData() {
        List<ExceptionLookupPO> exceptionLookupPOLst = new ArrayList<>();
        ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        exceptionLookupPO.setErrorCategory("API");
        exceptionLookupPO.setErrorCode("TRANS_00001");
        exceptionLookupPO.setMessageType("DISP_ERROR");
        exceptionLookupPO.setErrorMessage("Test Message");
        exceptionLookupPO.setLoggerMessage("Test Message");
        exceptionLookupPOLst.add(exceptionLookupPO);
        return exceptionLookupPOLst;
    }

    /**
     * Purpose: Creates the productPO data model
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        Mockito.when(exceptionLookupService.getExceptionLookupData(Mockito.any())).thenReturn(createData());
    }

    /**
     * Purpose: Test for case when media type is empty
     * 
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test
    @Ignore("No longer valid test case as the media type is hard-coded")
    public void testCreateTransactionRequestNoMediaType() throws MBSBaseException {
        createProductData();
        Mockito.when(roductControllerHelper.createProducts(Mockito.any())).thenReturn(prodPO);
        ResponseEntity<Object> responseObj = productController.createProducts(prodPO);
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
        createProductData();
        Mockito.when(roductControllerHelper.createProducts(null))
                .thenThrow(new MBSBusinessException("Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
        ResponseEntity<Object> responseObj = productController.createProducts(null);
        assertTrue(responseObj.getStatusCode().toString().equals("400"));
    }

    /**
     * Purpose: Test for case for successful call
     * 
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test
    public void testCreateProducts() throws MBSBaseException {
        createProductData();
        Mockito.when(roductControllerHelper.createProducts(Mockito.any())).thenReturn(prodPO);
        ResponseEntity<Object> responseObj = productController.createProducts(prodPO);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
    }

    /**
     * Purpose: Test for case for successful call
     * 
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test
    public void testCreateProductsException() throws MBSBaseException {
        createProductData();
        Mockito.when(roductControllerHelper.createProducts(Mockito.any()))
                .thenThrow(new MBSBusinessException("test Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
        ResponseEntity<Object> responseObj = productController.createProducts(prodPO);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("400"));
    }

    /**
     * @author Rajiv
     * @throws MBSBaseException
     */
    @Test
    public void testCreateProductsInternalServerError() throws MBSBaseException {
        createProductData();
        Mockito.when(roductControllerHelper.createProducts(Mockito.any()))
                .thenThrow(new RuntimeException("Runtine Exception!"));
        ResponseEntity<Object> responseObj = productController.createProducts(prodPO);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }
    
    /**
     * @author Rajiv
     * @throws MBSBaseException
     */
    @Test
    public void testCreateProductsInternalServerErrorExceptionCall() throws MBSBaseException {
        createProductData();
        Mockito.when(roductControllerHelper.createProducts(Mockito.any()))
                .thenThrow(new RuntimeException("Runtine Exception!"));
        Mockito.when(exceptionLookupService.getExceptionLookupData(Mockito.any())).thenThrow(new MBSBaseException("test"));
        ResponseEntity<Object> responseObj = productController.createProducts(prodPO);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }

    /**
     * Purpose: Test for case for successful call to get list of products
     * 
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test
    public void testGetProducts() throws MBSBaseException {
        createProductData();
        Mockito.when(roductControllerHelper.getProducts()).thenReturn(lstProdPO);
        ResponseEntity<Object> responseObj = productController.getProducts();
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
    }

    /**
     * Purpose: Test for case for successful call to get list of products
     * 
     * @throws MBSBaseException
     * @throws Exception
     */
    @Test
    public void testGetProductsException() throws MBSBaseException {
        createProductData();
        Mockito.when(roductControllerHelper.getProducts())
                .thenThrow(new MBSBusinessException("test Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
        ResponseEntity<Object> responseObj = productController.getProducts();
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("400"));
    }

    @Test
    public void testGetProductsInternalServerError() throws MBSBaseException {
        createProductData();
        Mockito.when(roductControllerHelper.getProducts()).thenThrow(new RuntimeException("Runtine Exception!"));
        ResponseEntity<Object> responseObj = productController.getProducts();
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }

    @Test
    public void clearAllMBSTransactionRequests() throws Exception {
        Mockito.doNothing().when(roductControllerHelper).clearAllProducts();
        productController.clearAllProducts();
    }

}
