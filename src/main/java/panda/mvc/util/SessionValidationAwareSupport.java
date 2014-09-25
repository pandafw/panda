package panda.mvc.util;

import java.util.LinkedHashMap;
import java.util.Map;

import panda.mvc.ActionContext;
import panda.servlet.HttpSessionMap;

/**
 * Provides a default implementation of SessionValidationAware. Returns collections for errors
 * and messages.
 */
@SuppressWarnings("unchecked")
public class SessionValidationAwareSupport implements SessionValidationAware, PandaXWorkConstants {

	private Map<String, Object> session;
	
	public void setActionContext(ActionContext context) {
		session = new HttpSessionMap(context.getRequest());
	}
	
	private Map<String, Object> getSession() {
		return session;
	}
	
	/**
	 * @see SessionValidationAware#setSessionErrors(java.util.Map)
	 */
	public synchronized void setSessionErrors(Map<String, String> errorMessages) {
		getSession().put(SESSION_ERRORS, errorMessages);
	}

	/**
	 * @see SessionValidationAware#getSessionErrors()
	 */
	public synchronized Map<String, String> getSessionErrors() {
		Map<String, String> errorMessages = (Map<String, String>)getSession().get(SESSION_ERRORS);
		if (errorMessages  == null) {
			errorMessages = new LinkedHashMap<String, String>();
			setSessionErrors(errorMessages);
		}
		return errorMessages;
	}

	/**
	 * @see SessionValidationAware#setSessionWarnings(java.util.Map)
	 */
	public synchronized void setSessionWarnings(Map<String, String> sessionWarnings) {
		getSession().put(SESSION_WARNINGS, sessionWarnings);
	}

	/**
	 * @see SessionValidationAware#getSessionWarnings()
	 */
	public synchronized Map<String, String> getSessionWarnings() {
		Map<String, String> warnMessages = (Map<String, String>)getSession().get(SESSION_WARNINGS);
		if (warnMessages  == null) {
			warnMessages = new LinkedHashMap<String, String>();
			setSessionWarnings(warnMessages);
		}
		return warnMessages;
	}

	/**
	 * @see SessionValidationAware#setSessionConfirms(java.util.Map)
	 */
	public synchronized void setSessionConfirms(Map<String, String> sessionConfirms) {
		getSession().put(SESSION_CONFIRMS, sessionConfirms);
	}

	/**
	 * @see SessionValidationAware#getSessionConfirms()
	 */
	public synchronized Map<String, String> getSessionConfirms() {
		Map<String, String> confMessages = (Map<String, String>)getSession().get(SESSION_CONFIRMS);
		if (confMessages  == null) {
			confMessages = new LinkedHashMap<String, String>();
			setSessionConfirms(confMessages);
		}
		return confMessages;
	}

	/**
	 * @see SessionValidationAware#setSessionMessages(java.util.Map)
	 */
	public synchronized void setSessionMessages(Map<String, String> messages) {
		getSession().put(SESSION_MESSAGES, messages);
	}

	/**
	 * @see SessionValidationAware#getSessionMessages()
	 */
	public synchronized Map<String, String> getSessionMessages() {
		Map<String, String> messages = (Map<String, String>)getSession().get(SESSION_MESSAGES);
		if (messages  == null) {
			messages = new LinkedHashMap<String, String>();
			setSessionMessages(messages);
		}
		return messages;
	}

	/**
	 * @see SessionValidationAware#addSessionError(java.lang.String, java.lang.String)
	 */
	public synchronized void addSessionError(String id, String anErrorMessage) {
		getSessionErrors().put(id, anErrorMessage);
	}

	/**
	 * @see SessionValidationAware#addSessionWarning(java.lang.String,
	 *      java.lang.String)
	 */
	public synchronized void addSessionWarning(String id, String aWarningMessage) {
		getSessionWarnings().put(id, aWarningMessage);
	}

	/**
	 * @see SessionValidationAware#addSessionConfirm(java.lang.String,
	 *      java.lang.String)
	 */
	public synchronized void addSessionConfirm(String id, String aConfirmMessage) {
		getSessionConfirms().put(id, aConfirmMessage);
	}

	/**
	 * @see SessionValidationAware#addSessionMessage(java.lang.String, java.lang.String)
	 */
	public synchronized void addSessionMessage(String id, String aMessage) {
		getSessionMessages().put(id, aMessage);
	}

	/**
	 * @see SessionValidationAware#removeSessionError(java.lang.String)
	 */
	public String removeSessionError(String id) {
		return getSessionErrors().remove(id);
	}

	/**
	 * @see SessionValidationAware#removeSessionWarning(java.lang.String)
	 */
	public String removeSessionWarning(String id) {
		return getSessionWarnings().remove(id);
	}

	/**
	 * @see SessionValidationAware#removeSessionConfirm(java.lang.String)
	 */
	public String removeSessionConfirm(String id) {
		return getSessionConfirms().remove(id);
	}

	/**
	 * @see SessionValidationAware#removeSessionMessage(java.lang.String)
	 */
	public String removeSessionMessage(String id) {
		return getSessionMessages().remove(id);
	}

	/**
	 * @see SessionValidationAware#hasSessionError(java.lang.String)
	 */
	public synchronized boolean hasSessionError(String id) {
		return getSessionErrors().containsKey(id);
	}

	/**
	 * @see SessionValidationAware#hasSessionWarning(java.lang.String)
	 */
	public synchronized boolean hasSessionWarning(String id) {
		return getSessionWarnings().containsKey(id);
	}

	/**
	 * @see SessionValidationAware#hasSessionConfirm(java.lang.String)
	 */
	public synchronized boolean hasSessionConfirm(String id) {
		return getSessionConfirms().containsKey(id);
	}

	/**
	 * @see SessionValidationAware#hasSessionMessage(java.lang.String)
	 */
	public synchronized boolean hasSessionMessage(String id) {
		return getSessionMessages().containsKey(id);
	}

	/**
	 * @see SessionValidationAware#hasSessionErrors()
	 */
	public synchronized boolean hasSessionErrors() {
		Map<String, String> errorMessages = (Map<String, String>)getSession().get(SESSION_ERRORS);
		return errorMessages != null && !errorMessages.isEmpty();
	}

	/**
	 * @see SessionValidationAware#hasSessionWarnings()
	 */
	public synchronized boolean hasSessionWarnings() {
		Map<String, String> warnMessages = (Map<String, String>)getSession().get(SESSION_WARNINGS);
		return warnMessages != null && !warnMessages.isEmpty();
	}

	/**
	 * @see SessionValidationAware#hasSessionConfirms()
	 */
	public synchronized boolean hasSessionConfirms() {
		Map<String, String> confMessages = (Map<String, String>)getSession().get(SESSION_CONFIRMS);
		return confMessages != null && !confMessages.isEmpty();
	}

	/**
	 * @see SessionValidationAware#hasSessionMessages()
	 */
	public synchronized boolean hasSessionMessages() {
		Map<String, String> messages = (Map<String, String>)getSession().get(SESSION_MESSAGES);
		return messages != null && !messages.isEmpty();
	}

	/**
	 * @return true if has messages
	 */
	public synchronized boolean hasMessages() {
		return (hasSessionWarnings() || hasSessionConfirms() || hasSessionMessages());
	}

	/**
	 * Clears session errors map.
	 * <p/>
	 * Will clear the map that contains session errors.
	 */
	public synchronized void clearSessionErrors() {
		getSessionErrors().clear();
	}

	/**
	 * Clears session warning map.
	 * <p/>
	 * Will clear the map that contains session warning.
	 */
	public synchronized void clearSessionWarnings() {
		getSessionWarnings().clear();
	}

	/**
	 * Clears session confirm map.
	 * <p/>
	 * Will clear the map that contains session confirm.
	 */
	public synchronized void clearSessionConfirms() {
		getSessionConfirms().clear();
	}

	/**
	 * Clears session messages map.
	 * <p/>
	 * Will clear the map that contains session messages.
	 */
	public synchronized void clearSessionMessages() {
		getSessionMessages().clear();
	}

	/**
	 * Clears messages.
	 * <p/>
	 * Will clear the maps.
	 */
	public synchronized void clearMessages() {
		getSessionWarnings().clear();
		getSessionConfirms().clear();
		getSessionMessages().clear();
	}

	/**
	 * Clears all error and messages.
	 * <p/>
	 * Will clear all the maps.
	 */
	public synchronized void clearErrorsAndMessages() {
		getSessionErrors().clear();
		getSessionWarnings().clear();
		getSessionConfirms().clear();
		getSessionMessages().clear();
	}
}
