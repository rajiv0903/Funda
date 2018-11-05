package com.fanniemae.mbsportal.utils;

import com.fanniemae.mbsportal.api.po.TradeServiceRequestPO;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.model.MBSProduct;
import com.fanniemae.mbsportal.model.MBSProfile;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author g8uaxt Created on 6/22/2018.
 */

public class MBSMockUtil {
        //utility methods
        
        public  List<TradeServiceRequestPO> createTradeServiceRequestPOs() {
                List<TradeServiceRequestPO> tradeServiceRequestPOs = new ArrayList<>();
                TradeServiceRequestPO tradeServiceRequestPO = new TradeServiceRequestPO();
                tradeServiceRequestPO.setPricePercent(new BigDecimal(0.1));
                tradeServiceRequestPO.setTradeSettlementDate(MBSPortalUtils.addDays(new Date(), 3));
                // tradeServiceRequestPO.set
                
                //add
                tradeServiceRequestPOs.add(tradeServiceRequestPO);
                return tradeServiceRequestPOs;
        }
        
        public  List<MBSTransactionRequest> createTransactionRequests() {
                List<MBSTransactionRequest> transactionRequests = new ArrayList<>();
                transactionRequests.add(createTransactionRequest(StateType.TRADER_CONFIRMED));
                return transactionRequests;
        }
        
        public  MBSTransactionRequest createTransactionRequest(StateType stateType) {
                ProductId prodId = new ProductId();
                prodId.setIdentifier(new Long(1));
                prodId.setSourceType(TradeConstants.PRODUCT_SOURCE_TYPE.PU.toString());
                prodId.setType(TradeConstants.PRODUCT_TYPE.MBS.toString());
                MBSTransactionRequest mbsTransactionRequest = new MBSTransactionRequest();
                mbsTransactionRequest.setProductId(prodId);
                mbsTransactionRequest.setCounterpartyTraderIdentifier("testLender");
                mbsTransactionRequest.setStateType(stateType.toString());
                mbsTransactionRequest.setSubmissionDate(new Date());
                mbsTransactionRequest.setTradeAmount(new BigDecimal(9000000));
                mbsTransactionRequest.setTradeBuySellType("SELL");
                mbsTransactionRequest.setTradeCouponRate(new BigDecimal(5.5));
                mbsTransactionRequest.setTradeSettlementDate(new Date());
                mbsTransactionRequest.setTransReqNumber("10001");
                mbsTransactionRequest
                        .setPricePercent(new BigDecimal(104.000000000).setScale(9).round(MathContext.DECIMAL64));
                mbsTransactionRequest.setTradeTraderIdentifierText("Test-trader");
                return mbsTransactionRequest;
        }
        
        public  MBSProfile createMBSProfile() {
                MBSProfile mbsProfile = new MBSProfile();
                mbsProfile.setBrsUserName("brs-testusr");
                return mbsProfile;
        }
        
        public  MBSProduct createMBSProduct() {
                MBSProduct mbsProduct = new MBSProduct();
                ProductId productId = new ProductId();
                productId.setIdentifier(1L);
                productId.setSourceType("PU");
                productId.setType("MBS");
                productId.setProductIdStr("1.PU.MBS");
                mbsProduct.setProductId(productId);
                mbsProduct.setProductNameCode("FN30");
                mbsProduct.setProductBRSCode("bkrish");
                mbsProduct.setBrsSubPortfolioShortName("SFB_UAT");
                return mbsProduct;
        }
        
}
