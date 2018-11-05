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

import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.model.MBSProduct;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.cache.AppCacheKeys;
import com.fanniemae.mbsportal.utils.cache.MBSPAppCache;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class handles the service part for the Product based requests
 * 
 * @author g8upjv
 *
 */
@SuppressWarnings("rawtypes")
@Service
public class ProductService extends BaseProcessor {
        
        /**
         * 
         * LOGGER Logger
         */
	@InjectLog
	private Logger LOGGER;
	
	/**
	 * 
	 * NO_MATCHING_PRODUCT String
	 */
	private String NO_MATCHING_PRODUCT = "There is no matching product to update";
	
	/**
         * 
         * mbsObjectCreator MBSObjectCreator
         */
        @Autowired
        MBSObjectCreator mbsObjectCreator;
	
	/**
	 * MBSP product app cache component
	 */
	@Autowired
		private MBSPAppCache mbspAppCache;

	/**
	 * 
	 * @param productTransformer the productTransformer
	 * @param productPersister the productPersister
	 * @param productPOTransformer the productPOTransformer
	 */
	@SuppressWarnings("unchecked")
	@Autowired
	public ProductService(Transformer productTransformer, Persister productPersister,
			Transformer productPOTransformer) {
		super.transformer = productTransformer;
		super.persister = productPersister;
		super.poTransformer = productPOTransformer;
	}

	/**
	 * 
	 * @param productPO the productPO
	 * @return ProductPO
	 * @throws MBSBaseException
	 */
	public ProductPO createProducts(ProductPO productPO) throws MBSBaseException {
		LOGGER.debug("Entering createProducts method in ProductService");
		TransformationObject transformationObject = new TransformationObject();
		transformationObject.setSourcePojo(productPO);
		if(Objects.nonNull(productPO) && Objects.nonNull(productPO.getProductId()) && Objects.nonNull(productPO.getProductId().getIdentifier())){
			ProductPO prodPOResponse = getByProductId(productPO.retrieveKey());
			if(Objects.isNull(prodPOResponse)){
				LOGGER.error(NO_MATCHING_PRODUCT);
				throw new MBSBusinessException(NO_MATCHING_PRODUCT, MBSExceptionConstants.BUSINESS_EXCEPTION);
			}
		}
		super.processRequest(transformationObject);
		LOGGER.debug("Exiting createProducts method in ProductService");
		return (ProductPO) transformationObject.getSourcePojo();
	}

	/**
	 * 
	 * This method clears the Product records
	 * 
	 * @throws MBSBaseException
	 */
	public void clearAll() throws MBSBaseException {
		persister.clearAll();
	}

	/**
	 * 
	 * This method retrieves the list of Products
	 * 
	 * @return List<ProductPO>
	 * @throws MBSBaseException
	 */
	@SuppressWarnings("unchecked")
	public List<ProductPO> getMBSProducts() throws MBSBaseException {

		LOGGER.debug("Entering getMBSProducts method in ProductService");

		List<ProductPO> listProductPO = new ArrayList<ProductPO>();
		List<MBSProduct> listMBSProduct =  (List<MBSProduct>)mbspAppCache.get(AppCacheKeys.PRODUCT_CACHE_KEY);
		if(listMBSProduct==null){
			LOGGER.debug("Cache returned null! going for dao call");
			listMBSProduct = persister.getBaseDao().getAll();
		}
		ProductPO productPO;
		TransformationObject transObj;
		if (!Objects.equals(listMBSProduct, null)) {
			LOGGER.debug("Size of list: " + listMBSProduct.size());
			for (MBSProduct mbsProduct : listMBSProduct) {
				transObj = mbsObjectCreator.getTransformationObject();
				transObj.setTargetPojo(mbsProduct);
				productPO = (ProductPO) poTransformer.transform(transObj).getSourcePojo();
				listProductPO.add(productPO);
			}
		}
		// TODO: Sort the records?
		LOGGER.debug("Exiting getMBSProducts method in ProductService");
		return listProductPO;
	}

	/**
	 * 
	 * This method retrieves the product based on id
	 * 
	 * @return ProductPO
	 * @throws MBSBaseException
	 */
	@SuppressWarnings("unchecked")
	public ProductPO getByProductId(String productId) throws MBSBaseException {

		LOGGER.debug("Entering getByProductId method in ProductService");

		ProductPO productPO = null;
		MBSProduct mbsProduct = (MBSProduct) persister.getBaseDao().getById(productId);
		TransformationObject transObj;
		if (!Objects.equals(mbsProduct, null)) {
			LOGGER.debug("Product PO: " + mbsProduct);
			transObj = mbsObjectCreator.getTransformationObject();
			transObj.setTargetPojo(mbsProduct);
			productPO = (ProductPO) poTransformer.transform(transObj).getSourcePojo();
		}
		LOGGER.debug("Exiting getByProductId method in ProductService");
		return productPO;
	}
}
