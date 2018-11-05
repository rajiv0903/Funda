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

import com.fanniemae.mbsportal.constants.AcceptedTradesBoolean;

/**
 * @author FannieMae
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AcceptedTradesBooleanEditorTest {

	@InjectMocks
	AcceptedTradesBooleanEditor acceptedTradesBooleanEditor;
	
	@Test
	public void testSetAsTextNullText() {
		acceptedTradesBooleanEditor.setAsText(null);
		assertEquals(AcceptedTradesBoolean.empty, acceptedTradesBooleanEditor.getValue());
	}
	
	@Test
	public void testSetAsTextEmptyText() {
		acceptedTradesBooleanEditor.setAsText("");
		assertEquals(AcceptedTradesBoolean.empty, acceptedTradesBooleanEditor.getValue());
	}

	@Test
	public void testSetAsTextBlankText() {
		acceptedTradesBooleanEditor.setAsText("  ");
		assertEquals(AcceptedTradesBoolean.empty, acceptedTradesBooleanEditor.getValue());
	}

	@Test
	public void testSetAsTextInvalidText() {
		acceptedTradesBooleanEditor.setAsText("f alse");
		assertNull(acceptedTradesBooleanEditor.getValue());
	}

	@Test
	public void testSetAsTextAsc() {
		acceptedTradesBooleanEditor.setAsText(" true ");
		assertEquals(AcceptedTradesBoolean.trueValue, acceptedTradesBooleanEditor.getValue());
	}

	@Test
	public void testSetAsTextDesc() {
		acceptedTradesBooleanEditor.setAsText("false ");
		assertEquals(AcceptedTradesBoolean.falseValue, acceptedTradesBooleanEditor.getValue());
	}
	@Test
	public void testSetAsTextValueDesc() {
		acceptedTradesBooleanEditor.setAsText("falseValue ");
		assertEquals(AcceptedTradesBoolean.falseValue, acceptedTradesBooleanEditor.getValue());
		acceptedTradesBooleanEditor.setAsText("trueValue ");
		assertEquals(AcceptedTradesBoolean.trueValue, acceptedTradesBooleanEditor.getValue());
	}
}
