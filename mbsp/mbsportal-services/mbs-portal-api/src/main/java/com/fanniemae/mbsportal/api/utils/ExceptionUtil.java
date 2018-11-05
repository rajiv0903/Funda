/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fanniemae.mbsportal.api.persister.ExceptionLookupPersister;
import com.fanniemae.mbsportal.model.MBSExceptionLookup;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * 
 * Util class to retrive exception details
 * 
 * @author g8upjv
 *
 */
public class ExceptionUtil {

    /**
     * 
     * LOGGER Logger
     */
    @InjectLog
    private Logger LOGGER;
    
    private ExceptionUtil(){
        
    }
    
    /**
     * 
     * exceptionLookupPersister  ExceptionLookupPersister
     */
    @Autowired
    ExceptionLookupPersister exceptionLookupPersister;
    
    /**
     * 
     * 
     * @param errorCode String
     * @return MBSExceptionLookup
     * @throws MBSBaseException
     */
    public MBSExceptionLookup getMBSExceptionData(String errorCode) throws MBSBaseException{
        MBSExceptionLookup mbsExceptionLookup;
        if(StringUtils.isBlank(errorCode)){
            mbsExceptionLookup = null;
        } else {
            try {
                mbsExceptionLookup = (MBSExceptionLookup) exceptionLookupPersister.getBaseDao().getById(errorCode.trim());
            } catch (MBSBaseException e) {
                LOGGER.error("Error when retrieving exception data message in getMBSExceptionData",e);
                throw e;
            }
        }
        return mbsExceptionLookup;
    }
}
