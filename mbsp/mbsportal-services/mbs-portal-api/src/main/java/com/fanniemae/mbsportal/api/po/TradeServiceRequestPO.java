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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by g8uaxt on 8/30/2017.
 * @author g8upjv
 */
// TODO: move it as POM dependency
@SuppressWarnings("serial")
public class TradeServiceRequestPO implements Serializable {

    /**
     * 
     * quantity Long
     */
    private Long quantity; // mbs input
    
    /**
     * 
     * tradeSettlementDate Date
     */
    private Date tradeSettlementDate; // mbs input
    
    /**
     * 
     * tradeDate Date
     */
    private Date tradeDate; // mbs input
    
    /**
     * 
     * tradeType String
     */
    private String tradeType; // mbs input
    
    /**
     * 
     * tbaSettlementDate Date
     */
    private Date tbaSettlementDate; // mbs input
    
    /**
     * 
     * tbaTicker String
     */
    private String tbaTicker; // mbs input
    
    /**
     * 
     * tbaCoupon BigDecimal
     */
    private BigDecimal tbaCoupon; // mbs input
    
    /**
     * 
     * sourceTransactionId String
     */
    private String sourceTransactionId; // mbs input
    
    /**
     * 
     * pricePercent BigDeciaml
     */
    private BigDecimal pricePercent; // mbs input
    
    /**
     * 
     * counterPartyName String
     */
    private String counterPartyName; // default
    
    /**
     * 
     * traderName String
     */
    private String traderName; // default
    
    /**
     * 
     * portfolioName String
     */
    private String portfolioName;
    
    /**
     * 
     * tradeVariance BigDecimal
     */
    private BigDecimal tradeVariance; // default
    
    /**
     * 
     * tradeConfirmedBy String
     */
    private String tradeConfirmedBy; // default
    
    /**
     * 
     * tradeConfirmedWith String
     */
    private String tradeConfirmedWith; // default
    
    /**
     * 
     * commentGeneral String
     */
    private String commentGeneral; // default - blank
    
    /**
     * 
     * purpose String
     */
    private String purpose; // default - blank
    
    /**
     * 
     * tbaType String
     */
    private String tbaType; // default - MBSSF
    
    /**
     * 
     * sourceSystem String
     */
    private String sourceSystem; //MBSP
    
    /**
     * 
     * @return the quantity
     */
    public Long getQuantity() {
        return quantity;
    }
    
    /**
     * 
     * @param quantity the quantity to set
     */
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
    
    /**
     * 
     * @return Date the tradeSettlementDate
     */
    public Date getTradeSettlementDate() {
        return tradeSettlementDate;
    }
    
    /**
     * 
     * @param tradeSettlementDate the tradeSettlementDate to set
     */
    public void setTradeSettlementDate(Date tradeSettlementDate) {
        this.tradeSettlementDate = tradeSettlementDate;
    }
    
    /**
     * 
     * @return Date the tradeDate
     */
    public Date getTradeDate() {
        return tradeDate;
    }
    
    /**
     * 
     * @param tradeDate the tradeDate to set
     */
    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }
    
    /**
     * 
     * @return String the tradeType
     */
    public String getTradeType() {
        return tradeType;
    }
    
    /**
     * 
     * @param tradeType the tradeType to set
     */
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
    
    /**
     * 
     * @return Date the tbaSettlementDate
     */
    public Date getTbaSettlementDate() {
        return tbaSettlementDate;
    }
    
    /**
     * 
     * @param tbaSettlementDate the tbaSettlementDate to set
     */
    public void setTbaSettlementDate(Date tbaSettlementDate) {
        this.tbaSettlementDate = tbaSettlementDate;
    }
    
    /**
     * 
     * @return String the tbaTicker
     */
    public String getTbaTicker() {
        return tbaTicker;
    }
    
    /**
     * 
     * @param tbaTicker the tbaTicker to set
     */
    public void setTbaTicker(String tbaTicker) {
        this.tbaTicker = tbaTicker;
    }
    
    /**
     * 
     * @return BigDecimal the tbaCoupon
     */
    public BigDecimal getTbaCoupon() {
        return tbaCoupon;
    }
    
    /**
     * 
     * @param tbaCoupon the tbaCoupon to set
     */
    public void setTbaCoupon(BigDecimal tbaCoupon) {
        this.tbaCoupon = tbaCoupon;
    }
    
    /**
     * @return String the sourceTransactionId
     */
    public String getSourceTransactionId() {
        return sourceTransactionId;
    }
    
    /**
     * 
     * @param sourceTransactionId the sourceTransactionId to set
     */
    public void setSourceTransactionId(String sourceTransactionId) {
        this.sourceTransactionId = sourceTransactionId;
    }
    
    /**
     * 
     * @return BigDecimal the pricePercent
     */
    public BigDecimal getPricePercent() {
        return pricePercent;
    }
    
    /**
     * 
     * @param pricePercent the pricePercent to set
     */
    public void setPricePercent(BigDecimal pricePercent) {
        this.pricePercent = pricePercent;
    }
    
    /**
     * 
     * @return String the counterPartyName
     */
    public String getCounterPartyName() {
        return counterPartyName;
    }
    
    /**
     * 
     * @param counterPartyName the counterPartyName to set
     */
    public void setCounterPartyName(String counterPartyName) {
        this.counterPartyName = counterPartyName;
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
     * @param traderName the traderName to set
     */
    public void setTraderName(String traderName) {
        this.traderName = traderName;
    }
    
    /**
     * 
     * @return String the portfolioName
     */
    public String getPortfolioName() {
        return portfolioName;
    }
    
    /**
     * 
     * @param portfolioName the portfolioName to set
     */
    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }
    
    /**
     * 
     * @return BigDecimal the tradeVariance
     */
    public BigDecimal getTradeVariance() {
        return tradeVariance;
    }
    
    /**
     * 
     * @param tradeVariance the tradeVariance to set
     */
    public void setTradeVariance(BigDecimal tradeVariance) {
        this.tradeVariance = tradeVariance;
    }
    
    /**
     * 
     * @return String the tradeConfirmedBy
     */
    public String getTradeConfirmedBy() {
        return tradeConfirmedBy;
    }
    
    /**
     * 
     * @param tradeConfirmedBy the tradeConfirmedBy to set
     */
    public void setTradeConfirmedBy(String tradeConfirmedBy) {
        this.tradeConfirmedBy = tradeConfirmedBy;
    }
    
    /**
     * 
     * @return String the tradeConfirmedWith
     */
    public String getTradeConfirmedWith() {
        return tradeConfirmedWith;
    }
    
    /**
     * 
     * @param tradeConfirmedWith the tradeConfirmedWith to set
     */
    public void setTradeConfirmedWith(String tradeConfirmedWith) {
        this.tradeConfirmedWith = tradeConfirmedWith;
    }
    
    /**
     * 
     * @return String the commentGeneral
     */
    public String getCommentGeneral() {
        return commentGeneral;
    }
    
    /**
     * 
     * @param commentGeneral the commentGeneral to set
     */
    public void setCommentGeneral(String commentGeneral) {
        this.commentGeneral = commentGeneral;
    }
    
    /**
     * 
     * @return String the purpose
     */
    public String getPurpose() {
        return purpose;
    }
    
    /**
     * 
     * @param purpose the purpose to set
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    
    /**
     * 
     * @return String the tbaType
     */
    public String getTbaType() {
        return tbaType;
    }
    
    /**
     * 
     * @param tbaType the tbaType to set
     */
    public void setTbaType(String tbaType) {
        this.tbaType = tbaType;
    }
    
    /**
     * 
     * @return String the sourceSystem
     */
    public String getSourceSystem() {
        return sourceSystem;
    }
    
    /**
     * 
     * @param sourceSystem the sourceSystem to set
     */
    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }
    
    /**
     * 
     * @return String
     */
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TradeServiceRequestPO [quantity=" + quantity + ", tradeSettlementDate=" + tradeSettlementDate
                + ", tradeDate=" + tradeDate + ", tradeType=" + tradeType + ", tbaSettlementDate=" + tbaSettlementDate
                + ", tbaTicker=" + tbaTicker + ", tbaCoupon=" + tbaCoupon + ", sourceTransactionId="
                + sourceTransactionId + ", pricePercent=" + pricePercent + ", counterPartyName=" + counterPartyName
                + ", traderName=" + traderName + ", portfolioName=" + portfolioName + ", tradeVariance=" + tradeVariance
                + ", tradeConfirmedBy=" + tradeConfirmedBy + ", tradeConfirmedWith=" + tradeConfirmedWith
                + ", commentGeneral=" + commentGeneral + ", purpose=" + purpose + ", tbaType=" + tbaType
                + ", sourceSystem=" + sourceSystem + "]";
    }                    
    
}
