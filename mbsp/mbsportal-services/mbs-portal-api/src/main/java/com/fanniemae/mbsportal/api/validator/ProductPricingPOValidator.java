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

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fanniemae.mbsportal.api.po.ProductPricingPO;
import com.fanniemae.mbsportal.api.transformation.ProductPricingRequestTransformer;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.validator.BaseValidator;

/**
 * @author g8uaxt Created on 9/13/2017.
 */
@Component
public class ProductPricingPOValidator<T extends TransformationObject> extends BaseValidator<T> {
    
        /**
         * 
         * LOGGER Logger variable
         */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductPricingRequestTransformer.class);
        
        /**
         * 
         * Purpose: This method validates the ProductPricing PO object
         * 
         * @param transformationObject TransformationObject
         * @throws MBSBaseException
         */
        @SuppressWarnings("unchecked")
        @Override
        public void validate(TransformationObject transformationObject) throws MBSBaseException {
                LOGGER.debug("Entering validate method in ProductPOValidator");
                List<ProductPricingPO> productPricingPOList = (List<ProductPricingPO>) transformationObject
                        .getSourcePojo();
                // Validate if any of mandatory info missing
                for(ProductPricingPO productPricingPO : productPricingPOList) {
                        if(Objects.isNull(productPricingPO.getProductId().getIdentifier()) || Objects
                                .isNull(productPricingPO.getProductId().getSourceType()) || Objects
                                .isNull(productPricingPO.getProductId().getType())) {
                                LOGGER.error("Bad Request:Production Pricing Mandatory field Missing/empty.");
                                throw new MBSBusinessException(
                                        "Bad Request:Production Pricing Mandatory field Missing/empty.", MBSExceptionConstants.BUSINESS_EXCEPTION);
                        }
                        
                        if(Objects.isNull(productPricingPO.getEffectiveDate()) || Objects
                                .isNull(productPricingPO.getSettlementDate()) || StringUtils
                                .isEmpty(productPricingPO.getPassThroughRate())) {
                                LOGGER.error("Bad Request:Production Pricing Mandatory field Missing/empty.");
                                throw new MBSBusinessException(
                                        "Bad Request:Production Pricing Mandatory field Missing/empty.", MBSExceptionConstants.BUSINESS_EXCEPTION);
                        }
                }
        }
}
