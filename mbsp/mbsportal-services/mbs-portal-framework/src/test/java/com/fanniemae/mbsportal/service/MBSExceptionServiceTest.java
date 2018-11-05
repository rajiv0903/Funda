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
package com.fanniemae.mbsportal.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fanniemae.mbsportal.dao.MBSExceptionLookupDao;
import com.fanniemae.mbsportal.model.MBSExceptionLookup;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.exception.DataExceptionMessage;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.validator.Validator;

/**
 * @author FannieMae
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class MBSExceptionServiceTest {

    @InjectMocks
    MBSExceptionService mbsExceptionService;
    
    @Mock
    Persister persister;
    
    @Mock
    MBSExceptionLookupDao mbsBaseDao;
    
    @Mock
    Validator validator;
    
    @Mock
    Transformer poTransformer;
    
    @Mock
    TransformationObject transObj;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MBSExceptionService.class);
    
    List<DataExceptionMessage> dataExceptionMessages = new ArrayList<DataExceptionMessage>();
    
    List<ExceptionLookupPO> exceptionLookupPOLst;

    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void testGetExceptionLookupDataEmptyInput() throws MBSBaseException {
    	when(persister.getBaseDao()).thenReturn(mbsBaseDao);    	
    	Optional<String> empty = Optional.empty();
    	List<ExceptionLookupPO> listExceptionLookupPO = mbsExceptionService.getExceptionLookupData(empty);
    	assertEquals(0, listExceptionLookupPO.size());
    }
    
    @Test
    public void testGetExceptionLookupDataEmptyInputListMBSExceptionLookup() throws MBSBaseException {
    	when(persister.getBaseDao()).thenReturn(mbsBaseDao);
    	List<MBSExceptionLookup> listMBSExceptionLookup = new ArrayList<MBSExceptionLookup>();
    	listMBSExceptionLookup.add(new MBSExceptionLookup());
    	when(mbsBaseDao.getAll()).thenReturn(listMBSExceptionLookup);
    	when(poTransformer.transform(Mockito.any(TransformationObject.class))).thenReturn(transObj);
    	List<ExceptionLookupPO> sourcePojo = new ArrayList<ExceptionLookupPO>();
    	when(transObj.getSourcePojo()).thenReturn(sourcePojo);
    	Optional<String> empty = Optional.empty();
    	List<ExceptionLookupPO> listExceptionLookupPO = mbsExceptionService.getExceptionLookupData(empty);
    	assertNotNull(listExceptionLookupPO);
    	assertEquals(0, listExceptionLookupPO.size());
    }
    
    @Test
    public void testGetExceptionLookupDataEmptyInputListMBSExceptionLookupEmpty() throws MBSBaseException {
    	when(persister.getBaseDao()).thenReturn(mbsBaseDao);
    	List<MBSExceptionLookup> listMBSExceptionLookup = new ArrayList<MBSExceptionLookup>();
    	listMBSExceptionLookup.add(new MBSExceptionLookup());
    	when(mbsBaseDao.getAll()).thenReturn(listMBSExceptionLookup);
    	dataExceptionMessages.add(new DataExceptionMessage(MBSExceptionConstants.PAGE_1000));
    	Optional<String> errorCodeOpt = Optional.of(dataExceptionMessages.get(0).getErrorCode());
    	List<ExceptionLookupPO> listExceptionLookupPO = mbsExceptionService.getExceptionLookupData(errorCodeOpt);
    	assertNotNull(listExceptionLookupPO);
    	assertEquals(0, listExceptionLookupPO.size());
    }
    
    @Test
    public void testGetExceptionLookupDataEmptyInputListMBSExceptionLookupNotEmpty() throws MBSBaseException {
    	when(persister.getBaseDao()).thenReturn(mbsBaseDao);
    	when(mbsBaseDao.getById(Mockito.any())).thenReturn(new MBSExceptionLookup());
    	List<MBSExceptionLookup> listMBSExceptionLookup = new ArrayList<MBSExceptionLookup>();
    	listMBSExceptionLookup.add(new MBSExceptionLookup());
    	when(mbsBaseDao.getAll()).thenReturn(listMBSExceptionLookup);
    	when(poTransformer.transform(Mockito.any(TransformationObject.class))).thenReturn(transObj);
    	List<ExceptionLookupPO> sourcePojo = new ArrayList<ExceptionLookupPO>();
    	when(transObj.getSourcePojo()).thenReturn(sourcePojo);
    	dataExceptionMessages.add(new DataExceptionMessage(MBSExceptionConstants.PAGE_1000));
    	Optional<String> errorCodeOpt = Optional.of(dataExceptionMessages.get(0).getErrorCode());
    	List<ExceptionLookupPO> listExceptionLookupPO = mbsExceptionService.getExceptionLookupData(errorCodeOpt);
    	assertNotNull(listExceptionLookupPO);
    	assertEquals(0, listExceptionLookupPO.size());
    }
        
    @Test
    public void testCreateExceptionByCodeAndLogEmptyDataExceptionMessages() throws MBSBaseException {
    	mbsExceptionService.createExceptionByCodeAndLog(new String("EXPORT_API"), new ArrayList<DataExceptionMessage>(), MBSExceptionConstants.BUSINESS_EXCEPTION, null);
    }
    
    @Test
    public void testCreateExceptionByCodeAndLogNullExceptionLookupObj() throws MBSBaseException {
    	when(persister.getBaseDao()).thenReturn(mbsBaseDao);    	
    	when(mbsBaseDao.getById(any())).thenReturn(null);
    	
    	dataExceptionMessages.add(new DataExceptionMessage(MBSExceptionConstants.PAGE_1000));
    	mbsExceptionService.createExceptionByCodeAndLog(new String("EXPORT_API"), dataExceptionMessages, MBSExceptionConstants.BUSINESS_EXCEPTION, null);
    }

    @Test(expected=MBSBusinessException.class)
    public void testCreateExceptionByCodeAndLogNonNullExceptionLookupPOListBUSINESS_EXCEPTION() throws MBSBaseException {
    	when(persister.getBaseDao()).thenReturn(mbsBaseDao);   
    	MBSExceptionLookup mbsExceptionLookup = new MBSExceptionLookup();
    	ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
    	exceptionLookupPO.setErrorMessage("Business exception");
    	when(mbsBaseDao.getById(any())).thenReturn(mbsExceptionLookup);
    	when(poTransformer.transform(Mockito.any(TransformationObject.class))).thenReturn(transObj);
    	List<ExceptionLookupPO> sourcePojo = new ArrayList<ExceptionLookupPO>();
    	sourcePojo.add(exceptionLookupPO);
    	when(transObj.getSourcePojo()).thenReturn(sourcePojo);	
    	
    	dataExceptionMessages.add(new DataExceptionMessage(MBSExceptionConstants.PAGE_1000));
    	mbsExceptionService.createExceptionByCodeAndLog(new String("EXPORT_API"), dataExceptionMessages, MBSExceptionConstants.BUSINESS_EXCEPTION, null);
    }

    @Test
    public void testCreateExceptionByCodeAndLogNonNullExceptionLookupPOListSYSTEM_EXCEPTION() throws MBSBaseException {
    	when(persister.getBaseDao()).thenReturn(mbsBaseDao);    	
    	MBSExceptionLookup mbsExceptionLookup = new MBSExceptionLookup();
    	ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
    	exceptionLookupPO.setErrorMessage("Business exception");
    	when(mbsBaseDao.getById(any())).thenReturn(mbsExceptionLookup);
    	when(poTransformer.transform(Mockito.any(TransformationObject.class))).thenReturn(transObj);
    	List<ExceptionLookupPO> sourcePojo = new ArrayList<ExceptionLookupPO>();
    	sourcePojo.add(exceptionLookupPO);
    	when(transObj.getSourcePojo()).thenReturn(sourcePojo);
    	
    	dataExceptionMessages.add(new DataExceptionMessage(MBSExceptionConstants.PAGE_1000));
    	mbsExceptionService.createExceptionByCodeAndLog(new String("EXPORT_API"), dataExceptionMessages, MBSExceptionConstants.SYSTEM_EXCEPTION, null);
    }
    
    @Test
    public void createBusinessExceptionAndLog_success() throws MBSBaseException {
        when(persister.getBaseDao()).thenReturn(mbsBaseDao);            
        MBSExceptionLookup mbsExceptionLookup = new MBSExceptionLookup();
        ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        exceptionLookupPO.setErrorMessage("Business exception");
        when(mbsBaseDao.getById(any())).thenReturn(mbsExceptionLookup);
        when(poTransformer.transform(Mockito.any(TransformationObject.class))).thenReturn(transObj);
        List<ExceptionLookupPO> sourcePojo = new ArrayList<ExceptionLookupPO>();
        sourcePojo.add(exceptionLookupPO);
        when(transObj.getSourcePojo()).thenReturn(sourcePojo);
        assertNotNull(mbsExceptionService.createBusinessExceptionAndLog(new String("TEST"),"TRAN_5001" , null));
    }
    
    @Test
    public void createSystemExceptionAndLog_success() throws MBSBaseException {
        when(persister.getBaseDao()).thenReturn(mbsBaseDao);            
        MBSExceptionLookup mbsExceptionLookup = new MBSExceptionLookup();
        ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        exceptionLookupPO.setErrorMessage("Business exception");
        when(mbsBaseDao.getById(any())).thenReturn(mbsExceptionLookup);
        when(poTransformer.transform(Mockito.any(TransformationObject.class))).thenReturn(transObj);
        List<ExceptionLookupPO> sourcePojo = new ArrayList<ExceptionLookupPO>();
        sourcePojo.add(exceptionLookupPO);
        when(transObj.getSourcePojo()).thenReturn(sourcePojo);
        assertNotNull(mbsExceptionService.createSystemExceptionAndLog(new String("TEST"),"INTSRV_00002" , null));
    }
    
    @Test
    public void logMessage_warn_success() throws MBSBaseException {
        ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        exceptionLookupPO.setLoggerMessage("Test message");
        exceptionLookupPO.setLogLevel("warn");
        mbsExceptionService.logMessage(new String("TEST"), exceptionLookupPO, null);
    }
    
    @Test
    public void logMessage_debug_success() throws MBSBaseException {
        ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        exceptionLookupPO.setLoggerMessage("Test message");
        exceptionLookupPO.setLogLevel("debug");
        mbsExceptionService.logMessage(new String("TEST"), exceptionLookupPO, null);
    }
    
    @Test
    public void logMessage_error_success() throws MBSBaseException {
        ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        exceptionLookupPO.setLoggerMessage("Test message");
        exceptionLookupPO.setLogLevel("error");
        mbsExceptionService.logMessage(new String("TEST"), exceptionLookupPO, null);
    }
    
    @Test
    public void logMessage_info_success() throws MBSBaseException {
        ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
        exceptionLookupPO.setLoggerMessage("Test message {} and {}");
        exceptionLookupPO.setLogLevel("info");
        mbsExceptionService.logMessage(new String("TEST"), exceptionLookupPO, "price", "tick");
    }
    
}
