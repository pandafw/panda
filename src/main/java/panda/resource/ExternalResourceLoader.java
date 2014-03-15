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
import panda.dao.sql.Sqls;
import panda.lang.Classes;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;


/**
 * A class for load database resource.
 * @author yf.frank.wang@gmail.com
 */
public abstract class ExternalResourceLoader {
	protected static Log log = Logs.getLog(ExternalResourceLoader.class);

	protected Map<String, Map<String, String>> resources = new HashMap<String, Map<String, String>>();

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
		return resources.get(className);
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
			return Objects.hashCodeBuilder().append(baseName).append(country).append(language).append(variant).toHashCode();
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

			BundleKey lastBundle = null;

			Map<String, String> properties = null;

			SqlLogger.logResultHeader(rs);
			
			Map<String, Map<String, String>> res = new HashMap<String, Map<String, String>>();
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
					res.put(bk.toString(), properties);
					bkList.add(bk);
				}

				String name = rs.getString(nameColumn);
				String value = rs.getString(valueColumn);
				properties.put(name, value);
			}
			resources = res;
		}
		finally {
			Sqls.safeClose(conn);
		}
	}
	
	/**
	 * load resources
	 * 
	 * @throws Exception if an error occurs
	 */
	@SuppressWarnings("unchecked")
	public void loadResources(List resList) throws Exception {
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

			String name = (String)bh.getPropertyValue(o, nameColumn);
			String value = (String)bh.getPropertyValue(o, valueColumn);
			properties.put(name, value);
		}
		
		resources = res;
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
