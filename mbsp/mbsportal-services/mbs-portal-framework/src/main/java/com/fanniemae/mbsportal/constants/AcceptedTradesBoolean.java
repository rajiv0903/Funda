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

package com.fanniemae.mbsportal.constants;

/**
 *
 * @author FannieMae
 *
 */
public enum AcceptedTradesBoolean {
	falseValue("false"), trueValue("true"), empty("");
	private String name;

	AcceptedTradesBoolean() {
	}

	AcceptedTradesBoolean(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public static AcceptedTradesBoolean getEnum(String sort) {
		if (sort == null) {
			return null;
		}
		if ("".equals(sort) || "empty".equals(sort)) {
			return AcceptedTradesBoolean.empty;
		}
		for (AcceptedTradesBoolean sortBy : AcceptedTradesBoolean.values()) {
			if (sortBy.getName().equalsIgnoreCase(sort) || sortBy.name().equalsIgnoreCase(sort) ) {
				return sortBy;
			}
		}
		return null;
	}
}
