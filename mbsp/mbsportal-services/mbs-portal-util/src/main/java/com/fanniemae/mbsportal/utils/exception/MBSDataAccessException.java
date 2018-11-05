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

package com.fanniemae.mbsportal.utils.exception;

/**
 * This is the base class for Data access exception
 * 
 * @author g8upjv
 * @date 01/20/2018
 *
 */
public class MBSDataAccessException extends MBSSystemException{

    /**
     * 
     * serialVersionUID long
     */
    private static final long serialVersionUID = 6407005224349091728L;
    
    /**
     * 
     * @param errMsg String
     * @param processId long
     */
    public MBSDataAccessException(String errMsg) {
        super( errMsg,  MBSExceptionConstants.SYSTEM_EXCEPTION);
    }
    
    /**
     * 
     * 
     * @param errMsg String
     * @param exceptionLookupPO ExceptionLookupPO
     */
    public MBSDataAccessException(String errMsg, ExceptionLookupPO exceptionLookupPO) {
        super( errMsg, exceptionLookupPO);
    }
    
    /**
     * 
     * @param errMsg
     * @param processId
     * @param rootExp
     */
    public MBSDataAccessException(String errMsg, long processId,  Exception rootExp) {
        super( errMsg,  processId, rootExp);
    }
}
