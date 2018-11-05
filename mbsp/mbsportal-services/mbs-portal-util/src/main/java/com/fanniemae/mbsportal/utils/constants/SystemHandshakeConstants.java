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

package com.fanniemae.mbsportal.utils.constants;

/**
 * 
 * @author: Rajiv Chaudhuri
 * @Date: May 7, 2018
 * @File: com.fanniemae.mbsportal.utils.constants.SystemHandshakeConstants.java
 * @Revision:
 * @Description: SystemHandshakeConstants.java
 */
public class SystemHandshakeConstants {

    public enum HEADER_MAP {

        USER_ID("x-mbsp-userid"), PASSWORD_KEY("x-mbsp-pwd-key"), AUTHORIZATION("authorization");

        /**
         * 
         * value Value
         */
        private final String value;

        /**
         * 
         * Constructor
         * @param value  Value to be set in constructor
         */
        private HEADER_MAP(String value) {
            this.value = value;
        }

        /**
         * 
         * getter method
         * @return String
         */
        public String getValue() {
            return value;
        }
    }
}
