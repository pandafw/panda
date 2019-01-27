package panda.mvc.validator;

import panda.ioc.annotation.IocBean;
import panda.lang.Regexs;


@IocBean(singleton=false)
public class EmailValidator extends AbstractStringValidator {
	public EmailValidator() {
		setMsgId(Validators.MSGID_EMAIL);
	}

	@Override
	protected boolean validateString(String value) {
		return Regexs.isEmail(value);
	}
}
