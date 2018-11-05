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

package com.fanniemae.mbsportal.api.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.config.TradeServiceProperties;
import com.fanniemae.mbsportal.api.enrichment.TransactionHistoryEnrichment;
import com.fanniemae.mbsportal.api.persister.TransactionPersister;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementRolePO;
import com.fanniemae.mbsportal.api.po.TradePO;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.api.transformation.TransactionRequestPOTransformer;
import com.fanniemae.mbsportal.api.transformation.TransactionRequestTransformer;
import com.fanniemae.mbsportal.api.utils.ExportUtils;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.api.validator.TransactionRequestPOValidator;
import com.fanniemae.mbsportal.api.validator.TransactionRequestValidator;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.dao.MBSProfileDao;
import com.fanniemae.mbsportal.dao.MBSQueryFunctionDao;
import com.fanniemae.mbsportal.dao.MBSTransactionRequestDao;
import com.fanniemae.mbsportal.enrichment.Enricher;
import com.fanniemae.mbsportal.gf.pojo.SortBy;
import com.fanniemae.mbsportal.model.MBSTrade;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.publisher.MessagePublisher;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;
import com.fanniemae.securitiesods.ods_core.domain.TradeParty;

/**
 * This class handles the test case for the TransactionExportService class
 *
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ExportUtils.class})
public class TransactionExportServiceTest extends BaseServiceTest {

	@InjectMocks
	private TransactionExportService transactionExportService;

    @InjectMocks
    TransactionHistoryService transactionHistoryService;
    @Mock
    TransactionHistoryEnrichment<TransformationObject> transactionHistoryEnrichment;
	@Mock
    private TransactionRequestService transactionRequestService;
    @Mock
    ProfileEntitlementService profileEntitlementService;
	@Mock
	TransactionRequestTransformer<TransformationObject> transactionRequestTransformer;
	@Mock
	TransactionRequestPOValidator<TransformationObject> transactionRequestPOValidator;
	@Mock
	TransactionRequestValidator<TransformationObject> transactionRequestValidator;
	@Mock
	TransactionPersister transactionPersister;
	@Mock
	Enricher<TransformationObject> transactionRequestEnrichment;
	@Mock
	MessagePublisher<TransformationObject> transactionRequestMessagePublisher;
	@Mock (name="poTransformer")
	TransactionRequestPOTransformer<TransformationObject> transactionRequestPOTransformer;
    @Mock
    MBSObjectCreator mbsObjectCreator;
	@Mock
	MBSQueryFunctionDao mbsQueryFunctionDao;
	@Mock
	MBSTransactionRequestDao mbsTransactionDao;
	@Mock
	TradeServiceProperties tradeServiceProperties;
	@Mock
    private MBSProfileDao mbsProfileDao;
    @Mock
    private TradeService tradeService;
    @Mock
    private MbspProperties mbsProperties;
    
    private List<TransactionRequestPO> lstTransPO;
    private TransactionRequestPO transactionRequestPO;
    private MBSTrade mbsTrade;
    private TransformationObject transformationObject;
    private TradePO mbsTradePO;
    private List<TradePO> mbsTradePOLst;
    private List<MBSTrade> mbsMBSTradeLst;
    private List<ProfileEntitlementPO> profileEntitlementPOLst;
    private ProfileEntitlementPO profileEntitlementPO;
    private ProfileEntitlementRolePO profileEntitlementRolePO;

	List<MBSTransactionRequest> lstMbsTrans;
	TransformationObject transObj;

	@Before
	public void setUp() throws Exception {
		transactionExportService = new TransactionExportService(transactionRequestTransformer,
				transactionRequestPOValidator, transactionRequestValidator, transactionPersister,
				transactionRequestPOTransformer, transactionRequestEnrichment, transactionRequestMessagePublisher);
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Method to create data for the test cases
	 */
	public void createUpdateData() {
		lstMbsTrans = new ArrayList<MBSTransactionRequest>();
		MBSTransactionRequest mbsTrans = new MBSTransactionRequest();
		mbsTrans.setTradeAmount(new BigDecimal(10101010));
		mbsTrans.setTradeBuySellType("SELL");
		mbsTrans.setTradeCouponRate(new BigDecimal(10.1));
		mbsTrans.setTradeSettlementDate(new Date());
		mbsTrans.setSubmissionDate(new Date());
		mbsTrans.setCounterpartyTraderIdentifier("testLender");
		mbsTrans.setSourceSystem("MBSP");
		lstMbsTrans.add(mbsTrans);
		transObj = new TransformationObject();

	}

	@Test
	public void testGetMBSTransReqHistoryCreateSortLENDER() throws MBSBaseException {
		MockitoAnnotations.initMocks(this);
		createUpdateData();
		String[] mockShakeOutUsers = new String[] {"MBSP_BRS"};
		List<String> userNames = new ArrayList<String>();
		userNames.add("MBSP");
		when(tradeServiceProperties.getShakeOutTestUsers()).thenReturn(mockShakeOutUsers);
		when(mbsProfileDao.getUserNamesForBRSNames(mockShakeOutUsers)).thenReturn(userNames);
        when(mbsObjectCreator.getTransformationObject()).thenReturn(transObj);
 
        transObj.setSourcePojo(transObj.getTargetPojo());
        TransformationObject mockTransformationObject = Mockito.mock(TransformationObject.class);
        TransactionRequestPO mockTransactionRequestPO = Mockito.mock(TransactionRequestPO.class);
        ProductPO mockProductPO = Mockito.mock(ProductPO.class);
        when(transactionRequestPOTransformer.transform(Mockito.any(TransformationObject.class))).thenReturn(mockTransformationObject);
        when(mockTransformationObject.getSourcePojo()).thenReturn(mockTransactionRequestPO);
        when(mockTransactionRequestPO.getProduct()).thenReturn(mockProductPO);
        when(mockProductPO.retrieveKey()).thenReturn("keyValue");
        when(transactionRequestService.getProduct(Mockito.anyString())).thenReturn(mockProductPO);
        doNothing().when(mockTransactionRequestPO).setProduct(Mockito.any(ProductPO.class));

		doNothing().when(transactionPersister).clearAll();
		doNothing().when((Persister) transactionPersister).persist(any());
		doNothing().when(transactionRequestPOValidator).validate(any());
		doNothing().when(transactionRequestValidator).validate(any());
		doNothing().when(transactionRequestMessagePublisher).publish(any());
		when(((Persister) transactionPersister).getBaseDao()).thenReturn(mbsTransactionDao);

		List<StateType> lstStateType = new ArrayList<StateType>();
		lstStateType.add(StateType.LENDER_OPEN);
		String sellerServiceNumber = "121212";
		String sortBy = "tradeBuySellType";
		String sortOrder = SortBy.asc.getName();
		String dateStart = "2018-01-01";
		String dateEnd = "2018-05-09";
		String dateType = "submissionDate";

		when(mbsQueryFunctionDao.getTransactionHistoryDetails(any(), any())).thenReturn(lstMbsTrans);
		List<TransactionRequestPO> lstTransPOResult = transactionExportService.getMBSTransReqHistory(MBSRoleType.LENDER,
				lstStateType, sellerServiceNumber, sortBy, sortOrder, dateStart, dateEnd, dateType);
		assertNotNull(lstTransPOResult);

	}

	@Test
	public void testGetMBSTransReqHistoryCreateSortTRADER() throws MBSBaseException {
		MockitoAnnotations.initMocks(this);
		createUpdateData();
		String[] mockShakeOutUsers = new String[] {"MBSP_BRS"};
		List<String> userNames = new ArrayList<String>();
		userNames.add("MBSP");
		when(tradeServiceProperties.getShakeOutTestUsers()).thenReturn(mockShakeOutUsers);
		when(mbsProfileDao.getUserNamesForBRSNames(mockShakeOutUsers)).thenReturn(userNames);
        when(mbsObjectCreator.getTransformationObject()).thenReturn(transObj);
 
        transObj.setSourcePojo(transObj.getTargetPojo());
        TransformationObject mockTransformationObject = Mockito.mock(TransformationObject.class);
        TransactionRequestPO mockTransactionRequestPO = Mockito.mock(TransactionRequestPO.class);
        ProductPO mockProductPO = Mockito.mock(ProductPO.class);
        when(transactionRequestPOTransformer.transform(Mockito.any(TransformationObject.class))).thenReturn(mockTransformationObject);
        when(mockTransformationObject.getSourcePojo()).thenReturn(mockTransactionRequestPO);
        when(mockTransactionRequestPO.getProduct()).thenReturn(mockProductPO);
        when(mockProductPO.retrieveKey()).thenReturn("keyValue");
        when(transactionRequestService.getProduct(Mockito.anyString())).thenReturn(mockProductPO);
        doNothing().when(mockTransactionRequestPO).setProduct(Mockito.any(ProductPO.class));

		doNothing().when(transactionPersister).clearAll();
		doNothing().when((Persister) transactionPersister).persist(any());
		doNothing().when(transactionRequestPOValidator).validate(any());
		doNothing().when(transactionRequestValidator).validate(any());
		doNothing().when(transactionRequestMessagePublisher).publish(any());
		when(((Persister) transactionPersister).getBaseDao()).thenReturn(mbsTransactionDao);

		List<StateType> lstStateType = new ArrayList<StateType>();
		lstStateType.add(StateType.LENDER_OPEN);
		String sellerServiceNumber = "121212";
		String sortBy = "tradeBuySellType";
		String sortOrder = SortBy.asc.getName();
		String dateStart = "2018-01-01";
		String dateEnd = "2018-05-09";
		String dateType = "submissionDate";

		when(mbsQueryFunctionDao.getTransactionHistoryDetails(any(), any())).thenReturn(lstMbsTrans);
		List<TransactionRequestPO> lstTransPOResult = transactionExportService.getMBSTransReqHistory(MBSRoleType.TRADER,
				lstStateType, sellerServiceNumber, sortBy, sortOrder, dateStart, dateEnd, dateType);
		assertNotNull(lstTransPOResult);

	}

	@Test
	public void testGetMBSTransReqHistoryCreateSortTSP() throws MBSBaseException {
		MockitoAnnotations.initMocks(this);
		createUpdateData();
		String[] mockShakeOutUsers = new String[] {"MBSP_BRS"};
		List<String> userNames = new ArrayList<String>();
		userNames.add("MBSP");
		when(tradeServiceProperties.getShakeOutTestUsers()).thenReturn(mockShakeOutUsers);
		when(mbsProfileDao.getUserNamesForBRSNames(mockShakeOutUsers)).thenReturn(userNames);
        when(mbsObjectCreator.getTransformationObject()).thenReturn(transObj);
 
        transObj.setSourcePojo(transObj.getTargetPojo());
        TransformationObject mockTransformationObject = Mockito.mock(TransformationObject.class);
        TransactionRequestPO mockTransactionRequestPO = Mockito.mock(TransactionRequestPO.class);
        ProductPO mockProductPO = Mockito.mock(ProductPO.class);
        when(transactionRequestPOTransformer.transform(Mockito.any(TransformationObject.class))).thenReturn(mockTransformationObject);
        when(mockTransformationObject.getSourcePojo()).thenReturn(mockTransactionRequestPO);
        when(mockTransactionRequestPO.getProduct()).thenReturn(mockProductPO);
        when(mockProductPO.retrieveKey()).thenReturn("keyValue");
        when(transactionRequestService.getProduct(Mockito.anyString())).thenReturn(mockProductPO);
        doNothing().when(mockTransactionRequestPO).setProduct(Mockito.any(ProductPO.class));

		doNothing().when(transactionPersister).clearAll();
		doNothing().when((Persister) transactionPersister).persist(any());
		doNothing().when(transactionRequestPOValidator).validate(any());
		doNothing().when(transactionRequestValidator).validate(any());
		doNothing().when(transactionRequestMessagePublisher).publish(any());
		when(((Persister) transactionPersister).getBaseDao()).thenReturn(mbsTransactionDao);

		List<StateType> lstStateType = new ArrayList<StateType>();
		lstStateType.add(StateType.LENDER_OPEN);
		String sellerServiceNumber = "121212";
		String sortBy = "tradeBuySellType";
		String sortOrder = SortBy.asc.getName();
		String dateStart = "2018-01-01";
		String dateEnd = "2018-05-09";
		String dateType = "submissionDate";

		when(mbsQueryFunctionDao.getTransactionHistoryDetails(any(), any())).thenReturn(lstMbsTrans);
		List<TransactionRequestPO> lstTransPOResult = transactionExportService.getMBSTransReqHistory(MBSRoleType.TSP,
				lstStateType, sellerServiceNumber, sortBy, sortOrder, dateStart, dateEnd, dateType);
		assertNotNull(lstTransPOResult);

	}

	@Test
	public void testGetMBSTransReqHistoryCreateSortBy() throws MBSBaseException {
		MockitoAnnotations.initMocks(this);
		createUpdateData();
		String[] mockShakeOutUsers = new String[] {"MBSP_BRS"};
		List<String> userNames = new ArrayList<String>();
		userNames.add("MBSP");
		when(tradeServiceProperties.getShakeOutTestUsers()).thenReturn(mockShakeOutUsers);
		when(mbsProfileDao.getUserNamesForBRSNames(mockShakeOutUsers)).thenReturn(userNames);
        when(mbsObjectCreator.getTransformationObject()).thenReturn(transObj);
 
        transObj.setSourcePojo(transObj.getTargetPojo());
        TransformationObject mockTransformationObject = Mockito.mock(TransformationObject.class);
        TransactionRequestPO mockTransactionRequestPO = Mockito.mock(TransactionRequestPO.class);
        ProductPO mockProductPO = Mockito.mock(ProductPO.class);
        when(transactionRequestPOTransformer.transform(Mockito.any(TransformationObject.class))).thenReturn(mockTransformationObject);
        when(mockTransformationObject.getSourcePojo()).thenReturn(mockTransactionRequestPO);
        when(mockTransactionRequestPO.getProduct()).thenReturn(mockProductPO);
        when(mockProductPO.retrieveKey()).thenReturn("keyValue");
        when(transactionRequestService.getProduct(Mockito.anyString())).thenReturn(mockProductPO);
        doNothing().when(mockTransactionRequestPO).setProduct(Mockito.any(ProductPO.class));

		doNothing().when(transactionPersister).clearAll();
		doNothing().when((Persister) transactionPersister).persist(any());
		doNothing().when(transactionRequestPOValidator).validate(any());
		doNothing().when(transactionRequestValidator).validate(any());
		doNothing().when(transactionRequestMessagePublisher).publish(any());
		when(((Persister) transactionPersister).getBaseDao()).thenReturn(mbsTransactionDao);

		List<StateType> lstStateType = new ArrayList<StateType>();
		lstStateType.add(StateType.LENDER_OPEN);
		String sellerServiceNumber = "121212";
		String sortBy = "tradeBuySellType1";
		String sortOrder = SortBy.asc.getName();
		String dateStart = "2018-01-01";
		String dateEnd = "2018-05-09";
		String dateType = "submissionDate";

		when(mbsQueryFunctionDao.getTransactionHistoryDetails(any(), any())).thenReturn(lstMbsTrans);
		List<TransactionRequestPO> lstTransPOResult = transactionExportService.getMBSTransReqHistory(MBSRoleType.LENDER,
				lstStateType, sellerServiceNumber, sortBy, sortOrder, dateStart, dateEnd, dateType);
		assertNotNull(lstTransPOResult);

	}
	
	@Test
	public void testGetMBSTransReqHistoryCreateSortNullQueryResults() throws MBSBaseException {
		MockitoAnnotations.initMocks(this);
		createUpdateData();
		String[] mockShakeOutUsers = new String[] {"MBSP_BRS"};
		List<String> userNames = new ArrayList<String>();
		userNames.add("MBSP");
		when(tradeServiceProperties.getShakeOutTestUsers()).thenReturn(mockShakeOutUsers);
		when(mbsProfileDao.getUserNamesForBRSNames(mockShakeOutUsers)).thenReturn(userNames);
        when(mbsObjectCreator.getTransformationObject()).thenReturn(transObj);
 
        transObj.setSourcePojo(transObj.getTargetPojo());
        TransformationObject mockTransformationObject = Mockito.mock(TransformationObject.class);
        TransactionRequestPO mockTransactionRequestPO = Mockito.mock(TransactionRequestPO.class);
        ProductPO mockProductPO = Mockito.mock(ProductPO.class);
        when(transactionRequestPOTransformer.transform(Mockito.any(TransformationObject.class))).thenReturn(mockTransformationObject);
        when(mockTransformationObject.getSourcePojo()).thenReturn(mockTransactionRequestPO);
        when(mockTransactionRequestPO.getProduct()).thenReturn(mockProductPO);
        when(mockProductPO.retrieveKey()).thenReturn("keyValue");
        when(transactionRequestService.getProduct(Mockito.anyString())).thenReturn(mockProductPO);
        doNothing().when(mockTransactionRequestPO).setProduct(Mockito.any(ProductPO.class));

		doNothing().when(transactionPersister).clearAll();
		doNothing().when((Persister) transactionPersister).persist(any());
		doNothing().when(transactionRequestPOValidator).validate(any());
		doNothing().when(transactionRequestValidator).validate(any());
		doNothing().when(transactionRequestMessagePublisher).publish(any());
		when(((Persister) transactionPersister).getBaseDao()).thenReturn(mbsTransactionDao);

		List<StateType> lstStateType = new ArrayList<StateType>();
		lstStateType.add(StateType.LENDER_OPEN);
		String sellerServiceNumber = "121212";
		String sortBy = "tradeBuySellType";
		String sortOrder = SortBy.asc.getName();
		String dateStart = "2018-01-01";
		String dateEnd = "2018-05-09";
		String dateType = "submissionDate";

		when(mbsQueryFunctionDao.getTransactionHistoryDetails(any(), any())).thenReturn(null);
		List<TransactionRequestPO> lstTransPOResult = transactionExportService.getMBSTransReqHistory(MBSRoleType.LENDER,
				lstStateType, sellerServiceNumber, sortBy, sortOrder, dateStart, dateEnd, dateType);
		assertNotNull(lstTransPOResult);

	}


	@Test(expected=MBSSystemException.class)
	public void testGetMBSTransReqHistoryCreateSortStateTypeNull() throws MBSBaseException {
		MockitoAnnotations.initMocks(this);
		String sellerServiceNumber = "121212";
		String sortBy = "tradeBuySellType";
		String sortOrder = SortBy.asc.getName();
		String dateStart = "2018-01-01";
		String dateEnd = "2018-05-09";
		String dateType = "submissionDate";
		List<TransactionRequestPO> lstTransPOResult = transactionExportService.getMBSTransReqHistory(MBSRoleType.LENDER,
				null, sellerServiceNumber, sortBy, sortOrder, dateStart, dateEnd, dateType);
		assertNotNull(lstTransPOResult);

	}
	
    public void createData() {
        profileEntitlementPOLst = new ArrayList<ProfileEntitlementPO>();
        transactionRequestPO = new TransactionRequestPO();
        transactionRequestPO.setLenderId("testLender");
        transactionRequestPO.setStateType(StateType.EXECUTED);
        transactionRequestPO.setTransReqId("17J00004");
        transactionRequestPO.setTraderId("testTrader");
        transactionRequestPO.setLenderId("testLender");
        lstTransPO = new ArrayList<TransactionRequestPO>();
        lstTransPO.add(transactionRequestPO);
        transformationObject = new TransformationObject();
        mbsTrade = new MBSTrade();
        TradeParty tradeParty = new TradeParty();
        tradeParty.setPartyShortName("TEST-C");
        mbsTrade.setTransReqNumber("17J00004");
        mbsTradePO = new TradePO();
        mbsTradePO.setTransactionRequestId(mbsTrade.getTransReqNumber());
        mbsTradePOLst = new ArrayList<>();
        mbsMBSTradeLst = new ArrayList<>();
        mbsTradePOLst.add(mbsTradePO);
        mbsMBSTradeLst.add(mbsTrade);
        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementRolePO = new ProfileEntitlementRolePO();
        profileEntitlementRolePO.setName("MBS Trade - Read Only");
        profileEntitlementPO = new ProfileEntitlementPO();
        profileEntitlementPO.setUserName("testLenderTrader");
        profileEntitlementPO.setFirstName("fn");
        profileEntitlementPO.setLastName("ln");
        profileEntitlementPO.setEmailAddress("test@gmail.com");
        profileEntitlementPO.setPartyShortName("TEST");
        profileEntitlementPO.addRole(profileEntitlementRolePO);
        profileEntitlementPOLst.add(profileEntitlementPO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.TRADE_REQUEST, mbsTradePOLst.get(0));
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.TRANSACTION_REQUEST, transactionRequestPO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.LENDER_PROFILE, profileEntitlementPO);
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.TRADER_PROFILE, profileEntitlementPO);
    }


    @Ignore
    @SuppressWarnings("unchecked")
	@Test
    public void testGetTransactionRequestExportSortedCSV() throws MBSBaseException {
        createData();
        PowerMockito.mockStatic(ExportUtils.class);
        when(ExportUtils.getCSVFromHistory(any(List.class), any(Date.class), any(Date.class), anyString(), any(MBSRoleType.class), anyString())).thenReturn(new byte[0]);
    	Map<String, String> map = new HashMap<>();
    	map.put("acceptedTrades", "true");
    	map.put("sortBy", "submissionDate");
    	map.put("sortOrder", "desc");
    	map.put("dateStart", "2018-04-11");
    	map.put("dateEnd", "2018-05-11");
    	map.put("exportType", "CSV");
    	map.put("dateType", "Request Date");
    	map.put("userName", "User Name");
        when(transactionExportService.getMBSTransReqHistory(Mockito.any(MBSRoleType.class), Mockito.any(List.class), Mockito.anyString(), 
        		Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(lstTransPO);
        when(tradeService.getMBSTrade(Mockito.anyString())).thenReturn(mbsTradePOLst);
        when(profileEntitlementService.getProfile(Mockito.any())).thenReturn(profileEntitlementPO);
        when(profileEntitlementService.getUserNames(Mockito.any())).thenReturn(profileEntitlementPOLst);
        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
        when(tradeServiceProperties.getShakeOutCPartyName()).thenReturn("T");
        Mockito.doNothing().when(transactionHistoryEnrichment).enrich(Mockito.any());
        String sellerServiceNumber = "121212";
        byte[] exportData = transactionHistoryService
                .getTransactionRequestExportSorted(MBSRoleType.LENDER, sellerServiceNumber, map);
        assertNotNull(exportData);
    }
    
    @Test
    public void testGetMBSTransReqHistorySize() throws MBSBaseException {
    	createData();
        String sellerServiceNumber = "121212";
        when(mbsProperties.getTransHistActiveDays()).thenReturn("-90");
		when(((Persister) transactionPersister).getBaseDao()).thenReturn(mbsTransactionDao);
        when(mbsTransactionDao.getTransHistorySize(Mockito.anyList())).thenReturn(1000);
    	Integer size = transactionExportService.getMBSTransReqHistorySize(MBSRoleType.LENDER, Arrays.asList(StateType.EXECUTION_IN_PROGRESS, StateType.LENDER_ACCEPTED, StateType.EXECUTED, StateType.PENDING_EXECUTION), sellerServiceNumber);
    	Assert.assertEquals(size, new Integer(1000));
    }
    
    @Test
    public void testGetMBSTransReqHistorySizeError() throws MBSBaseException {
    	createData();
        String sellerServiceNumber = "121212";
        when(mbsProperties.getTransHistActiveDays()).thenReturn("-90");
		when(((Persister) transactionPersister).getBaseDao()).thenReturn(mbsTransactionDao);
        when(mbsTransactionDao.getTransHistorySize(Mockito.anyList())).thenReturn(1000);
        try {
        	Integer size = transactionExportService.getMBSTransReqHistorySize(MBSRoleType.LENDER, null, sellerServiceNumber);
        } catch (Exception e) {
        	Assert.assertEquals(e.getClass(), MBSSystemException.class);
        	MBSSystemException mse = (MBSSystemException) e;
        	Assert.assertEquals(new Long(mse.getProcessId()), MBSExceptionConstants.SYSTEM_EXCEPTION);
        	Assert.assertEquals(mse.getRootExceptionMessage(), MBSPServiceConstants.NO_STATETYPE_LIST);
        	return;
        }
        Assert.fail("no exception");
    }
}
