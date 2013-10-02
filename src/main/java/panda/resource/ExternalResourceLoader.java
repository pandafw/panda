package panda.resource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.dao.sql.SqlLogger;
import panda.lang.Classes;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;


/**
 * A class for load database resource.
 * @author yf.frank.wang@gmail.com
 */
public abstract class ExternalResourceLoader {
	protected static Log log = Logs.getLog(ExternalResourceLoader.class);

	protected Map<String, Map<String, String>> resourceMap = new HashMap<String, Map<String, String>>();

	private static ExternalResourceLoader instance;
	
	protected DataSource dataSource;
	protected String tableName;
	protected String classColumn;
	protected String languageColumn;
	protected String countryColumn;
	protected String variantColumn;
	protected String nameColumn;
	protected String valueColumn;
	protected String whereClause;
	protected String emptyString = "*";
	protected String packageName;

	/**
	 * @return the instance
	 */
	public static ExternalResourceLoader getInstance() {
		if (instance == null) {
			instance = newInstance();
		}
		return instance;
	}

	/**
	 * @param instance the instance to set
	 */
	public static void setInstance(ExternalResourceLoader instance) {
		ExternalResourceLoader.instance = instance;
	}

	/**
	 * @return new instance
	 */
	public static ExternalResourceLoader newInstance() {
		try {
			return (ExternalResourceLoader)Classes.newInstance(ExternalResourceLoader.class.getName() + "16");
		}
		catch (Throwable ex) {
			log.warn("Failed to create " + ExternalResourceLoader.class.getName() 
				+ "16 instance, use " + ExternalResourceLoader15.class.getName() + " instead.");
			return new ExternalResourceLoader15();
		}
	}
	
	/**
	 * @return the classLoader
	 */
	public ClassLoader getClassLoader() {
		return null;
	}

    /**
	 * @param className class name
	 * @return the contents map
	 */
	public Map<String, String> getContentsMap(String className) {
		return resourceMap.get(className);
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
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
			final int prime = 31;
			int result = 1;
			result = prime * result + ((baseName == null) ? 0 : baseName.hashCode());
			result = prime * result + ((country == null) ? 0 : country.hashCode());
			result = prime * result + ((language == null) ? 0 : language.hashCode());
			result = prime * result + ((variant == null) ? 0 : variant.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			BundleKey other = (BundleKey) obj;
			if (baseName == null) {
				if (other.baseName != null)
					return false;
			}
			else if (!baseName.equals(other.baseName))
				return false;
			if (country == null) {
				if (other.country != null)
					return false;
			}
			else if (!country.equals(other.country))
				return false;
			if (language == null) {
				if (other.language != null)
					return false;
			}
			else if (!language.equals(other.language))
				return false;
			if (variant == null) {
				if (other.variant != null)
					return false;
			}
			else if (!variant.equals(other.variant))
				return false;
			return true;
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

	/**
	 * @param clazz class name
	 * @param language language
	 * @param country country
	 * @param variant variant
	 * @param name resource name
	 * @param value resource value 
	 * @return true if put resource successfully
	 */
	public synchronized boolean putResource(String clazz, String language, String country, String variant, String name, String value) {
		BundleKey bk = buildBundleKey(clazz, language, country, variant);
		Map<String, String> rm = resourceMap.get(bk.toString());
		if (rm != null) {
			if (value == null) {
				rm.remove(name);
			}
			else {
				rm.put(name, value);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * load resources
     * @throws Exception if an error occurs
	 */
	public void loadResources() throws Exception {
		String sql = "SELECT"
			+ " " + classColumn + ", "
			+ (Strings.isEmpty(languageColumn) ? "" : (" " + languageColumn + ", "))
			+ (Strings.isEmpty(countryColumn) ? "" : (" " + countryColumn + ", "))
			+ (Strings.isEmpty(variantColumn) ? "" : (" " + variantColumn + ", "))
			+ " " + nameColumn + ", "
			+ " " + valueColumn
			+ " FROM " + tableName
			+ (Strings.isEmpty(whereClause) ? "" : " WHERE " + whereClause)
			+ " ORDER BY "
			+ " " + classColumn + ", "
			+ (Strings.isEmpty(languageColumn) ? "" : (" " + languageColumn + ", "))
			+ (Strings.isEmpty(countryColumn) ? "" : (" " + countryColumn + ", "))
			+ (Strings.isEmpty(variantColumn) ? "" : (" " + variantColumn + ", "))
			+ " " + nameColumn + ", "
			+ " " + valueColumn;

		Connection conn = dataSource.getConnection();

		List<BundleKey> bkList = new ArrayList<BundleKey>();
		
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			resourceMap.clear();

			BundleKey lastBundle = null;

			Map<String, String> properties = null;

			SqlLogger.logResultHeader(rs);
			
			while (rs.next()) {
				SqlLogger.logResultValues(rs);

				String clazz = null;
				String language = null;
				String country = null;
				String variant = null;
				
				clazz = rs.getString(classColumn);
				if (Strings.isNotEmpty(languageColumn)) {
					language = rs.getString(languageColumn);
				}
				if (Strings.isNotEmpty(countryColumn)) {
					country = rs.getString(countryColumn);
				}
				if (Strings.isNotEmpty(variantColumn)) {
					variant = rs.getString(variantColumn);
				}
				
				BundleKey bk = buildBundleKey(clazz, language, country, variant);
				if (!bk.equals(lastBundle)) {
					lastBundle = bk;
					properties = new HashMap<String, String>();
					resourceMap.put(bk.toString(), properties);
					bkList.add(bk);
				}

				String name = rs.getString(nameColumn);
				String value = rs.getString(valueColumn);
				properties.put(name, value);
			}
		}
		finally {
			conn.close();
		}
	}
	
	/**
	 * load resources
     * @throws Exception if an error occurs
	 */
	@SuppressWarnings("unchecked")
	public void loadResources(List resList) throws Exception {
		resourceMap.clear();

		BundleKey lastBundle = null;

		Map<String, String> properties = null;

		for (Object o : resList) {
			BeanHandler bh = Beans.me().getBeanHandler(o.getClass());
			
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
				resourceMap.put(bk.toString(), properties);
			}

			String name = (String)bh.getPropertyValue(o, nameColumn);
			String value = (String)bh.getPropertyValue(o, valueColumn);
			properties.put(name, value);
		}
	}
	
	public ResourceBundle getBundle(String baseName) {
		return ResourceBundle.getBundle(baseName);
	}

	public ResourceBundle getBundle(String baseName, Locale locale) {
		return ResourceBundle.getBundle(baseName, locale);
	}

	public ResourceBundle getBundle(String baseName, Locale locale, ClassLoader loader) {
		return ResourceBundle.getBundle(baseName, locale, loader);
	}
}
