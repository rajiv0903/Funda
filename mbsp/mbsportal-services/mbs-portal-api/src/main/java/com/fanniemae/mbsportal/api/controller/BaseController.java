/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fanniemae.mbsportal.api.service.ExceptionLookupService;
import com.fanniemae.mbsportal.utils.ApiError;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.ExceptionResponsePO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSDataException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;

/**
 *
 * This class has the default methods for the logging the correlation
 *
 * 07/19/2017
 *
 * @author g8upjv
 *
 */
@RequestMapping(value = "")
public class BaseController {

    /**
     *
     * version1json MediaType
     */
    private MediaType version1json = new MediaType("application", "vnd.fnma-v1+json");

    /**
     *
     * exceptionLookupService ExceptionLookupService
     */
    @Autowired
    private ExceptionLookupService exceptionLookupService;

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionRequestController.class);

    /**
     *
     * @return MediaType the version1json
     */
    public MediaType getVersion1json() {
        return version1json;
    }

    /**
     *
     * This method returns the response based status codes
     *
     * @param responseCode
     * @param ex
     * @return ResponseEntity
     */
    public ResponseEntity<Object> getResponseEntityException(String className, String methodName, MBSBaseException ex) {
        LOGGER.debug("Entering getResponseEntityException in BaseController");

        // TODO: Add checks for UNAUTHORIZED and other status as required
        String responseCode = ex.getExceptionCode();
        LOGGER.debug("getResponseEntityException : ResponseCode: {}" , responseCode);
        if (responseCode.equals(TradeConstants.HTTP_STATUS_BAD_REQUEST)) {
 
            if (!Objects.isNull(ex.getExceptionLookupPO())) {
                List<ExceptionLookupPO> exceptionLookupPOLst = new ArrayList<ExceptionLookupPO>();
                exceptionLookupPOLst.add(ex.getExceptionLookupPO());
                ExceptionResponsePO exceptionResponsePO = MBSPortalUtils.getExceptionMessage(exceptionLookupPOLst);
                exceptionResponsePO.setStatus("BAD_REQUEST");
                LOGGER.debug("Exiting getResponseEntityException in BaseController");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponsePO);
            }

            if (CollectionUtils.isNotEmpty(ex.getExceptionLookupPOLst())) {
                ExceptionResponsePO exceptionResponsePO = MBSPortalUtils
                        .getExceptionMessage(ex.getExceptionLookupPOLst());
                exceptionResponsePO.setStatus("BAD_REQUEST");
                LOGGER.debug("Exiting getResponseEntityException in BaseController");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponsePO);
            }
            LOGGER.debug("Exiting getResponseEntityException in BaseController");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(ex.getRootExceptionMessage()));

        } else { 
            LOGGER.debug("Exiting getResponseEntityException in BaseController");
            return getResponseEntityInternalServerException(className, methodName, ex);
        }
    }

    /**
     *
     * This method returns the response for data validation exceptions
     *
     * @param className
     * @param methodName
     * @param ex
     * @return ResponseEntity
     */
    public ResponseEntity<Object> getResponseEntityDataException(String className, String methodName,
            MBSDataException ex) {
        LOGGER.debug("Entering getResponseEntityDataException in BaseController");
        if (!Objects.equals(ex, null)) {
            ExceptionResponsePO exceptionResponsePO = ex.getExcpRespPO();
            if (!Objects.isNull(exceptionResponsePO)) {
                exceptionResponsePO.setStatus("BAD_REQUEST");
                LOGGER.debug("Exiting getResponseEntityDataException in BaseController");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponsePO);
            }
        }
        LOGGER.debug("Exiting getResponseEntityDataException in BaseController");
        return getResponseEntityException(className, methodName, ex);
    }

    /**
     *
     * This method returns the response incase of exception/error for internal
     * server error
     *
     * @param className
     * @param methodName
     * @param ex
     * @return ResponseEntity
     */
    public ResponseEntity<Object> getResponseEntityInternalServerException(String className, String methodName,
            Exception ex) {
        LOGGER.debug("Entering getResponseEntityInternalServerException in BaseController");
        LOGGER.error("Failed in class " + className + " in method " + methodName + ". The exception :" + ex);
        List<ExceptionLookupPO> exceptionLookupPOLst;
        try {
            exceptionLookupPOLst = exceptionLookupService
                    .getExceptionLookupData(Optional.of(MBSExceptionConstants.SYSM_0002));
        } catch (MBSBaseException e) {
            LOGGER.error("Failed to retieve error messages" + e);
            LOGGER.debug("Exiting getResponseEntityInternalServerException in BaseController");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MBSPortalUtils.getExceptionInternalServer());
        }
        ExceptionResponsePO exceptionResponsePO = MBSPortalUtils.getExceptionMessage(exceptionLookupPOLst);
        exceptionResponsePO.setStatus("INTERNAL_SERVER_ERROR");
        LOGGER.debug("Exiting getResponseEntityInternalServerException in BaseController");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponsePO);
    }

}
