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

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSProduct;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.BaseTransformer;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * This class handles the
 * transformations required for Product
 *
 * @author g8upjv
 *
 */
@Component
public class ProductTransformer <T extends TransformationObject> extends  BaseTransformer<T> {

    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductTransformer.class);
    
    /**
     * 
     * Purpose: This method transforms the MBS Product object
     *
     * @param transformationObject
     *            The presentation object ProductPO
     * @return MBSProduct The model object is returned to save in
     *         Gemfire
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    @Override
    public TransformationObject transform(TransformationObject transformationObject) throws MBSBaseException {
        LOGGER.debug("Entering transform method in ProductTransformer");
        ProductPO productPO = (ProductPO)transformationObject.getSourcePojo();
        MBSProduct mbsProduct = new MBSProduct();
        ProductId productId = new ProductId();
        try{
	        if(!Objects.equals(productPO, null)){
	        	mbsProduct.setProductAgencyType(productPO.getAgencyType());
	        	mbsProduct.setSecurityTerm(productPO.getSecurityTerm());
	        	mbsProduct.setProductNameCode(productPO.getNameCode());
	        	mbsProduct.setProductPUCode(productPO.getPuNameCode());
	        	mbsProduct.setProductBRSCode(productPO.getBrsNameCode());
	        	mbsProduct.setBrsSubPortfolioShortName(productPO.getBrsSubPortfolioShortName());
	        	mbsProduct.setProductNameCodeDisplayOrderNum(productPO.getNameCodeSortOrder());
	        	mbsProduct.setProductDescription(productPO.getDescription());
	        	if(Objects.nonNull(productPO.getProductId().getIdentifier())){
	        		productId.setIdentifier(productPO.getProductId().getIdentifier().longValue());
	        	}
	        	productId.setSourceType(productPO.getProductId().getSourceType().toString());
	        	productId.setType(productPO.getProductId().getType().toString());
	        	mbsProduct.setProductId(productId);
	        }
        } catch (Exception ex) {
			LOGGER.error("Error when transforming input Product object", ex);
			throw new MBSSystemException("Error when transforming input Product object", MBSExceptionConstants.SYSTEM_EXCEPTION);
		}
        transformationObject.setTargetPojo(mbsProduct);
        LOGGER.debug("Exiting transform method in ProductTransformer");
        return transformationObject;
     }
    
}
