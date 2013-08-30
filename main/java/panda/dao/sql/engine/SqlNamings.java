package panda.dao.sql.engine;

import panda.lang.Strings;


/**
 * @author yf.frank.wang@gmail.com
 */
public class SqlNamings {
	protected static String fieldName2ColumnLabel(String fieldName) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fieldName.length(); i++) {
			char c = fieldName.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append('_');
				sb.append(c);
			}
			else {
				sb.append(Character.toUpperCase(c));
			}
		}
		return sb.toString();
	}
	
	protected static String columnLabel2FieldName(String columnLabel) {
		String[] ss = columnLabel.split("\\_");
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
		String[] ss = Strings.split(javaName, '.');
		StringBuilder sb = null;
		for (String s : ss) {
			if (s.length() > 0) {
				if (sb == null) {
					sb = new StringBuilder();
				}
				else {
					sb.append("_0_");
				}
				sb.append(fieldName2ColumnLabel(s));
			}
		}
		if (sb == null || sb.length() < 1) {
			throw new IllegalArgumentException("Illegal column alias: " + javaName);
		}
		return sb.toString();
	}

	/**
	 * columnLabel2JavaName
	 * @param columnLabel sql column label
	 * @return java style name
	 */
	public static String columnLabel2JavaName(String columnLabel) {
		String javaName = columnLabel.replaceAll("_0_", ".");
		String[] ss = javaName.split("\\.");
		StringBuilder sb = null;
		for (String s : ss) {
			if (s.length() > 0) {
				if (sb == null) {
					sb = new StringBuilder();
				}
				else {
					sb.append('.');
				}
				sb.append(columnLabel2FieldName(s));
			}
		}
		if (sb == null || sb.length() < 1) {
			throw new IllegalArgumentException("Illegal column label: " + columnLabel);
		}
		return sb.toString();
	}

}
