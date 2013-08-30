package panda.dao.sql.engine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import panda.bean.BeanHandler;
import panda.dao.sql.JdbcTypes;
import panda.dao.sql.SqlExecutor;
import panda.dao.sql.adapter.TypeAdapter;
import panda.dao.sql.adapter.TypeAdapters;
import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
public class SimpleSqlParser implements SqlParser {
	/**
	 * SqlSegment
	 */
	protected static interface SqlSegment {
		/**
		 * translate 
		 * @param parameter parameter object
		 * @param strings strings
		 * @param sqlParams sql parameters
		 * @return translated string
		 */
		String translate(SqlExecutor executor, Object parameter, List<String> strings, List<SqlParameter> sqlParams);
	}

	//-------------------------------------------------------------------------
	// Implements 
	//-------------------------------------------------------------------------
	/**
	 * AsAliasSegment
	 */
	protected static class AsAliasSegment implements SqlSegment {
		protected String alias;

		/**
		 * Constructor
		 * @param alias alias
		 */
		public AsAliasSegment(String alias) {
			this.alias = SqlNamings.javaName2ColumnLabel(alias);
		}

		/**
		 * translate 
		 * @param parameter parameter object
		 * @param strings strings
		 * @param sqlParams sql parameters
		 * @return translated string
		 */
		public String translate(SqlExecutor executor, Object parameter, List<String> strings, List<SqlParameter> sqlParams) {
			strings.add(alias);
			return alias;
		}
	}

	protected static class ParameterSegment implements SqlSegment {
		private String name;
		
		/**
		 * jdbc type or type alias
		 */
		private String type;
		private Integer scale; 
		private String mode;
		
		/**
		 * Constructor
		 * @param parameter parameter
		 */
		public ParameterSegment(String parameter) {
			parse(parameter);
		}
		
		/**
		 * parse
		 * @param content content
		 */
		protected void parse(String content) {
			name = content;
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
						throw new IllegalArgumentException("Illegal parameter: scale must be a parsable integer.", e);
					}
					type = type.substring(0, i);
				}

				// is this type check required?
				Integer sqlType = JdbcTypes.getType(type);
				if (sqlType == null) {
					throw new IllegalArgumentException("Illegal parameter '" + name + "': unknown JDBC type [" + type + "].");
				}
			}
		}

		/**
		 * translate 
		 * @param parameter parameter object
		 * @param strings strings
		 * @param sqlParams sql parameters
		 * @return translated string
		 */
		@SuppressWarnings("unchecked")
		public String translate(SqlExecutor executor, Object parameter, List<String> strings, List<SqlParameter> sqlParams) {
			String translatedString = null;

			Object paramValue = null;
			if (parameter != null) {
				BeanHandler beanHandler = executor.getBeans().getBeanHandler(parameter.getClass());
				paramValue = beanHandler.getBeanValue(parameter, name);
			}

			TypeAdapters typeAdapters = executor.getTypeAdapters();
			if (paramValue != null) {
				TypeAdapter typeAdapter = typeAdapters.getTypeAdapter(paramValue.getClass(), type);
				if (typeAdapter == null) {
					if (paramValue.getClass().isArray()) {
						int len = Array.getLength(paramValue);
						if (len > 0) {
							StringBuilder sb = null;
							for (int i = 0; i < len; i++) {
								Object v = Array.get(paramValue, i);
								sqlParams.add(new SqlParameter(name, v, type, scale, mode, typeAdapters));
								if (sb == null) {
									sb = new StringBuilder(len * 2);
								}
								else {
									sb.append(',');
								}
								sb.append('?');
							}
							translatedString = sb.toString(); 
							strings.add(translatedString);
							return translatedString;
						}
					}
					else if (paramValue instanceof Collection) {
						Collection c = (Collection)paramValue;
						if (!c.isEmpty()) {
							StringBuilder sb = null;
							for (Object v : c) {
								sqlParams.add(new SqlParameter(name, v, type, scale, mode, typeAdapters));
								if (sb == null) {
									sb = new StringBuilder(c.size() * 2);
								}
								else {
									sb.append(',');
								}
								sb.append('?');
							}
							translatedString = sb.toString(); 
							strings.add(translatedString);
							return translatedString;
						}
					}
				}
			}

			translatedString = "?";
			sqlParams.add(new SqlParameter(name, paramValue, type, scale, mode, typeAdapters));
			strings.add(translatedString);
			return translatedString;
		}
	}

	protected static class ReplacerSegment implements SqlSegment {
		private String propertyName;

		/**
		 * Constructor
		 * @param propertyName property name
		 */
		public ReplacerSegment(String propertyName) {
			this.propertyName = propertyName;
		}
		
		/**
		 * translate 
		 * @param parameter parameter object
		 * @param strings strings
		 * @param sqlParams sql parameters
		 * @return translated string
		 */
		@SuppressWarnings("unchecked")
		public String translate(SqlExecutor executor, Object parameter, List<String> strings, List<SqlParameter> sqlParams) {
			String translatedString = null;
			
			Object repValue = null;
			if (parameter != null) {
				BeanHandler beanHandler = executor.getBeans().getBeanHandler(parameter.getClass());
				repValue = beanHandler.getBeanValue(parameter, propertyName);
			}

			if (repValue != null) {
				translatedString = repValue.toString();
				strings.add(translatedString);
			}
			
			return translatedString;
		}
	}

	/**
	 * SqlCommentExpression
	 */
	protected static class SqlCommentSegment implements SqlSegment {
		private String comment;
		
		/**
		 * Constructor
		 * @param comment comment
		 */
		public SqlCommentSegment(String comment) {
			this.comment = comment;
		}

		/**
		 * translate 
		 * @param parameter parameter object
		 * @param strings strings
		 * @param sqlParams sql parameters
		 * @return translated string
		 */
		public String translate(SqlExecutor executor, Object parameter, List<String> strings, List<SqlParameter> sqlParams) {
			strings.add(comment);
			return comment;
		}
	}

	/**
	 * SqlStringSegment
	 */
	protected static class SqlStringSegment implements SqlSegment {
		private String string;
		
		/**
		 * @param string string
		 */
		public SqlStringSegment(String string) {
			this.string = string;
		}

		/**
		 * translate 
		 * @param parameter parameter object
		 * @param strings strings
		 * @param sqlParams sql parameters
		 * @return translated string
		 */
		public String translate(SqlExecutor executor, Object parameter, List<String> strings, List<SqlParameter> sqlParams) {
			strings.add(string);
			return string;
		}
	}

	/**
	 * SqlWordSegment
	 */
	protected static class SqlWordSegment implements SqlSegment {
		private static final int NORMAL = 0;
		private static final int COMMA = 1;
		private static final int AS = 2;
		private static final int AND = 3;
		private static final int OR = 4;
		
		private int kind;
		private String content;
		
		/**
		 * @param content content
		 */
		public SqlWordSegment(String content) {
			this.content = content;

			if (",".equals(content)) {
				kind = COMMA;
			}
			else if ("AS".equalsIgnoreCase(content)) {
				kind = AS;
			}
			else if ("AND".equalsIgnoreCase(content)) {
				kind = AND;
			}
			else if ("OR".equalsIgnoreCase(content)) {
				kind = OR;
			}
			else {
				kind = NORMAL;
			}
		}

		/**
		 * translate 
		 * @param parameter parameter object
		 * @param strings strings
		 * @param sqlParams sql parameters
		 * @return translated string
		 */
		public String translate(SqlExecutor executor, Object parameter, List<String> strings, List<SqlParameter> sqlParams) {
			if (kind == AND || kind == OR) {
				if (!strings.isEmpty()) {
					String prev = strings.get(strings.size() - 1);
					if ("WHERE".equalsIgnoreCase(prev) || prev.endsWith("(")) {
						return null;
					}
				}
			}
			else if (kind == COMMA) {
				if (!strings.isEmpty()) {
					String prev = strings.get(strings.size() - 1);
					if ("SET".equalsIgnoreCase(prev) || "BY".equalsIgnoreCase(prev)) {
						return null;
					}
				}
			}

			strings.add(content);
			return content;
		}
	}

	//-------------------------------------------------------------------------
	// properties
	//-------------------------------------------------------------------------
	protected final List<SqlSegment> segments;
	
	/**
	 * Constructor
	 * @param sql sql statement
	 */
	public SimpleSqlParser(String sql) {
		segments = new ArrayList<SqlSegment>();
		compile(sql, segments);
	}

	/**
	 * parse sql by parameter
	 * @param executor sql executor
	 * @param parameter parameter
	 * @param sqlParams parameter list (output)
	 * @return jdbc sql  
	 */
	public String parse(SqlExecutor executor, Object parameter, List<SqlParameter> sqlParams) {
		List<String> strings = new ArrayList<String>();
		
		StringBuilder sb = null;
		for (SqlSegment seg : segments) {
			String sql = seg.translate(executor, parameter, strings, sqlParams);
			if (Strings.isNotEmpty(sql)) {
				if (sb == null) {
					sb = new StringBuilder();
					sb.append(sql);
				}
				else {
					sb.append(' ');
					sb.append(sql);
				}
			}
		}

		String sql = sb == null ? null : sb.toString();
		return sql;
	}

	/**
	 * compile sql to segments
	 * 
	 * @param sql sql
	 * @param segments segments
	 */
	protected void compile(String sql, List<SqlSegment> segments) {
		int i;
		int start = 0;
		for (i = start; i < sql.length(); i++) {
			char c = sql.charAt(i);
			if (c == '/') {
				if (i + 1 < sql.length() && sql.charAt(i + 1) == '*') {
					if (i > start) {
						split(sql.substring(start, i), segments);
					}
					start = i;
					String comment = null;
					if (i + 2 < sql.length()) {
						int j = sql.indexOf("*/", i + 2);
						if (j > 0) {
							comment = sql.substring(i, j + 2);
							i = j + 1;
							start = j + 2;
						}
					}
					if (comment == null) {
						throw new IllegalArgumentException("Illegal sql statement (" + i + "): unexpected end of comment reached.");
					}
					
					segments.add(new SqlCommentSegment(comment));
				}
			}
			else if (c == '\'') {
				if (i > start) {
					split(sql.substring(start, i), segments);
				}
				start = i++;
				if (i >= sql.length()) {
					throw new IllegalArgumentException("Illegal sql statement (" + start + "): unexpected character ' reached.");
				}

				String str = null;
				for (; i < sql.length(); i++) {
					if (sql.charAt(i) == '\'') {
						if (i + 1 < sql.length() && sql.charAt(i + 1) == '\'') {
							i++;
							continue;
						}
						str = sql.substring(start, i + 1);
						start = i + 1;
						break;
					}
				}
				if (str == null) {
					throw new IllegalArgumentException("Illegal sql statement (" + start + "): unexpected end of string reached.");
				}
				segments.add(new SqlStringSegment(str));
			}
			else if (c == ':') {
				if (i > start) {
					split(sql.substring(start, i), segments);
				}

				start = i;
				for (i++; i < sql.length(); i++) {
					char c2 = sql.charAt(i);
					if (!Character.isJavaIdentifierPart(c2)	&& c2 != ':' && c2 != '.') {
						break;
					}
				}

				boolean dual = false;
				String param = sql.substring(start + 1, i);
				if (param.length() > 0 && param.charAt(0) == ':') {
					dual = true;
					param = param.substring(1);
				}
				if (param.length() < 1) {
					throw new IllegalArgumentException("Illegal sql statement (" + start + "): unexpected character : reached.");
				}

				if (dual) {
					segments.add(new ReplacerSegment(param));
				}
				else {
					segments.add(new ParameterSegment(param));
				}
				start = i--;
			}
		}

		if (i > start) {
			split(sql.substring(start, i), segments);
		}
	}

	/**
	 * isLastAsWord
	 * @param segments segments
	 * @return true if the last segment is 'AS'
	 */
	protected boolean isLastAsWord(List<SqlSegment> segments) {
		if (segments.isEmpty()) {
			return false;
		}
		
		SqlSegment seg = segments.get(segments.size() - 1);
		if (seg instanceof SqlWordSegment) {
			SqlWordSegment sws = (SqlWordSegment)seg;
			return sws.kind == SqlWordSegment.AS;
		}
		return false;
	}
	
	/**
	 * append sql
	 * @param sql sql
	 * @param segments segments
	 */
	protected void append(String sql, List<SqlSegment> segments) {
		if (isLastAsWord(segments)) {
			segments.add(new AsAliasSegment(sql));
		}
		else {
			segments.add(new SqlWordSegment(sql));
		}
	}

	/**
	 * split sql
	 * @param sql sql
	 * @param segments segments
	 */
	protected void split(String sql, List<SqlSegment> segments) {
		int start = 0;
		int i = 0;
		for (; i < sql.length(); i++) {
			int c = sql.charAt(i);
			if (Character.isWhitespace(c)) {
				if (i > start) {
					append(sql.substring(start, i), segments);
				}
				start = i + 1;
			}
			else if (c == ',') {
				if (i > start) {
					append(sql.substring(start, i), segments);
				}
				start = i + 1;
				segments.add(new SqlWordSegment(","));
			}
		}
		if (i > start) {
			append(sql.substring(start, i), segments);
		}
	}

}
