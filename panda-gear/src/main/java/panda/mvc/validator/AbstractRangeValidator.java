package panda.mvc.validator;

import panda.mvc.ActionContext;

/**
 * Base class for range based validators.
 * @param <T> comparable type
 */
public abstract class AbstractRangeValidator<T extends Comparable> extends AbstractValidator {

	protected abstract T getMax();

	protected abstract T getMin();

	@SuppressWarnings("unchecked")
	@Override
	protected boolean validateValue(ActionContext ac, Object val) {
		// if there is no value - don't do comparison
		// if a value is required, a required validator should be added to the field
		if (val == null) {
			return true;
		}

		Comparable value = (Comparable)val;

		// only check for a minimum value if the min parameter is set
		if ((getMin() != null) && (value.compareTo(getMin()) < 0)) {
			addFieldError(ac);
			return false;
		}
		
		if ((getMax() != null) && (value.compareTo(getMax()) > 0)) {
			addFieldError(ac);
			return false;
		}
		
		return true;
	}
}
