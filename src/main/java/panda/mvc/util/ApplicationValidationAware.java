package panda.mvc.util;

import java.util.Map;

/**
 * ApplicationValidationAware
 */
public interface ApplicationValidationAware {
	/**
	 * Set the Map of Application-level String error messages.
	 * 
	 * @param errorMessages Map of String error messages
	 */
	void setApplicationErrors(Map<String, String> errorMessages);

	/**
	 * Get the Map of Application-level error messages for this action. Error messages should not be
	 * added directly here, as implementations are free to return a new Map or an Unmodifiable Map.
	 * 
	 * @return Map of String error messages
	 */
	Map<String, String> getApplicationErrors();

	/**
	 * Add an Application-level error message to this Application.
	 * 
	 * @param id the message id
	 * @param anErrorMessage the error message
	 */
	void addApplicationError(String id, String anErrorMessage);

	/**
	 * Remove a Application-level error message.
	 * 
	 * @param id the message id
	 * @return the removed message
	 */
	String removeApplicationError(String id);

	/**
	 * Check whether there is a Application-level error message.
	 * 
	 * @param id the message id
	 * @return true if the Application-level error message with the input id has been registered
	 */
	boolean hasApplicationError(String id);

	/**
	 * Check whether there are any Application-level error messages.
	 * 
	 * @return true if any Application-level error messages have been registered
	 */
	boolean hasApplicationErrors();

	/**
	 * Set the Map of Application-level String messages (not errors).
	 * 
	 * @param messages Map of String messages (not errors).
	 */
	void setApplicationMessages(Map<String, String> messages);

	/**
	 * Get the Map of Application-level messages for this action. Messages should not be added
	 * directly here, as implementations are free to return a new Map or an Unmodifiable Map.
	 * 
	 * @return Map of String messages
	 */
	Map<String, String> getApplicationMessages();

	/**
	 * Add an Application-level message to this Application.
	 * 
	 * @param id the message id
	 * @param aMessage the message
	 */
	void addApplicationMessage(String id, String aMessage);

	/**
	 * Remove a Application-level message.
	 * 
	 * @param id the message id
	 * @return the removed message
	 */
	String removeApplicationMessage(String id);

	/**
	 * Check whether there is a Application-level message.
	 * 
	 * @param id the message id
	 * @return true if the Application-level message with the input id has been registered
	 */
	boolean hasApplicationMessage(String id);

	/**
	 * Checks whether there are any Application-level messages.
	 * 
	 * @return true if any Application-level messages have been registered
	 */
	boolean hasApplicationMessages();

	/**
	 * Set the Map of Application-level String warn messages.
	 * 
	 * @param warnMessages
	 */
	void setApplicationWarnings(Map<String, String> warnMessages);

	/**
	 * Get the Map of Application-level warn messages for this action. Error messages should not be
	 * added directly here, as implementations are free to return a new Map or an Unmodifiable Map.
	 * 
	 * @return Map of String warn messages
	 */
	Map<String, String> getApplicationWarnings();

	/**
	 * Add an Application-level warning message to this Application.
	 * 
	 * @param id the message id
	 * @param aWarnMessage the warning message
	 */
	void addApplicationWarning(String id, String aWarnMessage);

	/**
	 * Remove a Application-level warning message.
	 * 
	 * @param id the message id
	 * @return the removed message
	 */
	String removeApplicationWarning(String id);

	/**
	 * Check whether there is a Application-level warning message.
	 * 
	 * @param id the message id
	 * @return true if the Application-level warning message with the input id has been registered
	 */
	boolean hasApplicationWarning(String id);

	/**
	 * Check whether there are any Application-level warn messages.
	 * 
	 * @return true if any Application-level warn messages have been registered
	 */
	boolean hasApplicationWarnings();

	/**
	 * Set the Map of Application-level String confirm messages.
	 * 
	 * @param confirmMessages
	 */
	void setApplicationConfirms(Map<String, String> confirmMessages);

	/**
	 * Get the Map of Application-level confirm messages for this action. Error messages should not
	 * be added directly here, as implementations are free to return a new Map or an Unmodifiable
	 * Map.
	 * 
	 * @return Map of String confirm messages
	 */
	Map<String, String> getApplicationConfirms();

	/**
	 * Add an Application-level confirm message to this Application.
	 * 
	 * @param id the message id
	 * @param aConfirmMessage the confirm message
	 */
	void addApplicationConfirm(String id, String aConfirmMessage);

	/**
	 * Remove a Application-level confirm message.
	 * 
	 * @param id the message id
	 * @return the removed message
	 */
	String removeApplicationConfirm(String id);

	/**
	 * Check whether there is a Application-level confirm message.
	 * 
	 * @param id the message id
	 * @return true if the Application-level confirm message with the input id has been registered
	 */
	boolean hasApplicationConfirm(String id);

	/**
	 * Check whether there are any Application-level confirm messages.
	 * 
	 * @return true if any Application-level confirm messages have been registered
	 */
	boolean hasApplicationConfirms();

}
