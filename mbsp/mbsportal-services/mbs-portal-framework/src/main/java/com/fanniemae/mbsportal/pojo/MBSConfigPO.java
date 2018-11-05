/*
 * Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 * reserved under the copyright laws of the United States and international
 * conventions. Use of a copyright notice is precautionary only and does not
 * imply publication or disclosure. This software contains confidential
 * information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 * is prohibited without the prior written consent of Fannie Mae.
 */
package com.fanniemae.mbsportal.pojo;

import java.io.Serializable;

/**
 * @author g8uaxt Created on 1/18/2018.
 */

public class MBSConfigPO implements Serializable {
        
        /**
         * 
         * key String
         */
        private String key;
        /**
         * 
         * value String
         */
        private String value;
        /**
         * 
         * parent String
         */
        private String parent;
        
        /**
         * 
         * Constructor
         */
        public MBSConfigPO(){
        
        }
        
        /**
         * 
         * 
         * @param key the key
         * @param value the value
         * @param parent the parent
         */
        public MBSConfigPO(String key, String value, String parent) {
                this.key = key;
                this.value = value;
                this.parent = parent;
        }
        
        /**
         * 
         * @return String
         */
        public String getKey() {
                return key;
        }
        
        /**
         * 
         * @param key the key
         */
        public void setKey(String key) {
                this.key = key;
        }
        
        /**
         * 
         * @return String
         */
        public String getValue() {
                return value;
        }
        
        /**
         * 
         * @param key the key
         */
        public void setValue(String value) {
                this.value = value;
        }
        
        /**
         * 
         * @return String
         */
        public String getParent() {
                return parent;
        }
        
        /**
         * 
         * @param key the key
         */
        public void setParent(String parent) {
                this.parent = parent;
        }
        
        /**
         * 
         * @return String
         */
        @Override
        public String toString() {
                return "MBSConfigPO{" + "key='" + key + '\'' + ", value='" + value + '\'' + ", parent='" + parent + '\''
                        + '}';
        }
}
