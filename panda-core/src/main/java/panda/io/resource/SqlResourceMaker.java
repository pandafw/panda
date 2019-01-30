package panda.io.resource;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
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
	
	public boolean loadResources(DataSource dataSource, String tableName) throws IOException, SQLException {
		return loadResources(dataSource, tableName, null);
	}
	
	/**
	 * load resources
	 * 
	 * @param dataSource data source
	 * @param tableName table name
	 * @param whereClause where clause string
	 * @return true if resource is modified
	 * @throws Exception if an error occurs
	 */
	public boolean loadResources(DataSource dataSource, String tableName, String whereClause) throws IOException, SQLException {
		String sql = "SELECT"
			+ " " + classColumn
			+ (Strings.isEmpty(localeColumn) ? "" : (", " + localeColumn))
			+ (Strings.isEmpty(sourceColumn) ? (", " + nameColumn + ", " + valueColumn) : (", " + sourceColumn))
			+ (Strings.isEmpty(timestampColumn) ? "" : (", " + timestampColumn))
			+ " FROM " + tableName
			+ (Strings.isEmpty(whereClause) ? "" : " WHERE " + whereClause)
			+ " ORDER BY "
			+ " " + classColumn
			+ (Strings.isEmpty(localeColumn) ? "" : (", " + localeColumn))
			+ (Strings.isEmpty(sourceColumn) ? (", " + nameColumn) : "")
			;

		Connection conn = dataSource.getConnection();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			long now = System.currentTimeMillis();
			boolean modified = false;
			
			String last = null;

			Map<String, Object> properties = null;

			SqlLogger.logResultHeader(rs);
			
			Map<String, Map<String, Object>> res = new HashMap<String, Map<String, Object>>();
			while (rs.next()) {
				SqlLogger.logResultValues(rs);

				String clazz = null;
				String locale = null;
				
				clazz = rs.getString(classColumn);
				if (Strings.isNotEmpty(localeColumn)) {
					locale = rs.getString(localeColumn);
				}
				if (!modified) {
					if (Strings.isNotEmpty(timestampColumn)) {
						Long t = rs.getLong(timestampColumn);
						if (t > this.timestamp) {
							modified = true;
						}
					}
				}
				
				String bk = buildResourceName(clazz, locale);
				if (!bk.equals(last)) {
					last = bk;
					properties = new HashMap<String, Object>();
					res.put(bk.toString(), properties);
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

			if (timestamp == 0) {
				timestamp = now;
				modified = true;
			}
			
			return modified;
		}
		finally {
			Sqls.safeClose(conn);
		}
	}
}
