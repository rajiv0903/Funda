/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.api.enrichment;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.api.po.TspPartyLenderPO;
import com.fanniemae.mbsportal.api.service.ProfileEntitlementService;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.constants.TradeBuySell;
import com.fanniemae.mbsportal.enrichment.BaseEnricher;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;

/**
 * @author g8uaxt Created on 12/28/2017.
 */
@Component
public class TransactionRequestEnrichment<T extends TransformationObject> extends BaseEnricher<T> {
    
    /**
     * 
     * profileEntitlementService ProfileEntitlementService
     */
    @Autowired
    @Lazy
    ProfileEntitlementService profileEntitlementService;
    /**
     * 
     * LOGGER Logger variable
     */
    @InjectLog
    private Logger LOGGER;
    
    /**
     * 
     * 
     * @param transformationObject the TransformationObject
     * @throws MBSBaseException
     */
    @Override
    public void enrich(TransformationObject transformationObject) throws MBSBaseException {
        LOGGER.debug("Entering enrich method in TransactionRequestEnrichment");
        try {
            MBSTransactionRequest mbsTransactionRequest = enrichTargetObject(transformationObject);
            transformationObject.setTargetPojo(mbsTransactionRequest);
        } catch (MBSBaseException e) {
            LOGGER.error("Exception when enriching data", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Exception when enriching data", e);
            throw new MBSSystemException("TransactionRequestEnrichment failed", MBSExceptionConstants.SYSTEM_EXCEPTION,
                    e);
        }
        LOGGER.debug("Exiting enrich method in TransactionRequestEnrichment");
    }

    /**
     * 
     * enrichTargetObject
     * 
     * @param transformationObject the TransformationObject
     * @return MBSTransactionRequest
     * @throws MBSBaseException
     */
    MBSTransactionRequest enrichTargetObject(TransformationObject transformationObject) throws MBSBaseException {
        LOGGER.debug("Entering enrichTargetObject method in TransactionRequestEnrichment");
        MBSTransactionRequest mbsTransactionRequest = null;

        try {
            TransactionRequestPO transactionRequestPO = (TransactionRequestPO) transformationObject.getSourcePojo();
            mbsTransactionRequest = (MBSTransactionRequest) transformationObject.getTargetPojo();
            // tranReqId null??
            if (transactionRequestPO.getLenderId() != null && mbsTransactionRequest.getTransReqNumber() == null) {
                ProfileEntitlementPO profileEntitlementPO = profileEntitlementService
                        .getProfile(transactionRequestPO.getLenderId());
                //CMMBSSTA01-1371 - Changes for TSP - Start
                if(transformationObject.getMBSRoleType().equals(MBSRoleType.TSP)) {
                    mbsTransactionRequest.setTspShortName(profileEntitlementPO.getPartyShortName());
                  //CMMBSSTA01-1373 - Adding TSP name - Start
                    mbsTransactionRequest.setTspSellerServiceNumber(profileEntitlementPO.getSellerServicerNumber());
                  //CMMBSSTA01-1373 - Adding TSP name - End
                    List<TspPartyLenderPO> tspLenders = profileEntitlementPO.getTspLenders();
                    for(TspPartyLenderPO tspPartyLenderPO : tspLenders){
                        if(transactionRequestPO.getOboLenderSellerServicerNumber().equalsIgnoreCase(tspPartyLenderPO.getLenderSellerServicerNumber())){
                            mbsTransactionRequest.setDealerOrgName(tspPartyLenderPO.getName());
                            //CMMBSSTA01-1373 - Adding TSP name - Start
                            mbsTransactionRequest.setLenderShortName(tspPartyLenderPO.getShortName());
                          //CMMBSSTA01-1373 - Adding TSP name - End
                            break;
                        }
                    }
                } else {
                    mbsTransactionRequest.setDealerOrgName(profileEntitlementPO.getDealerOrgName());
                    //CMMBSSTA01-1373 - Start
                    mbsTransactionRequest.setLenderShortName(profileEntitlementPO.getPartyShortName());
                    mbsTransactionRequest.setLenderSellerServiceNumber(profileEntitlementPO.getSellerServicerNumber());
                  //CMMBSSTA01-1373 - End
                }
                //CMMBSSTA01-1371 - Changes for TSP - End
                mbsTransactionRequest.setCounterpartyTraderName(profileEntitlementPO.getFirstName()
                        + MBSPServiceConstants.FIRSTNAME_LASTNAME_SEPERATOR + profileEntitlementPO.getLastName());
                mbsTransactionRequest.setCounterPartyBuySellType(transactionRequestPO.getTradeBuySellType());
                // for trader
                if (TradeBuySell.BUY.name().equals(transactionRequestPO.getTradeBuySellType())) {
                    mbsTransactionRequest.setTradeBuySellType(TradeBuySell.SELL.name());
                } else {
                    mbsTransactionRequest.setTradeBuySellType(TradeBuySell.BUY.name());
                }
                mbsTransactionRequest.setProductNameCode(transactionRequestPO.getProduct().getNameCode());
            } else if (transactionRequestPO.getTraderId() == null) {
                // unlock trade for other traders
                if (mbsTransactionRequest.getTraderName() != null
                        && transactionRequestPO.getStateType().equals(StateType.LENDER_OPEN)) {
                    mbsTransactionRequest.setTraderName(null);
                }
            } else if (transactionRequestPO.getTraderId() != null) {
                ProfileEntitlementPO profileEntitlementPO = profileEntitlementService
                        .getProfile(transactionRequestPO.getTraderId());
                mbsTransactionRequest.setTraderName(profileEntitlementPO.getFirstName()
                        + MBSPServiceConstants.FIRSTNAME_LASTNAME_SEPERATOR + profileEntitlementPO.getLastName());
            } 
            mbsTransactionRequest.setStateTypeOrder(new Integer(transactionRequestPO.getStateType().getSortOrder()));

        } catch (Exception ex) {
            LOGGER.error("Exception when enriching history info, enrichToHistoryObject", ex);
            throw new MBSSystemException("Exception when enriching history info, enrichToHistoryObject",
                    MBSExceptionConstants.SYSTEM_EXCEPTION, ex);
        }
        return mbsTransactionRequest;
    }
    
    /**
     * 
     * 
     * @param profileEntitlementService ProfileEntitlementService
     * 
     */
    public void setProfileEntitlementService(ProfileEntitlementService profileEntitlementService) {
        this.profileEntitlementService = profileEntitlementService;
    }
}
