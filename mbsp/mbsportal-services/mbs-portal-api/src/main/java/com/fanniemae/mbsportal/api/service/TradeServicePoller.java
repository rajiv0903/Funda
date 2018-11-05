/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.service;
/**

 */

import static com.fanniemae.mbsportal.cdx.constants.CDXHeaderMap.JWS_TOKEN;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.geode.cache.CommitConflictException;
import org.apache.geode.cache.TransactionDataNodeHasDepartedException;
import org.apache.geode.cache.TransactionDataRebalancedException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.fanniemae.fnmpfj.gemfire.utils.client.GemfireUtil;
import com.fanniemae.mbsportal.api.config.TradeServiceProperties;
import com.fanniemae.mbsportal.api.po.TradeServiceRequestPO;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.dao.MBSProductDao;
import com.fanniemae.mbsportal.dao.MBSProfileDao;
import com.fanniemae.mbsportal.dao.MBSTransactionRequestDao;
import com.fanniemae.mbsportal.model.MBSProduct;
import com.fanniemae.mbsportal.model.MBSProfile;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.service.TokenService;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import com.fanniemae.mbsportal.utils.template.MBSRestInternalTemplate;

/**
 * Trade service posting Service which picksup MBS Trades (based on Scheduled
 * interval) and call Tradeservice webservice using DC token as security measure
 *
 * @author g8uaxt
 *
 */
@Service
public class TradeServicePoller {

    /**
     *
     * Have a static variable to store the URL being used currently
     */
    private static String tradeServiceUrl;
    @Autowired
    TradeServiceProperties tradeServiceProperties;
    @InjectLog
    private Logger LOGGER;
    @Autowired
    private MBSTransactionRequestDao mbsTransactionRequestDao;
    @Autowired
    private MBSProductDao mbsProductDao;
    @Autowired
    private MBSProfileDao mbsProfileDao;
    @Autowired
    private MBSRestInternalTemplate mbsRestInternalTemplate;
   
    @Autowired 
    private TradeServiceProxyClient tradeServiceProxyClient;

    @Autowired
    private TokenService tokenService;
    // CMMBSSTA01-1047 : Throttling and retry logic for trade service call
   
    // End - CMMBSSTA01-1047 : Throttling and retry logic for trade service call

    /**
     * 
     * processTrades - entry method for posting to TS Trade Service Poller Job
     * Calls this method - Fetch the Trades which are in TRADER_CONFIRMED state
     * filter with Source System (Blue/Green); Mark all Trade as
     * EXECUTION_IN_PROGRESS; Get the Current Session Stored at MBSPConfigProp
     * Region using the key - ts.sessionId (If session is not there call Token
     * Refresher); Call GW URL with session info
     * (https://d2cwxh19afvdx5.cloudfront.net/fmnapi/tradeservice) which intern
     * inject the JWT token and call Trade Service Proxy If API Gateway throw
     * any Error - UnAuthorized or GateWay Time Out then Reset the Session Info
     *
     *
     * @throws Exception
     */
    public void processTrades() throws Exception {
        LOGGER.debug("PostToTradeService ()");
        List<String> stateTypeLst = new ArrayList<>();
        stateTypeLst.add(StateType.TRADER_CONFIRMED.toString());
        // blocking other threads to deal with
        List<String> traderIds = acquireTradeToProcess(stateTypeLst);
        if (traderIds == null || traderIds.isEmpty()) {
            LOGGER.debug("No Pending Records to create Trade");
            return;
        }
        LOGGER.debug("Pending Records to create Trade");
        try {
            String sessionId = null;
            try {
                sessionId = tokenService.getValidSessionId();
            } catch (Exception e) {
                throw new Exception("exception while validating session -revert back status tradeIds: ");
            }
            try {
                // restrict to one thread
                LOGGER.debug("calling TradeService Proxy...");
                tradeServiceProxyClient.callTradeServiceProxy(sessionId, traderIds);
                LOGGER.debug("TradeService Proxy completed successful");
            } catch (HttpServerErrorException | HttpClientErrorException hsex) {
                if (hsex.getStatusCode() == HttpStatus.BAD_GATEWAY) {
                    LOGGER.debug("error in callTradeServiceProxy : Bad Gateway", hsex);
                }
                if (hsex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    LOGGER.debug("error in callTradeServiceProxy : UNAUTHORIZED ");
                    LOGGER.debug("Invalid Session : resetSessionAndToken", hsex);
                    tokenService.resetSessionAndToken();
                }
                if (hsex.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                    LOGGER.error("error in callTradeServiceProxy : INTERNAL_SERVER_ERROR ", hsex);
                }
                throw new Exception("exception while calling proxy -revert back status tradeIds: " + hsex.getMessage());
            } catch (Exception e) {
                String errorCode = e.getMessage();
                if (errorCode.contains(HttpStatus.UNAUTHORIZED.toString())) {
                    LOGGER.debug("Invalid Session : resetSessionAndToken");
                    tokenService.resetSessionAndToken();
                }
                LOGGER.error("In processTrades() ", e);
                throw new Exception("exception while calling proxy -revert back status tradeIds: " + e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + traderIds);
            getTradesAndChangeStatus(traderIds, StateType.TRADER_CONFIRMED);
        }
    }

    /**
     * 
     * This method will decide and acquire Trades to process by each scheduler
     * threads
     *
     * @param stateTypeLst the stateTypeLst
     * @return List<String>
     * @throws MBSBaseException
     */
    public List<String> acquireTradeToProcess(List<String> stateTypeLst) throws MBSBaseException {
        List<MBSTransactionRequest> pendingLists = null;
        List<String> pendingIds = new ArrayList<>();
        try {
            BaseProcessor.beginTransaction(GemfireUtil.getTransactionManager());
            // CMMBSSTA01-787: Added source system to query
            pendingLists = mbsTransactionRequestDao.getTransReqStateType(stateTypeLst, null, "asc",
                    tradeServiceProperties.getSourceSystem()); // StateType.TRADER_CONFIRMED
            if (pendingLists == null || pendingLists.isEmpty()) {
                LOGGER.debug("acquireTradeToProcess: No trades to process" + pendingIds);
                return pendingIds;
            }
            // CMMBSSTA01-787: End -change
            for (MBSTransactionRequest pendingList : pendingLists) {
                pendingList.setStateType(StateType.EXECUTION_IN_PROGRESS.name());
                pendingIds.add(pendingList.getTransReqNumber());
            }

            mbsTransactionRequestDao.updateAll(pendingLists);
            LOGGER.debug("acquireTradeToProcess: changed status to EXECUTION_IN_PROGRESS" + pendingIds);
            BaseProcessor.commitTransaction(GemfireUtil.getTransactionManager());
            // calling proxy

        } catch (CommitConflictException | TransactionDataRebalancedException
                | TransactionDataNodeHasDepartedException cce) {
            LOGGER.error("CommitConflict GF:", cce);
            BaseProcessor.rollbackTransaction(GemfireUtil.getTransactionManager());
            throw cce;
        } catch (Exception e) {
            LOGGER.error("processRequest:", e);
            BaseProcessor.handleTransactionException(GemfireUtil.getTransactionManager(), e);
        }
        return pendingIds;
    }

    /**
     * 
     * getTradesAndChangeStatus - Util method to update the list of Trades
     *
     * @param tradeIds the tradeIds
     * @param stateType the stateType
     * @throws MBSBaseException
     */
    public void getTradesAndChangeStatus(List<String> tradeIds, StateType stateType) throws MBSBaseException {
        List<MBSTransactionRequest> pendingLists = null;
        try {
            pendingLists = mbsTransactionRequestDao.getAll(tradeIds);
            if (pendingLists == null || pendingLists.isEmpty()) {
                throw new MBSSystemException("No records to update");
            }
            for (MBSTransactionRequest mbsTransactionRequest : pendingLists) {
                LOGGER.debug(
                        "In getTradesAndChangeStatus method TransReq# " + mbsTransactionRequest.getTransReqNumber());
                mbsTransactionRequest.setStateType(stateType.toString());
                mbsTransactionRequestDao.saveOrUpdate(mbsTransactionRequest);
            }
        } catch (MBSBaseException e) {
            LOGGER.error("getTradesAndChangeStatus failed ", e.getMessage());
            throw e;
        }

    }

    /**
     *
     * disptachAndUpdateStatus - Proxy will call this method to post TS
     *
     * @param authToken the authToken
     * @param tradeIds the tradeIds
     * @return boolean
     */
    public boolean disptachAndUpdateStatus(String authToken, List<String> tradeIds) {
        LOGGER.debug("TradeIds processing in disptachAndUpdateStatus " + tradeIds); // [["18A08817"]]
        List<MBSTransactionRequest> pendingLists = null;
        // CMMBSSTA01-1047 : Throttling and retry logic for trade service call
        Boolean postStatus = true;
        Boolean postStatusTemp = true;
        int maxRetry = Integer.valueOf(tradeServiceProperties.getMaxRetry()).intValue();
        int retryCount = 0;
        // End: CMMBSSTA01-1047 : Throttling and retry logic for trade service
        // call
        try {
            // get thet latest list from GF
            pendingLists = mbsTransactionRequestDao.getAll(tradeIds);
            LOGGER.debug("pendingLists in disptachAndUpdateStatus" + pendingLists);
            if (pendingLists == null || pendingLists.isEmpty()) {
                LOGGER.debug("disptachAndUpdateStatus: No matching trade ids to process " + tradeIds);
                return false;
            }
            List<TradeServiceRequestPO> tradeServiceRequestPOs = new ArrayList<>();
            for (MBSTransactionRequest mbsTransactionRequest : pendingLists) {
                LOGGER.debug("Picked up Trades to Send Trade Service " + mbsTransactionRequest);
                // CMMBSSTA01-1047 : Throttling and retry logic for trade
                // service call
                // Changes to send one trade at a time to TS
                tradeServiceRequestPOs.clear();
                tradeServiceRequestPOs.add(prepareTradeServicePO(mbsTransactionRequest));
                LOGGER.debug("Validating retryCount " + mbsTransactionRequest);
                retryCount = Objects.isNull(mbsTransactionRequest.getRetryCount()) ? 0
                        : Integer.valueOf(mbsTransactionRequest.getRetryCount()).intValue();
                LOGGER.debug("Inside retryCount validation callToTradeService: Req Id# "
                            + mbsTransactionRequest.getTransReqNumber());
                postStatusTemp = callToTradeSrvice(maxRetry, retryCount, tradeServiceRequestPOs,
                            mbsTransactionRequest, authToken);
                //Even if one record fails, update the status as false
                if (postStatus) {
                    postStatus = postStatusTemp;
                }
            }
            // End: CMMBSSTA01-1047 : Throttling and retry logic for trade
            // service call
            LOGGER.debug("TradeIds processed successfully in disptachAndUpdateStatus " + tradeIds);
        } catch (MBSBaseException mbse) {
            // revert to TRADER_CONFIRMED
            try {
                getTradesAndChangeStatus(tradeIds, StateType.TRADER_CONFIRMED);
            } catch (MBSBaseException e) {
                LOGGER.error("Failed in dispatchToTradeService:getTradesAndChangeStatus", e);
            }
            MBSExceptionConstants.logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER, "",
                    "Failed in dispatchToTradeService! will try in next schedule. No of Trades " + "failed : "
                            + tradeIds.size() + "  with Error: " + mbse.getRootExceptionMessage(),
                    "dispatchToTradeService", "", "");
            LOGGER.error("Failed in dispatchToTradeService with MBSBaseException", mbse);
            MBSExceptionConstants.resetLogAlert();
            postStatus = false;
        } catch (Exception ex) {
            // revert to TRADER_CONFIRMED
            try {
                getTradesAndChangeStatus(tradeIds, StateType.TRADER_CONFIRMED);
            } catch (MBSBaseException e) {
                LOGGER.error("Failed in dispatchToTradeService:getTradesAndChangeStatus", e);
            }
            MBSExceptionConstants
                    .logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER, "",
                            "Failed in dispatchToTradeService! will try in next schedule. No of Trades " + "failed : "
                                    + tradeIds.size() + "with Error: " + ex.getMessage(),
                            "dispatchToTradeService", "", "");
            LOGGER.error("Failed in dispatchToTradeService with Exception", ex);
            MBSExceptionConstants.resetLogAlert();
            postStatus = false;
        }
        return postStatus;
    }

    /**
     *
     *
     * For story - CMMBSSTA01-1047 : Throttling and retry logic for trade
     * service call
     *
     * @param maxRetry the maxRetry
     * @param retryCount the retryCount
     * @param tradeServiceRequestPOs the tradeServiceRequestPOs
     * @param mbsTransactionRequest the mbsTransactionRequest
     * @param authToken the authToken
     * @return boolean
     */
    public boolean callToTradeSrvice(int maxRetry, int retryCount, List<TradeServiceRequestPO> tradeServiceRequestPOs,
            MBSTransactionRequest mbsTransactionRequest, String authToken) throws MBSBaseException {

        int retryCountLocal = 0;
        ResponseEntity<List> tsResponse = null;
        boolean status = true;
        int totalCount;
        LOGGER.debug("IN callToTradeSrvice()" + mbsTransactionRequest.getTransReqNumber());
        tsResponse = dispatchToTradeService(tradeServiceRequestPOs, authToken);
        if (tsResponse.getStatusCode().equals(HttpStatus.OK)) {
            LOGGER.debug("IN callToTradeSrvice() TransReq# " + mbsTransactionRequest.getTransReqNumber());
            mbsTransactionRequest.setStateType(StateType.PENDING_EXECUTION.toString());
            mbsTransactionRequestDao.saveOrUpdate(mbsTransactionRequest);
        } else {
            retryCountLocal++;
            totalCount = retryCountLocal + retryCount;
            LOGGER.debug("Dispatch failed. Local Retry count: " + retryCountLocal);
            LOGGER.debug("Dispatch failed. Total Retry count: " + totalCount);
            LOGGER.debug("Dispatch failed. Response code : " + tsResponse.getStatusCodeValue());
            LOGGER.debug("Dispatch failed. Response body : " + tsResponse.getBody());
            LOGGER.debug("TransReq# " + mbsTransactionRequest.getTransReqNumber());
            mbsTransactionRequest.setRetryCount(String.valueOf(totalCount));
            mbsTransactionRequestDao.saveOrUpdate(mbsTransactionRequest);
        }
        if (Objects.nonNull(tsResponse) && (!(tsResponse.getStatusCode().equals(HttpStatus.OK)))) {
            status = logAndUpdateResponse(mbsTransactionRequest, tsResponse);
        }
        return status;
    }

    /**
     *
     * For story - CMMBSSTA01-1047 : Throttling and retry logic for trade
     * service call
     *
     * @param retryCount the retryCount
     * @param maxRetry the maxRetry
     * @param lastUpdated the lastUpdated
     * @param timeToCompare the timeToCompare
     * @return boolean
     */
    public boolean validateRetry(int retryCount, int maxRetry, long lastUpdated, long timeToCompare) {
        int multiplier = retryCount > 0 ? ((retryCount + maxRetry) / maxRetry) : 1;
        if (retryCount < maxRetry || (retryCount >= maxRetry
                && isRetryIntervalPassed(lastUpdated, (timeToCompare * (multiplier == 0 ? 1 : multiplier))))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * For story - CMMBSSTA01-1047 : Throttling and retry logic for trade
     * service call
     *
     * @param lastUpdated the lastUpdated
     * @param timeToCompare the timeToCompare
     * @return boolean
     */
    private boolean isRetryIntervalPassed(long lastUpdated, long timeToCompare) {
        if (System.currentTimeMillis() - lastUpdated >= timeToCompare)
            return true;
        else
            return false;
    }

    /**
     *
     * This method logs and updates the failure response - CMMBSSTA01-1047 :
     * Throttling and retry logic for trade service call
     *
     * @param mbsTransactionRequest
     *            - the trade details
     * @param tsResponse
     *            - the response from the service call
     * @return boolean
     */
    private boolean logAndUpdateResponse(MBSTransactionRequest mbsTransactionRequest, ResponseEntity<List> tsResponse) {
        List<String> transId = new ArrayList<>();
        boolean status = true;
        transId.add(mbsTransactionRequest.getTransReqNumber());
        // If after retries, the status is bad request, log it as error and move
        // on
        if (tsResponse.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            try {
                getTradesAndChangeStatus(transId, StateType.ERROR);
            } catch (MBSBaseException e) {
                LOGGER.error(
                        "Failed in disptachAndUpdateStatus:getTradesAndChangeStatus. Changing status " + "to error.",
                        e);
            }
            MBSExceptionConstants.logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER, "",
                    "dispatchToTradeService", "", "", "Failed in disptachAndUpdateStatus for trade id  "
                            + mbsTransactionRequest.getTransReqNumber() + "  with Error: Bad Request");
            LOGGER.error("Failed in disptachAndUpdateStatus for trade id  " + mbsTransactionRequest.getTransReqNumber()
                    + "  with Error: Bad Request");
            MBSExceptionConstants.resetLogAlert();
            status = false;
        }
        // If after retries, the status is server error, log it as error but
        // change the status back so that it can be retried later
        else {
            try {
                getTradesAndChangeStatus(transId, StateType.TRADER_CONFIRMED);
            } catch (MBSBaseException e) {
                LOGGER.error("Failed in dispatchToTradeService:getTradesAndChangeStatus. Changing status "
                        + "to trader confirmed.", e);
            }
            LOGGER.error("Failed in disptachAndUpdateStatus for trade id.  " + mbsTransactionRequest.getTransReqNumber()
                    + " Response Body : "+tsResponse.getBody()+" Response status code value "+tsResponse.getStatusCodeValue());
            status = false;
        }
        return status;
    }

   
    /**
     * 
     * dispatchToTradeService - Method will post to TS for processing at BR
     *
     * @param tradeServiceRequestPOs the tradeServiceRequestPOs
     * @param authToken the authToken
     * @return ResponseEntity<List>
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ResponseEntity<List> dispatchToTradeService(List<TradeServiceRequestPO> tradeServiceRequestPOs,
            String authToken) {
        // call the TS
        ResponseEntity<List> response = null;
        List<String> exceptionList = new ArrayList();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.set(JWS_TOKEN.getValue(), authToken);
            HttpEntity<List<TradeServiceRequestPO>> request = new HttpEntity<>(tradeServiceRequestPOs, headers);
            // CMMBSSTA01-1047 : Throttling and retry logic for trade service
            // call
            LOGGER.debug("calling TS with internal url:" + getTradeServiceUrl());
            response = mbsRestInternalTemplate.exchange(getTradeServiceUrl(), HttpMethod.POST, request, List.class);
            LOGGER.debug("Response from TS:" + response);
        } catch (Exception e) {
            LOGGER.error("Error while calling TS-tradeServiceRequestPOResponsefor URL:" + getTradeServiceUrl(), e);
            exceptionList.add("Exception occured when trying to connect trade service");
            //Added alert for exceptions
            MBSExceptionConstants.logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER, MBSExceptionConstants.SYSTEM_EXCEPTION.toString(),
                    "Failed in dispatchToTradeService when calling TS with Error: " + e.getMessage(),
                    "dispatchToTradeService", "ResponseEntity<List> dispatchToTradeService", "(List<TradeServiceRequestPO> tradeServiceRequestPOs, String authToken)");
            LOGGER.error("Failed in dispatchToTradeService with Exception", e);
            MBSExceptionConstants.resetLogAlert();

            if (Objects.nonNull(response)) {
                for(String json: (List<String>)response.getBody()){
                    LOGGER.error("Error while calling TS-tradeServiceRequestPOResponsefor URL. The response is {}",json);
                }
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionList);
        }
        if (Objects.isNull(response)) {
            LOGGER.error("Response from Trade service is null. Would retry later for URL." + getTradeServiceUrl());
            exceptionList.add("Response from Trade service is null. Would retry later for URL." + getTradeServiceUrl());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionList);
        } else {
            if(!response.getStatusCode().equals(HttpStatus.OK)){
                LOGGER.error("Error while calling TS-tradeServiceRequestPOResponsefor URL. The response code is "+response.getStatusCodeValue());
                LOGGER.error("The response body is "+response.getBody());
                if(response.getBody()!=null){
                    for(String json: (List<String>)response.getBody()){
                        LOGGER.error("Error while calling TS-tradeServiceRequestPOResponsefor URL. The response is {}",json);
                    }
                }
            }
        }
        LOGGER.debug("Response object from TS-tradeServiceRequestPOResponse:"
                + (List<TradeServiceRequestPO>) response.getBody());
        // End - CMMBSSTA01-1047 : Throttling and retry logic for trade service
        // call
        return response;
    }

    /**
     * 
     * prepareTradeServicePO . It does business validation in this method
     *
     * @param mbsTransactionRequest the mbsTransactionRequest
     * @return TradeServiceRequestPO
     * @throws MBSBaseException
     */
    protected TradeServiceRequestPO prepareTradeServicePO(MBSTransactionRequest mbsTransactionRequest)
            throws MBSBaseException {
        TradeServiceRequestPO tradeServiceRequestPO = new TradeServiceRequestPO();
        tradeServiceRequestPO.setQuantity(mbsTransactionRequest.getTradeAmount().longValue());
        if (MBSPortalUtils.isItPastDate(mbsTransactionRequest.getTradeSettlementDate())) {
            String errMsg = String.format("TradeSettlementDate %s is expired ",
                    mbsTransactionRequest.getTradeSettlementDate());
            LOGGER.error(errMsg);
            throw new MBSBusinessException(errMsg);
        }

        tradeServiceRequestPO.setTradeSettlementDate(mbsTransactionRequest.getTradeSettlementDate());
        tradeServiceRequestPO.setTradeDate(mbsTransactionRequest.getSubmissionDate());
        tradeServiceRequestPO.setTradeType(mbsTransactionRequest.getTradeBuySellType());
        tradeServiceRequestPO.setTbaSettlementDate(mbsTransactionRequest.getTradeSettlementDate());
        // send BR name code
        MBSProduct mbsProduct = ((MBSProduct) mbsProductDao
                .getById(mbsTransactionRequest.getProductId().getProductIdStr()));
        if (mbsProduct == null || mbsProduct.getProductBRSCode() == null) {
            LOGGER.error("MBSProduct/ProductBRSCode is empty:" + mbsProduct);
            throw new MBSBusinessException("MBSProduct/ProductBRSCode is empty:" + mbsProduct);
        }
        tradeServiceRequestPO.setTbaTicker(mbsProduct.getProductBRSCode());
        tradeServiceRequestPO.setTbaCoupon(mbsTransactionRequest.getTradeCouponRate());
        tradeServiceRequestPO.setSourceTransactionId(mbsTransactionRequest.getTransReqNumber());
        tradeServiceRequestPO.setPricePercent(mbsTransactionRequest.getPricePercent());
        tradeServiceRequestPO.setCommentGeneral(StringUtils.EMPTY);
        // Partylookup
        if (StringUtils.isBlank(mbsProduct.getBrsSubPortfolioShortName())) {
            String errMsg = String.format("BrsSubPortfolioShortName is empty %s for product %s",
                    mbsProduct.getBrsSubPortfolioShortName(), mbsProduct.getProductId().getProductIdStr());
            LOGGER.error(errMsg);
            throw new MBSBusinessException(errMsg);
        }
        tradeServiceRequestPO.setPurpose(StringUtils.EMPTY);
        tradeServiceRequestPO.setTbaType(tradeServiceProperties.getTbaType());
        tradeServiceRequestPO.setTradeVariance(BigDecimal.valueOf(0.01));
        if (StringUtils.isBlank(mbsTransactionRequest.getTradeTraderIdentifierText())) {
            String errMsg = String.format("TraderIdentifierText is empty for tradeId: %s",
                    mbsTransactionRequest.getTransReqNumber());
            LOGGER.error(errMsg);
            throw new MBSBusinessException(errMsg);
        }
        MBSProfile mbsTraderProfile = mbsProfileDao.getProfile(mbsTransactionRequest.getTradeTraderIdentifierText());
        String brsUserName = null;
        if (mbsTraderProfile != null) {
            brsUserName = mbsTraderProfile.getBrsUserName();
        }
        if (StringUtils.isBlank(brsUserName)) {
            String errMsg = String.format("BRS User name is invalid in MBSProfile for tradeId %s",
                    mbsTransactionRequest.getTransReqNumber());
            LOGGER.error(errMsg);
            throw new MBSBusinessException(errMsg);
        }
        // check for test users
        if (Arrays.stream(tradeServiceProperties.getShakeOutTestUsers()).anyMatch(brsUserName::equals)) {
            tradeServiceRequestPO.setPortfolioName(tradeServiceProperties.getShakeoutPortfolio());
            LOGGER.debug("Using shakeout Portfolio {} for BR User {} and CounterParty {}",
                    tradeServiceProperties.getShakeoutPortfolio(), brsUserName,
                    tradeServiceProperties.getShakeOutCPartyName());
            tradeServiceRequestPO.setCounterPartyName(tradeServiceProperties.getShakeOutCPartyName());
            tradeServiceRequestPO.setTradeConfirmedBy(StringUtils.EMPTY);
            tradeServiceRequestPO.setTradeConfirmedWith(StringUtils.EMPTY);

        } else {
          //CMMBSSTA01-1373 - Party short name populating - Start
            //The LenderShortName is getting populated from MSBTransaction. Earlier it was populated from Profile
            String counterPartyUserName = mbsTransactionRequest.getLenderShortName();
          //CMMBSSTA01-1373 - Party short name populating - End
            if (StringUtils.isEmpty(counterPartyUserName)) {
                String errMsg = String.format("Counter Party name is invalid in MBSProfile for tradeId %s",
                        mbsTransactionRequest.getTransReqNumber());
                LOGGER.error(errMsg);
                throw new MBSSystemException(errMsg);
            }
            tradeServiceRequestPO.setCounterPartyName(counterPartyUserName);
            tradeServiceRequestPO.setPortfolioName(mbsProduct.getBrsSubPortfolioShortName());
            tradeServiceRequestPO.setTradeConfirmedBy(tradeServiceProperties.getTradeConfirmedBy());
            tradeServiceRequestPO.setTradeConfirmedWith(tradeServiceProperties.getTradeConfirmedWith());
        }
        tradeServiceRequestPO.setTraderName(brsUserName.toUpperCase());
        // tradeServiceRequestPO.setSourceSystem(tradeServiceProperties.getSourceSystem());
        tradeServiceRequestPO.setSourceSystem(mbsTransactionRequest.getSourceSystem());
        return tradeServiceRequestPO;
    }

    /**
     *
     * @return String the tradeServiceUrl
     */
    public String getTradeServiceUrl() { //TODO: remove it later
        String[] tradeServiceUrls = tradeServiceProperties.getTraderServiceUrl();
        if (Objects.isNull(tradeServiceUrl)) {
            tradeServiceUrl = tradeServiceUrls[0];
        }
        return tradeServiceUrl;
    }

}
