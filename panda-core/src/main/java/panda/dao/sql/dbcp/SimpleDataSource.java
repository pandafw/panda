package panda.dao.sql.dbcp;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import panda.dao.sql.Sqls;
import panda.lang.Strings;
import panda.lang.Threads;
import panda.log.Log;
import panda.log.Logs;


/**
 * This is a simple, synchronous, thread-safe database connection pool.
 * <p/>
 * 
 * ---- JDBC PROPERTIES ----
 * <ul>
 * <li>jdbc.driver</li>
 * <li>jdbc.url</li>
 * <li>jdbc.username</li>
 * <li>jdbc.password</li>
 * <li>jdbc.autoCommit - default: false</li>
 * <li>jdbc.readOnly - default: false</li>
 * <li>jdbc.transactionLevel - default: none</li>
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

	private LinkedList<SimplePooledConnection> idles = new LinkedList<SimplePooledConnection>();

	private LinkedList<SimplePooledConnection> actives = new LinkedList<SimplePooledConnection>();

	private PoolStatus status = new PoolStatus();
	
	private long waiting;

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
	 * @return the status
	 */
	public PoolStatus getStatus() {
		return status;
	}

	/**
	 * @return The count of active connections
	 */
	public int getActives() {
		return actives.size();
	}

	/**
	 * @return the status of the connection pool
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(super.toString());
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
		sb.append("\n --------------------------------------------------------------");
		sb.append(status.toString());
		sb.append("\n===============================================================");

		return sb.toString();
	}

	/**
	 * close connection and remove from pool
	 * @param pcons connection list
	 */
	private void close(LinkedList<SimplePooledConnection> pcons) {
		while (pcons.size() > 0) {
			SimplePooledConnection pcon = pcons.pop();
			pcon.invalidate();

			Connection rcon = pcon.getRealConnection();
			try {
				if (!rcon.getAutoCommit() && !rcon.isReadOnly()) {
					rcon.rollback();
				}
			}
			catch (Throwable e) {
				// ignore
			}
			finally {
				Sqls.safeClose(rcon);
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

		if (!pcon.isValid()) {
			if (log.isDebugEnabled()) {
				log.debug("A bad connection (" + pcon.getRealHashCode()
						+ ") attempted to return to the pool, discarding connection.");
			}
			synchronized (POOL_LOCK) {
				actives.remove(pcon);
				status.incBadConnectionCount();
			}
			return;
		}

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
			closeConnection(pcon);
			synchronized (POOL_LOCK) {
				actives.remove(pcon);
				status.addCheckoutTime(pcon.getCheckoutTime());
			}
			return;
		}

		synchronized (POOL_LOCK) {
			actives.remove(pcon);

			status.addCheckoutTime(pcon.getCheckoutTime());

			if (waiting > 0 || idles.size() < pool.maxIdle) {
				SimplePooledConnection ncon = new SimplePooledConnection(rcon, this);
				ncon.setCreatedTimestamp(pcon.getCreatedTimestamp());
				ncon.setLastUsedTimestamp(pcon.getLastUsedTimestamp());
				idles.add(ncon);

				pcon.invalidate();
				if (log.isDebugEnabled()) {
					log.debug("Returned connection " + ncon.getRealHashCode() + " to pool.");
				}
				if (waiting > 0) {
					POOL_LOCK.notify();
				}
				return;
			}
		}
		
		// no waiting or idle pool is full
		closeConnection(pcon);
	}

	protected void closeConnection(SimplePooledConnection pcon) {
		Sqls.safeClose(pcon.getRealConnection());

		pcon.invalidate();

		if (log.isDebugEnabled()) {
			log.debug("Closed connection " + pcon.getRealHashCode() + ".");
		}
	}
	
	@Override
	protected Connection popConnection() throws SQLException {
		int bad = 0;
		long start = System.currentTimeMillis();
		long waitMillis = pool.maxWaitMillis;

		SimplePooledConnection pcon = null;
		while (true) {
			synchronized (POOL_LOCK) {
				if (pcon != null) {
					// remove bad connection
					actives.remove(pcon);
				}
				
				if (waiting >= pool.maxWait) {
					status.incTooManyWait();
					throw new SQLException("Failed to get a connection due to too many wait (" + waiting + ").");
				}
	
				while (true) {
					// get a idle connection
					pcon = getIdleConnection();
					if (pcon != null) {
						checkoutConnection(pcon, start);
						if (waiting > 0) {
							POOL_LOCK.notify();
						}
						break;
					}
	
					// get a overdue connection
					pcon = getOverdueConnection();
					if (pcon != null) {
						checkoutConnection(pcon, start);
						if (waiting > 0) {
							POOL_LOCK.notify();
						}
						break;
					}
	
					if (actives.size() < pool.maxActive) {
						// create a new connection
						pcon = createConnection();
						checkoutConnection(pcon, start);
						if (waiting > 0) {
							POOL_LOCK.notify();
						}
						return pcon;
					}
	
					// waited and timeout??
					if (waitMillis <= 0) {
						status.incWaitTimeout();
						if (log.isDebugEnabled()) {
							log.debug("Failed to get a connection due to wait timeout.");
						}
						throw new SQLException("Failed to get a connection due to wait timeout.");
					}
	
					// Must wait
					if (log.isDebugEnabled()) {
						log.debug("Waiting as long as " + waitMillis + " milliseconds for connection.");
					}
	
					status.incHadToWaitCount();
	
					waiting++;
					long waitStart = System.currentTimeMillis();
					Threads.safeWait(POOL_LOCK, waitMillis);
					long elapsed = System.currentTimeMillis() - waitStart;
					waiting--;
	
					status.addWaitTime(elapsed);
					waitMillis -= elapsed;
				}
			}

			if (pingConnection(pcon)) {
				try {
					setConnectionProperties(pcon.getRealConnection());
					return pcon;
				}
				catch (Throwable e) {
					log.error("Failed to set connection " + pcon.getRealHashCode() + " properties.", e);
					Sqls.safeClose(pcon.getRealConnection());
				}
			}

			bad++;
			status.incBadConnectionCount();
			if (log.isDebugEnabled()) {
				log.debug("A bad connection (" + pcon.getRealHashCode()
						+ ") was returned from the pool, getting another connection " + bad);
			}
		}
	}

	private SimplePooledConnection createConnection() throws SQLException {
		SimplePooledConnection pcon = new SimplePooledConnection(DriverManager.getConnection(jdbc.url, jdbc.prop), this);
		setConnectionProperties(pcon.getRealConnection());
		if (log.isDebugEnabled()) {
			log.debug("Created connection " + pcon.getRealHashCode() + ".");
		}
		return pcon;
	}

	private SimplePooledConnection getIdleConnection() {
		int bad = 0;
		while (idles.size() > 0) {
			// Pool has available connection
			SimplePooledConnection pcon = idles.pop();
			if (log.isDebugEnabled()) {
				log.debug("Checked out connection " + pcon.getRealHashCode() + " from pool.");
			}
			if (pcon.isValid()) {
				return pcon;
			}

			bad++;
			status.incBadConnectionCount();
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
			SimplePooledConnection oldest = actives.peek();
			long longestCheckoutTime = oldest.getCheckoutTime();
			if (longestCheckoutTime > pool.maxCheckoutMillis) {
				// Can claim overdue connection
				status.claimOverdueConnection(longestCheckoutTime);
				actives.pop();

				// reuse the overdue connection
				SimplePooledConnection pcon = new SimplePooledConnection(oldest.getRealConnection(), this);
				oldest.invalidate();
				if (log.isDebugEnabled()) {
					log.debug("Claimed overdue connection " + pcon.getRealHashCode() + ".");
				}
				if (pcon.isValid()) {
					return pcon;
				}
	
				bad++;
				status.incBadConnectionCount();
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

	private void checkoutConnection(SimplePooledConnection pcon, long start) {
		pcon.setCheckoutTimestamp(System.currentTimeMillis());
		pcon.setLastUsedTimestamp(System.currentTimeMillis());
		actives.add(pcon);
		status.checkConnection(start);
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
		catch (Throwable e) {
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
		catch (Throwable e) {
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
