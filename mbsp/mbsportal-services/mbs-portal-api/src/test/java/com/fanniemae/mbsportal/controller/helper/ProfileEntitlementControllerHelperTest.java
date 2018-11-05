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

package com.fanniemae.mbsportal.controller.helper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.api.cdx.CDXClientApi;
import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.controller.helper.ProfileEntitlementControllerHelper;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementRolePO;
import com.fanniemae.mbsportal.api.po.UserConfigPO;
import com.fanniemae.mbsportal.api.service.ProfileEntitlementService;
import com.fanniemae.mbsportal.api.utils.ServiceUrlUtil;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;


/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Dec 12, 2017
 * @Time 12:13:51 PM
 * 	com.fanniemae.mbsportal.api.controller.helper
 * 	ProfileEntitlementControllerHelperTest.java
 * @Description: Revision 1.1 - Added JUnit Test Cases for getProfile
 */
@RunWith(MockitoJUnitRunner.class)
public class ProfileEntitlementControllerHelperTest {
    
    @InjectMocks
    ProfileEntitlementControllerHelper profileEntitlementControllerHelper;

    @Mock
    ProfileEntitlementService profileEntitlementService;
    
    @Mock
    CDXClientApi cDXClientApi;
    
    @Mock
    MbspProperties mbspProperties;
    
    @Mock
    ServiceUrlUtil serviceUrlUtil;
    
    ProfileEntitlementPO profileEntitlementPO;
    
    String userName;
    private UserConfigPO userConfigPO;
    private String webSocketUrl;
    private String directApiUrl;
    
    @Before
    public void setUp() throws Exception {
        
        userName = "userName";
        
        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementPO.setUserName("username");
        profileEntitlementPO.setFirstName("firstName");
        profileEntitlementPO.setLastName("lastName");
        
        ProfileEntitlementRolePO role = new ProfileEntitlementRolePO();
        role.setName("MBS Trader - Execute");
        
        List<ProfileEntitlementRolePO> roles = new ArrayList<>();
        roles.add(role);
        
        profileEntitlementPO.setRoles(roles);
        
        webSocketUrl = "https://localhost:8443/mbsp-streaming";
        directApiUrl = "https://localhost:8080";
        
        userConfigPO = new UserConfigPO();
        userConfigPO.setWebSocketUrl(webSocketUrl);
        userConfigPO.setDirectApiUrl(directApiUrl);
        
        
        doReturn(userConfigPO).when(serviceUrlUtil).getUserConfigPO(userName, true);
    }
    
    @Test
    public void testGetEntitledProfile() throws MBSBaseException {
        
            Mockito.when(cDXClientApi.getProfileFromToken(Mockito.any())).thenReturn(profileEntitlementPO);
            Mockito.when(profileEntitlementService.saveOrUpdateProfile(Mockito.any(), Mockito.anyBoolean())).thenReturn(profileEntitlementPO);
            Mockito.when(profileEntitlementService.getProfile(Mockito.any())).thenReturn(profileEntitlementPO);
            
            Map<String, String> headers = new HashMap<>();
            ProfileEntitlementPO ProfileEntitlementPORet = profileEntitlementControllerHelper.saveOrUpdateProfile(headers);
            assertEquals(profileEntitlementPO.getUserName(), ProfileEntitlementPORet.getUserName());
    }
    @Test(expected= MBSBusinessException.class)
    public void testGetEntitledProfileException() throws MBSBaseException {
        
            Mockito.when(cDXClientApi.getProfileFromToken(Mockito.any())).thenReturn(profileEntitlementPO);
            Mockito.when(profileEntitlementService.saveOrUpdateProfile(Mockito.any(), Mockito.anyBoolean())).thenReturn(profileEntitlementPO);
            Mockito.when(profileEntitlementService.getProfile(Mockito.any())).thenThrow(new MBSBusinessException("Test Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
            
            Map<String, String> headers = new HashMap<>();
            ProfileEntitlementPO ProfileEntitlementPORet = profileEntitlementControllerHelper.saveOrUpdateProfile(headers);
            assertEquals(profileEntitlementPO.getUserName(), ProfileEntitlementPORet.getUserName());
    }
    
    @Test
    public void testCreateEntitledProfile() throws MBSBaseException {
        
            Mockito.when(cDXClientApi.getProfileFromToken(Mockito.any())).thenReturn(profileEntitlementPO);
            Mockito.when(profileEntitlementService.saveOrUpdateProfile(Mockito.any(), Mockito.anyBoolean())).thenReturn(profileEntitlementPO);
            Mockito.when(profileEntitlementService.getProfile(Mockito.any())).thenReturn(profileEntitlementPO);
            
            Map<String, String> headers = new HashMap<>();
            ProfileEntitlementPO ProfileEntitlementPORet = profileEntitlementControllerHelper.createProfile(profileEntitlementPO, headers);
            assertEquals(profileEntitlementPO.getUserName(), ProfileEntitlementPORet.getUserName());
    }
    
    @Test(expected= MBSBusinessException.class)
    public void testCreateEntitledProfileException() throws MBSBaseException {
        
            Mockito.when(cDXClientApi.getProfileFromToken(Mockito.any())).thenReturn(profileEntitlementPO);
            Mockito.when(profileEntitlementService.saveOrUpdateProfile(Mockito.any(), Mockito.anyBoolean())).thenThrow(new MBSBusinessException("Test Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
            Mockito.when(profileEntitlementService.getProfile(Mockito.any())).thenReturn(profileEntitlementPO);
            
            Map<String, String> headers = new HashMap<>();
            ProfileEntitlementPO ProfileEntitlementPORet = profileEntitlementControllerHelper.createProfile(profileEntitlementPO, headers);
            assertEquals(profileEntitlementPO.getUserName(), ProfileEntitlementPORet.getUserName());
    }
    
    @Test
    public void testClearAllProfiles() throws MBSBaseException {
        
            doNothing().when(profileEntitlementService).clearAll();
            profileEntitlementControllerHelper.clearAllProfiles();
    }
    
    @Test
    public void testgetProfile_Success() throws MBSBaseException {

        Mockito.when(cDXClientApi.getProfileFromToken(Mockito.any())).thenReturn(profileEntitlementPO);
        Mockito.when(profileEntitlementService.saveOrUpdateProfile(Mockito.any(), Mockito.anyBoolean())).thenReturn(profileEntitlementPO);
        Mockito.when(profileEntitlementService.getProfile(Mockito.any())).thenReturn(profileEntitlementPO);

        ProfileEntitlementPO ProfileEntitlementPORet = profileEntitlementControllerHelper.getProfile(userName);
        assertEquals(profileEntitlementPO.getUserName(), ProfileEntitlementPORet.getUserName());
    }

    @Test(expected = MBSBaseException.class)
    public void testgetProfile_Empty_UserName() throws MBSBaseException {

        Mockito.when(cDXClientApi.getProfileFromToken(Mockito.any())).thenReturn(profileEntitlementPO);
        Mockito.when(profileEntitlementService.saveOrUpdateProfile(Mockito.any(), Mockito.anyBoolean())).thenReturn(profileEntitlementPO);
        Mockito.when(profileEntitlementService.getProfile(Mockito.any())).thenReturn(profileEntitlementPO);

        profileEntitlementControllerHelper.getProfile(null);
    }

}
