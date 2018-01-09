package panda.el.opt.arithmetic;

import panda.el.ElContext;
import panda.el.opt.AbstractTwoOpt;

/**
 * "x"
 */
public class MulOpt extends AbstractTwoOpt {
	public int getPriority() {
		return 3;
	}

	public Object calculate(ElContext ec) {
		Number lval = (Number)getLeft(ec);
		Number rval = (Number)getRight(ec);
		if (isReturnNull(ec, lval, rval)) {
			return null;
		}
		if (rval instanceof Double || lval instanceof Double) {
			return lval.doubleValue() * rval.doubleValue();
		}
		if (rval instanceof Float || lval instanceof Float) {
			return lval.floatValue() * rval.floatValue();
		}
		if (rval instanceof Long || lval instanceof Long) {
			return lval.longValue() * rval.longValue();
		}
		return lval.intValue() * rval.intValue();
	}

	public String operator() {
		return "*";
	}

}
