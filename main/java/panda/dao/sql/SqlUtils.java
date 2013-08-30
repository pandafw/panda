package panda.dao.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import panda.lang.StringEscapes;

/**
 * utility class for sql
 * 
 * @author yf.frank.wang@gmail.coms
 */
public class SqlUtils {

	/**
	 * escapeSql
	 * @param str string
	 * @return escaped string
	 */
	public static String escapeSql(String str) {
		return StringEscapes.escapeSql(str);
	}

	/**
	 * escapeSqlLike
	 * @param str string
	 * @return escaped string
	 */
	public static String escapeSqlLike(String str) {
		return StringEscapes.escapeSqlLike(str);
	}


	/**
	 * like string 
	 * @param str string
	 * @return %str%
	 */
	public static String stringLike(String str) {
		return '%' + escapeSqlLike(str) + '%';
	}
	
	/**
	 * starts like string 
	 * @param str string
	 * @return str%
	 */
	public static String startsLike(String str) {
		return escapeSqlLike(str) + '%';
	}
	
	/**
	 * ends like string 
	 * @param str string
	 * @return %str
	 */
	public static String endsLike(String str) {
		return '%' + escapeSqlLike(str);
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
