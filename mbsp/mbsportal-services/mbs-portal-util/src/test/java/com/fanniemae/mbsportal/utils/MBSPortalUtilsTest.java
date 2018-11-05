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

package com.fanniemae.mbsportal.utils;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 *
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 18, 2018
 * @Time 1:36:45 PM
 * 	com.fanniemae.mbsportal.utils
 * 	MBSPortalUtilsTest.java
 * @Description: CMMBSSTA01-1023: Junit Test Cases for Greater Than and Less Than
 */
public class MBSPortalUtilsTest {
        
        public String numberInString;
        public int precision;
        public int scale;
        public BigDecimal number;
        
        @Before
        public void setUp() {
                number = new BigDecimal(3); scale = 2; precision = 1; numberInString = "3.00";
        }
        
        @Test
        public void getIntegerTrue() {
                String toGet = "4"; BigDecimal big = new java.math.BigDecimal(4);
                assertEquals(toGet, MBSPortalUtils.getInteger(big));
        }
        
        @Test
        public void getIntegerFalseNotEqual() {
                String toGet = "5"; BigDecimal big = new java.math.BigDecimal(4);
                assertNotEquals(toGet, MBSPortalUtils.getInteger(big));
        }
        
        @Test
        public void getIntegerFalseNotAString() {
                Integer toGet = 4; BigDecimal big = new java.math.BigDecimal(4);
                assertNotSame(toGet, MBSPortalUtils.getInteger(big));
        }
        
        @Test(expected = MBSBaseException.class)
        public void convertToBigDecimalNullValue() throws MBSBaseException {
                MBSPortalUtils.convertToBigDecimal("", 0, 0);
        }
        
        @Test(expected = MBSBaseException.class)
        public void convertToBigDecimalInvalidInput() throws MBSBaseException {
                MBSPortalUtils.convertToBigDecimal("ABC", 4, 1);
        }
        
        @Test
        public void convertToBigDecimalValidInput() throws MBSBaseException {
                BigDecimal big = new BigDecimal("124.0"); System.out.print(big);
                assertEquals(MBSPortalUtils.convertToBigDecimal("123.54", 3, 1), big);
        }
        
        @Test(expected = MBSBaseException.class)
        public void priceConvertToValuesNullInput() throws MBSBaseException {
                MBSPortalUtils.priceConvertToValues("", 0, 0);
        }
        
        @Test
        public void priceConvertToValuesValidInput() throws MBSBaseException {
                assertEquals("8.0", MBSPortalUtils.priceConvertToValues("4", 2, 1));
        }
        
        @Test(expected = MBSBaseException.class)
        public void convertTimestampToDateWithFormatterNullInput() throws MBSBaseException {
                MBSPortalUtils.convertTimestampToDateWithFormatter("", "");
        }
        
        @Test(expected = MBSBaseException.class)
        public void convertTimestampToDateWithFormatterInvalidInput() throws MBSBaseException {
                MBSPortalUtils.convertTimestampToDateWithFormatter("AB", "");
        }
        
        @Test
        public void convertTimeStampToDateWithFormatterValidInput() throws MBSBaseException, ParseException {
                DateFormat date = new SimpleDateFormat("hh:mm:ss"); Date timestamp = date.parse("10:20:30");
                assertEquals(timestamp, MBSPortalUtils.convertTimestampToDateWithFormatter("10:20:30", "hh:mm:ss"));
        }

        @Test(expected = MBSBaseException.class)
        public void convertToDateWithFormatterLocaleNullInput() throws MBSBaseException {
                Locale local = new Locale("English", "US");
                MBSPortalUtils.convertToDateWithFormatterLocale("", "", local);
        }
        
        @Test(expected = MBSBaseException.class)
        public void convertToDateWithFormatterLocaleInvalidInput() throws MBSBaseException {
                Locale local = new Locale("English", "US");
                MBSPortalUtils.convertToDateWithFormatterLocale("AB", "", local);
        }
        
        @Test
        public void convertToDateWithFormatterLocaleValidInput() throws MBSBaseException, ParseException {
                Locale testLocale = new Locale("English", "US");
                Date date = new SimpleDateFormat("EEE MMM dd hh:mm:ss z").parse("Thu Jan 01 10:20:30 EST 1970");
                assertEquals(date, MBSPortalUtils.convertToDateWithFormatterLocale("10:20:30", "hh:mm:ss", testLocale));
        }
        
        @Test
        public void getCurrentTimeStampValidInput() throws MBSBaseException {
                //current time in seconds is shortened because methods can't run at the exact same time.
                // This test is therefore not completely accurate, but verifies it's grabbing the timestamp.
                assertEquals(System.currentTimeMillis() / 1000, MBSPortalUtils.getCurrentTimeStamp() / 1000);
        }
        
        @Test
        public void isDateTrueValid() {
                assertTrue(MBSPortalUtils.isDateEqual(new Date()));
        }
        
        @Test
        public void isDateBeforeCutoffTimeFalse() throws MBSBaseException {
                assertFalse(MBSPortalUtils.isDateBeforeCutOffTime(0, 0, 0, 0));
        }
        
        @Test
        public void getDateTimeFromEpochMilliValid() throws MBSBaseException, ParseException {
                Date dateTime = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy")
                        .parse("Tue Dec 31 19:00:10 EST 1969");
                assertEquals(dateTime, MBSPortalUtils.getDateTimeFromEpochMilli((long) 10000));
        }
        
        @Test
        public void getExceptionInternalServer_Valid() {
                assertNotNull(MBSPortalUtils.getExceptionInternalServer());
        }
        
        @Test(expected = NullPointerException.class)
        public void getExceptionMessage_Valid() throws NullPointerException {
                List<ExceptionLookupPO> exceptionLookupPOLst = new ArrayList<ExceptionLookupPO>();
                MBSBaseException ex = new MBSBaseException("testing error");
                exceptionLookupPOLst.add(ex.getExceptionLookupPO());
                assertEquals("testing error", MBSPortalUtils.getExceptionMessage(exceptionLookupPOLst));
        }
        
        @Test
        public void getExceptionMessage_Null() throws NullPointerException {
                assertNotNull(MBSPortalUtils.getExceptionMessage(null));
        }
        
        @Test
        public void isItPastDateTrue() throws MBSBaseException, ParseException {
                String toBeChecked = "2017-09-28";
                DateFormat simpleDateFormat = new SimpleDateFormat(DateFormats.DATE_FORMAT_NO_TIMESTAMP);
                Date toBeCheckedDate = simpleDateFormat.parse(toBeChecked);
                assertTrue(MBSPortalUtils.isItPastDate(toBeCheckedDate));
        }
        
        @Test
        public void isItPastDateFalse() throws MBSBaseException, ParseException {
                String toBeChecked = "2022-09-28";
                DateFormat simpleDateFormat = new SimpleDateFormat(DateFormats.DATE_FORMAT_NO_TIMESTAMP);
                Date toBeCheckedDate = simpleDateFormat.parse(toBeChecked);
                assertFalse(MBSPortalUtils.isItPastDate(toBeCheckedDate));
        }
        
        @Test(expected = MBSBaseException.class)
        public void convertToLongException() throws MBSBaseException {
                MBSPortalUtils.convertToLong("");
        }
        
        @Test(expected = MBSBaseException.class)
        public void convertToLongInvalidValueException() throws MBSBaseException {
                MBSPortalUtils.convertToLong("ABC");
        }
        
        @Test
        public void convertToLong() throws MBSBaseException {
                assertEquals(MBSPortalUtils.convertToLong("123"), (new Long(123)).longValue());
        }
        
        @Test(expected = MBSBaseException.class)
        public void convertToDateWithFormatterException() throws MBSBaseException {
                MBSPortalUtils.convertToDateWithFormatter("", DateFormats.DATE_FORMAT_NO_TIMESTAMP);
        }
        
        @Test
        public void convertToDateWithFormatter() throws MBSBaseException {
                assertNotNull(
                        MBSPortalUtils.convertToDateWithFormatter("2018-01-01", DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        }
        
        @Test(expected = MBSBaseException.class)
        public void convertToDateWithFormatterExceptionInvalidDate() throws MBSBaseException {
                MBSPortalUtils.convertToDateWithFormatter("123ABC", DateFormats.DATE_FORMAT_NO_TIMESTAMP);
        }
        
        @Test
        public void convertDateToString() throws MBSBaseException {
                assertNotNull(MBSPortalUtils.convertDateToString(new Date(), DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        }
        
        @Test(expected = MBSBaseException.class)
        public void convertDateToStringException() throws MBSBaseException {
                MBSPortalUtils.convertDateToString(null, DateFormats.DATE_FORMAT_NO_TIMESTAMP);
        }
        
        @Test
        public void convertString() throws MBSBaseException {
                assertThat(MBSPortalUtils.convertToString(number, precision, scale), is(numberInString));
        }
        
        @Test
        public void convertStringPassingNull() throws MBSBaseException {
                String result = MBSPortalUtils.convertToString(null, precision, scale); assertNull(result);
        }
        
        @Test
        public void addDays() throws MBSBaseException {
                int days = 2; Date returnDate = MBSPortalUtils.addDays(new Date(), days); assertNotNull(returnDate);
        }
        
        @Test
        public void getLocalDateCurrentTimeStampFromGivenDateTime() throws MBSBaseException, Exception {
                Long timeStamp = MBSPortalUtils
                        .getLocalDateCurrentTimeStampFromGivenDateTime("2017-12-0112:29:01", "uuuu-MM-ddHH:mm:ss");
                assertTrue(timeStamp > 0);
        }
        
        @Test(expected = Exception.class)
        public void getLocalDateCurrentTimeStampFromGivenDateTimeThrowsException() throws MBSBaseException, Exception {
                Long timeStamp = MBSPortalUtils
                        .getLocalDateCurrentTimeStampFromGivenDateTime("2017-12-0112:29:01", "uuuu-MM-ddHH:mm");
                assertTrue(timeStamp > 0);
        }
        
        @Test
        public void getLocalDateCurrentTimeStamp() throws MBSBaseException {
                Long timeStamp = MBSPortalUtils.getLocalDateCurrentTimeStamp(); assertTrue(timeStamp > 0);
        }
        
        @Test
        public void getLocalZonedDateTimeFromEpochMilli() throws MBSBaseException, Exception {
                String dateTime = MBSPortalUtils
                        .getLocalZonedDateTimeFromEpochMilli(1512144234018L, "uuuu-MM-ddHH:mm:ss");
                assertNotNull(dateTime);
        }
        
        @Test(expected = MBSSystemException.class)
        public void getLocalZonedDateTimeFromEpochMilliThrowsException() throws MBSBaseException, Exception {
                String dateTime = MBSPortalUtils.getLocalZonedDateTimeFromEpochMilli(1512144234018L, "sss-MM-ddHH:m");
                assertNotNull(dateTime);
        }
        
        @Test
        public void isDateBeforeCutOffTime_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isDateBeforeCutOffTime(23, 59, 00, 00));
        }
        
        @Test
        public void isNumeric_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isNumeric("26000"));
        }
        
        @Test
        public void isNumeric_Not_Number_Success() throws MBSBaseException {
                assertFalse(MBSPortalUtils.isNumeric("-26000"));
        }
        
        @Test
        public void isInRange_Short_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils
                        .isInRange((short) 26000, TradeConstants.TRADE_MIN_VALUE, TradeConstants.TRADE_MAX_VALUE));
        }
        
        @Test
        public void isInRange_Byte_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isInRange((byte) 112, new BigInteger("-128"), new BigInteger("127")));
        }
        
        @Test
        public void isInRange_Int_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils
                        .isInRange((int) 26000, TradeConstants.TRADE_MIN_VALUE, TradeConstants.TRADE_MAX_VALUE));
        }
        
        @Test
        public void isInRange_Not_Int_Success() throws MBSBaseException {
                assertFalse(MBSPortalUtils
                        .isInRange((int) 24000, TradeConstants.TRADE_MIN_VALUE, TradeConstants.TRADE_MAX_VALUE));
        }
        
        @Test
        public void isInRange_Long_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils
                        .isInRange((long) 26000, TradeConstants.TRADE_MIN_VALUE, TradeConstants.TRADE_MAX_VALUE));
        }
        
        @Test
        public void isInRange_Float_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils
                        .isInRange((float) 26000.00f, TradeConstants.TRADE_MIN_VALUE, TradeConstants.TRADE_MAX_VALUE));
        }
        
        @Test
        public void isInRange_Double_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils
                        .isInRange((double) 26000.00, TradeConstants.TRADE_MIN_VALUE, TradeConstants.TRADE_MAX_VALUE));
        }
        
        @Test
        public void isInRange_BigInteger_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isInRange(BigInteger.valueOf(26000), TradeConstants.TRADE_MIN_VALUE,
                        TradeConstants.TRADE_MAX_VALUE));
        }
        
        @Test
        public void isInRange_BigDecimal_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isInRange(BigDecimal.valueOf(26000.00), TradeConstants.TRADE_MIN_VALUE,
                        TradeConstants.TRADE_MAX_VALUE));
        }
        
        @Test(expected = MBSBaseException.class)
        public void isInRange_Exception() throws MBSBaseException {
                MBSPortalUtils.isInRange(null, new BigInteger("1000"), new BigInteger("1000"));
        }
        
        @Test
        public void isGreaterOrEqual__Equal_Short_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isGreaterOrEqual((short) 25000, TradeConstants.TRADE_MIN_VALUE));
        }
        
        @Test
        public void isGreaterOrEqual_Short_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isGreaterOrEqual((short) 26000, TradeConstants.TRADE_MIN_VALUE));
        }
        
        @Test
        public void isGreaterOrEqual_Byte_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isGreaterOrEqual((byte) 112, new BigInteger("-128")));
        }
        
        @Test
        public void isGreaterOrEqual_Int_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isGreaterOrEqual((int) 26000, TradeConstants.TRADE_MIN_VALUE));
        }
        
        @Test
        public void isGreaterOrEqual_Not_Int_Success() throws MBSBaseException {
                assertFalse(MBSPortalUtils.isGreaterOrEqual((int) 24000, TradeConstants.TRADE_MIN_VALUE));
        }
        
        @Test
        public void isGreaterOrEqual_Long_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isGreaterOrEqual((long) 26000, TradeConstants.TRADE_MIN_VALUE));
        }
        
        @Test
        public void isGreaterOrEqual_Float_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isGreaterOrEqual((float) 26000.00f, TradeConstants.TRADE_MIN_VALUE));
        }
        
        @Test
        public void isGreaterOrEqual_Double_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isGreaterOrEqual((double) 26000.00, TradeConstants.TRADE_MIN_VALUE));
        }
        
        @Test
        public void isGreaterOrEqual_BigInteger_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isGreaterOrEqual(BigInteger.valueOf(26000), TradeConstants.TRADE_MIN_VALUE));
        }
        
        @Test
        public void isGreaterOrEqual_BigDecimal_Success() throws MBSBaseException {
                assertTrue(
                        MBSPortalUtils.isGreaterOrEqual(BigDecimal.valueOf(26000.00), TradeConstants.TRADE_MIN_VALUE));
        }
        
        @Test(expected = MBSBaseException.class)
        public void isGreaterOrEqual_Exception() throws MBSBaseException {
                MBSPortalUtils.isGreaterOrEqual(null, new BigInteger("1000"));
        }
        
        @Test
        public void isLessOrEqual_Equal_Short_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isLessOrEqual((short) 250000000, TradeConstants.TRADE_MAX_VALUE));
        }
        
        @Test
        public void isLessOrEqual_Short_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isLessOrEqual((short) 26000, TradeConstants.TRADE_MAX_VALUE));
        }
        
        @Test
        public void isLessOrEqual_Byte_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isLessOrEqual((byte) 112, new BigInteger("127")));
        }
        
        @Test
        public void isLessOrEqual_Int_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isLessOrEqual((int) 26000, TradeConstants.TRADE_MAX_VALUE));
        }
        
        @Test
        public void isLessOrEqual_Not_Int_Success() throws MBSBaseException {
                assertFalse(MBSPortalUtils.isLessOrEqual((int) 250000001, TradeConstants.TRADE_MAX_VALUE));
        }
        
        @Test
        public void isLessOrEqual_Long_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isLessOrEqual((long) 26000, TradeConstants.TRADE_MAX_VALUE));
        }
        
        @Test
        public void isLessOrEqual_Float_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isLessOrEqual((float) 26000.00f, TradeConstants.TRADE_MAX_VALUE));
        }
        
        @Test
        public void isLessOrEqual_Double_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isLessOrEqual((double) 26000.00, TradeConstants.TRADE_MAX_VALUE));
        }
        
        @Test
        public void isLessOrEqual_BigInteger_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isLessOrEqual(BigInteger.valueOf(26000), TradeConstants.TRADE_MAX_VALUE));
        }
        
        @Test
        public void isLessOrEqual_BigDecimal_Success() throws MBSBaseException {
                assertTrue(MBSPortalUtils.isLessOrEqual(BigDecimal.valueOf(26000.00), TradeConstants.TRADE_MAX_VALUE));
        }
        
        @Test(expected = MBSBaseException.class)
        public void isLessOrEqual_Exception() throws MBSBaseException {
                MBSPortalUtils.isLessOrEqual(null, new BigInteger("1000"));
        }
        
        @Test
        public void pricePercentTicksDisplayText_No_Eights_Success() throws MBSBaseException {
                assertEquals("00", MBSPortalUtils.pricePercentTicksDisplayText("00"));
        }
        
        @Test
        public void pricePercentTicksDisplayText_Null_Success() throws MBSBaseException {
                assertEquals(null, MBSPortalUtils.pricePercentTicksDisplayText(null));
        }
        
        @Test
        public void pricePercentTicksDisplayText_Symbolic_Eights_Success() throws MBSBaseException {
                assertEquals("00" + TradeConstants.EIGHTS_MIDDLE_VALUE_SIGN,
                        MBSPortalUtils.pricePercentTicksDisplayText("00" + TradeConstants.EIGHTS_MIDDLE_VALUE_SIGN));
        }
        
        @Test
        public void pricePercentTicksDisplayText_Numeric_Eights_Success() throws MBSBaseException {
                assertEquals("00" + TradeConstants.EIGHTS_MIDDLE_VALUE_SIGN,
                        MBSPortalUtils.pricePercentTicksDisplayText("00" + TradeConstants.EIGHTS_MIDDLE_VALUE_NUMERIC));
        }
        
        @Test(expected = MBSBaseException.class)
        public void convertToDoubleException() throws MBSBaseException {
                MBSPortalUtils.convertToDouble("");
        }
        
        @Test(expected = MBSBaseException.class)
        public void convertToDoubleInvalidValueException() throws MBSBaseException {
                MBSPortalUtils.convertToDouble("ABC");
        }
        
        @Test
        public void convertToDouble() throws MBSBaseException {
                assertEquals(MBSPortalUtils.convertToDouble("123"), (new Double(123)).doubleValue(), 0);
        }
        
        @Test
        public void convertGregorianToDate() throws MBSBaseException, DatatypeConfigurationException {
                GregorianCalendar gcal = new GregorianCalendar();
                gcal.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Calcutta")));
                XMLGregorianCalendar xmlCalDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
                assertNotNull(MBSPortalUtils.convertGregorianToDate(xmlCalDate, DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        }
        
        @Test(expected = MBSBaseException.class)
        public void convertGregorianToDateEmptyDate() throws MBSBaseException {
                XMLGregorianCalendar xmlCalDate = null;
                assertNotNull(MBSPortalUtils.convertGregorianToDate(xmlCalDate, DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        }
        
        @Test(expected = MBSBaseException.class)
        public void convertGregorianToDateInvalidInput()
                throws MBSBaseException, DatatypeConfigurationException, ParseException {
                GregorianCalendar gregcal = new GregorianCalendar();
                gregcal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2007-08-01 14:40"));
                XMLGregorianCalendar xmlCalDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregcal);
                MBSPortalUtils.convertGregorianToDate(xmlCalDate, "");
        }
        
        @Test
        public void convertGregorianToDateNoTimestamp() throws MBSBaseException, DatatypeConfigurationException {
                GregorianCalendar gcal = new GregorianCalendar();
                gcal.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Calcutta")));
                XMLGregorianCalendar xmlCalDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
                Date dateTemp = MBSPortalUtils.convertGregorianToDateNoTime(xmlCalDate);
                assertNotNull(MBSPortalUtils.convertGregorianToDateNoTime(xmlCalDate));
        }
        
        @Test(expected = MBSBaseException.class)
        public void convertGregorianToDateEmptyDateNoTimestamp() throws MBSBaseException {
                XMLGregorianCalendar xmlCalDate = null;
                assertNotNull(MBSPortalUtils.convertGregorianToDateNoTime(xmlCalDate));
        }
        
}
