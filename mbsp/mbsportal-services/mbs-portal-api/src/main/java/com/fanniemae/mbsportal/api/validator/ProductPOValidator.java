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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.validator.BaseValidator;

/**
 * Class Name: ProductPOValidator Purpose : This class handles the
 * validations for Transaction object
 * 
 * @author g8upjv
 *
 */
@Component
public class ProductPOValidator<T extends TransformationObject> extends BaseValidator<T> {
    
    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductPOValidator.class);

    /**
     * 
     * Purpose: This method validates the Transaction Request PO object
     * 
     * @param transformationObject TransformationObject
     * @throws MBSBaseException
     */
    @Override
    public void validate(TransformationObject transformationObject) throws MBSBaseException {
        LOGGER.debug("Entering validate method in ProductPOValidator");
    	ProductPO productPO = (ProductPO) transformationObject.getSourcePojo();
        // Validate if Name code is empty
        if (StringUtils.isEmpty(productPO.getDescription())) {
              LOGGER.error("Bad Request: Production description is empty.");
              throw new MBSBusinessException("Bad Request: Production description is empty.", MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
    }

}
