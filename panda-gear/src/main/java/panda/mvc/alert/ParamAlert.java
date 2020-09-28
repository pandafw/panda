package panda.mvc.alert;

import java.util.List;
import java.util.Map;

public interface ParamAlert {

	/**
	 * Get the field specific errors associated with this action. Error messages should not be added
	 * directly here, as implementations are free to return a new Collection or an Unmodifiable
	 * Collection.
	 * 
	 * @return Map with errors mapped from name (String) to Collection of String error messages
	 */
	Map<String, List<String>> getErrors();

	/**
	 * Get the field specific errors associated with this action. Error messages should not be added
	 * directly here, as implementations are free to return a new Collection or an Unmodifiable
	 * Collection.
	 * 
	 * @param name name of field
	 * @return Map with errors mapped from name (String) to Collection of String error messages
	 */
	List<String> getErrors(String name);

	/**
	 * Add an error message for a given field.
	 * 
	 * @param name name of field
	 * @param error the error message
	 */
	void addError(String name, String error);

	/**
	 * Check whether there are any field errors associated with this action.
	 * 
	 * @param name name field of field
	 * @return whether there are any field errors
	 */
	boolean hasErrors(String name);

	/**
	 * Check whether there are any field errors associated with this action.
	 * 
	 * @return whether there are any field errors
	 */
	boolean hasErrors();

	/**
	 * Remove field errors.
	 */
	void removeErrors(String name);

	/**
	 * Remove all field errors.
	 */
	void removeErrors();
}
