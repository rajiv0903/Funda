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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.enrichment.BaseEnricher;
import com.fanniemae.mbsportal.model.MBSParty;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.streaming.prop.StreamingClientProperties;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 22, 2018
 * @File: com.fanniemae.mbsportal.api.enrichment.PartyEnrichment.java 
 * @Revision: 
 * @Description: PartyEnrichment.java
 */
@Component
public class PartyEnrichment<T extends TransformationObject> extends BaseEnricher<T> {

    /**
     * 
     * LOGGER Logger variable
     */
     private static final Logger LOGGER = LoggerFactory.getLogger(PartyEnrichment.class);
    
    /**
     * 
     * mbspProperties MbspProperties
     */
    @Autowired
    MbspProperties mbspProperties;

    /**
     * 
     * streamingClientProperties StreamingClientProperties
     */
    @Autowired
    StreamingClientProperties streamingClientProperties;
    
    /**
     * 
     * 
     * @param obj the TransformationObject
     * @throws MBSBaseException
     */
    @Override
    public void enrich(TransformationObject transformationObject) throws MBSBaseException {
        LOGGER.debug("Entering enrich method in PartyEnricher");
        try {
            MBSParty mBSParty  = enrichTargetObject(transformationObject);
            transformationObject.setTargetPojo(mBSParty);
        } catch (MBSBaseException e) {
            LOGGER.error("Exception when enriching data", e);
            throw e;
        }
        LOGGER.debug("Exiting enrich method in PartyEnricher");
    }

    /**
     * 
     * enrichTargetObject
     * 
     * @param transformationObject the TransformationObject
     * @return MBSParty
     * @throws MBSBaseException
     */
    MBSParty enrichTargetObject(TransformationObject transformationObject) throws MBSBaseException {
        LOGGER.debug("Entering enrichTargetObject method in PartyEnricher");
        MBSParty mBSParty = null;

        try {
            mBSParty = (MBSParty) transformationObject.getTargetPojo();

            //CNAME default url 
            if(StringUtils.isBlank(mBSParty.getMbspPortalCnameUrlBase())){
                mBSParty.setMbspPortalCnameUrlBase(mbspProperties.getDirectApiUrl());
            }
            //streaming default url
            if(StringUtils.isBlank(mBSParty.getMbspStreamingUrlBase())){
                mBSParty.setMbspStreamingUrlBase(streamingClientProperties.getWebSocketUrl());
            }

        } catch (Exception ex) {
            LOGGER.error("Exception when enriching party info, enrichTargetObject", ex);
            throw new MBSSystemException("Exception when enriching party info, enrichToPartyObject",
                    MBSExceptionConstants.SYSTEM_EXCEPTION, ex);
        }
        return mBSParty;
    }
    
   
}
