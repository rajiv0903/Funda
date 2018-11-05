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

package com.fanniemae.mbsportal.pu.transformer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePriceHistory;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.BaseTransformer;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: Jul 2, 2018
 * @File: com.fanniemae.mbsportal.pu.transformer.MarketIndicativePriceHistoryMessageTransformer.java
 * @Revision:
 * @Description: MarketIndicativePriceHistoryMessageTransformer.java
 */
@Component
public class MarketIndicativePriceHistoryMessageTransformer<T extends TransformationObject>
        extends BaseTransformer<T> {

    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(MarketIndicativePriceHistoryMessageTransformer.class);

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

        LOGGER.debug("Entering transform method in MarketIndicativePriceHistoryMessageTransformer");

        List<MBSMarketIndicativePrice> mbsMarketIndicativePriceLst = (List<MBSMarketIndicativePrice>) transformationObject
                .getSourcePojo();
        List<MBSMarketIndicativePriceHistory> mBSMarketIndicativePriceHistory = convertToIndicativePriceHistoryLst(
                mbsMarketIndicativePriceLst);
        transformationObject.setTargetPojo(mBSMarketIndicativePriceHistory);

        LOGGER.debug("Exiting transform method in MarketIndicativePriceHistoryMessageTransformer");
        return transformationObject;
    }

    /**
     * 
     * @param mbsMarketIndicativePriceLst
     * @return List of MBSMarketIndicativePriceHistory
     * @throws MBSBaseException
     */
    private List<MBSMarketIndicativePriceHistory> convertToIndicativePriceHistoryLst(
            List<MBSMarketIndicativePrice> mbsMarketIndicativePriceLst) throws MBSBaseException {

        List<MBSMarketIndicativePriceHistory> mBSMarketIndicativePriceHistoryLst = new ArrayList<MBSMarketIndicativePriceHistory>();

        for (MBSMarketIndicativePrice mBSMarketIndicativePrice : mbsMarketIndicativePriceLst) {

            LOGGER.debug("mBSMarketIndicativePrice : {}", mBSMarketIndicativePrice);

            MBSMarketIndicativePriceHistory mBSMarketIndicativePriceHistory = new MBSMarketIndicativePriceHistory(null,
                    mBSMarketIndicativePrice.getProductNameCode(), mBSMarketIndicativePrice.getPuNameCode(),
                    mBSMarketIndicativePrice.getObservationDate(), mBSMarketIndicativePrice.getPassThroughRate(),
                    mBSMarketIndicativePrice.getForward(), mBSMarketIndicativePrice.getSettlementDate(),
                    mBSMarketIndicativePrice.getCutOffDate(), mBSMarketIndicativePrice.getBidPricePercent(),
                    mBSMarketIndicativePrice.getBidPriceText(), mBSMarketIndicativePrice.getAskPricePercent(),
                    mBSMarketIndicativePrice.getAskPriceText(), mBSMarketIndicativePrice.getEventType(),
                    mBSMarketIndicativePrice.getMidPricePercent(), mBSMarketIndicativePrice.getMidPriceText(),
                    MBSPortalUtils.getDateTimeFromEpochMilli(mBSMarketIndicativePrice.getObservationDate()));

            mBSMarketIndicativePriceHistoryLst.add(mBSMarketIndicativePriceHistory);
        }
        return mBSMarketIndicativePriceHistoryLst;

    }

}
