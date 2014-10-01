package panda.castor;

import java.util.HashMap;
import java.util.Map;

import panda.lang.CycleDetectStrategy;
import panda.lang.CycleDetector;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class CastContext extends CycleDetector implements CycleDetectStrategy {
	private int cycleDetectStrategy = CYCLE_DETECT_NOPROP;

	private Map<String, Object> context;
	
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
	
	public Object get(String key) {
		if (context == null) {
			return null;
		}
		return context.get(key);
	}
	
	public Object set(String key, Object obj) {
		if (context == null) {
			context = new HashMap<String, Object>();
		}
		return context.put(key, obj);
	}
}
