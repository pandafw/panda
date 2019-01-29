package panda.mvc.validator;

import java.lang.annotation.Annotation;
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
import panda.mvc.ValidateHandler;
import panda.mvc.Validator;

@IocBean(singleton=false)
public class VisitValidator extends AbstractValidator {
	private static final Log log = Logs.getLog(VisitValidator.class);

	@IocInject
	private ValidateHandler validateHandler;

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
			
			Annotation[] vs = null;
			if (pg instanceof Field) {
				vs = ((Field)pg).getAnnotations();
			}
			else if (pg instanceof Method) {
				vs = ((Method)pg).getAnnotations();
			}
			if (vs == null) {
				continue;
			}
			
			Object val = en.getValue().getValue(obj);
			if (!validateHandler.validate(ac, this, fn, val, vs)) {
				r = false;
			}
		}
		return r;
	}

}
