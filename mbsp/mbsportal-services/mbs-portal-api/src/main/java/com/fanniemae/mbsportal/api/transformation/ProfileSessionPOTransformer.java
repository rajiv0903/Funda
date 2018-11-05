/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.transformation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.ProfileSessionPO;
import com.fanniemae.mbsportal.model.MBSProfileSession;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.BaseTransformer;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Mar 13, 2018
 * @File: com.fanniemae.mbsportal.api.transformation.ProfileSessionPOTransformer.java
 * @Revision : 
 * @Description: CMMBSSTA01-1212 : (Tech) Trader user login calls MBSP API with
 *               valid Session ID
 */
@Component
public class ProfileSessionPOTransformer <T extends TransformationObject> extends  BaseTransformer<T> {

    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileSessionPOTransformer.class);

    /**
     * 
     * This method does the transformation of object from domain to PO
     * 
     * @param transformationObject TransformationObject
     * @return TransformationObject
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    @Override
    public TransformationObject transform(TransformationObject transformationObject) throws MBSBaseException {

        LOGGER.debug("Entering transform method in ProfileSessionPOTransformer");
        MBSProfileSession mBSProfileSession = (MBSProfileSession) transformationObject.getTargetPojo();
        ProfileSessionPO profileSessionPO = convertToProfileSessionPO(mBSProfileSession);
        transformationObject.setSourcePojo(profileSessionPO);
        LOGGER.debug("Exiting transform method in ProfileSessionPOTransformer");
        return transformationObject;
    }
    
    /**
     * 
     * Purpose: This does the conversion from MBSProfileSession to ProfileSessionPO object
     *
     * @param mBSProfileSession
     *            The TransactionRequest object
     * @return ProfileSessionPO The presentation object of MBSProfileSession object
     * @throws MBSBaseException
     */
    private ProfileSessionPO convertToProfileSessionPO(MBSProfileSession mBSProfileSession) throws MBSBaseException 
    {
        ProfileSessionPO profileSessionPO = new ProfileSessionPO();
        profileSessionPO.setSessionId(mBSProfileSession.getSessionId());
        profileSessionPO.setUserName(mBSProfileSession.getUserName());
        return profileSessionPO;
    }
}
