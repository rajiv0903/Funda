/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.config;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;

/**
 * Created by g8uaxt on 8/11/2017.
 * @author g8upjv
 */

@Component
public class MDCFilter implements Filter {
    
    /**
     * 
     * initialization method
     * 
     * @param filterConfig the filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
	/**
	 * 
	 * Filter method
	 * 
	 * @param servletRequest the servletRequest
	 * @param servletResponse the servletResponse
	 * @param filterChain the filterChain
	 * @throws IOException
	 * @throws ServletException
	 */
    @Override 
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        MDC.clear();
        //CorrelationFilter is configured to run before this filter
        MDC.put(MBSPServiceConstants.CORRELATION_ID_HEADER_NAME, httpRequest.getHeader(MBSPServiceConstants.CORRELATION_ID_HEADER_NAME));
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            // it's important to always clean the cid from the MDC,
            // this Thread goes to the pool but it's loglines would still contain the cid.
            MDC.remove(MBSPServiceConstants.CORRELATION_ID_HEADER_NAME);
        }
    }
    
    /**
     * 
     * Destroy method
     */
    @Override
    public void destroy() {
    }
    
}
