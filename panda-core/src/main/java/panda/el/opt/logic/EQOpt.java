package panda.el.opt.logic;

import panda.el.ElContext;
import panda.el.opt.AbstractTwoOpt;

/**
 * 等于
 */
public class EQOpt extends AbstractTwoOpt {
	public int getPriority() {
		return 7;
	}

	public Object calculate(ElContext ec) {
		Object lval = getLeft(ec);
		Object rval = getRight(ec);

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
