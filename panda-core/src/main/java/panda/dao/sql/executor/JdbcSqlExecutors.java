package panda.dao.sql.executor;

import java.sql.Connection;

import panda.dao.sql.SqlExecutor;
import panda.dao.sql.SqlExecutors;


/**
 */
public class JdbcSqlExecutors extends SqlExecutors {
	private static final JdbcSqlExecutors i = new JdbcSqlExecutors();

	/**
	 * @return static instance
	 */
	public static JdbcSqlExecutors i() {
		return i;
	}
	
	/**
	 * Constructor
	 * 
	 * @param connection connection
	 * @param resultSetType one of the following ResultSet constants: ResultSet.TYPE_FORWARD_ONLY,
	 *            ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
	 * @param resultSetConcurrency one of the following ResultSet constants:
	 *            ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
	 * @param resultSetHoldability one of the following ResultSet constants:
	 *            ResultSet.HOLD_CURSORS_OVER_COMMIT or ResultSet.CLOSE_CURSORS_AT_COMMIT
	 */
	@Override
	public SqlExecutor getExecutor(Connection connection, int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
		JdbcSqlExecutor se = new JdbcSqlExecutor(this);
		se.setConnection(connection);
		se.setResultSetType(resultSetType);
		se.setResultSetConcurrency(resultSetConcurrency);
		se.setResultSetHoldability(resultSetHoldability);
		return se;
	}

	//-------------------------------------------------------------------
	/**
	 * @return the JdbcSqlExecutor
	 */
	public static JdbcSqlExecutor getJdbcExecutor() {
		return (JdbcSqlExecutor)i.getExecutor();
	}

	/**
	 * @param connection connection
	 * @return the JdbcSqlExecutor
	 */
	public static JdbcSqlExecutor getJdbcExecutor(Connection connection) {
		return (JdbcSqlExecutor)i.getExecutor(connection);
	}

	/**
	 * @param connection connection
	 * @param resultSetType one of the following ResultSet constants: ResultSet.TYPE_FORWARD_ONLY,
	 *            ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
	 * @return the JdbcSqlExecutor
	 */
	public static JdbcSqlExecutor getJdbcExecutor(Connection connection, int resultSetType) {
		return (JdbcSqlExecutor)i.getExecutor(connection, resultSetType);
	}

	/**
	 * @param connection connection
	 * @param resultSetType one of the following ResultSet constants: ResultSet.TYPE_FORWARD_ONLY,
	 *            ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
	 * @param resultSetConcurrency one of the following ResultSet constants:
	 *            ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
	 * @return the JdbcSqlExecutor
	 */
	public static JdbcSqlExecutor getJdbcExecutor(Connection connection, int resultSetType, int resultSetConcurrency) {
		return (JdbcSqlExecutor)i.getExecutor(connection, resultSetType, resultSetConcurrency);
	}

	/**
	 * @param connection connection
	 * @param resultSetType one of the following ResultSet constants: ResultSet.TYPE_FORWARD_ONLY,
	 *            ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
	 * @param resultSetConcurrency one of the following ResultSet constants:
	 *            ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
	 * @param resultSetHoldability one of the following ResultSet constants:
	 *            ResultSet.HOLD_CURSORS_OVER_COMMIT or ResultSet.CLOSE_CURSORS_AT_COMMIT
	 * @return the JdbcSqlExecutor
	 */
	public static JdbcSqlExecutor getJdbcExecutor(Connection connection, int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
		return (JdbcSqlExecutor)i.getExecutor(connection, resultSetType, resultSetConcurrency, resultSetHoldability);
	}
	
}
