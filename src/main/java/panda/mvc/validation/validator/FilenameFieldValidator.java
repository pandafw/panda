package panda.mvc.validation.validator;

import panda.ioc.annotation.IocBean;

/**
 * FileNameFieldValidator
 */
@IocBean(singleton=false)
public class FilenameFieldValidator extends RegexValidator {

	public static final String filenamePattern = "[^\\\\/:*?\"<>|]*";

	public FilenameFieldValidator() {
		setRegex(filenamePattern);
		setCaseSensitive(false);
	}
}

