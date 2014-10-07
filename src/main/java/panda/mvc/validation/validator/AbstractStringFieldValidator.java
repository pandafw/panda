package panda.mvc.validation.validator;

import panda.lang.Strings;
import panda.mvc.ActionContext;

/**
 * Base class for string field validators.
 */
public abstract class AbstractStringFieldValidator extends AbstractFieldValidator {
	private boolean empty = false;
	private boolean strip = false;
	private boolean trim = false;

	/**
	 * @return the empty
	 */
	public boolean isEmpty() {
		return empty;
	}

	/**
	 * @param empty the empty to set
	 */
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	/**
	 * @return the strip
	 */
	public boolean getStrip() {
		return strip;
	}

	/**
	 * @param strip the strip to set
	 */
	public void setStrip(boolean strip) {
		this.strip = strip;
	}

	/**
	 * @return the trim
	 */
	public boolean getTrim() {
		return trim;
	}

	/**
	 * @param trim the trim to set
	 */
	public void setTrim(boolean trim) {
		this.trim = trim;
	}

	private String trimFieldValue(Object object) {
		CharSequence value = (CharSequence)getFieldValue(object, getName());

		if (strip) {
			return Strings.strip(value);
		}
		
		if (trim) {
			return Strings.trim(value);
		}

		return value == null ? null : value.toString();
	}

	public boolean validate(ActionContext ac, Object object) throws ValidationException {
		String value = trimFieldValue(object);

		if (Strings.isEmpty(value)) {
			if (!empty) {
				addFieldError(ac, getName(), value);
			}
			return empty;
		}
		
		return validateString(ac, object, value);
	}

	protected boolean validateString(ActionContext ac, Object object, String value) {
		if (validateString(value)) {
			return true;
		}

		addFieldError(ac, getName(), value);
		return false;
	}
	

	protected abstract boolean validateString(String value);
}
