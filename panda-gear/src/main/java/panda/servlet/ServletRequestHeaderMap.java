package panda.servlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import panda.lang.Exceptions;
import panda.lang.Objects;
import panda.lang.collection.MapEntry;


/**
 * A simple implementation of the {@link java.util.Map} interface to handle a collection of request headers.
 * 
 */
public class ServletRequestHeaderMap implements Map<String, Object> {
	private HttpServletRequest request;

	/**
	 * Saves the request to use as the backing for getting and setting values
	 * 
	 * @param request the http servlet request.
	 */
	public ServletRequestHeaderMap(final HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Removes all attributes from the request as well as clears entries in this map.
	 */
	@Override
	public void clear() {
		throw Exceptions.unsupported("clear");
	}

	@Override
	public Set<String> keySet() {
		Set<String> keys = new HashSet<String>();

		Enumeration<String> em = request.getHeaderNames();
		while (em.hasMoreElements()) {
			keys.add(em.nextElement());
		}

		return keys;
	}

	@Override
	public Collection<Object> values() {
		List<Object> vals = new ArrayList<Object>();

		Enumeration<String> em = request.getHeaderNames();
		while (em.hasMoreElements()) {
			vals.add(getHeader(em.nextElement()));
		}

		return vals;
	}

	/**
	 * Returns a Set of attributes from the http request.
	 * 
	 * @return a Set of attributes from the http request.
	 */
	@Override
	public Set<Entry<String, Object>> entrySet() {
		Set<Entry<String, Object>> entries = new HashSet<Entry<String, Object>>();

		Enumeration<String> em = request.getHeaderNames();
		while (em.hasMoreElements()) {
			final String key = em.nextElement();
			final Object val = getHeader(key);
			entries.add(new MapEntry<String, Object>(this, key, val));
		}

		return entries;
	}

	/**
	 * Returns the request attribute associated with the given key or <tt>null</tt> if it doesn't
	 * exist.
	 * 
	 * @param key the name of the request attribute.
	 * @return the request attribute or <tt>null</tt> if it doesn't exist.
	 */
	@Override
	public Object get(Object key) {
		return getHeader(key.toString());
	}

	/**
	 * Saves an attribute in the request.
	 * 
	 * @param key the name of the request attribute.
	 * @param value the value to set.
	 * @return the object that was just set.
	 */
	@Override
	public Object put(String key, Object value) {
		throw Exceptions.unsupported("put");
	}

	/**
	 * Removes the specified request attribute.
	 * 
	 * @param key the name of the attribute to remove.
	 * @return the value that was removed or <tt>null</tt> if the value was not found (and hence,
	 *         not removed).
	 */
	@Override
	public Object remove(Object key) {
		throw Exceptions.unsupported("remove");
	}

	@Override
	public int size() {
		int sz = 0;

		Enumeration<String> em = request.getHeaderNames();
		while (em.hasMoreElements()) {
			em.nextElement();
			sz++;
		}
		return sz;
	}

	@Override
	public boolean isEmpty() {
		return request.getHeaderNames().hasMoreElements();
	}

	@Override
	public boolean containsKey(Object key) {
		Enumeration em = request.getHeaderNames();
		while (em.hasMoreElements()) {
			if (Objects.equals(em.nextElement(), key)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		Enumeration<String> em = request.getHeaderNames();
		while (em.hasMoreElements()) {
			final String key = em.nextElement();
			final Object val = getHeader(key);
			if (Objects.equals(val, value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		throw Exceptions.unsupported("putAll");
	}

	private Object getHeader(String key) {
		Enumeration<String> em = request.getHeaders(key);
		if (em == null) {
			return null;
		}
		
		List<String> vs = new ArrayList<String>();
		while (em.hasMoreElements()) {
			vs.add(em.nextElement());
		}

		return vs.size() > 1 ? vs : vs.get(0);
	}

}
