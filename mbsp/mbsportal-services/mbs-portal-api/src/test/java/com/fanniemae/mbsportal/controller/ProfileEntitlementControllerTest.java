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

package com.fanniemae.mbsportal.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;

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
import org.springframework.http.ResponseEntity;

import com.fanniemae.mbsportal.api.controller.ProfileEntitlementController;
import com.fanniemae.mbsportal.api.controller.helper.ProfileEntitlementControllerHelper;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementRolePO;
import com.fanniemae.mbsportal.api.service.ExceptionLookupService;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Nov 22, 2017
 * @Time 10:53:07 AM com.fanniemae.mbsportal.api.controller
 *       ProductControllerTest.java
 * @Description: Incorporate the Internal Server Error - Status Code 500
 */
@RunWith(MockitoJUnitRunner.class)
public class ProfileEntitlementControllerTest {

    @InjectMocks
    ProfileEntitlementController profileEntitlementController;

    @Mock
    ProfileEntitlementControllerHelper profileEntitlementControllerHelper;
    
    @Mock
    ExceptionLookupService exceptionLookupService;

    ProfileEntitlementPO profileEntitlementPO;

    @Before
    public void setUp() throws Exception {

        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementPO.setUserName("username");
        profileEntitlementPO.setFirstName("firstName");
        profileEntitlementPO.setLastName("lastName");

        ProfileEntitlementRolePO role = new ProfileEntitlementRolePO();
        role.setName("MBS Trader - Execute");

        List<ProfileEntitlementRolePO> roles = new ArrayList<>();
        roles.add(role);

        profileEntitlementPO.setRoles(roles);
        Mockito.when(exceptionLookupService.getExceptionLookupData(Mockito.any())).thenReturn(createExceptionData());
    }

    @Test
    public void testGetEntitledProfile() throws MBSBaseException {

        Mockito.when(profileEntitlementControllerHelper.saveOrUpdateProfile(Mockito.any()))
                .thenReturn(profileEntitlementPO);
        Map<String, String> headers = new HashMap<>();
        ResponseEntity<Object> responseObj = profileEntitlementController.saveOrUpdateProfile(headers);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
    }

    @Test
    public void testGetEntitledProfileException() throws MBSBaseException {

        Mockito.when(profileEntitlementControllerHelper.saveOrUpdateProfile(Mockito.any()))
                .thenThrow(new MBSBusinessException("Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
        Map<String, String> headers = new HashMap<>();
        ResponseEntity<Object> responseObj = profileEntitlementController.saveOrUpdateProfile(headers);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("400"));
    }

    @Test
    public void testGetEntitledProfileInternalServerError() throws MBSBaseException {

        Mockito.when(profileEntitlementControllerHelper.saveOrUpdateProfile(Mockito.any()))
                .thenThrow(new RuntimeException("Runtine Exception!"));
        Map<String, String> headers = new HashMap<>();
        ResponseEntity<Object> responseObj = profileEntitlementController.saveOrUpdateProfile(headers);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }

    @Test
    public void testCreateEntitledProfile() throws MBSBaseException {

        Mockito.when(profileEntitlementControllerHelper.createProfile(Mockito.any(), Mockito.any()))
                .thenReturn(profileEntitlementPO);
        Map<String, String> headers = new HashMap<>();
        ResponseEntity<Object> responseObj = profileEntitlementController.createProfile(profileEntitlementPO,
                headers);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
    }

    @Test
    public void testCreateEntitledProfileException() throws MBSBaseException {

        Mockito.when(profileEntitlementControllerHelper.createProfile(Mockito.any(), Mockito.any()))
                .thenThrow(new MBSBusinessException("Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
        Map<String, String> headers = new HashMap<>();
        ResponseEntity<Object> responseObj = profileEntitlementController.createProfile(profileEntitlementPO,
                headers);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("400"));
    }

    @Test
    public void testCreateEntitledProfileInternalServerError() throws MBSBaseException {

        Mockito.when(profileEntitlementControllerHelper.createProfile(Mockito.any(), Mockito.any()))
                .thenThrow(new RuntimeException("Runtine Exception!"));
        Map<String, String> headers = new HashMap<>();
        ResponseEntity<Object> responseObj = profileEntitlementController.createProfile(profileEntitlementPO,
                headers);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }
    
    @Test
    public void testCreateEntitledProfileInternalServerErrorExceptionRetrieval() throws MBSBaseException {

        Mockito.when(profileEntitlementControllerHelper.createProfile(Mockito.any(), Mockito.any()))
                .thenThrow(new RuntimeException("Runtine Exception!"));
        Map<String, String> headers = new HashMap<>();
        Mockito.when(exceptionLookupService.getExceptionLookupData(Mockito.any())).thenThrow(new MBSBaseException("test"));
        ResponseEntity<Object> responseObj = profileEntitlementController.createProfile(profileEntitlementPO,
                headers);
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }

    @Test
    public void testClearAllProfiles() throws MBSBaseException {

        doNothing().when(profileEntitlementControllerHelper).clearAllProfiles();
        profileEntitlementController.clearAllProfiles();
    }

    @Test
    public void testGetProfile() throws MBSBaseException {

        Mockito.when(profileEntitlementControllerHelper.getProfile(Mockito.any())).thenReturn(profileEntitlementPO);
        ResponseEntity<Object> responseObj = profileEntitlementController.getProfile("username");
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("200"));
    }

    @Test
    public void testGetProfileException() throws MBSBaseException {

        Mockito.when(profileEntitlementControllerHelper.getProfile(Mockito.any()))
                .thenThrow(new MBSBusinessException("Exception", MBSExceptionConstants.BUSINESS_EXCEPTION));
        ResponseEntity<Object> responseObj = profileEntitlementController.getProfile("username");
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("400"));
    }

    @Test
    public void testGetProfileINternalServerError() throws MBSBaseException {

        Mockito.when(profileEntitlementControllerHelper.getProfile(Mockito.any()))
                .thenThrow(new RuntimeException("Runtine Exception!"));
        ResponseEntity<Object> responseObj = profileEntitlementController.getProfile("username");
        assertTrue(responseObj.hasBody());
        assertTrue(responseObj.getStatusCode().toString().equals("500"));
    }
    
    public List<ExceptionLookupPO> createExceptionData() {
        List<ExceptionLookupPO> exceptionLookupPOLst = new ArrayList<>();
        ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        exceptionLookupPO.setErrorCategory("API");
        exceptionLookupPO.setErrorCode("TRANS_00001");
        exceptionLookupPO.setMessageType("DISP_ERROR");
        exceptionLookupPO.setErrorMessage("Test Message");
        exceptionLookupPOLst.add(exceptionLookupPO);
        return exceptionLookupPOLst;
    }

}
