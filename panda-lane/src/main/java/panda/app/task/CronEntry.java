package panda.app.task;

import panda.lang.Objects;

public class CronEntry {
	private String url;
	private String description;
	private long delay;
	private long initialDelay;
	private long fixedDelay;
	private long fixedRate;
	private String cron;
	private boolean token;
	private Integer errorLimit;

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the delay
	 */
	public long getDelay() {
		return delay;
	}

	/**
	 * @param delay the delay to set
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}

	/**
	 * @return the initialDelay
	 */
	public long getInitialDelay() {
		return initialDelay;
	}

	/**
	 * @param initialDelay the initialDelay to set
	 */
	public void setInitialDelay(long initialDelay) {
		this.initialDelay = initialDelay;
	}

	/**
	 * @return the fixedDelay
	 */
	public long getFixedDelay() {
		return fixedDelay;
	}

	/**
	 * @param fixedDelay the fixedDelay to set
	 */
	public void setFixedDelay(long fixedDelay) {
		this.fixedDelay = fixedDelay;
	}

	/**
	 * @return the fixedRate
	 */
	public long getFixedRate() {
		return fixedRate;
	}

	/**
	 * @param fixedRate the fixedRate to set
	 */
	public void setFixedRate(long fixedRate) {
		this.fixedRate = fixedRate;
	}

	/**
	 * @return the cron
	 */
	public String getCron() {
		return cron;
	}

	/**
	 * @param cron the cron to set
	 */
	public void setCron(String cron) {
		this.cron = cron;
	}

	/**
	 * @return the token
	 */
	public boolean isToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(boolean token) {
		this.token = token;
	}

	/**
	 * @return the errorLimit
	 */
	public Integer getErrorLimit() {
		return errorLimit;
	}

	/**
	 * @param errorLimit the errorLimit to set
	 */
	public void setErrorLimit(Integer errorLimit) {
		this.errorLimit = errorLimit;
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("url", url)
				.append("description", description)
				.append("delay", delay)
				.append("initialDelay", initialDelay)
				.append("fixedDelay", fixedDelay)
				.append("cron", cron)
				.append("token", token)
				.append("errorLimit", errorLimit)
				.toString();
	}
}
