/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.service;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fanniemae.mbsportal.api.persister.ExceptionLookupPersister;
import com.fanniemae.mbsportal.api.transformation.ExceptionLookupPOTransformer;
import com.fanniemae.mbsportal.api.transformation.ExceptionLookupTransformer;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.api.validator.ExceptionLookupPOValidator;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.dao.MBSExceptionLookupDao;
import com.fanniemae.mbsportal.model.MBSExceptionLookup;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.validator.Validator;

/**
 * This class handles the test case for the ProductService class
 *
 * @author g8upjv
 *
 */

public class ExceptionLookupServiceTest extends BaseServiceTest {
        @Mock
        MBSExceptionLookupDao mbsExceptionLookupDao;
        
        @Mock
        MBSObjectCreator mbsObjectCreator;
        
        @InjectMocks
        ExceptionLookupService exceptionLookupService;
        
        List<ExceptionLookupPO> exceptionLookupPOLst;

        List<MBSExceptionLookup> mbsExceptionLookupLst;
        
        @Before
        public void setUp() throws Exception {
                exceptionLookupPOLst = createData();
                mbsExceptionLookupLst = createExceptionLookupList();
                when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
        }
        
        /**
         * Init mocks here not use Mock annotation
         * @throws MBSBaseException
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        public void initMocks() throws MBSBaseException {
                ExceptionLookupTransformer<TransformationObject> exceptionLookupTransformer = Mockito.mock(ExceptionLookupTransformer.class);
                ExceptionLookupPersister exceptionLookupPersister = Mockito.mock(ExceptionLookupPersister.class);
                ExceptionLookupPOTransformer<TransformationObject> exceptionLookupPOTransformer = Mockito.mock(ExceptionLookupPOTransformer.class);
                ExceptionLookupPOValidator<TransformationObject> exceptionLookupPOValidator = Mockito.mock(ExceptionLookupPOValidator.class);
                exceptionLookupService = new ExceptionLookupService(exceptionLookupTransformer, exceptionLookupPersister, exceptionLookupPOTransformer, exceptionLookupPOValidator);
                doNothing().when(exceptionLookupPersister).clearAll();
                doNothing().when(exceptionLookupPersister).clear(any());
                when(mbsExceptionLookupDao.getAll()).thenReturn(mbsExceptionLookupLst);
                when(((Persister) exceptionLookupPersister).getBaseDao()).thenReturn(mbsExceptionLookupDao);
                
                MockitoAnnotations.initMocks(this);
                
        }
        
        /**
         * 
         *
         * @throws MBSBaseException
         * @throws Exception
         */
        @Test(expected = MBSBaseException.class)
        public void testCreateExceptionDataThrowException() throws MBSBaseException {
                initMocks();
                when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
                List<ExceptionLookupPO> exceptionLookupPOLstResp = exceptionLookupService.createExceptionLookupData(null);
//                assertNotNull(exceptionLookupPOLstResp);
//                assertNotNull(exceptionLookupPOLstResp.size()>0);
        }
        
        
        
        /**
         * 
         *
         * @throws MBSBaseException
         * @throws Exception
         */
        @Test
        public void testCreateExceptionData() throws MBSBaseException {
                initMocks();
                ExceptionLookupTransformer<TransformationObject> exceptionLookupTransformer = Mockito.spy(ExceptionLookupTransformer.class);
                ExceptionLookupPersister exceptionLookupPersister = Mockito.mock(ExceptionLookupPersister.class);
                ExceptionLookupPOTransformer<TransformationObject> exceptionLookupPOTransformer = Mockito.spy(ExceptionLookupPOTransformer.class);
                ExceptionLookupPOValidator<TransformationObject> exceptionLookupPOValidator = Mockito.spy(ExceptionLookupPOValidator.class);
                exceptionLookupService = new ExceptionLookupService(exceptionLookupTransformer, exceptionLookupPersister, exceptionLookupPOTransformer, exceptionLookupPOValidator);
                MockitoAnnotations.initMocks(this);

                when(mbsExceptionLookupDao.getById(any())).thenReturn(mbsExceptionLookupLst.get(0));
                when(((Persister) exceptionLookupPersister).getBaseDao()).thenReturn(mbsExceptionLookupDao);
                TransformationObject transformationObject = new TransformationObject();
                transformationObject.setSourcePojo(createData());
                doReturn(transformationObject).when((Transformer) exceptionLookupTransformer).transform(any());
                doReturn(transformationObject).when((Transformer) exceptionLookupPOTransformer).transform(any());
                when(mbsObjectCreator.getTransformationObject()).thenReturn(transformationObject);
                doNothing().when((Validator) exceptionLookupPOValidator).validate(any());
                List<ExceptionLookupPO> exceptionLookupPOLstResp = exceptionLookupService.createExceptionLookupData(exceptionLookupPOLst);
                assertNotNull(exceptionLookupPOLstResp);
                assertNotNull(exceptionLookupPOLstResp.size() == 1);
        }
        
        @Test
        public void clearAllExceptionData() throws Exception {
                initMocks();
                Optional<String> strErrorCode = Optional.of("TRANS_00001");
                exceptionLookupService.clearData(strErrorCode);
        }
        
        @Test
        public void clearAllExceptionDataById() throws Exception {
                initMocks();
                Optional<String> strErrorCode = Optional.empty();
                exceptionLookupService.clearData(strErrorCode);
        }
        
        /**
         * Purpose: Test for case for successful call to get list of products
         *
         * @throws MBSBaseException
         * @throws Exception
         */
        @Test
        public void testGetExceptionLookupDataAllData() throws MBSBaseException {
                ExceptionLookupTransformer<TransformationObject> exceptionLookupTransformer = Mockito.spy(ExceptionLookupTransformer.class);
                ExceptionLookupPersister exceptionLookupPersister = Mockito.mock(ExceptionLookupPersister.class);
                ExceptionLookupPOTransformer<TransformationObject> exceptionLookupPOTransformer = Mockito.spy(ExceptionLookupPOTransformer.class);
                ExceptionLookupPOValidator<TransformationObject> exceptionLookupPOValidator = Mockito.spy(ExceptionLookupPOValidator.class);
                exceptionLookupService = new ExceptionLookupService(exceptionLookupTransformer, exceptionLookupPersister, exceptionLookupPOTransformer, exceptionLookupPOValidator);
                MockitoAnnotations.initMocks(this);
                Optional<String> strErrorCode = Optional.empty();
                doNothing().when(exceptionLookupPersister).clearAll();
                doNothing().when(exceptionLookupPersister).clear(any());
                when(mbsExceptionLookupDao.getAll()).thenReturn(mbsExceptionLookupLst);
                when(mbsExceptionLookupDao.getById(any())).thenReturn(mbsExceptionLookupLst.get(0));
                when(((Persister) exceptionLookupPersister).getBaseDao()).thenReturn(mbsExceptionLookupDao);
                TransformationObject transformationObject = new TransformationObject();
                transformationObject.setSourcePojo(createData());
                doReturn(transformationObject).when((Transformer) exceptionLookupTransformer).transform(any());
                doReturn(transformationObject).when((Transformer) exceptionLookupPOTransformer).transform(any());
                doNothing().when((Validator) exceptionLookupPOValidator).validate(any());
                when(mbsObjectCreator.getTransformationObject()).thenReturn(transformationObject);
                List<ExceptionLookupPO> exceptionLookupPOLstResp = exceptionLookupService.getExceptionLookupData(strErrorCode);
                assertNotNull(exceptionLookupPOLstResp);
                assertThat(exceptionLookupPOLstResp, hasSize(1));
        }
        
        /**
         * Purpose: Test for case for successful call to get list of products
         *
         * @throws MBSBaseException
         * @throws Exception
         */
        @Test
        public void testGetExceptionLookupDataMapAllData() throws MBSBaseException {
                ExceptionLookupTransformer<TransformationObject> exceptionLookupTransformer = Mockito.spy(ExceptionLookupTransformer.class);
                ExceptionLookupPersister exceptionLookupPersister = Mockito.mock(ExceptionLookupPersister.class);
                ExceptionLookupPOTransformer<TransformationObject> exceptionLookupPOTransformer = Mockito.spy(ExceptionLookupPOTransformer.class);
                ExceptionLookupPOValidator<TransformationObject> exceptionLookupPOValidator = Mockito.spy(ExceptionLookupPOValidator.class);
                exceptionLookupService = new ExceptionLookupService(exceptionLookupTransformer, exceptionLookupPersister, exceptionLookupPOTransformer, exceptionLookupPOValidator);
                MockitoAnnotations.initMocks(this);
                Optional<String> strErrorCode = Optional.empty();
                doNothing().when(exceptionLookupPersister).clearAll();
                doNothing().when(exceptionLookupPersister).clear(any());
                when(mbsExceptionLookupDao.getAll()).thenReturn(mbsExceptionLookupLst);
                when(mbsExceptionLookupDao.getById(any())).thenReturn(mbsExceptionLookupLst.get(0));
                when(((Persister) exceptionLookupPersister).getBaseDao()).thenReturn(mbsExceptionLookupDao);
                TransformationObject transformationObject = new TransformationObject();
                transformationObject.setSourcePojo(createData());
                doReturn(transformationObject).when((Transformer) exceptionLookupTransformer).transform(any());
                doReturn(transformationObject).when((Transformer) exceptionLookupPOTransformer).transform(any());
                doNothing().when((Validator) exceptionLookupPOValidator).validate(any());
                when(mbsObjectCreator.getTransformationObject()).thenReturn(transformationObject);
                Map<String, ExceptionLookupPO> exceptionLookupPOLstResp = exceptionLookupService.getExceptionLookupDataMap(strErrorCode);
                assertTrue(exceptionLookupPOLstResp.size()>0);
        }
        
        /**
         * Purpose: Test for case for successful call to get list of products
         *
         * @throws MBSBaseException
         * @throws Exception
         */
        @Test
        public void testGetExceptionLookupData() throws MBSBaseException {
            ExceptionLookupTransformer<TransformationObject> exceptionLookupTransformer = Mockito.spy(ExceptionLookupTransformer.class);
            ExceptionLookupPersister exceptionLookupPersister = Mockito.mock(ExceptionLookupPersister.class);
            ExceptionLookupPOTransformer<TransformationObject> exceptionLookupPOTransformer = Mockito.spy(ExceptionLookupPOTransformer.class);
            ExceptionLookupPOValidator<TransformationObject> exceptionLookupPOValidator = Mockito.spy(ExceptionLookupPOValidator.class);
            exceptionLookupService = new ExceptionLookupService(exceptionLookupTransformer, exceptionLookupPersister, exceptionLookupPOTransformer, exceptionLookupPOValidator);
            MockitoAnnotations.initMocks(this);
            Optional<String> strErrorCode = Optional.of("TRANS_00001");
            doNothing().when(exceptionLookupPersister).clearAll();
            doNothing().when(exceptionLookupPersister).clear(any());
            when(mbsExceptionLookupDao.getAll()).thenReturn(mbsExceptionLookupLst);
            when(mbsExceptionLookupDao.getById(any())).thenReturn(mbsExceptionLookupLst.get(0));
            when(((Persister) exceptionLookupPersister).getBaseDao()).thenReturn(mbsExceptionLookupDao);
            TransformationObject transformationObject = new TransformationObject();
            transformationObject.setSourcePojo(createData());
            doReturn(transformationObject).when((Transformer) exceptionLookupTransformer).transform(any());
            doReturn(transformationObject).when((Transformer) exceptionLookupPOTransformer).transform(any());
            doNothing().when((Validator) exceptionLookupPOValidator).validate(any());
            when(mbsObjectCreator.getTransformationObject()).thenReturn(transformationObject);
            List<ExceptionLookupPO> exceptionLookupPOLstResp = exceptionLookupService.getExceptionLookupData(strErrorCode);
            assertNotNull(exceptionLookupPOLstResp);
            assertThat(exceptionLookupPOLstResp, hasSize(1));
        }
        
        /**
         * Utility for 
         * @return
         */
        private List<MBSExceptionLookup> createExceptionLookupList() {
                List<MBSExceptionLookup> lstExceptionLookup = new ArrayList<MBSExceptionLookup>();
                MBSExceptionLookup mbsExceptionLookup = new MBSExceptionLookup();
                mbsExceptionLookup.setErrorCategory("API");
                mbsExceptionLookup.setErrorCode("TRANS_00001");
                mbsExceptionLookup.setMessageType("DISP_ERROR");
                mbsExceptionLookup.setErrorMessage("Test message");
                mbsExceptionLookup.setLoggerMessage("Test Message");
                lstExceptionLookup.add(mbsExceptionLookup);
                return lstExceptionLookup;
        }
        
        public List<ExceptionLookupPO> createData() {
            exceptionLookupPOLst = new ArrayList<>();
            ExceptionLookupPO exceptionLookupPO = new ExceptionLookupPO();
            exceptionLookupPO.setErrorCategory("API");
            exceptionLookupPO.setErrorCode("TRANS_00001");
            exceptionLookupPO.setMessageType("DISP_ERROR");
            exceptionLookupPO.setErrorMessage("Test Message");
            exceptionLookupPO.setLoggerMessage("Test message");
            exceptionLookupPOLst.add(exceptionLookupPO);
            return exceptionLookupPOLst;
        }
        
}
