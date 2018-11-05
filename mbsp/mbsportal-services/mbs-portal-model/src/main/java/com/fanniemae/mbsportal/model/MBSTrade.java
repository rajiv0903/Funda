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

package com.fanniemae.mbsportal.model;

import com.fanniemae.securitiesods.ods_core.domain.Trade;

/**
 * Class Name: MBSTrade Purpose : This is the domain class for the Trade
 * 
 * @author g8upjv
 * 
 *         09/22/2017 g8upjv
 * 
 */
public class MBSTrade extends Trade {

    /**
     * Serial Id
     */
    private static final long serialVersionUID = 1610652364259699266L;

    // Transaction id
    private String transReqNumber;

    /**
     * @return the transReqNumber
     */
    public String getTransReqNumber() {
        return transReqNumber;
    }

    /**
     * @param transReqNumber
     *            the transReqNumber to set
     */
    public void setTransReqNumber(String transReqNumber) {
        this.transReqNumber = transReqNumber;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MBSTrade [transReqNumber=" + transReqNumber + ", tradeId=" + tradeId + ", eventId=" + eventId
                + ", currentFaceAmount=" + currentFaceAmount + ", subPortfolioShortName=" + subPortfolioShortName
                + ", contractualSettlementStateCode=" + contractualSettlementStateCode + ", clearingAgencyTypeCode="
                + clearingAgencyTypeCode + ", odsTransactionTypeCode=" + odsTransactionTypeCode + ", odsVersionNumber="
                + odsVersionNumber + ", sourceVersionNumber=" + sourceVersionNumber + ", tradeDate=" + tradeDate
                + ", buySellCode=" + buySellCode + ", amount=" + amount + ", primaryTransactionTypeCode="
                + primaryTransactionTypeCode + ", secondaryTransactionTypeCode=" + secondaryTransactionTypeCode
                + ", toBeAnnouncedStipulatedSpecifiedCode=" + toBeAnnouncedStipulatedSpecifiedCode + ", couponRate="
                + couponRate + ", settlementDate=" + settlementDate + ", pricePercent=" + pricePercent
                + ", nettingParticipationLevelTypeCode=" + nettingParticipationLevelTypeCode + ", variancePercent="
                + variancePercent + ", currencyTypeCode=" + currencyTypeCode + ", confirmationStateCode="
                + confirmationStateCode + ", settlementStateCode=" + settlementStateCode
                + ", approvedUnderDeliveryAmount=" + approvedUnderDeliveryAmount + ", approvedOverDeliveryAmount="
                + approvedOverDeliveryAmount + ", optionExpirationDate=" + optionExpirationDate + ", sourceId="
                + sourceId + ", sourcePrimaryTradeId=" + sourcePrimaryTradeId + ", cusipTypeCode=" + cusipTypeCode
                + ", userDefinedFieldText=" + userDefinedFieldText + ", sourceTransactionEntryDate="
                + sourceTransactionEntryDate + ", sourceTransactionEntryUserId=" + sourceTransactionEntryUserId
                + ", sourceTransactionLastUpdateDate=" + sourceTransactionLastUpdateDate
                + ", sourceTransactionLastUpdateUserId=" + sourceTransactionLastUpdateUserId + ", subPortfolioId="
                + subPortfolioId + ", productCusipId=" + productCusipId + ", tbaCusipId=" + tbaCusipId
                + ", executionTypeCode=" + executionTypeCode + ", optionEffectiveDate=" + optionEffectiveDate
                + ", securityDollarRollLegTypeCode=" + securityDollarRollLegTypeCode + ", tradeAssignmentIndicator="
                + tradeAssignmentIndicator + ", traderInitialsText=" + traderInitialsText
                + ", tradeAlternateIdentifiers=" + tradeAlternateIdentifiers + ", tradeAttributes=" + tradeAttributes
                + ", tradeComments=" + tradeComments + ", tradePayment=" + tradePayment + ", tradeBalance="
                + tradeBalance + ", tradeStipulations=" + tradeStipulations + ", tradeAccounting=" + tradeAccounting
                + ", tradeParties=" + tradeParties + ", tradeDraws=" + tradeDraws + ", tradeProduct=" + tradeProduct
                + ", sourceCommentForSTIPS=" + sourceCommentForSTIPS + ", mtgSubtype=" + mtgSubtype
                + ", securityUserDefinedFieldText=" + securityUserDefinedFieldText + ", securitySourceUpdateDate="
                + securitySourceUpdateDate + ", flagsText=" + flagsText + ", tradeConfirmedBy=" + tradeConfirmedBy
                + ", purposeText=" + purposeText + ", sourceUDF=" + sourceUDF + ", sourceUDFRecord=" + sourceUDFRecord
                + ", transactionAgreementId=" + transactionAgreementId + ", processId=" + processId
                + ", logicalDeleteIndicator=" + logicalDeleteIndicator + ", createdOn=" + createdOn + ", lastUpdated="
                + lastUpdated + ", lastUpdatedBy=" + lastUpdatedBy + ", createdBy=" + createdBy + "]";
    }

}
