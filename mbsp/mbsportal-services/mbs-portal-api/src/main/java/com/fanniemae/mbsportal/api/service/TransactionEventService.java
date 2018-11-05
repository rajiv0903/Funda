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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.geode.cache.CommitConflictException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fanniemae.fnmpfj.gemfire.utils.client.GemfireUtil;
import com.fanniemae.mbsportal.api.publisher.TransactionRequestMessagePublisher;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.api.utils.MBSUtils;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.dao.MBSProductTransactionXrefDao;
import com.fanniemae.mbsportal.dao.MBSTransactionRequestDao;
import com.fanniemae.mbsportal.model.MBSProductTransactionXref;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * @author g8uaxt Created on 12/6/2017.
 */
@Component
public class TransactionEventService {

    /**
     *
     * LOGGER Logger variable
     */
    @InjectLog
    private Logger LOGGER;

    /**
     *
     * mBSTransactionRequestDao MBSTransactionRequestDao
     */
    @Autowired
    private MBSTransactionRequestDao mBSTransactionRequestDao;

    /**
     *
     * mbsProductTransactionXrefDao MBSProductTransactionXrefDao
     */
    @Autowired
    private MBSProductTransactionXrefDao mbsProductTransactionXrefDao;

    /**
     *
     * mbsObjectCreator MBSObjectCreator
     */
    @Autowired
    private MBSObjectCreator mbsObjectCreator;

    /**
     *
     * transactionRequestMessagePublisher TransactionRequestMessagePublisher
     */
    @Autowired
    private TransactionRequestMessagePublisher transactionRequestMessagePublisher;

    /**
     *
     * isActive boolean
     */
    boolean isActive = true;

    /**
     *
     * timeToTimeoutinMilli long
     */
    @Value("${mbsp.timeToTimeoutinMilli}")
    long timeToTimeoutinMilli; // 2 min 15 seconds

    /**
     *
     * sleepTime long
     */
    @Value("${mbsp.timeoutSleep}")
    long sleepTime; // 1 sec

    /**
     *
     * tradesToScan Map<String, MBSTransactionRequest>
     */
    // TODO: Recover this Map data on restart of TransactionEventService
    public Map<String, MBSTransactionRequest> tradesToScan = new ConcurrentHashMap<String, MBSTransactionRequest>();

    /**
     *
     * adding tx req to map for scanning
     *
     * @param key
     *            the key
     * @param obj
     *            the obj
     */
    public void add(String key, MBSTransactionRequest obj) {
        tradesToScan.put(key, obj);
    }

    /**
     *
     * remove tx req from map for not scanning
     *
     * @param key
     *            the key
     * @return MBSTransactionRequest
     */
    public MBSTransactionRequest remove(String key) {
        return tradesToScan.remove(key);
    }

    /**
     *
     * scan for status and timeout based on it
     *
     * @throws InterruptedException
     */
    @Async
    public void scanStatusAndTimedOut() throws InterruptedException {
        // TODO: add async name to it to track
        // scan the map &
        LOGGER.debug("TransactionEventService scanStatusAndTimedOut method running  asynchronously. "
                + Thread.currentThread().getName());
        while (isActive) {
            // tradesToScan.forEach((k, v) -> LOGGER.info("key : " + k + "
            // value : " + v));
            // scan map and mark timeout
            Iterator<Map.Entry<String, MBSTransactionRequest>> iterator = tradesToScan.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, MBSTransactionRequest> entry = iterator.next();
                // System.out.println(entry.getKey() + ":" + entry.getValue());
                String key = entry.getKey();
                // LOGGER.info("TransactionEventService validating key: " +
                // key);
                MBSTransactionRequest obj = entry.getValue();
                // if (isTimedOut(obj.getLastUpdated().getTime())) {
                // Changed to submitted time - CMMBSSTA01-1312
                if (isTimedOut(obj.getSubmissionDate().getTime())) {
                    LOGGER.debug("TransactionEventService key: " + key);
                    // persist this record as TIMEDOUT
                    try {
                        persistTradeAsTimedout(obj);
                        // no longer needed this key
                        LOGGER.debug("TransactionEventService removed key: " + key);
                        remove(key);
                    } catch (Exception e) {
                        LOGGER.error("TransactionEventService Error while persist: " + obj);
                    }
                }
            }
            // Thread.sleep(sleepTime);
            TimeUnit.MILLISECONDS.sleep(sleepTime);
        }
    }

    /**
     *
     * Description : This method will be called to take care of Time Out the
     * Transaction To check the accuracy need to make sure that existing status
     * should match with status what client is passing - To prevent that the
     * entry did not get updated from other system
     *
     * Once success the above logic then save to Gemfire with time Out status
     * and create events for both Trader and Lender
     *
     * @param obj
     *            MBSTransactionRequest
     * @return boolean
     * @throws MBSBaseException
     */
    boolean persistTradeAsTimedout(MBSTransactionRequest obj) throws MBSBaseException {

        try {
            // Remove Transaction
            BaseProcessor.beginTransaction(GemfireUtil.getTransactionManager());
            LOGGER.debug("Entering persistTradeAsTimedout in TransactionEventService");

            // MBSTransactionRequest existingObj = (MBSTransactionRequest)
            // mBSTransactionRequestDao
            // .getById(obj.getTransReqNumber());
            // CMMBSSTA01-1982 - Updated transaction request key
            LOGGER.debug("persistTradeAsTimedout obj key value: " + obj.getId());
            MBSTransactionRequest existingObj = (MBSTransactionRequest) mBSTransactionRequestDao.getById(obj.getId());
            TransformationObject transformationObject; 
            if (existingObj != null) {
                String stateType = existingObj.getStateType();
                if (stateType.equalsIgnoreCase(StateType.LENDER_TIMEOUT.name())
                        || stateType.equalsIgnoreCase(StateType.TRADER_TIMEOUT.name())) {
                    LOGGER.debug("Already timedout Status encountered:" + stateType);
                    //Publish it to trader only as we have other traders in other nodes
                    transformationObject = mbsObjectCreator.getTransformationObject();
                    transformationObject.setMBSRoleType(MBSRoleType.TRADER);
                    transformationObject.setTargetPojo(existingObj);
                    LOGGER.debug("Publish timeout to both trader only for timed out" + transformationObject.getTargetPojo()
                            + "role type " + transformationObject.getMBSRoleType());
                    transactionRequestMessagePublisher.publish(transformationObject);
                } else {
                    // Assumption - State Contains for Lender Side starts with
                    // Lender
                    if (!isItNonTrackingStatus(obj.getStateType())) {

                        String stateTypeNew = getWhoTimedOut(stateType);
                        existingObj.setStateType(stateTypeNew);
                        LOGGER.debug("Saving timed-out Status:" + stateTypeNew);
                        mBSTransactionRequestDao.saveOrUpdate(existingObj);
                        mBSTransactionRequestDao.saveEvents(existingObj, null);

                        /*
                         * Convert the Transaction to PO using TRADER Role-
                         * Trader First Name and Last Name Publish to Trader
                         * topic and Lender or TSP who owns it Publish Message
                         * even if time out by another nodes
                         */
                        transformationObject = mbsObjectCreator.getTransformationObject();
                        transformationObject.setMBSRoleType(MBSRoleType.LENDER_AND_TRADER);
                        transformationObject.setTargetPojo(existingObj);
                        LOGGER.debug("Publish timeout to both lender and trader " + transformationObject.getTargetPojo()
                                + "role type " + transformationObject.getMBSRoleType());
                        transactionRequestMessagePublisher.publish(transformationObject);
                    } else {
                        LOGGER.debug("Already terminal Status Transaction ID : {}, Status: {}, hence no time out",
                                obj.getTransReqNumber(), stateType);
                    }
                }

            } else {
                LOGGER.debug("The retrieved transaction is empty.");
            }
            BaseProcessor.commitTransaction(GemfireUtil.getTransactionManager());

        } catch (CommitConflictException cce) {
            LOGGER.error("CommitConflictException: with TxReq#" + obj.getTransReqNumber(), cce);
            BaseProcessor.rollbackTransaction(GemfireUtil.getTransactionManager());
            LOGGER.debug("Exiting persistTradeAsTimedout in TransactionEventService");
            return false;
        } catch (Exception e) {
            LOGGER.error("processRequest: with TxReq#" + obj.getTransReqNumber(), e);
            BaseProcessor.handleTransactionException(GemfireUtil.getTransactionManager(), e);
            MBSExceptionConstants.logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER, "",
                    "Failed save Timeout status! will try in next schedule. TxReqNo# " + obj.getTransReqNumber(),
                    "persistTradeAsTimedout", "", "");
            LOGGER.error("Failed dispatchToTradeService");
            MBSExceptionConstants.resetLogAlert();
            LOGGER.debug("Exiting persistTradeAsTimedout in TransactionEventService");
            throw new MBSSystemException(
                    "Failed to save time out transaction" + (obj != null ? obj.getTransReqNumber() : ""),
                    MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        LOGGER.debug("Exiting persistTradeAsTimedout in TransactionEventService");
        return true;
    }

    /**
     *
     * @param stateType
     *            String
     * @return String
     */
    String getWhoTimedOut(String stateType) {
        if (stateType.toLowerCase().contains("lender")) {
            return StateType.TRADER_TIMEOUT.name();
        } else {
            return StateType.LENDER_TIMEOUT.name();
        }

    }

    /**
     *
     * @param lastUpdated
     *            long
     * @return boolean
     */
    private boolean isTimedOut(long lastUpdated) {
        // scan last updated from TxRq and mark it if timeout.
        // if (System.currentTimeMillis() - lastUpdated >= timeToTimeoutinMilli)
        // Changed to get time from Util method with timezone - CMMBSSTA01-1312
        if (MBSPortalUtils.getCurrentTimeStamp() - lastUpdated >= timeToTimeoutinMilli) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     *
     * @param sourceSystem
     * @return List<MBSTransactionRequest>
     */
    public List<MBSTransactionRequest> getActiveTransactions(String sourceSystem) {
        List<MBSTransactionRequest> mbsTransactionRequestLst = new ArrayList<>();
        try {
            List<String> lstStateType = MBSUtils.convertStateTypeToStringList(StateType.getFlowList("TRADER_FLOW"));
            mbsTransactionRequestLst = mBSTransactionRequestDao.getTransReqStateType(lstStateType, null, "asc",
                    sourceSystem);
        } catch (Exception ex) {
            LOGGER.error("Error retrieving active trades from region");
        }
        return mbsTransactionRequestLst;
    }

    /**
     *
     * This status is not to track changes for timeout
     *
     * @param stateType
     *            String
     * @return boolean
     */
    public boolean isItNonTrackingStatus(String stateType) {
        return StateType.getEnum(stateType) == StateType.TRADER_CONFIRMED
                || StateType.getEnum(stateType) == StateType.LENDER_REJECTED
                || StateType.getEnum(stateType) == StateType.TRADER_REJECTED
                || StateType.getEnum(stateType) == StateType.TRADER_PASSED
                || StateType.getEnum(stateType) == StateType.EXECUTION_IN_PROGRESS
                || StateType.getEnum(stateType) == StateType.PENDING_EXECUTION
                || StateType.getEnum(stateType) == StateType.EXECUTED
                || StateType.getEnum(stateType) == StateType.LENDER_TIMEOUT
                || StateType.getEnum(stateType) == StateType.TRADER_TIMEOUT
                || StateType.getEnum(stateType) == StateType.ERROR;
    }

    /**
     *
     *
     * @throws MBSBaseException
     */
    public void publishOnStartup() throws MBSBaseException {

        Iterator<Map.Entry<String, MBSTransactionRequest>> iterator = tradesToScan.entrySet().iterator();
        try {
            while (iterator.hasNext()) {
                Map.Entry<String, MBSTransactionRequest> entry = iterator.next();
                String key = entry.getKey();
                LOGGER.debug("TransactionEventService publishOnStartup key: " + key);
                MBSTransactionRequest obj = entry.getValue();
                if (obj != null) {
                    TransformationObject transformationObject = mbsObjectCreator.getTransformationObject();
                    transformationObject.setMBSRoleType(MBSRoleType.LENDER_AND_TRADER);
                    transformationObject.setTargetPojo(obj);
                    transactionRequestMessagePublisher.publish(transformationObject);
                } else {
                    LOGGER.debug("publishOnStartup : The retrieved transaction is empty.");
                }
            }
        } catch (Exception e) {
            MBSExceptionConstants.logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER, "",
                    "Failed to publish trades at startup ", "publishOnStartup", "", "");
            LOGGER.error("Failed publishOnStartup");
            MBSExceptionConstants.resetLogAlert();
            throw new MBSSystemException("Failed to publish trades at startup", MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
    }

    /**
     *
     * @param mbsTransactionRequest
     */
    public void updateTransToCrossRef(MBSTransactionRequest mbsTransactionRequest) {
        try {
            if (Objects.nonNull(mbsTransactionRequest)) {
                LOGGER.debug("MBSTransactionRequest data before adding to cross ref {}", mbsTransactionRequest);
                MBSProductTransactionXref mbsProductTransactionXref = new MBSProductTransactionXref(
                        mbsTransactionRequest.getProductNameCode(), mbsTransactionRequest.getTradeCouponRate(),
                        mbsTransactionRequest.getTradeSettlementDate());
                MBSProductTransactionXref mbsProductTransactionXrefResp = (MBSProductTransactionXref) mbsProductTransactionXrefDao
                        .getById(mbsProductTransactionXref.getId());
                Set<String> transReqNumberSet;
                if (Objects.isNull(mbsProductTransactionXrefResp)) {
                    transReqNumberSet = new HashSet<>();
                    transReqNumberSet.add(mbsTransactionRequest.getTransReqNumber());
                    mbsProductTransactionXref.setTransReqIdSet(transReqNumberSet);
                    mbsProductTransactionXrefDao.saveOrUpdate(mbsProductTransactionXref);
                    LOGGER.debug("Added new entry in Xref region for {}", mbsProductTransactionXref);
                } else {
                    transReqNumberSet = mbsProductTransactionXrefResp.getTransReqIdSet();
                    transReqNumberSet.add(mbsTransactionRequest.getTransReqNumber());
                    mbsProductTransactionXrefResp.setTransReqIdSet(transReqNumberSet);
                    mbsProductTransactionXrefDao.saveOrUpdate(mbsProductTransactionXrefResp);
                    LOGGER.debug("Updated entry in Xref region for {}", mbsProductTransactionXrefResp);
                }
            }
        } catch (Exception ex) {
            // Presently logging and warning, but need to raise an exception for
            // the same.
            LOGGER.warn("Exception when retrieving cross reference data when adding  for {} and exception is {} ",
                    mbsTransactionRequest, ex);
        }
    }

    /**
     *
     * @param mbsTransactionRequest
     */
    public void removeTransToCrossRef(MBSTransactionRequest mbsTransactionRequest) {
        try {
            if (Objects.nonNull(mbsTransactionRequest)) {
                LOGGER.debug("MBSTransactionRequest data before removing the transid to cross ref {}",
                        mbsTransactionRequest);
                MBSProductTransactionXref mbsProductTransactionXref = new MBSProductTransactionXref(
                        mbsTransactionRequest.getProductNameCode(), mbsTransactionRequest.getTradeCouponRate(),
                        mbsTransactionRequest.getTradeSettlementDate());
                MBSProductTransactionXref mbsProductTransactionXrefResp = (MBSProductTransactionXref) mbsProductTransactionXrefDao
                        .getById(mbsProductTransactionXref.getId());
                Set<String> transReqNumberSet;
                LOGGER.debug("MBSProductTransactionXref data before removing from region {}",
                        mbsProductTransactionXrefResp);
                if (Objects.isNull(mbsProductTransactionXrefResp)) {
                    LOGGER.debug("Unable to retrieve the trans details from cross ref data for key {}",
                            mbsProductTransactionXref);
                } else {
                    transReqNumberSet = mbsProductTransactionXrefResp.getTransReqIdSet();
                    transReqNumberSet.remove(mbsTransactionRequest.getTransReqNumber());
                    if (transReqNumberSet.size() == 0) {
                        mbsProductTransactionXrefDao.clear(mbsProductTransactionXrefResp.getId());
                        LOGGER.debug("removeTransToCrossRef method: removed the entry in region for key {}",
                                mbsProductTransactionXrefResp.getId());
                    } else {
                        mbsProductTransactionXrefResp.setTransReqIdSet(transReqNumberSet);
                        mbsProductTransactionXrefDao.saveOrUpdate(mbsProductTransactionXrefResp);
                        LOGGER.debug("removeTransToCrossRef method:  removed the transreqId for key {}",
                                mbsProductTransactionXrefResp.getId());
                    }
                }
            }
        } catch (Exception ex) {
            // Presently logging and warning, but need to raise an exception for
            // the same.
            LOGGER.warn("Exception when removing from cross reference data for {} ", mbsTransactionRequest);

        }

    }

    /**
     * 
     * 
     * @param obj
     * @param subscriber
     * @throws MBSBaseException
     */
    public void publishTransaction(MBSTransactionRequest obj, MBSRoleType subscriber) {

        try {
            if (obj != null) {
                LOGGER.info("MBS transaction request publish method call: subscriber ", obj, subscriber);
                TransformationObject transformationObject = mbsObjectCreator.getTransformationObject();
                transformationObject.setMBSRoleType(subscriber);
                transformationObject.setTargetPojo(obj);
                transactionRequestMessagePublisher.publish(transformationObject);
            } else {
                LOGGER.info("publishTransaction : The retrieved transaction is empty.");
            }
        } catch (Exception e) {
            MBSExceptionConstants.logItForAlert(MBSExceptionConstants.SYSTEM_EXCEPTION_IDENTIFIER, "",
                    "Failed to publish trades ", "publishTransaction", "", "");
            LOGGER.error("Failed publishTransaction", obj);
            MBSExceptionConstants.resetLogAlert();
            // throw new MBSSystemException("Failed to publish trades",
            // MBSExceptionConstants.SYSTEM_EXCEPTION);
        }

    }

    /**
     * 
     * @param obj
     * @throws MBSBaseException
     * 
     */
    // @Override
    public void processEvent(MBSTransactionRequest obj) throws MBSBaseException {

        if (isItNonTrackingStatus(obj.getStateType())) {
            // remove the key if status goes to terminal status
            LOGGER.debug("MBSCqListenerAdapter Tx terminal status so removed key" + obj.getTransReqNumber()
                    + "with status : " + obj.getStateType());
            MBSTransactionRequest mbsTransactionRequest = remove(obj.getTransReqNumber());
            // If the trade has already been removed, do not publish
            if (Objects.nonNull(mbsTransactionRequest)){
                // remove from cross ref region
                removeTransToCrossRef(obj);
                //Do not publish for timeouts as it is already being published from different section
                if(!(StateType.TRADER_TIMEOUT.equals(obj.getStateType())
                        || StateType.LENDER_TIMEOUT.equals(obj.getStateType()))) {
                        publishTransaction(obj, MBSRoleType.TRADER);
                }
            }
        } else {
            LOGGER.debug("MBSCqListenerAdapter Tx added key" + " transaction key " + obj.getTransReqNumber() + "with "
                    + "status : " + obj.getStateType());
            add(obj.getTransReqNumber(), obj);
            // Update cross ref region
            updateTransToCrossRef(obj);
            publishTransaction(obj, MBSRoleType.TRADER);
        }
    }

}
