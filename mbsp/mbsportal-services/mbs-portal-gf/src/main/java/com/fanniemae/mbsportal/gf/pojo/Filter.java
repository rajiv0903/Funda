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

package com.fanniemae.mbsportal.gf.pojo;

import java.io.Serializable;
import java.util.Map;

/**
 * @author g8uaxt Created on 12/14/2017.
 */

@SuppressWarnings("serial")
public class Filter<T> implements Serializable {
        Map<String, T> fields;
        
       /* public Filter() {
        }*/
        
        public Filter(Map<String, T> fields) {
                this.fields = fields;
        }
        
        public Map<String, T> getFields() {
                return fields;
        }
        
        public void setFields(Map<String, T> fields) {
                this.fields = fields;
        }
        
        @Override
        public String toString() {
                return "Filter{" + "fields=" + fields + '}';
        }
}
