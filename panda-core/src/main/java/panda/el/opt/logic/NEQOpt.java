package panda.el.opt.logic;

import panda.el.ElContext;
import panda.el.opt.TwoOpt;

/**
 * 不等于
 */
public class NEQOpt extends TwoOpt {
	public int getPriority() {
		return 6;
	}

	public Object calculate(ElContext ec) {
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
