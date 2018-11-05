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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.ExceptionMessagesPO;
import com.fanniemae.mbsportal.utils.exception.ExceptionResponseMapper;
import com.fanniemae.mbsportal.utils.exception.ExceptionResponsePO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 10, 2018
 * @Time 3:55:32 PM com.fanniemae.mbsportal.utils MBSPortalUtils.java
 * @Description: CMMBSSTA01-787 - Added isNumeric and isInRange method
 */
//TODO: Moved Calender instance creation to common method
public class MBSPortalUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MBSPortalUtils.class);
    private static String regexValueAmt = "[,-]";

    private static String STRING_TO_LONG_EMPTY_INPUT = "String to Long conversion error: Input value is empty/null";
    private static String STRING_TO_LONG_INVALID_INPUT = "String to Long conversion error: Input value is invalid, parse error";
    private static String STRING_TO_BIGDECIMAL_INPUT = "String to Big Decimal conversion error: Input value is empty/null";
    private static String STRING_TO_BIGDECIMAL_INVALID_INPUT = "String to Big Decimal conversion error: Input value is invalid, parse error";
    private static String STRING_TO_DATE_INPUT = "String to Date conversion error: Input value is empty/null";
    private static String STRING_TO_DATE_INVALID_INPUT = "String to Date conversion error: Input value is invalid, parse error";
    private static String DATE_TO_STRING_INPUT = "Date to String conversion error: Input value is empty/null";
    private static String STRING_TO_NUMBER_INPUT = "String to Number conversion error: Input value is empty/null";
    private static String STRING_TO_NUMBER_INVALID_INPUT = "String to Number conversion error: Input value is invalid, parse error";

    public static final String TIME_ZONE_NYC = "America/New_York";
    public static final String DATE_TIME_FORMAT_UUUU_MM_DD_HH_MM_SS = "uuuu-MM-ddHH:mm:ss";
    
    
    public static final int DEFAULT_NUMBER_PAD_CHARACTER_TO_SHOW = 5;
    public static final char DEFAULT_PAD_CHARACTER = '*';
    

    /**
     * Purpose: This method converts String to long
     * 
     * @param value
     *            value - the String value that needs to converted to long
     *            (Example: "101")
     * @return long
     */
    public static long convertToLong(String value) throws MBSBaseException {
        Long amount;
        if (StringUtils.isEmpty(value)) {
            LOGGER.error(STRING_TO_LONG_EMPTY_INPUT);
            throw new MBSSystemException(STRING_TO_LONG_EMPTY_INPUT, MBSExceptionConstants.SYSTEM_EXCEPTION);
        } else {
            try {
                amount = Long.valueOf(value);
            } catch (Exception ex) {
                LOGGER.error(STRING_TO_LONG_INVALID_INPUT);
                throw new MBSSystemException(STRING_TO_LONG_INVALID_INPUT, MBSExceptionConstants.SYSTEM_EXCEPTION);
            }
        }
        return amount.longValue();
    }

    /**
     * formatting BigDecimal to string in 3 scale
     * 
     * @param input
     * @param precision
     * @param scale
     * @return
     * @throws MBSBaseException
     */
    public static String convertToString(BigDecimal input, int precision, int scale) throws MBSBaseException {
        if (input == null) {
            return null;
        }
        return input.abs(new MathContext(precision, RoundingMode.HALF_EVEN)).setScale(scale).toString();
    }

    public static String getInteger(BigDecimal input) {
        return String.valueOf(input.intValue());
    }

    /**
     * This method converts String to BigDecimal
     * 
     * @param input
     * @param precision
     * @param scale
     * @return
     * @throws MBSBaseException
     */
    public static BigDecimal convertToBigDecimal(String input, int precision, int scale) throws MBSBaseException {
        BigDecimal amount;
        if (StringUtils.isEmpty(input)) {
            LOGGER.error(STRING_TO_BIGDECIMAL_INPUT);
            throw new MBSSystemException(STRING_TO_BIGDECIMAL_INPUT, MBSExceptionConstants.SYSTEM_EXCEPTION);
        } else {
            try {
                amount = new BigDecimal(input.replace(regexValueAmt, ""),
                        new MathContext(precision, RoundingMode.HALF_EVEN)).setScale(scale);
            } catch (Exception ex) {
                LOGGER.error(STRING_TO_BIGDECIMAL_INVALID_INPUT);
                throw new MBSSystemException(STRING_TO_BIGDECIMAL_INVALID_INPUT,
                        MBSExceptionConstants.SYSTEM_EXCEPTION);
            }
        }
        return amount;
    }
    
    /**
     * This method converts String to BigDecimal
     * @param input
     * @param multiplicationFactor
     * @param scale
     * @return
     * @throws MBSBaseException
     * scale is set to 9
     */
    public static String priceConvertToValues(String input, int multiplicationFactor, int scale)
            throws MBSBaseException {
        BigDecimal value = new BigDecimal(0);
        if (StringUtils.isNotBlank(input)) {
            value = new BigDecimal(input);
            BigDecimal multiplicand = new BigDecimal(multiplicationFactor);
            value = value.multiply(multiplicand).setScale(scale, RoundingMode.HALF_EVEN);
        } else {
            LOGGER.error(STRING_TO_BIGDECIMAL_INPUT);
            throw new MBSSystemException(STRING_TO_BIGDECIMAL_INPUT, MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        return value.toString();
    }
    
    /**
    *
    * @param value
    * @param format
    * @return
    * @throws MBSBaseException
    */
   public static Date convertTimestampToDateWithFormatter(String value, String format) throws MBSBaseException {

       DateFormat simpleDateFormat = new SimpleDateFormat(format);
       simpleDateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of(TIME_ZONE_NYC)));
       Date date = new Date();
       if (StringUtils.isEmpty(value)) {
           LOGGER.error(STRING_TO_DATE_INPUT);
           throw new MBSSystemException(STRING_TO_DATE_INPUT, MBSExceptionConstants.SYSTEM_EXCEPTION);
       } else {
           try {
               date = simpleDateFormat.parse(value);
           } catch (ParseException ex) {
               LOGGER.error(STRING_TO_DATE_INVALID_INPUT);
               throw new MBSSystemException(STRING_TO_DATE_INVALID_INPUT, MBSExceptionConstants.SYSTEM_EXCEPTION);
           }
       }
       return date;
   }

    /**
     *
     * @param value
     * @param format
     * @return
     * @throws MBSBaseException
     */
    public static Date convertToDateWithFormatter(String value, String format) throws MBSBaseException {

        DateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of(TIME_ZONE_NYC)));
        Date date = new Date();
        if (StringUtils.isEmpty(value)) {
            LOGGER.error(STRING_TO_DATE_INPUT);
            throw new MBSSystemException(STRING_TO_DATE_INPUT, MBSExceptionConstants.SYSTEM_EXCEPTION);
        } else {
            try {
                date = simpleDateFormat.parse(value);
            } catch (ParseException ex) {
                LOGGER.error(STRING_TO_DATE_INVALID_INPUT);
                throw new MBSSystemException(STRING_TO_DATE_INVALID_INPUT, MBSExceptionConstants.SYSTEM_EXCEPTION);
            }
        }
        return date;
    }
    
    /**
     * convertGregorianToDate
     * @param xmlCalDate
     * @return
     * @throws MBSBaseException
     */
    public static Date convertGregorianToDate(XMLGregorianCalendar xmlCalDate, String format) throws MBSBaseException{
        DateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of(TIME_ZONE_NYC)));
        Date date = getCurrentDate();
        if(Objects.equals(xmlCalDate, null)){
            LOGGER.error("Date value is empty");
            throw new MBSBaseException("Date value is empty");
        } else {
            try {
                date = simpleDateFormat.parse(simpleDateFormat.format(xmlCalDate.toGregorianCalendar().getTime()));
            } catch (ParseException ex) {
                LOGGER.error(STRING_TO_DATE_INVALID_INPUT);
                throw new MBSSystemException(STRING_TO_DATE_INVALID_INPUT, MBSExceptionConstants.SYSTEM_EXCEPTION);
            }  
        }
        return date;
    }
    
    /**
     * 
     * @return Calendar
     */
    public static Calendar getCalendarInstance(){
        TimeZone.setDefault(TimeZone.getTimeZone(TIME_ZONE_NYC));
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of(TIME_ZONE_NYC)));
        return calendar;
        
    }

    
    /**
     * convertGregorianToDateNoTime
     * @param xmlCalDate
     * @return Date
     * @throws MBSBaseException
     */
    public static Date convertGregorianToDateNoTime(XMLGregorianCalendar xmlCalDate) throws MBSBaseException{
        Calendar calendar = getCalendarInstance();
        calendar.clear();
        calendar.setTimeZone(TimeZone.getTimeZone(ZoneId.of(TIME_ZONE_NYC)));
        if(Objects.equals(xmlCalDate, null)){
            LOGGER.error("Date value is empty");
            throw new MBSBaseException("Date value is empty");
        } else {
            calendar.set(xmlCalDate.getYear(), xmlCalDate.getMonth()-1, xmlCalDate.getDay());
        }
        return calendar.getTime();
    }
    
    /**
    *
    * @param value
    * @param format
    * @return
    * @throws MBSBaseException
    */
   public static Date convertToDateWithFormatterLocale(String value, String format, Locale locale) throws MBSBaseException {

       DateFormat simpleDateFormat = new SimpleDateFormat(format, locale);
       Date date = new Date();
       if (StringUtils.isEmpty(value)) {
           LOGGER.error(STRING_TO_DATE_INPUT);
           throw new MBSSystemException(STRING_TO_DATE_INPUT, MBSExceptionConstants.SYSTEM_EXCEPTION);
       } else {
           try {
               date = simpleDateFormat.parse(value);
           } catch (ParseException ex) {
               LOGGER.error(STRING_TO_DATE_INVALID_INPUT);
               throw new MBSSystemException(STRING_TO_DATE_INVALID_INPUT, MBSExceptionConstants.SYSTEM_EXCEPTION);
           }
       }
       return date;
   }
   
   /**
    *
    * @param value
    * @return
    * @throws MBSBaseException
    */
    public static double convertToDouble(String value) throws MBSBaseException {
        double output = 0.0;
        if (StringUtils.isEmpty(value)) {
            LOGGER.error(STRING_TO_NUMBER_INPUT);
            throw new MBSSystemException(STRING_TO_NUMBER_INPUT, MBSExceptionConstants.SYSTEM_EXCEPTION);
        } else {
            try {
                output = Double.parseDouble(value.replace(",", ""));
            } catch (NumberFormatException ex) {
                LOGGER.error(STRING_TO_NUMBER_INVALID_INPUT);
                throw new MBSSystemException(STRING_TO_NUMBER_INVALID_INPUT, MBSExceptionConstants.SYSTEM_EXCEPTION);
            }
        }
        return output;
    }

    /**
     *
     * @param dateVal
     * @param format
     * @return
     * @throws MBSBaseException
     */
    public static String convertDateToString(Date dateVal, String format) throws MBSBaseException {

        DateFormat simpleDateFormat = new SimpleDateFormat(format);// "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of(TIME_ZONE_NYC)));
        String strDate = "";
        if (!Objects.equals(dateVal, null)) {
            strDate = simpleDateFormat.format(dateVal);
        } else {
            LOGGER.error(DATE_TO_STRING_INPUT);
            throw new MBSSystemException(DATE_TO_STRING_INPUT, MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        return strDate;
    }

    /**
     * Purpose: This method returns the current date
     * 
     * @return
     */
    public static Date getCurrentDate() {
        Calendar calendar = getCalendarInstance();
        calendar.setTime(new Date());
        return calendar.getTime();

    }
    
    /**
     * 
     * @return long
     */
    public static long getCurrentTimeStamp() {
        return getCurrentDate().getTime();

    }
    
   

    /**
     * Date to check whether it expired already
     * 
     * @param dateToCheck
     * @return
     */
    public static boolean isItPastDate(Date dateToCheck) {
        Calendar calCheck = getCalendarInstance();
        calCheck.setTime(dateToCheck);
        //calCheck.setTimeZone(TimeZone.getTimeZone(ZoneId.of(TIME_ZONE_NYC)));
        LocalDate dateToCheckLocal = LocalDate.of(calCheck.get(1), calCheck.get(2) + 1,
                calCheck.get(Calendar.DAY_OF_MONTH));
        LocalDate currentDate = LocalDate.now(ZoneId.of(TIME_ZONE_NYC));
        return dateToCheckLocal.isBefore(currentDate);
    }

    /**
     * To add date. To decrement, send minus number
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        Calendar cal = getCalendarInstance();
        cal.setTime(date);
        //cal.setTimeZone(TimeZone.getTimeZone(ZoneId.of(TIME_ZONE_NYC)));
        cal.add(Calendar.DATE, days); // minus number would decrement the days
        return cal.getTime();
    }

    /**
     * Date to check whether it expired already
     *
     * @param dateToCheck
     * @return
     * @throws MBSBaseException
     */
    public static boolean isDateEqual(Date dateToCheck) {
        Calendar calCheck = getCalendarInstance();
        calCheck.setTime(dateToCheck);
        //calCheck.setTimeZone(TimeZone.getTimeZone(ZoneId.of(TIME_ZONE_NYC)));
        LocalDate dateToCheckLocal = LocalDate.of(calCheck.get(1), calCheck.get(2) + 1,
                calCheck.get(Calendar.DAY_OF_MONTH));
        LocalDate currentDate = LocalDate.now(ZoneId.of(TIME_ZONE_NYC));
        return dateToCheckLocal.isEqual(currentDate);
    }
    
    /**
     * Date to check whether it is passed
     * @param cutOffHour
     * @param cutOffMin
     * @param cutOffSec
     * @param cutOffMilliSec
     * @return
     * @throws MBSBaseException
     */
    public static boolean isDateBeforeCutOffTime(int cutOffHour, int cutOffMin, int cutOffSec, int cutOffMilliSec)
            throws MBSBaseException {
        Calendar cutOffCal = getCalendarInstance();
        cutOffCal.setTime(MBSPortalUtils.getCurrentDate());
        //cutOffCal.setTimeZone(TimeZone.getTimeZone(ZoneId.of(TIME_ZONE_NYC)));
        cutOffCal.set(Calendar.HOUR_OF_DAY, cutOffHour);
        cutOffCal.set(Calendar.MINUTE, cutOffMin);
        cutOffCal.set(Calendar.SECOND, cutOffSec);
        cutOffCal.set(Calendar.MILLISECOND, cutOffMilliSec);
        if (MBSPortalUtils.getCurrentDate().before(cutOffCal.getTime())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @param dateTime
     * @param formatter
     * @return
     * @throws Exception
     */
    public static long getLocalDateCurrentTimeStampFromGivenDateTime(String dateTime, String formatter)
            throws Exception {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(formatter)).atZone(ZoneId.of(TIME_ZONE_NYC))
                .toInstant().toEpochMilli();

    }

    /**
     * 
     * @return
     */
    public static long getLocalDateCurrentTimeStamp() {
        return LocalDateTime.now().atZone(ZoneId.of(TIME_ZONE_NYC)).toInstant().toEpochMilli();

    }

    /**
     * @param epochMilli
     * @param formatter
     * @return
     */
    public static String getLocalZonedDateTimeFromEpochMilli(Long epochMilli, String formatter)  throws MBSBaseException {
        try{
            Instant instant = Instant.ofEpochMilli(epochMilli);
            ZoneId zoneId = ZoneId.of(TIME_ZONE_NYC);
            ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);
            return zdt.format(DateTimeFormatter.ofPattern(formatter));
        }catch(Exception exe){
            LOGGER.error("Failed to tranform the input data {"+ epochMilli +"} in format {"+formatter+"}");
            throw new MBSSystemException("Failed to tranform the input data {"+ epochMilli +"} in format {"+formatter+"}", MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
    }
    
    /**
     * 
     * @param epochMilli
     * @return
     * @throws MBSBaseException
     */
    public static Date getDateTimeFromEpochMilli(Long epochMilli)  throws MBSBaseException {
        
        Date date = new Date(epochMilli);
        return date;
    }

    /**
     * 
     * @param value
     * @return
     */
    public static boolean isNumeric(String value) {
        return StringUtils.isNumeric(value);
    }

    /**
     * 
     * @param number
     * @param min
     * @param max
     * @return
     */
    public static boolean isInRange(Number number, BigInteger min, BigInteger max) throws MBSBaseException {

        try {

            BigInteger bigInteger = null;
            if (number instanceof Byte || number instanceof Short || number instanceof Integer
                    || number instanceof Long) {
                bigInteger = BigInteger.valueOf(number.longValue());

            } else if (number instanceof Float || number instanceof Double) {
                bigInteger = new BigDecimal(number.doubleValue()).toBigInteger();

            } else if (number instanceof BigInteger) {
                bigInteger = (BigInteger) number;

            } else if (number instanceof BigDecimal) {
                bigInteger = ((BigDecimal) number).toBigInteger();

            } else {
                bigInteger = new BigDecimal(number.doubleValue()).toBigInteger();
            }
            return max.compareTo(bigInteger) >= 0 && min.compareTo(bigInteger) <= 0;

        } catch (NumberFormatException e) {
            return false;
        }
        catch (NullPointerException e) {
            LOGGER.error("Input a Null value as a Number", e);
            throw new MBSBaseException("Input a Null value as a Number", e);
        }
    }
    
    /**
     *
     * @param numberToCompare
     * @param numberWithWhichToComapre
     * @return
     * @throws MBSBaseException
     */
    public static boolean isGreaterOrEqual(Number numberToCompare, BigInteger numberWithWhichToComapre) throws MBSBaseException {

        try {

            BigInteger bigInteger = null;
            if (numberToCompare instanceof Byte || numberToCompare instanceof Short
                    || numberToCompare instanceof Integer || numberToCompare instanceof Long) {
                bigInteger = BigInteger.valueOf(numberToCompare.longValue());

            } else if (numberToCompare instanceof Float || numberToCompare instanceof Double) {
                bigInteger = new BigDecimal(numberToCompare.doubleValue()).toBigInteger();

            } else if (numberToCompare instanceof BigInteger) {
                bigInteger = (BigInteger) numberToCompare;

            } else if (numberToCompare instanceof BigDecimal) {
                bigInteger = ((BigDecimal) numberToCompare).toBigInteger();

            } else {
                bigInteger = new BigDecimal(numberToCompare.doubleValue()).toBigInteger();
            }
            return numberWithWhichToComapre.compareTo(bigInteger) <= 0;

        } catch (NumberFormatException e) {
            return false;
        }
        catch (NullPointerException e) {
            LOGGER.error("Input a Null value as a Number", e);
            throw new MBSBaseException("Input a Null value as a Number", e);
        }
    }

    /**
     * 
     * @param numberToCompare
     * @param numberWithWhichToComapre
     * @return
     */
    public static boolean isLessOrEqual(Number numberToCompare, BigInteger numberWithWhichToComapre) throws
            MBSBaseException {

        try {

            BigInteger bigInteger = null;
            if (numberToCompare instanceof Byte || numberToCompare instanceof Short
                    || numberToCompare instanceof Integer || numberToCompare instanceof Long) {
                bigInteger = BigInteger.valueOf(numberToCompare.longValue());

            } else if (numberToCompare instanceof Float || numberToCompare instanceof Double) {
                bigInteger = new BigDecimal(numberToCompare.doubleValue()).toBigInteger();

            } else if (numberToCompare instanceof BigInteger) {
                bigInteger = (BigInteger) numberToCompare;

            } else if (numberToCompare instanceof BigDecimal) {
                bigInteger = ((BigDecimal) numberToCompare).toBigInteger();

            } else {
                bigInteger = new BigDecimal(numberToCompare.doubleValue()).toBigInteger();
            }
            return numberWithWhichToComapre.compareTo(bigInteger) >= 0;

        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            LOGGER.error("Input a Null value as a Number", e);
            throw new MBSBaseException("Input a Null value as a Number", e);
        }
    }

    /**
     * 
     * Utility Method for Backward Compatibility: CMMBSSTA01-1040: If there is
     * any existing Trade with Eights 4 then convert into +
     * 
     * @param pricePercentTicksText
     * @return String
     */
    public static String pricePercentTicksDisplayText(String pricePercentTicksText) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.length(pricePercentTicksText) == 3) {
            if (TradeConstants.EIGHTS_MIDDLE_VALUE_NUMERIC
                    .equalsIgnoreCase(StringUtils.substring(pricePercentTicksText, 2))) {
                sb.append(StringUtils.substring(pricePercentTicksText, 0, 2) + TradeConstants.EIGHTS_MIDDLE_VALUE_SIGN);
                return sb.toString();
            }
        }
        return pricePercentTicksText;
    }
    
    /**
     *
     * @param exList
     * @param errorCode
     * @param errorMessage
     * @param errorCategory
     * @param messageType
     * @return
     */
    // Not needed now
    @Deprecated
    public static List<ExceptionLookupPO> createExceptionList(List<ExceptionLookupPO> exList, String errorCode,
            String errorMessage, String errorCategory, String messageType) {
        List<ExceptionLookupPO> exceptionList;
        if (Objects.isNull(exList) || exList.size() <= 0) {
            exceptionList = new ArrayList<>();
        } else {
            exceptionList = exList;
        }
        ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        exceptionLookupPO.setErrorCode(errorCode);
        exceptionLookupPO.setErrorCategory(errorCategory);
        exceptionLookupPO.setMessageType(messageType);
        exceptionLookupPO.setErrorMessage(errorMessage);
        exceptionList.add(exceptionLookupPO);
        return exceptionList;
    }
    
    /**
     *
     * @param exceptionLookupPOLst
     * @return
     */
    public static ExceptionResponsePO getExceptionMessage(List<ExceptionLookupPO> exceptionLookupPOLst) {

        ExceptionResponsePO exceptionResponsePO = null;
        ExceptionResponseMapper exceptionResponseMapper = new ExceptionResponseMapper();
        if (Objects.nonNull(exceptionLookupPOLst) && exceptionLookupPOLst.size() > 0) {
            exceptionResponsePO = exceptionResponseMapper.transformResponse(exceptionLookupPOLst);
        } else {
            return getExceptionInternalServer();
        }
        return exceptionResponsePO;
    }

    /**
     * 
     * 
     * @return ExceptionResponsePO
     */
    public static ExceptionResponsePO getExceptionInternalServer() {

        List<ExceptionMessagesPO> messages = new ArrayList<ExceptionMessagesPO>();
        ExceptionMessagesPO exceptionMessagesPO = new ExceptionMessagesPO();
        exceptionMessagesPO.setErrorMessage(MBSExceptionConstants.SYSM_0002_ERROR_MESSAGE);
        exceptionMessagesPO.setMessageCode(MBSExceptionConstants.SYSM_0002);
        exceptionMessagesPO.setMessageType(MBSExceptionConstants.DISPLAY_MESSAGE);
        messages.add(exceptionMessagesPO);
        return (new ExceptionResponsePO(messages, null, "INTERNAL_SERVER_ERROR"));

    }


    public static String getLeftPaddedString(String str, int len, char padChar) {

        if (StringUtils.isBlank(str)) {
            return str;
        }
        return StringUtils.leftPad(StringUtils.right(str, len), str.length(), padChar);
    }

    public static String getLeftPaddedString(String str) {

        if (StringUtils.isBlank(str)) {
            return str;
        }
        return getLeftPaddedString(str, DEFAULT_NUMBER_PAD_CHARACTER_TO_SHOW, DEFAULT_PAD_CHARACTER);
    }
 
}
