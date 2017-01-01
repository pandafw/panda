package panda.servlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import panda.lang.Collections;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.lang.collection.MapEntry;

/**
 * A simple implementation of the {@link java.util.Map} interface to handle a collection of HTTP
 * session attributes. The {@link #entrySet()} method enumerates over all session attributes and
 * creates a Set of entries. Note, this will occur lazily - only when the entry set is asked for.
 * 
 */
public class HttpSessionMap implements Map<String, Object> {
	protected HttpSession session;
	protected HttpServletRequest request;

	/**
	 * Creates a new session map given a http servlet request. Note, ths enumeration of request
	 * attributes will occur when the map entries are asked for.
	 * 
	 * @param request the http servlet request object.
	 */
	public HttpSessionMap(HttpServletRequest request) {
		// note, holding on to this request and relying on lazy session initalization will not work
		// if you are running your action invocation in a background task, such as using the
		// "execAndWait" interceptor
		this.request = request;
		this.session = request.getSession(false);
	}

	/**
	 * Creates a new session map.
	 * 
	 * @param session session.
	 */
	public HttpSessionMap(HttpSession session) {
		this.request = null;
		this.session = session;
	}


	/**
	 * Invalidate the http session.
	 */
	public void invalidate() {
		if (session == null) {
			return;
		}

		synchronized (session) {
			session.invalidate();
			session = null;
		}
	}

	/**
	 * Removes all attributes from the session as well as clears entries in this map.
	 */
	public void clear() {
		if (session == null) {
			return;
		}

		synchronized (session) {
			Enumeration<String> attributeNamesEnum = session.getAttributeNames();
			while (attributeNamesEnum.hasMoreElements()) {
				session.removeAttribute(attributeNamesEnum.nextElement());
			}
		}

	}

	public Set<String> keySet() {
		if (session == null) {
			return Collections.emptySet();
		}

		synchronized (session) {
			Set<String> keys = new HashSet<String>();
	
			Enumeration em = session.getAttributeNames();
			while (em.hasMoreElements()) {
				keys.add(em.nextElement().toString());
			}
			return keys;
		}
	}

	public Collection<Object> values() {
		if (session == null) {
			return Collections.emptyList();
		}

		synchronized (session) {
			List<Object> vals = new ArrayList<Object>();
	
			Enumeration em = session.getAttributeNames();
			while (em.hasMoreElements()) {
				final String key = em.nextElement().toString();
				final Object value = session.getAttribute(key);
				vals.add(value);
			}
			return vals;
		}
	}

	/**
	 * Returns a Set of attributes from the http session.
	 * 
	 * @return a Set of attributes from the http session.
	 */
	public Set<Entry<String, Object>> entrySet() {
		if (session == null) {
			return Collections.emptySet();
		}

		synchronized (session) {
			Set<Entry<String, Object>> entries = new HashSet<Entry<String, Object>>();

			Enumeration enumeration = session.getAttributeNames();

			while (enumeration.hasMoreElements()) {
				final String key = enumeration.nextElement().toString();
				final Object value = session.getAttribute(key);
				entries.add(new MapEntry<String, Object>(this, key, value));
			}
			return entries;
		}
	}

	/**
	 * Returns the session attribute associated with the given key or <tt>null</tt> if it doesn't
	 * exist.
	 * 
	 * @param key the name of the session attribute.
	 * @return the session attribute or <tt>null</tt> if it doesn't exist.
	 */
	public Object get(Object key) {
		if (session == null) {
			return null;
		}

		synchronized (session) {
			return session.getAttribute(key.toString());
		}
	}

	protected void checkSession() {
		synchronized (this) {
			if (session == null) {
				if (request == null) {
					throw new IllegalArgumentException("session is null");
				}
				session = request.getSession(true);
			}
		}
	}

	/**
	 * Saves an attribute in the session.
	 * 
	 * @param key the name of the session attribute.
	 * @param value the value to set.
	 * @return the object that was just set.
	 */
	public Object put(String key, Object value) {
		checkSession();
		synchronized (session) {
			Object oldValue = get(key);
			session.setAttribute(key, value);
			return oldValue;
		}
	}

	/**
	 * Removes the specified session attribute.
	 * 
	 * @param key the name of the attribute to remove.
	 * @return the value that was removed or <tt>null</tt> if the value was not found (and hence,
	 *         not removed).
	 */
	public Object remove(Object key) {
		if (session == null) {
			return null;
		}

		synchronized (session) {
			Object value = get(key);
			session.removeAttribute(key.toString());
			return value;
		}
	}

	public int size() {
		if (session == null) {
			return 0;
		}

		int sz = 0;
		Enumeration em = session.getAttributeNames();
		while (em.hasMoreElements()) {
			em.nextElement();
			sz++;
		}
		return sz;
	}

	public boolean isEmpty() {
		if (session == null) {
			return false;
		}
		return session.getAttributeNames().hasMoreElements();
	}

	/**
	 * Checks if the specified session attribute with the given key exists.
	 * 
	 * @param key the name of the session attribute.
	 * @return <tt>true</tt> if the session attribute exits or <tt>false</tt> if it doesn't exist.
	 */
	public boolean containsKey(Object key) {
		if (session == null) {
			return false;
		}

		synchronized (session) {
			Enumeration em = session.getAttributeNames();
			while (em.hasMoreElements()) {
				if (Objects.equals(em.nextElement(), key)) {
					return true;
				}
			}
			return false;
		}
	}

	public boolean containsValue(Object value) {
		if (session == null) {
			return false;
		}

		synchronized (session) {
			Enumeration em = session.getAttributeNames();
			while (em.hasMoreElements()) {
				final String key = em.nextElement().toString();
				final Object val = session.getAttribute(key);
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
		
		checkSession();
		synchronized (session) {
			for (Entry<? extends String, ? extends Object> en : m.entrySet()) {
				if (Strings.isNotEmpty(en.getKey())) {
					session.setAttribute(en.getKey(), en.getValue());
				}
			}
		}
	}
}
