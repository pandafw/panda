package panda.mvc.validator;

import panda.ioc.annotation.IocBean;
import panda.lang.Objects;
import panda.mvc.ActionContext;



/**
 * empty field validator.
 */
@IocBean(singleton=false)
public class EmptyValidator extends AbstractValidator {

	public EmptyValidator() {
		setMsgId(Validators.MSGID_EMPTY);
	}

	@Override
	protected boolean validateValue(ActionContext ac, Object value) {
		if (value == null) {
			return true;
		}

		if (!Objects.isEmpty(value)) {
			return true;
		}
		
		addFieldError(ac);
		return false;
	}
}
