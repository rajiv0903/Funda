/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.api.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fanniemae.mbsportal.constants.AcceptedTradesBoolean;
import com.fanniemae.mbsportal.constants.RegionColumnList;
import com.fanniemae.mbsportal.gf.pojo.SortBy;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.DataExceptionMessage;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;

/**
 * Class for validation of transaction history input fields.
 *
 * @author e4umgc
 *
 */
public class TransactionHistoryPOValidator {

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionHistoryPOValidator.class);

    /**
     * Validates the Accepted trades field. can be true or false, but is
     * required for history for Export - it has to be false
     *
     * @param dataExceptionMessages
     * @param acceptedTrades
     * @param feature
     * @return
     */
    public static boolean validateAcceptedTrades(List<DataExceptionMessage> dataExceptionMessages,
            AcceptedTradesBoolean acceptedTrades, String feature) {
        DataExceptionMessage excpMsg = null;
        if (Objects.equals(acceptedTrades, null)) {
            if (MBSPServiceConstants.TRANS_HISTORY_EXPORT.equalsIgnoreCase(feature)) {
                excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_INVALID_INPUT);
                excpMsg.setFieldMessage("accepted_trade");
            } else if (MBSPServiceConstants.TRANS_HISTORY.equalsIgnoreCase(feature)) {
                excpMsg = new DataExceptionMessage(MBSExceptionConstants.PAGE_1001);
                excpMsg.setFieldMessage("accepted trade");
            }
        } else if (AcceptedTradesBoolean.empty.equals(acceptedTrades)) {
            if (MBSPServiceConstants.TRANS_HISTORY_EXPORT.equalsIgnoreCase(feature)) {
                excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_EMPTY_INPUT);
                excpMsg.setFieldMessage("accepted_trade");
            } else if (MBSPServiceConstants.TRANS_HISTORY.equalsIgnoreCase(feature)) {
                excpMsg = new DataExceptionMessage(MBSExceptionConstants.PAGE_1000);
                excpMsg.setFieldMessage("accepted trade");
            }
        } else if (MBSPServiceConstants.TRANS_HISTORY_EXPORT.equalsIgnoreCase(feature)
                && !MBSPServiceConstants.FALSE.equalsIgnoreCase(acceptedTrades.getName())) {
            excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_INVALID_INPUT);
            excpMsg.setFieldMessage("accepted_trade " + acceptedTrades.getName() + ". Should be 'false'");
        }

        if (null != excpMsg) {
            dataExceptionMessages.add(excpMsg);
        }
        return false;
    }

    /**
     * Validates the sort field - accepted values are any valid column, but is
     * required for history for Export - accepted value is 'submissionDate'
     *
     * @param excpMsgs
     * @param inputInvalid
     * @param errorText
     * @param sortBy
     * @return
     */
    public static boolean validateSortField(List<DataExceptionMessage> dataExceptionMessages, RegionColumnList sortBy,
            String feature) {
        DataExceptionMessage excpMsg = null;
        if (Objects.equals(sortBy, null)) {
            if (MBSPServiceConstants.TRANS_HISTORY_EXPORT.equalsIgnoreCase(feature)) {
                excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_INVALID_INPUT);
                excpMsg.setFieldMessage("sort_by");
            } else if (feature.equalsIgnoreCase("HISTORY")) {
                excpMsg = new DataExceptionMessage(MBSExceptionConstants.PAGE_1001);
                excpMsg.setFieldMessage("sort by");
            }
        } else if (RegionColumnList.empty.equals(sortBy)) {
            if (MBSPServiceConstants.TRANS_HISTORY_EXPORT.equalsIgnoreCase(feature)) {
                excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_EMPTY_INPUT);
                excpMsg.setFieldMessage("sort_by");
            } else if (feature.equalsIgnoreCase("HISTORY")) {
                excpMsg = new DataExceptionMessage(MBSExceptionConstants.PAGE_1000);
                excpMsg.setFieldMessage("sort by");
            }
        } else if (MBSPServiceConstants.TRANS_HISTORY_EXPORT.equalsIgnoreCase(feature)
                && !MBSPServiceConstants.SUBMISSION_DATE.equalsIgnoreCase(sortBy.name())) {
            excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_INVALID_INPUT);
            excpMsg.setFieldMessage("sort_by " + sortBy.name() + ". Should be 'submissionDate'");
        }

        if (null != excpMsg) {
            dataExceptionMessages.add(excpMsg);
        }
        return false;
    }

    /**
     * Validates the sort order - make sure it is either 'asc' or 'desc' for
     * history
     *
     * @param excpMsgs
     * @param inputInvalid
     * @param errorText
     * @param sortOrder
     * @return
     */
    public static boolean validateSortOrder(List<DataExceptionMessage> dataExceptionMessages, SortBy sortOrder,
            String feature) {
        DataExceptionMessage excpMsg = null;
        if (Objects.equals(sortOrder, null)) {
            if (MBSPServiceConstants.TRANS_HISTORY_EXPORT.equalsIgnoreCase(feature)) {
                excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_INVALID_INPUT);
                excpMsg.setFieldMessage("sort_order");
            } else if (feature.equalsIgnoreCase("HISTORY")) {
                excpMsg = new DataExceptionMessage(MBSExceptionConstants.PAGE_1001);
                excpMsg.setFieldMessage("sort order");
            }
        } else if (SortBy.empty.equals(sortOrder)) {
            if (MBSPServiceConstants.TRANS_HISTORY_EXPORT.equalsIgnoreCase(feature)) {
                excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_EMPTY_INPUT);
                excpMsg.setFieldMessage("sort_order");
            } else if (feature.equalsIgnoreCase("HISTORY")) {
                excpMsg = new DataExceptionMessage(MBSExceptionConstants.PAGE_1000);
                excpMsg.setFieldMessage("sort order");
            }
        }

        if (null != excpMsg) {
            dataExceptionMessages.add(excpMsg);
        }

        return false;
    }

    /**
     * Validates the page index. Must be greater than 0.
     *
     * @param excpMsgs
     * @param inputInvalid
     * @param errorText
     * @param sortOrder
     * @return
     */
    public static boolean validatePageIndex(List<DataExceptionMessage> dataExceptionMessages, Integer pageIndex,
            String feature) {
        DataExceptionMessage excpMsg = null;
        // TODO:: Needs to move error codes to constants file
        if (Objects.equals(pageIndex, null) || pageIndex == 0) {
            if (feature.equalsIgnoreCase("HISTORY")) {
                excpMsg = new DataExceptionMessage(MBSExceptionConstants.PAGE_1000);
            }
        } else if (pageIndex < 0) {
            excpMsg = new DataExceptionMessage(MBSExceptionConstants.PAGE_1001);
        }

        if (null != excpMsg) {
            excpMsg.setFieldMessage("page index");
            dataExceptionMessages.add(excpMsg);
        }

        return false;
    }

    /**
     * Validates the page size. Must be greater than 0.
     *
     * @param excpMsgs
     * @param inputInvalid
     * @param errorText
     * @param sortOrder
     * @return
     */
    public static boolean validatePageSize(List<DataExceptionMessage> dataExceptionMessages, Integer pageSize,
            String feature) {
        DataExceptionMessage excpMsg = null;
        if (Objects.equals(pageSize, null) || pageSize == 0) {
            if (feature.equalsIgnoreCase("HISTORY")) {
                excpMsg = new DataExceptionMessage(MBSExceptionConstants.PAGE_1000);
            }
        } else if (pageSize < 0) {
            if (feature.equalsIgnoreCase("HISTORY")) {
                excpMsg = new DataExceptionMessage(MBSExceptionConstants.PAGE_1001);
            }
        }
        if (null != excpMsg) {
            excpMsg.setFieldMessage("page size");
            dataExceptionMessages.add(excpMsg);
        }

        return false;
    }

    /**
     * Validates the export type - make sure it is either 'CSV' or 'EXCEL'
     *
     * @param dataExceptionMessages
     * @param exportType
     * @param feature
     * @return
     */
    public static boolean validateExportType(List<DataExceptionMessage> dataExceptionMessages, String exportType,
            String feature) {
        DataExceptionMessage excpMsg = null;
        if (StringUtils.isBlank(exportType)) {
            excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_EMPTY_INPUT);
            excpMsg.setFieldMessage("export_type");
        } else {
            exportType = exportType.trim();
            if (!MBSPServiceConstants.EXCEL.equalsIgnoreCase(exportType)
                    && !MBSPServiceConstants.CSV.equalsIgnoreCase(exportType)) {
                excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_INVALID_INPUT);
                excpMsg.setFieldMessage("export_type " + exportType + ". Should be 'EXCEL/CSV'");
            }
        }
        if (null != excpMsg) {
            dataExceptionMessages.add(excpMsg);
        }
        return false;
    }

    /**
     * Validates the date type - accepted value is 'submissionDate'
     *
     * @param dataExceptionMessages
     * @param dateType
     * @param feature
     * @return
     */
    public static boolean validateDateType(List<DataExceptionMessage> dataExceptionMessages, String dateType,
            String feature) {
        DataExceptionMessage excpMsg = null;
        if (StringUtils.isBlank(dateType)) {
            excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_EMPTY_INPUT);
            excpMsg.setFieldMessage("date type");
            dataExceptionMessages.add(excpMsg);
        } else if (!MBSPServiceConstants.SUBMISSION_DATE.equalsIgnoreCase(dateType.trim())) {
            excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_INVALID_INPUT);
            excpMsg.setFieldMessage("date type " + dateType.trim() + ". Should be ''submissionDate''");
            dataExceptionMessages.add(excpMsg);
        }
        return false;
    }

    /**
     * Validates the from and to dates for the export transaction history
     * functionality
     *
     * @param dataExceptionMessages
     * @param startDate
     * @param endDate
     * @param feature
     * @return
     */
    public static boolean validateDates(List<DataExceptionMessage> dataExceptionMessages, String startDate,
            String endDate, String feature) {
        LocalDate startDt = null;
        LocalDate endDt = null;
        DataExceptionMessage excpMsg = null;
        boolean startDtInvalid = false, endDtInvalid = false;

        if (StringUtils.isBlank(startDate)) {
            excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_EMPTY_INPUT);
            excpMsg.setFieldMessage("start date");
            dataExceptionMessages.add(excpMsg);
            startDtInvalid = true;
        } else {
            try {
                startDt = getLocalDateFromString(startDate.trim(), DateFormats.DATE_FORMAT_NO_TIMESTAMP);
            } catch (DateTimeParseException e) {
                excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_INVALID_DATE_FORMAT);
                excpMsg.setFieldMessage("start date " + startDate.trim());
                dataExceptionMessages.add(excpMsg);
                startDtInvalid = true;
                LOGGER.info(MBSPServiceConstants.MSG_CODE_INVALID_DATE_FORMAT
                        + " - Start Date format is invalid, correct format:ﾠﾠ'yyyy-MM-dd'.");
            }
        }

        if (!startDtInvalid) {
            if (LocalDate.now().isBefore(startDt)) {
                excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_FUTURE_DATE);
                excpMsg.setFieldMessage(" start_date " + startDate.trim());
                dataExceptionMessages.add(excpMsg);
                LOGGER.info(MBSPServiceConstants.MSG_CODE_FUTURE_DATE
                        + " Invalidﾠstart date, should be less than current date.");
            }
        }

        if (StringUtils.isBlank(endDate)) {
            excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_EMPTY_INPUT);
            excpMsg.setFieldMessage(" end_date");
            dataExceptionMessages.add(excpMsg);
            endDtInvalid = true;
        } else {
            try {
                endDt = getLocalDateFromString(endDate.trim(), DateFormats.DATE_FORMAT_NO_TIMESTAMP);

            } catch (DateTimeParseException e) {
                excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_INVALID_END_DATE_FORMAT);
                excpMsg.setFieldMessage(" end_date " + endDate.trim());
                dataExceptionMessages.add(excpMsg);
                endDtInvalid = true;
                LOGGER.info(MBSPServiceConstants.MSG_CODE_INVALID_END_DATE_FORMAT
                        + " End Date format is invalid,ﾠcorrect format:ﾠﾠ'yyyy-MM-dd'"); 
            }
        }

        if (!endDtInvalid) {
            if (LocalDate.now().isBefore(endDt)) {
                excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_FUTURE_DATE);
                excpMsg.setFieldMessage(" end_date " + endDate.trim());
                dataExceptionMessages.add(excpMsg);
                LOGGER.info(MBSPServiceConstants.MSG_CODE_FUTURE_DATE
                        + " Invalidﾠend date, should be less than current date."); 
            }
        }

        if (!startDtInvalid && !endDtInvalid) {
//        	System.out.println("validateDates method: " + "startDt: " + startDt + " endDt: " + endDt);

            if (startDt.isAfter(endDt)) {
//            	System.out.println("validateDates method startdate after endate");
                excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_FROM_DATE_AFTER_TO_DATE);
                excpMsg.setFieldMessage(startDt + " and " + endDt);
                dataExceptionMessages.add(excpMsg);
            }

            if (startDt.isBefore(endDt.minusDays(MBSPServiceConstants.THREE_SIXTY_FIVE))) {
                excpMsg = new DataExceptionMessage(MBSPServiceConstants.MSG_CODE_INVALID_DATE_RANGE);
                excpMsg.setFieldMessage(" ");
                dataExceptionMessages.add(excpMsg);
            }

        }
        return false;
    }

    /**
     * Converts String to Date
     *
     * @param date
     *            string
     * @return LocalDate
     * 
     */
    private static LocalDate getLocalDateFromString(String date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate javaDate = LocalDate.parse(date, formatter);
        return javaDate;
    }
}
