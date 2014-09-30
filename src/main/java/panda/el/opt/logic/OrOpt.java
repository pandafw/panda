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
			throw new ElException("操作数类型错误!");
		}
		if ((Boolean)lval) {
			return true;
		}

		Object rval = getRight(ec);
		if (!(rval instanceof Boolean)) {
			throw new ElException("操作数类型错误!");
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
