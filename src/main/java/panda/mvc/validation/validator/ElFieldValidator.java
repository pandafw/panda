package panda.mvc.validation.validator;

import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;

public class ElFieldValidator extends AbstractFieldValidator {

	private static final Log log = Logs.getLog(ElFieldValidator.class);
	
	private String expression;

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getExpression() {
		return expression;
	}

	public boolean validate(ActionContext ac, Object object) throws ValidationException {
		Object value = getFieldValue(object, getName());

		Object result = evalExpression(ac, object, expression);

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
		
		addFieldError(ac, getName(), value);
		return false;
	}
}
