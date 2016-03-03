package panda.wing.action;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.dao.entity.Entity;
import panda.dao.entity.EntityFKey;
import panda.dao.entity.EntityField;
import panda.dao.entity.EntityHelper;
import panda.dao.entity.EntityIndex;
import panda.filepool.FileItem;
import panda.io.FileNames;
import panda.io.FileTypes;
import panda.io.Streams;
import panda.io.stream.CsvReader;
import panda.io.stream.ListReader;
import panda.io.stream.XlsReader;
import panda.io.stream.XlsxReader;
import panda.lang.Charsets;
import panda.lang.Collections;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.Mvcs;
import panda.wing.constant.RC;


/**
 * @param <T> data type
 */
public abstract class GenericImportAction<T> extends GenericBaseAction<T> {
	private final static Log log = Logs.getLog(GenericImportAction.class);
	
	public static class Arg {
		private FileItem file;
		private boolean loose = false;
		public FileItem getFile() {
			return file;
		}
		public void setFile(FileItem file) {
			this.file = file;
		}
		public boolean isLoose() {
			return loose;
		}
		public void setLoose(boolean loose) {
			this.loose = loose;
		}
	}

	public static class Ret {
		private Object headers;
		private List<List<String>> warning = new ArrayList<List<String>>();
		private List<List<String>> success = new ArrayList<List<String>>();
		public List<List<String>> getWarning() {
			return warning;
		}
		public void setWarning(List<List<String>> warning) {
			this.warning = warning;
		}
		public List<List<String>> getSuccess() {
			return success;
		}
		public void setSuccess(List<List<String>> success) {
			this.success = success;
		}
		public Object getHeaders() {
			return headers;
		}
		public void setHeaders(Object headers) {
			this.headers = headers;
		}
	}
	
	/**
	 * @return result
	 * @throws Exception if an error occurs
	 */
	protected Object import_(Arg arg) {
		if (arg.file == null) {
			return null;
		}

		ListReader reader = null;
		try {
			String fext = FileNames.getExtension(arg.file.getName());
			if (FileTypes.XLS.equalsIgnoreCase(fext)) {
				reader = getXlsReader(arg.file, false);
			}
			else if (FileTypes.XLSX.equalsIgnoreCase(fext)) {
				reader = getXlsReader(arg.file, true);
			}
			else if (FileTypes.CSV.equalsIgnoreCase(fext)) {
				reader = getCsvReader(arg.file, CsvReader.COMMA_SEPARATOR);
			}
			else if (FileTypes.TSV.equalsIgnoreCase(fext) 
					|| FileTypes.TXT.equalsIgnoreCase(fext)) {
				reader = getCsvReader(arg.file, CsvReader.TAB_SEPARATOR);
			}
			else {
				addFieldError("file", getText("error-file"));
				return null;
			}
			return impFile(reader, !arg.loose);
		}
		catch (Throwable e) {
			logException("import", e);
			return null;
		}
		finally {
			Streams.safeClose((Closeable)reader);
			try {
				arg.file.delete();
			}
			catch (IOException e) {
				log.error("Failed to delete file " + arg.file.getName(), e);
			}
		}
	}

	protected XlsReader getXlsReader(FileItem file, boolean ooxml) throws Exception {
		InputStream input = file.getInputStream();
		if (input == null) {
			return null;
		}
		
		if (ooxml) {
			return new XlsxReader(input);
		}
		return new XlsReader(input);
	}

	protected CsvReader getCsvReader(FileItem file, char separator) throws Exception {
		InputStream input = file.getInputStream();
		if (input == null) {
			return null;
		}
		
		return new CsvReader(
			new InputStreamReader(Streams.toBOMInputStream(input), Charsets.UTF_8),
			separator);
	}

	protected String[] mapColumns(List<String> headers) {
		String[] columns = new String[headers.size()];
		Entity<T> en = getEntity();
		for (EntityField ef : en.getFields()) {
			String t = getText("a.t." + ef.getName());
			int i = headers.indexOf(t);
			if (i >= 0) {
				columns[i] = ef.getName();
			}
		}
		
		for (int i = 0; i < columns.length; i++) {
			if (columns[i] == null) {
				addActionError(getHeaderError(i + 1, headers.get(i)));
				return null;
			}
		}
		return columns;
	}

	protected Object impFile(final ListReader csv, final boolean strict) throws Exception {
		if (csv == null) {
			return null;
		}
		
		final List<String> headers = csv.readList();
		if (headers == null || headers.isEmpty()) {
			return null;
		}

		final String[] columns = mapColumns(headers);
		if (columns == null) {
			return null;
		}
		
		final Ret ret = new Ret();
		ret.headers = headers;

		if (strict) {
			getDao().exec(new Runnable() {
				@Override
				public void run() {
					try {
						impData(ret, csv, columns, strict);
					}
					catch (Exception e) {
						throw Exceptions.wrapThrow(e);
					}
				}
			});
		}
		else {
			impData(ret, csv, columns, strict);
		}

		if (ret.success.size() > 0) {
			addActionMessage(getMessage("info-imported", ret.success.size()));
		}
		if (ret.warning.size() > 0) {
			addActionWarning(getMessage("warn-skipped", ret.warning.size()));
		}
		return ret;
	}
	
	protected void impData(Ret ret, ListReader csv, String[] columns, boolean strict) throws Exception {
		for (int i = 1; ; i++) {
			List<String> row = csv.readList();
			if (row == null) {
				break;
			}

			row = Collections.stripToNull(row);
			if (row == null) {
				continue;
			}
			
			Map<String, Object> values = rowToMap(columns, row);
			if (values.isEmpty()) {
				continue;
			}
			
			try {
				T data = Mvcs.castValue(context, values, type);
				saveData(data);
				ret.success.add(row);
			}
			catch (Exception e) {
				if (strict) {
					String msg = getDataError(i, Strings.join(row, ','), e);
					throw new RuntimeException(msg);
				}
				Collections.insert(row, 0, "[" + i + "]: " + e.getMessage());
				ret.warning.add(row);
				continue;
			}
		}
	}

	protected Map<String, Object> rowToMap(String[] columns, List<String> row) {
		Map<String, Object> values = new HashMap<String, Object>(columns.length);
		for (int c = 0; c < columns.length; c++) {
			String v = null;
			if (c < row.size()) {
				v = Strings.stripToNull(row.get(c));
			}

			if (v != null) {
				values.put(columns[c], v);
			}
		}
		return values;
	}
	
	protected void saveData(T data) {
		prepareData(data);
		
		checkData(data);
		
		getDao().insert(data);
	}

	protected void logException(String method, Throwable e) {
		log.warn(method, e);

		if (assist().isDebugEnabled()) {
			String s = Exceptions.getStackTrace(e);
			addActionError(s);
		}
		else {
			addActionError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	protected String getHeaderError(int col, Object val) {
		Map m = new HashMap();
		m.put("col", col);
		m.put("value", val);

		return getMessage("error-header", m);
	}

	@SuppressWarnings("unchecked")
	protected String getDataError(int row, Object val, Throwable e) {
		Map m = new HashMap();
		m.put("row", row);
		m.put("value", val);
		m.put("error", e.getMessage());

		return getMessage("error-data", m);
	}

	@SuppressWarnings("unchecked")
	protected String getValueError(int row, int col, Object val, Throwable e) {
		Map m = new HashMap();
		m.put("row", row);
		m.put("col", col);
		m.put("value", val);
		m.put("error", e.getMessage());

		return getMessage("error-value", m);
	}

	protected void prepareData(T data) {
		EntityHelper.clearIdentityValue(getEntity(), data);
		assist().initCommonFields(data);
	}

	//------------------------------------------------------------
	// check methods
	//------------------------------------------------------------
	protected void checkData(T data) {
		checkNotNulls(data);
		checkPrimaryKeys(data);
		checkUniqueKeys(data);
		checkForeignKeys(data);
	}

	/**
	 * checkNotNulls
	 * @param data data
	 */
	protected void checkNotNulls(T data) {
		List<EntityField> efs = EntityHelper.checkNotNulls(entity, data);
		if (Collections.isNotEmpty(efs)) {
			throw new IllegalArgumentException(dataIncorrectError(data, efs));
		}
	}

	/**
	 * checkPrimaryKeys
	 * @param data data
	 */
	protected void checkPrimaryKeys(T data) {
		if (!EntityHelper.checkPrimaryKeys(getDao(), getEntity(), data)) {
			throw new IllegalArgumentException(dataDuplicateError(data, getEntity().getPrimaryKeys()));
		}
	}

	/**
	 * checkUniqueKeys
	 * @param data data
	 */
	protected void checkUniqueKeys(T data) {
		Collection<EntityIndex> eis = getEntity().getIndexes();
		if (Collections.isEmpty(eis)) {
			return;
		}
		
		for (EntityIndex ei : eis) {
			if (!EntityHelper.checkUniqueIndex(getDao(), getEntity(), data, ei)) {
				throw new IllegalArgumentException(dataDuplicateError(data, ei.getFields()));
			}
		}
	}

	/**
	 * checkForeignKeys
	 * @param data
	 */
	protected void checkForeignKeys(T data) {
		Collection<EntityFKey> efks = getEntity().getForeignKeys();
		if (Collections.isEmpty(efks)) {
			return;
		}
		
		for (EntityFKey efk : efks) {
			if (!EntityHelper.checkForeignKey(getDao(), getEntity(), data, efk)) {
				throw new IllegalArgumentException(dataIncorrectError(data, efk.getFields()));
			}
		}
	}

	//------------------------------------------------------------
	// error message methods
	//------------------------------------------------------------
	protected String dataFieldErrors(T data, Collection<EntityField> efs, String dataErrMsg) {
		StringBuilder sb = new StringBuilder();
		sb.append(" (");
		for (EntityField ef :efs) {
			if (!displayField(ef.getName())) {
				continue;
			}
			
			EntityField eff = mappedEntityField(ef);

			String label = getFieldLabel(eff.getName());
			sb.append(label);
			sb.append(": ");

			Object fv = eff.getValue(data);
			if (fv != null) {
				sb.append(Mvcs.castString(context, fv));
			}
			sb.append(", ");
		}
		
		if (sb.charAt(sb.length() - 1) == ' ') {
			sb.setLength(sb.length() - 2);
		}
		sb.append(')');
		return getMessage(dataErrMsg, sb.toString());
	}
	
	protected String dataDuplicateError(T data, Collection<EntityField> efs) {
		return dataFieldErrors(data, efs, RC.ERROR_DATA_DUPLICATE);
	}

	protected String dataIncorrectError(T data, Collection<EntityField> efs) {
		return dataFieldErrors(data, efs, RC.ERROR_DATA_INCORRECT);
	}
}
