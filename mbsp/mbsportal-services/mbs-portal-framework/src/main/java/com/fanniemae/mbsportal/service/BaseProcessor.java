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

package com.fanniemae.mbsportal.service;

import java.util.Objects;

import org.apache.geode.cache.CacheTransactionManager;
import org.apache.geode.cache.CommitConflictException;
import org.apache.geode.cache.TransactionDataNodeHasDepartedException;
import org.apache.geode.cache.TransactionDataRebalancedException;
import org.apache.geode.cache.TransactionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fanniemae.fnmpfj.gemfire.utils.client.GemfireUtil;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.discovery.Discovery;
import com.fanniemae.mbsportal.enrichment.Enricher;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.publisher.MessagePublisher;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.validator.Validator;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Date: Apr 16, 2018
 * @File: com.fanniemae.mbsportal.service.BaseProcessor.java
 * @Revision : 
 * @Description: Added Publisher and PO Enricher
 */
public class BaseProcessor<T extends TransformationObject> implements Processor<T> {
    public BaseProcessor() {

    }

    @SuppressWarnings("rawtypes")
    public BaseProcessor(Discovery discovery, Transformer transformer, Enricher enricher, Validator validator,
            Persister persister, Validator domainValidator, Transformer poTransformer,
            Enricher poEnricher, MessagePublisher messagePublisher) {
        this.discovery = discovery;
        this.transformer = transformer;
        this.enricher = enricher;
        this.validator = validator;
        this.persister = persister;
        this.domainValidator = domainValidator;
        this.poTransformer = poTransformer;
        this.poEnricher = poEnricher;
        this.messagePublisher = messagePublisher;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseProcessor.class);

    @SuppressWarnings("rawtypes")
    protected Discovery discovery;
    @SuppressWarnings("rawtypes")
    protected Transformer transformer;
    @SuppressWarnings("rawtypes")
    protected Enricher enricher;
    @SuppressWarnings("rawtypes")
    protected Validator validator;
    @SuppressWarnings("rawtypes")
    protected Persister persister;
    @SuppressWarnings("rawtypes")
    protected Validator domainValidator;
    @SuppressWarnings("rawtypes")
    protected Transformer poTransformer;
    @SuppressWarnings("rawtypes")
    protected Enricher poEnricher;
    @SuppressWarnings("rawtypes")
    protected MessagePublisher messagePublisher;

    /**
     * Process Request
     * 
     * @param transformationObject
     */
    @SuppressWarnings({ "unchecked" })
    public void processRequest(T transformationObject) throws MBSBaseException {
        CacheTransactionManager cacheTransactionManager = null;
        try {
            LOGGER.debug("Starting Gemfire Transaction for MBS pipeline");
            // Begin gemfire transaction
            cacheTransactionManager = GemfireUtil.getTransactionManager();

            if (!Objects.equals(validator, null)) {
                validator.validate(transformationObject);
            }
            if (!Objects.equals(discovery, null)) {
                discovery.discover(transformationObject);
            }
            if (!Objects.equals(transformer, null)) {
                transformer.transform(transformationObject);
            }
            if (!Objects.equals(enricher, null)) {
                enricher.enrich(transformationObject);
            }
            if (!Objects.equals(domainValidator, null)) {
                domainValidator.validate(transformationObject);
            }
            if (!Objects.equals(persister, null)) {
                persister.persist(transformationObject);
            }
            if (!Objects.equals(poTransformer, null)) {
                poTransformer.transform(transformationObject);
            }
            if (!Objects.equals(poEnricher, null)) {
                poEnricher.enrich(transformationObject);
            }
            if (!Objects.equals(messagePublisher, null)) {
                /*
                 * If it is Lender Activity then do not publish for Lender - Trader will get informed from Query Listener Event  
                 * If it is Trader Activity then assign the role as Lender so that Actual Publishing using Streaming server happen for Lender 
                 * and for Trader it will be Query Listener Event  
                 */
                if(Objects.nonNull(filterTransObjectForLender(transformationObject))){
                    TransformationObject transformationObjectPublish= new TransformationObject();
                    transformationObjectPublish.setMBSRoleType(MBSRoleType.LENDER);
                    transformationObjectPublish.setTargetPojo(transformationObject.getTargetPojo());
                    messagePublisher.publish(transformationObjectPublish);
                }
            }

        } catch (CommitConflictException | TransactionDataRebalancedException
                | TransactionDataNodeHasDepartedException cce) {
            rollbackTransaction(cacheTransactionManager);
            LOGGER.warn("Exception occurred - {} - Rolled back Gemfire Transaction for the MBS "
                    + "pipeline while processing transformationObject", cce.getMessage());
            throw new MBSBaseException(cce);
        }
        LOGGER.debug("processSourceDataRecord() - End of while loop");
    }

    /**
     *
     * @param cacheTransactionManager
     */
    public static void rollbackTransaction(CacheTransactionManager cacheTransactionManager) {
        if (cacheTransactionManager != null && cacheTransactionManager.exists()) {
            cacheTransactionManager.rollback();
        }
    }

    /**
     * Begin transaction
     * 
     * @param cacheTransactionManager
     */
    public static void beginTransaction(CacheTransactionManager cacheTransactionManager) {
        if (null != cacheTransactionManager && cacheTransactionManager.exists()) {
            cacheTransactionManager.begin();
            LOGGER.debug("Gemfire Transaction begin for MBS pipeline");
        }
    }

    /**
     * Handle transaction exception
     * 
     * @param cacheTransactionManager
     * @param e
     * @throws MBSBaseException
     */
    public static void handleTransactionException(CacheTransactionManager cacheTransactionManager, Exception e)
            throws MBSBaseException {

        if (cacheTransactionManager != null && cacheTransactionManager.exists()) {
            cacheTransactionManager.rollback();
            LOGGER.warn("Exception occurred - {} - Rolled back Gemfire Transaction for the MBS ");
        }
        throw new MBSBaseException(e);
    }

    /**
     * Commit transation
     * 
     * @param cacheTransactionManager
     */
    public static void commitTransaction(CacheTransactionManager cacheTransactionManager) {
        if (null != cacheTransactionManager && cacheTransactionManager.exists()) {
            cacheTransactionManager.commit();
            LOGGER.debug("Gemfire Transaction committed for ODS pipeline");
        }

    }

    /**
     * Suspend transation
     * 
     * @param cacheTransactionManager
     * @param transformationObject
     */
    public TransactionId suspendTransaction(CacheTransactionManager cacheTransactionManager,
            TransformationObject transformationObject) throws Exception {
        return null;
    }

    /**
     * resume transaction
     * 
     * @param cacheTransactionManager
     * @param transformationObject
     */
    public void resumeTransaction(CacheTransactionManager cacheTransactionManager,
            TransformationObject transformationObject, TransactionId transactionId) throws Exception {
    }
    
    /**
     * 
     * @param transformationObject
     * @return TransformationObject
     */
    public TransformationObject filterTransObjectForLender(TransformationObject transformationObject){
        
        //Filter and Publish only to Lender
        MBSTransactionRequest mbsTransReq = (MBSTransactionRequest) transformationObject.getTargetPojo();
        if(Objects.nonNull(mbsTransReq)){
            StateType reqStateType = StateType.getEnum(mbsTransReq.getStateType());
            //Lender Open and active version 1 dont publish, Publish when active version is other than 1 
            if(reqStateType.equals(StateType.LENDER_OPEN) && mbsTransReq.getActiveVersion().intValue() == 1){
                return null;
            } else if( reqStateType.equals(StateType.LENDER_ACCEPTED) ||
                    reqStateType.equals(StateType.LENDER_REJECTED)){
                return null;
            }
        } else {
            return null;            
        }
        return transformationObject;
        
    }

}
