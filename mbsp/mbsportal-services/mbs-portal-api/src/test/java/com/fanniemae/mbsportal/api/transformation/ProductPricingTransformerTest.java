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

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.po.ProductPricingPO;
import com.fanniemae.mbsportal.model.MBSProductPricingRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;

/**
 * This class handles the test case for the ProductPricingTransformerTest class
 * 
 * date: 07/24/2017
 * 
 * @author g8upjv Initial version 12/04/2017 
 * 
 * 
 */

public class ProductPricingTransformerTest{

	@SuppressWarnings("rawtypes")
	private ProductPricingRequestTransformer productPricingRequestTransformer = new ProductPricingRequestTransformer();

	List<ProductPricingPO> productPricingPORequest;
	
	ProductPO prodPO;

	ProductIdPO productId;


	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/**
	 * Purpose: This method tests the call to transformation from
	 * ProductPricingPO to MBSTransaction for successful call
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTransform() throws Exception {
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
		productPricingRequestTransformer.transform(transformationObject);
		List<MBSProductPricingRequest> mbsTransactionLst = (List<MBSProductPricingRequest>) transformationObject.getTargetPojo();
		assertTrue(mbsTransactionLst.get(0).getBuySellInd().equals("BUY"));
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