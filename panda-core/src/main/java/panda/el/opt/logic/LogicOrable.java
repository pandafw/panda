package panda.el.opt.logic;

import panda.el.ELContext;
import panda.el.opt.DoubleOp;

/**
 * (A ||| B) : if (A is not empty and not false) return A; else return B;
 */
public class LogicOrable extends DoubleOp {

	public int getPriority() {
		return 12;
	}

	public Object calculate(ELContext ec) {
		Object lval = calcLeft(ec);
		if (!Logics.isFalse(lval)) {
			return lval;
		}

		return calcRight(ec);
	}

	public String operator() {
		return "|||";
	}
}
