package panda.mvc.validation;

import panda.mvc.ActionContext;
import panda.mvc.validation.annotation.Validate;
import panda.mvc.validation.validator.Validator;


public interface Validators {
	/**
	 * @param ac action context
	 * @return true if no validation errors
	 */
	boolean validate(ActionContext ac) throws ValidateException;

	/**
	 * create validator
	 * @param ac action context
	 * @param v validator annotation
	 * @return validator
	 */
	Validator createValidator(ActionContext ac, Validate v);
}
