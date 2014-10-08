package panda.mvc.validation.validator;

import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;

@IocBean(singleton=false)
public class RequiredValidator extends AbstractValidator {

	@Override
	protected boolean validateValue(ActionContext ac, Object value) {
		if (value != null) {
			return true;
		}
		
		addFieldError(ac);
		return false;
	}
}
