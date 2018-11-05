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

package com.fanniemae.mbsportal.controller.interceptor.entitlement;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.method.HandlerMethod;

import com.fanniemae.mbsportal.api.cdx.CDXClientApi;
import com.fanniemae.mbsportal.api.controller.interceptor.entitlement.EntitlementInterceptor;
import com.fanniemae.mbsportal.api.controller.interceptor.entitlement.EntitlementRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.entitlement.service.EntitlementInterceptorService;
import com.fanniemae.mbsportal.api.service.ExceptionLookupService;
import com.fanniemae.mbsportal.cdx.config.CDXApiClientConfig;
import com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author Rajiv Chaudhuri
 * @Date Nov 3, 2017
 * @Time: 4:47:12 PM
 * @Description: com.fanniemae.mbsportal.api.controller.interceptor.entitlement
 *               EntitlementInterceptorTest.java
 * @Purpose: JUnit test class for EntitlementInterceptor
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class EntitlementInterceptorTest {

    @InjectMocks
    EntitlementInterceptor entitlementInterceptor;

    @Mock
    CDXClientApi cDXClientApi;
    @Mock
    CDXApiClientConfig cDXApiClientConfig;
    @Mock
    ExceptionLookupService exceptionLookupService;
    @Mock
    ObjectMapper mapper;
    @Mock
    EntitlementInterceptorService entitlementInterceptorService;
    @Mock
    HandlerMethod handlerMethod;
    @Mock
    EntitlementRequired entitlementRequired;

    MockHttpServletRequest mockedRequest;
    MockHttpServletResponse mockedResponse;

    String[] authorizedRoles = { EntitlementRole.ADMIN, EntitlementRole.TRADER_TRADE_EXECUTE };
    String[] adminRolesStringArray = { EntitlementRole.ADMIN };

    String[] authorizedRolesV2 = { EntitlementRole.MBSP_FM_ADMIN_HE, EntitlementRole.MBSP_FM_TRADER_EXECUTE_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE, EntitlementRole.MBSP_FM_TRADER_EXECUTE_LE };
    
    String[] adminRolesStringArrayV2 = { EntitlementRole.MBSP_FM_ADMIN_HE, EntitlementRole.MBSP_FM_ADMIN_LE };

    List<String> lenderRoles = Arrays.asList(new String[] { EntitlementRole.LENDER_TRADE_EXECUTE });
    List<String> adminRoles = Arrays.asList(new String[] { EntitlementRole.ADMIN });

    List<String> adminRolesV2 = Arrays
            .asList(new String[] { EntitlementRole.MBSP_FM_ADMIN_HE, EntitlementRole.MBSP_FM_ADMIN_LE });

    /**
     * 
     */
    @Before
    public void setUp() throws Exception {

        mockedRequest = new MockHttpServletRequest();
        mockedResponse = new MockHttpServletResponse();

        Mockito.when(exceptionLookupService.getExceptionLookupData(Mockito.any())).thenReturn(createExceptionData(MBSExceptionConstants.SYSM_0001));
        Mockito.when(mapper.writeValueAsString(Mockito.any())).thenReturn("{ message: test }");
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
    public void preHandle_PassThrough_No_Token_No_SessionID_Success() throws Exception {

        doReturn(true).when(cDXApiClientConfig).isEntitlementpassthrough();
        doReturn(entitlementRequired).when(handlerMethod).getMethodAnnotation(EntitlementRequired.class);
        entitlementInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod);
        Assert.assertEquals(HttpStatus.OK.value(), mockedResponse.getStatus());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void preHandle_No_Token_No_SessionID_BAD_REQUEST_Failure() throws Exception {

        doReturn(false).when(cDXApiClientConfig).isEntitlementpassthrough();
        doReturn(entitlementRequired).when(handlerMethod).getMethodAnnotation(EntitlementRequired.class);
        entitlementInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod);
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), mockedResponse.getStatus());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void preHandle_Lender_Role_Token_Trader_Activity_UnAuthorized_Failure() throws Exception {

        doReturn(false).when(cDXApiClientConfig).isEntitlementpassthrough();
        doReturn(authorizedRoles).when(entitlementRequired).roles();
        doReturn(lenderRoles).when(cDXClientApi).getRoles(Mockito.any());

        mockedRequest.addHeader(CDXHeaderMap.JWS_TOKEN.getValue(), "token");
        doReturn(entitlementRequired).when(handlerMethod).getMethodAnnotation(EntitlementRequired.class);
        entitlementInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod);
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), mockedResponse.getStatus());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void preHandle_Lender_Role_Token_Trader_Activity_Failure_Message_retrieval() throws Exception {

        doReturn(false).when(cDXApiClientConfig).isEntitlementpassthrough();
        doReturn(authorizedRoles).when(entitlementRequired).roles();
        doReturn(lenderRoles).when(cDXClientApi).getRoles(Mockito.any());
        Mockito.when(exceptionLookupService.getExceptionLookupData(Mockito.any()))
                .thenThrow(new MBSBaseException("test"));
        mockedRequest.addHeader(CDXHeaderMap.JWS_TOKEN.getValue(), "token");
        doReturn(entitlementRequired).when(handlerMethod).getMethodAnnotation(EntitlementRequired.class);
        entitlementInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), mockedResponse.getStatus());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void preHandle_Lender_Role_Token_Trader_Activity_Failure_Empty_Error_message() throws Exception {

        doReturn(false).when(cDXApiClientConfig).isEntitlementpassthrough();
        doReturn(authorizedRoles).when(entitlementRequired).roles();
        doReturn(lenderRoles).when(cDXClientApi).getRoles(Mockito.any());
        Mockito.when(exceptionLookupService.getExceptionLookupData(Mockito.any()))
                .thenReturn(new ArrayList<ExceptionLookupPO>());
        mockedRequest.addHeader(CDXHeaderMap.JWS_TOKEN.getValue(), "token");
        doReturn(entitlementRequired).when(handlerMethod).getMethodAnnotation(EntitlementRequired.class);
        entitlementInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), mockedResponse.getStatus());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void preHandle_Admin_Role_Token_Trader_Activity_Success() throws Exception {

        doReturn(false).when(cDXApiClientConfig).isEntitlementpassthrough();
        doReturn(authorizedRoles).when(entitlementRequired).roles();
        doReturn(adminRoles).when(cDXClientApi).getRoles(Mockito.any());

        mockedRequest.addHeader(CDXHeaderMap.JWS_TOKEN.getValue(), "token");
        doReturn(entitlementRequired).when(handlerMethod).getMethodAnnotation(EntitlementRequired.class);
        entitlementInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod);
        Assert.assertEquals(HttpStatus.OK.value(), mockedResponse.getStatus());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void preHandle_Admin_Role_V2_Token_Trader_Activity_Success() throws Exception {

        doReturn(false).when(cDXApiClientConfig).isEntitlementpassthrough();
        doReturn(authorizedRolesV2).when(entitlementRequired).roles();
        doReturn(adminRolesV2).when(cDXClientApi).getRoles(Mockito.any());

        mockedRequest.addHeader(CDXHeaderMap.JWS_TOKEN.getValue(), "token");
        doReturn(entitlementRequired).when(handlerMethod).getMethodAnnotation(EntitlementRequired.class);
        entitlementInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod);
        Assert.assertEquals(HttpStatus.OK.value(), mockedResponse.getStatus());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void preHandle_No_Role_Token_Trader_Activity_Failure_Message_Retrieval() throws Exception {

        doReturn(false).when(cDXApiClientConfig).isEntitlementpassthrough();
        doReturn(authorizedRoles).when(entitlementRequired).roles();
        doReturn(null).when(cDXClientApi).getRoles(Mockito.any());

        doThrow(new MBSBaseException("test")).when(exceptionLookupService).getExceptionLookupData(any());
        mockedRequest.addHeader(CDXHeaderMap.JWS_TOKEN.getValue(), "token");
        doReturn(entitlementRequired).when(handlerMethod).getMethodAnnotation(EntitlementRequired.class);
        entitlementInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), mockedResponse.getStatus());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void preHandle_No_Role_Token_Trader_Activity_Failure_No_Message() throws Exception {

        doReturn(false).when(cDXApiClientConfig).isEntitlementpassthrough();
        doReturn(authorizedRoles).when(entitlementRequired).roles();
        doReturn(null).when(cDXClientApi).getRoles(any());
        
        List<ExceptionLookupPO> exceptionLookupPOLst = createExceptionData(MBSExceptionConstants.SYSM_0002);
        doReturn(exceptionLookupPOLst).when(exceptionLookupService).getExceptionLookupData(any());
        
        mockedRequest.addHeader(CDXHeaderMap.JWS_TOKEN.getValue(), "token");
        doReturn(entitlementRequired).when(handlerMethod).getMethodAnnotation(EntitlementRequired.class);
        entitlementInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), mockedResponse.getStatus());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void preHandle_No_Role_Token_Trader_Activity_Failure() throws Exception {

        doReturn(false).when(cDXApiClientConfig).isEntitlementpassthrough();
        doReturn(authorizedRoles).when(entitlementRequired).roles();
        doReturn(null).when(cDXClientApi).getRoles(Mockito.any());

        mockedRequest.addHeader(CDXHeaderMap.JWS_TOKEN.getValue(), "token");
        doReturn(entitlementRequired).when(handlerMethod).getMethodAnnotation(EntitlementRequired.class);
        entitlementInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod);
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), mockedResponse.getStatus());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void preHandle_Trade_Role_Session_Trader_Activity_Success() throws Exception {

        doReturn(false).when(cDXApiClientConfig).isEntitlementpassthrough();
        doReturn(authorizedRoles).when(entitlementRequired).roles();
        mockedRequest.addHeader(CDXHeaderMap.SESSION_ID.getValue(), "sessionid");

        doReturn(true).when(entitlementInterceptorService).performAuthorizationUsingSessionID(anyString(), anyList(),
                anyObject(), anyObject());

        doReturn(entitlementRequired).when(handlerMethod).getMethodAnnotation(EntitlementRequired.class);
        entitlementInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod);
        Assert.assertEquals(HttpStatus.OK.value(), mockedResponse.getStatus());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void preHandle_Trade_Role_Session_Trader_Activity_Failure() throws Exception {

        doReturn(false).when(cDXApiClientConfig).isEntitlementpassthrough();
        doReturn(authorizedRoles).when(entitlementRequired).roles();
        mockedRequest.addHeader(CDXHeaderMap.SESSION_ID.getValue(), "sessionid");

        doReturn(false).when(entitlementInterceptorService).performAuthorizationUsingSessionID(anyString(), anyList(),
                anyObject(), anyObject());

        mockedResponse.setStatus(HttpStatus.UNAUTHORIZED.value());

        doReturn(entitlementRequired).when(handlerMethod).getMethodAnnotation(EntitlementRequired.class);
        entitlementInterceptor.preHandle(mockedRequest, mockedResponse, handlerMethod);
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), mockedResponse.getStatus());
    }

}
