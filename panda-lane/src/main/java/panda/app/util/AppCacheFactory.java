package panda.app.util;

import java.io.InputStream;
import java.util.Map;
import java.util.WeakHashMap;

import panda.app.AppConstants;
import panda.io.Settings;
import panda.io.Streams;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.Systems;
import panda.lang.collection.ExpireMap;
import panda.log.Log;
import panda.log.Logs;

@IocBean(create="initialize", depose="depose")
public class AppCacheFactory {
	private static final Log log = Logs.getLog(AppCacheFactory.class);

	private static final String EHCACHE_CONFIG = "ehcache.xml";
	private static final String EHCACHE = "ehcache";

	@IocInject
	protected Settings settings;

	/**
	 * cache provider
	 */
	@IocInject(value=AppConstants.CACHE_PROVIDER, required=false)
	protected String provider;
	
	/**
	 * cache name
	 */
	@IocInject(value=AppConstants.CACHE_NAME, required=false)
	protected String name;
	
	/**
	 * cache max-age seconds
	 */
	@IocInject(value=AppConstants.CACHE_MAXAGE, required=false)
	protected int maxAge;

	/**
	 * cache instance
	 */
	protected Map cache;
	

	/**
	 * @return the cache
	 */
	public Map getCache() {
		return cache;
	}


	/**
	 * initialize
	 */
	public void initialize() throws Exception {
		cache = buildCache();
	}
	
	protected Map buildCache() throws Exception {
		if (Systems.IS_OS_APPENGINE) {
			log.info("Build Gae Cache");
			return GaeHelper.buildCache(maxAge);
		}

		if (EHCACHE.equalsIgnoreCase(provider)) {
			log.info("Build EHCache");

			InputStream is = Streams.getStream(EHCACHE_CONFIG);
			if (is == null) {
				throw new IllegalArgumentException("Failed to config ehcache, missing " + EHCACHE_CONFIG);
			}
			return EHCacheHelper.buildCache(is, name, maxAge);
		}

		log.info("Build Internal Java Cache");
		if (maxAge > 0) {
			return Collections.synchronizedMap(new ExpireMap<String, String>(
					new WeakHashMap<String, String>(), maxAge));
		}

		return Collections.synchronizedMap(new WeakHashMap<String, String>());
	}

	public void depose() {
		if (EHCACHE.equalsIgnoreCase(provider)) {
			EHCacheHelper.shutdownCache();
		}
	}
}

