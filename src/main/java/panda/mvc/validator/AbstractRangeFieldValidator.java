package panda.mvc.validator;

import panda.mvc.ActionContext;

/**
 * Base class for range based validators.
 */
public abstract class AbstractRangeFieldValidator<T extends Comparable> extends AbstractFieldValidator {

	protected abstract T getMax();

	protected abstract T getMin();

	@SuppressWarnings("unchecked")
	public boolean validate(ActionContext ac, Object object) throws ValidationException {
		Object obj = getFieldValue(object, getName());
		Comparable value = (Comparable)obj;

		// if there is no value - don't do comparison
		// if a value is required, a required validator should be added to the field
		if (value == null) {
			return true;
		}

		// only check for a minimum value if the min parameter is set
		if ((getMin() != null) && (value.compareTo(getMin()) < 0)) {
			addFieldError(ac, getName(), value);
			return false;
		}
		
		if ((getMax() != null) && (value.compareTo(getMax()) > 0)) {
			addFieldError(ac, getName(), object);
			return false;
		}
		
		return true;
	}
}
