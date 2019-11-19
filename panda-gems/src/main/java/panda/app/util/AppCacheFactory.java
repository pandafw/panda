package panda.app.util;

import java.io.InputStream;
import java.util.Map;
import java.util.WeakHashMap;

import panda.app.constant.MVC;
import panda.io.Settings;
import panda.io.Streams;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.Systems;
import panda.lang.collection.ExpireMap;
import panda.lang.reflect.Methods;
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
	@IocInject(value=MVC.CACHE_PROVIDER, required=false)
	protected String provider;
	
	/**
	 * cache name
	 */
	@IocInject(value=MVC.CACHE_NAME, required=false)
	protected String name;
	
	/**
	 * cache max-age seconds
	 */
	@IocInject(value=MVC.CACHE_MAXAGE, required=false)
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
	 * @throws Exception if an error occurred
	 */
	public void initialize() throws Exception {
		cache = buildCache();
	}
	
	@SuppressWarnings("unchecked")
	protected Map buildCache() throws Exception {
		if (Systems.IS_OS_APPENGINE) {
			log.info("Build Gae Cache");
			Class cls = Classes.getClass("panda.gae.app.cache.GaeCaches");
			return (Map)Methods.invokeStaticMethod(cls, "buildCache", maxAge);
		}

		if (EHCACHE.equalsIgnoreCase(provider)) {
			log.info("Build EHCache");

			InputStream is = Streams.openInputStream(EHCACHE_CONFIG);
			if (is == null) {
				throw new IllegalArgumentException("Failed to config ehcache, missing " + EHCACHE_CONFIG);
			}
			return EHCacheHelper.buildCache(is, name, maxAge);
		}

		log.info("Build Internal Java Cache");
		if (maxAge > 0) {
			return Collections.synchronizedMap(new ExpireMap(new WeakHashMap(), maxAge * 1000L));
		}

		return Collections.synchronizedMap(new WeakHashMap());
	}

	public void depose() {
		if (EHCACHE.equalsIgnoreCase(provider)) {
			EHCacheHelper.shutdownCache();
		}
	}
}

