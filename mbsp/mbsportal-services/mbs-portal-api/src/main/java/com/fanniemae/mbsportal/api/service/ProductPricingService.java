/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.api.enrichment.ProductPricingRequestEnrichment;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPricingPO;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.calendar.client.EnterpriseCalendarServiceClient;
import com.fanniemae.mbsportal.calendar.schema.v1.GetCalendarDayResponse;
import com.fanniemae.mbsportal.dao.MBSProductPricingDao;
import com.fanniemae.mbsportal.model.MBSProductPricingRequest;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import com.fanniemae.mbsportal.validator.Validator;

/**
 * 
 * @author g8uaxt Created on 9/11/2017.
 */
@SuppressWarnings("rawtypes")
@Service
public class ProductPricingService extends BaseProcessor {

    /**
     * 
     * LOGGER Logger variable
     */
    @InjectLog
    private Logger LOGGER;
    
    /**
     * 
     * productPricingRequestEnrichment This method does the post processes after the initialization
     */
    @Autowired
    private ProductPricingRequestEnrichment productPricingRequestEnrichment;
    
    /**
     * 
     * enterpriseCalendarServiceClient EnterpriseCalendarServiceClient
     */
    @Autowired
    EnterpriseCalendarServiceClient enterpriseCalendarServiceClient;
    
    /**
     * 
     * mbsObjectCreator MBSObjectCreator
     */
    @Autowired
    MBSObjectCreator mbsObjectCreator;
    
    /**
     * 
     * Constructor
     * 
     * @param productPricingPOValidator the productPricingPOValidator
     * @param productPricingRequestTransformer the productPricingRequestTransformer
     * @param productPricingPersister the productPricingPersister
     * @param productPricingPOTransformer the productPricingPOTransformer
     */
    @Autowired
    public ProductPricingService(Validator productPricingPOValidator, Transformer productPricingRequestTransformer,
            Persister productPricingPersister, Transformer productPricingPOTransformer) {
        super(null, productPricingRequestTransformer, null, productPricingPOValidator, productPricingPersister, null,
                productPricingPOTransformer,null, null);
    }

    /**
     * 
     * This method clears the Product Pricing records
     * 
     * @throws MBSBaseException
     */
    public void clearAll() throws MBSBaseException {
        // clear productPricing requests
        persister.clearAll();
        LOGGER.debug("cleared all productPricing  ...");
    }

    /**
     * 
     * Creates the product pricing details
     *
     * @param productPricingPORequest the productPricingPORequest
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    public void createProductPricing(List<ProductPricingPO> productPricingPORequest) throws MBSBaseException {
        LOGGER.debug("Entering createProductPricing method in ProductPricingService");
        TransformationObject transformationObject = mbsObjectCreator.getTransformationObject();
        // TODO: why?? transformationObject.setMBSRoleType(mbsRoleType);
        transformationObject.setSourcePojo(productPricingPORequest);
        //Call to calendar service and set the expiration date
        for(ProductPricingPO productPricingPO: productPricingPORequest){
        	if(StringUtils.isNotEmpty(productPricingPO.getSettlementDate())){
        		if(StringUtils.isEmpty(productPricingPO.getCutOffDate())){
	        		try {
	        			Date date = MBSPortalUtils.convertToDateWithFormatter(productPricingPO.getSettlementDate(), DateFormats.DATE_FORMAT_NO_TIMESTAMP);
		        		GetCalendarDayResponse getCalendarDayResponse = enterpriseCalendarServiceClient.getCalendarDay(date, MBSPServiceConstants.CUTTOFF_CALENDAR_TYPE, MBSPServiceConstants.CUTTOFF_DAYS, true, true);
		        		LOGGER.debug("Timezone for cuttoff date "+getCalendarDayResponse.getCalendarDays().get(0).getDate().toGregorianCalendar().getTimeZone());
		        		Date respDate =MBSPortalUtils.convertGregorianToDate(getCalendarDayResponse.getCalendarDays().get(0).getDate(), DateFormats.DATE_FORMAT_NO_TIMESTAMP);
		        		//Date respDate = getCalendarDayResponse.getCalendarDays().get(0).getDate().toGregorianCalendar().getTime();
		        		LOGGER.debug("Calendar date "+respDate+" for Settlement date"+productPricingPO.getSettlementDate());
		        		productPricingPO.setCutOffDate(MBSPortalUtils.convertDateToString(respDate, DateFormats.DATE_FORMAT_NO_TIMESTAMP));
	        		} catch (MBSBaseException ex) {
        	                    LOGGER.error("createProductPricing: get Calendar Service", ex);
        	                    throw ex;
        	                    
        	                } catch (Exception ex) {
        	                    LOGGER.error("createProductPricing: get Calendar Service", ex);
        	                    throw new MBSBaseException("Calendar Service error", ex);
        	                    
        	                }
        		}
        	} else {
        		LOGGER.error("Bad Request:Production Pricing Mandatory field settlement date Missing/empty.");
                throw new MBSBusinessException(
                        "Bad Request:Production Pricing Mandatory field settlement date Missing/empty.", MBSExceptionConstants.BUSINESS_EXCEPTION);
        	}
        }
        
        super.processRequest(transformationObject);
        LOGGER.debug("Exiting createProductPricing method in ProductPricingService");
    }

    /**
     *
     * The method gets all the product pricing details 
     *
     * @param productId the productId
     * @param filterDeletedRecord the filterDeletedRecord
     * @return List<ProductPricingPO>
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    public List<ProductPricingPO> getProductPricing(ProductIdPO productId, boolean filterDeletedRecord) throws MBSBaseException {
        
        List<ProductPricingPO> productPricingPOResponse = null;
        List<MBSProductPricingRequest> listMBSProductPricing = ((MBSProductPricingDao) persister.getBaseDao())
                .getAllByProductKey(productId.getIdentifier(), productId.getSourceType().name(),
                        productId.getType().name());
        for(MBSProductPricingRequest mbsProductPricingRequest: listMBSProductPricing){
        	if(Objects.isNull(mbsProductPricingRequest.getCutOffDate())){
        		try {
	        		GetCalendarDayResponse getCalendarDayResponse = enterpriseCalendarServiceClient.getCalendarDay(mbsProductPricingRequest.getSettlementDate(), MBSPServiceConstants.CUTTOFF_CALENDAR_TYPE, MBSPServiceConstants.CUTTOFF_DAYS, true, true);
	        		mbsProductPricingRequest.setCutOffDate(MBSPortalUtils.convertGregorianToDate(getCalendarDayResponse.getCalendarDays().get(0).getDate(), DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        		} catch (MBSBaseException ex) {
                            LOGGER.error("createProductPricing: get Calendar Service", ex);
                            throw ex;
                        } catch (Exception ex) {
                            LOGGER.error("createProductPricing: get Calendar Service", ex);
                            throw new MBSBaseException("Calendar Service error", ex);
                    }
    		//Commit the change
    		((MBSProductPricingDao)persister.getBaseDao()).saveOrUpdate(mbsProductPricingRequest);
        	} 
        }
        TransformationObject transformationObject = mbsObjectCreator.getTransformationObject();
        transformationObject.setFilterDeletedRecord(filterDeletedRecord); //Set whether to filter the record based on soft delete
        transformationObject.setTargetPojo(listMBSProductPricing);
        productPricingRequestEnrichment.enrich(transformationObject);
        poTransformer.transform(transformationObject);
        productPricingPOResponse = (List<ProductPricingPO>) transformationObject.getSourcePojo();
        return productPricingPOResponse;
    }

    /**
     *
     * This method gets all product pricing details
     * @param filterDeletedRecord the filterDeletedRecord
     * @return List<ProductPricingPO>
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    public List<ProductPricingPO> getAllProductPricing(boolean filterDeletedRecord) throws MBSBaseException {
        LOGGER.debug("Entering getAllProductPricing method in ProductPricingService");
        List<ProductPricingPO> productPricingPOResponse = null;
        List<MBSProductPricingRequest> listMBSProductPricing = persister.getBaseDao().getAll();
        for(MBSProductPricingRequest mbsProductPricingRequest: listMBSProductPricing){
        	if(Objects.isNull(mbsProductPricingRequest.getCutOffDate())){
        		try {
	        		GetCalendarDayResponse getCalendarDayResponse = enterpriseCalendarServiceClient.getCalendarDay(mbsProductPricingRequest.getSettlementDate(), MBSPServiceConstants.CUTTOFF_CALENDAR_TYPE, MBSPServiceConstants.CUTTOFF_DAYS, true, true);
	        		Date respDate = getCalendarDayResponse.getCalendarDays().get(0).getDate().toGregorianCalendar().getTime();
	        		mbsProductPricingRequest.setCutOffDate(MBSPortalUtils.convertGregorianToDate(getCalendarDayResponse.getCalendarDays().get(0).getDate(), DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        		} catch (MBSBaseException ex) {
                            LOGGER.error("createProductPricing: get Calendar Service", ex);
                            throw ex;
                            
                        } catch (Exception ex) {
                            LOGGER.error("createProductPricing: get Calendar Service", ex);
                            throw new MBSBaseException("Calendar Service error", ex);
                            
                        }
        		//Commit the change
        		((MBSProductPricingDao)persister.getBaseDao()).saveOrUpdate(mbsProductPricingRequest);
        	} 
        }
        TransformationObject transObj = mbsObjectCreator.getTransformationObject();
        transObj.setFilterDeletedRecord(filterDeletedRecord); //Set whether to filter the record based on soft delete
        transObj.setTargetPojo(listMBSProductPricing);
        productPricingRequestEnrichment.enrich(transObj);
        List<MBSProductPricingRequest> listMBSProductPricingFilterd = (List<MBSProductPricingRequest>) transObj
                .getTargetPojo();
        if (!Objects.equals(listMBSProductPricingFilterd, null)) {
            LOGGER.debug("Size of list: " + listMBSProductPricingFilterd.size());
            transObj.setTargetPojo(listMBSProductPricingFilterd);
            poTransformer.transform(transObj);
            if (transObj != null && transObj.getSourcePojo() != null) {
                productPricingPOResponse = (List<ProductPricingPO>) transObj.getSourcePojo();
            }
        } else {
            LOGGER.debug("No Records to fetch- getAllProductPricing method in ProductPricingService");
        }
        LOGGER.debug("Exiting getAllProductPricing method in ProductPricingService" + productPricingPOResponse);
        return productPricingPOResponse;
    }
}
