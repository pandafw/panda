package panda.el.opt.logic;

import panda.el.ElContext;
import panda.el.ElException;
import panda.el.opt.TwoTernary;

/**
 * or(||)
 */
public class OrOpt extends TwoTernary {

	public int getPriority() {
		return 12;
	}

	public Object calculate(ElContext ec) {
		Object lval = getLeft(ec);
		if (!(lval instanceof Boolean)) {
			throw new ElException("Invalid left bool object for '||': " + lval);
		}
		if ((Boolean)lval) {
			return true;
		}

		Object rval = getRight(ec);
		if (!(rval instanceof Boolean)) {
			throw new ElException("Invalid right bool object for '||': " + rval);
		}
		if ((Boolean)rval) {
			return true;
		}

		return false;
	}

	public String operator() {
		return "||";
	}

}
