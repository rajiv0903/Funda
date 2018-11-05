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

/**
 * 
 * Model class for price
 * Created by g8upjv on 6/01/2018.
 */
public class MBSMarketIndicativePrice extends MBSBaseEntity {
    
    /**
     *
     * serialVersionUID long
     */
    private static final long serialVersionUID = 8715106937440850601L;
    
    /**
     * 
     * productNameCode String
     */
    private String productNameCode;
    
    /**
     * 
     * puNameCode String
     */
    private String puNameCode;
    
    /**
     * 
     * observationDate Long
     */
    private Long observationDate;
    
    /**
     * 
     * passThroughRate BigDecimal
     */
    private BigDecimal passThroughRate;
    
    /**
     * 
     * forward Integer
     */
    private Integer forward;
    
    /**
     * 
     * settlementDate Date
     */
    private Date settlementDate;
    
    /**
     * 
     * cutOffDate Date
     */
    private Date cutOffDate;
    
    /**
     * 
     * bidPricePercent BigDecimal
     */
    private BigDecimal bidPricePercent;
    
    /**
     * 
     * bidPriceText String
     */
    private String bidPriceText;
    
    /**
     * 
     * askPricePercent BigDecimal
     */
    private BigDecimal askPricePercent;
    
    /**
     * 
     * askPriceText String
     */
    private String askPriceText;
    
    /**
     * 
     * eventType String
     */
    private String eventType; //TBA or Roll
    
    /**
     * 
     * midPricePercent BigDecimal
     */
    private BigDecimal midPricePercent;
    
    /**
     * 
     * midPriceText String
     */
    private String midPriceText;
    
    /**
     * 
     * MBSMarketIndicativePrice constructor
     */
    public MBSMarketIndicativePrice() {
    }
    
    /**
     * 
     * @param productNameCode the productNameCode to set
     * @param puNameCode the puNameCode to set
     * @param observationDate the historyId to set 
     * @param passThroughRate the observationDate to set
     * @param forward the forward to set
     * @param settlementDate the settlementDate to set
     * @param cutOffDate the cutOffDate to set
     * @param bidPricePercent the bidPricePercent to set
     * @param bidPriceText the bidPriceText to set
     * @param askPricePercent the askPricePercent to set
     * @param askPriceText the askPriceText to set
     * @param eventType the eventType to set
     * @param midPricePercent the midPricePercent to set
     * @param midPriceText the midPriceText to set
     */
    public MBSMarketIndicativePrice(String productNameCode, String puNameCode, Long observationDate, BigDecimal passThroughRate, Integer forward, Date settlementDate,
            Date cutOffDate, BigDecimal bidPricePercent, String bidPriceText, BigDecimal askPricePercent, String askPriceText, 
            String eventType, BigDecimal midPricePercent, String midPriceText) {
        
       this.productNameCode = productNameCode;
       this.puNameCode = puNameCode;
       this.observationDate = observationDate;
       this.passThroughRate = passThroughRate;
       this.forward = forward;
       this.settlementDate = settlementDate;
       this.cutOffDate = cutOffDate;
       this.bidPricePercent = bidPricePercent;
       this.bidPriceText = bidPriceText;
       this.askPricePercent = askPricePercent;
       this.askPriceText = askPriceText;
       this.eventType = eventType;
       this.midPricePercent = midPricePercent;
       this.midPriceText = midPriceText;
    }
    
    
    /**
     * 
     * 
     * @return long
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * 
     * @return Serializable
     */
    @Override
    public Serializable getId() {
        return new PartitionResolverCompoundKey(0, this.getProductNameCode(), 
                this.getPassThroughRate(), this.getSettlementDate());
    }

    /**
     * 
     * @return the productNameCode String
     */
    public String getProductNameCode() {
        return productNameCode;
    }

    /**
     * 
     * @param productNameCode the productNameCode to set
     */
    public void setProductNameCode(String productNameCode) {
        this.productNameCode = productNameCode;
    }
    
    /**
     * 
     * @return String
     */
    public String getPuNameCode() {
        return puNameCode;
    }
    
    /**
     * 
     * @param puNameCode
     */
    public void setPuNameCode(String puNameCode) {
        this.puNameCode = puNameCode;
    }
    
    /**
     * 
     * @return the observationDate
     */
    public Long getObservationDate() {
        return observationDate;
    }

    /**
     * 
     * @param observationDate the observationDate to set
     */
    public void setObservationDate(Long observationDate) {
        this.observationDate = observationDate;
    }

    /**
     * 
     * @return the passThroughRate
     */
    public BigDecimal getPassThroughRate() {
        return passThroughRate;
    }

    /**
     * 
     * @param passThroughRate the passThroughRate to set
     */
    public void setPassThroughRate(BigDecimal passThroughRate) {
        this.passThroughRate = passThroughRate;
    }

    /**
     * 
     * @return the forward
     */
    public Integer getForward() {
        return forward;
    }

    /**
     * 
     * @param forward the forward to set
     */
    public void setForward(Integer forward) {
        this.forward = forward;
    }

    /**
     * 
     * @return the settlementDate
     */
    public Date getSettlementDate() {
        return settlementDate;
    }

    /**
     * 
     * @param settlementDate the settlementDate to set
     */
    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    /**
     * 
     * @return the cutOffDate
     */
    public Date getCutOffDate() {
        return cutOffDate;
    }

    /**
     * 
     * @param cutOffDate the cutOffDate to set
     */
    public void setCutOffDate(Date cutOffDate) {
        this.cutOffDate = cutOffDate;
    }

    /**
     * 
     * @return the bidPricePercent
     */
    public BigDecimal getBidPricePercent() {
        return bidPricePercent;
    }

    /**
     * 
     * @param bidPricePercent the bidPricePercent to set
     */
    public void setBidPricePercent(BigDecimal bidPricePercent) {
        this.bidPricePercent = bidPricePercent;
    }

    /**
     * 
     * @return the bidPriceText
     */
    public String getBidPriceText() {
        return bidPriceText;
    }

    /**
     * 
     * @param bidPriceText the bidPriceText to set
     */
    public void setBidPriceText(String bidPriceText) {
        this.bidPriceText = bidPriceText;
    }

    /**
     * 
     * @return the askPricePercent
     */
    public BigDecimal getAskPricePercent() {
        return askPricePercent;
    }

    /**
     * 
     * @param askPricePercent the askPricePercent to set
     */
    public void setAskPricePercent(BigDecimal askPricePercent) {
        this.askPricePercent = askPricePercent;
    }

    /**
     * 
     * @return the askPriceText
     */
    public String getAskPriceText() {
        return askPriceText;
    }

    /**
     * 
     * @param askPriceText the askPriceText to set
     */
    public void setAskPriceText(String askPriceText) {
        this.askPriceText = askPriceText;
    }

    /**
     * 
     * @return the eventType
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * 
     * @param eventType the eventType to set
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * 
     * @return the midPricePercent
     */
    public BigDecimal getMidPricePercent() {
        return midPricePercent;
    }

    /**
     * 
     * @param midPricePercent the midPricePercent to set
     */
    public void setMidPricePercent(BigDecimal midPricePercent) {
        this.midPricePercent = midPricePercent;
    }

    /**
     * 
     * @return the midPriceText
     */
    public String getMidPriceText() {
        return midPriceText;
    }

    /**
     * 
     * @param midPriceText the midPriceText to set
     */
    public void setMidPriceText(String midPriceText) {
        this.midPriceText = midPriceText;
    }
    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MBSMarketIndicativePrice{" + "productNameCode='" + productNameCode + '\'' + ", puNameCode='"
                + puNameCode + '\'' + ", observationDate=" + observationDate + ", passThroughRate=" + passThroughRate
                + ", forward=" + forward + ", settlementDate=" + settlementDate + ", cutOffDate=" + cutOffDate
                + ", bidPricePercent=" + bidPricePercent + ", bidPriceText='" + bidPriceText + '\''
                + ", askPricePercent=" + askPricePercent + ", askPriceText='" + askPriceText + '\'' + ", eventType='"
                + eventType + '\'' + ", midPricePercent=" + midPricePercent + ", midPriceText='" + midPriceText + '\''
                + "} " + super.toString();
    }
}
