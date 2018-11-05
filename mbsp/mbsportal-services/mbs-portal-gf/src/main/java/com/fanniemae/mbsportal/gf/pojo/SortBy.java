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

/**
 *
 * @author g8uaxt
 *
 */
public enum SortBy {
	asc("asc"), desc("desc"), empty("");
	private String name;

	SortBy() {
	}

	SortBy(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public static SortBy getEnum(String sort) {
		if (sort == null) {
			return null;
		}
		if ("".equals(sort) || "empty".equals(sort)) {
			return SortBy.empty;

		}
		for (SortBy sortBy : SortBy.values()) {
			if (sortBy.getName().equalsIgnoreCase(sort)) {
				return sortBy;
			}
		}
		return null;
	}
}
