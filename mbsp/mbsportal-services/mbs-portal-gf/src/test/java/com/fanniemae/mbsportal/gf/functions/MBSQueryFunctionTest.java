///*
// *
// *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
// *  reserved under the copyright laws of the United States and international
// *  conventions. Use of a copyright notice is precautionary only and does not
// *  imply publication or disclosure. This software contains confidential
// *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
// *  is prohibited without the prior written consent of Fannie Mae.
// *
// */
//
//package com.fanniemae.mbsportal.gf.functions;
//
//import com.fanniemae.fnmpfj.gemfire.utils.client.QueryResultCollector;
//import com.fanniemae.mbsportal.gf.pojo.Filter;
//import com.fanniemae.mbsportal.gf.pojo.Page;
//import com.fanniemae.mbsportal.gf.pojo.Sort;
//import com.fanniemae.mbsportal.gf.pojo.SortBy;
//import com.fanniemae.mbsportal.po.TransactionHistory;
//import java.io.Serializable;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import org.apache.geode.cache.execute.Execution;
//import org.apache.geode.cache.execute.FunctionService;
//import org.apache.geode.cache.execute.ResultCollector;
//
//
///**
// * @author g8uaxt Created on 12/14/2017.
// */
//
//public class MBSQueryFunctionTest {
//        
//        @SuppressWarnings("rawtypes")
//        org.apache.geode.cache.Region region;
//     
//        public static void main(String[] args) {
//                MBSQueryFunctionTest mbsQueryFunctionTest = new MBSQueryFunctionTest();
//                //mbsQueryFunctionTest.formFilterQuery();
//                mbsQueryFunctionTest.formQuery();
//        }
//        
//        // @Test
//        public void execute() {
//               // String QUERY_FUNCTION = "MBSQueryFunction";
//                
//                String QUERY_FUNCTION = MBSQueryFunction.class.getName();
//                Map fields = new HashMap<String,String>();
//                fields.put("transReqNumber","AJ0003");
//                Filter filter = new Filter(fields);
//                Page page = new Page(20,3);
//                Sort sort = new Sort(Arrays.asList("tradeTraderIdentifierText","transReqNumber"), SortBy.desc);
//                //prepare args
//                Serializable[] args = new Serializable[]{"TRANSACTION_HISTORY",filter,page,sort,new Integer(100)};
//                Execution exec = FunctionService.onRegion(region).withArgs(args)
//                        .withCollector(new QueryResultCollector());
//                //ResultCollector<SelectResults<Object>, List<Object>> result = exec.execute(QUERY_FUNCTION);
//                ResultCollector<?,?> result = exec.execute(QUERY_FUNCTION);
//                List<TransactionHistory> mbsTransactionRequests = (List<TransactionHistory>) result.getResult();
//                System.out.println("mbsTransactionRequests: "+mbsTransactionRequests);
//                System.out.println("mbsTransactionRequests size: "+mbsTransactionRequests.size());
//                
//        }
//        
//        public void formQuery(){
//                Map<String, List<String>> filter = new HashMap();
//                filter.put("stateType", Arrays.asList("LENDER_REJECTED","TRADER_TIMEOUT"));
//                filter.put("counterpartyTraderIdentifier", Arrays.asList("p3hbrmxe","p3hbrmxe1"));
//                Filter filters = new Filter(filter);
//                Sort sort = new Sort(Arrays.asList("submissionDate"), SortBy.desc);
//                Integer limit = new Integer(10);
//                MBSQueryFunction mbsQueryFunction = new MBSQueryFunction();
////                String query = mbsQueryFunction.formQuery("MBSTransaction",filters, sort, limit);
////                System.out.println(query);
//        }
//        
//        public void formFilterQuery(){
//                MBSQueryFunction mbsQueryFunction = new MBSQueryFunction();
////                String query = mbsQueryFunction.formFilterQuery("stateType",Arrays.asList("LENDER_REJECTED","TRADER_TIMEOUT"));
////                System.out.println(query);
//        }
//}
