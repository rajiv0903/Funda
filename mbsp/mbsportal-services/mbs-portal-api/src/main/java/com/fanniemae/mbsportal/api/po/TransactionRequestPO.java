/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.po;

import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.constants.StateType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Class Name: TransactionRequestPO Purpose : This class is the presentation
 * model for Transaction Request object
 * 
 * @author g8upjv
 * 
 */
@JsonInclude(Include.NON_NULL)
public class TransactionRequestPO {
    
    /**
     * 
     * transReqId String
     */
    private String transReqId;
    
    /**
     * 
     * tradeAmount String
     */
    private String tradeAmount;
    
    /**
     * 
     * product ProductPO
     */
    private ProductPO product;
    
    /**
     * 
     * tradeSettlementDate String
     */
    private String tradeSettlementDate;
    
    /**
     * 
     * tradeCouponRate String
     */ 
    private String tradeCouponRate;
    
    /**
     * 
     * tradeBuySellType String
     */
    private String tradeBuySellType;
    
    /**
     * 
     * stateType StateType
     */
    private StateType stateType;
    
    /**
     * 
     * pricePercent String
     */
    private String pricePercent;
    
    /**
     * 
     * pricePercentHandleText String
     */
    private String pricePercentHandleText;
    
    /**
     * 
     * pricePercentTicksText String
     */
    private String pricePercentTicksText;
    
    /**
     * 
     * submissionDate String
     */
    private String submissionDate;
    
    /**
     *
     * lastUpdatedDate String
     */
    private String lastUpdatedDate;
    
    /**
     * 
     * acceptanceDate String
     */
    private String acceptanceDate;
    
    /**
     * 
     * traderId String
     */
    private String traderId;
    
    /**
     * Lender Name String
     */
    private String lenderName;
    
    /**
     * 
     * lenderId String
     */
    private String lenderId;
    
    /**
     * 
     * lenderEntityShortName String
     */
    private String lenderEntityShortName;
    
    /**
     * 
     * traderName String
     */
    private String traderName;
    
    /**
     * 
     * activeVersion Long
     */
    private Long activeVersion;
    
    /**
     * 
     * oboLenderSellerServicerNumber String
     */
    private String oboLenderSellerServicerNumber;
    
    /**
     * 
     * tspShortName String
     */
    private String tspShortName;
    
    /**
     * 
     * entity name
     */
    private String dealerOrgName;
    
    /**
     * 
     * last published time
     */
    private String lastPublishTime;
    
    /**
     * 
     * streamPricePercent String
     */
    private String streamPricePercent;
    
    /**
     * 
     * streamPricePercentHandleText String
     */
    private String streamPricePercentHandleText;
    
    /**
     * 
     * streamPricePercentTicksText String
     */
    private String streamPricePercentTicksText;

    /**
     * 
     * @return String the tradeAmount
     */
    public String getTradeAmount() {
        return tradeAmount;
    }

    /**
     * 
     * 
     * @param tradeAmount
     *            the tradeAmount to set
     */
    public void setTradeAmount(String tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    /**
     * 
     * @return String the tradeSettlementDate
     */
    public String getTradeSettlementDate() {
        return tradeSettlementDate;
    }

    /**
     * 
     * @param tradeSettlementDate
     *            the tradeSettlementDate to set
     */
    public void setTradeSettlementDate(String tradeSettlementDate) {
        this.tradeSettlementDate = tradeSettlementDate;
    }

    /**
     * 
     * @return String the tradeCouponRate
     */
    public String getTradeCouponRate() {
        return tradeCouponRate;
    }

    /**
     * 
     * @param tradeCouponRate
     *            the tradeCouponRate to set
     */
    public void setTradeCouponRate(String tradeCouponRate) {
        this.tradeCouponRate = tradeCouponRate;
    }

    /**
     * 
     * @return String the tradeBuySellType
     */
    public String getTradeBuySellType() {
        return tradeBuySellType;
    }

    /**
     * 
     * @param tradeBuySellType
     *            the tradeBuySellType to set
     */
    public void setTradeBuySellType(String tradeBuySellType) {
        this.tradeBuySellType = tradeBuySellType;
    }

    /**
     * 
     * @return ProductPO the product
     */
    public ProductPO getProduct() {
        return product;
    }

    /**
     * 
     * @param product
     *            the product to set
     */
    public void setProduct(ProductPO product) {
        this.product = product;
    }

    /**
     * 
     * @return StateType the stateType
     */
    public StateType getStateType() {
        return stateType;
    }

    /**
     * 
     * @param stateType
     *            the stateType to set
     */
    public void setStateType(StateType stateType) {
        this.stateType = stateType;
    }

    /**
     * 
     * @return String the pricePercent
     */
    public String getPricePercent() {
        return pricePercent;
    }

    /**
     * 
     * @param pricePercent
     *            the pricePercent to set
     */
    public void setPricePercent(String pricePercent) {
        this.pricePercent = pricePercent;
    }

    /**
     * 
     * @return String the pricePercentTicksText
     */
    public String getPricePercentTicksText() {
        return pricePercentTicksText;
    }

    /**
     * 
     * @param pricePercentTicksText
     *            the pricePercentTicksText to set
     */
    public void setPricePercentTicksText(String pricePercentTicksText) {
        this.pricePercentTicksText = pricePercentTicksText;
    }

    /**
     * 
     * @return String the submissionDate
     */
    public String getSubmissionDate() {
        return submissionDate;
    }

    /**
     * 
     * @param submissionDate
     *            the submissionDate to set
     */
    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }
    
    /**
     * String the lastUpdatedDate
     * @return
     */
    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }
    
    /**
     * @param lastUpdatedDate
     * the lastUpdatedDate to set
     */
    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    /**
     * 
     * @return String the acceptanceDate
     */
    public String getAcceptanceDate() {
        return acceptanceDate;
    }

    /**
     * 
     * @param acceptanceDate
     *            the acceptanceDate to set
     */
    public void setAcceptanceDate(String acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    /**
     * 
     * @return String the traderId
     */
    public String getTraderId() {
        return traderId;
    }

    /**
     * 
     * @param traderId
     *            the traderId to set
     */
    public void setTraderId(String traderId) {
        this.traderId = traderId;
    }

    /**
     * 
     * @return String the lenderId
     */
    public String getLenderId() {
        return lenderId;
    }

    /**
     * 
     * @param counterpartyTraderIdentifier
     *            the counterpartyTraderIdentifier to set
     */
    public void setLenderId(String lenderId) {
        this.lenderId = lenderId;
    }

    public String getLenderName() {
		return lenderName;
	}

	public void setLenderName(String lenderName) {
		this.lenderName = lenderName;
	}

	/**
     * 
     * @return String the transReqId
     */
    public String getTransReqId() {
        return transReqId;
    }

    /**
     * 
     * @param transReqId
     *            the transReqId to set
     */
    public void setTransReqId(String transReqId) {
        this.transReqId = transReqId;
    }

    /**
     *
     * @return String
     */
    public String getPricePercentHandleText() {
        return pricePercentHandleText;
    }

    /**
     *
     * @param pricePercentHandleText
     */
    public void setPricePercentHandleText(String pricePercentHandleText) {
        this.pricePercentHandleText = pricePercentHandleText;
    }

    /**
     * @return String the lenderEntityShortName
     */
    public String getLenderEntityShortName() {
        return lenderEntityShortName;
    }

    /**
     * 
     * @param lenderEntityName
     *            the lenderEntityShortName to set
     */
    public void setLenderEntityShortName(String lenderEntityShortName) {
        this.lenderEntityShortName = lenderEntityShortName;
    }

    /**
     * 
     * @return String the traderName
     */
    public String getTraderName() {
        return traderName;
    }

    /**
     * 
     * @param traderName
     *            the traderName to set
     */
    public void setTraderName(String traderName) {
        this.traderName = traderName;
    }

    /**
     * 
     * @return Long the activeVersion
     */
    public Long getActiveVersion() {
        return activeVersion;
    }

    /**
     * 
     * @param activeVersion
     *            the activeVersion to set
     */
    public void setActiveVersion(Long activeVersion) {
        this.activeVersion = activeVersion;
    }

//    /**
//     * @return the oboLenderSellerServiceNumber
//     */
//    public String getOboLenderSellerServiceNumber()-- {
//        return oboLenderSellerServiceNumber;
//    }
//
//    /**
//     * @param oboLenderSellerServiceNumber the oboLenderSellerServiceNumber to set
//     */
//    public void setOboLenderSellerServiceNumber(String oboLenderSellerServiceNumber) {
//        this.oboLenderSellerServiceNumber = oboLenderSellerServiceNumber;
//    }
    
    
    /**
     * @return the oboLenderSellerServicerNumber
     */
    public String getOboLenderSellerServicerNumber() {
        return oboLenderSellerServicerNumber;
    }
    /**
     * @param oboLenderSellerServicerNumber the oboLenderSellerServicerNumber to set
     */
    public void setOboLenderSellerServicerNumber(String oboLenderSellerServicerNumber) {
        this.oboLenderSellerServicerNumber = oboLenderSellerServicerNumber;
    }

    /**
     * @return the tspShortName
     */
    public String getTspShortName() {
        return tspShortName;
    }

    /**
     * @param tspShortName the tspShortName to set
     */
    public void setTspShortName(String tspShortName) {
        this.tspShortName = tspShortName;
    }

    /**
     * 
     * @return the dealerOrgName
     */
    public String getDealerOrgName() {
        return dealerOrgName;
    }

    /**
     * 
     * @param dealerOrgName the dealerOrgName to set
     */
    public void setDealerOrgName(String dealerOrgName) {
        this.dealerOrgName = dealerOrgName;
    }
    
    
    /**
     * 
     * @return the lastPublishTime
     */
    public String getLastPublishTime() {
        return lastPublishTime;
    }

    /**
     * 
     * @param lastPublishTime the lastPublishTime to set
     */
    public void setLastPublishTime(String lastPublishTime) {
        this.lastPublishTime = lastPublishTime;
    }

    /**
     * @return the streamPricePercent
     */
    public String getStreamPricePercent() {
        return streamPricePercent;
    }

    /**
     * @param streamPricePercent the streamPricePercent to set
     */
    public void setStreamPricePercent(String streamPricePercent) {
        this.streamPricePercent = streamPricePercent;
    }

    /**
     * @return the streamPricePercentHandleText
     */
    public String getStreamPricePercentHandleText() {
        return streamPricePercentHandleText;
    }

    /**
     * @param streamPricePercentHandleText the streamPricePercentHandleText to set
     */
    public void setStreamPricePercentHandleText(String streamPricePercentHandleText) {
        this.streamPricePercentHandleText = streamPricePercentHandleText;
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
     *  (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TransactionRequestPO [transReqId=" + transReqId + ", tradeAmount=" + tradeAmount + ", product="
                + product + ", tradeSettlementDate=" + tradeSettlementDate + ", tradeCouponRate=" + tradeCouponRate
                + ", tradeBuySellType=" + tradeBuySellType + ", stateType=" + stateType + ", pricePercent="
                + pricePercent + ", pricePercentHandleText=" + pricePercentHandleText + ", pricePercentTicksText="
                + pricePercentTicksText + ", submissionDate=" + submissionDate + ", lastUpdatedDate=" + lastUpdatedDate
                + ", acceptanceDate=" + acceptanceDate + ", traderId=" + traderId + ", lenderId=" + lenderId
                + ", lenderEntityShortName=" + lenderEntityShortName + ", traderName=" + traderName + ", activeVersion="
                + activeVersion + ", oboLenderSellerServicerNumber=" + oboLenderSellerServicerNumber + ", tspShortName="
                + tspShortName + ", dealerOrgName=" + dealerOrgName + ", lastPublishTime=" + lastPublishTime
                + ", streamPricePercent=" + streamPricePercent + ", streamPricePercentHandleText="
                + streamPricePercentHandleText + ", streamPricePercentTicksText=" + streamPricePercentTicksText + "]";
    }

      
    

}
