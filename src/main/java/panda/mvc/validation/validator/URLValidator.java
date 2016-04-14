package panda.mvc.validation.validator;

import java.util.regex.Pattern;

import panda.ioc.annotation.IocBean;
import panda.mvc.validation.Validators;


@IocBean(singleton=false)
public class URLValidator extends RegexValidator {

	public static final String URL_REGEX = "(https?://)?([\\w\\.\\-]+)\\.([a-z\\.]{2,6})(/[\\w\\.\\-\\+&%\\?]*)*";

	public static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX, Pattern.CASE_INSENSITIVE);

	public URLValidator() {
		setRegex(URL_REGEX);
		setCaseSensitive(false);
		setMsgId(Validators.MSGID_URL);
	}

	@Override
	protected Pattern getPattern() {
		return URL_PATTERN;
	}
}
