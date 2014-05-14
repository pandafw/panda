package panda.el.opt.logic;

import panda.el.ElContext;
import panda.el.ElException;
import panda.el.opt.TwoTernary;

/**
 * and
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class AndOpt extends TwoTernary {
	public int getPriority() {
		return 11;
	}

	public Object calculate(ElContext ec) {
		Object lval = getLeft(ec);
		if (!(lval instanceof Boolean)) {
			throw new ElException("操作数类型错误!");
		}
		if (!(Boolean)lval) {
			return false;
		}
		Object rval = getRight(ec);
		if (!(rval instanceof Boolean)) {
			throw new ElException("操作数类型错误!");
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
