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
package com.fanniemae.mbsportal.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.po.UserConfigPO;
import com.fanniemae.mbsportal.api.utils.ServiceUrlUtil;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.dao.MBSPartyDao;
import com.fanniemae.mbsportal.dao.MBSProfileDao;
import com.fanniemae.mbsportal.model.MBSParty;
import com.fanniemae.mbsportal.model.MBSProfile;
import com.fanniemae.mbsportal.model.UserConfig;
import com.fanniemae.mbsportal.streaming.prop.StreamingClientProperties;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 25, 2018
 * @File: com.fanniemae.mbsportal.utils.ServiceUrlUtilTest.java 
 * @Revision: 
 * @Description: ServiceUrlUtilTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class ServiceUrlUtilTest extends BaseServiceTest{
    
    @Mock
    private MbspProperties mbspProperties;
    
    @Mock
    private StreamingClientProperties streamingClientProperties;

    @Mock
    private MBSProfileDao mBSProfileDao;

    @Mock
    private MBSPartyDao mbsPartyDao;

    @InjectMocks
    ServiceUrlUtil serviceUrlUtil;

    private String userName = "userName";
    private MBSParty mBSParty;
    private MBSProfile mBSProfile;
    
    private UserConfigPO userConfigPO;
    private String webSocketUrl;
    private String directApiUrl;
    private UserConfig userConfig;
    

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        webSocketUrl = "https://localhost:8443/mbsp-streaming";
        directApiUrl = "https://localhost:8080";
        
        userConfigPO = new UserConfigPO();
        userConfigPO.setWebSocketUrl(webSocketUrl);
        userConfigPO.setDirectApiUrl(directApiUrl);
        
        userConfig = new UserConfig();
        userConfig.setWebSocketUrl(webSocketUrl);
        userConfig.setDirectApiUrl(directApiUrl);

        mBSProfile = new MBSProfile();
        mBSProfile.setUserConfig(userConfig);
        mBSProfile.setSellerServicerNumber("1234");

        mBSParty = new MBSParty();
        mBSParty.setMbspPortalCnameUrlBase(directApiUrl+"/party");
        mBSParty.setMbspStreamingUrlBase(webSocketUrl+"/party");
        mBSParty.setSellerServicerNumber("1234");

    }

   
    @Test
    public void getUserConfigPO_Default_Success() throws Exception {

        mBSProfile.getUserConfig().setWebSocketUrl(null);
        mBSProfile.getUserConfig().setDirectApiUrl(null);
        mBSParty.setMbspStreamingUrlBase(null);
        mBSParty.setMbspPortalCnameUrlBase(null);
        
        doReturn(directApiUrl).when(mbspProperties).getDirectApiUrl();
        doReturn(webSocketUrl).when(streamingClientProperties).getWebSocketUrl();
        doReturn(mBSProfile).when(mBSProfileDao).getProfile(anyString());
        doReturn(mBSParty).when(mbsPartyDao).getParty(anyString());        
        UserConfigPO userConfigPO = serviceUrlUtil.getUserConfigPO(userName, false);
        assertEquals(directApiUrl, userConfigPO.getDirectApiUrl());
        assertEquals(webSocketUrl, userConfigPO.getWebSocketUrl());
    }
    
    @Test
    public void getUserConfigPO_Default_For_No_Profile_Success() throws Exception {
        
        doReturn(directApiUrl).when(mbspProperties).getDirectApiUrl();
        doReturn(webSocketUrl).when(streamingClientProperties).getWebSocketUrl();
        doReturn(null).when(mBSProfileDao).getProfile(anyString());
        doReturn(null).when(mbsPartyDao).getParty(anyString());        
        UserConfigPO userConfigPO = serviceUrlUtil.getUserConfigPO(userName, false);
        assertEquals(directApiUrl, userConfigPO.getDirectApiUrl());
        assertEquals(webSocketUrl, userConfigPO.getWebSocketUrl());
    }
    
    @Test
    public void getUserConfigPO_Default_For_Null_UserName_But_Party_For_Internal_Success() throws Exception {
        
        doReturn(null).when(mBSProfileDao).getProfile(anyString());
        doReturn(mBSParty).when(mbsPartyDao).getParty(anyString());        
        UserConfigPO userConfigPO = serviceUrlUtil.getUserConfigPO(null, true);
        assertEquals(mBSParty.getMbspPortalCnameUrlBase(), userConfigPO.getDirectApiUrl());
        //assertEquals(mBSParty.getMbspStreamingUrlBase(), userConfigPO.getWebSocketUrl());
    }
    
    @Test
    public void getUserConfigPO_Default_No_Existing_Config_Success() throws Exception {

        mBSProfile.setUserConfig(null);
        mBSParty.setMbspStreamingUrlBase(null);
        mBSParty.setMbspPortalCnameUrlBase(null);
        
        doReturn(directApiUrl).when(mbspProperties).getDirectApiUrl();
        doReturn(webSocketUrl).when(streamingClientProperties).getWebSocketUrl();
        doReturn(mBSProfile).when(mBSProfileDao).getProfile(anyString());
        doReturn(mBSParty).when(mbsPartyDao).getParty(anyString());        
        UserConfigPO userConfigPO = serviceUrlUtil.getUserConfigPO(userName, false);
        assertEquals(directApiUrl, userConfigPO.getDirectApiUrl());
        assertEquals(webSocketUrl, userConfigPO.getWebSocketUrl());
    }
    
    @Test
    public void getUserConfigPO_Default_No_SellerServiceNumber_Success() throws Exception {

        mBSProfile.setSellerServicerNumber(null);
        mBSParty.setMbspStreamingUrlBase(null);
        mBSParty.setMbspPortalCnameUrlBase(null);
        
        doReturn(directApiUrl).when(mbspProperties).getDirectApiUrl();
        doReturn(webSocketUrl).when(streamingClientProperties).getWebSocketUrl();
        doReturn(mBSProfile).when(mBSProfileDao).getProfile(anyString());
        doReturn(mBSParty).when(mbsPartyDao).getParty(anyString());        
        UserConfigPO userConfigPO = serviceUrlUtil.getUserConfigPO(userName, false);
        assertEquals(directApiUrl, userConfigPO.getDirectApiUrl());
        assertEquals(webSocketUrl, userConfigPO.getWebSocketUrl());
    }

    @Test
    public void getUserConfigPO_Profile_Success() throws Exception {

        doReturn(directApiUrl).when(mbspProperties).getDirectApiUrl();
        doReturn(webSocketUrl).when(streamingClientProperties).getWebSocketUrl();
        doReturn(mBSProfile).when(mBSProfileDao).getProfile(anyString());
        doReturn(mBSParty).when(mbsPartyDao).getParty(anyString());
        UserConfigPO userConfigPO = serviceUrlUtil.getUserConfigPO(userName, false);
        assertEquals(directApiUrl, userConfigPO.getDirectApiUrl());
        assertEquals(webSocketUrl, userConfigPO.getWebSocketUrl());
    }

    @Test
    public void getServerUrl_Party_Success() throws Exception {

        mBSProfile.getUserConfig().setWebSocketUrl(null);
        mBSProfile.getUserConfig().setDirectApiUrl(null);
        
        doReturn(directApiUrl).when(mbspProperties).getDirectApiUrl();
        doReturn(webSocketUrl).when(streamingClientProperties).getWebSocketUrl();
        
        doReturn(mBSProfile).when(mBSProfileDao).getProfile(anyString());
        doReturn(mBSParty).when(mbsPartyDao).getParty(anyString());
        UserConfigPO userConfigPO = serviceUrlUtil.getUserConfigPO(userName, false);
        assertEquals(mBSParty.getMbspPortalCnameUrlBase(), userConfigPO.getDirectApiUrl());
        //assertEquals(mBSParty.getMbspStreamingUrlBase(), userConfigPO.getWebSocketUrl());
        
    }

    @Test(expected=MBSBaseException.class)
    public void getServerUrl_Profile_Failure() throws Exception {

        doReturn(directApiUrl).when(mbspProperties).getDirectApiUrl();
        doReturn(webSocketUrl).when(streamingClientProperties).getWebSocketUrl();
        
        doThrow(MBSBaseException.class).when(mBSProfileDao).getProfile(anyString());
        doReturn(mBSParty).when(mbsPartyDao).getParty(anyString());
        
        serviceUrlUtil.getUserConfigPO(userName, false);
    }

    @Test(expected=MBSBaseException.class)
    public void getServerUrl_Party_Failure() throws Exception {
        
        doReturn(directApiUrl).when(mbspProperties).getDirectApiUrl();
        doReturn(webSocketUrl).when(streamingClientProperties).getWebSocketUrl();
        
        doReturn(mBSProfile).when(mBSProfileDao).getProfile(anyString());
        doThrow(MBSBaseException.class).when(mbsPartyDao).getParty(anyString());
        
        serviceUrlUtil.getUserConfigPO(userName, false);
    }

}
