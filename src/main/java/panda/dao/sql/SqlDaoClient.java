package panda.dao.sql;

import javax.sql.DataSource;

import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.dao.sql.executor.ExtendSqlManager;
import panda.dao.sql.expert.SqlExpert;

/**
 * @author yf.frank.wang@gmail.com
 */
public class SqlDaoClient extends DaoClient {
	protected DataSource dataSource;
	protected SqlExpert sqlExpert;
	protected SqlManager sqlManager = new ExtendSqlManager();
	
	/**
	 */
	public SqlDaoClient(String name) {
		this(name, null);
	}

	/**
	 * @param name dao client name
	 * @param dataSource the dataSource to set
	 */
	public SqlDaoClient(String name, DataSource dataSource) {
		super(name);
		this.dataSource = dataSource;
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @return the sqlExpert
	 */
	public SqlExpert getSqlExpert() {
		return sqlExpert;
	}

	/**
	 * @param sqlExpert the sqlExpert to set
	 */
	public void setSqlExpert(SqlExpert sqlExpert) {
		this.sqlExpert = sqlExpert;
	}

	/**
	 * @return the sqlManager
	 */
	public SqlManager getSqlManager() {
		return sqlManager;
	}

	/**
	 * @param sqlManager the sqlManager to set
	 */
	public void setSqlManager(SqlManager sqlManager) {
		this.sqlManager = sqlManager;
	}

	/**
	 * @return a new dao instance
	 */
	public Dao getDao() {
		return new SqlDao(this);
	}
}
