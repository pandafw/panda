package panda.mvc.validator;

import panda.ioc.annotation.IocBean;
import panda.net.mail.EmailAddress;
import panda.net.mail.EmailException;

/**
 * Internet Email Address Validator
 * ex: Demo <demo@gooogle.com>
 */
@IocBean(singleton=false)
public class ImailValidator extends AbstractStringValidator {
	public ImailValidator() {
		setMsgId(Validators.MSGID_IMAIL);
	}

	@Override
	protected boolean validateString(String value) {
		try {
			EmailAddress.parse(value);
			return true;
		}
		catch (EmailException e) {
			return false;
		}
	}
}
