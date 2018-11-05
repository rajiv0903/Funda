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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.geode.cache.query.internal.ResultsSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fanniemae.mbsportal.model.MBSTrade;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * This class is the test class for the Product Dao class
 * <p>
 * 
 * 07/24/2017
 * 
 * @author g8upjv
 */
public class MBSTradeDaoTest extends BaseDaoTest {

	private static String regionName = "MBSTrade";
	@Mock
	Logger LOGGER;
	
	@Autowired
	private MBSTradeDao mbsTradeDao;

	MBSTrade mbsTrade;
	
	ResultsSet selectResults; 

	@SuppressWarnings({ "unchecked", "rawtypes" })
        @Before
	public void setUp() throws Exception {
		when(mockRegion.getName()).thenReturn(regionName);
		when(mockRegionService.getRegion(regionName)).thenReturn(mockRegion);
		List<MBSTrade> lstMBSTrade = new ArrayList<>();
		MBSTrade mbsTrade1 = new MBSTrade();
		mbsTrade1.setTransReqNumber("17I00001");
		lstMBSTrade.add(mbsTrade1);
		selectResults = new ResultsSet();
		selectResults.asList().addAll(lstMBSTrade);
		when(((BaseDaoWrapper)baseDaoWrapper).query(any(), any())).thenReturn(selectResults);

	}

	/**
	 * Case when no seq id is passed/generated
	 * 
	 * @throws MBSBaseException
	 */
	@Test
	public void saveOrUpdateNoSeqId() throws MBSBaseException {
		//doNothing().when((BaseDaoWrapper) baseDaoWrapper).saveOrUpdate(any());
		mbsTrade = new MBSTrade();
		mbsTrade.setTransReqNumber(null);
		mbsTradeDao.saveOrUpdate(mbsTrade);
		assertEquals(null, mbsTrade.getTransReqNumber());
	}

	/**
	 * 
	 * @throws MBSBaseException
	 */
	@Test
	public void saveOrUpdate() throws MBSBaseException {
		//doNothing().when((BaseDaoWrapper) baseDaoWrapper).saveOrUpdate(any());
		mbsTrade = new MBSTrade();
		mbsTrade.setTransReqNumber("17K00001");
		mbsTradeDao.saveOrUpdate(mbsTrade);
		assertEquals("17K00001", mbsTrade.getTransReqNumber());
	}
	
	/**
	 * 
	 * @throws MBSBaseException
	 */
	@Test
	public void getByTransReqId() throws MBSBaseException{
		setup();
		String transId = "17J00001";
		MBSTrade results = mbsTradeDao.getByTransReqId(transId);
	}

}
