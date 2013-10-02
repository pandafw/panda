package panda.castor;

import panda.lang.CycleDetectStrategy;
import panda.lang.CycleDetector;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class CastContext extends CycleDetector implements CycleDetectStrategy {
	private int cycleDetectStrategy = CYCLE_DETECT_NOPROP;

	/**
	 * @return the cycleDetectStrategy
	 */
	public int getCycleDetectStrategy() {
		return cycleDetectStrategy;
	}

	/**
	 * @param cycleDetectStrategy the cycleDetectStrategy to set
	 */
	public void setCycleDetectStrategy(int cycleDetectStrategy) {
		this.cycleDetectStrategy = cycleDetectStrategy;
	}
}
