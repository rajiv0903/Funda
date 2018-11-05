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

package com.fanniemae.mbsportal.controller.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.api.cdx.CDXClientApi;
import com.fanniemae.mbsportal.api.controller.helper.TransactionHistoryControllerHelper;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementRolePO;
import com.fanniemae.mbsportal.api.po.TransactionHistoryPO;
import com.fanniemae.mbsportal.api.po.TransactionHistorySortPO;
import com.fanniemae.mbsportal.api.service.TransactionHistoryService;
import com.fanniemae.mbsportal.constants.AcceptedTradesBoolean;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.RegionColumnList;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.gf.pojo.SortBy;
import com.fanniemae.mbsportal.service.MBSExceptionService;
import com.fanniemae.mbsportal.utils.aws.AWSS3Service;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;
import com.fanniemae.mbsportal.utils.exception.ExceptionMessagesPO;
import com.fanniemae.mbsportal.utils.exception.ExceptionResponsePO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSDataException;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Jan 22, 2018
 * @Time 12:12:16 PM com.fanniemae.mbsportal.api.controller.helper
 *       TransactionHistoryControllerHelperTest.java
 * @Description: CMMBSSTA01-946: (Tech) Delete Old Transaction History Endpoint
 *               and Test- Added JUnit Test Cases for Different Scenarios
 */

@RunWith(MockitoJUnitRunner.class)
public class TransactionHistoryControllerHelperTest {

    @InjectMocks
    TransactionHistoryControllerHelper transactionHistoryControllerHelper;

    @Mock
    TransactionHistoryService transactionHistoryService;

    @Mock
    CDXClientApi cdxClientApi;
    
    @Mock
    MBSExceptionService mbsExceptionService;
    
    @Mock
    AWSS3Service s3Service;

    TransactionHistoryPO transactionHistoryPO;

    ProductPO prodPO;

    ProductIdPO productId;

    List<TransactionHistoryPO> lstMbsTransHist;

    Map<String, String> headerMap;

    ProfileEntitlementPO profileEntitlementPO;
    
    String publicURLString;

    /**
     *
     */
    public void createData() {
        transactionHistoryPO = new TransactionHistoryPO();
        headerMap = new HashMap<String, String>();
        prodPO = new ProductPO();
        productId = new ProductIdPO();
        productId.setIdentifier(new Long(1));
        productId.setSourceType(PRODUCT_SOURCE_TYPE.PU);
        productId.setType(PRODUCT_TYPE.MBS);
        prodPO.setProductId(productId);
        transactionHistoryPO.setProduct(prodPO);
        transactionHistoryPO.setTradeAmount("500000");
        transactionHistoryPO.setTradeBuySellType("SELL");
        transactionHistoryPO.setTradeCouponRate("2.0");
        transactionHistoryPO.setTradeSettlementDate("2017-10-12");
        transactionHistoryPO.setLenderName("98765432");
        transactionHistoryPO.setStateType(StateType.LENDER_OPEN);

        lstMbsTransHist = new ArrayList<TransactionHistoryPO>();
        lstMbsTransHist.add(transactionHistoryPO);
        

        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementPO.setUserName("username");
        profileEntitlementPO.setFirstName("firstName");
        profileEntitlementPO.setLastName("lastName");
        profileEntitlementPO.setSellerServicerNumber("11111");
        
        publicURLString = "http://thisBucket/myFolder/myFile.csv";
    }

    @Before
    public void setUp() throws Exception {

    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void getTransactionHistorySorted_With_Trader_Role_Success() throws MBSBaseException {
        createData();
        Map<String, String> headerMap = new HashMap<>();
        String sellerServiceNumber = "121212";
        String sortBy = "tradeBuySellType";
        String sortOrder = SortBy.asc.getName();
        Integer pageIndex = 1;
        Integer pageSize = 100;
        when(transactionHistoryService.getTransactionRequestSorted(MBSRoleType.TRADER, sellerServiceNumber, false, sortBy, sortOrder,
                pageIndex, pageSize)).thenReturn(lstMbsTransHist);
        doNothing().when(mbsExceptionService).createBusinessExceptionsAndLog(Mockito.anyString(), Mockito.anyList(), Matchers.<String>anyVararg());


        List<ProfileEntitlementRolePO> profRoleLst = new ArrayList<ProfileEntitlementRolePO>();
        ProfileEntitlementRolePO profRole = new ProfileEntitlementRolePO();
        profRole.setName(EntitlementRole.TRADER_TRADE_EXECUTE);
        profRoleLst.add(profRole);
        profileEntitlementPO.setRoles(profRoleLst);
        profileEntitlementPO.setSellerServicerNumber(sellerServiceNumber);
        profileEntitlementPO.setFannieMaeUser(true);
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        when(cdxClientApi.getProfileEntitlementPO(any(), any())).thenReturn(profileEntitlementPO);

        List<TransactionHistoryPO> lstMbsTransHistRet = transactionHistoryControllerHelper
                .getTransactionHistorySorted(headerMap, AcceptedTradesBoolean.falseValue, RegionColumnList.getEnum(sortBy), SortBy.getEnum(sortOrder), pageIndex, pageSize, new TransactionHistorySortPO());
                
        assertEquals(lstMbsTransHist.size(), lstMbsTransHistRet.size());
    }
    
    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void getTransactionHistorySorted_With_Trader_Role_V2_Success() throws MBSBaseException {
        createData();
        Map<String, String> headerMap = new HashMap<>();
        String sellerServiceNumber = "121212";
        String sortBy = "tradeBuySellType";
        String sortOrder = SortBy.asc.getName();
        Integer pageIndex = 1;
        Integer pageSize = 100;
        lstMbsTransHist = new ArrayList<>();
        when(transactionHistoryService.getTransactionRequestSorted(MBSRoleType.TRADER, sellerServiceNumber, false, sortBy, sortOrder,
                pageIndex, pageSize)).thenReturn(lstMbsTransHist);
        doNothing().when(mbsExceptionService).createBusinessExceptionsAndLog(Mockito.anyString(), Mockito.anyList(), Matchers.<String>anyVararg());

        List<ProfileEntitlementRolePO> profRoleLst = new ArrayList<ProfileEntitlementRolePO>();
        
        ProfileEntitlementRolePO profRole = new ProfileEntitlementRolePO();
        profRole.setName(EntitlementRole.MBSP_FM_TRADER_EXECUTE_HE);
        profRoleLst.add(profRole);
        
        profRole = new ProfileEntitlementRolePO();
        profRole.setName(EntitlementRole.MBSP_FM_TRADER_EXECUTE_LE);
        profRoleLst.add(profRole);
        
        profileEntitlementPO.setRoles(profRoleLst);
        profileEntitlementPO.setSellerServicerNumber(sellerServiceNumber);
        profileEntitlementPO.setFannieMaeUser(true);

        when(cdxClientApi.getProfileEntitlementPO(any(), any())).thenReturn(profileEntitlementPO);

        List<TransactionHistoryPO> lstMbsTransHistRet = transactionHistoryControllerHelper
        		.getTransactionHistorySorted(headerMap, AcceptedTradesBoolean.falseValue, RegionColumnList.getEnum(sortBy), SortBy.getEnum(sortOrder), pageIndex, pageSize, new TransactionHistorySortPO());
        assertEquals(lstMbsTransHist.size(), lstMbsTransHistRet.size());
    }

    @Test
    public void getTransactionHistorySorted_With_Lender_Role_Success() throws MBSBaseException {

        createData();
        Map<String, String> headerMap = new HashMap<>();
        String sellerServiceNumber = "121212";
        String sortBy = "tradeBuySellType";
        String sortOrder = SortBy.asc.getName();
        Integer pageIndex = 1;
        Integer pageSize = 100;
        

        List<ProfileEntitlementRolePO> profRoleLst = new ArrayList<ProfileEntitlementRolePO>();
        ProfileEntitlementRolePO profRole = new ProfileEntitlementRolePO();
        profRole.setName(EntitlementRole.LENDER_TRADE_EXECUTE);
        profRoleLst.add(profRole);
        profileEntitlementPO.setRoles(profRoleLst);
        profileEntitlementPO.setSellerServicerNumber(sellerServiceNumber);

        doReturn(lstMbsTransHist).when(transactionHistoryService).getTransactionRequestSorted(MBSRoleType.LENDER, sellerServiceNumber,
                false, sortBy, sortOrder, pageIndex, pageSize);
        doNothing().when(mbsExceptionService).createBusinessExceptionsAndLog(Mockito.anyString(), Mockito.anyList(), Matchers.<String>anyVararg());

        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        doReturn(profileEntitlementPO).when(cdxClientApi).getProfileEntitlementPO(any(), any());

        List<TransactionHistoryPO> lstMbsTransHistRet = transactionHistoryControllerHelper
				.getTransactionHistorySorted(headerMap, AcceptedTradesBoolean.falseValue, RegionColumnList.getEnum(sortBy), SortBy.getEnum(sortOrder), pageIndex, pageSize, new TransactionHistorySortPO());

        assertEquals(lstMbsTransHist.size(), lstMbsTransHistRet.size());
    }
    
    @Test
    public void getTransactionHistorySorted_With_Lender_Role_Success_AcceptedTrades() throws MBSBaseException {

        createData();
        Map<String, String> headerMap = new HashMap<>();
        String sellerServiceNumber = "121212";
        String sortBy = "tradeBuySellType";
        String sortOrder = SortBy.asc.getName();
        Integer pageIndex = 1;
        Integer pageSize = 100;

        List<ProfileEntitlementRolePO> profRoleLst = new ArrayList<ProfileEntitlementRolePO>();
        ProfileEntitlementRolePO profRole = new ProfileEntitlementRolePO();
        profRole.setName(EntitlementRole.LENDER_TRADE_EXECUTE);
        profRoleLst.add(profRole);
        profileEntitlementPO.setRoles(profRoleLst);
        profileEntitlementPO.setSellerServicerNumber(sellerServiceNumber);

        doReturn(lstMbsTransHist).when(transactionHistoryService).getTransactionRequestSorted(MBSRoleType.LENDER, sellerServiceNumber, 
                true, sortBy, sortOrder, pageIndex, pageSize);
        doNothing().when(mbsExceptionService).createBusinessExceptionsAndLog(Mockito.anyString(), Mockito.anyList(), Matchers.<String>anyVararg());

        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        doReturn(profileEntitlementPO).when(cdxClientApi).getProfileEntitlementPO(any(), any());

        List<TransactionHistoryPO> lstMbsTransHistRet = transactionHistoryControllerHelper
				.getTransactionHistorySorted(headerMap, AcceptedTradesBoolean.trueValue, RegionColumnList.getEnum(sortBy), SortBy.getEnum(sortOrder), pageIndex, pageSize, new TransactionHistorySortPO());

        assertEquals(lstMbsTransHist.size(), lstMbsTransHistRet.size());
    }

    @Test
    public void getTransactionHistorySorted_With_Admin_Role_Success() throws MBSBaseException {

        createData();
        Map<String, String> headerMap = new HashMap<>();
        String sellerServiceNumber = "121212";
        String sortBy = "tradeBuySellType";
        String sortOrder = SortBy.asc.getName();
        Integer pageIndex = 1;
        Integer pageSize = 100;

        List<ProfileEntitlementRolePO> profRoleLst = new ArrayList<ProfileEntitlementRolePO>();
        ProfileEntitlementRolePO profRole = new ProfileEntitlementRolePO();
        profRole.setName(EntitlementRole.ADMIN);
        profRoleLst.add(profRole);
        profileEntitlementPO.setRoles(profRoleLst);
        profileEntitlementPO.setSellerServicerNumber(sellerServiceNumber);

        doReturn(lstMbsTransHist).when(transactionHistoryService).getTransactionRequestSorted(MBSRoleType.LENDER, sellerServiceNumber, 
                false, sortBy, sortOrder, pageIndex, pageSize);
        doNothing().when(mbsExceptionService).createBusinessExceptionsAndLog(Mockito.anyString(), Mockito.anyList(), Matchers.<String>anyVararg());

        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        doReturn(profileEntitlementPO).when(cdxClientApi).getProfileEntitlementPO(any(), any());

        List<TransactionHistoryPO> lstMbsTransHistRet = transactionHistoryControllerHelper
				.getTransactionHistorySorted(headerMap, AcceptedTradesBoolean.falseValue, RegionColumnList.getEnum(sortBy), SortBy.getEnum(sortOrder), pageIndex, pageSize, new TransactionHistorySortPO());

        assertEquals(lstMbsTransHist.size(), lstMbsTransHistRet.size());
    }
    
    /**
     * 
     * CMMBSSTA01-1371: (Lender page) TSP chooses / switches a lender
     * @throws MBSBaseException
     */
    @Test
    public void getTransactionHistorySorted_With_TSP_Role_Success() throws MBSBaseException {

        createData();
        Map<String, String> headerMap = new HashMap<>();
        String sellerServiceNumber = "121212";
        String sortBy = "tradeBuySellType";
        String sortOrder = SortBy.asc.getName();
        Integer pageIndex = 1;
        Integer pageSize = 100;

        List<ProfileEntitlementRolePO> profRoleLst = new ArrayList<ProfileEntitlementRolePO>();
        ProfileEntitlementRolePO profRole = new ProfileEntitlementRolePO();
        profRole.setName(EntitlementRole.TSP_TRADE_EXECUTE);
        profRoleLst.add(profRole);
        profileEntitlementPO.setRoles(profRoleLst);
        profileEntitlementPO.setTspUser(true);
        profileEntitlementPO.setSellerServicerNumber(sellerServiceNumber);

        doReturn(lstMbsTransHist).when(transactionHistoryService).getTransactionRequestSorted(MBSRoleType.TSP, sellerServiceNumber, 
                false, sortBy, sortOrder, pageIndex, pageSize);
        doNothing().when(mbsExceptionService).createBusinessExceptionsAndLog(Mockito.anyString(), Mockito.anyList(), Matchers.<String>anyVararg());

        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        doReturn(profileEntitlementPO).when(cdxClientApi).getProfileEntitlementPO(any(), any());

        List<TransactionHistoryPO> lstMbsTransHistRet = transactionHistoryControllerHelper
				.getTransactionHistorySorted(headerMap, AcceptedTradesBoolean.falseValue, RegionColumnList.getEnum(sortBy), SortBy.getEnum(sortOrder), pageIndex, pageSize, new TransactionHistorySortPO());

        assertEquals(lstMbsTransHist.size(), lstMbsTransHistRet.size());
    }

    @Test(expected = MBSBusinessException.class)
    public void getTransactionHistorySorted_Empty_Profile_Failure() throws MBSBaseException {

        createData();
        Map<String, String> headerMap = new HashMap<>();
        String sellerServiceNumber = "121212";
        String sortBy = "tradeBuySellType";
        String sortOrder = SortBy.asc.getName();
        Integer pageIndex = 1;
        Integer pageSize = 100;

        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        doThrow(MBSBusinessException.class).when(cdxClientApi).getProfileEntitlementPO(any(), any());
        doNothing().when(mbsExceptionService).createBusinessExceptionsAndLog(Mockito.anyString(), Mockito.anyList(), Matchers.<String>anyVararg());

        doReturn(lstMbsTransHist).when(transactionHistoryService).getTransactionRequestSorted(MBSRoleType.LENDER, sellerServiceNumber, 
                false, sortBy, sortOrder, pageIndex, pageSize);

        List<TransactionHistoryPO> lstMbsTransHistRet = transactionHistoryControllerHelper
				.getTransactionHistorySorted(headerMap, AcceptedTradesBoolean.falseValue, RegionColumnList.getEnum(sortBy), SortBy.getEnum(sortOrder), pageIndex, pageSize, new TransactionHistorySortPO());


        assertEquals(lstMbsTransHist.size(), lstMbsTransHistRet.size());
    }

    @Test(expected = MBSBusinessException.class)
    public void getTransactionHistorySorted_Empty_Profile_Roles_Failure() throws MBSBaseException {

        createData();
        Map<String, String> headerMap = new HashMap<>();
        String sellerServiceNumber = "121212";
        String sortBy = "tradeBuySellType";
        String sortOrder = SortBy.asc.getName();
        Integer pageIndex = 1;
        Integer pageSize = 100;

        profileEntitlementPO.setRoles(null);

        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        when(cdxClientApi.getProfileEntitlementPO(any(), any())).thenReturn(null);

        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        doThrow(MBSBusinessException.class).when(cdxClientApi).getProfileEntitlementPO(any(), any());

        doReturn(lstMbsTransHist).when(transactionHistoryService).getTransactionRequestSorted(MBSRoleType.LENDER, sellerServiceNumber, 
                false, sortBy, sortOrder, pageIndex, pageSize);
        doNothing().when(mbsExceptionService).createBusinessExceptionsAndLog(Mockito.anyString(), Mockito.anyList(), Matchers.<String>anyVararg());

        List<TransactionHistoryPO> lstMbsTransHistRet = transactionHistoryControllerHelper
				.getTransactionHistorySorted(headerMap, AcceptedTradesBoolean.falseValue, RegionColumnList.getEnum(sortBy), SortBy.getEnum(sortOrder), pageIndex, pageSize, new TransactionHistorySortPO());


        assertEquals(lstMbsTransHist.size(), lstMbsTransHistRet.size());
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    public void getTransactionHistoryExportSorted_With_Trader_Role_Success() throws MBSBaseException {
        createData();
        Map<String, String> headerMap = new HashMap<>();
        String sortBy = "submissionDate";
        String sortOrder = SortBy.asc.getName();
        String startDate = "2018-05-30";
        String endDate = "2018-05-30";
        String exportType = "CSV";
        String dateType = "submissionDate";

        mockServiceAndEntitlement();
        
        String lstMbsTransHistRet = transactionHistoryControllerHelper
                .getTransactionHistoryExportBySort(headerMap, AcceptedTradesBoolean.falseValue, RegionColumnList.getEnum(sortBy), SortBy.getEnum(sortOrder), startDate, endDate, exportType, dateType);
        Assert.assertNotNull(lstMbsTransHistRet);
        assertEquals(publicURLString.length(), lstMbsTransHistRet.length());
    }

    private void mockServiceAndEntitlement() throws MBSBaseException {
        Mockito.when(transactionHistoryService.getTransactionRequestExportSorted(Mockito.any(MBSRoleType.class), Mockito.anyString(), Mockito.any(Map.class))).thenReturn(new byte[500]);

        List<ProfileEntitlementRolePO> profRoleLst = new ArrayList<ProfileEntitlementRolePO>();
        ProfileEntitlementRolePO profRole = new ProfileEntitlementRolePO();
        profRole.setName(EntitlementRole.TRADER_TRADE_EXECUTE);
        profRoleLst.add(profRole);
        profileEntitlementPO.setRoles(profRoleLst);
        // CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid
        when(cdxClientApi.getProfileEntitlementPO(any(), any())).thenReturn(profileEntitlementPO);
		when(s3Service.getUnSignedURL(Mockito.anyString(), Mockito.eq(new byte[500]))).thenReturn(publicURLString);
	}

	/**
     * 
     * @throws MBSBaseException
     */
    @Test
    @Ignore
    public void getTransactionHistoryExportSorted_With_Trader_Role_ValidationError() throws MBSBaseException {
        createData();
        Map<String, String> headerMap = new HashMap<>();
        String acceptedTradesBoolean = "blah";
        String sortBy = "blah";
        String sortOrder = "blah";
        String startDate = "blah";
        String endDate = "blah";
        String exportType = "blah";
        String dateType = "blah";
        mockServiceAndEntitlement();

        String lstMbsTransHistRet = null;
        try {
        	lstMbsTransHistRet = transactionHistoryControllerHelper
                .getTransactionHistoryExportBySort(headerMap, AcceptedTradesBoolean.getEnum(acceptedTradesBoolean), RegionColumnList.getEnum(sortBy), SortBy.getEnum(sortOrder), startDate, endDate, exportType, dateType);
        	fail("Invalid Inputs should throw exception");
        } catch (Exception e) {
        	Assert.assertEquals(MBSDataException.class.getName(), e.getClass().getName());
        	MBSDataException mde = (MBSDataException) e;
        	Assert.assertNotNull(mde.getExcpRespPO());
        	Assert.assertNotNull(mde.getExcpRespPO().getMessages());
        	List<String> errorMessages = Arrays.asList(
        			"Invalid input parameter accepted_trades",
        			"Invalid input parameter sort_by",
        			"Invalid input parameter sort_order",
        			"Invalid input parameter export_type:blah",
        			"Invalid input parameter date_type:blah",
        			"Start Date and End Date format should be yyyy-MM-dd, blah",
        			"Start Date and End Date format should be yyyy-MM-dd, blah");
        	List<String> errorCodes = Arrays.asList("EXPT_1001","EXPT_1001","EXPT_1001","EXPT_1001","EXPT_1001","EXPT_1003","EXPT_1003");
        	Assert.assertEquals(errorMessages.size(),mde.getExcpRespPO().getMessages().size());
        	int i = 0;
        	for (ExceptionMessagesPO message : mde.getExcpRespPO().getMessages()) {
        		Assert.assertEquals(errorMessages.get(i), message.getErrorMessage());
        		Assert.assertEquals(errorCodes.get(i), message.getMessageCode());
        		i++;
        	}
        }
        Assert.assertNull(lstMbsTransHistRet);
    }

    /**
     * 
     * @throws MBSBaseException
     */
    @Test
    @Ignore
    public void getTransactionHistoryExportSorted_With_Trader_Role_ValidationNulls() throws MBSBaseException {
        createData();
        Map<String, String> headerMap = new HashMap<>();
        String acceptedTradesBoolean = "";
        String sortBy = "";
        String sortOrder = "";
        String startDate = "";
        String endDate = "";
        String exportType = "";
        String dateType = "";
        mockServiceAndEntitlement();

        String lstMbsTransHistRet = null;
        try {
        	lstMbsTransHistRet = transactionHistoryControllerHelper
                .getTransactionHistoryExportBySort(headerMap, AcceptedTradesBoolean.getEnum(acceptedTradesBoolean), RegionColumnList.getEnum(sortBy), SortBy.getEnum(sortOrder), startDate, endDate, exportType, dateType);
        	fail("Invalid Inputs should throw exception");
        } catch (Exception e) {
        	Assert.assertEquals(MBSDataException.class.getName(), e.getClass().getName());
        	MBSDataException mde = (MBSDataException) e;
        	Assert.assertNotNull(mde.getExcpRespPO());
        	Assert.assertNotNull(mde.getExcpRespPO().getMessages());
        	List<String> errorMessages = Arrays.asList(
        			"Input parameter is empty. accepted_trades",
        			"Input parameter is empty. sort_by",
        			"Input parameter is empty. sort_order",
        			"Input parameter is empty. export_type",
        			"Input parameter is empty. date_type",
        			"Input parameter is empty. start_date",
        			"Input parameter is empty. end_date");
        	List<String> errorCodes = Arrays.asList("EXPT_1000","EXPT_1000","EXPT_1000","EXPT_1000","EXPT_1000","EXPT_1000","EXPT_1000");
        	Assert.assertEquals(errorMessages.size(),mde.getExcpRespPO().getMessages().size());
        	int i = 0;
        	for (ExceptionMessagesPO message : mde.getExcpRespPO().getMessages()) {
        		Assert.assertEquals(errorMessages.get(i), message.getErrorMessage());
        		Assert.assertEquals(errorCodes.get(i), message.getMessageCode());
        		i++;
        	}
        }
        Assert.assertNull(lstMbsTransHistRet);
    }

}
