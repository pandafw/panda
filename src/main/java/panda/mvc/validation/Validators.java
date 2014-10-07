package panda.mvc.validation;

import panda.mvc.ActionContext;


public interface Validators {
	/**
	 * @param ac action context
	 */
	void validate(ActionContext ac);

	/**
	 * @param ac action context
	 * @param name parameter name
	 * @param object parameter value
	 * @return true to continue
	 */
	boolean validate(ActionContext ac, String name, Object object);
}
