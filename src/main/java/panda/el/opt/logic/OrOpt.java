package panda.el.opt.logic;

import panda.el.ElContext;
import panda.el.ElException;
import panda.el.opt.TwoTernary;

/**
 * or(||)
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class OrOpt extends TwoTernary {

	public int getPriority() {
		return 12;
	}

	public Object calculate(ElContext ec) {
		Object lval = calculateItem(ec, left);
		if (!(lval instanceof Boolean)) {
			throw new ElException("操作数类型错误!");
		}
		if ((Boolean)lval) {
			return true;
		}
		Object rval = calculateItem(ec, right);
		if (!(rval instanceof Boolean)) {
			throw new ElException("操作数类型错误!");
		}
		if ((Boolean)rval) {
			return true;
		}
		return false;
	}

	public String fetchSelf() {
		return "||";
	}

}
