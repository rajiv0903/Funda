/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.util.CollectionUtils;
import com.fanniemae.mbsportal.model.MBSExceptionLookup;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.exception.DataExceptionMessage;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessWarning;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;
import com.fanniemae.mbsportal.utils.exception.MBSSystemWarning;
import com.fanniemae.mbsportal.validator.Validator;

/**
 * This class handles the service part for the ExceptionLookup based requests
 * 
 * @author g8upjv
 *
 */
@SuppressWarnings("rawtypes")
@Service
public class MBSExceptionService extends BaseProcessor {
        
        /**
         * 
         * LOGGER Logger
         */
//	@InjectLog
//	private Logger LOGGER;
    private static final Logger LOGGER = LoggerFactory.getLogger(MBSExceptionService.class);

	
	
	/**
	 * 
	 * NO_DATA_EXCEPTION String
	 */
	private String NO_DATA_EXCEPTION = "There is no data to create/update";


	/**
	 * 
	 * @param exceptionLookupTransformer Transformer the exceptionLookupTransformer
	 * @param exceptionLookupPersister Persister the exceptionLookupPersister
	 * @param exceptionLookupPOTransformer Transformer the exceptionLookupPOTransformer
	 */
	@SuppressWarnings("unchecked")
	@Autowired
	public MBSExceptionService(Transformer exceptionTransformer, Persister exceptionPersister,
			Transformer exceptionPOTransformer, Validator exceptionPOValidator) {
		super.transformer = exceptionTransformer;
		super.persister = exceptionPersister;
		super.poTransformer = exceptionPOTransformer;
		super.validator = exceptionPOValidator;
	}

	/**
	 * 
	 * @param exceptionLookupPOLst List<ExceptionLookupPO> the exceptionLookupPOLst
	 * @return List<ExceptionLookupPO>
	 * @throws MBSBaseException
	 */
	public List<ExceptionLookupPO> createExceptionLookupData(List<ExceptionLookupPO> exceptionLookupPOLst) throws MBSBaseException {
		LOGGER.debug("Entering createExceptionLookupData method in ExceptionLookupService");
		TransformationObject transformationObject = new TransformationObject();//mbsObjectCreator.getTransformationObject();
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
		TransformationObject transObj = new TransformationObject();//mbsObjectCreator.getTransformationObject();
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
        

        /**
         * 
         * 
         * @param caller
         * @param errorCode
         * @param messageArgs
         * @return MBSBaseException
         * @throws MBSBaseException
         */
        public MBSBaseException createBusinessExceptionAndLog(Object caller, String errorCode, Object...loggerMessageArgs) throws MBSBaseException{
            
            return createExceptionByCodeAndLog(caller, errorCode, MBSExceptionConstants.BUSINESS_EXCEPTION, loggerMessageArgs);
        }
        
        /**
         * 
         * 
         * @param caller
         * @param errorCode
         * @param messageArgs
         * @return MBSBaseException
         * @throws MBSBaseException
         */
        public MBSBaseException createSystemExceptionAndLog(Object caller, String errorCode, Object...loggerMessageArgs) throws MBSBaseException{
            
            return createExceptionByCodeAndLog(caller, errorCode, MBSExceptionConstants.SYSTEM_EXCEPTION, loggerMessageArgs);
        }
        
        /**
         * 
         * 
         * @param caller
         * @param errorCode
         * @param messageArgs
         * @return MBSBaseException
         * @throws MBSBaseException
         */
        public MBSBaseException createBusinessWarningnAndLog(Object caller, String errorCode, Object...loggerMessageArgs) throws MBSBaseException{
            
            return createExceptionByCodeAndLog(caller, errorCode, MBSExceptionConstants.BUSINESS_WARNING, loggerMessageArgs);
        }
        
        /**
         * 
         * 
         * @param caller
         * @param errorCode
         * @param messageArgs
         * @return MBSBaseException
         * @throws MBSBaseException
         */
        public MBSBaseException createSystemWarningnAndLog(Object caller, String errorCode, Object...loggerMessageArgs) throws MBSBaseException{
            
            return createExceptionByCodeAndLog(caller, errorCode, MBSExceptionConstants.SYSTEM_WARNING, loggerMessageArgs);
        }
        
       
        /**
         * 
         * @param caller
         * @param errorObjects
         * @param loggerMessageArgs
         * @return
         * @throws MBSBaseException
         */
        public void createBusinessExceptionsAndLog(Object caller, List<DataExceptionMessage> errorObjects, Object... loggerMessageArgs) throws MBSBaseException {
            
            createExceptionByCodeAndLog(caller, errorObjects, MBSExceptionConstants.BUSINESS_EXCEPTION,  loggerMessageArgs);
        }
        
        
      
        
        /**
         * 
         * 
         * @param caller
         * @param errorCode
         * @param exceptionId
         * @param loggerMessageArgs
         * @return MBSBaseException
         * @throws MBSBaseException
         */
        public MBSBaseException createExceptionByCodeAndLog(Object caller, String errorCode, Long exceptionId, Object...loggerMessageArgs) throws MBSBaseException{
            
            MBSBaseException mbsBaseException = null;
            Optional<String> errorCodeOpt = Optional.of(errorCode);
            List<ExceptionLookupPO> excpetionLookupPOLst = getExceptionLookupData(errorCodeOpt);
            if(Objects.nonNull(excpetionLookupPOLst) && excpetionLookupPOLst.size()>0){
                ExceptionLookupPO exceptionLookupPO =  excpetionLookupPOLst.get(0);
                if(Objects.nonNull(exceptionLookupPO)){
                    if(MBSExceptionConstants.SYSTEM_EXCEPTION.equals(exceptionId)){
                        mbsBaseException = new MBSSystemException(exceptionLookupPO);
                    } else if (MBSExceptionConstants.BUSINESS_EXCEPTION.equals(exceptionId)){
                        mbsBaseException = new MBSBusinessException(exceptionLookupPO);
                    }else if (MBSExceptionConstants.BUSINESS_WARNING.equals(exceptionId)){
                        mbsBaseException = new MBSBusinessWarning(exceptionLookupPO);
                    }else if (MBSExceptionConstants.SYSTEM_WARNING.equals(exceptionId)){
                        mbsBaseException = new MBSSystemWarning(exceptionLookupPO);
                    }
                    logMessage(caller, exceptionLookupPO, loggerMessageArgs);
                }
            }
            return mbsBaseException;
        }
        
        /**
         * 
         * @param caller
         * @param dataExceptionMessages
         * @param exceptionId
         * @param loggerMessageArgs
         * @return
         * @throws MBSBaseException
         */
        public void createExceptionByCodeAndLog(Object caller, List<DataExceptionMessage> dataExceptionMessages, Long exceptionId, Object...loggerMessageArgs) throws MBSBaseException {
        	MBSBaseException mbsBaseException = null;
        	List<ExceptionLookupPO> exceptionPOMsgs = new ArrayList<ExceptionLookupPO>();
        	if (!CollectionUtils.isNullOrEmpty(dataExceptionMessages)) {
	        	for (DataExceptionMessage dataExceptionMessage : dataExceptionMessages) {
	        		Optional<String> errorCodeOpt = Optional.of(dataExceptionMessage.getErrorCode());
	                List<ExceptionLookupPO> excpetionLookupPOLst = getExceptionLookupData(errorCodeOpt);
	                if(Objects.nonNull(excpetionLookupPOLst) && excpetionLookupPOLst.size()>0){
	                    ExceptionLookupPO exceptionLookupPO =  excpetionLookupPOLst.get(0);
	                    if(Objects.nonNull(exceptionLookupPO)){
	                        if(MBSExceptionConstants.SYSTEM_EXCEPTION.equals(exceptionId)){
	                            mbsBaseException = new MBSSystemException(exceptionLookupPO);
	                        } else if (MBSExceptionConstants.BUSINESS_EXCEPTION.equals(exceptionId)){
	                        	String errorMessage = exceptionLookupPO.getErrorMessage();
	                        	errorMessage = errorMessage +  " " + dataExceptionMessage.getFieldMessage();
	                        	exceptionLookupPO.setErrorMessage(errorMessage);
	                        	exceptionPOMsgs.add(exceptionLookupPO);	                            
	                        }
	                        logMessage(caller, exceptionLookupPO, dataExceptionMessage.getFieldMessage());
	                    }
	                }
	        	}
	        	if (exceptionPOMsgs.size() > 0) {
	        		throw new MBSBusinessException("BAD_INPUT", exceptionPOMsgs);
	        	}
        	}
        }
        
        /**
         * 
         * @param caller
         * @param exceptionLookupPO
         * @param loggerMessageArgs
         */
        //Need to validate this method for different scenarios
        public void logMessage(Object caller, ExceptionLookupPO exceptionLookupPO, Object...loggerMessageArgs){
            
            Logger log = LoggerFactory.getLogger(caller.getClass());
            String logLevel = exceptionLookupPO.getLogLevel();
            if(StringUtils.isBlank(logLevel)){
                logLevel = StringUtils.EMPTY;
            }
            switch (logLevel) {
                case MBSExceptionConstants.LOG_LEVEL_ERROR:
                    log.error(exceptionLookupPO.getLoggerMessage(), loggerMessageArgs);
                    break;
    
                case MBSExceptionConstants.LOG_LEVEL_INFO:
                    log.info(exceptionLookupPO.getLoggerMessage(), loggerMessageArgs);
                    break;
    
                case MBSExceptionConstants.LOG_LEVEL_DEBUG:
                    log.debug(exceptionLookupPO.getLoggerMessage(), loggerMessageArgs);
                    break;
    
                case MBSExceptionConstants.LOG_LEVEL_WARN:
                    log.warn(exceptionLookupPO.getLoggerMessage(), loggerMessageArgs);
                    break;
    
                default:
                    log.debug(exceptionLookupPO.getLoggerMessage(), loggerMessageArgs);
                    break;
            }
            
        }
}
