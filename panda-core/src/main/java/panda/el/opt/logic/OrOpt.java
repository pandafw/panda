package panda.el.opt.logic;

import panda.el.El;
import panda.el.ElContext;
import panda.el.opt.TwoOpt;

/**
 * or(||)
 */
public class OrOpt extends TwoOpt {

	public int getPriority() {
		return 12;
	}

	public Object calculate(ElContext ec) {
		boolean lval = El.isTrue(getLeft(ec));
		if (lval) {
			return true;
		}

		boolean rval = El.isTrue(getRight(ec));
		if (rval) {
			return true;
		}

		return false;
	}

	public String operator() {
		return "||";
	}

}
