/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fanniemae.mbsportal.api.controller.helper.ProductPricingControllerHelper;
import com.fanniemae.mbsportal.api.controller.interceptor.entitlement.EntitlementRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.RequestHeaderRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.ResponseHeaderRequired;
import com.fanniemae.mbsportal.api.po.ProductPricingPO;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.constants.TradeConstants;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author g8uaxt Created on 9/11/2017.
 */
@RestController
public class ProductPricingController extends BaseController {

    @PostConstruct
    public void init() {
        LOGGER.info("Product Pricing Controller Started");
    }

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductPricingController.class);

    /**
     *
     * productPricingControllerHelper ProductPricingControllerHelper
     */
    @Autowired
    private ProductPricingControllerHelper productPricingControllerHelper;

    /**
     *
     * CLASS_NAME String
     */
    public static final String CLASS_NAME = "ProductPricingController";

    /**
     *
     * Method to create the product pricing
     *
     * @param productPricingPORequest
     *            the productPricingPORequest
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/mbsproductpricing", method = RequestMethod.POST, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE })
    @ResponseHeaderRequired
    public ResponseEntity<Object> createProductPricing(@RequestBody List<ProductPricingPO> productPricingPORequest) {

        LOGGER.debug("Entering createProductPricing in ProductPricingController");

        ProductPricingPO productPricingPOResponse = null;
        try {
            productPricingPOResponse = productPricingControllerHelper.createProductPricing(productPricingPORequest);

        } catch (MBSBaseException ex) {
            LOGGER.error("Exiting createProductPricing in ProductPricingController");
            return getResponseEntityException(CLASS_NAME, "createProductPricing", ex);
        } catch (Exception ex) {
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- Start
            LOGGER.error("Exiting createProductPricing in ProductPricingController");
            return getResponseEntityInternalServerException(CLASS_NAME, "createProductPricing", ex);
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- End
        }
        LOGGER.debug("Exiting createProductPricing in ProductPricingController");
        return ResponseEntity.ok(productPricingPOResponse);
    }

    /**
     *
     * Method to retrieve all product pricing details
     *
     * @param identifier
     *            the identifier
     * @param sourceType
     *            the sourceType
     * @param type
     *            the type
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = { "/mbsproductpricing",
            "/capital-markets/trading/securities/mbs/productpricing" }, method = RequestMethod.GET, produces = {
                    MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.LENDER_TRADE_EXECUTE, EntitlementRole.TRADER_TRADE_EXECUTE,
            EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_TRADER_EXECUTE_HE, EntitlementRole.MBSP_FM_TRADER_EXECUTE_LE,
            EntitlementRole.MBSP_FM_ADMIN_HE, EntitlementRole.MBSP_FM_ADMIN_LE, EntitlementRole.TSP_TRADE_EXECUTE,
            // CMMBSSTA01-882 - Read Only User access - Start
            EntitlementRole.MBSP_FM_TRADER_READ_ONLY_LE, EntitlementRole.MBSP_FM_TRADER_READ_ONLY_HE
            // CMMBSSTA01-882 - Read Only User access - End
    })
    @ResponseHeaderRequired
    public ResponseEntity<Object> getAllProductPricing(@PathVariable("identifier") Optional<Long> identifier,
            @PathVariable("sourceType") Optional<TradeConstants.PRODUCT_SOURCE_TYPE> sourceType,
            @PathVariable("type") Optional<TradeConstants.PRODUCT_TYPE> type) {

        LOGGER.debug("Entering getAllProductPricing in ProductPricingController");

        List<ProductPricingPO> productPricingPOResponse = new ArrayList<>();
        try {
            productPricingPOResponse = productPricingControllerHelper.getAllProductPricing(true);

        } catch (MBSBaseException ex) {
            LOGGER.error("Exiting getAllProductPricing in ProductPricingController");
            return getResponseEntityException(CLASS_NAME, "getAllProductPricing", ex);
        } catch (Exception ex) {
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- Start
            LOGGER.error("Exiting getAllProductPricing in ProductPricingController");
            return getResponseEntityInternalServerException(CLASS_NAME, "getAllProductPricing", ex);
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- End
        }
        LOGGER.debug("Exiting getAllProductPricing in ProductPricingController");
        return ResponseEntity.ok(productPricingPOResponse);
    }

    /**
     *
     * Method to retrieve product pricing details for a particular product
     *
     * @param identifier
     *            the identifier
     * @param sourceType
     *            the sourceType
     * @param type
     *            the type
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = { "/mbsproductpricing/{identifier}/{sourceType}/{type}",
            "/capital-markets/trading/securities/mbs/productpricing/{identifier}/{sourceType}/{type}" }, method = RequestMethod.GET, produces = {
                    MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.LENDER_TRADE_EXECUTE, EntitlementRole.TRADER_TRADE_EXECUTE,
            EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_TRADER_EXECUTE_HE, EntitlementRole.MBSP_FM_TRADER_EXECUTE_LE,
            EntitlementRole.MBSP_FM_ADMIN_HE, EntitlementRole.MBSP_FM_ADMIN_LE, EntitlementRole.TSP_TRADE_EXECUTE,
            // CMMBSSTA01-882 - Read Only User access - Start
            EntitlementRole.MBSP_FM_TRADER_READ_ONLY_LE, EntitlementRole.MBSP_FM_TRADER_READ_ONLY_HE
            // CMMBSSTA01-882 - Read Only User access - End
    })
    @ResponseHeaderRequired
    public ResponseEntity<Object> getProductPricing(@PathVariable("identifier") Long identifier,
            @PathVariable("sourceType") TradeConstants.PRODUCT_SOURCE_TYPE sourceType,
            @PathVariable("type") TradeConstants.PRODUCT_TYPE type) {

        LOGGER.debug("Entering getProductPricing in ProductPricingController");
        List<ProductPricingPO> productPricingPOResponse = null;
        try {
            productPricingPOResponse = productPricingControllerHelper.getProductPricing(identifier, sourceType, type, true);

        } catch (MBSBaseException ex) {
            LOGGER.error("Pricing controller error ", ex);
            LOGGER.error("Exiting getProductPricing in ProductPricingController");
            return getResponseEntityException(CLASS_NAME, "getProductPricing", ex);

        } catch (Exception ex) {
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- Start
            LOGGER.error("Exiting getProductPricing in ProductPricingController");
            return getResponseEntityInternalServerException(CLASS_NAME, "getProductPricing", ex);
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- End

        }
        LOGGER.debug("Exiting getProductPricing in ProductPricingController" + productPricingPOResponse);
        return ResponseEntity.ok(productPricingPOResponse);
    }

    /**
     *
     * @throws MBSBaseException
     */
    @RequestMapping(value = "/mbsproductpricing", method = RequestMethod.DELETE, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE })
    @ResponseHeaderRequired
    public void clearAllProductPricing() throws MBSBaseException {

        LOGGER.debug("Entering clearAllProductPricing in ProductPricingController");
        productPricingControllerHelper.clearAllProductPricing();
        LOGGER.debug("Exiting clearAllProductPricing in ProductPricingController");
    }
}
