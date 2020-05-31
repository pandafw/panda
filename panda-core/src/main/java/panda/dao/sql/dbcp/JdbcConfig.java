package panda.dao.sql.dbcp;

import java.sql.Connection;
import java.util.Properties;

import panda.dao.sql.Sqls;
import panda.lang.Asserts;

public class JdbcConfig {
	protected String driver;
	protected String url;
	protected boolean autoCommit = false;
	protected boolean readOnly = false;
	protected int transactionIsolation = Connection.TRANSACTION_NONE;

	protected Properties prop = new Properties();

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		Asserts.notEmpty(driver, "The drive property is empty.");

		try {
			Class.forName(driver);
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException("Failed to initialize jdbc drive: " + driver, e);
		}
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return prop.getProperty("user");
	}

	public void setUsername(String username) {
		prop.put("user", username);
	}

	public String getPassword() {
		return prop.getProperty("password");
	}

	public void setPassword(String password) {
		prop.put("password", password);
	}

	public boolean isAutoCommit() {
		return autoCommit;
	}

	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	/**
	 * @return the transactionLevel
	 */
	public String getTransactionLevel() {
		return Sqls.printTransactionLevel(transactionIsolation);
	}

	/**
	 * @param transactionLevel the transactionLevel to set
	 */
	public void setTransactionLevel(String transactionLevel) {
		this.transactionIsolation = Sqls.parseTransactionLevel(transactionLevel);
	}

	/**
	 * @return the transactionIsolation
	 */
	public int getTransactionIsolation() {
		return transactionIsolation;
	}

	/**
	 * @param transactionIsolation the transactionIsolation to set
	 */
	public void setTransactionIsolation(int transactionIsolation) {
		this.transactionIsolation = transactionIsolation;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		Asserts.notNull(prop, "The prop property is null.");
		this.prop = prop;
	}
}

