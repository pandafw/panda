package panda.el.opt.logic;

import panda.el.ElContext;
import panda.el.opt.AbstractTwoOpt;

/**
 * or(||)
 */
public class OrOpt extends AbstractTwoOpt {

	public int getPriority() {
		return 12;
	}

	public Object calculate(ElContext ec) {
		boolean lval = Logics.isTrue(getLeft(ec));
		if (lval) {
			return true;
		}

		boolean rval = Logics.isTrue(getRight(ec));
		if (rval) {
			return true;
		}

		return false;
	}

	public String operator() {
		return "||";
	}

}
