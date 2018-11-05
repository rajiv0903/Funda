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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.constants.TradeBuySell;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.BaseTransformer;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_SOURCE_TYPE;
import com.fanniemae.mbsportal.utils.constants.TradeConstants.PRODUCT_TYPE;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * Class Name: TransactionRequestPOTransformer Purpose : This class handles the
 * transformations required for Transaction Request PO
 * 
 * @author g8upjv
 *
 */
@Component
public class TransactionRequestPOTransformer<T extends TransformationObject> extends BaseTransformer<T> {

    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionRequestPOTransformer.class);

    /**
     * 
     * Purpose: This method transforms the MBS Transaction Request object The
     * value of TransactionRequest model from Gemfire
     * 
     * @param transformationObject
     *            TransformationObject
     * @return TransformationObject
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    @Override
    public TransformationObject transform(TransformationObject transformationObject) throws MBSBaseException {
        MBSTransactionRequest mbsTransactionRequest = (MBSTransactionRequest) transformationObject.getTargetPojo();

        LOGGER.debug("Entering transform method in TransactionRequestPOTransformer");
        //CMMBSSTA01-1157 - Stream Pricing - Start
        MBSMarketIndicativePrice mbsMarketIndicativePrice = (MBSMarketIndicativePrice)transformationObject.getTransformationDataMap().get(MBSPServiceConstants.STREAM_PRICE);
        //CMMBSSTA01-1157 - Stream Pricing - End
        // Transform the Presentation object to domain object
        TransactionRequestPO transactionRequestPO = convertToTransactionPO(mbsTransactionRequest, mbsMarketIndicativePrice);
        // set BUY or SELL
        flipBuySell(transformationObject, transactionRequestPO, mbsTransactionRequest);
        transformationObject.setSourcePojo(transactionRequestPO);
        LOGGER.debug("Exiting transform method in TransactionRequestPOTransformer");
        return transformationObject;
    }

    /**
     * 
     * Purpose: This does the conversion from TransationPO to Transaction object
     *
     * @param mbsTransactionRequest
     *            The TransactionRequest object
     * @return TransactionRequestPO The presentaion object of TransactionRequest
     *         object
     * @throws MBSBaseException
     */
    private TransactionRequestPO convertToTransactionPO(MBSTransactionRequest mbsTransactionRequest, MBSMarketIndicativePrice mbsMarketIndicativePrice)
            throws MBSBaseException {
        TransactionRequestPO mbsTransactionPO = new TransactionRequestPO();
        ProductPO prodPO = new ProductPO();
        ProductIdPO prodIdPO = new ProductIdPO();
        try {
            mbsTransactionPO.setTransReqId(mbsTransactionRequest.getTransReqNumber());
            prodIdPO.setIdentifier(mbsTransactionRequest.getProductId().getIdentifier());
            prodIdPO.setSourceType(PRODUCT_SOURCE_TYPE.getEnum(mbsTransactionRequest.getProductId().getSourceType()));
            prodIdPO.setType(PRODUCT_TYPE.getEnum(mbsTransactionRequest.getProductId().getType()));
            prodPO.setProductId(prodIdPO);
            //Product Name Code
            prodPO.setNameCode(mbsTransactionRequest.getProductNameCode());
            //End
            mbsTransactionPO.setProduct(prodPO);
            // removed decimal values from this amount
            mbsTransactionPO.setTradeAmount(MBSPortalUtils.getInteger(mbsTransactionRequest.getTradeAmount()));
            mbsTransactionPO.setTradeSettlementDate(MBSPortalUtils.convertDateToString(
                    mbsTransactionRequest.getTradeSettlementDate(), DateFormats.DATE_FORMAT_NO_TIMESTAMP));
            // mbsTransactionPO.setTradeBuySellType(mbsTransactionRequest.getTradeBuySellType());
          //CMMBSSTA01-1525 - Updating coupon rate to 2 decimals
            mbsTransactionPO.setTradeCouponRate(
                    MBSPortalUtils.convertToString(mbsTransactionRequest.getTradeCouponRate(), 5, 2));
            mbsTransactionPO.setStateType(StateType.getEnum(mbsTransactionRequest.getStateType()));
            mbsTransactionPO.setSubmissionDate(MBSPortalUtils.convertDateToString(
                    mbsTransactionRequest.getSubmissionDate(), DateFormats.DATE_FORMAT_WITH_TIMESTAMP));
            mbsTransactionPO.setLastUpdatedDate(MBSPortalUtils.convertDateToString(
                    mbsTransactionRequest.getLastUpdated(), DateFormats.DATE_FORMAT_WITH_TIMESTAMP));
            mbsTransactionPO.setTraderId(mbsTransactionRequest.getTradeTraderIdentifierText());
            mbsTransactionPO.setLenderId(String.valueOf(mbsTransactionRequest.getCounterpartyTraderIdentifier()));
//            mbsTransactionPO.setLenderName(mbsTransactionRequest.getCounterpartyTraderName());
            
            String lenderName = mbsTransactionRequest.getCounterpartyTraderName();
            if(StringUtils.isNotBlank(lenderName) && lenderName.contains(MBSPServiceConstants.FIRSTNAME_LASTNAME_SEPERATOR))
            {
                //Last Name, First Name since the data is being store FN,LN
            	lenderName = lenderName.split(MBSPServiceConstants.FIRSTNAME_LASTNAME_SEPERATOR)[1] + MBSPServiceConstants.FIRSTNAME_LASTNAME_SEPERATOR
                        + lenderName.split(MBSPServiceConstants.FIRSTNAME_LASTNAME_SEPERATOR)[0];
                
            }
            mbsTransactionPO.setLenderName(lenderName);
            
            // split the tick/handle
            //TODO: Change it to handle and ticks
            if (mbsTransactionRequest.getpricePercentTicksText() != null) {
                String[] parts = mbsTransactionRequest.getpricePercentTicksText().split("-");
                /*
                 * CMMBSSTA01-1040: (Trader page) Allow '+' in third digit of
                 * ticks field and display in ticket and trx history
                 */
                mbsTransactionPO.setPricePercentTicksText(MBSPortalUtils.pricePercentTicksDisplayText(parts[0]));
                // End
                mbsTransactionPO.setPricePercentHandleText(parts[1]);
            }
            mbsTransactionPO
                    .setPricePercent(MBSPortalUtils.convertToString(mbsTransactionRequest.getPricePercent(), 13, 9));
            /*
             * CMMBSSTA01-1022: (Tech) Maintain versions for transaction request
             */
            mbsTransactionPO.setActiveVersion(mbsTransactionRequest.getActiveVersion());

            // CMMBSSTA01-1371 - Changes for TSP - Start
            mbsTransactionPO.setOboLenderSellerServicerNumber(mbsTransactionRequest.getLenderSellerServiceNumber());
            mbsTransactionPO.setTspShortName(mbsTransactionRequest.getTspShortName());
            // CMMBSSTA01-1371 - Changes for TSP - End

            // CMMBSSTA01-1373 - Adding TSP name - Start
            mbsTransactionPO.setDealerOrgName(mbsTransactionRequest.getDealerOrgName());
            mbsTransactionPO.setLenderEntityShortName(mbsTransactionRequest.getLenderShortName());
            // CMMBSSTA01-1373 - Adding TSP name - End
            LOGGER.debug("convertToTransactionPO:Coupon Rate-->" + mbsTransactionPO.getTradeCouponRate());
            
            mbsTransactionPO.setLastPublishTime(MBSPortalUtils.convertDateToString(MBSPortalUtils.getCurrentDate(), DateFormats.DATE_FORMAT_WITH_TIMESTAMP));
            
            //TODO: This transformation is not required as Data is being stored F.LN and that is what UI needs everywhere 
            String traderName = mbsTransactionRequest.getTraderName();
            if(StringUtils.isNotBlank(traderName) && traderName.contains(MBSPServiceConstants.FIRSTNAME_LASTNAME_SEPERATOR))
            {
                //Last Name, First Name since the data is being store FN,LN
                traderName = traderName.split(MBSPServiceConstants.FIRSTNAME_LASTNAME_SEPERATOR)[1] + MBSPServiceConstants.FIRSTNAME_LASTNAME_SEPERATOR
                        + traderName.split(MBSPServiceConstants.FIRSTNAME_LASTNAME_SEPERATOR)[0];
                
            }
            mbsTransactionPO.setTraderName(traderName);
            
            //CMMBSSTA01-1157 - Stream Pricing - Start
            if(Objects.nonNull(mbsMarketIndicativePrice) && Objects.nonNull(mbsMarketIndicativePrice.getBidPriceText())
                    && Objects.nonNull(mbsMarketIndicativePrice.getAskPriceText())){
                String[] parts;
                    if (TradeBuySell.BUY.name().equals(mbsTransactionRequest.getTradeBuySellType())) {
                        parts = mbsMarketIndicativePrice.getBidPriceText().split("-");
                    } else {
                        parts = mbsMarketIndicativePrice.getAskPriceText().split("-");
                    }
                mbsTransactionPO.setStreamPricePercentTicksText(MBSPortalUtils.pricePercentTicksDisplayText(parts[1]));
                mbsTransactionPO.setStreamPricePercentHandleText(parts[0]);
            //If the stream price is not available, display the price already stored.
            } else if (mbsTransactionRequest.getStreamPricePercentTicksText() != null) {
                String[] parts = mbsTransactionRequest.getStreamPricePercentTicksText().split("-");
                mbsTransactionPO.setStreamPricePercentTicksText(MBSPortalUtils.pricePercentTicksDisplayText(parts[1]));
                mbsTransactionPO.setStreamPricePercentHandleText(parts[0]);
            }
            //If no stream [prices are available it will be sent as blank
            //CMMBSSTA01-1157 - Stream Pricing - End
            
        } catch (Exception ex) {
            LOGGER.error("Error when transforming in TransactionRequestPOTransformer", ex);
            throw new MBSSystemException("Error when transforming in TransactionRequestPOTransformer",
                    MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        return mbsTransactionPO;
    }

    /**
     * 
     * To flip status before presenting data to the caller
     * 
     * @param transformationObject
     *            TransformationObject
     * @param transactionRequestPO
     *            TransactionRequestPO
     * @param mbsTransactionRequest
     *            MBSTransactionRequest
     */
    private void flipBuySell(TransformationObject transformationObject, TransactionRequestPO transactionRequestPO,
            MBSTransactionRequest mbsTransactionRequest) {
        if (MBSRoleType.TRADER.equals(transformationObject.getMBSRoleType())) {
            if (TradeBuySell.BUY.toString().equals(mbsTransactionRequest.getTradeBuySellType())) {
                transactionRequestPO.setTradeBuySellType(TradeBuySell.BID.toString());
            } else if (TradeBuySell.SELL.toString().equals(mbsTransactionRequest.getTradeBuySellType())) {
                transactionRequestPO.setTradeBuySellType(TradeBuySell.OFFER.toString());
            }
        } else if (MBSRoleType.LENDER.equals(transformationObject.getMBSRoleType())
                || MBSRoleType.TSP.equals(transformationObject.getMBSRoleType())) {
            transactionRequestPO.setTradeBuySellType(mbsTransactionRequest.getCounterPartyBuySellType());
        }
    }
}
