package panda.exts.freemarker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.dao.sql.SqlLogger;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;


/**
 * load template from database
 */
public class DatabaseTemplateLoader extends StringTemplateLoader {
	private static final Log log = Logs.getLog(DatabaseTemplateLoader.class);

	private DataSource dataSource;
	private String tableName;
	private String nameColumn;
	private String languageColumn;
	private String countryColumn;
	private String variantColumn;
	private String sourceColumn;
	private String timestampColumn;
	private String whereClause;
	private String emptyString = "_";
	private String extension = ".ftl";

	
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
	 * @param timestampColumn the timestampColumn to set
	 */
	public void setTimestampColumn(String timestampColumn) {
		this.timestampColumn = timestampColumn;
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
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
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

	/**
     * Load templates from database.
     * @throws Exception if an error occurs
     */
    public void loadTemplates() throws Exception {
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
			
			templates.clear();

			SqlLogger.logResultHeader(rs);

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
				putTemplate(templateName, templateSource, lastModified);

				if (log.isDebugEnabled()) {
					log.debug("load template - " + templateName);
				}
			}
		}
		finally {
			conn.close();
		}
	}

	/**
     * Load templates from list.
     */
	@SuppressWarnings("unchecked")
	public void loadTemplates(List tplList) {
		templates.clear();

		for (Object o : tplList) {
	    	BeanHandler bh = Beans.me().getBeanHandler(o.getClass());
			long lastModified = System.currentTimeMillis();
			if (Strings.isNotEmpty(timestampColumn)) {
				Date v = (Date)bh.getPropertyValue(o, timestampColumn);
				if (v != null) {
					lastModified = v.getTime();
				}
			}

			String name = null;
			String language = null;
			String country = null;
			String variant = null;
			
			name = (String)bh.getPropertyValue(o, nameColumn);
			if (Strings.isNotEmpty(languageColumn)) {
				language = (String)bh.getPropertyValue(o, languageColumn);
			}
			if (Strings.isNotEmpty(countryColumn)) {
				country = (String)bh.getPropertyValue(o, countryColumn);
			}
			if (Strings.isNotEmpty(variantColumn)) {
				variant = (String)bh.getPropertyValue(o, variantColumn);
			}

			String templateName = buildTemplateName(name, language, country, variant); 
			String templateSource = (String)bh.getPropertyValue(o, sourceColumn);
			putTemplate(templateName, templateSource, lastModified);

			if (log.isDebugEnabled()) {
				log.debug("load template - " + templateName);
			}
		}
	}

    private void appendLocalePart(StringBuilder name, String v) {
		if (emptyString != null && emptyString.equals(v)) {
			v = null;
		}
		if (Strings.isNotEmpty(v)) {
			name.append('_').append(v);
		}
    }

    /**
     * @param name name
     * @param language language
     * @param country country
     * @param variant variant
     * @return template name
     */
    private String buildTemplateName(String name, String language, String country, String variant) {
		StringBuilder templateName = new StringBuilder();
		
		templateName.append(name);
		appendLocalePart(templateName, language);
		appendLocalePart(templateName, country);
		appendLocalePart(templateName, variant);
		
		if (Strings.isNotEmpty(extension)) {
			templateName.append(extension);
		}

		return templateName.toString();
    }
    
    /**
     * Puts a template into the loader. A call to this method is identical to 
     * the call to the {@link #putTemplate(String, String, String, String, String, long)} 
     * passing <tt>System.currentTimeMillis()</tt> as the third argument.
     * @param name the name of the template.
     * @param language language
     * @param country country
     * @param variant variant
     * @param templateSource the source code of the template.
     */
    public void putTemplate(String name, String language, String country, String variant, String templateSource) {
    	putTemplate(name, language, country, variant, templateSource, System.currentTimeMillis());
    }

    /**
     * Puts a template into the loader. A call to this method is identical to 
     * the call to the {@link #putTemplate(String, String, String, String, String, long)} 
     * passing <tt>System.currentTimeMillis()</tt> as the third argument.
     * @param name the name of the template.
     * @param language language
     * @param country country
     * @param variant variant
     * @param templateSource the source code of the template.
     * @param lastModified the time of last modification of the template in 
     * terms of <tt>System.currentTimeMillis()</tt>
     */
    public void putTemplate(String name, String language, String country, String variant, String templateSource, long lastModified) {
    	putTemplate(buildTemplateName(name, language, country, variant), templateSource, lastModified);
    }
}
