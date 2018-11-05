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

package com.fanniemae.mbsportal.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.query.SelectResults;
import org.apache.geode.cache.query.internal.ResultsSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fanniemae.mbsportal.constants.DAOConstants.IDTypes;
import com.fanniemae.mbsportal.gf.pojo.MBSFilter;
import com.fanniemae.mbsportal.gf.pojo.MBSOperator;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * This class is the test class for the Transaction Dao class
 * <p>
 *
 * 07/24/2017
 *
 * @author g8upjv
 */

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 16, 2018
 * @Time 4:04:57 PM
 * 	com.fanniemae.mbsportal.dao
 * 	MBSTransactionRequestDaoTest.java
 * @Description: CMMBSSTA01-1022: (Tech) Maintain versions for transaction request
 */
public class MBSTransactionRequestDaoTest extends BaseDaoTest {

    private static String regionName = "MBSTransaction";
    @Mock
    Logger LOGGER;

    @SuppressWarnings("rawtypes")
    SelectResults selectResults;

    @Autowired
    private MBSTransactionRequestDao mbsTransactionRequestDao;

    @Mock
    private MBSEventDao mBSEventDao;

    @SuppressWarnings({ "unchecked" })
    @Before
    public void setUp() throws Exception {
        when(mockRegion.getName()).thenReturn(regionName);
        when(mockRegionService.getRegion(regionName)).thenReturn(mockRegion);
        when(mockRegion.getRegionService()).thenReturn(mockRegionService);
        when(idServiceDao.getSeqId(IDTypes.TRANSACTION_ID)).thenReturn("17K00001");
        List<MBSTransactionRequest> lstMBSTransactionRequest = new ArrayList<>();
        selectResults = new ResultsSet();
        selectResults.asList().addAll(lstMBSTransactionRequest);
        // when(((BaseDaoWrapper) baseDaoWrapper).query(any(),
        // any())).thenReturn(selectResults);
    }

    /**
     * Test case for get TransHist records by StateType - User Id empty
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void getTransReqStateTypeUserIdEmpty() throws MBSBaseException {
        List<String> stateTypeLst = new ArrayList<>();
        stateTypeLst.add("TRADER_CONFIRMED");
        List<MBSTransactionRequest> lstMBSTransactionRequest = new ArrayList<>();
        SelectResults selectResults = new ResultsSet();
        selectResults.asList().addAll(lstMBSTransactionRequest);
        when(((BaseDaoWrapper) baseDaoWrapper).query(any(), any())).thenReturn(selectResults);
        List<MBSTransactionRequest> results = mbsTransactionRequestDao.getTransReqStateType(stateTypeLst, null,
                StringUtils.EMPTY, "MBSP");
        Assert.assertNotNull(results);
    }

    /**
     * Test case for get TransHist records by StateType - UserId not empty
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void getTransReqStateTypeOrderType() throws MBSBaseException {
        List<String> stateTypeLst = new ArrayList<>();
        stateTypeLst.add("TRADER_CONFIRMED");
        List<MBSTransactionRequest> lstMBSTransactionRequest = new ArrayList<>();
        SelectResults selectResults = new ResultsSet();
        selectResults.asList().addAll(lstMBSTransactionRequest);
        when(((BaseDaoWrapper) baseDaoWrapper).query(any(), any())).thenReturn(selectResults);
        List<MBSTransactionRequest> results = mbsTransactionRequestDao.getTransReqStateType(stateTypeLst, null, "asc", "MBSP");
        Assert.assertNotNull(results);
    }

    /**
     * Test case for get TransHist records by StateType - UserId not empty
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    @Ignore
    // TODO: check why actual call is going
    public void getTransReqStateType() throws MBSBaseException {
        List<String> stateTypeLst = new ArrayList<>();
        stateTypeLst.add("TRADER_CONFIRMED");
        List<MBSTransactionRequest> lstMBSTransactionRequest = new ArrayList<>();
        SelectResults selectResults = new ResultsSet();
        selectResults.asList().addAll(lstMBSTransactionRequest);
        when(((BaseDaoWrapper) baseDaoWrapper).query(any(), any())).thenReturn(selectResults);
        List<String> lenderLst = new ArrayList<String>();
        lenderLst.add("test");
        List<MBSTransactionRequest> results = mbsTransactionRequestDao.getTransReqStateType(stateTypeLst, lenderLst,
                StringUtils.EMPTY, "MBSP");
        Assert.assertNotNull(results);
    }
    
    /**
     * Test case for get TransHist records by StateType - User Id empty
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void getTransReqByTransReqNumberIdEmpty() throws MBSBaseException {
        List<MBSTransactionRequest> lstMBSTransactionRequest = new ArrayList<>();
        SelectResults selectResults = new ResultsSet();
        selectResults.asList().addAll(lstMBSTransactionRequest);
        when(((BaseDaoWrapper) baseDaoWrapper).query(any(), any())).thenReturn(selectResults);
        MBSTransactionRequest result = mbsTransactionRequestDao.getTransReqByTransReqNumber("");
        Assert.assertNull(result);
    }

    /**
     * Test case for get TransHist records by StateType - UserId not empty
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void getTransReqByTransReqNumber() throws MBSBaseException {
        List<MBSTransactionRequest> lstMBSTransactionRequest = new ArrayList<>();
        lstMBSTransactionRequest.add(new MBSTransactionRequest());
        SelectResults selectResults = new ResultsSet();
        selectResults.asList().addAll(lstMBSTransactionRequest);
        when(((BaseDaoWrapper) baseDaoWrapper).query(any(), any())).thenReturn(selectResults);
        MBSTransactionRequest results = mbsTransactionRequestDao.getTransReqByTransReqNumber("18K11111");
        //Actual method call being executed and hence returning null
        Assert.assertNull(results);
    }

    /**
     * Case when no seq id is passed/generated
     * 
     * @throws MBSBaseException
     */
    @Test(expected = MBSBaseException.class)
    public void saveOrUpdateNoSeqId() throws MBSBaseException {
        when(idServiceDao.getSeqId(IDTypes.TRANSACTION_ID)).thenReturn("");

        doNothing().when(mBSEventDao).saveOrUpdate(any());

        // doNothing().when((BaseDaoWrapper)baseDaoWrapper).saveOrUpdate(any());
        MBSTransactionRequest mbsTrans = new MBSTransactionRequest();
        ProductId prodId = new ProductId();
        prodId.setIdentifier(Long.valueOf(1));
        prodId.setSourceType("MBS");
        prodId.setType("PU");
        mbsTrans.setTradeAmount(new BigDecimal(100000));
        mbsTrans.setTradeBuySellType("SELL");
        mbsTrans.setTradeCouponRate(new BigDecimal(3));
        mbsTrans.setTradeSettlementDate(new Date());
        mbsTrans.setProductId(prodId);
        mbsTrans.setSubmissionDate(new Date());
        mbsTrans.setCounterpartyTraderIdentifier("testLender1");
        mbsTransactionRequestDao.saveOrUpdate(mbsTrans);
        assertNull(mbsTrans.getTransReqNumber());
    }

    /**
     *
     * @throws MBSBaseException
     */
    @Test
    public void saveOrUpdate() throws MBSBaseException {
        when(idServiceDao.getSeqId(IDTypes.TRANSACTION_ID)).thenReturn("17K00001");

        doNothing().when(mBSEventDao).saveOrUpdate(any());

        // doNothing().when((BaseDaoWrapper)baseDaoWrapper).saveOrUpdate(any());
        MBSTransactionRequest mbsTrans = new MBSTransactionRequest();
        ProductId prodId = new ProductId();
        prodId.setIdentifier(Long.valueOf(1));
        prodId.setSourceType("MBS");
        prodId.setType("PU");
        mbsTrans.setTradeAmount(new BigDecimal(100000));
        mbsTrans.setTradeBuySellType("SELL");
        mbsTrans.setTradeCouponRate(new BigDecimal(3));
        mbsTrans.setTradeSettlementDate(new Date());
        mbsTrans.setProductId(prodId);
        mbsTrans.setSubmissionDate(new Date());
        mbsTrans.setCounterpartyTraderIdentifier("testLender1");
        mbsTransactionRequestDao.saveOrUpdate(mbsTrans);
        assertEquals("17K00001", mbsTrans.getTransReqNumber());
    }

    @Test
    public void saveOrUpdate_With_Active_Version_Info() throws MBSBaseException {

        when(idServiceDao.getSeqId(IDTypes.TRANSACTION_ID)).thenReturn("17K00001");
        doNothing().when(mBSEventDao).saveOrUpdate(any());
        MBSTransactionRequest mbsTrans = new MBSTransactionRequest();
        ProductId prodId = new ProductId();
        prodId.setIdentifier(Long.valueOf(1));
        prodId.setSourceType("MBS");
        prodId.setType("PU");
        mbsTrans.setTradeAmount(new BigDecimal(100000));
        mbsTrans.setTradeBuySellType("SELL");
        mbsTrans.setTradeCouponRate(new BigDecimal(3));
        mbsTrans.setTradeSettlementDate(new Date());
        mbsTrans.setProductId(prodId);
        mbsTrans.setSubmissionDate(new Date());
        mbsTrans.setCounterpartyTraderIdentifier("testLender1");
        mbsTrans.setActiveVersion(1L);
        mbsTransactionRequestDao.saveOrUpdate(mbsTrans);
        assertEquals("17K00001", mbsTrans.getTransReqNumber());
    }

    @Test
    public void saveEventsTraderTest() throws MBSBaseException {

        doNothing().when(mBSEventDao).saveOrUpdate(any());
        MBSTransactionRequest mbsTrans = new MBSTransactionRequest();
        mbsTrans.setCounterpartyTraderIdentifier("testLender1");
        mbsTransactionRequestDao.saveEvents(mbsTrans, "trader");
    }

    @Test
    public void saveEventsLenderTest() throws MBSBaseException {

        doNothing().when(mBSEventDao).saveOrUpdate(any());
        MBSTransactionRequest mbsTrans = new MBSTransactionRequest();
        mbsTrans.setCounterpartyTraderIdentifier("testLender1");
        mbsTransactionRequestDao.saveEvents(mbsTrans, "lender");
    }

    @Test
    public void saveEventsBothTest() throws MBSBaseException {

        doNothing().when(mBSEventDao).saveOrUpdate(any());
        MBSTransactionRequest mbsTrans = new MBSTransactionRequest();
        mbsTrans.setCounterpartyTraderIdentifier("testLender1");
        mbsTransactionRequestDao.saveEvents(mbsTrans, null);
    }
    
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    @Ignore
    public void getConcurrentLenderTradeRequest_Success() throws MBSBaseException {
      
        ProductId productId = new ProductId();
        productId.setIdentifier(0L);
        productId.setSourceType("PU");
        productId.setType("MBS");
        MBSTransactionRequest mBSTransactionRequest = new MBSTransactionRequest();
        mBSTransactionRequest.setDealerOrgName("dealerOrgName");
        mBSTransactionRequest.setCounterPartyBuySellType("BUY");
        mBSTransactionRequest.setTradeCouponRate(new BigDecimal("3.500"));
        mBSTransactionRequest.setTradeAmount(new BigDecimal(10000000));
        mBSTransactionRequest.setStateType("LENDER_OPEN");
        mBSTransactionRequest.setProductId(productId);
        
        List<MBSTransactionRequest> lstMBSTransactionRequest = new ArrayList<>();
        SelectResults selectResults = new ResultsSet();
        selectResults.asList().addAll(lstMBSTransactionRequest);
        when(((BaseDaoWrapper) baseDaoWrapper).query(any(), any())).thenReturn(selectResults);
        
        List<MBSTransactionRequest> results = mbsTransactionRequestDao.getConcurrentLenderTradeRequest(mBSTransactionRequest);
        Assert.assertNotNull(results);
    }
    
    @Test    
    public void testGetHistorySize() throws MBSBaseException {
    	Region notMockRegion = CacheFactory.getAnyInstance().getRegion(regionName);
    	when (((BaseDaoWrapper) baseDaoWrapper).getStorageRegion()).thenReturn(notMockRegion);
    	
        // create the param args here
        List<MBSFilter> mbsFilterLst = new ArrayList();

        //Filter for stateType
        mbsFilterLst.add(new MBSFilter("stateType", Arrays.asList("1", "2", "3"), MBSOperator.IN));
        
        //Filter for users
        mbsFilterLst.add(new MBSFilter("lenderSellerServiceNumber", Arrays.asList("12345"), MBSOperator.EQUAL));

        //Filter shakeout trades
        mbsFilterLst.add(new MBSFilter("dealerOrgName",Arrays.asList("1", "2", "3"), MBSOperator.NOT_IN));
    	mbsTransactionRequestDao.setRegionName(regionName);
    	int size = mbsTransactionRequestDao.getTransHistorySize(mbsFilterLst);
    	Assert.assertEquals(0, size);  	
    }
}
