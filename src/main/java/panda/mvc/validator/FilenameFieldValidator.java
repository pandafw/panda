package panda.mvc.validator;

/**
 * FileNameFieldValidator
 */
public class FilenameFieldValidator extends RegexFieldValidator {

	public static final String filenamePattern = "[^\\\\/:*?\"<>|]*";

	public FilenameFieldValidator() {
		setRegex(filenamePattern);
		setCaseSensitive(false);
	}
}

