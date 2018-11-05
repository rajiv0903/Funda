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

import com.fanniemae.mbsportal.api.config.AppVault;
import com.fanniemae.mbsportal.api.config.TradeServiceProperties;
import com.fanniemae.mbsportal.api.po.TradeServiceRequestPO;
import com.fanniemae.mbsportal.cdx.template.GatewayProxyTemplate;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.dao.MBSProductDao;
import com.fanniemae.mbsportal.dao.MBSProfileDao;
import com.fanniemae.mbsportal.dao.MBSTransactionRequestDao;
import com.fanniemae.mbsportal.model.MBSProduct;
import com.fanniemae.mbsportal.model.MBSProfile;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.service.ConfigService;
import com.fanniemae.mbsportal.service.TokenService;
import com.fanniemae.mbsportal.utils.MBSMockUtil;
import com.fanniemae.mbsportal.utils.cdx.token.CDXTokenRefresher;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;
import com.fanniemae.mbsportal.utils.template.MBSRestInternalTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import org.apache.geode.cache.CommitConflictException;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * @author g8uaxt Created on 9/28/2017.
 */
public class TradeServicePollerTest extends BaseServiceTest {
        
        @Autowired
        TradeServicePoller tradeServicePoller;
        @Autowired
        AppVault appVault;
        @Mock
        CDXTokenRefresher tokenRefresher;
        @Mock
        TradeServiceProxyClient tradeServiceProxyClient;
        @Autowired
        TradeServiceProperties tradeServiceProperties;
        @Autowired
        private MBSTransactionRequestDao mbsTransactionRequestDao;
        @Autowired
        private MBSProductDao mbsProductDao;
        @Autowired
        private MBSProfileDao mbsProfileDao;
        @Mock
        private ConfigService configService;
        @Autowired
        private GatewayProxyTemplate gatewayProxyTemplate;
        @Autowired
        private TokenService tokenService;
        @Mock
        private List<String> tradeIds;
        @Mock
        private MBSTransactionRequest mbsTransactionRequest;
        @Mock
        private ResponseEntity<List> tsResponse;
        
        @Autowired
        private MBSRestInternalTemplate mbsRestInternalTemplate;
        
        private List<MBSTransactionRequest> transactionRequests = null;
        private List<TradeServiceRequestPO> tradeServiceRequestPOs = new ArrayList<>();
        private String authToken = "xfadfdsfs";
        private MBSProduct mbsProduct = null;
        private MBSMockUtil mbsMockUtil=null;
        
        HttpHeaders headers=null;
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Before
        public void setUp() throws Exception {
                //mbsTransactionRequestDao.setUserName(userName);
                mbsMockUtil = new MBSMockUtil();
                String[] urlArray = {"http://local"};
                when(tradeServiceProperties.getTraderServiceUrl()).thenReturn(urlArray);
                transactionRequests = mbsMockUtil.createTransactionRequests();
                List<String> stateTypeLst = new ArrayList<>();
                stateTypeLst.add(StateType.TRADER_CONFIRMED.toString());
                when(mbsTransactionRequestDao.getTransReqStateType(anyList(), anyList(), anyString(), anyString()))
                        .thenReturn(transactionRequests);
                when(mbsTransactionRequestDao.getAll(anyList())).thenReturn(transactionRequests);
                mbsProduct = mbsMockUtil.createMBSProduct();
                when(mbsProductDao.getById("1.PU.MBS")).thenReturn(mbsProduct);
                // when(mbsProductDao.getById(any())).thenReturn(mbsProduct);
                when(mbsProfileDao.getProfile("Test-trader")).thenReturn(mbsMockUtil.createMBSProfile());
                when(mbsProfileDao.getProfile("testLender")).thenReturn(mbsMockUtil.createMBSProfile());
                
                when(appVault.getTsAdminToken()).thenReturn(null);
                when(tradeServiceProxyClient.callTradeServiceProxy(any(), anyList())).thenReturn(true);
                //when(tokenRefresher.decrypt(any())).thenReturn(null);
                when(tokenRefresher.refreshLogin()).thenReturn("342342342dfjslldfs-sdf");
                
        
                doNothing().when(configService).saveConfigInfo(any(),any(),any());
                doNothing().when(mbsTransactionRequestDao).saveOrUpdate(any());
                
                headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.put("Cache-Control", Arrays.asList("max-age=3600"));
                            ResponseEntity<List> mockResponse = new ResponseEntity<List>(tradeServiceRequestPOs, headers,
                        HttpStatus.OK);
                when(gatewayProxyTemplate
                        .exchange(anyString(), Mockito.<HttpMethod>any(), Mockito.<HttpEntity<List<?>>>any(),
                                Matchers.any(Class.class)))
                        .thenReturn(mockResponse);//Mockito.<ResponseEntity<List>>any()
                
                when(mbsRestInternalTemplate
                        .exchange(anyString(), Mockito.<HttpMethod>any(), Mockito.<HttpEntity<List<?>>>any(),
                                Matchers.any(Class.class)))
                        .thenReturn(mockResponse);
        }
        
        @Test
        public void validateRetry_isRetryIntervalPassed_True() {
                System.out.print(tradeServicePoller.validateRetry(1, 1, 1, 1));
        }
        
        @Test
        public void validateRetry_isRetryIntervalPassed_False() {
                System.out.print(tradeServicePoller.validateRetry(2, 1, System.currentTimeMillis(), 999999999));
        }
        
        @Test
        public void dispatchAndUpdateStatus_Success() {
                when(tradeServiceProperties.getMaxRetry()).thenReturn("2");
                tradeServicePoller.disptachAndUpdateStatus(authToken, tradeIds);
        }
        
        @Test
        public void dispatchAndUpdateStatus_NullPendingList() throws MBSBaseException {
                when(tradeServiceProperties.getMaxRetry()).thenReturn("2");
                when(mbsTransactionRequestDao.getAll(tradeIds)).thenReturn(null);
                tradeServicePoller.disptachAndUpdateStatus(authToken, tradeIds);
        }
        
        @Test
        public void callToTradeService_Success() throws MBSBaseException {
                tradeServicePoller.callToTradeSrvice(1, 1, tradeServiceRequestPOs, mbsTransactionRequest, authToken);
        }
        
        @Test
        public void callToTradeService_Else_Success() throws MBSBaseException {
                when(tradeServicePoller.dispatchToTradeService(tradeServiceRequestPOs, authToken)).thenReturn(null);
                tradeServicePoller.callToTradeSrvice(1, 1, tradeServiceRequestPOs, mbsTransactionRequest, authToken);
        }
        
        @Test (expected = Exception.class)
        public void dispatchToTradeService_CatchException() {
                doThrow(new Exception()).when(tradeServicePoller.dispatchToTradeService(tradeServiceRequestPOs,
                        authToken));
        }
        
        @Test
        public void logAndUpdateResponse() throws MBSBaseException {
                when(tsResponse.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
                when(tradeServicePoller.dispatchToTradeService(tradeServiceRequestPOs, authToken)).thenReturn(null);
                tradeServicePoller.callToTradeSrvice(1, 1, tradeServiceRequestPOs, mbsTransactionRequest, authToken);
        }
        
        @SuppressWarnings("rawtypes")
        @Test
        public void postToTradeServiceListForResponseNotOK()
                throws MBSBaseException, InterruptedException, JsonProcessingException {
                tradeServiceRequestPOs = mbsMockUtil.createTradeServiceRequestPOs();
               /* ObjectMapper objectMapper = new ObjectMapper();
                String tradeServiceRequestPOsAsJson = objectMapper.writeValueAsString(tradeServiceRequestPOs);
                List<String> body = new ArrayList<>();
                body.add(tradeServiceRequestPOsAsJson);
                ResponseEntity<List> mockResponseTest = new ResponseEntity<List>(tradeServiceRequestPOs, headers,
                        HttpStatus.BAD_REQUEST);
                when(mbsRestInternalTemplate
                        .exchange(anyString(), Mockito.<HttpMethod>any(), Mockito.<HttpEntity<List<?>>>any(),
                                Matchers.any(Class.class)))
                        .thenReturn(mockResponseTest);*/
                ResponseEntity<List> status = tradeServicePoller.dispatchToTradeService(tradeServiceRequestPOs, authToken);
                assertTrue(status.getStatusCode().equals(HttpStatus.OK) || status.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR));
        }
        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Test
        public void postToTradeServiceListEmptyResponse() throws MBSBaseException, InterruptedException {
                tradeServiceRequestPOs = mbsMockUtil.createTradeServiceRequestPOs();
                when(mbsRestInternalTemplate
                        .exchange(anyString(), Mockito.<HttpMethod>any(), Mockito.<HttpEntity<List<?>>>any(),
                                Matchers.any(Class.class))).thenReturn(null);
                
                ResponseEntity<List> status = tradeServicePoller.dispatchToTradeService(tradeServiceRequestPOs, authToken);
                assertTrue(status.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR));
        }
        
        @Test
        public void getTradesAndChangeStatus() throws MBSBaseException {
                List<String> tradeIds = Arrays.asList("1A00012", "1A00013");
                tradeServicePoller.getTradesAndChangeStatus(tradeIds, StateType.TRADER_CONFIRMED);
        }
        
        @SuppressWarnings("unchecked")
        @Test(expected = MBSSystemException.class)
        public void getTradesAndChangeStatusForNotValidTradeIds() throws MBSBaseException {
                List<String> tradeIds = Arrays.asList("1A00012", "1A00013");
                when(mbsTransactionRequestDao.getAll(anyList())).thenReturn(null);
                tradeServicePoller.getTradesAndChangeStatus(tradeIds, StateType.TRADER_CONFIRMED);
        }
        
        @SuppressWarnings({ "rawtypes", "unused" })
        @Test
        public void postToTradeServiceListForNoRefresh() throws Exception {
                when(appVault.getTsAdminToken()).thenReturn("New Token for test");
                tradeServiceRequestPOs = mbsMockUtil.createTradeServiceRequestPOs();
                ResponseEntity<List> status = tradeServicePoller.dispatchToTradeService(tradeServiceRequestPOs, authToken);
                //assertFalse(status);
        }
        
        @Test
        public void prepareTradeServicePO() throws MBSBaseException, ParseException {
                when(tradeServiceProperties.getShakeOutTestUsers())
                        .thenReturn(Stream.of("brs-testusr").toArray(String[]::new));
                tradeServicePoller.prepareTradeServicePO(mbsMockUtil.createTransactionRequests().get(0));
                
        }
        
        @Test
        public void prepareTradeServicePOForOtherUser() throws MBSBaseException, ParseException {
                when(tradeServiceProperties.getShakeOutTestUsers())
                        .thenReturn(Stream.of("brs-produsr").toArray(String[]::new));
                MBSTransactionRequest mbsTransactionRequest = mbsMockUtil.createTransactionRequests().get(0);
                mbsTransactionRequest.setLenderShortName("TEST-C");
                tradeServicePoller.prepareTradeServicePO(mbsTransactionRequest);
                
        }
        
        @Test(expected = MBSBusinessException.class)
        public void prepareTradeServicePOExpiredSettlementDate() throws MBSBaseException, ParseException {
                MBSTransactionRequest mbsTransactionRequest = mbsMockUtil.createTransactionRequests().get(0);
                String currentDateStr = "2017-11-03";
                DateFormat simpleDateFormat = new SimpleDateFormat(DateFormats.DATE_FORMAT_NO_TIMESTAMP);
                Date pastDate = simpleDateFormat.parse(currentDateStr);
                mbsTransactionRequest.setTradeSettlementDate(pastDate);
                when(tradeServiceProperties.getShakeOutTestUsers())
                        .thenReturn(Stream.of("brs-produsr").toArray(String[]::new));
                tradeServicePoller.prepareTradeServicePO(mbsTransactionRequest);
                
        }
        
        @Test(expected = MBSBusinessException.class)
        public void prepareTradeServicePOTraderIdEmpty() throws MBSBaseException, ParseException {
                MBSTransactionRequest mbsTransactionRequest = mbsMockUtil.createTransactionRequests().get(0);
                mbsTransactionRequest.setTradeTraderIdentifierText(null);
                when(tradeServiceProperties.getShakeOutTestUsers())
                        .thenReturn(Stream.of("brs-produsr").toArray(String[]::new));
                tradeServicePoller.prepareTradeServicePO(mbsTransactionRequest);
                
        }
        
        @Test(expected = MBSBusinessException.class)
        public void prepareTradeServicePOBrsNameNull() throws MBSBaseException, ParseException {
                MBSProfile mbsTraderProfile = mbsMockUtil.createMBSProfile();
                mbsTraderProfile.setBrsUserName(null);
                when(mbsProfileDao.getProfile(any())).thenReturn(mbsTraderProfile);
                MBSTransactionRequest mbsTransactionRequest = mbsMockUtil.createTransactionRequests().get(0);
                when(tradeServiceProperties.getShakeOutTestUsers())
                        .thenReturn(Stream.of("brs-produsr").toArray(String[]::new));
                tradeServicePoller.prepareTradeServicePO(mbsTransactionRequest);
                
        }
        
        @Test(expected = MBSSystemException.class)
        public void prepareTradeServicePOCounterPartyNull() throws MBSBaseException, ParseException {
                MBSProfile mbsTraderProfile = mbsMockUtil.createMBSProfile();
                //mbsTraderProfile.setPartyShortName(null);
                when(mbsProfileDao.getProfile("test-counter-trader")).thenReturn(mbsTraderProfile);
                MBSTransactionRequest mbsTransactionRequest = mbsMockUtil.createTransactionRequests().get(0);
                mbsTransactionRequest.setCounterpartyTraderIdentifier("test-counter-trader");
                when(tradeServiceProperties.getShakeOutTestUsers())
                        .thenReturn(Stream.of("brs-produsr").toArray(String[]::new));
                tradeServicePoller.prepareTradeServicePO(mbsTransactionRequest);
        }
        
        @Test(expected = MBSBusinessException.class)
        public void prepareTradeServicePOProductNull() throws MBSBaseException, ParseException {
                when(mbsProductDao.getById(any())).thenReturn(null);
                MBSTransactionRequest mbsTransactionRequest = mbsMockUtil.createTransactionRequests().get(0);
                when(tradeServiceProperties.getShakeOutTestUsers())
                        .thenReturn(Stream.of("brs-produsr").toArray(String[]::new));
                tradeServicePoller.prepareTradeServicePO(mbsTransactionRequest);
        }
        @Test(expected = MBSBusinessException.class)
        public void prepareTradeServicePOSubPortfolioNull() throws MBSBaseException, ParseException {
                when(mbsProductDao.getById(any())).thenReturn(mbsProduct);
                mbsProduct.setBrsSubPortfolioShortName(null);
                MBSTransactionRequest mbsTransactionRequest = mbsMockUtil.createTransactionRequests().get(0);
                when(tradeServiceProperties.getShakeOutTestUsers())
                        .thenReturn(Stream.of("brs-produsr").toArray(String[]::new));
                tradeServicePoller.prepareTradeServicePO(mbsTransactionRequest);
        }
        
        @Test
        public void acquireTradeToProcess() throws MBSBaseException {
                List<String> stateTypeLst = Arrays.asList(StateType.TRADER_CONFIRMED.name());
                tradeServicePoller.acquireTradeToProcess(stateTypeLst);
        }
       
        @SuppressWarnings("unchecked")
        @Test
        public void acquireTradeToProcessForNoTrades() throws MBSBaseException {
                List<String> stateTypeLst = Arrays.asList(StateType.TRADER_CONFIRMED.name());
                when(mbsTransactionRequestDao.getTransReqStateType(anyList(), anyList(), anyString(), anyString()))
                        .thenReturn(null);
                List<String> list = tradeServicePoller.acquireTradeToProcess(stateTypeLst);
                assertTrue(list.isEmpty());
        }
        
        @SuppressWarnings("unused")
        @Test(expected = CommitConflictException.class)
        public void acquireTradeToProcessForCommitException() throws MBSBaseException {
                List<String> stateTypeLst = Arrays.asList(StateType.TRADER_CONFIRMED.name());
                doThrow(new CommitConflictException("test cce exception")).when(mbsTransactionRequestDao)
                        .updateAll(any());
                List<String> list = tradeServicePoller.acquireTradeToProcess(stateTypeLst);
                //assertNull(list);
        }
  
}
