package panda.mvc;

import panda.mvc.annotation.Validate;


public interface ValidatorCreator {
	/**
	 * create validator
	 * @param ac action context
	 * @param v validator annotation
	 * @return validator
	 */
	Validator create(ActionContext ac, Validate v);

	/**
	 * create validator
	 * @param ac action context
	 * @param name validator name
	 * @return validator
	 */
	Validator create(ActionContext ac, String name);
}
