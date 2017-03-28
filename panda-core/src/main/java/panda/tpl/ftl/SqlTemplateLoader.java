package panda.tpl.ftl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;

import javax.sql.DataSource;

import panda.dao.sql.SqlLogger;
import panda.dao.sql.Sqls;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;


/**
 * load template from database
 */
public class SqlTemplateLoader extends ExternalTemplateLoader {
	private static final Log log = Logs.getLog(SqlTemplateLoader.class);

	public void loadTemplates(DataSource dataSource, String tableName) throws Exception {
		loadTemplates(dataSource, tableName, null);
	}
	
	/**
	 * Load templates from database.
	 * 
	 * @param dataSource the data source
	 * @param tableName the table name
	 * @param whereClause the where clause
	 * @throws Exception if an error occurs
	 */
	public void loadTemplates(DataSource dataSource, String tableName, String whereClause) throws Exception {
		String sql = "SELECT"
			+ " " + nameColumn + ", "
			+ (Strings.isEmpty(languageColumn) ? "" : (" " + languageColumn + ", "))
			+ (Strings.isEmpty(countryColumn) ? "" : (" " + countryColumn + ", "))
			+ (Strings.isEmpty(variantColumn) ? "" : (" " + variantColumn + ", "))
			+ (Strings.isEmpty(timestampColumn) ? "" : (" " + timestampColumn + ", "))
			+ " " + sourceColumn
			+ " FROM " + tableName
			+ (Strings.isEmpty(whereClause) ? "" : " WHERE " + whereClause)
			+ " ORDER BY "
			+ " " + nameColumn + ", "
			+ (Strings.isEmpty(languageColumn) ? "" : (" " + languageColumn + ", "))
			+ (Strings.isEmpty(countryColumn) ? "" : (" " + countryColumn + ", "))
			+ (Strings.isEmpty(variantColumn) ? "" : (" " + variantColumn + ", "))
			+ " " + sourceColumn;

		Connection conn = dataSource.getConnection();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			SqlLogger.logResultHeader(rs);

			Map<String, StringTemplateSource> templates = newTemplates();
			while (rs.next()) {
				SqlLogger.logResultValues(rs);
				
				long lastModified = System.currentTimeMillis();
				if (Strings.isNotEmpty(timestampColumn)) {
					Date v = rs.getTimestamp(timestampColumn);
					if (v != null) {
						lastModified = v.getTime();
					}
				}

				String name = null;
				String language = null;
				String country = null;
				String variant = null;
				
				name = rs.getString(nameColumn);
				if (Strings.isNotEmpty(languageColumn)) {
					language = rs.getString(languageColumn);
				}
				if (Strings.isNotEmpty(countryColumn)) {
					country = rs.getString(countryColumn);
				}
				if (Strings.isNotEmpty(variantColumn)) {
					variant = rs.getString(variantColumn);
				}

				String templateName = buildTemplateName(name, language, country, variant); 
				String templateSource = rs.getString(sourceColumn);
				templates.put(name, new StringTemplateSource(name, templateSource, lastModified));

				if (log.isDebugEnabled()) {
					log.debug("load template - " + templateName);
				}
			}
			
			setTemplates(templates);
		}
		finally {
			Sqls.safeClose(conn);
		}
	}
}
