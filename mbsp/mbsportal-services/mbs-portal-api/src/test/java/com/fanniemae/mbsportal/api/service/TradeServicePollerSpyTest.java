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
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.service.ConfigService;
import com.fanniemae.mbsportal.utils.MBSMockUtil;
import com.fanniemae.mbsportal.utils.cdx.token.CDXTokenRefresher;
import com.fanniemae.mbsportal.utils.template.MBSRestInternalTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * @author g8uaxt Created on 6/22/2018.
 */

public class TradeServicePollerSpyTest extends BaseServiceTest {
        
        @Autowired
        TradeServicePoller tradeServicePoller;
        
        @Mock
        private Logger LOGGER;
        @Autowired
        AppVault appVault;
        @Mock
        CDXTokenRefresher tokenRefresher;
        @Mock
        TradeServiceProxyClient tradeServiceProxyClient;
        @Autowired
        TradeServiceProperties tradeServiceProperties;
        HttpHeaders headers = null;
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
        private MBSRestInternalTemplate mbsRestInternalTemplate;
        
        private List<MBSTransactionRequest> transactionRequests = null;
        private List<TradeServiceRequestPO> tradeServiceRequestPOs = new ArrayList<>();
        private String authToken = "xfadfdsfs";
        private MBSProduct mbsProduct = null;
        private MBSMockUtil mbsMockUtil=null;
        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Before
        public void setUp() throws Exception {
                //mbsTransactionRequestDao.setUserName(userName);
                String[] urlArray = { "http://local" };
                when(tradeServiceProperties.getTraderServiceUrl()).thenReturn(urlArray);
                mbsMockUtil = new MBSMockUtil();
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
                
                when(tokenRefresher.refreshLogin()).thenReturn("342342342dfjslldfs-sdf");
                
                doNothing().when(configService).saveConfigInfo(any(), any(), any());
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
                                Matchers.any(Class.class))).thenReturn(mockResponse);
        }
        
        @SuppressWarnings("unchecked")
        @Test
        public void processTrades() throws Exception {
                TradeServicePoller tradeServicePollerSpy = Mockito.spy(tradeServicePoller);
                tradeServiceRequestPOs = mbsMockUtil.createTradeServiceRequestPOs();
                ResponseEntity<List> mockResponse = new ResponseEntity<List>(tradeServiceRequestPOs, headers,
                        HttpStatus.OK);
                doReturn(mockResponse).when(tradeServicePollerSpy).dispatchToTradeService(anyList(), any());
                when(tokenRefresher.decrypt(any())).thenReturn(null);
                tradeServicePollerSpy.processTrades();
        }
        
}
