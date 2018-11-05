/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.utils.constants;

/**
 * Purpose : This class stores all the constants
 *
 * @author g8upjv
 *
 */
public class MBSPServiceConstants {

    /**
     * Name of the application.
     */
    public static final String APP_NAME = "MBS Trading";

    /**
     *
     * CORRELATION_ID_HEADER_NAME The header name for Fannie mae correlation id
     */
    public static final String CORRELATION_ID_HEADER_NAME = "Fannie-Mae-Correlation-ID";

    // TODO temp error messages
    /**
     *
     * INVALID_ACCEPT_HEADER Invalid accept header
     */
    public static final String INVALID_ACCEPT_HEADER = "Invalid Accept header parameter";

    /**
     *
     * INVALID_TRADER_ID This error message is set when trader id from input and
     * logged in user does not match
     */
    public static final String INVALID_TRADER_ID = "Trader Id in the request and logged in trader does not match.";

    /**
     *
     * TRANSACTION_REQUEST Constant for TransactionRequest class
     */
    public static final String TRANSACTION_REQUEST = "TransactionRequest";
    /**
     *
     * TRADE_REQUEST Constant for TradeRequest class
     */
    public static final String TRADE_REQUEST = "Trade";
    /**
     *
     * LENDER_PROFILE Constant for Lender Profile
     */
    public static final String LENDER_PROFILE = "LenderProfile";
    /**
     *
     * TRADER_PROFILE Constant for Trader Profile
     */
    public static final String TRADER_PROFILE = "TraderProfile";
    /**
     *
     * PRODUCT_PRICING Constant for Product Pricing
     */
    public static final String PRODUCT_PRICING = "ProductPricing";

    /**
     *
     * STREAM_PRICE Constant for Stream Pricing
     */
    public static final String STREAM_PRICE = "MBSMarketIndicativePrice";

    /**
     *
     * PARTY_INFO Constant for Party Info
     */
    public static final String PARTY_INFO = "PartyInfo";
    /**
     *
     * EXCEPTION_MESSAGES Constant for exception messages
     */
    public static final String EXCEPTION_MESSAGES = "ExceptionMessages";
    /**
     *
     * CUTTOFF_CALENDAR_TYPE Constant for Calendar type
     */
    public static final String CUTTOFF_CALENDAR_TYPE = "FRBNY";
    /**
     *
     * CUTTOFF_DAYS Constant for Cut off days
     */
    public static final int CUTTOFF_DAYS = -2;
    /**
     *
     * FIRSTNAME_LASTNAME_SEPERATOR Constant for name separator class
     */
    public static final String FIRSTNAME_LASTNAME_SEPERATOR = ",";

    /**
     *
     * RESPONSE_HEADER_SERVER_TIME_NAME Response Header Server Time Header Key
     * Name
     */
    public static final String RESPONSE_HEADER_SERVER_TIME_NAME = "x-server-time";

    public static final String RESPONSE_ACCESS_CONTROL_EXPOSE_HEADER_NAME = "access-control-expose-headers";

    /**
     *
     * NO_MATCHING_PRODUCT String
     */
    public static final String NO_MATCHING_PRODUCT = "There is no matching product in Gemfire";

    /**
     *
     * NO_MATCHING_PRODUCT_PRICING String
     */
    public static final String NO_MATCHING_PRODUCT_PRICING = "There is no matching product pricing in Gemfire";

    /**
     *
     * NO_STATETYPE_LIST String
     */
    public static final String NO_STATETYPE_LIST = "There are no state type list being passed";

    /**
     *
     * NOT_ELIGIBLE_STATUS String
     */
    public static final String NOT_ELIGIBLE_STATUS = " is not eligible Status Type";

    /**
     *
     * NO_DATA String
     */
    public static final String NO_DATA = " There is no data for the requested id";

    /**
     *
     * EXCPETION_VALIDATE_SOURCE_SYSTEM String
     */
    public static final String EXCPETION_VALIDATE_SOURCE_SYSTEM = " Exception occured when validating source system.";

    // Transaction History Export constants

    /**
     * EXPORT_HEADER String
     */
    public static final String EXPORT_HEADER = "Fannie Mae Transaction History";

    /**
     * TO_DATE String
     */
    public static final String TO_DATE = "To";

    /**
     * FROM_DATE String
     */
    public static final String FROM_DATE = "From";

    /**
     * the CSV_LINE_BEGIN constant
     */
    public static final String CSV_LINE_BEGIN = "\"";

    /**
     * the CSV_SEPARATOR constant
     */
    public static final String CSV_SEPARATOR = "\",\"";

    /**
     * the CSV_LINE_SEPARATOR constant
     */
    public static final String CSV_LINE_SEPARATOR = "\"\n";

    /**
     * the CSV_COMMA constant
     */
    public static final String CSV_COMMA = ",";

    /**
     * the CSV_LINE_BREAK constant
     */
    public static final String CSV_LINE_BREAK = "\n";

    /**
     * the NO_RESULTS_FOUND constant
     */
    public static final String NO_RESULTS_FOUND = "NO RESULTS FOUND";

    // EXPORT transaction history constants

    /**
     * the BAD_INPUT constant
     */
    public static final String BAD_INPUT = "BAD_INPUT";
    /**
     * the THREE_SIXTY_FIVE constant
     */
    public static final int THREE_SIXTY_FIVE = 365;
    /**
     * the INVALID_SORT_FIELD constant
     */
    public static final String INVALID_SORT_FIELD = "Invalid sort field";
    /**
     * the INVALID_SORT_ORDER constant
     */
    public static final String INVALID_SORT_ORDER = "Invalid sort order";
    /**
     * the INVALID_EXPORT_TYPE constant
     */
    public static final String INVALID_EXPORT_TYPE = "Invalid export type";
    /**
     * the INVALID_DATE_TYPE constant
     */
    public static final String INVALID_DATE_TYPE = "Invalid date type";
    /**
     * the SUBMISSION_DATE constant
     */
    public static final String SUBMISSION_DATE = "submissionDate";
    /**
     * the ASC constant
     */
    public static final String ASC = "ASC";
    /**
     * the DESC constant
     */
    public static final String DESC = "DESC";
    /**
     * the CSV constant
     */
    public static final String CSV = "CSV";
    /**
     * the EXCEL constant
     */
    public static final String EXCEL = "EXCEL";
    /**
     * the FALSE constant
     */
    public static final String FALSE = "false";

    public static final String TRANS_HISTORY = "HISTORY";

    public static final String TRANS_HISTORY_EXPORT = "HISTORY_EXPORT";

    // message Codes for transaction history Export errors

    /**
     * the ERR_MSG_EMPTY_INPUT constant
     */
    public static final String ERR_MSG_EMPTY_INPUT = "Input parameter is empty.";
    /**
     * the ERR_MSG_INVALID_INPUT constant
     */
    public static final String ERR_MSG_INVALID_INPUT = "Invalid input parameter";
    /**
     * the ERR_MSG_FROM_DATE_AFTER_TO_DATE constant
     */
    public static final String ERR_MSG_FROM_DATE_AFTER_TO_DATE = "Start date cannot be greater than end date";
    /**
     * the ERR_MSG_INVALID_DATE_FORMAT constant
     */
    public static final String ERR_MSG_INVALID_DATE_FORMAT = "Start Date and End Date format should be yyyy-MM-dd";
    /**
     * the ERR_MSG_INVALID_DATE_RANGE constant
     */
    public static final String ERR_MSG_INVALID_DATE_RANGE = "Invalid date range, should not be more than 365 days.";
    /**
     * the ERR_MSG_INVALID_END_DATE constant
     */
    public static final String ERR_MSG_INVALID_END_DATE = "Invalid end date, should not be greater than current date.";

    /**
     * the ERR_MSG_INVALID_START_DATE constant
     */
    public static final String ERR_MSG_INVALID_START_DATE = "Start date cannot be greater thanﾠtoday's date.ﾠ";

    /**
     * the MSG_CODE_EMPTY_INPUT constant
     */
    public static final String MSG_CODE_EMPTY_INPUT = "EXPT_1000";
    /**
     * the MSG_CODE_INVALID_INPUT constant
     */
    public static final String MSG_CODE_INVALID_INPUT = "EXPT_1001";
    /**
     * the MSG_CODE_FROM_DATE_AFTER_TO_DATE constant
     */
    public static final String MSG_CODE_FROM_DATE_AFTER_TO_DATE = "EXPT_1002";
    /**
     * the MSG_CODE_INVALID_DATE_FORMAT constant
     */
    public static final String MSG_CODE_INVALID_DATE_FORMAT = "EXPT_1003";
    /**
     * the MSG_CODE_INVALID_DATE_RANGE constant
     */
    public static final String MSG_CODE_INVALID_DATE_RANGE = "EXPT_1004";
    /**
     * the MSG_CODE_INVALID_END_DATE constant
     */

    public static final String MSG_CODE_INVALID_END_DATE_FORMAT = "EXPT_1004";
    public static final String MSG_CODE_FUTURE_DATE = "EXPT_1005";

    public static final String LOCAL = "LOCAL";

    public static final String DEFAULT = "DEFAULT";

    public static final String CSV_EXTENSION = ".csv";

    public static final String XLSX_EXTENSION = ".xlsx";

    public static final String BUY_SELL_COMMENT = "Note: Buy/Sell displayed is from the perspective of ";
    
    public static final Character yesChar = 'Y';
    
    public static final Character noChar = 'N';

}