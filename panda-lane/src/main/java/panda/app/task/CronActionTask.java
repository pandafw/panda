package panda.app.task;

import panda.lang.Objects;

public class CronActionTask extends ActionTask {
	private long delay;
	private long initialDelay;
	private long fixedDelay;
	private long fixedRate;
	private String cron;

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

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.appendSuper(super.toString())
				.append("delay", delay)
				.append("initialDelay", initialDelay)
				.append("fixedDelay", fixedDelay)
				.append("fixedRate", fixedRate)
				.append("cron", cron)
				.toString();
	}
}
