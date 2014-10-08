package panda.mvc.validation.validator;

import java.util.Map;

import panda.ioc.annotation.IocBean;
import panda.lang.Iterators;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.mvc.ActionContext;


@IocBean(singleton=false)
public class ConstantValidator extends AbstractValidator {

	protected Boolean ignoreCase = false;
	protected String list;
	protected Object consts;

	/**
	 * @return the ignoreCase
	 */
	public Boolean getIgnoreCase() {
		return ignoreCase;
	}

	/**
	 * @param ignoreCase the ignoreCase to set
	 */
	public void setIgnoreCase(Boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	/**
	 * @return list
	 */
	public String getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(String list) {
		this.list = Strings.strip(list);
	}

	/**
	 * @return the constants
	 */
	public String getConsts() {
		if (consts instanceof Map) {
			return '[' + Strings.join(((Map)consts).values(), ", ", "'", "'") + ']';
		}
		
		if (consts != null) {
			return '[' + Strings.join(Iterators.asIterator(consts), ", ", "'", "'") + ']';
		}
		
		return "[]";
	}

	@Override
	protected boolean validateValue(ActionContext ac, Object value) {
		if (Objects.isEmpty(value)) {
			return true;
		}
		
		if (Objects.isEmpty(consts)) {
			throw new IllegalArgumentException("The constant values of '" + getName() + "' is empty.");
		}

		if (Iterators.isIterable(value)) {
			for (Object v : Iterators.asIterable(value)) {
				if (!validate(ac, v, consts)) {
					addFieldError(ac);
					return false;
				}
			}
			return true;
		}

		if (!validate(ac, value, consts)) {
			addFieldError(ac);
			return false;
		}
		return true;
	}

	protected boolean validate(ActionContext ac, Object value, Object consts) {
		if (consts instanceof Map) {
			consts = ((Map)consts).keySet();
		}
		
		for (Object v : Iterators.asIterable(consts)) {
			if (v != null) {
				if (ignoreCase) {
					if (v.toString().equalsIgnoreCase(value.toString())) {
						return true;
					}
				}
				else {
					if (v.equals(value)) {
						return true;
					}
					if (v.toString().equals(value.toString())) {
						return true;
					}
				}
			}
		}

		return false;
	}
}
