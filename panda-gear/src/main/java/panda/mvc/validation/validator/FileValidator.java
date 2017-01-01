package panda.mvc.validation.validator;

import java.io.File;

import panda.io.Files;
import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;
import panda.vfs.FileItem;

/**
 * FileLengthFieldValidator
 */
@IocBean(singleton=false)
public class FileValidator extends AbstractValidator {

	private Long minLength = null;
	private Long maxLength = null;
	
	/**
	 * file exists
	 */
	private boolean exists;

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
	public boolean isExists() {
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
	protected boolean validateValue(ActionContext ac, Object value) {
		if (value == null) {
			return true;
		}

		if (validateFile(ac, value)) {
			return true;
		}
		
		addFieldError(ac);
		return false;
	}
	
	protected boolean validateFile(ActionContext ac, Object value) {
		if (value instanceof File) {
			File f = (File)value;

			exists = f.exists() && f.isFile();
			if (!exists) {
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
			throw new IllegalArgumentException("field [" + getName() + "] ("
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
