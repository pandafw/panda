package panda.servlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import panda.lang.Collections;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.lang.collection.MapEntry;


/**
 * A simple implementation of the {@link java.util.Map} interface to handle a collection of attributes and
 * init parameters in a {@link javax.servlet.ServletContext} object. The {@link #entrySet()} method
 * enumerates over all servlet context attributes and init parameters and returns a collection of both.
 * Note, this will occur lazily - only when the entry set is asked for.
 * 
 * @author yf.frank.wang@gmail.com
 */
public class ServletContextMap implements Map<String, Object> {
	private ServletContext context;

	/**
	 * Creates a new map object given the servlet context.
	 * 
	 * @param ctx the servlet context
	 */
	public ServletContextMap(ServletContext ctx) {
		this.context = ctx;
	}

	/**
	 * Removes all entries from the Map and removes all attributes from the servlet context.
	 */
	public void clear() {
		synchronized (context) {
			Enumeration em = context.getAttributeNames();
			while (em.hasMoreElements()) {
				context.removeAttribute(em.nextElement().toString());
			}
		}
	}

	public Set<String> keySet() {
		synchronized (context) {
			Set<String> keys = new HashSet<String>();
	
			// Add servlet context attributes
			Enumeration em = context.getAttributeNames();
			while (em.hasMoreElements()) {
				keys.add(em.nextElement().toString());
			}

			// Add servlet context init params (does not override attribute)
			em = context.getInitParameterNames();
			while (em.hasMoreElements()) {
				keys.add(em.nextElement().toString());
			}
			return keys;
		}
	}

	public Collection<Object> values() {
		synchronized (context) {
			List<Object> vals = new ArrayList<Object>();

			Set<String> keys = keySet();
			for (String key : keys) {
				vals.add(get(key));
			}
			return vals;
		}
	}

	/**
	 * Creates a Set of all servlet context attributes as well as context init parameters.
	 * 
	 * @return a Set of all servlet context attributes as well as context init parameters.
	 */
	public Set<Entry<String, Object>> entrySet() {
		synchronized (context) {
			Set<Entry<String, Object>> entries = new HashSet<Entry<String, Object>>();

			Set<String> keys = keySet();
			for (String key : keys) {
				Object val = get(key);
				entries.add(new MapEntry<String, Object>(this, key, val));
			}
			return entries;
		}
	}

	/**
	 * Returns the servlet context attribute or init parameter based on the given key. If the entry
	 * is not found, <tt>null</tt> is returned.
	 * 
	 * @param key the entry key.
	 * @return the servlet context attribute or init parameter or <tt>null</tt> if the entry is not
	 *         found.
	 */
	public Object get(Object key) {
		synchronized (context) {
			// Try context attributes first, then init params
			// This gives the proper shadowing effects
			String keyString = key.toString();
			Object value = context.getAttribute(keyString);
	
			return (value == null) ? context.getInitParameter(keyString) : value;
		}
	}

	/**
	 * Sets a servlet context attribute given a attribute name and value.
	 * 
	 * @param key the name of the attribute.
	 * @param value the value to set.
	 * @return the attribute that was just set.
	 */
	public Object put(String key, Object value) {
		synchronized (context) {
			Object oldValue = get(key);
			context.setAttribute(key, value);
			return oldValue;
		}
	}

	/**
	 * Removes the specified servlet context attribute.
	 * 
	 * @param key the attribute to remove.
	 * @return the entry that was just removed.
	 */
	public Object remove(Object key) {
		Object value = get(key);
		context.removeAttribute(key.toString());
		return value;
	}

	public int size() {
		int sz = 0;

		Enumeration em = context.getAttributeNames();
		while (em.hasMoreElements()) {
			em.nextElement();
			sz++;
		}
		return sz;
	}

	public boolean isEmpty() {
		return context.getAttributeNames().hasMoreElements();
	}

	/**
	 * Checks if the specified session attribute with the given key exists.
	 * 
	 * @param key the name of the session attribute.
	 * @return <tt>true</tt> if the session attribute exits or <tt>false</tt> if it doesn't exist.
	 */
	public boolean containsKey(Object key) {
		synchronized (context) {
			Enumeration em = context.getAttributeNames();
			while (em.hasMoreElements()) {
				if (Objects.equals(em.nextElement(), key)) {
					return true;
				}
			}
			return false;
		}
	}

	public boolean containsValue(Object value) {
		synchronized (context) {
			Enumeration em = context.getAttributeNames();
			while (em.hasMoreElements()) {
				final String key = em.nextElement().toString();
				final Object val = context.getAttribute(key);
				if (Objects.equals(val, value)) {
					return true;
				}
			}
			return false;
		}
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		if (Collections.isEmpty(m)) {
			return;
		}
		
		synchronized (context) {
			for (Entry<? extends String, ? extends Object> en : m.entrySet()) {
				if (Strings.isNotEmpty(en.getKey())) {
					context.setAttribute(en.getKey(), en.getValue());
				}
			}
		}
	}
}
