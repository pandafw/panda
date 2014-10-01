package panda.castor;

import java.util.HashMap;
import java.util.Map;

import panda.lang.CycleDetectStrategy;
import panda.lang.CycleDetector;

public class CastContext extends CycleDetector implements CycleDetectStrategy {
	private int cycleDetectStrategy = CYCLE_DETECT_NOPROP;

	private Castors castors;
	private Map<String, Object> context;
	
	/**
	 * @param castors
	 */
	public CastContext(Castors castors) {
		this.castors = castors;
	}

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
	
	/**
	 * @return the castors
	 */
	public Castors getCastors() {
		return castors;
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
