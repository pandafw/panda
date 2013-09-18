package panda.dao.sql.executor;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

import panda.bean.PropertyAccessor;
import panda.dao.sql.JdbcTypes;
import panda.dao.sql.SqlExecutor;
import panda.dao.sql.SqlNamings;
import panda.dao.sql.adapter.TypeAdapter;
import panda.dao.sql.adapter.TypeAdapters;

/**
 * @author yf.frank.wang@gmail.com
 */
public abstract class JavaScriptSqlParser implements SqlParser {
	private StringBuilder sql;
	private StringBuilder buf;
	private String last;
	
	private boolean colon;
	private boolean comment;
	private boolean quote;
	private boolean escape;

	protected List<SqlParameter> sqlParams;
	
	protected Object parameter;
	protected PropertyAccessor bean;

	/**
	 * Constructor
	 */
	public JavaScriptSqlParser() {
	}

	/**
	 * parse
	 */
	protected abstract void parse() throws Exception;
	
	/**
	 * translate sql by parameter
	 * @param executor sql executor
	 * @param parameter parameter
	 * @param sqlParams parameter list (output)
	 * @return jdbc sql  
	 */
	public String parse(SqlExecutor executor, Object parameter, List<SqlParameter> sqlParams) {
		this.sqlParams = sqlParams;
		this.parameter = parameter;
		this.bean = new PropertyAccessor(this.parameter);

		reset();
		
		try {
			parse();
		}
		catch (RuntimeException e) {
			throw e;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		flush();
		
		return toString();
	}

	/**
	 * append string
	 * @param s string
	 */
	protected void append(String s) {
		if (s != null && s.length() > 0) {
			for (int i = 0; i < s.length(); i++) {
				append(s.charAt(i));
			}
		}
	}
	
	/**
	 * append char
	 * @param c char
	 */
	protected void append(char c) {
		if (quote) {
			if (c == '\'') {
				buf.append(c);
				escape = !escape;
			}
			else {
				if (escape) {
					escape = false;
					quote = false;
					forceFlush();
				}
				buf.append(c);
			}
		}
		else if (comment) {
			buf.append(c);
			if (c == '/') {
				if (buf.charAt(buf.length() - 1) == '*') {
					comment = false;
					forceFlush();
				}
			}
		}
		else if (colon) {
			if (Character.isJavaIdentifierPart(c) || c == ':' || c == '.') {
				buf.append(c);
			}
			else {
				colon = false;
				flush();
				append(c);
			}
		}
		else {
			if (c == '\'') {
				buf.append(c);
				quote = true;
				escape = false;
			}
			else if (c == '*') {
				buf.append(c);
				if (buf.length() == 1 && buf.charAt(0) == '/') {
					comment = true;
				}
			}
			else if (c == ',') {
				flush();
				buf.append(c);
				flush();
			}
			else if (c == ':') {
				flush();
				buf.append(c);
				colon = true;
			}
			else if (Character.isWhitespace(c)) {
				flush();
			}
			else {
				buf.append(c);
			}
		}
	}
	
	/**
	 * reset
	 */
	private void reset() {
		buf = new StringBuilder();
		sql = new StringBuilder();

		colon = false;
		quote = false;
		comment = false;
		escape = false;
	}
	
	/**
	 * make a error message
	 * @param msg message
	 * @return error message
	 */
	private String error(String msg) {
		StringBuilder sb = new StringBuilder(50 + sql.length() + buf.length());
		sb.append("Illegal sql statement (")
		  .append(sql.length())
		  .append("): ")
		  .append(msg)
		  .append(" - ")
		  .append(sql.toString())
		  .append(' ')
		  .append(buf.toString());
		
		return sb.toString();
	}
	
	/**
	 * flush absolutely
	 */
	private void forceFlush() {
		if (buf != null && buf.length() > 0) {
			if (sql.length() > 0) {
				sql.append(' ');
			}
			sql.append(buf.toString());
			buf.setLength(0);
		}
		last = null;
	}
	
	/**
	 * flush
	 */
	private void flush() {
		if (buf == null || buf.length() < 1) {
			return;
		}

		if (quote) {
			throw new RuntimeException(error("unexpected end of string"));
		}
		if (comment) {
			throw new RuntimeException(error("unexpected end of comment"));
		}
		
		String str = buf.toString();

		boolean append = true;
		if (last != null) {
			if (str.charAt(0) == ':') {
				if (str.length() < 2) {
					throw new RuntimeException(error("unexpected parameter reached"));
				}
				if (str.charAt(1) == ':') {
					if (str.length() < 3) {
						throw new RuntimeException(error("unexpected replacement reached"));
					}
					str = replacement(str.substring(2));
				}
				else {
					str = parameter(str.substring(1));
				}
			}
			if (last.equalsIgnoreCase("AS")) {
				str = SqlNamings.javaName2ColumnLabel(str);
			}
			else if (last.equalsIgnoreCase("WHERE")) {
				if (str.equalsIgnoreCase("AND") || str.equalsIgnoreCase("OR")) {
					append = false;
				}
			}
			else if (last.equalsIgnoreCase("SET") || last.equalsIgnoreCase("BY")) {
				if (str.equals(",")) {
					append = false;
				}
			}
		}

		if (append) {
			last = str;
			if (last != null && last.length() > 0) {
				if (sql.length() > 0) {
					sql.append(' ');
				}
				sql.append(last);
			}
		}

		buf.setLength(0);
	}
	
	private String parameter(String name) {
		String type = null;
		Integer scale = null; 
		String mode = null;

		int i = name.indexOf(':');
		if (i > 0) {
			type = name.substring(i + 1).toUpperCase();
			name = name.substring(0, i);

			i = type.indexOf(':');
			if (i > 0) {
				mode = type.substring(i + 1);
				type = type.substring(0, i);
			}

			i = type.indexOf('.');
			if (i > 0) {
				try {
					scale = Integer.parseInt(type.substring(i + 1));
				}
				catch (NumberFormatException e) {
					error("parameter scale must be a parsable integer");
				}
				type = type.substring(0, i);
			}
			
			Integer sqlType = JdbcTypes.getType(type);
			if (sqlType == null) {
				error("': unknown JDBC type [" + type + "].");
			}
		}

		//TODO: 
		TypeAdapters typeAdapters = TypeAdapters.me();
		Object paramValue = bean.get(name);
		if (paramValue != null) {
			TypeAdapter typeAdapter = typeAdapters.getTypeAdapter(paramValue.getClass(), type);
			if (typeAdapter == null) {
				if (paramValue.getClass().isArray()) {
					int len = Array.getLength(paramValue);
					if (len > 0) {
						StringBuilder sb = null;
						for (i = 0; i < len; i++) {
							Object v = Array.get(paramValue, i);
							this.sqlParams.add(new SqlParameter(name, v, type, scale, mode, typeAdapters));
							if (sb == null) {
								sb = new StringBuilder(len * 2);
							}
							else {
								sb.append(',');
							}
							sb.append('?');
						}
						return sb.toString();
					}
				}
				else if (paramValue instanceof Collection) {
					Collection c = (Collection)paramValue;
					if (!c.isEmpty()) {
						StringBuilder sb = null;
						for (Object v : c) {
							this.sqlParams.add(new SqlParameter(name, v, type, scale, mode, typeAdapters));
							if (sb == null) {
								sb = new StringBuilder(c.size() * 2);
							}
							else {
								sb.append(',');
							}
							sb.append('?');
						}
						return sb.toString();
					}
				}
			}
		}

		this.sqlParams.add(new SqlParameter(name, paramValue, type, scale, mode, typeAdapters));
		return "?";
	}
	
	private String replacement(String replacement) {
		Object val = bean.get(replacement);
		return val != null ? val.toString() : "";
	}
	
	/**
	 * @return a string representation of the object.
	 */
	public String toString() {
		return sql.toString();
	}
}
