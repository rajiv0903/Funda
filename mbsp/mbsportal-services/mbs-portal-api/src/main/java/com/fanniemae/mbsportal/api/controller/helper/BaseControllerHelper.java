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

import org.springframework.http.MediaType;

/**
 * @author gaur5c
 */
public class BaseControllerHelper {

    /**
     * 
     * version1json MediaType
     */
    private MediaType version1json = new MediaType("application", "vnd.fnma-v1+json");
    
    /**
     * 
     * @return MediaType
     */
    public MediaType getVersion1json() {
        return version1json;
    }
}
