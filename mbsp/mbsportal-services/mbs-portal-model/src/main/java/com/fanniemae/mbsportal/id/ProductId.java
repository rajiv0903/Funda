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

package com.fanniemae.mbsportal.id;

import java.io.Serializable;
import java.util.Objects;

/**
 * This is the key class to identify gemfire objects in the region. It uses its
 * hashcode to persists and retrieve the objects.
 * 
 * @author g8uaxt Created on 9/7/2017.
 */
public class ProductId implements Serializable {

    /**
     * Generated serial id
     */
    private static final long serialVersionUID = -8984619804184184098L;
    /**
     * Product identifier:generated number
     */
    private Long identifier;
    /**
     * Source Type of the Product
     */
    private String sourceType;
    /**
     * Product type
     */
    private String type;
    /**
     * The product ID str
     */
    private String productIdStr;

    /**
     * 
     * @return Long
     */
    public Long getIdentifier() {
        return identifier;
    }

    /**
     * 
     * @param identifier
     */
    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    /**
     * 
     * @return String
     */
    public String getSourceType() {
        return sourceType;
    }

    /**
     * 
     * @param sourceType
     */
    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    /**
     * 
     * @return String
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ProductId))
            return false;
        ProductId productId = (ProductId) o;
        return Objects.equals(getIdentifier(), productId.getIdentifier())
                && Objects.equals(getSourceType(), productId.getSourceType())
                && Objects.equals(getType(), productId.getType());
    }

    /**
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(getIdentifier(), getSourceType(), getType());
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return identifier + sourceType.toUpperCase().trim() + type.toUpperCase().trim();
    }

    /**
     * @return the productIdStr
     */
    public String getProductIdStr() {
        return this.getIdentifier() + "." + this.getSourceType() + "." + this.getType();
    }

    /**
     * @param productIdStr
     *            the productIdStr to set
     */
    public void setProductIdStr(String productIdStr) {
        this.productIdStr = productIdStr;
    }
}
