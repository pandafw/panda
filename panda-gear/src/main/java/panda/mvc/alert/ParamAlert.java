package panda.mvc.alert;

import java.util.List;
import java.util.Map;

public interface ParamAlert {

	/**
	 * Get the field specific errors associated with this action. Error messages should not be added
	 * directly here, as implementations are free to return a new Collection or an Unmodifiable
	 * Collection.
	 * 
	 * @return Map with errors mapped from fieldname (String) to Collection of String error messages
	 */
	Map<String, List<String>> getErrors();

	/**
	 * Set the field error map of fieldname (String) to Collection of String error messages.
	 * 
	 * @param errors field error map
	 */
	void setErrors(Map<String, List<String>> errors);

	/**
	 * Add an error message for a given field.
	 * 
	 * @param fieldName name of field
	 * @param error the error message
	 */
	void addError(String fieldName, String error);

	/**
	 * Check whether there are any field errors associated with this action.
	 * 
	 * @return whether there are any field errors
	 */
	boolean hasErrors();

	/**
	 * Clear errors.
	 */
	void clearErrors();
}
