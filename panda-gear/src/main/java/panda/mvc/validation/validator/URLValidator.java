package panda.mvc.validation.validator;

import panda.ioc.annotation.IocBean;
import panda.lang.Regexs;


@IocBean(singleton=false)
public class URLValidator extends AbstractStringValidator {
	@Override
	protected boolean validateString(String value) {
		return Regexs.isURL(value);
	}
}
