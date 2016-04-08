package panda.log.impl;

import panda.log.LogLevel;


public abstract class BaseLogAdapter extends AbstractLogAdapter {
	protected LogLevel threshold;
	
	/**
	 * @return the threshold
	 */
	public LogLevel getThreshold() {
		return threshold;
	}

	/**
	 * @param threshold the threshold to set
	 */
	public void setThreshold(LogLevel threshold) {
		this.threshold = threshold;
	}

	@Override
	protected void setProperty(String name, String value) {
		if ("threahold".equalsIgnoreCase(name)) {
			setThreshold(LogLevel.parse(value));
		}
		else {
			super.setProperty(name, value);
		}
	}
}
