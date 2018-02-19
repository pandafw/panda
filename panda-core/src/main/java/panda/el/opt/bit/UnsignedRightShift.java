package panda.el.opt.bit;

import panda.el.ELContext;
import panda.el.opt.AbstractTwoOpt;

/**
 * Unsigned Right Shift: >>>
 */
public class UnsignedRightShift extends AbstractTwoOpt {
	public int getPriority() {
		return 5;
	}

	public Object calculate(ELContext ec) {
		Integer lval = (Integer)getLeft(ec);
		Integer rval = (Integer)getRight(ec);
		if (isReturnNull(ec, lval, rval)) {
			return null;
		}
		return lval >>> rval;
	}

	public String operator() {
		return ">>>";
	}
}
