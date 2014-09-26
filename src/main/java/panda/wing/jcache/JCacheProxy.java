package panda.wing.jcache;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheEntry;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheListener;
import net.sf.jsr107cache.CacheStatistics;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import panda.lang.Exceptions;

/**
 */
public class JCacheProxy implements Cache {
	private Map map;

	public JCacheProxy(Map map) {
		this.map = map;
	}

	private Object toCompareKey(Object key) {
		return key instanceof String ? ((String)key).toLowerCase() : key;
	}

	public void clear() {
		map.clear();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(toCompareKey(key));
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@SuppressWarnings("unchecked")
	public Set<java.util.Map.Entry> entrySet() {
		return map.entrySet();
	}

	public Object get(Object key) {
		return map.get(toCompareKey(key));
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set keySet() {
		return map.keySet();
	}

	@SuppressWarnings("unchecked")
	public Object put(Object key, Object value) {
		return map.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public void putAll(Map m) {
		map.putAll(m);
	}

	public Object remove(Object key) {
		return map.remove(key);
	}

	public int size() {
		return map.size();
	}

	public Collection values() {
		return map.values();
	}

	public boolean equals(Object o) {
		return map.equals(o);
	}

	public int hashCode() {
		return map.hashCode();
	}

	public String toString() {
		return map.toString();
	}

	public Map getAll(Collection keys) throws CacheException {
		throw Exceptions.unsupported();
	}

	public void load(Object key) throws CacheException {
		throw Exceptions.unsupported();
	}

	public void loadAll(Collection keys) throws CacheException {
		throw Exceptions.unsupported();
	}

	public Object peek(Object key) {
		throw Exceptions.unsupported();
	}

	public CacheEntry getCacheEntry(Object key) {
		throw Exceptions.unsupported();
	}

	public CacheStatistics getCacheStatistics() {
		throw Exceptions.unsupported();
	}

	public void evict() {
	}

	public void addListener(CacheListener listener) {
	}

	public void removeListener(CacheListener listener) {
	}
}
