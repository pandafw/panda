package panda.mvc.validation.validator;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import panda.bean.Beans;
import panda.bean.PropertyAccessor;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.validation.Validators;
import panda.mvc.validation.annotation.Validates;
import panda.mvc.validation.annotation.Validate;

@IocBean(singleton=false)
public class VisitorValidator extends AbstractValidator {
	protected static class NameFieldValidator extends AbstractValidator {
		public NameFieldValidator(Validator parent) {
			setParent(parent);
		}
		
		@Override
		protected boolean validateValue(ActionContext ac, Object value) {
			return true;
		}
	}
	
	private static final Log log = Logs.getLog(VisitorValidator.class);

	@IocInject
	private Validators validators;

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

	@Override
	protected boolean validateValue(ActionContext ac, Object value) {
		if (value == null) {
			return true;
		}

		if (!checkCondition(ac, value)) {
			return true;
		}
		
		if (value instanceof Collection) {
			Collection coll = (Collection) value;
			return validateCollectionElements(ac, coll);
		}
		
		if (value instanceof Map) {
			Map map = (Map) value;
			return validateMapElements(ac, map);
		}
		
		if (value instanceof Object[]) {
			Object[] array = (Object[]) value;
			return validateArrayElements(ac, array);
		}

		return validateObject(ac, this, value);
	}
	
	private boolean checkCondition(ActionContext ac, Object value) {
		if (Strings.isEmpty(condition)) {
			return true;
		}
		
		Boolean answer = Boolean.FALSE;
		Object obj = evalExpression(ac, condition);

		if ((obj != null) && (obj instanceof Boolean)) {
			answer = (Boolean)obj;
		}
		else {
			log.warn("Got result of " + obj + " when trying to get Boolean.");
		}

		return answer;
	}
	
	private boolean validateMapElements(ActionContext ac, Map map) {
		boolean r = true;
		NameFieldValidator nfv = new NameFieldValidator(this);
		for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
			Entry es = (Entry)it.next();
			Object k = es.getKey();
			Object o = es.getValue();
			if (k != null && o != null) {
				nfv.setName(k.toString());
				if (!validateObject(ac, nfv, o)) {
					r = false;
					if (isShortCircuit()) {
						return false;
					}
				}
			}
		}
		return r;
	}

	private boolean validateArrayElements(ActionContext ac, Object[] array) {
		boolean r = true;
		NameFieldValidator nfv = new NameFieldValidator(this);
		for (int i = 0; i < array.length; i++) {
			Object o = array[i];
			if (o != null) {
				nfv.setName('[' + String.valueOf(i) + ']');
				if (!validateObject(ac, nfv, o)) {
					r = false;
					if (isShortCircuit()) {
						return false;
					}
				}
			}
		}
		return r;
	}

	private boolean validateCollectionElements(ActionContext ac, Collection coll) {
		boolean r = true;
		NameFieldValidator nfv = new NameFieldValidator(this);
		int i = 0;
		for (Object o : coll) {
			if (o != null) {
				nfv.setName('[' + String.valueOf(i) + ']');
				if (!validateObject(ac, nfv, o)) {
					r = false;
					if (isShortCircuit()) {
						return false;
					}
				}
			}
			i++;
		}
		return r;
	}

	private boolean validateObject(ActionContext ac, Validator parent, Object obj) {
		boolean r = true;
		
		Map<String, PropertyAccessor> pas = Beans.getPropertyAccessors(obj.getClass());
		for (Entry<String, PropertyAccessor> en : pas.entrySet()) {
			String fn = en.getKey();
			Member pg = en.getValue().getGetter();
			if (pg == null) {
				continue;
			}
			
			Validates av = null;
			if (pg instanceof Field) {
				av = ((Field)pg).getAnnotation(Validates.class);
			}
			else if (pg instanceof Method) {
				av = ((Method)pg).getAnnotation(Validates.class);
			}
			if (av == null) {
				continue;
			}
			
			Object val = en.getValue().getValue(obj);
			for (Validate v : av.value()) {
				Validator fv = validators.createValidator(ac, v);
				fv.setName(fn);
				fv.setParent(parent);
				
				if (!fv.validate(ac, val)) {
					r = false;
					if (isShortCircuit()) {
						return false;
					}
				}
			}
		}
		return r;
	}

}
