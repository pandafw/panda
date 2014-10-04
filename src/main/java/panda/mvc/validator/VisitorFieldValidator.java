package panda.mvc.validator;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.validator.ActionValidatorManager;
import com.opensymphony.xwork2.validator.DelegatingValidatorContext;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.ValidatorContext;

public class VisitorFieldValidator extends AbstractFieldValidator {

	private String context;
	private boolean appendPrefix = true;
	private ActionValidatorManager actionValidatorManager;

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

	/**
	 * @see com.opensymphony.xwork2.validator.Validator#validate(java.lang.Object)
	 */
	public void validate(Object object) throws ValidationException {
		String fieldName = getFieldName();
		Object value = this.getFieldValue(fieldName, object);
		if (value == null) {
			return;
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

	/**
	 * AppendingValidatorContext
	 */
	public static class AppendingValidatorContext extends DelegatingValidatorContext {
		private String field;
		private String message;
		private ValidatorContext parent;

		/**
		 * Constructor
		 * @param parent parent
		 * @param object object
		 * @param field field
		 * @param message message
		 */
		public AppendingValidatorContext(ValidatorContext parent, Object object, String field,
				String message) {
			super(parent, makeTextProvider(object, parent), parent);

			this.field = field;
			this.message = message;
			this.parent = parent;
		}

		/**
		 * Translates a simple field name into a full field name in Ognl syntax
		 * 
		 * @param fieldName field name in OGNL syntax
		 * @return field name in OGNL syntax
		 */
		@Override
		public String getFullFieldName(String fieldName) {
			return field + "." + fieldName;
		}

		/**
		 * @param fieldName field name
		 * @return full field name
		 */
		public String getFullFieldNameFromParent(String fieldName) {
			return parent.getFullFieldName(field + "." + fieldName);
		}

		/**
		 * @see com.opensymphony.xwork2.validator.DelegatingValidatorContext#addActionError(java.lang.String)
		 */
		@Override
		public void addActionError(String anErrorMessage) {
			super.addFieldError(getFullFieldName(field), message + anErrorMessage);
		}

		/**
		 * @see com.opensymphony.xwork2.validator.DelegatingValidatorContext#addFieldError(java.lang.String, java.lang.String)
		 */
		@Override
		public void addFieldError(String fieldName, String errorMessage) {
			super.addFieldError(getFullFieldName(fieldName), message + errorMessage);
		}
	}
}
