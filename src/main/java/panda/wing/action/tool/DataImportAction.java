package panda.wing.action.tool;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.bind.json.Jsons;
import panda.cast.Castors;
import panda.dao.Dao;
import panda.filepool.FileItem;
import panda.io.FileNames;
import panda.io.stream.CsvReader;
import panda.lang.Charsets;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Logs;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Ok;
import panda.mvc.validation.Validators;
import panda.mvc.validation.annotation.Validates;
import panda.wing.action.AbstractAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;
import panda.wing.util.AppActionAssist;

@At("${super_context}/dataimp")
@Auth(AUTH.SUPER)
@Ok(View.SFTL)
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
		public String encoding = Charsets.UTF_8;
		public FileItem file;
		public int commitSize = 1000;
		public int count;
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
			if ("xls".equalsIgnoreCase(fext)) {
				byte[] data = arg.file.getData();
				if (data != null) {
					impXls(data, false);
				}
			}
			else if ("xlsx".equalsIgnoreCase(fext)) {
				byte[] data = arg.file.getData();
				if (data != null) {
					impXls(data, true);
				}
			}
			else if ("csv".equalsIgnoreCase(fext)) {
				if (Strings.isEmpty(arg.target)) {
					addFieldError("target", getText(Validators.MSGID_REQUIRED));
				}
				else {
					byte[] data = arg.file.getData();
					if (data != null) {
						impCsv(data, CsvReader.COMMA_SEPARATOR);
					}
				}
			}
			else if ("tsv".equalsIgnoreCase(fext) 
					|| "txt".equalsIgnoreCase(fext)) {
				if (Strings.isEmpty(arg.target)) {
					addFieldError("target", getText(Validators.MSGID_REQUIRED));
				}
				else {
					byte[] data = arg.file.getData();
					if (data != null) {
						impCsv(data, CsvReader.TAB_SEPARATOR);
					}
				}
			}
			else if (Strings.isNotEmpty(arg.file.getName())) {
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

	protected Date parseDate(String str, String format) throws Exception {
		if ("now".equalsIgnoreCase(str)) {
			return DateTimes.getDate();
		}
		
		if (Strings.isNotBlank(format)) {
			return DateTimes.parse(str, format);
		}
		try {
			return DateTimes.timestampFormat().parse(str);
		}
		catch (ParseException e) {
			try {
				return DateTimes.datetimeFormat().parse(str);
			}
			catch (ParseException e2) {
				return DateTimes.timeFormat().parse(str);
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
			else {
				cv = v;
			}
		}
		return cv;
	}

	protected void logException(String method, Throwable e) {
		Logs.getLog(getClass()).warn(method, e);
		if (getAssist().isDebugEnabled()) {
			String s = Exceptions.getStackTrace(e);
			getActionAware().addError(e.getMessage() + "\n" + s);
		}
		else {
			getActionAware().addError(e.getMessage());
		}
	}

	/**
	 * XlsImport
	 * @param data data
	 */
	protected void impXls(byte[] data, boolean ooxml) {
		try {
			InputStream is = new ByteArrayInputStream(data);
			Workbook wb = null;
			if (ooxml) {
				wb = (Workbook)Classes.newInstance(
						"org.apache.poi.xssf.usermodel.XSSFWorkbook", is, InputStream.class);
			}
			else {
				wb = new HSSFWorkbook(is);
			}
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				Sheet sheet = wb.getSheetAt(i);
				impXlsSheetData(sheet);
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
	
	@SuppressWarnings("unchecked")
	protected Map<String, Object> getRowValues(Sheet sheet, int r,
			List<String> columns, List<DataType> types, List uploadData) {
		Row row = sheet.getRow(r);
		if (row == null) {
			return null;
		}

		String[] line = new String[columns.size()];
		
		boolean empty = true;
		Map<String, Object> values = new HashMap<String, Object>(columns.size());
		for (int c = 0; c < columns.size(); c++) {
			Cell cell = row.getCell(c);
			if (cell == null) {
				continue;
			}

			String v = null;
			try {
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_NUMERIC:
					v = String.valueOf(cell.getNumericCellValue());
					if (Strings.contains(v, '.')) {
						v = Strings.stripEnd(v, "0");
						v = Strings.stripEnd(v, ".");
					}
					break;
				case Cell.CELL_TYPE_FORMULA:
					try {
						v = String.valueOf(cell.getNumericCellValue());
					}
					catch (Exception e) {
						v = cell.getStringCellValue();
					}
					break;
				default: 
					v = cell.getStringCellValue();
					break;
				}
				line[c] = v;

				Object cv;
				if (Strings.isBlank(v)) {
					cv = null;
				}
				else {
					empty = false;

					String type = types.get(c).type;
					if ("date".equalsIgnoreCase(type)) {
						try {
							cv = cell.getDateCellValue();
						}
						catch (Exception e) {
							cv = getCellValue(v, c, types);
						}
					}
					else {
						cv = getCellValue(v, c, types);
					}
				}
				values.put(columns.get(c), cv);
			}
			catch (Exception e) {
				String msg = getError(sheet.getSheetName(), r + 1, c + 1, v, e);
				throw new RuntimeException(msg);
			}
		}

		if (empty) {
			return null;
		}
		else {
			uploadData.add(line);
			return values;
		}
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
	
	protected void checkColumns(Class<?> type, List<String> columns) {
		BeanHandler bh = Beans.i().getBeanHandler(type);
		for (String c : columns) {
			if (!bh.canWriteProperty(c)) {
				throw new RuntimeException("[" + arg.file.getName() + "!" + arg.target + "] - the table column(" + c + ") is incorrect!");
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void impXlsSheetData(final Sheet sheet) throws Exception {
		final List table = new ArrayList();
		
		arg.target = sheet.getSheetName();
		final Class<?> targetType = resolveTargetType(arg.target);

		table.add(arg.target);

		final List<String> columns = getHeadValues(sheet, 0);
		if (columns.isEmpty()) {
			throw new Exception("[" + arg.file.getName() + "!" + arg.target + "] - the table column is empty!");
		}
		table.add(columns);

		checkColumns(targetType, columns);
		
		List<String> row2 = getHeadValues(sheet, 1);
		if (row2.size() != columns.size()) {
			throw new Exception("[" + arg.file.getName() + "!" + arg.target + "] - the column types is incorrect!");
		}
		table.add(row2);
		
		final List<DataType> types = new ArrayList<DataType>();
		for (String v : row2) {
			types.add(new DataType(v));
		}
		
		final Dao dao = getDaoClient().getDao();
		if (arg.deleteAll) {
			dao.deletes(targetType);
		}

		dao.exec(new Runnable() {
			@Override
			public void run() {
				int cnt = 0;
				for (int i = 2; ; i++) {
					Map<String, Object> values = getRowValues(sheet, i, columns, types, table);
					if (values == null) {
						break;
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
				addActionMessage(getText("success-import", "success-import", arg));
			}
		});

		tableList.add(table);
	}
	
	/**
	 * CsvImport
	 * @param data data
	 * @param separator separator
	 */
	@SuppressWarnings("unchecked")
	protected void impCsv(byte[] data, char separator) {
		try {
			final List table = new ArrayList();
			
			table.add(arg.target);
			final Class<?> targetType = resolveTargetType(arg.target);

			final CsvReader csv = getCsvReader(data, separator);

			final List<String> columns = csv.readNext();
			if (columns == null || columns.isEmpty()) {
				throw new Exception("[" + arg.file.getName() + "] - the table column is empty!");
			}
			table.add(columns);

			checkColumns(targetType, columns);
			
			List<String> row2 = csv.readNext();
			if (row2 == null || row2.size() != columns.size()) {
				throw new Exception("[" + arg.file.getName() + "] - the column types is incorrect!");
			}
			table.add(row2);
			
			final List<DataType> types = new ArrayList<DataType>();
			for (String v : row2) {
				types.add(new DataType(v));
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
						Map<String, Object> values = getRowValues(csv, i, columns, types, table);
						if (values == null) {
							break;
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
					addActionMessage(getText("success-import", "success-import", arg));
				}
			});

			tableList.add(table);
		}
		catch (Throwable e) {
			logException("CsvImport", e);
		}
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Object> getRowValues(CsvReader csv, int r,
			List<String> columns, List<DataType> types, List uploadData) {
		try {
			List<String> row = csv.readNext();
			if (row == null) {
				return null;
			}
			uploadData.add(row);
	
			boolean empty = true;
			
			Map<String, Object> values = new HashMap<String, Object>(columns.size());
			for (int c = 0; c < columns.size(); c++) {
				String v = null;
				if (c < row.size()) {
					v = row.get(c);
				}
				
				try {
					Object cv = getCellValue(v, c, types);
	
					empty = (cv == null);
	
					values.put(columns.get(c), cv);
				}
				catch (Exception e) {
					String msg = getError(arg.target, r, c, v, e);
					throw new Exception(msg);
				}
			}
			
			return empty ? null : values;
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
		int offset = 0;
		int length = data.length;
		
		if (length > 3 && Charsets.UTF_8.equalsIgnoreCase(arg.encoding)) {
			if (data[0] == -17 && data[1] == -69 && data[2] == -65) {
				offset = 3;
				length -= 3;
			}
		}
		return new CsvReader(new InputStreamReader(
				new ByteArrayInputStream(data, offset, length), arg.encoding),
				separator);
	}

	protected void prepareData(Object data) {
		((AppActionAssist)getAssist()).initCommonFields(data);
	}

	protected void saveRow(Dao dao, Class targetType, Object row) {
		Object data = Castors.scast(row, targetType);
		prepareData(data);
		if (arg.deleteAll) {
			dao.insert(data);
		}
		else {
			dao.save(data);
		}
	}
}
