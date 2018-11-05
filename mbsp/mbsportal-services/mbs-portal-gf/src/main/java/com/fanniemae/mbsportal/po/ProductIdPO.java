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

package com.fanniemae.mbsportal.po;

import java.io.Serializable;

/**
 * This class is the presentation
 * model for Product object
 * 
 * @author g8upjv
 * 
 */
public class ProductIdPO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3553433810461830541L;
	/**
	 * Product identifier
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
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the identifier
	 */
	public Long getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(Long identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the sourceType
	 */
	public String getSourceType() {
		return sourceType;
	}

	/**
	 * @param sourceType the sourceType to set
	 */
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "ProductIdPO{" + "identifier=" + identifier + ", sourceType=" + sourceType + ", type=" + type + '}';
	}
}
