/*
 * Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 * reserved under the copyright laws of the United States and international
 * conventions. Use of a copyright notice is precautionary only and does not
 * imply publication or disclosure. This software contains confidential
 * information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 * is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Default class for implementation
 *
 * @author g8uaxt Created on 7/13/2018.
 */

@SuppressWarnings("serial")
public class MBSThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {


    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MBSThreadPoolTaskExecutor.class);

    // TODO: start with default but extend it when go for customization

    /**
     * print stat if enabled
     */
    public void printStats() {
        LOGGER.debug("MBSThreadPoolTaskExecutor stats-->  Active count={}, PoolSize={}", this.getActiveCount(),
                this.getPoolSize());
    }
}
