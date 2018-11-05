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

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.fanniemae.mbsportal.gf.pojo.SortBy;


/**
 * @author FannieMae
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SortByEditorTest {
	
	@InjectMocks
	SortByEditor sortByEditor;
	
	@Test
	public void testSetAsTextNullText() {
		sortByEditor.setAsText(null);
		assertEquals(SortBy.empty, sortByEditor.getValue());
	}
	
	@Test
	public void testSetAsTextEmptyText() {
		sortByEditor.setAsText("");
		assertEquals(SortBy.empty, sortByEditor.getValue());
	}

	@Test
	public void testSetAsTextBlankText() {
		sortByEditor.setAsText("  ");
		assertEquals(SortBy.empty, sortByEditor.getValue());
	}

	@Test
	public void testSetAsTextInvalidText() {
		sortByEditor.setAsText(" asc desc ");
		assertNull(sortByEditor.getValue());
	}

	@Test
	public void testSetAsTextAsc() {
		sortByEditor.setAsText(" asc ");
		assertEquals(SortBy.asc, sortByEditor.getValue());
	}

	@Test
	public void testSetAsTextDesc() {
		sortByEditor.setAsText("desc ");
		assertEquals(SortBy.desc, sortByEditor.getValue());
	}
	
	@Test
	public void testSetAsTextException() {
		sortByEditor.setAsText("desc ");
		assertEquals(SortBy.desc, sortByEditor.getValue());
	}

}
