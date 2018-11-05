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

/**
 * @author g8uaxt Created on 9/11/2017.
 */

@SuppressWarnings("serial")
public class ProductPricingPO implements Serializable {

    /**
     * 
     * productId ProductIdPO
     */
    private ProductIdPO productId;

    /**
     * 
     * productNameCode String
     */
    private String productNameCode;

    /**
     * 
     * passThroughRate String
     */
    private String passThroughRate;

    /**
     * 
     * marketTermType int
     */
    private int marketTermType;

    /**
     * 
     * settlementDate String
     */
    private String settlementDate;

    /**
     * 
     * cutOffDate String
     */
    private String cutOffDate;

    /**
     * 
     * effectiveDate String
     */
    private String effectiveDate;

    /**
     * 
     * isActive boolean
     */
    private boolean isActive;

    /**
     * 
     * BuySellIndicator String
     */
    private String BuySellIndicator;

    /**
     * 
     * BuySellIndicator String
     */
    private boolean markAsDelete;

    /**
     * 
     * @return ProductIdPO
     */
    public ProductIdPO getProductId() {
        return productId;
    }

    /**
     * 
     * @param productId
     *            the productId
     */
    public void setProductId(ProductIdPO productId) {
        this.productId = productId;
    }

    /**
     *
     * @return String
     */
    public String getProductNameCode() {
        return productNameCode;
    }

    /**
     * 
     * @param productNameCode
     *            the productNameCode
     */
    public void setProductNameCode(String productNameCode) {
        this.productNameCode = productNameCode;
    }

    /**
     * 
     * @return String
     */
    public String getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * 
     * @param effectiveDate
     *            the effectiveDate
     */
    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * 
     * @return String
     */
    public String getPassThroughRate() {
        return passThroughRate;
    }

    /**
     * 
     * @param passThroughRate
     *            the passThroughRate
     */
    public void setPassThroughRate(String passThroughRate) {
        this.passThroughRate = passThroughRate;
    }

    /**
     * 
     * @return int
     */
    public int getMarketTermType() {
        return marketTermType;
    }

    /**
     * 
     * @param marketTermType
     *            the marketTermType
     */
    public void setMarketTermType(int marketTermType) {
        this.marketTermType = marketTermType;
    }

    /**
     * 
     * @return String
     */
    public String getSettlementDate() {
        return settlementDate;
    }

    /**
     * 
     * @param settlementDate
     *            the settlementDate
     */
    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    /**
     * 
     * @return String
     */
    public String getBuySellIndicator() {
        return BuySellIndicator;
    }

    /**
     * 
     * @param buySellIndicator
     *            the buySellIndicator
     */
    public void setBuySellIndicator(String buySellIndicator) {
        BuySellIndicator = buySellIndicator;
    }

  

    /**
     * 
     * @return String the cutOffDate
     */
    public String getCutOffDate() {
        return cutOffDate;
    }

    /**
     * 
     * @param cutOffDate
     *            the cutOffDate to set
     */
    public void setCutOffDate(String cutOffDate) {
        this.cutOffDate = cutOffDate;
    }

    /**
     * 
     * @return boolean the isActive
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * 
     * @param isActive
     *            the isActive to set
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * 
     * @return boolean the markAsDelete
     */
    public boolean isMarkAsDelete() {
        return markAsDelete;
    }

    /**
     * 
     * @param markAsDelete
     *            the markAsDelete to set
     */
    public void setMarkAsDelete(boolean markAsDelete) {
        this.markAsDelete = markAsDelete;
    }
    
    /**
     * 
     * @return String
     */
    @Override
    public String toString() {
        return "ProductPricingPO [productId=" + productId + ", productNameCode=" + productNameCode + ", isActive="
                + isActive + ", passThroughRate=" + passThroughRate + ", marketTermType=" + marketTermType
                + ", settlementDate=" + settlementDate + ", cutOffDate=" + cutOffDate + ", effectiveDate="
                + effectiveDate + ", BuySellIndicator=" + BuySellIndicator  + ", markAsDelete=" + markAsDelete +"]";
    }

}
