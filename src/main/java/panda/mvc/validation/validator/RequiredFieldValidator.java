package panda.mvc.validation.validator;

import panda.mvc.ActionContext;

public class RequiredFieldValidator extends AbstractFieldValidator {

	public boolean validate(ActionContext ac, Object object) {
		Object value = getFieldValue(object, getName());

		if (value != null) {
			return true;
		}
		
		addFieldError(ac, getName(), value);
		return false;
	}
}
