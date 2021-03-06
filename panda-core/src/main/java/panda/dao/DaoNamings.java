package panda.dao;

import panda.lang.Strings;
import panda.lang.Texts;


/**
 */
public class DaoNamings {
	private static boolean isAllUpperCase(final CharSequence cs) {
		if (Strings.isEmpty(cs)) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			char c = cs.charAt(i);
			if (Character.isLetter(c) && !Character.isUpperCase(c)) {
				return false;
			}
		}
		return true;
	}

	private static boolean isJavaName(final CharSequence cs) {
		if (Strings.isEmpty(cs)) {
			return false;
		}

		boolean au = true;
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			char c = cs.charAt(i);
			if (c == '_') {
				return false;
			}
			if (au && Character.isLetter(c)) {
				if (!Character.isUpperCase(c)) {
					au = false;
				}
			}
		}
		return !au;
	}

	/**
	 * javaName2ColumnLabel
	 * @param javaName java style name
	 * @return sql column label
	 */
	public static String javaName2ColumnLabel(String javaName) {
		if (isAllUpperCase(javaName)) {
			return Strings.replace(javaName, ".", "_0_");
		}
		
		StringBuilder sb = new StringBuilder();
		int len = javaName.length();
		for (int i = 0; i < len; i++) {
			char c = javaName.charAt(i);
			if (c == '.') {
				sb.append("_0_");
			}
			else if (Character.isUpperCase(c)) {
				sb.append('_');
				sb.append(c);
			}
			else {
				sb.append(Character.toUpperCase(c));
			}
		}
		
		if (sb.length() < 1) {
			throw new IllegalArgumentException("Illegal java name: " + javaName);
		}
		return sb.toString();
	}

	/**
	 * columnLabel2JavaName
	 * @param columnLabel sql column label
	 * @return java style name
	 */
	public static String columnLabel2JavaName(String columnLabel) {
		if (Strings.isEmpty(columnLabel)) {
			return Strings.EMPTY;
		}
		
		String javaName = Strings.replace(columnLabel, "_0_", ".");
		if (isJavaName(javaName)) {
			return javaName;
		}
		
		StringBuilder sb = new StringBuilder();
		boolean toUpper = false;
		int len = javaName.length();
		for (int i = 0; i < len; i++) {
			char c = javaName.charAt(i);
			if (c == '.') {
				sb.append(c);
			}
			else if (c == '_') {
				toUpper = true;
			}
			else if (toUpper) {
				sb.append(Character.toUpperCase(c));
				toUpper = false;
			}
			else {
				sb.append(Character.toLowerCase(c));
			}
		}
		return sb.toString();
	}

	/**
	 * convert a java class name to table name
	 * e.g.: 'HelloWorld' -> 'hello_world'
	 * @param javaName java style name
	 * @return table name
	 */
	public static String javaName2TableName(String javaName) {
		return Texts.uncamelWord(javaName, '_');
	}

	/**
	 * convert a java property name to column name
	 * e.g.: 'helloWorld' -> 'hello_world'
	 * @param javaName java style name
	 * @return column name
	 */
	public static String javaName2ColumnName(String javaName) {
		return Texts.uncamelWord(javaName, '_');
	}
}
