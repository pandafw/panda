package panda.mvc.validator;

import java.io.File;

import panda.filepool.FileItem;
import panda.io.Files;
import panda.mvc.ActionContext;

/**
 * FileLengthFieldValidator
 */
public class FileFieldValidator extends AbstractFieldValidator {

	private Long minLength = null;
	private Long maxLength = null;
	
	/**
	 * file exists
	 */
	private Boolean exists = null;

	/**
	 * file length
	 */
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
	 * @return the exists
	 */
	public Boolean getExists() {
		return exists;
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

		if (validateFile(ac, value)) {
			return true;
		}
		
		addFieldError(ac, getName(), value);
		return false;
	}
	
	protected boolean validateFile(ActionContext ac, Object value) {
		if (value instanceof File) {
			File f = (File)value;

			exists = f.exists();
			if (!exists || !f.isFile()) {
				return false;
			}
			length = f.length();
		}
		else if (value instanceof FileItem) {
			FileItem f = (FileItem)value;
			exists = f.isExists();
			if (!exists) {
				return false;
			}
			length = (long)f.getSize();
		}
		else {
			throw new ValidationException("field [" + getName() + "] ("
					+ value.getClass() + ") is not a instance of "
					+ File.class.getName() + " / " + FileItem.class.getName());
		}

		// only check for a minimum value if the min parameter is set
		if (minLength != null && length < minLength) {
			return false;
		}

		// only check for a maximum value if the max parameter is set
		if (maxLength != null && length > maxLength) {
			return false;
		}
		
		return true;
	}
}
