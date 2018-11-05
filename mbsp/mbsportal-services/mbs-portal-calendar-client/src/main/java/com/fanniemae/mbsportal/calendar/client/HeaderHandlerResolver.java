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

package com.fanniemae.mbsportal.calendar.client;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Nov 30, 2017
 * @Time 1:33:05 PM
 * 	com.fanniemae.mbsportal.calendar.client
 * 	HeaderHandlerResolver.java
 * @Description:
 */
public class HeaderHandlerResolver implements HandlerResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderHandlerResolver.class);

    /**
     * The List of handlers.
     */
    private final List<Handler> handlerChain;

    /**
     * Create a new HeaderHandlerResolver with HTTP Basic Authentication
     * parameters
     * 
     * @param userID
     *            - a User ID
     * @param password
     *            - a corresponding password
     */
    public HeaderHandlerResolver(final String userID, final String password) {
        LOGGER.debug("Creating new HandlerResolver and adding HTTP Basic Authentication");
        this.handlerChain = new ArrayList<Handler>();
        final HttpBasicAuthenticationHandler httpAuth = new HttpBasicAuthenticationHandler(userID, password);
        this.handlerChain.add(httpAuth);
    }

    /**
     * Retrieve the list of handlers
     * 
     * @param portInfo -  port information.
     * @return List<Handler> - a list of Handlers
     */
    public List<Handler> getHandlerChain(final PortInfo portInfo) {
        return this.handlerChain;
    }

}
