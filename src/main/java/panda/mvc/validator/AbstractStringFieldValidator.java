package panda.mvc.validator;

import panda.lang.Strings;
import panda.mvc.ActionContext;

/**
 * Base class for string field validators.
 */
public abstract class AbstractStringFieldValidator extends AbstractFieldValidator {
	private boolean strip = false;

	private boolean trim = false;

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

	protected String trimFieldValue(Object object) throws ValidationException {
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
			return true;
		}
		
		return validateString(ac, object, value);
	}

	protected abstract boolean validateString(ActionContext ac, Object object, String value) throws ValidationException;
}
