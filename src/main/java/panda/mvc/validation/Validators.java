package panda.mvc.validation;

import panda.mvc.ActionContext;


public interface Validators {
	/**
	 * @param ac action context
	 * @param name parameter name
	 * @param object parameter value
	 * @return true to continue
	 */
	boolean valiate(ActionContext ac, String name, Object object);
}
