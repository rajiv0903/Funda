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

package com.fanniemae.mbsportal.service;

import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.dao.MBSTradeDao;
import com.fanniemae.mbsportal.dao.MBSTransactionRequestDao;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSTrade;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * 
 * @author g8upjv
 *
 */
/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Dec 29, 2017
 * @Time 1:11:52 PM
 * 	com.fanniemae.mbsportal.service
 * 	ESBTransactionRequestServiceImplTest.java
 * @Description: Added uncovered line 
 */
@RunWith(MockitoJUnitRunner.class)
public class ESBTransactionRequestServiceImplTest {

    @InjectMocks
    private ESBTransactionRequestService esbTransactionRequestService = new ESBTransactionRequestServiceImpl();

    @Mock
    private MBSTransactionRequestDao mbsTransactionRequestDao;

    @Mock
    private MBSTradeDao mbsTradeDao;

    /**
     * Test case for getMBSTrade
     * 
     * @throws Exception
     */
    @Test
    public void testGetMBSTrade() throws MBSBaseException {
        when(mbsTransactionRequestDao.getTransReqByTransReqNumber(any())).thenReturn(createSampleTransReq());
        MBSTransactionRequest mbsTransReq = esbTransactionRequestService.getMBSTransReq("17K00002");
        assertTrue(mbsTransReq.getTransReqNumber().equals("10001"));
    }

    /**
     * Test case for updateMBSTransReq
     * 
     * @throws Exception
     */
    @Test
    public void updateMBSTransReq() throws MBSBaseException {
        doNothing().when(mbsTransactionRequestDao).saveOrUpdate(any());
        assertTrue(esbTransactionRequestService.updateMBSTransReq(createSampleTransReq()));
    }

    /**
     * Test case for updateMBSTransReq when passing empty object
     * 
     * @throws Exception
     */
    @Test(expected = MBSBaseException.class)
    public void updateMBSTransReqException() throws MBSBaseException {
        doNothing().when(mbsTransactionRequestDao).saveOrUpdate(any());
        assertTrue(esbTransactionRequestService.updateMBSTransReq(null));
    }

    /**
     * Test case for updateMBSTransReq
     * 
     * @throws Exception
     */
    @Test
    public void updateMBSTrade() throws MBSBaseException {
        doNothing().when(mbsTradeDao).saveOrUpdate(any());
        assertTrue(esbTransactionRequestService.updateMBSTrade(createSampleTrade()));
    }

    /**
     * Test case for updateMBSTransReq when passing empty object
     * 
     * @throws Exception
     */
    @Test(expected = MBSBaseException.class)
    public void updateMBSTradeException() throws MBSBaseException {
        doNothing().when(mbsTradeDao).saveOrUpdate(any());
        assertTrue(esbTransactionRequestService.updateMBSTrade(null));
    }
    
    /**
     * 
     * @throws MBSBaseException
     */
    @Test(expected=MBSBaseException.class)
    public void testGetMBSTrade_Throw_MBSBaseException() throws MBSBaseException {
        doThrow(MBSBaseException.class).when(mbsTransactionRequestDao).getTransReqByTransReqNumber(any());
        MBSTransactionRequest mbsTransReq = esbTransactionRequestService.getMBSTransReq("17K00002");
        assertTrue(mbsTransReq.getTransReqNumber().equals("10001"));
    }
    
    /**
     * 
     * @throws MBSBaseException
     */
    @Test(expected=MBSBaseException.class)
    public void testGetMBSTrade_Throw_RuntimeException() throws MBSBaseException {
        doThrow(RuntimeException.class).when(mbsTransactionRequestDao).getTransReqByTransReqNumber(any());
        MBSTransactionRequest mbsTransReq = esbTransactionRequestService.getMBSTransReq("17K00002");
        assertTrue(mbsTransReq.getTransReqNumber().equals("10001"));
    }
    
    /**
     * 
     * @throws MBSBaseException
     */
    @Test(expected=MBSBaseException.class)
    public void updateMBSTransReq_Throw_RuntimeException() throws MBSBaseException {
        doThrow(RuntimeException.class).when(mbsTransactionRequestDao).saveOrUpdate(any());
        assertTrue(esbTransactionRequestService.updateMBSTransReq(createSampleTransReq()));
    }
    
    /**
     * 
     * @throws MBSBaseException
     */
    @Test(expected=MBSBaseException.class)
    public void updateMBSTrade_Throw_RuntimeException() throws MBSBaseException {
        doThrow(RuntimeException.class).when(mbsTradeDao).saveOrUpdate(any());
        assertTrue(esbTransactionRequestService.updateMBSTrade(createSampleTrade()));
    }

    /**
     * This method creates a sample data for MBSTransactionRequest
     * 
     * @return
     */
    private MBSTransactionRequest createSampleTransReq() {
        MBSTransactionRequest mbsTransactionRequest = new MBSTransactionRequest();
        ProductId prodId = new ProductId();
        prodId.setIdentifier(new Long(1));
        prodId.setSourceType(PRODUCT_SOURCE_TYPE.PU.toString());
        prodId.setType(PRODUCT_TYPE.MBS.toString());
        mbsTransactionRequest.setProductId(prodId);
        mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
        mbsTransactionRequest.setStateType(StateType.LENDER_OPEN.toString());
        mbsTransactionRequest.setSubmissionDate(new Date());
        mbsTransactionRequest.setTradeAmount(new BigDecimal(9000000));
        mbsTransactionRequest.setTradeBuySellType("SELL");
        mbsTransactionRequest.setTradeCouponRate(new BigDecimal(5.5));
        mbsTransactionRequest.setTradeSettlementDate(new Date());
        mbsTransactionRequest.setTransReqNumber("10001");
        mbsTransactionRequest.setPricePercent(new BigDecimal(104.000000000).setScale(9).round(MathContext.DECIMAL64));
        return mbsTransactionRequest;
    }

    /**
     * This method creates a sample data for MBSTransactionRequest
     * 
     * @return
     */
    private MBSTrade createSampleTrade() {
        MBSTrade mbsTrade = new MBSTrade();
        mbsTrade.setTransReqNumber("17K00001");
        return mbsTrade;
    }

}
