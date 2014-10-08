package panda.mvc.validation.validator;

import panda.ioc.annotation.IocBean;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;

@IocBean(singleton=false)
public class ElValidator extends AbstractValidator {

	private static final Log log = Logs.getLog(ElValidator.class);
	
	private String expression;

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getExpression() {
		return expression;
	}

	@Override
	protected boolean validateValue(ActionContext ac, Object value) {
		Object result = evalExpression(ac, expression);

		Boolean answer = false;
		if ((result != null) && (result instanceof Boolean)) {
			answer = (Boolean)result;
		}
		else {
			log.warn("Got result of '" + result + "' when trying to get Boolean of '" + expression + "'.");
		}

		if (answer.booleanValue()) {
			return true;
		}
		
		addFieldError(ac);
		return false;
	}
}
