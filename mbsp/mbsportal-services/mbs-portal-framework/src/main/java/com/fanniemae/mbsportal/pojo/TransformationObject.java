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

package com.fanniemae.mbsportal.pojo;

import java.util.HashMap;
import java.util.Map;

import com.fanniemae.mbsportal.constants.MBSRoleType;

/**
 * Created by g8uaxt on 8/18/2017.
 */
public class TransformationObject implements Cloneable {

    /**
     * sourcePojo declaration
     */
    private Object sourcePojo;

    /**
     * The sourceType - Lender or Trader
     */
    private MBSRoleType mbsRoleType;

    private Object targetPojo;
    /**
     * transformationDataMap declaration
     */
    private Map<Object, Object> transformationDataMap = null;
    
    /**
     * The mergedProfile - default is merged always
     */
    private boolean mergedProfile = true;
    
    /**
     * reqHeaderMap to take the request header for future use
     */
    private Map<String, String> reqHeaderMap;
    
    /**
     * The filterDeletedRecord - Filter record if mark as deleted
     */
    private boolean filterDeletedRecord = false;
    
    /**
     * Getter for transformation data map
     * 
     * @return Map&ltObject, Object&gt
     */
    public Map<Object, Object> getTransformationDataMap() {
        if (transformationDataMap == null) {
            transformationDataMap = new HashMap<Object, Object>();
        }
        return transformationDataMap;
    }

    public Object getSourcePojo() {
        return sourcePojo;
    }

    public void setSourcePojo(Object sourcePojo) {
        this.sourcePojo = sourcePojo;
    }

    public Object getTargetPojo() {
        return targetPojo;
    }

    public void setTargetPojo(Object targetPojo) {
        this.targetPojo = targetPojo;
    }

    /**
     * @return the sourceType
     */
    public MBSRoleType getMBSRoleType() {
        return mbsRoleType;
    }
    
    /**
     *
     * @param mbsRoleType
     */
    public void setMBSRoleType(MBSRoleType mbsRoleType) {
        this.mbsRoleType = mbsRoleType;
    }

   /**
    * 
    * @return the mergedProfile
    */
    public boolean isMergedProfile() {
        return mergedProfile;
    }

    /**
     * 
     * @param mergedProfile
     */
    public void setMergedProfile(boolean mergedProfile) {
        this.mergedProfile = mergedProfile;
    }

    /**
     * 
     * @return the reqHeaderMap
     */
    public Map<String, String> getReqHeaderMap() {
        return reqHeaderMap;
    }

    /**
     * 
     * @param reqHeaderMap the reqHeaderMap
     */
    public void setReqHeaderMap(Map<String, String> reqHeaderMap) {
        this.reqHeaderMap = reqHeaderMap;
    }

   
    /**
     * 
     * Clone method for getting clone object
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * 
     * @return the filterDeletedRecord
     */
    public boolean isFilterDeletedRecord() {
        return filterDeletedRecord;
    }

    /**
     * 
     * @param filterDeletedRecord the filterDeletedRecord
     */
    public void setFilterDeletedRecord(boolean filterDeletedRecord) {
        this.filterDeletedRecord = filterDeletedRecord;
    }
    
    

}
