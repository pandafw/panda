package panda.mvc.validation.validator;

import panda.ioc.annotation.IocBean;
import panda.mvc.validation.Validators;


@IocBean(singleton=false)
public class EmailValidator extends RegexValidator {

	public static final String EMAIL_ADDRESS_PATTERN = "\\b^['_a-z0-9-\\+]+(\\.['_a-z0-9-\\+]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*\\.([a-z]{2,6})$\\b";

	public EmailValidator() {
		setRegex(EMAIL_ADDRESS_PATTERN);
		setCaseSensitive(false);
		setMsgId(Validators.MSGID_EMAIL);
	}
}
