package panda.mvc.util;

import java.util.List;
import java.util.Map;

public interface TextProvider {

	/**
	 * Checks if a message name exists.
	 * 
	 * @param key message name to check for
	 * @return boolean true if name exists, false otherwise.
	 */
	boolean hasKey(String key);

	/**
	 * Gets a message based on a message name or if no message is found the provided name is returned.
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @return the message as found in the resource bundle, or the provided name if none is found.
	 */
	String getText(String key);

	/**
	 * Gets a message based on a name, or, if the message is not found, a supplied default value is
	 * returned.
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def the default value which will be returned if no message is found
	 * @return the message as found in the resource bundle, or def if none is found
	 */
	String getText(String key, String def);

	/**
	 * Gets a message based on a name using the supplied arg, or, if the message is not found, a
	 * supplied default value is returned.
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def the default value which will be returned if no message is found
	 * @param arg object to be used in a EL expression such as "${top}"
	 * @return the message as found in the resource bundle, or def if none is found
	 */
	String getText(String key, String def, Object arg);

	/**
	 * getTextAsBoolean
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @return boolean value
	 */
	Boolean getTextAsBoolean(String key);

	/**
	 * getTextAsBoolean
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def default value
	 * @return boolean value
	 */
	Boolean getTextAsBoolean(String key, Boolean def);

	/**
	 * getTextAsInt
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @return integer value
	 */
	Integer getTextAsInt(String key);

	/**
	 * getTextAsInt
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def default value
	 * @return integer value
	 */
	Integer getTextAsInt(String key, Integer def);

	/**
	 * getTextAsLong
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @return long value
	 */
	Long getTextAsLong(String key);

	/**
	 * getTextAsLong
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def default value
	 * @return long value
	 */
	Long getTextAsLong(String key, Long def);

	/**
	 * getTextAsList
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @return list value
	 */
	List getTextAsList(String key);

	/**
	 * getTextAsList
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def default value
	 * @return list value
	 */
	List getTextAsList(String key, List def);

	/**
	 * getTextAsMap
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @return map value
	 */
	Map getTextAsMap(String key);

	/**
	 * getTextAsMap
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def default value
	 * @return map value
	 */
	Map getTextAsMap(String key, Map def);
}
