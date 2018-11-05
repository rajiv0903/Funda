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

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Rajiv Chaudhuri
 * @Version:
 * @Date Nov 30, 2017
 * @Time 1:37:18 PM
 * 	com.fanniemae.mbsportal.calendar.client
 * 	HttpBasicAuthenticationHandler.java
 * @Description:
 */
public class HttpBasicAuthenticationHandler implements SOAPHandler<SOAPMessageContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpBasicAuthenticationHandler.class);

    /**
     * The user ID for authentication purposes.
     */
    private final String userID;
    /**
     * The password.
     */
    private final String password;

    /**
     * Creates a new HttpBasicAuthenticationHandler with the provided user ID
     * and password.
     * 
     * @param userId
     *            - the user ID
     * @param pWord
     *            - the corresponding password
     */
    public HttpBasicAuthenticationHandler(final String userId, final String pWord) {
        this.userID = userId;
        this.password = pWord;
    }

    /**
     * Handle the SOAPMessage and add authentication.
     * 
     * @param context
     *            - the SOAPMessageContext.
     * @return boolean - true for outbound, false otherwise.
     */
    @Override
    public boolean handleMessage(final SOAPMessageContext context) {
        final Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty.booleanValue()) {
            try {
                context.put(BindingProvider.USERNAME_PROPERTY, this.userID);
                context.put(BindingProvider.PASSWORD_PROPERTY, this.password);
            } catch (final Exception e) {
                final String err = e.getClass().getName() + e.getMessage() + " when adding Basic HTTP Authentication.";
                LOGGER.error(err, e);
                throw new IllegalStateException(err, e);
            }
        }
        return outboundProperty;
    }

    /**
     * 
     * @param context
     * @return boolean
     */
    @Override
    public boolean handleFault(final SOAPMessageContext context) {
        LOGGER.warn("Error occurred setting Basic HTTP Header");
        if (context.getMessage() != null) {
            try {
                SOAPMessage soapMsg = context.getMessage();
                LOGGER.warn("SOAP Message Header (if any): " + soapMsg.getSOAPHeader());
                LOGGER.warn("SOAP Message Body (if any): " + soapMsg.getSOAPBody());
            } catch (SOAPException e) {
                LOGGER.warn("Error trying to handle SOAP Fault", e);
            }
        }
        return false;
    }

    /**
     * This method overrides the close method from SOAPHandler
     * 
     * @param context
     */
    @Override
    public void close(final MessageContext context) {
        LOGGER.debug("Closing handler for Authorization");
    }

    /**
     * Retrieve headers. However un-implemented at this time.
     * 
     * @return currently returns null
     */
    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    /**
     * @return the userID
     */
    public String getUserID() {
        return this.userID;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }

}
