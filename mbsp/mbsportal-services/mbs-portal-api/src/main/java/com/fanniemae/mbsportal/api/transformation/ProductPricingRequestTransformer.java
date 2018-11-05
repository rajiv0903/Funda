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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.ProductPricingPO;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSProductPricingRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.BaseTransformer;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * This class handles the
 * transformations required for Product
 * @author g8uaxt Created on 9/13/2017.
 */
@Component
@Qualifier("productPricingRequestTransformer")
public class ProductPricingRequestTransformer<T extends TransformationObject> extends BaseTransformer<T> {
	
        /**
         * 
         * LOGGER Logger variable
         */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductPricingRequestTransformer.class);
        
	/**
	 * 
	 * @param transformationObject TransformationObject
	 * @return TransformationObject
	 * @throws MBSBaseException
	 */
        @SuppressWarnings("unchecked")
        @Override
        public TransformationObject transform(TransformationObject transformationObject) throws MBSBaseException {
                LOGGER.debug("Entering transform method in ProductPricingRequestTransformer");
                List<ProductPricingPO> productPricingPOList = (List<ProductPricingPO>) transformationObject
                        .getSourcePojo();
                List<MBSProductPricingRequest> mbsProductPricingRequestList = new ArrayList<>();
                for(ProductPricingPO productPricingPO : productPricingPOList) {
                        mbsProductPricingRequestList.add(convertToModel(productPricingPO));
                }
                transformationObject.setTargetPojo(mbsProductPricingRequestList);
                LOGGER.debug("Exiting transform method in ProductPricingRequestTransformer");
                return transformationObject;
        }
        
        /**
         * 
         * Convert to Model object MBSProductPricingRequest
         * 
         * @param productPricingPORequest ProductPricingPO
         * @return MBSProductPricingRequest
         * @throws MBSBaseException
         */
        private MBSProductPricingRequest convertToModel(ProductPricingPO productPricingPORequest)
                throws MBSBaseException {
                MBSProductPricingRequest mbsProductPricingRequest = new MBSProductPricingRequest();
                ProductId productId = new ProductId();
                productId.setIdentifier(productPricingPORequest.getProductId().getIdentifier());
                productId.setSourceType(productPricingPORequest.getProductId().getSourceType().name());
                productId.setType(productPricingPORequest.getProductId().getType().name());
                mbsProductPricingRequest.setProductId(productId);
                mbsProductPricingRequest.setProductNameCode(productPricingPORequest.getProductNameCode());
                mbsProductPricingRequest
                        .setEffectiveDate(MBSPortalUtils.convertToDateWithFormatter(productPricingPORequest.getEffectiveDate(),
                                DateFormats.DATE_FORMAT_NO_TIMESTAMP));
                //CMMBSSTA01-1525 - Updating coupon rate to 2 decimals
                mbsProductPricingRequest.setPassThroughRate(
                        MBSPortalUtils.convertToBigDecimal(productPricingPORequest.getPassThroughRate(), 5, 2));
                mbsProductPricingRequest.setMktTermType(Integer.valueOf(productPricingPORequest.getMarketTermType()));
                mbsProductPricingRequest
                        .setSettlementDate(MBSPortalUtils.convertToDateWithFormatter(productPricingPORequest
                                .getSettlementDate(),DateFormats.DATE_FORMAT_NO_TIMESTAMP));
                mbsProductPricingRequest
                .setCutOffDate(MBSPortalUtils.convertToDateWithFormatter(productPricingPORequest
                        .getCutOffDate(),DateFormats.DATE_FORMAT_NO_TIMESTAMP));
                //mbsProductPricingRequest.setMktPricePercent(productPricingPORequest.get);
                mbsProductPricingRequest.setBuySellInd(productPricingPORequest.getBuySellIndicator());
                
                if(productPricingPORequest.isMarkAsDelete()){
                    mbsProductPricingRequest.setLogicalDeleteIndicator(MBSPServiceConstants.yesChar);
                }
                return mbsProductPricingRequest;
        }
}
