package panda.dao.sql.dbcp;

import panda.lang.Strings;
import panda.lang.time.DateTimes;

public class PoolConfig {
	protected int maxActive = 10;
	protected int maxIdle = 5;
	protected long maxCheckoutTime = DateTimes.MS_WEEK;
	protected String pingQuery = "";
	protected int pingTimeout = 1;
	protected long pingOlderThan;
	protected long pingNotUsedFor = 600000;
	protected long timeToWait = 20000;

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
	public long getMaxCheckoutTime() {
		return maxCheckoutTime;
	}
	public void setMaxCheckoutTime(long maxCheckoutTime) {
		this.maxCheckoutTime = maxCheckoutTime;
	}
	public String getPingQuery() {
		return pingQuery;
	}
	public void setPingQuery(String pingQuery) {
		this.pingQuery = Strings.stripToEmpty(pingQuery);
	}
	public long getPingTimeout() {
		return pingTimeout * 1000L;
	}
	public void setPingTimeout(long pingTimeout) {
		this.pingTimeout = (int)(pingTimeout / 1000);
		if (this.pingTimeout < 1) {
			this.pingTimeout = 1;
		}
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
	public long getTimeToWait() {
		return timeToWait;
	}
	public void setTimeToWait(long timeToWait) {
		this.timeToWait = timeToWait;
	}
}
