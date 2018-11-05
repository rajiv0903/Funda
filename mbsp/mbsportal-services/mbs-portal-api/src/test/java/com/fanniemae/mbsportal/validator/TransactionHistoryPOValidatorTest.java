package com.fanniemae.mbsportal.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.fanniemae.mbsportal.api.validator.TransactionHistoryPOValidator;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.constants.AcceptedTradesBoolean;
import com.fanniemae.mbsportal.constants.RegionColumnList;
import com.fanniemae.mbsportal.gf.pojo.SortBy;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.DataExceptionMessage;
import com.fanniemae.mbsportal.utils.exception.ExceptionMessagesPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;

public class TransactionHistoryPOValidatorTest extends BaseServiceTest {

    List<ExceptionMessagesPO> excpMsgs = new ArrayList<ExceptionMessagesPO>();

    @Test
    public void testValidateAcceptedTradesNull() throws MBSBaseException {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateAcceptedTrades(dataExceptionMessages, null, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSExceptionConstants.PAGE_1001);
    }

    @Test
    public void testValidateAcceptedTradesEmpty() throws MBSBaseException {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateAcceptedTrades(dataExceptionMessages, AcceptedTradesBoolean.empty,
                "History");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSExceptionConstants.PAGE_1000);
    }

    @Test
    public void testValidateAcceptedTradesTrueFalse() throws MBSBaseException {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateAcceptedTrades(dataExceptionMessages, AcceptedTradesBoolean.trueValue,
                "History");
        Assert.assertEquals(dataExceptionMessages.size(), 0);
        TransactionHistoryPOValidator.validateAcceptedTrades(dataExceptionMessages, AcceptedTradesBoolean.falseValue,
                "History");
        Assert.assertEquals(dataExceptionMessages.size(), 0);
    }

    @Test
    public void testValidateSortFieldNull() throws MBSBaseException {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateSortField(dataExceptionMessages, null, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSExceptionConstants.PAGE_1001);
    }

    @Test
    public void testValidateSortFieldEmpty() throws MBSBaseException {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateSortField(dataExceptionMessages, RegionColumnList.empty, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSExceptionConstants.PAGE_1000);
    }

    @Test
    public void testValidateSortFieldValue() throws MBSBaseException {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateSortField(dataExceptionMessages, RegionColumnList.lenderBuySellType,
                "History");
        Assert.assertEquals(dataExceptionMessages.size(), 0);
        TransactionHistoryPOValidator.validateSortField(dataExceptionMessages, RegionColumnList.stateType, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 0);
    }

    @Test
    public void testValidateSortOrderNull() throws MBSBaseException {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateSortOrder(dataExceptionMessages, null, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSExceptionConstants.PAGE_1001);
    }

    @Test
    public void testValidateSortOrderEmpty() throws MBSBaseException {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateSortOrder(dataExceptionMessages, SortBy.empty, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSExceptionConstants.PAGE_1000);
    }

    @Test
    public void testValidateSortOrderAscDesc() throws MBSBaseException {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateSortOrder(dataExceptionMessages, SortBy.asc, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 0);
        TransactionHistoryPOValidator.validateSortOrder(dataExceptionMessages, SortBy.desc, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 0);
    }

    @Test
    public void testValidatePageIndexNull() throws MBSBaseException {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validatePageIndex(dataExceptionMessages, null, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSExceptionConstants.PAGE_1000);
        dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validatePageIndex(dataExceptionMessages, 0, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSExceptionConstants.PAGE_1000);
    }

    @Test
    public void testValidatePageIndexInvalid() throws MBSBaseException {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validatePageIndex(dataExceptionMessages, -1, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSExceptionConstants.PAGE_1001);
    }

    @Test
    public void testValidatePageIndexValid() throws MBSBaseException {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validatePageIndex(dataExceptionMessages, 1, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 0);
        TransactionHistoryPOValidator.validatePageIndex(dataExceptionMessages, 12, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 0);
    }

    @Test
    public void testValidatePageSizeNull() throws MBSBaseException {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validatePageSize(dataExceptionMessages, null, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSExceptionConstants.PAGE_1000);
        dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validatePageSize(dataExceptionMessages, 0, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSExceptionConstants.PAGE_1000);
    }

    @Test
    public void testValidatePageSizeInvalid() throws MBSBaseException {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validatePageSize(dataExceptionMessages, -1, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSExceptionConstants.PAGE_1001);
    }

    @Test 
    public void testValidatePageSizeValid() throws MBSBaseException {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validatePageSize(dataExceptionMessages, 1, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 0);
        TransactionHistoryPOValidator.validatePageSize(dataExceptionMessages, 12, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 0);
    }

   // validateDates tests
    @Test
    public void testexportvalidateDatesStartDateNullFutureEndDate() {
    	List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        String tomorrowsDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        TransactionHistoryPOValidator.validateDates(dataExceptionMessages, null, tomorrowsDate, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 2);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_EMPTY_INPUT);
        Assert.assertEquals(dataExceptionMessages.get(1).getErrorCode(), MBSPServiceConstants.MSG_CODE_FUTURE_DATE);
        
 
    }

    @Test
    public void testexportvalidateDatesStartDateWrongFormatEndDateNull() {
    	List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateDates(dataExceptionMessages," 20181005" , null, "History");
        Assert.assertEquals(dataExceptionMessages.size(), 2);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_INVALID_DATE_FORMAT);
        Assert.assertEquals(dataExceptionMessages.get(1).getErrorCode(), MBSPServiceConstants.MSG_CODE_EMPTY_INPUT);
    	
    }
    
    

    @Test
    public void testexportvalidateDatesInvalidDateFormat() {
    	List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateDates(dataExceptionMessages, "20181005 ", " 20181005 ", "History"); 
        Assert.assertEquals(dataExceptionMessages.size(), 2);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_INVALID_DATE_FORMAT);
        Assert.assertEquals(dataExceptionMessages.get(1).getErrorCode(), MBSPServiceConstants.MSG_CODE_INVALID_END_DATE_FORMAT);
    }
    


    @Test
    public void testexportvalidateDatesStartDateAfterEndDate() {
    	List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateDates(dataExceptionMessages, "2018-05-07 ", " 2018-05-06 ", "History"); 
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_FROM_DATE_AFTER_TO_DATE);
    }

    @Test
    public void testexportvalidateDatesOutOfRange() {
    	List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateDates(dataExceptionMessages, "2017-02-05 ", "2018-02-06 ", "History"); 
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_INVALID_DATE_RANGE);

    }

    @Test
    public void testexportvalidateDatesEndDateInFuture() {
    	List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
    	Date date = new Date();
    	String todaysDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        String tomorrowsDate = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        TransactionHistoryPOValidator.validateDates(dataExceptionMessages, todaysDate, tomorrowsDate, "History"); 
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_FUTURE_DATE);
    }

    @Test
    public void testexportvalidateDatesValidDates() {
    	List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
    	Date date = new Date();
        String todaysDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        TransactionHistoryPOValidator.validateDates(dataExceptionMessages, todaysDate, todaysDate, "History"); 
        Assert.assertEquals(dataExceptionMessages.size(), 0);
 
    }

    // validateAcceptedTrades
    @Test
    public void testexportValidateAcceptedTradesNull() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateAcceptedTrades(dataExceptionMessages, null, "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_INVALID_INPUT);
    }

    @Test
    public void testexportValidateAcceptedTradesEmpty() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateAcceptedTrades(dataExceptionMessages, AcceptedTradesBoolean.empty,
                "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_EMPTY_INPUT);

    }

    @Test
    public void testexportValidateAcceptedTradesTrue() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateAcceptedTrades(dataExceptionMessages, AcceptedTradesBoolean.trueValue, "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_INVALID_INPUT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testexportValidateAcceptedTradesIncorrect() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateAcceptedTrades(dataExceptionMessages, AcceptedTradesBoolean.valueOf("falseWrong"), "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_INVALID_INPUT);
    }

    @Test
    public void testexportValidateAcceptedTradesCorrect() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateAcceptedTrades(dataExceptionMessages, AcceptedTradesBoolean.falseValue, "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 0);
    }

    // validateSortField tests
    @Test
    public void testexportValidateSortFieldNull() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateSortField(dataExceptionMessages, null, "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_INVALID_INPUT);
    }

    @Test
    public void testexportValidateSortFieldEmpty() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateSortField(dataExceptionMessages, RegionColumnList.empty, "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_EMPTY_INPUT);
    }

    @Test
    public void testexportValidateSortFieldDiffColumn() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateSortField(dataExceptionMessages, RegionColumnList.lenderBuySellType, "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_INVALID_INPUT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testexportValidateSortFieldIncorrect() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateSortField(dataExceptionMessages, RegionColumnList.valueOf("wrongColumn"), "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_INVALID_INPUT);
    }

    @Test
    public void testexportValidateSortFieldCorrect() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateSortField(dataExceptionMessages, RegionColumnList.submissionDate, "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 0);
    }

    // validateSortOrder tests
    @Test
    public void testexportValidateSortOrderNull() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateSortOrder(dataExceptionMessages, null, "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_INVALID_INPUT);
    }

    @Test
    public void testexportValidateSortOrderEmpty() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateSortOrder(dataExceptionMessages, SortBy.empty, "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_EMPTY_INPUT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testexportValidateSortOrderIncorrect() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();    
        TransactionHistoryPOValidator.validateSortOrder(dataExceptionMessages, SortBy.valueOf("wrongColumn"), "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_INVALID_INPUT);
    }

    @Test
    public void testexportValidateSortOrderAsc() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();  
        TransactionHistoryPOValidator.validateSortOrder(dataExceptionMessages, SortBy.asc, "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 0);
    }

    @Test
    public void testexportValidateSortOrderDesc() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();  
        TransactionHistoryPOValidator.validateSortOrder(dataExceptionMessages, SortBy.desc, "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 0);
    }
    
    

     //validateExportType tests
    @Test
    public void testexportValidateExportTypeNull() {   
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateExportType(dataExceptionMessages, null, "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_EMPTY_INPUT);
    }

    @Test
    public void testexportValidateExportTypeIncorrect() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();    
        TransactionHistoryPOValidator.validateExportType(dataExceptionMessages, "cSvExCeL", "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_INVALID_INPUT);
    }

    @Test
    public void testexportValidateExportTypeCsv() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();    
        TransactionHistoryPOValidator.validateExportType(dataExceptionMessages, "cSv", "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 0);

    }

    @Test
    public void testexportValidateExportTypeExcel() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();    
        TransactionHistoryPOValidator.validateExportType(dataExceptionMessages, "ExCeL", "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 0);
    }

    // validateDateType tests
    @Test
    public void testexportValidateDateTypeNull() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateDateType(dataExceptionMessages, null, "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_EMPTY_INPUT);
    }

    @Test
    public void testexportValidateDateTypeEmpty() {  
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateDateType(dataExceptionMessages, "", "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_EMPTY_INPUT);
    }

    @Test
    public void testexportValidateDateTypeIncorrect() {
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();    
        TransactionHistoryPOValidator.validateExportType(dataExceptionMessages, "submissionD", "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 1);
        Assert.assertNotNull(dataExceptionMessages.get(0));
        Assert.assertEquals(dataExceptionMessages.get(0).getErrorCode(), MBSPServiceConstants.MSG_CODE_INVALID_INPUT);
    }

    @Test
    public void testexportValidateDateTypeCorrect() {   
        List<DataExceptionMessage> dataExceptionMessages = new ArrayList<>();
        TransactionHistoryPOValidator.validateDateType(dataExceptionMessages, "SuBmIsSiOnDaTe", "History_Export");
        Assert.assertEquals(dataExceptionMessages.size(), 0);

    }
}

