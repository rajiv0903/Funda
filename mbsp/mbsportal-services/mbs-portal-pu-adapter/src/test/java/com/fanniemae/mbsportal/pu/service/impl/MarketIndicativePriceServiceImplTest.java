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

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;

import com.fanniemae.mbsportal.dao.MBSMarketIndicativePriceDao;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.pu.service.impl.MarketIndicativePriceServiceImpl;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * MBSMarketIndicativePriceServiceImplTest
 * 
 * @author g8upjv
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class MarketIndicativePriceServiceImplTest {

    
    @Mock
    private MBSMarketIndicativePriceDao mbsMarketIndicativePriceDao;
    
    
    @InjectMocks
    private MarketIndicativePriceServiceImpl marketIndicativePriceServiceImpl;
    
    @Before
    public void setUp() throws Exception {
        when(mbsMarketIndicativePriceDao.getById(Mockito.any())).thenReturn(new MBSMarketIndicativePrice());
    }

    /**
     * Test case for transform
     * 
     * @throws Exception
     */
    @Test(expected=MBSBaseException.class)
    public void updateMBSMarketIndicativePriceNullData() throws MBSBaseException {
        marketIndicativePriceServiceImpl.updateMarketIndicativePrice(null);
        
    }
    
    /**
     * Test case for transform
     * 
     * @throws Exception
     */
    @Test(expected=MBSBaseException.class)
    public void updateMBSMarketIndicativePriceMBSBaseException() throws MBSBaseException {
        Mockito.doThrow(new MBSBaseException("Test")).when(mbsMarketIndicativePriceDao).saveOrUpdate(Mockito.any());
        marketIndicativePriceServiceImpl.updateMarketIndicativePrice(new MBSMarketIndicativePrice());
        
    }
    
    /**
     * Test case for transform
     * 
     * @throws Exception
     */
    @Test(expected=MBSBaseException.class)
    public void updateMBSMarketIndicativePriceException() throws MBSBaseException {
        Mockito.doThrow(Exception.class).when(mbsMarketIndicativePriceDao).saveOrUpdate(Mockito.any());
        marketIndicativePriceServiceImpl.updateMarketIndicativePrice(new MBSMarketIndicativePrice());
        
    }
    
    /**
     * Test case for transform
     * 
     * @throws Exception
     */
    @Test
    public void updateMBSMarketIndicativePrice() throws MBSBaseException {
        Mockito.doNothing().when(mbsMarketIndicativePriceDao).saveOrUpdate(Mockito.any());
        boolean status = marketIndicativePriceServiceImpl.updateMarketIndicativePrice(new MBSMarketIndicativePrice());
        assertEquals(status, true);
        
    }

}
