/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.model.MBSExceptionLookup;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import com.fanniemae.mbsportal.validator.Validator;

/**
 * This class handles the service part for the ExceptionLookup based requests
 * 
 * @author g8upjv
 *
 */
//Replaced by MBSExceptionService
@SuppressWarnings("rawtypes")
@Service
@Deprecated
public class ExceptionLookupService extends BaseProcessor {
        
        /**
         * 
         * LOGGER Logger
         */
	@InjectLog
	private Logger LOGGER;
	
	/**
	 * 
	 * NO_DATA_EXCEPTION String
	 */
	private String NO_DATA_EXCEPTION = "There is no data to create/update";
	
	/**
	 * 
	 * mbsObjectCreator MBSObjectCreator
	 */
	@Autowired
	MBSObjectCreator mbsObjectCreator;


	/**
	 * 
	 * @param exceptionLookupTransformer Transformer the exceptionLookupTransformer
	 * @param exceptionLookupPersister Persister the exceptionLookupPersister
	 * @param exceptionLookupPOTransformer Transformer the exceptionLookupPOTransformer
	 */
	@SuppressWarnings("unchecked")
	@Autowired
	public ExceptionLookupService(Transformer exceptionLookupTransformer, Persister exceptionLookupPersister,
			Transformer exceptionLookupPOTransformer, Validator exceptionLookupPOValidator) {
		super.transformer = exceptionLookupTransformer;
		super.persister = exceptionLookupPersister;
		super.poTransformer = exceptionLookupPOTransformer;
		super.validator = exceptionLookupPOValidator;
	}

	/**
	 * 
	 * @param exceptionLookupPOLst List<ExceptionLookupPO> the exceptionLookupPOLst
	 * @return List<ExceptionLookupPO>
	 * @throws MBSBaseException
	 */
	public List<ExceptionLookupPO> createExceptionLookupData(List<ExceptionLookupPO> exceptionLookupPOLst) throws MBSBaseException {
		LOGGER.debug("Entering createExceptionLookupData method in ExceptionLookupService");
		TransformationObject transformationObject = mbsObjectCreator.getTransformationObject();
		transformationObject.setSourcePojo(exceptionLookupPOLst);
		if(Objects.isNull(exceptionLookupPOLst) || exceptionLookupPOLst.size() <= 0){
		    LOGGER.error(NO_DATA_EXCEPTION);
		    throw new MBSBusinessException(NO_DATA_EXCEPTION, MBSExceptionConstants.BUSINESS_EXCEPTION);
		}
		super.processRequest(transformationObject);
		LOGGER.debug("Exiting createExceptionLookupData method in ExceptionLookupService");
		return (List<ExceptionLookupPO>) transformationObject.getSourcePojo();
	}

	/**
	 * 
	 * This method clears the ExceptionLookup records
	 * 
	 * @throws MBSBaseException
	 */
	public void clearData(Optional<String> errorCode) throws MBSBaseException {
	    if(errorCode.isPresent() && StringUtils.isNotEmpty(errorCode.get())) {
	        persister.clear(errorCode.get());    
	    } else {
	        persister.clearAll();
	    }
	}

	/**
	 * 
	 * This method retrieves the list of ExceptionLookup
	 * 
	 * @return List<ExceptionLookupPO>
	 * @throws MBSBaseException
	 */
	@SuppressWarnings("unchecked")
	public List<ExceptionLookupPO> getExceptionLookupData(Optional<String> errorCode) throws MBSBaseException {

		LOGGER.debug("Entering getExceptionLookupData method in ExceptionLookupService");

		List<ExceptionLookupPO> listExceptionLookupPO = new ArrayList<ExceptionLookupPO>();
		List<MBSExceptionLookup> listMBSExceptionLookup = new ArrayList<MBSExceptionLookup>();
		MBSExceptionLookup mbsExceptionLookup;
		if(errorCode.isPresent() && StringUtils.isNotEmpty(errorCode.get())) {
		    mbsExceptionLookup = (MBSExceptionLookup) persister.getBaseDao().getById(errorCode.get());
		    if(Objects.nonNull(mbsExceptionLookup)){
		        listMBSExceptionLookup.add(mbsExceptionLookup); 
		    }
		} else {
		    listMBSExceptionLookup = persister.getBaseDao().getAll();
		}
		TransformationObject transObj = mbsObjectCreator.getTransformationObject();
		if (!Objects.equals(listMBSExceptionLookup, null) && listMBSExceptionLookup.size() >0) {
		    transObj.setTargetPojo(listMBSExceptionLookup);
		    listExceptionLookupPO = (List<ExceptionLookupPO>) poTransformer.transform(transObj).getSourcePojo();
		}
		LOGGER.debug("Exiting getExceptionLookupData method in ExceptionLookupService");
		return listExceptionLookupPO;
	}
	
	       /**
         * 
         * This method retrieves the map of ExceptionLookup
         * 
         * @return List<ExceptionLookupPO>
         * @throws MBSBaseException
         */
        @SuppressWarnings("unchecked")
        public Map<String, ExceptionLookupPO> getExceptionLookupDataMap(Optional<String> errorCode) throws MBSBaseException {

                LOGGER.debug("Entering getExceptionLookupDataMap method in ExceptionLookupService");
                Map<String, ExceptionLookupPO> mapExceptionLookupPO = new HashMap<String, ExceptionLookupPO>();
                List<ExceptionLookupPO> listExceptionLookupPO = getExceptionLookupData(errorCode);
                for(ExceptionLookupPO exceptionLookupPO: listExceptionLookupPO){
                    mapExceptionLookupPO.put(exceptionLookupPO.getErrorCode(), exceptionLookupPO);
                }
                
                LOGGER.debug("Exiting getExceptionLookupDataMap method in ExceptionLookupService");
                return mapExceptionLookupPO;
        }

}
