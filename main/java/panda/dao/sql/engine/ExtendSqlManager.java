package panda.dao.sql.engine;

import java.sql.Connection;

import panda.dao.sql.SqlExecutor;

/**
 * @author yf.frank.wang@gmail.com
 */
public class ExtendSqlManager extends SimpleSqlManager {
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
	public SqlExecutor getExecutor(Connection connection, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) {
		ExtendSqlExecutor se = new ExtendSqlExecutor(this);
		se.setConnection(connection);
		se.setResultSetType(resultSetType);
		se.setResultSetConcurrency(resultSetConcurrency);
		se.setResultSetHoldability(resultSetHoldability);
		return se;
	}
}
