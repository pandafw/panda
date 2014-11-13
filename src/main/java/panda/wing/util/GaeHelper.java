package panda.wing.util;

import java.util.HashMap;
import java.util.Map;

import javax.cache.CacheFactory;
import javax.cache.CacheManager;

import com.google.appengine.api.memcache.stdimpl.GCacheFactory;

import panda.lang.collection.SafeMap;

public class GaeHelper {

	@SuppressWarnings("unchecked")
	public static Map buildCache(int expires) throws Exception {
		CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
		Map props = new HashMap();
		if (expires > 0) {
			props.put(GCacheFactory.EXPIRATION_DELTA, expires);
		}
		return new SafeMap(cacheFactory.createCache(props));
	}

}

