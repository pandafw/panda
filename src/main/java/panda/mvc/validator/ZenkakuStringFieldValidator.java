package panda.mvc.validator;

import panda.lang.AsiaStrings;
import panda.mvc.ActionContext;

public class ZenkakuStringFieldValidator extends AbstractStringFieldValidator {

	@Override
	protected boolean validateString(ActionContext ac, Object object, String value) throws ValidationException {
		if (AsiaStrings.isZenkakuString(value)) {
			return true;
		}

		addFieldError(ac, getName(), value);
		return false;
	}

}
