/*
 * Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 * reserved under the copyright laws of the United States and international
 * conventions. Use of a copyright notice is precautionary only and does not
 * imply publication or disclosure. This software contains confidential
 * information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 * is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.utils;

import com.fanniemae.mbsportal.utils.logging.InjectLog;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

/**
 * Handler to print any throwable exception in thread execution
 * @author g8uaxt Created on 7/13/2018.
 */
@Component
public class MBSAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
        /**
         *
         * LOGGER Logger variable
         */
        @InjectLog
        private Logger LOGGER;
        
        @Override
        public void handleUncaughtException(
                Throwable throwable, Method method, Object... obj) {
                //TODO: create Exception alert later
                LOGGER.error("Exception message - " + throwable.getMessage()+" Method name - " + method.getName());
                for (Object param : obj) {
                        LOGGER.error("Parameter value - " + param);
                }
        }
        
}
