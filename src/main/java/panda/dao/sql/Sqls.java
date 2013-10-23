package panda.dao.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import panda.lang.Strings;

/**
 * utility class for sql
 * 
 * @author yf.frank.wang@gmail.coms
 */
public class Sqls {
	/**
	 * escape sql Like string
	 * 
	 * @param str string
	 * @return escaped string
	 */
	public static String escapeLike(String str) {
		final char esc = '~';

		StringBuilder result = new StringBuilder();

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == esc) {
				result.append(esc);
				result.append(esc);
				continue;
			}
			if (c == '%' || c == '_') {
				result.append(esc);
				result.append(c);
			}
			else {
				result.append(c);
			}
		}

		return result.toString();
	}

	/**
	 * <p>
	 * Escapes the characters in a <code>String</code> to be suitable to pass to an SQL query.
	 * </p>
	 * <p>
	 * For example,
	 * 
	 * <pre>
	 * statement.executeQuery(&quot;SELECT * FROM MOVIES WHERE TITLE='&quot; + StringEscapeUtils.escapeSql(&quot;McHale's Navy&quot;) + &quot;'&quot;);
	 * </pre>
	 * 
	 * </p>
	 * <p>
	 * At present, this method only turns single-quotes into doubled single-quotes (
	 * <code>"McHale's Navy"</code> => <code>"McHale''s Navy"</code>). It does not handle the cases
	 * of percent (%) or underscore (_) for use in LIKE clauses.
	 * </p>
	 * see http://www.jguru.com/faq/view.jsp?EID=8881
	 * 
	 * @param str the string to escape, may be null
	 * @return a new String, escaped for SQL, <code>null</code> if null string input
	 */
	public static String escapeString(String str) {
		return Strings.replace(str, "'", "''");
	}

	/**
	 * like string 
	 * @param str string
	 * @return %str%
	 */
	public static String stringLike(String str) {
		return '%' + escapeLike(str) + '%';
	}
	
	/**
	 * starts like string 
	 * @param str string
	 * @return str%
	 */
	public static String startsLike(String str) {
		return escapeLike(str) + '%';
	}
	
	/**
	 * ends like string 
	 * @param str string
	 * @return %str
	 */
	public static String endsLike(String str) {
		return '%' + escapeLike(str);
	}
	
	private static Pattern illegalFieldNamePattern = Pattern.compile(".*[^a-zA-Z_0-9\\.].*");

	/**
	 * isIllegalFieldName
	 * @param fieldName fieldName
	 * @return true if fieldName is illegal
	 */
	public static boolean isIllegalFieldName(String fieldName) {
		Matcher m = illegalFieldNamePattern.matcher(fieldName);
		return m.matches();
	}

	public static void safeClose(ResultSet rs, Statement stat) {
		safeClose(rs);
		safeClose(stat);
	}

	/**
	 * close ResultSet with out throw exception
	 * @param resultSet result set
	 */
	public static void safeClose(ResultSet resultSet) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
		}
		catch (SQLException e) {
		}
	}

	/**
	 * close ResultSet with out throw exception
	 * @param resultSet result set
	 */
	public static void safeClose(SqlResultSet resultSet) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
		}
		catch (SQLException e) {
		}
	}
	
	/**
	 * close statement with out throw exception
	 * @param statement statement
	 */
	public static void safeClose(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		}
		catch (SQLException e) {
		}
	}
	
	/**
	 * close connection with out throw exception
	 * @param connection connection
	 */
	public static void safeClose(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		}
		catch (SQLException e) {
		}
	}
	
	/**
	 * rollback with out throw exception
	 * @param connection connection
	 */
	public static void safeRollback(Connection connection) {
		try {
			if (connection != null) {
				connection.rollback();
			}
		}
		catch (SQLException e) {
		}
	}

	/**
	 * skip result
	 * @param resultSet result set
	 * @param skip            The number of results to ignore.
	 * @throws SQLException if a SQL exception occurs
	 */
	public static void skipResultSet(ResultSet resultSet, int skip) throws SQLException {
		if (skip > 0) {
			if (resultSet.getType() != ResultSet.TYPE_FORWARD_ONLY) {
				resultSet.absolute(skip);
			}
			else {
				for (; skip > 0; skip--) {
					if (!resultSet.next()) {
						return;
					}
				}
			}
		}
	}

	/**
	 * skip result
	 * @param resultSet result set
	 * @param skip            The number of results to ignore.
	 * @throws SQLException if a SQL exception occurs
	 */
	public static void skipResultSet(SqlResultSet resultSet, int skip) throws SQLException {
		if (skip > 0) {
			if (resultSet.getType() != ResultSet.TYPE_FORWARD_ONLY) {
				resultSet.absolute(skip);
			}
			else {
				for (; skip > 0; skip--) {
					if (!resultSet.next()) {
						return;
					}
				}
			}
		}
	}

	/**
	 * @param type SQL type from java.sql.Types
	 * @return true if the type is a binary type
	 * @see Types
	 */
	public static boolean isBinaryType(int type) {
		return (type == Types.BINARY 
				|| type == Types.BLOB
				|| type == Types.LONGVARBINARY
				|| type == Types.VARBINARY);
	}
}
