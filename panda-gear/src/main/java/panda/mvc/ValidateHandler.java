package panda.mvc;

import panda.mvc.annotation.Validate;


public interface ValidateHandler {
	/**
	 * validate value
	 * @param ac action context
	 * @param name field name
	 * @param value field value
	 * @return validate result
	 */
	boolean validate(ActionContext ac, String name, Object value);

	/**
	 * validate value
	 * @param ac action context
	 * @param parent parent validator
	 * @param name field name
	 * @param value field value
	 * @return validate result
	 */
	boolean validate(ActionContext ac, Validator parent, String name, Object value);

	/**
	 * validate value
	 * @param ac action context
	 * @param parent parent validator
	 * @param name field name
	 * @param value field value
	 * @param vs validate annotation array
	 * @return validate result
	 */
	boolean validate(ActionContext ac, Validator parent, String name, Object value, Validate[] vs);
	
}
