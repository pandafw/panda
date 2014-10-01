package panda.mvc.validator;

import java.util.Collection;

import com.opensymphony.xwork2.validator.ValidationException;


/**
 * RequiredCollectionFieldValidator
 */
public class RequiredCollectionFieldValidator extends AbstractFieldValidator {
	protected Boolean trimNull;

	/**
	 * @return the trimNull
	 */
	public Boolean getTrimNull() {
		return trimNull;
	}

	/**
	 * @param trimNull the trimNull to set
	 */
	public void setTrimNull(Boolean trimNull) {
		this.trimNull = trimNull;
	}

	/**
	 * @see com.opensymphony.xwork2.validator.Validator#validate(java.lang.Object)
	 */
	public void validate(Object object) throws ValidationException {
		Object value = getFieldValue(object, getName());

		if (value == null) {
			addFieldError(ac, object, getName());
			return;
		}

		if (!(value instanceof Collection)) {
			throw new ValidationException("filed [" + getFieldName() + "] (" + value.getClass()
					+ ") is not a instance of " + Collection.class);
		}

		Collection c = (Collection)value;
		if (c.isEmpty()) {
			addFieldError(ac, object, getName());
			return;
		}

		if (Boolean.TRUE.equals(trimNull)) {
			for (Object o : c) {
				if (o != null) {
					return;
				}
			}
			addFieldError(ac, object, getName());
		}
	}
}
