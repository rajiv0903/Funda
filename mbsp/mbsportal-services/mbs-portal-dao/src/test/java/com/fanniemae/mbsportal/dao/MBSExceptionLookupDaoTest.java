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
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fanniemae.mbsportal.model.MBSExceptionLookup;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * This class is the test class for the  Dao class
 * <p>
 * 
 * 03/06/2018
 * 
 * @author g8upjv
 */
public class MBSExceptionLookupDaoTest extends BaseDaoTest {

	private static String regionName = "MBSExceptionLookup";

	@Autowired
	private MBSExceptionLookupDao mbsExceptionLookupDao;

	MBSExceptionLookup mbsExceptionLookup;

	@Before
	public void setUp() throws Exception {
		when(mockRegion.getRegionService()).thenReturn(mockRegionService);
		when(mockRegion.getName()).thenReturn(regionName);
		when(mockRegionService.getRegion(regionName)).thenReturn(mockRegion);
	}

	/**
	 * 
	 * @throws MBSBaseException
	 */
	@Test
	public void saveOrUpdate() throws MBSBaseException {
		//doNothing().when((BaseDaoWrapper) baseDaoWrapper).saveOrUpdate(any());
		mbsExceptionLookup = new MBSExceptionLookup();
		mbsExceptionLookup.setErrorCategory("API");
                mbsExceptionLookup.setErrorCode("TRANS_00001");
                mbsExceptionLookup.setMessageType("DISP_ERROR");
                mbsExceptionLookup.setErrorMessage("Test message");
		mbsExceptionLookupDao.saveOrUpdate(mbsExceptionLookup);
		assertEquals("TRANS_00001", mbsExceptionLookup.getErrorCode());
	}

}
