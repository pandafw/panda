package panda.mvc.validator;

import panda.ioc.annotation.IocBean;
import panda.lang.Regexs;

@IocBean(singleton=false)
public class FilenameValidator extends AbstractStringValidator {
	public FilenameValidator() {
		setMsgId(Validators.MSGID_FILENAME);
	}

	@Override
	protected boolean validateString(String value) {
		return Regexs.isFileName(value);
	}
}

