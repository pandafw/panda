package panda.mvc;

import java.lang.annotation.Annotation;


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
	 * @param vas validate annotations
	 * @return validate result
	 */
	boolean validate(ActionContext ac, Validator parent, String name, Object value, Annotation[] vas);
}
