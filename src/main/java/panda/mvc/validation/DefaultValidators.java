package panda.mvc.validation;

import panda.mvc.ActionContext;


public class DefaultValidators implements Validators {
	public boolean valiate(ActionContext ac, String name, Object object) {
		return true;
	}
}
