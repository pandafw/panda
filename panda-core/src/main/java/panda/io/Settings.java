package panda.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import panda.bind.json.Jsons;
import panda.cast.Castors;
import panda.lang.Strings;


public class Settings implements Map<String, String> {
	private Map<String, String> props = new HashMap<String, String>();
	
	public Settings() {
	}

	public Settings(String... files) throws IOException {
		load(files);
	}

	/**
	 * load multiple properties files.
	 * 
	 * @param paths properties files
	 * @throws IOException if an IO error occurred
	 */
	public synchronized void load(String... paths) throws IOException {
		for (String path : paths) {
			load(path);
		}
	}

	/**
	 * @param is input stream
	 * @throws IOException if an IO error occurs
	 */
	public synchronized void load(InputStream is) throws IOException {
		Properties ps = new Properties();
		ps.load(is);
		putAll(ps);
	}

	/**
	 * @param file file
	 * @throws IOException if an IO error occurs
	 */
	public synchronized void load(File file) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			load(is);
		}
		finally {
			Streams.safeClose(is);
		}
	}

	/**
	 * @param file file
	 * @throws IOException if an IO error occurs
	 */
	public synchronized void load(String file) throws IOException {
		InputStream is = null;
		try {
			is = Streams.getStream(file);
			load(is);
		}
		finally {
			Streams.safeClose(is);
		}
	}
	
	//------------------------------------------------------------------------
	@Override
	public synchronized int size() {
		return props.size();
	}

	@Override
	public synchronized boolean isEmpty() {
		return props.isEmpty();
	}

	@Override
	public synchronized boolean containsKey(Object key) {
		return props.containsKey(key);
	}

	@Override
	public synchronized boolean containsValue(Object value) {
		return props.containsValue(value);
	}

	@Override
	public synchronized String get(Object key) {
		return props.get(key);
	}

	@Override
	public synchronized String put(String key, String value) {
		return props.put(key, value);
	}

	@Override
	public synchronized String remove(Object key) {
		return props.remove(key);
	}

	public synchronized void putAll(Properties ps) {
		for (Iterator<Entry<Object, Object>> i = ps.entrySet().iterator(); i.hasNext(); ) {
			Entry<Object, Object> e = i.next();
			props.put(e.getKey().toString(), e.getValue().toString());
		}
	}

	@Override
	public synchronized void putAll(Map<? extends String, ? extends String> m) {
		props.putAll(m);
	}

	@Override
	public synchronized void clear() {
		props.clear();
	}

	@Override
	public synchronized Set<String> keySet() {
		return props.keySet();
	}

	@Override
	public synchronized Collection<String> values() {
		return props.values();
	}

	@Override
	public synchronized Set<Entry<String, String>> entrySet() {
		return props.entrySet();
	}
	
	//------------------------------------------------------------------------
	public String setProperty(String key, String value) {
		return put(key, value);
	}
	
	public String getProperty(String key, String def) {
		String v = get(key);
		if (Strings.isEmpty(v)) {
			v = def;
		}
		return v;
	}

	public String getProperty(String key) {
		return get(key);
	}

	/**
	 * @param key key
	 * @return the property
	 */
	public boolean getPropertyAsBoolean(String key) {
		return getPropertyAsBoolean(key, false);
	}

	/**
	 * @param key key
	 * @param def default value
	 * @return the property
	 */
	public boolean getPropertyAsBoolean(String key, boolean def) {
		String v = Strings.stripToNull(getProperty(key));
		if (Strings.isEmpty(v)) {
			return def;
		}
		return (Boolean)Castors.scast(v, boolean.class);
	}

	/**
	 * @param key key
	 * @return the property
	 */
	public int getPropertyAsInt(String key) {
		return getPropertyAsInt(key, 0);
	}

	/**
	 * @param key key
	 * @param def default value
	 * @return the property
	 */
	public int getPropertyAsInt(String key, int def) {
		String v = Strings.stripToNull(getProperty(key));
		if (Strings.isEmpty(v)) {
			return def;
		}
		return (Integer)Castors.scast(v, int.class);
	}

	/**
	 * @param key key
	 * @return the property
	 */
	public long getPropertyAsLong(String key) {
		return getPropertyAsLong(key, 0L);
	}

	/**
	 * @param key key
	 * @param def default value
	 * @return the property
	 */
	public Long getPropertyAsLong(String key, long def) {
		String v = Strings.stripToNull(getProperty(key));
		if (Strings.isEmpty(v)) {
			return def;
		}
		return (Long)Castors.scast(v, long.class);
	}

	/**
	 * getPropertyAsList
	 * @param name resource name
	 * @return List value
	 */
	public List<?> getPropertyAsList(String name) {
		return getPropertyAsList(name, null);
	}
	
	/**
	 * getPropertyAsList
	 * @param name resource name
	 * @param defaultValue default value
	 * @return list value
	 */
	public List<?> getPropertyAsList(String name, List<?> defaultValue) {
		List<?> list = defaultValue;

		String expr = Strings.stripToNull(get(name));
		if (Strings.isNotEmpty(expr)) {
			if (!Strings.startsWithChar(expr, '[') && !Strings.endsWithChar(expr, ']')) {
				expr = "[" + expr + "]";
			}

			list = Jsons.fromJson(expr, ArrayList.class);
		}

		return list;
	}

	/**
	 * @param name resource name
	 * @return map value
	 */
	public Map<String, ?> getPropertyAsMap(String name) {
		return getPropertyAsMap(name, null);
	}
	
	/**
	 * @param name resource name
	 * @param defaultValue default value
	 * @return map value
	 */
	public Map<String, ?> getPropertyAsMap(String name, Map<String, ?> defaultValue) {
		Map<String, ?> map = defaultValue;

		String expr = Strings.stripToNull(get(name));
		if (Strings.isNotEmpty(expr)) {
			if (!Strings.startsWithChar(expr, '{') && !Strings.endsWithChar(expr, '}')) {
				expr = "{" + expr + "}";
			}

			map = Jsons.fromJson(expr, LinkedHashMap.class);
		}

		return map;
	}
}
