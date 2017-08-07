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
	StateProvider saveState(String name, String value);
	
	/**
	 * Load state
	 * @param name state name
	 * @return state value 
	 */
	String loadState(String name);
}
