package panda.mvc.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * ActionValidationAware
 */
public interface ActionValidationAware {

	/**
	 * Set the Collection of Action-level String error messages.
	 * 
	 * @param errorMessages Collection of String error messages
	 */
	void setActionErrors(Collection<String> errorMessages);

	/**
	 * Get the Collection of Action-level error messages for this action. Error messages should not
	 * be added directly here, as implementations are free to return a new Collection or an
	 * Unmodifiable Collection.
	 * 
	 * @return Collection of String error messages
	 */
	Collection<String> getActionErrors();

	/**
	 * Set the Collection of Action-level String messages (not errors).
	 * 
	 * @param messages Collection of String messages (not errors).
	 */
	void setActionMessages(Collection<String> messages);

	/**
	 * Get the Collection of Action-level messages for this action. Messages should not be added
	 * directly here, as implementations are free to return a new Collection or an Unmodifiable
	 * Collection.
	 * 
	 * @return Collection of String messages
	 */
	Collection<String> getActionMessages();

	/**
	 * Set the field error map of fieldname (String) to Collection of String error messages.
	 * 
	 * @param errorMap field error map
	 */
	void setFieldErrors(Map<String, List<String>> errorMap);

	/**
	 * Get the field specific errors associated with this action. Error messages should not be added
	 * directly here, as implementations are free to return a new Collection or an Unmodifiable
	 * Collection.
	 * 
	 * @return Map with errors mapped from fieldname (String) to Collection of String error messages
	 */
	Map<String, List<String>> getFieldErrors();

	/**
	 * Add an Action-level error message to this Action.
	 * 
	 * @param anErrorMessage the error message
	 */
	void addActionError(String anErrorMessage);

	/**
	 * Add an Action-level message to this Action.
	 * 
	 * @param aMessage the message
	 */
	void addActionMessage(String aMessage);

	/**
	 * Add an error message for a given field.
	 * 
	 * @param fieldName name of field
	 * @param errorMessage the error message
	 */
	void addFieldError(String fieldName, String errorMessage);

	/**
	 * Check whether there are any Action-level error messages.
	 * 
	 * @return true if any Action-level error messages have been registered
	 */
	boolean hasActionErrors();

	/**
	 * Checks whether there are any Action-level messages.
	 * 
	 * @return true if any Action-level messages have been registered
	 */
	boolean hasActionMessages();

	/**
	 * Checks whether there are any action errors or field errors.
	 * <p/>
	 * <b>Note</b>: that this does not have the same meaning as in WW 1.x.
	 * 
	 * @return <code>(hasActionErrors() || hasFieldErrors())</code>
	 */
	boolean hasErrors();

	/**
	 * Check whether there are any field errors associated with this action.
	 * 
	 * @return whether there are any field errors
	 */
	boolean hasFieldErrors();

	/**
	 * Set the Collection of Action-level String warn messages.
	 * 
	 * @param warnMessages
	 */
	void setActionWarnings(Collection<String> warnMessages);

	/**
	 * Get the Collection of Action-level warn messages for this action. Error messages should not
	 * be added directly here, as implementations are free to return a new Collection or an
	 * Unmodifiable Collection.
	 * 
	 * @return Collection of String warn messages
	 */
	Collection<String> getActionWarnings();

	/**
	 * Add an Action-level warning message to this Action.
	 * 
	 * @param aWarnMessage the warning message
	 */
	void addActionWarning(String aWarnMessage);

	/**
	 * Check whether there are any Action-level warn messages.
	 * 
	 * @return true if any Action-level warn messages have been registered
	 */
	boolean hasActionWarnings();

	/**
	 * Set the Collection of Action-level String confirm messages.
	 * 
	 * @param confirmMessages
	 */
	void setActionConfirms(Collection<String> confirmMessages);

	/**
	 * Get the Collection of Action-level confirm messages for this action. Error messages should
	 * not be added directly here, as implementations are free to return a new Collection or an
	 * Unmodifiable Collection.
	 * 
	 * @return Collection of String confirm messages
	 */
	Collection<String> getActionConfirms();

	/**
	 * Add an Action-level confirm message to this Action.
	 * 
	 * @param aConfirmMessage the confirm message
	 */
	void addActionConfirm(String aConfirmMessage);

	/**
	 * Check whether there are any Action-level confirm messages.
	 * 
	 * @return true if any Action-level confirm messages have been registered
	 */
	boolean hasActionConfirms();

}
