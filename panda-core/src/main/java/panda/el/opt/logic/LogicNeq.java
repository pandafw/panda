package panda.el.opt.logic;

import panda.el.ELContext;
import panda.el.opt.DoubleOp;

/**
 * 不等于
 */
public class LogicNeq extends DoubleOp {
	public int getPriority() {
		return 6;
	}

	public Object calculate(ELContext ec) {
		Object lval = calcLeft(ec);
		Object rval = calcRight(ec);

		if (lval == rval) {
			return false;
		}
		if (lval == null || rval == null) {
			return true;
		}
		
		return !lval.equals(rval);
	}

	public String operator() {
		return "!=";
	}

}
