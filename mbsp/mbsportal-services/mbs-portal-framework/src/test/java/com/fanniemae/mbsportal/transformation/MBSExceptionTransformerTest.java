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

import com.fanniemae.mbsportal.model.MBSExceptionLookup;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * This class handles the test case for the class
 * 
 * date: 03/06/2018
 * 
 * @author g8upjv
 * 
 */

public class MBSExceptionTransformerTest {

	private ExceptionTransformer exceptionLookupTransformer = new ExceptionTransformer();

	TransformationObject transformationObject = new TransformationObject();
	
	List<ExceptionLookupPO> exceptionLookupPOLst;

	/**
	 * Purpose: This method tests the call to transformation 
	 * 
	 * @throws MBSBaseException
	 * 
	 * @throws Exception
	 */
	@Test
	public void transformProductTransformer() throws MBSBaseException {
	        createData();
		transformationObject.setSourcePojo(exceptionLookupPOLst);
		exceptionLookupTransformer.transform(transformationObject);
		List<MBSExceptionLookup> mbsExceptionLookupLst = (List<MBSExceptionLookup>) transformationObject.getTargetPojo();
		assertTrue(mbsExceptionLookupLst.size() == 1);
	}
	
	@Test
	public void transformProductTransformerEmptyData() throws MBSBaseException {
	    exceptionLookupPOLst = new ArrayList<>();
            transformationObject.setSourcePojo(exceptionLookupPOLst);
            exceptionLookupTransformer.transform(transformationObject);
            List<MBSExceptionLookup> mbsExceptionLookupLst = (List<MBSExceptionLookup>) transformationObject.getTargetPojo();
            assertTrue(mbsExceptionLookupLst.size() == 0);
	}
	
	public List<ExceptionLookupPO> createData() {
            exceptionLookupPOLst = new ArrayList<>();
            ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
            exceptionLookupPO.setErrorCategory("API");
            exceptionLookupPO.setErrorCode("TRANS_00001");
            exceptionLookupPO.setMessageType("DISP_ERROR");
            exceptionLookupPO.setErrorMessage("Test Message");
            exceptionLookupPOLst.add(exceptionLookupPO);
            return exceptionLookupPOLst;
        }

}