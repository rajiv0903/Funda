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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fanniemae.mbsportal.api.controller.helper.TransactionHistoryControllerHelper;
import com.fanniemae.mbsportal.api.controller.interceptor.entitlement.EntitlementRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.RequestHeaderRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.ResponseHeaderRequired;
import com.fanniemae.mbsportal.api.po.TransactionHistoryPO;
import com.fanniemae.mbsportal.api.po.TransactionHistorySortPO;
import com.fanniemae.mbsportal.constants.AcceptedTradesBoolean;
import com.fanniemae.mbsportal.constants.RegionColumnList;
import com.fanniemae.mbsportal.gf.pojo.SortBy;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSDataException;

/**
 * Class Name: TransactionHistoryController Purpose : This class is the
 * controller for the Lenders transaction related interactions
 *
 * @author g8upjv
 *
 */
@RestController
public class TransactionHistoryController extends BaseController {

    @PostConstruct
    public void init() {
        LOGGER.info("Transaction History Controller Started");
    }

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionHistoryController.class);

    /**
     *
     * transactionHistoryControllerHelper TransactionHistoryControllerHelper
     */
    @Autowired
    private TransactionHistoryControllerHelper transactionHistoryControllerHelper;

    /**
     *
     * CLASS_NAME String
     */
    public static final String CLASS_NAME = "TransactionHistoryController";

    /**
     *
     * This method retrieve the transaction request objects (sorted by column
     * name passed by) by calling the service
     *
     * @param headers
     *            the headers
     * @param sortBy
     *            the sortBy
     * @param sortOrder
     *            the sortOrder
     * @param pageIndex
     *            the pageIndex
     * @param pageSize
     *            the pageSize
     * @return ResponseEntity<Object>
     * @throws MBSBaseException
     */
    @RequestMapping(value = { "/transactionshistory/{acceptedTrades}/{sortBy}/{sortOrder}/{pageIndex}/{pageSize}",
            "/capital-markets/trading/securities/mbs/transactionshistory/{acceptedTrades}/{sortBy}/{sortOrder}/{pageIndex}/{pageSize}" }, method = RequestMethod.GET, produces = {
                    MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.LENDER_TRADE_EXECUTE, EntitlementRole.TRADER_TRADE_EXECUTE,
            EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_TRADER_EXECUTE_HE, EntitlementRole.MBSP_FM_TRADER_EXECUTE_LE,
            EntitlementRole.MBSP_FM_ADMIN_HE, EntitlementRole.MBSP_FM_ADMIN_LE, EntitlementRole.TSP_TRADE_EXECUTE,
            // CMMBSSTA01-882 - Read Only User access - Start
            EntitlementRole.MBSP_FM_TRADER_READ_ONLY_LE, EntitlementRole.MBSP_FM_TRADER_READ_ONLY_HE
            // CMMBSSTA01-882 - Read Only User access - End
    })
    @ResponseHeaderRequired
    public ResponseEntity<Object> getTransactionHistoryBySortAndPage(@RequestHeader Map<String, String> headers,
            @PathVariable(value = "acceptedTrades") AcceptedTradesBoolean acceptedTrades,
            @PathVariable(value = "sortBy") RegionColumnList sortBy,
            @PathVariable(value = "sortOrder") SortBy sortOrder, @PathVariable(value = "pageIndex") Integer pageIndex,
            @PathVariable(value = "pageSize") Integer pageSize) throws MBSBaseException {

        LOGGER.info("getTransactionHistoryBySortAndPage method in TransactionHistoryController with"
                + " values {},{},{},{},{}", acceptedTrades, sortBy, sortOrder, pageIndex, pageSize);
        TransactionHistorySortPO transactionHistorySortPO = new TransactionHistorySortPO();

        List<TransactionHistoryPO> lstTransactionPO = new ArrayList<TransactionHistoryPO>();
        try {
            LOGGER.debug("calling helper class ....");
            lstTransactionPO = transactionHistoryControllerHelper.getTransactionHistorySorted(headers, acceptedTrades,
                    sortBy, sortOrder, pageIndex, pageSize, transactionHistorySortPO);
            transactionHistorySortPO.setSortBy(sortBy.name());
            transactionHistorySortPO.setSortOrder(sortOrder.name());
            transactionHistorySortPO.setPageIndex(pageIndex);
            transactionHistorySortPO.setPageSize(pageSize);
            // LOGGER.debug("sortby name:" + sortBy.getSortName());
            transactionHistorySortPO.setList(lstTransactionPO);
        } catch (MBSBaseException ex) {
            LOGGER.error("Exiting getTransactionHistoryBySortAndPage method in TransactionHistoryController");
            return getResponseEntityException(CLASS_NAME, "getTransactionHistoryBySortAndPage", ex);
        } catch (Exception ex) {
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- Start
            LOGGER.error("Exiting getTransactionHistoryBySortAndPage method in TransactionHistoryController");
            return getResponseEntityInternalServerException(CLASS_NAME, "getTransactionHistoryBySortAndPage", ex);
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- End

        }
        LOGGER.debug("Exiting getTransactionHistoryBySortAndPage method in TransactionHistoryController");
        return ResponseEntity.ok(transactionHistorySortPO);
    }

    /**
     *
     * This method exports the transaction history data to a csv or xls file
     * format for both traders and lenders
     *
     * @param headers
     *            the headers
     * @param sortBy
     *            the sortBy
     * @param sortOrder
     *            the sortOrder
     * @param pageIndex
     *            the pageIndex
     * @param pageSize
     *            the pageSize
     * @return ResponseEntity<Object>
     * @throws MBSBaseException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value = {
            "/transactionshistory/export/{acceptedTrades}/{sortBy}/{sortOrder}/{startDate}/{endDate}/{exportType}/{dateType}" }, method = RequestMethod.GET, produces = {
                    MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.LENDER_TRADE_EXECUTE, EntitlementRole.TRADER_TRADE_EXECUTE,
            EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_TRADER_EXECUTE_HE, EntitlementRole.MBSP_FM_TRADER_EXECUTE_LE,
            EntitlementRole.MBSP_FM_ADMIN_HE, EntitlementRole.MBSP_FM_ADMIN_LE, EntitlementRole.TSP_TRADE_EXECUTE,
            // CMMBSSTA01-882 - Read Only User access - Start
            EntitlementRole.MBSP_FM_TRADER_READ_ONLY_LE, EntitlementRole.MBSP_FM_TRADER_READ_ONLY_HE
            // CMMBSSTA01-882 - Read Only User access - End
    })
    @ResponseHeaderRequired
    public ResponseEntity<Object> getTransactionHistoryExportBySort(@RequestHeader Map<String, String> headers,
            @PathVariable(value = "acceptedTrades") AcceptedTradesBoolean acceptedTrades,
            @PathVariable(value = "sortBy") RegionColumnList sortBy,
            @PathVariable(value = "sortOrder") SortBy sortOrder, @PathVariable(value = "startDate") String startDate,
            @PathVariable(value = "endDate") String endDate, @PathVariable(value = "exportType") String exportType,
            @PathVariable(value = "dateType") String dateType, HttpServletResponse httpResponse)
            throws MBSBaseException {
        LOGGER.info(
                "Entering getTransactionHistoryExportBySort Export API in TransactionHistoryController with"
                        + " values {},{},{},{},{},{},{}",
                acceptedTrades, sortBy, startDate, endDate, sortOrder, exportType, dateType);
        LOGGER.debug(
                "Entering getTransactionHistoryExportBySort method in TransactionHistoryController with"
                        + " values {},{},{},{},{},{},{}",
                acceptedTrades, sortBy, startDate, endDate, sortOrder, exportType, dateType);
        String unsignedUrl = null;
        final int fileCount = 1;
        UUID requestId = UUID.randomUUID();
        try {

            String cdxTraceId = headers.get("x-fnma-trace-id");
            LOGGER.debug("cdxTraceId:" + cdxTraceId);

            unsignedUrl = transactionHistoryControllerHelper.getTransactionHistoryExportBySort(headers, acceptedTrades,
                    sortBy, sortOrder, startDate, endDate, exportType, dateType);

            httpResponse.setHeader("x-cp-request-id", requestId.toString());
            httpResponse.setIntHeader("x-cp-s3file-count", fileCount);
            httpResponse.setHeader("x-cp-s3files", unsignedUrl);
            httpResponse.setStatus(200);
        } catch (MBSDataException ex) {
            LOGGER.error("Exiting getTransactionHistoryExportBySort Export API method in TransactionHistoryController");
            return getResponseEntityDataException(CLASS_NAME, "getTransactionHistoryExportBySort", ex);
        } catch (MBSBaseException ex) {
            LOGGER.error("Exiting getTransactionHistoryExportBySort Export API method in TransactionHistoryController");
            return getResponseEntityException(CLASS_NAME, "getTransactionHistoryExportBySort", ex);
        } catch (Exception ex) {
            LOGGER.error("Exiting getTransactionHistoryExportBySort Export API method in TransactionHistoryController");
            return getResponseEntityInternalServerException(CLASS_NAME, "getTransactionHistoryExportBySort", ex);

        }
        LOGGER.debug("Exiting getTransactionHistoryExportBySort Export API method in TransactionHistoryController");

        return new ResponseEntity(HttpStatus.OK);
    }
}
