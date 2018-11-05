/*
 * Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 * reserved under the copyright laws of the United States and international
 * conventions. Use of a copyright notice is precautionary only and does not
 * imply publication or disclosure. This software contains confidential
 * information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 * is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.config;

import com.fanniemae.mbsportal.api.utils.MBSAsyncExceptionHandler;
import com.fanniemae.mbsportal.api.utils.MBSThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncConfigurer;

/**
 * One place config to control thread pools application level
 *
 * @author g8uaxt Created on 7/13/2018.
 */
//@Configuration
//@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
        /**
         * Logger
         */
        private static final Logger LOGGER = LoggerFactory.getLogger(AsyncConfig.class);
        @Autowired
        private MBSAsyncExceptionHandler mbsAsyncExceptionHandler;
        
        @Autowired
        private MBSThreadPoolTaskExecutor mbsThreadPoolTaskExecutor;
        
        @Override
        public Executor getAsyncExecutor() {
                LOGGER.debug("ThreadPoolTaskExecutor Active count: "+mbsThreadPoolTaskExecutor.getActiveCount());
                return mbsThreadPoolTaskExecutor;
        }
        
        @Override
        public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
                LOGGER.debug("binding MBSAsyncExceptionHandler "+mbsAsyncExceptionHandler);
                return mbsAsyncExceptionHandler;
        }
}
