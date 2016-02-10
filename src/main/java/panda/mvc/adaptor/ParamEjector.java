package panda.mvc.adaptor;

import java.util.Set;

/**
 * 参数提取器
 */
public interface ParamEjector {
	public final static String ALL = "*";

	/**
	 * eject all
	 */
	Object eject();
	
	/**
	 * eject by name
	 */
	Object eject(String name);

	/**
	 * keys
	 */
	Set<String> keys();
}
