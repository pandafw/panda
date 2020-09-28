package panda.mvc.alert;

import java.util.Collection;

public interface ActionAlert {

	/**
	 * Get the Collection of error messages for this action. Error messages should not
	 * be added directly here, as implementations are free to return a new Collection or an
	 * Unmodifiable Collection.
	 * 
	 * @return Collection of String error messages
	 */
	Collection<String> getErrors();

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
	 * Remove errors.
	 */
	void removeErrors();
	
	/**
	 * Get the Collection of warn messages for this action. Error messages should not
	 * be added directly here, as implementations are free to return a new Collection or an
	 * Unmodifiable Collection.
	 * 
	 * @return Collection of String warn messages
	 */
	Collection<String> getWarnings();

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
	 * Remove warnings.
	 */
	void removeWarnings();
	
	/**
	 * Get the Collection of confirm messages for this action. Error messages should
	 * not be added directly here, as implementations are free to return a new Collection or an
	 * Unmodifiable Collection.
	 * 
	 * @return Collection of String confirm messages
	 */
	Collection<String> getConfirms();

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
	 * Remove confirms.
	 */
	void removeConfirms();
	
	/**
	 * Get the Collection of messages for this action. Messages should not be added
	 * directly here, as implementations are free to return a new Collection or an Unmodifiable
	 * Collection.
	 * 
	 * @return Collection of String messages
	 */
	Collection<String> getMessages();

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
	 * Remove messages.
	 */
	void removeMessages();

	/**
	 * Checks whether there are any error/warnings/confirms/messages.
	 * 
	 * @return true if has any error/warnings/confirms/messages.
	 */
	boolean hasContents();
	
	/**
	 * Clear all.
	 */
	void clear();
}
