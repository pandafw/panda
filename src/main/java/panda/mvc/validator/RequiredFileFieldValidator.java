package panda.mvc.validator;

import java.io.File;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;

import panda.exts.fileupload.UploadFile;

import com.opensymphony.xwork2.validator.ValidationException;

/**
 * RequiredFileFieldValidator
 */
public class RequiredFileFieldValidator extends AbstractFieldValidator {

	/**
	 * @see com.opensymphony.xwork2.validator.Validator#validate(java.lang.Object)
	 */
	public void validate(Object object) throws ValidationException {
		Object value = getFieldValue(object, getName());

		if (value instanceof UploadFile) {
			value = ((UploadFile)value).getFile();
		}

		if (value == null) {
			addFieldError(ac, object, getName());
			return;
		}

		if (value instanceof File) {
			File f = (File)value;

			if (!f.exists() || !f.isFile()) {
				addFieldError(ac, object, getName());
			}
		}
		else if (value instanceof FileObject) {
			FileObject f = (FileObject)value;

			try {
				if (!f.exists() || !FileType.FILE.equals(f.getType())) {
					addFieldError(ac, object, getName());
				}
			}
			catch (FileSystemException e) {
				throw new ValidationException("field [" + getFieldName() + "] ("
						+ value.getClass() + ") ERROR: " + e.getMessage());
			}
		}
		else {
			throw new ValidationException("field [" + getFieldName() + "] ("
					+ value.getClass() + ") is not a instance of "
					+ File.class.getName() + " / " + FileObject.class.getName());
		}
	}
}
