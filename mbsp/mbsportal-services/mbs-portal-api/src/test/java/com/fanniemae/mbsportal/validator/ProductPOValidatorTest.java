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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.validator.ProductPOValidator;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * This class handles the test case for the ProductPOValidatorTest class
 * 
 * @author g8upjv
 * 
 */
public class ProductPOValidatorTest {

	@SuppressWarnings("rawtypes")
        private ProductPOValidator productPOValidator = new ProductPOValidator();
	private ProductPO productPO;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/**
	 * Purpose: This method tests the call to validation for state type with
	 * Correct status value
	 *
	 * @throws Exception
	 */
	@Test
	public void validate() throws Exception {
		productPO = new ProductPO();
		productPO.setDescription("Test Description");
		TransformationObject transformationObject = new TransformationObject();
		transformationObject.setSourcePojo(productPO);
		productPOValidator.validate(transformationObject);
		assertTrue(productPO.getDescription().equals("Test Description"));

	}

	@Test(expected = MBSBaseException.class)
	public void validateInvalidDescription() throws MBSBaseException {
		productPO = new ProductPO();
		productPO.setDescription("");
		TransformationObject transformationObject = new TransformationObject();
		transformationObject.setSourcePojo(productPO);
		productPOValidator.validate(transformationObject);
		assertTrue(productPO.getDescription().equals(""));
	}

}
