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

package com.fanniemae.mbsportal.utils.epv;

import java.lang.reflect.Method;

import com.fanniemae.mbsportal.utils.exception.MBSBaseException;

/**
 * @author g8uaxt Created on 10/26/2017.
 */

public class EpvConnector {
        //The name of the EPV vault factory class.
        private static String EPV_FACTORY = "com.fanniemae.sharedservices.evas.VaultFactory";
        
        //The name of the EPV vault class.
        private static String EPV_VAULT = "com.fanniemae.sharedservices.evas.vault.Vault";
        
        //The name of the method to be invoked on the vault factory class.
        private static final String GET_VAULT = "getVault";
        
        //The name of the method to be invoked on the vault.
        private static final String GET_CONTENT = "getContent";
 
        private String valutPwd = null;
        private Object vault;
        
        public void initialize(String appCode, String env,String objRef) throws MBSBaseException{
                vault = getEnvVault(appCode,env);
                valutPwd=getPassword(objRef);
        }
        public void initialize(String appCode, String env) throws MBSBaseException{
                vault = getEnvVault(appCode,env);
                //valutPwd=getPassword(objRef);
        }
        
        private Object getEnvVault(String appCode, String env) throws MBSBaseException {
                Object vault = null;
               try {
                        Class<?> vaultFactoryClass = Class.forName(EPV_FACTORY);
                        Method getVaultMethod = vaultFactoryClass.getMethod(GET_VAULT, String.class, String.class);
                        vault = getVaultMethod.invoke(null, appCode, env);
                } catch (Throwable t) {
                        throw new MBSBaseException("Unable to get vault.  " +
                                "APP_CODE: " + appCode + "  ENV:  " + env , t);
                }
                return vault;
       }
        
        /**
         * getPassword
         * @param objRef
         * @return
         * @throws MBSBaseException
         */
        public String getPassword(String objRef) throws MBSBaseException {
                try {
                        Class<?> vaultClass = Class.forName(EPV_VAULT);
                        Method getPassword = vaultClass.getMethod(GET_CONTENT, String.class);
                        return (String) getPassword.invoke(vault, objRef);
                } catch (Exception e) {
                        //Unable to force this exception for testing purposes.
                        throw new MBSBaseException("Unable to get the password from the EPV vault.  " +
                                "  OBJECT REF:  " + objRef, e);
                }
        }
        
        /**
         * getValutPwd
         * @return
         */
        public String getValutPwd() {
                return valutPwd;
        }
}
