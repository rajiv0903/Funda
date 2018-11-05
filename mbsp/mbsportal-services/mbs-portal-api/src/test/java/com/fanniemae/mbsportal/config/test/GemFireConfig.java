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

package com.fanniemae.mbsportal.config.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.annotation.Resource;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.client.Pool;
import org.apache.geode.cache.client.PoolFactory;
import org.apache.geode.cache.client.PoolManager;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.test.context.ActiveProfiles;

/**
 * Created by g8uaxt on 7/31/2017.
 */
@Configuration
@ActiveProfiles("test")
public class GemFireConfig {
       private static String locatorHost="dlv-fmn-a001.fanniemae.com";
        private static int locatorPort=10334;
        //Dev "10.108.5.207", 10000
        
        @Resource(name = "gemfireProperties")
        private Properties gemfireProperties;
        
        @Bean(name = "gemfireProperties")
        public Properties gemfireProperties() throws IOException {
                return PropertiesLoaderUtils.loadProperties(new ClassPathResource("gemfire.properties"));
        }
        
      
        @Bean
        public ReflectionBasedAutoSerializer reflectionBasedAutoSerializer() {
                List<String> patterns = Arrays.asList(new String[] { "com.fanniemae.mbsportal.model.domain.*",
                        "com.fanniemae.securitiesods.ods_core.domain.*", "com.fanniemae.mbsportal.id.*","com.fanniemae.fnmpfj.gemfire.utils.client.identity.Sequence#identity=name" });
                ReflectionBasedAutoSerializer reflectionBasedAutoSerializer = new ReflectionBasedAutoSerializer();
                reflectionBasedAutoSerializer.setSerializableClasses(patterns);
                //reflectionBasedAutoSerializer.
                return reflectionBasedAutoSerializer;
        }
      
        @SuppressWarnings("rawtypes")
        @Bean
        public Region mbsFNMPFJSequences() {
                ClientRegionFactory<String, String> cRegionFactory = clientCache()
                        .createClientRegionFactory(ClientRegionShortcut.PROXY);
                Region<String, String> region = cRegionFactory.setPoolName(mtaPoolManager().getName())
                        .create("FNMPFJSequences");
                return region;
        }
        
        
        @SuppressWarnings("rawtypes")
        @Bean
        public Region mbsProductRegion() {
                ClientRegionFactory<String, String> cRegionFactory = clientCache()
                        .createClientRegionFactory(ClientRegionShortcut.PROXY);
                Region<String, String> region = cRegionFactory.setPoolName(mtaPoolManager().getName())
                        .create("MBSProduct"); //TODO: make it refer from class MBSProduct.class
                return region;
        }
        
        @SuppressWarnings("rawtypes")
        @Bean
        public Region mbsExceptionLookupRegion() {
                ClientRegionFactory<String, String> cRegionFactory = clientCache()
                        .createClientRegionFactory(ClientRegionShortcut.PROXY);
                Region<String, String> region = cRegionFactory.setPoolName(mtaPoolManager().getName())
                        .create("MBSExceptionLookup"); //TODO: make it refer from class MBSProduct.class
                return region;
        }
        
        @SuppressWarnings("rawtypes")
        @Bean
        public Region mbsTransactionRegion() {
                ClientRegionFactory<String, String> cRegionFactory = clientCache()
                        .createClientRegionFactory(ClientRegionShortcut.PROXY);
                Region<String, String> region = cRegionFactory.setPoolName(mtaPoolManager().getName())
                        .create("MBSTransaction");
                return region;
        }
        @SuppressWarnings("rawtypes")
        @Bean
        public Region mbsProductPricingRegion() {
                ClientRegionFactory<String, String> cRegionFactory = clientCache()
                        .createClientRegionFactory(ClientRegionShortcut.PROXY);
                Region<String, String> region = cRegionFactory.setPoolName(mtaPoolManager().getName())
                        .create("MBSProductPricing");
                return region;
        }
        @SuppressWarnings("rawtypes")
        @Bean
        public Region mbsTradeRegion() {
                ClientRegionFactory<String, String> cRegionFactory = clientCache()
                        .createClientRegionFactory(ClientRegionShortcut.PROXY);
                Region<String, String> region = cRegionFactory.setPoolName(mtaPoolManager().getName())
                        .create("MBSTrade");
                return region;
        }
        
        @SuppressWarnings("rawtypes")
        @Bean
        public Region mbsEventRegion() {
                ClientRegionFactory<String, String> cRegionFactory = clientCache()
                        .createClientRegionFactory(ClientRegionShortcut.PROXY);
                Region<String, String> region = cRegionFactory.setPoolName(mtaPoolManager().getName())
                        .create("MBSEvent");
                return region;
        }
        
        @SuppressWarnings("rawtypes")
        @Bean
        public Region mbsMarketIndicativePriceRegion() {
                ClientRegionFactory<String, String> cRegionFactory = clientCache()
                        .createClientRegionFactory(ClientRegionShortcut.PROXY);
                Region<String, String> region = cRegionFactory.setPoolName(mtaPoolManager().getName())
                        .create("MBSMarketIndicativePrice");
                return region;
        }
        
        @SuppressWarnings("rawtypes")
        @Bean
        public Region mbsProfileRegion() {
                ClientRegionFactory<String, String> cRegionFactory = clientCache()
                        .createClientRegionFactory(ClientRegionShortcut.PROXY);
                Region<String, String> region = cRegionFactory.setPoolName(mtaPoolManager().getName())
                        .create("MBSProfile");
                return region;
        }
        
        @SuppressWarnings("rawtypes")
        @Bean
        public Region mbsProductTransactionXrefRegion() {
                ClientRegionFactory<String, String> cRegionFactory = clientCache()
                        .createClientRegionFactory(ClientRegionShortcut.PROXY);
                Region<String, String> region = cRegionFactory.setPoolName(mtaPoolManager().getName())
                        .create("MBSProductTransactionXref"); //TODO: make it refer from class MBSProductTransactionXref.class
                return region;
        }
        
    
        
        
        @Bean
        //@DependsOn({"mbsProductRegion","mbsTransactionRegion","mbsProductPricingRegion"})
        public Pool mtaPoolManager() {
                PoolFactory pf = PoolManager.createFactory();
               pf.addLocator(locatorHost, locatorPort);
               //pf.addLocator("10.108.5.207", 10000);
                pf.setSubscriptionEnabled(true);
                return pf.create("mta-pool");
        }
        
        @Bean
       // @DependsOn({"mtaPoolManager"})
        public ClientCache clientCache() {
                ClientCache c = new ClientCacheFactory(gemfireProperties).addPoolLocator(locatorHost,locatorPort) //TODO: make it driven by prop file- 'gemfire-settings.prop' ?
                        .setPdxSerializer(reflectionBasedAutoSerializer()).setPdxReadSerialized(false)
                        //TODO:set prop
                        .setPoolSubscriptionEnabled(false).setPoolMaxConnections(1).setPoolReadTimeout(300000)
                        .setPoolServerGroup("").create();
                return c;
        }
        
}
