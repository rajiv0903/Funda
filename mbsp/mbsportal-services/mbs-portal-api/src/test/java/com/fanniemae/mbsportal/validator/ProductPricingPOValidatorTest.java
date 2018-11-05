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

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.po.ProductPricingPO;
import com.fanniemae.mbsportal.api.validator.ProductPricingPOValidator;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * Class Name: TransactionRequestPOValidatorTest Purpose : This class handles
 * the test case for the TransactionRequestPOValidator class
 * 
 * @author g8upjv
 * 
 */


public class ProductPricingPOValidatorTest {
	

    @SuppressWarnings("rawtypes")
    private ProductPricingPOValidator productPricingPOValidator = new ProductPricingPOValidator();
    
	List<ProductPricingPO> productPricingPORequest;
	
	ProductPO prodPO;

	ProductIdPO productId;


    /**
     * Purpose: This method tests the call to validation for state type with
     * Correct status value
     *
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @Test(expected=MBSBaseException.class)
    public void testValidateException() throws Exception {
		productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("BUY");
        productPricingPO.setEffectiveDate(null);
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setCutOffDate("2020-09-19");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(productPricingPORequest);
        productPricingPOValidator.validate(transformationObject);
        List<ProductPricingPO> productPricingPORequestLst =(List<ProductPricingPO>)transformationObject.getSourcePojo();
        assertTrue(productPricingPORequestLst.get(0).getBuySellIndicator().equals("BUY"));
        
    }

    /**
     * Purpose: This method tests the call to validation for state type with
     * trans req id
     *
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @Test
    public void testValidate() throws Exception {
		productPricingPORequest = new ArrayList<>();
        ProductPricingPO productPricingPO = new ProductPricingPO();
        productPricingPO.setProductId(createProductIdPO(Long.valueOf(10)));
        productPricingPO.setProductNameCode("FN30");
        productPricingPO.setBuySellIndicator("BUY");
        productPricingPO.setEffectiveDate("2017-10-11");
        productPricingPO.setSettlementDate("2020-09-21");
        productPricingPO.setCutOffDate("2020-09-19");
        productPricingPO.setPassThroughRate("4.000");
        productPricingPO.setMarketTermType(360);
        productPricingPORequest.add(productPricingPO);
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(productPricingPORequest);
        productPricingPOValidator.validate(transformationObject);
        List<ProductPricingPO> productPricingPORequestLst =(List<ProductPricingPO>)transformationObject.getSourcePojo();
        assertTrue(productPricingPORequestLst.get(0).getBuySellIndicator().equals("BUY"));
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
