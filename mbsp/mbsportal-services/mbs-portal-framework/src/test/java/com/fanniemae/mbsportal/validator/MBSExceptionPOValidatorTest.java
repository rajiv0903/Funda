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

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * This class handles the test case for the ExceptionLookupPOValidatorTest class
 * 
 * @author g8upjv
 * 
 */
public class MBSExceptionPOValidatorTest {

	@SuppressWarnings("rawtypes")
        private ExceptionPOValidator exceptionLookupPOValidator = new ExceptionPOValidator();
	
	List<ExceptionLookupPO> exceptionLookupPOLst;

	/**
	 * Purpose: This method tests the call to validation for state type with
	 * Correct status value
	 *
	 * @throws Exception
	 */
	@Test
	public void validate() throws Exception {
		TransformationObject transformationObject = new TransformationObject();
		transformationObject.setSourcePojo(createData());
		exceptionLookupPOValidator.validate(transformationObject);
		assertNotNull(transformationObject.getSourcePojo());

	}

	@Test(expected = MBSBaseException.class)
	public void validateEmptyCategory() throws MBSBaseException {
	    exceptionLookupPOLst = new ArrayList<>();
            ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
            exceptionLookupPO.setErrorCategory("");
            exceptionLookupPO.setErrorCode("TRANS_00001");
            exceptionLookupPO.setMessageType("DISP_ERROR");
            exceptionLookupPO.setErrorMessage("Test Message");
            exceptionLookupPOLst.add(exceptionLookupPO);
            TransformationObject transformationObject = new TransformationObject();
            transformationObject.setSourcePojo(exceptionLookupPOLst);
            exceptionLookupPOValidator.validate(transformationObject);
	}
	
	@Test(expected = MBSBaseException.class)
        public void validateEmptyErrorCode() throws MBSBaseException {
            exceptionLookupPOLst = new ArrayList<>();
            ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
            exceptionLookupPO.setErrorCategory("API");
            exceptionLookupPO.setErrorCode("");
            exceptionLookupPO.setMessageType("DISP_ERROR");
            exceptionLookupPO.setErrorMessage("Test Message");
            exceptionLookupPOLst.add(exceptionLookupPO);
            TransformationObject transformationObject = new TransformationObject();
            transformationObject.setSourcePojo(exceptionLookupPOLst);
            exceptionLookupPOValidator.validate(transformationObject);
        }
	
	@Test(expected = MBSBaseException.class)
        public void validateEmptyMessage() throws MBSBaseException {
            exceptionLookupPOLst = new ArrayList<>();
            ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
            exceptionLookupPO.setErrorCategory("API");
            exceptionLookupPO.setErrorCode("TRANS_00001");
            exceptionLookupPO.setMessageType("DISP_ERROR");
            exceptionLookupPO.setErrorMessage("");
            exceptionLookupPOLst.add(exceptionLookupPO);
            TransformationObject transformationObject = new TransformationObject();
            transformationObject.setSourcePojo(exceptionLookupPOLst);
            exceptionLookupPOValidator.validate(transformationObject);
        }
	
	public List<ExceptionLookupPO> createData() {
            exceptionLookupPOLst = new ArrayList<>();
            ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
            exceptionLookupPO.setErrorCategory("API");
            exceptionLookupPO.setErrorCode("TRAN_0001");
            exceptionLookupPO.setMessageType("DISP_ERROR");
            exceptionLookupPO.setErrorMessage("Test Message");
            exceptionLookupPO.setLoggerMessage("Test message");
            exceptionLookupPOLst.add(exceptionLookupPO);
            return exceptionLookupPOLst;
        }

}
