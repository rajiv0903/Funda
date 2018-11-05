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

package com.fanniemae.mbsportal.pu.jobs;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.fanniemae.mbsportal.constants.DAOConstants;
import com.fanniemae.mbsportal.dao.MBSConfigPropDao;
import com.fanniemae.mbsportal.dao.MBSMarketIndicativePriceDao;
import com.fanniemae.mbsportal.model.MBSConfigProp;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSDataAccessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * @author: Rajiv Chaudhuri
 * @Date: Jul 5, 2018
 * @File: com.fanniemae.mbsportal.pu.jobs.PriceFeedMessageExpectedFrequency.java
 * @Revision:
 * @Description: PriceFeedMessageExpectedFrequency.java
 */
@Configuration
@EnableScheduling
@EnableAsync
public class PriceFeedMessageExpectedFrequency {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceFeedMessageExpectedFrequency.class);

    private static final String LOGGING_METHOD_NAME = "priceFeedExpectedFrequencySchedulerTask";
    private static final String LOGGING_METHOD_SIGNATURE = "com.fanniemae.mbsportal.pu.jobs.PriceFeedMessageExpectedFrequency.priceFeedExpectedFrequencySchedulerTask()";

    /**
     * 
     * mbsConfigPropDao MBSConfigPropDao
     */
    @Autowired
    MBSConfigPropDao mbsConfigPropDao;

    /**
     * 
     * mBSMarketIndicativePriceDao MBSMarketIndicativePriceDao
     */
    @Autowired
    MBSMarketIndicativePriceDao mBSMarketIndicativePriceDao;
    
    /**
     * 
     * runPriceDownTimeJob Run the JOB to check
     */
    @Value("${mbsp.pu.runPriceDownTimeJob}")
    boolean runPriceDownTimeJob;

    @Scheduled(cron= "${mbsp.pu.priceDownTimeJobScheduler}")
    //@Scheduled(cron= "0 * 8-17 ? * 1-5")
    public void priceFeedExpectedFrequencySchedulerTask() {

        try {
            LOGGER.debug("runPriceDownTimeJob: {}", runPriceDownTimeJob);
            
            if(!runPriceDownTimeJob){
                return;
            }
            /*
             * Get the difference (NN) from Configuration
             */
            MBSConfigProp mbsConfigProp = (MBSConfigProp) mbsConfigPropDao
                    .getById(DAOConstants.PRICE_DOWNTIME_THRESHOLD_IN_MILLISECONDS);

            LOGGER.debug("mbsConfigProp: {}", mbsConfigProp);

            if (Objects.isNull(mbsConfigProp) || StringUtils.isBlank(mbsConfigProp.getValue())) {
                throw new MBSDataAccessException(
                        "No Data Mapped for Key :{" + DAOConstants.PRICE_DOWNTIME_THRESHOLD_IN_MILLISECONDS + "}");
            }
            /*
             * Query MBSMarketIndicativePriceDao and Fetch the Max Last Updated
             * Time
             */
            MBSMarketIndicativePrice mBSMarketIndicativePriceLastUpdated = mBSMarketIndicativePriceDao
                    .getLastUpdatedRecord();

            LOGGER.debug("mBSMarketIndicativePriceLastUpdated: {}", mBSMarketIndicativePriceLastUpdated);

            if (Objects.isNull(mBSMarketIndicativePriceLastUpdated)
                    || Objects.isNull(mBSMarketIndicativePriceLastUpdated.getLastUpdatedDate())) {
                throw new MBSSystemException("No Data at Pricing Region hence no last updated record");
            }
            /*
             * Find the Difference (System Time - Last Updated Time) > NN then
             * Raise System Error
             */
            long currrentTime = MBSPortalUtils.getCurrentTimeStamp();
            long lastUpdated = mBSMarketIndicativePriceLastUpdated.getLastUpdated().getTime();

            long diff = currrentTime - lastUpdated;

            LOGGER.debug("currrentTime: {},  lastUpdated: {}, diff: {}", currrentTime, lastUpdated, diff);
            
            //If difference is positive and greater than configured value then raise error 
            if(diff > 0 &&  diff >= Long.valueOf(mbsConfigProp.getValue())){
                throw new MBSSystemException("No Pricing Information Received for last {"+ diff/60000 +"} minutes");
            }

        } catch (MBSBaseException exe) {

            MDC.put(MBSExceptionConstants.ERROR_TYPE, MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER);
            MDC.put(MBSExceptionConstants.ERROR_PROCESS_ID, String.valueOf(MBSExceptionConstants.SYSTEM_EXCEPTION));
            MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD, LOGGING_METHOD_NAME);
            MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD_SIGNATURE, LOGGING_METHOD_SIGNATURE);
            MDC.put(MBSExceptionConstants.SOURCE_METHOD_ARGS, "");
            MDC.put(MBSExceptionConstants.ERROR_MESSGE, exe.getRootExceptionMessage());

            LOGGER.error("MBSBaseException: {}", exe.getRootException());

        } catch (Exception exe) {

            MDC.put(MBSExceptionConstants.ERROR_TYPE, MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER);
            MDC.put(MBSExceptionConstants.ERROR_PROCESS_ID, String.valueOf(MBSExceptionConstants.SYSTEM_EXCEPTION));
            MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD, LOGGING_METHOD_NAME);
            MDC.put(MBSExceptionConstants.ERROR_SOURCE_METHOD_SIGNATURE, LOGGING_METHOD_SIGNATURE);
            MDC.put(MBSExceptionConstants.SOURCE_METHOD_ARGS, "");
            MDC.put(MBSExceptionConstants.ERROR_MESSGE, exe.getMessage());

            LOGGER.error("Exception: {}", exe);
        } finally {

            MDC.remove(MBSExceptionConstants.ERROR_TYPE);
            MDC.remove(MBSExceptionConstants.ERROR_PROCESS_ID);
            MDC.remove(MBSExceptionConstants.ERROR_SOURCE_METHOD);
            MDC.remove(MBSExceptionConstants.ERROR_SOURCE_METHOD_SIGNATURE);
            MDC.remove(MBSExceptionConstants.SOURCE_METHOD_ARGS);
            MDC.remove(MBSExceptionConstants.ERROR_MESSGE);
        }
    }
}
