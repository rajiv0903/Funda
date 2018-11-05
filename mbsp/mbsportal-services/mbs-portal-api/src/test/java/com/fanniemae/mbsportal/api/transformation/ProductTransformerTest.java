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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.transformation.ProductTransformer;
import com.fanniemae.mbsportal.model.MBSProduct;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * This class handles the test case for the ProductTransaformer class
 * 
 * date: 10/9/2017
 * 
 * @author g8upjv
 * 
 */

public class ProductTransformerTest {

	private ProductTransformer productTransformer = new ProductTransformer();

	ProductPO prodPO;

	ProductIdPO productId;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	TransformationObject transformationObject = new TransformationObject();

	/**
	 * Purpose: This method tests the call to transformation from
	 * TransactionRequestPO to MBSTransaction for successful call
	 * 
	 * @throws MBSBaseException
	 * 
	 * @throws Exception
	 */
	@Test
	public void transformProductTransformer() throws MBSBaseException {
		prodPO = new ProductPO();
		productId = new ProductIdPO();
		productId.setIdentifier(new Long(1));
		productId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
		productId.setType(PRODUCT_TYPE.MBS);
		prodPO.setProductId(productId);
		prodPO.setAgencyType("FN");
		prodPO.setDescription("FN30");
		prodPO.setNameCode("1");
		prodPO.setNameCodeSortOrder(1);
		prodPO.setSecurityTerm(360);
		transformationObject.setSourcePojo(prodPO);
		productTransformer.transform(transformationObject);
		MBSProduct mbsProduct = (MBSProduct) transformationObject.getTargetPojo();
		assertTrue(mbsProduct.getProductDescription().equals("FN30"));
	}
	
	@Test(expected=MBSBaseException.class)
	public void transformProductTransformerException() throws MBSBaseException {
		prodPO = new ProductPO();
		productId = new ProductIdPO();
		productId.setIdentifier(new Long(1));
		productId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
		productId.setType(PRODUCT_TYPE.MBS);
		prodPO.setProductId(null);
		prodPO.setAgencyType("FN");
		prodPO.setDescription("FN30");
		prodPO.setNameCode("1");
		prodPO.setNameCodeSortOrder(1);
		prodPO.setSecurityTerm(360);
		transformationObject.setSourcePojo(prodPO);
		productTransformer.transform(transformationObject);
		MBSProduct mbsProduct = (MBSProduct) transformationObject.getTargetPojo();
		assertTrue(mbsProduct.getProductDescription().equals("FN30"));
	}

}