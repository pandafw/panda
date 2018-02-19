package panda.el.opt.logic;

import panda.el.ElContext;
import panda.el.opt.AbstractTwoOpt;

/**
 * (A ||| B) : if (A is not empty and not false) return A; else return B;
 */
public class OrableOpt extends AbstractTwoOpt {

	public int getPriority() {
		return 12;
	}

	public Object calculate(ElContext ec) {
		Object lval = getLeft(ec);
		if (!Logics.isFalse(lval)) {
			return lval;
		}

		return getRight(ec);
	}

	public String operator() {
		return "|||";
	}
}
