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

package com.fanniemae.mbsportal.persister;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.geode.cache.Region;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.fnmpfj.gemfire.dao.BaseDaoImpl;
import com.fanniemae.mbsportal.api.persister.PartyPersister;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.dao.MBSPartyDao;
import com.fanniemae.mbsportal.model.MBSParty;
import com.fanniemae.mbsportal.model.TspPartyLender;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 30, 2018
 * @File: com.fanniemae.mbsportal.persister.PartyPersisterTest.java 
 * @Revision: 
 * @Description: PartyPersisterTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class PartyPersisterTest extends BaseServiceTest{
    
    @InjectMocks
    PartyPersister partyPersister;
    
    @Mock
    MBSPartyDao mBSPartyDao;
    
    @Mock
    Region<String, MBSParty> region;
    
    private String sellerSerivcerNo;
    private MBSParty mBSPartyMock;
    Set<String> keySets;
    
    @Before
    public void setUp() throws Exception {
        
        sellerSerivcerNo = "1234";
        createMbsParty();
        keySets = new HashSet<>();
        keySets.add("1234");
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void getDao_Success() throws MBSBaseException {
        
        BaseDaoImpl baseDaoImpl =  partyPersister.getDao();
        Assert.assertNull(baseDaoImpl);
    }
   
    
    @Test
    public void getBaseDao_Success() throws MBSBaseException {
        
        MBSPartyDao mBSPartyDaoRet =  (MBSPartyDao) partyPersister.getBaseDao();
        Assert.assertNotNull(mBSPartyDaoRet);
    }
    
    
    @Test
    public void persist_Success() throws MBSBaseException {
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSPartyMock);
        doNothing().when(mBSPartyDao).saveOrUpdate(any());
        partyPersister.persist(transformationObject);
    }
    
    @Test
    public void clearAll_Success() throws MBSBaseException {
        
        doReturn(region).when(mBSPartyDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSPartyMock).when(region).destroy(any());
        partyPersister.clearAll();
    }
    
    @Test
    public void clear_Success() throws MBSBaseException {
        
        doReturn(region).when(mBSPartyDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSPartyMock).when(region).destroy(any());
        partyPersister.clear("key");
    }
    
    @Test
    public void removeParty_Success() throws MBSBaseException {
        
        doReturn(region).when(mBSPartyDao).getStorageRegion();
        doReturn(keySets).when(region).keySetOnServer();
        doReturn(mBSPartyMock).when(region).destroy(any());
        partyPersister.removeParty("key");
    }
    
    
    private void createMbsParty() throws MBSBaseException {
        List<TspPartyLender> tspPartyLenders = new ArrayList<>();

        TspPartyLender tspPartyLender = new TspPartyLender();
        tspPartyLender.setEffectiveDate(
                MBSPortalUtils.convertToDateWithFormatter("2018-05-23", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        tspPartyLender.setExpirationDate(
                MBSPortalUtils.convertToDateWithFormatter("2022-05-23", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        tspPartyLender.setLenderSellerServicerNumber("1235");
        tspPartyLender.setName("TEST-CIAM COMPANY");
        tspPartyLender.setShortName("TCCO");

        tspPartyLenders.add(tspPartyLender);

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

}
