package panda.mvc.validator;

import panda.ioc.annotation.IocBean;
import panda.lang.Regexs;


@IocBean(singleton=false)
public class URLValidator extends AbstractStringValidator {
	public URLValidator() {
		setMsgId(Validators.MSGID_URL);
	}

	@Override
	protected boolean validateString(String value) {
		return Regexs.isURL(value);
	}
}
