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

package com.fanniemae.mbsportal.enrichment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.api.enrichment.ProfileEntitlemenPOEnrichment;
import com.fanniemae.mbsportal.api.po.PartyPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementRolePO;
import com.fanniemae.mbsportal.api.po.TspPartyLenderPO;
import com.fanniemae.mbsportal.api.po.UserConfigPO;
import com.fanniemae.mbsportal.api.utils.ServiceUrlUtil;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 31, 2018
 * @File: com.fanniemae.mbsportal.enrichment.ProfileEntitlemenPOEnrichmentTest.java
 * @Revision:
 * @Description: ProfileEntitlemenPOEnrichmentTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class ProfileEntitlemenPOEnrichmentTest {

    @InjectMocks
    private ProfileEntitlemenPOEnrichment<TransformationObject> profileEntitlemenPOEnrichment;

    @Mock
    private ServiceUrlUtil serviceUrlUtil;

    private TransformationObject transformationObject;

    private String sellerSerivcerNo;
    private ProfileEntitlementPO profileEntitlementPO;
    private PartyPO partyPO;
    private PartyPO tspPartyPO;
    private String webSocketUrl;
    private String directApiUrl;
    private UserConfigPO userConfigPO;

    @Before
    public void setUp() throws Exception {

        webSocketUrl = "https://localhost:8443/mbsp-streaming";
        directApiUrl = "https://localhost:8080";
        sellerSerivcerNo = "1234";
        createLenderProfile();
        createUserConfigPO();
        createParty();
        createTSPParty();
        transformationObject = new TransformationObject();

    }

    @Test
    public void enrich_Success() throws Exception {

        doReturn(userConfigPO).when(serviceUrlUtil).getUserConfigPO(anyString(), anyBoolean());

        transformationObject.setSourcePojo(profileEntitlementPO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PARTY_INFO, partyPO);

        profileEntitlemenPOEnrichment.enrich(transformationObject);
        ProfileEntitlementPO profileEntitlementPORet = (ProfileEntitlementPO) transformationObject.getSourcePojo();

        assertEquals(profileEntitlementPO.getPartyShortName(), profileEntitlementPORet.getPartyShortName());
        assertEquals(userConfigPO.getDirectApiUrl(), profileEntitlementPORet.getUserConfig().getDirectApiUrl());
        assertEquals(userConfigPO.getWebSocketUrl(), profileEntitlementPORet.getUserConfig().getWebSocketUrl());
        assertNull(profileEntitlementPORet.getTspLenders());

    }

    @Test
    public void enrich_TSP_Success() throws Exception {

        doReturn(userConfigPO).when(serviceUrlUtil).getUserConfigPO(anyString(), anyBoolean());

        transformationObject.setSourcePojo(profileEntitlementPO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PARTY_INFO, tspPartyPO);

        profileEntitlemenPOEnrichment.enrich(transformationObject);
        ProfileEntitlementPO profileEntitlementPORet = (ProfileEntitlementPO) transformationObject.getSourcePojo();

        assertEquals(profileEntitlementPO.getPartyShortName(), profileEntitlementPORet.getPartyShortName());
        assertEquals(userConfigPO.getDirectApiUrl(), profileEntitlementPORet.getUserConfig().getDirectApiUrl());
        assertEquals(userConfigPO.getWebSocketUrl(), profileEntitlementPORet.getUserConfig().getWebSocketUrl());
        assertEquals(tspPartyPO.getTspPartyLenders().size(), profileEntitlementPORet.getTspLenders().size());

    }

    @Test(expected = MBSSystemException.class)
    public void enrich_Throw_Error_UserConfig_Failure() throws Exception {

        doThrow(MBSSystemException.class).when(serviceUrlUtil).getUserConfigPO(anyString(), anyBoolean());

        transformationObject.setSourcePojo(profileEntitlementPO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PARTY_INFO, partyPO);

        profileEntitlemenPOEnrichment.enrich(transformationObject);

    }

    /**
     * 
     * Create Party Info
     */
    private void createParty() {

        partyPO = new PartyPO();
        partyPO.setEffectiveDate("2018-05-23");
        partyPO.setExpirationDate("2022-05-23");
        partyPO.setInstitutionType("TSP");
        partyPO.setTspPartyLenders(null);
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
     * Create TSP Party Info
     */
    private void createTSPParty() {

        List<TspPartyLenderPO> tspPartyPOLenders = new ArrayList<>();

        TspPartyLenderPO tspPartyLenderPO = new TspPartyLenderPO();
        tspPartyLenderPO.setEffectiveDate("2018-05-23");
        tspPartyLenderPO.setExpirationDate("2012-05-23");
        tspPartyLenderPO.setLenderSellerServicerNumber("1235");
        tspPartyLenderPO.setName("TEST-CIAM COMPANY");
        tspPartyLenderPO.setShortName("TCCO");

        tspPartyPOLenders.add(tspPartyLenderPO);

        tspPartyPO = new PartyPO();
        tspPartyPO.setEffectiveDate("2018-05-23");
        tspPartyPO.setExpirationDate("2022-05-23");
        tspPartyPO.setInstitutionType("TSP");
        tspPartyPO.setTspPartyLenders(tspPartyPOLenders);
        tspPartyPO.setName("UCDP-TEST NON-SSN");
        tspPartyPO.setMbspPortalCnameUrlBase("https://mbsp01-devl.e003.fanniemae.com");
        tspPartyPO.setMbspStreamingUrlBase("https://dilv-fmn-a001.fanniemae.com:8443/mbsp-streaming");
        tspPartyPO.setNameEffectiveDate("2018-05-23");
        tspPartyPO.setNameExpirationDate("2022-05-23");
        tspPartyPO.setSellerServicerNumber(sellerSerivcerNo);
        tspPartyPO.setShortName("UTNS");
        tspPartyPO.setStateType("ACTIVE");
    }

    /**
     * 
     * Create Profile Info
     */
    private void createLenderProfile() {

        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementPO.setFirstName("MBSTrade");
        profileEntitlementPO.setLastName("Execute");
        profileEntitlementPO.setEmailAddress("localtestuser@mbsp.com");
        profileEntitlementPO.setMobileNumber("2027526632");
        profileEntitlementPO.setWorkNumber("2027526632");
        profileEntitlementPO.setSellerServicerNumber("28577");
        profileEntitlementPO.setCustomerName("");
        profileEntitlementPO.setUserName("p3hbrmxe");
        profileEntitlementPO.setDealerOrgName("MOVEMENT MORTGAGE, LLC");
        profileEntitlementPO.setDealerOrgId("708994");
        List<ProfileEntitlementRolePO> roleList = new ArrayList<>();
        ProfileEntitlementRolePO rolePO = new ProfileEntitlementRolePO();
        rolePO.setName(EntitlementRole.LENDER_TRADE_EXECUTE);
        roleList.add(rolePO);

        profileEntitlementPO.setRoles(roleList);
        profileEntitlementPO.setSellerServicerDetails(null);
        profileEntitlementPO.setInstitutionId("");
        profileEntitlementPO.setDefaultSellerServicerNumber("");
        profileEntitlementPO.setLenderDetails("");
        profileEntitlementPO.setFannieMaeUser(false);
        profileEntitlementPO.setPartyShortName("TEST-C");
        profileEntitlementPO.setBrsUserName(null);
    }

    /**
     * 
     * Create User Config
     */
    private void createUserConfigPO() {

        userConfigPO = new UserConfigPO();
        userConfigPO.setDirectApiUrl(directApiUrl);
        userConfigPO.setWebSocketUrl(webSocketUrl);
    }

}
