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
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fanniemae.mbsportal.constants.DAOConstants.IDTypes;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSProduct;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * This class is the test class for the Product Dao class
 * <p>
 * 
 * 07/24/2017
 * 
 * @author g8upjv
 */
public class MBSProductDaoTest extends BaseDaoTest {

	private static String regionName = "MBSProduct";

	@Autowired
	private MBSProductDao mtaProductDao;

	MBSProduct mbsProd;

	ProductId prodId;

	@Before
	public void setUp() throws Exception {
		when(mockRegion.getRegionService()).thenReturn(mockRegionService);
		when(mockRegion.getName()).thenReturn(regionName);
		when(mockRegionService.getRegion(regionName)).thenReturn(mockRegion);
		when(idServiceDao.getSeqId(IDTypes.PRODUCT_ID)).thenReturn("1");
	}

	/**
	 * Case when no seq id is passed/generated
	 * 
	 * @throws MBSBaseException
	 */
	@Test (expected=MBSBaseException.class)
	public void saveOrUpdateNoSeqId() throws MBSBaseException {
		when(idServiceDao.getSeqId(IDTypes.PRODUCT_ID)).thenReturn("");
		//doNothing().when((BaseDaoWrapper) baseDaoWrapper).saveOrUpdate(any());
		ProductId prodId = new ProductId();
		prodId.setSourceType("MBS");
		prodId.setType("PU");
		mbsProd = new MBSProduct();
		mbsProd.setProductId(prodId);
		mbsProd.setProductNameCode("FN30");
		mtaProductDao.saveOrUpdate(mbsProd);
		assertNull(mbsProd.getProductId().getIdentifier());
	}

	/**
	 * 
	 * @throws MBSBaseException
	 */
	@Test
	public void saveOrUpdate() throws MBSBaseException {
		when(idServiceDao.getSeqId(IDTypes.PRODUCT_ID)).thenReturn("1");
		//doNothing().when((BaseDaoWrapper) baseDaoWrapper).saveOrUpdate(any());
		ProductId prodId = new ProductId();
		prodId.setSourceType("MBS");
		prodId.setType("PU");
		mbsProd = new MBSProduct();
		mbsProd.setProductId(prodId);
		mbsProd.setProductNameCode("FN30");
		mtaProductDao.saveOrUpdate(mbsProd);
		assertEquals(new Long(1), mbsProd.getProductId().getIdentifier());
	}

}
