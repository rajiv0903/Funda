/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.transformation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.model.MBSExceptionLookup;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.BaseTransformer;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * This class handles the transformations required for ExceptionLookup
 *
 * @author g8upjv
 *
 */
//TODO: Use the transformer from framework package
@Deprecated
@Component
public class ExceptionLookupPOTransformer<T extends TransformationObject> extends BaseTransformer<T> {

	// @InjectLog
	// private Logger LOGGER;
        /**
         * 
         * LOGGER Logger variable
         */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionLookupPOTransformer.class);

	/**
	 * 
	 * Purpose: This method transforms the MBSExceptionLookup object
	 *
	 * @param transformationObject TransformationObject
	 * @return TransformationObject
	 * @throws MBSBaseException
	 */
	@SuppressWarnings("unchecked")
        @Override
	public TransformationObject transform(TransformationObject transformationObject) throws MBSBaseException {
		LOGGER.debug("Entering transform method in ExceptionLookupPOTransformer");
		List<MBSExceptionLookup> mbsExceptionLookupLst = (List<MBSExceptionLookup>) transformationObject.getTargetPojo();
		List<ExceptionLookupPO> exceptionLookupPOLst = new ArrayList<>();
		ExceptionLookupPO exceptionLookupPO;
		// Transform the Presentation object to domain object
		for(MBSExceptionLookup mbsExceptionLookup : mbsExceptionLookupLst){
		    exceptionLookupPO = convertToExceptionLookupPO(mbsExceptionLookup);
		    exceptionLookupPOLst.add(exceptionLookupPO);
		}
		transformationObject.setSourcePojo(exceptionLookupPOLst);
		LOGGER.debug("Exiting transform method in ExceptionLookupPOTransformer");
		return transformationObject;
	}

	/**
	 * 
	 * Purpose: This does the conversion from MBSExceptionLookup to ExceptionLookupPO object
	 *
	 * @param mbsExceptionLookup MBSExceptionLookup
	 * @return ExceptionLookupPO The presentation object of MBSExceptionLookup object
	 * @throws MBSBaseException
	 */
	private ExceptionLookupPO convertToExceptionLookupPO(MBSExceptionLookup mbsExceptionLookup) {
	        ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
	        exceptionLookupPO.setErrorCode(mbsExceptionLookup.getErrorCode());
		exceptionLookupPO.setErrorCategory(mbsExceptionLookup.getErrorCategory());
		exceptionLookupPO.setErrorMessage(mbsExceptionLookup.getErrorMessage());
		exceptionLookupPO.setMessageType(mbsExceptionLookup.getMessageType());
		exceptionLookupPO.setLoggerMessage(mbsExceptionLookup.getLoggerMessage());
		exceptionLookupPO.setClassificationTypeCode(mbsExceptionLookup.getClassificationTypeCode());
		exceptionLookupPO.setLogLevel(mbsExceptionLookup.getLogLevel());
		LOGGER.debug("ExceptionLookupPO values:" + exceptionLookupPO);
		return exceptionLookupPO;
	}
}
