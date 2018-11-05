/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 *
 */

package com.fanniemae.mbsportal.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fanniemae.fnmpfj.gemfire.dao.PartitionResolverCompoundKey;
import com.fanniemae.mbsportal.id.ProductId;

/**
 * 
 * Class Name: MBSTransactionRequest Purpose : This is the domain class for the
 * Transaction Request
 * 
 * @author g8upjv
 * 
 *         07/14/2017 g8upjv Initial version. 07/27/2017 g8upjv Updated the type
 *         of productIdentifier field and
 * 
 */
//TODO: Add index to transReqNumber
public class MBSTransactionRequest extends MBSBaseEntity {

    private static final long serialVersionUID = -2086842154611065879L;

    /**
     * 
     * Transaction id
     */
    private String transReqNumber;

    /**
     * 
     * Product id
     */
    private ProductId productId;

    /**
     * The product name code value. Example: FN30 for Fannie Mae 30yr Product
     */
    private String productNameCode;

    /**
     * 
     * Amount set by lender
     */
    private BigDecimal tradeAmount;

    /**
     * 
     * Status showing whether the trade has been priced, accepted, rejected
     */
    private String stateType;

    /**
     * 
     * status column order
     */
    private Integer stateTypeOrder;

    /**
     * 
     * Price quoted by trader in decimal
     */
    private BigDecimal pricePercent;

    /**
     * 
     * Price quoted by trader in ticks
     */
    private String pricePercentTicksText;

    /**
     * 
     * Date when the trade was submitted by lender
     */
    private Date submissionDate;

    /**
     * 
     * Date when the trade was accepted
     */
    private Date acceptanceDate;

    /**
     * 
     * settlement date from pricing data
     */
    private Date tradeSettlementDate;

    /**
     * 
     * Coupon rate from pricing data
     */
    private BigDecimal tradeCouponRate;

    /**
     * 
     * Whether the trade is BUY or SELL
     */
    private String tradeBuySellType;

    /**
     * 
     * Counter party or Lender buy/sell
     */
    private String counterPartyBuySellType;

    /**
     * 
     * The trader id
     */
    private String tradeTraderIdentifierText;

    /**
     * 
     * Trader name - firstname lastname
     */
    private String traderName;

    /**
     * 
     * The lender id
     */
    private String counterpartyTraderIdentifier;

    /**
     * 
     * Lender name - firstname lastname
     */
    private String counterpartyTraderName;

    /**
     * 
     * entity name
     */
    private String dealerOrgName;

    /**
     * 
     * active version
     */
    private Long activeVersion;

    // CMMBSSTA01-787 - For blue/green
    /**
     * 
     * source system
     */
    private String sourceSystem;

    // CMMBSSTA01-1047 : Retry count
    /**
     * 
     * Stores the retry count for each record.
     */
    private String retryCount;

    /**
     * 
     * Stores on behalf of lender seller service number.
     */
    private String lenderSellerServiceNumber; // Set it from Profile for Lenders
                                              // and TSP

    // CMMBSSTA01-1373 - Adding TSP name - Start
    /**
     * 
     * Stores the tsp user short name
     */
    private String tspShortName;

    /**
     * 
     * tspSellerServiceNumber String
     */
    private String tspSellerServiceNumber;

    /**
     * 
     * lenderShortName String
     */
    private String lenderShortName;

    // CMMBSSTA01-1373 - Adding TSP name - End
    
    /**
     * 
     * Price streamed in decimal
     */
    private BigDecimal streamPricePercent;

    /**
     * 
     * Price streamed in ticks
     */
    private String streamPricePercentTicksText;

    /**
     * @see com.fanniemae.fnmpfj.gemfire.dao.BaseEntity#getId()
     */
    @Override
    public Serializable getId() {
        //return transReqNumber;
        return new PartitionResolverCompoundKey(0, this.tradeSettlementDate, this.transReqNumber);
    }

    /**
     * @return the transReqNumber
     */
    public String getTransReqNumber() {
        return transReqNumber;
    }

    /**
     * @param transReqNumber
     *            the transReqNumber to set
     */
    public void setTransReqNumber(String transReqNumber) {
        this.transReqNumber = transReqNumber;
    }

    /**
     * @return the productId
     */
    public ProductId getProductId() {
        return productId;
    }

    /**
     * @param productId
     *            the productId to set
     */
    public void setProductId(ProductId productId) {
        this.productId = productId;
    }

    public String getProductNameCode() {
        return productNameCode;
    }

    public void setProductNameCode(String productNameCode) {
        this.productNameCode = productNameCode;
    }

    /**
     * @return the tradeAmount
     */
    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    /**
     * @param tradeAmount
     *            the tradeAmount to set
     */
    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    /**
     * @return the stateType
     */
    public String getStateType() {
        return stateType;
    }

    /**
     * @param stateType
     *            the stateType to set
     */
    public void setStateType(String stateType) {
        this.stateType = stateType;
    }

    /**
     * @return the pricePercent
     */
    public BigDecimal getPricePercent() {
        return pricePercent;
    }

    /**
     * @param pricePercent
     *            the pricePercent to set
     */
    public void setPricePercent(BigDecimal pricePercent) {
        this.pricePercent = pricePercent;
    }

    /**
     * @return the pricePercentTicksText
     */
    public String getpricePercentTicksText() {
        return pricePercentTicksText;
    }

    /**
     * @param pricePercentTicksText
     *            the pricePercentTicksText to set
     */
    public void setPricePercentTicksText(String pricePercentTicksText) {
        this.pricePercentTicksText = pricePercentTicksText;
    }

    /**
     * @return the submissionDate
     */
    public Date getSubmissionDate() {
        return submissionDate;
    }

    /**
     * @param submissionDate
     *            the submissionDate to set
     */
    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    /**
     * @return the acceptanceDate
     */
    public Date getAcceptanceDate() {
        return acceptanceDate;
    }

    /**
     * @param acceptanceDate
     *            the acceptanceDate to set
     */
    public void setAcceptanceDate(Date acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    /**
     * @return the tradeSettlementDate
     */
    public Date getTradeSettlementDate() {
        return tradeSettlementDate;
    }

    /**
     * @param tradeSettlementDate
     *            the tradeSettlementDate to set
     */
    public void setTradeSettlementDate(Date tradeSettlementDate) {
        this.tradeSettlementDate = tradeSettlementDate;
    }

    /**
     * @return the tradeCouponRate
     */
    public BigDecimal getTradeCouponRate() {
        return tradeCouponRate;
    }

    /**
     * @param tradeCouponRate
     *            the tradeCouponRate to set
     */
    public void setTradeCouponRate(BigDecimal tradeCouponRate) {
        this.tradeCouponRate = tradeCouponRate;
    }

    /**
     * @return the tradeBuySellType
     */
    public String getTradeBuySellType() {
        return tradeBuySellType;
    }

    /**
     * @param tradeBuySellType
     *            the tradeBuySellType to set
     */
    public void setTradeBuySellType(String tradeBuySellType) {
        this.tradeBuySellType = tradeBuySellType;
    }

    /**
     * @return the tradeTraderIdentifierText
     */
    public String getTradeTraderIdentifierText() {
        return tradeTraderIdentifierText;
    }

    /**
     * @param tradeTraderIdentifierText
     *            the tradeTraderIdentifierText to set
     */
    public void setTradeTraderIdentifierText(String tradeTraderIdentifierText) {
        this.tradeTraderIdentifierText = tradeTraderIdentifierText;
    }

    /**
     * @return the counterpartyTraderIdentifier
     */
    public String getCounterpartyTraderIdentifier() {
        return counterpartyTraderIdentifier;
    }

    /**
     * @param counterpartyTraderIdentifier
     *            the counterpartyTraderIdentifier to set
     */
    public void setCounterpartyTraderIdentifier(String counterpartyTraderIdentifier) {
        this.counterpartyTraderIdentifier = counterpartyTraderIdentifier;
    }

    /**
     * 
     * @return Integer
     */
    public Integer getStateTypeOrder() {
        return stateTypeOrder;
    }

    /**
     * 
     * @param stateTypeOrder
     */
    public void setStateTypeOrder(Integer stateTypeOrder) {
        this.stateTypeOrder = stateTypeOrder;
    }

    /**
     * 
     * 
     * @return String
     */
    public String getPricePercentTicksText() {
        return pricePercentTicksText;
    }

    /**
     * 
     * 
     * @return String
     */
    public String getCounterPartyBuySellType() {
        return counterPartyBuySellType;
    }

    /**
     * 
     * 
     * @param counterPartyBuySellType
     */
    public void setCounterPartyBuySellType(String counterPartyBuySellType) {
        this.counterPartyBuySellType = counterPartyBuySellType;
    }

    /**
     * 
     * @return String
     */
    public String getTraderName() {
        return traderName;
    }

    /**
     * 
     * @param traderName
     */
    public void setTraderName(String traderName) {
        this.traderName = traderName;
    }

    /**
     * 
     * @return String
     */
    public String getCounterpartyTraderName() {
        return counterpartyTraderName;
    }

    /**
     * 
     * @param counterpartyTraderName
     */
    public void setCounterpartyTraderName(String counterpartyTraderName) {
        this.counterpartyTraderName = counterpartyTraderName;
    }

    /**
     * 
     * @return String
     */
    public String getDealerOrgName() {
        return dealerOrgName;
    }

    /**
     * 
     * @param dealerOrgName
     */
    public void setDealerOrgName(String dealerOrgName) {
        this.dealerOrgName = dealerOrgName;
    }

    /**
     * 
     * @return String
     */
    public Long getActiveVersion() {
        return activeVersion;
    }

    /**
     * 
     * @param activeVersion
     */
    public void setActiveVersion(Long activeVersion) {
        this.activeVersion = activeVersion;
    }

    /**
     * 
     * @return the sourceSystem
     */
    public String getSourceSystem() {
        return sourceSystem;
    }

    /**
     * 
     * @param sourceSystem
     *            the sourceSystem to set
     */
    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    /**
     * 
     * @return the retryCount
     */
    public String getRetryCount() {
        return retryCount;
    }

    /**
     * 
     * @param retryCount
     *            the retryCount to set
     */
    public void setRetryCount(String retryCount) {
        this.retryCount = retryCount;
    }

    /**
     *
     * @return the tspShortName
     */
    public String getTspShortName() {
        return tspShortName;
    }

    /**
     * 
     * @param tspShortName
     *            the tspShortName to set
     */
    public void setTspShortName(String tspShortName) {
        this.tspShortName = tspShortName;
    }

    /**
     * @return the lenderSellerServiceNumber
     */
    public String getLenderSellerServiceNumber() {
        return lenderSellerServiceNumber;
    }

    /**
     * @param lenderSellerServiceNumber
     *            the lenderSellerServiceNumber to set
     */
    public void setLenderSellerServiceNumber(String lenderSellerServiceNumber) {
        this.lenderSellerServiceNumber = lenderSellerServiceNumber;
    }

    /**
     * @return the tspSellerServiceNumber
     */
    public String getTspSellerServiceNumber() {
        return tspSellerServiceNumber;
    }

    /**
     * @param tspSellerServiceNumber
     *            the tspSellerServiceNumber to set
     */
    public void setTspSellerServiceNumber(String tspSellerServiceNumber) {
        this.tspSellerServiceNumber = tspSellerServiceNumber;
    }

    /**
     * @return the lenderShortName
     */
    public String getLenderShortName() {
        return lenderShortName;
    }

    /**
     * @param lenderShortName
     *            the lenderShortName to set
     */
    public void setLenderShortName(String lenderShortName) {
        this.lenderShortName = lenderShortName;
    }

    /**
     * @return the streamPricePercent
     */
    public BigDecimal getStreamPricePercent() {
        return streamPricePercent;
    }

    /**
     * @param streamPricePercent the streamPricePercent to set
     */
    public void setStreamPricePercent(BigDecimal streamPricePercent) {
        this.streamPricePercent = streamPricePercent;
    }

    /**
     * @return the streamPricePercentTicksText
     */
    public String getStreamPricePercentTicksText() {
        return streamPricePercentTicksText;
    }

    /**
     * @param streamPricePercentTicksText the streamPricePercentTicksText to set
     */
    public void setStreamPricePercentTicksText(String streamPricePercentTicksText) {
        this.streamPricePercentTicksText = streamPricePercentTicksText;
    }

    /**
     * 
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MBSTransactionRequest [transReqNumber=" + transReqNumber + ", productId=" + productId
                + ", productNameCode=" + productNameCode + ", tradeAmount=" + tradeAmount + ", stateType=" + stateType
                + ", stateTypeOrder=" + stateTypeOrder + ", pricePercent=" + pricePercent + ", pricePercentTicksText="
                + pricePercentTicksText + ", submissionDate=" + submissionDate + ", acceptanceDate=" + acceptanceDate
                + ", tradeSettlementDate=" + tradeSettlementDate + ", tradeCouponRate=" + tradeCouponRate
                + ", tradeBuySellType=" + tradeBuySellType + ", counterPartyBuySellType=" + counterPartyBuySellType
                + ", tradeTraderIdentifierText=" + tradeTraderIdentifierText + ", traderName=" + traderName
                + ", counterpartyTraderIdentifier=" + counterpartyTraderIdentifier + ", counterpartyTraderName="
                + counterpartyTraderName + ", dealerOrgName=" + dealerOrgName + ", activeVersion=" + activeVersion
                + ", sourceSystem=" + sourceSystem + ", retryCount=" + retryCount + ", lenderSellerServiceNumber="
                + lenderSellerServiceNumber + ", tspShortName=" + tspShortName + ", tspSellerServiceNumber="
                + tspSellerServiceNumber + ", lenderShortName=" + lenderShortName + ", streamPricePercent="
                + streamPricePercent + ", streamPricePercentTicksText=" + streamPricePercentTicksText + "]";
    }

}
