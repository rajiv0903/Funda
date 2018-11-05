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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fanniemae.mbsportal.api.persister.ProfileSessionPersister;
import com.fanniemae.mbsportal.api.po.ProfileSessionPO;
import com.fanniemae.mbsportal.api.transformation.ProfileSessionPOTransformer;
import com.fanniemae.mbsportal.api.transformation.ProfileSessionTransformer;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.api.validator.ProfileSessionValidator;
import com.fanniemae.mbsportal.config.test.BaseServiceTest;
import com.fanniemae.mbsportal.dao.MBSProfileSessionDao;
import com.fanniemae.mbsportal.model.MBSProfileSession;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Mar 15, 2018
 * @File: com.fanniemae.mbsportal.api.service.ProfileSessionServiceTest.java
 * @Revision :
 * @Description: ProfileSessionServiceTest.java
 */
public class ProfileSessionServiceTest extends BaseServiceTest {

    @Mock
    MBSObjectCreator mbsObjectCreator;
    @Mock
    MBSProfileSessionDao mBSProfileSessionDao;
    @Mock
    ProfileSessionTransformer<TransformationObject> profileSessionTransformer;
    @Mock
    ProfileSessionPOTransformer<TransformationObject> profileSessionPOTransformer;
    @Mock
    ProfileSessionValidator<TransformationObject> profileSessionValidator;
    @Mock
    ProfileSessionPersister profileSessionPersister;

    @InjectMocks
    ProfileSessionService profileSessionService;

    ProfileSessionPO profileSessionPO;
    TransformationObject transformationObject;
    MBSProfileSession mBSProfileSession;

    @Before
    public void setUp() throws Exception {

        transformationObject = new TransformationObject();

        profileSessionPO = new ProfileSessionPO();
        profileSessionPO.setSessionId("sessionId");
        profileSessionPO.setUserName("userName");

        mBSProfileSession = new MBSProfileSession();
        mBSProfileSession.setSessionId("sessionId");
        mBSProfileSession.setUserName("userName");
    }

    /**
     * Initialize the mocks
     *
     * @throws MBSBaseException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void initMocks() throws MBSBaseException {

        transformationObject.setSourcePojo(profileSessionPO);
        transformationObject.setTargetPojo(mBSProfileSession);
        when(profileSessionPOTransformer.transform(any())).thenReturn(transformationObject);

        profileSessionService = new ProfileSessionService(profileSessionValidator, profileSessionTransformer,
                profileSessionPersister, profileSessionPOTransformer);
        MockitoAnnotations.initMocks(this);
        doNothing().when((Persister) profileSessionPersister).persist(any());
        doNothing().when(profileSessionValidator).validate(any());
        when(profileSessionPersister.getDao()).thenReturn(mBSProfileSessionDao);
        when(mbsObjectCreator.getTransformationObject()).thenReturn(new TransformationObject());
    }

    /**
     * @author gaur5c
     * @throws MBSBaseException
     */
    @Test
    public void saveProfileSession_Success() throws MBSBaseException {
        try {
            initMocks();
            ProfileSessionPO profileSessionPORet = profileSessionService.saveProfileSession(profileSessionPO);
            assertEquals(profileSessionPORet.getUserName(), profileSessionPO.getUserName());

        } catch (MBSBaseException e) {
            Assert.fail("Should not have thrown any exception");
        }

    }

    /**
     * @author gaur5c
     * @throws MBSBaseException
     */
    @Test
    public void getProfileSession_Success() throws MBSBaseException {

        initMocks();
        when(mBSProfileSessionDao.getById(any())).thenReturn(mBSProfileSession);

        ProfileSessionPO profileSessionPORet = profileSessionService.getProfileSession("sessionId");
        assertEquals(profileSessionPORet.getUserName(), profileSessionPO.getUserName());
    }

    /**
     * @author gaur5c
     * @throws MBSBaseException
     */
    @Test
    public void getProfileSession_Failure() throws MBSBaseException {

        initMocks();
        when(mBSProfileSessionDao.getById(any())).thenReturn(null);
        ProfileSessionPO profileSessionPORet = profileSessionService.getProfileSession("sessionId");
        assertNull(profileSessionPORet);
    }
}
