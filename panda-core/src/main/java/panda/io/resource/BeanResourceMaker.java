package panda.io.resource;

import java.io.IOException;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;


/**
 * A class for load database resource.
 */
public class BeanResourceMaker implements ResourceMaker {
	protected static Log log = Logs.getLog(BeanResourceMaker.class);

	protected Map<String, Map<String, Object>> resources = new HashMap<String, Map<String, Object>>();

	protected String classColumn;
	protected String localeColumn;
	protected String sourceColumn;
	protected String nameColumn;
	protected String valueColumn;
	protected String timestampColumn;

	protected String emptyString = "*";
	protected String packageName;

	protected long timestamp;

	public Resource getResource(Resource parent, String baseName, Locale locale, ClassLoader classLoader) throws IOException {
		Map<String, Object> map = resources.get(Resources.toBundleName(baseName, locale));
		if (map != null) {
			MapResource res = new MapResource(map, parent, locale);
			return res;
		}
		return null;
	}

	/**
	 * @return the classColumn
	 */
	public String getClassColumn() {
		return classColumn;
	}

	/**
	 * @param classColumn the classColumn to set
	 */
	public void setClassColumn(String classColumn) {
		this.classColumn = classColumn;
	}

	/**
	 * @return the localeColumn
	 */
	public String getLocaleColumn() {
		return localeColumn;
	}

	/**
	 * @param localeColumn the localeColumn to set
	 */
	public void setLocaleColumn(String localeColumn) {
		this.localeColumn = localeColumn;
	}

	/**
	 * @return the sourceColumn
	 */
	public String getSourceColumn() {
		return sourceColumn;
	}

	/**
	 * @param sourceColumn the sourceColumn to set
	 */
	public void setSourceColumn(String sourceColumn) {
		this.sourceColumn = sourceColumn;
	}

	/**
	 * @return the nameColumn
	 */
	public String getNameColumn() {
		return nameColumn;
	}

	/**
	 * @param nameColumn the nameColumn to set
	 */
	public void setNameColumn(String nameColumn) {
		this.nameColumn = nameColumn;
	}

	/**
	 * @return the valueColumn
	 */
	public String getValueColumn() {
		return valueColumn;
	}

	/**
	 * @param valueColumn the valueColumn to set
	 */
	public void setValueColumn(String valueColumn) {
		this.valueColumn = valueColumn;
	}

	/**
	 * @return the timestampColumn
	 */
	public String getTimestampColumn() {
		return timestampColumn;
	}

	/**
	 * @param timestampColumn the timestampColumn to set
	 */
	public void setTimestampColumn(String timestampColumn) {
		this.timestampColumn = timestampColumn;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @return the emptyString
	 */
	public String getEmptyString() {
		return emptyString;
	}

	/**
	 * @param emptyString the emptyString to set
	 */
	public void setEmptyString(String emptyString) {
		this.emptyString = emptyString;
	}

	protected String buildResourceName(String clazz, String locale) {
		StringBuilder sb = new StringBuilder();

		if (Strings.isNotEmpty(packageName)) {
			sb.append(packageName).append('.');
		}
		sb.append(clazz);

		if (Strings.isNotEmpty(locale) && !locale.equals(emptyString)) {
			sb.append('_').append(locale);
		}

		return sb.toString();
	}

	/**
	 * load resources
	 * @param resList resource list
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean loadResources(List<?> resList) {
		Map<String, Map<String, Object>> res = new HashMap<String, Map<String, Object>>();

		long now = System.currentTimeMillis();
		boolean modified = false;
		
		String last = null;
		Map<String, Object> properties = null;

		for (Object o : resList) {
			BeanHandler bh = Beans.i().getBeanHandler(o.getClass());
			
			String clazz = null;
			String locale = null;
			
			clazz = (String)bh.getPropertyValue(o, classColumn);
			if (Strings.isNotEmpty(localeColumn)) {
				locale = (String)bh.getPropertyValue(o, localeColumn);
			}
			if (!modified) {
				if (Strings.isNotEmpty(timestampColumn)) {
					Long t = null;
					Object v = bh.getPropertyValue(o, timestampColumn);
					if (v instanceof Long) {
						t = (Long)v;
					}
					else if (v instanceof Date) {
						t = ((Date)v).getTime();
					}
					else if (v instanceof Calendar) {
						t = ((Calendar)v).getTimeInMillis();
					}
					
					if (t > this.timestamp) {
						modified = true;
					}
				}
			}
			
			String bk = buildResourceName(clazz, locale);
			if (!bk.equals(last)) {
				last = bk;
				properties = new HashMap<String, Object>();
				res.put(bk, properties);
			}

			if (Strings.isEmpty(sourceColumn)) {
				String name = (String)bh.getPropertyValue(o, nameColumn);
				String value = (String)bh.getPropertyValue(o, valueColumn);
				properties.put(name, value);
			}
			else {
				Properties ps = new Properties();
				String source = (String)bh.getPropertyValue(o, sourceColumn);
				try {
					ps.load(new StringReader(source));
				}
				catch (IOException e) {
					throw Exceptions.wrapThrow(e);
				}
				for (Entry<Object, Object> en : ps.entrySet()) {
					properties.put((String)en.getKey(), (String)en.getValue());
				}
			}
		}

		resources = res;
		
		if (timestamp == 0) {
			timestamp = now;
			modified = true;
		}
		
		return modified;
	}
}
