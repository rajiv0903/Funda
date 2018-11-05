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

package com.fanniemae.mbsportal.controller.interceptor.entitlement.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fanniemae.mbsportal.api.cdx.CDXClientApi;
import com.fanniemae.mbsportal.api.controller.interceptor.entitlement.service.EntitlementInterceptorService;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementRolePO;
import com.fanniemae.mbsportal.api.po.ProfileSessionPO;
import com.fanniemae.mbsportal.api.service.ExceptionLookupService;
import com.fanniemae.mbsportal.api.service.ProfileEntitlementService;
import com.fanniemae.mbsportal.api.service.ProfileSessionService;
import com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author: Rajiv Chaudhuri
 * @Date: Jul 12, 2018
 * @File: com.fanniemae.mbsportal.controller.interceptor.entitlement.service.EntitlementInterceptorServiceTest.java
 * @Revision:
 * @Description: EntitlementInterceptorServiceTest.java
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class EntitlementInterceptorServiceTest {

    @InjectMocks
    EntitlementInterceptorService entitlementInterceptorService;

    @Mock
    ProfileSessionService profileSessionService;
    @Mock
    ProfileEntitlementService profileEntitlementService;
    @Mock
    CDXClientApi cDXClientApi;

    @Mock
    ExceptionLookupService exceptionLookupService;
    
    @Mock
    ObjectMapper mapper;
    MockHttpServletRequest mockedRequest;
    MockHttpServletResponse mockedResponse;

    
    
    List<String> authorizedRoles = Arrays.asList(new String[] {  
            EntitlementRole.ADMIN, 
            EntitlementRole.MBSP_FM_ADMIN_HE, 
            EntitlementRole.MBSP_FM_ADMIN_LE,
            EntitlementRole.TRADER_TRADE_EXECUTE , 
            EntitlementRole.MBSP_FM_TRADER_EXECUTE_HE, 
            EntitlementRole.MBSP_FM_TRADER_EXECUTE_LE});
    
    List<String> lenderRoles = Arrays.asList(new String[] { EntitlementRole.LENDER_TRADE_EXECUTE });
    
    List<String> readOnlyRoles = Arrays.asList(new String[] { 
            EntitlementRole.MBSP_FM_TRADER_READ_ONLY_HE,
            EntitlementRole.MBSP_FM_TRADER_READ_ONLY_LE});
    
    ProfileSessionPO profileSessionPO;
    ProfileEntitlementPO profileEntitlementPO;
    String userName = "userName";
    String sessionId = "sessionId";
    
    @Before
    public void setUp() throws Exception{
        
        mockedRequest = new MockHttpServletRequest();
        mockedResponse = new MockHttpServletResponse();
        
        mockedRequest.addHeader(CDXHeaderMap.SESSION_ID.getValue(), "sessionid");
        mockedRequest.addHeader(CDXHeaderMap.CHANNEL.getValue(), "web");
        mockedRequest.addHeader(CDXHeaderMap.SUB_CHANNEL.getValue(), "MBSP");
        
        profileSessionPO = new ProfileSessionPO();
        profileSessionPO.setUserName(userName);
        profileSessionPO.setSessionId(sessionId);
        
        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementPO.setUserName(userName);
        profileEntitlementPO.setRoles(assignRoleToProfile(authorizedRoles));
        
        List<ExceptionLookupPO> exceptionLookupPOLst = createExceptionData(MBSExceptionConstants.SYSM_0001);

        
        doReturn(exceptionLookupPOLst).when(exceptionLookupService).getExceptionLookupData(any());
        doReturn("{ message: test }").when(mapper).writeValueAsString(any());
    }
    
    public List<ExceptionLookupPO> createExceptionData(String errorCode) {
        List<ExceptionLookupPO> exceptionLookupPOLst = new ArrayList<>();
        ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        exceptionLookupPO.setErrorCategory("API");
        exceptionLookupPO.setErrorCode(errorCode);
        exceptionLookupPO.setMessageType("DISP_ERROR");
        exceptionLookupPO.setErrorMessage("Test Message");
        exceptionLookupPOLst.add(exceptionLookupPO);
        return exceptionLookupPOLst;
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void performAuthorizationUsingSessionID_Session_Exists_Success() throws Exception {
               
        doReturn(profileSessionPO).when(profileSessionService).getProfileSession(any());
        doReturn(profileEntitlementPO).when(profileEntitlementService).getProfile(any());
        
        authorizedRoles = authorizedRoles.stream().map(String::toLowerCase).collect(Collectors.toList());

        boolean status = entitlementInterceptorService.performAuthorizationUsingSessionID(sessionId, authorizedRoles, mockedRequest, mockedResponse);
        assertEquals(HttpStatus.OK.value() , mockedResponse.getStatus());
        assertTrue(status);
    }
    
    /**
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void performAuthorizationUsingSessionID_Session_Not_Exists_Success() throws Exception {
               
        doReturn(null).when(profileSessionService).getProfileSession(any());
        doReturn(profileEntitlementPO).when(cDXClientApi).getProfileFromSessionID(anyMap());
        doReturn(profileSessionPO).when(profileSessionService).saveProfileSession(anyObject());
        
        doReturn(profileEntitlementPO).when(profileEntitlementService).getProfile(any());
        
        authorizedRoles = authorizedRoles.stream().map(String::toLowerCase).collect(Collectors.toList());                
        boolean status = entitlementInterceptorService.performAuthorizationUsingSessionID(sessionId, authorizedRoles, mockedRequest, mockedResponse);
        assertEquals(HttpStatus.OK.value() , mockedResponse.getStatus());
        assertTrue(status);
    }
    
    
    
    /**
     * 
     * @throws Exception
     */
    @Test
    public void performAuthorizationUsingSessionID_Not_Supported_Role_Un_Authorized_Failure() throws Exception {
               
        doReturn(profileSessionPO).when(profileSessionService).getProfileSession(any());
        //Assign Lender Role - Which is not supported 
        profileEntitlementPO.setRoles(assignRoleToProfile(lenderRoles));        
        doReturn(profileEntitlementPO).when(profileEntitlementService).getProfile(any());
        
        authorizedRoles = authorizedRoles.stream().map(String::toLowerCase).collect(Collectors.toList());

        boolean status = entitlementInterceptorService.performAuthorizationUsingSessionID(sessionId, authorizedRoles, mockedRequest, mockedResponse);
        assertEquals(HttpStatus.UNAUTHORIZED.value() , mockedResponse.getStatus());
        assertFalse(status);
    }
    
    /**
     * 
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @Test
    public void performAuthorizationUsingSessionID_Not_Supported_Role_ISE_Failure() throws Exception {
               
        doReturn(profileSessionPO).when(profileSessionService).getProfileSession(any());
        //Assign Lender Role - Which is not supported 
        profileEntitlementPO.setRoles(assignRoleToProfile(lenderRoles));        
        doReturn(profileEntitlementPO).when(profileEntitlementService).getProfile(any());

        doThrow(new MBSBaseException("test")).when(exceptionLookupService).getExceptionLookupData(any());
        authorizedRoles = authorizedRoles.stream().map(String::toLowerCase).collect(Collectors.toList());

        boolean status = entitlementInterceptorService.performAuthorizationUsingSessionID(sessionId, authorizedRoles, mockedRequest, mockedResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value() , mockedResponse.getStatus());
        assertFalse(status);
    }
    
    
    /**
     * 
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @Test
    public void performAuthorizationUsingSessionID_Not_Supported_Role_ISE_Msg_Failure() throws Exception {
               
        doReturn(profileSessionPO).when(profileSessionService).getProfileSession(any());
        //Assign Lender Role - Which is not supported 
        profileEntitlementPO.setRoles(assignRoleToProfile(lenderRoles));        
        doReturn(profileEntitlementPO).when(profileEntitlementService).getProfile(any());

        List<ExceptionLookupPO> exceptionLookupPOLst = createExceptionData(MBSExceptionConstants.SYSM_0002);
        doReturn(exceptionLookupPOLst).when(exceptionLookupService).getExceptionLookupData(any());
        authorizedRoles = authorizedRoles.stream().map(String::toLowerCase).collect(Collectors.toList());

        boolean status = entitlementInterceptorService.performAuthorizationUsingSessionID(sessionId, authorizedRoles, mockedRequest, mockedResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value() , mockedResponse.getStatus());
        assertFalse(status);
    }
    
    
    /**
     * 
     * @throws Exception
     */
    @Test
    public void performAuthorizationUsingSessionID_Not_Authorized_Role_Un_Authorized_Failure() throws Exception {
               
        doReturn(profileSessionPO).when(profileSessionService).getProfileSession(any());
        //Assign Lender Role - Which is not supported 
        profileEntitlementPO.setRoles(assignRoleToProfile(readOnlyRoles));        
        doReturn(profileEntitlementPO).when(profileEntitlementService).getProfile(any());
        
        authorizedRoles = authorizedRoles.stream().map(String::toLowerCase).collect(Collectors.toList());

        boolean status = entitlementInterceptorService.performAuthorizationUsingSessionID(sessionId, authorizedRoles, mockedRequest, mockedResponse);
        assertEquals(HttpStatus.UNAUTHORIZED.value() , mockedResponse.getStatus());
        assertFalse(status);
    }
    
    /**
     * 
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @Test
    public void performAuthorizationUsingSessionID_Not_Authorized_Role_ISE_Failure() throws Exception {
               
        doReturn(profileSessionPO).when(profileSessionService).getProfileSession(any());
        //Assign Lender Role - Which is not supported 
        profileEntitlementPO.setRoles(assignRoleToProfile(readOnlyRoles));        
        doReturn(profileEntitlementPO).when(profileEntitlementService).getProfile(any());

        doThrow(new MBSBaseException("test")).when(exceptionLookupService).getExceptionLookupData(any());
        authorizedRoles = authorizedRoles.stream().map(String::toLowerCase).collect(Collectors.toList());

        boolean status = entitlementInterceptorService.performAuthorizationUsingSessionID(sessionId, authorizedRoles, mockedRequest, mockedResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value() , mockedResponse.getStatus());
        assertFalse(status);
    }
    
    
    /**
     * 
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @Test
    public void performAuthorizationUsingSessionID_Not_Authorized_Role_ISE_Msg_Failure() throws Exception {
               
        doReturn(profileSessionPO).when(profileSessionService).getProfileSession(any());
        //Assign Lender Role - Which is not supported 
        profileEntitlementPO.setRoles(assignRoleToProfile(readOnlyRoles));        
        doReturn(profileEntitlementPO).when(profileEntitlementService).getProfile(any());

        List<ExceptionLookupPO> exceptionLookupPOLst = createExceptionData(MBSExceptionConstants.SYSM_0002);
        doReturn(exceptionLookupPOLst).when(exceptionLookupService).getExceptionLookupData(any());
        authorizedRoles = authorizedRoles.stream().map(String::toLowerCase).collect(Collectors.toList());

        boolean status = entitlementInterceptorService.performAuthorizationUsingSessionID(sessionId, authorizedRoles, mockedRequest, mockedResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value() , mockedResponse.getStatus());
        assertFalse(status);
    }
    
    /**
     * Assign the role 
     * @param roles
     */
    private List<ProfileEntitlementRolePO> assignRoleToProfile(List<String> roles){
        
        List<ProfileEntitlementRolePO> roleLst = new ArrayList<>();
        
        for(String role: roles){
            ProfileEntitlementRolePO profileEntitlementRolePO = new ProfileEntitlementRolePO();
            profileEntitlementRolePO.setName(role);
            
            roleLst.add(profileEntitlementRolePO);
        }
        
        return roleLst;
    }
}
