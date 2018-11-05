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

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.constants.TradeBuySell;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.service.MBSExceptionService;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.ErrorMessage;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessWarning;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.validator.BaseValidator;

/**
 * Class Name: TransactionRequestPOValidator Purpose : This class handles the
 * validations for Transaction object
 * 
 * @author g8upjv
 *
 */
/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 10, 2018
 * @Time 2:45:48 PM com.fanniemae.mbsportal.api.validator
 *       TransactionRequestPOValidator.java
 * @Description: CMMBSSTA01-46: (Lender page) Lender>TBA Trading Business
 *               validations
 */
@Component
public class TransactionRequestPOValidator<T extends TransformationObject> extends BaseValidator<T> {

    /**
     *
     * mbsExceptionService MBSExceptionService
     */
    @Autowired
    MBSExceptionService mbsExceptionService;

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionRequestPOValidator.class);
    // Messages
    /**
     *
     * TRANS_REQ_ID_NOT_REQ String
     */
    private String TRANS_REQ_ID_NOT_REQ = "Bad Request: Trans req id should not be sent as part of request.";
    /**
     *
     * INVALID_TRANSREQ_ID String
     */
    private String INVALID_TRANSREQ_ID = "Bad Request: Trans req id should be sent as part of request.";
    /**
     *
     * INVALID_LENDER_ID String
     */
    private String INVALID_LENDER_ID = "Bad Request: Lender id is missing or invalid.";
    /**
     *
     * INVALID_LENDER_ID String
     */
    private String NO_TRANS_SETTLEMENT_DATE = "Bad Request: Transaction settlement date is missing or invalid.";
    /**
     *
     * INVALID_PRODUCT_ID String
     */
    private String INVALID_PRODUCT_ID = "Bad Request: Product Id is missing or invalid";
    /**
     *
     * INVALID_TRADETYPE String
     */
    private String INVALID_TRADETYPE = "Bad Request: Trade type is missing or invalid.";
    /**
     *
     * INVALID_TRADER_ID String
     */
    private String INVALID_TRADER_ID = "Bad Request: Trader id is missing or invalid.";
    /**
     *
     * INVALID_STATETYPE String
     */
    private String INVALID_STATETYPE = "Bad Request: State Type is invalid for this request.";

    /**
     *
     * INVALID_OBO_PARAMS String
     */
    private String INVALID_OBO_PARAMS = "Bad Request: OBO Lender details missing or invalid";

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

        LOGGER.debug("Entering validate method in TransactionRequestPOValidator");

        /*
         * CMMBSSTA01-1022: (Tech) Maintain versions for transaction request
         */
        Long existingValueFromUser = transactionRequestPO.getActiveVersion();

        Map<String, ExceptionLookupPO> exceptionLookupPOMap = (HashMap<String, ExceptionLookupPO>) transformationObject
                .getTransformationDataMap().get(MBSPServiceConstants.EXCEPTION_MESSAGES);

        /*
         * Expect version always from User
         */
        if (Objects.isNull(existingValueFromUser)) {
            LOGGER.warn(ErrorMessage.INVALID_VERSION_TO_PERFORM_ACTION.value() + ", Version From User:"
                    + existingValueFromUser);
            throw new MBSBusinessWarning(ErrorMessage.INVALID_VERSION_TO_PERFORM_ACTION.value(),
                    MBSExceptionConstants.BUSINESS_WARNING);
            /*
             * Enable it once we got the message
             */
            // TODO: Use Exception Framework
            // throw mbsExceptionService.createBusinessWarningnAndLog(this, MBSExceptionConstants.TRAN_5005, existingValueFromUser);

        }
        if (transactionRequestPO.getStateType() == StateType.LENDER_OPEN) {

            if (transformationObject.getMBSRoleType().equals(MBSRoleType.LENDER)
                    || transformationObject.getMBSRoleType().equals(MBSRoleType.TSP)) {
                /*
                 * User should send 1 first Trade Request
                 */
                if (existingValueFromUser.longValue() != 1L) {

                    LOGGER.warn(ErrorMessage.INVALID_VERSION_TO_PERFORM_ACTION.value()
                            + " First Time Version should be 1, Version From User:" + existingValueFromUser);
                    throw new MBSBusinessWarning(ErrorMessage.INVALID_VERSION_TO_PERFORM_ACTION.value(),
                            MBSExceptionConstants.BUSINESS_WARNING);
                    /*
                     * Enable it once we got the message
                     */
                    // TODO: Use Exception Framework
                    // throw
                    // mbsExceptionService.createBusinessWarningnAndLog(this,
                    // MBSExceptionConstants.TRAN_5006, existingValueFromUser);
                }
            }
        }
        // end

        if (transactionRequestPO.getStateType() == StateType.LENDER_OPEN) {
            if (transformationObject.getMBSRoleType().equals(MBSRoleType.LENDER)
                    || transformationObject.getMBSRoleType().equals(MBSRoleType.TSP)) {
                validateLenderOpen(transactionRequestPO, transformationObject.getMBSRoleType());

            } else if (transformationObject.getMBSRoleType().equals(MBSRoleType.TRADER)) {
                /*
                 * Validate trans request id
                 */
                if (StringUtils.isEmpty(transactionRequestPO.getTransReqId())) {
                    LOGGER.error(INVALID_TRANSREQ_ID);
                    throw new MBSBusinessException(INVALID_TRANSREQ_ID, MBSExceptionConstants.BUSINESS_EXCEPTION);
                }
            }
        } else if (transactionRequestPO.getStateType() == StateType.TRADER_PRICED
                || transactionRequestPO.getStateType() == StateType.TRADER_REPRICED) {

            /*
             * Eagerly assign 00
             */
            if (transactionRequestPO.getPricePercentTicksText() == null
                    || StringUtils.isEmpty(transactionRequestPO.getPricePercentTicksText().trim())) {
                transactionRequestPO.setPricePercentTicksText(TradeConstants.TRADE_TICK_DEFAULT_VALUE);
            }

            /*
             * CMMBSSTA01-760: (Trader page) Trader price input validations
             * Handle and Tick Must be Numeric Handle and Tick should not be
             * greater than 3 digits Handle Should not be stared with 0 Handle
             * Range - 95 to 110 Tick Range - 0 to 31 If Eights is there then
             * Range - 0 to 7
             */
            String handle = transactionRequestPO.getPricePercentHandleText();
            String tick = transactionRequestPO.getPricePercentTicksText();

            if (StringUtils.isBlank(handle)) {
                throw mbsExceptionService.createBusinessExceptionAndLog(this, MBSExceptionConstants.TRAN_5001, handle);
            }

            validateHandle(handle, exceptionLookupPOMap);

            validateTick(tick, exceptionLookupPOMap);

            /*
             * CMMBSSTA01-1023: (Trader page) Services test: Price input
             */
            validateMaxValueHandleWithTick(handle, tick, exceptionLookupPOMap);

            /*
             * Validate trader id
             */
            if (StringUtils.isEmpty(transactionRequestPO.getTraderId())) {
                LOGGER.error(INVALID_TRADER_ID);
                throw new MBSBusinessException(INVALID_TRADER_ID, MBSExceptionConstants.BUSINESS_EXCEPTION);
            }

        } else if (transactionRequestPO.getStateType() == StateType.TRADER_CONFIRMED
                || transactionRequestPO.getStateType() == StateType.TRADER_PASSED
                || transactionRequestPO.getStateType() == StateType.TRADER_REJECTED) {
            /*
             * Validate trans request id
             */
            if (StringUtils.isEmpty(transactionRequestPO.getTransReqId())) {
                LOGGER.error(INVALID_TRANSREQ_ID);
                throw new MBSBusinessException(INVALID_TRANSREQ_ID, MBSExceptionConstants.BUSINESS_EXCEPTION);
            }
            if (StringUtils.isEmpty(transactionRequestPO.getTraderId())) {
                LOGGER.error(INVALID_TRADER_ID);
                throw new MBSBusinessException(INVALID_TRADER_ID, MBSExceptionConstants.BUSINESS_EXCEPTION);
            }
        } else if (transactionRequestPO.getStateType() == StateType.PENDING_EXECUTION) {
            /*
             * Validate trans request id
             */
            if (StringUtils.isEmpty(transactionRequestPO.getTransReqId())) {
                LOGGER.error(INVALID_TRANSREQ_ID);
                throw new MBSBusinessException(INVALID_TRANSREQ_ID, MBSExceptionConstants.BUSINESS_EXCEPTION);
            }
        } else if (transactionRequestPO.getStateType() == StateType.LENDER_ACCEPTED
                || transactionRequestPO.getStateType() == StateType.LENDER_REJECTED) {
            /*
             * Validate lender id
             */
            if (StringUtils.isEmpty(transactionRequestPO.getLenderId())) {
                LOGGER.error(INVALID_LENDER_ID);
                throw new MBSBusinessException(INVALID_LENDER_ID, MBSExceptionConstants.BUSINESS_EXCEPTION);
            }
            if (StringUtils.isEmpty(transactionRequestPO.getTransReqId())) {
                LOGGER.error(INVALID_TRANSREQ_ID);
                throw new MBSBusinessException(INVALID_TRANSREQ_ID, MBSExceptionConstants.BUSINESS_EXCEPTION);
            }
        } else {
            LOGGER.error(INVALID_STATETYPE);
            throw new MBSBusinessException(INVALID_STATETYPE, MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
    }

    /**
     *
     *
     * @param transactionRequestPO
     *            TransactionRequestPO
     * @param mbsRoleType
     *            MBSRoleType
     * @throws MBSBaseException
     */
    public void validateLenderOpen(TransactionRequestPO transactionRequestPO, MBSRoleType mbsRoleType)
            throws MBSBaseException {

        /*
         * CMMBSSTA01-46: (Lender page) Lender>TBA Trading Business validations
         * Do not allow negatives, alpha characters, and decimals Amount must be
         * between 25,000 to 250,000,000
         */
        String tradeAmount = transactionRequestPO.getTradeAmount();

        if (!MBSPortalUtils.isNumeric(tradeAmount)) {
            LOGGER.error(ErrorMessage.LENDER_INVALID_TRADE_AMOUNT.value() + ", Trade Amount:" + tradeAmount);
            throw new MBSBusinessException(ErrorMessage.LENDER_INVALID_TRADE_AMOUNT.value(),
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }

        if (!MBSPortalUtils.isInRange(new BigInteger(tradeAmount), TradeConstants.TRADE_MIN_VALUE,
                TradeConstants.TRADE_MAX_VALUE)) {
            LOGGER.error(ErrorMessage.LENDER_INVALID_TRADE_AMOUNT.value() + ", Trade Amount:" + tradeAmount);
            throw new MBSBusinessException(ErrorMessage.LENDER_INVALID_TRADE_AMOUNT.value(),
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        /*
         * Validate trans request id
         */
        if (StringUtils.isNotEmpty(transactionRequestPO.getTransReqId())) {
            LOGGER.error(TRANS_REQ_ID_NOT_REQ);
            throw new MBSBusinessException(TRANS_REQ_ID_NOT_REQ, MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        /*
         * Validate trans settlement date
         */
        if (StringUtils.isEmpty(transactionRequestPO.getTradeSettlementDate())) {
            LOGGER.error(NO_TRANS_SETTLEMENT_DATE);
            throw new MBSBusinessException(NO_TRANS_SETTLEMENT_DATE, MBSExceptionConstants.BUSINESS_EXCEPTION);
        }

        if (Objects.equals(transactionRequestPO.getProduct(), null)
                || Objects.equals(transactionRequestPO.getProduct().getProductId(), null)
                || Objects.equals(transactionRequestPO.getProduct().getProductId().getIdentifier(), null)) {
            LOGGER.error(INVALID_PRODUCT_ID);
            throw new MBSBusinessException(INVALID_PRODUCT_ID, MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        /*
         * Validate Trade type
         */
        if (StringUtils.isEmpty(transactionRequestPO.getTradeBuySellType())
                || !(transactionRequestPO.getTradeBuySellType().equalsIgnoreCase(TradeBuySell.BUY.toString())
                        || transactionRequestPO.getTradeBuySellType().equalsIgnoreCase(TradeBuySell.SELL.toString()))) {
            LOGGER.error(INVALID_TRADETYPE);
            throw new MBSBusinessException(INVALID_TRADETYPE, MBSExceptionConstants.BUSINESS_EXCEPTION);
        }

        if (mbsRoleType.equals(MBSRoleType.TSP)) {
            if (StringUtils.isEmpty(transactionRequestPO.getOboLenderSellerServicerNumber())
                    || StringUtils.isEmpty(transactionRequestPO.getTspShortName())) {
                LOGGER.error(INVALID_OBO_PARAMS);
                throw new MBSBusinessException(INVALID_OBO_PARAMS, MBSExceptionConstants.BUSINESS_EXCEPTION);

            }
        }

    }

    /**
     *
     *
     * @param handle
     *            String
     * @throws MBSBaseException
     */
    private void validateHandle(String handle, Map<String, ExceptionLookupPO> exceptionLookupPOMap)
            throws MBSBaseException {

        if (!MBSPortalUtils.isNumeric(handle)) {
            throw mbsExceptionService.createBusinessExceptionAndLog(this, MBSExceptionConstants.TRAN_5002, handle);
        }

        if (StringUtils.length(handle) > 3) {
            throw mbsExceptionService.createBusinessExceptionAndLog(this, MBSExceptionConstants.TRAN_5002, handle);
        }

        if (StringUtils.startsWith(handle, "0")) {
            throw mbsExceptionService.createBusinessExceptionAndLog(this, MBSExceptionConstants.TRAN_5002, handle);
        }

        if (!MBSPortalUtils.isInRange(new BigInteger(handle), TradeConstants.TRADE_HANDLE_MIN_VALUE,
                TradeConstants.TRADE_HANDLE_MAX_VALUE)) {
            throw mbsExceptionService.createBusinessExceptionAndLog(this, MBSExceptionConstants.TRAN_5002, handle);
        }
    }

    /**
     *
     *
     * @param tick
     *            String
     * @throws MBSBaseException
     */
    private void validateTick(String tick, Map<String, ExceptionLookupPO> exceptionLookupPOMap)
            throws MBSBaseException {

        /*
         * For Null tick the length will be 0 - But we have assigned the tick
         */
        if (StringUtils.length(tick) > 3) {
            throw mbsExceptionService.createBusinessExceptionAndLog(this, MBSExceptionConstants.TRAN_5003, tick);
        }

        String eights = null;
        if (StringUtils.length(tick) == 3) {
            eights = StringUtils.substring(tick, 2);
            tick = StringUtils.substring(tick, 0, 2);
        }

        /*
         * CMMBSSTA01-1023: (Trader page) Services test: Price input validation
         * errors Eights might be + hence apply tick part
         */
        if (!MBSPortalUtils.isNumeric(tick)) {
            throw mbsExceptionService.createBusinessExceptionAndLog(this, MBSExceptionConstants.TRAN_5003, tick);
        }

        if (!MBSPortalUtils.isInRange(new BigInteger(tick), TradeConstants.TRADE_TICK_MIN_VALUE,
                TradeConstants.TRADE_TICK_MAX_VALUE)) {
            throw mbsExceptionService.createBusinessExceptionAndLog(this, MBSExceptionConstants.TRAN_5003, tick);
        }

        /*
         * CMMBSSTA01-1023: (Trader page) Services test: Price input validation
         * errors Convert the Eights if it is + then covert into 4 and apply
         * logic
         */
        if (TradeConstants.EIGHTS_MIDDLE_VALUE_SIGN.equalsIgnoreCase(eights)) {
            eights = TradeConstants.EIGHTS_MIDDLE_VALUE_NUMERIC;
        }

        if (StringUtils.isNotBlank(eights) && !MBSPortalUtils.isInRange(new BigInteger(eights),
                TradeConstants.TRADE_EIGHTS_MIN_VALUE, TradeConstants.TRADE_EIGHTS_MAX_VALUE)) {

            Object[] values = new Object[] { eights, tick };
            throw mbsExceptionService.createBusinessExceptionAndLog(this, MBSExceptionConstants.TRAN_5003, values);
        }
    }

    /**
     * CMMBSSTA01-1023: (Trader page) Services test: Price input validation
     * errors
     *
     * @param handle
     *            String
     * @param tick
     *            String
     * @throws MBSBaseException
     */
    private void validateMaxValueHandleWithTick(String handle, String tick,
            Map<String, ExceptionLookupPO> exceptionLookupPOMap) throws MBSBaseException {

        /*
         * If Handle is 110 then tick has to be numeric and should not be
         * greater than zero Greater than check is done earlier; hence check the
         * equal to max value
         */
        try {
            if (MBSPortalUtils.isGreaterOrEqual(new BigInteger(handle), TradeConstants.TRADE_HANDLE_MAX_VALUE)) {
                if (!MBSPortalUtils.isNumeric(tick)
                        || !MBSPortalUtils.isLessOrEqual(new BigInteger(tick), TradeConstants.TRADE_TICK_MIN_VALUE)) {
                    Object[] values = new Object[] { handle, tick };
                    throw mbsExceptionService.createBusinessExceptionAndLog(this, MBSExceptionConstants.TRAN_5003,
                            values);
                }
            }
        } catch (MBSBaseException e) {
            Object[] values = new Object[] { handle, tick };
            throw mbsExceptionService.createBusinessExceptionAndLog(this, MBSExceptionConstants.TRAN_5003, values);
        }

    }
}
