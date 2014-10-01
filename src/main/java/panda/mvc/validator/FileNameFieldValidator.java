package panda.mvc.validator;

/**
 * FileNameFieldValidator
 */
public class FileNameFieldValidator extends RegexFieldValidator {

	public static final String filenamePattern = "[^\\\\/:*?\"<>|]*";

	public FileNameFieldValidator() {
		setExpression(filenamePattern);
		setCaseSensitive(false);
	}

}



