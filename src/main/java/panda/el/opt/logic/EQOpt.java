package panda.el.opt.logic;

import panda.el.ElContext;
import panda.el.opt.TwoTernary;

/**
 * 等于
 */
public class EQOpt extends TwoTernary {
	public int getPriority() {
		return 7;
	}

	public Object calculate(ElContext ec) {
		Object lval = getLeft(ec);
		Object rval = getRight(ec);
		if (lval == rval) {
			return true;
		}
		return lval.equals(rval);
	}

	public String operator() {
		return "==";
	}
}
