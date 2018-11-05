/*
 * 
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.editor;

import java.beans.PropertyEditorSupport;

import org.apache.geode.internal.lang.StringUtils;

import com.fanniemae.mbsportal.constants.RegionColumnList;

/**
 * This is a custom property editor that will be used to translate a String to
 * RegionColumnList enum
 * 
 * @author FannieMae
 *
 */
public class RegionColumnListEditor extends PropertyEditorSupport {
	public void setAsText(String text) {
		if (StringUtils.isBlank(text)) {
			setValue(RegionColumnList.empty);
		} else {
			try {
				setValue(RegionColumnList.valueOf(text.trim()));
			} catch (Exception ex) {
				setValue(null);
			}
		}
	}
}
