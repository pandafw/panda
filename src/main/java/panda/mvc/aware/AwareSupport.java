package panda.mvc.aware;

import java.util.ArrayList;
import java.util.Collection;

import panda.lang.Collections;

/**
 * Provides a default implementation of ValidationAware. Returns new collections for errors
 * and messages.
 */
public class AwareSupport implements Aware {

	private Collection<String> errors;
	private Collection<String> warnings;
	private Collection<String> confirms;
	private Collection<String> messages;

	/**
	 * @see panda.mvc.aware.Aware#getErrors()
	 */
	public Collection<String> getErrors() {
		return errors;
	}

	protected Collection<String> _getErrors() {
		if (errors == null) {
			errors = new ArrayList<String>();
		}
		return errors;
	}

	/**
	 * @see panda.mvc.aware.Aware#setErrors(java.util.Collection)
	 */
	public void setErrors(Collection<String> errors) {
		this.errors = errors;
	}

	/**
	 * @see panda.mvc.aware.Aware#addError(java.lang.String)
	 */
	public void addError(String error) {
		_getErrors().add(error);
	}

	/**
	 * @see panda.mvc.aware.Aware#getWarnings()
	 */
	public Collection<String> getWarnings() {
		return warnings;
	}

	protected Collection<String> _getWarnings() {
		if (warnings == null) {
			warnings = new ArrayList<String>();
		}
		return warnings;
	}

	/**
	 * @see panda.mvc.aware.Aware#setWarnings(java.util.Collection)
	 */
	public void setWarnings(Collection<String> warnings) {
		this.warnings = warnings;
	}

	/**
	 * @see panda.mvc.aware.Aware#addWarning(java.lang.String)
	 */
	public void addWarning(String warning) {
		_getWarnings().add(warning);
	}

	/**
	 * @see panda.mvc.aware.Aware#getConfirms()
	 */
	public Collection<String> getConfirms() {
		return confirms;
	}

	protected Collection<String> _getConfirms() {
		if (confirms == null) {
			confirms = new ArrayList<String>();
		}
		return confirms;
	}

	/**
	 * @see panda.mvc.aware.Aware#setConfirms(java.util.Collection)
	 */
	public void setConfirms(Collection<String> confirms) {
		this.confirms = confirms;
	}

	/**
	 * @see panda.mvc.aware.Aware#addConfirm(java.lang.String)
	 */
	public void addConfirm(String confirm) {
		_getConfirms().add(confirm);
	}

	/**
	 * @see panda.mvc.aware.Aware#getMessages()
	 */
	public Collection<String> getMessages() {
		return messages;
	}

	protected Collection<String> _getMessages() {
		if (messages == null) {
			messages = new ArrayList<String>();
		}
		return messages;
	}

	/**
	 * @see panda.mvc.aware.Aware#setMessages(java.util.Collection)
	 */
	public void setMessages(Collection<String> messages) {
		this.messages = messages;
	}

	/**
	 * @see panda.mvc.aware.Aware#addMessage(java.lang.String)
	 */
	public void addMessage(String message) {
		_getMessages().add(message);
	}

	/**
	 * @see panda.mvc.aware.Aware#hasErrors()
	 */
	public boolean hasErrors() {
		return Collections.isNotEmpty(errors);
	}

	/**
	 * @see panda.mvc.aware.Aware#hasWarnings()
	 */
	public boolean hasWarnings() {
		return Collections.isNotEmpty(warnings);
	}

	/**
	 * @see panda.mvc.aware.Aware#hasConfirms()
	 */
	public boolean hasConfirms() {
		return Collections.isNotEmpty(confirms);
	}

	/**
	 * @see panda.mvc.aware.Aware#hasMessages()
	 */
	public boolean hasMessages() {
		return Collections.isNotEmpty(messages);
	}

	/**
	 * @see panda.mvc.aware.Aware#isEmpty()
	 */
	public boolean isEmpty() {
		return Collections.isEmpty(errors) 
				&& Collections.isEmpty(warnings) 
				&& Collections.isEmpty(confirms) 
				&& Collections.isEmpty(messages);
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
