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


/**
 * @author g8uaxt Created on 12/14/2017.
 */

@SuppressWarnings("serial")
public class Sort implements Serializable {
        
//        private List<String> fields = new ArrayList<String>();
		private String[] fields;
        private SortBy sortBy;
        
//        public Sort() {
//        }
//        
        public Sort(String field, SortBy sortBy) {
        	this.fields = new String[] { field };
        	this.sortBy = sortBy;
        }
        
//        public Sort(List<String>  fields, SortBy sortBy) {
//                this.fields = fields;
//                this.sortBy = sortBy;
//        }
        
//        public List<String> getFields() {
//                return fields;
//        }
//        
//        public void setFields(ArrayList<String> fields) {
//                this.fields = fields;
//        }
//        
        
        public SortBy getSortBy() {
                return sortBy;
        }
        
        /**
		 * @return the fields
		 */
		public String[] getFields() {
			return fields;
		}

		/**
		 * @param fields the fields to set
		 */
		public void setFields(String[] fields) {
			this.fields = fields;
		}
		
		public void addField(String field) {
			this.fields = new String[] { fields[0], field };
//			(String[]) Arrays.asList(fields, field).toArray();
		}

		public void setSortBy(SortBy sortBy) {
                this.sortBy = sortBy;
        }
        
        @Override
        public String toString() {
                return "Sort{" + "fields=" + fields.toString() + ", sortBy=" + sortBy + '}';
        }
}
