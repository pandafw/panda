package panda.tool.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import panda.args.Option;
import panda.tool.AbstractCommandTool;
import panda.tool.AbstractFileTool;

/**
 * Base class for sql tool.
 */
public abstract class AbstractSqlTool extends AbstractFileTool {
	/**
	 * Constructor
	 */
	public AbstractSqlTool() {
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected String jdbcDriver;
	protected String jdbcUrl;
	protected String jdbcUsername;
	protected String jdbcPassword = "";
	protected boolean autoCommit = false;
	protected Connection connection;

	/**
	 * @return the jdbcDriver
	 */
	public String getJdbcDriver() {
		return jdbcDriver;
	}

	/**
	 * @param jdbcDriver the jdbcDriver to set
	 */
	@Option(opt='d', option="driver", arg="DRIVER", required=true, usage="JDBC Driver")
	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	/**
	 * @return the jdbcUrl
	 */
	public String getJdbcUrl() {
		return jdbcUrl;
	}

	/**
	 * @param jdbcUrl the jdbcUrl to set
	 */
	@Option(opt='c', option="conn", arg="URL", required=true, usage="JDBC Connection URL")
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	/**
	 * @return the jdbcUsername
	 */
	public String getJdbcUsername() {
		return jdbcUsername;
	}

	/**
	 * @param jdbcUsername the jdbcUsername to set
	 */
	@Option(opt='u', option="user", arg="NAME", required=true, usage="JDBC User Name")
	public void setJdbcUsername(String jdbcUsername) {
		this.jdbcUsername = jdbcUsername;
	}

	/**
	 * @return the jdbcPassword
	 */
	public String getJdbcPassword() {
		return jdbcPassword;
	}

	/**
	 * @param jdbcPassword the jdbcPassword to set
	 */
	@Option(opt='p', option="pass", arg="PASS", usage="JDBC User Password")
	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}

	/**
	 * @return the autoCommit
	 */
	public boolean isAutoCommit() {
		return autoCommit;
	}

	/**
	 * @param autoCommit the autoCommit to set
	 */
	@Option(opt='a', option="autocommit", usage="Auto commit")
	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	protected void connect() throws SQLException, ClassNotFoundException {
		if (connection == null) {
			Class.forName(jdbcDriver);
			connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
			connection.setAutoCommit(autoCommit);
		}
	}
	
	protected void disconnect() {
		try {
			if (jdbcUrl.startsWith("jdbc:hsqldb:file:")) {
				connection.createStatement().execute("SHUTDOWN");
			}
			if (connection != null) {
				connection.close();
				connection = null;
			}
		}
		catch (Throwable e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	protected void commit() {
		try {
			connection.commit();
		}
		catch (Throwable e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	protected void rollback() {
		try {
			connection.rollback();
		}
		catch (Throwable e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	protected void truncateTable(String tableName) throws Exception {
		try {
			String sql = "TRUNCATE TABLE " + tableName;
			Statement st = connection.createStatement();
			st.execute(sql);
			st.close();
		}
		catch (SQLException ex) {
			String sql = "DELETE FROM " + tableName;
			Statement st = connection.createStatement();
			st.execute(sql);
			st.close();
		}
		connection.commit();
	}
	
	@Override
	protected void checkParameters() throws Exception {
		super.checkParameters();
		
		AbstractCommandTool.checkRequired(jdbcDriver, "jdbcDriver");
		AbstractCommandTool.checkRequired(jdbcUrl, "jdbcUrl");
		AbstractCommandTool.checkRequired(jdbcUsername, "jdbcUsername");
	}

	@Override
	protected void printParameters() {
		super.printParameters();
		
		println4("jdbcDriver = " + getJdbcDriver());
		println4("jdbcUrl = " + getJdbcUrl());
		println4("jdbcUsername = " + getJdbcUsername());
		println4("jdbcPassword = " + getJdbcPassword());
		println4("autoCommit = " + isAutoCommit());
	}

	@Override
	protected void beforeProcess() throws Exception {
		super.beforeProcess();
		connect();
	}

	@Override
	protected void finalProcess() {
		super.finalProcess();
		disconnect();
	}
}
