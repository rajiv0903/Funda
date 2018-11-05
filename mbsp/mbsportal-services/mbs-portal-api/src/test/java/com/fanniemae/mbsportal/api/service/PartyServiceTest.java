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

package com.fanniemae.mbsportal.api.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fanniemae.mbsportal.api.enrichment.PartyEnrichment;
import com.fanniemae.mbsportal.api.persister.PartyPersister;
import com.fanniemae.mbsportal.api.po.PartyPO;
import com.fanniemae.mbsportal.api.po.TspPartyLenderPO;
import com.fanniemae.mbsportal.api.transformation.PartyPOTransformer;
import com.fanniemae.mbsportal.api.transformation.PartyTransformer;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.api.validator.PartyValidator;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.dao.MBSPartyDao;
import com.fanniemae.mbsportal.model.MBSParty;
import com.fanniemae.mbsportal.model.TspPartyLender;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Dec 12, 2017
 * @Time 12:04:30 PM com.fanniemae.mbsportal.api.service
 *       ProfileEntitlementServiceTest.java
 * @Description: Revision 1.1- Added Test Cases for getUserNames and other
 *               scenarios
 */

public class PartyServiceTest extends BaseServiceTest {

    @Mock
    MBSPartyDao mBSPartyDao;

    @InjectMocks
    PartyService partyService;

    @Mock
    MBSObjectCreator mbsObjectCreator;

    String sellerSerivcerNo;
    private PartyPO partyPOMock;
    private MBSParty mBSPartyMock;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {

        sellerSerivcerNo = "1234";
        createParty();
        createMbsParty();
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void saveParty_Success() throws MBSBaseException {
        try {
            PartyValidator partyValidator = Mockito.spy(PartyValidator.class);
            PartyTransformer partyTransformer = Mockito.spy(PartyTransformer.class);
            PartyEnrichment partyEnrichment = Mockito.spy(PartyEnrichment.class);
            PartyPersister partyPersister = Mockito.mock(PartyPersister.class);
            PartyPOTransformer partyPOTransformer = Mockito.spy(PartyPOTransformer.class);

            partyService = new PartyService(partyValidator, partyTransformer, partyEnrichment, partyPersister,
                    partyPOTransformer);
            MockitoAnnotations.initMocks(this);

            when(mBSPartyDao.getById(anyInt())).thenReturn(mBSPartyMock);
            when(((Persister) partyPersister).getBaseDao()).thenReturn(mBSPartyDao);
            when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
            partyService.saveParty(partyPOMock);

        } catch (MBSBaseException e) {
            Assert.fail("Should not have thrown any exception");
        }
    }

    /**
     * @throws MBSBaseException
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void saveParty_For_Not_Existing_Profile_Success() throws MBSBaseException {
        try {
            PartyValidator partyValidator = Mockito.spy(PartyValidator.class);
            PartyTransformer partyTransformer = Mockito.spy(PartyTransformer.class);
            PartyEnrichment partyEnrichment = Mockito.spy(PartyEnrichment.class);
            PartyPersister partyPersister = Mockito.mock(PartyPersister.class);
            PartyPOTransformer partyPOTransformer = Mockito.spy(PartyPOTransformer.class);

            partyService = new PartyService(partyValidator, partyTransformer, partyEnrichment, partyPersister,
                    partyPOTransformer);
            MockitoAnnotations.initMocks(this);

            doThrow(Exception.class).when(mBSPartyDao).getById(any());
            when(((Persister) partyPersister).getBaseDao()).thenReturn(mBSPartyDao);
            when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
            partyService.saveParty(partyPOMock);

        } catch (MBSBaseException e) {
            Assert.fail("Should not have thrown any exception");
        }

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void getParty_Success() throws MBSBaseException {

        PartyValidator partyValidator = Mockito.spy(PartyValidator.class);
        PartyTransformer partyTransformer = Mockito.spy(PartyTransformer.class);
        PartyEnrichment partyEnrichment = Mockito.spy(PartyEnrichment.class);
        PartyPersister partyPersister = Mockito.mock(PartyPersister.class);
        PartyPOTransformer partyPOTransformer = Mockito.spy(PartyPOTransformer.class);

        partyService = new PartyService(partyValidator, partyTransformer, partyEnrichment, partyPersister,
                partyPOTransformer);
        MockitoAnnotations.initMocks(this);

        when(mBSPartyDao.getById(anyInt())).thenReturn(mBSPartyMock);
        when(((Persister) partyPersister).getBaseDao()).thenReturn(mBSPartyDao);

        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSPartyMock);
        transformationObject.setSourcePojo(partyPOMock);
        doReturn(transformationObject).when((Transformer) partyPOTransformer).transform(any());
        when(mbsObjectCreator.getTransformationObject()).thenReturn(transformationObject);
        PartyPO partyPOActual = partyService.getParty(sellerSerivcerNo);
        Assert.assertEquals(partyPOMock.getSellerServicerNumber(), partyPOActual.getSellerServicerNumber());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test(expected = MBSBusinessException.class)
    public void getParty_Party_Does_Not_Exist_Failure() throws MBSBaseException {

        PartyValidator partyValidator = Mockito.spy(PartyValidator.class);
        PartyTransformer partyTransformer = Mockito.spy(PartyTransformer.class);
        PartyEnrichment partyEnrichment = Mockito.spy(PartyEnrichment.class);
        PartyPersister partyPersister = Mockito.mock(PartyPersister.class);
        PartyPOTransformer partyPOTransformer = Mockito.spy(PartyPOTransformer.class);

        partyService = new PartyService(partyValidator, partyTransformer, partyEnrichment, partyPersister,
                partyPOTransformer);
        MockitoAnnotations.initMocks(this);

        when(mBSPartyDao.getById(anyInt())).thenReturn(null);
        when(((Persister) partyPersister).getBaseDao()).thenReturn(mBSPartyDao);

        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSPartyMock);
        transformationObject.setSourcePojo(partyPOMock);
        doReturn(transformationObject).when((Transformer) partyPOTransformer).transform(any());
        partyService.getParty(sellerSerivcerNo);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void clear_Success() throws MBSBaseException {

        PartyValidator partyValidator = Mockito.spy(PartyValidator.class);
        PartyTransformer partyTransformer = Mockito.spy(PartyTransformer.class);
        PartyEnrichment partyEnrichment = Mockito.spy(PartyEnrichment.class);
        PartyPersister partyPersister = Mockito.mock(PartyPersister.class);
        PartyPOTransformer partyPOTransformer = Mockito.spy(PartyPOTransformer.class);

        when(mBSPartyDao.removeById(any())).thenReturn(mBSPartyMock);
        when(((Persister) partyPersister).getBaseDao()).thenReturn(mBSPartyDao);
        doNothing().when((Persister) partyPersister).clear(any());

        partyService = new PartyService(partyValidator, partyTransformer, partyEnrichment, partyPersister,
                partyPOTransformer);
        MockitoAnnotations.initMocks(this);

        partyService.clear(sellerSerivcerNo);

    }

    @SuppressWarnings("rawtypes")
    @Test
    public void clearAll_Success() throws MBSBaseException {

        PartyValidator partyValidator = Mockito.spy(PartyValidator.class);
        PartyTransformer partyTransformer = Mockito.spy(PartyTransformer.class);
        PartyEnrichment partyEnrichment = Mockito.spy(PartyEnrichment.class);
        PartyPersister partyPersister = Mockito.mock(PartyPersister.class);
        PartyPOTransformer partyPOTransformer = Mockito.spy(PartyPOTransformer.class);

        when(mBSPartyDao.getById(any())).thenReturn(mBSPartyMock);
        doNothing().when((Persister) partyPersister).clearAll();

        partyService = new PartyService(partyValidator, partyTransformer, partyEnrichment, partyPersister,
                partyPOTransformer);
        MockitoAnnotations.initMocks(this);

        partyService.clearAll();

    }

    /**
     *  Create Party
     */
    private void createParty() {

        List<TspPartyLenderPO> tspPartyPOLenders = new ArrayList<>();

        TspPartyLenderPO tspPartyLenderPO = new TspPartyLenderPO();
        tspPartyLenderPO.setEffectiveDate("2018-05-23");
        tspPartyLenderPO.setExpirationDate("2012-05-23");
        tspPartyLenderPO.setLenderSellerServicerNumber("1235");
        tspPartyLenderPO.setName("TEST-CIAM COMPANY");
        tspPartyLenderPO.setShortName("TCCO");

        tspPartyPOLenders.add(tspPartyLenderPO);

        partyPOMock = new PartyPO();
        partyPOMock.setEffectiveDate("2018-05-23");
        partyPOMock.setExpirationDate("2022-05-23");
        partyPOMock.setInstitutionType("TSP");
        partyPOMock.setTspPartyLenders(tspPartyPOLenders);
        partyPOMock.setName("UCDP-TEST NON-SSN");
        partyPOMock.setMbspPortalCnameUrlBase("https://mbsp01-devl.e003.fanniemae.com");
        partyPOMock.setMbspStreamingUrlBase("https://dilv-fmn-a001.fanniemae.com:8443/mbsp-streaming");
        partyPOMock.setNameEffectiveDate("2018-05-23");
        partyPOMock.setNameExpirationDate("2022-05-23");
        partyPOMock.setSellerServicerNumber(sellerSerivcerNo);
        partyPOMock.setShortName("UTNS");
        partyPOMock.setStateType("ACTIVE");
    }

    /**
     * 
     * Create MBS Party
     * @throws MBSBaseException
     */
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
