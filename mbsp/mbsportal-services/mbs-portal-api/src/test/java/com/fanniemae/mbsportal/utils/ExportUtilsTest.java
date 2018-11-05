/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.utils;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.xmlbeans.XmlException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.po.TransactionHistoryPO;
import com.fanniemae.mbsportal.api.utils.ExportUtils;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * Util class for creating export data in csv or Excel spreadsheet formats.
 * 
 * 
 * @author e3uikb
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ExportUtilsTest {
    
    List<TransactionHistoryPO> transHistPOLst = new ArrayList<>();
    TransactionHistoryPO transactionHistoryPO, transactionHistoryPO1, transactionHistoryPO2;
    ProductPO prodPO;
    ProductIdPO productId;
    
    private static final String CSV_HEADER_DATA_FIRST_LINE = "\"MBS Trading\",\"Created by - \",\"junitTestName\"";
    private static final String CSV_HEADER_DATA_BEFORE_TIMESTAMP = "\"Fannie Mae Transaction History\",\"Create Date - \",\"";
    private static final String CSV_HEADER_DATA_AFTER_TIMESTAMP = "\"\n\"Trades from 4/11/2018 to 5/11/2018\"";
    private static final String CSV_HEADER_DATA_LAST = "\"Note: Buy/Sell displayed is from the perspective of ";
    
    private static final String CSV_HEADER_ROW_TRADER = "\"REQUEST TIME\",\"ENTITY\",\"TSP\",\"TRADE DATE\",\"B/S\",\"AMOUNT\",\"PROD\","
                + "\"CPN\",\"SETTLE\",\"PRICE\",\"STATUS\",\"TRANS ID\",\"INV#\",\"PORT\",\"USER\",\"FM TRADER\"";

    private static final String CSV_HEADER_ROW_LENDER = "\"REQUEST TIME\",\"ENTITY\",\"TRADE DATE\",\"B/S\",\"AMOUNT\",\"PROD\","
                + "\"CPN\",\"SETTLE\",\"PRICE\",\"STATUS\",\"TRANS ID\",\"INV#\",\"USER\"";

    private static final Object[] INPUT_DATA = {
                1L,                                    // 0. Identifier
                PRODUCT_SOURCE_TYPE.PU,                // 1. Product source type
                PRODUCT_TYPE.MBS,                      // 2. Product Type
            "500,000",                             // 3. Amount
            "SELL",                                // 4. Buy/Sell type
            "2.0",                                 // 5. Coupon
            "2017-10-12",                          // 6. Settlement Date
            "98765432",                            // 7. Lender Name
            StateType.TRADER_CONFIRMED,            // 8. Status
            "2018-04-25T13:19:42.723-0500",        // 9. Submission Date
            "101",                                 // 10. Handle
            "102",                                 // 11. Tick
            "MOVEMENT MORTGAGE, INC",              // 12. Entity Name
            "2018-04-25T13:19:42.723-0500",        // 13. Trade Date
            "PC30",                                // 14. Product Name
            "4",                                   // 15. Trans ID
            123456L,                               // 16. Inventory num
            "Trader Joe",                          // 17. Trader Name
            "800"                                  // 18. Subportfolio ShortName
                };
    
    private static final Object[] OUTPUT_DATA = {
                1L,
                PRODUCT_SOURCE_TYPE.PU,
                PRODUCT_TYPE.MBS,
            "500,000",
            "SELL",
            "2.0",
            "10/12/17",
            "98765432",
            StateType.TRADER_CONFIRMED.getDisplayName(),
            "4/25/18",
            "101",                                 // Handle
            "102",                                 // Tick
            "MOVEMENT MORTGAGE, INC",              // 12. Entity Name
            "4/25/18",                             // 13. Trade Date
            "PC30",                                // 14. Product Name
            "4",                                   // 15. Trans ID
            "123456",                              // 16. Inventory num
            "Trader Joe",                          // 17. Trader Name
            "800"                                  // 18. Subportfolio ShortName
                };
    private static final Object[] OUTPUT_DATA_LENDER = {
                1L,
                PRODUCT_SOURCE_TYPE.PU,
                PRODUCT_TYPE.MBS,
            "500,000",
            "SELL",
            "2.0",
            "10/12/17",
            "98765432",
            StateType.TRADER_CONFIRMED.getDisplayName(),
            "4/25/18",
            "101",                                 // Handle
            "102",                                 // Tick
            "MOVEMENT MORTGAGE, INC",              // 12. Entity Name
            "4/25/18",                             // 13. Trade Date
            "PC30",                                // 14. Product Name
            "4",                                   // 15. Trans ID
            "123456"                               // 16. Inventory num
                };
    
    Date toDate; 
    
    Date fromDate;
    
    @Before
    public void setup() {
        
        transactionHistoryPO = new TransactionHistoryPO();
        prodPO = new ProductPO();
        productId = new ProductIdPO();
        productId.setIdentifier(new Long((long)INPUT_DATA[0]));
        productId.setSourceType((PRODUCT_SOURCE_TYPE) INPUT_DATA[1]);
        productId.setType((PRODUCT_TYPE) INPUT_DATA[2]);
        prodPO.setProductId(productId);
        prodPO.setNameCode((String) INPUT_DATA[14]);
        transactionHistoryPO.setProduct(prodPO);
        transactionHistoryPO.setTradeAmount((String) INPUT_DATA[3]);
        transactionHistoryPO.setTradeBuySellType((String) INPUT_DATA[4]);
        transactionHistoryPO.setTradeCouponRate((String) INPUT_DATA[5]);
        transactionHistoryPO.setTradeSettlementDate((String) INPUT_DATA[6]);
        transactionHistoryPO.setLenderName((String) INPUT_DATA[7]);
        transactionHistoryPO.setStateType((StateType) INPUT_DATA[8]);
        transactionHistoryPO.setSubmissionDate((String) INPUT_DATA[9]); 
        transactionHistoryPO.setPricePercentHandleText((String) INPUT_DATA[10]);
        transactionHistoryPO.setPricePercentTicksText((String) INPUT_DATA[11]);   
        transactionHistoryPO.setLenderEntityName((String) INPUT_DATA[12]);
        transactionHistoryPO.setTradeDate((String) INPUT_DATA[13]);
        transactionHistoryPO.setTransReqId((String) INPUT_DATA[15]);
        transactionHistoryPO.setTradeSrcPrimaryId((Long) INPUT_DATA[16]);
        transactionHistoryPO.setTraderName((String) INPUT_DATA[17]);
        transactionHistoryPO.setTradeSubPortfolioShortName((String) INPUT_DATA[18]);
        
        transHistPOLst = new ArrayList<TransactionHistoryPO>();
        transHistPOLst.add(transactionHistoryPO);
        
        transactionHistoryPO1 = new TransactionHistoryPO();
        transactionHistoryPO1.setPricePercentHandleText("98");
        transactionHistoryPO1.setPricePercentTicksText(null);        
        transHistPOLst.add(transactionHistoryPO1);
        
        transactionHistoryPO2 = new TransactionHistoryPO();
        transactionHistoryPO2.setPricePercentHandleText(null);
        transactionHistoryPO2.setPricePercentTicksText("317");                
        transHistPOLst.add(transactionHistoryPO2);
        
        
        transactionHistoryPO2 = new TransactionHistoryPO();
        transactionHistoryPO2.setPricePercentHandleText(null);
        transactionHistoryPO2.setPricePercentTicksText(null);                
        transHistPOLst.add(transactionHistoryPO2);
        
        
 }
    
    @Test
    public void testGetCSVFromHistoryTrader() throws MBSBaseException {
        byte[] b = ExportUtils.getCSVFromHistory(transHistPOLst, 
                        MBSPortalUtils.convertToDateWithFormatter("2018-04-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        MBSPortalUtils.convertToDateWithFormatter("2018-05-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), "junitTestName", MBSRoleType.TRADER, "");
        
        String csvData = new String(b);
        System.out.println("output is: " + csvData);
        System.out.println(CSV_HEADER_DATA_LAST + "Fannie Mae\"");
        assertNotNull(b);
        Assert.assertTrue("header line one not found " + CSV_HEADER_DATA_FIRST_LINE, csvData.contains(CSV_HEADER_DATA_FIRST_LINE));
        Assert.assertTrue("header first part not found " + CSV_HEADER_DATA_BEFORE_TIMESTAMP, csvData.contains(CSV_HEADER_DATA_BEFORE_TIMESTAMP));
        Assert.assertTrue("header second part not found", csvData.contains(CSV_HEADER_DATA_AFTER_TIMESTAMP));
        Assert.assertTrue("header third part not found", csvData.contains(CSV_HEADER_DATA_LAST + "Fannie Mae\""));
        Assert.assertTrue("header row not found", csvData.contains(CSV_HEADER_ROW_TRADER));
        
        // check for valid price formats
        Assert.assertTrue("price value handle-ticks not found", csvData.contains("101 - 102"));
        Assert.assertTrue("price value handle - 00 not found", csvData.contains("98 - 00"));
        Assert.assertTrue("price value 00 - ticks not found", csvData.contains("00 - 317"));
        
        
        for (int i = 3; i < INPUT_DATA.length; i++) {
                Assert.assertTrue(INPUT_DATA[i] + " not found",csvData.contains(OUTPUT_DATA[i].toString()));
        }
    }
        
        @Test
        public void testGetCSVFromHistoryTrader_NullTransHistPOLst() throws MBSBaseException {
                List<TransactionHistoryPO> transHistPOLst_Null = new ArrayList<TransactionHistoryPO>();
                byte[] b = ExportUtils.getCSVFromHistory(transHistPOLst_Null,
                        MBSPortalUtils.convertToDateWithFormatter("2018-04-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP),
                        MBSPortalUtils.convertToDateWithFormatter("2018-05-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), "junitTestName", MBSRoleType.TRADER, "");
                
                String csvData = new String(b);
                System.out.println("output is: " + csvData);
                System.out.println(CSV_HEADER_DATA_LAST + "Fannie Mae\"");
                Assert.assertNotNull(b);
        }
    
    @Test
    public void testGetCSVFromHistoryLender() throws MBSBaseException {
        byte[] b = ExportUtils.getCSVFromHistory(transHistPOLst, 
                        MBSPortalUtils.convertToDateWithFormatter("2018-04-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        MBSPortalUtils.convertToDateWithFormatter("2018-05-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        "junitTestName", MBSRoleType.LENDER, "MOVEMENT MORTGAGE, LLC");
        
        String csvData = new String(b);
        System.out.println("output is: " + csvData);
        assertNotNull(b);
        Assert.assertTrue("header line one not found " + CSV_HEADER_DATA_FIRST_LINE, csvData.contains(CSV_HEADER_DATA_FIRST_LINE));
        Assert.assertTrue("header first part not found " + CSV_HEADER_DATA_BEFORE_TIMESTAMP, csvData.contains(CSV_HEADER_DATA_BEFORE_TIMESTAMP));
        Assert.assertTrue("header second part not found", csvData.contains(CSV_HEADER_DATA_AFTER_TIMESTAMP));
        Assert.assertTrue("header row not found", csvData.contains(CSV_HEADER_ROW_LENDER));
        
        // check for valid price formats
        Assert.assertTrue("price value handle-ticks not found", csvData.contains("101 - 102"));
        Assert.assertTrue("price value handle - 00 not found", csvData.contains("98 - 00"));
        Assert.assertTrue("price value 00 - ticks not found", csvData.contains("00 - 317"));
        
        
        for (int i = 3; i < OUTPUT_DATA_LENDER.length; i++) {
                Assert.assertTrue(INPUT_DATA[i] + " " + OUTPUT_DATA_LENDER[i] + " not found",csvData.contains(OUTPUT_DATA_LENDER[i].toString()));
        }
    }
    
    @Test
    public void testGetCSVFromHistoryNullResultsTrader() throws MBSBaseException {
        byte[] b = ExportUtils.getCSVFromHistory(null, 
                        MBSPortalUtils.convertToDateWithFormatter("2018-04-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        MBSPortalUtils.convertToDateWithFormatter("2018-05-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        "junitTestName", MBSRoleType.TRADER, "");
        
        String csvData = new String(b);
        System.out.println("output is: " + csvData);
        assertNotNull(b);
        Assert.assertTrue("header line one not found " + CSV_HEADER_DATA_FIRST_LINE, csvData.contains(CSV_HEADER_DATA_FIRST_LINE));
        Assert.assertTrue("header first part not found " + CSV_HEADER_DATA_BEFORE_TIMESTAMP, csvData.contains(CSV_HEADER_DATA_BEFORE_TIMESTAMP));
        Assert.assertTrue("header second part not found", csvData.contains(CSV_HEADER_DATA_AFTER_TIMESTAMP));
        Assert.assertTrue("header row not found", csvData.contains(CSV_HEADER_ROW_TRADER));
        Assert.assertTrue("NO RESULTS FOUND not found", csvData.contains(MBSPServiceConstants.NO_RESULTS_FOUND));
        
    }
    
    @Test
    public void testGetCSVFromHistoryNullResultsLender() throws MBSBaseException {
        byte[] b = ExportUtils.getCSVFromHistory(null, 
                        MBSPortalUtils.convertToDateWithFormatter("2018-04-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        MBSPortalUtils.convertToDateWithFormatter("2018-05-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        "junitTestName", MBSRoleType.LENDER, "MOVEMENT MORTGAGE, LLC");
        
        String csvData = new String(b);
        System.out.println("output is: " + csvData);
        assertNotNull(b);
        Assert.assertTrue("header line one not found " + CSV_HEADER_DATA_FIRST_LINE, csvData.contains(CSV_HEADER_DATA_FIRST_LINE));
        Assert.assertTrue("header first part not found " + CSV_HEADER_DATA_BEFORE_TIMESTAMP, csvData.contains(CSV_HEADER_DATA_BEFORE_TIMESTAMP));
        Assert.assertTrue("header second part not found", csvData.contains(CSV_HEADER_DATA_AFTER_TIMESTAMP));
        Assert.assertTrue("header row not found " + CSV_HEADER_ROW_LENDER, csvData.contains(CSV_HEADER_ROW_LENDER));
        Assert.assertTrue("NO RESULTS FOUND not found", csvData.contains(MBSPServiceConstants.NO_RESULTS_FOUND));
        
    }
    
    @Test
    public void testGetCSVFromHistoryZeroResultsFoundTrader() throws MBSBaseException {
        byte[] b = ExportUtils.getCSVFromHistory(new ArrayList<TransactionHistoryPO>(), 
                        MBSPortalUtils.convertToDateWithFormatter("2018-04-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        MBSPortalUtils.convertToDateWithFormatter("2018-05-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        "junitTestName", MBSRoleType.TRADER, "");
        
        String csvData = new String(b);
        System.out.println("output is: " + csvData);
        assertNotNull(b);
        Assert.assertTrue("header line one not found " + CSV_HEADER_DATA_FIRST_LINE, csvData.contains(CSV_HEADER_DATA_FIRST_LINE));
        Assert.assertTrue("header first part not found " + CSV_HEADER_DATA_BEFORE_TIMESTAMP, csvData.contains(CSV_HEADER_DATA_BEFORE_TIMESTAMP));
        Assert.assertTrue("header second part not found", csvData.contains(CSV_HEADER_DATA_AFTER_TIMESTAMP));
        Assert.assertTrue("header row not found", csvData.contains(CSV_HEADER_ROW_TRADER));
        Assert.assertTrue("NO RESULTS FOUND not found", csvData.contains(MBSPServiceConstants.NO_RESULTS_FOUND));
        
    }
    
    @Test
    public void testGetCSVFromHistoryZeroResultsFoundLender() throws MBSBaseException {
        byte[] b = ExportUtils.getCSVFromHistory(new ArrayList<TransactionHistoryPO>(), 
                        MBSPortalUtils.convertToDateWithFormatter("2018-04-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        MBSPortalUtils.convertToDateWithFormatter("2018-05-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        "junitTestName", MBSRoleType.LENDER, "MOVEMENT MORTGAGE, LLC");         
        String csvData = new String(b);
        System.out.println("output is: " + csvData);
        assertNotNull(b);
        Assert.assertTrue("header line one not found " + CSV_HEADER_DATA_FIRST_LINE, csvData.contains(CSV_HEADER_DATA_FIRST_LINE));
        Assert.assertTrue("header first part not found " + CSV_HEADER_DATA_BEFORE_TIMESTAMP, csvData.contains(CSV_HEADER_DATA_BEFORE_TIMESTAMP));
        Assert.assertTrue("header second part not found", csvData.contains(CSV_HEADER_DATA_AFTER_TIMESTAMP));
        Assert.assertTrue("header row not found", csvData.contains(CSV_HEADER_ROW_LENDER));
        Assert.assertTrue("NO RESULTS FOUND not found", csvData.contains(MBSPServiceConstants.NO_RESULTS_FOUND));
        
    }
    
    @Test
    public void testGetXLFromHistoryTrader() throws MBSBaseException, XmlException, OpenXML4JException, IOException, XmlException {

        byte[] b = ExportUtils.getXLFromHistory(transHistPOLst, 
                        MBSPortalUtils.convertToDateWithFormatter("2018-04-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        MBSPortalUtils.convertToDateWithFormatter("2018-05-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        "junitTestName", MBSRoleType.TRADER, "");
        
         File f = new File("TestDataOutput.xlsx");
         FileOutputStream fos = null;
         try {
                         fos = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                        throw new MBSSystemException("file not found");
                }
         
         try {
            fos.write(b);
                        fos.close();
                } catch (IOException e) {
                        throw new MBSSystemException("io error");
                }

                FileInputStream fs = new FileInputStream(f);
                OPCPackage d = OPCPackage.open(fs);
                XSSFExcelExtractor xe = new XSSFExcelExtractor(d);
                xe.setFormulasNotResults(true);
                xe.setIncludeSheetNames(true);
                String value = xe.getText();
                System.out.println("xlsx data is - " + value);
                fs.close();
        Assert.assertTrue("header line one not found - MBS Trading", value.contains("MBS Trading"));
        Assert.assertTrue("header first part not found - Fannie Mae Transaction History", value.contains("Fannie Mae Transaction History"));
        Assert.assertTrue("header second part not found - Trades from 4/11/2018 to 5/11/2018", value.contains("Trades from 4/11/2018 to 5/11/2018"));
        Assert.assertTrue("header row not found - TRADE DATE", value.contains("TRADE DATE"));
        
        for (int i = 3; i < OUTPUT_DATA.length; i++) {
                Assert.assertTrue(INPUT_DATA[i] + " not found",value.contains(OUTPUT_DATA[i].toString()));
        }

        // check for valid price formats
        Assert.assertTrue("price value handle-ticks not found", value.contains("101 - 102"));
        Assert.assertTrue("price value handle - 00 not found", value.contains("98 - 00"));
        Assert.assertTrue("price value 00 - ticks not found", value.contains("00 - 317"));
        
    }

    @Test
    public void testGetXLFromHistoryLender() throws MBSBaseException, XmlException, OpenXML4JException, IOException, XmlException {

        byte[] b = ExportUtils.getXLFromHistory(transHistPOLst, 
                        MBSPortalUtils.convertToDateWithFormatter("2018-04-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        MBSPortalUtils.convertToDateWithFormatter("2018-05-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        "junitTestName", MBSRoleType.LENDER, "MOVEMENT MORTGAGE, LLC");
        
         File f = new File("TestDataOutput.xlsx");
         FileOutputStream fos = null;
         try {
                         fos = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                        throw new MBSSystemException("file not found");
                }
         
         try {
            fos.write(b);
                        fos.close();
                } catch (IOException e) {
                        throw new MBSSystemException("io error");
                }

                FileInputStream fs = new FileInputStream(f);
                OPCPackage d = OPCPackage.open(fs);
                XSSFExcelExtractor xe = new XSSFExcelExtractor(d);
                xe.setFormulasNotResults(true);
                xe.setIncludeSheetNames(true);
                String value = xe.getText();
                System.out.println("xlsx data is - " + value);
                fs.close();
        Assert.assertTrue("header line one not found - MBS Trading", value.contains("MBS Trading"));
        Assert.assertTrue("header first part not found - Fannie Mae Transaction History", value.contains("Fannie Mae Transaction History"));
        Assert.assertTrue("header second part not found - Trades from 4/11/2018 to 5/11/2018", value.contains("Trades from 4/11/2018 to 5/11/2018"));
        Assert.assertTrue("header row not found - TRADE DATE", value.contains("TRADE DATE"));
        
        for (int i = 3; i < OUTPUT_DATA_LENDER.length; i++) {
                Assert.assertTrue(INPUT_DATA[i] + " not found",value.contains(OUTPUT_DATA_LENDER[i].toString()));
        }

        // check for valid price formats
        Assert.assertTrue("price value handle-ticks not found", value.contains("101 - 102"));
        Assert.assertTrue("price value handle - 00 not found", value.contains("98 - 00"));
        Assert.assertTrue("price value 00 - ticks not found", value.contains("00 - 317"));
        
    }

    @Test
    public void testGetXLFromHistoryTSP() throws MBSBaseException, XmlException, OpenXML4JException, IOException, XmlException {

        byte[] b = ExportUtils.getXLFromHistory(transHistPOLst, 
                        MBSPortalUtils.convertToDateWithFormatter("2018-04-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        MBSPortalUtils.convertToDateWithFormatter("2018-05-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        "junitTestName", MBSRoleType.TSP, "Optimal Blue");
        
         File f = new File("TestDataOutput.xlsx");
         FileOutputStream fos = null;
         try {
                         fos = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                        throw new MBSSystemException("file not found");
                }
         
         try {
            fos.write(b);
                        fos.close();
                } catch (IOException e) {
                        throw new MBSSystemException("io error");
                }

                FileInputStream fs = new FileInputStream(f);
                OPCPackage d = OPCPackage.open(fs);
                XSSFExcelExtractor xe = new XSSFExcelExtractor(d);
                xe.setFormulasNotResults(true);
                xe.setIncludeSheetNames(true);
                String value = xe.getText();
                System.out.println("xlsx data is - " + value);
                fs.close();
        Assert.assertTrue("header line one not found - MBS Trading", value.contains("MBS Trading"));
        Assert.assertTrue("header first part not found - Fannie Mae Transaction History", value.contains("Fannie Mae Transaction History"));
        Assert.assertTrue("header second part not found - Trades from 4/11/2018 to 5/11/2018", value.contains("Trades from 4/11/2018 to 5/11/2018"));
        Assert.assertTrue("header row not found - TRADE DATE", value.contains("TRADE DATE"));
        
        for (int i = 3; i < OUTPUT_DATA_LENDER.length; i++) {
                Assert.assertTrue(INPUT_DATA[i] + " not found",value.contains(OUTPUT_DATA_LENDER[i].toString()));
        }

        // check for valid price formats
        Assert.assertTrue("price value handle-ticks not found", value.contains("101 - 102"));
        Assert.assertTrue("price value handle - 00 not found", value.contains("98 - 00"));
        Assert.assertTrue("price value 00 - ticks not found", value.contains("00 - 317"));
        
    }

    @Test
    public void testGetXLFromHistoryNullResultsTrader() throws MBSBaseException, XmlException, OpenXML4JException, IOException, XmlException {

        byte[] b = ExportUtils.getXLFromHistory(null, 
                        MBSPortalUtils.convertToDateWithFormatter("2018-04-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        MBSPortalUtils.convertToDateWithFormatter("2018-05-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        "junitTestName", MBSRoleType.TRADER, "");
        
         File f = new File("TestDataOutput.xlsx");
         FileOutputStream fos = null;
         try {
                         fos = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                        throw new MBSSystemException("file not found");
                }
         
         try {
            fos.write(b);
                        fos.close();
                } catch (IOException e) {
                        throw new MBSSystemException("io error");
                }

                FileInputStream fs = new FileInputStream(f);
                OPCPackage d = OPCPackage.open(fs);
                XSSFExcelExtractor xe = new XSSFExcelExtractor(d);
                xe.setFormulasNotResults(true);
                xe.setIncludeSheetNames(true);
                String value = xe.getText();
                System.out.println("xlsx data is - " + value);
                fs.close();
        Assert.assertTrue("header line one not found - MBS Trading", value.contains("MBS Trading"));
        Assert.assertTrue("header first part not found - Fannie Mae Transaction History", value.contains("Fannie Mae Transaction History"));
        Assert.assertTrue("header second part not found - Trades from 4/11/2018 to 5/11/2018", value.contains("Trades from 4/11/2018 to 5/11/2018"));
        Assert.assertTrue("header row not found - TRADE DATE", value.contains("TRADE DATE"));
        Assert.assertTrue("NO RESULTS FOUND not found", value.contains(MBSPServiceConstants.NO_RESULTS_FOUND));
                
    }

    @Test
    public void testGetXLFromHistoryNullResultsLender() throws MBSBaseException, XmlException, OpenXML4JException, IOException, XmlException {

        byte[] b = ExportUtils.getXLFromHistory(null, 
                        MBSPortalUtils.convertToDateWithFormatter("2018-04-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        MBSPortalUtils.convertToDateWithFormatter("2018-05-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        "junitTestName", MBSRoleType.LENDER, "MOVEMENT MORTGAGE, LLC");
        
         File f = new File("TestDataOutput.xlsx");
         FileOutputStream fos = null;
         try {
                         fos = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                        throw new MBSSystemException("file not found");
                }
         
         try {
            fos.write(b);
                        fos.close();
                } catch (IOException e) {
                        throw new MBSSystemException("io error");
                }

                FileInputStream fs = new FileInputStream(f);
                OPCPackage d = OPCPackage.open(fs);
                XSSFExcelExtractor xe = new XSSFExcelExtractor(d);
                xe.setFormulasNotResults(true);
                xe.setIncludeSheetNames(true);
                String value = xe.getText();
                System.out.println("xlsx data is - " + value);
                fs.close();
        Assert.assertTrue("header line one not found - MBS Trading", value.contains("MBS Trading"));
        Assert.assertTrue("header first part not found - Fannie Mae Transaction History", value.contains("Fannie Mae Transaction History"));
        Assert.assertTrue("header second part not found - Trades from 4/11/2018 to 5/11/2018", value.contains("Trades from 4/11/2018 to 5/11/2018"));
        Assert.assertTrue("header row not found - TRADE DATE", value.contains("TRADE DATE"));
        Assert.assertTrue("NO RESULTS FOUND not found", value.contains(MBSPServiceConstants.NO_RESULTS_FOUND));
                
    }

    @Test
    public void testGetXLFromHistoryZeroResultsFoundTrader() throws MBSBaseException, XmlException, OpenXML4JException, IOException, XmlException {

        byte[] b = ExportUtils.getXLFromHistory(new ArrayList<TransactionHistoryPO>(), 
                        MBSPortalUtils.convertToDateWithFormatter("2018-04-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        MBSPortalUtils.convertToDateWithFormatter("2018-05-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        "junitTestName", MBSRoleType.TRADER, "");
        
         File f = new File("TestDataOutput.xlsx");
         FileOutputStream fos = null;
         try {
                         fos = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                        throw new MBSSystemException("file not found");
                }
         
         try {
            fos.write(b);
                        fos.close();
                } catch (IOException e) {
                        throw new MBSSystemException("io error");
                }

                FileInputStream fs = new FileInputStream(f);
                OPCPackage d = OPCPackage.open(fs);
                XSSFExcelExtractor xe = new XSSFExcelExtractor(d);
                xe.setFormulasNotResults(true);
                xe.setIncludeSheetNames(true);
                String value = xe.getText();
                System.out.println("xlsx data is - " + value);
                fs.close();
        Assert.assertTrue("header line one not found - MBS Trading", value.contains("MBS Trading"));
        Assert.assertTrue("header first part not found - Fannie Mae Transaction History", value.contains("Fannie Mae Transaction History"));
        Assert.assertTrue("header second part not found - Trades from 4/11/2018 to 5/11/2018", value.contains("Trades from 4/11/2018 to 5/11/2018"));
        Assert.assertTrue("header row not found - TRADE DATE", value.contains("TRADE DATE"));
        Assert.assertTrue("NO RESULTS FOUND not found", value.contains(MBSPServiceConstants.NO_RESULTS_FOUND));
                
    }

    @Test
    public void testGetXLFromHistoryZeroResultsFoundLender() throws MBSBaseException, XmlException, OpenXML4JException, IOException, XmlException {

        byte[] b = ExportUtils.getXLFromHistory(new ArrayList<TransactionHistoryPO>(), 
                        MBSPortalUtils.convertToDateWithFormatter("2018-04-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        MBSPortalUtils.convertToDateWithFormatter("2018-05-11", DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        "junitTestName", MBSRoleType.LENDER, "MOVEMENT MORTGAGE, LLC");
        
         File f = new File("TestDataOutput.xlsx");
         FileOutputStream fos = null;
         try {
                         fos = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                        throw new MBSSystemException("file not found");
                }
         
         try {
            fos.write(b);
                        fos.close();
                } catch (IOException e) {
                        throw new MBSSystemException("io error");
                }

                FileInputStream fs = new FileInputStream(f);
                OPCPackage d = OPCPackage.open(fs);
                XSSFExcelExtractor xe = new XSSFExcelExtractor(d);
                xe.setFormulasNotResults(true);
                xe.setIncludeSheetNames(true);
                String value = xe.getText();
                System.out.println("xlsx data is - " + value);
                fs.close();
        Assert.assertTrue("header line one not found - MBS Trading", value.contains("MBS Trading"));
        Assert.assertTrue("header first part not found - Fannie Mae Transaction History", value.contains("Fannie Mae Transaction History"));
        Assert.assertTrue("header second part not found - Trades from 4/11/2018 to 5/11/2018", value.contains("Trades from 4/11/2018 to 5/11/2018"));
        Assert.assertTrue("header row not found - TRADE DATE", value.contains("TRADE DATE"));
        Assert.assertTrue("NO RESULTS FOUND not found", value.contains(MBSPServiceConstants.NO_RESULTS_FOUND));
                
    }


}