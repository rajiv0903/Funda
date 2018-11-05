/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author g8uaxt Created on 9/29/2017.
 */
@Component
@ConfigurationProperties("mbsp.ts")
public class TradeServiceProperties {
        
        /**
         * 
         * traderServiceUrl String[]
         */
        //@NotEmpty
        private String[] traderServiceUrl;
        /**
         * 
         * tbaType String
         */
        //@NotEmpty
        private String tbaType;
        /**
         * 
         * tradeConfirmedBy String
         */
        //@NotEmpty
        private String tradeConfirmedBy;
        /**
         * 
         * tradeConfirmedWith String
         */
        //@NotEmpty
        private String tradeConfirmedWith;
        /**
         * 
         * sourceSystem String
         */
        //@NotEmpty
        private String sourceSystem;
        /**
         * 
         * refreshUrl String
         */
        // @NotEmpty
        private String refreshUrl;
        /**
         * 
         * shakeOutTestUsers String[]
         */
        private String[] shakeOutTestUsers;
        /**
         * 
         * shakeoutPortfolio String
         */
        private String shakeoutPortfolio;
        /**
         * 
         * shakeOutCPartyName String
         */
        private String shakeOutCPartyName;
        
        /**
         * 
         * maxRetry maximum retry count
         */
        private String maxRetry;
        /**
         * 
         * intervalAfterMaxRetry Retry Interval after the max retry is reached. It should be in milli seconds.
         */
        private String intervalAfterMaxRetry;
        
        
        /**
         * 
         * @return String[]
         */
        public String[] getTraderServiceUrl() {
                return traderServiceUrl;
        }
        
        /**
         * 
         * @param traderServiceUrl the traderServiceUrl
         */
        public void setTraderServiceUrl(String[] traderServiceUrl) {
                this.traderServiceUrl = traderServiceUrl;
        }
        
        /**
         * 
         * @return String
         */
        public String getTbaType() {
                return tbaType;
        }
        
        /**
         * 
         * @param tbaType the tbaType
         */
        public void setTbaType(String tbaType) {
                this.tbaType = tbaType;
        }
        
        /**
         * 
         * @return String
         */
        public String getTradeConfirmedBy() {
                return tradeConfirmedBy;
        }
        
        /**
         * 
         * @param tradeConfirmedBy the tradeConfirmedBy
         */
        public void setTradeConfirmedBy(String tradeConfirmedBy) {
                this.tradeConfirmedBy = tradeConfirmedBy;
        }
        
        /**
         * 
         * @return String
         */
        public String getTradeConfirmedWith() {
                return tradeConfirmedWith;
        }
        
        /**
         * 
         * @param tradeConfirmedWith the tradeConfirmedWith
         */
        public void setTradeConfirmedWith(String tradeConfirmedWith) {
                this.tradeConfirmedWith = tradeConfirmedWith;
        }
        
        /**
         * 
         * @return String
         */
        public String getSourceSystem() {
                return sourceSystem;
        }
        
        /**
         * 
         * @param sourceSystem the sourceSystem
         */
        public void setSourceSystem(String sourceSystem) {
                this.sourceSystem = sourceSystem;
        }
        
       
        
        /**
         * 
         * @return String
         */
        public String getRefreshUrl() {
                return refreshUrl;
        }
        
        /**
         * 
         * @param refreshUrl the refreshUrl
         */
        public void setRefreshUrl(String refreshUrl) {
                this.refreshUrl = refreshUrl;
        }
        
        
        /**
         * 
         * @return String[]
         */
        public String[] getShakeOutTestUsers() {
                return shakeOutTestUsers;
        }
        
        /**
         * 
         * @param shakeOutTestUsers the shakeOutTestUsers
         */
        public void setShakeOutTestUsers(String[] shakeOutTestUsers) {
                this.shakeOutTestUsers = shakeOutTestUsers;
        }
        
        /**
         * 
         * @return String
         */
        public String getShakeoutPortfolio() {
                return shakeoutPortfolio;
        }
        
        /**
         * 
         * @param shakeoutPortfolio the shakeoutPortfolio
         */
        public void setShakeoutPortfolio(String shakeoutPortfolio) {
                this.shakeoutPortfolio = shakeoutPortfolio;
        }
        
        /**
         * 
         * @return String
         */
        public String getShakeOutCPartyName() {
                return shakeOutCPartyName;
        }
        
        /**
         * 
         * @param shakeOutCPartyName the shakeOutCPartyName
         */
        public void setShakeOutCPartyName(String shakeOutCPartyName) {
                this.shakeOutCPartyName = shakeOutCPartyName;
        }

        /**
         * @return the maxRetry
         */
        public String getMaxRetry() {
            return maxRetry;
        }

        /**
         * @param maxRetry the maxRetry to set
         */
        public void setMaxRetry(String maxRetry) {
            this.maxRetry = maxRetry;
        }

        /**
         * @return the intervalAfterMaxRetry
         */
        public String getIntervalAfterMaxRetry() {
            return intervalAfterMaxRetry;
        }

        /**
         * @param intervalAfterMaxRetry the intervalAfterMaxRetry to set
         */
        public void setIntervalAfterMaxRetry(String intervalAfterMaxRetry) {
            this.intervalAfterMaxRetry = intervalAfterMaxRetry;
        }
        
}