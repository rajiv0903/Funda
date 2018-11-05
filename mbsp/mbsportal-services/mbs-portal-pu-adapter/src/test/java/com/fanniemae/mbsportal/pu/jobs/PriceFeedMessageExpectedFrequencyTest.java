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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.constants.DAOConstants;
import com.fanniemae.mbsportal.dao.MBSConfigPropDao;
import com.fanniemae.mbsportal.dao.MBSMarketIndicativePriceDao;
import com.fanniemae.mbsportal.model.MBSConfigProp;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author: Rajiv Chaudhuri
 * @Date: Jul 9, 2018
 * @File: com.fanniemae.mbsportal.pu.jobs.PriceFeedMessageExpectedFrequencyTest.java
 * @Revision:
 * @Description: PriceFeedMessageExpectedFrequencyTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class PriceFeedMessageExpectedFrequencyTest {

    @Mock
    MBSConfigPropDao mbsConfigPropDao;

    @Mock
    MBSMarketIndicativePriceDao mBSMarketIndicativePriceDao;

    @InjectMocks
    private PriceFeedMessageExpectedFrequency priceFeedMessageExpectedFrequency;

    private MBSConfigProp mbsConfigProp;
    private String priceDownTimeThreshold;

    private MBSMarketIndicativePrice mBSMarketIndicativePrice;

    @Before
    public void setUp() throws Exception {

        priceDownTimeThreshold = "300000";
        createMbsConfigProp();
        createMBSMarketIndicativePrice();
    }

    @Test
    public void priceFeedExpectedFrequencySchedulerTask_Configured_To_Not_Run_Success() throws Exception {

        priceFeedMessageExpectedFrequency.runPriceDownTimeJob = false;
        
        doReturn(mbsConfigProp).when(mbsConfigPropDao).getById(anyString());
        doReturn(mBSMarketIndicativePrice).when(mBSMarketIndicativePriceDao).getLastUpdatedRecord();
        priceFeedMessageExpectedFrequency.priceFeedExpectedFrequencySchedulerTask();
    }

    @Test
    public void priceFeedExpectedFrequencySchedulerTask_Success() throws Exception {

        priceFeedMessageExpectedFrequency.runPriceDownTimeJob = true;
        
        doReturn(mbsConfigProp).when(mbsConfigPropDao).getById(anyString());
        doReturn(mBSMarketIndicativePrice).when(mBSMarketIndicativePriceDao).getLastUpdatedRecord();
        priceFeedMessageExpectedFrequency.priceFeedExpectedFrequencySchedulerTask();
    }

    @Test
    public void priceFeedExpectedFrequencySchedulerTask_No_Config_Prop_Failure() throws Exception {

        priceFeedMessageExpectedFrequency.runPriceDownTimeJob = true;
        
        doReturn(null).when(mbsConfigPropDao).getById(anyString());
        doReturn(mBSMarketIndicativePrice).when(mBSMarketIndicativePriceDao).getLastUpdatedRecord();
        priceFeedMessageExpectedFrequency.priceFeedExpectedFrequencySchedulerTask();
    }

    @Test
    public void priceFeedExpectedFrequencySchedulerTask_No_Config_Prop_Value_Failure() throws Exception {

        mbsConfigProp.setValue(null);
        priceFeedMessageExpectedFrequency.runPriceDownTimeJob = true;
        
        doReturn(mbsConfigProp).when(mbsConfigPropDao).getById(anyString());
        doReturn(mBSMarketIndicativePrice).when(mBSMarketIndicativePriceDao).getLastUpdatedRecord();
        priceFeedMessageExpectedFrequency.priceFeedExpectedFrequencySchedulerTask();
    }
    
    @Test
    public void priceFeedExpectedFrequencySchedulerTask_No_Price_Record_Failure() throws Exception {

        priceFeedMessageExpectedFrequency.runPriceDownTimeJob = true;
        
        doReturn(mbsConfigProp).when(mbsConfigPropDao).getById(anyString());
        doReturn(null).when(mBSMarketIndicativePriceDao).getLastUpdatedRecord();
        priceFeedMessageExpectedFrequency.priceFeedExpectedFrequencySchedulerTask();
    }
    
    @Test
    public void priceFeedExpectedFrequencySchedulerTask_Price_Record_Empty_Date_Failure() throws Exception {

        mBSMarketIndicativePrice.setLastUpdated(null);
        priceFeedMessageExpectedFrequency.runPriceDownTimeJob = true;
        
        doReturn(mbsConfigProp).when(mbsConfigPropDao).getById(anyString());
        doReturn(mBSMarketIndicativePrice).when(mBSMarketIndicativePriceDao).getLastUpdatedRecord();
        priceFeedMessageExpectedFrequency.priceFeedExpectedFrequencySchedulerTask();
    }
    
    @Test
    public void priceFeedExpectedFrequencySchedulerTask_No_Price_Feed_For_Last_10_Mins_Failure() throws Exception {

        mBSMarketIndicativePrice.setLastUpdated(MBSPortalUtils.getDateTimeFromEpochMilli(MBSPortalUtils.getCurrentTimeStamp()- Long.valueOf(priceDownTimeThreshold)-  Long.valueOf(priceDownTimeThreshold)));
        priceFeedMessageExpectedFrequency.runPriceDownTimeJob = true;
        
        doReturn(mbsConfigProp).when(mbsConfigPropDao).getById(anyString());
        doReturn(mBSMarketIndicativePrice).when(mBSMarketIndicativePriceDao).getLastUpdatedRecord();
        priceFeedMessageExpectedFrequency.priceFeedExpectedFrequencySchedulerTask();
    }

    /**
     * 
     * @throws MBSBaseException
     */
    private void createMBSMarketIndicativePrice() throws MBSBaseException {

        mBSMarketIndicativePrice = new MBSMarketIndicativePrice();
        mBSMarketIndicativePrice
                .setLastUpdated(MBSPortalUtils.getDateTimeFromEpochMilli(MBSPortalUtils.getCurrentTimeStamp()));
    }

    /**
     * 
     */
    private void createMbsConfigProp() {

        mbsConfigProp = new MBSConfigProp();
        mbsConfigProp.setKey(DAOConstants.PRICE_DOWNTIME_THRESHOLD_IN_MILLISECONDS);
        mbsConfigProp.setValue(priceDownTimeThreshold);
    }
}
