package panda.mvc.validator;

import panda.lang.Strings;
import panda.mvc.ActionContext;


public class RequiredStringFieldValidator extends AbstractStringFieldValidator {

	@Override
	public boolean validate(ActionContext ac, Object object) throws ValidationException {
		String value = trimFieldValue(object);
		return validateString(ac, object, value);
	}

	@Override
	protected boolean validateString(ActionContext ac, Object object, String value) throws ValidationException {
		if (Strings.isNotEmpty(value)) {
			return true;
		}
		
		addFieldError(ac, getName(), value);
		return false;
	}
}
