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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.api.cdx.CDXClientApi;
import com.fanniemae.mbsportal.api.controller.helper.ProductPricingControllerHelper;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.po.ProductPricingPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.service.ProductPricingService;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;

/**
 * @author g8upjv
 * Initial version 12/05/2017
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductPricingControllerHelperTest {

    @InjectMocks
    ProductPricingControllerHelper productPricingControllerHelper;

    @Mock
    ProductPricingService productPricingService;

    @Mock
    CDXClientApi cdxClientApi;

    ProductPO prodPO;

    ProductIdPO productId;

    Map<String, String> headerMap;

    ProfileEntitlementPO profileEntitlementPO;
    
	List<ProductPricingPO> productPricingPOLst;


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
     * Purpose: 
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

    }
    
    /**
     * Success scenario
     * @throws MBSBaseException
     */
    @Test
    public void testCreateProductPricing() throws MBSBaseException {
            createData();
            Mockito.doNothing().when(productPricingService).createProductPricing(Mockito.any());
            productPricingControllerHelper.createProductPricing(productPricingPOLst);
            assertTrue(true);
    }
    
    /**
     * Exception scenario - list is null
     * @throws MBSBaseException
     */
    @Test(expected= MBSBusinessException.class)
    public void testCreateProductPricingException() throws MBSBaseException {
            createData();
            Mockito.doNothing().when(productPricingService).createProductPricing(Mockito.any());
            productPricingControllerHelper.createProductPricing(null);
            assertTrue(true);
    }
    
    /**
     * Success scenario
     * @throws MBSBaseException
     */
    @Test
    public void testGetAllProductPricing() throws MBSBaseException {
            createData();
            Mockito.when(productPricingService.getAllProductPricing(Mockito.anyBoolean())).thenReturn(productPricingPOLst);
            List<ProductPricingPO> response = productPricingControllerHelper.getAllProductPricing(false);
            assertNotNull(response);
    }
    
    /**
     * Success scenario
     * @throws MBSBaseException
     */
    @Test
    public void testGetAllProductPricing_Filtered() throws MBSBaseException {
            createData();
            Mockito.when(productPricingService.getAllProductPricing(Mockito.anyBoolean())).thenReturn(productPricingPOLst);
            List<ProductPricingPO> response = productPricingControllerHelper.getAllProductPricing(true);
            assertNotNull(response);
    }
    
    
    /**
     * Success scenario
     * @throws MBSBaseException
     */
    @Test
    public void testGetProductPricing() throws MBSBaseException {
            createData();
            Mockito.when(productPricingService.getProductPricing(Mockito.any(), Mockito.anyBoolean())).thenReturn(productPricingPOLst);
            List<ProductPricingPO> response = productPricingControllerHelper.getProductPricing(new Long(1), TradeConstants.PRODUCT_SOURCE_TYPE.PU, TradeConstants.PRODUCT_TYPE.MBS, false);
            assertNotNull(response);
    }
    
    /**
     * Success scenario
     * @throws MBSBaseException
     */
    @Test
    public void testGetProductPricing_Filtered() throws MBSBaseException {
            createData();
            Mockito.when(productPricingService.getProductPricing(Mockito.any(), Mockito.anyBoolean())).thenReturn(productPricingPOLst);
            List<ProductPricingPO> response = productPricingControllerHelper.getProductPricing(new Long(1), TradeConstants.PRODUCT_SOURCE_TYPE.PU, TradeConstants.PRODUCT_TYPE.MBS, true);
            assertNotNull(response);
    }

}
