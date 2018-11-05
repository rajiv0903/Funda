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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.cdx.CDXClientApi;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.api.po.TspPartyLenderPO;
import com.fanniemae.mbsportal.api.service.TransactionRequestService;
import com.fanniemae.mbsportal.api.utils.MBSUtils;
import com.fanniemae.mbsportal.aspect.exception.ExceptionTracingAnnotation;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;

/**
 * @author gaur5c
 */
@Component
public class TransactionRequestControllerHelper extends BaseControllerHelper {

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionRequestControllerHelper.class);

    /**
     *
     * transReqServicev1 TransactionRequestService
     */
    @Autowired
    private TransactionRequestService transReqServicev1;

    /**
     *
     * cdxClientApi CDXClientApi
     */
    @Autowired
    private CDXClientApi cdxClientApi;

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
            return transReqServicev1;
        } else {
            return null;
        }
    }

    /**
     *
     * create transaction request for lender
     *
     * @param transactionRequestPO
     *            the transactionRequestPO
     * @param headers
     *            Map having header info and values
     * @return TransactionRequestPO
     * @throws MBSBaseException
     */
    @SuppressWarnings("rawtypes")
    @ExceptionTracingAnnotation
    public TransactionRequestPO createTransactionRequestLender(TransactionRequestPO transactionRequestPO,
            Map<String, String> headers) throws MBSBaseException {
        LOGGER.debug("Entering createTransactionRequestLender method in TransactionRequestControllerHelper");
        String mediaType = MediaType.APPLICATION_JSON_VALUE;
        String lenderName;
        TransactionRequestPO transReqPOResponse;
        MBSRoleType mbsRoleType = MBSRoleType.LENDER;

        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        ProfileEntitlementPO profileEntitlementPO = cdxClientApi.getProfileEntitlementPO(headers, null);
        if (Objects.equals(profileEntitlementPO, null)) {
            LOGGER.error("Failure to retrieve profile");
            throw new MBSBusinessException("Failed to retrieve profile", MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        // CMMBSSTA01-1371 - Changes for TSP - Start
        if (profileEntitlementPO.isTspUser()) {
            mbsRoleType = MBSRoleType.TSP;
        }
        if (profileEntitlementPO.isFannieMaeUser()) {
            LOGGER.error("Trader user cannot create trades - user :" + profileEntitlementPO.getUserName());
            throw new MBSBusinessException(
                    "Trader user cannot create trades - user :" + profileEntitlementPO.getUserName(),
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        // CMMBSSTA01-1371 - Changes for TSP - End
        lenderName = profileEntitlementPO.getUserName();
        // Select the required version of the service object
        if (!Objects.equals(transactionRequestPO, null)) {
            BaseProcessor transReqService = selectDataServiceByMediaType(mediaType.trim());

            if (!Objects.equals(transReqService, null)) {
                // Over-riding the lender id in the request with the lender id
                // from user profile
                transactionRequestPO.setLenderId(lenderName);

                /*
                 * CMMBSSTA01-1022: (Tech) Maintain versions for transaction
                 * request Backward Compatibility
                 */
                Long existingValueFromUser = transactionRequestPO.getActiveVersion();
                if (Objects.isNull(existingValueFromUser)) {

                    transactionRequestPO.setActiveVersion(1L);
                }
                // End of Backward Compatibility

                // CMMBSSTA01-1371 - Changes for TSP - Start
                if (profileEntitlementPO.isTspUser()
                        && StringUtils.isNotBlank(transactionRequestPO.getOboLenderSellerServicerNumber())) {
                    transactionRequestPO.setTspShortName(profileEntitlementPO.getPartyShortName());
                    boolean validLender = false;
                    List<TspPartyLenderPO> tspLenders = profileEntitlementPO.getTspLenders();
                    if (Objects.nonNull(tspLenders)) {
                        for (TspPartyLenderPO tspPartyLenderPO : tspLenders) {
                            if (transactionRequestPO.getOboLenderSellerServicerNumber()
                                    .equalsIgnoreCase(tspPartyLenderPO.getLenderSellerServicerNumber())) {
                                // Setting the lender party short name for TSP
                                transactionRequestPO.setLenderEntityShortName(tspPartyLenderPO.getName());
                                validLender = true;
                                break;
                            }
                        }
                    }
                    if (!validLender) {
                        LOGGER.error("For the TSP user :" + profileEntitlementPO.getUserName()
                                + " cannot do trade on behalf of lender seller service number : "
                                + transactionRequestPO.getOboLenderSellerServicerNumber());
                        throw new MBSBusinessException(
                                "For the TSP user :" + profileEntitlementPO.getUserName()
                                        + " cannot do trade on behalf of lender seller service number : "
                                        + transactionRequestPO.getOboLenderSellerServicerNumber(),
                                MBSExceptionConstants.BUSINESS_EXCEPTION);
                    }
                }
                // CMMBSSTA01-1371 - Changes for TSP - End
                transReqPOResponse = ((TransactionRequestService) transReqService)
                        .createMBSTransReq(transactionRequestPO, mbsRoleType, headers);

            } else {
                LOGGER.error(
                        "Failed to create Transaction, transReqService object is null, wrong Accept header parameter");
                throw new MBSBusinessException(
                        "Failed to create Transaction, transReqService object is null, wrong Accept header parameter",
                        MBSExceptionConstants.BUSINESS_EXCEPTION);

            }
        } else {
            LOGGER.error("Failed to create Transaction, transactionRequestPO(input) object is null");
            throw new MBSBusinessException("Failed to create Transaction, transactionRequestPO(input) object is null",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);

        }
        LOGGER.debug("Exiting createTransactionRequestLender method in TransactionRequestControllerHelper");
        return transReqPOResponse;
    }

    /**
     *
     * Hit/Lift price for lender
     *
     * @param transReqId
     *            the transReqId
     * @param transactionRequestPO
     *            the transactionRequestPO
     * @param headers
     *            Map having header info and values
     * @return TransactionRequestPO
     * @throws MBSBaseException
     */
    @SuppressWarnings("rawtypes")
    @ExceptionTracingAnnotation
    public TransactionRequestPO acceptRejectPriceLender(String transReqId, TransactionRequestPO transactionRequestPO,
            Map<String, String> headers) throws MBSBaseException {
        LOGGER.debug("Entering acceptRejectPriceLender method in TransactionRequestControllerHelper");
        String mediaType = MediaType.APPLICATION_JSON_VALUE;
        String lenderName;
        MBSRoleType mbsRoleType = MBSRoleType.LENDER;

        if (!Objects.equals(transactionRequestPO, null)) {

            // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
            ProfileEntitlementPO profileEntitlementPO = cdxClientApi.getProfileEntitlementPO(headers, null);

            if (!Objects.equals(profileEntitlementPO, null)) {
                lenderName = profileEntitlementPO.getUserName();

            } else {
                LOGGER.error("Failed to retrieve lender profile");
                throw new MBSBusinessException("Failed to retrieve lender profile",
                        MBSExceptionConstants.BUSINESS_EXCEPTION);
            }
            // CMMBSSTA01-1371 - Changes for TSP - Start
            if (profileEntitlementPO.isTspUser()) {
                mbsRoleType = MBSRoleType.TSP;
            }
            // CMMBSSTA01-1371 - Changes for TSP - End
            LOGGER.debug("TransactionRequestController: acceptRejectPriceLender: lenderid: " + lenderName);
            BaseProcessor transReqService = selectDataServiceByMediaType(mediaType.trim());
            // get the status and see if it is valid
            if (!Objects.equals(transReqService, null)) {
                transactionRequestPO.setTransReqId(transReqId);
                // Over-riding the lender id in the request with the lender id
                // from user profile
                transactionRequestPO.setLenderId(lenderName);
                transactionRequestPO = ((TransactionRequestService) transReqService)
                        .updateMBSTransReq(transactionRequestPO, mbsRoleType, headers);

            } else {
                LOGGER.error(
                        "Failed to create Transaction, transReqService object is null, wrong Accept header parameter");
                throw new MBSBusinessException(
                        "Failed to create Transaction, transReqService object is null, wrong Accept header parameter",
                        MBSExceptionConstants.BUSINESS_EXCEPTION);

            }
        } else {
            LOGGER.error(
                    "Failed to update Transaction for lender accept/reject, transactionRequestPO(input) object is null");
            throw new MBSBusinessException(
                    "Failed to update Transaction for lender accept/reject, transactionRequestPO(input) object is null",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);

        }
        LOGGER.debug("Exiting acceptRejectPriceLender method in TransactionRequestControllerHelper");
        return transactionRequestPO;
    }

    /**
     *
     * Clears all the transactions
     *
     * @throws MBSBaseException
     */
    public void clearAllTransactionRequests() throws MBSBaseException {
        LOGGER.debug("Entering clearAllTransactionRequests method in TransactionRequestControllerHelper");
        transReqServicev1.clearAll();
        LOGGER.debug("Exiting clearAllTransactionRequests method in TransactionRequestControllerHelper");
    }

    /**
     *
     * Submit price/re-price for trader
     *
     * @param transReqId
     *            the transReqId
     * @param transactionRequestPO
     *            the transactionRequestPO
     * @param headers
     *            Map having header info and values
     * @return TransactionRequestPO
     * @throws MBSBaseException
     */
    @SuppressWarnings("rawtypes")
    @ExceptionTracingAnnotation
    public TransactionRequestPO submitPriceRequest(String transReqId, TransactionRequestPO transactionRequestPO,
            Map<String, String> headers) throws MBSBaseException {
        LOGGER.debug("Entering submitPriceRequest method in TransactionRequestControllerHelper");
        String mediaType = MediaType.APPLICATION_JSON_VALUE;
        String traderName;
        TransactionRequestPO transactionRequestPOResponse;

        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        ProfileEntitlementPO profileEntitlementPO = cdxClientApi.getProfileEntitlementPO(headers, MBSRoleType.TRADER);

        if (!Objects.equals(profileEntitlementPO, null)) {
            traderName = profileEntitlementPO.getUserName();

        } else {
            LOGGER.error("Failed to retrieve trader profile");
            // Message
            throw new MBSBusinessException("Failed to retrieve trader profile",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);

        }
        BaseProcessor transReqService = selectDataServiceByMediaType(mediaType.trim());
        // get the status and see if it is valid
        if (null != transactionRequestPO) {
            transactionRequestPO.setTransReqId(transReqId);
            // Logic for soft lock of the transaction by trader - Start
            if (transactionRequestPO.getStateType().equals(StateType.LENDER_OPEN)) {
                if (StringUtils.isEmpty(transactionRequestPO.getTraderId())) {
                    transactionRequestPO.setTraderId(null);
                } else {
                    if (transactionRequestPO.getTraderId().equalsIgnoreCase(traderName)) {
                        transactionRequestPO.setTraderId(traderName);
                    } else {
                        LOGGER.error(MBSPServiceConstants.INVALID_TRADER_ID + " Input id : "
                                + transactionRequestPO.getTraderId() + " and user id : " + traderName);
                        throw new MBSBusinessException(MBSPServiceConstants.INVALID_TRADER_ID,
                                MBSExceptionConstants.BUSINESS_EXCEPTION);
                    }
                }
            } else {
                transactionRequestPO.setTraderId(traderName);
            }
            // Logic for soft lock of the transaction by trader - End
            transactionRequestPOResponse = ((TransactionRequestService) transReqService)
                    .updateMBSTransReq(transactionRequestPO, MBSRoleType.TRADER, headers);
        } else {
            LOGGER.error(MBSPServiceConstants.INVALID_ACCEPT_HEADER);
            throw new MBSBusinessException(MBSPServiceConstants.INVALID_ACCEPT_HEADER,
                    MBSExceptionConstants.BUSINESS_EXCEPTION);

        }
        LOGGER.debug("Exiting submitPriceRequest method in TransactionRequestControllerHelper");
        return transactionRequestPOResponse;
    }

    /**
     *
     * get the transaction request for lender
     *
     * @param transreqid
     *            the transreqid
     * @param headers
     *            Map having header info and values
     * @return Object
     * @throws MBSBaseException
     */
    @SuppressWarnings("rawtypes")
    @ExceptionTracingAnnotation
    public Object getTransactionRequestsLender(Optional<String> transreqid, Map<String, String> headers)
            throws MBSBaseException {
        LOGGER.debug("Entering getTransactionRequestsLender method in TransactionRequestControllerHelper");
        String mediaType = MediaType.APPLICATION_JSON_VALUE;
        List<TransactionRequestPO> lstTransactionPO;
        List<String> lstStateType = MBSUtils.convertStateTypeToStringList(StateType.getFlowList("LENDER_FLOW"));
        String lenderName;
        MBSRoleType mbsRoleType = MBSRoleType.LENDER;

        /*
         * CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
         * Pass null so that Lender User Name get fetched from token
         */
        ProfileEntitlementPO profileEntitlementPO = cdxClientApi.getProfileEntitlementPO(headers, null);

        if (!Objects.equals(profileEntitlementPO, null)) {
            lenderName = profileEntitlementPO.getUserName();

        } else {
            LOGGER.error("Failed to retrieve lender profile");
            throw new MBSBusinessException("Failed to retrieve lender profile",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        // CMMBSSTA01-1371 - Changes for TSP - Start
        if (profileEntitlementPO.isTspUser()) {
            mbsRoleType = MBSRoleType.TSP;
        }
        // CMMBSSTA01-1371 - Changes for TSP - End
        BaseProcessor transReqService = selectDataServiceByMediaType(mediaType.trim());
        if (!Objects.equals(transReqService, null)) {
            lstTransactionPO = ((TransactionRequestService) transReqService).getMBSTransReq(lenderName, transreqid,
                    lstStateType, mbsRoleType);
        } else {
            LOGGER.error(MBSPServiceConstants.INVALID_ACCEPT_HEADER);
            throw new MBSBusinessException(MBSPServiceConstants.INVALID_ACCEPT_HEADER,
                    MBSExceptionConstants.BUSINESS_EXCEPTION);

        }
        LOGGER.debug("Exiting getTransactionRequestsLender method in TransactionRequestControllerHelper");
        // To send single object if the request is for a single record
        if (transreqid.isPresent() && StringUtils.isNotEmpty(transreqid.get())) {
            if (lstTransactionPO.isEmpty()) {
                return new TransactionRequestPO();

            } else {
                return lstTransactionPO.get(0);

            }
        } else {
            return lstTransactionPO;

        }
    }

    /**
     *
     * get the transaction request for trader
     *
     * @param transreqid
     *            the transreqid
     * @return Object
     * @throws MBSBaseException
     */
    @SuppressWarnings("rawtypes")
    @ExceptionTracingAnnotation
    public Object getTransactionRequestsTrader(Optional<String> transreqid) throws MBSBaseException {
        LOGGER.debug("Entering getTransactionRequestsTrader method in TransactionRequestControllerHelper");
        String mediaType = MediaType.APPLICATION_JSON_VALUE;
        List<TransactionRequestPO> lstTransactionPO;
        List<String> lstStateType = MBSUtils.convertStateTypeToStringList(StateType.getFlowList("TRADER_FLOW"));

        BaseProcessor transReqService = selectDataServiceByMediaType(mediaType.trim());
        if (!Objects.equals(transReqService, null)) {
            lstTransactionPO = ((TransactionRequestService) transReqService).getMBSTransReq(StringUtils.EMPTY,
                    transreqid, lstStateType, MBSRoleType.TRADER);
        } else {
            LOGGER.error(MBSPServiceConstants.INVALID_ACCEPT_HEADER);
            throw new MBSBusinessException(MBSPServiceConstants.INVALID_ACCEPT_HEADER,
                    MBSExceptionConstants.BUSINESS_EXCEPTION);

        }
        LOGGER.debug("Exiting getTransactionRequestsTrader method in TransactionRequestControllerHelper");
        // To send single object if the request is for a single record
        if (transreqid.isPresent() && StringUtils.isNotEmpty(transreqid.get())) {
            if (lstTransactionPO.isEmpty()) {
                return new TransactionRequestPO();

            } else {
                return lstTransactionPO.get(0);

            }
        } else {
            return lstTransactionPO;

        }
    }
}
