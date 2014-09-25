package panda.mvc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides a default implementation of ActionValidationAware. Returns new collections for errors
 * and messages (defensive copy).
 */
public class ActionValidationAwareSupport implements ActionValidationAware {

	private Map<String, List<String>> fieldErrors;
	private Collection<String> actionErrors;
	private Collection<String> actionWarnings;
	private Collection<String> actionConfirms;
	private Collection<String> actionMessages;

	/**
	 * @see panda.mvc.util.ActionValidationAware#setActionErrors(java.util.Collection)
	 */
	public void setActionErrors(Collection<String> errorMessages) {
		this.actionErrors = errorMessages;
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#getActionErrors()
	 */
	public Collection<String> getActionErrors() {
		if (actionErrors == null) {
			actionErrors = new ArrayList<String>();
		}
		return actionErrors;
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#setActionWarnings(java.util.Collection)
	 */
	public void setActionWarnings(Collection<String> actionWarnings) {
		this.actionWarnings = actionWarnings;
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#getActionWarnings()
	 */
	public Collection<String> getActionWarnings() {
		if (actionWarnings == null) {
			actionWarnings = new ArrayList<String>();
		}
		return actionWarnings;
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#setActionConfirms(java.util.Collection)
	 */
	public void setActionConfirms(Collection<String> actionConfirms) {
		this.actionConfirms = actionConfirms;
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#getActionConfirms()
	 */
	public Collection<String> getActionConfirms() {
		if (actionConfirms == null) {
			actionConfirms = new ArrayList<String>();
		}
		return actionConfirms;
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#setActionMessages(java.util.Collection)
	 */
	public void setActionMessages(Collection<String> messages) {
		this.actionMessages = messages;
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#getActionMessages()
	 */
	public Collection<String> getActionMessages() {
		if (actionMessages == null) {
			actionMessages = new ArrayList<String>();
		}
		return actionMessages;
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#setFieldErrors(java.util.Map)
	 */
	public void setFieldErrors(Map<String, List<String>> errorMap) {
		this.fieldErrors = errorMap;
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#getFieldErrors()
	 */
	public Map<String, List<String>> getFieldErrors() {
		if (fieldErrors == null) {
			fieldErrors = new LinkedHashMap<String, List<String>>();
		}
		return fieldErrors;
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#addFieldError(java.lang.String,
	 *      java.lang.String)
	 */
	public void addFieldError(String fieldName, String errorMessage) {
		final Map<String, List<String>> errors = getFieldErrors();
		List<String> thisFieldErrors = errors.get(fieldName);

		if (thisFieldErrors == null) {
			thisFieldErrors = new ArrayList<String>();
			errors.put(fieldName, thisFieldErrors);
		}

		thisFieldErrors.add(errorMessage);
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#addActionError(java.lang.String)
	 */
	public void addActionError(String anErrorMessage) {
		getActionErrors().add(anErrorMessage);
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#addActionWarning(java.lang.String)
	 */
	public void addActionWarning(String aWarningMessage) {
		getActionWarnings().add(aWarningMessage);
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#addActionConfirm(java.lang.String)
	 */
	public void addActionConfirm(String aConfirmMessage) {
		getActionConfirms().add(aConfirmMessage);
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#addActionMessage(java.lang.String)
	 */
	public void addActionMessage(String aMessage) {
		getActionMessages().add(aMessage);
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#hasFieldErrors()
	 */
	public boolean hasFieldErrors() {
		return (fieldErrors != null) && !fieldErrors.isEmpty();
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#hasActionErrors()
	 */
	public boolean hasActionErrors() {
		return (actionErrors != null) && !actionErrors.isEmpty();
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#hasErrors()
	 */
	public boolean hasErrors() {
		return (hasActionErrors() || hasFieldErrors());
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#hasActionWarnings()
	 */
	public boolean hasActionWarnings() {
		return (actionWarnings != null) && !actionWarnings.isEmpty();
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#hasActionConfirms()
	 */
	public boolean hasActionConfirms() {
		return (actionConfirms != null) && !actionConfirms.isEmpty();
	}

	/**
	 * @see panda.mvc.util.ActionValidationAware#hasActionMessages()
	 */
	public boolean hasActionMessages() {
		return (actionMessages != null) && !actionMessages.isEmpty();
	}

	/**
	 * @return true if has messages
	 */
	public boolean hasMessages() {
		return (hasActionWarnings() || hasActionConfirms() || hasActionMessages());
	}

	/**
	 * Clears field errors Map.
	 * <p/>
	 * Will clear the Map that contains field errors.
	 */
	public void clearFieldErrors() {
		getFieldErrors().clear();
	}

	/**
	 * Clears action errors list.
	 * <p/>
	 * Will clear the list that contains action errors.
	 */
	public void clearActionErrors() {
		getActionErrors().clear();
	}

	/**
	 * Clears all error list/maps.
	 * <p/>
	 * Will clear the Map and list that contain field errors and action errors.
	 */
	public void clearErrors() {
		getFieldErrors().clear();
		getActionErrors().clear();
	}

	/**
	 * Clears action warning list.
	 * <p/>
	 * Will clear the list that contains action warning.
	 */
	public void clearActionWarnings() {
		getActionWarnings().clear();
	}

	/**
	 * Clears action confirm list.
	 * <p/>
	 * Will clear the list that contains action confirm.
	 */
	public void clearActionConfirms() {
		getActionConfirms().clear();
	}

	/**
	 * Clears action messages list.
	 * <p/>
	 * Will clear the list that contains action messages.
	 */
	public void clearActionMessages() {
		getActionMessages().clear();
	}

	/**
	 * Clears messages list.
	 * <p/>
	 * Will clear the list that contains action messages.
	 */
	public void clearMessages() {
		getActionWarnings().clear();
		getActionConfirms().clear();
		getActionMessages().clear();
	}

	/**
	 * Clears all error and messages list/maps.
	 * <p/>
	 * Will clear the maps/lists that contain field errors, action errors and action messages.
	 */
	public void clearErrorsAndMessages() {
		getFieldErrors().clear();
		getActionErrors().clear();
		getActionWarnings().clear();
		getActionConfirms().clear();
		getActionMessages().clear();
	}
}
