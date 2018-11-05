/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 *
 */

package com.fanniemae.mbsportal.api.transformation;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.ProductMarketIndicativePricingVO;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.BaseTransformer;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jun 13, 2018
 * @File: com.fanniemae.mbsportal.pu.transformer.MarketIndicativePriceMessageTransformer.java
 * @Revision:
 * @Description: MarketIndicativePriceMessageTransformer.java
 */
@Component
public class MarketIndicativePriceMessageTransformer<T extends TransformationObject> extends BaseTransformer<T> {

    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketIndicativePriceMessageTransformer.class);

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

        LOGGER.debug("Entering transform method in MarketIndicativePriceMessageTransformer");

        MBSMarketIndicativePrice mbsMarketIndicativePrice = (MBSMarketIndicativePrice) transformationObject
                .getSourcePojo();
        ProductMarketIndicativePricingVO productMarketIndicativePricingVO = convertToIndicativePriceVOLst(
                mbsMarketIndicativePrice);
        transformationObject.setTargetPojo(productMarketIndicativePricingVO);

        LOGGER.debug("Exiting transform method in MarketIndicativePriceMessageTransformer");
        return transformationObject;
    }

    /**
     * 
     * @param mbsMarketIndicativePrice
     * @return List
     * @throws MBSBaseException
     */
    private ProductMarketIndicativePricingVO convertToIndicativePriceVOLst(
            MBSMarketIndicativePrice mBSMarketIndicativePrice) throws MBSBaseException {

        LOGGER.debug("mBSMarketIndicativePrice : {}", mBSMarketIndicativePrice);

        String[] bidParts = StringUtils.isNotBlank(mBSMarketIndicativePrice.getBidPriceText())? mBSMarketIndicativePrice.getBidPriceText().split("-") : "".split("-");
        String[] askParts = StringUtils.isNotBlank(mBSMarketIndicativePrice.getAskPriceText())?  mBSMarketIndicativePrice.getAskPriceText().split("-") : "".split("-");
        
        ProductMarketIndicativePricingVO.ProductMarketIndicativePriceVO bidPrice = new ProductMarketIndicativePricingVO.ProductMarketIndicativePriceVO(
                (bidParts.length ==2 )? bidParts[0] : null,
                (bidParts.length ==2 )? MBSPortalUtils.pricePercentTicksDisplayText(bidParts[1])  : null);
        ProductMarketIndicativePricingVO.ProductMarketIndicativePriceVO askPrice = new ProductMarketIndicativePricingVO.ProductMarketIndicativePriceVO(
                (askParts.length ==2 )?  askParts[0] : null,
                (askParts.length ==2 )?  MBSPortalUtils.pricePercentTicksDisplayText(askParts[1])  : null);


        ProductMarketIndicativePricingVO productMarketIndicativePricingVO = new ProductMarketIndicativePricingVO(
                mBSMarketIndicativePrice.getProductNameCode(), 
                Objects.nonNull(mBSMarketIndicativePrice.getSettlementDate())? MBSPortalUtils.convertDateToString(mBSMarketIndicativePrice.getSettlementDate(), DateFormats.DATE_FORMAT_NO_TIMESTAMP) : null, 
                Objects.nonNull(mBSMarketIndicativePrice.getObservationDate())? MBSPortalUtils.getLocalZonedDateTimeFromEpochMilli(mBSMarketIndicativePrice.getObservationDate(),MBSPortalUtils.DATE_TIME_FORMAT_UUUU_MM_DD_HH_MM_SS): null,
                Objects.nonNull(mBSMarketIndicativePrice.getPassThroughRate())? MBSPortalUtils.convertToString(mBSMarketIndicativePrice.getPassThroughRate(), 5, 2) : null,
                bidPrice, 
                askPrice);
        return productMarketIndicativePricingVO;

    }

}
