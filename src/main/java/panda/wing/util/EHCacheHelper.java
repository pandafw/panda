package panda.wing.util;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;

import panda.lang.Asserts;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.collection.ListSet;

public class EHCacheHelper {
	public static class EHCacheMap implements Map {
		private final Cache cache;
		
		/**
		 * @param cache
		 */
		public EHCacheMap(Cache cache) {
			this.cache = cache;
		}

		@Override
		public int size() {
			return cache.getSize();
		}

		@Override
		public boolean isEmpty() {
			return cache.getSize() == 0;
		}

		@Override
		public boolean containsKey(Object key) {
			return cache.isKeyInCache(key);
		}

		@Override
		public boolean containsValue(Object value) {
			return cache.isValueInCache(value);
		}

		@Override
		public Object get(Object key) {
			Element e = cache.get(key);
			return e == null ? null : e.getObjectValue();
		}

		@Override
		public Object put(Object key, Object value) {
			Asserts.notNull(key);
			Element e = cache.putIfAbsent(new Element(key, value));
			return e == null ? null : e.getObjectValue();
		}

		@Override
		public Object remove(Object key) {
			Asserts.notNull(key);
			Element e = cache.removeAndReturnElement(key);
			return e == null ? null : e.getObjectValue();
		}

		@Override
		@SuppressWarnings("unchecked")
		public void putAll(Map m) {
			for (Entry en : (Set<Entry>)m.entrySet()) {
				cache.put(new Element(en.getKey(), en.getValue()));
			}
		}

		@Override
		public void clear() {
			cache.removeAll();
		}

		@Override
		@SuppressWarnings("unchecked")
		public Set keySet() {
			return new ListSet(cache.getKeys());
		}

		@Override
		public Collection values() {
			throw Exceptions.unsupported("values() of Cache is unsupported.");
		}

		@Override
		public Set<Entry> entrySet() {
			throw Exceptions.unsupported("entrySet() of Cache is unsupported.");
		}
		
	}
	
	public static Map buildCache(InputStream is, String cacheName, int cacheExpire) throws Exception {
		Configuration conf = null;
		
		conf = ConfigurationFactory.parseConfiguration(is);
		is.close();

		CacheConfiguration cc = null;
		if (Strings.isEmpty(cacheName)) {
			cacheName = "_default";
			cc = conf.getCacheConfigurations().get(cacheName);
			if (cc == null) {
				cc = conf.getDefaultCacheConfiguration();
				cc = cc.clone();
				cc.setName(cacheName);
				conf.addCache(cc);
			}
		}
		else {
			cc = conf.getCacheConfigurations().get(cacheName);
		}

		if (cacheExpire > 0) {
			cc.setTimeToLiveSeconds(cacheExpire);
		}
		
		CacheManager manager = new CacheManager(conf);
		Cache c = manager.getCache(cacheName);
		return new EHCacheMap(c);
	}
	
	public static void shutdownCache() {
		List knownCacheManagers = CacheManager.ALL_CACHE_MANAGERS;
		while (!knownCacheManagers.isEmpty()) {
			((CacheManager)CacheManager.ALL_CACHE_MANAGERS.get(0)).shutdown();
		}
	}
}
