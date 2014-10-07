package panda.mvc.validation.aware;

import java.util.ArrayList;
import java.util.Collection;

import panda.lang.Collections;

/**
 * Provides a default implementation of ValidationAware. Returns new collections for errors
 * and messages.
 */
public class ValidationAwareSupport implements ValidationAware {

	private Collection<String> errors;
	private Collection<String> warnings;
	private Collection<String> confirms;
	private Collection<String> messages;

	/**
	 * @see panda.mvc.validation.aware.ValidationAware#getErrors()
	 */
	public Collection<String> getErrors() {
		if (errors == null) {
			errors = new ArrayList<String>();
		}
		return errors;
	}

	/**
	 * @see panda.mvc.validation.aware.ValidationAware#setErrors(java.util.Collection)
	 */
	public void setErrors(Collection<String> errors) {
		this.errors = errors;
	}

	/**
	 * @see panda.mvc.validation.aware.ValidationAware#addError(java.lang.String)
	 */
	public void addError(String error) {
		getErrors().add(error);
	}

	/**
	 * @see panda.mvc.validation.aware.ValidationAware#getWarnings()
	 */
	public Collection<String> getWarnings() {
		if (warnings == null) {
			warnings = new ArrayList<String>();
		}
		return warnings;
	}

	/**
	 * @see panda.mvc.validation.aware.ValidationAware#setWarnings(java.util.Collection)
	 */
	public void setWarnings(Collection<String> warnings) {
		this.warnings = warnings;
	}

	/**
	 * @see panda.mvc.validation.aware.ValidationAware#addWarning(java.lang.String)
	 */
	public void addWarning(String warning) {
		getWarnings().add(warning);
	}

	/**
	 * @see panda.mvc.validation.aware.ValidationAware#getConfirms()
	 */
	public Collection<String> getConfirms() {
		if (confirms == null) {
			confirms = new ArrayList<String>();
		}
		return confirms;
	}

	/**
	 * @see panda.mvc.validation.aware.ValidationAware#setConfirms(java.util.Collection)
	 */
	public void setConfirms(Collection<String> confirms) {
		this.confirms = confirms;
	}

	/**
	 * @see panda.mvc.validation.aware.ValidationAware#addConfirm(java.lang.String)
	 */
	public void addConfirm(String confirm) {
		getConfirms().add(confirm);
	}

	/**
	 * @see panda.mvc.validation.aware.ValidationAware#getMessages()
	 */
	public Collection<String> getMessages() {
		if (messages == null) {
			messages = new ArrayList<String>();
		}
		return messages;
	}

	/**
	 * @see panda.mvc.validation.aware.ValidationAware#setMessages(java.util.Collection)
	 */
	public void setMessages(Collection<String> messages) {
		this.messages = messages;
	}

	/**
	 * @see panda.mvc.validation.aware.ValidationAware#addMessage(java.lang.String)
	 */
	public void addMessage(String message) {
		getMessages().add(message);
	}

	/**
	 * @see panda.mvc.validation.aware.ValidationAware#hasErrors()
	 */
	public boolean hasErrors() {
		return Collections.isNotEmpty(errors);
	}

	/**
	 * @see panda.mvc.validation.aware.ValidationAware#hasWarnings()
	 */
	public boolean hasWarnings() {
		return Collections.isNotEmpty(warnings);
	}

	/**
	 * @see panda.mvc.validation.aware.ValidationAware#hasConfirms()
	 */
	public boolean hasConfirms() {
		return Collections.isNotEmpty(confirms);
	}

	/**
	 * @see panda.mvc.validation.aware.ValidationAware#hasMessages()
	 */
	public boolean hasMessages() {
		return Collections.isNotEmpty(messages);
	}

	/**
	 * Clears all error list.
	 * <p/>
	 * Will clear the list that contain errors.
	 */
	public void clearErrors() {
		Collections.clear(errors);
	}

	/**
	 * Clears warning list.
	 * <p/>
	 * Will clear the list that contains warning.
	 */
	public void clearWarnings() {
		Collections.clear(warnings);
	}

	/**
	 * Clears confirm list.
	 * <p/>
	 * Will clear the list that contains confirm.
	 */
	public void clearConfirms() {
		Collections.clear(confirms);
	}

	/**
	 * Clears messages list.
	 * <p/>
	 * Will clear the list that contains messages.
	 */
	public void clearMessages() {
		Collections.clear(messages);
	}

	/**
	 * Clear all.
	 */
	public void clear() {
		clearErrors();
		clearWarnings();
		clearConfirms();
		clearMessages();
	}
}
