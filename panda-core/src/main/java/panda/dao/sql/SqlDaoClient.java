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
import panda.log.Log;
import panda.log.Logs;

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
			String dpn = meta.getDatabaseProductName();
			String dpv = meta.getDatabaseProductVersion();
			return getExpert(dpn, dpv);
		}
		finally {
			Sqls.safeClose(conn);
		}
	}

	/**
	 * get SqlExpert by productName and version
	 * 
	 * @param name database product name
	 * @param version database version
	 * @return SqlExpert
	 * @see java.sql.Connection#getMetaData()
	 * @see java.sql.DatabaseMetaData#getDatabaseProductName()
	 */
	public SqlExpert getExpert(String name, String version) {
		String db = (name + " " + version).toLowerCase();
		log.info("Get SqlExpert for " + db);

		SqlExpert se = sqlExpertConfig.matchExpert(db);
		if (se == null) {
			throw new RuntimeException("Failed to find SqlExpert for database '" + db + "'");
		}
		se.setOptions(sqlExpertConfig.getOptions());
		se.setClient(this);
		se.setCastors(getCastors());
		se.setDatabaseMeta(name, version);
		
		return se;
	}

	/**
	 * @return database meta
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
