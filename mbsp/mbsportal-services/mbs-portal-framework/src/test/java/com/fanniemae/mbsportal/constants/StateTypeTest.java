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

import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author Rajiv Chaudhuri
 * @Date Nov 15, 2017
 * @Time 11:33:55 AM com.fanniemae.mbsportal.constants StateTypeTest.java
 */
@RunWith(MockitoJUnitRunner.class)
public class StateTypeTest {
        
        @Before
        public void setUp() throws Exception {
        
        }
        
        @Test
        public void testGetStep() {
                
                Assert.assertTrue(StateType.LENDER_OPEN.getStep() > 0);
                Assert.assertTrue(StateType.TRADER_PRICED.getStep() > 0);
                Assert.assertTrue(StateType.TRADER_REPRICED.getStep() > 0);
                Assert.assertTrue(StateType.TRADER_PASSED.getStep() > 0);
                Assert.assertTrue(StateType.LENDER_ACCEPTED.getStep() > 0);
                Assert.assertTrue(StateType.LENDER_REJECTED.getStep() > 0);
                Assert.assertTrue(StateType.TRADER_CONFIRMED.getStep() > 0);
                Assert.assertTrue(StateType.TRADER_REJECTED.getStep() > 0);
                Assert.assertTrue(StateType.PENDING_EXECUTION.getStep() > 0);
                Assert.assertTrue(StateType.EXECUTED.getStep() > 0);
                Assert.assertTrue(StateType.LENDER_TIMEOUT.getStep() > 0);
                Assert.assertTrue(StateType.TRADER_TIMEOUT.getStep() > 0);
                Assert.assertTrue(StateType.ERROR.getStep() > 0);
        }
        
        @Test
        public void isValidState() {
                
                assertTrue(StateType.TRADER_PRICED.isValidState(StateType.LENDER_OPEN));
                assertTrue(StateType.TRADER_PASSED.isValidState(StateType.LENDER_OPEN));
                
                assertTrue(StateType.LENDER_ACCEPTED.isValidState(StateType.TRADER_PRICED));
                assertTrue(StateType.TRADER_REJECTED.isValidState(StateType.TRADER_PRICED));
                
                assertTrue(StateType.LENDER_REJECTED.isValidState(StateType.TRADER_PRICED));
                assertTrue(StateType.LENDER_REJECTED.isValidState(StateType.LENDER_OPEN));
                
                
                assertTrue(StateType.TRADER_CONFIRMED.isValidState(StateType.LENDER_ACCEPTED));
                
                assertTrue(StateType.PENDING_EXECUTION.isValidState(StateType.TRADER_CONFIRMED));
                
                assertTrue(StateType.EXECUTED.isValidState(StateType.PENDING_EXECUTION));
                
                //        assertFalse(StateType.LENDER_TIMEOUT.isValidState(null));
                //        assertFalse(StateType.TRADER_TIMEOUT.isValidState(null));
                //        assertFalse(StateType.ERROR.isValidState(null));
                
                assertTrue(StateType.LENDER_REJECTED.isValidState(StateType.LENDER_ACCEPTED));
                
        }
        
        @Test
        public void getStateTypeForLenderOpen() {
                
                String stateType = StateType.getStateType("LENDER_OPEN");
                assertTrue(stateType.equals(StateType.LENDER_OPEN.name().toString()));
        }
        
        @Test
        public void getEnumForLenderOpen() {
                
                StateType stateType = StateType.getEnum("LENDER_OPEN");
                assertTrue(stateType.getStep() == StateType.LENDER_OPEN.getStep());
        }
        
        @Test
        public void getFlowListLender() {
                
                List<StateType> stateLst = StateType.getFlowList("LENDER_FLOW");
                assertTrue(stateLst.contains(StateType.LENDER_OPEN));
                
                stateLst = StateType.getFlowList("TRADER_FLOW");
                assertTrue(stateLst.contains(StateType.TRADER_PRICED));
                
                stateLst = StateType.getFlowList("LENDER_HISTORY_FLOW");
                assertTrue(stateLst.contains(StateType.TRADER_CONFIRMED));
                
                stateLst = StateType.getFlowList("TRADER_HISTORY_FLOW");
                assertTrue(stateLst.contains(StateType.PENDING_EXECUTION));
                
                stateLst = StateType.getFlowList("NOT_MATCHING");
                assertTrue(stateLst.contains(StateType.LENDER_OPEN));
                
                stateLst = StateType.getFlowList("TRADER_HISTORY_FLOW");
                assertTrue(stateLst.contains(StateType.TRADER_TIMEOUT));
                
                stateLst = StateType.getFlowList("LENDER_HISTORY_FLOW");
                assertTrue(stateLst.contains(StateType.LENDER_TIMEOUT));
        }
        
        @Test
        public void isValidStateForTraderRePriced() {
                assertTrue(StateType.TRADER_REPRICED.isValidState(StateType.TRADER_PRICED));
                assertTrue(StateType.TRADER_REPRICED.isValidState(StateType.TRADER_REPRICED));
                assertTrue(StateType.TRADER_REPRICED.isValidState(StateType.LENDER_ACCEPTED));
        }
        
        @Test
        public void notValidStateForTraderRePriced() {
                assertFalse(StateType.TRADER_REPRICED.isValidState(StateType.LENDER_OPEN));
                assertFalse(StateType.TRADER_REPRICED.isValidState(StateType.TRADER_TIMEOUT));
                assertFalse(StateType.TRADER_REPRICED.isValidState(StateType.LENDER_TIMEOUT));
        }
        
        @Test
        public void getFlowListForTraderRePricedLenderFlow() {
                List<StateType> stateLst = StateType.getFlowList("LENDER_FLOW");
                assertTrue(stateLst.contains(StateType.TRADER_REPRICED));
        }
        
        @Test
        public void getFlowListForTraderRePricedTraderFlow() {
                List<StateType> stateLst = StateType.getFlowList("TRADER_FLOW");
                assertTrue(stateLst.contains(StateType.TRADER_REPRICED));
        }
        
}
