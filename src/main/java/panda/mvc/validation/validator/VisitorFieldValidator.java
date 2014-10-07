package panda.mvc.validation.validator;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;

public class VisitorFieldValidator extends AbstractFieldValidator {

	private static final Log log = Logs.getLog(VisitorFieldValidator.class);

	private String condition;

	/**
	 * @return the condition
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * @param condition the condition to set
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	public boolean validate(ActionContext ac, Object object) throws ValidationException {
		Object value = getFieldValue(object, getName());
		if (value == null) {
			return true;
		}

		if (!checkCondition(ac, value)) {
			return true;
		}
		
		if (value instanceof Collection) {
			Collection coll = (Collection) value;
			Object[] array = coll.toArray();

			return validateArrayElements(ac, array);
		}
		
		if (value instanceof Map) {
			Map map = (Map) value;

			return validateMapElements(ac, map);
		}
		
		if (value instanceof Object[]) {
			Object[] array = (Object[]) value;

			return validateArrayElements(ac, array);
		}

		return validateObject(ac, getName(), value);
	}
	
	private boolean checkCondition(ActionContext ac, Object value) throws ValidationException {
		if (Strings.isEmpty(condition)) {
			return true;
		}
		
		Boolean answer = Boolean.FALSE;
		Object obj = evalExpression(ac, value, condition);

		if ((obj != null) && (obj instanceof Boolean)) {
			answer = (Boolean)obj;
		}
		else {
			log.warn("Got result of " + obj + " when trying to get Boolean.");
		}

		return answer;
	}
	
	private boolean validateMapElements(ActionContext ac, Map map)
			throws ValidationException {
		for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
			Entry es = (Entry)it.next();
			Object k = es.getKey();
			Object o = es.getValue();
			if (k != null && o != null) {
				if (!validateObject(ac, getName() + '.' + k.toString(), o)) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean validateArrayElements(ActionContext ac, Object[] array)
			throws ValidationException {
		for (int i = 0; i < array.length; i++) {
			Object o = array[i];
			if (o != null) {
				if (!validateObject(ac, getName() + '[' + i + ']', o)) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean validateObject(ActionContext ac, String fieldName, Object o)
			throws ValidationException {
		
		//TODO
		return false;
	}

}
