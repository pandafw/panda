package panda.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import panda.bind.json.JsonObject;
import panda.bind.json.Jsons;
import panda.lang.Strings;


/**
 * Properties extented class.
 * @author yf.frank.wang@gmail.com
 */
@SuppressWarnings("serial")
public class PropertiesEx extends Properties {
	public PropertiesEx() throws IOException {
		super();
	}

	public PropertiesEx(String... paths) throws IOException {
		super();
		setPaths(paths);
	}

	/**
	 * @param file file
	 * @param props properties
	 * @throws IOException if an IO error occurs
	 */
	public static void load(Properties props, File file) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			props.load(is);
		}
		finally {
			Streams.safeClose(is);
		}
	}


	/**
	 * 加载指定文件/文件夹的Properties文件,合并成一个Properties对象
	 * <p>
	 * <b style=color:red>如果有重复的key,请务必注意加载的顺序!!<b/>
	 * 
	 * @param paths 需要加载的Properties文件路径
	 */
	public void setPaths(String... paths) throws IOException {
		InputStream is = null;
		try {
			for (String path : paths) {
				is = Streams.getStream(path);
				load(is);
				is.close();
			}
		}
		finally {
			Streams.safeClose(is);
		}
	}

	/**
	 * @param file file
	 * @throws IOException if an IO error occurs
	 */
	public void load(File file) throws IOException {
		load(this, file);
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
	 * @return the property
	 */
	public boolean getPropertyAsBoolean(String key, boolean defv) {
		String v = Strings.stripToNull(getProperty(key));
		if (Strings.isEmpty(v)) {
			return defv;
		}
		return Boolean.parseBoolean(v);
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
	 * @return the property
	 */
	public int getPropertyAsInt(String key, int defaultVar) {
		try {
			return Integer.parseInt(getProperty(key));
		}
		catch (NumberFormatException e) {
			return defaultVar;
		}
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
	 * @return the property
	 */
	public Long getPropertyAsLong(String key, long defaultVar) {
		try {
			return Long.parseLong(getProperty(key));
		}
		catch (NumberFormatException e) {
			return defaultVar;
		}
	}

	/**
	 * getPropertyAsList
	 * @param name resource name
	 * @return List value
	 */
	public List getPropertyAsList(String name) {
		return getPropertyAsList(name, null);
	}
	
	/**
	 * getPropertyAsList
	 * @param name resource name
	 * @param defaultValue default value
	 * @return list value
	 */
	public List getPropertyAsList(String name, List defaultValue) {
		List list = defaultValue;

		String expr = getProperty(name);
		if (Strings.isNotEmpty(expr)) {
			expr = Strings.strip(expr);
			if (!expr.startsWith("[") || !expr.endsWith("]")) {
				expr = "[" + expr + "]";
			}

			String em = "Invalid json array expression: " + name + " - " + expr;

			list = Jsons.fromJson(expr, ArrayList.class);
			if (list == null) {
				throw new RuntimeException(em);
			}
		}

		return list;
	}

	/**
	 * @param name resource name
	 * @return map value
	 */
	public Map getPropertyAsMap(String name) {
		return getPropertyAsMap(name, null);
	}
	
	/**
	 * @param name resource name
	 * @param defaultValue default value
	 * @return map value
	 */
	public Map getPropertyAsMap(String name, Map defaultValue) {
		Map map = defaultValue;

		String json = getProperty(name);
		map = JsonObject.fromJson(json);

		return map;
	}
}
