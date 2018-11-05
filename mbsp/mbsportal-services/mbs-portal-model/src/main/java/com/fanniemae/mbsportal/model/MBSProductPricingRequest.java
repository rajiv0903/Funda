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

package com.fanniemae.mbsportal.model;/*

 reserved under the copyright laws of the United States and international
 conventions. Use of a copyright notice is precautionary only and does not
 imply publication or disclosure. This software contains confidential
 information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 is prohibited without the prior written consent of Fannie Mae.
 */

import com.fanniemae.fnmpfj.gemfire.dao.PartitionResolverCompoundKey;
import com.fanniemae.mbsportal.id.ProductId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by g8uaxt on 6/21/2017.
 */
public class MBSProductPricingRequest extends MBSBaseEntity {

    private static final long serialVersionUID = 7797570660271561263L;

    private Long pricingIndicativeIdentifier;
    private String pricingProductIdStr;
    private ProductId productId;
    private String productNameCode;
    private Date effectiveDate;
    private BigDecimal passThroughRate;
    private Integer mktTermType;
    private Date settlementDate;
    private Date cutOffDate;
    private BigDecimal guaranteeFeeRate;
    private BigDecimal mktPricePercent;
    private String buySellInd;
    private BigDecimal bidPricePercent;
    private String bidPriceText;
    private BigDecimal askPricePercent;
    private String askPriceText;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Long getPricingIndicativeIdentifier() {
        return pricingIndicativeIdentifier;
    }

    public void setPricingIndicativeIdentifier(Long pricingIndicativeIdentifier) {
        this.pricingIndicativeIdentifier = pricingIndicativeIdentifier;
    }

    public String getPricingProductIdStr() {
        return pricingProductIdStr;
    }

    public void setPricingProductIdStr(String pricingProductIdStr) {
        this.pricingProductIdStr = pricingProductIdStr;
    }

    public ProductId getProductId() {
        return productId;
    }

    public void setProductId(ProductId productId) {
        this.productId = productId;
    }

    public String getProductNameCode() {
        return productNameCode;
    }

    public void setProductNameCode(String productNameCode) {
        this.productNameCode = productNameCode;
    }

    @Override
    public Serializable getId() {
        return new PartitionResolverCompoundKey(0, this.productId.getProductIdStr(), this.getEffectiveDate(),
                this.getPassThroughRate(), this.getMktTermType(), this.getSettlementDate());
    }

    // TODO: check is this key for this.getSettlementDate()?
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public BigDecimal getPassThroughRate() {
        return passThroughRate;
    }

    public void setPassThroughRate(BigDecimal passThroughRate) {
        this.passThroughRate = passThroughRate;
    }

    public Integer getMktTermType() {
        return mktTermType;
    }

    public void setMktTermType(Integer mktTermType) {
        this.mktTermType = mktTermType;
    }

    public Date getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    public BigDecimal getGuaranteeFeeRate() {
        return guaranteeFeeRate;
    }

    public void setGuaranteeFeeRate(BigDecimal guaranteeFeeRate) {
        this.guaranteeFeeRate = guaranteeFeeRate;
    }

    public BigDecimal getMktPricePercent() {
        return mktPricePercent;
    }

    public void setMktPricePercent(BigDecimal mktPricePercent) {
        this.mktPricePercent = mktPricePercent;
    }

    public String getBuySellInd() {
        return buySellInd;
    }

    public void setBuySellInd(String buySellInd) {
        this.buySellInd = buySellInd;
    }

    public BigDecimal getBidPricePercent() {
        return bidPricePercent;
    }

    public void setBidPricePercent(BigDecimal bidPricePercent) {
        this.bidPricePercent = bidPricePercent;
    }

    public String getBidPriceText() {
        return bidPriceText;
    }

    public void setBidPriceText(String bidPriceText) {
        this.bidPriceText = bidPriceText;
    }

    public BigDecimal getAskPricePercent() {
        return askPricePercent;
    }

    public void setAskPricePercent(BigDecimal askPricePercent) {
        this.askPricePercent = askPricePercent;
    }

    public String getAskPriceText() {
        return askPriceText;
    }

    public void setAskPriceText(String askPriceText) {
        this.askPriceText = askPriceText;
    }

    /**
     * @return the cutOffDate
     */
    public Date getCutOffDate() {
        return cutOffDate;
    }

    /**
     * @param cutOffDate
     *            the cutOffDate to set
     */
    public void setCutOffDate(Date cutOffDate) {
        this.cutOffDate = cutOffDate;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MBSProductPricingRequest [pricingIndicativeIdentifier=" + pricingIndicativeIdentifier
                + ", pricingProductIdStr=" + pricingProductIdStr + ", productId=" + productId + ", productNameCode="
                + productNameCode + ", effectiveDate=" + effectiveDate + ", passThroughRate=" + passThroughRate
                + ", mktTermType=" + mktTermType + ", settlementDate=" + settlementDate + ", cutOffDate=" + cutOffDate
                + ", guaranteeFeeRate=" + guaranteeFeeRate + ", mktPricePercent=" + mktPricePercent + ", buySellInd="
                + buySellInd + ", bidPricePercent=" + bidPricePercent + ", bidPriceText=" + bidPriceText
                + ", askPricePercent=" + askPricePercent + ", askPriceText=" + askPriceText + ", createdOn=" + createdOn
                + ", lastUpdated=" + lastUpdated + ", lastUpdatedBy=" + lastUpdatedBy + ", createdBy=" + createdBy
                + "]";
    }

    
}
