package panda.el.opt.bit;

import panda.el.ElContext;
import panda.el.opt.TwoOpt;

/**
 * XOR: ^
 */
public class BitXor extends TwoOpt {
	public int getPriority() {
		return 9;
	}

	public Object calculate(ElContext ec) {
		Integer lval = (Integer)getLeft(ec);
		Integer rval = (Integer)getRight(ec);
		if (isReturnNull(ec, lval, rval)) {
			return null;
		}
		return lval ^ rval;
	}

	public String operator() {
		return "^";
	}
}