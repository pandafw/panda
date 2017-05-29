package panda.tool.sql;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import panda.args.Option;
import panda.bind.json.Jsons;
import panda.io.Streams;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.lang.time.FastDateFormat;

/**
 * Base class for table data import
 */
public abstract class AbstractDataImportor extends AbstractSqlTool {
	protected static class DataType {
		String type;
		String format;
		
		public DataType(String type) {
			int i = type.indexOf(':');
			if (i >= 0) {
				this.type = type.substring(0, i);
				this.format = type.substring(i + 1);
			}
			else {
				this.type = type;
			}
		}
	}
	
	/**
	 * Constructor
	 */
	public AbstractDataImportor() {
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected boolean truncate = false;
	protected Integer commit = 1000;
	
	protected int cntRecord;
	protected File currentFile;
	

	/**
	 * @param truncate the truncate to set
	 */
	@Option(opt='T', option="truncate", usage="Truncate table before import data")
	public void setTruncate(boolean truncate) {
		this.truncate = truncate;
	}

	/**
	 * @param commit the commit to set
	 */
	@Option(opt='L', option="limit", arg="NUM", usage="Commit limit count (default is 1000)")
	public void setCommit(Integer commit) {
		this.commit = commit;
	}

	@Override
	protected void beforeProcess() throws Exception {
		super.beforeProcess();
		
		cntRecord = 0;

		if (source.isDirectory()) {
			println0("Importing: " + source.getPath());
		}
	}
	
	@Override
	protected void afterProcess() throws Exception {
		super.afterProcess();
		
		println0(cntRecord + " records of " + cntFile + " files import successfully");
	}

	@Override
	protected void processFile(File f) throws Exception {
		println1("Processing file: " + f.getPath());

		currentFile = f;
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			importFile(fis);
		}
		finally {
			Streams.safeClose(fis);
		}
	}

	protected void importFile(FileInputStream fis) throws Exception {
	}
	
	private static FastDateFormat timestampFormat = DateTimes.timestampFormat();
	private static FastDateFormat datetimeFormat = DateTimes.isoDatetimeNotFormat();
	private static FastDateFormat dateFormat = DateTimes.isoDateFormat();
	
	protected Date parseDate(String str, String format) throws Exception {
		if ("now".equalsIgnoreCase(str)) {
			return Calendar.getInstance().getTime();
		}
		if (Strings.isNotBlank(format)) {
			SimpleDateFormat df = new SimpleDateFormat(format);
			return df.parse(str);
		}
		try {
			return timestampFormat.parse(str);
		}
		catch (ParseException e) {
			try {
				return datetimeFormat.parse(str);
			}
			catch (ParseException e2) {
				return dateFormat.parse(str);
			}
		}
	}

	protected Object getCellValue(String v, int c, List<DataType> types) throws Exception {
		Object cv = null;
		if (Strings.isNotBlank(v)) {
			String type = types.get(c).type;
			String format = types.get(c).format;
			if ("boolean".equalsIgnoreCase(type)) {
				if ("true".equalsIgnoreCase(v) || "1".equals(v) || "yes".equalsIgnoreCase(v)) {
					cv = true;
				}
				else if ("false".equalsIgnoreCase(v) || "0".equals(v) || "no".equalsIgnoreCase(v)) {
					cv = false;
				}
			}
			else if ("char".equalsIgnoreCase(type) 
					|| "character".equalsIgnoreCase(type)) {
				cv = v.length() > 0 ? v.charAt(0) : '\0';
			}
			else if ("number".equalsIgnoreCase(type) 
					|| "numeric".equalsIgnoreCase(type)
					|| "decimal".equalsIgnoreCase(type)) {
				cv = NumberFormat.getInstance().parse(v);
			}
			else if ("byte".equalsIgnoreCase(type)) {
				cv = NumberFormat.getInstance().parse(v).byteValue();
			}
			else if ("short".equalsIgnoreCase(type)) {
				cv = NumberFormat.getInstance().parse(v).shortValue();
			}
			else if ("int".equalsIgnoreCase(type)
					|| "integer".equalsIgnoreCase(type)) {
				cv = NumberFormat.getInstance().parse(v).intValue();
			}
			else if ("long".equalsIgnoreCase(type)) {
				cv = NumberFormat.getInstance().parse(v).longValue();
			}
			else if ("float".equalsIgnoreCase(type)) {
				cv = NumberFormat.getInstance().parse(v).floatValue();
			}
			else if ("double".equalsIgnoreCase(type)) {
				cv = NumberFormat.getInstance().parse(v).doubleValue();
			}
			else if ("bigint".equalsIgnoreCase(type)
					|| "BigInteger".equalsIgnoreCase(type)) {
				cv = new BigInteger(v);
			}
			else if ("bigdec".equalsIgnoreCase(type)
					|| "BigDecimal".equalsIgnoreCase(type)) {
				cv = new BigDecimal(v);
			}
			else if ("date".equalsIgnoreCase(type)) {
				cv = parseDate(v, format);
			}
			else if ("list".equalsIgnoreCase(type)) {
				cv = Jsons.fromJson(v, ArrayList.class);
			}
			else if ("set".equalsIgnoreCase(type)) {
				cv = Jsons.fromJson(v, LinkedHashSet.class);
			}
			else if ("map".equalsIgnoreCase(type)) {
				cv = Jsons.fromJson(v, LinkedHashMap.class);
			}
			else if ("file".equalsIgnoreCase(type)) {
				File file;
				if (v.startsWith(".")) {
					file = new File(currentFile, v);
				}
				else {
					file = new File(v);
				}
				cv = Streams.toByteArray(file);
			}
			else {
				cv = v;
			}
		}
		return cv;
	}
	
	protected String getInsertSql(String tableName, List<String> columns, List<DataType> types) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ")
			.append(tableName)
			.append("(");
		for (int i = 0; i < columns.size(); i++) {
			String c = columns.get(i);
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(c);
		}
		sb.append(") VALUES(");

		for (int i = 0; i < columns.size(); i++) {
			String c = columns.get(i);
			String t = types.get(i).type;
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(':').append(c).append(':').append(t);
		}
		sb.append(")");
		
		return sb.toString();
	}
}
