package panda.mvc.validator;

import panda.lang.Strings;

import com.opensymphony.xwork2.validator.ValidationException;


/**
 * Compare field validator.
 */
public class CompareFieldValidator extends AbstractFieldValidator {

	private String target;
	private String comparator;

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * @return the comparator
	 */
	public String getComparator() {
		return comparator;
	}

	/**
	 * @param comparator the comparator to set
	 */
	public void setComparator(String comparator) {
		this.comparator = comparator;
	}


	/**
	 * @see com.opensymphony.xwork2.validator.Validator#validate(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public void validate(Object object) throws ValidationException {
		if (Strings.isEmpty(target)) {
			throw new ValidationException("target can not be empty");
		}
		
		Comparable targetValue = (Comparable)getFieldValue(target, object);
		Comparable fieldValue = (Comparable)getFieldValue(object, getName());

		if (fieldValue != null && targetValue != null) {
			int result = fieldValue.compareTo(targetValue);
			if ("eq".equals(comparator)) {
				if (result != 0) {
					addFieldError(ac, object, getName());
				}
			}
			else if ("le".equals(comparator)) {
				if (result > 0) {
					addFieldError(ac, object, getName());
				}
			}
			else if ("lt".equals(comparator)) {
				if (result >= 0) {
					addFieldError(ac, object, getName());
				}
			}
			else if ("ge".equals(comparator)) {
				if (result < 0) {
					addFieldError(ac, object, getName());
				}
			}
			else if ("gt".equals(comparator)) {
				if (result > 0) {
					addFieldError(ac, object, getName());
				}
			}
			else {
				throw new ValidationException("Invalid comparator [" + comparator + "]. Please set one of these values [ 'eq', 'le', 'lt', 'ge', 'gt' ].");
			}
		}
	}
}
