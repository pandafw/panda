package panda.mvc;

import java.util.List;
import java.util.Map;

public interface TextProvider {

	/**
	 * Checks if a message key exists.
	 * 
	 * @param key message key to check for
	 * @return boolean true if key exists, false otherwise.
	 */
	boolean hasKey(String key);

	/**
	 * Gets a message based on a message key or if no message is found the provided key is returned.
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @return the message as found in the resource bundle, or the provided key if none is found.
	 */
	String getText(String key);

	/**
	 * Gets a message based on a key, or, if the message is not found, a supplied default value is
	 * returned.
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param defaultValue the default value which will be returned if no message is found
	 * @return the message as found in the resource bundle, or defaultValue if none is found
	 */
	String getText(String key, String defaultValue);

	/**
	 * Gets a message based on a key using the supplied obj, as defined in
	 * {@link java.text.MessageFormat}, or, if the message is not found, a supplied default value is
	 * returned.
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param defaultValue the default value which will be returned if no message is found
	 * @param obj obj to be used in a {@link java.text.MessageFormat} message
	 * @return the message as found in the resource bundle, or defaultValue if none is found
	 */
	String getText(String key, String defaultValue, String obj);

	/**
	 * Gets a message based on a key using the supplied args, as defined in
	 * {@link java.text.MessageFormat} or the provided key if no message is found.
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param args a list args to be used in a {@link java.text.MessageFormat} message
	 * @return the message as found in the resource bundle, or the provided key if none is found.
	 */
	String getText(String key, List<?> args);

	/**
	 * Gets a message based on a key using the supplied args, as defined in
	 * {@link java.text.MessageFormat}, or the provided key if no message is found.
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param args an array args to be used in a {@link java.text.MessageFormat} message
	 * @return the message as found in the resource bundle, or the provided key if none is found.
	 */
	String getText(String key, String[] args);

	/**
	 * Gets a message based on a key using the supplied args, as defined in
	 * {@link java.text.MessageFormat}, or, if the message is not found, a supplied default value is
	 * returned.
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param defaultValue the default value which will be returned if no message is found
	 * @param args a list args to be used in a {@link java.text.MessageFormat} message
	 * @return the message as found in the resource bundle, or defaultValue if none is found
	 */
	String getText(String key, String defaultValue, List<?> args);

	/**
	 * Gets a message based on a key using the supplied args, as defined in
	 * {@link java.text.MessageFormat}, or, if the message is not found, a supplied default value is
	 * returned.
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param defaultValue the default value which will be returned if no message is found
	 * @param args an array args to be used in a {@link java.text.MessageFormat} message
	 * @return the message as found in the resource bundle, or defaultValue if none is found
	 */
	String getText(String key, String defaultValue, String[] args);

	/**
	 * getTextAsBoolean
	 * 
	 * @param name resource name
	 * @return boolean value
	 */
	Boolean getTextAsBoolean(String name);

	/**
	 * getTextAsBoolean
	 * 
	 * @param name resource name
	 * @param defaultValue default value
	 * @return boolean value
	 */
	Boolean getTextAsBoolean(String name, Boolean defaultValue);

	/**
	 * getTextAsInt
	 * 
	 * @param name resource name
	 * @return integer value
	 */
	Integer getTextAsInt(String name);

	/**
	 * getTextAsInt
	 * 
	 * @param name resource name
	 * @param defaultValue default value
	 * @return integer value
	 */
	Integer getTextAsInt(String name, Integer defaultValue);

	/**
	 * getTextAsLong
	 * 
	 * @param name resource name
	 * @return long value
	 */
	Long getTextAsLong(String name);

	/**
	 * getTextAsLong
	 * 
	 * @param name resource name
	 * @param defaultValue default value
	 * @return long value
	 */
	Long getTextAsLong(String name, Long defaultValue);

	/**
	 * getTextAsList
	 * 
	 * @param name resource name
	 * @return list value
	 */
	List getTextAsList(String name);

	/**
	 * getTextAsList
	 * 
	 * @param name resource name
	 * @param defaultValue default value
	 * @return list value
	 */
	List getTextAsList(String name, List defaultValue);

	/**
	 * getTextAsMap
	 * 
	 * @param name resource name
	 * @return map value
	 */
	Map getTextAsMap(String name);

	/**
	 * getTextAsMap
	 * 
	 * @param name resource name
	 * @param defaultValue default value
	 * @return map value
	 */
	Map getTextAsMap(String name, Map defaultValue);
}
