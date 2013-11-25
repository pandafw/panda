package panda.tool.sql;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import panda.dao.sql.SqlNamings;
import panda.lang.Arrays;
import panda.lang.Classes;
import panda.lang.Strings;


/**
 * Base class for data export
 */
public abstract class AbstractDataExportor extends AbstractSqlTool {

	protected static class Column {
		private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		
		protected String name;
		protected String property;
		protected String type;
		protected String pattern;
		protected Format format;
		protected Class<?> clazz;
		
		public Column(String name, String type) throws Exception {
			this.name = name;
			this.property = SqlNamings.columnLabel2JavaName(name);
			
			int i = type.indexOf(':');
			if (i >= 0) {
				this.type = type.substring(0, i);
				pattern = type.substring(i + 1);
			}
			else {
				this.type = type;
			}
			init();
		}
		
		private void init() throws Exception {
			int i = type.indexOf('.');
			if (i < 0) {
				type = getType(type);
			}
			
			clazz = Classes.getClass(type);
			if (Number.class.isAssignableFrom(clazz)) {
				if (Strings.isNotEmpty(pattern)) {
					format = new DecimalFormat(pattern);
				}
			}
			else if (Date.class.isAssignableFrom(clazz)) {
				if (Strings.isNotEmpty(pattern)) {
					format = new SimpleDateFormat(pattern);
				}
				else {
					format = dateFormat;
				}
			}

			if (type.startsWith(JAVA_LANG)) {
				type = type.substring(JAVA_LANG.length());
			}
 		}
		
		public String getTypeString() {
			return type + (pattern != null ? ':' + pattern : ""); 
		}

		public String formatValue(Object value) {
			if (value == null) {
				return "";
			}
			return format != null ? format.format(value) : value.toString();
		}
		
		private String getType(String type) {
			if ("char".equalsIgnoreCase(type) 
					|| "character".equalsIgnoreCase(type)) {
				type = Character.class.getName();
			}
			else if ("str".equalsIgnoreCase(type) 
					|| "string".equalsIgnoreCase(type)) {
				type = String.class.getName();
			}
			else if ("number".equalsIgnoreCase(type) 
					|| "numeric".equalsIgnoreCase(type)
					|| "decimal".equalsIgnoreCase(type)) {
				type = BigDecimal.class.getName();
			}
			else if ("byte".equalsIgnoreCase(type)) {
				type = Byte.class.getName();
			}
			else if ("short".equalsIgnoreCase(type)) {
				type = Short.class.getName();
			}
			else if ("int".equalsIgnoreCase(type)
					|| "integer".equalsIgnoreCase(type)) {
				type = Integer.class.getName();
			}
			else if ("long".equalsIgnoreCase(type)) {
				type = Long.class.getName();
			}
			else if ("bigint".equalsIgnoreCase(type)
					|| "BigInteger".equalsIgnoreCase(type)) {
				type = BigInteger.class.getName();
			}
			else if ("bigdec".equalsIgnoreCase(type)
					|| "BigDecimal".equalsIgnoreCase(type)) {
				type = BigDecimal.class.getName();
			}
			else if ("date".equalsIgnoreCase(type)) {
				type = Date.class.getName();
			}
			else if ("file".equalsIgnoreCase(type)) {
				type = byte[].class.getName();
			}
			
			return type;
		}
		
		private static final String JAVA_LANG = "java.lang.";
	}
	
	/**
	 * Constructor
	 */
	public AbstractDataExportor() {
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected int cntRecord;
	protected File currentFile;
	
	@Override
	protected void beforeProcess() throws Exception {
		super.beforeProcess();
		
		cntRecord = 0;

		if (source.isDirectory()) {
			println0("Exporting: " + source.getPath() + " " + Arrays.asList(includes));
		}
	}
	
	@Override
	protected void afterProcess() throws Exception {
		super.afterProcess();
		
		println0(cntRecord + " records of " + cntFile + " files export successfully");
	}

	@Override
	protected void processFile(File f) throws Exception {
		println1("Processing file: " + f.getPath());
		currentFile = f;
	}

	protected String getSelectSql(String tableName, List<String> columns) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		for (int i = 0; i < columns.size(); i++) {
			String c = columns.get(i);
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(c);
		}
		sb.append(" FROM ").append(tableName);
		
		return sb.toString();
	}
}
