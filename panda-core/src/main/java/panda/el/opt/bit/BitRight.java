package panda.el.opt.bit;

import panda.el.ELContext;
import panda.el.opt.DoubleOp;

/**
 * Right Shift: >>
 */
public class BitRight extends DoubleOp {
	public int getPriority() {
		return 5;
	}

	public Object calculate(ELContext ec) {
		Integer lval = (Integer)calcLeft(ec);
		Integer rval = (Integer)calcRight(ec);
		if (isReturnNull(ec, lval, rval)) {
			return null;
		}
		return lval >> rval;
	}

	public String operator() {
		return ">>";
	}

}
