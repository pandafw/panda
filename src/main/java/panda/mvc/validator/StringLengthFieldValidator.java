package panda.mvc.validator;

import panda.mvc.ActionContext;

public class StringLengthFieldValidator extends AbstractStringFieldValidator {

	private Integer maxLength = null;
	private Integer minLength = null;
	private Integer length = null;

	/**
	 * @return the maxLength
	 */
	public Integer getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * @return the minLength
	 */
	public Integer getMinLength() {
		return minLength;
	}

	/**
	 * @param minLength the minLength to set
	 */
	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	/**
	 * @return the length
	 */
	public Integer getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(Integer length) {
		this.length = length;
	}

	@Override
	protected boolean validateString(ActionContext ac, Object object, String value) throws ValidationException {
		length = value.length();

		// only check for a minimum value if the min parameter is set
		if (minLength != null && length < minLength) {
			addFieldError(ac, getName(), value);
			return false;
		}

		// only check for a maximum value if the max parameter is set
		if (maxLength != null && length > maxLength) {
			addFieldError(ac, getName(), value);
			return false;
		}
		
		return true;
	}
}
