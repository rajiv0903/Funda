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

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fanniemae.mbsportal.api.controller.helper.ProductControllerHelper;
import com.fanniemae.mbsportal.api.controller.interceptor.entitlement.EntitlementRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.RequestHeaderRequired;
import com.fanniemae.mbsportal.api.controller.interceptor.header.ResponseHeaderRequired;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.utils.constants.EntitlementRole;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * This class is the controller for the Product related transactions
 *
 * @author g8upjv
 *
 */
@RestController
public class ProductController extends BaseController {

    @PostConstruct
    public void init() {
        LOGGER.info("Product Controller Started");
    }

    /**
     *
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    /**
     *
     * productControllerHelper passes the call from controller to service
     */
    @Autowired
    ProductControllerHelper productControllerHelper;

    /**
     *
     * CLASS_NAME String
     */
    public static final String CLASS_NAME = "ProductController";

    /**
     *
     * Purpose: This method calls the TransactionRequestService's create method
     * to create Transaction Request for version1
     *
     * @param productPO
     *            The body parameter with values from the input is taken from
     *            the presentation object
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = "/mbsproducts", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE,
            "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE })
    @ResponseHeaderRequired
    public ResponseEntity<Object> createProducts(@RequestBody ProductPO productPO) {

        LOGGER.debug("Entering createProducts method in ProductController");
        ProductPO productPOResponse = null;
        try {
            productPOResponse = productControllerHelper.createProducts(productPO);

        } catch (MBSBaseException ex) {
            LOGGER.error("Exiting getProducts method in ProductController");
            return getResponseEntityException(CLASS_NAME, "createProducts", ex);
        } catch (Exception ex) {
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- Start
            LOGGER.error("Exiting getProducts method in ProductController");
            return getResponseEntityInternalServerException(CLASS_NAME, "createProducts", ex);
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- End
        }
        LOGGER.debug("Exiting getProducts method in ProductController");
        return ResponseEntity.ok(productPOResponse);
    }

    /**
     *
     * Purpose: This method clears all the records in Products
     *
     * @throws MBSBaseException
     */
    @RequestMapping(value = "/mbsproducts", method = RequestMethod.DELETE, produces = {
            MediaType.APPLICATION_JSON_VALUE, "application/vnd.fnma-v1+json" })
    @RequestHeaderRequired
    @EntitlementRequired(roles = { EntitlementRole.ADMIN, EntitlementRole.MBSP_FM_ADMIN_HE,
            EntitlementRole.MBSP_FM_ADMIN_LE })
    @ResponseHeaderRequired
    public void clearAllProducts() throws MBSBaseException {
        LOGGER.debug("Entering clearAllProducts method in ProductController");
        productControllerHelper.clearAllProducts();
        LOGGER.debug("Exiting clearAllProducts method in ProductController");
    }

    /**
     *
     * Purpose: This method retrieve the products object by calling the service
     *
     * @return ResponseEntity<Object>
     */
    @RequestMapping(value = { "/mbsproducts",
            "/capital-markets/trading/securities/mbs/products" }, method = RequestMethod.GET, produces = {
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
    public ResponseEntity<Object> getProducts() {
        LOGGER.debug("Entering getProducts method in ProductController");
        List<ProductPO> lstProductPO;
        try {
            lstProductPO = productControllerHelper.getProducts();

        } catch (MBSBaseException ex) {
            LOGGER.error("Exiting getProducts method in ProductController");
            return getResponseEntityException(CLASS_NAME, "getProducts", ex);
        } catch (Exception ex) {
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- Start
            LOGGER.error("Exiting getProducts method in ProductController");
            return getResponseEntityInternalServerException(CLASS_NAME, "getProducts", ex);
            // CMMBSSTA01-882, CMMBSSTA01-874 - Exception handling- End
        }
        LOGGER.debug("Exiting getProducts method in ProductController");
        return ResponseEntity.ok(lstProductPO);
    }
}
