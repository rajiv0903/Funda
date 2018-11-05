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
package com.fanniemae.mbsportal.cdx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fanniemae.mbsportal.api.cdx.CDXClientApi;
import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.ProfileSessionPO;
import com.fanniemae.mbsportal.api.service.ProfileEntitlementService;
import com.fanniemae.mbsportal.api.service.ProfileSessionService;
import com.fanniemae.mbsportal.cdx.config.CDXApiClientConfig;
import com.fanniemae.mbsportal.cdx.constants.CDXApiProfileEnum;
import com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap;
import com.fanniemae.mbsportal.cdx.framework.jwt.JWT;
import com.fanniemae.mbsportal.cdx.framework.jwt.JWTCreator;
import com.fanniemae.mbsportal.cdx.framework.jwt.algorithms.Algorithm;
import com.fanniemae.mbsportal.cdx.framework.jwt.interfaces.DecodedJWT;
import com.fanniemae.mbsportal.cdx.template.GatewayProxyTemplate;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Dec 12, 2017
 * @Time 9:55:32 AM com.fanniemae.mbsportal.api.cdx CDXClientApiTest.java
 * @Description: Revision 1.1 - Added JUnit Test Cases for Throws Exception
 */
@RunWith(MockitoJUnitRunner.class)
public class CDXClientApiTest {

    @InjectMocks
    CDXClientApi cDXClientApi;

    @Mock
    CDXClientApi cDXClientApiMock;

    @Mock
    CDXApiClientConfig cDXApiClientConfig;

    @Mock
    RestTemplate restTemplate;

    @Mock
    GatewayProxyTemplate gatewayProxyTemplate;

    @Mock
    ProfileEntitlementService profileEntitlementService;

    @Mock
    ProfileSessionService profileSessionService;
    
    @Spy
    MbspProperties mbspProperties;

    DecodedJWT jwt;
    String sginedToken;

    DecodedJWT jwtTSP;
    String sginedTokenTSP;

    private String cdxCertFileName = "cdxapi.chain.crt";

    private String baseurl = "https://d2o5ku9ymibe1l.cloudfront.net/cdxapi/";
    private String profileapi = "getprofile";

    ResponseEntity<String> profileResponse;
    ResponseEntity<String> profileResponseNotOK;
    ProfileEntitlementPO profileEntitlementPO;
    String responseBody;
    ProfileSessionPO profileSessionPO;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        keyGenerator.initialize(1024);
        KeyPair kp = keyGenerator.genKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();

        createToken(publicKey, privateKey);

        createTSPToken(publicKey, privateKey);

        cDXApiClientConfig.setBaseurl("https://d2o5ku9ymibe1l.cloudfront.net/cdxapi/");
        cDXApiClientConfig.setProfileapi("getprofile");

        ObjectMapper mapper = new ObjectMapper();
        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementPO.setUserName("username");

        responseBody = mapper.writeValueAsString(profileEntitlementPO);
        profileResponse = new ResponseEntity<String>(responseBody, HttpStatus.OK);
        profileResponseNotOK = new ResponseEntity<String>(responseBody, HttpStatus.UNAUTHORIZED);

        profileSessionPO = new ProfileSessionPO();
        profileSessionPO.setUserName("username");
        profileSessionPO.setSessionId("sessionId");
        
        mbspProperties.setFnmSellerSerivcerNo(123);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetProfileFromToken() throws Exception {

        cDXClientApi = Mockito.spy(new CDXClientApi());
        

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(CDXHeaderMap.JWS_TOKEN.getValue(), CDXHeaderMap.JWS_TOKEN.getValue());
        doReturn(jwt).when(cDXClientApi).getDecodedJWT(Mockito.any());

        //doReturn(1000).when(mbspProperties).getFnmSellerSerivcerNo();
        doReturn(1000).when(mbspProperties).getFnmSellerSerivcerNo();
        ProfileEntitlementPO profileEntitlementPORet = cDXClientApi.getProfileFromToken(headerMap);

        assertEquals(jwt.getClaim(CDXApiProfileEnum.USER_NAME.getValue()).asString(),
                profileEntitlementPORet.getUserName());

        assertFalse(profileEntitlementPORet.isTspUser());
    }

    /**
     * 
     * CMMBSSTA01-1371: (Lender page) TSP chooses / switches a lender *
     * 
     * @throws Exception
     */
    @Test
    public void test_GetProfileEntitlementPO_From_Token_TSP_Success() throws Exception {

        cDXClientApi = Mockito.spy(new CDXClientApi());

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(CDXHeaderMap.JWS_TOKEN.getValue(), CDXHeaderMap.JWS_TOKEN.getValue());
        doReturn(jwtTSP).when(cDXClientApi).getDecodedJWT(Mockito.any());

        doReturn(1000).when(mbspProperties).getFnmSellerSerivcerNo();
        ProfileEntitlementPO profileEntitlementPORet = cDXClientApi.getProfileFromToken(headerMap);

        assertEquals(jwtTSP.getClaim(CDXApiProfileEnum.USER_NAME.getValue()).asString(),
                profileEntitlementPORet.getUserName());
        assertTrue(profileEntitlementPORet.isTspUser());
    }

    /**
     * 
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @Test(expected = MBSBaseException.class)
    public void testGetProfileFromToken_Throw_Exception() throws Exception {

        cDXClientApi = Mockito.spy(new CDXClientApi());

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(CDXHeaderMap.JWS_TOKEN.getValue(), CDXHeaderMap.JWS_TOKEN.getValue());
        doThrow(MBSBaseException.class).when(cDXClientApi).getDecodedJWT(Mockito.any());

        ProfileEntitlementPO profileEntitlementPORet = cDXClientApi.getProfileFromToken(headerMap);
    }

    /**
     * 
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @Test(expected = MBSBaseException.class)
    public void testGetProfileFromToken_Throw_Runtime_Exception() throws Exception {

        cDXClientApi = Mockito.spy(new CDXClientApi());

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(CDXHeaderMap.JWS_TOKEN.getValue(), CDXHeaderMap.JWS_TOKEN.getValue());
        doThrow(RuntimeException.class).when(cDXClientApi).getDecodedJWT(Mockito.any());

        ProfileEntitlementPO profileEntitlementPORet = cDXClientApi.getProfileFromToken(headerMap);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetRoles() throws Exception {

        cDXClientApi = Mockito.spy(new CDXClientApi());

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(CDXHeaderMap.JWS_TOKEN.getValue(), CDXHeaderMap.JWS_TOKEN.getValue());
        doReturn(jwt).when(cDXClientApi).getDecodedJWT(Mockito.any());

        List<String> roles = cDXClientApi.getRoles(sginedToken);

        assertTrue(CollectionUtils.containsAny(
                Arrays.asList(jwt.getClaim(CDXApiProfileEnum.ROLES.getValue()).asArray(String.class)), roles));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetRoles_Throws_Exception() throws Exception {

        cDXClientApi = Mockito.spy(new CDXClientApi());

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(CDXHeaderMap.JWS_TOKEN.getValue(), CDXHeaderMap.JWS_TOKEN.getValue());
        doThrow(RuntimeException.class).when(cDXClientApi).getDecodedJWT(Mockito.any());

        List<String> roles = cDXClientApi.getRoles(sginedToken);
        assertTrue(roles.size() == 0);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getProfileEntitlementPO_Default_Trader() throws Exception {

        cDXApiClientConfig.setEntitlementpassthrough(true);
        Map<String, String> headerMap = new HashMap<String, String>();
        doReturn(true).when(cDXApiClientConfig).isEntitlementpassthrough();
        doReturn(1000).when(mbspProperties).getFnmSellerSerivcerNo();
        ProfileEntitlementPO profileEntitlementPORet = cDXClientApi.getProfileEntitlementPO(headerMap,
                MBSRoleType.TRADER);
        assertNotNull(profileEntitlementPORet);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getProfileEntitlementPO_Default_Trader_V2() throws Exception {

        cDXApiClientConfig.setEntitlementpassthrough(true);
        Map<String, String> headerMap = new HashMap<String, String>();
        doReturn(true).when(cDXApiClientConfig).isEntitlementpassthrough();
        doReturn(1000).when(mbspProperties).getFnmSellerSerivcerNo();
        ProfileEntitlementPO profileEntitlementPORet = cDXClientApi.getProfileEntitlementPO(headerMap,
                MBSRoleType.TRADER);
        assertNotNull(profileEntitlementPORet);
        assertNotNull(profileEntitlementPORet.getRoles().contains(EntitlementRole.MBSP_FM_TRADER_EXECUTE_HE));
        assertNotNull(profileEntitlementPORet.getRoles().contains(EntitlementRole.MBSP_FM_TRADER_EXECUTE_LE));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getProfileEntitlementPO_Default_Lender() throws Exception {

        cDXApiClientConfig.setEntitlementpassthrough(true);
        Map<String, String> headerMap = new HashMap<String, String>();
        doReturn(true).when(cDXApiClientConfig).isEntitlementpassthrough();
        ProfileEntitlementPO profileEntitlementPORet = cDXClientApi.getProfileEntitlementPO(headerMap,
                MBSRoleType.LENDER);
        assertNotNull(profileEntitlementPORet);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getProfileEntitlementPO_Default() throws Exception {

        cDXApiClientConfig.setEntitlementpassthrough(true);
        Map<String, String> headerMap = new HashMap<String, String>();
        doReturn(true).when(cDXApiClientConfig).isEntitlementpassthrough();
        ProfileEntitlementPO profileEntitlementPORet = cDXClientApi.getProfileEntitlementPO(headerMap, null);
        assertNotNull(profileEntitlementPORet);
    }
    
    /**
     *
     * @throws Exception
     */
    @Test
    public void getProfileEntitlementPO_NonNullUserName() throws Exception {
        
        cDXApiClientConfig.setEntitlementpassthrough(true);
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("username", "api");
        doReturn(true).when(cDXApiClientConfig).isEntitlementpassthrough();
        ProfileEntitlementPO profileEntitlementPORet = cDXClientApi.getProfileEntitlementPO(headerMap, null);
    }

    /**
     * 
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @Test(expected = MBSBusinessException.class)
    public void getDecodedJWT_Bad_Token() throws Exception {
        doReturn(this.getClass().getClassLoader().getResource(cdxCertFileName).getPath().split(""))
                .when(cDXApiClientConfig).getCertlocation();
        DecodedJWT jwt = cDXClientApi.getDecodedJWT(sginedToken);
    }

    /**
     * 
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @Test(expected = MBSBusinessException.class)
    public void getDecodedJWT_Empty_Token() throws Exception {
        doReturn(this.getClass().getClassLoader().getResource(cdxCertFileName).getPath().split(""))
                .when(cDXApiClientConfig).getCertlocation();
        DecodedJWT jwt = cDXClientApi.getDecodedJWT(null);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    @Ignore
    public void getDecodedJWT_Success() throws Exception {

        doReturn(this.getClass().getClassLoader().getResource(cdxCertFileName).getPath().split(""))
                .when(cDXApiClientConfig).getCertlocation();
        DecodedJWT jwt = cDXClientApi.getDecodedJWT(sginedToken);
        assertEquals(jwt.getClaim(CDXApiProfileEnum.USER_NAME.getValue()).asString(), "username");
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getProfileEntitlementPO_From_Session_Success() throws Exception {

        doReturn(true).when(cDXApiClientConfig).isEntitlementpassthrough();
        doReturn(profileSessionPO).when(profileSessionService).getProfileSession(anyString());
        doReturn(profileEntitlementPO).when(profileEntitlementService).getProfile(anyString());

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(CDXHeaderMap.SESSION_ID.getValue(), CDXHeaderMap.SESSION_ID.getValue());

        ProfileEntitlementPO profileEntitlementPORet = cDXClientApi.getProfileEntitlementPO(headerMap, null);
        assertEquals(profileEntitlementPO.getUserName(), profileEntitlementPORet.getUserName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSBusinessException.class)
    public void getProfileEntitlementPO_From_Token_Failure() throws Exception {

        doReturn(true).when(cDXApiClientConfig).isEntitlementpassthrough();

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(CDXHeaderMap.JWS_TOKEN.getValue(), CDXHeaderMap.JWS_TOKEN.getValue());

        ProfileEntitlementPO profileEntitlementPORet = cDXClientApi.getProfileEntitlementPO(headerMap, null);
        assertEquals(profileEntitlementPO.getUserName(), profileEntitlementPORet.getUserName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getProfileFromSessionID_Success() throws Exception {

        doReturn(baseurl).when(cDXApiClientConfig).getBaseurl();
        doReturn(profileapi).when(cDXApiClientConfig).getProfileapi();

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(CDXHeaderMap.CHANNEL.getValue(), CDXHeaderMap.CHANNEL.getValue());
        headerMap.put(CDXHeaderMap.SUB_CHANNEL.getValue(), CDXHeaderMap.SUB_CHANNEL.getValue());
        headerMap.put(CDXHeaderMap.SESSION_ID.getValue(), CDXHeaderMap.SESSION_ID.getValue());

        doReturn(profileResponse).when(gatewayProxyTemplate).exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any());

        ProfileEntitlementPO profileEntitlementPORet = cDXClientApi.getProfileFromSessionID(headerMap);
        assertEquals(profileEntitlementPO.getUserName(), profileEntitlementPORet.getUserName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSSystemException.class)
    public void getProfileFromSessionID_CDX_Down_Failure() throws Exception {

        doReturn(baseurl).when(cDXApiClientConfig).getBaseurl();
        doReturn(profileapi).when(cDXApiClientConfig).getProfileapi();

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(CDXHeaderMap.CHANNEL.getValue(), CDXHeaderMap.CHANNEL.getValue());
        headerMap.put(CDXHeaderMap.SUB_CHANNEL.getValue(), CDXHeaderMap.SUB_CHANNEL.getValue());
        headerMap.put(CDXHeaderMap.SESSION_ID.getValue(), CDXHeaderMap.SESSION_ID.getValue());

        doThrow(MBSSystemException.class).when(gatewayProxyTemplate).exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any());

        ProfileEntitlementPO profileEntitlementPORet = cDXClientApi.getProfileFromSessionID(headerMap);
        assertEquals(profileEntitlementPO.getUserName(), profileEntitlementPORet.getUserName());
    }

    @Test(expected = MBSSystemException.class)
    public void getProfileFromSessionID_CDX_Down_Failure_Exception() throws Exception {

        doReturn(baseurl).when(cDXApiClientConfig).getBaseurl();
        doReturn(profileapi).when(cDXApiClientConfig).getProfileapi();

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(CDXHeaderMap.CHANNEL.getValue(), CDXHeaderMap.CHANNEL.getValue());
        headerMap.put(CDXHeaderMap.SUB_CHANNEL.getValue(), CDXHeaderMap.SUB_CHANNEL.getValue());
        headerMap.put(CDXHeaderMap.SESSION_ID.getValue(), CDXHeaderMap.SESSION_ID.getValue());

        doThrow(Exception.class).when(gatewayProxyTemplate).exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any());

        ProfileEntitlementPO profileEntitlementPORet = cDXClientApi.getProfileFromSessionID(headerMap);
        assertEquals(profileEntitlementPO.getUserName(), profileEntitlementPORet.getUserName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSSystemException.class)
    public void getProfileFromSessionID_Profile_Null_Failure() throws Exception {

        doReturn(baseurl).when(cDXApiClientConfig).getBaseurl();
        doReturn(profileapi).when(cDXApiClientConfig).getProfileapi();

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(CDXHeaderMap.CHANNEL.getValue(), CDXHeaderMap.CHANNEL.getValue());
        headerMap.put(CDXHeaderMap.SUB_CHANNEL.getValue(), CDXHeaderMap.SUB_CHANNEL.getValue());
        headerMap.put(CDXHeaderMap.SESSION_ID.getValue(), CDXHeaderMap.SESSION_ID.getValue());

        doReturn(null).when(gatewayProxyTemplate).exchange(Matchers.anyString(), Matchers.any(HttpMethod.class),
                Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any());

        ProfileEntitlementPO profileEntitlementPORet = cDXClientApi.getProfileFromSessionID(headerMap);
        assertEquals(profileEntitlementPO.getUserName(), profileEntitlementPORet.getUserName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test(expected = MBSBaseException.class)
    public void getProfileFromSessionID_Other_Than_OK_Status_Failure() throws Exception {

        doReturn(baseurl).when(cDXApiClientConfig).getBaseurl();
        doReturn(profileapi).when(cDXApiClientConfig).getProfileapi();

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(CDXHeaderMap.CHANNEL.getValue(), CDXHeaderMap.CHANNEL.getValue());
        headerMap.put(CDXHeaderMap.SUB_CHANNEL.getValue(), CDXHeaderMap.SUB_CHANNEL.getValue());
        headerMap.put(CDXHeaderMap.SESSION_ID.getValue(), CDXHeaderMap.SESSION_ID.getValue());

        doReturn(profileResponseNotOK).when(gatewayProxyTemplate).exchange(Matchers.anyString(),
                Matchers.any(HttpMethod.class), Matchers.<HttpEntity<?>> any(), Matchers.<Class<String>> any());

        ProfileEntitlementPO profileEntitlementPORet = cDXClientApi.getProfileFromSessionID(headerMap);
        assertEquals(profileEntitlementPO.getUserName(), profileEntitlementPORet.getUserName());
    }

    /**
     * 
     * @param publicKey
     * @param privateKey
     */
    @SuppressWarnings("deprecation")
    private void createToken(final RSAPublicKey publicKey, final RSAPrivateKey privateKey) {
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim(CDXApiProfileEnum.FIRSTNAME.getValue(), "FirstName");
        builder.withClaim(CDXApiProfileEnum.LASTNAME.getValue(), "LastName");
        builder.withClaim(CDXApiProfileEnum.USER_NAME.getValue(), "username");
        builder.withArrayClaim(CDXApiProfileEnum.ROLES.getValue(),
                new String[] { EntitlementRole.LENDER_TRADE_EXECUTE, EntitlementRole.MBSP_FM_ADMIN_HE });
        builder.withClaim(CDXApiProfileEnum.FANNIEMAE_USER.getValue(), true);
        builder.withClaim(CDXApiProfileEnum.SELLER_SERVICE_NUMBER.getValue(), "1234");
        builder.withClaim(CDXApiProfileEnum.FANNIEMAE_USER.getValue(), true);

        sginedToken = builder.sign(Algorithm.RSA256(publicKey, privateKey));
        jwt = JWT.require(Algorithm.RSA256(publicKey)).build().verify(sginedToken);
    }

    /**
     * 
     * @param publicKey
     * @param privateKey
     */
    @SuppressWarnings("deprecation")
    private void createTSPToken(final RSAPublicKey publicKey, final RSAPrivateKey privateKey) {
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim(CDXApiProfileEnum.FIRSTNAME.getValue(), "FirstName");
        builder.withClaim(CDXApiProfileEnum.LASTNAME.getValue(), "LastName");
        builder.withClaim(CDXApiProfileEnum.USER_NAME.getValue(), "username");
        builder.withArrayClaim(CDXApiProfileEnum.ROLES.getValue(), new String[] { EntitlementRole.TSP_TRADE_EXECUTE });
        builder.withClaim(CDXApiProfileEnum.SELLER_SERVICE_NUMBER.getValue(), "1234");
        builder.withClaim(CDXApiProfileEnum.FANNIEMAE_USER.getValue(), false);

        sginedTokenTSP = builder.sign(Algorithm.RSA256(publicKey, privateKey));
        jwtTSP = JWT.require(Algorithm.RSA256(publicKey)).build().verify(sginedTokenTSP);
    }

}
