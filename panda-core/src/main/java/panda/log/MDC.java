package panda.log;

import java.util.HashMap;
import java.util.Map;

/**
 * The MDC class provides mapped diagnostic contexts. 
 * A Mapped Diagnostic Context, or MDC in short, is an instrument for distinguishing interleaved log output from different sources. 
 * Log output is typically interleaved when a server handles multiple clients near-simultaneously.
 * 
 * The MDC is managed on a per thread basis. 
 * A child thread automatically inherits a copy of the mapped diagnostic context of its parent.
 */
public class MDC {
	private static final ThreadLocal<Map<String, Object>> tlm = new ThreadLocal<Map<String, Object>>() {
		@Override
		protected Map<String, Object> initialValue() {
			return new HashMap<String, Object>();
		}
	};
	
	public static Object put(String key, Object obj) {
		return tlm.get().put(key, obj);
	}
	
	public static Object get(String key) {
		return tlm.get().get(key);
	}
	
	public static Object remove(String key) {
		return tlm.get().remove(key);
	}
	
	public static Map<String, Object> map() {
		return tlm.get();
	}
}
