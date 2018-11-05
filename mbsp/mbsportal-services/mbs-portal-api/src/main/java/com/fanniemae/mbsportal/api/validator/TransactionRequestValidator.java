/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.validator;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.po.ProductPricingPO;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.service.MBSExceptionService;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.constants.ErrorMessage;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessWarning;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.validator.BaseValidator;

/**
 * Created by g8uaxt on 8/21/2017.
 */
@Component
public class TransactionRequestValidator<T extends TransformationObject> extends BaseValidator<T> {

    /**
     * 
     * LOGGER Logger variable
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(TransactionRequestValidator.class);

    /**
     *
     * mbsExceptionService MBSExceptionService
     */
//    @Autowired
//    MBSExceptionService mbsExceptionService;
    
    /**
     * 
     * mbspProperties MbspProperties
     */
    @Autowired
    MbspProperties mbspProperties;
    /**
     * 
     * NO_TRANSACTION_REQUEST String
     */
    private String NO_TRANSACTION_REQUEST = "No records found for transReqId";
    /**
     * 
     * NOT_ELIGIBLE_STATUS String
     */
    // 5005
    private String NOT_ELIGIBLE_STATUS = " Trade cannot be changed as it is in state: ";

    /**
     * 
     * INVALID_PARTY_SHORTNAME String
     */
    private String INVALID_PARTY_SHORTNAME = "Bad Request: The party short name for the lender is missing in the profile.";

    /**
     * 
     * Purpose: This method validates the Transaction Request PO object
     * 
     * @param transformationObject
     *            TransformationObject
     * @throws MBSBaseException
     */
    @Override
    public void validate(TransformationObject transformationObject) throws MBSBaseException {
        TransactionRequestPO transactionRequestPO = (TransactionRequestPO) transformationObject.getSourcePojo();
        if ((transactionRequestPO.getStateType().equals(StateType.LENDER_OPEN)
                && (transformationObject.getMBSRoleType().equals(MBSRoleType.TRADER)))
                || transactionRequestPO.getStateType().equals(StateType.TRADER_PRICED)
                || transactionRequestPO.getStateType().equals(StateType.TRADER_REPRICED)
                || transactionRequestPO.getStateType().equals(StateType.LENDER_ACCEPTED)
                || transactionRequestPO.getStateType().equals(StateType.TRADER_CONFIRMED)
                || transactionRequestPO.getStateType().equals(StateType.TRADER_PASSED)
                || transactionRequestPO.getStateType().equals(StateType.TRADER_REJECTED)
                || transactionRequestPO.getStateType().equals(StateType.PENDING_EXECUTION)
                || transactionRequestPO.getStateType().equals(StateType.LENDER_REJECTED)) {
            MBSTransactionRequest mbsTransaction = (MBSTransactionRequest) transformationObject.getTargetPojo();
            // do business validation
            // TRAN 6000
            if (Objects.equals(mbsTransaction, null)) {
                LOGGER.error(NO_TRANSACTION_REQUEST + transactionRequestPO.getTransReqId());
                throw new MBSBusinessException(NO_TRANSACTION_REQUEST + transactionRequestPO.getTransReqId(),
                        MBSExceptionConstants.BUSINESS_EXCEPTION);
            }

            /*
             * CMMBSSTA01-1022: (Tech) Maintain versions for transaction request
             */
            Long existingValueFromUser = transactionRequestPO.getActiveVersion();
            Long existingValueAtSystem = !Objects.isNull(mbsTransaction) ? mbsTransaction.getActiveVersion() : null;

            if (transactionRequestPO.getStateType() != StateType.TRADER_REPRICED) {
                /*
                 * Existing Version Cannot be empty for Existing Records
                 * Assumption: We are not taking care of existing records -
                 * Which exists before installation of this functionality
                 */
                if (Objects.isNull(existingValueAtSystem)) {
                    LOGGER.warn(ErrorMessage.INVALID_VERSION_TO_PERFORM_ACTION.value() + ", Version From User:"
                            + existingValueFromUser + ", Version At System:" + existingValueAtSystem);
                    throw new MBSBusinessWarning(ErrorMessage.INVALID_VERSION_TO_PERFORM_ACTION.value(),
                            MBSExceptionConstants.BUSINESS_EXCEPTION);
                    
                    //TODO: Follow Exception Framework
                    //throw mbsExceptionService.createBusinessWarningnAndLog(this, MBSExceptionConstants.TRAN_5004, new Object[] {existingValueAtSystem, existingValueFromUser});
                }
                /*
                 * System is now advance with version, but user requested to
                 * operate with old version info- which is not matching with
                 * system version
                 */
                if (existingValueAtSystem.longValue() != existingValueFromUser.longValue()) {

                    LOGGER.warn(ErrorMessage.INVALID_VERSION_TO_PERFORM_ACTION.value() + ", Version From User:"
                            + existingValueFromUser + ", Version At System:" + existingValueAtSystem);
                    throw new MBSBusinessWarning(ErrorMessage.INVALID_VERSION_TO_PERFORM_ACTION.value(),
                            MBSExceptionConstants.BUSINESS_EXCEPTION);
                    
                    //TODO: Follow Exception Framework
                    //throw mbsExceptionService.createBusinessWarningnAndLog(this, MBSExceptionConstants.TRAN_5004, new Object[] {existingValueAtSystem, existingValueFromUser});
                }
            }
            // End

            validateWorkflowStatus(transactionRequestPO.getStateType(),
                    StateType.getEnum(mbsTransaction.getStateType()));
            mbsTransaction.setStateType(transactionRequestPO.getStateType().toString());

        } else if (transactionRequestPO.getStateType().equals(StateType.LENDER_OPEN)
                && ((transformationObject.getMBSRoleType().equals(MBSRoleType.LENDER)))
                || (transformationObject.getMBSRoleType().equals(MBSRoleType.TSP))) {
            MBSTransactionRequest mbsTransaction = (MBSTransactionRequest) transformationObject.getTargetPojo();
            // do business validation
            if (Objects.equals(mbsTransaction, null)) {
                LOGGER.error(NO_TRANSACTION_REQUEST + transactionRequestPO.getTransReqId());
                throw new MBSBusinessException(NO_TRANSACTION_REQUEST + transactionRequestPO.getTransReqId(),
                        MBSExceptionConstants.BUSINESS_EXCEPTION);
            }
            List<ProductPricingPO> productPricingPOLst = (List<ProductPricingPO>) transformationObject
                    .getTransformationDataMap().get(MBSPServiceConstants.PRODUCT_PRICING);
            // Validating Party Short Name
            if (StringUtils.isBlank(mbsTransaction.getLenderShortName())) {
                LOGGER.error(INVALID_PARTY_SHORTNAME);
                throw new MBSBusinessException(INVALID_PARTY_SHORTNAME, MBSExceptionConstants.BUSINESS_EXCEPTION);

            }
            mbsTransaction.setTradeSettlementDate(
                    validateSettlementDate(productPricingPOLst, mbsTransaction.getTradeSettlementDate()));
            mbsTransaction.setSubmissionDate(
                    validateCutOffDate(productPricingPOLst, mbsTransaction.getTradeSettlementDate()));
            validateCoupon(productPricingPOLst, mbsTransaction.getTradeCouponRate());
        }

    }

    /**
     * 
     * 
     * @param productPricingPOLst
     *            List<ProductPricingPO>
     * @param tradeCouponRate
     *            BigDecimal
     * @throws MBSBaseException
     */
    protected void validateCoupon(List<ProductPricingPO> productPricingPOLst, BigDecimal tradeCouponRate)
            throws MBSBaseException {
        boolean couponPresent = false;
        String couponRate = MBSPortalUtils.convertToString(tradeCouponRate, 5, 2);
        for (ProductPricingPO productPricingPO : productPricingPOLst) {
            if (productPricingPO.getPassThroughRate().equals(couponRate)) {
                couponPresent = true;
                break;
            }
        }
        // If coupon not present then throw error
        if (!couponPresent) {
            LOGGER.error("Input coupon rate is not valid");
            throw new MBSBusinessException("Input coupon rate is not valid", MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
    }

    /**
     * 
     * validateCutOffDate
     * 
     * @param productPricingPOLst
     *            List<ProductPricingPO>
     * @param settlementDate
     *            String
     * @return Date
     * @throws MBSBaseException
     */
    protected Date validateCutOffDate(List<ProductPricingPO> productPricingPOLst, Date settleDate)
            throws MBSBaseException {
        String settlementDate = MBSPortalUtils.convertDateToString(settleDate, DateFormats.DATE_FORMAT_NO_TIMESTAMP);
        Date submissionDate = MBSPortalUtils.getCurrentDate();
        String cutOffDateStr = null;
        int cutOffHour = Integer.valueOf(mbspProperties.getCutOffHour()).intValue();
        int cutOffMin = Integer.valueOf(mbspProperties.getCutOffMinute()).intValue();
        int cutOffSec = Integer.valueOf(mbspProperties.getCutOffSecond()).intValue();
        int cutOffMilliSec = Integer.valueOf(mbspProperties.getCutOffMillisecond()).intValue();
        // Get the cutoff date
        for (ProductPricingPO productPricingPO : productPricingPOLst) {
            if (productPricingPO.getSettlementDate().equals(settlementDate)) {
                cutOffDateStr = productPricingPO.getCutOffDate();
                break;
            }
        }
        // Check whether cutoff date is today
        if (StringUtils.isNotEmpty(cutOffDateStr)) {
            Date cutOffDate = MBSPortalUtils.convertToDateWithFormatter(cutOffDateStr,
                    DateFormats.DATE_FORMAT_NO_TIMESTAMP);
            if (!MBSPortalUtils.isItPastDate(cutOffDate)) {
                if (MBSPortalUtils.isDateEqual(cutOffDate)) {
                    if (!MBSPortalUtils.isDateBeforeCutOffTime(cutOffHour, cutOffMin, cutOffSec, cutOffMilliSec)) {
                        LOGGER.error("Trade request after cutoff date/time");
                        throw new MBSBusinessException("Fannie Mae is no longer trading this settlement month",
                                MBSExceptionConstants.BUSINESS_EXCEPTION);
                    }
                }
            } else {
                LOGGER.error("Trade request after cutoff date/time");
                throw new MBSBusinessException("Trade request after cutoff date/time",
                        MBSExceptionConstants.BUSINESS_EXCEPTION);
            }
        } else {
            LOGGER.error("Cutoff date is not populated or empty.");
            throw new MBSBusinessException("Cutoff date is not populated or empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        return submissionDate;

    }

    /**
     * 
     * validateSettlementDate
     * 
     * @param productPricingPOLst
     *            List<ProductPricingPO>
     * @param settlementDate
     *            String
     * @return Date
     * @throws MBSBaseException
     */
    protected Date validateSettlementDate(List<ProductPricingPO> productPricingPOLst, Date settleDate)
            throws MBSBaseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(settleDate);
        int inputSettleMonth = cal.get(Calendar.MONTH);
        int inputSettleYear = cal.get(Calendar.YEAR);
        Date tradeSettlementDate = null;
        Date referenceTradeSettlementDate = null;
        // Get the settlement date
        for (ProductPricingPO productPricingPO : productPricingPOLst) {
            referenceTradeSettlementDate = MBSPortalUtils.convertToDateWithFormatter(
                    productPricingPO.getSettlementDate(), DateFormats.DATE_FORMAT_NO_TIMESTAMP);
            cal = Calendar.getInstance();
            cal.setTime(referenceTradeSettlementDate);
            // Validate the settlement month
            if (inputSettleMonth == cal.get(Calendar.MONTH)) {
                tradeSettlementDate = referenceTradeSettlementDate;
                break;
            }
        }
        if (Objects.isNull(tradeSettlementDate)) {
            LOGGER.error("Input trade settlement date is not valid.");
            throw new MBSBusinessException("Input trade settlement date is not valid.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);

        } else {
            return tradeSettlementDate;
        }
    }

    /**
     * 
     * this will do the status validation of next step in workflow
     * 
     * @param futureState
     *            StateType
     * @param currentState
     *            StateType
     * @throws MBSBaseException
     */
    protected void validateWorkflowStatus(StateType futureState, StateType currentState) throws MBSBaseException {
        LOGGER.debug("Request stateType" + futureState);
        LOGGER.debug("From GemFire StateType" + currentState);
        if (!futureState.isValidState(currentState)) {
            LOGGER.error(NOT_ELIGIBLE_STATUS + currentState);
            throw new MBSBusinessException(NOT_ELIGIBLE_STATUS + currentState,
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
    }
}
