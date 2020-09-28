package panda.mvc.alert;

import java.util.ArrayList;
import java.util.Collection;

import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.lang.Collections;

@IocBean(type=ActionAlert.class, scope=Scope.REQUEST)
public class ActionAlertSupport implements ActionAlert {

	private Collection<String> errors;
	private Collection<String> warnings;
	private Collection<String> confirms;
	private Collection<String> messages;

	/**
	 * @see panda.mvc.alert.ActionAlert#getErrors()
	 */
	@Override
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
	 * @see panda.mvc.alert.ActionAlert#addError(java.lang.String)
	 */
	@Override
	public void addError(String error) {
		_getErrors().add(error);
	}

	/**
	 * @see panda.mvc.alert.ActionAlert#getWarnings()
	 */
	@Override
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
	 * @see panda.mvc.alert.ActionAlert#addWarning(java.lang.String)
	 */
	@Override
	public void addWarning(String warning) {
		_getWarnings().add(warning);
	}

	/**
	 * @see panda.mvc.alert.ActionAlert#getConfirms()
	 */
	@Override
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
	 * @see panda.mvc.alert.ActionAlert#addConfirm(java.lang.String)
	 */
	@Override
	public void addConfirm(String confirm) {
		_getConfirms().add(confirm);
	}

	/**
	 * @see panda.mvc.alert.ActionAlert#getMessages()
	 */
	@Override
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
	 * @see panda.mvc.alert.ActionAlert#addMessage(java.lang.String)
	 */
	@Override
	public void addMessage(String message) {
		_getMessages().add(message);
	}

	/**
	 * @see panda.mvc.alert.ActionAlert#hasErrors()
	 */
	@Override
	public boolean hasErrors() {
		return Collections.isNotEmpty(errors);
	}

	/**
	 * @see panda.mvc.alert.ActionAlert#hasWarnings()
	 */
	@Override
	public boolean hasWarnings() {
		return Collections.isNotEmpty(warnings);
	}

	/**
	 * @see panda.mvc.alert.ActionAlert#hasConfirms()
	 */
	@Override
	public boolean hasConfirms() {
		return Collections.isNotEmpty(confirms);
	}

	/**
	 * @see panda.mvc.alert.ActionAlert#hasMessages()
	 */
	@Override
	public boolean hasMessages() {
		return Collections.isNotEmpty(messages);
	}

	/**
	 * @see panda.mvc.alert.ActionAlert#hasContents()
	 */
	@Override
	public boolean hasContents() {
		return Collections.isNotEmpty(errors) 
				|| Collections.isNotEmpty(warnings) 
				|| Collections.isNotEmpty(confirms) 
				|| Collections.isNotEmpty(messages);
	}
	
	/**
	 * Removes all error list.
	 * <p/>
	 * Will remove the list that contain errors.
	 */
	@Override
	public void removeErrors() {
		Collections.clear(errors);
	}

	/**
	 * Removes warning list.
	 * <p/>
	 * Will remove the list that contains warning.
	 */
	@Override
	public void removeWarnings() {
		Collections.clear(warnings);
	}

	/**
	 * Removes confirm list.
	 * <p/>
	 * Will remove the list that contains confirm.
	 */
	@Override
	public void removeConfirms() {
		Collections.clear(confirms);
	}

	/**
	 * Removes messages list.
	 * <p/>
	 * Will remove the list that contains messages.
	 */
	@Override
	public void removeMessages() {
		Collections.clear(messages);
	}

	/**
	 * Clear all.
	 */
	@Override
	public void clear() {
		removeErrors();
		removeWarnings();
		removeConfirms();
		removeMessages();
	}
}
