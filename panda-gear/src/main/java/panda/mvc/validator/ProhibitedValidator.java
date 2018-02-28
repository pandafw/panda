package panda.mvc.validator;

import java.util.Map;

import panda.ioc.annotation.IocBean;
import panda.lang.Iterators;
import panda.mvc.ActionContext;
import panda.mvc.Validators;


@IocBean(singleton=false)
public class ProhibitedValidator extends ConstantValidator {

	/**
	 * 
	 */
	public ProhibitedValidator() {
		setMsgId(Validators.MSGID_PROHIBITED);
	}

	@Override
	protected boolean validate(ActionContext ac, Object value, Object consts) {
		if (consts instanceof Map) {
			consts = ((Map)consts).keySet();
		}
		
		for (Object v : Iterators.asIterable(consts)) {
			if (v != null) {
				if (ignoreCase) {
					if (v.toString().equalsIgnoreCase(value.toString())) {
						return false;
					}
				}
				else {
					if (v.equals(value)) {
						return false;
					}
					if (v.toString().equals(value.toString())) {
						return false;
					}
				}
			}
		}

		return true;
	}
}
