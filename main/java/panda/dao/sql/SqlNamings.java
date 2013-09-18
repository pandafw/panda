package panda.dao.sql;

import panda.lang.Strings;


/**
 * @author yf.frank.wang@gmail.com
 */
public class SqlNamings {
	protected static String columnLabel2FieldName(String columnLabel) {
		String[] ss = Strings.split(columnLabel, '_');
		StringBuilder sb = null;
		for (String s : ss) {
			if (s.length() > 0) {
				if (sb == null) {
					sb = new StringBuilder();
					sb.append(s.toLowerCase());
				}
				else {
					sb.append(Character.toUpperCase(s.charAt(0)));
					if (s.length() > 1) {
						sb.append(s.substring(1).toLowerCase());
					}
				}
			}
		}
		if (sb == null || sb.length() < 1) {
			throw new IllegalArgumentException("Illegal column label: " + columnLabel);
		}
		return sb.toString();
	}
	
	/**
	 * javaName2ColumnLabel
	 * @param javaName java style name
	 * @return sql column label
	 */
	public static String javaName2ColumnLabel(String javaName) {
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
		StringBuilder sb = new StringBuilder();

		String javaName = Strings.replace(columnLabel, "_0_", ".");
		
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

		if (sb.length() < 1) {
			throw new IllegalArgumentException("Illegal column label: " + columnLabel);
		}
		return sb.toString();
	}

	/**
	 * convert a java class name to table name
	 * e.g.: 'HelloWorld' -> 'Hello_World'
	 * @param javaName java style name
	 * @return table name
	 */
	public static String javaName2TableName(String javaName) {
		return Strings.uncamelWord(javaName, '_');
	}
}
