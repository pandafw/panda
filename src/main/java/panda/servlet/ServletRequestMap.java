package panda.servlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import panda.lang.Collections;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.lang.collection.MapEntry;


/**
 * A simple implementation of the {@link java.util.Map} interface to handle a collection of request attributes.
 * 
 * @author yf.frank.wang@gmail.com
 */
public class ServletRequestMap implements Map<String, Object> {
	private ServletRequest request;

	/**
	 * Saves the request to use as the backing for getting and setting values
	 * 
	 * @param request the http servlet request.
	 */
	public ServletRequestMap(final ServletRequest request) {
		this.request = request;
	}

	/**
	 * Removes all attributes from the request as well as clears entries in this map.
	 */
	public void clear() {
		Enumeration keys = request.getAttributeNames();

		while (keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			request.removeAttribute(key);
		}
	}

	public Set<String> keySet() {
		Set<String> keys = new HashSet<String>();

		Enumeration em = request.getAttributeNames();
		while (em.hasMoreElements()) {
			keys.add(em.nextElement().toString());
		}

		return keys;
	}

	public Collection<Object> values() {
		List<Object> vals = new ArrayList<Object>();

		Enumeration em = request.getAttributeNames();
		while (em.hasMoreElements()) {
			final String key = em.nextElement().toString();
			final Object value = request.getAttribute(key);
			vals.add(value);
		}

		return vals;
	}

	/**
	 * Returns a Set of attributes from the http request.
	 * 
	 * @return a Set of attributes from the http request.
	 */
	public Set<Entry<String, Object>> entrySet() {
		Set<Entry<String, Object>> entries = new HashSet<Entry<String, Object>>();

		Enumeration em = request.getAttributeNames();
		while (em.hasMoreElements()) {
			final String key = em.nextElement().toString();
			final Object value = request.getAttribute(key);
			entries.add(new MapEntry<String, Object>(this, key, value));
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
	public Object get(Object key) {
		return request.getAttribute(key.toString());
	}

	/**
	 * Saves an attribute in the request.
	 * 
	 * @param key the name of the request attribute.
	 * @param value the value to set.
	 * @return the object that was just set.
	 */
	public Object put(String key, Object value) {
		Object oldValue = get(key);
		request.setAttribute(key, value);
		return oldValue;
	}

	/**
	 * Removes the specified request attribute.
	 * 
	 * @param key the name of the attribute to remove.
	 * @return the value that was removed or <tt>null</tt> if the value was not found (and hence,
	 *         not removed).
	 */
	public Object remove(Object key) {
		Object value = get(key);
		request.removeAttribute(key.toString());
		return value;
	}

	public int size() {
		int sz = 0;

		Enumeration em = request.getAttributeNames();
		while (em.hasMoreElements()) {
			em.nextElement();
			sz++;
		}
		return sz;
	}

	public boolean isEmpty() {
		return request.getAttributeNames().hasMoreElements();
	}

	public boolean containsKey(Object key) {
		Enumeration em = request.getAttributeNames();
		while (em.hasMoreElements()) {
			if (Objects.equals(em.nextElement(), key)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsValue(Object value) {
		Enumeration em = request.getAttributeNames();
		while (em.hasMoreElements()) {
			final String key = em.nextElement().toString();
			final Object val = request.getAttribute(key);
			if (Objects.equals(val, value)) {
				return true;
			}
		}
		return false;
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		if (Collections.isEmpty(m)) {
			return;
		}
		
		for (Entry<? extends String, ? extends Object> en : m.entrySet()) {
			if (Strings.isNotEmpty(en.getKey())) {
				request.setAttribute(en.getKey(), en.getValue());
			}
		}
	}
}
