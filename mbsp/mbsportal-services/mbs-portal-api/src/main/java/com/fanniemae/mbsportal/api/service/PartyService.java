package com.fanniemae.mbsportal.api.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.api.po.PartyPO;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.enrichment.Enricher;
import com.fanniemae.mbsportal.model.MBSParty;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import com.fanniemae.mbsportal.validator.Validator;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 22, 2018
 * @File: com.fanniemae.mbsportal.api.service.PartyService.java
 * @Revision:
 * @Description: PartyService.java
 */
@SuppressWarnings("rawtypes")
@Service
public class PartyService extends BaseProcessor {

    /**
     * 
     * LOGGER Logger
     */
    @InjectLog
    private Logger LOGGER;

    /**
     * 
     * mbsObjectCreator MBSObjectCreator
     */
    @Autowired
    MBSObjectCreator mbsObjectCreator;



    /**
     * 
     * 
     * @param partyValidator
     *            the partyValidator
     * @param partyTransformer
     *            the partyTransformer
     * @param partyEnrichment
     *            the partyEnrichment
     * @param partyPersister
     *            the partyPersister
     * @param partyPOTransformer
     *            the partyPOTransformer
     */
    @SuppressWarnings("unchecked")
    @Autowired
    public PartyService(
            Validator partyValidator, 
            Transformer partyTransformer,
            Enricher partyEnrichment,
            Persister partyPersister,
            Transformer partyPOTransformer) {

        super.validator = partyValidator;
        super.transformer = partyTransformer;
        super.enricher = partyEnrichment;
        super.persister = partyPersister;
        super.poTransformer = partyPOTransformer;
    }

    /**
     * 
     * @param partyPO the PartyPO
     * @return PartyPO
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    public PartyPO saveParty(PartyPO partyPO) throws MBSBaseException {

        LOGGER.debug("Entering saveParty method in PartyService");
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setSourcePojo(partyPO);

        MBSParty mBSParty = null;
        // Get the existing party for merging
        try {
            mBSParty = this.getMBSPartyFromRepo(partyPO.getSellerServicerNumber());

        } catch (Exception ex) {
            LOGGER.warn("saveParty falied for user : {} ", partyPO.getSellerServicerNumber());

        }
        if (mBSParty == null) {
            mBSParty = new MBSParty();
        }
        transformationObject.setTargetPojo(mBSParty);
        // end of merging
        super.processRequest(transformationObject);
        LOGGER.debug("Exiting saveParty method in PartyService");
        return (PartyPO) transformationObject.getSourcePojo();
    }

    /**
     * 
     * @param sellerSerivcerNumber the sellerSerivcerNumber
     * @return MBSParty
     * @throws MBSBaseException
     */
    protected MBSParty getMBSPartyFromRepo(String sellerSerivcerNumber) throws MBSBaseException {
        return (MBSParty) super.persister.getBaseDao().getById(sellerSerivcerNumber);
    }

    /**
     * 
     * @param sellerSerivcerNumber the sellerSerivcerNumber
     * @return PartyPO
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    public PartyPO getParty(String sellerSerivcerNumber) throws MBSBaseException {
        
        LOGGER.debug("Entering getParty method in PartyService");
        LOGGER.debug("Entering getParty method in PartyService: sellerSerivcerNumber:" + sellerSerivcerNumber);
        MBSParty mBSParty = this.getMBSPartyFromRepo(sellerSerivcerNumber);
        if (mBSParty == null) {
            throw new MBSBusinessException("No Party information for the sellerSerivcerNumber:" + sellerSerivcerNumber,
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        TransformationObject transformationObject = new TransformationObject();
        transformationObject.setTargetPojo(mBSParty);
        transformationObject = super.poTransformer.transform(transformationObject);
        LOGGER.debug("Exiting getParty method in PartyService");
        return (PartyPO) transformationObject.getSourcePojo();
    }
    
    /**
     * 
     * This method clears the Party records     * 
     * @throws MBSBaseException
     */
    public void clear(String sellerSerivcerNumber) throws MBSBaseException {
        super.persister.getBaseDao().removeById(sellerSerivcerNumber);
    }
    
    /**
     * 
     * This method clears the Party records     * 
     * @throws MBSBaseException
     */
    public void clearAll() throws MBSBaseException {
        persister.clearAll();
    }

}
