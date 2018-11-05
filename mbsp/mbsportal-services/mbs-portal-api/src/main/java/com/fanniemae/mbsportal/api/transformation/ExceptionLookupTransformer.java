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
public class ExceptionLookupTransformer<T extends TransformationObject> extends BaseTransformer<T> {

    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionLookupTransformer.class);

    /**
     * 
     * Purpose: This method transforms the MBS Product object
     *
     * @param transformationObject
     *            TransformationObject
     * @return TransformationObject
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    @Override
    public TransformationObject transform(TransformationObject transformationObject) throws MBSBaseException {
        LOGGER.debug("Entering transform method in ExceptionLookupTransformer");
        List<ExceptionLookupPO> exceptionLookupPOLst = (List<ExceptionLookupPO>) transformationObject.getSourcePojo();
        List<MBSExceptionLookup> mbsExceptionLookupLst = new ArrayList<MBSExceptionLookup>();
        for (ExceptionLookupPO exceptionLookupPO : exceptionLookupPOLst) {
            mbsExceptionLookupLst.add(convertToModel(exceptionLookupPO));
        }
        transformationObject.setTargetPojo(mbsExceptionLookupLst);
        LOGGER.debug("Exiting transform method in ExceptionLookupTransformer");
        return transformationObject;
    }

    /**
     * 
     * 
     * @param exceptionLookupPO
     *            ExceptionLookupPO
     * @return MBSExceptionLookup
     * @throws MBSBaseException
     */
    public MBSExceptionLookup convertToModel(ExceptionLookupPO exceptionLookupPO) {
        MBSExceptionLookup mbsExceptionLookup = new MBSExceptionLookup();
        mbsExceptionLookup.setErrorCategory(exceptionLookupPO.getErrorCategory());
        mbsExceptionLookup.setErrorCode(exceptionLookupPO.getErrorCode());
        mbsExceptionLookup.setErrorMessage(exceptionLookupPO.getErrorMessage());
        mbsExceptionLookup.setMessageType(exceptionLookupPO.getMessageType());
        mbsExceptionLookup.setLoggerMessage(exceptionLookupPO.getLoggerMessage());
        mbsExceptionLookup.setClassificationTypeCode(exceptionLookupPO.getClassificationTypeCode());
        mbsExceptionLookup.setLogLevel(exceptionLookupPO.getLogLevel());
        return mbsExceptionLookup;
    }

}
