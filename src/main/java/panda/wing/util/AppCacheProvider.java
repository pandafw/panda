package panda.wing.util;

import java.io.InputStream;
import java.util.Map;
import java.util.WeakHashMap;

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
public class AppCacheProvider {
	private static final Log log = Logs.getLog(AppCacheProvider.class);

	private static final String EHCACHE_CONFIG = "ehcache.xml";

	@IocInject
	protected Settings settings;

	private static final int EH = 1;
	
	protected int type;
	
	/**
	 * cache name
	 */
	protected String name;
	
	/**
	 * cache expire seconds
	 */
	protected int expires;

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
		name = settings.getProperty("cache.name");
		expires = settings.getPropertyAsInt("cache.expires", 0);
		cache = buildCache();
	}
	
	protected Map buildCache() throws Exception {
		if (Systems.IS_OS_APPENGINE) {
			log.info("Build Gae Cache");
			return GaeHelper.buildCache(expires);
		}
		
		InputStream is = Streams.getStream(EHCACHE_CONFIG);
		if (is != null) {
			log.info("Build EHCache");
			type = EH;
			return EHCacheHelper.buildCache(is, name, expires);
		}
		
		log.info("Build Internal Java Cache");
		if (expires > 0) {
			return Collections.synchronizedMap(new ExpireMap<String, String>(
					new WeakHashMap<String, String>(), expires));
		}

		return Collections.synchronizedMap(new WeakHashMap<String, String>());
	}

	public void depose() {
		if (type == EH) {
			EHCacheHelper.shutdownCache();
		}
	}
}
