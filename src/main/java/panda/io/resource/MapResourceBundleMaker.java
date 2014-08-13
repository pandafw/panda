package panda.io.resource;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ResourceBundle;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.lang.Exceptions;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;


/**
 * A class for load database resource.
 * @author yf.frank.wang@gmail.com
 */
public class MapResourceBundleMaker implements ResourceBundleMaker {
	protected static Log log = Logs.getLog(MapResourceBundleMaker.class);

	protected Map<String, Map<String, String>> resources = new HashMap<String, Map<String, String>>();

	protected String tableName;
	protected String classColumn;
	protected String languageColumn;
	protected String countryColumn;
	protected String variantColumn;
	protected String sourceColumn;
	protected String nameColumn;
	protected String valueColumn;
	protected String whereClause;
	protected String emptyString = "*";
	protected String packageName;

	@SuppressWarnings("unchecked")
	public ResourceBundle getBundle(String baseName, Locale locale, ClassLoader loader,
			boolean reload) throws IllegalAccessException, InstantiationException, IOException {
		Map contents = resources.get(Resources.toBundleName(baseName, locale));
		if (contents != null) {
			MapResourceBundle bundle = new MapResourceBundle(contents);
			return bundle;
		}
		return null;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
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
	 * @return the languageColumn
	 */
	public String getLanguageColumn() {
		return languageColumn;
	}

	/**
	 * @param languageColumn the languageColumn to set
	 */
	public void setLanguageColumn(String languageColumn) {
		this.languageColumn = languageColumn;
	}

	/**
	 * @return the countryColumn
	 */
	public String getCountryColumn() {
		return countryColumn;
	}

	/**
	 * @param countryColumn the countryColumn to set
	 */
	public void setCountryColumn(String countryColumn) {
		this.countryColumn = countryColumn;
	}

	/**
	 * @return the variantColumn
	 */
	public String getVariantColumn() {
		return variantColumn;
	}

	/**
	 * @param variantColumn the variantColumn to set
	 */
	public void setVariantColumn(String variantColumn) {
		this.variantColumn = variantColumn;
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
	 * @return the whereClause
	 */
	public String getWhereClause() {
		return whereClause;
	}

	/**
	 * @param whereClause the whereClause to set
	 */
	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
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

	protected static class BundleKey {
		public String baseName;
		public String language;
		public String country;
		public String variant;
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(baseName);
			
			if (Strings.isEmpty(language)) {
				return sb.toString();
			}
			sb.append('_').append(language);
			
			if (Strings.isEmpty(country)) {
				return sb.toString();
			}
			sb.append('_').append(country);

			if (Strings.isEmpty(variant)) {
				return sb.toString();
			}
			sb.append('_').append(variant);
			
			return sb.toString();
		}
	
		public Locale toLocale() {
			if (Strings.isNotEmpty(language) && Strings.isNotEmpty(country) && Strings.isNotEmpty(variant)) {
				return new Locale(language, country, variant);
			}
			else if (Strings.isNotEmpty(language) && Strings.isNotEmpty(country)) {
				return new Locale(language, country);
			}
			else if (Strings.isNotEmpty(language)) {
				return new Locale(language);
			}
			else {
				return Locale.getDefault();
			}
		}
		
		public String toMissesKey() {
			return baseName + "_" + toLocale().toString();
		}

		@Override
		public int hashCode() {
			return Objects.hashCodes(baseName, country, language, variant);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			
			BundleKey rhs = (BundleKey) obj;
			return Objects.equalsBuilder().append(baseName, rhs.baseName)
					.append(country, rhs.country)
					.append(language, rhs.language)
					.append(variant, rhs.variant)
					.isEquals();
		}
	}

	protected BundleKey buildBundleKey(String clazz, String language, String country, String variant) {
		BundleKey bk = new BundleKey();
		
		if (packageName != null) {
			bk.baseName = packageName + '.' + clazz;
		}
		else {
			bk.baseName = clazz;
		}
		
		if (emptyString != null && emptyString.equals(language)) {
			return bk;
		}
		else {
			bk.language = language;
		}

		if (emptyString != null && emptyString.equals(country)) {
			return bk;
		}
		else {
			bk.country = country;
		}
		
		if (emptyString != null && emptyString.equals(variant)) {
			return bk;
		}
		else {
			bk.variant = variant;
		}
		
		return bk;
	}

//  comment for thread unsafe
//	/**
//	 * @param clazz class name
//	 * @param language language
//	 * @param country country
//	 * @param variant variant
//	 * @param name resource name
//	 * @param value resource value 
//	 * @return true if put resource successfully
//	 */
//	public boolean putResource(String clazz, String language, String country, String variant, String name, String value) {
//		BundleKey bk = buildBundleKey(clazz, language, country, variant);
//		Map<String, String> rm = resources.get(bk.toString());
//		if (rm != null) {
//			if (value == null) {
//				rm.remove(name);
//			}
//			else {
//				rm.put(name, value);
//			}
//			return true;
//		}
//		return false;
//	}

	/**
	 * load resources
	 */
	@SuppressWarnings("unchecked")
	public void loadResources(List resList) {
		Map<String, Map<String, String>> res = new HashMap<String, Map<String, String>>();

		BundleKey lastBundle = null;
		Map<String, String> properties = null;
		
		for (Object o : resList) {
			BeanHandler bh = Beans.i().getBeanHandler(o.getClass());
			
			String clazz = null;
			String language = null;
			String country = null;
			String variant = null;
			
			clazz = (String)bh.getPropertyValue(o, classColumn);
			if (Strings.isNotEmpty(languageColumn)) {
				language = (String)bh.getPropertyValue(o, languageColumn);
			}
			if (Strings.isNotEmpty(countryColumn)) {
				country = (String)bh.getPropertyValue(o, countryColumn);
			}
			if (Strings.isNotEmpty(variantColumn)) {
				variant = (String)bh.getPropertyValue(o, variantColumn);
			}
			
			BundleKey bk = buildBundleKey(clazz, language, country, variant);
			if (!bk.equals(lastBundle)) {
				lastBundle = bk;
				properties = new HashMap<String, String>();
				res.put(bk.toString(), properties);
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
	}
}
