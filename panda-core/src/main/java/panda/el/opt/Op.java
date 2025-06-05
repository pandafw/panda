package panda.el.opt;

import panda.el.ELContext;
import panda.el.ELException;
import panda.el.ELObj;
import panda.el.Operator;

/**
 * abstract class for Operator
 */
public abstract class Op implements Operator {
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
	protected Object calculateItem(ELContext ec, Object obj) {
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
		if (obj instanceof ELObj) {
			return ((ELObj)obj).getObj(ec);
		}
		if (obj instanceof Operator) {
			return ((Operator)obj).calculate(ec);
		}
		throw new ELException("Unknown calculate value: " + obj);

	}
}
