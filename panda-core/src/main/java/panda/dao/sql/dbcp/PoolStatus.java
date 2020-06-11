package panda.dao.sql.dbcp;

public class PoolStatus {
	private long requestCount;
	private long accumulatedRequestTime;
	private long accumulatedCheckoutTime;
	private long claimedOverdueConnectionCount;
	private long accumulatedCheckoutTimeOfOverdueConnections;
	private long accumulatedWaitTime;
	private long hadToWaitCount;
	private long waitTimeoutCount;
	private long tooManyWaitCount;
	private long badConnectionCount;

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
	 * @return the waitTimeoutCount
	 */
	public long getWaitTimeoutCount() {
		return waitTimeoutCount;
	}

	/**
	 * @return the tooManyWaitCount
	 */
	public long getTooManyWaitCount() {
		return tooManyWaitCount;
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n requestCount                    ").append(getRequestCount());
		sb.append("\n averageRequestTime              ").append(getAverageRequestTime());
		sb.append("\n averageCheckoutTime             ").append(getAverageCheckoutTime());
		sb.append("\n claimedOverdue                  ").append(getClaimedOverdueConnectionCount());
		sb.append("\n averageOverdueCheckoutTime      ").append(getAverageOverdueCheckoutTime());
		sb.append("\n averageWaitTime                 ").append(getAverageWaitTime());
		sb.append("\n hadToWaitCount                  ").append(getHadToWaitCount());
		sb.append("\n waitTimeoutCount                ").append(getWaitTimeoutCount());
		sb.append("\n tooManyWaitCount                ").append(getTooManyWaitCount());
		sb.append("\n badConnectionCount              ").append(getBadConnectionCount());
		return sb.toString();
	}

	public synchronized void incBadConnectionCount() {
		badConnectionCount++;
	}

	public synchronized void addCheckoutTime(long checkoutTime) {
		accumulatedCheckoutTime += checkoutTime;
	}

	public synchronized void incHadToWaitCount() {
		hadToWaitCount++;
	}

	public synchronized void addWaitTime(long elapsed) {
		accumulatedWaitTime += elapsed;
	}

	public synchronized void claimOverdueConnection(long longestCheckoutTime) {
		claimedOverdueConnectionCount++;
		accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
		accumulatedCheckoutTime += longestCheckoutTime;
	}

	public synchronized void checkConnection(long start) {
		requestCount++;
		accumulatedRequestTime += System.currentTimeMillis() - start;
	}

	public synchronized void incWaitTimeout() {
		waitTimeoutCount++;
	}

	public synchronized void incTooManyWait() {
		tooManyWaitCount++;
	}
}
