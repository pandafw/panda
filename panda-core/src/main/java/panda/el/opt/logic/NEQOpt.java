package panda.el.opt.logic;

import panda.el.ELContext;
import panda.el.opt.AbstractTwoOpt;

/**
 * 不等于
 */
public class NEQOpt extends AbstractTwoOpt {
	public int getPriority() {
		return 6;
	}

	public Object calculate(ELContext ec) {
		Object lval = getLeft(ec);
		Object rval = getRight(ec);

		if (lval == rval) {
			return false;
		}
		if (lval == null || rval == null) {
			return true;
		}
		
		return !lval.equals(rval);
	}

	public String operator() {
		return "!=";
	}

}
