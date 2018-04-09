package com.tracfone.ws.cache.guava;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.FactoryBean;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
/**
 * Generic implementation of GUAVA cache, whoever using this factory need to add below configuration into the spring xml file. 
 * 
 * <bean id="cacheFromGuavaFactoryBean"
      class="com.tracfone.ws.cache.guava.GuavaCacheFactoryBean">
      <property name="expireMillisecondsAfterWrite" value="$SPRINGFARM{GuavaCacheExpireMillisecondsAfterWrite}" />
		<property name="maximumSize" value="$SPRINGFARM{GuavaCacheMaximumSize}" />
	</bean>
 * 
 * below is the snippet of the code to be used in java file
 * 
 * Cache<String, ServicePlanSelectorEventResult> cache = (Cache<String, ServicePlanSelectorEventResult>) webContext.getAttribute("SERVICE_PLAN_CACHE");
 * cache = cacheFactory.getObject();
 * cache.get(cacheKey, new Callable<ServicePlanSelectorEventResult>() {
				@Override
				public ServicePlanSelectorEventResult call() throws CBOClientException {
					return callCBOServicePlanSelectorByCriteria(applicationContext, event, partClass);
				}
			});
 * 
 * @author Manzoor Ahmed
 *
 * @param <K>
 * @param <V>
 */

public class GuavaCacheFactoryBean<K, V> implements FactoryBean<Cache<K, V>> {

	private Map<String,Object> propertiesMap; 
	
	// Properties used by the Guava Cache builder
	private Long expireMinutesAfterWrite;
	private Long maximumSize;
	//etc

	@SuppressWarnings("unchecked")
	@Override
	public Cache<K, V> getObject() throws Exception {
		CacheBuilder<K, V> builder = (CacheBuilder<K, V>) CacheBuilder.newBuilder();
		if (expireMinutesAfterWrite != null && expireMinutesAfterWrite.longValue()>0) {
			builder.expireAfterWrite(expireMinutesAfterWrite, TimeUnit.MINUTES);
		}
		if (maximumSize != null && maximumSize.longValue()>0) {
			builder.maximumSize(maximumSize);
		}
		return builder.build();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<Cache> getObjectType() {
		return Cache.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	

	public void setMaximumSize(Long maximumSize) {
		this.maximumSize = maximumSize;
	}

	public void setExpireMinutesAfterWrite(Long expireMinutesAfterWrite) {
		this.expireMinutesAfterWrite = expireMinutesAfterWrite;
	}

	public Map<String, Object> getPropertiesMap() {
		return propertiesMap;
	}

	public void setPropertiesMap(Map<String, Object> propertiesMap) {
		this.propertiesMap = propertiesMap;
	}
}
