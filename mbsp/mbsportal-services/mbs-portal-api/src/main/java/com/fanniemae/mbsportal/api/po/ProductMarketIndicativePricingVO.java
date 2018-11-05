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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jun 13, 2018
 * @File: com.fanniemae.mbsportal.pu.vo.ProductMarketIndicativePricingVO.java
 * @Revision:
 * @Description: ProductMarketIndicativePricingVO.java
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ProductMarketIndicativePricingVO implements Serializable {

    /**
     * 
     * serialVersionUID serialVersionUID
     */
    private static final long serialVersionUID = 2479372572720516153L;

    /**
     * 
     * ProductMarketIndicativePricingVO constructor
     */
    public ProductMarketIndicativePricingVO() {
    }

    /**
     * 
     * @param productNameCode
     *            the productNameCode
     * @param settlementDate
     *            the settlementDate
     * @param observationDateText
     *            the observationDateText
     * @param passThroughRate
     *            the passThroughRate
     * @param bidPrice
     *            the bidPrice
     * @param askPrice
     *            the askPrice
     */
    public ProductMarketIndicativePricingVO(String productNameCode, String settlementDate, String observationDateText,
            String passThroughRate, ProductMarketIndicativePriceVO bidPrice, ProductMarketIndicativePriceVO askPrice) {
        this.productNameCode = productNameCode;
        this.settlementDate = settlementDate;
        this.observationDateText = observationDateText;
        this.passThroughRate = passThroughRate;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
    }

    /**
     * 
     * productNameCode Product Name Code
     */
    private String productNameCode;;
    /**
     * 
     * settlementDate String
     */
    private String settlementDate;

    /**
     * 
     * observationDateText String
     */
    private String observationDateText;
    /**
     * 
     * passThroughRate String
     */
    private String passThroughRate;

    /**
     * 
     * bidPrice ProductMarketIndicativePriceVO
     */
    private ProductMarketIndicativePriceVO bidPrice;

    /**
     * 
     * askPrice ProductMarketIndicativePriceVO
     */
    private ProductMarketIndicativePriceVO askPrice;

    /**
     * 
     * @return productNameCode the productNameCode
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
     * @return settlementDate the settlementDate
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
     * @return observationDateText the observationDateText
     */
    public String getObservationDateText() {
        return observationDateText;
    }

    /**
     * 
     * @param observationDateText
     *            the observationDateText
     */
    public void setObservationDateText(String observationDateText) {
        this.observationDateText = observationDateText;
    }

    /**
     * 
     * @return passThroughRate the passThroughRate
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
     * @return bidPrice the bidPrice
     */
    public ProductMarketIndicativePriceVO getBidPrice() {
        return bidPrice;
    }

    /**
     * 
     * @param bidPrice
     *            the bidPrice
     */
    public void setBidPrice(ProductMarketIndicativePriceVO bidPrice) {
        this.bidPrice = bidPrice;
    }

    /**
     * 
     * @return askPrice the askPrice
     */
    public ProductMarketIndicativePriceVO getAskPrice() {
        return askPrice;
    }
    
    /**
     * 
     * 
     * @param askPrice
     */
    public void setAskPrice(ProductMarketIndicativePriceVO askPrice) {
        this.askPrice = askPrice;
    }
    
    /**
     * 
     * 
     * @author g8upjv
     *
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(Include.NON_NULL)
    public static class ProductMarketIndicativePriceVO {
        
        /**
         * 
         * ProductMarketIndicativePriceVO constructor
         */
        public ProductMarketIndicativePriceVO() {
        }

        /**
         * 
         * @param handle
         *            the handle
         * @param tick
         *            the tick
         */
        public ProductMarketIndicativePriceVO(String handle, String tick) {

            this.handle = handle;
            this.tick = tick;

        }

        /**
         * 
         * handle String
         */
        private String handle;
        /**
         * 
         * tick String
         */
        private String tick;

        /**
         * 
         * @return handle the handle
         */
        public String getHandle() {
            return handle;
        }

        /**
         * 
         * @param handle
         */
        public void setHandle(String handle) {
            this.handle = handle;
        }

        /**
         * 
         * @return tick the tick
         */
        public String getTick() {
            return tick;
        }

        /**
         * 
         * @param tick
         *            the tick
         */
        public void setTick(String tick) {
            this.tick = tick;
        }
        
        /**
         * 
         * @return String
         */
        @Override
        public String toString() {
            return "ProductMarketIndicativePriceVO [handle=" + handle + ", tick=" + tick + "]";
        }
    }

}
