/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.controller.helper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.aspect.exception.ExceptionTracingAnnotation;
import com.fanniemae.mbsportal.service.MBSExceptionService;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;

/**
 *
 * The Helper class for Exception Lookup Controller
 *
 * @author g8upjv
 *
 */
@Component
public class ExceptionLookupControllerHelper extends BaseControllerHelper {

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionLookupControllerHelper.class);

    /**
     *
     * mbsExceptionService MBSExceptionService
     */
    @Autowired
    private MBSExceptionService mbsExceptionService;

    /**
     *
     * Create ExceptionLookupData
     *
     * @param exceptionLookupPOLst
     *            List<ExceptionLookupPO> the exceptionLookupPOLst
     * @return ExceptionLookupPO
     * @throws MBSBaseException
     */
    @SuppressWarnings("rawtypes")
    @ExceptionTracingAnnotation
    public List<ExceptionLookupPO> createExceptionLookupData(List<ExceptionLookupPO> exceptionLookupPOLst)
            throws MBSBaseException {
        LOGGER.debug("Entering createExceptionLookupData method in ExceptionLookupControllerHelper");
        String mediaType = MediaType.APPLICATION_JSON_VALUE;
        List<ExceptionLookupPO> exceptionLookupPOLstResponse;

        if (Objects.equals(exceptionLookupPOLst, null) || exceptionLookupPOLst.size() == 0) {
            LOGGER.error("Failed to create ExceptionLookup data, exceptionLookupPOLst(input) object is null/empty");
            throw new MBSBusinessException(
                    "Failed to create ExceptionLookup data, exceptionLookupPOLst(input) object is null/empty",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        } else {
            // exceptionLookupPOLstResponse = ((ExceptionLookupService)
            // exceptionLookupServicev1).createExceptionLookupData(exceptionLookupPOLst);
            exceptionLookupPOLstResponse = ((MBSExceptionService) mbsExceptionService).createExceptionLookupData(exceptionLookupPOLst);
        }
        LOGGER.debug("Exiting createExceptionLookupData method in ExceptionLookupControllerHelper");
        return exceptionLookupPOLstResponse;
    }

    /**
     *
     * clears all exception look up data
     *
     * @throws MBSBaseException
     */
    @ExceptionTracingAnnotation
    public void clearAllExceptionLookupData(Optional<String> errorCode) throws MBSBaseException {
        LOGGER.debug("Entering clearAllExceptionLookupData method in ExceptionLookupControllerHelper");
        mbsExceptionService.clearData(errorCode);
        LOGGER.debug("Exiting clearAllExceptionLookupData method in ExceptionLookupControllerHelper");
    }

    /**
     *
     * get the list of ExceptionLookup
     *
     * @param errorCode
     *            String
     * @return List<ExceptionLookupPO>
     * @throws MBSBaseException
     *
     */
    @SuppressWarnings("rawtypes")
    @ExceptionTracingAnnotation
    public List<ExceptionLookupPO> getExceptionLookupData(Optional<String> errorCode) throws MBSBaseException {
        LOGGER.debug("Entering getExceptionLookupData method in ExceptionLookupControllerHelper");
        String mediaType = MediaType.APPLICATION_JSON_VALUE;
        List<ExceptionLookupPO> lstExceptionLookupPO;
        lstExceptionLookupPO = ((MBSExceptionService) mbsExceptionService).getExceptionLookupData(errorCode);
        LOGGER.debug("Exiting getExceptionLookupData method in ExceptionLookupControllerHelper");
        return lstExceptionLookupPO;
    }

}
