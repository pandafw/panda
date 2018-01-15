package panda.app.action.crud;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import panda.app.constant.RES;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityFKey;
import panda.dao.entity.EntityField;
import panda.dao.entity.EntityHelper;
import panda.dao.entity.EntityIndex;
import panda.io.FileNames;
import panda.io.FileTypes;
import panda.io.Streams;
import panda.io.stream.BOMInputStream;
import panda.io.stream.ByteArrayOutputStream;
import panda.io.stream.CsvReader;
import panda.io.stream.ListReader;
import panda.io.stream.XlsReader;
import panda.io.stream.XlsxReader;
import panda.lang.Arrays;
import panda.lang.Chars;
import panda.lang.Charsets;
import panda.lang.Collections;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.Mvcs;
import panda.mvc.view.util.Escapes;
import panda.vfs.FileItem;

/**
 * @param <T> data type
 */
public abstract class GenericImportAction<T> extends GenericBaseAction<T> {
	private final static Log log = Logs.getLog(GenericImportAction.class);
	
	protected static String[] FEXTS = { FileTypes.CSV, FileTypes.TSV, FileTypes.TXT, FileTypes.XLS, FileTypes.XLSX };

	public static class Arg {
		private FileItem file;
		private boolean update = false;
		
		/** not strict (continue if error occurs) */
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

		public boolean isUpdate() {
			return update;
		}

		public void setUpdate(boolean update) {
			this.update = update;
		}
	}

	public static class Ret {
		private Object headers;
		private List<List<?>> warning = new ArrayList<List<?>>();
		private List<List<?>> success = new ArrayList<List<?>>();

		public List<List<?>> getWarning() {
			return warning;
		}

		public void setWarning(List<List<?>> warning) {
			this.warning = warning;
		}

		public List<List<?>> getSuccess() {
			return success;
		}

		public void setSuccess(List<List<?>> success) {
			this.success = success;
		}

		public Object getHeaders() {
			return headers;
		}

		public void setHeaders(Object headers) {
			this.headers = headers;
		}
	}

	/** argument */
	private Arg arg;
	
	/** treat number as text string (XlsReader) */
	private boolean numAsText;
	
	/** key definition for update */
	private Object updateKey;

	/**
	 * @return the numAsText
	 */
	protected boolean isNumAsText() {
		return numAsText;
	}

	/**
	 * @param numAsText the numAsText to set
	 */
	protected void setNumAsText(boolean numAsText) {
		this.numAsText = numAsText;
	}

	/**
	 * @return the updateKey
	 */
	protected Object getUpdateKey() {
		return updateKey;
	}

	/**
	 * @param updateKey the updateKey to set
	 */
	protected void setUpdateKey(Object updateKey) {
		this.updateKey = updateKey;
	}

	/**
	 * @return the updatable
	 */
	public boolean isUpdatable() {
		return updateKey != null;
	}
	
	/**
	 * @param fext file extension
	 * @return true if the file extension is acceptable
	 */
	protected boolean isAcceptableFileType(String fext) {
		return Arrays.contains(FEXTS, fext);
	}
	
	/**
	 * @param arg the input arguments
	 * @return result
	 * @throws Exception if an error occurs
	 */
	protected Object importx(Arg arg) {
		this.arg = arg;
		if (arg.file == null) {
			return null;
		}

		ListReader reader = null;
		try {
			InputStream input = null;
			String fext = FileNames.getExtension(arg.file.getName());
			if (FileTypes.ZIP.equalsIgnoreCase(fext)) {
				ZipInputStream zis = new ZipInputStream(arg.file.getInputStream(), Charsets.CS_UTF_8);
				ZipEntry entry;
				try {
					while ((entry = zis.getNextEntry()) != null) {
						fext = FileNames.getExtension(entry.getName());
						if (isAcceptableFileType(fext)) {
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							Streams.copy(zis, baos);
							input = baos.toInputStream();
							break;
						}
						zis.closeEntry();
					}
				}
				finally {
					zis.close();
				}
				
				if (input == null) {
					addFieldError("file", getText("error-file"));
					return null;
				}
			}

			if (input == null) {
				if (!isAcceptableFileType(fext)) {
					addFieldError("file", getText("error-file"));
					return null;
				}
				input = arg.file.getInputStream();
			}

			if (FileTypes.XLS.equalsIgnoreCase(fext)) {
				reader = getXlsReader(input, false);
			}
			else if (FileTypes.XLSX.equalsIgnoreCase(fext)) {
				reader = getXlsReader(input, true);
			}
			else if (FileTypes.CSV.equalsIgnoreCase(fext)) {
				reader = getCsvReader(input, Chars.COMMA);
			}
			else if (FileTypes.TSV.equalsIgnoreCase(fext) || FileTypes.TXT.equalsIgnoreCase(fext)) {
				reader = getCsvReader(input, Chars.TAB);
			}
			else {
				addFieldError("file", getText("error-file"));
				return null;
			}
			return impFile(reader);
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

	/**
	 * get XLS reader
	 * @param input input stream
	 * @param ooxml xlsx format
	 * @return XlsReader
	 * @throws IOException if an IO error occurred
	 */
	protected XlsReader getXlsReader(InputStream input, boolean ooxml) throws IOException {
		if (input == null) {
			return null;
		}

		XlsReader xr = ooxml ? new XlsxReader(input) : new XlsReader(input);
		xr.setNumAsText(numAsText);
		return xr;
	}


	/**
	 * get Csv reader
	 * @param input input stream
	 * @param separator separator
	 * @return CsvReader
	 * @throws IOException if an IO error occurred
	 */
	protected CsvReader getCsvReader(InputStream input, char separator) throws IOException {
		if (input == null) {
			return null;
		}

		BOMInputStream bis = Streams.toBOMInputStream(input);
		Charset cs = bis.getBOMCharset();
		return new CsvReader(new InputStreamReader(bis, cs == null ? Charsets.CS_UTF_8 : cs), separator);
	}

	/**
	 * mapping headers to columns
	 * @param headers headers
	 * @return columns
	 */
	protected String[] mapColumns(List<?> headers) {
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

	/**
	 * import file
	 * @param lst list reader
	 * @return Ret Object
	 * @throws IOException if an IO error occurred
	 */
	protected Object impFile(final ListReader lst) throws IOException {
		if (lst == null) {
			return null;
		}

		final List<?> headers = lst.readList();
		if (headers == null || headers.isEmpty()) {
			return null;
		}

		final String[] columns = mapColumns(headers);
		if (columns == null) {
			return null;
		}

		final Ret ret = new Ret();
		ret.headers = headers;

		if (arg.loose) {
			impDatas(ret, lst, columns);
		}
		else {
			getDao().exec(new Runnable() {
				@Override
				public void run() {
					try {
						impDatas(ret, lst, columns);
					}
					catch (Exception e) {
						throw Exceptions.wrapThrow(e);
					}
				}
			});
		}

		if (ret.success.size() > 0) {
			addActionMessage(getMessage("info-imported", ret.success.size()));
		}
		if (ret.warning.size() > 0) {
			addActionWarning(getMessage("warn-skipped", ret.warning.size()));
		}
		return ret;
	}

	/**
	 * @param ret the Ret object
	 * @param lst the list reader
	 * @param columns columns
	 * @throws IOException if an IO error occurred
	 */
	protected void impDatas(Ret ret, ListReader lst, String[] columns) throws IOException {
		for (int i = 1; ; i++) {
			List row = lst.readList();
			if (row == null) {
				break;
			}

			trimRow(row);

			if (Collections.isItemsEmpty(row)) {
				continue;
			}

			Map<String, Object> values = rowToMap(columns, row);
			if (values.isEmpty()) {
				continue;
			}

			try {
				T data = castData(values);

				impData(data);
				
				ret.success.add(row);
			}
			catch (Exception e) {
				if (!arg.loose) {
					String vs = rowToString(row);
					String msg = getDataError(i, vs, e);
					throw new RuntimeException(msg);
				}
				Collections.insert(row, 0, "[" + i + "]: " + e.getMessage());
				ret.warning.add(row);
			}
		}
	}

	/**
	 * import data
	 * @param data the the input data
	 */
	protected void impData(T data) {
		trimData(data);

		checkNotNulls(data);
		validateData(data);
		checkForeignKeys(data);
		if (updateKey != null && arg.update) {
			T sdat = findSourceData(data);
			if (sdat != null) {
				checkUniqueIndexesOnUpdate(data, sdat);
				updateData(data, sdat);
				return;
			}
		}
		
		checkPrimaryKeysOnInsert(data);
		checkUniqueIndexesOnInsert(data);
		insertData(data);
	}

	/**
	 * convert row data to data map
	 * @param columns columns 
	 * @param row row data
	 * @return data map
	 */
	protected Map<String, Object> rowToMap(String[] columns, List<?> row) {
		Map<String, Object> values = new HashMap<String, Object>(columns.length);
		for (int c = 0; c < columns.length && c < row.size(); c++) {
			Object v = row.get(c);

			if (v instanceof CharSequence) {
				v = Strings.stripToNull((CharSequence)v);
			}
			if (v != null) {
				values.put(columns[c], v);
			}
		}
		return values;
	}

	/**
	 * log exception
	 * @param method method name
	 * @param e exception
	 */
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

	/**
	 * get header error
	 * @param col column number
	 * @param val value
	 * @return error message
	 */
	@SuppressWarnings("unchecked")
	protected String getHeaderError(int col, Object val) {
		Map m = new HashMap();
		m.put("col", col);
		m.put("value", val);

		return getMessage("error-header", m);
	}

	/**
	 * get data error
	 * @param row row number
	 * @param val value
	 * @param e exception
	 * @return error message
	 */
	@SuppressWarnings("unchecked")
	protected String getDataError(int row, Object val, Throwable e) {
		Map m = new HashMap();
		m.put("row", row);
		m.put("value", val);
		m.put("error", e.getMessage());

		return getMessage("error-data", m);
	}

	/**
	 * get value error
	 * @param row row number
	 * @param col col number
	 * @param val value
	 * @param e exception
	 * @return error message
	 */
	@SuppressWarnings("unchecked")
	protected String getValueError(int row, int col, Object val, Throwable e) {
		Map m = new HashMap();
		m.put("row", row);
		m.put("col", col);
		m.put("value", val);
		m.put("error", e.getMessage());

		return getMessage("error-value", m);
	}

	/**
	 * trim data of row 
	 * @param row row data
	 */
	protected void trimRow(List row) {
	}

	/**
	 * cast data map to data object
	 * @param values data map
	 * @return data object
	 */
	protected T castData(Map<String, Object> values) {
		context.clearCastErrors();
		return Mvcs.castValueWithErrors(context, values, getType(), null);
	}

	/**
	 * trim data
	 * @param data data object
	 */
	protected void trimData(T data) {
		assist().initCommonFields(data);
	}

	/**
	 * insert data
	 * @param data the input data
	 */
	protected void insertData(T data) {
		getDao().insert(data);
	}

	/**
	 * update data
	 * @param data the input data
	 * @param sdat the source data
	 */
	protected void updateData(T data, T sdat) {
		EntityHelper.copyPrimaryKeyValues(getEntity(), sdat, data);
		getDao().update(data);
	}
	
	// ------------------------------------------------------------
	// check methods
	// ------------------------------------------------------------
	/**
	 * checkNotNulls
	 * 
	 * @param data data
	 */
	protected void checkNotNulls(T data) {
		List<EntityField> efs = EntityHelper.checkNotNulls(getEntity(), data);
		if (Collections.isNotEmpty(efs)) {
			throw new IllegalArgumentException(dataIncorrectError(data, efs));
		}
	}

	/**
	 * validate data
	 * 
	 * @param data data
	 */
	protected void validateData(T data) {
		if (Mvcs.validate(getContext(), data)) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(getMessage(RES.ERROR_INPUT));
		for (Entry<String, List<String>> en : getParamAware().getErrors().entrySet()) {
			String label = getFieldLabel(en.getKey()) + ": ";
			for (String s : en.getValue()) {
				sb.append(Chars.LF).append(label).append(s);
			}
		}
		getParamAware().clearErrors();

		throw new IllegalArgumentException(sb.toString());
	}

	/**
	 * find existing data
	 * @param data the input data
	 * @return data object
	 */
	protected T findSourceData(T data) {
		if (updateKey instanceof EntityField) {
			EntityField ef = (EntityField)updateKey;
			return EntityHelper.fetchDataByField(getDao(), getEntity(), ef, data);
		}
		else if (updateKey instanceof EntityIndex) {
			Collection<EntityField> efs = ((EntityIndex)updateKey).getFields();
			return EntityHelper.fetchDataByFields(getDao(), getEntity(), efs, data);
		}
		else if (updateKey instanceof Collection) {
			return EntityHelper.fetchDataByFields(getDao(), getEntity(), (Collection)updateKey, data);
		}
		else if (updateKey instanceof String[]) {
			return EntityHelper.fetchDataByFields(getDao(), getEntity(), (String[])updateKey, data);
		}
		else {
			throw new IllegalArgumentException("Invalid update key: " + updateKey.getClass());
		}
	}
	
	/**
	 * checkPrimaryKeys
	 * 
	 * @param data the input data
	 */
	protected void checkPrimaryKeysOnInsert(T data) {
		EntityField eid = getEntity().getIdentity();
		if (eid == null) {
			if (!EntityHelper.hasPrimaryKeyValues(getEntity(), data)) {
				throw new IllegalArgumentException(dataIncorrectError(data, getEntity().getPrimaryKeys()));
			}
		}
		else {
			Object id = eid.getValue(data);
			if (!getDao().isValidIdentity(id)) {
				return;
			}
		}
		
		if (getDao().exists(getEntity(), data)) {
			throw new IllegalArgumentException(dataDuplicateError(data, getEntity().getPrimaryKeys()));
		}
	}

	/**
	 * checkUniqueIndexes for insert
	 * 
	 * @param data the input data
	 */
	protected void checkUniqueIndexesOnInsert(T data) {
		EntityIndex ei = EntityHelper.findDuplicateUniqueIndex(getDao(), getEntity(), data, null);
		if (ei != null) {
			throw new IllegalArgumentException(dataDuplicateError(data, ei.getFields()));
		}
	}

	/**
	 * checkUniqueIndexes for update
	 * 
	 * @param data the input data
	 * @param sdat the source data
	 */
	protected void checkUniqueIndexesOnUpdate(T data, T sdat) {
		EntityIndex ei = EntityHelper.findDuplicateUniqueIndex(getDao(), getEntity(), data, sdat);
		if (ei != null) {
			throw new IllegalArgumentException(dataDuplicateError(data, ei.getFields()));
		}
	}

	/**
	 * checkForeignKeys
	 * 
	 * @param data the input data
	 */
	protected void checkForeignKeys(T data) {
		EntityFKey efk = EntityHelper.findIncorrectForeignKey(getDao(), getEntity(), data);
		if (efk == null) {
			return;
		}
		
		throw new IllegalArgumentException(dataIncorrectError(data, efk.getFields()));
	}

	// ------------------------------------------------------------
	// error message methods
	// ------------------------------------------------------------
	/**
	 * convert row data to string
	 * @param row row data
	 * @return the data string
	 */
	protected String rowToString(final List row) {
		Iterator iterator = row.iterator();
		final Object first = iterator.next();

		// two or more elements
		final StringBuilder buf = new StringBuilder(256);
		if (first != null) {
			buf.append(formatValue(first));
		}

		while (iterator.hasNext()) {
			buf.append(',');
			final Object obj = iterator.next();
			if (obj != null) {
				buf.append(formatValue(obj));
			}
		}

		return buf.toString();
	}

	/**
	 * get data field errors
	 * @param data the input data
	 * @param efs entity fields
	 * @param dataErrMsg data error message
	 * @return the error message
	 */
	protected String dataFieldErrors(T data, Collection<EntityField> efs, String dataErrMsg) {
		return dataFieldErrors(data, efs, dataErrMsg, null);
	}

	/**
	 * get data field errors
	 * @param data the input data
	 * @param efs entity fields
	 * @param dataErrMsg data error message
	 * @param format the data format
	 * @return the error message
	 */
	protected String dataFieldErrors(T data, Collection<EntityField> efs, String dataErrMsg, String format) {
		StringBuilder sb = new StringBuilder();
		sb.append(" (");
		for (EntityField ef : efs) {
			EntityField eff = mappedEntityField(ef);
			if (!displayField(eff.getName())) {
				continue;
			}

			String label = getFieldLabel(eff.getName());
			sb.append(label);
			sb.append(": ");

			Object fv = eff.getValue(data);
			if (fv != null) {
				sb.append(Mvcs.castString(context, fv, format));
			}
			sb.append(", ");
		}

		if (sb.charAt(sb.length() - 1) == ' ') {
			sb.setLength(sb.length() - 2);
		}
		sb.append(')');
		return getMessage(dataErrMsg, sb.toString());
	}

	/**
	 * get data duplicate error
	 * @param data the input data
	 * @param efs entity fields
	 * @return the error message
	 */
	protected String dataDuplicateError(T data, Collection<EntityField> efs) {
		return dataFieldErrors(data, efs, RES.ERROR_DATA_DUPLICATE);
	}

	/**
	 * get data incorrect error
	 * @param data the input data
	 * @param efs entity fields
	 * @return the error message
	 */
	protected String dataIncorrectError(T data, Collection<EntityField> efs) {
		return dataFieldErrors(data, efs, RES.ERROR_DATA_INCORRECT);
	}

	// ------------------------------------------------------------
	// html escape methods
	//
	/**
	 * escape value
	 * @param v value
	 * @return escaped string
	 */
	public String escapeValue(Object v) {
		String s = formatValue(v);
		return Escapes.escape(s, Escapes.ESCAPE_PHTML);
	}

	/**
	 * escape value
	 * @param v value
	 * @param format format
	 * @return escaped string
	 */
	protected String escapeValue(Object v, String format) {
		String s = formatValue(v, format);
		return Escapes.escape(s, Escapes.ESCAPE_PHTML);
	}

	/**
	 * format value
	 * @param v value
	 * @return formated string
	 */
	public String formatValue(Object v) {
		return formatValue(v, null);
	}

	/**
	 * format value
	 * @param v value
	 * @param format format
	 * @return formated string
	 */
	protected String formatValue(Object v, String format) {
		if (v == null) {
			return Strings.EMPTY;
		}

		String s = null;
		if (v instanceof CharSequence) {
			s = ((CharSequence)v).toString();
		}
		else {
			s = Mvcs.castString(getContext(), v, format);
		}
		return s;
	}
}
