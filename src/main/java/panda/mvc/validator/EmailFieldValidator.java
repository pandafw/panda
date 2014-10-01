package panda.mvc.validator;


public class EmailFieldValidator extends RegexFieldValidator {

	public static final String EMAIL_ADDRESS_PATTERN = "\\b^['_a-z0-9-\\+]+(\\.['_a-z0-9-\\+]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*\\.([a-z]{2}|aero|arpa|asia|biz|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|nato|net|org|pro|tel|travel|xxx)$\\b";

	public EmailFieldValidator() {
		setRegex(EMAIL_ADDRESS_PATTERN);
		setCaseSensitive(false);
	}
}
