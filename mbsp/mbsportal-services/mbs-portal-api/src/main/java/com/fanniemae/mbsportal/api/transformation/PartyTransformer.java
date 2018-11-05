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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
 * @File: com.fanniemae.mbsportal.api.transformation.PartyTransformer.java 
 * @Revision: 
 * @Description: PartyTransformer.java
 */
@Component
public class PartyTransformer<T extends TransformationObject> extends BaseTransformer<T> {

    /**
     * 
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PartyTransformer.class);

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
        LOGGER.debug("Entering transform method in PartyTransformer");
        PartyPO partyPO = (PartyPO) transformationObject.getSourcePojo();
        MBSParty mBSParty = (MBSParty) transformationObject.getTargetPojo();

        // Transform the Presentation object to domain object
        mBSParty = convertToModel(partyPO, mBSParty);
        transformationObject.setTargetPojo(mBSParty);
        LOGGER.debug("Exiting transform method in PartyTransformer");
        return transformationObject;
    }

    /**
     * 
     * Convert to Model object from PartyPO
     * 
     * @param partyPO  PartyPO
     * @return mBSParty MBSParty
     * @throws MBSBaseException
     */
    private MBSParty convertToModel(PartyPO partyPO, MBSParty mBSParty) throws MBSBaseException {
        
        mBSParty.setSellerServicerNumber(partyPO.getSellerServicerNumber());
        mBSParty.setName(partyPO.getName());
        mBSParty.setShortName(partyPO.getShortName());
        mBSParty.setEffectiveDate(MBSPortalUtils.convertToDateWithFormatter(partyPO.getEffectiveDate(), DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        if(StringUtils.isNotBlank(partyPO.getExpirationDate())){
            mBSParty.setExpirationDate(MBSPortalUtils.convertToDateWithFormatter(partyPO.getExpirationDate(), DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        }
        mBSParty.setNameEffectiveDate(MBSPortalUtils.convertToDateWithFormatter(partyPO.getNameEffectiveDate(), DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        if(StringUtils.isNotBlank(partyPO.getNameExpirationDate())){
            mBSParty.setNameExpirationDate(MBSPortalUtils.convertToDateWithFormatter(partyPO.getNameExpirationDate(), DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        }
        mBSParty.setStateType(partyPO.getStateType());
        mBSParty.setInstitutionType(partyPO.getInstitutionType());
        
        List<TspPartyLender> tspPartyLenders = mBSParty.getTspPartyLenders();
        
        if(CollectionUtils.isNotEmpty(partyPO.getTspPartyLenders())) {
            tspPartyLenders = new ArrayList<>();

            for (TspPartyLenderPO tspPartyLenderPO : partyPO.getTspPartyLenders()) {
                
                TspPartyLender tspPartyLender = new TspPartyLender(
                        tspPartyLenderPO.getLenderSellerServicerNumber(),
                        MBSPortalUtils.convertToDateWithFormatter(tspPartyLenderPO.getEffectiveDate(), DateFormats.DATE_FORMAT_NO_TIMESTAMP), 
                        StringUtils.isNotBlank(tspPartyLenderPO.getExpirationDate())? MBSPortalUtils.convertToDateWithFormatter(tspPartyLenderPO.getExpirationDate(), DateFormats.DATE_FORMAT_NO_TIMESTAMP):null,
                        partyPO.getSellerServicerNumber(),
                        tspPartyLenderPO.getName(),
                        tspPartyLenderPO.getShortName());
                
                //add to list
                tspPartyLenders.add(tspPartyLender);
            }
        }
        mBSParty.setTspPartyLenders(tspPartyLenders);
        
        mBSParty.setMbspPortalCnameUrlBase(partyPO.getMbspPortalCnameUrlBase());
        mBSParty.setMbspStreamingUrlBase(partyPO.getMbspStreamingUrlBase());
        

        return mBSParty;
    }
}
