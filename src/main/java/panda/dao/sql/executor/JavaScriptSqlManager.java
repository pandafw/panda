package panda.dao.sql.executor;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import panda.dao.sql.SqlExecutor;
import panda.dao.sql.SqlManager;
import panda.lang.DynamicClassLoader;


/**
 * @author yf.frank.wang@gmail.com
 */
public class JavaScriptSqlManager extends SqlManager {
	/**
	 * sequence
	 */
	private int sequence = 0;

	/**
	 * dynamicClassLoader
	 */
	private DynamicClassLoader dynamicClassLoader = new DynamicClassLoader();

	/**
	 * sqlParserCache
	 */
	private Map<String, Class<JdbcSqlParser>> sqlParserCache = new ConcurrentHashMap<String, Class<JdbcSqlParser>>();
	
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
		JavaScriptSqlExecutor se = new JavaScriptSqlExecutor(this);
		se.setConnection(connection);
		se.setResultSetType(resultSetType);
		se.setResultSetConcurrency(resultSetConcurrency);
		se.setResultSetHoldability(resultSetHoldability);
		return se;
	}


	/**
	 * @return the sequence
	 */
	public int getSequence() {
		return ++sequence;
	}


	/**
	 * @return the dynamicClassLoader
	 */
	public DynamicClassLoader getDynamicClassLoader() {
		return dynamicClassLoader;
	}

	/**
	 * @return the sqlParserCache
	 */
	public Map<String, Class<JdbcSqlParser>> getSqlParserCache() {
		return sqlParserCache;
	}
}
