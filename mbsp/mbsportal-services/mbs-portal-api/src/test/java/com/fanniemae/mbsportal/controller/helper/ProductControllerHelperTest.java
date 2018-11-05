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

package com.fanniemae.mbsportal.controller.helper;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import com.fanniemae.mbsportal.api.controller.helper.ProductControllerHelper;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.service.ProductService;
import com.fanniemae.mbsportal.aspect.exception.ExceptionTracingAspect;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSDataAccessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;




/**
 * @author gaur5c
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductControllerHelperTest {

    @InjectMocks
    ProductControllerHelper productControllerHelper;

    @Mock
    ProductService productService;

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

    /**
     * Purpose: Creates the productPO data model
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

    }
    
    @Test
    public void testCreateProducts() throws MBSBaseException {
            createProductData();
            Mockito.when(productService.createProducts(Mockito.any())).thenReturn(prodPO);
            ProductPO ProductPORet = productControllerHelper.createProducts(prodPO);
            assertEquals(prodPO.getNameCode(), ProductPORet.getNameCode());
    }
    
    @Test(expected = MBSBusinessException.class)
    public void testCreateProductsException() throws MBSBaseException {
            createProductData();
            Mockito.when(productService.createProducts(Mockito.any())).thenThrow(new MBSBusinessException("Test Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
            productControllerHelper.createProducts(prodPO);

    }
    
    @Test(expected = MBSBusinessException.class)
    public void testCreateProductsExceptionEmptyInput() throws MBSBaseException {
            createProductData();
            Mockito.when(productService.createProducts(Mockito.any())).thenReturn(prodPO);
            productControllerHelper.createProducts(null);

    }
    
    @Test
    public void testGetProducts() throws MBSBaseException {
            createProductData();
            Mockito.when(productService.getMBSProducts()).thenReturn(lstProdPO);
            List<ProductPO> lstProdPORet = productControllerHelper.getProducts();
            assertEquals(lstProdPO.size(), lstProdPORet.size());
    }
    
    @Test(expected = MBSBusinessException.class)
    public void testGetProductsException() throws MBSBaseException {
            createProductData();
            Mockito.when(productService.getMBSProducts()).thenThrow(new MBSBusinessException("Test Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
            productControllerHelper.getProducts();
    }

    @Test
    public void clearAllMBSTransactionRequests() throws MBSBaseException {
            Mockito.doNothing().when(productService).clearAll();
            productControllerHelper.clearAllProducts();
    }
    
    @Test
    public void testPointCutSuccess()throws MBSBaseException {
        
        AspectJProxyFactory factory = new AspectJProxyFactory(productControllerHelper);
        ExceptionTracingAspect aspect = new ExceptionTracingAspect();
        factory.addAspect(aspect);
        ProductControllerHelper proxy = factory.getProxy();
        Mockito.doNothing().when(productService).clearAll();
        proxy.clearAllProducts();
    }
    
    @Test(expected=MBSBaseException.class)
    public void testPointCutMBSBusinessException()throws MBSBaseException {
        
        createProductData();
        AspectJProxyFactory factory = new AspectJProxyFactory(productControllerHelper);
        ExceptionTracingAspect aspect = new ExceptionTracingAspect();
        factory.addAspect(aspect);
        ProductControllerHelper proxy = factory.getProxy();
        Mockito.when(productService.createProducts(Mockito.any())).thenThrow(new MBSBusinessException("Test Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
        proxy.createProducts(prodPO);
    }
    
    @Test(expected=MBSBaseException.class)
    public void testPointCutMBSDataAccessException()throws MBSBaseException {
        
        createProductData();
        AspectJProxyFactory factory = new AspectJProxyFactory(productControllerHelper);
        ExceptionTracingAspect aspect = new ExceptionTracingAspect();
        factory.addAspect(aspect);
        ProductControllerHelper proxy = factory.getProxy();
        Mockito.when(productService.createProducts(Mockito.any())).thenThrow(new MBSDataAccessException("Test Exception"));
        proxy.createProducts(prodPO);
    }
    
    @Test(expected=MBSBaseException.class)
    public void testPointCutMBSSystemException()throws MBSBaseException {
        
        createProductData();
        AspectJProxyFactory factory = new AspectJProxyFactory(productControllerHelper);
        ExceptionTracingAspect aspect = new ExceptionTracingAspect();
        factory.addAspect(aspect);
        ProductControllerHelper proxy = factory.getProxy();
        Mockito.when(productService.createProducts(Mockito.any())).thenThrow(new MBSSystemException("Test Exception", MBSExceptionConstants.SYSTEM_EXCEPTION));
        proxy.createProducts(prodPO);
    }
    
    @Test(expected=MBSBaseException.class)
    public void testPointCutMBSBaseException()throws MBSBaseException {
        
        createProductData();
        AspectJProxyFactory factory = new AspectJProxyFactory(productControllerHelper);
        ExceptionTracingAspect aspect = new ExceptionTracingAspect();
        factory.addAspect(aspect);
        ProductControllerHelper proxy = factory.getProxy();
        Mockito.when(productService.createProducts(Mockito.any())).thenThrow(new MBSBaseException("Test Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
        proxy.createProducts(prodPO);
    }
    
    @Test(expected=Exception.class)
    public void testPointCutException()throws MBSBaseException {
        
        createProductData();
        AspectJProxyFactory factory = new AspectJProxyFactory(productControllerHelper);
        ExceptionTracingAspect aspect = new ExceptionTracingAspect();
        factory.addAspect(aspect);
        ProductControllerHelper proxy = factory.getProxy();
        Mockito.when(productService.createProducts(Mockito.any())).thenThrow(new Exception("Test Exception"));
        proxy.createProducts(prodPO);
    }
    
    @Test(expected=Throwable.class)
    public void testPointCutThrowable()throws MBSBaseException {
        
        createProductData();
        AspectJProxyFactory factory = new AspectJProxyFactory(productControllerHelper);
        ExceptionTracingAspect aspect = new ExceptionTracingAspect();
        factory.addAspect(aspect);
        ProductControllerHelper proxy = factory.getProxy();
        Mockito.when(productService.createProducts(Mockito.any())).thenThrow(new Throwable("Test Exception"));
        proxy.createProducts(prodPO);
    }
   

}
