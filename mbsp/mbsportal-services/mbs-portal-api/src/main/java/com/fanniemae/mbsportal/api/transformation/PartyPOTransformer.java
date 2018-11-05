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
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.PartyPO;
import com.fanniemae.mbsportal.api.po.TspPartyLenderPO;
import com.fanniemae.mbsportal.model.MBSParty;
import com.fanniemae.mbsportal.model.TspPartyLender;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.transformation.BaseTransformer;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 22, 2018
 * @File: com.fanniemae.mbsportal.api.transformation.PartyPOTransformer.java 
 * @Revision: 
 * @Description: PartyPOTransformer.java
 */
@Component
public class PartyPOTransformer<T extends TransformationObject> extends BaseTransformer<T> {

    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PartyPOTransformer.class);

    /**
     * 
     * This method does the transformation of object from domain to PO
     * 
     * @param transformationObject
     *            TransformationObject
     * @return TransformationObject
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    @Override
    public TransformationObject transform(TransformationObject transformationObject) throws MBSBaseException {

        LOGGER.debug("Entering transform method in PartyPOTransformer");
        MBSParty mBSParty = (MBSParty) transformationObject.getTargetPojo();
        PartyPO partyPO = convertToPartyPO(mBSParty);
        transformationObject.setSourcePojo(partyPO);
        LOGGER.debug("Exiting transform method in PartyPOTransformer");
        return transformationObject;
    }

    /**
     * 
     * Purpose: This does the conversion from MBSParty to PartyPO object
     *
     * @param mBSParty
     *            The TransactionRequest object
     * @return PartyPO The presentation object of MBSParty object
     * @throws MBSBaseException
     */
    private PartyPO convertToPartyPO(MBSParty mBSParty) throws MBSBaseException {
        PartyPO partyPO = new PartyPO();

        partyPO.setSellerServicerNumber(mBSParty.getSellerServicerNumber());
        partyPO.setName(mBSParty.getName());
        partyPO.setShortName(mBSParty.getShortName());
        partyPO.setEffectiveDate(MBSPortalUtils.convertDateToString(mBSParty.getEffectiveDate(), DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        if(!Objects.isNull(mBSParty.getExpirationDate())){
            partyPO.setExpirationDate(MBSPortalUtils.convertDateToString(mBSParty.getExpirationDate(),DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        }
        partyPO.setNameEffectiveDate(MBSPortalUtils.convertDateToString(mBSParty.getNameEffectiveDate(),DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        if(!Objects.isNull(mBSParty.getNameExpirationDate())){
            partyPO.setNameExpirationDate(MBSPortalUtils.convertDateToString(mBSParty.getNameExpirationDate(),DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        }
        partyPO.setStateType(mBSParty.getStateType());
        partyPO.setInstitutionType(mBSParty.getInstitutionType());

        List<TspPartyLenderPO> tspPartyLenders = partyPO.getTspPartyLenders();

        if (CollectionUtils.isNotEmpty(mBSParty.getTspPartyLenders())) {
            tspPartyLenders = new ArrayList<>();

            for (TspPartyLender tspPartyLender : mBSParty.getTspPartyLenders()) {

                TspPartyLenderPO tspPartyLenderPO = new TspPartyLenderPO(
                        tspPartyLender.getLenderSellerServicerNumber(),
                        MBSPortalUtils.convertDateToString(tspPartyLender.getEffectiveDate(),  DateFormats.DATE_FORMAT_NO_TIMESTAMP),
                        !Objects.isNull(tspPartyLender.getExpirationDate())? MBSPortalUtils.convertDateToString(tspPartyLender.getExpirationDate(),  DateFormats.DATE_FORMAT_NO_TIMESTAMP):null,
                        tspPartyLender.getName(),
                        tspPartyLender.getShortName());
                
                tspPartyLenders.add(tspPartyLenderPO);
            }
        }
        partyPO.setTspPartyLenders(tspPartyLenders);

        partyPO.setMbspPortalCnameUrlBase(mBSParty.getMbspPortalCnameUrlBase());
        partyPO.setMbspStreamingUrlBase(mBSParty.getMbspStreamingUrlBase());
        // end

        LOGGER.debug(partyPO.toString());

        return partyPO;
    }
}
