package panda.mvc.validation.aware;

import java.util.Collection;

public interface ValidationAware {

	/**
	 * Get the Collection of error messages for this action. Error messages should not
	 * be added directly here, as implementations are free to return a new Collection or an
	 * Unmodifiable Collection.
	 * 
	 * @return Collection of String error messages
	 */
	Collection<String> getErrors();

	/**
	 * Set the Collection of String error messages.
	 * 
	 * @param errors Collection of String error messages
	 */
	void setErrors(Collection<String> errors);
	/**
	 * Add an error message to this Action.
	 * 
	 * @param error the error message
	 */
	void addError(String error);

	/**
	 * Check whether there are any error messages.
	 * 
	 * @return true if any error messages have been registered
	 */
	boolean hasErrors();

	/**
	 * Clear errors.
	 */
	void clearErrors();
	
	/**
	 * Get the Collection of warn messages for this action. Error messages should not
	 * be added directly here, as implementations are free to return a new Collection or an
	 * Unmodifiable Collection.
	 * 
	 * @return Collection of String warn messages
	 */
	Collection<String> getWarnings();

	/**
	 * Set the Collection of String warn messages.
	 * 
	 * @param warnings
	 */
	void setWarnings(Collection<String> warnings);

	/**
	 * Add an warning message to this Action.
	 * 
	 * @param warn the warning message
	 */
	void addWarning(String warn);

	/**
	 * Check whether there are any warn messages.
	 * 
	 * @return true if any warn messages have been registered
	 */
	boolean hasWarnings();

	/**
	 * Clear warnings.
	 */
	void clearWarnings();
	
	/**
	 * Get the Collection of confirm messages for this action. Error messages should
	 * not be added directly here, as implementations are free to return a new Collection or an
	 * Unmodifiable Collection.
	 * 
	 * @return Collection of String confirm messages
	 */
	Collection<String> getConfirms();

	/**
	 * Set the Collection of String confirm messages.
	 * 
	 * @param confirms
	 */
	void setConfirms(Collection<String> confirms);

	/**
	 * Add an confirm message to this Action.
	 * 
	 * @param confirm the confirm message
	 */
	void addConfirm(String confirm);

	/**
	 * Check whether there are any confirm messages.
	 * 
	 * @return true if any confirm messages have been registered
	 */
	boolean hasConfirms();

	/**
	 * Clear confirms.
	 */
	void clearConfirms();
	
	/**
	 * Get the Collection of messages for this action. Messages should not be added
	 * directly here, as implementations are free to return a new Collection or an Unmodifiable
	 * Collection.
	 * 
	 * @return Collection of String messages
	 */
	Collection<String> getMessages();

	/**
	 * Set the Collection of String messages (not errors).
	 * 
	 * @param messages Collection of String messages (not errors).
	 */
	void setMessages(Collection<String> messages);

	/**
	 * Add an message to this Action.
	 * 
	 * @param message the message
	 */
	void addMessage(String message);

	/**
	 * Checks whether there are any messages.
	 * 
	 * @return true if any messages have been registered
	 */
	boolean hasMessages();
	
	/**
	 * Clear messages.
	 */
	void clearMessages();
	
	/**
	 * Clear all.
	 */
	void clear();
}
