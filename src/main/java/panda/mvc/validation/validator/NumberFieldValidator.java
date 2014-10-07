package panda.mvc.validation.validator;

import panda.lang.Classes;
import panda.mvc.ActionContext;

public class NumberFieldValidator extends AbstractFieldValidator {

	private Number max = null;
	private Number min = null;

	/**
	 * @return the max
	 */
	public Number getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(Number max) {
		this.max = max;
	}

	/**
	 * @return the min
	 */
	public Number getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(Number min) {
		this.min = min;
	}

	@Override
	public boolean validate(ActionContext ac, Object object) throws ValidationException {
		Object value = getFieldValue(object, getName());
		Number num = (Number)value;

		// if there is no value - don't do comparison
		// if a value is required, a required validator should be added to the field
		if (num == null) {
			return true;
		}
		
		if (validateNumber(num)) {
			return true;
		}

		addFieldError(ac, getName(), value);
		return false;
	}
	
	protected boolean validateNumber(Number num) {
		// only check for a minimum value if the min parameter is set
		if (min != null) {
			if (Classes.isFloatLike(num.getClass()) || Classes.isFloatLike(min.getClass())) {
				if (num.doubleValue() < min.doubleValue()) {
					return false;
				}
			}
			else {
				if (num.longValue() < min.longValue()) {
					return false;
				}
			}
		}

		// only check for a maximum value if the max parameter is set
		if (max != null) {
			if (Classes.isFloatLike(num.getClass()) || Classes.isFloatLike(max.getClass())) {
				if (num.doubleValue() > max.doubleValue()) {
					return false;
				}
			}
			else {
				if (num.longValue() > max.longValue()) {
					return false;
				}
			}
		}

		return true;
	}
}
