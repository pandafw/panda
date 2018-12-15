package panda.dao.sql.dbcp;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * SimplePooledConnection
 */
public class SimplePooledConnection extends ProxyConnection {

	private SimpleDataSource dataSource;

	private long checkoutTimestamp;

	private long createdTimestamp;

	private long lastUsedTimestamp;

	/**
	 * Constructor for SimplePooledConnection that uses the Connection and SimpleDataSource
	 * passed in
	 * 
	 * @param connection - the connection that is to be presented as a pooled connection
	 * @param dataSource - the dataSource that the connection is from
	 */
	public SimplePooledConnection(Connection connection, SimpleDataSource dataSource) {
		super(connection);
		this.dataSource = dataSource;
		this.createdTimestamp = System.currentTimeMillis();
		this.lastUsedTimestamp = System.currentTimeMillis();
	}

	/**
	 * @see java.sql.Connection#close()
	 */
	@Override
	public void close() throws SQLException {
		dataSource.pushConnection(this);
	}

	/**
	 * Method to see if the connection is usable, run ping query if pingQuery is setted
	 * 
	 * @return True if the connection is usable
	 */
	public boolean testValid() {
		return isValid() && dataSource.pingConnection(this);
	}

	/**
	 * Getter for the time that the connection was created
	 * 
	 * @return The creation timestamp
	 */
	public long getCreatedTimestamp() {
		return createdTimestamp;
	}

	/**
	 * Setter for the time that the connection was created
	 * 
	 * @param createdTimestamp - the timestamp
	 */
	public void setCreatedTimestamp(long createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	/**
	 * Getter for the time that the connection was last used
	 * 
	 * @return - the timestamp
	 */
	public long getLastUsedTimestamp() {
		return lastUsedTimestamp;
	}

	/**
	 * Setter for the time that the connection was last used
	 * 
	 * @param lastUsedTimestamp - the timestamp
	 */
	public void setLastUsedTimestamp(long lastUsedTimestamp) {
		this.lastUsedTimestamp = lastUsedTimestamp;
	}

	/**
	 * Getter for the time since this connection was last used
	 * 
	 * @return - the time since the last use
	 */
	public long getTimeElapsedSinceLastUse() {
		return System.currentTimeMillis() - lastUsedTimestamp;
	}

	/**
	 * Getter for the age of the connection
	 * 
	 * @return the age
	 */
	public long getAge() {
		return System.currentTimeMillis() - createdTimestamp;
	}

	/**
	 * Getter for the timestamp that this connection was checked out
	 * 
	 * @return the timestamp
	 */
	public long getCheckoutTimestamp() {
		return checkoutTimestamp;
	}

	/**
	 * Setter for the timestamp that this connection was checked out
	 * 
	 * @param timestamp the timestamp
	 */
	public void setCheckoutTimestamp(long timestamp) {
		this.checkoutTimestamp = timestamp;
	}

	/**
	 * Getter for the time that this connection has been checked out
	 * 
	 * @return the time
	 */
	public long getCheckoutTime() {
		return System.currentTimeMillis() - checkoutTimestamp;
	}
}
