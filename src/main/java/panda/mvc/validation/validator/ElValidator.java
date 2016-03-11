package panda.mvc.validation.validator;

import panda.ioc.annotation.IocBean;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;

@IocBean(singleton=false)
public class ElValidator extends AbstractValidator {

	private static final Log log = Logs.getLog(ElValidator.class);
	
	private String el;

	public void setEl(String el) {
		this.el = el;
	}

	public String getEl() {
		return el;
	}

	@Override
	protected boolean validateValue(ActionContext ac, Object value) {
		if (getName() != null && value == null) {
			// skip null field value
			return true;
		}
		
		Object result = evalExpression(ac, el);

		boolean answer = false;
		if ((result != null) && (result instanceof Boolean)) {
			answer = ((Boolean)result).booleanValue();
		}
		else {
			log.warn("Got result of '" + result + "' when trying to get Boolean of '" + el + "'.");
		}

		if (answer) {
			return true;
		}
		
		addFieldError(ac);
		return false;
	}
}
