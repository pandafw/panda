package panda.dao.sql.executor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import panda.bean.Beans;
import panda.dao.sql.SqlExecutor;

/**
 * @author yf.frank.wang@gmail.com
 */
public class ExtendSqlParser extends SimpleSqlParser {
	/**
	 * LogicalContainerSegment
	 */
	protected static class LogicalContainerSegment implements SqlSegment {
		protected List<SqlSegment> items;

		/**
		 * Constructor
		 * @param items items
		 */
		public LogicalContainerSegment(List<SqlSegment> items) {
			this.items = items;
		}

		/**
		 * translate 
		 * @param parameter parameter object
		 * @param strings strings
		 * @param sqlParams sql parameters
		 * @return translated string
		 */
		public String translate(SqlExecutor executor, Object parameter, List<String> strings, List<SqlParameter> sqlParams) {
			if (items == null || items.isEmpty()) {
				return null;
			}
			
			int stmSize = strings.size();
			int pmSize = sqlParams.size();
			
			boolean logic = false;
			StringBuilder sb = null;
			for (SqlSegment seg : items) {
				String sql = seg.translate(executor, parameter, strings, sqlParams);
				if (sql != null && sql.length() > 0) {
					if (seg instanceof LogicalExpressionSegment) {
						logic = true;
					}
					if (sb == null) {
						sb = new StringBuilder();
					}
					else {
						sb.append(' ');
					}
					sb.append(sql);
				}
			}
			if (logic) {
				return sb.toString();
			}
			else {
				for (int i = stmSize; i < strings.size(); i++) {
					strings.remove(i);
				}
				for (int i = pmSize; i < sqlParams.size(); i++) {
					sqlParams.remove(i);
				}
				return null;
			}
		}
	}

	/**
	 * LogicalExpressionSegment
	 */
	protected static class LogicalExpressionSegment extends LogicalContainerSegment {
		private final static int AT = 1;
		private final static int NOT = 2;

		private String propertyName;
		private int operator;
		
		/**
		 * Constructor
		 * @param propertyName property name
		 * @param items items
		 */
		public LogicalExpressionSegment(String propertyName, List<SqlSegment> items) {
			super(items);
			if (propertyName.charAt(0) == '!') {
				operator = NOT;
				this.propertyName = propertyName;
			}
			else {
				operator = AT;
				this.propertyName = propertyName.substring(1);
			}
		}

		/**
		 * Constructor
		 * @param propertyName property name
		 * @param items items
		 * @param operator operator
		 */
		public LogicalExpressionSegment(String propertyName, List<SqlSegment> items, int operator) {
			super(items);
			this.operator = operator;
			this.propertyName = propertyName;
		}

		private boolean isExpressionTrue(Object paramObject) {
			Object expValue = null;
			if (paramObject != null) {
				expValue = Beans.getBean(paramObject, propertyName);
			}
			
			if (operator == AT) {
				return !(expValue == null
						|| (expValue instanceof Boolean && Boolean.FALSE.equals(expValue))
						|| (expValue.getClass().isArray() && Array.getLength(expValue) < 1)
						|| (expValue instanceof Collection && ((Collection)expValue).isEmpty()));
			}
			else if (operator == NOT) {
				return (expValue == null
						|| (expValue instanceof Boolean && Boolean.FALSE.equals(expValue))
						|| (expValue.getClass().isArray() && Array.getLength(expValue) < 1)
						|| (expValue instanceof Collection && ((Collection)expValue).isEmpty()));
			}
			else {
				return false;
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
			if (items == null || items.isEmpty()) {
				return null;
			}
			
			if (isExpressionTrue(parameter)) {
				StringBuilder sb = null;
				for (SqlSegment seg : items) {
					String sql = seg.translate(executor, parameter, strings, sqlParams);
					if (sql != null && sql.length() > 0) {
						if (sb == null) {
							sb = new StringBuilder();
						}
						else {
							sb.append(' ');
						}
						sb.append(sql);
					}
				}
				if (sb != null) {
					return sb.toString();
				}
			}
			return null;
		}
	}

	/**
	 * Constructor
	 * @param sql sql statement
	 */
	public ExtendSqlParser(String sql) {
		super(sql);
	}

	/**
	 * compile sql to segments
	 * 
	 * @param sql sql
	 * @param segments segments
	 */
	@Override
	protected void compile(String sql, List<SqlSegment> segments) {
		compile(sql, segments, 0, false);
	}

	/**
	 * compile sql to segments
	 * 
	 * @param sql sql
	 * @param segments segments
	 * @param start start position
	 * @param close close ']' need or not
	 */
	private int compile(String sql, List<SqlSegment> statements, int start, boolean close) {
		int i;
		for (i = start; i < sql.length(); i++) {
			char c = sql.charAt(i);
			if (c == '/') {
				if (i + 1 < sql.length() && sql.charAt(i + 1) == '*') {
					if (i > start) {
						split(sql.substring(start, i), statements);
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
					
					statements.add(new SqlCommentSegment(comment));
				}
			}
			else if (c == '\'') {
				if (i > start) {
					split(sql.substring(start, i), statements);
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
				statements.add(new SqlStringSegment(str));
			}
			else if (c == '@') {
				if (i > start) {
					split(sql.substring(start, i), statements);
				}
				start = i++;
				if (i >= sql.length()) {
					throw new IllegalArgumentException("Illegal sql statement (" + start + "): unexpected character @ reached.");
				}
				
				int notStart = 0;
				if (sql.charAt(i) == '!') {
					notStart = i++;
					if (i >= sql.length()) {
						throw new IllegalArgumentException("Illegal sql statement (" + start + "): unexpected character @! reached.");
					}
				}
				if (sql.charAt(i) == '[') {
					if (notStart > 0) {
						throw new IllegalArgumentException("Illegal sql statement (" + start + "): unexpected string @![...] reached.");
					}

					List<SqlSegment> items = new ArrayList<SqlSegment>();
					i = compile(sql, items, i + 1, true);
					LogicalContainerSegment stm = new LogicalContainerSegment(items);
					statements.add(stm);
					start = i + 1;
				}
				else {
					String expression = null;
					int j;
					for (j = i; j < sql.length(); j++) {
						char c2 = sql.charAt(j);
						if (c2 == '[') {
							expression = sql.substring(i, j);
							break;
						}
						else if (Character.isWhitespace(c2)) {
							expression = sql.substring(i, j);
							boolean open = false;
							for (; j < sql.length(); j++) {
								char c3 = sql.charAt(j);
								if ('[' == c3) {
									open = true;
									break;
								}
								else if (!Character.isWhitespace(c3)) {
									throw new IllegalArgumentException("Illegal sql statement (" + start + "): unexpected character " + c3 + " reached.");
								}
							}
							if (!open) {
								throw new IllegalArgumentException("Illegal sql statement (" + start + "): unexpected open bracket [ reached.");
							}
							break;
						}
					}
					if (expression == null) {
						throw new IllegalArgumentException("Illegal sql statement (" + start + "): unexpected character " + c + " reached.");
					}

					List<SqlSegment> items = new ArrayList<SqlSegment>();
					i = compile(sql, items, j + 1, true);

					LogicalExpressionSegment stm = new LogicalExpressionSegment(expression, items,
							notStart > 0 ? LogicalExpressionSegment.NOT
									: LogicalExpressionSegment.AT);
					statements.add(stm);
					start = i + 1;
				}
			}
			else if (c == ':') {
				if (i > start) {
					split(sql.substring(start, i), statements);
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
					statements.add(new ReplacerSegment(param));
				}
				else {
					statements.add(new ParameterSegment(param));
				}
				start = i--;
			}
			else if (c == ']') {
				if (close) {
					if (i > start) {
						split(sql.substring(start, i), statements);
					}
					return i;
				}
			}
		}

		if (close) {
			throw new IllegalArgumentException("Illegal sql statement (" + i + "): unexpected end of statement reached. Unclosed [...] statement. ']' is required.");
		}
		if (i > start) {
			split(sql.substring(start, i), statements);
		}
		return i;
	}
}
