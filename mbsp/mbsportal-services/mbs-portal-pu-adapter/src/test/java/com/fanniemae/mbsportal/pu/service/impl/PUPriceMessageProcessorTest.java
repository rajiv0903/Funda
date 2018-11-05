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

package com.fanniemae.mbsportal.pu.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.all.messaging.api.ESBClient;
import com.fanniemae.all.messaging.message.ESBMessage;
import com.fanniemae.all.messaging.message.ESBMessageFactory;
import com.fanniemae.mbsportal.dao.MBSMarketIndicativePriceDao;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.pu.service.MarketIndicativePriceHistoryService;
import com.fanniemae.mbsportal.pu.service.MarketIndicativePriceService;
import com.fanniemae.mbsportal.pu.service.PUPriceMessageProcessor;
import com.fanniemae.mbsportal.pu.transformer.PUPriceMessageTransformer;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * 
 * This is the test class for PUPriceMessageProcessor
 * 
 * @author g8upjv
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PUPriceMessageProcessorTest {

    @Mock
    private ESBClient esbClient;

    @Mock
    private PUPriceMessageTransformer puPriceMessageTransformer;

    @Mock
    private MarketIndicativePriceService marketIndicativePriceService;

    @Mock
    private MarketIndicativePriceHistoryService marketIndicativePriceHistoryService;

    @Mock
    private MBSMarketIndicativePriceDao mbsMarketIndicativePriceDao;

    private ClassLoader classLoader;
    private String inputMsg;

    private ESBMessage esbMessage;

    @InjectMocks
    private PUPriceMessageProcessor puPriceMessageProcessor;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        classLoader = getClass().getClassLoader();
        inputMsg = Files.toString(new File(classLoader.getResource("pricing_dev_sample_message_1.xml").getFile()),
                Charsets.UTF_8);
        esbMessage = ESBMessageFactory.createESBMessage(inputMsg);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void prepareAndProcess_First_Time_Success() throws Exception {

        doNothing().when(esbClient).suspendMessage(any());
        doNothing().when(marketIndicativePriceHistoryService).saveMarketIndicativePriceHistoryAsync(anyObject());
        when(puPriceMessageTransformer.transform(any(), any())).thenReturn(createSampleTransformedPrice());
        when(marketIndicativePriceService.updateMarketIndicativePrice(any())).thenReturn(true);
        doReturn(null).when(mbsMarketIndicativePriceDao).getById(anyObject());
        puPriceMessageProcessor.prepareAndProcess(esbMessage);

    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void prepareAndProcess_Success() throws Exception {

        doNothing().when(esbClient).suspendMessage(any());
        doNothing().when(marketIndicativePriceHistoryService).saveMarketIndicativePriceHistoryAsync(anyObject());
        when(puPriceMessageTransformer.transform(any(), any())).thenReturn(createSampleTransformedPrice());
        when(marketIndicativePriceService.updateMarketIndicativePrice(any())).thenReturn(true);
        doReturn(createExistingPrice()).when(mbsMarketIndicativePriceDao).getById(anyObject());
        puPriceMessageProcessor.prepareAndProcess(esbMessage);

    }

    /**
     * 
     * @throws Exception
     *             Skips the message
     * 
     */
    @Test
    public void prepareAndProcess_SuccessEmptyMessage() throws Exception {

        doNothing().when(esbClient).suspendMessage(any());
        doNothing().when(marketIndicativePriceHistoryService).saveMarketIndicativePriceHistoryAsync(anyObject());
        when(puPriceMessageTransformer.transform(any(), any())).thenReturn(createSampleTransformedPrice());
        when(marketIndicativePriceService.updateMarketIndicativePrice(any())).thenReturn(true);
        doReturn(createExistingPrice()).when(mbsMarketIndicativePriceDao).getById(anyObject());
        puPriceMessageProcessor.prepareAndProcess(null);

    }

    /**
     * 
     * @throws Exception
     *             Skips the message
     */
    @SuppressWarnings("unchecked")
    @Test
    public void prepareAndProcess_SuccessEmptyMessageException() throws Exception {

        doNothing().when(esbClient).suspendMessage(any());
        doNothing().when(marketIndicativePriceHistoryService).saveMarketIndicativePriceHistoryAsync(anyObject());
        when(puPriceMessageTransformer.transform(any(), any())).thenThrow(Exception.class);
        when(marketIndicativePriceService.updateMarketIndicativePrice(any())).thenReturn(true);
        doReturn(createExistingPrice()).when(mbsMarketIndicativePriceDao).getById(anyObject());
        puPriceMessageProcessor.prepareAndProcess(esbMessage);
        // TODO:Some asset needs to be there

    }

    /**
     * This method creates a sample data for MBSTransactionRequest
     * 
     * @return
     * @throws MBSBaseException
     */
    private List<MBSMarketIndicativePrice> createSampleTransformedPrice() throws MBSBaseException {

        List<MBSMarketIndicativePrice> mbsMarketIndicativePriceLst = new ArrayList<>();

        MBSMarketIndicativePrice mbsMarketIndicativePrice = new MBSMarketIndicativePrice();
        mbsMarketIndicativePrice.setEventType("TBA");
        mbsMarketIndicativePrice.setProductNameCode("FN15");
        mbsMarketIndicativePrice.setSettlementDate(
                MBSPortalUtils.convertToDateWithFormatter("2018-04-17", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        mbsMarketIndicativePrice.setPassThroughRate(new BigDecimal("4.00"));
        mbsMarketIndicativePrice.setBidPricePercent(new BigDecimal("95.5625"));
        mbsMarketIndicativePrice.setAskPricePercent(new BigDecimal("95.5625"));

        mbsMarketIndicativePriceLst.add(mbsMarketIndicativePrice);

        mbsMarketIndicativePrice = new MBSMarketIndicativePrice();
        mbsMarketIndicativePrice.setEventType("TBA");
        mbsMarketIndicativePrice.setProductNameCode("FN30");
        mbsMarketIndicativePrice.setSettlementDate(
                MBSPortalUtils.convertToDateWithFormatter("2018-04-17", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        mbsMarketIndicativePrice.setPassThroughRate(new BigDecimal("4.50"));
        mbsMarketIndicativePrice.setBidPricePercent(new BigDecimal("96.5625"));
        mbsMarketIndicativePrice.setAskPricePercent(new BigDecimal("96.5625"));

        mbsMarketIndicativePriceLst.add(mbsMarketIndicativePrice);

        return mbsMarketIndicativePriceLst;
    }

    /**
     * 
     * @return mbsMarketIndicativePrice MBSMarketIndicativePrice
     * @throws MBSBaseException
     */
    private MBSMarketIndicativePrice createExistingPrice() throws MBSBaseException {

        MBSMarketIndicativePrice mbsMarketIndicativePrice = new MBSMarketIndicativePrice();
        mbsMarketIndicativePrice.setEventType("TBA");
        mbsMarketIndicativePrice.setProductNameCode("FN30");
        mbsMarketIndicativePrice.setSettlementDate(
                MBSPortalUtils.convertToDateWithFormatter("2018-04-17", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        mbsMarketIndicativePrice.setPassThroughRate(new BigDecimal("4.50"));
        mbsMarketIndicativePrice.setBidPricePercent(new BigDecimal("96.5625"));
        mbsMarketIndicativePrice.setAskPricePercent(new BigDecimal("96.5625"));

        return mbsMarketIndicativePrice;
    }
}
