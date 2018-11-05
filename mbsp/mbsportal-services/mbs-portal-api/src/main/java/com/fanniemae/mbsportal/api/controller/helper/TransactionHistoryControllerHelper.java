/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.api.controller.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.cdx.CDXClientApi;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementRolePO;
import com.fanniemae.mbsportal.api.po.TransactionHistoryPO;
import com.fanniemae.mbsportal.api.po.TransactionHistorySortPO;
import com.fanniemae.mbsportal.api.service.TransactionHistoryService;
import com.fanniemae.mbsportal.api.validator.TransactionHistoryPOValidator;
import com.fanniemae.mbsportal.constants.AcceptedTradesBoolean;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.RegionColumnList;
import com.fanniemae.mbsportal.gf.pojo.SortBy;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.service.MBSExceptionService;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.aws.AWSS3Service;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.DataExceptionMessage;
import com.fanniemae.mbsportal.utils.exception.ExceptionMessagesPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSDataException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 *
 * @author g8upjv
 *
 */
@Component
public class TransactionHistoryControllerHelper extends BaseControllerHelper {

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionHistoryControllerHelper.class);

    /**
     *
     * transHistServicev1 TransactionHistoryService
     */
    @Autowired
    private TransactionHistoryService transHistServicev1;

    @Autowired
    private MBSExceptionService mbsExceptionService;

    /**
     *
     * cdxClientApi CDXClientApi
     */
    @Autowired
    private CDXClientApi cdxClientApi;

    @Autowired
    private AWSS3Service s3Service;

    private static final String TRADER_FILENAME_PREDICATE = "MBS_Trade_History_FNMA_";

    private static final String LENDER_FILENAME_PREDICATE = "FNMA_MBS_Trade_History_";

    /**
     *
     * select the version based on the media type
     *
     * @param mediaType
     *            the mediaType
     * @return BaseProcessor
     */
    @SuppressWarnings("rawtypes")
    private BaseProcessor selectDataServiceByMediaType(String mediaType) {
        LOGGER.debug("Entering selectDataServiceByMediaType method in TransactionHistoryControllerHelper " + mediaType);
        if (mediaType.equals(MediaType.APPLICATION_JSON_VALUE) || mediaType.equals(getVersion1json().toString())) {
            return transHistServicev1;
        } else {
            return null;
        }
    }

    /**
     *
     * get the transaction history details in sorted order
     *
     * @param headers
     *            Map having header info and values
     * @param sortBy
     *            the sortBy
     * @param sortOrder
     *            the sortOrder
     * @param pageIndex
     *            the pageIndex
     * @param pageSize
     *            the pageSize
     * @return List<TransactionHistoryPO>
     * @throws MBSBaseException
     */
    @SuppressWarnings("rawtypes")
    public List<TransactionHistoryPO> getTransactionHistorySorted(Map<String, String> headers,
            AcceptedTradesBoolean acceptedTrades, RegionColumnList sortBy, SortBy sortOrder, Integer pageIndex,
            Integer pageSize, TransactionHistorySortPO transactionHistorySortPO) throws MBSBaseException {

        LOGGER.debug("Entering getTransactionHistorySorted method in TransactionHistoryControllerHelper");
        ProfileEntitlementPO profileEntitlementPO = getProfileEntitlementPO(headers);
        // CMMBSSTA01-1802 - input validate input params
        validateTransactionHistoryRequest(acceptedTrades, sortBy, sortOrder, pageIndex, pageSize,
                MBSPServiceConstants.TRANS_HISTORY);
        // CMMBSSTA01-1802 - input validation ends

        String mediaType = MediaType.APPLICATION_JSON_VALUE;
        List<TransactionHistoryPO> lstTransactionPO = new ArrayList<TransactionHistoryPO>();
        MBSRoleType mbsRoleType;

        String sellerServiceNumber = getSellerServiceNumber(profileEntitlementPO);
        boolean isTradeAccepted = acceptedTrades.getName().equals("true");
        LOGGER.debug("isTradeAccepted:" + isTradeAccepted);
        mbsRoleType = getMbsRoleType(profileEntitlementPO);
        BaseProcessor transReqService = selectDataServiceByMediaType(mediaType.trim());
        if (!Objects.equals(transReqService, null)) {
            lstTransactionPO = transHistServicev1.getTransactionRequestSorted(mbsRoleType, sellerServiceNumber,
                    isTradeAccepted, sortBy.name(), sortOrder.name(), pageIndex, pageSize);
            if (!CollectionUtils.isEmpty(lstTransactionPO)) {
                transactionHistorySortPO.setTotalRecords(transHistServicev1.getTransactionRequestSortedSize(mbsRoleType,
                        sellerServiceNumber, isTradeAccepted));
            } else {
                transactionHistorySortPO.setTotalRecords(0);
            }
        } else {
            throw new MBSSystemException(MBSPServiceConstants.INVALID_ACCEPT_HEADER,
                    MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        LOGGER.debug("Exiting getTransactionHistorySorted method in TransactionHistoryControllerHelper");
        return lstTransactionPO;
    }

    /**
     *
     * get the transaction history details for exporting to a file
     *
     * @param headers
     * @param acceptedTrades
     * @param sortBy
     * @param sortOrder
     * @param startDate
     * @param endDate
     * @param exportType
     * @param dateType
     * @param httpResponse
     * @return
     * @throws MBSBaseException
     */
    public String getTransactionHistoryExportBySort(Map<String, String> headers, AcceptedTradesBoolean acceptedTrades,
            RegionColumnList sortBy, SortBy sortOrder, String startDate, String endDate, String exportType,
            String dateType) throws MBSBaseException {

        LOGGER.debug("Entering getTransactionHistoryExportBySort method in TransactionHistoryControllerHelper");

        byte[] exportData = null;
        String fileName = null;
        String mediaType = MediaType.APPLICATION_JSON_VALUE;
        MBSRoleType mbsRoleType;

        ProfileEntitlementPO profileEntitlementPO = getProfileEntitlementPO(headers);
        String sellerServiceNumber = getSellerServiceNumber(profileEntitlementPO);

        List<ExceptionMessagesPO> excpMsgs = new ArrayList<ExceptionMessagesPO>();
        validateTransactionHistoryExportRequest(acceptedTrades, sortBy, sortOrder, startDate, endDate, exportType,
                dateType, MBSPServiceConstants.TRANS_HISTORY_EXPORT);

        mbsRoleType = getMbsRoleType(profileEntitlementPO);
        @SuppressWarnings("rawtypes")
        BaseProcessor transReqService = selectDataServiceByMediaType(mediaType.trim());
        if (!Objects.equals(transReqService, null)) {
            Map<String, String> map = new HashMap<>();
            map.put("acceptedTrades", acceptedTrades.getName());
            map.put("sortBy", sortBy.getSortName());
            map.put("sortOrder", sortOrder.name());
            map.put("dateStart", startDate);
            map.put("dateEnd", endDate);
            map.put("exportType", exportType);
            map.put("dateType", dateType);
            map.put("userName", profileEntitlementPO.getFirstName() + " " + profileEntitlementPO.getLastName());
            map.put("org", profileEntitlementPO.getDealerOrgName());
            exportData = transHistServicev1.getTransactionRequestExportSorted(mbsRoleType, sellerServiceNumber, map);

            String predicate = mbsRoleType.equals(MBSRoleType.TRADER) ? TRADER_FILENAME_PREDICATE
                    : LENDER_FILENAME_PREDICATE;
            String extension = exportType.equalsIgnoreCase("CSV") ? MBSPServiceConstants.CSV_EXTENSION
                    : MBSPServiceConstants.XLSX_EXTENSION;

            fileName = predicate + profileEntitlementPO.getUserName() + "_" + MBSPortalUtils.convertDateToString(
                    MBSPortalUtils.getCurrentDate(), DateFormats.DATE_FORMAT_NO_DASHES_WITH_TIMESTAMP) + extension;

            LOGGER.debug("Exiting getTransactionHistoryExportBySort method in TransactionHistoryControllerHelper");
            return s3Service.getUnSignedURL(fileName, exportData);
        } else {
            throw new MBSSystemException(MBSPServiceConstants.INVALID_ACCEPT_HEADER,
                    MBSExceptionConstants.SYSTEM_EXCEPTION);
        }

    }

    /**
     *
     * get the mbs role type based on the user profile information
     *
     * @param profileEntitlementPO
     * @return
     */
    private MBSRoleType getMbsRoleType(ProfileEntitlementPO profileEntitlementPO) {
        MBSRoleType mbsRoleType;
        // CMMBSSTA01-1371 - Changes for TSP - Start
        if (profileEntitlementPO.isTspUser()) {
            mbsRoleType = MBSRoleType.TSP;
        } else if (profileEntitlementPO.isFannieMaeUser()) {
            mbsRoleType = MBSRoleType.TRADER;
        } else {
            mbsRoleType = MBSRoleType.LENDER;
        }
        // CMMBSSTA01-1371 - Changes for TSP - End
        return mbsRoleType;
    }

    /**
     *
     * get the seller service number from the user profile information
     *
     * @param profileEntitlementPO
     * @return
     */
    private String getSellerServiceNumber(ProfileEntitlementPO profileEntitlementPO) {
        // CMMBSSTA01-1373 - Adding TSP name - Start
        String sellerServiceNumber = StringUtils.EMPTY;
        sellerServiceNumber = profileEntitlementPO.getSellerServicerNumber();
        // CMMBSSTA01-1373 - Adding TSP name - End
        return sellerServiceNumber;
    }

    /**
     *
     * get the user profile information from the request headers
     *
     * @param headers
     * @return
     * @throws MBSBaseException
     * @throws MBSBusinessException
     */
    private ProfileEntitlementPO getProfileEntitlementPO(Map<String, String> headers)
            throws MBSBaseException, MBSBusinessException {
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        ProfileEntitlementPO profileEntitlementPO = cdxClientApi.getProfileEntitlementPO(headers, null);
        if (Objects.equals(profileEntitlementPO, null)) {
            LOGGER.error("Failure to retrieve profile");
            throw new MBSBusinessException("Failed to retrieve profile", MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        List<ProfileEntitlementRolePO> profEntRoleLst = profileEntitlementPO.getRoles();
        if (CollectionUtils.isEmpty(profEntRoleLst)) {
            LOGGER.error("Failure to retrieve roles in Profile");
            throw new MBSBusinessException("Failed to retrieve roles in Profile",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        return profileEntitlementPO;
    }

    /**
     *
     * @param acceptedTrades
     * @param sortBy
     * @param sortOrder
     * @param pageIndex
     * @param pageSize
     * @param feature
     * @throws MBSBaseException
     * @throws MBSDataException
     */
    private void validateTransactionHistoryRequest(AcceptedTradesBoolean acceptedTrades, RegionColumnList sortBy,
            SortBy sortOrder, Integer pageIndex, Integer pageSize, String feature) throws MBSBaseException {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<DataExceptionMessage>();

        TransactionHistoryPOValidator.validateAcceptedTrades(dataExceptionMessages, acceptedTrades, feature);
        TransactionHistoryPOValidator.validateSortField(dataExceptionMessages, sortBy, feature);
        TransactionHistoryPOValidator.validateSortOrder(dataExceptionMessages, sortOrder, feature);
        if (feature.equalsIgnoreCase(MBSPServiceConstants.TRANS_HISTORY)) {
            TransactionHistoryPOValidator.validatePageIndex(dataExceptionMessages, pageIndex, feature);
            TransactionHistoryPOValidator.validatePageSize(dataExceptionMessages, pageSize, feature);
        }
        if (!CollectionUtils.isEmpty(dataExceptionMessages)) {
            mbsExceptionService.createBusinessExceptionsAndLog(new String("EXPORT_API"), dataExceptionMessages, null);
        }
    }

    private void validateTransactionHistoryExportRequest(AcceptedTradesBoolean acceptedTrades, RegionColumnList sortBy,
            SortBy sortOrder, String startDate, String endDate, String exportType, String dateType, String feature)
            throws MBSBaseException {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<DataExceptionMessage>();

        TransactionHistoryPOValidator.validateAcceptedTrades(dataExceptionMessages, acceptedTrades, feature);
        TransactionHistoryPOValidator.validateSortField(dataExceptionMessages, sortBy, feature);
        TransactionHistoryPOValidator.validateSortOrder(dataExceptionMessages, sortOrder, feature);
        if (MBSPServiceConstants.TRANS_HISTORY_EXPORT.equalsIgnoreCase(feature)) {
            TransactionHistoryPOValidator.validateExportType(dataExceptionMessages, exportType, feature);
            TransactionHistoryPOValidator.validateDateType(dataExceptionMessages, dateType, feature);
            TransactionHistoryPOValidator.validateDates(dataExceptionMessages, startDate, endDate, feature);
        }
        if (!CollectionUtils.isEmpty(dataExceptionMessages)) {
            mbsExceptionService.createBusinessExceptionsAndLog(new String("EXPORT_API"), dataExceptionMessages, null);
        }
    }
}