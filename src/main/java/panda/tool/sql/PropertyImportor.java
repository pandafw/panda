package panda.tool.sql;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;

import panda.dao.sql.SqlExecutor;
import panda.dao.sql.SqlManager;
import panda.io.FileNames;
import panda.io.Streams;
import panda.lang.Locales;
import panda.lang.Strings;
import panda.util.tool.AbstractCommandTool;

/**
 * Import java properties to database
 */
public class PropertyImportor extends AbstractSqlTool {
	/**
	 * Main class for ImpProperties
	 */
	public static class Main extends AbstractSqlTool.Main {
		/**
		 * @param args arguments
		 */
		public static void main(String[] args) {
			Main cgm = new Main();
			
			Object cg = new PropertyImportor();

			cgm.execute(cg, args);
		}

		@Override
		protected void addCommandLineOptions() throws Exception {
			super.addCommandLineOptions();
			
			addCommandLineOption("si", "insert sql", "insert sql template [e.g.: INSERT INTO RESOURCE VALUES(:clazz, :language, :country, :variant, :name, :value)", true);
			addCommandLineOption("su", "update sql", "update sql template [e.g.: UPDATE RESOURCE SET VALUE=:value WHERE CLAZZ=:class AND LANGUAGE=:language AND COUNTRY=:country AND NAME=:name) ]");
			addCommandLineOption("sd", "delete sql", "delete sql template [e.g.: DELETE FROM RESOURCE WHERE CLAZZ=:clazz AND LANGUAGE=:language AND COUNTRY=:country AND VARIANT=:variant)");
			addCommandLineOption("pn", "prefix", "prefix of class name");
			addCommandLineOption("es", "emptystr", "string for emtpy locale field (language, country, variant)");
		}

		@Override
		protected void getCommandLineOptions(CommandLine cl) throws Exception {
			super.getCommandLineOptions(cl);
			
			if (cl.hasOption("sd")) {
				setParameter("deleteSql", cl.getOptionValue("sd"));
			}

			if (cl.hasOption("si")) {
				setParameter("insertSql", cl.getOptionValue("si"));
			}

			if (cl.hasOption("su")) {
				setParameter("updateSql", cl.getOptionValue("su"));
			}

			if (cl.hasOption("pn")) {
				setParameter("prefix", cl.getOptionValue("pn"));
			}

			if (cl.hasOption("es")) {
				setParameter("emtpystr", cl.getOptionValue("es"));
			}
		}
	}

	/**
	 * Constructor
	 */
	public PropertyImportor() {
		includes = new String[] { "**/*.properties" };
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected String deleteSql;
	protected String updateSql;
	protected String insertSql;
	protected String prefix;
	protected String emptystr;
	
	private int cntDel;
	private int cntUpd;
	private int cntIns;
	
	/**
	 * @return the deleteSql
	 */
	public String getDeleteSql() {
		return deleteSql;
	}

	/**
	 * @return the updateSql
	 */
	public String getUpdateSql() {
		return updateSql;
	}

	/**
	 * @return the insertSql
	 */
	public String getInsertSql() {
		return insertSql;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @return the emptystr
	 */
	public String getEmptystr() {
		return emptystr;
	}

	/**
	 * @param deleteSql the deleteSql to set
	 */
	public void setDeleteSql(String deleteSql) {
		this.deleteSql = Strings.stripToNull(deleteSql);
	}

	/**
	 * @param updateSql the updateSql to set
	 */
	public void setUpdateSql(String updateSql) {
		this.updateSql = Strings.stripToNull(updateSql);
	}

	/**
	 * @param insertSql the insertSql to set
	 */
	public void setInsertSql(String insertSql) {
		this.insertSql = Strings.stripToNull(insertSql);
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = Strings.stripToNull(prefix);
	}
	
	/**
	 * @param emptystr the emptystr to set
	 */
	public void setEmptystr(String emptystr) {
		this.emptystr = Strings.stripToNull(emptystr);
	}

	@Override
	protected void checkParameters() throws Exception {
		super.checkParameters();
		
		AbstractCommandTool.checkRequired(insertSql, "insertSql");
	}

	@Override
	protected void beforeProcess() throws Exception {
		super.beforeProcess();

		cntDel = 0;
		cntUpd = 0;
		cntIns = 0;

		if (source.isDirectory()) {
			println0("Importing properties: " + source.getPath());
		}
	}

	@Override
	protected void afterProcess() throws Exception {
		super.afterProcess();
		
		String s = cntFile + " files processed";
		if (cntDel > 0) {
			s += ", " + cntDel + " properties deleted";
		}
		if (cntUpd > 0) {
			s += ", " + cntUpd + " properties updated";
		}
		if (cntIns > 0) {
			s += ", " + cntIns + " properties inserted";
		}
		println0(s + " successfully");
	}

	private String getClazz(File f) {
		String c = FileNames.removeLeadingPath(source, f);
		c = FileNames.removeExtension(c);

		c = c.replace('/', '.').replace('\\', '.');
		if (c.charAt(0) == '.') {
			c = c.substring(1);
		}

		int ub = c.indexOf('_');
		if (ub > 0) {
			c = c.substring(0, ub);
		}

		return c;
	}
	
	private String getLocaleValue(String val) {
		return Strings.isEmpty(val) ? emptystr : val;
	}
	
	private final static Locale defaultLocale = new Locale("", "", "");
	
	@Override
	protected void processFile(File f) throws Exception {
		println1("Importing property file: " + f.getPath());

		Locale locale = Locales.localeFromFileName(f, defaultLocale);
		if (Strings.isNotEmpty(locale.toString()) 
				&& !Locales.isAvailableLocale(locale)) {
			println0("Warning: " + locale + " is not a valid locale [" + f.getName() + "]");
		}
		
		String clazz = getClazz(f);
		if (clazz.startsWith(prefix)) {
			clazz = clazz.substring(prefix.length());
		}
		String language = getLocaleValue(locale.getLanguage());
		String country = getLocaleValue(locale.getCountry());
		String variant = getLocaleValue(locale.getVariant());

		Map<String, String> param = new HashMap<String, String>();
		param.put("clazz", clazz);
		param.put("language", language);
		param.put("country", country);
		param.put("variant", variant);

		FileInputStream fis = null;
		Properties p = new Properties();
		try {
			fis = new FileInputStream(f);
			p.load(fis);
		}
		catch (Exception e) {
			throw new Exception("Failed to load " + f.getPath(), e);
		}
		finally {
			Streams.safeClose(fis);
		}

		SqlExecutor executor = SqlManager.me().getExecutor(connection); 
		try {
			if (Strings.isNotEmpty(deleteSql)) {
				cntDel = executor.update(deleteSql, param);
			}

			for (Iterator<Entry<Object, Object>> it = p.entrySet().iterator(); it.hasNext(); ) {
				Entry<Object, Object> en = it.next();
				String k = en.getKey().toString();
				String v = en.getValue().toString();
				
				param.put("name", k);
				param.put("value", v);
				
				int cu = 0;
				if (Strings.isNotEmpty(updateSql)) {
					cu = executor.update(updateSql, param);
					cntUpd += cu;
				}

				if (cu == 0) {
					cntIns += executor.update(insertSql, param);
				}
			}

			connection.commit();
		}
		catch (Exception e) {
			rollback();
			throw e;
		}
	}	
}
