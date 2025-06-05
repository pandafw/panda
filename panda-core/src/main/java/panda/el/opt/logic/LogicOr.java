package panda.el.opt.logic;

import panda.el.ELContext;
import panda.el.opt.DoubleOp;

/**
 * or(||)
 */
public class LogicOr extends DoubleOp {

	public int getPriority() {
		return 12;
	}

	public Object calculate(ELContext ec) {
		boolean lval = Logics.isTrue(calcLeft(ec));
		if (lval) {
			return true;
		}

		boolean rval = Logics.isTrue(calcRight(ec));
		if (rval) {
			return true;
		}

		return false;
	}

	public String operator() {
		return "||";
	}

}
