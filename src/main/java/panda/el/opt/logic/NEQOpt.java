package panda.el.opt.logic;

import panda.el.ElContext;
import panda.el.opt.TwoTernary;

/**
 * 不等于
 */
public class NEQOpt extends TwoTernary {
	public int getPriority() {
		return 6;
	}

	public Object calculate(ElContext ec) {
		Object lval = getLeft(ec);
		Object rval = getRight(ec);
		if (lval == rval) {
			return false;
		}
		return !lval.equals(rval);
	}

	public String operator() {
		return "!=";
	}

}
