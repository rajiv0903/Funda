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
import java.util.List;

/**
 * 
 * 
 * @author g8upjv
 * 
 */
public class MBSFilter implements Serializable {
    
    /**
     *
     * serialVersionUID long
     */
    private static final long serialVersionUID = -1852922480036071210L;
    
    /**
     * 
     * columnName String
     */
    private String columnName;
    
    /**
     * 
     * valuesLst List<String>
     */
    private List<String> valuesLst;
    
    /**
     * 
     * operator MBSOperator
     */
    private MBSOperator operator;
    
    /**
     * 
     * 
     * @param columnName String
     * @param valuesLst List<String>
     * @param in String
     */
    public MBSFilter(String columnName, List<String> valuesLst, MBSOperator in) {
        super();
        this.columnName = columnName;
        this.valuesLst = valuesLst;
        this.operator = in;
    }

    /**
     * 
     * @return the columnName
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * 
     * @param columnName the columnName to set
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * 
     * @return the valuesLst
     */
    public List<String> getValuesLst() {
        return valuesLst;
    }

    /**
     * 
     * @param valuesLst the valuesLst to set
     */
    public void setValuesLst(List<String> valuesLst) {
        this.valuesLst = valuesLst;
    }

    /**
     * 
     * @return the operator
     */
    public MBSOperator getOperator() {
        return operator;
    }

    /**
     * 
     * @param operator the operator to set
     */
    public void setOperator(MBSOperator operator) {
        this.operator = operator;
    }

    /**
     * 
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MBSFilter [columnName=" + columnName + ", valuesLst=" + valuesLst + ", operator=" + operator.toString() + "]";
    }


    
}
