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

package com.fanniemae.mbsportal.dao;

import static com.fanniemae.mbsportal.constants.DAOConstants.IDTypes.MARKET_INDICATIVE_PRICE_HISTORY_ID;
import static com.fanniemae.mbsportal.constants.DAOConstants.IDTypes.PRODUCT_ID;
import static com.fanniemae.mbsportal.constants.DAOConstants.IDTypes.PRODUCT_PRICING_ID;
import static com.fanniemae.mbsportal.constants.DAOConstants.IDTypes.TRANSACTION_ID;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fanniemae.fnmpfj.gemfire.utils.client.identity.GemFireSequenceHelper;
import com.fanniemae.fnmpfj.gemfire.utils.client.identity.SequenceBlock;
import com.fanniemae.fnmpfj.gemfire.utils.client.identity.exceptions.IdentityGenerationException;
import com.fanniemae.mbsportal.constants.DAOConstants;
import com.fanniemae.mbsportal.constants.DAOConstants.IDTypes;
import com.fanniemae.mbsportal.util.DAOUtils;
import com.fanniemae.mbsportal.utils.exception.MBSBaseException;
import com.fanniemae.mbsportal.utils.exception.MBSBusinessException;
import com.fanniemae.mbsportal.utils.exception.MBSExceptionConstants;
import com.fanniemae.mbsportal.utils.exception.MBSSystemException;

/**
 * Created by g8uaxt on 8/3/2017.
 * 
 * @author g8upjv
 */
@Component
public class IDServiceDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(IDServiceDao.class);

    /**
     * seqBlockMap declaration
     */
    private static Map<String, SequenceBlock> seqBlockMap = new ConcurrentHashMap<String, SequenceBlock>();

    /**
     * get the seq id
     * 
     * @param type
     * @param quantity
     * @return String
     * @throws MBSBaseException
     */
    public String getSeqId(IDTypes type, int quantity, int maxSequenceValue) throws MBSBaseException {
        long id = 0l;
        String format = null;
        switch (type) {
        case TRANSACTION_ID:
            LOGGER.debug("creating seq number for " + TRANSACTION_ID);
            id = getId(type, quantity, maxSequenceValue);
            LOGGER.debug("Generated seq number {} for {}", id, TRANSACTION_ID);
            if (id > maxSequenceValue) {
                throw new MBSBusinessException("Out of Range Sequence id for Transaction",
                        MBSExceptionConstants.BUSINESS_EXCEPTION);
            }
            LOGGER.debug("Workaround - created random seq number {} for type {} ", id, TRANSACTION_ID);
            format = "%05d";
            break;
        case PRODUCT_ID:
            LOGGER.debug("creating seq number for " + PRODUCT_ID);
            id = getId(type, quantity, maxSequenceValue);
            LOGGER.debug("Generated seq number {} for {}", id, PRODUCT_ID);
            if (id > maxSequenceValue) {
                throw new MBSBusinessException("Out of Range Sequence id for Product",
                        MBSExceptionConstants.BUSINESS_EXCEPTION);
            }
            format = null;
            break;
        case PRODUCT_PRICING_ID:
            LOGGER.debug("creating seq number for " + PRODUCT_PRICING_ID);
            id = getId(type, quantity, maxSequenceValue);
            LOGGER.debug("Generated seq number {} for {}", id, PRODUCT_PRICING_ID);
            if (id > maxSequenceValue) {
                throw new MBSBusinessException("Out of Range Sequence id for Product Pricing",
                        MBSExceptionConstants.BUSINESS_EXCEPTION);
            }
            format = null;
            break;
        case MARKET_INDICATIVE_PRICE_HISTORY_ID:
            LOGGER.debug("creating seq number for " + MARKET_INDICATIVE_PRICE_HISTORY_ID);
            id = getId(type, quantity, maxSequenceValue);
            LOGGER.debug("Generated seq number {} for {}", id, MARKET_INDICATIVE_PRICE_HISTORY_ID);
            if (id > maxSequenceValue) {
                throw new MBSBusinessException("Out of Range Sequence id for Product Pricing",
                        MBSExceptionConstants.BUSINESS_EXCEPTION);
            }
            format = null;
            break;
        default:
            LOGGER.debug("Going for default loop- fixIt");
            break;
        }
        return DAOUtils.formatSeqNumber(id, type, format);
    }

    /**
     * Called by the get*Id() methods to retrieve the next Id to be assigned
     * Also, refills the SequenceBlock if it is empty with the set QTY of
     * elements <br>
     *
     * @param type
     *            - the name of the Sequence
     * @param quantity
     *            - the quantity of unique Ids in increasing order to retrieve
     * @param maxSequenceValue
     *            - the Max Sequence Value
     * @return long - the single unique Id that is next in line to be used for
     *         the next Event, Trade, etc.
     */
    protected long getId(IDTypes type, int quantity, int maxSequenceValue) throws MBSBaseException {
        // TODO: why do we need this? , String eventType
        // TODO: protected scope is for unit test :(
        synchronized (seqBlockMap) {
            SequenceBlock seqBlock = seqBlockMap.get(type.getName());
            if (seqBlock == null || !seqBlock.hasNext()) {
                LOGGER.debug("Calling ID Service for {} ", type.getName());
                try {
                    seqBlock = GemFireSequenceHelper.next(type.getName(), quantity);
                } catch (IdentityGenerationException sdne) {
                    // create it first time if not exists
                    if (sdne.getMessage().contains("GemFireSequenceDoesNotExistException")) {
                        LOGGER.debug("creating seq first time {} ", type.getName());
                        GemFireSequenceHelper.createSequence(type.getName(), 0, maxSequenceValue);
                        seqBlock = GemFireSequenceHelper.next(type.getName(), quantity);
                    }
                } catch (Exception e) {
                    LOGGER.error("seqBlock ID Service exception ", e);
                    throw new MBSSystemException("seqBlock ID Service exception ", e);
                }
            }
            seqBlockMap.put(type.getName(), seqBlock);
            LOGGER.debug("seqBlock ID Service for {} " + seqBlock.hasNext());
            if (seqBlock == null || !seqBlock.hasNext()) {
                LOGGER.error("Sequence Block is empty or null.");
                throw new MBSSystemException("Sequence Block is empty or null.",
                        new Exception("Unable to get next ID for " + type.getName()));
            } else {
                return seqBlock.next();
            }
        }
    }

    /**
     * getSeqId
     * 
     * @param type
     * @return String
     * @throws MBSBaseException
     */
    public String getSeqId(IDTypes type) throws MBSBaseException {
        LOGGER.debug("entering in getSeqId for " + type.getName());
        return getSeqId(type, 1, DAOConstants.SEQUENCE_DAFULT);
    }

    /**
     * getSeqId
     * 
     * @param type
     * @param maxSequenceValue
     * @return String
     * @throws MBSBaseException
     */
    public String getSeqId(IDTypes type, int maxSequenceValue) throws MBSBaseException {
        LOGGER.debug("entering in getSeqId for " + type.getName());
        LOGGER.debug("entering in getSeqId with " + maxSequenceValue);
        return getSeqId(type, 1, maxSequenceValue);
    }

    /**
     * getSeqId
     * 
     * @param type
     * @param quantity
     * @return String
     * @throws MBSBaseException
     */
    public String getSeqId(IDTypes type, String quantity) throws MBSBaseException {
        int tmpQty = 1;
        try {
            tmpQty = Integer.parseInt(quantity);
        } catch (NumberFormatException nfe) {
            throw new MBSSystemException("Invalid Quantity",
                    new NumberFormatException("quantity is not a number" + nfe.getMessage()));
        }
        return this.getSeqId(type, tmpQty, DAOConstants.SEQUENCE_DAFULT);
    }
}
