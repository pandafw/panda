package panda.dao.sql.executor;

import java.sql.Connection;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import panda.dao.sql.SqlExecutor;
import panda.dao.sql.SqlManager;


/**
 * @author yf.frank.wang@gmail.com
 */
public class SimpleSqlManager extends SqlManager {
	/**
	 * lock for cache
	 */
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	/**
	 * sqlParserCache
	 */
	private Map<String, SqlParser> sqlParserCache = new WeakHashMap<String, SqlParser>();
	
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
		SimpleSqlExecutor se = new SimpleSqlExecutor(this);
		se.setConnection(connection);
		se.setResultSetType(resultSetType);
		se.setResultSetConcurrency(resultSetConcurrency);
		se.setResultSetHoldability(resultSetHoldability);
		return se;
	}

	/**
	 * @return the sqlParserCache
	 */
	public SqlParser getSqlParser(String key) {
		if (sqlParserCache == null) {
			return null;
		}
		
		try {
			lock.readLock().lock();
			return sqlParserCache.get(key);
		}
		finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * put sqlParser to cache
	 * @param key key
	 * @param parser parser
	 */
	public void putSqlParser(String key, SqlParser parser) {
		if (sqlParserCache == null) {
			return;
		}

		try {
			lock.writeLock().lock();
			sqlParserCache.put(key, parser);
		}
		finally {
			lock.writeLock().unlock();
		}
	}
}
