/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

/**
 * @author gaur5c
 */

package com.fanniemae.mbsportal.api.cdx;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementRolePO;
import com.fanniemae.mbsportal.api.po.ProfileSessionPO;
import com.fanniemae.mbsportal.api.service.ProfileEntitlementService;
import com.fanniemae.mbsportal.api.service.ProfileSessionService;
import com.fanniemae.mbsportal.cdx.config.CDXApiClientConfig;
import com.fanniemae.mbsportal.cdx.constants.CDXApiProfileEnum;
import com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap;
import com.fanniemae.mbsportal.cdx.framework.jwt.JWT;
import com.fanniemae.mbsportal.cdx.framework.jwt.RSAUtils;
import com.fanniemae.mbsportal.cdx.framework.jwt.algorithms.Algorithm;
import com.fanniemae.mbsportal.cdx.framework.jwt.interfaces.DecodedJWT;
import com.fanniemae.mbsportal.cdx.template.GatewayProxyTemplate;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSRetryableException;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

/**
 * @author Rajiv Chaudhuri
 * @Date: Mar 13, 2018
 * @File: com.fanniemae.mbsportal.api.cdx.CDXClientApi.java
 * @Revision :
 * @Description: CMMBSSTA01-1212 : (Tech) Trader user login calls MBSP API with
 *               valid Session ID
 */
@Component
@EnableRetry
public class CDXClientApi {

    /**
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CDXClientApi.class);

    /**
     * cDXApiClientConfig API client config obj
     */
    @Autowired
    CDXApiClientConfig cDXApiClientConfig;

    /**
     * mbspProperties config obj
     */
    @Autowired
    MbspProperties mbspProperties;

    /**
     * profileEntitlementService ProfileEntitlementService
     */
    @Autowired
    ProfileEntitlementService profileEntitlementService;
    /**
     * rsaPublicKeys RSAPublicKey[]
     */
    RSAPublicKey[] rsaPublicKeys;
    /**
     * profileSessionService ProfileSessionService
     */
    @Autowired
    ProfileSessionService profileSessionService;
    /**
     * gatewayProxyTemplate GatewayProxyTemplate
     */
    @Autowired
    @Qualifier("gatewayProxyTemplate")
    private GatewayProxyTemplate gatewayProxyTemplate;

    /**
     * @param headerMap
     *            - Header Map from Request
     * @return ProfileEntitlementPO from CDX
     * @throws MBSBaseException
     * @Description: CMMBSSTA01-1212 : (Tech) Trader user login calls MBSP API
     *               with valid Session ID
     */
    public ProfileEntitlementPO getProfileFromSessionID(Map<String, String> headerMap)
            throws MBSRetryableException, MBSBaseException {

        LOGGER.debug("Entering getProfileFromSessionID method in CDXClientApi");
        ProfileEntitlementPO profileEntitlementPO = null;
        ResponseEntity<String> response = null;
        String channel = headerMap.get(CDXHeaderMap.CHANNEL.getValue());
        String subChannel = headerMap.get(CDXHeaderMap.SUB_CHANNEL.getValue());
        String sessionId = headerMap.get(CDXHeaderMap.SESSION_ID.getValue());
        String paddedId = MBSPortalUtils.getLeftPaddedString(sessionId);
        try {

            String url = cDXApiClientConfig.getBaseurl() + cDXApiClientConfig.getProfileapi();
            HttpHeaders headers = new HttpHeaders();
            headers.add(CDXHeaderMap.CHANNEL.getValue(), channel);
            headers.add(CDXHeaderMap.SUB_CHANNEL.getValue(), headerMap.get(CDXHeaderMap.SUB_CHANNEL.getValue()));
            headers.add(CDXHeaderMap.SESSION_ID.getValue(), sessionId);

            LOGGER.info("url : {}, CHANNEL : {} , SUB_CHANNEL: {} , Padded Id: {}", url, channel, subChannel, paddedId);

            HttpEntity<String> request = new HttpEntity<>(headers);
            response = gatewayProxyTemplate.exchange(url, HttpMethod.POST, request, String.class);

            LOGGER.debug("CDXClientAPI Response Code: {}, Response Body: {}", response.getStatusCode().value(), response.getBody());

            if (HttpStatus.CREATED.value() == response.getStatusCode().value()
                    || HttpStatus.OK.value() == response.getStatusCode().value()) {

                ObjectMapper mapper = new ObjectMapper();
                profileEntitlementPO = mapper.readValue(response.getBody(), ProfileEntitlementPO.class);
            } else {
                LOGGER.error("InValid Response recieved from getProfileFromSessionID Call, Status code: {}, Padded ID: {}", response.getStatusCode().value(), paddedId);
                throw new MBSBaseException(response.getBody(), response.getStatusCode().value());
            }

            /*
             * Need to check whether it is required or not
             */
            if (Objects.equals(profileEntitlementPO, null)) {
                LOGGER.error("Failed to convert Profile PO from response, Status code: {}, Padded ID: {}", response.getStatusCode().value(), paddedId);
                throw new MBSSystemException(StringUtils.EMPTY);
            }
            LOGGER.debug("Exiting getProfileFromSessionID method in CDXClientApi");
            return profileEntitlementPO;

        } catch (HttpClientErrorException exe) {
            LOGGER.error("Exception recieved from getProfileFromSessionID Call, Exception Status Code: {}, Padded ID: {}", exe.getStatusCode().value(), paddedId);

            if (HttpStatus.TOO_MANY_REQUESTS.value() == exe.getStatusCode().value()) {
                throw new MBSRetryableException("Too many request raised hence going for retry");

            } else {
                throw new MBSSystemException("Failed to fetch profile for Exception Status Code:" + exe.getStatusCode().value() +  "Padded ID: " + paddedId);
            }
        } catch (Exception exe) {
            LOGGER.error("Failed to fetch profile for Padded ID: {}, Exception: {}", paddedId, exe);
            if (response != null && response.getBody() != null) {
                LOGGER.error("Response Code: {}, Response Body: {}", response.getStatusCodeValue(), response.getBody());
            }
            // TODO: no need to throw here I guess
            throw new MBSSystemException("Failed to fetch profile for padded ID:" + paddedId);
        }
    }

    /**
     * @param headerMap
     *            Map for header values
     * @return ProfileEntitlementPO
     * @throws MBSBaseException
     * @author Rajiv This method wraps the call to get Profile for local and
     *         higher environments CMMBSSTA01-1212 : (Tech) Trader user login
     *         calls MBSP API with valid Session ID
     */
    public ProfileEntitlementPO getProfileEntitlementPO(Map<String, String> headerMap, MBSRoleType mbsRoleType)
            throws MBSBaseException {

        LOGGER.debug("Entering getProfileEntitlementPO method in CDXClientApi");
        String signedToken = headerMap.get(CDXHeaderMap.JWS_TOKEN.getValue());
        String sessionId = headerMap.get(CDXHeaderMap.SESSION_ID.getValue());
        String paddedId = MBSPortalUtils.getLeftPaddedString(sessionId);
        LOGGER.debug("Signed Token: {}, Padded ID: {}", signedToken, paddedId);

        if (cDXApiClientConfig.isEntitlementpassthrough()
                && (StringUtils.isBlank(signedToken) && StringUtils.isBlank(sessionId))) {
            ProfileEntitlementPO profileEntitlementPO = new ProfileEntitlementPO();
            if (Objects.nonNull(headerMap.get("username"))) {
                String userName = headerMap.get("username");
                profileEntitlementPO = profileEntitlementService.getProfile(userName);
            } else {

                /*
                 * CMMBSSTA01-1107: API Integration Validation- User Has to Be
                 * Valid
                 */
                if (Objects.equals(null, mbsRoleType)) {
                    mbsRoleType = MBSRoleType.LENDER;
                }
                if (MBSRoleType.LENDER.equals(mbsRoleType)) {
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

                } else if (MBSRoleType.TRADER.equals(mbsRoleType)) {

                    profileEntitlementPO.setFirstName("TraderB");
                    profileEntitlementPO.setLastName("ExecuteB");
                    profileEntitlementPO.setEmailAddress("traderb_executeb@fanniemae.com");
                    profileEntitlementPO.setMobileNumber("(202) 752-1000");
                    profileEntitlementPO.setWorkNumber("(202) 752-1000");
                    profileEntitlementPO.setSellerServicerNumber("" + mbspProperties.getFnmSellerSerivcerNo());
                    profileEntitlementPO.setCustomerName("");
                    profileEntitlementPO.setUserName("tdexec23");
                    profileEntitlementPO.setDealerOrgName("Fannie Mae");
                    profileEntitlementPO.setDealerOrgId("24");
                    List<ProfileEntitlementRolePO> roleList = new ArrayList<>();

                    ProfileEntitlementRolePO rolePO = new ProfileEntitlementRolePO();
                    rolePO.setName(EntitlementRole.TRADER_TRADE_EXECUTE);
                    roleList.add(rolePO);

                    rolePO = new ProfileEntitlementRolePO();
                    rolePO.setName(EntitlementRole.MBSP_FM_TRADER_EXECUTE_HE);
                    roleList.add(rolePO);

                    rolePO = new ProfileEntitlementRolePO();
                    rolePO.setName(EntitlementRole.MBSP_FM_TRADER_EXECUTE_LE);
                    roleList.add(rolePO);

                    profileEntitlementPO.setRoles(roleList);
                    profileEntitlementPO.setSellerServicerDetails(null);
                    profileEntitlementPO.setInstitutionId(null);
                    profileEntitlementPO.setDefaultSellerServicerNumber("");
                    profileEntitlementPO.setLenderDetails(null);
                    profileEntitlementPO.setFannieMaeUser(true);
                    profileEntitlementPO.setPartyShortName("FNM");
                    profileEntitlementPO.setBrsUserName("w8djain");
                }
            }

            LOGGER.debug("Local profile " + profileEntitlementPO);
            return profileEntitlementPO;

        } else {
            ProfileEntitlementPO profileEntitlementPO = null;

            if (!StringUtils.isBlank(signedToken)) {
                profileEntitlementPO = getProfileFromToken(headerMap);
                // Retrieving the lender details for TSP user
                if (profileEntitlementPO.isTspUser()) {
                    profileEntitlementPO = profileEntitlementService.getProfile(profileEntitlementPO.getUserName());
                }

            } else if (!StringUtils.isBlank(sessionId)) {

                ProfileSessionPO profileSessionPO = profileSessionService.getProfileSession(sessionId);
                profileEntitlementPO = profileEntitlementService.getProfile(profileSessionPO.getUserName());
            }
            return profileEntitlementPO;
        }

    }

    /**
     * This method gets the profile from the token
     *
     * @param headerMap
     *            Map for header values
     * @return ProfileEntitlementPO
     * @throws MBSBaseException
     */
    public ProfileEntitlementPO getProfileFromToken(Map<String, String> headerMap) throws MBSBaseException {

        LOGGER.debug("Entering getProfileFromToken method CDXClientApi");

        String signedToken = headerMap.get(CDXHeaderMap.JWS_TOKEN.getValue());
        LOGGER.debug("getProfileFromToken : Signed Token: {}", signedToken);
        try {

            DecodedJWT jwt = getDecodedJWT(signedToken);

            ProfileEntitlementPO profileEntitlementPO = new ProfileEntitlementPO();
            profileEntitlementPO.setFirstName(jwt.getClaim(CDXApiProfileEnum.FIRSTNAME.getValue()).asString());
            profileEntitlementPO.setLastName(jwt.getClaim(CDXApiProfileEnum.LASTNAME.getValue()).asString());
            profileEntitlementPO.setEmailAddress(jwt.getClaim(CDXApiProfileEnum.EMAIL.getValue()).asString());
            profileEntitlementPO.setMobileNumber(jwt.getClaim(CDXApiProfileEnum.MOBILE_NUMBER.getValue()).asString());
            profileEntitlementPO.setWorkNumber(jwt.getClaim(CDXApiProfileEnum.WORK_NUMBER.getValue()).asString());
            profileEntitlementPO.setCustomerName(jwt.getClaim(CDXApiProfileEnum.CUSTOMER_NAME.getValue()).asString());
            profileEntitlementPO.setUserName(jwt.getClaim(CDXApiProfileEnum.USER_NAME.getValue()).asString());
            profileEntitlementPO.setSellerServicerNumber(
                    jwt.getClaim(CDXApiProfileEnum.SELLER_SERVICE_NUMBER.getValue()).asString());
            profileEntitlementPO
                    .setDealerOrgName(jwt.getClaim(CDXApiProfileEnum.DEALER_ORG_NAME.getValue()).asString());
            profileEntitlementPO.setDealerOrgId(jwt.getClaim(CDXApiProfileEnum.DEALER_ORG_ID.getValue()).asString());

            List<ProfileEntitlementRolePO> roleList = new ArrayList<>();
            String[] roles = jwt.getClaim(CDXApiProfileEnum.ROLES.getValue()).asArray(String.class);
            for (String role : roles) {
                ProfileEntitlementRolePO rolePO = new ProfileEntitlementRolePO();
                rolePO.setName(role);
                roleList.add(rolePO);
            }
            profileEntitlementPO.setRoles(roleList);

            profileEntitlementPO.setSellerServicerDetails(null);

            profileEntitlementPO.setInstitutionId(jwt.getClaim(CDXApiProfileEnum.INSTITUTION_ID.getValue()).asString());
            profileEntitlementPO.setDefaultSellerServicerNumber(
                    jwt.getClaim(CDXApiProfileEnum.DEFAULT_SELLER_SERVICE_NUMBER.getValue()).asString());

            profileEntitlementPO.setLenderDetails(jwt.getClaim(CDXApiProfileEnum.LENDER_DETAILS.getValue()).asString());

            profileEntitlementPO
                    .setFannieMaeUser(jwt.getClaim(CDXApiProfileEnum.FANNIEMAE_USER.getValue()).asBoolean());

            // profileEntitlementPO.setPartyShortName(null);
            profileEntitlementPO.setBrsUserName(null);

            /*
             * CMMBSSTA01-1371: (Lender page) TSP chooses / switches a lender
             */
            List<String> userRoles = profileEntitlementPO.getRoles().stream().map(ProfileEntitlementRolePO::getName)
                    .map(String::toLowerCase).collect(Collectors.toList());
            List<String> tspRole = new ArrayList<String>(
                    Arrays.asList(EntitlementRole.TSP_TRADE_EXECUTE.toLowerCase()));

            if (CollectionUtils.containsAny(userRoles, tspRole)) {
                profileEntitlementPO.setTspUser(true);
            }

            // if Trader - Assign default SSN
            if (profileEntitlementPO.isFannieMaeUser()
                    && StringUtils.isBlank(profileEntitlementPO.getSellerServicerNumber())) {
                profileEntitlementPO.setSellerServicerNumber("" + mbspProperties.getFnmSellerSerivcerNo());
            }
            // Done

            return profileEntitlementPO;

        } catch (MBSBaseException exe) {
            LOGGER.error("Error in getProfileFromToken: {}", exe);
            throw exe;

        } catch (Exception exe) {
            LOGGER.error("Error in getProfileFromToken: {}", exe);
            throw new MBSBusinessException("Error to get profile for token:" + signedToken,
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }

    }

    /**
     * This method is getting list of roles based on the signed token
     *
     * @param signedToken
     *            String having the token
     * @return List<String>
     */
    public List<String> getRoles(String signedToken) {

        LOGGER.debug("CDXClientApi: getRoles: signedToken: {}", signedToken);

        List<String> roleList = new ArrayList<>();
        String userName;
        try {
            DecodedJWT jwt = getDecodedJWT(signedToken);
            String[] roles = jwt.getClaim(CDXApiProfileEnum.ROLES.getValue()).asArray(String.class);
            userName = jwt.getClaim(CDXApiProfileEnum.USER_NAME.getValue()).asString();

            if (roles != null && roles.length > 0) {
                roleList = Arrays.asList(roles);
            }

            /*
             * Set the User ID (For Logback MDC- Variable)
             */
            MDC.put(MBSExceptionConstants.INFO_USER_ID, userName);
            
            LOGGER.debug("CDXClientApi: getRoles: username: {}, roleList: {}", userName, roleList);

        } catch (Exception exe) {
            LOGGER.error("Error in getRoles: {}", exe);
        }
        return roleList;
    }

    /**
     * This method gets the RSA keys
     *
     * @return RSAPublicKey[]
     * @throws MBSBaseException
     */
    private RSAPublicKey[] getRSAKeys() throws MBSBaseException {

        if (rsaPublicKeys == null) {

            String[] keys = cDXApiClientConfig.getCertlocation();
            rsaPublicKeys = new RSAPublicKey[keys.length];

            LOGGER.debug("Loading the publicKey started");
            for (int i = 0; i < keys.length; i++) {
                rsaPublicKeys[i] = (RSAPublicKey) RSAUtils.getPublicKeyFromFile(keys[i]);
            }
            LOGGER.debug("Loading the publicKey ended");
        }
        return rsaPublicKeys;
    }

    /**
     * This method gets the decoded JWT from signed token
     *
     * @param signedToken
     * @return DecodedJWT
     * @throws MBSBaseException
     */
    @SuppressWarnings({ "deprecation" })
    public DecodedJWT getDecodedJWT(String signedToken) throws MBSBaseException {

        if (StringUtils.isBlank(signedToken)) {
            throw new MBSBusinessException("User Profile Token is empty", MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        DecodedJWT decodedJWT = null;
        getRSAKeys();
        if (rsaPublicKeys == null) {
            throw new MBSBusinessException("Empty Certificates", MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        for (int i = 0; i < rsaPublicKeys.length; i++) {
            try {
                decodedJWT = JWT.require(Algorithm.RSA256(rsaPublicKeys[i])).build().verify(signedToken.trim());
                if (decodedJWT != null) {
                    return decodedJWT;
                }
            } catch (Exception exe) {
                LOGGER.debug("Bad User Profile Token for iteration: " + i);
            }
        }
        if (decodedJWT == null) {
            throw new MBSBusinessException("Bad User Profile Token", MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        return decodedJWT;
    }

    /**
     * This will method will check the validity of session - if session is not
     * valid then it will throw error Exception for 4xx and for any unknown
     * CDXClientException
     *
     * @param sessionId
     * @return
     */
    public boolean isSessionValid(String sessionId) {

        LOGGER.debug("Entering isSessionValid method in CDXClientApi");
        boolean retVal = false;

        try {
            String url = cDXApiClientConfig.getBaseurl() + cDXApiClientConfig.getSessionapi();
            HttpHeaders headers = prepareHeader(sessionId);
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = gatewayProxyTemplate.exchange(url, HttpMethod.POST, request,
                    String.class);
            if (HttpStatus.CREATED.value() == response.getStatusCode().value()
                    || HttpStatus.OK.value() == response.getStatusCode().value()) {

                LOGGER.debug("Response Body: {}", response.getBody());
                if ("Y".equalsIgnoreCase(response.getBody())) {
                    retVal = true;
                }
            }

        } catch (HttpClientErrorException hcex) {
            LOGGER.info("isSessionValid: body Ex= {}", hcex);
            /*
             * MBSExceptionConstants.logItForAlert(MBSExceptionConstants.
             * SYSTEM_EXCEPTION_IDENTIFIER, "",
             * "isSessionValid failed with Exception and values..  ",
             * this.getClass() .getEnclosingMethod(), hcex);
             * MBSExceptionConstants.resetLogAlert();
             */
            // let return to false due to invalid session
            // throw hcex;
        } catch (HttpServerErrorException ex) {
            // it is cdx error case. what to do?
            LOGGER.error("CDX is not available. isSessionValid: Ex= {}", ex);
            MBSExceptionConstants.logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER, "",
                    "isSessionValid failed with Exception and values..", this.getClass().getEnclosingMethod(), ex);
            MBSExceptionConstants.resetLogAlert();
            // throw ex;
        } catch (Exception ex) {
            LOGGER.error("isSessionValid: Ex= {}", ex);
            MBSExceptionConstants.logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER, "",
                    "isSessionValid failed with Exception and values..", this.getClass().getEnclosingMethod(), ex);
            MBSExceptionConstants.resetLogAlert();
            // throw ex;
        }
        LOGGER.debug("Exiting isSessionValid method in CDXClientApi");
        return retVal;
    }

    /**
     * prepareHeader
     *
     * @param sessionId
     * @return
     */
    private HttpHeaders prepareHeader(String sessionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CDXHeaderMap.CHANNEL.getValue(), CDXHeaderMap.CHANNEL_VALUE.getValue());
        headers.add(CDXHeaderMap.SUB_CHANNEL.getValue(), CDXHeaderMap.SUB_CHANNEL_VALUE.getValue());
        headers.add(CDXHeaderMap.SESSION_ID.getValue(), sessionId);
        return headers;
    }
}
