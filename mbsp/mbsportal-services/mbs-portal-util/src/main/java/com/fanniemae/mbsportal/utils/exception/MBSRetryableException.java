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
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 11, 2018
 * @File: com.fanniemae.mbsportal.utils.exception.MBSRetryableException.java 
 * @Revision: 
 * @Description: MBSRetryableException.java
 */
public class MBSRetryableException extends MBSBaseException {
        
        private static final long serialVersionUID = 6407005224349091728L;
        
        /**
         *
         * @param errMsg String
         */
        public MBSRetryableException(String errMsg) {
                super(errMsg, MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        
       /**
        *
        * @param errMsg String
        * @param exceptionLookupPO ExceptionLookupPO
        */
       public MBSRetryableException(String errMsg, ExceptionLookupPO exceptionLookupPO) {
               super(errMsg, MBSExceptionConstants.SYSTEM_EXCEPTION, exceptionLookupPO);
       }
        
      /**
       *
       * @param exceptionLookupPO ExceptionLookupPO
       */
       public MBSRetryableException(ExceptionLookupPO exceptionLookupPO) {
           super(exceptionLookupPO.getErrorMessage(), MBSExceptionConstants.SYSTEM_EXCEPTION, exceptionLookupPO);
       }

       /**
         *
         * @param errMsg String
         * @param processId long
         */
        public MBSRetryableException(String errMsg, long processId) {
                super(errMsg, processId);
        }
        
        /**
         * MBSSystemException with root exception info
         * @param errMsg
         * @param rootExp
         */
        public MBSRetryableException(String errMsg, Exception rootExp,  ExceptionLookupPO exceptionLookupPO) {
                super(errMsg, MBSExceptionConstants.SYSTEM_EXCEPTION, rootExp, exceptionLookupPO);
        }
        
        /**
         * MBSSystemException with root exception info
         * @param errMsg
         * @param rootExp
         */
        public MBSRetryableException(Exception rootExp,  ExceptionLookupPO exceptionLookupPO) {
                super(exceptionLookupPO.getErrorMessage(), MBSExceptionConstants.SYSTEM_EXCEPTION, rootExp, exceptionLookupPO);
        }
        
        /**
         * 
         * @param errMsg 
         * @param rootExp
         */
        public MBSRetryableException(String errMsg,Exception rootExp) {
            super(errMsg, MBSExceptionConstants.SYSTEM_EXCEPTION, rootExp);
        }
        
        /**
         *
         * @param errMsg
         * @param processId
         * @param rootExp
         */
        public MBSRetryableException(String errMsg, long processId, Exception rootExp) {
                super(errMsg, processId, rootExp);
        }
        
}
