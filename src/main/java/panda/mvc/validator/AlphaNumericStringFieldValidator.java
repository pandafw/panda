package panda.mvc.validator;

import panda.lang.Strings;
import panda.mvc.ActionContext;

public class AlphaNumericStringFieldValidator extends AbstractStringFieldValidator {

	@Override
	protected boolean validateString(ActionContext ac, Object object, String value) throws ValidationException {
		if (Strings.isAlphanumeric(value)) {
			return true;
		}
		
		addFieldError(ac, getName(), value);
		return false;
	}
}
