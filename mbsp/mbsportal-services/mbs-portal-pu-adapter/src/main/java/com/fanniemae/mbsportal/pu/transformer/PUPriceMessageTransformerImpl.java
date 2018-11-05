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

import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.model.MBSProduct;
import com.fanniemae.mbsportal.pu.constants.MessageEventType;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import static com.fanniemae.mbsportal.utils.PricingUtil.getHandle;
import static com.fanniemae.mbsportal.utils.PricingUtil.getTicks;
import com.fanniemae.mbsportal.utils.cache.AppCacheKeys;
import com.fanniemae.mbsportal.utils.cache.MBSPAppCache;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSDataAccessException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * ECF Transformer Implementation class has methods for the transformation
 *
 * @author g8upjv
 */
@Component
public class PUPriceMessageTransformerImpl implements PUPriceMessageTransformer {
        
        /**
         * 
         * LOGGER logger declaration
         */
        private static final Logger LOGGER = LoggerFactory.getLogger(PUPriceMessageTransformerImpl.class);
        
        /**
         * 
         * objMapper ObjectMapper
         */
        @Autowired
        private ObjectMapper objMapper;
        
        /**
         * 
         * mbspAppCache MBSPAppCache
         */
        @Autowired
        private MBSPAppCache mbspAppCache;
        
        /**
         * 
         * transform transform message into pojo
         *
         * @param inputMessage
         * @param eventType
         * @return
         * @throws MBSBaseException
         */
        @Override
        public List<MBSMarketIndicativePrice> transform(String inputMessage, MessageEventType eventType)
                throws MBSBaseException {
                
                List<MBSMarketIndicativePrice> mbsMarketIndicativePrice = new ArrayList<MBSMarketIndicativePrice>();
                try {
                        mbsMarketIndicativePrice = parseJsonPricePayload(inputMessage, eventType.getEventType());
                        
                } catch(MBSBaseException ex) {                        
                        LOGGER.error( "PUPriceMessageTransformerImpl: Error when converting PU Price Message from PU to MBS : {}", ex);
                        throw ex;
                        
                } catch(Exception ex) {                    
                    LOGGER.error( "PUPriceMessageTransformerImpl: Error when converting PU Price Message from PU to MBS : {}", ex);
                    throw new MBSDataAccessException("PUPriceMessageTransformerImpl: Error when converting PU Price Message from PU to MBS");
                    
                }
                return mbsMarketIndicativePrice;
        }
        
        /**
         * 
         * parseJsonPricePayload parsing json payload into list
         *
         * @param payload
         * @param eventType
         * @return
         * @throws MBSBaseException
         */
        public List<MBSMarketIndicativePrice> parseJsonPricePayload(String payload, String eventType)
                throws MBSBaseException {
                List<MBSMarketIndicativePrice> mbsMarketIndicativePriceLst = new ArrayList<MBSMarketIndicativePrice>();
                objMapper = new ObjectMapper();
                MBSMarketIndicativePrice mbsMarketIndicativePrice;
                try {
                        if(StringUtils.isBlank(payload)) {
                                LOGGER.error("The pricing payload from PU is empty");
                                throw new MBSDataAccessException("PUPriceMessageTransformerImpl: Error when when parsing data from payload from PU");
                        } else {
                                JsonNode jsonNode = objMapper.readTree(payload);
                                JsonNode jsonNodeArray = jsonNode.get("prices");
                                String pricesArray = jsonNode.get("prices").toString();
                                for(int i = 0; i < jsonNodeArray.size(); i++) {
                                        mbsMarketIndicativePrice = new MBSMarketIndicativePrice();
                                        JsonNode jsonPriceNode = jsonNodeArray.get(i);
                                        JsonNode collectionNode = jsonPriceNode.get("Collection");
                                        
                                        String collectionObservationTime =  collectionNode.get("CollectionObservationTime").asText();
                                        
                                        LOGGER.debug("collectionObservationTime: {}", collectionObservationTime);
                                        
                                        long effectiveTime = (MBSPortalUtils.convertToDateWithFormatterLocale(
                                                collectionObservationTime,
                                                DateFormats.DATE_FORMAT_WITH_TIMESTAMP_PRICING, Locale.US)).getTime();
                                        mbsMarketIndicativePrice.setObservationDate(Long.valueOf(effectiveTime));
                                        JsonNode simpleAttributesNode = collectionNode.get("CollectionAttributes")
                                                .get("SimpleAttributes");
                                        
                                        String askPrice = simpleAttributesNode.get("AskPrice").asText();
                                        String bidPrice = simpleAttributesNode.get("BidPrice").asText();
                                        String midPrice = simpleAttributesNode.get("MidPrice").asText();
                                        
                                        LOGGER.debug("askPrice: {}, bidPrice: {}, midPrice: {}", askPrice, bidPrice, midPrice);
                                        
                                        mbsMarketIndicativePrice
                                                .setAskPricePercent(MBSPortalUtils.convertToBigDecimal(askPrice, 13, 9));
                                        mbsMarketIndicativePrice
                                                .setAskPriceText(getHandle(askPrice) + "-" + getTicks(askPrice));
                                        
                                        mbsMarketIndicativePrice
                                                .setBidPricePercent(MBSPortalUtils.convertToBigDecimal(bidPrice, 13, 9));
                                        mbsMarketIndicativePrice
                                                .setBidPriceText(getHandle(bidPrice) + "-" + getTicks(bidPrice));
                                        
                                        mbsMarketIndicativePrice
                                                .setMidPricePercent(MBSPortalUtils.convertToBigDecimal(midPrice, 13, 9));
                                        mbsMarketIndicativePrice
                                                .setMidPriceText(getHandle(midPrice) + "-" + getTicks(midPrice));
                                        mbsMarketIndicativePrice.setPassThroughRate(MBSPortalUtils
                                                .convertToBigDecimal(simpleAttributesNode.get("Coupon").asText(), 5, 4)
                                                .scaleByPowerOfTen(2));
                                        mbsMarketIndicativePrice.setForward(
                                                Integer.valueOf(simpleAttributesNode.get("Forward").asText()));
                                        mbsMarketIndicativePrice.setSettlementDate(MBSPortalUtils
                                                .convertToDateWithFormatter(
                                                        simpleAttributesNode.get("SettlementDate").asText(),
                                                        DateFormats.DATE_FORMAT_NO_TIMESTAMP));
                                        long maturity = MBSPortalUtils
                                                .convertToLong(simpleAttributesNode.get("Maturity").asText());
                                        String term = String.valueOf(maturity / 12);
                                        //change name code
                                        String nameCodeOrg = simpleAttributesNode.get("IssuingAgency").asText();
                                        mbsMarketIndicativePrice.setPuNameCode(nameCodeOrg + term);
                                        String nameCodeMbsp = getMBSProductNameCode(nameCodeOrg, term);
                                        mbsMarketIndicativePrice.setProductNameCode(nameCodeMbsp);
                                        LOGGER.debug("PUPriceMessageTransformerImpl - Parsing Price info {}",mbsMarketIndicativePrice);
                                        mbsMarketIndicativePriceLst.add(mbsMarketIndicativePrice);
                                }
                        }
                } catch(MBSBaseException ex) {
                        LOGGER.error("PUPriceMessageTransformerImpl: Error when parsing data from payload from PU: {}", ex);
                        throw ex;
                        
                } catch(Exception ex) {
                        LOGGER.error("PUPriceMessageTransformerImpl: Error when parsing data from payload from PU: {}",ex);
                        throw new MBSDataAccessException("PUPriceMessageTransformerImpl: Error when parsing data from payload from PU");
                }
                return mbsMarketIndicativePriceLst;
        }
        
        /**
         * 
         * getMBSProductNameCode
         *
         * @param puNameCode
         * @param term
         * @return String
         */
        public String getMBSProductNameCode(String puNameCode, String term) throws MBSBaseException {
                String mbspProductCode = null;
                List<MBSProduct> mbspProducts = null;
                mbspProducts = (List<MBSProduct>) mbspAppCache.get(AppCacheKeys.PRODUCT_CACHE_KEY);
                if(mbspProducts==null){
                        LOGGER.warn("Cache returned null and fix it");
                }
                for(MBSProduct mbspProduct : mbspProducts) {
                        if(mbspProduct.getProductPUCode().equalsIgnoreCase(puNameCode + term)) {
                                mbspProductCode = mbspProduct.getProductNameCode();
                        }
                }
                LOGGER.debug("Translated Product Name code {} for PU code {} ", mbspProductCode, puNameCode + term);
                return mbspProductCode;
        }
}
