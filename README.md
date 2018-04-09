# Funda



Cache<String, ServicePlanSelectorEventResult> cache = (Cache<String, ServicePlanSelectorEventResult>) webContext
				.getAttribute(ServicePlanConstants.SERVICE_PLAN_INFO_CACHE_V2);
		if (cache == null) {
			try {
				GuavaCacheFactoryBean<String, ServicePlanSelectorEventResult> cacheFactory = applicationContext.getBean(GuavaCacheFactoryBean.class);
				cache = cacheFactory.getObject();
				webContext.setAttribute(ServicePlanConstants.SERVICE_PLAN_INFO_CACHE_V2, cache);
			} catch (Exception exe) {
				logger.error(exe.getMessage(), exe);
			}
		}
		event.setCache(cache);
    
   
   protected ServicePlanSelectorEventResult callGetServicePlanSelectorByCriteria(final ServicePlanSelectorEvent event, final ApplicationContext applicationContext,
			final String partClass, final String xslName) throws ExecutionException 
	{
   String cacheKey = helper.createCacheKey(partClass, event.getBrandName(), event.getSourceSystem(), event.getLanguage());
		logger.debug("cacheKey " + cacheKey);

		// CHECK IN CACHE IF OBJECT PRESENT RETURN IT ELSE CALL  .
		StopWatch stopWatch = new StopWatch();
		stopWatch.reset();
		stopWatch.start();
		ServicePlanSelectorEventResult planSelectorEventResult = event.getCache().get(cacheKey, new Callable<ServicePlanSelectorEventResult>() {
			@Override
			public ServicePlanSelectorEventResult call() throws Exception {
				return callCBOServicePlanSelectorByCriteria(applicationContext, event, partClass, event.getEsn(),  xslName);
			}
		});
    
    
