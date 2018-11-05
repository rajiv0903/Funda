/*
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 *  
 */
package com.fanniemae.mbsportal.api.editor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.constants.RegionColumnList;

/**
 * @author FannieMae
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class RegionColumnListEditorTest {

	@InjectMocks
	RegionColumnListEditor regionColumnListEditor;
	
	@Test
	public void testSetAsTextNullText() {
		regionColumnListEditor.setAsText(null);
		assertEquals(RegionColumnList.empty, regionColumnListEditor.getValue());
	}
	
	@Test
	public void testSetAsTextEmptyText() {
		regionColumnListEditor.setAsText("");
		assertEquals(RegionColumnList.empty, regionColumnListEditor.getValue());
	}

	@Test
	public void testSetAsTextBlankText() {
		regionColumnListEditor.setAsText("  ");
		assertEquals(RegionColumnList.empty, regionColumnListEditor.getValue());
	}

	@Test
	public void testSetAsTextInvalidText() {
		regionColumnListEditor.setAsText(" tradeSettlementDate ");
		assertEquals(RegionColumnList.tradeSettlementDate, regionColumnListEditor.getValue());
	}

	@Test
	public void testSetAsTextAsc() {
		regionColumnListEditor.setAsText(" submissionDate ");
		assertEquals(RegionColumnList.submissionDate, regionColumnListEditor.getValue());
	}

	@Test
	public void testSetAsTextDesc() {
		regionColumnListEditor.setAsText("submi ssionDate");
		assertNull(regionColumnListEditor.getValue());
	}

	@Test
	public void testSetAsTextInvalidTextTrans() {
		regionColumnListEditor.setAsText(" transReqId ");
		assertEquals(RegionColumnList.transReqId, regionColumnListEditor.getValue());
	}

}
