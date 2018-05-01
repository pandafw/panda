package panda.app.action.tool;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import panda.app.action.AbstractAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.bind.json.Jsons;
import panda.dao.Dao;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.io.FileNames;
import panda.io.FileTypes;
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
import panda.lang.reflect.Types;
import panda.log.Logs;
import panda.mvc.Mvcs;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.Validates;
import panda.mvc.annotation.param.Param;
import panda.mvc.validator.Validators;
import panda.vfs.FileItem;

@At("${super_path}/dataimp")
@Auth(AUTH.SUPER)
@To(View.SFTL)
public class DataImportAction extends AbstractAction {
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
	protected Set<String> targets;
	protected List tableList = new ArrayList();
	
	/**
	 * @return the targetSet
	 */
	public Set<String> getTargets() {
		if (targets == null) {
			targets = new TreeSet<String>();
			for (Class<?> c : getDaoClient().getEntities().keySet()) {
				targets.add(c.getName());
			}
		}
		return targets;
	}

	/**
	 * @return the tableList
	 */
	public List getTableList() {
		return tableList;
	}

	/**
	 * @param arg the input arguments
	 * @return result list
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
			if (FileTypes.XLS.equalsIgnoreCase(fext)) {
				byte[] data = arg.file.getData();
				if (data != null) {
					impXls(data, false);
				}
			}
			else if (FileTypes.XLSX.equalsIgnoreCase(fext)) {
				byte[] data = arg.file.getData();
				if (data != null) {
					impXls(data, true);
				}
			}
			else if (FileTypes.CSV.equalsIgnoreCase(fext)) {
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
			else if (FileTypes.TSV.equalsIgnoreCase(fext) 
					|| FileTypes.TXT.equalsIgnoreCase(fext)) {
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
	 * @param ooxml ooxml or not
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
	
	protected Entity<?> resolveTargetEntity(String target) {
		for (Entry<Class<?>, Entity<?>> en : getDaoClient().getEntities().entrySet()) {
			if (en.getKey().getName().equalsIgnoreCase(target)) {
				return en.getValue();
			}
		}

		for (Entry<Class<?>, Entity<?>> en : getDaoClient().getEntities().entrySet()) {
			if (en.getKey().getSimpleName().equalsIgnoreCase(target)) {
				return en.getValue();
			}
		}

		Class<?> cls = Classes.findClass(target);
		if (cls != null) {
			Entity<?> en = getDaoClient().getEntity(cls);
			if (en != null) {
				return en;
			}
		}

		throw new IllegalArgumentException("Invalid target type: " + target);
	}

	protected Class<?> resolveTargetType(String target) {
		Class<?> cls = Classes.findClass(target);
		if (cls == null) {
			for (Class<?> c : getDaoClient().getEntities().keySet()) {
				if (c.getName().equalsIgnoreCase(target) || c.getSimpleName().equalsIgnoreCase(target) ) {
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
	 * @throws Exception if an error occurred
	 */
	@SuppressWarnings("unchecked")
	protected void impSheet(final ListReader<?> sheet) throws Exception {
		final List table = new ArrayList();
		
		table.add(arg.target);

		final Entity<?> en = resolveTargetEntity(arg.target);

		final List<?> columns = sheet.readList();
		if (columns == null || columns.isEmpty()) {
			throw new Exception("[" + arg.file.getName() + "] - the table column is empty!");
		}
		table.add(columns);

		checkColumns(en.getType(), columns);
		
		final Dao dao = getDaoClient().getDao();
		if (arg.deleteAll) {
			dao.deletes(en);
		}

		dao.exec(new Runnable() {
			@Override
			public void run() {
				int cnt = 0;
				for (int i = 3; ; i++) {
					Map<String, Object> values = getRowValues(en, sheet, i, columns, table);
					if (values == null) {
						break;
					}
					if (values.isEmpty()) {
						continue;
					}

					Object data = castData(values, en.getType());
					
					trimData(data);

					saveData(dao, data);

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
	protected Map<String, Object> getRowValues(Entity<?> entity, ListReader<?> sheet, int r, List<?> columns, List uploadData) {
		try {
			List row = sheet.readList();
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
						Object o = v;
						Object ch = columns.get(c);
						String sh = Strings.defaultString(ch);

						EntityField ef = entity.getField(sh);
						if (Collection.class.isAssignableFrom(Types.getRawType(ef.getType()))) {
							o = Jsons.fromJson(v.toString());
						}
						values.put(sh, o);
					}
					catch (Exception e) {
						String msg = getError(arg.target, r, c + 1, v, e);
						throw new RuntimeException(msg);
					}
					
					// convert to string for view
					if (!(v instanceof CharSequence)) {
						String sv = Mvcs.castString(getContext(), v);
						row.set(c, sv);
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

	protected Object castData(Object values, Class<?> type) {
		return Mvcs.castValue(context, values, type);
	}

	protected void trimData(Object data) {
		assist().initCommonFields(data);
	}

	protected void saveData(Dao dao, Object data) {
		if (arg.deleteAll) {
			dao.insert(data);
		}
		else {
			dao.save(data);
		}
	}
}
