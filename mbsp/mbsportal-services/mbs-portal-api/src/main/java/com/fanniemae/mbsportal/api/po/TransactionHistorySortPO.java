/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.po;

import java.util.List;

/**
 * @author g8uaxt Created on 12/19/2017.
 */

public class TransactionHistorySortPO {
        
       /**
        * 
        * sortBy String
        */
        private String sortBy;
        
        /**
        * 
        * sortOrder String
        */
        private String sortOrder;
        
        /**
        * 
        * totalRecords Integer
        */
        private Integer totalRecords;
        
        /**
        * 
        * pageIndex Integer
        */
        private Integer pageIndex;
        
        /**
        * 
        * pageSize Integer
        */
        private Integer pageSize;
        
        /**
        * 
        * list List<TransactionHistoryPO>
        */
        private List<TransactionHistoryPO> list;
        
        /**
         * 
         * @return String
         */
        public String getSortBy() {
                return sortBy;
        }
        
        /**
         * 
         * @param sortBy the sortBy
         */
        public void setSortBy(String sortBy) {
                this.sortBy = sortBy;
        }
        
        /**
         * 
         * @return String
         */
        public String getSortOrder() {
                return sortOrder;
        }
        
        /**
         * 
         * @param sortOrder the sortOrder
         */
        public void setSortOrder(String sortOrder) {
                this.sortOrder = sortOrder;
        }
        
        /**
         * 
         * @return Integer
         */
        public Integer getTotalRecords() {
                return totalRecords;
        }
        
        /**
         * 
         * @param totalRecords the totalRecords
         */
        public void setTotalRecords(Integer totalRecords) {
                this.totalRecords = totalRecords;
        }
        
        /**
         * 
         * @return Integer
         */
        public Integer getPageIndex() {
                return pageIndex;
        }
        
        /**
         * 
         * @param pageIndex the pageIndex
         */
        public void setPageIndex(Integer pageIndex) {
                this.pageIndex = pageIndex;
        }
        
        /**
         * 
         * @return Integer
         */
        public Integer getPageSize() {
                return pageSize;
        }
        
        /**
         * 
         * @param pageSize the pageSize
         */
        public void setPageSize(Integer pageSize) {
                this.pageSize = pageSize;
        }
        
        /**
         * 
         * @return List<TransactionHistoryPO>
         */
        public List<TransactionHistoryPO> getList() {
                return list;
        }
        
        /**
         * 
         * @param list the List<TransactionHistoryPO>
         */
        public void setList(List<TransactionHistoryPO> list) {
                this.list = list;
        }
}
