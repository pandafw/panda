package panda.mvc.validator;

import java.io.File;

import panda.filepool.FileItem;
import panda.io.Files;
import panda.mvc.ActionContext;

/**
 * FileLengthFieldValidator
 */
public class FileLengthFieldValidator extends AbstractFieldValidator {

	private Long minLength = null;
	private Long maxLength = null;
	private Long length = null;

	/**
	 * @return the maxLength
	 */
	public Long getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(Long maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * @return the minLength
	 */
	public Long getMinLength() {
		return minLength;
	}

	/**
	 * @param minLength the minLength to set
	 */
	public void setMinLength(Long minLength) {
		this.minLength = minLength;
	}

	/**
	 * @return the length
	 */
	public Long getLength() {
		return length;
	}

	/**
	 * @return minFileSize
	 */
	public String getMinFileSize() {
		return Files.toDisplaySize(minLength);
	}
	
	/**
	 * @return maxFileSize
	 */
	public String getMaxFileSize() {
		return Files.toDisplaySize(maxLength);
	}
	
	/**
	 * @return fileSize
	 */
	public String getFileSize() {
		return Files.toDisplaySize(length);
	}
	
	@Override
	public boolean validate(ActionContext ac, Object object) throws ValidationException {
		Object value = getFieldValue(object, getName());

		if (value == null) {
			return true;
		}

		if (value instanceof File) {
			File f = (File)value;

			if (!f.exists() || !f.isFile()) {
				addFieldError(ac, getName(), value);
				return false;
			}
			length = f.length();
		}
		else if (value instanceof FileItem) {
			FileItem f = (FileItem)value;
			length = (long)f.getSize();
		}
		else {
			throw new ValidationException("field [" + getName() + "] ("
					+ value.getClass() + ") is not a instance of "
					+ File.class.getName() + " / " + FileItem.class.getName());
		}

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
