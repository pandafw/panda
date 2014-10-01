package panda.mvc.validator;

import com.opensymphony.xwork2.validator.ValidationException;

/**
 * required image field validator.
 */
public class RequiredImageFieldValidator extends ImageFieldValidator {

	/**
	 * @see com.opensymphony.xwork2.validator.Validator#validate(java.lang.Object)
	 */
	public void validate(Object object) throws ValidationException {
		Object value = getFieldValue(object, getName());

		if (value == null) {
			addFieldError(ac, object, getName());
			return;
		}

		if (getImage(value) == null) {
			addFieldError(ac, object, getName());
		}
	}
}
