/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.validator;

import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.PartyPO;
import com.fanniemae.mbsportal.api.po.TspPartyLenderPO;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.validator.BaseValidator;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 22, 2018
 * @File: com.fanniemae.mbsportal.api.validator.PartyValidator.java
 * @Revision:
 * @Description: PartyValidator.java
 */
@Component
public class PartyValidator<T extends TransformationObject> extends BaseValidator<T> {

    /**
     * 
     * LOGGER Logger variable
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(PartyValidator.class);

    /**
     * 
     * Purpose: This method validates the PartyPO object
     * 
     * @param transformationObject
     *            TransformationObject
     * @throws MBSBaseException
     */
    @Override
    public void validate(TransformationObject transformationObject) throws MBSBaseException {

        LOGGER.debug("Entering validate method in PartyValidator");

        PartyPO partyPO = (PartyPO) transformationObject.getSourcePojo();

        if (Objects.isNull(partyPO)){
            LOGGER.error("Bad Request: Party Mandatory Missing/empty.");
            throw new MBSBusinessException("Bad Request: Party Mandatory Missing/empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        // sellerServicerNumber
        if (!StringUtils.isNumeric(partyPO.getSellerServicerNumber())) {
            LOGGER.error("Bad Request: Party Mandatory sellerServicerNumber Missing/empty.");
            throw new MBSBusinessException("Bad Request: Party Mandatory sellerServicerNumber Missing/empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        // name
        if (StringUtils.isBlank(partyPO.getName())) {
            LOGGER.error("Bad Request: Party Mandatory name Missing/empty.");
            throw new MBSBusinessException("Bad Request: Party name Missing/empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        // shortName
        if (StringUtils.isBlank(partyPO.getShortName())) {
            LOGGER.error("Bad Request: Party Mandatory shortName Missing/empty.");
            throw new MBSBusinessException("Bad Request: Party shortName Missing/empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        // effectiveDate
        if ( StringUtils.isBlank(partyPO.getEffectiveDate())) {
            LOGGER.error("Bad Request: Party Mandatory effectiveDate Missing/empty.");
            throw new MBSBusinessException("Bad Request: Party effectiveDate Missing/empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        // nameEffectiveDate
        if (StringUtils.isBlank(partyPO.getNameEffectiveDate())) {
            LOGGER.error("Bad Request: Party Mandatory nameEffectiveDate Missing/empty.");
            throw new MBSBusinessException("Bad Request: Party nameEffectiveDate Missing/empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        // stateType
        if (StringUtils.isBlank(partyPO.getStateType())) {
            LOGGER.error("Bad Request: Party Mandatory stateType Missing/empty.");
            throw new MBSBusinessException("Bad Request: Party stateType Missing/empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        // institutionType
        if (StringUtils.isBlank(partyPO.getInstitutionType())) {
            LOGGER.error("Bad Request: Party Mandatory institutionType Missing/empty.");
            throw new MBSBusinessException("Bad Request: Party institutionType Missing/empty.",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        // mBSPartyLenders
        if (CollectionUtils.isNotEmpty(partyPO.getTspPartyLenders())) {
            for (TspPartyLenderPO tspPartyLenderPO : partyPO.getTspPartyLenders()) {
                
                // lenderSellerServicerNumber
                if (!StringUtils.isNumeric(""+tspPartyLenderPO.getLenderSellerServicerNumber())) {
                    LOGGER.error("Bad Request: Party Lender Mandatory lenderSellerServicerNumber Missing/empty.");
                    throw new MBSBusinessException("Bad Request: Party Lender lenderSellerServiceNumber Missing/empty.",
                            MBSExceptionConstants.BUSINESS_EXCEPTION);
                }
                // effectiveDate
                if (StringUtils.isBlank(tspPartyLenderPO.getEffectiveDate())) {
                    LOGGER.error("Bad Request: Party Lender Mandatory effectiveDate Missing/empty.");
                    throw new MBSBusinessException("Bad Request: Party Lender effectiveDate Missing/empty.",
                            MBSExceptionConstants.BUSINESS_EXCEPTION);
                }
                //name
                if (StringUtils.isBlank(tspPartyLenderPO.getName())) {
                    LOGGER.error("Bad Request: Party Lender Mandatory name Missing/empty.");
                    throw new MBSBusinessException("Bad Request: Party Lender name Missing/empty.",
                            MBSExceptionConstants.BUSINESS_EXCEPTION);
                }
                //shortName
                if (StringUtils.isBlank(tspPartyLenderPO.getShortName())) {
                    LOGGER.error("Bad Request: Party Lender Mandatory shortName Missing/empty.");
                    throw new MBSBusinessException("Bad Request: Party Lender shortName Missing/empty.",
                            MBSExceptionConstants.BUSINESS_EXCEPTION);
                }
            }
        }
    }
}
