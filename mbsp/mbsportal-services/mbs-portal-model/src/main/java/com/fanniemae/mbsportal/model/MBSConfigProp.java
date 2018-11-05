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

package com.fanniemae.mbsportal.model;

import java.io.Serializable;

/**
 * @author g8uaxt Created on 1/8/2018.
 */

public class MBSConfigProp extends MBSBaseEntity{
        private String key;
        private String value;
        private String parent;
        private String dataType;
        
        @Override
        public Serializable getId() {
                return this.key;
        }
        
        public String getKey() {
                return key;
        }
        
        public void setKey(String key) {
                this.key = key;
        }
        
        public String getValue() {
                return value;
        }
        
        public void setValue(String value) {
                this.value = value;
        }
        
        public String getParent() {
                return parent;
        }
        
        public void setParent(String parent) {
                this.parent = parent;
        }
        
        public String getDataType() {
                return dataType;
        }
        
        public void setDataType(String dataType) {
                this.dataType = dataType;
        }
}
