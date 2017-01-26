package panda.wing.action.tool;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.bind.json.Jsons;
import panda.dao.Dao;
import panda.io.FileNames;
import panda.io.FileType;
import panda.io.Streams;
import panda.io.stream.BOMInputStream;
import panda.io.stream.CsvReader;
import panda.io.stream.ListReader;
import panda.io.stream.XlsReader;
import panda.io.stream.XlsxReader;
import panda.lang.Chars;
import panda.lang.Charsets;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Logs;
import panda.mvc.Mvcs;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.validation.Validators;
import panda.mvc.validation.annotation.Validates;
import panda.vfs.FileItem;
import panda.wing.action.AbstractAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;

@At("${super_context}/dataimp")
@Auth(AUTH.SUPER)
@To(View.SFTL)
public class DataImportAction extends AbstractAction {
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
	
	public static class Arg {
		public boolean deleteAll = false;
		public String target;
		private String encoding;
		public FileItem file;
		public int commitSize = 1000;
		public int count;
		public String getEncoding() {
			return encoding;
		}
		public void setEncoding(String encoding) {
			this.encoding = Strings.stripToNull(encoding);
		}
	}

	protected Arg arg;
	protected Set<String> targetSet;
	protected List tableList = new ArrayList();
	
	/**
	 * @return the targetSet
	 */
	public Set<String> getTargetSet() {
		if (targetSet == null) {
			targetSet = new TreeSet<String>();
			for (Class<?> c : getDaoClient().getEntities().keySet()) {
				targetSet.add(c.getName());
			}
		}
		return targetSet;
	}

	/**
	 * @return the tableList
	 */
	public List getTableList() {
		return tableList;
	}

	/**
	 * @return INPUT
	 * @throws Exception if an error occurs
	 */
	@At("")
	public Object execute(@Param @Validates Arg arg) throws Exception {
		this.arg = arg;
		if (arg.file == null) {
			return null;
		}
		
		try {
			String fext = FileNames.getExtension(arg.file.getName());
			if (FileType.XLS.equalsIgnoreCase(fext)) {
				byte[] data = arg.file.getData();
				if (data != null) {
					impXls(data, false);
				}
			}
			else if (FileType.XLSX.equalsIgnoreCase(fext)) {
				byte[] data = arg.file.getData();
				if (data != null) {
					impXls(data, true);
				}
			}
			else if (FileType.CSV.equalsIgnoreCase(fext)) {
				if (Strings.isEmpty(arg.target)) {
					addFieldError("target", getText(Validators.MSGID_REQUIRED));
				}
				else {
					byte[] data = arg.file.getData();
					if (data != null) {
						impCsv(data, Chars.COMMA);
					}
				}
			}
			else if (FileType.TSV.equalsIgnoreCase(fext) 
					|| FileType.TXT.equalsIgnoreCase(fext)) {
				if (Strings.isEmpty(arg.target)) {
					addFieldError("target", getText(Validators.MSGID_REQUIRED));
				}
				else {
					byte[] data = arg.file.getData();
					if (data != null) {
						impCsv(data, Chars.TAB);
					}
				}
			}
			else {
				addFieldError("file", getText("error-file-format"));
			}
		}
		finally {
			if (arg.file != null) {
				arg.file.delete();
			}
		}
		return tableList;
	}

	protected Object getCellValue(Object v, int c, List<DataType> types) throws Exception {
		if (v == null) {
			return v;
		}

		Object cv = null;
		String type = types.get(c).type;
		if ("list".equalsIgnoreCase(type)) {
			cv = Jsons.fromJson(v.toString(), ArrayList.class);
		}
		else if ("set".equalsIgnoreCase(type)) {
			cv = Jsons.fromJson(v.toString(), LinkedHashSet.class);
		}
		else if ("map".equalsIgnoreCase(type)) {
			cv = Jsons.fromJson(v.toString(), LinkedHashMap.class);
		}
		else {
			cv = v;
		}
		return cv;
	}

	protected void logException(String method, Throwable e) {
		Logs.getLog(getClass()).warn(method, e);
		if (assist().isDebugEnabled()) {
			String s = Exceptions.getStackTrace(e);
			addActionError(e.getMessage() + "\n" + s);
		}
		else {
			addActionError(e.getMessage());
		}
	}
	
	/**
	 * CsvImport
	 * @param data data
	 * @param separator separator
	 */
	protected void impCsv(byte[] data, char separator) {
		try {
			final CsvReader csv = getCsvReader(data, separator);
			impSheet(csv);
		}
		catch (Throwable e) {
			logException("CsvImport", e);
		}
	}

	/**
	 * XlsImport
	 * @param data data
	 */
	protected void impXls(byte[] data, boolean ooxml) {
		try {
			InputStream is = new ByteArrayInputStream(data);
			XlsReader xls = ooxml ? new XlsxReader(is) : new XlsReader(is);
			for (int i = 0; i < xls.getSheetCount(); i++) {
				xls.setSheet(i);
				arg.target = xls.getSheetName();
				impSheet(xls);
			}
		}
		catch (Throwable e) {
			logException("XlsImport", e);
		}
	}

	protected List<String> getHeadValues(Sheet sheet, int r) throws Exception {
		List<String> values = new ArrayList<String>();

		Row row = sheet.getRow(r);
		if (row != null) {
			for (int c = 0; ; c++) {
				Cell cell = row.getCell(c);
				if (cell == null) {
					break;
				}
				String v = null;
				try {
					v = cell.getStringCellValue();
					if (Strings.isBlank(v)) {
						break;
					}
					values.add(v);
				}
				catch (Exception e) {
					throw new Exception("[" + sheet.getSheetName() + "] - head value is incorrect: (" + r + "," + c + ") - " + v, e);
				}
			}
		}
		
		return values;
	}
	
	protected Class<?> resolveTargetType(String target) {
		Class<?> cls = Classes.findClass(target);
		if (cls == null) {
			for (Class<?> c : getDaoClient().getEntities().keySet()) {
				if (c.getSimpleName().equalsIgnoreCase(target)) {
					return c;
				}
			}
		}
		
		if (cls == null) {
			throw new IllegalArgumentException("Invalid target type: " + target);
		}
		return cls;
	}
	
	protected void checkColumns(Class<?> type, List<?> columns) {
		BeanHandler bh = Beans.i().getBeanHandler(type);
		for (Object o : columns) {
			String c = o == null ? "" : o.toString();
			if (!bh.canWriteProperty(c)) {
				throw new RuntimeException("[" + arg.file.getName() + "!" + arg.target + "] - the table column(" + c + ") is incorrect!");
			}
		}
	}

	/**
	 * CsvImport
	 * @param sheet current sheet of ListReader
	 */
	@SuppressWarnings("unchecked")
	protected void impSheet(final ListReader<?> sheet) throws Exception {
		final List table = new ArrayList();
		
		table.add(arg.target);
		final Class<?> targetType = resolveTargetType(arg.target);

		final List<?> columns = sheet.readList();
		if (columns == null || columns.isEmpty()) {
			throw new Exception("[" + arg.file.getName() + "] - the table column is empty!");
		}
		table.add(columns);

		checkColumns(targetType, columns);
		
		List<?> row2 = sheet.readList();
		if (row2 == null || row2.size() != columns.size()) {
			throw new Exception("[" + arg.file.getName() + "] - the column types is incorrect!");
		}
		table.add(row2);
		
		final List<DataType> types = new ArrayList<DataType>();
		for (Object v : row2) {
			String c = v == null ? "" : v.toString();
			types.add(new DataType(c));
		}

		final Dao dao = getDaoClient().getDao();
		if (arg.deleteAll) {
			dao.deletes(targetType);
		}

		dao.exec(new Runnable() {
			@Override
			public void run() {
				int cnt = 0;
				for (int i = 3; ; i++) {
					Map<String, Object> values = getRowValues(sheet, i, columns, types, table);
					if (values == null) {
						break;
					}
					if (values.isEmpty()) {
						continue;
					}

					saveRow(dao, targetType, values);

					cnt++;
					if (cnt % arg.commitSize == 0) {
						dao.commit();
					}
				}

				if (cnt > 0 && cnt % arg.commitSize != 0) {
					dao.commit();
				}

				arg.count = cnt;
			}
		});

		addActionMessage(getText("success-import", "success-import", arg));
		tableList.add(table);
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Object> getRowValues(ListReader<?> sheet, int r,
			List<?> columns, List<DataType> types, List uploadData) {
		try {
			List<?> row = sheet.readList();
			if (row == null) {
				return null;
			}
	
			Map<String, Object> values = new HashMap<String, Object>(columns.size());
			for (int c = 0; c < columns.size() && c < row.size(); c++) {
				Object v = row.get(c);

				if (v instanceof CharSequence) {
					v = Strings.stripToNull((CharSequence)v);
				}
				if (v != null) {
					try {
						Object cv = getCellValue(v, c, types);
						Object ch = columns.get(c);
						String sh = ch == null ? null : ch.toString();
						values.put(sh, cv);
					}
					catch (Exception e) {
						String msg = getError(arg.target, r, c + 1, v, e);
						throw new RuntimeException(msg);
					}
				}
			}
			
			if (!values.isEmpty()) {
				uploadData.add(row);
			}
			return values;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	@SuppressWarnings("unchecked")
	protected String getError(String sheet, int row, int col, Object val, Throwable e) {
		Map m = new HashMap();
		m.put("target", sheet);
		m.put("row", row);
		m.put("col", col);
		m.put("value", val);
		m.put("error", e.getMessage());

		return getText("error-value", e.getMessage(), m);
	}

	protected CsvReader getCsvReader(byte[] data, char separator) throws Exception {
		BOMInputStream bis = Streams.toBOMInputStream(new ByteArrayInputStream(data));
		Charset cs = bis.getBOMCharset();
		if (cs == null) {
			cs = Charsets.toCharset(arg.encoding, Charsets.CS_UTF_8);
		}
		return new CsvReader(new InputStreamReader(bis, cs), separator);
	}

	protected void trimData(Object data) {
		assist().initCommonFields(data);
	}

	protected void saveRow(Dao dao, Class targetType, Object row) {
		Object data = Mvcs.castValue(context, row, targetType);
		trimData(data);
		if (arg.deleteAll) {
			dao.insert(data);
		}
		else {
			dao.save(data);
		}
	}
}
