package panda.mvc.validation.validator;

import panda.ioc.annotation.IocBean;

@IocBean(singleton=false)
public class FilenameValidator extends RegexValidator {

	public static final String filenamePattern = "[^\\\\/:*?\"<>|]*";

	public FilenameValidator() {
		setRegex(filenamePattern);
		setCaseSensitive(false);
	}
}

