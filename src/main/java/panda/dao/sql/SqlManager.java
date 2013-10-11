package panda.dao.sql;

import java.sql.Connection;
import java.sql.ResultSet;

import panda.bean.Beans;
import panda.dao.sql.adapter.TypeAdapters;
import panda.dao.sql.executor.DynamicSqlManager;


/**
 * @author yf.frank.wang@gmail.com
 */
public abstract class SqlManager {
	private static SqlManager me = new DynamicSqlManager();

	/**
	 * @return the me
	 */
	public static SqlManager me() {
		return me;
	}

	/**
	 * @return the me
	 */
	public static SqlManager getMe() {
		return me;
	}

	/**
	 * @param me the me to set
	 */
	public static void setMe(SqlManager me) {
		SqlManager.me = me;
	}
	
	/**
	 * Beans
	 */
	protected Beans beans = Beans.me();

	/**
	 * typeAdapters
	 */
	protected TypeAdapters typeAdapters = TypeAdapters.me();
	
	/**
	 * @return the beans
	 */
	public Beans getBeans() {
		return beans;
	}

	/**
	 * @param beans the beans to set
	 */
	public void setBeans(Beans beans) {
		this.beans = beans;
	}

	/**
	 * @return the typeAdapters
	 */
	public TypeAdapters getTypeAdapters() {
		return typeAdapters;
	}

	/**
	 * @param typeAdapters the typeAdapters to set
	 */
	public void setTypeAdapters(TypeAdapters typeAdapters) {
		this.typeAdapters = typeAdapters;
	}

	/**
	 * Constructor
	 */
	public SqlExecutor getExecutor() {
		return getExecutor(null, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	}

	/**
	 * Constructor
	 * @param connection connection
	 */
	public SqlExecutor getExecutor(Connection connection) {
		return getExecutor(connection, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	}

	/**
	 * Constructor
	 * 
	 * @param connection connection
	 * @param resultSetType one of the following ResultSet constants: ResultSet.TYPE_FORWARD_ONLY,
	 *            ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
	 */
	public SqlExecutor getExecutor(Connection connection, int resultSetType) {
		return getExecutor(connection, resultSetType, ResultSet.CONCUR_READ_ONLY);
	}

	/**
	 * Constructor
	 * 
	 * @param connection connection
	 * @param resultSetType one of the following ResultSet constants: ResultSet.TYPE_FORWARD_ONLY,
	 *            ResultSet.TYPE_SCROLL_INSENSITIVE, or ResultSet.TYPE_SCROLL_SENSITIVE
	 * @param resultSetConcurrency one of the following ResultSet constants:
	 *            ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
	 */
	public SqlExecutor getExecutor(Connection connection, int resultSetType, int resultSetConcurrency) {
		return getExecutor(connection, resultSetType, resultSetConcurrency, 0);
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
	public abstract SqlExecutor getExecutor(Connection connection, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability);
}
