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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.fanniemae.mbsportal.model.MBSParty;
import com.fanniemae.mbsportal.model.TspPartyLender;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 29, 2018
 * @File: com.fanniemae.mbsportal.dao.MBSPartyDaoTest.java
 * @Revision:
 * @Description: MBSPartyDaoTest.java
 */
public class MBSPartyDaoTest extends BaseDaoTest {

    private static String regionName = "MBSParty";

    @Autowired
    private MBSPartyDao mBSPartyDao;

    @Mock
    MBSBaseDao<MBSParty> mBSBaseDao;

    MBSParty mBSPartyMock;
    String sellerSerivcerNo;

    @Before
    public void setUp() throws Exception {

        when(mockRegion.getRegionService()).thenReturn(mockRegionService);
        when(mockRegion.getName()).thenReturn(regionName);
        when(mockRegionService.getRegion(regionName)).thenReturn(mockRegion);

        sellerSerivcerNo = "1234";

        List<TspPartyLender> tspPartyLenders = new ArrayList<>();

        TspPartyLender tspPartyLender = new TspPartyLender();
        tspPartyLender.setEffectiveDate(
                MBSPortalUtils.convertToDateWithFormatter("2018-05-23", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        tspPartyLender.setExpirationDate(
                MBSPortalUtils.convertToDateWithFormatter("2012-05-23", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        tspPartyLender.setLenderSellerServicerNumber("1235");
        tspPartyLender.setName("TEST-CIAM COMPANY");
        tspPartyLender.setShortName("TCCO");
        tspPartyLender.setTspSellerServicerNumber(sellerSerivcerNo);

        // MBS Party Mock
        mBSPartyMock = new MBSParty();
        mBSPartyMock.setEffectiveDate(
                MBSPortalUtils.convertToDateWithFormatter("2018-05-23", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        mBSPartyMock.setExpirationDate(
                MBSPortalUtils.convertToDateWithFormatter("2022-05-23", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        mBSPartyMock.setInstitutionType("TSP");
        mBSPartyMock.setTspPartyLenders(tspPartyLenders);
        mBSPartyMock.setName("UCDP-TEST NON-SSN");
        mBSPartyMock.setMbspPortalCnameUrlBase("https://mbsp01-devl.e003.fanniemae.com");
        mBSPartyMock.setMbspStreamingUrlBase("https://dilv-fmn-a001.fanniemae.com:8443/mbsp-streaming");
        mBSPartyMock.setNameEffectiveDate(
                MBSPortalUtils.convertToDateWithFormatter("2018-05-23", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        mBSPartyMock.setNameExpirationDate(
                MBSPortalUtils.convertToDateWithFormatter("2022-05-23", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        mBSPartyMock.setSellerServicerNumber(sellerSerivcerNo);
        mBSPartyMock.setShortName("UTNS");
        mBSPartyMock.setStateType("ACTIVE");

    }
   

    @SuppressWarnings("rawtypes")
    @Test
    public void getParty_Success() throws MBSBaseException {
        
        doReturn(mBSPartyMock).when((BaseDaoWrapper) baseDaoWrapper).getById(anyString());
        MBSParty mBSPartyActual = mBSPartyDao.getParty(sellerSerivcerNo);
        assertEquals(mBSPartyMock.getSellerServicerNumber(), mBSPartyActual.getSellerServicerNumber());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void removeParty_Success() throws MBSBaseException {
        
        doReturn(mBSPartyMock).when((BaseDaoWrapper) baseDaoWrapper).removeById(anyString());
        MBSParty mBSPartyActual = mBSPartyDao.removeParty(sellerSerivcerNo);
        assertEquals(mBSPartyMock.getSellerServicerNumber(), mBSPartyActual.getSellerServicerNumber());
    }

}
