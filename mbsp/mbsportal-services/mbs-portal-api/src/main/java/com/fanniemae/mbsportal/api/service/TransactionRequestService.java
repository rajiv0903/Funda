/*
 *
 *  Copyright (c) 2018 Fannie Mae. All rights reserved. Unpublished -- Rights
 *  reserved under the copyright laws of the United States and international
 *  conventions. Use of a copyright notice is precautionary only and does not
 *  imply publication or disclosure. This software contains confidential
 *  information and trade secrets of Fannie Mae. Use, disclosure, or reproduction
 *  is prohibited without the prior written consent of Fannie Mae.
 */

package com.fanniemae.mbsportal.api.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.api.config.TradeServiceProperties;
import com.fanniemae.mbsportal.api.po.ProductIdPO;
import com.fanniemae.mbsportal.api.po.ProductPO;
import com.fanniemae.mbsportal.api.po.ProductPricingPO;
import com.fanniemae.mbsportal.api.po.ProfileEntitlementPO;
import com.fanniemae.mbsportal.api.po.TransactionRequestPO;
import com.fanniemae.mbsportal.api.utils.MBSObjectCreator;
import com.fanniemae.mbsportal.api.utils.MBSUtils;
import com.fanniemae.mbsportal.constants.MBSRoleType;
import com.fanniemae.mbsportal.constants.StateType;
import com.fanniemae.mbsportal.dao.MBSMarketIndicativePriceDao;
import com.fanniemae.mbsportal.dao.MBSQueryFunctionDao;
import com.fanniemae.mbsportal.dao.MBSTransactionRequestDao;
import com.fanniemae.mbsportal.enrichment.Enricher;
import com.fanniemae.mbsportal.gf.pojo.MBSFilter;
import com.fanniemae.mbsportal.gf.pojo.MBSOperator;
import com.fanniemae.mbsportal.gf.pojo.Page;
import com.fanniemae.mbsportal.gf.pojo.Sort;
import com.fanniemae.mbsportal.gf.pojo.SortBy;
import com.fanniemae.mbsportal.model.MBSMarketIndicativePrice;
import com.fanniemae.mbsportal.model.MBSTransactionRequest;
import com.fanniemae.mbsportal.persist.Persister;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.publisher.MessagePublisher;
import com.fanniemae.mbsportal.service.BaseProcessor;
import com.fanniemae.mbsportal.transformation.Transformer;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.DateFormats;
import com.fanniemae.mbsportal.utils.constants.ErrorMessage;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;
import com.fanniemae.mbsportal.utils.exception.ExceptionLookupPO;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;
import com.fanniemae.mbsportal.utils.logging.InjectLog;
import com.fanniemae.mbsportal.validator.Validator;

/**
 * This class handles the service part for the Lenders trade requests
 *
 * @author g8upjv
 *
 */
@SuppressWarnings("rawtypes")
@Service
public class TransactionRequestService extends BaseProcessor {

    /**
     *
     * profileEntitlementService ProfileEntitlementService
     */
    @Autowired
    ProfileEntitlementService profileEntitlementService;

    /**
     *
     * mbsQueryFunctionDao MBSQueryFunctionDao
     */
    @Autowired
    MBSQueryFunctionDao mbsQueryFunctionDao;

    /**
     *
     * LOGGER Logger variable
     */
    @InjectLog
    private Logger LOGGER;

    /**
     *
     * productService ProductService
     */
    @Autowired
    private ProductService productService;

    /**
     *
     * productPricingService ProductPricingService
     */
    @Autowired
    private ProductPricingService productPricingService;

    /**
     *
     * tradeServiceProperties TradeServiceProperties
     */
    @Autowired
    TradeServiceProperties tradeServiceProperties;

    /**
     *
     * mbspProperties MbspProperties
     */
    @Autowired
    MbspProperties mbspProperties;

    /**
     *
     * mbsObjectCreator MBSObjectCreator
     */
    @Autowired
    MBSObjectCreator mbsObjectCreator;

    /**
     *
     * exceptionLookupService ExceptionLookupService
     */
    @Autowired
    private ExceptionLookupService exceptionLookupService;

    /**
     *
     * mbsMarketIndicativePriceDao MBSMarketIndicativePriceDao
     */
    @Autowired
    MBSMarketIndicativePriceDao mbsMarketIndicativePriceDao;

    /**
     *
     *
     * @param transactionRequestTransformer
     *            Transformer
     * @param transactionRequestPOValidator
     *            PO Validator
     * @param transactionRequestValidator
     *            Validator
     * @param transactionPersister
     *            Persister
     * @param transactionRequestPOTransformer
     *            PO Transformer
     * @param transactionRequestEnrichment
     *            Enricher
     * @param transactionRequestPublisher
     *            Publisher
     */
    @SuppressWarnings("unchecked")
    @Autowired
    public TransactionRequestService(Transformer transactionRequestTransformer, Validator transactionRequestPOValidator,
            Validator transactionRequestValidator, Persister transactionPersister,
            Transformer transactionRequestPOTransformer, Enricher transactionRequestEnrichment,
            MessagePublisher transactionRequestMessagePublisher) {
        super.transformer = transactionRequestTransformer;
        super.validator = transactionRequestPOValidator;
        super.enricher = transactionRequestEnrichment;
        super.persister = transactionPersister;
        super.domainValidator = transactionRequestValidator;
        super.poTransformer = transactionRequestPOTransformer;
        super.messagePublisher = transactionRequestMessagePublisher;
    }

    /**
     *
     * @param transactionRequestPO
     *            TransactionRequestPO
     * @param mbsRoleType
     *            MBSRoleType
     * @return TransactionRequestPO
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    public TransactionRequestPO createMBSTransReq(TransactionRequestPO transactionRequestPO, MBSRoleType mbsRoleType,
            Map<String, String> headers) throws MBSBaseException {
        LOGGER.debug("Entering createMBSTransReq method in TransactionRequestService");
        Map<String, ExceptionLookupPO> exceptionLookupPOMap = getExceptionMap();
        String productNameCode = StringUtils.EMPTY;
        /*
         * Check if matching product is available
         */
        if (!Objects.equals(transactionRequestPO, null)) {
            if (Objects.equals(transactionRequestPO.getProduct(), null)
                    || Objects.equals(getProduct(transactionRequestPO.getProduct().retrieveKey()), null)) {
                LOGGER.error(MBSPServiceConstants.NO_MATCHING_PRODUCT);
                throw new MBSBusinessException(MBSPServiceConstants.NO_MATCHING_PRODUCT,
                        MBSExceptionConstants.BUSINESS_EXCEPTION);
            } else {
                ProductPO productPO = getProduct(transactionRequestPO.getProduct().retrieveKey());
                productNameCode = productPO.getNameCode();
                transactionRequestPO.getProduct().setNameCode(productNameCode);
            }
            if (!transactionRequestPO.getStateType().equals(StateType.LENDER_OPEN)) {
                LOGGER.error(transactionRequestPO.getStateType().toString() + MBSPServiceConstants.NOT_ELIGIBLE_STATUS);
                throw new MBSBusinessException(
                        transactionRequestPO.getStateType().toString() + MBSPServiceConstants.NOT_ELIGIBLE_STATUS,
                        MBSExceptionConstants.BUSINESS_EXCEPTION);
            }
        }
        TransformationObject transformationObject = mbsObjectCreator.getTransformationObject();
        // CMMBSSTA01-1157 - Stream Pricing - Start
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE,
                getStreamedPricing(productNameCode, transactionRequestPO.getTradeCouponRate(),
                        transactionRequestPO.getTradeSettlementDate()));
        // CMMBSSTA01-1157 - Stream Pricing - End

        List<ProductPricingPO> productPricingPOLst = getProductPricing(
                transactionRequestPO.getProduct().getProductId(), true); // filtering the record
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.PRODUCT_PRICING, productPricingPOLst);
        // CMMBSSTA01-874 - Start for handling exception
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.EXCEPTION_MESSAGES,
                exceptionLookupPOMap);
        // CMMBSSTA01-874 - End for handling exception
        transformationObject.setReqHeaderMap(headers);
        // Passing the sourceType - Lender/Trader
        transformationObject.setMBSRoleType(mbsRoleType);
        transformationObject.setSourcePojo(transactionRequestPO);
        super.processRequest(transformationObject);
        TransactionRequestPO transactionRequestPOResponse = (TransactionRequestPO) transformationObject.getSourcePojo();
        transactionRequestPOResponse.setProduct(getProduct(transactionRequestPOResponse.getProduct().retrieveKey()));
        LOGGER.debug("Exiting createMBSTransReq method in TransactionRequestService");
        return transactionRequestPOResponse;
    }

    /**
     *
     * Clears all the trades in this region
     * 
     * @throws MBSBaseException
     */
    public void clearAll() throws MBSBaseException {
        persister.clearAll();
    }

    /**
     *
     * update lender price submit
     *
     * @param transactionRequestPO
     *            TransactionRequestPO
     * @return TransactionRequestPO
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    public TransactionRequestPO updateMBSTransReq(TransactionRequestPO transactionRequestPO, MBSRoleType mbsRoleType,
            Map<String, String> headers) throws MBSBaseException {
        LOGGER.debug("Entering updateMBSTransReq method in TransactionRequestService");
        // setInitialTradeInfo(transactionRequestPO, mbsRoleType);
        TransformationObject transformationObject = mbsObjectCreator.getTransformationObject();
        // CMMBSSTA01-874 - Start for handling exception
        Map<String, ExceptionLookupPO> exceptionLookupPOMap = getExceptionMap();
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.EXCEPTION_MESSAGES,
                exceptionLookupPOMap);
        // CMMBSSTA01-874 - End for handling exception
        ProfileEntitlementPO lenderProfileEntitlementPO = null;
        ProfileEntitlementPO traderProfileEntitlementPO = null;
        transformationObject.setReqHeaderMap(headers);
        transformationObject.setSourcePojo(transactionRequestPO);
        transformationObject.setMBSRoleType(mbsRoleType);

        // CMMBSSTA01-1982 - start
        // MBSTransactionRequest mbsTransaction = (MBSTransactionRequest)
        // persister.getBaseDao()
        // .getById(transactionRequestPO.getTransReqId());
        MBSTransactionRequest mbsTransaction = ((MBSTransactionRequestDao) persister.getBaseDao())
                .getTransReqByTransReqNumber(transactionRequestPO.getTransReqId());
        // CMMBSSTA01-1982 - end
        // CMMBSSTA01-787: Validate if the source system matches
        if (!validSourceSystem(mbsTransaction)) {
            LOGGER.error(MBSPServiceConstants.NO_DATA);
            throw new MBSBusinessException(MBSPServiceConstants.NO_DATA, MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        // CMMBSSTA01-787: End -change
        transformationObject.setTargetPojo(mbsTransaction);

        // CMMBSSTA01-1157 - Stream Pricing - Start
        transformationObject.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE,
                getStreamedPricing(mbsTransaction.getProductNameCode(),
                        MBSPortalUtils.convertToString(mbsTransaction.getTradeCouponRate(), 5, 2),
                        MBSPortalUtils.convertDateToString(mbsTransaction.getTradeSettlementDate(),
                                DateFormats.DATE_FORMAT_NO_TIMESTAMP)));
        // CMMBSSTA01-1157 - Stream Pricing - End

        /*
         * CMMBSSTA01-1107: API Integration Validation- User Has to Be Valid If
         * Lender Role - Expect Lender ID Always If Trader - And Lender Open
         * State then Trader ID may optional; otherwise Trader ID should be
         * there
         */
        if (MBSRoleType.LENDER.equals(mbsRoleType)) {

            lenderProfileEntitlementPO = profileEntitlementService.getProfile(transactionRequestPO.getLenderId());
            if (Objects.equals(lenderProfileEntitlementPO, null)
                    || StringUtils.isBlank(lenderProfileEntitlementPO.getPartyShortName())) {
                LOGGER.error(ErrorMessage.INVALID_MAPPED_LENDER_TO_PERFORM_ACTION.value()
                        + ", User does not have party short name, User Name:" + transactionRequestPO.getLenderId());
                throw new MBSBusinessException(ErrorMessage.INVALID_MAPPED_LENDER_TO_PERFORM_ACTION.value(),
                        MBSExceptionConstants.BUSINESS_EXCEPTION);
            }
        }
        // Not a soft lock situation
        if (MBSRoleType.TRADER.equals(mbsRoleType)) {
            if (!transactionRequestPO.getStateType().equals(StateType.LENDER_OPEN)) {

                traderProfileEntitlementPO = profileEntitlementService.getProfile(transactionRequestPO.getTraderId());
                if (Objects.equals(traderProfileEntitlementPO, null)
                        || StringUtils.isBlank(traderProfileEntitlementPO.getBrsUserName())) {
                    LOGGER.error(ErrorMessage.INVALID_MAPPED_TRADER_TO_PERFORM_ACTION.value()
                            + ", User does not have brs user name, User Name:" + transactionRequestPO.getLenderId());
                    throw new MBSBusinessException(ErrorMessage.INVALID_MAPPED_TRADER_TO_PERFORM_ACTION.value(),
                            MBSExceptionConstants.BUSINESS_EXCEPTION);
                }
            }
        }
        // End

        super.processRequest(transformationObject);
        TransactionRequestPO transactionRequestPOResponse = (TransactionRequestPO) transformationObject.getSourcePojo();
        transactionRequestPOResponse.setProduct(getProduct(transactionRequestPOResponse.getProduct().retrieveKey()));

        /*
         * For Lender Activity Or Trader - Lender Open Soft Lock- Trader may not
         * come from UI Hence Fetch from Persisted Data
         */
        if (Objects.nonNull(transactionRequestPOResponse.getTraderId())) {
            // If null then fetch from persisted Data - Lender Activity
            if (Objects.equals(traderProfileEntitlementPO, null)) {
                traderProfileEntitlementPO = profileEntitlementService
                        .getProfile(transactionRequestPOResponse.getTraderId());
            }
            // Assign Trader Name if Exists
            // TODO: This transformation is not required as Data is being stored
            // F.LN and that is what UI needs everywhere - See Enricher
            if (!Objects.equals(traderProfileEntitlementPO, null)) {
                transactionRequestPOResponse.setTraderName(
                        traderProfileEntitlementPO.getLastName() + "," + traderProfileEntitlementPO.getFirstName());
            }
        }
        LOGGER.debug("Exiting updateMBSTransReq method in TransactionRequestService");
        return transactionRequestPOResponse;
    }

    /**
     *
     * Purpose: This method returns the list of all the Transaction Request
     * object from the Gemfire data store for lender
     *
     * @param lenderId
     *            String
     * @param transReqId
     *            Optional String
     * @param stateType
     *            List<String>
     * @param mbsRoleType
     *            MBSRoleType
     * @return List<TransactionRequestPO> The list of Transaction Request values
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    public List<TransactionRequestPO> getMBSTransReq(String lenderId, Optional<String> transReqId,
            List<String> stateType, MBSRoleType mbsRoleType) throws MBSBaseException {

        LOGGER.debug("Entering getMBSTransReq method in TransactionRequestService");
        List<TransactionRequestPO> listTransReqPO = new ArrayList<TransactionRequestPO>();
        List<MBSTransactionRequest> listMBSTransReq = new ArrayList<MBSTransactionRequest>();
        if (mbsRoleType.equals(MBSRoleType.LENDER) || mbsRoleType.equals(MBSRoleType.TSP)) {
            listMBSTransReq = getLenderTransactions(transReqId, stateType, lenderId);
        } else if (mbsRoleType.equals(MBSRoleType.TRADER)) {
            listMBSTransReq = getTraderTransactions(transReqId, stateType);
        }
        TransactionRequestPO mbsTransactionPO;
        TransformationObject transObj;
        ProfileEntitlementPO profileEntitlementPO;
        if (!Objects.equals(listMBSTransReq, null) && listMBSTransReq.size() > 0) {
            LOGGER.debug("Size of list: " + listMBSTransReq.size());
            for (MBSTransactionRequest mbsTransReq : listMBSTransReq) {
                transObj = mbsObjectCreator.getTransformationObject();
                transObj.setMBSRoleType(mbsRoleType);
                transObj.setTargetPojo(mbsTransReq);
                // CMMBSSTA01-1157 - Stream Pricing - Start
                transObj.getTransformationDataMap().put(MBSPServiceConstants.STREAM_PRICE,
                        getStreamedPricing(mbsTransReq.getProductNameCode(),
                                MBSPortalUtils.convertToString(mbsTransReq.getTradeCouponRate(), 5, 2),
                                MBSPortalUtils.convertDateToString(mbsTransReq.getTradeSettlementDate(),
                                        DateFormats.DATE_FORMAT_NO_TIMESTAMP)));
                // CMMBSSTA01-1157 - Stream Pricing - End
                mbsTransactionPO = (TransactionRequestPO) (poTransformer.transform(transObj)).getSourcePojo();
                mbsTransactionPO.setProduct(getProduct(mbsTransactionPO.getProduct().retrieveKey()));
                // Populate Entity name
                profileEntitlementPO = profileEntitlementService.getProfile(mbsTransactionPO.getLenderId());
                // TODO: This transformation is not required as Data is being
                // stored F.LN and that is what UI needs everywhere
                // Trader name
                if (Objects.nonNull(mbsTransactionPO.getTraderId())) {
                    profileEntitlementPO = profileEntitlementService.getProfile(mbsTransactionPO.getTraderId());
                    if (!Objects.equals(profileEntitlementPO, null)) {
                        mbsTransactionPO.setTraderName(
                                profileEntitlementPO.getLastName() + "," + profileEntitlementPO.getFirstName());
                    }
                }
                listTransReqPO.add(mbsTransactionPO);
            }
        }
        LOGGER.debug("Exiting getMBSTransReq method in TransactionRequestService");
        return listTransReqPO;
    }

    /**
     *
     * This method returns the lender transaction requests based on the input
     * queries transid, statetype and lenderid
     *
     * @param transReqId
     *            the transReqId
     * @param stateType
     *            the stateType
     * @param lenderId
     *            the lenderId
     * @return List<MBSTransactionRequest>
     * @throws MBSBaseException
     */
    public List<MBSTransactionRequest> getLenderTransactions(Optional<String> transReqId, List<String> stateType,
            String lenderId) throws MBSBaseException {
        List<MBSTransactionRequest> listMBSTransReq = new ArrayList<MBSTransactionRequest>();
        if (transReqId.isPresent() && StringUtils.isNotEmpty(transReqId.get())) {
            // CMMBSSTA01-1982 - start
            // MBSTransactionRequest mbsTransactionRequest =
            // (MBSTransactionRequest) ((MBSTransactionRequestDao) persister
            // .getBaseDao()).getById(transReqId.get());
            MBSTransactionRequest mbsTransactionRequest = ((MBSTransactionRequestDao) persister.getBaseDao())
                    .getTransReqByTransReqNumber(transReqId.get());
            // CMMBSSTA01-1982 - end
            // CMMBSSTA01-787: Validate if the source system matches
            if (validSourceSystem(mbsTransactionRequest)) {
                // CMMBSSTA01-787: End -change
                if (mbsTransactionRequest.getCounterpartyTraderIdentifier().equalsIgnoreCase(lenderId)) {
                    listMBSTransReq.add(mbsTransactionRequest);
                }
            } else {
                LOGGER.error(MBSPServiceConstants.NO_DATA);
                throw new MBSBusinessException(MBSPServiceConstants.NO_DATA, MBSExceptionConstants.BUSINESS_EXCEPTION);
            }
        } else {
            List<String> lenderLst = new ArrayList<String>();
            lenderLst.add(lenderId);
            listMBSTransReq = ((MBSTransactionRequestDao) persister.getBaseDao()).getTransReqStateType(stateType,
                    lenderLst, "asc", tradeServiceProperties.getSourceSystem());
        }
        return listMBSTransReq;
    }

    /**
     *
     * This method returns the trader transaction requests based on the input
     * queries transid, statetype and lenderid
     *
     * @param transReqId
     *            the transReqId
     * @param stateType
     *            the stateType
     * @return List<MBSTransactionRequest>
     * @throws MBSBaseException
     */
    // TODO: "To be converted to use GF function dao"
    public List<MBSTransactionRequest> getTraderTransactions(Optional<String> transReqId, List<String> stateType)
            throws MBSBaseException {
        List<MBSTransactionRequest> listMBSTransReq = new ArrayList<MBSTransactionRequest>();
        if (transReqId.isPresent() && StringUtils.isNotEmpty(transReqId.get())) {
            // CMMBSSTA01-787: Validate if the source system matches

            // CMMBSSTA01-1982 - start
            // MBSTransactionRequest mbsTransactionRequest =
            // (MBSTransactionRequest) ((MBSTransactionRequestDao) persister
            // .getBaseDao()).getById(transReqId.get());
            MBSTransactionRequest mbsTransactionRequest = ((MBSTransactionRequestDao) persister.getBaseDao())
                    .getTransReqByTransReqNumber(transReqId.get());
            // CMMBSSTA01-1982 - end

            if (validSourceSystem(mbsTransactionRequest)) {
                // CMMBSSTA01-787: End -change
                listMBSTransReq.add(mbsTransactionRequest);
            } else {
                LOGGER.error(MBSPServiceConstants.NO_DATA);
                throw new MBSBusinessException(MBSPServiceConstants.NO_DATA, MBSExceptionConstants.BUSINESS_EXCEPTION);
            }
        } else {
            listMBSTransReq = ((MBSTransactionRequestDao) persister.getBaseDao()).getTransReqStateType(stateType, null,
                    "asc", tradeServiceProperties.getSourceSystem());
        }
        return listMBSTransReq;
    }

    /**
     *
     * Purpose: This method returns the list of all the Transaction Request
     * object from the Gemfire data store for lender
     *
     * CMMBSSTA01-1202 - Created method for the filtering story
     *
     * @param mbsRoleType
     *            MBSRoleType
     * @param stateTypeEnumLst
     *            List<StateType>
     * @param sortBy
     *            String
     * @param sortOrder
     *            String
     * @param pageIndex
     *            Integer
     * @param pageSize
     *            Integer
     * @return List<TransactionRequestPO> The list of Transaction Request values
     * @throws MBSBaseException
     */
    @SuppressWarnings("unchecked")
    public List<TransactionRequestPO> getMBSTransReqHistory(MBSRoleType mbsRoleType, List<StateType> stateTypeEnumLst,
            String sellerServiceNumber, String sortBy, String sortOrder, Integer pageIndex, Integer pageSize)
            throws MBSBaseException {
        LOGGER.debug("Entering getMBSTransReqHistory method in TransactionRequestService");
        List<String> strStateTypeLst;
        if (Objects.equals(stateTypeEnumLst, null)) {
            LOGGER.error(MBSPServiceConstants.NO_STATETYPE_LIST);
            throw new MBSSystemException(MBSPServiceConstants.NO_STATETYPE_LIST,
                    MBSExceptionConstants.SYSTEM_EXCEPTION);
        }
        strStateTypeLst = MBSUtils.convertStateTypeToStringList(stateTypeEnumLst);
        List<TransactionRequestPO> listTransReqPO = new ArrayList<TransactionRequestPO>();
        List<MBSTransactionRequest> listMBSTransReq = null;
        // create the param args here
        List<MBSFilter> mbsFilterLst = new ArrayList();
        // Filter for stateType
        mbsFilterLst.add(new MBSFilter("stateType", strStateTypeLst, MBSOperator.IN));
        // Filter for users
        // CMMBSSTA01-1373 - Adding TSP name - Start
        List<String> sellerServiceNumberLst = new ArrayList();
        sellerServiceNumberLst.add(sellerServiceNumber);
        if (mbsRoleType.equals(MBSRoleType.LENDER)) {
            mbsFilterLst.add(new MBSFilter("lenderSellerServiceNumber", sellerServiceNumberLst, MBSOperator.IN));
        } else if (mbsRoleType.equals(MBSRoleType.TSP)) {
            mbsFilterLst.add(new MBSFilter("tspSellerServiceNumber", sellerServiceNumberLst, MBSOperator.IN));
        }

        mbsFilterLst.add(new MBSFilter("lenderShortName", Arrays.asList(tradeServiceProperties.getShakeOutCPartyName()),
                MBSOperator.NOT_IN));

        // CMMBSSTA01-1373 - Adding TSP name - End
        // Filter based on date
        int cutOffDays = Integer.valueOf(mbspProperties.getTransHistActiveDays()).intValue();
        Date cutOffDate = MBSPortalUtils.addDays(MBSPortalUtils.getCurrentDate(), cutOffDays);
        List<String> subDateLst = new ArrayList();
        subDateLst.add(MBSPortalUtils.convertDateToString(cutOffDate, DateFormats.DATE_FORMAT_NO_TIMESTAMP));
        mbsFilterLst.add(new MBSFilter("submissionDate", subDateLst, MBSOperator.GREATER_THAN_DATE));
        Page page = new Page(pageSize, pageIndex);
        if (sortBy.equalsIgnoreCase("tradeBuySellType") && mbsRoleType == MBSRoleType.LENDER) {
            sortBy = "counterPartyBuySellType";
        }
        Sort sort = new Sort(sortBy, SortBy.getEnum(sortOrder));
        
        // TODO - Change the limit value when developing for pagination with
        // pageSize
        Integer limit = new Integer(100);
        listMBSTransReq = mbsQueryFunctionDao.getTransactionHistoryDetails(mbsFilterLst, page, sort, limit);
        // listMBSTransReq =
        // mbsQueryFunctionDao.getTransactionHistory(filterLst, page, sort,
        // limit);
        TransactionRequestPO mbsTransactionPO;
        TransformationObject transObj;
        if (!Objects.equals(listMBSTransReq, null)) {
            for (MBSTransactionRequest mbsTransReq : listMBSTransReq) {
                transObj = mbsObjectCreator.getTransformationObject();
                transObj.setMBSRoleType(mbsRoleType);
                transObj.setTargetPojo(mbsTransReq);
                mbsTransactionPO = (TransactionRequestPO) poTransformer.transform(transObj).getSourcePojo();
                mbsTransactionPO.setProduct(getProduct(mbsTransactionPO.getProduct().retrieveKey()));
                listTransReqPO.add(mbsTransactionPO);
            }
        }
        LOGGER.debug("Exiting getMBSTransReq method in TransactionRequestService");
        return listTransReqPO;
    }

    /**
     *
     *
     * @param productId
     *            the productId
     * @return ProductPO the prodMap
     * @throws MBSBaseException
     */
    public ProductPO getProduct(String productId) throws MBSBaseException {
        ProductPO productPO = productService.getByProductId(productId);
        if (Objects.equals(productPO, null)) {
            LOGGER.error(MBSPServiceConstants.NO_MATCHING_PRODUCT);
            throw new MBSBusinessException(MBSPServiceConstants.NO_MATCHING_PRODUCT,
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        return productPO;
    }

    /**
     *
     * @param productIdPO
     *            ProductIdPO
     * @return List<ProductPricingPO>
     * @throws MBSBaseException
     */
    public List<ProductPricingPO> getProductPricing(ProductIdPO productIdPO, boolean filterDeletedRecord) throws MBSBaseException {
        List<ProductPricingPO> productPricingPOLst = productPricingService.getProductPricing(productIdPO, filterDeletedRecord);
        if (CollectionUtils.isEmpty(productPricingPOLst)) {
            LOGGER.error(MBSPServiceConstants.NO_MATCHING_PRODUCT_PRICING);
            throw new MBSBusinessException(MBSPServiceConstants.NO_MATCHING_PRODUCT_PRICING,
                    MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        return productPricingPOLst;
    }

    /**
     *
     * @param productIdPO
     *            ProductIdPO
     * @return List<ProductPricingPO>
     * @throws MBSBaseException
     */
    public MBSMarketIndicativePrice getStreamedPricing(String productNameCode, String passThroughRate,
            String settlementDate) throws MBSBaseException {
        MBSMarketIndicativePrice mbsMarketIndicativePrice = new MBSMarketIndicativePrice();
        try {
            MBSMarketIndicativePrice mbsMarketIndicativePriceId = new MBSMarketIndicativePrice();
            mbsMarketIndicativePriceId.setProductNameCode(productNameCode);
            mbsMarketIndicativePriceId.setPassThroughRate(MBSPortalUtils.convertToBigDecimal(passThroughRate, 5, 2));
            mbsMarketIndicativePriceId.setSettlementDate(
                    MBSPortalUtils.convertToDateWithFormatter(settlementDate, DateFormats.DATE_FORMAT_NO_TIMESTAMP));
            LOGGER.debug("MBSMarketIndicativePrice for {} and price id {} ", productNameCode,
                    mbsMarketIndicativePriceId.getId());
            mbsMarketIndicativePrice = (MBSMarketIndicativePrice) mbsMarketIndicativePriceDao
                    .getById(mbsMarketIndicativePriceId.getId());
            if (Objects.isNull(mbsMarketIndicativePrice)) {
                LOGGER.warn("Price not available" + mbsMarketIndicativePriceId);
            }
        } catch (Exception ex) {
            LOGGER.warn("Exception retrieving stream pricing from gemfire.", ex);
            return null;
        }
        return mbsMarketIndicativePrice;
    }

    // CMMBSSTA01-874 - Start for handling exception
    /**
     *
     *
     * @return Map<String, ExceptionLookupPO>
     * @throws MBSBaseException
     */
    public Map<String, ExceptionLookupPO> getExceptionMap() throws MBSBaseException {
        Map<String, ExceptionLookupPO> exceptionLookupMap = exceptionLookupService
                .getExceptionLookupDataMap(Optional.empty());
        if (Objects.isNull(exceptionLookupMap) || exceptionLookupMap.isEmpty()) {
            LOGGER.error("Exception messages are empty.");
            throw new MBSBusinessException("Exception messages are empty.", MBSExceptionConstants.BUSINESS_EXCEPTION);
        }
        return exceptionLookupMap;
    }
    // CMMBSSTA01-874 - End for handling exception

    /**
     *
     * This method returns the status based on the configured source system
     *
     * @param mbsTransactionRequest
     *            MBSTransactionRequest
     * @return boolean
     * @throws MBSBaseException
     */
    public boolean validSourceSystem(MBSTransactionRequest mbsTransactionRequest) throws MBSBaseException {
        try {
            if (Objects.nonNull(mbsTransactionRequest) && mbsTransactionRequest.getSourceSystem()
                    .equalsIgnoreCase(tradeServiceProperties.getSourceSystem())) {
                LOGGER.debug("Source Sytem : " + mbsTransactionRequest.getSourceSystem() + " trans req "
                        + mbsTransactionRequest.getTransReqNumber());
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            LOGGER.error(MBSPServiceConstants.EXCPETION_VALIDATE_SOURCE_SYSTEM);
            throw new MBSSystemException(MBSPServiceConstants.EXCPETION_VALIDATE_SOURCE_SYSTEM,
                    MBSExceptionConstants.SYSTEM_EXCEPTION);
        }

    }

}
