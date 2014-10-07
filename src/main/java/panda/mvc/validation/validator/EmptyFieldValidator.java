package panda.mvc.validation.validator;

import panda.lang.Objects;
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

		if (!Objects.isEmpty(value)) {
			return true;
		}
		
		addFieldError(ac, getName(), value);
		return false;
	}
}
