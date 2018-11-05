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

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import org.slf4j.MDC;

public class MBSExceptionConstants {
        
        public static final String ERROR_TYPE = "ERROR_TYPE";
        public static final String ERROR_PROCESS_ID = "ERROR_PROCESS_ID";
        public static final String ERROR_MESSGE = "ERROR_MESSGE";
        public static final String ERROR_SOURCE_METHOD = "ERROR_SOURCE_METHOD";
        public static final String ERROR_SOURCE_METHOD_SIGNATURE = "ERROR_SOURCE_METHOD_SIGNATURE";
        public static final String SOURCE_METHOD_ARGS = "SOURCE_METHOD_ARGS";
        
        public static final String INFO_USER_ID = "USER_ID";
        public static final String INFO_TRANSACTION_ID = "TRANSACTION_ID";
        
        // Constant Related to UI Logging
        public static final String UI_LOGGING_USER_ID = "USER_ID";
        public static final String UI_LOGGING_HTTP_CODE = "HTTP_CODE";
        public static final String UI_LOGGING_CHANNEL = "CHANNEL";
        public static final String UI_LOGGING_TRANSACTION_ID = "TRANSACTION_ID";
        public static final String UI_LOGGING_MSG_TYPE = "MSG_TYPE";
        public static final String UI_LOGGING_MODULE = "MODULE";
        public static final String UI_LOGGING_PAGE = "PAGE";
        public static final String UI_LOGGING_FUNCTIONALITY = "FUNCTIONALITY";
        public static final String UI_LOGGING_MSG = "MESSAGE";
        public static final String UI_LOGGING_TIME_STAMP = "TIME_STAMP";
        
        public static final Long SYSTEM_EXCEPTION = 10001L;
        public static final Long BUSINESS_EXCEPTION = 20001L;
        public static final Long BUSINESS_WARNING = 30001L;
        public static final Long SYSTEM_WARNING = 40001L;
        
        /**
         * BASE_EXCEPTION_IDENTIFIER String
         */
        public static final String BASE_EXCEPTION_IDENTIFIER = "MBSP_ERROR";
        /**
         * BUSINESS_EXCEPTION_IDENTIFIER String
         */
        public static final String BUSINESS_EXCEPTION_IDENTIFIER = "MBSP_BUSINESS_ERROR";
        
        /**
         * SYSTEM_EXCEPTION_IDENTIFIER String
         */
        public static final String SYSTEM_EXCEPTION_IDENTIFIER = "MBSP_SYSTEM_ERROR";
        
        /**
         * BUSINESS_WARNING_IDENTIFIER String
         */
        public static final String BUSINESS_WARNING_IDENTIFIER = "MBSP_BUSINESS_WARNING";
        
        /**
         * SYSTEM_WARNING_IDENTIFIER String
         */
        public static final String SYSTEM_WARNING_IDENTIFIER = "MBSP_SYSTEM_WARNING";
        
        /**
         * LOG_LEVEL_ERROR String
         */
        public static final String LOG_LEVEL_ERROR = "error";
        
        /**
         * LOG_LEVEL_WARN String
         */
        public static final String LOG_LEVEL_WARN = "warn";
        
        /**
         * LOG_LEVEL_DEBUG String
         */
        public static final String LOG_LEVEL_DEBUG = "debug";
        /**
         * LOG_LEVEL_INFO String
         */
        public static final String LOG_LEVEL_INFO = "info";
        
        //Error messages
        
        /**
         * DISPLAY_MESSAGE String
         */
        public static final String DISPLAY_MESSAGE = "DISP";
        
        /**
         * Internal Server error
         * <p>
         * SYSM_0002_ERROR_MESSAGE String
         */
        public static final String DO_NOT_DISPLAY_MESSAGE = "NO_DISP";
        
        /**
         * SYSM_00001 String
         */
        public static final String SYSM_0001 = "SYSM_0001";
        
        /**
         * Internal Server error
         * <p>
         * SYSM_00002 String
         */
        public static final String SYSM_0002 = "SYSM_0002";
        /**
         * Internal Server error
         * <p>
         * SYSM_0002_ERROR_MESSAGE String
         */
        public static final String SYSM_0002_ERROR_MESSAGE = "Internal Server Error";
        
        /**
         * Internal Server error
         * <p>
         * INTSRV_00002 String
         */
        public static final String SYSM_0003 = "SYSM_0003";
        
        /**
         * Transaction validation 5001
         * <p>
         * TRAN_5001 String
         */
        public static final String TRAN_5001 = "TRAN_5001";
        
        /**
         * Transaction validation 5001
         * <p>
         * TRAN_5002 String
         */
        public static final String TRAN_5002 = "TRAN_5002";
        
        /**
         * Transaction validation 5003
         * <p>
         * TRAN_5003 String
         */
        public static final String TRAN_5003 = "TRAN_5003";
        
        /**
         * Transaction validation 5004
         * <p>
         * TRAN_5004 String
         */
        public static final String TRAN_5004 = "TRAN_5004";
        
        // CMMBSSTA01-1802 starts
        /**
         * page validation 0001 for empty or null fields - accepted trade, sort by, sort order, page size, page index
         * <p>
         * PAGE_0001 String
         */
        public static final String PAGE_1000 = "PAGE_1000";
        
        /**
         * page validation 0002 for invalid fields - accepted trade, sort by, sort order, page size, page index
         * <p>
         * PAGE_0001 String
         */
        public static final String PAGE_1001 = "PAGE_1001";
        
        //CMMBSSTA01-1802  - ends
        
        /**
         * Utility method to log for background tasks
         *
         * @param errType
         * @param errProcId
         * @param errMsg
         * @param errSrcMethod
         * @param errSrcMethodSignature
         * @param errSrcMethodArgs
         */
        public static void logItForAlert(String errType, String errProcId, String errMsg, String errSrcMethod,
                String errSrcMethodSignature, String errSrcMethodArgs) {
                
                MDC.put(MBSExceptionConstants.ERROR_TYPE, errType);
                MDC.put(MBSExceptionConstants.ERROR_PROCESS_ID, errProcId);
                MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD, errSrcMethod);
                MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD_SIGNATURE, errSrcMethodSignature);
                MDC.put(MBSExceptionConstants.SOURCE_METHOD_ARGS, errSrcMethodArgs);
                MDC.put(MBSExceptionConstants.ERROR_MESSGE, errMsg);
        }
        
        /**
         * Simplified version of logging . //TODO: improve more
         *
         * @param errType
         * @param errProcId
         * @param errMsg
         * @param method
         */
        public static void logItForAlert(String errType, String errProcId, String errMsg, Method method, Throwable t) {
                
                MDC.put(MBSExceptionConstants.ERROR_TYPE, errType);
                MDC.put(MBSExceptionConstants.ERROR_PROCESS_ID, errProcId);
                if(method == null) {
                        MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD, "No Method Info");
                        MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD_SIGNATURE, "No Method Signature Info");
                } else {
                        MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD, method.getName());
                        MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD_SIGNATURE, getParametersFormatted(method));
                }
                MDC.put(MBSExceptionConstants.SOURCE_METHOD_ARGS, "Not Needed");
                MDC.put(MBSExceptionConstants.ERROR_MESSGE, errMsg);
        }
        
        /**
         * getParametersFormatted  //TODO: improve more
         *
         * @param method
         * @return
         */
        private static String getParametersFormatted(Method method) {
                Parameter[] parameters = method.getParameters();
                StringBuilder sb = new StringBuilder();
                for(Parameter p : parameters) {
                        if(p.isNamePresent()) {
                                sb.append(
                                        "name: " + p.getName() + " Type:" + p.getParameterizedType() + "Value: " + " ");
                        }
                }
                return sb.toString();
        }
        
        /**
         * resetLogAlert
         */
        public static void resetLogAlert() {
                MDC.remove(MBSExceptionConstants.ERROR_TYPE);
                MDC.remove(MBSExceptionConstants.ERROR_PROCESS_ID);
                MDC.remove(MBSExceptionConstants.ERROR_SOURCE_METHOD);
                MDC.remove(MBSExceptionConstants.ERROR_SOURCE_METHOD_SIGNATURE);
                MDC.remove(MBSExceptionConstants.SOURCE_METHOD_ARGS);
                MDC.remove(MBSExceptionConstants.ERROR_MESSGE);
        }
}
