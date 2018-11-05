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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.transformation.ProductPOTransformer;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSProduct;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * This class handles the test case for the ProductPOTransformerTest class
 * 
 * date 08/02/2017
 * 
 * @author g8upjv
 * 
 */

public class ProductPOTransformerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductPOTransformerTest.class);

	@SuppressWarnings("rawtypes")
        private ProductPOTransformer productPOTransformer = new ProductPOTransformer();

	private MBSProduct mbsProduct;

	private ProductId prodId;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/**
	 * This method tests the call to transformation from ProductPO to
	 * MBSTransaction for successful call
	 * 
	 * @throws Exception
	 */
	@Test
	public void transform() throws Exception {
		mbsProduct = new MBSProduct();
		prodId = new ProductId();
		prodId.setIdentifier(new Long(1));
		prodId.setSourceType(PRODUCT_SOURCE_TYPE.PU.toString());
		prodId.setType(PRODUCT_TYPE.MBS.toString());
		mbsProduct.setProductId(prodId);
		mbsProduct.setProductAgencyType("FN");
		mbsProduct.setProductDescription("FN30");
		mbsProduct.setProductNameCode("FN30");
		mbsProduct.setProductNameCodeDisplayOrderNum(1);
		mbsProduct.setSecurityTerm(360);
		TransformationObject transformationObject = new TransformationObject();
		transformationObject.setTargetPojo(mbsProduct);
		productPOTransformer.transform(transformationObject);
		ProductPO prodPO = (ProductPO) transformationObject.getSourcePojo();
		assertTrue(prodPO.getNameCode().equals("FN30"));
	}
	
	@Test(expected=MBSBaseException.class)
	public void transformException() throws Exception {
		mbsProduct = new MBSProduct();
		mbsProduct.setProductId(null);
		mbsProduct.setProductAgencyType("FN");
		mbsProduct.setProductDescription("FN30");
		mbsProduct.setProductNameCode("FN30");
		mbsProduct.setProductNameCodeDisplayOrderNum(1);
		mbsProduct.setSecurityTerm(360);
		TransformationObject transformationObject = new TransformationObject();
		transformationObject.setTargetPojo(mbsProduct);
		productPOTransformer.transform(transformationObject);
		ProductPO prodPO = (ProductPO) transformationObject.getSourcePojo();
		assertTrue(prodPO.getNameCode().equals("FN30"));
	}

}
