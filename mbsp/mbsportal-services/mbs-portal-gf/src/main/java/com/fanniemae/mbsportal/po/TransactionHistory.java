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

package com.fanniemae.mbsportal.po;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author g8uaxt Created on 12/14/2017.
 */

@SuppressWarnings("serial")
public class TransactionHistory implements Serializable{
        private String transReqId;
        private String tradeAmount;
        private ProductPO product;
        private String tradeSettlementDate;
        private String tradeCouponRate;
        private String tradeBuySellType;
        private String stateType;
        private String pricePercent;
        private String pricePercentHandleText;
        private String pricePercentTicksText;
        private String submissionDate;
        //TODO: Change lender name and trader name to lender and trader profile objects
        private String traderName;
        private String lenderName;
        private String tradeSrcId;
        private long tradeSubPortfolioId;
        private long tradeSrcPrimaryId;
        private String tradeSubPortfolioShortName;
        private String tradeDate;
        private String lenderEntityName;
        
        public String getTransReqId() {
                return transReqId;
        }
        
        public void setTransReqId(String transReqId) {
                this.transReqId = transReqId;
        }
        
        public String getTradeAmount() {
                return tradeAmount;
        }
        
        public void setTradeAmount(String tradeAmount) {
                this.tradeAmount = tradeAmount;
        }
        
        public ProductPO getProduct() {
                return product;
        }
        
        public void setProduct(ProductPO product) {
                this.product = product;
        }
        
        public String getTradeSettlementDate() {
                return tradeSettlementDate;
        }
        
        public void setTradeSettlementDate(String tradeSettlementDate) {
                this.tradeSettlementDate = tradeSettlementDate;
        }
        
        public String getTradeCouponRate() {
                return tradeCouponRate;
        }
        
        public void setTradeCouponRate(String tradeCouponRate) {
                this.tradeCouponRate = tradeCouponRate;
        }
        
        public String getTradeBuySellType() {
                return tradeBuySellType;
        }
        
        public void setTradeBuySellType(String tradeBuySellType) {
                this.tradeBuySellType = tradeBuySellType;
        }
        
        public String getStateType() {
                return stateType;
        }
        
        public void setStateType(String stateType) {
                this.stateType = stateType;
        }
        
        public String getPricePercent() {
                return pricePercent;
        }
        
        public void setPricePercent(String pricePercent) {
                this.pricePercent = pricePercent;
        }
        
        public String getPricePercentHandleText() {
                return pricePercentHandleText;
        }
        
        public void setPricePercentHandleText(String pricePercentHandleText) {
                this.pricePercentHandleText = pricePercentHandleText;
        }
        
        public String getPricePercentTicksText() {
                return pricePercentTicksText;
        }
        
        public void setPricePercentTicksText(String pricePercentTicksText) {
                this.pricePercentTicksText = pricePercentTicksText;
        }
        
        public String getSubmissionDate() {
                return submissionDate;
        }
        
        public void setSubmissionDate(String submissionDate) {
                this.submissionDate = submissionDate;
        }
        
        public String getTraderName() {
                return traderName;
        }
        
        public void setTraderName(String traderName) {
                this.traderName = traderName;
        }
        
        public String getLenderName() {
                return lenderName;
        }
        
        public void setLenderName(String lenderName) {
                this.lenderName = lenderName;
        }
        
        public String getTradeSrcId() {
                return tradeSrcId;
        }
        
        public void setTradeSrcId(String tradeSrcId) {
                this.tradeSrcId = tradeSrcId;
        }
        
        public long getTradeSubPortfolioId() {
                return tradeSubPortfolioId;
        }
        
        public void setTradeSubPortfolioId(long tradeSubPortfolioId) {
                this.tradeSubPortfolioId = tradeSubPortfolioId;
        }
        
        public long getTradeSrcPrimaryId() {
                return tradeSrcPrimaryId;
        }
        
        public void setTradeSrcPrimaryId(long tradeSrcPrimaryId) {
                this.tradeSrcPrimaryId = tradeSrcPrimaryId;
        }
        
        public String getTradeSubPortfolioShortName() {
                return tradeSubPortfolioShortName;
        }
        
        public void setTradeSubPortfolioShortName(String tradeSubPortfolioShortName) {
                this.tradeSubPortfolioShortName = tradeSubPortfolioShortName;
        }
        
        public String getTradeDate() {
                return tradeDate;
        }
        
        public void setTradeDate(String tradeDate) {
                this.tradeDate = tradeDate;
        }
        
        public String getLenderEntityName() {
                return lenderEntityName;
        }
        
        public void setLenderEntityName(String lenderEntityName) {
                this.lenderEntityName = lenderEntityName;
        }
        
        @Override
        public boolean equals(Object o) {
                if(this == o)
                        return true;
                if(!(o instanceof TransactionHistory))
                        return false;
                TransactionHistory that = (TransactionHistory) o;
                return Objects.equals(getTransReqId(), that.getTransReqId());
        }
        
        @Override
        public int hashCode() {
                return Objects.hash(getTransReqId());
        }
        
        @Override
        public String toString() {
                return "TransactionHistory{" + "transReqId='" + transReqId + '\'' + ", tradeAmount='" + tradeAmount
                        + '\'' + ", product=" + product + ", tradeSettlementDate='" + tradeSettlementDate + '\''
                        + ", tradeCouponRate='" + tradeCouponRate + '\'' + ", tradeBuySellType='" + tradeBuySellType
                        + '\'' + ", stateType='" + stateType + '\'' + ", pricePercent='" + pricePercent + '\''
                        + ", pricePercentHandleText='" + pricePercentHandleText + '\'' + ", pricePercentTicksText='"
                        + pricePercentTicksText + '\'' + ", submissionDate='" + submissionDate + '\'' + ", traderName='"
                        + traderName + '\'' + ", lenderName='" + lenderName + '\'' + ", tradeSrcId='" + tradeSrcId
                        + '\'' + ", tradeSubPortfolioId=" + tradeSubPortfolioId + ", tradeSrcPrimaryId="
                        + tradeSrcPrimaryId + ", tradeSubPortfolioShortName='" + tradeSubPortfolioShortName + '\''
                        + ", tradeDate='" + tradeDate + '\'' + ", lenderEntityName='" + lenderEntityName + '\'' + '}';
        }
}
