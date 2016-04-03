package panda.wing.util;

import java.io.InputStream;
import java.util.Map;
import java.util.WeakHashMap;

import panda.io.Settings;
import panda.io.Streams;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.lang.collection.ExpireMap;
import panda.log.Log;
import panda.log.Logs;
import panda.wing.constant.SC;

@IocBean(create="initialize", depose="depose")
public class AppCacheProvider {
	private static final Log log = Logs.getLog(AppCacheProvider.class);

	private static final String EHCACHE_CONFIG = "ehcache.xml";
	private static final String GAE = "gae";
	private static final String EHCACHE = "ehcache";

	@IocInject
	protected Settings settings;

	/**
	 * cache provider
	 */
	protected String provider;
	
	/**
	 * cache name
	 */
	protected String name;
	
	/**
	 * cache max-age seconds
	 */
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
		provider = settings.getProperty(SC.CACHE_PROVIDER);
		name = settings.getProperty(SC.CACHE_NAME);
		maxAge = settings.getPropertyAsInt(SC.CACHE_MAXAGE, 0);
		cache = buildCache();
	}
	
	protected Map buildCache() throws Exception {
		if (GAE.equalsIgnoreCase(provider)) {
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
