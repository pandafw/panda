package panda.mvc.util;

import java.util.Map;

/**
 * SessionValidationAware
 */
public interface SessionValidationAware {
	/**
	 * Set the Map of Session-level String error messages.
	 * 
	 * @param errorMessages Map of String error messages
	 */
	void setSessionErrors(Map<String, String> errorMessages);

	/**
	 * Get the Map of Session-level error messages for this action. Error messages should not be
	 * added directly here, as implementations are free to return a new Map or an Unmodifiable Map.
	 * 
	 * @return Map of String error messages
	 */
	Map<String, String> getSessionErrors();

	/**
	 * Add an Session-level error message to this Session.
	 * 
	 * @param id the message id
	 * @param anErrorMessage the error message
	 */
	void addSessionError(String id, String anErrorMessage);

	/**
	 * Remove a Session-level error message.
	 * 
	 * @param id the message id
	 * @return the removed message
	 */
	String removeSessionError(String id);

	/**
	 * Check whether there is a Session-level error message.
	 * 
	 * @param id the message id
	 * @return true if the Session-level error message with the input id has been registered
	 */
	boolean hasSessionError(String id);

	/**
	 * Check whether there are any Session-level error messages.
	 * 
	 * @return true if any Session-level error messages have been registered
	 */
	boolean hasSessionErrors();

	/**
	 * Set the Map of Session-level String messages (not errors).
	 * 
	 * @param messages Map of String messages (not errors).
	 */
	void setSessionMessages(Map<String, String> messages);

	/**
	 * Get the Map of Session-level messages for this action. Messages should not be added
	 * directly here, as implementations are free to return a new Map or an Unmodifiable Map.
	 * 
	 * @return Map of String messages
	 */
	Map<String, String> getSessionMessages();

	/**
	 * Add an Session-level message to this Session.
	 * 
	 * @param id the message id
	 * @param aMessage the message
	 */
	void addSessionMessage(String id, String aMessage);

	/**
	 * Remove a Session-level message.
	 * 
	 * @param id the message id
	 * @return the removed message
	 */
	String removeSessionMessage(String id);

	/**
	 * Check whether there is a Session-level message.
	 * 
	 * @param id the message id
	 * @return true if the Session-level message with the input id has been registered
	 */
	boolean hasSessionMessage(String id);

	/**
	 * Checks whether there are any Session-level messages.
	 * 
	 * @return true if any Session-level messages have been registered
	 */
	boolean hasSessionMessages();

	/**
	 * Set the Map of Session-level String warn messages.
	 * 
	 * @param warnMessages
	 */
	void setSessionWarnings(Map<String, String> warnMessages);

	/**
	 * Get the Map of Session-level warn messages for this action. Error messages should not be
	 * added directly here, as implementations are free to return a new Map or an Unmodifiable Map.
	 * 
	 * @return Map of String warn messages
	 */
	Map<String, String> getSessionWarnings();

	/**
	 * Add an Session-level warning message to this Session.
	 * 
	 * @param id the message id
	 * @param aWarnMessage the warning message
	 */
	void addSessionWarning(String id, String aWarnMessage);

	/**
	 * Remove a Session-level warning message.
	 * 
	 * @param id the message id
	 * @return the removed message
	 */
	String removeSessionWarning(String id);

	/**
	 * Check whether there is a Session-level warning message.
	 * 
	 * @param id the message id
	 * @return true if the Session-level warning message with the input id has been registered
	 */
	boolean hasSessionWarning(String id);

	/**
	 * Check whether there are any Session-level warn messages.
	 * 
	 * @return true if any Session-level warn messages have been registered
	 */
	boolean hasSessionWarnings();

	/**
	 * Set the Map of Session-level String confirm messages.
	 * 
	 * @param confirmMessages
	 */
	void setSessionConfirms(Map<String, String> confirmMessages);

	/**
	 * Get the Map of Session-level confirm messages for this action. Error messages should not
	 * be added directly here, as implementations are free to return a new Map or an Unmodifiable
	 * Map.
	 * 
	 * @return Map of String confirm messages
	 */
	Map<String, String> getSessionConfirms();

	/**
	 * Add an Session-level confirm message to this Session.
	 * 
	 * @param id the message id
	 * @param aConfirmMessage the confirm message
	 */
	void addSessionConfirm(String id, String aConfirmMessage);

	/**
	 * Remove a Session-level confirm message.
	 * 
	 * @param id the message id
	 * @return the removed message
	 */
	String removeSessionConfirm(String id);

	/**
	 * Check whether there is a Session-level confirm message.
	 * 
	 * @param id the message id
	 * @return true if the Session-level confirm message with the input id has been registered
	 */
	boolean hasSessionConfirm(String id);

	/**
	 * Check whether there are any Session-level confirm messages.
	 * 
	 * @return true if any Session-level confirm messages have been registered
	 */
	boolean hasSessionConfirms();

}
