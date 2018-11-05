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

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * This class is the test class for the MBSMarketIndicativePrice Dao class
 * <p>
 * 
 * 06/18/2018
 * 
 * @author g8upjv
 */
public class MBSMarketIndicativePriceDaoTest extends BaseDaoTest {

	private static String regionName = "MBSMarketIndicativePrice";

	@Autowired
	private MBSMarketIndicativePriceDao mbsMarketIndicativePriceDao;

	MBSMarketIndicativePrice mbsMarketIndicativePrice;

	ProductId prodId;

	@Before
	public void setUp() throws Exception {
		when(mockRegion.getRegionService()).thenReturn(mockRegionService);
		when(mockRegion.getName()).thenReturn(regionName);
		when(mockRegionService.getRegion(regionName)).thenReturn(mockRegion);
	}

	/**
	 * Case when no seq id is passed/generated
	 * 
	 * @throws MBSBaseException
	 */
	@Test
	public void saveOrUpdateSuccess() throws MBSBaseException {
		//doNothing().when((BaseDaoWrapper) baseDaoWrapper).saveOrUpdate(any());
	        mbsMarketIndicativePrice = new MBSMarketIndicativePrice();
	        mbsMarketIndicativePrice.setAskPriceText("101-101");
	        mbsMarketIndicativePrice.setBidPriceText("101-101");
	        mbsMarketIndicativePrice.setProductNameCode("FN30");
	        mbsMarketIndicativePriceDao.saveOrUpdate(mbsMarketIndicativePrice);
		//assertNull(mbsMarketIndicativePrice);
	}

}
