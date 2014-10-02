package panda.mvc.validator;

import java.util.Collection;

import panda.mvc.ActionContext;


/**
 * RequiredCollectionFieldValidator
 */
public class RequiredCollectionFieldValidator extends AbstractFieldValidator {
	protected Boolean allowNullElem;

	/**
	 * @return the allowNullElem
	 */
	public Boolean getAllowNullElem() {
		return allowNullElem;
	}

	/**
	 * @param allowNullElem the allowNullElem to set
	 */
	public void setAllowNullElem(Boolean allowNullElem) {
		this.allowNullElem = allowNullElem;
	}


	@Override
	public boolean validate(ActionContext ac, Object object) throws ValidationException {
		Object value = getFieldValue(object, getName());

		if (value == null) {
			addFieldError(ac, getName(), value);
			return false;
		}

		if (!(value instanceof Collection)) {
			throw new ValidationException("The value of field '" + getName() + "' (" + value.getClass()
					+ ") is not a instance of " + Collection.class);
		}

		Collection c = (Collection)value;
		if (c.isEmpty()) {
			addFieldError(ac, getName(), value);
			return false;
		}

		if (Boolean.FALSE.equals(allowNullElem)) {
			for (Object o : c) {
				if (o == null) {
					addFieldError(ac, getName(), value);
					return false;
				}
			}
		}
		
		return true;
	}
}
