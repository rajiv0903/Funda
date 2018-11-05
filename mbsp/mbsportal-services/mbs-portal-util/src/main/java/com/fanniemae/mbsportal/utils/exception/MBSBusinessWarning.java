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
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Aug 1, 2018
 * @File: com.fanniemae.mbsportal.utils.exception.MBSBusinessWarning.java 
 * @Revision: 
 * @Description: MBSBusinessWarning.java
 */
public class MBSBusinessWarning extends MBSBaseException {
        
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
        public MBSBusinessWarning(String errMsg, ExceptionLookupPO exceptionLookupPO) {
                super(errMsg, MBSExceptionConstants.BUSINESS_WARNING, exceptionLookupPO);
        }
        
        /**
         * 
         * @param excepList ExceptionLookupPO
         */
        public MBSBusinessWarning(ExceptionLookupPO exceptionLookupPO) {
                super(exceptionLookupPO.getErrorMessage(), MBSExceptionConstants.BUSINESS_WARNING, exceptionLookupPO);
        }
        
        
        /**
         * 
         * @param excepList ExceptionLookupPO
         */
        public MBSBusinessWarning(String errMsg, List<ExceptionLookupPO> exceptionLookupPO) {
                super(errMsg, MBSExceptionConstants.BUSINESS_WARNING, exceptionLookupPO);
        }
        
        
        /**
         * 
         * @param errMsg String
         */
        public MBSBusinessWarning(String errMsg) {
                super(errMsg, MBSExceptionConstants.BUSINESS_WARNING);
        }
        
        /**
         * 
         * @param errMsg
         * @param processId
         */
        public MBSBusinessWarning(String errMsg, long processId) {
                super(errMsg, processId);
        }
        
        /**
         * 
         * @param errMsg
         * @param processId
         * @param rootExp
         */
        public MBSBusinessWarning(String errMsg, long processId, Exception rootExp) {
                super(errMsg, processId, rootExp);
        }
        
        /**
         * 
         * 
         * @param errMsg String
         * @param rootExp Exception
         * @param ExceptionLookupPO
         */
        public MBSBusinessWarning(String errMsg, Exception rootExp, ExceptionLookupPO exceptionLookupPO) {
            super(errMsg, MBSExceptionConstants.BUSINESS_WARNING, rootExp, exceptionLookupPO);
        }
        
        /**
         * 
         * 
         * @param errMsg String
         * @param rootExp Exception
         * @param ExceptionLookupPO
         */
        public MBSBusinessWarning(Exception rootExp, ExceptionLookupPO exceptionLookupPO) {
            super(exceptionLookupPO.getErrorMessage(), MBSExceptionConstants.BUSINESS_WARNING, rootExp, exceptionLookupPO);
        }

}
