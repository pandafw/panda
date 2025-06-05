package panda.el.opt.math;

import panda.el.ELContext;
import panda.el.opt.DoubleOp;

/**
 * "-"
 */
public class MathSub extends DoubleOp {

	public String operator() {
		return "-";
	}

	public int getPriority() {
		return 4;
	}

	public Object calculate(ELContext ec) {
		Number lval = (Number)calcLeft(ec);
		Number rval = (Number)calcRight(ec);

		if (isReturnNull(ec, lval, rval)) {
			return null;
		}

		if (rval instanceof Double || lval instanceof Double) {
			return lval.doubleValue() - rval.doubleValue();
		}
		if (rval instanceof Float || lval instanceof Float) {
			return lval.floatValue() - rval.floatValue();
		}
		if (rval instanceof Long || lval instanceof Long) {
			return lval.longValue() - rval.longValue();
		}
		return lval.intValue() - rval.intValue();
	}

}
