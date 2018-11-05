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

import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.model.MBSProduct;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.BaseTransformer;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * This class handles the transformations required for Product PO
 *
 * @author g8upjv
 *
 */
@Component
public class ProductPOTransformer<T extends TransformationObject> extends BaseTransformer<T> {

	// @InjectLog
	// private Logger LOGGER;
        /**
         * 
         * LOGGER Logger variable
         */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductPOTransformer.class);

	/**
	 * 
	 * Purpose: This method transforms the MBS Product object
	 *
	 * @param transformationObject TransformationObject
	 * @return TransformationObject
	 * @throws MBSBaseException
	 */
	@SuppressWarnings("unchecked")
        @Override
	public TransformationObject transform(TransformationObject transformationObject) throws MBSBaseException {
		MBSProduct mbsProduct = (MBSProduct) transformationObject.getTargetPojo();

		LOGGER.debug("Entering transform method in ProductPOTransformer");
		// Transform the Presentation object to domain object
		ProductPO productPO = convertToProductPO(mbsProduct);
		transformationObject.setSourcePojo(productPO);
		LOGGER.debug("Exiting transform method in ProductPOTransformer");
		return transformationObject;
	}

	/**
	 * 
	 * Purpose: This does the conversion from MBSProduct to ProductPO object
	 *
	 * @param mbsProduct MBSProduct
	 * @return ProductPO The presentation object of MBSProduct object
	 * @throws MBSBaseException
	 */
	private ProductPO convertToProductPO(MBSProduct mbsProduct) throws MBSBaseException {
		ProductPO productPO = new ProductPO();
		ProductIdPO productIdPO = new ProductIdPO();
		try {
			productIdPO.setIdentifier(mbsProduct.getProductId().getIdentifier());
			productIdPO.setSourceType(PRODUCT_SOURCE_TYPE.valueOf(mbsProduct.getProductId().getSourceType()));
			productIdPO.setType(PRODUCT_TYPE.valueOf(mbsProduct.getProductId().getType()));
			productPO.setProductId(productIdPO);
			productPO.setAgencyType(mbsProduct.getProductAgencyType());
			productPO.setDescription(mbsProduct.getProductDescription());
			productPO.setNameCode(mbsProduct.getProductNameCode());
			productPO.setPuNameCode(mbsProduct.getProductPUCode());
			productPO.setBrsNameCode(mbsProduct.getProductBRSCode());
			productPO.setBrsSubPortfolioShortName(mbsProduct.getBrsSubPortfolioShortName());
			productPO.setSecurityTerm(mbsProduct.getSecurityTerm());
			productPO.setNameCodeSortOrder(mbsProduct.getProductNameCodeDisplayOrderNum());
			LOGGER.debug("Product PO values:" + productPO);

		} catch (Exception ex) {
			LOGGER.error("Error when transforming Product object from data store ", ex);
			throw new MBSSystemException("Error when transforming Product object from data store ", MBSExceptionConstants.SYSTEM_EXCEPTION);
		}
		return productPO;
	}
}
