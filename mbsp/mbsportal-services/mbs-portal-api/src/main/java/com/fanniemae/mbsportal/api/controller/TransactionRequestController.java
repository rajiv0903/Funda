/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.api.controller;

import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fanniemae.mbsportal.api.controller.helper.TransactionRequestControllerHelper;
import com.fanniemae.mbsportal.api.controller.interceptor.entitlement.EntitlementRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.RequestHeaderRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.ResponseHeaderRequired;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * Class Name: TransactionRequestController Purpose : This class is the
 * controller for the Lenders transaction related interactions
 *
 * @author g8upjv
 *
 */
@RestController
public class TransactionRequestController extends BaseController {

    @PostConstruct
    public void init() {
        LOGGER.info("Transaction Request Controller Started");
    }

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionRequestController.class);
    /**
     *
     * transactionRequestControllerHelper TransactionRequestControllerHelper
     */
    @Autowired
    TransactionRequestControllerHelper transactionRequestControllerHelper;

    /**
     *
     * CLASS_NAME String
     */
    public static final String CLASS_NAME = "TransactionRequestController";

    /**
     *
     * Purpose: This method calls the TransactionRequestService's create method
     * to create Transaction Request for version1
     *
     * @param transactionRequestPO
     *            the transactionRequestPO
     * @param headers
     *            Map having headers and values
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = { "/mbslendertransactions",
            "/capital-markets/trading/securities/mbs/lendertransactions" }, method = RequestMethod.POST, produces = {
                    MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.LENDER_TRADE_EXECUTE, EntitlementRole.ADMIN,
            EntitlementRole.MBSP_FM_ADMIN_HE, EntitlementRole.MBSP_FM_ADMIN_LE, EntitlementRole.TSP_TRADE_EXECUTE })
    @ResponseHeaderRequired
    public ResponseEntity<Object> createTransactionRequestLender(@RequestBody TransactionRequestPO transactionRequestPO,
            @RequestHeader Map<String, String> headers) {

        LOGGER.debug("Entering createTransactionRequestLenderId method in TransactionRequestController");

        TransactionRequestPO transReqPOResponse = null;
        try {

            transReqPOResponse = transactionRequestControllerHelper.createTransactionRequestLender(transactionRequestPO,
                    headers);
        } catch (MBSBaseException ex) {
            LOGGER.error("Failed to create Transaction in the service call" + ex);
            LOGGER.error("Exiting createTransactionRequestLenderId method in TransactionRequestController");
            return getResponseEntityException(CLASS_NAME, "createTransactionRequestLender", ex);
            
        } catch (Exception ex) {
            LOGGER.error("Exiting createTransactionRequestLenderId method in TransactionRequestController");
            return getResponseEntityInternalServerException(CLASS_NAME, "createTransactionRequestLender", ex);
        }
        LOGGER.debug("Exiting createTransactionRequestLenderId method in TransactionRequestController");
        return ResponseEntity.ok(transReqPOResponse);
    }

    /**
     *
     * Purpose: This method updates the acceptance of the price by lender
     *
     * @param transReqId
     *            the transReqId
     * @param transactionRequestPO
     *            the transactionRequestPO
     * @param headers
     *            Map having headers and values
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = { "/mbslendertransactions/{transreqid}" }, method = RequestMethod.PUT, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.LENDER_TRADE_EXECUTE, EntitlementRole.ADMIN,
            EntitlementRole.MBSP_FM_ADMIN_HE, EntitlementRole.MBSP_FM_ADMIN_LE, EntitlementRole.TSP_TRADE_EXECUTE })
    @ResponseHeaderRequired
    public ResponseEntity<Object> acceptRejectPriceLender(@PathVariable(value = "transreqid") String transReqId,
            @RequestBody TransactionRequestPO transactionRequestPO, @RequestHeader Map<String, String> headers) {

        TransactionRequestPO transactionResponsePO = null;
        LOGGER.debug("Entering acceptRejectPriceLender method in TransactionRequestController" + transReqId);
        try {
            transactionResponsePO = transactionRequestControllerHelper.acceptRejectPriceLender(transReqId,
                    transactionRequestPO, headers);

        } catch (MBSBaseException ex) {
            LOGGER.error("Exiting acceptRejectPriceLender method in TransactionRequestController");
            return getResponseEntityException(CLASS_NAME, "acceptRejectPriceLender", ex);
            
        } catch (Exception ex) {            
            LOGGER.error("Exiting acceptRejectPriceLender method in TransactionRequestController");
            return getResponseEntityInternalServerException(CLASS_NAME, "acceptRejectPriceLender", ex);            
        }
        LOGGER.debug("Exiting acceptRejectPriceLender method in TransactionRequestController");
        return ResponseEntity.ok(transactionResponsePO);
    }

    /**
     *
     * Purpose: This method clears all the records in TransactionRequest
     *
     * @throws MBSBaseException
     */
    @RequestMapping(value = "/mbscleartransactions", method = RequestMethod.DELETE, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @EntitlementRequired(roles = { EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE })
    @ResponseHeaderRequired
    public void clearAllTransactionRequests() throws MBSBaseException {

        LOGGER.debug("Entering clearAllTransactionRequests method in TransactionRequestController");
        transactionRequestControllerHelper.clearAllTransactionRequests();
        LOGGER.debug("Exiting clearAllTransactionRequests method in TransactionRequestController");
    }

    /**
     *
     * Trader submits updates
     *
     * @param transReqId
     *            the transReqId
     * @param transactionRequestPO
     *            the transactionRequestPO
     * @param headers
     *            Map having headers and values
     * @return ResponseEntity<Object>
     * @throws MBSBaseException
     */
    @RequestMapping(value = { "/mbstradertransactions/{transreqid}" }, method = RequestMethod.PUT, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.TRADER_TRADE_EXECUTE, EntitlementRole.ADMIN,
            EntitlementRole.MBSP_FM_TRADER_EXECUTE_HE, EntitlementRole.MBSP_FM_TRADER_EXECUTE_LE,
            EntitlementRole.MBSP_FM_ADMIN_HE, EntitlementRole.MBSP_FM_ADMIN_LE })
    @ResponseHeaderRequired
    public ResponseEntity<Object> submitPriceRequest(@PathVariable(value = "transreqid") String transReqId,
            @RequestBody TransactionRequestPO transactionRequestPO, @RequestHeader Map<String, String> headers)
            throws MBSBaseException {

        LOGGER.debug("Entering submitPriceRequest method in TransactionRequestController" + transReqId);

        TransactionRequestPO transactionResponsePO = null;
        try {
            transactionResponsePO = transactionRequestControllerHelper.submitPriceRequest(transReqId,
                    transactionRequestPO, headers);

        } catch (MBSBaseException ex) {
            LOGGER.error("Exiting submitPriceRequest method in TransactionRequestController");
            return getResponseEntityException(CLASS_NAME, "submitPriceRequest", ex);

        } catch (Exception ex) {
            LOGGER.error("Exiting submitPriceRequest method in TransactionRequestController");
            return getResponseEntityInternalServerException(CLASS_NAME, "submitPriceRequest", ex);

        }
        LOGGER.debug("Exiting submitPriceRequest method in TransactionRequestController" + transReqId);
        return ResponseEntity.ok(transactionResponsePO);
    }

    /**
     *
     * Lender submits updates
     *
     * @param transreqid
     *            the transreqid
     * @param headers
     *            Map having headers and values
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = { "/mbslendertransactions", "/mbslendertransactions/{transreqid}",
            "/capital-markets/trading/securities/mbs/lendertransactions",
            "/capital-markets/trading/securities/mbs/lendertransactions/{transreqid}" }, method = RequestMethod.GET, produces = {
                    MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.LENDER_TRADE_EXECUTE, EntitlementRole.ADMIN,
            EntitlementRole.MBSP_FM_ADMIN_HE, EntitlementRole.MBSP_FM_ADMIN_LE, EntitlementRole.TSP_TRADE_EXECUTE })
    @ResponseHeaderRequired
    public ResponseEntity<Object> getTransactionRequestsLender(@PathVariable Optional<String> transreqid,
            @RequestHeader Map<String, String> headers) {
        LOGGER.debug("Entering getTransactionRequestsLender method in TransactionRequestController");

        Object obj;
        try {
            obj = transactionRequestControllerHelper.getTransactionRequestsLender(transreqid, headers);

        } catch (MBSBaseException ex) {
            LOGGER.error("Exiting getTransactionRequestsLender method in TransactionRequestController");
            return getResponseEntityException(CLASS_NAME, "getTransactionRequestsLender", ex);

        } catch (Exception ex) {
            LOGGER.error("Exiting getTransactionRequestsLender method in TransactionRequestController");
            return getResponseEntityInternalServerException(CLASS_NAME, "getTransactionRequestsLender", ex);
            
        }
        LOGGER.debug("Exiting getTransactionRequestsLender method in TransactionRequestController");
        return ResponseEntity.ok(obj);
    }

    /**
     *
     * Get the transaction requests
     *
     * @param transreqid
     * @return ResponseEntity<Object>
     * @throws MBSBaseException
     */
    @RequestMapping(value = { "/mbstradertransactions",
            "/mbstradertransactions/{transreqid}" }, method = RequestMethod.GET, produces = {
                    MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.TRADER_TRADE_EXECUTE, EntitlementRole.ADMIN,
            EntitlementRole.MBSP_FM_TRADER_EXECUTE_HE, EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_TRADER_EXECUTE_LE, EntitlementRole.MBSP_FM_ADMIN_LE,
            EntitlementRole.MBSP_FM_TRADER_READ_ONLY_LE, EntitlementRole.MBSP_FM_TRADER_READ_ONLY_HE
    })
    @ResponseHeaderRequired
    public ResponseEntity<Object> getTransactionRequestsTrader(@PathVariable Optional<String> transreqid)
            throws MBSBaseException {
        LOGGER.debug("Entering getTransactionRequestsTrader method in TransactionRequestController");

        Object obj;
        try {
            obj = transactionRequestControllerHelper.getTransactionRequestsTrader(transreqid);
        } catch (MBSBaseException ex) {
            LOGGER.error("Exiting getTransactionRequestsTrader method in TransactionRequestController");
            return getResponseEntityException(CLASS_NAME, "getTransactionRequestsTrader", ex);

        } catch (Exception ex) {
            LOGGER.error("Exiting getTransactionRequestsTrader method in TransactionRequestController");
            return getResponseEntityInternalServerException(CLASS_NAME, "getTransactionRequestsTrader", ex);
            
        }
        LOGGER.debug("Exiting getTransactionRequestsTrader method in TransactionRequestController");
        return ResponseEntity.ok(obj);
    }

}
