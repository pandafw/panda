package panda.tool.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.cli.CommandLine;

import panda.util.tool.AbstractCommandTool;
import panda.util.tool.AbstractFileTool;

/**
 * Base class for sql tool.
 */
public abstract class AbstractSqlTool extends AbstractFileTool {
	/**
	 * Base main class for code generator. Parse basic command line options.
	 */
	protected abstract static class Main extends AbstractFileTool.Main {
		@Override
		protected void addCommandLineOptions() throws Exception {
			super.addCommandLineOptions();
			
			addCommandLineOption("jd", "jdbcDriver", "JDBC Driver");

			addCommandLineOption("jc", "jdbcUrl", "JDBC Connection URL");

			addCommandLineOption("ju", "jdbcUsername", "JDBC User Name");
			
			addCommandLineOption("jp", "jdbcPassword", "JDBC User Password");
			
			addCommandLineOption("ac", "autoCommit", "Auto commit");
		}

		@Override
		protected void getCommandLineOptions(CommandLine cl) throws Exception {
			super.getCommandLineOptions(cl);
			
			if (cl.hasOption("jd")) {
				setParameter("jdbcDriver", cl.getOptionValue("jd").trim());
			}
			else {
				errorRequired(options, "jdbcDriver");
			}

			if (cl.hasOption("jc")) {
				setParameter("jdbcUrl", cl.getOptionValue("jc").trim());
			}
			else {
				errorRequired(options, "jdbcUrl");
			}

			if (cl.hasOption("ju")) {
				setParameter("jdbcUsername", cl.getOptionValue("ju").trim());
			}
			else {
				errorRequired(options, "jdbcUsername");
			}
			
			if (cl.hasOption("jp")) {
				setParameter("jdbcPassword", cl.getOptionValue("jp").trim());
			}

			if (cl.hasOption("ac")) {
				setParameter("autoCommit", true);
			}
		}
	}
	
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
	protected void finalProcess() throws Exception {
		super.finalProcess();
		disconnect();
	}
}
