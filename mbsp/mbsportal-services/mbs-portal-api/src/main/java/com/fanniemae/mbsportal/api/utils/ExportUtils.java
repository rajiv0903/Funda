/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fanniemae.mbsportal.api.constants.TransactionHistoryHeaders;
import com.fanniemae.mbsportal.api.po.TransactionHistoryPO;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * Util class for creating export data in csv or Excel spreadsheet formats.
 * 
 * 
 * @author e3uikb
 *
 */
public class ExportUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportUtils.class);
    private static final String IO_ERROR = "An IO error occured while writing the output";
    private static final String CREATED_BY = "Created by - ";
    private static final String CREATED_ON = "Create Date - ";

    /**
     * Title row containing "MBS Trading"
     */
    private static final int TITLE_ROW_NUM = 0;

    /**
     * Subtitle Row number containing "Fannie Mae Transaction History"
     */
    private static final int SUB_TITLE_ROW_NUM = 1;

    /**
     * Row number containing: To date
     */
    private static final int TO_FROM_DATE_ROW_NUM = 2;

    private static final int NOTE_ROW_NUM = 3;

    /**
     * Row number containing: Header row. note that the first row with data will
     * immediately follow this one.
     */
    private static final int HEADER_ROW_NUM = 5;

    private static final int MERGE_BEGIN = 0;
    private static final int MERGE_END = 13;

    private static enum CSV_CELL_TYPE {
        FIRST, MIDDLE, END;
    }

    /**
     * 
     * get a byte array of the transaction history info. in CSV format
     * 
     * @param transHistPOLst
     * @param toDate
     * @param fromDate
     * @return
     */
    public static byte[] getCSVFromHistory(List<TransactionHistoryPO> transHistPOLst, Date fromDate, Date toDate,
            String userName, MBSRoleType mbsRoleType, String org) throws MBSBaseException {
        LOGGER.debug("Starts getCSVFromHistory method");
        StringBuilder csvOutput = new StringBuilder();
        if (mbsRoleType.equals(MBSRoleType.TRADER)) {
            org = "Fannie Mae";
        }

        csvOutput = populateHeaderInfo(csvOutput, toDate, fromDate, userName, mbsRoleType, org);
        if (transHistPOLst == null || transHistPOLst.size() == 0) {
            csvOutput.append(MBSPServiceConstants.CSV_LINE_BEGIN);
            csvOutput.append(MBSPServiceConstants.NO_RESULTS_FOUND);
            csvOutput.append(MBSPServiceConstants.CSV_LINE_SEPARATOR);
        } else {
            csvOutput = populateData(csvOutput, transHistPOLst, mbsRoleType);
        }
        LOGGER.debug("Ends getCSVFromHistory method");
        return csvOutput.toString().getBytes();

    }

    /**
     * 
     * populate the header portion of the CSV
     * 
     * @param csvOutput
     * @param toDate
     * @param fromDate
     * @return
     */
    private static StringBuilder populateHeaderInfo(StringBuilder csvOutput, Date toDate, Date fromDate,
            String userName, MBSRoleType mbsRoleType, String org) throws MBSBaseException {

        csvOutput.append(MBSPServiceConstants.CSV_LINE_BEGIN);
        csvOutput.append(MBSPServiceConstants.APP_NAME);
        csvOutput.append(MBSPServiceConstants.CSV_SEPARATOR);
        csvOutput.append(CREATED_BY);
        csvOutput.append(MBSPServiceConstants.CSV_SEPARATOR);
        csvOutput.append(userName);
        csvOutput.append(MBSPServiceConstants.CSV_LINE_SEPARATOR);
        csvOutput.append(MBSPServiceConstants.CSV_LINE_BEGIN);
        csvOutput.append(MBSPServiceConstants.EXPORT_HEADER);
        csvOutput.append(MBSPServiceConstants.CSV_SEPARATOR);
        csvOutput.append(CREATED_ON);
        csvOutput.append(MBSPServiceConstants.CSV_SEPARATOR);
        csvOutput.append(
                MBSPortalUtils.convertDateToString(MBSPortalUtils.getCurrentDate(), DateFormats.DATE_FORMAT_EXPORT));
        csvOutput.append(MBSPServiceConstants.CSV_LINE_SEPARATOR);
        csvOutput.append(MBSPServiceConstants.CSV_LINE_BEGIN);
        csvOutput.append(createFromToString(fromDate, toDate));
        csvOutput.append(MBSPServiceConstants.CSV_LINE_SEPARATOR);
        csvOutput.append(MBSPServiceConstants.CSV_LINE_BEGIN);
        csvOutput.append(MBSPServiceConstants.BUY_SELL_COMMENT + org);
        csvOutput.append(MBSPServiceConstants.CSV_LINE_SEPARATOR);
        csvOutput.append(MBSPServiceConstants.CSV_LINE_BREAK);

        csvOutput.append(MBSPServiceConstants.CSV_LINE_BEGIN);
        for (TransactionHistoryHeaders csvHeaders : TransactionHistoryHeaders.values()) {
            if (Arrays.asList(csvHeaders.getAllowedRoles()).contains(mbsRoleType)) {
                csvOutput.append(csvHeaders.getDisplayName());
                csvOutput.append(MBSPServiceConstants.CSV_SEPARATOR);
            }
        }
        csvOutput.setLength(csvOutput.length() - 2);

        csvOutput.append(MBSPServiceConstants.CSV_LINE_BREAK);

        return csvOutput;
    }

    /**
     * 
     * populate the data (transaction history) of the CSV
     * 
     * @param csvOutput
     * @param transHistPOLst
     * @return
     */
    private static StringBuilder populateData(StringBuilder csvOutput, List<TransactionHistoryPO> transHistPOLst,
            MBSRoleType mbsRoleType) throws MBSBaseException {

        for (TransactionHistoryPO presentationObject : transHistPOLst) {
            createCell(reformatDate(presentationObject.getSubmissionDate(), TransactionHistoryHeaders.REQUEST_TIME),
                    CSV_CELL_TYPE.FIRST, csvOutput, TransactionHistoryHeaders.REQUEST_TIME, mbsRoleType);
            createCell(presentationObject.getLenderEntityName(), CSV_CELL_TYPE.MIDDLE, csvOutput,
                    TransactionHistoryHeaders.ENTITY, mbsRoleType);
            createCell(presentationObject.getTspShortName(), CSV_CELL_TYPE.MIDDLE, csvOutput,
                    TransactionHistoryHeaders.TSP, mbsRoleType);
            createCell(reformatDate(presentationObject.getTradeDate(), TransactionHistoryHeaders.TRADE_DATE),
                    CSV_CELL_TYPE.MIDDLE, csvOutput, TransactionHistoryHeaders.TRADE_DATE, mbsRoleType);
            createCell(presentationObject.getTradeBuySellType(), CSV_CELL_TYPE.MIDDLE, csvOutput,
                    TransactionHistoryHeaders.BUY_SELL, mbsRoleType);
            createCell(format(presentationObject.getTradeAmount(), 0), CSV_CELL_TYPE.MIDDLE, csvOutput,
                    TransactionHistoryHeaders.AMOUNT, mbsRoleType);
            createCell((presentationObject.getProduct() == null) ? "-" : presentationObject.getProduct().getNameCode(),
                    CSV_CELL_TYPE.MIDDLE, csvOutput, TransactionHistoryHeaders.PRODUCT, mbsRoleType);
            createCell(format(presentationObject.getTradeCouponRate(), 2), CSV_CELL_TYPE.MIDDLE, csvOutput,
                    TransactionHistoryHeaders.COUPON, mbsRoleType);
            createCell(
                    reformatDate(presentationObject.getTradeSettlementDate(),
                            TransactionHistoryHeaders.SETTLEMENT_DATE),
                    CSV_CELL_TYPE.MIDDLE, csvOutput, TransactionHistoryHeaders.SETTLEMENT_DATE, mbsRoleType);
            createCell(createPricePerValue(presentationObject), CSV_CELL_TYPE.MIDDLE, csvOutput,
                    TransactionHistoryHeaders.REQUEST_TIME, mbsRoleType);
            createCell(
                    (presentationObject.getStateType() == null) ? "-"
                            : presentationObject.getStateType().getDisplayName(),
                    CSV_CELL_TYPE.MIDDLE, csvOutput, TransactionHistoryHeaders.STATUS, mbsRoleType);
            createCell(presentationObject.getTransReqId(), CSV_CELL_TYPE.MIDDLE, csvOutput,
                    TransactionHistoryHeaders.TRANSACTION_ID, mbsRoleType);
            createCell(
                    presentationObject.getTradeSrcPrimaryId() == 0 ? null
                            : Long.toString(presentationObject.getTradeSrcPrimaryId()),
                    CSV_CELL_TYPE.MIDDLE, csvOutput, TransactionHistoryHeaders.INVENTORY_NUMBER, mbsRoleType);
            createCell(presentationObject.getTradeSubPortfolioShortName(), CSV_CELL_TYPE.MIDDLE, csvOutput,
                    TransactionHistoryHeaders.PORTFOLIO, mbsRoleType);
            createCell(presentationObject.getLenderName(), CSV_CELL_TYPE.MIDDLE, csvOutput,
                    TransactionHistoryHeaders.USER_NAME, mbsRoleType);
            createCell(presentationObject.getTraderName(), CSV_CELL_TYPE.MIDDLE, csvOutput,
                    TransactionHistoryHeaders.TRADER_NAME, mbsRoleType);
            csvOutput.append(MBSPServiceConstants.CSV_LINE_SEPARATOR);
        }

        return csvOutput;
    }

    /**
     * Create price output as <handle> - <tick> formatted
     * 
     * @param presentationObject
     * @return
     */
    private static String createPricePerValue(TransactionHistoryPO presentationObject) {
        return (presentationObject.getPricePercentHandleText() == null)
                ? ((presentationObject.getPricePercentTicksText() == null) ? null
                        : "00 - " + presentationObject.getPricePercentTicksText())
                : ((presentationObject.getPricePercentTicksText() == null)
                        ? presentationObject.getPricePercentHandleText() + " - 00"
                        : presentationObject.getPricePercentHandleText() + " - "
                                + presentationObject.getPricePercentTicksText());
    }

    /**
     * Get excel spreadsheet from incoming data set as a byte array.
     * 
     * @param transHistPOLst
     * @param toDate
     * @param fromDate
     * @return
     */
    public static byte[] getXLFromHistory(List<TransactionHistoryPO> transHistPOLst, Date fromDate, Date toDate,
            String userName, MBSRoleType mbsRoleType, String org) throws MBSBaseException {
        LOGGER.debug("Starts getXLFromHistory method");
        OutputStream baos = new ByteArrayOutputStream(65536);
        Workbook wb = new XSSFWorkbook();

        if (mbsRoleType.equals(MBSRoleType.TRADER)) {
            org = "Fannie Mae";
        }

        Sheet s = wb.createSheet();
        Map<String, CellStyle> styleMap = createStyles(wb);
        int width = createHeader(wb, s, styleMap.get("Bold"), mbsRoleType);
        createTopLevelData(wb, s, toDate, fromDate, userName, styleMap, width, org);
        createDataRows(wb, s, transHistPOLst, styleMap, mbsRoleType);
        for (int i = 0; i < width; i++) {
            s.autoSizeColumn(i);
        }

        try {
            wb.write(baos);
        } catch (IOException e) {
            LOGGER.error(IO_ERROR);
            throw new MBSSystemException(IO_ERROR, MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        LOGGER.debug("Ends getXLFromHistory method");
        return ((ByteArrayOutputStream) baos).toByteArray();
    }

    /**
     * Creates the data portion of the excel spreadsheet
     * 
     * @param wb
     * @param s
     * @param transHistPOLst
     */
    private static void createDataRows(Workbook wb, Sheet s, List<TransactionHistoryPO> transHistPOLst,
            Map<String, CellStyle> styleMap, MBSRoleType mbsRoleType) throws MBSBaseException {
        Row transRow = null;

        int transRowNum = HEADER_ROW_NUM + 1;
        if (transHistPOLst == null || transHistPOLst.size() == 0) {
            transRow = s.createRow(transRowNum);
            createCell(transRow, 0, MBSPServiceConstants.NO_RESULTS_FOUND, styleMap.get("Bold"), null);
            transRowNum++;
        } else {
            for (TransactionHistoryPO presentationObject : transHistPOLst) {
                transRow = s.createRow(transRowNum);
                populateRow(transRow, presentationObject, (transRowNum % 2) == 0, styleMap, mbsRoleType);
                transRowNum++;
            }
        }
    }

    /**
     * Creates the header for the excel spreadsheet.
     * 
     * @param wb
     * @param s
     */
    private static int createHeader(Workbook wb, Sheet s, CellStyle cs, MBSRoleType mbsRoleType) {
        Row headerRow = s.createRow(HEADER_ROW_NUM);
        int cellNum = 0;
        for (TransactionHistoryHeaders headers : TransactionHistoryHeaders.values()) {
            if (createCell(headerRow, headers, headers.getDisplayName(), mbsRoleType, cs, cellNum)) {
                cellNum++;
            }
        }
        return cellNum;
    }

    /**
     * Creates the style Maps
     * 
     * @param wb
     * @return
     */
    private static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> stylesMap = new HashMap<>();
        CreationHelper helper = wb.getCreationHelper();
        short timeFormat = helper.createDataFormat().getFormat(DateFormats.DATE_FORMAT_XL_EXPORT);
        short dateFormat = helper.createDataFormat().getFormat(DateFormats.DATE_FORMAT_EXPORT_SETTLE_TRADE);
        short couponFormat = helper.createDataFormat().getFormat("0.00");
        short amountFormat = helper.createDataFormat().getFormat("#,##0");

        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cs.setAlignment(HorizontalAlignment.LEFT);
        addBorderToCell(cs);
        stylesMap.put("Shaded Left", cs);

        cs = wb.createCellStyle();
        cs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cs.setAlignment(HorizontalAlignment.RIGHT);
        addBorderToCell(cs);
        cs.setDataFormat(amountFormat);
        stylesMap.put("Shaded Right Amount", cs);

        cs = wb.createCellStyle();
        cs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cs.setAlignment(HorizontalAlignment.RIGHT);
        addBorderToCell(cs);
        stylesMap.put("Shaded Right", cs);

        cs = wb.createCellStyle();
        cs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cs.setAlignment(HorizontalAlignment.RIGHT);
        cs.setDataFormat(dateFormat);
        addBorderToCell(cs);
        stylesMap.put("Shaded Right Date", cs);

        cs = wb.createCellStyle();
        cs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cs.setAlignment(HorizontalAlignment.RIGHT);
        cs.setDataFormat(timeFormat);
        addBorderToCell(cs);
        stylesMap.put("Shaded Right Time", cs);

        cs = wb.createCellStyle();
        cs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cs.setAlignment(HorizontalAlignment.CENTER);
        addBorderToCell(cs);
        stylesMap.put("Shaded Center", cs);

        cs = wb.createCellStyle();
        cs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cs.setAlignment(HorizontalAlignment.CENTER);
        addBorderToCell(cs);
        cs.setDataFormat(couponFormat);
        stylesMap.put("Shaded Center Coupon", cs);

        cs = wb.createCellStyle();
        cs.setAlignment(HorizontalAlignment.LEFT);
        stylesMap.put("Left No Border", cs);

        cs = wb.createCellStyle();
        cs.setAlignment(HorizontalAlignment.LEFT);
        addBorderToCell(cs);
        stylesMap.put("Left", cs);

        cs = wb.createCellStyle();
        cs.setAlignment(HorizontalAlignment.RIGHT);
        addBorderToCell(cs);
        cs.setDataFormat(amountFormat);
        stylesMap.put("Right Amount", cs);

        cs = wb.createCellStyle();
        cs.setAlignment(HorizontalAlignment.RIGHT);
        addBorderToCell(cs);
        stylesMap.put("Right", cs);

        cs = wb.createCellStyle();
        cs.setAlignment(HorizontalAlignment.RIGHT);
        addBorderToCell(cs);
        cs.setDataFormat(dateFormat);
        stylesMap.put("Right Date", cs);

        cs = wb.createCellStyle();
        cs.setAlignment(HorizontalAlignment.RIGHT);
        addBorderToCell(cs);
        cs.setDataFormat(timeFormat);
        stylesMap.put("Right Time", cs);

        cs = wb.createCellStyle();
        cs.setAlignment(HorizontalAlignment.RIGHT);
        stylesMap.put("Right No Border", cs);

        cs = wb.createCellStyle();
        cs.setAlignment(HorizontalAlignment.RIGHT);
        cs.setDataFormat(timeFormat);
        stylesMap.put("Right Time No Border", cs);

        cs = wb.createCellStyle();
        cs.setAlignment(HorizontalAlignment.CENTER);
        addBorderToCell(cs);
        stylesMap.put("Center", cs);

        cs = wb.createCellStyle();
        cs.setAlignment(HorizontalAlignment.CENTER);
        addBorderToCell(cs);
        cs.setDataFormat(couponFormat);
        stylesMap.put("Center Coupon", cs);

        cs = wb.createCellStyle();
        Font f = wb.createFont();
        f.setBold(true);
        cs.setFont(f);
        cs.setAlignment(HorizontalAlignment.CENTER);
        addBorderToCell(cs);
        stylesMap.put("Bold", cs);

        cs = wb.createCellStyle();
        f = wb.createFont();
        f.setBold(true);
        cs.setFont(f);
        cs.setAlignment(HorizontalAlignment.CENTER);
        stylesMap.put("Bold No Border", cs);

        cs = wb.createCellStyle();
        f = wb.createFont();
        f.setItalic(true);
        cs.setFont(f);
        cs.setAlignment(HorizontalAlignment.CENTER);
        stylesMap.put("Note", cs);

        return stylesMap;
    }

    /**
     * Adds a border to a cell
     * 
     * @param cs
     */
    private static void addBorderToCell(CellStyle cs) {
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cs.setBorderRight(BorderStyle.THIN);
        cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cs.setBorderTop(BorderStyle.THIN);
        cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
    }

    /**
     * Populates a row with data from a single Transaction history presentation
     * object.
     * 
     * @param transRow
     * @param presentationObject
     * @param styleMap
     */
    private static void populateRow(Row transRow, TransactionHistoryPO presentationObject, boolean odd,
            Map<String, CellStyle> styleMap, MBSRoleType mbsRoleType) throws MBSBaseException {
        int cellNum = 0;
        if (createCell(transRow, TransactionHistoryHeaders.REQUEST_TIME,
                parseTimestamp(presentationObject.getSubmissionDate(), DateFormats.DATE_FORMAT_WITH_TIMESTAMP),
                mbsRoleType, getShadeNoShadeStyle(styleMap, odd, "Right Time"),
                getShadeNoShadeStyle(styleMap, odd, "Center"), cellNum))
            cellNum++;
        if (createCell(transRow, TransactionHistoryHeaders.ENTITY, presentationObject.getLenderEntityName(),
                mbsRoleType, getShadeNoShadeStyle(styleMap, odd, "Left"), getShadeNoShadeStyle(styleMap, odd, "Center"),
                cellNum))
            cellNum++;
        if (createCell(transRow, TransactionHistoryHeaders.TSP, presentationObject.getTspShortName(), mbsRoleType,
                getShadeNoShadeStyle(styleMap, odd, "Left"), getShadeNoShadeStyle(styleMap, odd, "Center"), cellNum))
            cellNum++;
        if (createCell(transRow, TransactionHistoryHeaders.TRADE_DATE,
                parseDate(presentationObject.getTradeDate(), DateFormats.DATE_FORMAT_NO_TIMESTAMP), mbsRoleType,
                getShadeNoShadeStyle(styleMap, odd, "Right Date"), getShadeNoShadeStyle(styleMap, odd, "Center"),
                cellNum))
            cellNum++;
        if (createCell(transRow, TransactionHistoryHeaders.BUY_SELL, presentationObject.getTradeBuySellType(),
                mbsRoleType, getShadeNoShadeStyle(styleMap, odd, "Left"), getShadeNoShadeStyle(styleMap, odd, "Center"),
                cellNum))
            cellNum++;
        if (createCell(transRow, TransactionHistoryHeaders.AMOUNT, parseNumber(presentationObject.getTradeAmount()),
                mbsRoleType, getShadeNoShadeStyle(styleMap, odd, "Right Amount"),
                getShadeNoShadeStyle(styleMap, odd, "Center"), cellNum))
            cellNum++;
        if (createCell(transRow, TransactionHistoryHeaders.PRODUCT,
                (presentationObject.getProduct() == null) ? null : presentationObject.getProduct().getNameCode(),
                mbsRoleType, getShadeNoShadeStyle(styleMap, odd, "Left"), getShadeNoShadeStyle(styleMap, odd, "Center"),
                cellNum))
            cellNum++;
        if (createCell(transRow, TransactionHistoryHeaders.COUPON, parseNumber(presentationObject.getTradeCouponRate()),
                mbsRoleType, getShadeNoShadeStyle(styleMap, odd, "Center Coupon"),
                getShadeNoShadeStyle(styleMap, odd, "Center"), cellNum))
            cellNum++;
        if (createCell(transRow, TransactionHistoryHeaders.SETTLEMENT_DATE,
                parseDate(presentationObject.getTradeSettlementDate(), DateFormats.DATE_FORMAT_NO_TIMESTAMP),
                mbsRoleType, getShadeNoShadeStyle(styleMap, odd, "Right Date"),
                getShadeNoShadeStyle(styleMap, odd, "Center"), cellNum))
            cellNum++;
        if (createCell(transRow, TransactionHistoryHeaders.PRICE, createPricePerValue(presentationObject), mbsRoleType,
                getShadeNoShadeStyle(styleMap, odd, "Center"), getShadeNoShadeStyle(styleMap, odd, "Center"), cellNum))
            cellNum++;
        if (createCell(transRow, TransactionHistoryHeaders.STATUS,
                (presentationObject.getStateType() == null) ? null : presentationObject.getStateType().getDisplayName(),
                mbsRoleType, getShadeNoShadeStyle(styleMap, odd, "Left"), getShadeNoShadeStyle(styleMap, odd, "Center"),
                cellNum))
            cellNum++;
        if (createCell(transRow, TransactionHistoryHeaders.TRANSACTION_ID, presentationObject.getTransReqId(),
                mbsRoleType, getShadeNoShadeStyle(styleMap, odd, "Left"), getShadeNoShadeStyle(styleMap, odd, "Center"),
                cellNum))
            cellNum++;
        if (createCell(transRow, TransactionHistoryHeaders.INVENTORY_NUMBER,
                presentationObject.getTradeSrcPrimaryId() == 0 ? null : presentationObject.getTradeSrcPrimaryId(),
                mbsRoleType, getShadeNoShadeStyle(styleMap, odd, "Left"), getShadeNoShadeStyle(styleMap, odd, "Center"),
                cellNum))
            cellNum++;
        if (createCell(transRow, TransactionHistoryHeaders.PORTFOLIO,
                presentationObject.getTradeSubPortfolioShortName(), mbsRoleType,
                getShadeNoShadeStyle(styleMap, odd, "Left"), getShadeNoShadeStyle(styleMap, odd, "Center"), cellNum))
            cellNum++;
        if (createCell(transRow, TransactionHistoryHeaders.USER_NAME, presentationObject.getLenderName(), mbsRoleType,
                getShadeNoShadeStyle(styleMap, odd, "Left"), getShadeNoShadeStyle(styleMap, odd, "Center"), cellNum))
            cellNum++;
        if (createCell(transRow, TransactionHistoryHeaders.TRADER_NAME, presentationObject.getTraderName(), mbsRoleType,
                getShadeNoShadeStyle(styleMap, odd, "Left"), getShadeNoShadeStyle(styleMap, odd, "Center"), cellNum))
            cellNum++;
    }

    private static Date parseTimestamp(String inputDate, String format) throws MBSBaseException {
        if (inputDate == null)
            return null;
        return MBSPortalUtils.convertTimestampToDateWithFormatter(inputDate, format);
    }

    private static Date parseDate(String inputDate, String format) throws MBSBaseException {
        if (inputDate == null)
            return null;
        return MBSPortalUtils.convertToDateWithFormatter(inputDate, format);
    }

    private static Double parseNumber(String input) throws MBSBaseException {
        if (input == null)
            return null;
        return MBSPortalUtils.convertToDouble(input);
    }

    /**
     * reformats incoming dates into the correct output
     * 
     * @param inputDate
     * @param fieldName
     * @return
     * @throws MBSBaseException
     */
    private static String reformatDate(String inputDate, TransactionHistoryHeaders fieldName) throws MBSBaseException {
        if (inputDate == null)
            return null;
        if (fieldName == TransactionHistoryHeaders.REQUEST_TIME) {
            Date d = MBSPortalUtils.convertTimestampToDateWithFormatter(inputDate,
                    DateFormats.DATE_FORMAT_WITH_TIMESTAMP);
            return MBSPortalUtils.convertDateToString(d, DateFormats.DATE_FORMAT_EXPORT);
        }
        if (fieldName == TransactionHistoryHeaders.SETTLEMENT_DATE
                || fieldName == TransactionHistoryHeaders.TRADE_DATE) {
            Date d = MBSPortalUtils.convertToDateWithFormatter(inputDate, DateFormats.DATE_FORMAT_NO_TIMESTAMP);
            return MBSPortalUtils.convertDateToString(d, DateFormats.DATE_FORMAT_EXPORT_SETTLE_TRADE);
        }
        Date d = MBSPortalUtils.convertToDateWithFormatter(inputDate, DateFormats.DATE_FORMAT_NO_TIMESTAMP);
        return MBSPortalUtils.convertDateToString(d, DateFormats.DATE_FORMAT_EXPORT_FROM_TO);
    }

    /**
     * format coupon and amount fields.
     * 
     * @param str
     * @param precision
     * @return
     */
    private static String format(String str, int precision) {
        if (str == null)
            return null;
        double d = Double.parseDouble(str.replace(",", ""));
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumFractionDigits(precision);
        numberFormat.setMaximumFractionDigits(precision);
        String formattedStr = numberFormat.format(d);
        return formattedStr;
    }

    /**
     * returns the shaded style based on the flag so that alternate rows are
     * shaded.
     * 
     * @param styleMap
     * @param shade
     * @param align
     * @return
     */
    private static CellStyle getShadeNoShadeStyle(Map<String, CellStyle> styleMap, boolean shade, String align) {
        // return styleMap.get(shade ? ("Shaded " + align) : align);
        return styleMap.get(align);
    }

    /**
     * Creates a cell in the CSV output
     * 
     * @param value
     * @param type
     * @param csvOutput
     */
    private static void createCell(String value, CSV_CELL_TYPE type, StringBuilder csvOutput,
            TransactionHistoryHeaders fieldName, MBSRoleType mbsRoleType) {
        if (!Arrays.asList(fieldName.getAllowedRoles()).contains(mbsRoleType)) {
            return;
        }
        switch (type) {
        case FIRST:
            csvOutput.append(MBSPServiceConstants.CSV_LINE_BEGIN);
            break;
        case MIDDLE:
            csvOutput.append(MBSPServiceConstants.CSV_SEPARATOR);
            break;
        case END:
            csvOutput.append(MBSPServiceConstants.CSV_LINE_SEPARATOR);
            break;
        default:
            ;
        }
        if (value == null) {
            csvOutput.append("-");
        } else {
            csvOutput.append(value);
        }
    }

    /**
     * Creates a single cell with the style provided and saves that cell into a
     * row.
     * 
     * @param transRow
     * @param ordinal
     * @param value
     * @param cs
     */
    private static boolean createCell(Row transRow, TransactionHistoryHeaders fieldName, Object value,
            MBSRoleType mbsRoleType, CellStyle cs, int ordinal) {
        return createCell(transRow, fieldName, value, mbsRoleType, cs, null, ordinal);
    }

    /**
     * Creates a single cell with the style provided and saves that cell into a
     * row.
     * 
     * @param transRow
     * @param fieldName
     * @param value
     * @param mbsRoleType
     * @param cs
     * @param csBold
     */
    private static boolean createCell(Row transRow, TransactionHistoryHeaders fieldName, Object value,
            MBSRoleType mbsRoleType, CellStyle cs, CellStyle csBold, int ordinal) {
        if (!Arrays.asList(fieldName.getAllowedRoles()).contains(mbsRoleType)) {
            return false;
        }
        createCell(transRow, ordinal, value, cs, csBold);
        return true;
    }

    /**
     * Creates a single cell with the style provided and saves that cell into a
     * row.
     * 
     * @param transRow
     * @param ordinal
     * @param value
     * @param cs
     * @param csBold
     */
    private static void createCell(Row transRow, int ordinal, Object value, CellStyle cs, CellStyle csBold) {
        if (value == null) {
            createCell(transRow, ordinal, "-", csBold, null);
            return;
        }
        Cell c = transRow.createCell(ordinal);
        switch (value.getClass().getName()) {
        case "java.lang.String":
            c.setCellValue((String) value);
            break;
        case "java.lang.Long":
            c.setCellValue((Long) value);
            break;
        case "java.util.Date":
            c.setCellValue((Date) value);
            break;
        case "java.lang.Double":
            c.setCellValue((Double) value);
            break;
        default:
            c.setCellValue(value.toString());
        }
        c.setCellStyle(cs);
    }

    /**
     * 
     * Creates the portion of the spreadsheet which includes the to date and
     * from date
     * 
     * @param wb
     * @param s
     * @param toDate
     * @param fromDate
     * @param userName
     * @param styleMap
     * @throws MBSBaseException
     */
    private static void createTopLevelData(Workbook wb, Sheet s, Date toDate, Date fromDate, String userName,
            Map<String, CellStyle> styleMap, int width, String org) throws MBSBaseException {
        Row titleRow = s.createRow(TITLE_ROW_NUM);
        CellStyle csBold = styleMap.get("Bold No Border");
        CellStyle csLeft = styleMap.get("Left No Border");
        CellStyle csRight = styleMap.get("Right No Border");
        CellStyle csNote = styleMap.get("Note");

        createCell(titleRow, 0, MBSPServiceConstants.APP_NAME, csBold, null);
        int index1 = s.addMergedRegion(new CellRangeAddress(TITLE_ROW_NUM, TITLE_ROW_NUM, MERGE_BEGIN, width - 3));
        createCell(titleRow, width - 2, CREATED_BY, csLeft, null);
        createCell(titleRow, width - 1, userName, csLeft, null);

        Row subTitleRow = s.createRow(SUB_TITLE_ROW_NUM);
        createCell(subTitleRow, 0, MBSPServiceConstants.EXPORT_HEADER, csBold, null);
        int index2 = s
                .addMergedRegion(new CellRangeAddress(SUB_TITLE_ROW_NUM, SUB_TITLE_ROW_NUM, MERGE_BEGIN, width - 3));
        createCell(subTitleRow, width - 2, CREATED_ON, csLeft, null);
        createCell(subTitleRow, width - 1, MBSPortalUtils.convertDateToString(MBSPortalUtils.getCurrentDate(),
                DateFormats.DATE_FORMAT_EXPORT_CREATED_FORMAT), csRight, null);

        Row toDateRow = s.createRow(TO_FROM_DATE_ROW_NUM);
        String output = createFromToString(fromDate, toDate);
        createCell(toDateRow, 0, output, csBold, null);
        int index3 = s.addMergedRegion(
                new CellRangeAddress(TO_FROM_DATE_ROW_NUM, TO_FROM_DATE_ROW_NUM, MERGE_BEGIN, width - 3));

        Row noteDateRow = s.createRow(NOTE_ROW_NUM);
        createCell(noteDateRow, 0, MBSPServiceConstants.BUY_SELL_COMMENT + org, csNote, null);
        int index4 = s.addMergedRegion(new CellRangeAddress(NOTE_ROW_NUM, NOTE_ROW_NUM, MERGE_BEGIN, width - 3));

    }

    /**
     * Creates the text "Trades from <fromDate> to <toDate>"
     * 
     * @param fromDate
     * @param toDate
     * @return
     * @throws MBSBaseException
     */
    private static String createFromToString(Date fromDate, Date toDate) throws MBSBaseException {
        return new StringBuilder().append("Trades from ")
                .append(MBSPortalUtils.convertDateToString(fromDate, DateFormats.DATE_FORMAT_EXPORT_FROM_TO))
                .append(" to ")
                .append(MBSPortalUtils.convertDateToString(toDate, DateFormats.DATE_FORMAT_EXPORT_FROM_TO)).toString();
    }

}
