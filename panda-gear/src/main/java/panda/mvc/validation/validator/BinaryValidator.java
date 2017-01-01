package panda.mvc.validation.validator;

import panda.io.Files;
import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;

@IocBean(singleton=false)
public class BinaryValidator extends AbstractValidator {

	private Integer minLength = null;
	private Integer maxLength = null;
	
	/**
	 * byte array length
	 */
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
	protected boolean validateValue(ActionContext ac, Object value) {
		if (value == null) {
			return true;
		}

		if (!(value instanceof byte[])) {
			throw new IllegalArgumentException("field [" + getName() + "] (" + value.getClass()
					+ ") is not a instance of " + byte[].class);
		}

		byte[] b = (byte[])value;

		length = b.length;

		// only check for a minimum value if the min parameter is set
		if (minLength != null && length < minLength) {
			addFieldError(ac);
			return false;
		}

		// only check for a maximum value if the max parameter is set
		if (maxLength != null && length > maxLength) {
			addFieldError(ac);
			return false;
		}
		
		return true;
	}
}
