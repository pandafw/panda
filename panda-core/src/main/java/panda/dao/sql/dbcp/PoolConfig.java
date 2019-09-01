package panda.dao.sql.dbcp;

import panda.lang.Strings;
import panda.lang.time.DateTimes;

public class PoolConfig {
	protected int maxActive = 100;
	protected int maxIdle = 20;
	protected int maxWait = 1000;
	protected long maxWaitMillis = 20000;
	protected long maxCheckoutMillis = DateTimes.MS_WEEK;

	protected String pingQuery = "";
	protected int pingTimeout = 1;
	protected long pingOlderThan;
	protected long pingNotUsedFor = 600000;

	public int getMaxActive() {
		return maxActive;
	}
	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}
	public int getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}
	public int getMaxWait() {
		return maxWait;
	}
	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}
	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}
	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}
	public long getMaxCheckoutMillis() {
		return maxCheckoutMillis;
	}
	public void setMaxCheckoutMillis(long maxCheckoutMillis) {
		this.maxCheckoutMillis = maxCheckoutMillis;
	}
	public String getPingQuery() {
		return pingQuery;
	}
	public void setPingQuery(String pingQuery) {
		this.pingQuery = Strings.stripToEmpty(pingQuery);
	}
	public int getPingTimeout() {
		return pingTimeout;
	}
	public void setPingTimeout(int pingTimeout) {
		this.pingTimeout = pingTimeout;
	}
	public long getPingOlderThan() {
		return pingOlderThan;
	}
	public void setPingOlderThan(long pingOlderThan) {
		this.pingOlderThan = pingOlderThan;
	}
	public long getPingNotUsedFor() {
		return pingNotUsedFor;
	}
	public void setPingNotUsedFor(long pingNotUsedFor) {
		this.pingNotUsedFor = pingNotUsedFor;
	}
}
