package panda.mvc.impl;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.mvc.ActionContext;
import panda.mvc.ValidateHandler;
import panda.mvc.Validator;
import panda.mvc.ValidatorCreator;
import panda.mvc.annotation.Validate;
import panda.mvc.validator.Validators;

@IocBean(type=ValidateHandler.class)
public class DefaultValidateHandler implements ValidateHandler {
	// -------------------------------------------------------
	@IocInject
	private ValidatorCreator vc;
	
	public DefaultValidateHandler() {
	}

	@Override
	public boolean validate(ActionContext ac, String name, Object value) {
		return validate(ac, null, name, value);
	}

	@Override
	public boolean validate(ActionContext ac, Validator parent, String name, Object value) {
		return validate(ac, parent, name, value, null);
	}
	
	@Override
	public boolean validate(ActionContext ac, Validator parent, String name, Object value, Validate[] vs) {
		if (Arrays.isEmpty(vs)) {
			Validator fv = vc.create(ac, Validators.VISIT);
			fv.setName(name);
			return fv.validate(ac, value);
		}

		boolean r = true;
		for (Validate v : vs) {
			Validator fv = vc.create(ac, v);
			fv.setName(name);
			fv.setParent(parent);
			if (!fv.validate(ac, value)) {
				if (fv.isShortCircuit()) {
					return false;
				}
				r = false;
			}
		}
		return r;
	}
}
