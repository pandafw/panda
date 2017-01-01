package panda.mvc.validation.validator;

import panda.ioc.annotation.IocBean;
import panda.lang.Regexs;


@IocBean(singleton=false)
public class EmailValidator extends AbstractStringValidator {
	@Override
	protected boolean validateString(String value) {
		return Regexs.isEmail(value);
	}
}
