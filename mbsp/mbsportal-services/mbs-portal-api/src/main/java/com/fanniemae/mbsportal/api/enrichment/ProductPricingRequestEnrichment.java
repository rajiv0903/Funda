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

package com.fanniemae.mbsportal.api.enrichment;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fanniemae.mbsportal.api.config.MbspProperties;
import com.fanniemae.mbsportal.enrichment.BaseEnricher;
import com.fanniemae.mbsportal.model.MBSProductPricingRequest;
import com.fanniemae.mbsportal.pojo.TransformationObject;
import com.fanniemae.mbsportal.utils.MBSPortalUtils;
import com.fanniemae.mbsportal.utils.constants.MBSPServiceConstants;

/**
 * 
 * This class provides the enrichment to the product pricing data model
 * 
 * 
 * @author g8uaxt Created on 9/14/2017.
 * @author g8upjv Updated on 11/27/2017
 * 
 */
@Component
public class ProductPricingRequestEnrichment<T extends TransformationObject> extends BaseEnricher<T> {
        
       /**
        * 
        * LOGGER Logger variable
        */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductPricingRequestEnrichment.class);
	
	/**
	 * 
	 * mbspProperties MbspProperties
	 */
	@Autowired
	MbspProperties mbspProperties;

	/**
	 * 
	 * Returns the Calendar instance
	 * 
	 * @return Calendar
	 */
	public Calendar getCurrentCal() {
		return Calendar.getInstance();
	}

	/**
	 * 
	 * The product pricing details are grouped by product and returned
	 * 
	 * 
	 * @param listMBSProductPricing the listMBSProductPricing
	 * @return Map<String, List<MBSProductPricingRequest>>
	 */
	public Map<String, List<MBSProductPricingRequest>> getProductPricingMap(
			List<MBSProductPricingRequest> listMBSProductPricing) {
		Map<String, List<MBSProductPricingRequest>> priceMap = new HashMap<String, List<MBSProductPricingRequest>>();
		Date today = MBSPortalUtils.getCurrentDate();
		int monthsDisplay = Integer.valueOf(mbspProperties.getNumberOfDisplayMonths()).intValue();
		Calendar calToday;
		Calendar calCutOff;
		for (MBSProductPricingRequest mbsProductPricingRequest : listMBSProductPricing) {
			if (!MBSPortalUtils.isItPastDate(mbsProductPricingRequest.getCutOffDate())) {
				calToday = getCurrentCal();
				calToday.setTimeZone(TimeZone.getTimeZone(ZoneId.of(MBSPortalUtils.TIME_ZONE_NYC)));
				calToday.setTimeInMillis(today.getTime());
				calCutOff = getCurrentCal();
				calCutOff.setTimeZone(TimeZone.getTimeZone(ZoneId.of(MBSPortalUtils.TIME_ZONE_NYC)));
				calCutOff.setTimeInMillis(mbsProductPricingRequest.getCutOffDate().getTime());
				for (int i = 0; i < monthsDisplay + 1; i++) {
					if (calToday.get(Calendar.MONTH) == (calCutOff.get(Calendar.MONTH))
							&& (calToday.get(Calendar.YEAR) == calCutOff.get(Calendar.YEAR))) {
						priceMap.put(mbsProductPricingRequest.getProductId().getProductIdStr(),
								updateMap(priceMap, mbsProductPricingRequest));
						break;
					}
					calToday.add(Calendar.MONTH, 1);
				}
			}
		}
		return priceMap;

	}

	private List<MBSProductPricingRequest> updateMap(Map<String, List<MBSProductPricingRequest>> priceMap, 
			MBSProductPricingRequest mbsProductPricingRequest) {
		List<MBSProductPricingRequest> mbsProdPricingReqLst ;
		if (priceMap.containsKey(mbsProductPricingRequest.getProductId().getProductIdStr())) {
			mbsProdPricingReqLst = priceMap
					.get(mbsProductPricingRequest.getProductId().getProductIdStr());
		} else {
			mbsProdPricingReqLst = new ArrayList<MBSProductPricingRequest>();
		}
		mbsProdPricingReqLst.add(mbsProductPricingRequest);
		return mbsProdPricingReqLst;
	}

	/**
	 * 
	 * The product pricing details are filtered by cut off dates
	 * 
	 * @param listMBSProductPricing thye listMBSProductPricing
	 * @return List<MBSProductPricingRequest>
	 */
	public List<MBSProductPricingRequest> isValidCuttOff(List<MBSProductPricingRequest> listMBSProductPricing) {
		List<MBSProductPricingRequest> filteredList = new ArrayList<MBSProductPricingRequest>();
		Map<String, List<MBSProductPricingRequest>> priceMap = getProductPricingMap(listMBSProductPricing);
		List<MBSProductPricingRequest> mbsProdPricingReqLstTemp;
		Date today = MBSPortalUtils.getCurrentDate();
		Calendar calTodayDate;
		int currentMonth;
		boolean isCurrentMonth = false;
		Calendar calCutOff;
		Calendar calToday;
		int monthsDisplay = Integer.valueOf(mbspProperties.getNumberOfDisplayMonths()).intValue();
		for (Map.Entry<String, List<MBSProductPricingRequest>> entry : priceMap.entrySet()) {
			String key = entry.getKey();
			mbsProdPricingReqLstTemp = priceMap.get(key);
			calTodayDate = getCurrentCal();
			calTodayDate.setTimeInMillis(today.getTime());
			calTodayDate.setTimeZone(TimeZone.getTimeZone(ZoneId.of(MBSPortalUtils.TIME_ZONE_NYC)));
			currentMonth = calTodayDate.get(Calendar.MONTH);
			isCurrentMonth = false;
			for (MBSProductPricingRequest mbsProductPricingRequest : mbsProdPricingReqLstTemp) {
				calCutOff = getCurrentCal();
				calCutOff.setTimeInMillis(mbsProductPricingRequest.getCutOffDate().getTime());
				calCutOff.setTimeZone(TimeZone.getTimeZone(ZoneId.of(MBSPortalUtils.TIME_ZONE_NYC)));
				if (currentMonth == calCutOff.get(Calendar.MONTH)) {
					isCurrentMonth = true;
					break;
				}
			}
			if (isCurrentMonth) {
				for (MBSProductPricingRequest mbsProductPricingRequest : mbsProdPricingReqLstTemp) {
					calToday = getCurrentCal();
					calToday.setTimeInMillis(today.getTime());
					calToday.setTimeZone(TimeZone.getTimeZone(ZoneId.of(MBSPortalUtils.TIME_ZONE_NYC)));
					calCutOff = getCurrentCal();
					calCutOff.setTimeInMillis(mbsProductPricingRequest.getCutOffDate().getTime());
					calCutOff.setTimeZone(TimeZone.getTimeZone(ZoneId.of(MBSPortalUtils.TIME_ZONE_NYC)));
					for (int i = 0; i < monthsDisplay; i++) {
						if (calToday.get(Calendar.MONTH) == (calCutOff.get(Calendar.MONTH))) {
							filteredList.add(mbsProductPricingRequest);
						}
						calToday.add(Calendar.MONTH, 1);
					}
				}
			} else {
				filteredList.addAll(mbsProdPricingReqLstTemp);
			}
		}
		return filteredList;
	}

	/**
	 * 
	 * Purpose: This method enriches the Transaction Request object
	 * 
	 * @param transformationObject the transformationObject
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void enrich(TransformationObject transformationObject) {
		LOGGER.debug("Entering enrich method in ProductPricingRequestEnrichment");
		List<MBSProductPricingRequest> listMBSProductPricing = (List<MBSProductPricingRequest>) transformationObject
				.getTargetPojo();
		LOGGER.debug("Before filtered size" + listMBSProductPricing.size());
		List<MBSProductPricingRequest> listMBSProductPricingFiltered = isValidCuttOff(listMBSProductPricing);
		
		if(transformationObject.isFilterDeletedRecord()){
		    listMBSProductPricingFiltered = listMBSProductPricingFiltered.stream().filter(
		            data -> MBSPServiceConstants.noChar.equals(data.getLogicalDeleteIndicator())).collect(Collectors.toList()); 
		}
		
		LOGGER.debug("After filtered size" + listMBSProductPricingFiltered.size());
		transformationObject.setTargetPojo(listMBSProductPricingFiltered);
		LOGGER.debug("Exit enrich method in ProductPricingRequestEnrichment");
	}
}
