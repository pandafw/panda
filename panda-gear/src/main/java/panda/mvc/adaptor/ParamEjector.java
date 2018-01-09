package panda.mvc.adaptor;

import java.util.Set;

/**
 * Parameter Ejector
 */
public interface ParamEjector {
	public final static String ALL = "*";

	/**
	 * eject all
	 * @return the parameter
	 */
	Object eject();
	
	/**
	 * eject by name
	 * @param name parameter name
	 * @return the parameter
	 */
	Object eject(String name);

	/**
	 * keys
	 * @return key set
	 */
	Set<String> keys();
}
