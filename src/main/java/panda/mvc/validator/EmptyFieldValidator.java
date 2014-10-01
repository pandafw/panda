package panda.mvc.validator;

import panda.mvc.ActionContext;



/**
 * empty field validator.
 */
public class EmptyFieldValidator extends AbstractFieldValidator {

	public boolean validate(ActionContext ac, Object object) {
		Object value = getFieldValue(object, getName());

		if (value == null) {
			return true;
		}

		if (value.getClass().isArray()) {
			if (((Object[])value).length == 0) {
				return true;
			}
		}
		else if (value instanceof String) {
			if (((String) value).length() == 0) {
				return true;
			}
		}

		addFieldError(ac, getName(), value);
		return false;
	}
}
