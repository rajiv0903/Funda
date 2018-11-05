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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
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

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.enrichment.ProfileEntitlemenPOEnrichment;
import com.fanniemae.mbsportal.api.persister.ProfileEntitlemenPersister;
import com.fanniemae.mbsportal.api.po.PartyPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementRolePO;
import com.fanniemae.mbsportal.api.po.TspPartyLenderPO;
import com.fanniemae.mbsportal.api.po.UserConfigPO;
import com.fanniemae.mbsportal.api.transformation.ProfileEntitlemenPOTransformer;
import com.fanniemae.mbsportal.api.transformation.ProfileEntitlementTransformer;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.api.validator.ProfileEntitlementValidator;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.dao.MBSProfileDao;
import com.fanniemae.mbsportal.model.MBSProfile;
import com.fanniemae.mbsportal.model.MBSProfileRole;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Dec 12, 2017
 * @Time 12:04:30 PM
 * 	com.fanniemae.mbsportal.api.service
 * 	ProfileEntitlementServiceTest.java
 * @Description: Revision 1.1- Added Test Cases for getUserNames and other scenarios 
 */

public class ProfileEntitlementServiceTest extends BaseServiceTest {

    @Mock
    private MBSProfileDao mBSProfileDao;
    @Mock
    private PartyService partyService;
    @Mock
    private MBSObjectCreator mbsObjectCreator;
    @Mock
    private MbspProperties mbspProperties;
    @InjectMocks
    private ProfileEntitlementService profileEntitlementService;


    private ProfileEntitlementPO profileEntitlementPOMock;
    private ProfileEntitlementRolePO profileEntitlementRolePOMock;
    private MBSProfile mBSProfileMock;
    private MBSProfileRole mBSProfileRoleMock;
    private String userName;
    private String dealerOrgId;
    private List<MBSProfile> mBSProfileLst;
    private PartyPO partyPO;
    private UserConfigPO userConfigPO;
    private String webSocketUrl;
    private String directApiUrl;
    String sellerSerivcerNo;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        
        userName = "t3user";
        dealerOrgId = "dealerOrgId";
        sellerSerivcerNo = "1234";
        webSocketUrl = "https://localhost:8443/mbsp-streaming";
        directApiUrl = "https://localhost:8080";
        
        createProfile();
        createMbsProfile();
        createParty();        
        createUserConfigPO();

    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void saveOrUpdateProfile_External_User_Merging_Success() throws MBSBaseException {
        try {
            ProfileEntitlementValidator profileEntitlementValidator = Mockito.spy(ProfileEntitlementValidator.class);
            ProfileEntitlementTransformer profileEntitlementTransformer = Mockito.spy(ProfileEntitlementTransformer.class);
            ProfileEntitlemenPersister profileEntitlemenPersister = Mockito.mock(ProfileEntitlemenPersister.class);
            ProfileEntitlemenPOTransformer profileEntitlemenPOTransformer = Mockito.spy(ProfileEntitlemenPOTransformer.class);
            ProfileEntitlemenPOEnrichment profileEntitlemenPOEnrichment = Mockito.mock(ProfileEntitlemenPOEnrichment.class);
            
            profileEntitlementService = new ProfileEntitlementService(profileEntitlementValidator,
                    profileEntitlementTransformer, profileEntitlemenPersister, profileEntitlemenPOTransformer, profileEntitlemenPOEnrichment);
            MockitoAnnotations.initMocks(this);
            
            when(mBSProfileDao.getById(any())).thenReturn(mBSProfileMock);
            when(((Persister) profileEntitlemenPersister).getBaseDao()).thenReturn(mBSProfileDao);
            when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
            doReturn(partyPO).when(partyService).getParty(anyString());
            profileEntitlementService.saveOrUpdateProfile(profileEntitlementPOMock, true);
            
        } catch (MBSBaseException e) {
            Assert.fail("Should not have thrown any exception");
        }

    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void saveOrUpdateProfile_Internal_User_Merging_Success() throws MBSBaseException {
        try {
            ProfileEntitlementValidator profileEntitlementValidator = Mockito.spy(ProfileEntitlementValidator.class);
            ProfileEntitlementTransformer profileEntitlementTransformer = Mockito.spy(ProfileEntitlementTransformer.class);
            ProfileEntitlemenPersister profileEntitlemenPersister = Mockito.mock(ProfileEntitlemenPersister.class);
            ProfileEntitlemenPOTransformer profileEntitlemenPOTransformer = Mockito.spy(ProfileEntitlemenPOTransformer.class);
            ProfileEntitlemenPOEnrichment profileEntitlemenPOEnrichment = Mockito.mock(ProfileEntitlemenPOEnrichment.class);
            
            profileEntitlementService = new ProfileEntitlementService(profileEntitlementValidator,
                    profileEntitlementTransformer, profileEntitlemenPersister, profileEntitlemenPOTransformer, profileEntitlemenPOEnrichment);
            MockitoAnnotations.initMocks(this);
            
            profileEntitlementPOMock.setFannieMaeUser(true);
            
            when(mBSProfileDao.getById(any())).thenReturn(mBSProfileMock);
            when(((Persister) profileEntitlemenPersister).getBaseDao()).thenReturn(mBSProfileDao);
            when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
            doReturn(partyPO).when(partyService).getParty(anyString());
            doReturn(Integer.parseInt(sellerSerivcerNo)).when(mbspProperties).getFnmSellerSerivcerNo();
            profileEntitlementService.saveOrUpdateProfile(profileEntitlementPOMock, true);
            
        } catch (MBSBaseException e) {
            Assert.fail("Should not have thrown any exception");
        }

    }
    
    @SuppressWarnings("rawtypes")
    @Test(expected=MBSBusinessException.class)
    public void saveOrUpdateProfile_External_User_Merging_Without_SSN_Failure() throws MBSBaseException {

        ProfileEntitlementValidator profileEntitlementValidator = Mockito.spy(ProfileEntitlementValidator.class);
        ProfileEntitlementTransformer profileEntitlementTransformer = Mockito.spy(ProfileEntitlementTransformer.class);
        ProfileEntitlemenPersister profileEntitlemenPersister = Mockito.mock(ProfileEntitlemenPersister.class);
        ProfileEntitlemenPOTransformer profileEntitlemenPOTransformer = Mockito.spy(ProfileEntitlemenPOTransformer.class);
        ProfileEntitlemenPOEnrichment profileEntitlemenPOEnrichment = Mockito.mock(ProfileEntitlemenPOEnrichment.class);
        
        profileEntitlementService = new ProfileEntitlementService(profileEntitlementValidator,
                profileEntitlementTransformer, profileEntitlemenPersister, profileEntitlemenPOTransformer, profileEntitlemenPOEnrichment);
        MockitoAnnotations.initMocks(this);
        
        
        profileEntitlementPOMock.setSellerServicerNumber("");
        profileEntitlementPOMock.setFannieMaeUser(false);
        
        when(mBSProfileDao.getById(any())).thenReturn(mBSProfileMock);
        when(((Persister) profileEntitlemenPersister).getBaseDao()).thenReturn(mBSProfileDao);
        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
        doReturn(partyPO).when(partyService).getParty(anyString());
        profileEntitlementService.saveOrUpdateProfile(profileEntitlementPOMock, false);

    }

    
    @SuppressWarnings("rawtypes")
    @Test
    public void saveOrUpdateProfile_Without_Merging_Success() throws MBSBaseException {
        try {
            ProfileEntitlementValidator profileEntitlementValidator = Mockito.spy(ProfileEntitlementValidator.class);
            ProfileEntitlementTransformer profileEntitlementTransformer = Mockito.spy(ProfileEntitlementTransformer.class);
            ProfileEntitlemenPersister profileEntitlemenPersister = Mockito.mock(ProfileEntitlemenPersister.class);
            ProfileEntitlemenPOTransformer profileEntitlemenPOTransformer = Mockito.spy(ProfileEntitlemenPOTransformer.class);
            ProfileEntitlemenPOEnrichment profileEntitlemenPOEnrichment = Mockito.mock(ProfileEntitlemenPOEnrichment.class);
            
            profileEntitlementService = new ProfileEntitlementService(profileEntitlementValidator,
                    profileEntitlementTransformer, profileEntitlemenPersister, profileEntitlemenPOTransformer, profileEntitlemenPOEnrichment);
            MockitoAnnotations.initMocks(this);
            
            when(mBSProfileDao.getById(any())).thenReturn(mBSProfileMock);
            when(((Persister) profileEntitlemenPersister).getBaseDao()).thenReturn(mBSProfileDao);
            when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
            doReturn(partyPO).when(partyService).getParty(anyString());
            profileEntitlementService.saveOrUpdateProfile(profileEntitlementPOMock, false);
            
        } catch (MBSBaseException e) {
            Assert.fail("Should not have thrown any exception");
        }

    }
    /**
     * @throws MBSBaseException
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void saveOrUpdateProfile_For_Not_Existing_Profile_Success() throws MBSBaseException {
        try {
            ProfileEntitlementValidator profileEntitlementValidator = Mockito.spy(ProfileEntitlementValidator.class);
            ProfileEntitlementTransformer profileEntitlementTransformer = Mockito.spy(ProfileEntitlementTransformer.class);
            ProfileEntitlemenPersister profileEntitlemenPersister = Mockito.mock(ProfileEntitlemenPersister.class);
            ProfileEntitlemenPOTransformer profileEntitlemenPOTransformer = Mockito.spy(ProfileEntitlemenPOTransformer.class);
            ProfileEntitlemenPOEnrichment profileEntitlemenPOEnrichment = Mockito.mock(ProfileEntitlemenPOEnrichment.class);
            
            profileEntitlementService = new ProfileEntitlementService(profileEntitlementValidator,
                    profileEntitlementTransformer, profileEntitlemenPersister, profileEntitlemenPOTransformer, profileEntitlemenPOEnrichment);
            MockitoAnnotations.initMocks(this);
            
            doThrow(Exception.class).when(mBSProfileDao).getById(any());
            when(((Persister) profileEntitlemenPersister).getBaseDao()).thenReturn(mBSProfileDao);
            when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
            doReturn(partyPO).when(partyService).getParty(anyString());
            profileEntitlementService.saveOrUpdateProfile(profileEntitlementPOMock, true);
            
        } catch (MBSBaseException e) {
            Assert.fail("Should not have thrown any exception");
        }
    }
    
   
    @SuppressWarnings({ "rawtypes",  "unchecked" })
    @Test
    public void getProfile_Success() throws MBSBaseException {
        
        ProfileEntitlementValidator profileEntitlementValidator = Mockito.spy(ProfileEntitlementValidator.class);
        ProfileEntitlementTransformer profileEntitlementTransformer = Mockito.mock(ProfileEntitlementTransformer.class);
        ProfileEntitlemenPersister profileEntitlemenPersister = Mockito.mock(ProfileEntitlemenPersister.class);
        ProfileEntitlemenPOTransformer profileEntitlemenPOTransformer = Mockito.spy(ProfileEntitlemenPOTransformer.class);
        ProfileEntitlemenPOEnrichment profileEntitlemenPOEnrichment = Mockito.mock(ProfileEntitlemenPOEnrichment.class);
        
        profileEntitlementService = new ProfileEntitlementService(profileEntitlementValidator,
                profileEntitlementTransformer, profileEntitlemenPersister, profileEntitlemenPOTransformer, profileEntitlemenPOEnrichment);
        MockitoAnnotations.initMocks(this);

        when(mBSProfileDao.getById(any())).thenReturn(mBSProfileMock);
        when(((Persister) profileEntitlemenPersister).getBaseDao()).thenReturn(mBSProfileDao);
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSProfileMock);
        transformationObject.setSourcePojo(profileEntitlementPOMock);
        doReturn(transformationObject).when((Transformer) profileEntitlemenPOTransformer).transform(any());
        when(mbsObjectCreator.getTransformationObject()).thenReturn(transformationObject);
        doReturn(partyPO).when(partyService).getParty(anyString());
        ProfileEntitlementPO profileEntitlementPOActual = profileEntitlementService.getProfile(userName);
        Assert.assertEquals(profileEntitlementPOMock.getUserName(), profileEntitlementPOActual.getUserName());
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test(expected=MBSBusinessException.class)
    public void getProfile_Profile_Does_Not_Exist_Failure() throws MBSBaseException {
        
        ProfileEntitlementValidator profileEntitlementValidator = Mockito.spy(ProfileEntitlementValidator.class);
        ProfileEntitlementTransformer profileEntitlementTransformer = Mockito.mock(ProfileEntitlementTransformer.class);
        ProfileEntitlemenPersister profileEntitlemenPersister = Mockito.mock(ProfileEntitlemenPersister.class);
        ProfileEntitlemenPOTransformer profileEntitlemenPOTransformer = Mockito.spy(ProfileEntitlemenPOTransformer.class);
        ProfileEntitlemenPOEnrichment profileEntitlemenPOEnrichment = Mockito.mock(ProfileEntitlemenPOEnrichment.class);
        
        profileEntitlementService = new ProfileEntitlementService(profileEntitlementValidator,
                profileEntitlementTransformer, profileEntitlemenPersister, profileEntitlemenPOTransformer, profileEntitlemenPOEnrichment);
        MockitoAnnotations.initMocks(this);

        when(mBSProfileDao.getById(any())).thenReturn(null);
        when(((Persister) profileEntitlemenPersister).getBaseDao()).thenReturn(mBSProfileDao);
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSProfileMock);
        transformationObject.setSourcePojo(profileEntitlementPOMock);
        doReturn(transformationObject).when((Transformer) profileEntitlemenPOTransformer).transform(any());
        doReturn(partyPO).when(partyService).getParty(anyString());
        profileEntitlementService.getProfile(userName);
    }
    
    
    @SuppressWarnings({ "rawtypes",  "unchecked" })
    @Test(expected=MBSBusinessException.class)
    public void getProfile_Failed_To_Enrich_Party_Failure() throws MBSBaseException {
        
        ProfileEntitlementValidator profileEntitlementValidator = Mockito.spy(ProfileEntitlementValidator.class);
        ProfileEntitlementTransformer profileEntitlementTransformer = Mockito.mock(ProfileEntitlementTransformer.class);
        ProfileEntitlemenPersister profileEntitlemenPersister = Mockito.mock(ProfileEntitlemenPersister.class);
        ProfileEntitlemenPOTransformer profileEntitlemenPOTransformer = Mockito.spy(ProfileEntitlemenPOTransformer.class);
        ProfileEntitlemenPOEnrichment profileEntitlemenPOEnrichment = Mockito.mock(ProfileEntitlemenPOEnrichment.class);
        
        profileEntitlementService = new ProfileEntitlementService(profileEntitlementValidator,
                profileEntitlementTransformer, profileEntitlemenPersister, profileEntitlemenPOTransformer, profileEntitlemenPOEnrichment);
        MockitoAnnotations.initMocks(this);

        when(mBSProfileDao.getById(any())).thenReturn(mBSProfileMock);
        when(((Persister) profileEntitlemenPersister).getBaseDao()).thenReturn(mBSProfileDao);
        
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSProfileMock);
        transformationObject.setSourcePojo(profileEntitlementPOMock);
        
        //set empty ssn
        profileEntitlementPOMock.setSellerServicerNumber(null);
        
        doReturn(transformationObject).when((Transformer) profileEntitlemenPOTransformer).transform(any());
        when(mbsObjectCreator.getTransformationObject()).thenReturn(transformationObject);
        doReturn(partyPO).when(partyService).getParty(anyString());
        profileEntitlementService.getProfile(userName);
    }
    
    

    @SuppressWarnings("rawtypes")
    @Test
    public void clearAll_Success() throws MBSBaseException {

        ProfileEntitlementValidator profileEntitlementValidator = Mockito.spy(ProfileEntitlementValidator.class);
        ProfileEntitlementTransformer profileEntitlementTransformer = Mockito.mock(ProfileEntitlementTransformer.class);
        ProfileEntitlemenPersister profileEntitlemenPersister = Mockito.mock(ProfileEntitlemenPersister.class);
        ProfileEntitlemenPOTransformer profileEntitlemenPOTransformer = Mockito.spy(ProfileEntitlemenPOTransformer.class);
        ProfileEntitlemenPOEnrichment profileEntitlemenPOEnrichment = Mockito.mock(ProfileEntitlemenPOEnrichment.class);

        when(mBSProfileDao.getById(any())).thenReturn(mBSProfileMock);
        doNothing().when((Persister) profileEntitlemenPersister).clearAll();

        profileEntitlementService = new ProfileEntitlementService(profileEntitlementValidator,
                profileEntitlementTransformer, profileEntitlemenPersister, profileEntitlemenPOTransformer, profileEntitlemenPOEnrichment);
        MockitoAnnotations.initMocks(this);
        
        profileEntitlementService.clearAll();

    }

    @SuppressWarnings({ "rawtypes" })
    @Test
    public void getUserNames_Success() throws MBSBaseException {

        ProfileEntitlementValidator profileEntitlementValidator = Mockito.spy(ProfileEntitlementValidator.class);
        ProfileEntitlementTransformer profileEntitlementTransformer = Mockito.mock(ProfileEntitlementTransformer.class);
        ProfileEntitlemenPersister profileEntitlemenPersister = Mockito.mock(ProfileEntitlemenPersister.class);
        ProfileEntitlemenPOTransformer profileEntitlemenPOTransformer = Mockito.spy(ProfileEntitlemenPOTransformer.class);
        ProfileEntitlemenPOEnrichment profileEntitlemenPOEnrichment = Mockito.mock(ProfileEntitlemenPOEnrichment.class);
        
        profileEntitlementService = new ProfileEntitlementService(profileEntitlementValidator,
                profileEntitlementTransformer, profileEntitlemenPersister, profileEntitlemenPOTransformer, profileEntitlemenPOEnrichment);
        MockitoAnnotations.initMocks(this);


        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
        when(mBSProfileDao.getMBSProfile(any())).thenReturn(mBSProfileLst);
        when(((Persister) profileEntitlemenPersister).getBaseDao()).thenReturn(mBSProfileDao);
        doReturn(partyPO).when(partyService).getParty(anyString());
        List<ProfileEntitlementPO> profileLst = profileEntitlementService.getUserNames(userName);
        assertTrue(profileLst.size() == 1);
    }

    @SuppressWarnings({ "rawtypes" })
    @Test
    public void getUserNames_No_Profile_For_Dealer_Success() throws MBSBaseException {

        ProfileEntitlementValidator profileEntitlementValidator = Mockito.spy(ProfileEntitlementValidator.class);
        ProfileEntitlementTransformer profileEntitlementTransformer = Mockito.mock(ProfileEntitlementTransformer.class);
        ProfileEntitlemenPersister profileEntitlemenPersister = Mockito.mock(ProfileEntitlemenPersister.class);
        ProfileEntitlemenPOTransformer profileEntitlemenPOTransformer = Mockito.spy(ProfileEntitlemenPOTransformer.class);
        ProfileEntitlemenPOEnrichment profileEntitlemenPOEnrichment = Mockito.mock(ProfileEntitlemenPOEnrichment.class);
        
        profileEntitlementService = new ProfileEntitlementService(profileEntitlementValidator,
                profileEntitlementTransformer, profileEntitlemenPersister, profileEntitlemenPOTransformer, profileEntitlemenPOEnrichment);
        MockitoAnnotations.initMocks(this);

        when(mBSProfileDao.getMBSProfile(any())).thenReturn(new ArrayList<MBSProfile>());
        when(((Persister) profileEntitlemenPersister).getBaseDao()).thenReturn(mBSProfileDao);
        doReturn(partyPO).when(partyService).getParty(anyString());
        
        List<ProfileEntitlementPO> profileLst = profileEntitlementService.getUserNames(userName);
        assertTrue(profileLst.size() == 0);
    }
    
    @SuppressWarnings("rawtypes")
    @Test(expected=MBSBusinessException.class)
    public void getUserNames_For_User_Name_Null_Failure() throws MBSBaseException {
        
        ProfileEntitlementValidator profileEntitlementValidator = Mockito.spy(ProfileEntitlementValidator.class);
        ProfileEntitlementTransformer profileEntitlementTransformer = Mockito.mock(ProfileEntitlementTransformer.class);
        ProfileEntitlemenPersister profileEntitlemenPersister = Mockito.mock(ProfileEntitlemenPersister.class);
        ProfileEntitlemenPOTransformer profileEntitlemenPOTransformer = Mockito.spy(ProfileEntitlemenPOTransformer.class);
        ProfileEntitlemenPOEnrichment profileEntitlemenPOEnrichment = Mockito.mock(ProfileEntitlemenPOEnrichment.class);
        
        profileEntitlementService = new ProfileEntitlementService(profileEntitlementValidator,
                profileEntitlementTransformer, profileEntitlemenPersister, profileEntitlemenPOTransformer, profileEntitlemenPOEnrichment);
        MockitoAnnotations.initMocks(this);
        
        when(mBSProfileDao.getMBSProfile(any())).thenReturn(new ArrayList<MBSProfile>());
        when(((Persister) profileEntitlemenPersister).getBaseDao()).thenReturn(mBSProfileDao);
        doReturn(partyPO).when(partyService).getParty(anyString());
        
        List<ProfileEntitlementPO> profileLst = profileEntitlementService.getUserNames(null);
        assertTrue(profileLst.size() == 0);
    }
    
    @SuppressWarnings("rawtypes")
    @Test(expected=MBSBusinessException.class)
    public void getUserNames_For_User_Name_Empty_Failure() throws MBSBaseException {
        
        ProfileEntitlementValidator profileEntitlementValidator = Mockito.spy(ProfileEntitlementValidator.class);
        ProfileEntitlementTransformer profileEntitlementTransformer = Mockito.mock(ProfileEntitlementTransformer.class);
        ProfileEntitlemenPersister profileEntitlemenPersister = Mockito.mock(ProfileEntitlemenPersister.class);
        ProfileEntitlemenPOTransformer profileEntitlemenPOTransformer = Mockito.spy(ProfileEntitlemenPOTransformer.class);
        ProfileEntitlemenPOEnrichment profileEntitlemenPOEnrichment = Mockito.spy(ProfileEntitlemenPOEnrichment.class);
        
        profileEntitlementService = new ProfileEntitlementService(profileEntitlementValidator,
                profileEntitlementTransformer, profileEntitlemenPersister, profileEntitlemenPOTransformer, profileEntitlemenPOEnrichment);
        MockitoAnnotations.initMocks(this);
        
        when(mBSProfileDao.getMBSProfile(any())).thenReturn(new ArrayList<MBSProfile>());
        when(((Persister) profileEntitlemenPersister).getBaseDao()).thenReturn(mBSProfileDao);
        doReturn(partyPO).when(partyService).getParty(anyString());
        
        List<ProfileEntitlementPO> profileLst = profileEntitlementService.getUserNames("");
        assertTrue(profileLst.size() == 0);
    }
    
    /**
     * 
     * Create Profile
     */
    private void createProfile() {
        
        profileEntitlementRolePOMock = new ProfileEntitlementRolePO();
        profileEntitlementRolePOMock.setName("MBS Trade - Read Only");

        // Profile Mock
        profileEntitlementPOMock = new ProfileEntitlementPO();
        profileEntitlementPOMock.setUserName(userName);
        profileEntitlementPOMock.setFirstName("fn");
        profileEntitlementPOMock.setLastName("ln");
        profileEntitlementPOMock.setEmailAddress("test@gmail.com");
        profileEntitlementPOMock.addRole(profileEntitlementRolePOMock);
        profileEntitlementPOMock.setSellerServicerNumber("1234");
        profileEntitlementPOMock.setFannieMaeUser(false);
    }

    /**
     * 
     * Create MBS Profile
     */
    private void createMbsProfile() {
        
        mBSProfileRoleMock = new MBSProfileRole();
        mBSProfileRoleMock.setName("MBS Trade - Read Only");
        // MBS Profile Mock
        mBSProfileMock = new MBSProfile();
        mBSProfileMock.setUserName(userName);
        mBSProfileMock.setFirstName("fn");
        mBSProfileMock.setLastName("ln");
        mBSProfileMock.setEmailAddress("test@gmail.com");
        List<MBSProfileRole> mbsRoles = new ArrayList<>();
        mbsRoles.add(mBSProfileRoleMock);
        mBSProfileMock.setRoles(mbsRoles);
        mBSProfileMock.setDealerOrgId(dealerOrgId);
        
        mBSProfileLst  = new ArrayList<>();
        mBSProfileLst.add(mBSProfileMock);
        mBSProfileMock.setSellerServicerNumber("1234");
    }

    /**
     * 
     * Create Party
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

        partyPO = new PartyPO();
        partyPO.setEffectiveDate("2018-05-23");
        partyPO.setExpirationDate("2022-05-23");
        partyPO.setInstitutionType("TSP");
        partyPO.setTspPartyLenders(tspPartyPOLenders);
        partyPO.setName("UCDP-TEST NON-SSN");
        partyPO.setMbspPortalCnameUrlBase("https://mbsp01-devl.e003.fanniemae.com");
        partyPO.setMbspStreamingUrlBase("https://dilv-fmn-a001.fanniemae.com:8443/mbsp-streaming");
        partyPO.setNameEffectiveDate("2018-05-23");
        partyPO.setNameExpirationDate("2022-05-23");
        partyPO.setSellerServicerNumber(sellerSerivcerNo);
        partyPO.setShortName("UTNS");
        partyPO.setStateType("ACTIVE");
    }

    /**
     * 
     * Create User Config PO
     */
    private void createUserConfigPO() {
        
        userConfigPO = new UserConfigPO();
        userConfigPO.setWebSocketUrl(webSocketUrl);
        userConfigPO.setDirectApiUrl(directApiUrl);
    }


}
