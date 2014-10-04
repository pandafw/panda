package panda.mvc.validator;

import java.util.Collection;

import panda.mvc.ActionContext;


public class CollectionFieldValidator extends AbstractFieldValidator {
	private boolean emtpy = false;
	private boolean nullElem = true;

	/**
	 * @return the emtpy
	 */
	public boolean isEmtpy() {
		return emtpy;
	}

	/**
	 * @param emtpy the emtpy to set
	 */
	public void setEmtpy(boolean emtpy) {
		this.emtpy = emtpy;
	}

	/**
	 * @return the nullElem
	 */
	public boolean isNullElem() {
		return nullElem;
	}

	/**
	 * @param nullElem the nullElem to set
	 */
	public void setNullElem(boolean nullElem) {
		this.nullElem = nullElem;
	}


	@Override
	public boolean validate(ActionContext ac, Object object) throws ValidationException {
		Object value = getFieldValue(object, getName());

		if (value == null) {
			return true;
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

		if (!nullElem) {
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
