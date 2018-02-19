package panda.el.opt.logic;

import panda.el.ELContext;
import panda.el.ELException;
import panda.el.opt.AbstractTwoOpt;

/**
 * and
 */
public class AndOpt extends AbstractTwoOpt {
	public int getPriority() {
		return 11;
	}

	public Object calculate(ELContext ec) {
		Object lval = getLeft(ec);
		if (!(lval instanceof Boolean)) {
			throw new ELException("Invalid left bool object for '&&': " + lval);
		}
		if (!(Boolean)lval) {
			return false;
		}
		Object rval = getRight(ec);
		if (!(rval instanceof Boolean)) {
			throw new ELException("Invalid right bool object for '&&': " + rval);
		}
		if (!(Boolean)rval) {
			return false;
		}
		return true;
	}

	public String operator() {
		return "&&";
	}

}
