/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.controller.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPricingPO;
import com.fanniemae.mbsportal.api.service.ProductPricingService;
import com.fanniemae.mbsportal.aspect.exception.ExceptionTracingAnnotation;
import com.fanniemae.mbsportal.id.ProductId;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * @author gaur5c
 */
@Component
public class ProductPricingControllerHelper extends BaseControllerHelper {

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductPricingControllerHelper.class);

    /**
     *
     * productPricingServiceV1 ProductPricingService
     */
    @Autowired
    private ProductPricingService productPricingServiceV1;

    /**
     *
     * select the media type version
     *
     * @param mediaType
     *            the mediaType
     * @return BaseProcessor
     */
    @SuppressWarnings("rawtypes")
    private BaseProcessor selectDataServiceByMediaType(String mediaType) {
        LOGGER.debug("Entering selectDataServiceByMediaType method in ProductPricingControllerHelper " + mediaType);
        if (mediaType.equals(MediaType.APPLICATION_JSON_VALUE) || mediaType.equals(getVersion1json().toString())) {
            return productPricingServiceV1;
        } else {
            return null;
        }
    }

    /**
     *
     * create product pricing
     *
     * @param productPricingPORequest
     *            the productPricingPORequest
     * @return ProductPricingPO
     * @throws MBSBaseException
     */
    @ExceptionTracingAnnotation
    public ProductPricingPO createProductPricing(List<ProductPricingPO> productPricingPORequest)
            throws MBSBaseException {
        LOGGER.debug("Entering createProductPricing method in ProductPricingControllerHelper ");
        String mediaType = MediaType.APPLICATION_JSON_VALUE;

        ProductPricingPO productPricingPOResponse = null;

        if (Objects.equals(productPricingPORequest, null)) {
            LOGGER.error("Failed to create Transaction, productPricingPORequest(input) object is null");
            throw new MBSBusinessException(
                    "Failed to create Transaction, productPricingPORequest(input) object is null",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        ProductPricingService productPricingService = (ProductPricingService) selectDataServiceByMediaType(
                mediaType.trim());

        if (Objects.equals(productPricingService, null)) {
            LOGGER.error(MBSPServiceConstants.INVALID_ACCEPT_HEADER);
            throw new MBSSystemException(MBSPServiceConstants.INVALID_ACCEPT_HEADER,
                    MBSExceptionConstants.SYSTEM_EXCEPTION);

        }
        productPricingService.createProductPricing(productPricingPORequest);
        LOGGER.debug("Exiting createProductPricing method in ProductPricingControllerHelper");
        return productPricingPOResponse;
    }

    /**
     *
     * Get all the product pricing
     *
     * @return List<ProductPricingPO>
     * @throws MBSBaseException
     *
     */
    @ExceptionTracingAnnotation
    public List<ProductPricingPO> getAllProductPricing(boolean filterDeletedRecord) throws MBSBaseException {
        LOGGER.debug("Entering getAllProductPricing method in ProductPricingControllerHelper");
        String mediaType = MediaType.APPLICATION_JSON_VALUE;

        List<ProductPricingPO> productPricingPOResponse = new ArrayList<>();

        ProductPricingService productPricingService = (ProductPricingService) selectDataServiceByMediaType(
                mediaType.trim());

        if (Objects.equals(productPricingService, null)) {
            LOGGER.error(MBSPServiceConstants.INVALID_ACCEPT_HEADER);
            throw new MBSSystemException(MBSPServiceConstants.INVALID_ACCEPT_HEADER,
                    MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        productPricingPOResponse = productPricingService.getAllProductPricing(filterDeletedRecord);
        LOGGER.debug("Exiting getAllProductPricing method in ProductPricingControllerHelper");
        return productPricingPOResponse;
    }

    /**
     *
     * Get product pricing details for input passed
     *
     * @param identifier
     *            the identifier
     * @param sourceType
     *            the sourceType
     * @param type
     *            the type
     * @return List<ProductPricingPO>
     * @throws MBSBaseException
     */
    @ExceptionTracingAnnotation
    public List<ProductPricingPO> getProductPricing(Long identifier, TradeConstants.PRODUCT_SOURCE_TYPE sourceType,
            TradeConstants.PRODUCT_TYPE type, boolean filterDeletedRecord) throws MBSBaseException {
        LOGGER.debug("Entering getProductPricing method in ProductPricingControllerHelper");
        String mediaType = MediaType.APPLICATION_JSON_VALUE;
        ProductIdPO productIdPO = null;
        List<ProductPricingPO> productPricingPOResponse = null;

        ProductPricingService productPricingService = (ProductPricingService) selectDataServiceByMediaType(
                mediaType.trim());
        if (Objects.equals(productPricingService, null)) {
            LOGGER.error(MBSPServiceConstants.INVALID_ACCEPT_HEADER);
            throw new MBSSystemException(MBSPServiceConstants.INVALID_ACCEPT_HEADER,
                    MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        productIdPO = new ProductIdPO();
        productIdPO.setIdentifier(identifier);
        productIdPO.setSourceType(sourceType);
        productIdPO.setType(type);
        ProductId productId = new ProductId();
        productId.setIdentifier(productIdPO.getIdentifier());
        productId.setSourceType(productIdPO.getSourceType().name());
        productId.setType(productIdPO.getType().name());

        productPricingPOResponse = productPricingService.getProductPricing(productIdPO, filterDeletedRecord);
        LOGGER.debug("Exiting getProductPricing method in ProductPricingControllerHelper");
        return productPricingPOResponse;
    }

    /**
     *
     * clear all the product pricing details
     *
     * @throws MBSBaseException
     */
    @ExceptionTracingAnnotation
    public void clearAllProductPricing() throws MBSBaseException {
        LOGGER.debug("Entering clearAllProductPricing method in ProductPricingControllerHelper");
        productPricingServiceV1.clearAll();
        LOGGER.debug("Exiting clearAllProductPricing method in ProductPricingControllerHelper");
    }
}
