package panda.el.opt;

import panda.el.ElContext;
import panda.el.ElException;
import panda.el.Operator;
import panda.el.obj.ElObj;

/**
 * abstract class for Operator
 */
public abstract class AbstractOpt implements Operator {
	/**
	 * @return the operator string
	 */
	public abstract String operator();

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (obj.equals(operator())) {
			return true;
		}
		return super.equals(obj);
	}

	public String toString() {
		return String.valueOf(operator());
	}

	/**
	 * calculate item
	 * @param ec EL Context
	 * @param obj object
	 * @return calculated result
	 */
	protected Object calculateItem(ElContext ec, Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Number) {
			return obj;
		}
		if (obj instanceof Boolean) {
			return obj;
		}
		if (obj instanceof String) {
			return obj;
		}
		if (obj instanceof ElObj) {
			return ((ElObj)obj).getObj(ec);
		}
		if (obj instanceof Operator) {
			return ((Operator)obj).calculate(ec);
		}
		throw new ElException("Unknown calculate value: " + obj);

	}
}
