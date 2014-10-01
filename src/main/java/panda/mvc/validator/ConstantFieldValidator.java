package panda.mvc.validator;

import java.util.Map;

import panda.lang.Iterators;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.mvc.ActionContext;


public class ConstantFieldValidator extends AbstractFieldValidator {

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
	public boolean validate(ActionContext ac, Object object) throws ValidationException {
		Object value = getFieldValue(object, getName());
		if (Objects.isEmpty(value)) {
			return true;
		}
		
		if (Objects.isEmpty(consts)) {
			throw new ValidationException("The constant values of '" + getName() + "' is empty.");
		}

		if (Iterators.isIterable(value)) {
			for (Object v : Iterators.asIterable(value)) {
				if (!validate(ac, object, v, consts)) {
					return false;
				}
			}
			return true;
		}

		return validate(ac, object, value, consts);
	}

	protected boolean validate(ActionContext ac, Object object, Object value, Object consts) {
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

		addFieldError(ac, getName(), value);
		return false;
	}
}
