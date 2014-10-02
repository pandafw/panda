package panda.mvc.validator;

import java.io.File;

import panda.filepool.FileItem;
import panda.mvc.ActionContext;

public class RequiredFileFieldValidator extends AbstractFieldValidator {
	@Override
	public boolean validate(ActionContext ac, Object object) throws ValidationException {
		Object value = getFieldValue(object, getName());

		if (value == null) {
			addFieldError(ac, getName(), value);
			return false;
		}

		if (value instanceof FileItem) {
			return true;
		}

		if (value instanceof File) {
			File f = (File)value;

			if (f.exists() && f.isFile()) {
				return true;
			}
			
			addFieldError(ac, getName(), value);
			return false;
		}

		throw new ValidationException("The value of field '" + getName() + "' ("
				+ value.getClass() + ") is not a instance of "
				+ File.class.getName() + " / " + FileItem.class.getName());
	}
}
