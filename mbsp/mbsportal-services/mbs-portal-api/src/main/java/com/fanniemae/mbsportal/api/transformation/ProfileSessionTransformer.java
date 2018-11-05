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
 * @File: com.fanniemae.mbsportal.api.transformation.ProfileSessionTransformer.java
 * @Revision :
 * @Description: CMMBSSTA01-1212 : (Tech) Trader user login calls MBSP API with
 *               valid Session ID
 */
@Component
public class ProfileSessionTransformer<T extends TransformationObject> extends BaseTransformer<T> {

    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileSessionTransformer.class);

    /**
     * 
     * @param transformationObject
     *            TransformationObject
     * @return TransformationObject
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    @Override
    public TransformationObject transform(TransformationObject transformationObject) throws MBSBaseException {

        LOGGER.debug("Entering transform method in ProfileSessionTransformer");
        ProfileSessionPO profileSessionPO = (ProfileSessionPO) transformationObject.getSourcePojo();

        MBSProfileSession mBSProfileSession = (MBSProfileSession) transformationObject.getTargetPojo();

        // Transform the Presentation object to domain object
        mBSProfileSession = convertToModel(profileSessionPO, mBSProfileSession);
        transformationObject.setTargetPojo(mBSProfileSession);
        LOGGER.debug("Exiting transform method in ProfileSessionTransformer");
        return transformationObject;
    }

    /**
     * 
     * Convert to Model object from ProfileSessionPO
     * 
     * @param profileSessionPO
     *            ProfileSessionPO
     * @return mBSProfileSession MBSProfileSession
     * @throws MBSBaseException
     */
    private MBSProfileSession convertToModel(ProfileSessionPO profileSessionPO, MBSProfileSession mBSProfileSession)
            throws MBSBaseException {

        mBSProfileSession.setSessionId(profileSessionPO.getSessionId());
        mBSProfileSession.setUserName(profileSessionPO.getUserName());
        return mBSProfileSession;
    }

}
