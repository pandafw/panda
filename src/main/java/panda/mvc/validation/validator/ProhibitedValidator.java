package panda.mvc.validation.validator;

import java.util.Map;

import panda.ioc.annotation.IocBean;
import panda.lang.Iterators;
import panda.mvc.ActionContext;


@IocBean(singleton=false)
public class ProhibitedValidator extends ConstantValidator {

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
