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

package com.fanniemae.mbsportal.gf.functions;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.apache.geode.LogWriter;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.query.Query;
import org.apache.geode.cache.query.SelectResults;
import org.apache.geode.pdx.internal.PdxInstanceImpl;

import com.fanniemae.mbsportal.gf.pojo.MBSFilter;
import com.fanniemae.mbsportal.gf.pojo.MBSOperator;
import com.fanniemae.mbsportal.gf.pojo.Page;
import com.fanniemae.mbsportal.gf.pojo.Sort;

/**
 * @author g8uaxt Created on 12/14/2017.
 */
@SuppressWarnings("serial")
public class MBSQueryFunction implements Function, Declarable {

    /**
     * Logger variable
     */
    LogWriter LOGGER = CacheFactory.getAnyInstance().getLogger();

    /**
     * 
     * DATE_FORMAT_HYPHEN String
     */
    public static final String DATE_FORMAT_HYPHEN = "yyyy-MM-dd";

    /**
     * 
     * Override execute
     * 
     * @param ctx
     */
    @Override
    public void execute(FunctionContext ctx) {
        try {
            LOGGER.info("MBSQueryFunction called in GF Server");
            // RegionFunctionContext rctx = (RegionFunctionContext) ctx;
            LOGGER.error("Inside execute method ");
            Object[] args = (Object[]) ctx.getArguments();
            if (args.length < 1)
                throw new RuntimeException("MBSQueryFunction requires at least on argument (the query)");
            String opreationName = (String) args[0]; // TRANSACTION_HISTORY
            System.out.println("=======================opreationName====================" + opreationName);
            List<Object> queryArgsLst = new ArrayList();
            List<?> result = null;
            if (opreationName.equalsIgnoreCase("TRANSACTION_HISTORY")) {
                List<MBSFilter> mbsFilterLst = null;
                Page page = null;
                Sort sort = null;
                Integer limit = 0;
                mbsFilterLst = (List<MBSFilter>) getSerializable(args[1]);
                if (args.length > 1) {
                    if (args[2] != null) {
                        page = (Page) getSerializable(args[2]);
                    } else {
                        page = new Page(-1, 1);
                    }
                }
                if (args.length > 2) {
                    if (args[3] != null)
                        sort = (Sort) getSerializable(args[3]);
                }
                if (args.length > 3) {
                    if (args[4] != null)
                        limit = (Integer) getSerializable(args[4]);
                }

                LOGGER.info("MBSQueryFunction going to query with arg " + mbsFilterLst.size() + page + sort + limit);
                String query = buildQuery("MBSTransaction", mbsFilterLst, sort, limit, queryArgsLst, page);
                LOGGER.info("MBSQueryFunction result: " + query);
                Query q = CacheFactory.getAnyInstance().getQueryService().newQuery(query);
                if (args.length == 1)
                    result = ((SelectResults<?>) q.execute()).asList();
                else
                    LOGGER.info("params size" + queryArgsLst.size());
                result = ((SelectResults<?>) q.execute(queryArgsLst.toArray())).asList();
                LOGGER.info("MBSQueryFunction result: " + result);
                LOGGER.info("MBSQueryFunction result: size: " + result.size());
                result = filterResult(result, page);
                ctx.getResultSender().lastResult(result);
                LOGGER.info("MBSQueryFunction completed in GF Server");

            } else {

                LOGGER.error("MBSQueryFunction doesn't support this operation");
                throw new RuntimeException("MBSQueryFunction doesn't support this operation");
            }
            LOGGER.info("MBSQueryFunction Exit in GF Server");
        } catch (Throwable e) {
            LOGGER.error("MBSQueryFunction query execution failed", e);
            throw new RuntimeException("MBSQueryFunction query execution failed", e);
        }
    }

    private List<Object> filterResult(List<?> result, Page page) {
        if (page.getPosition() < 0) {
            return (List<Object>) result;
        }
        List<Object> filteredResults = new ArrayList<>();
        int pageSize = page.getSize();
        int offsetPosition = pageSize * (page.getPosition() - 1);
        for (int i = offsetPosition; i < result.size(); i++) {
            filteredResults.add(result.get(i));
        }
        return filteredResults;
    }

    private void exportMBSTransactions(FunctionContext ctx, Serializable[] args, String regionName,
            List<MBSFilter> mbsFilterLst) {
        SelectResults<?> result = null;
        List<Object> queryArgsLst = new ArrayList();
        try {

            String query = buildQuery(regionName, mbsFilterLst, null, null, queryArgsLst, null);
            LOGGER.info("exportMBSTransactions Function result: " + result);
            Query q = CacheFactory.getAnyInstance().getQueryService().newQuery(query);
            if (args.length == 1)
                result = (SelectResults<?>) q.execute();
            else
                LOGGER.info("params size" + queryArgsLst.size());
            result = (SelectResults<?>) q.execute(queryArgsLst.toArray());
            LOGGER.info("exportMBSTransactions result: " + result);
            LOGGER.info("exportMBSTransactions result: size: " + result.size());
            ctx.getResultSender().lastResult(result);
            LOGGER.info("exportMBSTransactions completed in GF Server");
        } catch (Throwable e) {
            LOGGER.error("exportMBSTransactions query execution failed", e);
            throw new RuntimeException("exportMBSTransactions query execution failed", e);
        }
    }

    /**
     * 
     * @param obj
     * @return Serializable
     */
    private Object getSerializable(Object obj) {
        if (obj instanceof PdxInstanceImpl) {
            return ((PdxInstanceImpl) obj).getObject();
        }
        return obj;
    }

    /**
     * 
     * buildQuery CMMBSSTA01-1202 - Created method for the filtering story
     * 
     * @param regionName
     * @param filter
     * @param sort
     * @param limit
     * @return String
     */
    protected String buildQuery(String regionName, List<MBSFilter> filter, Sort sort, Integer limit,
            List<Object> queryArgsLst, Page page) {
        //TODO: Use bind parameters
        StringBuilder query = new StringBuilder();
        query.append("select * from /").append(regionName);
        if (Objects.nonNull(filter) && filter.size() > 0) {
            LOGGER.info("inside buildQuery, inside  " + filter.size());
            query.append(" where ");
            for (MBSFilter mbsFilter : filter) {
                if (Objects.nonNull(mbsFilter.getValuesLst()) && mbsFilter.getValuesLst().size() > 0) {
                    query.append(buildFilterQuery(mbsFilter, queryArgsLst));
                    query.append(" and ");
                }
            }
            // remove last "and"
            query.delete(query.length() - 5, query.length());
        }
        // TODO: supports only only field?
        if (sort != null) {
            // if (sort.getFields()[0] != null) {
            // query.append(" order by ").append(sort.getFields()[0]).append("
            // ");
            // //Add one more field
            // }
            // CMMBSSTA01-1373 - Secondary Sort - Start
            query.append(" order by ");
            // TODO: supports only one primary field!!!!!!
            boolean primary = true;
            for (String sortColumn : sort.getFields()) {

                query.append(sortColumn).append(" ").append(primary ? sort.getSortBy().getName() : "desc");
                query.append(",");
                if (primary) {
                    primary = false;
                }
            }
            // remove the last comma
            query.deleteCharAt(query.length() - 1);
            query.append(" ");
            // CMMBSSTA01-1373 - Secondary Sort - End
            // if (sort.getSortBy() != null) {
            // query.append(sort.getSortBy().getName());
            // }
        }
        if (page == null || page.getSize() < 0) {
        } else {
            query.append(" " + "limit ").append(calculateLimit(page));
        }
        LOGGER.info("MBSQueryFunction query {}" + query.toString());
        return query.toString();
    }

    private String calculateLimit(Page page) {
        long pageSize = page.getSize();
        if (pageSize < 0) {
            return null;
        }
        long offsetLimit = pageSize * page.getPosition();
        return offsetLimit + "";
    }

    /**
     * 
     * buildFilterQuery CMMBSSTA01-1202 - Created method for the filtering story
     * 
     * @param mbsFilter
     *            MBSFilter
     * @return String
     */
    protected String buildFilterQuery(MBSFilter mbsFilter, List<Object> queryArgsLst) {
        StringBuilder queryWhereString = new StringBuilder();
        int counter = 1;
        if (Objects.nonNull(queryArgsLst)) {
            counter = queryArgsLst.size() + 1;
        }

        if (mbsFilter.getOperator().equals(MBSOperator.IN)) {
            queryWhereString.append(" ").append(mbsFilter.getColumnName()).append(" in SET( "); // "counterpartyTraderIdentifier
                                                                                                // in
            for (String value : mbsFilter.getValuesLst()) {
                queryWhereString.append("$").append(counter).append(",");
                queryArgsLst.add(value);
                counter++;
            }
            // remove the last comma
            queryWhereString.deleteCharAt(queryWhereString.length() - 1);
            queryWhereString.append(" ) ");
        } else if (mbsFilter.getOperator().equals(MBSOperator.NOT_IN)) {
            queryWhereString.append(" ").append("NOT (").append(mbsFilter.getColumnName()).append(" in SET( ");
            for (String value : mbsFilter.getValuesLst()) {
                queryWhereString.append("$").append(counter).append(",");
                queryArgsLst.add(value);
                counter++;
            }
            // remove the last comma
            queryWhereString.deleteCharAt(queryWhereString.length() - 1);
            queryWhereString.append(" )) ");
        } else if (mbsFilter.getOperator().equals(MBSOperator.GREATER_THAN_DATE)) {
            queryWhereString.append(" ").append(mbsFilter.getColumnName()).append(" > "); // "counterpartyTraderIdentifier
                                                                                          // in
            // There should only one value with date in the list
            for (String value : mbsFilter.getValuesLst()) {
                if (convertToDateWithFormatter(value, DATE_FORMAT_HYPHEN).getTime() != 0) {
                    queryWhereString.append("$").append(counter).append(",");
                    queryArgsLst.add(convertToDateWithFormatter(value, DATE_FORMAT_HYPHEN));
                    counter++;
                }
            }
            // remove the last comma
            queryWhereString.deleteCharAt(queryWhereString.length() - 1);
        } else if (mbsFilter.getOperator().equals(MBSOperator.LESSER_THAN_DATE)) {
            queryWhereString.append(" ").append(mbsFilter.getColumnName()).append(" < "); // "counterpartyTraderIdentifier
            // in
            // There should only one value with date in the list
            for (String value : mbsFilter.getValuesLst()) {
                if (convertToDateWithFormatter(value, DATE_FORMAT_HYPHEN).getTime() != 0) {
                    queryWhereString.append("$").append(counter).append(",");
                    queryArgsLst.add(convertToDateWithFormatter(value, DATE_FORMAT_HYPHEN));
                    counter++;
                }
            }
            // remove the last comma
            queryWhereString.deleteCharAt(queryWhereString.length() - 1);
        }
        return queryWhereString.toString();
    }

    /**
     *
     * @param value
     * @param format
     * @return
     * @throws MBSBaseException
     */
    public Date convertToDateWithFormatter(String value, String format) {
        DateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        if (Objects.isNull(value) || value.length() == 0) {
            date.setTime(0);
            return date;
        } else {
            try {
                date = simpleDateFormat.parse(value);
            } catch (ParseException ex) {
                date.setTime(0);
                return date;
            }
        }
        return date;
    }

    /**
     * 
     * @return String
     */
    @Override
    public String getId() {
        return getClass().getName();
    }

    /**
     * 
     * @return boolean
     */
    @Override
    public boolean hasResult() {
        return true;
    }

    /**
     * 
     * @return boolean
     */
    @Override
    public boolean isHA() {
        return false;
    }

    /**
     * 
     * @return boolean
     */
    @Override
    public boolean optimizeForWrite() {
        return false;
    }

    /**
     * 
     * @param props
     */
    @Override
    public void init(Properties props) {
    }
}
