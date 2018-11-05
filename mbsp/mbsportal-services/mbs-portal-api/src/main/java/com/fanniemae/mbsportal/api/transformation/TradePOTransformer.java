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

import com.fanniemae.mbsportal.api.po.TradePO;
import com.fanniemae.mbsportal.model.MBSTrade;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.BaseTransformer;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * This class handles the transformations required for Trade PO
 * 
 * @author g8upjv
 *
 */
@Component
public class TradePOTransformer<T extends TransformationObject> extends BaseTransformer<T> {
	
        /**
         * 
         * LOGGER Logger variable
         */
        private static final Logger LOGGER = LoggerFactory.getLogger(TradePOTransformer.class);

	/**
	 * 
	 * Purpose: This method transforms the MBS Trade object
	 * 
         * @param transformationObject TransformationObject
         * @return TransformationObject
         * @throws MBSBaseException
         */
	@SuppressWarnings("unchecked")
        @Override
	public TransformationObject transform(TransformationObject transformationObject) throws MBSBaseException {
		MBSTrade mbsTrade = (MBSTrade) transformationObject.getTargetPojo();

		LOGGER.debug("Entering transform method in TradePOTransformer");
		// Transform the Presentation object to domain object
		TradePO tradePO = convertToTradePO(mbsTrade);
		transformationObject.setSourcePojo(tradePO);
		LOGGER.debug("Exiting transform method in TradePOTransformer");
		return transformationObject;
	}

	/**
	 * 
	 * Purpose: This does the conversion from TradePO to Trade object
	 *
	 * @param mbsTrade the Trade object
	 * @return TradePO The presentation object of MBSTrade object
	 * @throws MBSBaseException
	 */
	private TradePO convertToTradePO(MBSTrade mbsTrade) throws MBSBaseException {
		LOGGER.debug("convertToTradePO trans id -->" + mbsTrade.getTransReqNumber());
		TradePO mbsTradePO = new TradePO();
		try {
			mbsTradePO.setTransactionRequestId(mbsTrade.getTransReqNumber());
			mbsTradePO.setTradeSourceId(mbsTrade.getSourceId());
			mbsTradePO.setTradeSourcePrimaryId(mbsTrade.getSourcePrimaryTradeId());
			mbsTradePO.setTradeSubPortfolioId(mbsTrade.getSubPortfolioId());
			mbsTradePO.setTradeDate(
					MBSPortalUtils.convertDateToString(mbsTrade.getTradeDate(), DateFormats.DATE_FORMAT_NO_TIMESTAMP));
			mbsTradePO.setLenderEntityName(mbsTrade.getTradeParty().getPartyShortName());
			mbsTradePO.setTradeSubPortfolioShortName(mbsTrade.getSubPortfolioShortName());
		} catch (MBSBaseException ex) {
			LOGGER.error("Error when transforming object from data store " + ex);
			throw ex;
		} catch (Exception ex) {
			LOGGER.error("Error when transforming object from data store " + ex);
			throw new MBSSystemException("Error when transforming object from data store ", MBSExceptionConstants.SYSTEM_EXCEPTION);
		}
		return mbsTradePO;
	}
}
