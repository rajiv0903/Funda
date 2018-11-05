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

package com.fanniemae.mbsportal.constants;

/**
 * Created by g8uaxt on 8/3/2017.
 * 
 * @author g8uaxt
 */
public class DAOConstants {

    /**
     * if Max sequence is not being passed create with the default one
     */
    public static final Integer SEQUENCE_DAFULT = 99999;

    /**
     * if Max sequence is not being passed create with the default one
     */
    public static final Integer SEQUENCE_TWO_MILLIONS = 2000000;
    
    
    /**
     * 
     * AUTH_TOKEN TS auth token
     */
    public static final String AUTH_TOKEN = "ts.authKey";
    /**
     * 
     * SESSION_ID TS session id
     */
    public static final String SESSION_ID = "ts.sessionId";
    
    /**
     * 
     * SESSION_ID_PARENT_KEY TS session id parent key
     */
    public static final String SESSION_ID_PARENT_KEY = "TS";
    
    
    /**
     * 
     * PRICE_DOWNTIME_THRESHOLD_IN_MILLISECONDS PRICE_DOWNTIME_THRESHOLD_IN_MILLISECONDS (Milliseconds)
     */
    public static final String PRICE_DOWNTIME_THRESHOLD_IN_MILLISECONDS = "priceDownTimeThreshold";
    
    /**
     * 
     * PRICE_DOWNTIME_THRESHOLD_IN_MILLISECONDS_PARENT_KEY PU PRICE_DOWNTIME_THRESHOLD_IN_MILLISECONDS_PARENT_KEY parent key
     */
    public static final String PRICE_DOWNTIME_THRESHOLD_IN_MILLISECONDS_PARENT_KEY = "PU";
    
    /**
     * 
     * PU STREAMING_INTERNAL_URL String
     */
    public static final String STREAMING_INTERNAL_URL = "streamingInternalUrl";

    /**
     * 
     * @author g8upjv
     *
     */
    public enum IDTypes {
        TRANSACTION_ID("MBSP_TransactionRequestID", 1), PRODUCT_ID("MBSP_ProductID", 2), PRODUCT_PRICING_ID(
                "MBSP_ProductPricingID",
                3), MARKET_INDICATIVE_PRICE_HISTORY_ID("MBSP_MarketIndicativePriceHistoryID", 4);

        private final String seqName;
        private final int id;

        /**
         * 
         * @param seqName
         * @param id
         */
        IDTypes(String seqName, int id) {
            this.seqName = seqName;
            this.id = id;
        }

        /**
         * 
         * @return String
         */
        public String getName() {
            return seqName;
        }

        /**
         * 
         * @return int
         */
        public int getId() {
            return id;
        }
    }

    /**
     * 
     * @author g8upjv
     *
     */
    public enum MonthType {

        JANUARY(0, "A"), FEBRUARY(1, "B"), MARCH(2, "C"), APRIL(3, "D"), MAY(4, "E"), JUNE(5, "F"), JULY(6,
                "G"), AUGUST(7, "H"), SEPTEMBER(8, "I"), OCTOBER(9, "J"), NOVEMBER(10, "K"), DECEMBER(11, "L");

        private final int monthCode;
        private final String monthValue;

        /**
         * 
         * @param monthCode
         * @param monthValue
         */
        private MonthType(int monthCode, String monthValue) {
            this.monthCode = monthCode;
            this.monthValue = monthValue;
        }

        /**
         * 
         * @param month
         * @return
         */
        public static String getMonthCode(int month) {

            for (MonthType monthType : MonthType.values()) {
                if (monthType.getMonthCode() == month) {
                    return monthType.getMonthValue();
                }
            }
            return "";
        }

        /**
         * @return the monthCode
         */
        public int getMonthCode() {
            return monthCode;
        }

        /**
         * @return the monthValue
         */
        public String getMonthValue() {
            return monthValue;
        }
    }
}
