/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.api.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;

/**
 * Class Name: CorrelationFilter
 * Purpose 	 : This class generates the correlation id if not present already and set in the header 
 * 
 * @Date 07/18/2017		
 * @author g8upjv
 * Initial version. 
 * 
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationFilter extends GenericFilterBean {
	
    /**
     * LOGGER Logger variable
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CorrelationFilter.class);
	
    /**
     * 
     * filter method
     * 
     * @param servletRequest the servletRequest
     * @param servletResponse the servletResponse
     * @param filterChain the filterChain
     * @throws IOException
     * @throws ServletException
     *  
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        
    	HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        if(!hasCorrelationHeader(httpRequest)) {
            httpRequest = new HttpServletRequestWrapper(httpRequest) {
                private final String correlationId = UUID.randomUUID().toString();
                @Override
                public String getHeader(String headerName) {
                    if(MBSPServiceConstants.CORRELATION_ID_HEADER_NAME.equals(headerName)) {
                        return correlationId;
                    }
                    return super.getHeader(headerName);
                }
                @Override
                public Enumeration<String> getHeaders(String headerName) {
                    if(MBSPServiceConstants.CORRELATION_ID_HEADER_NAME.equals(headerName)) {
                        return Collections.enumeration(Arrays.asList(correlationId));
                    }
                    return super.getHeaders(headerName);
                }
                @Override
                public Enumeration<String> getHeaderNames() {
                    Collection<String> headerNames = Collections.list(super.getHeaderNames());
                    headerNames.add(MBSPServiceConstants.CORRELATION_ID_HEADER_NAME);
                    return Collections.enumeration(headerNames);
                }
            };
        }
        httpResponse.setHeader(MBSPServiceConstants.CORRELATION_ID_HEADER_NAME, httpRequest.getHeader(MBSPServiceConstants.CORRELATION_ID_HEADER_NAME));
        LOGGER.debug("CorrelationFilter class: correlation id: "+httpRequest.getHeader(MBSPServiceConstants.CORRELATION_ID_HEADER_NAME));
        filterChain.doFilter(httpRequest, httpResponse);
    }

    /**
     * 
     * Purpose: This method returns the value of the correlation id header
     * 
     * @param httpRequest the httpRequest
     * @return boolean
     */
    private boolean hasCorrelationHeader(HttpServletRequest httpRequest) {
        return httpRequest.getHeader(MBSPServiceConstants.CORRELATION_ID_HEADER_NAME) != null;
    }
}
