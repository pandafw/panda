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
import panda.mvc.Mvcs;
import panda.mvc.validation.Validators;
import panda.mvc.validation.annotation.Validates;

@IocBean(singleton=false)
public class VisitValidator extends AbstractValidator {
	protected static class ParentValidator extends AbstractValidator {
		public ParentValidator(Validator parent, Object value) {
			setParent(parent);
			setValue(value);
		}
		
		@Override
		protected boolean validateValue(ActionContext ac, Object value) {
			return true;
		}
	}
	
	private static final Log log = Logs.getLog(VisitValidator.class);

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

	/**
	 * @return the validators
	 */
	public Validators getValidators() {
		return validators;
	}

	/**
	 * @param validators the validators to set
	 */
	public void setValidators(Validators validators) {
		this.validators = validators;
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
		
		boolean answer = false;
		Object obj = Mvcs.findValue(ac, condition, this);

		if ((obj != null) && (obj instanceof Boolean)) {
			answer = ((Boolean)obj).booleanValue();
		}
		else {
			log.warn("Got result of " + obj + " when trying to get Boolean.");
		}

		return answer;
	}
	
	private boolean validateMapElements(ActionContext ac, Map map) {
		boolean r = true;

		VisitValidator vv = ac.getIoc().get(VisitValidator.class);
		for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
			Entry es = (Entry)it.next();
			Object k = es.getKey();
			Object o = es.getValue();
			if (k != null && o != null) {
				vv.setParent(this);
				vv.setName(k.toString());
				if (!vv.validate(ac, o)) {
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

		VisitValidator vv = ac.getIoc().get(VisitValidator.class);
		for (int i = 0; i < array.length; i++) {
			Object o = array[i];
			if (o != null) {
				vv.setName('[' + String.valueOf(i) + ']');
				if (!vv.validate(ac, o)) {
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

		VisitValidator vv = ac.getIoc().get(VisitValidator.class);
		int i = 0;
		for (Object o : coll) {
			if (o != null) {
				vv.setName('[' + String.valueOf(i) + ']');
				if (!vv.validate(ac, o)) {
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
			
			Validates vs = null;
			if (pg instanceof Field) {
				vs = ((Field)pg).getAnnotation(Validates.class);
			}
			else if (pg instanceof Method) {
				vs = ((Method)pg).getAnnotation(Validates.class);
			}
			if (vs == null) {
				continue;
			}
			
			Object val = en.getValue().getValue(obj);
			if (!validators.validate(ac, this, fn, val, vs.value())) {
				if (vs.shortCircuit()) {
					return false;
				}
				r = false;
			}
		}
		return r;
	}

}
