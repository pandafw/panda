package panda.mvc.validator;

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

	private String expression;
	private String context;
	private boolean appendPrefix = true;
	private ActionValidatorManager actionValidatorManager;

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getExpression() {
		return expression;
	}

	/**
	 * @param mgr ActionValidatorManager
	 */
	@Inject
	public void setActionValidatorManager(ActionValidatorManager mgr) {
		this.actionValidatorManager = mgr;
	}

	/**
	 * Sets whether the field name of this field validator should be prepended
	 * to the field name of the visited field to determine the full field name
	 * when an error occurs. The default is true.
	 * @param appendPrefix the appendPrefix to set
	 */
	public void setAppendPrefix(boolean appendPrefix) {
		this.appendPrefix = appendPrefix;
	}

	/**
	 * Flags whether the field name of this field validator should be prepended
	 * to the field name of the visited field to determine the full field name
	 * when an error occurs. The default is true.
	 * @return appendPrefix
	 */
	public boolean isAppendPrefix() {
		return appendPrefix;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
	}

	public boolean validate(ActionContext ac, Object object) throws ValidationException {
		Object value = getFieldValue(object, getName());
		if (value == null) {
			return true;
		}

		if (!checkCondition(ac, value)) {
			return true;
		}
		
		ValueStack stack = ActionContext.getContext().getValueStack();

		stack.push(object);

		String visitorContext = (context == null) ? ActionContext.getContext().getName() : context;

		if (value instanceof Collection) {
			Collection coll = (Collection) value;
			Object[] array = coll.toArray();

			validateArrayElements(array, fieldName, visitorContext);
		}
		else if (value instanceof Map) {
			Map map = (Map) value;
			validateMapElements(map, fieldName, visitorContext);
		}
		else if (value instanceof Object[]) {
			Object[] array = (Object[]) value;

			validateArrayElements(array, fieldName, visitorContext);
		}
		else {
			validateObject(fieldName, value, visitorContext);
		}

		stack.pop();
	}
	
	private boolean checkCondition(ActionContext ac, Object value) throws ValidationException {
		if (Strings.isEmpty(expression)) {
			return true;
		}
		
		Boolean answer = Boolean.FALSE;
		Object obj = evalExpression(ac, value, expression);

		if ((obj != null) && (obj instanceof Boolean)) {
			answer = (Boolean)obj;
		}
		else {
			log.warn("Got result of " + obj + " when trying to get Boolean.");
		}

		return answer;
	}
	
	private void validateMapElements(Map map, String fieldName, String visitorContext)
			throws ValidationException {
		if (map == null) {
			return;
		}

		for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
			Entry es = (Entry)it.next();
			Object k = es.getKey();
			Object o = es.getValue();
			if (k != null && o != null) {
				validateObject(fieldName + "." + k.toString(), o, visitorContext);
			}
		}
	}

	private void validateArrayElements(Object[] array, String fieldName, String visitorContext)
			throws ValidationException {
		if (array == null) {
			return;
		}

		for (int i = 0; i < array.length; i++) {
			Object o = array[i];
			if (o != null) {
				validateObject(fieldName + "[" + i + "]", o, visitorContext);
			}
		}
	}

	private void validateObject(String fieldName, Object o, String visitorContext)
			throws ValidationException {
		ValueStack stack = ActionContext.getContext().getValueStack();
		stack.push(o);

		ValidatorContext validatorContext;

		if (appendPrefix) {
			validatorContext = new AppendingValidatorContext(getValidatorContext(), o, fieldName,
					getMessage(o));
		}
		else {
			ValidatorContext parent = getValidatorContext();
			validatorContext = new DelegatingValidatorContext(parent, DelegatingValidatorContext
					.makeTextProvider(o, parent), parent);
		}

		actionValidatorManager.validate(o, visitorContext, validatorContext);
		stack.pop();
	}

}
