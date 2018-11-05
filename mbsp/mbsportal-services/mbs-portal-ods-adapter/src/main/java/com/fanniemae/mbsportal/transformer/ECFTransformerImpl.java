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

package com.fanniemae.mbsportal.transformer;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.constants.MBSAdapterConstants;
import com.fanniemae.mbsportal.ecf.ods.EventMessage;
import com.fanniemae.mbsportal.ecf.ods.Trade;
import com.fanniemae.mbsportal.ecf.ods.TradeAttribute;
import com.fanniemae.mbsportal.ecf.ods.TradeEventMessage;
import com.fanniemae.mbsportal.model.MBSTrade;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSDataAccessException;
import com.fanniemae.securitiesods.ods_core.domain.TradeAccounting;
import com.fanniemae.securitiesods.ods_core.domain.TradeBalance;
import com.fanniemae.securitiesods.ods_core.domain.TradeParty;
import com.fanniemae.securitiesods.ods_core.domain.TradePayment;
import com.fanniemae.securitiesods.ods_core.domain.TradeProduct;
import com.google.common.base.Objects;

/**
 * ECF Transformer Implementation class has methods for the transformation
 * 
 * @author g8upjv
 *
 */
@Component
public class ECFTransformerImpl implements ECFTransformer {

    /**
     * logger declaration
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ECFTransformerImpl.class);

    @Value("${mbsp.ts.sourceSystem}")
    String sourceSystem;

    /**
     * currentVerJAXBContext declaration
     */
    protected static JAXBContext ecfJaxbContext;

    Unmarshaller unmarshaller;

    /**
     * 
     * @param inputMessage
     * @return
     * @throws MBSBaseException
     */
    @Override
    public TradeEventMessage getMBSTrade(String inputMessage) throws MBSBaseException {

        TradeEventMessage tradeEventMessage;
        EventMessage eventMsg;
        try {
            if (Objects.equal(null, ecfJaxbContext)) {
                ecfJaxbContext = JAXBContext.newInstance(EventMessage.class);
            }
            if (Objects.equal(null, unmarshaller)) {
                unmarshaller = ecfJaxbContext.createUnmarshaller();
            }
            StringReader reader = new StringReader(inputMessage);
            eventMsg = (EventMessage) unmarshaller.unmarshal(reader);
            tradeEventMessage = eventMsg.getTradeEventMessage();
            LOGGER.debug("Converted " + tradeEventMessage);
        } catch (JAXBException ex) {
            LOGGER.error(
                    "javax.xml.bind.JAXBException in ECFTransformerImpl when unmarshalling ECF \n Thrown as a ParsingException :"
                            + ex);
            throw new MBSDataAccessException(
                    "javax.xml.bind.JAXBException in ECFTransformerImpl when unmarshalling ECF \n Thrown as a ParsingException");
        }
        return tradeEventMessage;
    }

    /**
     * 
     * @param TradeEventMessage
     *            inputMessage
     * @return
     * @throws MBSBaseException
     */
    @Override
    public MBSTrade transform(TradeEventMessage inputMessage) throws MBSBaseException {

        Trade trade = inputMessage.getTrade();
        // Only populating the Trade details, Trade Attributes, TradeAccounting,
        // TradeBalance, TradeParty, TradePayment and TradeProducts
        MBSTrade mbsTrade = new MBSTrade();
        try {
            if (!Objects.equal(trade, null)) {
                mbsTrade.setAmount(trade.getAmount());
                mbsTrade.setApprovedOverDeliveryAmount(trade.getApprovedOverDeliveryAmount());
                mbsTrade.setApprovedUnderDeliveryAmount(trade.getApprovedUnderDeliveryAmount());
                mbsTrade.setBuySellCode(trade.getBuySellCode());
                mbsTrade.setConfirmationStateCode(trade.getConfirmationStateCode());
                mbsTrade.setContractualSettlementStateCode(trade.getContractualSettlementStateCode());
                mbsTrade.setCouponRate(trade.getCouponRate());
                mbsTrade.setCreatedOn(Objects.equal(trade.getCreatedOn(), null) ? null
                        : MBSPortalUtils.convertGregorianToDate(trade.getCreatedOn(), DateFormats.DATE_FORMAT_WITH_TIMESTAMP));
                mbsTrade.setCurrencyTypeCode(trade.getCurrencyTypeCode());
                mbsTrade.setCurrentFaceAmount(trade.getCurrentFaceAmount());
                mbsTrade.setCusipTypeCode(trade.getCusipTypeCode());
                mbsTrade.setExecutionTypeCode(trade.getExecutionTypeCode());
                mbsTrade.setLastUpdated(Objects.equal(trade.getLastUpdated(), null) ? null
                        : MBSPortalUtils.convertGregorianToDate(trade.getLastUpdated(), DateFormats.DATE_FORMAT_WITH_TIMESTAMP));
                mbsTrade.setLogicalDeleteIndicator(String.valueOf(trade.getLogicalDeleteIndicator()).charAt(0));
                mbsTrade.setNettingParticipationLevelTypeCode(trade.getNettingParticipationLevelTypeCode());
                mbsTrade.setOdsTransactionTypeCode(trade.getOdsTransactionTypeCode());
                mbsTrade.setOdsVersionNumber(trade.getOdsVersionNumber());
                mbsTrade.setOptionEffectiveDate(Objects.equal(trade.getOptionEffectiveDate(), null) ? null
                        : MBSPortalUtils.convertGregorianToDate(trade.getOptionEffectiveDate(), DateFormats.DATE_FORMAT_WITH_TIMESTAMP));
                mbsTrade.setOptionExpirationDate(Objects.equal(trade.getOptionExpirationDate(), null) ? null
                        : MBSPortalUtils.convertGregorianToDate(trade.getOptionExpirationDate(), DateFormats.DATE_FORMAT_WITH_TIMESTAMP));
                mbsTrade.setPricePercent(trade.getPricePercent());
                mbsTrade.setPrimaryTransactionTypeCode(trade.getPrimaryTransactionTypeCode());
                mbsTrade.setProductCusipId(trade.getProductCusipId());
                mbsTrade.setSecondaryTransactionTypeCode(trade.getSecondaryTransactionTypeCode());
                mbsTrade.setSecurityDollarRollLegTypeCode(trade.getSecurityDollarRollLegTypeCode());
                mbsTrade.setSettlementDate(Objects.equal(trade.getSettlementDate(), null) ? null
                        : MBSPortalUtils.convertGregorianToDate(trade.getSettlementDate(), DateFormats.DATE_FORMAT_WITH_TIMESTAMP));
                mbsTrade.setClearingAgencyTypeCode(trade.getSettlementLocation());
                mbsTrade.setSettlementStateCode(trade.getSettlementStateCode());
                mbsTrade.setSourceId(trade.getSourceId());
                mbsTrade.setSourcePrimaryTradeId(trade.getSourcePrimaryTradeId());
                mbsTrade.setSourceTransactionEntryDate(Objects.equal(trade.getSourceTransactionEntryDate(), null) ? null
                        : MBSPortalUtils.convertGregorianToDate(trade.getSourceTransactionEntryDate(), DateFormats.DATE_FORMAT_WITH_TIMESTAMP));
                mbsTrade.setSourceTransactionEntryUserId(trade.getSourceTransactionEntryUserId());
                mbsTrade.setSourceTransactionLastUpdateDate(
                        Objects.equal(trade.getSourceTransactionLastUpdateDate(), null) ? null
                                : MBSPortalUtils.convertGregorianToDate(trade.getSourceTransactionLastUpdateDate(), DateFormats.DATE_FORMAT_WITH_TIMESTAMP));
                mbsTrade.setSourceTransactionLastUpdateUserId(trade.getSourceTransactionLastUpdateUserId());
                mbsTrade.setSourceVersionNumber(trade.getSourceVersionNumber());
                mbsTrade.setSubPortfolioId(trade.getSubPortfolioId());
                mbsTrade.setSubPortfolioShortName(trade.getSubPortfolioShortName());
                mbsTrade.setTbaCusipId(trade.getTbaCusipId());
                mbsTrade.setToBeAnnouncedStipulatedSpecifiedCode(trade.getToBeAnnouncedStipulatedSpecifiedCode());
                if (trade.getTradeAccounting() != null) {
                    TradeAccounting tradeAcct = new TradeAccounting();
                    tradeAcct.setTradeBookIntentTypeCode(trade.getTradeAccounting().getTradeBookIntentTypeCode());
                    tradeAcct.setTradeTaxIntentTypeCode(trade.getTradeAccounting().getTradeTaxIntentTypeCode());
                    mbsTrade.setTradeAccounting(tradeAcct);
                }
                mbsTrade.setTradeAssignmentIndicator(trade.getTradeAssignmentIndicator().charAt(0));
                if (trade.getTradeBalance() != null) {
                    TradeBalance tradeBal = new TradeBalance();
                    tradeBal.setContractualUnsettledBalanceAmount(
                            trade.getTradeBalance().getContractualUnsettledBalanceAmount());
                    tradeBal.setEarlyFundingAllocatedAmount(trade.getTradeBalance().getEarlyFundingAllocatedAmount());
                    tradeBal.setEarlyFundingSettledAmount(trade.getTradeBalance().getEarlyFundingSettledAmount());
                    tradeBal.setNettedAmount(trade.getTradeBalance().getNettedAmount());
                    tradeBal.setNonEarlyFundingAllocatedAmount(
                            trade.getTradeBalance().getNonEarlyFundingAllocatedAmount());
                    tradeBal.setNonEarlyFundingSettledAmount(trade.getTradeBalance().getNonEarlyFundingSettledAmount());
                    tradeBal.setOpenBalanceAmount(trade.getTradeBalance().getOpenBalanceAmount());
                    tradeBal.setPoolNettedAmount(trade.getTradeBalance().getPoolNettedAmount());
                    tradeBal.setPoolPairoffAmount(trade.getTradeBalance().getPoolPairOffAmount());
                    tradeBal.setToBeAnnouncedPairoffAmount(trade.getTradeBalance().getToBeAnnouncedPairoffAmount());
                    tradeBal.setUnsettledBalanceAmount(trade.getTradeBalance().getUnsettledBalanceAmount());
                    tradeBal.setToBeAnnouncedRepriceAmount(trade.getTradeBalance().getToBeAnnouncedRepriceAmount());
                    mbsTrade.setTradeBalance(tradeBal);
                }
                mbsTrade.setTradeDate(Objects.equal(trade.getTradeDate(), null) ? null
                        : MBSPortalUtils.convertGregorianToDateNoTime(trade.getTradeDate()));
                if (trade.getTradeParty() != null) {
                    TradeParty tradeParty = new TradeParty();
                    tradeParty.setPartyEnterpriseId(trade.getTradeParty().getEnterprisePartyId());
                    tradeParty.setPartyId(trade.getTradeParty().getPartyId());
                    tradeParty.setPartyRoleTypeCode(trade.getTradeParty().getPartyRoleTypeCode());
                    tradeParty.setSourceId(trade.getTradeParty().getSourceId());
                    tradeParty.setPartyShortName(trade.getTradeParty().getPartyShortName());
                    mbsTrade.setTradeParty(tradeParty);
                }
                if (trade.getTradePayment() != null) {
                    TradePayment tradePymt = new TradePayment();
                    tradePymt.setFeeAmount(trade.getTradePayment().getFeeAmount());
                    tradePymt.setFeeTypeCode(trade.getTradePayment().getFeeTypeCode());
                    tradePymt.setInterestAmount(trade.getTradePayment().getInterestAmount());
                    tradePymt.setPrincipalAmount(trade.getTradePayment().getPrincipalAmount());
                    mbsTrade.setTradePayment(tradePymt);
                }
                if (trade.getTradeProduct() != null) {
                    TradeProduct tradeProduct = new TradeProduct();
                    tradeProduct.setCollateralHousingTypeCode(trade.getTradeProduct().getCollateralHousingTypeCode());
                    tradeProduct.setFinancialInstrumentInterestBehaviorCode(
                            trade.getTradeProduct().getFinancialInstrumentInterestBehaviorCode());
                    tradeProduct.setFinancialInstrumentMaturityTerm(
                            trade.getTradeProduct().getFinancialInstrumentMaturityTerm().intValue());
                    tradeProduct.setSecurityCategoryTypeCode(trade.getTradeProduct().getSecurityCategoryTypeCode());
                    tradeProduct.setSecurityIssuerCode(trade.getTradeProduct().getSecurityIssuerCode());
                    tradeProduct.setShortDescription(trade.getTradeProduct().getShortDescription());
                    mbsTrade.setTradeProduct(tradeProduct);
                }
                mbsTrade.setTraderInitialsText(trade.getTraderInitialsText());
                mbsTrade.setTransactionAgreementAmount(trade.getTransactionAgreementAmount());
                mbsTrade.setTransactionAgreementPricePercent(trade.getTransactionAgreementPricePercent());
                mbsTrade.setTransactionAgreementTypeCode(trade.getTransactionAgreementTypeCode());
                mbsTrade.setTransactionBuySellCode(trade.getTransactionBuySellCode());
                mbsTrade.setVariancePercent(trade.getVariancePercent());
                if (trade.getTradeAttributes() != null && trade.getTradeAttributes().size() > 0) {
                    boolean alreadyFound = false;
                    for (TradeAttribute srcTradeAttr : trade.getTradeAttributes()) {
                        com.fanniemae.securitiesods.ods_core.domain.TradeAttribute tradeAttr = new com.fanniemae.securitiesods.ods_core.domain.TradeAttribute();
                        tradeAttr.setDataTypeCode(srcTradeAttr.getDataTypeCode());
                        tradeAttr.setName(srcTradeAttr.getName());
                        tradeAttr.setOperatorCode(srcTradeAttr.getOperatorCode());
                        tradeAttr.setTradeAttributeId(srcTradeAttr.getTradeAttributeId());
                        tradeAttr.setTypeCode(srcTradeAttr.getTypeCode());
                        tradeAttr.setValue(srcTradeAttr.getValue());
                        LOGGER.debug("ECFTransformerImpl: Attribute sourceSystem:" + sourceSystem);
                        LOGGER.debug("ECFTransformerImpl: Attribute Name:" + tradeAttr.getName());
                        LOGGER.debug("ECFTransformerImpl: Attribute Value:" + tradeAttr.getValue());
                        // Setting the trans req number
                        if (!alreadyFound
                                && tradeAttr.getName().equalsIgnoreCase(MBSAdapterConstants.MBS_TRANS_REQ_ID)) {
                            if (StringUtils.isNotEmpty(tradeAttr.getValue())) {

                                if (tradeAttr.getValue().length() > sourceSystem.length()
                                        && tradeAttr.getValue().startsWith(sourceSystem)) {

                                    mbsTrade.setTransReqNumber(tradeAttr.getValue().substring(sourceSystem.length()).trim());
                                    LOGGER.debug("ECFTransformerImpl: transform: TransReqNumber:" + mbsTrade.getTransReqNumber());
                                    alreadyFound = true;
                                } else {
                                    LOGGER.debug("Skipping the Message for the TransReqNumber: " + tradeAttr.getValue());
                                }
                            } else {
                                LOGGER.error("Trade attribute for key MBS_TRANS_ID is empty");
                                throw new MBSDataAccessException("ECFTransformerImpl: Trade attribute for key MBS_TRANS_ID is empty");
                            }
                        }
                        mbsTrade.getTradeAttributes().add(tradeAttr);
                    }
                }
            } else {
                LOGGER.error("ECFTransformerImpl: Trade Message from BlackRock is null ");
                throw new MBSDataAccessException("ECFTransformerImpl: Trade Message from BlackRock is null");
            }
        } catch (MBSBaseException ex) {
            
            LOGGER.error("ECFTransformerImpl: Error when converting Trade Message from BlackRock to MBSTrade :", ex);
            throw ex;
            
        } catch (Exception ex) {
            
            LOGGER.error("ECFTransformerImpl: Error when converting Trade Message from BlackRock to MBSTrade : ", ex);
            throw new MBSDataAccessException( "ECFTransformerImpl: Error when converting Trade Message from BlackRock to MBSTrade");
        }
        return mbsTrade;
    }

}
