package panda.dao.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.dao.DatabaseMeta;
import panda.dao.sql.executor.JdbcSqlExecutor;
import panda.dao.sql.executor.JdbcSqlManager;
import panda.dao.sql.expert.SqlExpert;
import panda.dao.sql.expert.SqlExpertConfig;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;

/**
 * @author yf.frank.wang@gmail.com
 */
@IocBean(type=DaoClient.class)
public class SqlDaoClient extends DaoClient {
	private static Log log = Logs.getLog(SqlDaoClient.class);
	
	private static SqlExpertConfig sqlExpertConfig = new SqlExpertConfig();
	
	protected DataSource dataSource;
	protected SqlExpert sqlExpert;
	protected SqlManager sqlManager = new JdbcSqlManager();
	
	/**
	 * Constructor
	 */
	public SqlDaoClient() {
	}
	
	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 * @throws SQLException if a sql error occurs
	 */
	@IocInject
	public void setDataSource(DataSource dataSource) throws SQLException {
		this.dataSource = dataSource;
		this.sqlExpert = getExpert(dataSource);
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
	 * @return the jdbcSqlExecutor
	 */
	public JdbcSqlExecutor getJdbcSqlExecutor() {
		return (JdbcSqlExecutor)sqlManager.getExecutor();
	}
	
	/**
	 * get SqlExpert by DataSource
	 * 
	 * @param dataSource data source
	 * @return SqlExpert
	 * @throws SQLException if a sql error occurs
	 */
	public SqlExpert getExpert(DataSource dataSource) throws SQLException {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			DatabaseMetaData meta = conn.getMetaData();
			String pnm = meta.getDatabaseProductName();
			String ver = meta.getDatabaseProductVersion();
			return getExpert(pnm, ver);
		}
		finally {
			Sqls.safeClose(conn);
		}
	}

	/**
	 * get SqlExpert by productName and version
	 * 
	 * @param productName database product name
	 * @param version database version
	 * @return SqlExpert
	 * @see java.sql.Connection#getMetaData()
	 * @see java.sql.DatabaseMetaData#getDatabaseProductName()
	 */
	public SqlExpert getExpert(String productName, String version) {
		String dbName = (productName + " " + version).toLowerCase();
		log.info("Get SqlExpert for " + dbName);

		SqlExpert se = sqlExpertConfig.matchExpert(dbName);
		if (null == se) {
			throw Exceptions.makeThrow("Can not support database '%s %s'", productName, version);
		}
		se.setOptions(sqlExpertConfig.getOptions());
		se.setClient(this);
		se.setCastors(getCastors());
		se.setDatabaseMeta(productName, version);
		
		return se;
	}

	/**
	 * @return datebase meta
	 */
	@Override
	public DatabaseMeta getDatabaseMeta() {
		return sqlExpert.getDatabaseMeta();
	}

	/**
	 * @return a new dao instance
	 */
	public Dao getDao() {
		return new SqlDao(this);
	}
}
