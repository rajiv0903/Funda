/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.transformation;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.config.TradeServiceProperties;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.constants.TradeBuySell;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.BaseTransformer;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * Class Name: TransactionRequestTransformer Purpose : This class handles the
 * transformations required for Transaction Request
 *
 * @author g8upjv
 *
 */
/**
 *
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 12, 2018
 * @Time 6:45:52 PM com.fanniemae.mbsportal.api.transformation
 *       TransactionRequestTransformer.java
 * @Description: CMMBSSTA01-1022: (Tech) Maintain versions for transaction
 *               request
 */
@Component
public class TransactionRequestTransformer<T extends TransformationObject> extends BaseTransformer<T> {

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionRequestTransformer.class);

    /**
     *
     * mbspProperties MbspProperties
     */
    @Autowired
    MbspProperties mbspProperties;

    /**
     *
     * tradeServiceProperties TradeServiceProperties
     */
    @Autowired
    TradeServiceProperties tradeServiceProperties;

    /**
     *
     * Purpose: This method transforms the MBS Transaction Request object
     *
     * @param transformationObject
     *            The presentation object TransactionRequestPO
     * @return TransformationObject
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    @Override
    public TransformationObject transform(TransformationObject transformationObject) throws MBSBaseException {
        LOGGER.debug("Entering transform method in TransactionRequestTransformer");
        TransactionRequestPO transactionRequestPO = (TransactionRequestPO) transformationObject.getSourcePojo();

        MBSTransactionRequest mbsTransaction = (MBSTransactionRequest) transformationObject.getTargetPojo();

        MBSMarketIndicativePrice mbsMarketIndicativePrice = (MBSMarketIndicativePrice) transformationObject
                .getTransformationDataMap().get(MBSPServiceConstants.STREAM_PRICE);

        if (transactionRequestPO != null && transactionRequestPO.getStateType() != null) {
            if (transactionRequestPO.getStateType().equals(StateType.TRADER_PRICED)
                    || transactionRequestPO.getStateType().equals(StateType.TRADER_REPRICED)) {

                /*
                 * CMMBSSTA01-1023: (Trader page) Services test: Price input
                 * validation errors Convert the Eights if it is + then covert
                 * into 4 and apply logic Required Before Price Percentage
                 * Calculation
                 */
                String pricePercentTicksForCalculation = transactionRequestPO.getPricePercentTicksText();
                String tick = transactionRequestPO.getPricePercentTicksText();
                String eights = "";
                if (StringUtils.length(tick) == 3) {
                    eights = StringUtils.substring(tick, 2);
                    tick = StringUtils.substring(tick, 0, 2);
                }
                // To take care of Price Percentage
                if (TradeConstants.EIGHTS_MIDDLE_VALUE_SIGN.equalsIgnoreCase(eights)) {
                    eights = TradeConstants.EIGHTS_MIDDLE_VALUE_NUMERIC;
                    pricePercentTicksForCalculation = tick + eights;
                }
                /*
                 * CMMBSSTA01-1040: (Trader page) Allow '+' in third digit of
                 * ticks field and display in ticket and trx history If User
                 * send + then save as +
                 */
                if (TradeConstants.EIGHTS_MIDDLE_VALUE_NUMERIC.equalsIgnoreCase(eights)) {
                    eights = TradeConstants.EIGHTS_MIDDLE_VALUE_SIGN;
                    transactionRequestPO.setPricePercentTicksText(tick + eights);
                }
                // End

                mbsTransaction.setPricePercentTicksText(transactionRequestPO.getPricePercentTicksText() + "-"
                        + transactionRequestPO.getPricePercentHandleText());
                mbsTransaction.setPricePercent(calculatePricePercent(pricePercentTicksForCalculation,
                        transactionRequestPO.getPricePercentHandleText()));
                mbsTransaction.setTradeTraderIdentifierText(transactionRequestPO.getTraderId());
                // CMMBSSTA01-1157 - Stream Pricing - Start
                populateStreamPrice(mbsTransaction, mbsMarketIndicativePrice);
                // CMMBSSTA01-1157 - Stream Pricing - End
            } else if (transactionRequestPO.getStateType().equals(StateType.LENDER_OPEN)) {
                // LENDER OPEN from Lender is new request and from trader is for
                // soft lock of the
                // transaction by the trader
                if (transformationObject.getMBSRoleType().equals(MBSRoleType.LENDER)
                        // CMMBSSTA01-1371 - Changes for TSP - Start
                        || transformationObject.getMBSRoleType().equals(MBSRoleType.TSP)) {
                    // CMMBSSTA01-1371 - Changes for TSP - Start
                    // Create New Model Object
                    mbsTransaction = convertToTransactionModel(transformationObject);
                } else if (transformationObject.getMBSRoleType().equals(MBSRoleType.TRADER)) {
                    mbsTransaction.setTradeTraderIdentifierText(transactionRequestPO.getTraderId());
                    // CMMBSSTA01-1157 - Stream Pricing - Start
                    populateStreamPrice(mbsTransaction, mbsMarketIndicativePrice);
                    // CMMBSSTA01-1157 - Stream Pricing - End
                }
            } else if (transactionRequestPO.getStateType().equals(StateType.TRADER_CONFIRMED)) {
                mbsTransaction.setTradeTraderIdentifierText(transactionRequestPO.getTraderId());
                mbsTransaction.setAcceptanceDate(MBSPortalUtils.getCurrentDate());
                populateStreamPrice(mbsTransaction, mbsMarketIndicativePrice);
            } else if (transactionRequestPO.getStateType().equals(StateType.TRADER_PASSED)
                    || transactionRequestPO.getStateType().equals(StateType.TRADER_REJECTED)) {
                mbsTransaction.setTradeTraderIdentifierText(transactionRequestPO.getTraderId());
                populateStreamPrice(mbsTransaction, mbsMarketIndicativePrice);
            } else if (transactionRequestPO.getStateType().equals(StateType.LENDER_ACCEPTED)
                    || transactionRequestPO.getStateType().equals(StateType.LENDER_REJECTED)) {
                mbsTransaction.setCounterpartyTraderIdentifier(transactionRequestPO.getLenderId());
                populateStreamPrice(mbsTransaction, mbsMarketIndicativePrice);
            }
        }
        transformationObject.setTargetPojo(mbsTransaction);
        LOGGER.debug("Exiting transform method in TransactionRequestTransformer");
        return transformationObject;

    }

    /**
     *
     * This method populates the stream price
     *
     * @param mbsTransaction
     * @param mbsMarketIndicativePrice
     */
    public void populateStreamPrice(MBSTransactionRequest mbsTransaction,
            MBSMarketIndicativePrice mbsMarketIndicativePrice) {

        // CMMBSSTA01-1157 - Stream Pricing - Start
        if (Objects.nonNull(mbsMarketIndicativePrice) && Objects.nonNull(mbsMarketIndicativePrice.getBidPricePercent())
                && Objects.nonNull(mbsMarketIndicativePrice.getAskPricePercent())) {
            if (TradeBuySell.BUY.name().equals(mbsTransaction.getTradeBuySellType())) {
                mbsTransaction.setStreamPricePercent(mbsMarketIndicativePrice.getBidPricePercent());
                mbsTransaction.setStreamPricePercentTicksText(mbsMarketIndicativePrice.getBidPriceText());
            } else {
                mbsTransaction.setStreamPricePercent(mbsMarketIndicativePrice.getAskPricePercent());
                mbsTransaction.setStreamPricePercentTicksText(mbsMarketIndicativePrice.getAskPriceText());
            }
        }
        // CMMBSSTA01-1157 - Stream Pricing - End
    }

    /**
     *
     *
     * @param pricePercentTicksText
     *            String
     * @param pricePercentHandleText
     *            String
     * @return BigDecimal
     * @throws MBSBaseException
     */
    protected BigDecimal calculatePricePercent(String pricePercentTicksText, String pricePercentHandleText)
            throws MBSBaseException {

        BigDecimal handle = new BigDecimal(pricePercentHandleText);
        // split the tick & htick
        BigDecimal tick = null;
        BigDecimal htickCal = null;
        // TODO: reject if more than 3 digit in validator
        if (pricePercentTicksText.length() == 3) {
            tick = new BigDecimal(pricePercentTicksText.substring(0, 2));
            BigDecimal htick = new BigDecimal(pricePercentTicksText.substring(2, 3));
            htickCal = htick.divide(new BigDecimal(8));
            LOGGER.debug("htickCal:{}", htickCal);
        } else {
            tick = new BigDecimal(pricePercentTicksText);
            htickCal = new BigDecimal(0);
        }
        LOGGER.debug("tick {} and htick: {}", tick, htickCal);
        LOGGER.debug("htickCal:{}", htickCal);
        BigDecimal tickCal = tick.add(htickCal).divide(new BigDecimal(32));
        LOGGER.debug("tickCal:{}", tickCal);
        BigDecimal pricePercent = handle.add(tickCal).setScale(9).round(MathContext.DECIMAL64);
        LOGGER.debug("pricePercent:{}", pricePercent);
        return pricePercent;
    }

    /**
     *
     * Purpose: This does the conversion from TransationPO to Transaction object
     *
     * @param transformationObject
     *            The presentation object TransactionRequestPO
     * @return MBSTransactionRequest The model object is returned to save in
     *         Gemfire
     * @throws MBSBaseException
     */
    private MBSTransactionRequest convertToTransactionModel(TransformationObject transformationObject)
            throws MBSBaseException {
        TransactionRequestPO mbsTransactionPO = (TransactionRequestPO) transformationObject.getSourcePojo();
        MBSTransactionRequest mbsTransaction = new MBSTransactionRequest();
        ProductId prodId = new ProductId();
        MBSMarketIndicativePrice mbsMarketIndicativePrice = (MBSMarketIndicativePrice) transformationObject
                .getTransformationDataMap().get(MBSPServiceConstants.STREAM_PRICE);
        try {
            // TODO set the product identifier from product service
            prodId.setIdentifier(mbsTransactionPO.getProduct().getProductId().getIdentifier());
            prodId.setSourceType(mbsTransactionPO.getProduct().getProductId().getSourceType().toString());
            prodId.setType(mbsTransactionPO.getProduct().getProductId().getType().toString());
            mbsTransaction.setProductId(prodId);
            mbsTransaction.setProductNameCode(mbsTransactionPO.getProduct().getNameCode());
            mbsTransaction.setTradeAmount(MBSPortalUtils.convertToBigDecimal(mbsTransactionPO.getTradeAmount(), 13, 3));
            mbsTransaction.setTradeSettlementDate(MBSPortalUtils.convertToDateWithFormatter(
                    mbsTransactionPO.getTradeSettlementDate(), DateFormats.DATE_FORMAT_NO_TIMESTAMP));
            // CMMBSSTA01-1525 - Updating coupon rate to 2 decimals
            mbsTransaction.setTradeCouponRate(
                    MBSPortalUtils.convertToBigDecimal(mbsTransactionPO.getTradeCouponRate(), 5, 2));
            mbsTransaction.setStateType(mbsTransactionPO.getStateType().toString());
            // mbsTransaction
            // .setSubmissionDate(validateDate(transformationObject,
            // mbsTransactionPO.getTradeSettlementDate()));
            mbsTransaction.setCounterpartyTraderIdentifier(mbsTransactionPO.getLenderId());
            mbsTransaction.setTransReqNumber(mbsTransactionPO.getTransReqId());

            // CMMBSSTA01-1371 - Changes for TSP - Start
            if (transformationObject.getMBSRoleType().equals(MBSRoleType.TSP)) {
                mbsTransaction.setLenderSellerServiceNumber(mbsTransactionPO.getOboLenderSellerServicerNumber());
                mbsTransaction.setTspShortName(mbsTransactionPO.getTspShortName());
            }
            // CMMBSSTA01-1371 - Changes for TSP - End

            // CMMBSSTA01-1157 - Stream Pricing - Start
            if (Objects.nonNull(mbsMarketIndicativePrice)
                    && Objects.nonNull(mbsMarketIndicativePrice.getBidPricePercent())
                    && Objects.nonNull(mbsMarketIndicativePrice.getAskPricePercent())) {
                if (TradeBuySell.SELL.name().equals(mbsTransactionPO.getTradeBuySellType())) {
                    mbsTransaction.setStreamPricePercent(mbsMarketIndicativePrice.getBidPricePercent());
                    mbsTransaction.setStreamPricePercentTicksText(mbsMarketIndicativePrice.getBidPriceText());
                } else {
                    mbsTransaction.setStreamPricePercent(mbsMarketIndicativePrice.getAskPricePercent());
                    mbsTransaction.setStreamPricePercentTicksText(mbsMarketIndicativePrice.getAskPriceText());
                }
            }
            // CMMBSSTA01-1157 - Stream Pricing - End

            /*
             * CMMBSSTA01-1022: (Tech) Maintain versions for transaction request
             * Assign 0 here as we are going to increase the version at Dao
             * layer- Lender Open
             */
            mbsTransaction.setActiveVersion(0L);
            // CMMBSSTA01-787: Added source system when transaction is getting
            // created
            mbsTransaction.setSourceSystem(tradeServiceProperties.getSourceSystem().toUpperCase());
            // CMMBSSTA01-787: End -change

        } catch (MBSBaseException ex) {
            LOGGER.error("Error when transforming object", ex);
            throw ex;
        } catch (Exception ex) {
            LOGGER.error("Error when transforming in TransactionRequestTransformer", ex);
            throw new MBSSystemException("Error when transforming in TransactionRequestTransformer",
                    MBSExceptionConstants.SYSTEM_EXCEPTION, ex);
        }
        return mbsTransaction;
    }

}
