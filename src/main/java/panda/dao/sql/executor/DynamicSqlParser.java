package panda.dao.sql.executor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import panda.bean.Beans;

/**
 * @author yf.frank.wang@gmail.com
 */
public class DynamicSqlParser extends SimpleSqlParser {
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

		public boolean translate(StringBuilder sql, JdbcSqlExecutor executor, Object parameter, List<JdbcSqlParameter> sqlParams) {
			if (items == null || items.isEmpty()) {
				return false;
			}
			
			int size = sql.length();

			boolean logic = false;
			for (SqlSegment seg : items) {
				if (seg.translate(sql, executor, parameter, sqlParams)) {
					if (!logic) {
						logic = (seg instanceof LogicalContainerSegment);
					}
				}
			}

			if (logic) {
				return true;
			}

			sql.setLength(size);
			return false;
		}

		@Override
		public String toString() {
			return "[L]: " + items.toString();
		}
	}

	/**
	 * LogicalExpressionSegment
	 */
	protected static class LogicalExpressionSegment extends LogicalContainerSegment {
		private final static char AT = '@';
		private final static char NOT = '!';

		private String propertyName;
		private char operator;
		
		/**
		 * Constructor
		 * @param propertyName property name
		 * @param items items
		 */
		public LogicalExpressionSegment(String propertyName, List<SqlSegment> items) {
			super(items);
			operator = propertyName.charAt(0);
			this.propertyName = propertyName.substring(1);
		}

		/**
		 * Constructor
		 * @param propertyName property name
		 * @param items items
		 * @param operator operator
		 */
		public LogicalExpressionSegment(String propertyName, List<SqlSegment> items, char operator) {
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
		
		public boolean translate(StringBuilder sql, JdbcSqlExecutor executor, Object parameter, List<JdbcSqlParameter> sqlParams) {
			if (items == null || items.isEmpty() || !isExpressionTrue(parameter)) {
				return false;
			}
			
			boolean r = false;
			for (SqlSegment seg : items) {
				if (seg.translate(sql, executor, parameter, sqlParams)) {
					r = true;
				}
			}
			return r;
		}

		@Override
		public String toString() {
			return "[E]: " + operator + propertyName;
		}
	}

	/**
	 * Constructor
	 * @param sql sql statement
	 */
	public DynamicSqlParser(String sql) {
		super(sql);
	}

	/**
	 * Constructor
	 * @param sql sql statement
	 */
	public DynamicSqlParser(String sql, boolean keepComments) {
		super(sql, keepComments);
	}

	/**
	 * compile sql to segments
	 * 
	 * @param sql sql
	 * @param segments segments
	 */
	@Override
	protected void compile(String sql, List<SqlSegment> segments, boolean keepComments) {
		compile(sql, segments, keepComments, 0, false);
	}

	/**
	 * compile sql to segments
	 * 
	 * @param sql sql
	 * @param segments segments
	 * @param start start position
	 * @param close close ']' need or not
	 */
	private int compile(String sql, List<SqlSegment> segments, boolean keepComments, int start, boolean close) {
		int i;
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
			else if (c == '@') {
				if (i > start) {
					split(sql.substring(start, i), segments);
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
					i = compile(sql, items, keepComments, i + 1, true);
					LogicalContainerSegment stm = new LogicalContainerSegment(items);
					segments.add(stm);
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
					i = compile(sql, items, keepComments, j + 1, true);

					LogicalExpressionSegment stm = new LogicalExpressionSegment(expression, items,
							notStart > 0 ? LogicalExpressionSegment.NOT
									: LogicalExpressionSegment.AT);
					segments.add(stm);
					start = i + 1;
				}
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
			else if (c == ']') {
				if (close) {
					if (i > start) {
						split(sql.substring(start, i), segments);
					}
					return i;
				}
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

		if (close) {
			throw new IllegalArgumentException("Illegal sql statement (" + i + "): unexpected end of statement reached. Unclosed [...] statement. ']' is required.");
		}
		if (i > start) {
			split(sql.substring(start, i), segments);
		}
		return i;
	}
}
