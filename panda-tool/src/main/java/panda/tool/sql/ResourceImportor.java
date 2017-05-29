package panda.tool.sql;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import panda.args.Option;
import panda.dao.sql.SqlExecutor;
import panda.dao.sql.SqlManager;
import panda.io.FileNames;
import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.Locales;
import panda.lang.Strings;

/**
 * Import resources to database
 */
public class ResourceImportor extends AbstractSqlTool {
	/**
	 * @param args arguments
	 */
	public static void main(String[] args) {
		new ResourceImportor().execute(args);
	}

	/**
	 * Constructor
	 */
	public ResourceImportor() {
		includes = new String[] { "**/*.properties" };
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected String updateSql;
	protected String insertSql;
	protected String encoding = Charsets.ISO_8859_1;
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
	@Option(opt='U', option="update", arg="SQL", usage="update sql template [e.g.: UPDATE RESOURCE SET SOURCE=:source WHERE LANGUAGE=:language AND COUNTRY=:country AND VARIANT=:variant) ]")
	public void setUpdateSql(String updateSql) {
		this.updateSql = Strings.stripToNull(updateSql);
	}

	/**
	 * @param insertSql the insertSql to set
	 */
	@Option(opt='I', option="insert", arg="SQL", required=true, usage="insert sql template [e.g.: INSERT INTO RESOURCE VALUES(:language, :country:, :variant, :source) ]")
	public void setInsertSql(String insertSql) {
		this.insertSql = Strings.stripToNull(insertSql);
	}

	/**
	 * @param encoding the encoding to set
	 */
	@Option(opt='C', option="charset", arg="CHARSET", usage="The charset of the resource source file")
	public void setEncoding(String encoding) {
		this.encoding = Strings.stripToNull(encoding);
	}
	
	/**
	 * @param prefix the prefix to set
	 */
	@Option(opt='P', option="prefix", arg="PREFIX", usage="prefix of class name")
	public void setPrefix(String prefix) {
		this.prefix = Strings.stripToNull(prefix);
	}

	/**
	 * @param emptystr the emptystr to set
	 */
	@Option(opt='E', option="emptystr", arg="STR", usage="string for emtpy locale field (language, country, variant)")
	public void setEmptystr(String emptystr) {
		this.emptystr = Strings.stripToNull(emptystr);
	}

	@Override
	protected void beforeProcess() throws Exception {
		super.beforeProcess();
		
		cntUpd = 0;
		cntIns = 0;

		if (source.isDirectory()) {
			println0("Importing resources: " + source.getPath());
		}
	}

	@Override
	protected void afterProcess() throws Exception {
		super.afterProcess();
		
		String s = cntFile + " files processed";
		if (cntUpd > 0) {
			s += ", " + cntUpd + " resources updated";
		}
		if (cntIns > 0) {
			s += ", " + cntIns + " resources inserted";
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
		println1("Importing resource file: " + f.getPath());
		
		FileInputStream fis = null;
		SqlExecutor executor = SqlManager.i().getExecutor(connection); 
		
		try {
			fis = new FileInputStream(f);
			
			byte[] buf = new byte[fis.available()];

			fis.read(buf);
			
			Map<String, String> param = new HashMap<String, String>();
			
			String clazz = getClazz(f);
			if (clazz.startsWith(prefix)) {
				clazz = clazz.substring(prefix.length());
			}
			param.put("clazz", clazz);
			
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
