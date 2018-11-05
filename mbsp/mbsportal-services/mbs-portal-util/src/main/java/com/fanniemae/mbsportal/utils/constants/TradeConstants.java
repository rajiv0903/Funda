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


package com.fanniemae.mbsportal.utils.constants;

import java.math.BigInteger;

/**
 * @author g8uaxt Created on 9/8/2017.
 */

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 10, 2018
 * @Time 2:44:46 PM com.fanniemae.mbsportal.utils.constants TradeConstants.java
 * @Description: Added Trade Min and Max value
 */
public class TradeConstants {

    /**
     * The {@code String} value that is to be used for Trader Event User ID
     */
    public static final String TRADE_EVENT_USRE_NAME = "mbspeventtrader";
    
    public static final String TRADER_PUBLISH_USRE_NAME = "trader";
    
    /**
     * The {@code String} value that is to be used for Symbolic Representation of Eight value 4
     */
    public static final String EIGHTS_MIDDLE_VALUE_SIGN = "+";
    
    /**
     * The {@code String} value that is to be used for Middle Value of Eight
     */
    public static final String EIGHTS_MIDDLE_VALUE_NUMERIC = "4";
    
    /**
     * The {@code String} value that is to be used for Default Value of Eight
     */
    public static final String TRADE_TICK_DEFAULT_VALUE = "00";
    
    /**
     * The {@code BigInteger} value that is to be used Trade Minimum Value
     */
    public static final BigInteger TRADE_MIN_VALUE = new BigInteger("25000");
    
    /**
     * The {@code BigInteger} value that is to be used Trade Maximum Value
     */
    public static final BigInteger TRADE_MAX_VALUE = new BigInteger("250000000");

    /**
     * The {@code BigInteger} value that is to be used Trade Handle Minimum Value
     */
    public static final BigInteger TRADE_HANDLE_MIN_VALUE = new BigInteger("90");
    
    /**
     * The {@code BigInteger} value that is to be used Trade Handle Maximum Value
     */
    public static final BigInteger TRADE_HANDLE_MAX_VALUE = new BigInteger("115");

    /**
     * The {@code BigInteger} value that is to be used Trade Tick Minimum Value
     */
    public static final BigInteger TRADE_TICK_MIN_VALUE = new BigInteger("00");
    
    /**
     * The {@code BigInteger} value that is to be used Trade Tick Maximum Value
     */
    public static final BigInteger TRADE_TICK_MAX_VALUE = new BigInteger("31");

    /**
     * The {@code BigInteger} value that is to be used Trade Eight Maximum Value
     */
    public static final BigInteger TRADE_EIGHTS_MAX_VALUE = new BigInteger("7");
    
    /**
     * The {@code BigInteger} value that is to be used Trade Eight Minimum Value
     */
    public static final BigInteger TRADE_EIGHTS_MIN_VALUE = new BigInteger("0");
    
    /**
     * 
     * HTTP_STATUS_BAD_REQUEST String
     */
    public static final String HTTP_STATUS_BAD_REQUEST = "400";
    
    /**
     * 
     * HTTP_STATUS_INTERNAL_SERVER String
     */
    public static final String HTTP_STATUS_INTERNAL_SERVER = "500";

    public enum PRODUCT_TYPE {
        MBS("MBS");
        private String type;

        PRODUCT_TYPE(String type) {
            this.type = type;
        }

        public static PRODUCT_TYPE getEnum(String typeIn) {
            for (PRODUCT_TYPE type : PRODUCT_TYPE.values()) {
                if (type.getType().equalsIgnoreCase(typeIn))
                    return type;
            }
            return null;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    public enum PRODUCT_SOURCE_TYPE {
        PU("PU");
        private String srcType;

        PRODUCT_SOURCE_TYPE(String srcType) {
            this.srcType = srcType;
        }

        public static PRODUCT_SOURCE_TYPE getEnum(String srcType) {
            for (PRODUCT_SOURCE_TYPE type : PRODUCT_SOURCE_TYPE.values()) {
                if (type.getSrcType().equalsIgnoreCase(srcType))
                    return type;
            }
            return null;
        }

        public String getSrcType() {
            return srcType;
        }

        public void setSrcType(String srcType) {
            this.srcType = srcType;
        }

        @Override
        public String toString() {
            return this.srcType;
        }

    }

    public enum MktTermType {
        FN30(360);
        private int month;

        MktTermType(int month) {
            this.month = month;
        }

        public static MktTermType getEnum(int month) {
            for (MktTermType term : MktTermType.values()) {
                if (term.getTerm() == month)
                    return term;
            }
            return null;
        }

        public int getTerm() {
            return this.month;
        }

        @Override
        public String toString() {
            return String.valueOf(month);
        }
    }

}
