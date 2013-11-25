package panda.tool.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.dao.sql.SqlExecutor;
import panda.dao.sql.SqlManager;
import panda.dao.sql.SqlResultSet;
import panda.io.Streams;
import panda.io.stream.CsvReader;
import panda.io.stream.CsvWriter;
import panda.lang.Chars;
import panda.lang.Charsets;
import panda.lang.Strings;
import panda.tool.DynaBean;

/**
 * Export data from database to csv 
 */
public class CsvDataExportor extends AbstractDataExportor {
	/**
	 * Main class
	 */
	public static class Main extends AbstractDataExportor.Main {
		/**
		 * @param args arguments
		 */
		public static void main(String[] args) {
			Main cgm = new Main();
			
			Object cg = new CsvDataExportor();

			cgm.execute(cg, args);
		}

		@Override
		protected void addCommandLineOptions() throws Exception {
			super.addCommandLineOptions();
			
			addCommandLineOption("c", "charset", "charset");
		}

		@Override
		protected void getCommandLineOptions(CommandLine cl) throws Exception {
			super.getCommandLineOptions(cl);
			
			if (cl.hasOption("c")) {
				setParameter("charset", cl.getOptionValue("c").trim());
			}
		}
	}
	
	/**
	 * Constructor
	 */
	public CsvDataExportor() {
		includes = new String[] { "**/*.csv" };
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected String charset;
	private String tableName;
	private String selectSql;
	private List<Column> columns;
	
	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @param charset the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	@Override
	protected void processFile(File f) throws Exception {
		super.processFile(f);

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);

			Reader in;
			if (Strings.isEmpty(charset)) {
				in = new InputStreamReader(fis);
			}
			else {
				in = new InputStreamReader(fis, charset);
				if (Charsets.isUnicodeCharset(charset)) {
					in = new PushbackReader(in, 1);
					int bom = in.read();
					if (Chars.BOM != bom) {
						((PushbackReader)in).unread(bom);
					}
				}
			}

			CsvReader csv = new CsvReader(in);
			readExportInfo(csv);
		}
		finally {
			Streams.safeClose(fis);
		}
	
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);

			OutputStreamWriter osw;
			if (Strings.isEmpty(charset)) {
				osw = new OutputStreamWriter(fos);
			}
			else {
				osw = new OutputStreamWriter(fos, charset);
			}

			CsvWriter csv = new CsvWriter(osw);
			if (Charsets.isUnicodeCharset(charset)) {
				osw.write(Chars.BOM);
			}
			exportHead(csv);
			exportData(csv);
		}
		finally {
			Streams.safeClose(fos);
		}
	}

	private void readExportInfo(CsvReader csv) throws Exception {
		List<String> tns = csv.readNext();
		if (tns == null || tns.isEmpty()) {
			throw new Exception("[" + currentFile.getName() + "] - the table name is empty!");
		}
		
		tableName = tns.get(0);
		selectSql = tns.size() > 1 ? tns.get(1) : null;
		
		List<String> row1 = csv.readNext();
		if (row1 == null || row1.isEmpty()) {
			throw new Exception("[" + tableName + "] - the table column is empty!");
		}
		
		List<String> row2 = csv.readNext();
		if (row2 == null || row2.size() != row1.size()) {
			throw new Exception("[" + tableName + "] - the column types is incorrect!");
		}
		
		columns = new ArrayList<Column>();
		for (int i = 0; i < row2.size(); i++) {
			columns.add(new Column(row1.get(i), row2.get(i)));
		}

		if (Strings.isEmpty(selectSql)) {
			selectSql = getSelectSql(tableName, row1);
		}
	}
	
	private Class<?> createDataClass() throws Exception {
		Map<String, String> properties = new HashMap<String, String>();
		for (int i = 0; i < columns.size(); i++) {
			Column c = columns.get(i);
			properties.put(c.name, c.type);
		}
		
		String name = this.getClass().getName().toLowerCase() + "beans." + tableName;
		Class<?> cls = DynaBean.createBeanClass(name, properties);
		return cls;
	}

	private void exportHead(CsvWriter csv) throws Exception {
		csv.writeNext(new String[] { tableName, selectSql });
		
		String[] ns = new String[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			ns[i] = columns.get(i).name;
		}
		csv.writeNext(ns);

		String[] ts = new String[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			ts[i] = columns.get(i).getTypeString();
		}
		csv.writeNext(ts);
		
		csv.flush();
	}
	
	private void exportData(CsvWriter csv) throws Exception {
		print2("Exporting data: " + selectSql + " ");
		int cnt = 0;
		try {
			Class<?> recClass = createDataClass();
			BeanHandler beanh = Beans.me().getBeanHandler(recClass);

			SqlExecutor executor = SqlManager.me().getExecutor(connection); 
			SqlResultSet<?> srs = executor.selectResultSet(selectSql, recClass);
			while (srs.next()) {
				Object data = srs.getResult();
				List<String> row = getRowValues(data, beanh);
				csv.writeNext(row);
				cnt++;
				cntRecord++;
				if (cnt % 1000 == 0) {
					print3(".");
				}
			}
			srs.close();
			csv.flush();
		}
		catch (Exception e) {
			rollback();
			throw new Exception("Failed to export data [" + selectSql + "]", e);
		}
		finally {
			println2(" " + cnt);
		}
	}	

	@SuppressWarnings("unchecked")
	private List<String> getRowValues(Object data, BeanHandler beanh) throws Exception {
		List<String> values = new ArrayList<String>();
		for (Column c : columns) {
			Object value = beanh.getBeanValue(data, c.property);
			values.add(c.formatValue(value));
		}
		return values;
	}
}
