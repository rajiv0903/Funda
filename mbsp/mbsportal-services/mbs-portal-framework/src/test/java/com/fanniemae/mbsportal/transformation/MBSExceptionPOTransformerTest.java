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

package com.fanniemae.mbsportal.transformation;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fanniemae.mbsportal.model.MBSExceptionLookup;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;

/**
 * This class handles the test case for the POTransformer class
 * 
 * date 08/02/2017
 * 
 * @author g8upjv
 * 
 */

public class MBSExceptionPOTransformerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MBSExceptionPOTransformerTest.class);

	@SuppressWarnings("rawtypes")
        private ExceptionPOTransformer exceptionLookupPOTransformer = new ExceptionPOTransformer();

	private List<MBSExceptionLookup> mbsExceptionLookupLst;

	/**
	 * This method tests the call to transformation from ProductPO to
	 * MBSTransaction for successful call
	 * 
	 * @throws Exception
	 */
	@Test
	public void transform() throws Exception {
	        mbsExceptionLookupLst = new ArrayList<>();
	        MBSExceptionLookup mbsExceptionLookup = new MBSExceptionLookup();
	        mbsExceptionLookup.setErrorCategory("API");
                mbsExceptionLookup.setErrorCode("TRANS_00001");
                mbsExceptionLookup.setMessageType("DISP_ERROR");
                mbsExceptionLookup.setErrorMessage("Test message");
                mbsExceptionLookupLst.add(mbsExceptionLookup);
		TransformationObject transformationObject = new TransformationObject();
		transformationObject.setTargetPojo(mbsExceptionLookupLst);
		exceptionLookupPOTransformer.transform(transformationObject);
		List<ExceptionLookupPO> exceptionLookupPOLstResp = (List<ExceptionLookupPO>) transformationObject.getSourcePojo();
		assertTrue(exceptionLookupPOLstResp.size() == 1);
	}
	
	/**
         * This method tests the call to transformation from ProductPO to
         * MBSTransaction for successful call
         * 
         * @throws Exception
         */
        @Test
        public void transformEmptyInput() throws Exception {
                mbsExceptionLookupLst = new ArrayList<>();
                TransformationObject transformationObject = new TransformationObject();
                transformationObject.setTargetPojo(mbsExceptionLookupLst);
                exceptionLookupPOTransformer.transform(transformationObject);
                List<ExceptionLookupPO> exceptionLookupPOLstResp = (List<ExceptionLookupPO>) transformationObject.getSourcePojo();
                assertTrue(exceptionLookupPOLstResp.size() == 0);
        }

}
