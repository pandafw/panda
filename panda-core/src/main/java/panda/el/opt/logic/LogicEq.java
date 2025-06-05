package panda.el.opt.logic;

import panda.el.ELContext;
import panda.el.opt.DoubleOp;

/**
 * 等于
 */
public class LogicEq extends DoubleOp {
	public int getPriority() {
		return 7;
	}

	public Object calculate(ELContext ec) {
		Object lval = calcLeft(ec);
		Object rval = calcRight(ec);

		if (lval == rval) {
			return true;
		}
		if (lval == null || rval == null) {
			return false;
		}
		if (lval instanceof Character) {
			lval = lval.toString();
		}
		if (rval instanceof Character) {
			rval = rval.toString();
		}
		return lval.equals(rval);
	}

	public String operator() {
		return "==";
	}
}
