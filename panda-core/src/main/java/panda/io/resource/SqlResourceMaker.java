package panda.io.resource;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.DataSource;

import panda.dao.sql.SqlLogger;
import panda.dao.sql.Sqls;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;


/**
 * A class for load database resource.
 */
public class SqlResourceMaker extends BeanResourceMaker {
	protected static Log log = Logs.getLog(SqlResourceMaker.class);
	
	public void loadResources(DataSource dataSource, String tableName) throws Exception {
		loadResources(dataSource, tableName, null);
	}
	
	/**
	 * load resources
	 * 
	 * @param dataSource data source
	 * @param tableName table name
	 * @param whereClause where clause string
	 * @throws Exception if an error occurs
	 */
	public void loadResources(DataSource dataSource, String tableName, String whereClause) throws Exception {
		String sql = "SELECT"
			+ " " + classColumn
			+ (Strings.isEmpty(languageColumn) ? "" : (", " + languageColumn))
			+ (Strings.isEmpty(countryColumn) ? "" : (", " + countryColumn))
			+ (Strings.isEmpty(variantColumn) ? "" : (", " + variantColumn))
			+ (Strings.isEmpty(sourceColumn) ? (", " + nameColumn + ", " + valueColumn) : (", " + sourceColumn))
			+ " FROM " + tableName
			+ (Strings.isEmpty(whereClause) ? "" : " WHERE " + whereClause)
			+ " ORDER BY "
			+ " " + classColumn
			+ (Strings.isEmpty(languageColumn) ? "" : (", " + languageColumn))
			+ (Strings.isEmpty(countryColumn) ? "" : (", " + countryColumn))
			+ (Strings.isEmpty(variantColumn) ? "" : (", " + variantColumn))
			+ (Strings.isEmpty(sourceColumn) ? (", " + nameColumn) : "")
			;

		Connection conn = dataSource.getConnection();

		List<BundleKey> bkList = new ArrayList<BundleKey>();
		
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			BundleKey lastBundle = null;

			Map<String, Object> properties = null;

			SqlLogger.logResultHeader(rs);
			
			Map<String, Map<String, Object>> res = new HashMap<String, Map<String, Object>>();
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
					properties = new HashMap<String, Object>();
					res.put(bk.toString(), properties);
					bkList.add(bk);
				}

				if (Strings.isEmpty(sourceColumn)) {
					String name = rs.getString(nameColumn);
					String value = rs.getString(valueColumn);
					properties.put(name, value);
				}
				else {
					Properties ps = new Properties();
					String source = rs.getString(sourceColumn);
					ps.load(new StringReader(source));
					for (Entry<Object, Object> en : ps.entrySet()) {
						properties.put((String)en.getKey(), (String)en.getValue());
					}
				}
			}
			resources = res;
		}
		finally {
			Sqls.safeClose(conn);
		}
	}
}
