package panda.mvc.validation.validator;

import java.util.regex.Pattern;

import panda.ioc.annotation.IocBean;
import panda.mvc.validation.Validators;


@IocBean(singleton=false)
public class EmailValidator extends RegexValidator {

	public static final String EMAIL_REGEX = "([\\w_\\.\\+\\-]+)@([\\w\\.\\-]+)\\.([a-zA-Z\\.]{2,6})";

	public static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	
	public EmailValidator() {
		setRegex(EMAIL_REGEX);
		setMsgId(Validators.MSGID_EMAIL);
	}

	@Override
	protected Pattern getPattern() {
		return EMAIL_PATTERN;
	}
}
