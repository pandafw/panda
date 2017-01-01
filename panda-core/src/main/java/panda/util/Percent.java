package panda.util;

/**
 * Percent
 * 
 */
public class Percent {
	/**
	 * DEFAULT_MIN = 1;
	 */
	public static int DEFAULT_MIN = 1;
	
	/**
	 * DEFAULT_MAX = 100;
	 */
	public static int DEFAULT_MAX = 100;
	
	/**
	 * DEFAULT_STEP = 1;
	 */
	public static int DEFAULT_STEP = 1;
	
	protected long min;
	protected long max;
	protected long step;
	protected long value;
	
	/**
	 * constructor
	 */
	public Percent() {
		min = DEFAULT_MIN;
		max = DEFAULT_MAX;
		step = DEFAULT_STEP;
		value = min;
	}

	/**
	 * @return max
	 */
	public long getMax() {
		return max;
	}

	/**
	 * @param max max
	 */
	public void setMax(long max) {
		this.max = max;
	}

	/**
	 * @return min
	 */
	public long getMin() {
		return min;
	}

	/**
	 * @param min min
	 */
	public void setMin(long min) {
		this.min = min;
	}

	/**
	 * @return step
	 */
	public long getStep() {
		return step;
	}

	/**
	 * @param step step
	 */
	public void setStep(long step) {
		this.step = step;
	}
	
	/**
	 * step it
	 */
	public void stepIt() {
		setValue(value + step);
	}
	
	/**
	 * @return value
	 */
	public long getValue() {
		return value;
	}

	/**
	 * @param value value
	 */
	public void setValue(long value) {
		this.value = value > max ? max : value;
	}

	/**
	 * @return percent
	 */
	public int getPercent() {
		return (int)((float)(value - min) * 100.00 / (max - min));
	}
}
