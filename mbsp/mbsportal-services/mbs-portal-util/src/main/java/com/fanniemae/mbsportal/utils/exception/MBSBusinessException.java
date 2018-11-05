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

import java.util.List;

/**
 * This is the base class for Business exception
 * 
 * @author g8upjv
 * @date 01/20/2018
 *
 */
public class MBSBusinessException extends MBSBaseException {
        
       /**
        * 
        *  serialVersionUID long
        */
        private static final long serialVersionUID = 6407005224349091728L;
        
        /**
         * 
         * @param errMsg String
         * @param excepList ExceptionLookupPO
         */
        //TODO: replace the usage of this method the contructor having only exception
        public MBSBusinessException(String errMsg, ExceptionLookupPO exceptionLookupPO) {
                super(errMsg, MBSExceptionConstants.BUSINESS_EXCEPTION, exceptionLookupPO);
        }
        
        /**
         * 
         * @param excepList ExceptionLookupPO
         */
        public MBSBusinessException(ExceptionLookupPO exceptionLookupPO) {
                super(exceptionLookupPO.getErrorMessage(), MBSExceptionConstants.BUSINESS_EXCEPTION, exceptionLookupPO);
        }
        
        
        /**
         * 
         * @param excepList ExceptionLookupPO
         */
        public MBSBusinessException(String errMsg, List<ExceptionLookupPO> exceptionLookupPO) {
                super(errMsg, MBSExceptionConstants.BUSINESS_EXCEPTION, exceptionLookupPO);
        }
        
        
        /**
         * 
         * @param errMsg String
         */
        public MBSBusinessException(String errMsg) {
                super(errMsg, MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        
        /**
         * 
         * @param errMsg
         * @param processId
         */
        public MBSBusinessException(String errMsg, long processId) {
                super(errMsg, processId);
        }
        
        /**
         * 
         * @param errMsg
         * @param processId
         * @param rootExp
         */
        public MBSBusinessException(String errMsg, long processId, Exception rootExp) {
                super(errMsg, processId, rootExp);
        }
        
        /**
         * 
         * 
         * @param errMsg String
         * @param rootExp Exception
         * @param ExceptionLookupPO
         */
        //TODO: to be replaced by below constructor
        public MBSBusinessException(String errMsg, Exception rootExp, ExceptionLookupPO exceptionLookupPO) {
            super(errMsg, MBSExceptionConstants.BUSINESS_EXCEPTION, rootExp, exceptionLookupPO);
        }
        
        /**
         * 
         * 
         * @param errMsg String
         * @param rootExp Exception
         * @param ExceptionLookupPO
         */
        public MBSBusinessException(Exception rootExp, ExceptionLookupPO exceptionLookupPO) {
            super(exceptionLookupPO.getErrorMessage(), MBSExceptionConstants.BUSINESS_EXCEPTION, rootExp, exceptionLookupPO);
        }

}
