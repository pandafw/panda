package panda.mvc.util;

import java.util.LinkedHashMap;
import java.util.Map;

import panda.ioc.annotation.IocInject;
import panda.mvc.ActionContext;
import panda.servlet.ServletContextMap;

/**
 * Provides a default implementation of ApplicationValidationAware. Returns collections for errors
 * and messages.
 */
@SuppressWarnings("unchecked")
public class ApplicationValidationAwareSupport implements ApplicationValidationAware, PandaXWorkConstants {
	private Map<String, Object> application;

	@IocInject
	public void setActionContext(ActionContext context) {
		application = new ServletContextMap(context.getServlet());
	}
	
	private Map<String, Object> getApplication() {
		return application;
	}
	
	/**
	 * @see ApplicationValidationAware#setApplicationErrors(java.util.Map)
	 */
	public synchronized void setApplicationErrors(Map<String, String> errorMessages) {
		getApplication().put(APPLICATION_ERRORS, errorMessages);
	}

	/**
	 * @see ApplicationValidationAware#getApplicationErrors()
	 */
	public synchronized Map<String, String> getApplicationErrors() {
		Map<String, String> errorMessages = (Map<String, String>)getApplication().get(APPLICATION_ERRORS);
		if (errorMessages  == null) {
			errorMessages = new LinkedHashMap<String, String>();
			setApplicationErrors(errorMessages);
		}
		return errorMessages;
	}

	/**
	 * @see ApplicationValidationAware#setApplicationWarnings(java.util.Map)
	 */
	public synchronized void setApplicationWarnings(Map<String, String> applicationWarnings) {
		getApplication().put(APPLICATION_WARNINGS, applicationWarnings);
	}

	/**
	 * @see ApplicationValidationAware#getApplicationWarnings()
	 */
	public synchronized Map<String, String> getApplicationWarnings() {
		Map<String, String> warnMessages = (Map<String, String>)getApplication().get(APPLICATION_WARNINGS);
		if (warnMessages  == null) {
			warnMessages = new LinkedHashMap<String, String>();
			setApplicationWarnings(warnMessages);
		}
		return warnMessages;
	}

	/**
	 * @see ApplicationValidationAware#setApplicationConfirms(java.util.Map)
	 */
	public synchronized void setApplicationConfirms(Map<String, String> applicationConfirms) {
		getApplication().put(APPLICATION_CONFIRMS, applicationConfirms);
	}

	/**
	 * @see ApplicationValidationAware#getApplicationConfirms()
	 */
	public synchronized Map<String, String> getApplicationConfirms() {
		Map<String, String> confMessages = (Map<String, String>)getApplication().get(APPLICATION_CONFIRMS);
		if (confMessages  == null) {
			confMessages = new LinkedHashMap<String, String>();
			setApplicationConfirms(confMessages);
		}
		return confMessages;
	}

	/**
	 * @see ApplicationValidationAware#setApplicationMessages(java.util.Map)
	 */
	public synchronized void setApplicationMessages(Map<String, String> messages) {
		getApplication().put(APPLICATION_MESSAGES, messages);
	}

	/**
	 * @see ApplicationValidationAware#getApplicationMessages()
	 */
	public synchronized Map<String, String> getApplicationMessages() {
		Map<String, String> messages = (Map<String, String>)getApplication().get(APPLICATION_MESSAGES);
		if (messages  == null) {
			messages = new LinkedHashMap<String, String>();
			setApplicationMessages(messages);
		}
		return messages;
	}

	/**
	 * @see ApplicationValidationAware#addApplicationError(java.lang.String, java.lang.String)
	 */
	public synchronized void addApplicationError(String id, String anErrorMessage) {
		getApplicationErrors().put(id, anErrorMessage);
	}

	/**
	 * @see ApplicationValidationAware#addApplicationWarning(java.lang.String,
	 *      java.lang.String)
	 */
	public synchronized void addApplicationWarning(String id, String aWarningMessage) {
		getApplicationWarnings().put(id, aWarningMessage);
	}

	/**
	 * @see ApplicationValidationAware#addApplicationConfirm(java.lang.String,
	 *      java.lang.String)
	 */
	public synchronized void addApplicationConfirm(String id, String aConfirmMessage) {
		getApplicationConfirms().put(id, aConfirmMessage);
	}

	/**
	 * @see ApplicationValidationAware#addApplicationMessage(java.lang.String, java.lang.String)
	 */
	public synchronized void addApplicationMessage(String id, String aMessage) {
		getApplicationMessages().put(id, aMessage);
	}

	/**
	 * @see ApplicationValidationAware#removeApplicationError(java.lang.String)
	 */
	public String removeApplicationError(String id) {
		return getApplicationErrors().remove(id);
	}

	/**
	 * @see ApplicationValidationAware#removeApplicationWarning(java.lang.String)
	 */
	public String removeApplicationWarning(String id) {
		return getApplicationWarnings().remove(id);
	}

	/**
	 * @see ApplicationValidationAware#removeApplicationConfirm(java.lang.String)
	 */
	public String removeApplicationConfirm(String id) {
		return getApplicationConfirms().remove(id);
	}

	/**
	 * @see ApplicationValidationAware#removeApplicationMessage(java.lang.String)
	 */
	public String removeApplicationMessage(String id) {
		return getApplicationMessages().remove(id);
	}

	/**
	 * @see ApplicationValidationAware#hasApplicationError(java.lang.String)
	 */
	public synchronized boolean hasApplicationError(String id) {
		return getApplicationErrors().containsKey(id);
	}

	/**
	 * @see ApplicationValidationAware#hasApplicationWarning(java.lang.String)
	 */
	public synchronized boolean hasApplicationWarning(String id) {
		return getApplicationWarnings().containsKey(id);
	}

	/**
	 * @see ApplicationValidationAware#hasApplicationConfirm(java.lang.String)
	 */
	public synchronized boolean hasApplicationConfirm(String id) {
		return getApplicationConfirms().containsKey(id);
	}

	/**
	 * @see ApplicationValidationAware#hasApplicationMessage(java.lang.String)
	 */
	public synchronized boolean hasApplicationMessage(String id) {
		return getApplicationMessages().containsKey(id);
	}

	/**
	 * @see ApplicationValidationAware#hasApplicationErrors()
	 */
	public synchronized boolean hasApplicationErrors() {
		Map<String, String> errorMessages = (Map<String, String>)getApplication().get(APPLICATION_ERRORS);
		return errorMessages != null && !errorMessages.isEmpty();
	}

	/**
	 * @see ApplicationValidationAware#hasApplicationWarnings()
	 */
	public synchronized boolean hasApplicationWarnings() {
		Map<String, String> warnMessages = (Map<String, String>)getApplication().get(APPLICATION_WARNINGS);
		return warnMessages != null && !warnMessages.isEmpty();
	}

	/**
	 * @see ApplicationValidationAware#hasApplicationConfirms()
	 */
	public synchronized boolean hasApplicationConfirms() {
		Map<String, String> confMessages = (Map<String, String>)getApplication().get(APPLICATION_CONFIRMS);
		return confMessages != null && !confMessages.isEmpty();
	}

	/**
	 * @see ApplicationValidationAware#hasApplicationMessages()
	 */
	public synchronized boolean hasApplicationMessages() {
		Map<String, String> messages = (Map<String, String>)getApplication().get(APPLICATION_MESSAGES);
		return messages != null && !messages.isEmpty();
	}

	/**
	 * @return true if has messages
	 */
	public synchronized boolean hasMessages() {
		return (hasApplicationWarnings() || hasApplicationConfirms() || hasApplicationMessages());
	}

	/**
	 * Clears application errors map.
	 * <p/>
	 * Will clear the map that contains application errors.
	 */
	public synchronized void clearApplicationErrors() {
		getApplicationErrors().clear();
	}

	/**
	 * Clears application warning map.
	 * <p/>
	 * Will clear the map that contains application warning.
	 */
	public synchronized void clearApplicationWarnings() {
		getApplicationWarnings().clear();
	}

	/**
	 * Clears application confirm map.
	 * <p/>
	 * Will clear the map that contains application confirm.
	 */
	public synchronized void clearApplicationConfirms() {
		getApplicationConfirms().clear();
	}

	/**
	 * Clears application messages map.
	 * <p/>
	 * Will clear the map that contains application messages.
	 */
	public synchronized void clearApplicationMessages() {
		getApplicationMessages().clear();
	}

	/**
	 * Clears messages.
	 * <p/>
	 * Will clear the maps.
	 */
	public synchronized void clearMessages() {
		getApplicationWarnings().clear();
		getApplicationConfirms().clear();
		getApplicationMessages().clear();
	}

	/**
	 * Clears all error and messages.
	 * <p/>
	 * Will clear all the maps.
	 */
	public synchronized void clearErrorsAndMessages() {
		getApplicationErrors().clear();
		getApplicationWarnings().clear();
		getApplicationConfirms().clear();
		getApplicationMessages().clear();
	}
}
