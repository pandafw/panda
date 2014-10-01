package panda.mvc.validator;

import panda.mvc.ActionContext;

/**
 * Base class for range based validators.
 */
public abstract class AbstractRangeValidator extends AbstractFieldValidator {

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
		if ((getMinComparatorValue() != null) && (value.compareTo(getMinComparatorValue()) < 0)) {
			addFieldError(ac, getName(), value);
			return false;
		}
		
		if ((getMaxComparatorValue() != null) && (value.compareTo(getMaxComparatorValue()) > 0)) {
			addFieldError(ac, getName(), object);
			return false;
		}
		
		return true;
	}

	protected abstract Comparable getMaxComparatorValue();

	protected abstract Comparable getMinComparatorValue();
}
