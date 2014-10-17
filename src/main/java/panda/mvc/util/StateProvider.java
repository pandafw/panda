package panda.mvc.util;


/**
 * StateProvider
 */
public interface StateProvider {
	/**
	 * Save state
	 * @param name state name
	 * @param value state value
	 */
	StateProvider saveState(String name, Object value);
	
	/**
	 * Load state
	 * @param name state name
	 * @return state value 
	 */
	Object loadState(String name);
}
