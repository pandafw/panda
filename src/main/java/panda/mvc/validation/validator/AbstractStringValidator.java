package panda.mvc.validation.validator;

import panda.lang.Strings;
import panda.mvc.ActionContext;

/**
 * Base class for string field validators.
 */
public abstract class AbstractStringValidator extends AbstractValidator {
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

	private String trimFieldValue(Object value) {
		CharSequence str = (CharSequence)value;

		if (strip) {
			return Strings.strip(str);
		}
		
		if (trim) {
			return Strings.trim(str);
		}

		return str == null ? null : str.toString();
	}

	@Override
	protected boolean validateValue(ActionContext ac, Object value) {
		String str = trimFieldValue(value);

		if (Strings.isEmpty(str)) {
			if (!empty) {
				addFieldError(ac);
			}
			return empty;
		}
		
		return validateString(ac, str);
	}

	protected boolean validateString(ActionContext ac, String value) {
		if (validateString(value)) {
			return true;
		}

		addFieldError(ac);
		return false;
	}
	

	protected abstract boolean validateString(String value);
}
