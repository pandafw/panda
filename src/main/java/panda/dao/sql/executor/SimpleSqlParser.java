package panda.dao.sql.executor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import panda.bean.BeanHandler;
import panda.dao.DaoNamings;
import panda.dao.DaoTypes;
import panda.dao.sql.adapter.TypeAdapters;
import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
public class SimpleSqlParser extends JdbcSqlParser {
	/**
	 * SqlSegment
	 */
	protected static interface SqlSegment {
		/**
		 * translate 
		 * @param sql sql to append
		 * @param parameter parameter object
		 * @param sqlParams sql parameters
		 * @return true if sql appended
		 */
		boolean translate(StringBuilder sql, JdbcSqlExecutor executor, Object parameter, List<JdbcSqlParameter> sqlParams);
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
			this.alias = DaoNamings.javaName2ColumnLabel(alias);
		}

		public boolean translate(StringBuilder sql, JdbcSqlExecutor executor, Object parameter, List<JdbcSqlParameter> sqlParams) {
			sql.append(' ').append(alias);
			return true;
		}

		@Override
		public String toString() {
			return "[A]: " + alias;
		}
	}

	protected static class ParameterSegment implements SqlSegment {
		protected String name;
		
		/**
		 * jdbc type or type alias
		 */
		protected String type;
		protected Integer scale; 
		protected String mode;
		
		/**
		 * Constructor
		 */
		protected ParameterSegment() {
		}
		
		/**
		 * Constructor
		 * @param index parameter index
		 * @param content parameter detail content
		 */
		public ParameterSegment(int index, String content) {
			parse(content);
			name = String.valueOf(index);
		}
		
		/**
		 * parse
		 * @param content content
		 */
		protected void parse(String content) {
			name = content;
			int i = name.indexOf(':');
			if (i > 0) {
				type = name.substring(i + 1).toUpperCase().toUpperCase();
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
						throw new IllegalArgumentException("Illegal parameter '" + name + "': scale must be a parsable integer.", e);
					}
					type = type.substring(0, i);
				}

				// is this type check required?
				Integer sqlType = DaoTypes.getType(type);
				if (sqlType == null) {
					throw new IllegalArgumentException("Illegal parameter '" + name + "': unknown JDBC type [" + type + "].");
				}
			}
		}

		@SuppressWarnings("unchecked")
		public boolean translate(StringBuilder sql, JdbcSqlExecutor executor, Object parameter, List<JdbcSqlParameter> sqlParams) {
			Object paramValue = null;
			if (parameter != null) {
				BeanHandler bh = executor.getBeans().getBeanHandler(parameter.getClass());
				paramValue = bh.getBeanValue(parameter, name);
			}

			TypeAdapters typeAdapters = executor.getTypeAdapters();
			sqlParams.add(new JdbcSqlParameter(name, paramValue, type, scale, mode, typeAdapters));

			sql.append(" ?");
			return true;
		}

		protected void toString(StringBuilder sb) {
			sb.append(name);
			if (type != null) {
				sb.append(':').append(type);
				if (scale != null) {
					sb.append('.').append(scale);
				}
			}
			if (mode != null) {
				sb.append(':').append(mode);
			}
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("[P]: ");
			toString(sb);
			return sb.toString();
		}
	}

	protected static class VariableSegment extends ParameterSegment {
		/**
		 * Constructor
		 * @param parameter parameter
		 */
		public VariableSegment(String parameter) {
			parse(parameter);
		}
		
		@SuppressWarnings("unchecked")
		public boolean translate(StringBuilder sql, JdbcSqlExecutor executor, Object parameter, List<JdbcSqlParameter> sqlParams) {
			Object paramValue = null;
			if (parameter != null) {
				BeanHandler bh = executor.getBeans().getBeanHandler(parameter.getClass());
				paramValue = bh.getBeanValue(parameter, name);
			}

			TypeAdapters typeAdapters = executor.getTypeAdapters();
			if (paramValue != null && (DaoTypes.LIST.equals(type) || DaoTypes.SET.equals(type))) {
				if (paramValue.getClass().isArray()) {
					int len = Array.getLength(paramValue);
					if (len > 0) {
						sql.append(' ');
						for (int i = 0; i < len; i++) {
							Object v = Array.get(paramValue, i);
							sqlParams.add(new JdbcSqlParameter(name, v, type, scale, mode, typeAdapters));
							sql.append("?,");
						}
						sql.setLength(sql.length() - 1);
						return true;
					}
				}
				else if (paramValue instanceof Collection) {
					Collection c = (Collection)paramValue;
					if (!c.isEmpty()) {
						sql.append(' ');
						for (Object v : c) {
							sqlParams.add(new JdbcSqlParameter(name, v, type, scale, mode, typeAdapters));
							sql.append("?,");
						}
						sql.setLength(sql.length() - 1);
						return true;
					}
				}
			}

			sqlParams.add(new JdbcSqlParameter(name, paramValue, type, scale, mode, typeAdapters));
			sql.append(" ?");
			return true;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("[V]: ");
			toString(sb);
			return sb.toString();
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
		
		@SuppressWarnings("unchecked")
		public boolean translate(StringBuilder sql, JdbcSqlExecutor executor, Object parameter, List<JdbcSqlParameter> sqlParams) {
			Object rv = null;
			if (parameter != null) {
				BeanHandler bh = executor.getBeans().getBeanHandler(parameter.getClass());
				rv = bh.getBeanValue(parameter, propertyName);
			}

			if (rv != null) {
				sql.append(' ').append(rv.toString());
				return true;
			}
			
			return false;
		}

		@Override
		public String toString() {
			return "[R]: " + propertyName;
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

		public boolean translate(StringBuilder sql, JdbcSqlExecutor executor, Object parameter, List<JdbcSqlParameter> sqlParams) {
			sql.append(' ').append(comment);
			return false;
		}

		@Override
		public String toString() {
			return "[C]: " + comment;
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

		public boolean translate(StringBuilder sql, JdbcSqlExecutor executor, Object parameter, List<JdbcSqlParameter> sqlParams) {
			sql.append(' ').append(string);
			return true;
		}

		@Override
		public String toString() {
			return "[S]: " + string;
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
		private static final int PAREN = 5;
		
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
			else if ("(".equals(content) || ")".equals(content)) {
				kind = PAREN;
			}
			else {
				kind = NORMAL;
			}
		}

		public boolean translate(StringBuilder sql, JdbcSqlExecutor executor, Object parameter, List<JdbcSqlParameter> sqlParams) {
			if (kind == AND || kind == OR) {
				if (Strings.endsWith(sql, "(") || Strings.endsWithIgnoreCase(sql, " WHERE")) {
					return false;
				}
			}
			else if (kind == COMMA) {
				if (Strings.endsWithIgnoreCase(sql, " SET") || Strings.endsWithIgnoreCase(sql, " BY")) {
					return false;
				}
			}

			if (kind != PAREN) {
				sql.append(' ');
			}
			sql.append(content);
			return true;
		}

		@Override
		public String toString() {
			return "[W]: (" + kind + "): " + content;
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
		this(sql, false);
	}

	/**
	 * Constructor
	 * @param sql sql statement
	 * @param keepComments keep comments
	 */
	public SimpleSqlParser(String sql, boolean keepComments) {
		segments = new ArrayList<SqlSegment>();
		compile(sql, segments, keepComments);
	}

	/**
	 * parse sql by parameter
	 * @param executor sql executor
	 * @param parameter parameter
	 * @param sqlParams parameter list (output)
	 * @return jdbc sql  
	 */
	public String parse(JdbcSqlExecutor executor, Object parameter, List<JdbcSqlParameter> sqlParams) {
		StringBuilder sql = new StringBuilder();
		for (SqlSegment seg : segments) {
			seg.translate(sql, executor, parameter, sqlParams);
		}
		return Strings.strip(sql);
	}

	protected int skipComment(int start, int i, String sql, List<SqlSegment> segments, boolean keepComments) {
		if (i + 1 < sql.length() && sql.charAt(i + 1) == '*') {
			if (i > start) {
				split(sql.substring(start, i), segments);
			}
			String comment = null;
			if (i + 2 < sql.length()) {
				int j = sql.indexOf("*/", i + 2);
				if (j > 0) {
					comment = sql.substring(i, j + 2);
					i = j + 1;
				}
			}
			if (comment == null) {
				throw new IllegalArgumentException("Illegal sql statement (" + i + "): unexpected end of comment reached.");
			}
			
			if (keepComments) {
				segments.add(new SqlCommentSegment(comment));
			}
		}
		return i;
	}
	
	protected int skipString(int start, int i, String sql, List<SqlSegment> segments) {
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
				break;
			}
		}
		if (str == null) {
			throw new IllegalArgumentException("Illegal sql statement (" + start + "): unexpected end of string reached.");
		}
		segments.add(new SqlStringSegment(str));
		return i;
	}
	
	/**
	 * compile sql to segments
	 * 
	 * @param sql sql
	 * @param segments segments
	 */
	protected void compile(String sql, List<SqlSegment> segments, boolean keepComments) {
		int i;
		int start = 0;
		int qmark = 0;
		for (i = start; i < sql.length(); i++) {
			char c = sql.charAt(i);
			if (c == '/') {
				int j = skipComment(start, i, sql, segments, keepComments);
				if (j > i) {
					i = j;
					start = i + 1;
				}
			}
			else if (c == '\'') {
				i = skipString(start, i, sql, segments);
				start = i + 1;
			}
			else if (c == ':') {
				if (i > start) {
					split(sql.substring(start, i), segments);
				}

				start = i;
				i = skipJavaIdentifier(sql, i);

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
					segments.add(new VariableSegment(param));
				}
				start = i--;
			}
			else if (c == '?') {
				if (i > start) {
					split(sql.substring(start, i), segments);
				}

				start = i;
				i = skipJavaIdentifier(sql, i);

				String param = sql.substring(start, i);
				if (param.length() < 1) {
					throw new IllegalArgumentException("Illegal sql statement (" + start + "): unexpected character : reached.");
				}

				segments.add(new ParameterSegment(qmark++, param));
				start = i--;
			}
		}

		if (i > start) {
			split(sql.substring(start, i), segments);
		}
	}

	protected int skipJavaIdentifier(String sql, int i) {
		for (i++; i < sql.length(); i++) {
			char c2 = sql.charAt(i);
			if (!Character.isJavaIdentifierPart(c2)	&& c2 != ':' && c2 != '.') {
				break;
			}
		}
		return i;
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
			else if (Strings.contains(",()[]", c)) {
				if (i > start) {
					append(sql.substring(start, i), segments);
				}
				start = i + 1;
				segments.add(new SqlWordSegment(String.valueOf((char)c)));
			}
		}
		if (i > start) {
			append(sql.substring(start, i), segments);
		}
	}

}
