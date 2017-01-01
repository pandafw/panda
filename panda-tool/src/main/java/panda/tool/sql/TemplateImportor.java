package panda.tool.sql;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.cli.CommandLine;

import panda.dao.sql.SqlExecutor;
import panda.dao.sql.SqlManager;
import panda.io.FileNames;
import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.Locales;
import panda.lang.Strings;
import panda.util.tool.AbstractCommandTool;

/**
 * Import freemarker templates to database
 */
public class TemplateImportor extends AbstractSqlTool {
	/**
	 * Main class for ImpTemplates
	 */
	public static class Main extends AbstractSqlTool.Main {
		/**
		 * @param args arguments
		 */
		public static void main(String[] args) {
			Main cgm = new Main();
			
			Object cg = new TemplateImportor();

			cgm.execute(cg, args);
		}

		@Override
		protected void addCommandLineOptions() throws Exception {
			super.addCommandLineOptions();
			
			addCommandLineOption("si", "insert sql", "insert sql template [e.g.: INSERT INTO TEMPLATE VALUES(:language, :country:, :variant, :source) ]", true);

			addCommandLineOption("su", "update sql", "update sql template [e.g.: UPDATE TEMPLATE SET SOURCE=:source WHERE LANGUAGE=:language AND COUNTRY=:country AND VARIANT=:variant) ]");

			addCommandLineOption("ed", "encoding", "encoding of template source file");

			addCommandLineOption("pn", "prefix", "prefix of class name");

			addCommandLineOption("es", "emptystr", "string for emtpy locale field (language, country, variant)");
		}

		@Override
		protected void getCommandLineOptions(CommandLine cl) throws Exception {
			super.getCommandLineOptions(cl);
			
			if (cl.hasOption("si")) {
				setParameter("insertSql", cl.getOptionValue("si"));
			}

			if (cl.hasOption("su")) {
				setParameter("updateSql", cl.getOptionValue("su"));
			}

			if (cl.hasOption("ed")) {
				setParameter("encoding", cl.getOptionValue("ed"));
			}

			if (cl.hasOption("pn")) {
				setParameter("prefix", cl.getOptionValue("pn"));
			}

			if (cl.hasOption("es")) {
				setParameter("emptystr", cl.getOptionValue("es"));
			}
		}
	}
	
	/**
	 * Constructor
	 */
	public TemplateImportor() {
		includes = new String[] { "**/*.ftl" };
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected String updateSql;
	protected String insertSql;
	protected String encoding;
	protected String prefix;
	protected String emptystr;
	
	private int cntUpd;
	private int cntIns;
	
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
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
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
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = Strings.stripToNull(encoding);
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
		
		cntUpd = 0;
		cntIns = 0;

		if (source.isDirectory()) {
			println0("Importing templates: " + source.getPath());
		}
	}

	@Override
	protected void afterProcess() throws Exception {
		super.afterProcess();
		
		String s = cntFile + " files processed";
		if (cntUpd > 0) {
			s += ", " + cntUpd + " templates updated";
		}
		if (cntIns > 0) {
			s += ", " + cntIns + " templates inserted";
		}

		println0(s + " successfully");
	}
	
	private String getName(File f) {
		String name = FileNames.removeLeadingPath(source, f).replace('\\', '/');
		name = name.startsWith("/") ? name.substring(1) : name;
		name = FileNames.removeExtension(name);
		
		String[] sa = name.split("\\_");
		
		if (sa.length > 3) {
			if (sa[sa.length - 3].length() == 2 && sa[sa.length - 2].length() == 2) {
				name = Strings.join(Arrays.subarray(sa, 0, sa.length - 3), '_');
			}
			else if (sa[sa.length - 2].length() == 2 && sa[sa.length - 1].length() == 2) {
				name = Strings.join(Arrays.subarray(sa, 0, sa.length - 2), '_');
			}
			else if (sa[sa.length - 1].length() == 2) {
				name = Strings.join(Arrays.subarray(sa, 0, sa.length - 1), '_');
			}
		}
		else if (sa.length == 3) {
			if (sa[sa.length - 2].length() == 2 && sa[sa.length - 1].length() == 2) {
				name = Strings.join(Arrays.subarray(sa, 0, sa.length - 2), '_');
			}
			else if (sa[sa.length - 1].length() == 2) {
				name = Strings.join(Arrays.subarray(sa, 0, sa.length - 1), '_');
			}
		}
		else if (sa.length == 2) {
			if (sa[sa.length - 1].length() == 2) {
				name = Strings.join(Arrays.subarray(sa, 0, sa.length - 1), '_');
			}
		}
		return name;
	}

	private String getLocaleValue(String val) {
		return Strings.isEmpty(val) ? emptystr : val;
	}
	
	private final static Locale defaultLocale = new Locale("", "", "");
	
	@Override
	protected void processFile(File f) throws Exception {
		println1("Importing template file: " + f.getPath());
		
		FileInputStream fis = null;
		SqlExecutor executor = SqlManager.i().getExecutor(connection); 
		
		try {
			fis = new FileInputStream(f);
			
			byte[] buf = new byte[fis.available()];

			fis.read(buf);
			
			Map<String, String> param = new HashMap<String, String>();
			
			String name = getName(f);
			if (Strings.isNotEmpty(prefix)) {
				name = prefix + name;
			}
			param.put("name", name);
			
			Locale locale = Locales.localeFromFileName(f, defaultLocale);
			if (Strings.isNotEmpty(locale.toString()) 
					&& !Locales.isAvailableLocale(locale)) {
				println0("Warning: " + locale + " is not a valid locale [" + f.getName() + "]");
			}

			param.put("language", getLocaleValue(locale.getLanguage()));
			param.put("country", getLocaleValue(locale.getCountry()));
			param.put("variant", getLocaleValue(locale.getVariant()));

			String source;
			if (Strings.isNotEmpty(encoding)) {
				source = new String(buf, encoding);
			}
			else {
				String c = Charsets.charsetFromLocale(locale);
				if (Strings.isNotEmpty(c)) {
					source = new String(buf, c);
				}
				else {
					source = new String(buf); 
				}
			}
			if (source.length() > 0 && source.charAt(0) == '\ufeff') {
				source = source.substring(1);
			}
			param.put("source", source);
			
			int cu = 0;
			if (Strings.isNotEmpty(updateSql)) {
				cu = executor.update(updateSql, param);
				cntUpd += cu;
			}

			if (cu == 0) {
				cntIns += executor.update(insertSql, param);
			}

			connection.commit();
		}
		catch (Exception e) {
			rollback();
			throw e;
		}
		finally {
			Streams.safeClose(fis);
		}
	}	
}
