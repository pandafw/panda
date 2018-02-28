package panda.mvc.validator;

import java.util.Map;

import panda.bind.json.Jsons;
import panda.ioc.annotation.IocBean;
import panda.lang.Iterators;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.Validators;


@IocBean(singleton=false)
public class ConstantValidator extends AbstractValidator {

	protected Boolean ignoreCase = false;
	protected Object list;

	/**
	 * 
	 */
	public ConstantValidator() {
		setMsgId(Validators.MSGID_CONSTANT);
	}

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
	public Object getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(Object list) {
		this.list = list;
	}

	/**
	 * @return the constants
	 */
	public String getConsts() {
		if (list instanceof Map) {
			return '[' + Strings.join(((Map)list).values(), ", ", "'", "'") + ']';
		}
		
		if (list != null) {
			return '[' + Strings.join(Iterators.asIterator(list), ", ", "'", "'") + ']';
		}
		
		return "[]";
	}

	@Override
	protected boolean validateValue(ActionContext ac, Object value) {
		if (Objects.isEmpty(value)) {
			return true;
		}
		
		if (Objects.isEmpty(list)) {
			throw new IllegalArgumentException("The constant values of '" + getName() + "' is empty.");
		}
		
		if (list instanceof CharSequence) {
			list = Jsons.fromJson((CharSequence)list);
		}

		if (Iterators.isIterable(value)) {
			for (Object v : Iterators.asIterable(value)) {
				if (!validate(ac, v, list)) {
					addFieldError(ac);
					return false;
				}
			}
			return true;
		}

		if (!validate(ac, value, list)) {
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
