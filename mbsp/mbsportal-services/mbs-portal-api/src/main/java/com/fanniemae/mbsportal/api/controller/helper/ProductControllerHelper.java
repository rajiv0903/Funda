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

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.service.ProductService;
import com.fanniemae.mbsportal.aspect.exception.ExceptionTracingAnnotation;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * @author gaur5c
 */
@Component
public class ProductControllerHelper extends BaseControllerHelper {

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductControllerHelper.class);

    /**
     *
     * productServicev1 ProductService
     */
    @Autowired
    private ProductService productServicev1;

    /**
     *
     * Purpose: This method returns the proper version of the Service object
     * based on the media type. Default is version1
     *
     * @param mediaType
     *            the mediaType
     * @return BaseProcessor
     *
     */
    @SuppressWarnings("rawtypes")
    private BaseProcessor selectDataServiceByMediaType(String mediaType) {
        LOGGER.debug("Entering selectDataServiceByMediaType method in ProductControllerHelper " + mediaType);
        if (mediaType.equals(MediaType.APPLICATION_JSON_VALUE) || mediaType.equals(getVersion1json().toString())) {
            return productServicev1;
        } else {
            return null;
        }
    }

    /**
     *
     * Create products
     *
     * @param productPO
     *            the productPO
     * @return ProductPO
     * @throws MBSBaseException
     */
    @SuppressWarnings("rawtypes")
    @ExceptionTracingAnnotation
    public ProductPO createProducts(ProductPO productPO) throws MBSBaseException {
        LOGGER.debug("Entering createProducts method in ProductControllerHelper");
        String mediaType = MediaType.APPLICATION_JSON_VALUE;
        ProductPO productPOResponse;

        if (!Objects.equals(productPO, null)) {
            BaseProcessor productService = selectDataServiceByMediaType(mediaType.trim());

            if (!Objects.equals(productService, null)) {

                productPOResponse = ((ProductService) productService).createProducts(productPO);

            } else {
                LOGGER.error(MBSPServiceConstants.INVALID_ACCEPT_HEADER);
                throw new MBSSystemException(MBSPServiceConstants.INVALID_ACCEPT_HEADER,
                        MBSExceptionConstants.SYSTEM_EXCEPTION);

            }
        } else {
            LOGGER.error("Failed to create Product, productPO(input) object is null");
            throw new MBSBusinessException("Failed to create Product, productPO(input) object is null",
                    MBSExceptionConstants.BUSINESS_EXCEPTION);

        }
        LOGGER.debug("Exiting createProducts method in ProductControllerHelper");
        return productPOResponse;
    }

    /**
     *
     * clears all products
     *
     * @throws MBSBaseException
     */
    @ExceptionTracingAnnotation
    public void clearAllProducts() throws MBSBaseException {
        LOGGER.debug("Entering clearAllProducts method in ProductControllerHelper");
        productServicev1.clearAll();
        LOGGER.debug("Exiting clearAllProducts method in ProductControllerHelper");
    }

    /**
     *
     * get the list of products
     *
     * @return List<ProductPO>
     * @throws MBSBaseException
     *
     */
    @SuppressWarnings("rawtypes")
    @ExceptionTracingAnnotation
    public List<ProductPO> getProducts() throws MBSBaseException {
        LOGGER.debug("Entering getProducts method in ProductControllerHelper");
        String mediaType = MediaType.APPLICATION_JSON_VALUE;
        List<ProductPO> lstProductPO;
        BaseProcessor productService = selectDataServiceByMediaType(mediaType.trim());

        if (!Objects.equals(productService, null)) {
            lstProductPO = ((ProductService) productService).getMBSProducts();

        } else {
            LOGGER.error(MBSPServiceConstants.INVALID_ACCEPT_HEADER);
            throw new MBSSystemException(MBSPServiceConstants.INVALID_ACCEPT_HEADER,
                    MBSExceptionConstants.SYSTEM_EXCEPTION);

        }
        LOGGER.debug("Exiting getProducts method in ProductControllerHelper");
        return lstProductPO;
    }

}
