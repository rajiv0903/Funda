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
 * @File: com.fanniemae.mbsportal.utils.exception.MBSSystemWarning.java 
 * @Revision: 
 * @Description: MBSSystemWarning.java
 */
public class MBSSystemWarning extends MBSBaseException {
        
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
        public MBSSystemWarning(String errMsg, ExceptionLookupPO exceptionLookupPO) {
                super(errMsg, MBSExceptionConstants.SYSTEM_WARNING, exceptionLookupPO);
        }
        
        /**
         * 
         * @param excepList ExceptionLookupPO
         */
        public MBSSystemWarning(ExceptionLookupPO exceptionLookupPO) {
                super(exceptionLookupPO.getErrorMessage(), MBSExceptionConstants.SYSTEM_WARNING, exceptionLookupPO);
        }
        
        
        /**
         * 
         * @param excepList ExceptionLookupPO
         */
        public MBSSystemWarning(String errMsg, List<ExceptionLookupPO> exceptionLookupPO) {
                super(errMsg, MBSExceptionConstants.SYSTEM_WARNING, exceptionLookupPO);
        }
        
        
        /**
         * 
         * @param errMsg String
         */
        public MBSSystemWarning(String errMsg) {
                super(errMsg, MBSExceptionConstants.SYSTEM_WARNING);
        }
        
        /**
         * 
         * @param errMsg
         * @param processId
         */
        public MBSSystemWarning(String errMsg, long processId) {
                super(errMsg, processId);
        }
        
        /**
         * 
         * @param errMsg
         * @param processId
         * @param rootExp
         */
        public MBSSystemWarning(String errMsg, long processId, Exception rootExp) {
                super(errMsg, processId, rootExp);
        }
        
        /**
         * 
         * 
         * @param errMsg String
         * @param rootExp Exception
         * @param ExceptionLookupPO
         */
        public MBSSystemWarning(String errMsg, Exception rootExp, ExceptionLookupPO exceptionLookupPO) {
            super(errMsg, MBSExceptionConstants.SYSTEM_WARNING, rootExp, exceptionLookupPO);
        }
        
        /**
         * 
         * 
         * @param errMsg String
         * @param rootExp Exception
         * @param ExceptionLookupPO
         */
        public MBSSystemWarning(Exception rootExp, ExceptionLookupPO exceptionLookupPO) {
            super(exceptionLookupPO.getErrorMessage(), MBSExceptionConstants.SYSTEM_WARNING, rootExp, exceptionLookupPO);
        }

}
