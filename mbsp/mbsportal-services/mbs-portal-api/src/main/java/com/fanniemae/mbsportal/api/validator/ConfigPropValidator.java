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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.ConfigPropPO;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.validator.BaseValidator;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 6, 2018
 * @File: com.fanniemae.mbsportal.api.validator.ConfigPropValidator.java
 * @Revision:
 * @Description: ConfigPropValidator.java
 */
@Component
public class ConfigPropValidator<T extends TransformationObject> extends BaseValidator<T> {

    /**
     * 
     * LOGGER Logger variable
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(ConfigPropValidator.class);

    /**
     * 
     * Purpose: This method validates the MBSConfigPO object
     * 
     * @param transformationObject
     *            TransformationObject
     * @throws MBSBaseException
     */
    @Override
    public void validate(TransformationObject transformationObject) throws MBSBaseException {

        LOGGER.debug("Entering validate method in ConfigPropValidator");

        ConfigPropPO configPO = (ConfigPropPO) transformationObject.getSourcePojo();

        if (Objects.isNull(configPO)) {
            LOGGER.error("Bad Request: Config Mandatory Missing/empty.");
            throw new MBSBusinessException("Bad Request: Config Mandatory Missing/empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        // key
        if (StringUtils.isBlank(configPO.getKey())) {
            LOGGER.error("Bad Request: Config Mandatory key Missing/empty.");
            throw new MBSBusinessException("Bad Request: Config Mandatory key Missing/empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        // value
        if (StringUtils.isBlank(configPO.getValue())) {
            LOGGER.error("Bad Request: Config Mandatory value Missing/empty.");
            throw new MBSBusinessException("Bad Request: Config value Missing/empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        // parent
        if (StringUtils.isBlank(configPO.getParent())) {
            LOGGER.error("Bad Request: Config Mandatory parent Missing/empty.");
            throw new MBSBusinessException("Bad Request: Config parent Missing/empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }

        // data type
        if (StringUtils.isBlank(configPO.getDataType())) {
            LOGGER.error("Bad Request: Config Mandatory dataType Missing/empty.");
            throw new MBSBusinessException("Bad Request: Config dataType Missing/empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        
        LOGGER.debug("Exiting validate method in ConfigPropValidator");

    }
}
