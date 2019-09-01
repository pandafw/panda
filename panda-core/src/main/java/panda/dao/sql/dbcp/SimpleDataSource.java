package panda.dao.sql.dbcp;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import panda.dao.sql.Sqls;
import panda.lang.Strings;
import panda.lang.Threads;
import panda.log.Log;
import panda.log.Logs;


/**
 * This is a simple, synchronous, thread-safe database connection pool.
 * <p/>
 * 
 * ---- REQUIRED PROPERTIES ----
 * <ul>
 * <li>jdbc.driver</li>
 * <li>jdbc.url</li>
 * <li>jdbc.username</li>
 * <li>jdbc.password</li>
 * <li>jdbc.autoCommit - default: false</li>
 * <li>jdbc.readOnly - default: false</li>
 * </ul>
 * <p/>
 * 
 * ---- POOLING PROPERTIES ----
 * <ul>
 * <li>pool.maxActive - default: 100</li>
 * <li>pool.maxIdle - default: 20</li>
 * <li>pool.maxWait - default: 1000</li>
 * <li>pool.maxWaitMillis - default: 20000 (milliseconds)</li>
 * <li>pool.maxCheckoutMillis - default: 1 week (milliseconds)</li>
 * <li>pool.pingQuery</li>
 * <li>pool.pingTimeout - default: 1 (seconds)</li>
 * <li>pool.pingOlderThan - default: 0 (milliseconds)</li>
 * <li>pool.pingNotUsedFor - default: 600000 (milliseconds)</li>
 * </ul>
 * <p/>
 * 
 */
public class SimpleDataSource extends AbstractDataSource implements Closeable {
	private static Log log = Logs.getLog(SimpleDataSource.class);

	//--------------------------------------------------------------------------------------
	// PROPERTY FIELDS FOR POOL CONFIGURATION
	//--------------------------------------------------------------------------------------
	private PoolConfig pool = new PoolConfig();

	//--------------------------------------------------------------------------------------
	// FIELDS LOCKED BY POOL_LOCK
	//--------------------------------------------------------------------------------------
	private final Object POOL_LOCK = new Object();

	private List<SimplePooledConnection> idles = new ArrayList<SimplePooledConnection>();

	private List<SimplePooledConnection> actives = new ArrayList<SimplePooledConnection>();

	private long requestCount;

	private long accumulatedRequestTime;

	private long accumulatedCheckoutTime;

	private long claimedOverdueConnectionCount;

	private long accumulatedCheckoutTimeOfOverdueConnections;

	private long accumulatedWaitTime;

	private long waiting;
	
	private long hadToWaitCount;

	private long badConnectionCount;

	/**
	 * Constructor
	 */
	public SimpleDataSource() {
		super();
	}

	/**
	 * @return the pool
	 */
	public PoolConfig getPool() {
		return pool;
	}

	/**
	 * @param pool the pool to set
	 */
	public void setPool(PoolConfig pool) {
		this.pool = pool;
	}

	/**
	 * Getter for the number of connection requests made
	 * 
	 * @return The number of connection requests made
	 */
	public long getRequestCount() {
		return requestCount;
	}

	/**
	 * Getter for the average time required to get a connection to the database
	 * 
	 * @return The average time
	 */
	public long getAverageRequestTime() {
		return requestCount == 0 ? 0 : accumulatedRequestTime / requestCount;
	}

	/**
	 * Getter for the average time spent waiting for connections that were in use
	 * 
	 * @return The average time
	 */
	public long getAverageWaitTime() {
		return hadToWaitCount == 0 ? 0 : accumulatedWaitTime / hadToWaitCount;
	}

	/**
	 * Getter for the number of requests that had to wait for connections that were in use
	 * 
	 * @return The number of requests that had to wait
	 */
	public long getHadToWaitCount() {
		return hadToWaitCount;
	}

	/**
	 * Getter for the number of invalid connections that were found in the pool
	 * 
	 * @return The number of invalid connections
	 */
	public long getBadConnectionCount() {
		return badConnectionCount;
	}

	/**
	 * Getter for the number of connections that were claimed before they were returned
	 * 
	 * @return The number of connections
	 */
	public long getClaimedOverdueConnectionCount() {
		return claimedOverdueConnectionCount;
	}

	/**
	 * Getter for the average age of overdue connections
	 * 
	 * @return The average age
	 */
	public long getAverageOverdueCheckoutTime() {
		return claimedOverdueConnectionCount == 0 ? 0 : accumulatedCheckoutTimeOfOverdueConnections
				/ claimedOverdueConnectionCount;
	}

	/**
	 * Getter for the average age of a connection checkout
	 * 
	 * @return The average age
	 */
	public long getAverageCheckoutTime() {
		return requestCount == 0 ? 0 : accumulatedCheckoutTime / requestCount;
	}

	/**
	 * @return The count of active connections
	 */
	public int getActives() {
		return actives.size();
	}

	/**
	 * Returns the status of the connection pool
	 * 
	 * @return The status
	 */
	@Override
	public String getStatus() {
		StringBuilder sb = new StringBuilder();

		sb.append(super.getStatus());
		sb.append("\n pool.maxActive                  ").append(pool.getMaxActive());
		sb.append("\n pool.maxIdle                    ").append(pool.getMaxIdle());
		sb.append("\n pool.maxWait                    ").append(pool.getMaxWait());
		sb.append("\n pool.maxWaitMillis              ").append(pool.getMaxWaitMillis());
		sb.append("\n pool.maxCheckoutMillis          ").append(pool.getMaxCheckoutMillis());
		sb.append("\n pool.pingQuery                  ").append(pool.getPingQuery());
		sb.append("\n pool.pingTimeout                ").append(pool.getPingTimeout());
		sb.append("\n pool.pingOlderThan              ").append(pool.getPingOlderThan());
		sb.append("\n pool.pingNotUsedFor             ").append(pool.getPingNotUsedFor());
		sb.append("\n --------------------------------------------------------------");
		sb.append("\n activeConnections               ").append(actives.size());
		sb.append("\n idleConnections                 ").append(idles.size());
		sb.append("\n requestCount                    ").append(getRequestCount());
		sb.append("\n averageRequestTime              ").append(getAverageRequestTime());
		sb.append("\n averageCheckoutTime             ").append(getAverageCheckoutTime());
		sb.append("\n claimedOverdue                  ").append(getClaimedOverdueConnectionCount());
		sb.append("\n averageOverdueCheckoutTime      ").append(getAverageOverdueCheckoutTime());
		sb.append("\n hadToWait                       ").append(getHadToWaitCount());
		sb.append("\n averageWaitTime                 ").append(getAverageWaitTime());
		sb.append("\n badConnectionCount              ").append(getBadConnectionCount());
		sb.append("\n===============================================================");

		return sb.toString();
	}

	/**
	 * close connection and remove from pool
	 * @param pcons connection list
	 */
	private void close(List<SimplePooledConnection> pcons) {
		for (int i = pcons.size() - 1; i >= 0; i--) {
			try {
				SimplePooledConnection pcon = pcons.remove(i);
				pcon.invalidate();

				Connection rcon = pcon.getRealConnection();
				try {
					if (!rcon.getAutoCommit() && !rcon.isReadOnly()) {
						rcon.rollback();
					}
				}
				catch (SQLException e) {
					// ignore
				}
				finally {
					Sqls.safeClose(rcon);
				}
			}
			catch (Exception e) {
				// ignore
			}
		}
	}

	/**
	 * Closes all of the connections in the pool
	 */
	@Override
	public void close() {
		synchronized (POOL_LOCK) {
			if (actives.size() > 0 || idles.size() > 0) {
				close(actives);
				close(idles);

				if (log.isDebugEnabled()) {
					log.debug("SimpleDataSource forcefully closed/removed all connections.");
				}
			}
		}
	}

	protected void pushConnection(SimplePooledConnection pcon) throws SQLException {
		if (log.isDebugEnabled()) {
			log.debug("Return connection (" + pcon.getRealHashCode() + ") to the pool.");
		}

		synchronized (POOL_LOCK) {
			actives.remove(pcon);
			if (!pcon.isValid()) {
				if (log.isDebugEnabled()) {
					log.debug("A bad connection (" + pcon.getRealHashCode()
							+ ") attempted to return to the pool, discarding connection.");
				}
				badConnectionCount++;
				return;
			}
			
			accumulatedCheckoutTime += pcon.getCheckoutTime();

			boolean valid = true;
			Connection rcon = pcon.getRealConnection();
			try {
				if (!rcon.getAutoCommit() && !rcon.isReadOnly()) {
					rcon.rollback();
				}
			}
			catch (SQLException e) {
				if (log.isDebugEnabled()) {
					log.debug("Failed to rollback returned connection " + pcon.getRealHashCode() + ": " + e.getMessage());
				}
				valid = false;
			}
			
			if (valid && (waiting > 0 || idles.size() < pool.maxIdle)) {
				SimplePooledConnection ncon = new SimplePooledConnection(rcon, this);
				ncon.setCreatedTimestamp(pcon.getCreatedTimestamp());
				ncon.setLastUsedTimestamp(pcon.getLastUsedTimestamp());
				idles.add(ncon);

				pcon.invalidate();
				if (log.isDebugEnabled()) {
					log.debug("Returned connection " + ncon.getRealHashCode() + " to pool.");
				}
				POOL_LOCK.notifyAll();
			}
			else {
				Sqls.safeClose(rcon);

				pcon.invalidate();

				if (log.isDebugEnabled()) {
					log.debug("Closed connection " + pcon.getRealHashCode() + ".");
				}
			}
		}
	}

	@Override
	protected Connection popConnection() throws SQLException {
		long start = System.currentTimeMillis();
		long waitMillis = pool.maxWaitMillis;
		
		synchronized (POOL_LOCK) {
			if (waiting >= pool.maxWait) {
				throw new SQLException("Failed to get a connection due to too many wait (" + waiting + ").");
			}
			while (true) {
				// get a idle connection
				SimplePooledConnection pcon = getIdleConnection();
				if (pcon != null) {
					checkoutConnection(pcon, start);
					return pcon;
				}

				// get a overdue connection
				pcon = getOverdueConnection();
				if (pcon != null) {
					checkoutConnection(pcon, start);
					return pcon;
				}

				if (actives.size() < pool.maxActive) {
					// create a new connection
					pcon = createConnection();
					checkoutConnection(pcon, start);
					return pcon;
				}

				// waited and timeout??
				if (waitMillis <= 0) {
					if (log.isDebugEnabled()) {
						log.debug("SimpleDataSource: Failed to get a connection due to wait timeout.");
					}
					throw new SQLException("Failed to get a connection due to wait timeout.");
				}

				// Must wait
				if (log.isDebugEnabled()) {
					log.debug("Waiting as long as " + waitMillis + " milliseconds for connection.");
				}

				
				hadToWaitCount++;

				waiting++;
				long waitStart = System.currentTimeMillis();
				Threads.safeWait(POOL_LOCK, waitMillis);
				long elapsed = System.currentTimeMillis() - waitStart;
				waiting--;

				accumulatedWaitTime += elapsed;
				waitMillis -= elapsed;
			}
		}
	}

	private SimplePooledConnection createConnection() throws SQLException {
		SimplePooledConnection pcon = new SimplePooledConnection(DriverManager.getConnection(jdbc.url, jdbc.prop), this);
		if (log.isDebugEnabled()) {
			log.debug("Created connection " + pcon.getRealHashCode() + ".");
		}
		return pcon;
	}

	private SimplePooledConnection getIdleConnection() {
		int bad = 0;
		while (idles.size() > 0) {
			// Pool has available connection
			SimplePooledConnection pcon = idles.remove(0);
			if (log.isDebugEnabled()) {
				log.debug("Checked out connection " + pcon.getRealHashCode() + " from pool.");
			}
			if (pcon.testValid()) {
				return pcon;
			}

			bad++;
			badConnectionCount++;
			if (log.isDebugEnabled()) {
				log.debug("A bad idle connection (" + pcon.getRealHashCode()
						+ ") was returned from the pool, getting another connection " + bad);
			}
			continue;
		}
		return null;
	}

	private SimplePooledConnection getOverdueConnection() {
		int bad = 0;
		while (actives.size() > 0) {
			SimplePooledConnection oldest = (SimplePooledConnection)actives.get(0);
			long longestCheckoutTime = oldest.getCheckoutTime();
			if (longestCheckoutTime > pool.maxCheckoutMillis) {
				// Can claim overdue connection
				claimedOverdueConnectionCount++;
				accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
				accumulatedCheckoutTime += longestCheckoutTime;
				actives.remove(oldest);
				
				SimplePooledConnection pcon = new SimplePooledConnection(oldest.getRealConnection(), this);
				oldest.invalidate();
				if (log.isDebugEnabled()) {
					log.debug("Claimed overdue connection " + pcon.getRealHashCode() + ".");
				}
				if (pcon.testValid()) {
					return pcon;
				}
	
				bad++;
				badConnectionCount++;
				if (log.isDebugEnabled()) {
					log.debug("A bad overdue connection (" + pcon.getRealHashCode()
							+ ") was returned from the pool, getting another connection " + bad);
				}
				continue;
			}
			break;
		}
		return null;
	}

	private void checkoutConnection(SimplePooledConnection pcon, long start) throws SQLException {
		if (pcon.isReadOnly() != jdbc.readOnly) {
			pcon.setReadOnly(jdbc.readOnly);
		}
		if (pcon.getAutoCommit() != jdbc.autoCommit) {
			pcon.setAutoCommit(jdbc.autoCommit);
		}
		pcon.setCheckoutTimestamp(System.currentTimeMillis());
		pcon.setLastUsedTimestamp(System.currentTimeMillis());
		actives.add(pcon);
		requestCount++;
		accumulatedRequestTime += System.currentTimeMillis() - start;
	}

	/**
	 * Method to check to see if a connection is still usable
	 * 
	 * @param pcon - the connection to check
	 * @return True if the connection is still usable
	 */
	protected boolean pingConnection(SimplePooledConnection pcon) {
		Connection rcon = pcon.getRealConnection();
		try {
			if (rcon.isClosed()) {
				return false;
			}
		}
		catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("Connection " + pcon.getRealHashCode() + " is BAD: " + e.getMessage());
			}
			return false;
		}

		if (Strings.isEmpty(pool.pingQuery)
				|| ((pool.pingOlderThan <= 0 || pcon.getAge() <= pool.pingOlderThan)
					&& (pool.pingNotUsedFor <= 0 || pcon.getTimeElapsedSinceLastUse() <= pool.pingNotUsedFor))) {
			return true;
		}

		if (log.isDebugEnabled()) {
			log.debug("Testing connection " + pcon.getRealHashCode() + " ...");
		}

		try {
			Statement statement = rcon.createStatement();
			statement.setQueryTimeout(pool.pingTimeout);
			ResultSet rs = statement.executeQuery(pool.pingQuery);
			rs.close();
			statement.close();

			if (log.isDebugEnabled()) {
				log.debug("Connection " + pcon.getRealHashCode() + " is GOOD!");
			}
			return true;
		}
		catch (Exception e) {
			if (log.isWarnEnabled()) {
				log.warn("Execution of ping query '" + pool.pingQuery + "' failed: " + e.getMessage());
			}
			
			Sqls.safeClose(rcon);

			if (log.isDebugEnabled()) {
				log.debug("Connection " + pcon.getRealHashCode() + " is BAD: " + e.getMessage());
			}
			
			return false;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		close();
	}
}
