package panda.mvc.validator;

import java.util.Map;

import panda.lang.Iterators;
import panda.mvc.ActionContext;


public class ProhibitedFieldValidator extends ConstantFieldValidator {

	@Override
	protected boolean validate(ActionContext ac, Object object, Object value, Object consts) {
		if (consts instanceof Map) {
			consts = ((Map)consts).keySet();
		}
		
		for (Object v : Iterators.asIterable(consts)) {
			if (v != null) {
				if (ignoreCase) {
					if (v.toString().equalsIgnoreCase(value.toString())) {
						addFieldError(ac, getName(), value);
						return false;
					}
				}
				else {
					if (v.equals(value)) {
						addFieldError(ac, getName(), value);
						return false;
					}
					if (v.toString().equals(value.toString())) {
						addFieldError(ac, getName(), value);
						return false;
					}
				}
			}
		}

		return true;
	}
}
