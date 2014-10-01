package panda.mvc.validator;

import panda.io.Files;
import panda.mvc.ActionContext;

public class BinaryLengthFieldValidator extends AbstractFieldValidator {

	private Integer minLength = null;
	private Integer maxLength = null;
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
	 * @return minBinSize
	 */
	public String getMinBinSize() {
		return Files.toDisplaySize(minLength);
	}

	/**
	 * @return maxBinSize
	 */
	public String getMaxBinSize() {
		return Files.toDisplaySize(maxLength);
	}

	/**
	 * @return BinSize
	 */
	public String getBinSize() {
		return Files.toDisplaySize(length);
	}

	@Override
	public boolean validate(ActionContext ac, Object object) throws ValidationException {
		Object value = getFieldValue(object, getName());

		if (value == null) {
			return true;
		}

		if (!(value instanceof byte[])) {
			throw new ValidationException("field [" + getName() + "] (" + value.getClass()
					+ ") is not a instance of " + byte[].class);
		}

		byte[] b = (byte[])value;

		length = b.length;

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
