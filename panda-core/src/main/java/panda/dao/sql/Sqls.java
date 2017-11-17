package panda.dao.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.NamingException;
import javax.sql.DataSource;

import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.util.JndiLookup;

/**
 * utility class for sql
 */
public abstract class Sqls {
	private static final Log log = Logs.getLog(Sqls.class);

	/**
	 * lookup jndi data source
	 * 
	 * @param jndi jndi string
	 * @return the DataSource
	 * @throws NamingException if a naming exception is encountered
	 */
	public static DataSource lookupJndiDataSource(String jndi) throws NamingException {
		return (DataSource)JndiLookup.lookup(jndi);
	}

	/**
	 * escape sql Like string
	 * 
	 * @param str string
	 * @param esc escape char
	 * @return escaped string
	 */
	public static String escapeLike(String str, char esc) {
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
	 * @param esc escape char
	 * @return %str%
	 */
	public static String stringLike(String str, char esc) {
		return '%' + escapeLike(str, esc) + '%';
	}
	
	/**
	 * starts like string 
	 * @param str string
	 * @param esc escape char
	 * @return str%
	 */
	public static String startsLike(String str, char esc) {
		return escapeLike(str, esc) + '%';
	}
	
	/**
	 * ends like string 
	 * @param str string
	 * @param esc escape char
	 * @return %str
	 */
	public static String endsLike(String str, char esc) {
		return '%' + escapeLike(str, esc);
	}

	private static void toRegex(StringBuilder sb, char c) {
		switch (c) {
		case '%':
			sb.append(".*");
			break;
		case '_':
			sb.append(".");
			break;
		default:
			sb.append(c);
			break;
		}
	}

	/**
	 * convert like string to regular expression
	 * @param str like string
	 * @param esc escape char
	 * @return regular expression
	 */
	public static String like2regex(String str, char esc) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == esc) {
				if (i < str.length() - 1) {
					i++;
					c = str.charAt(i);
					sb.append(c);
					continue;
				}
			}
			toRegex(sb, c);
		}
		return sb.toString();
	}
	
	/**
	 * convert like string to regular expression
	 * @param str like string
	 * @return regular expression
	 */
	public static String like2regex(String str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			toRegex(sb, c);
		}
		return sb.toString();
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
			if (resultSet != null && !resultSet.isClosed()) {
				resultSet.close();
			}
		}
		catch (SQLException e) {
			log.warn("Failed to close ResultSet", e);
		}
	}

	/**
	 * close ResultSet with out throw exception
	 * @param resultSet result set
	 */
	public static void safeClose(SqlResultSet<?> resultSet) {
		try {
			if (resultSet != null && !resultSet.isClosed()) {
				resultSet.close();
			}
		}
		catch (SQLException e) {
			log.warn("Failed to close SqlResultSet", e);
		}
	}
	
	/**
	 * close statement with out throw exception
	 * @param statement statement
	 */
	public static void safeClose(Statement statement) {
		try {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
		}
		catch (SQLException e) {
			log.warn("Failed to close Statement", e);
		}
	}
	
	/**
	 * close connection with out throw exception
	 * @param connection connection
	 */
	public static void safeClose(Connection connection) {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		}
		catch (SQLException e) {
			log.warn("Failed to close Connection", e);
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
			log.warn("Failed to rollback", e);
		}
	}

	/**
	 * skip result
	 * @param resultSet result set
	 * @param skip            The number of results to ignore.
	 * @throws SQLException if a SQL exception occurs
	 */
	public static void skipResultSet(ResultSet resultSet, long skip) throws SQLException {
		skipResultSet(resultSet, (int)skip);
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
	public static void skipResultSet(SqlResultSet<?> resultSet, long skip) throws SQLException {
		skipResultSet(resultSet, (int)skip);
	}

	/**
	 * skip result
	 * @param resultSet result set
	 * @param skip            The number of results to ignore.
	 * @throws SQLException if a SQL exception occurs
	 */
	public static void skipResultSet(SqlResultSet<?> resultSet, int skip) throws SQLException {
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
	 * 0-9a-zA-Z_
	 * @param c character
	 * @return true if the specified character is [0-9a-zA-Z_]
	 */
	public static boolean isValidColumnChar(int c) {
		return c == '_' || (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
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

	/**
	 * @param type SQL type from java.sql.Types
	 * @return true if the type is a number type
	 * @see Types
	 */
	public static boolean isCharType(int type) {
		return (type == Types.CHAR 
				|| type == Types.VARCHAR
				|| type == Types.LONGNVARCHAR);
	}

	/**
	 * @param type SQL type from java.sql.Types
	 * @return true if the type is a number type
	 * @see Types
	 */
	public static boolean isNumberType(int type) {
		return (type == Types.TINYINT 
				|| type == Types.INTEGER
				|| type == Types.DECIMAL
				|| type == Types.REAL
				|| type == Types.FLOAT
				|| type == Types.DOUBLE);
	}
}
