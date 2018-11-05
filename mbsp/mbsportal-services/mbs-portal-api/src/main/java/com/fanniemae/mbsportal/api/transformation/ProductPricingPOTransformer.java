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
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPricingPO;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSProductPricingRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.BaseTransformer;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * PO transformer for ProductPricingPO
 * @author g8uaxt Created on 9/13/2017.
 */
@Component
@Qualifier("productPricingPOTransformer")
public class ProductPricingPOTransformer<T extends TransformationObject> extends BaseTransformer<T> {
        
       /**
        * 
        * LOGGER Logger variable
        */
        @InjectLog
        private Logger LOGGER;
        
       /**
        * 
        * mbspProperties MbspProperties
        */
        @Autowired
        MbspProperties mbspProperties;
        
        /**
         * 
         * 
         * @param transformationObject TransformationObject
         * @return TransformationObject
         * @throws MBSBaseException
         */
        @SuppressWarnings("unchecked")
        @Override
        public TransformationObject transform(TransformationObject transformationObject) throws MBSBaseException {
                List<MBSProductPricingRequest> mbsProductPricingRequestList = (List<MBSProductPricingRequest>)
                        transformationObject
                        .getTargetPojo();
                LOGGER.debug("Entering transform method in ProductPricingPOTransformer");
                // Transform the Presentation object to domain object
                List<ProductPricingPO> productPricingPOList = new ArrayList<>();
                for(MBSProductPricingRequest mbsProductPricingRequest : mbsProductPricingRequestList) {
                        productPricingPOList.add(convertToPO(mbsProductPricingRequest));
                }
                transformationObject.setSourcePojo(productPricingPOList);
                LOGGER.debug("Exiting transform method in ProductPricingPOTransformer");
                return transformationObject;
        }
        
        /**
         * 
         * 
         * @param mbsProductPricingRequest MBSProductPricingRequest
         * @return ProductPricingPO
         * @throws MBSBaseException
         */
        private ProductPricingPO convertToPO(MBSProductPricingRequest mbsProductPricingRequest)
                throws MBSBaseException {
                ProductPricingPO productPricingPO = new ProductPricingPO();
                productPricingPO.setProductId(convertToPO(mbsProductPricingRequest.getProductId()));
                productPricingPO.setEffectiveDate(
                        MBSPortalUtils.convertDateToString(mbsProductPricingRequest.getEffectiveDate(),
                                DateFormats.DATE_FORMAT_NO_TIMESTAMP));
              //CMMBSSTA01-1525 - Updating coupon rate to 2 decimals
                productPricingPO.setPassThroughRate(
                        MBSPortalUtils.convertToString(mbsProductPricingRequest.getPassThroughRate(), 5, 2));
                productPricingPO.setMarketTermType(mbsProductPricingRequest.getMktTermType().intValue());
                productPricingPO.setSettlementDate(
                        MBSPortalUtils.convertDateToString(mbsProductPricingRequest.getSettlementDate(),
                                DateFormats.DATE_FORMAT_NO_TIMESTAMP));
                productPricingPO.setCutOffDate(
                		MBSPortalUtils.convertDateToString(mbsProductPricingRequest.getCutOffDate(),
                                DateFormats.DATE_FORMAT_NO_TIMESTAMP));
                productPricingPO.setProductNameCode(mbsProductPricingRequest.getProductNameCode()); //TODO: change it
                productPricingPO.setBuySellIndicator(mbsProductPricingRequest.getBuySellInd());
                boolean monthActive = updateActive(mbsProductPricingRequest.getCutOffDate());
                if(!monthActive){
                    LOGGER.debug("The product pricing info - filtering",productPricingPO);
                }
                productPricingPO.setActive(monthActive);
                productPricingPO.setMarkAsDelete(mbsProductPricingRequest.getLogicalDeleteIndicator().equals(MBSPServiceConstants.yesChar));
                return productPricingPO;
        }
        
        /**
         * 
         * Check whether the data can be active based on cutoff date
         * 
         * @param cutOffDate Date
         * @return boolean
         * @throws MBSBaseException
         */
        public boolean updateActive(Date cutOffDate) throws MBSBaseException{
        	try {
				int cutOffHour = Integer.valueOf(mbspProperties.getCutOffHour()).intValue();
				int cutOffMin = Integer.valueOf(mbspProperties.getCutOffMinute()).intValue();
				int cutOffSec = Integer.valueOf(mbspProperties.getCutOffSecond()).intValue();
				int cutOffMilliSec = Integer.valueOf(mbspProperties.getCutOffMillisecond()).intValue();
        		        if(MBSPortalUtils.isDateEqual(cutOffDate)){
        		            return MBSPortalUtils.isDateBeforeCutOffTime(cutOffHour, cutOffMin, cutOffSec, cutOffMilliSec);
				} else {
					return true;
				}
			} catch (MBSBaseException e) {
				LOGGER.error("Error in checking active status");
                throw e;
			}catch (Exception e) {
				LOGGER.error("Error in checking active status");
                throw new MBSSystemException("Error in checking active status", MBSExceptionConstants.SYSTEM_EXCEPTION);
			}
        }
        
        /**
         * 
         * @param productId ProductId
         * @return ProductIdPO
         */
        private ProductIdPO convertToPO(ProductId productId) {
                ProductIdPO productIdPO = new ProductIdPO();
                productIdPO.setIdentifier(productId.getIdentifier());
                productIdPO.setSourceType(TradeConstants.PRODUCT_SOURCE_TYPE.getEnum(productId.getSourceType()));
                productIdPO.setType(TradeConstants.PRODUCT_TYPE.getEnum(productId.getType()));
                return productIdPO;
        }
        
}
