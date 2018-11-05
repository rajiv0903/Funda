/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.validator;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.ProfileSessionPO;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.validator.BaseValidator;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Mar 13, 2018
 * @File: com.fanniemae.mbsportal.api.validator.ProfileSessionValidator.java
 * @Revision :
 * @Description: CMMBSSTA01-1212 : (Tech) Trader user login calls MBSP API with
 *               valid Session ID
 */
@Component
public class ProfileSessionValidator<T extends TransformationObject> extends BaseValidator<T> {

    /**
     * 
     * LOGGER Logger variable
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(ProfileSessionValidator.class);

    /**
     * 
     * Purpose: This method validates the ProfileSession PO object
     * 
     * @param transformationObject
     *            TransformationObject
     * @throws MBSBaseException
     */
    @Override
    public void validate(TransformationObject transformationObject) throws MBSBaseException {

        LOGGER.debug("Entering validate method in ProfileSessionValidator");

        ProfileSessionPO profileSessionPO = (ProfileSessionPO) transformationObject.getSourcePojo();

        if (Objects.isNull(profileSessionPO.getSessionId()) // session ID
        ) {
            LOGGER.error("Bad Request:Profile Session Mandatory sessionid Missing/empty.");
            throw new MBSBusinessException("Bad Request:Profile Session Mandatory sessionid Missing/empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        if (Objects.isNull(profileSessionPO.getUserName()) // username
        ) {
            LOGGER.error("Bad Request:Profile Session Mandatory username Missing/empty.");
            throw new MBSBusinessException("Bad Request:Profile Session username Missing/empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
    }

}
