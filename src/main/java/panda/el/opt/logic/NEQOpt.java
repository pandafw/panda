package panda.el.opt.logic;

import panda.el.ElContext;
import panda.el.opt.TwoTernary;

/**
 * 不等于
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class NEQOpt extends TwoTernary {
	public int getPriority() {
		return 6;
	}

	public Object calculate(ElContext ec) {
		Object lval = calculateItem(ec, this.left);
		Object rval = calculateItem(ec, this.right);
		if (lval == rval) {
			return false;
		}
		return !lval.equals(rval);
	}

	public String fetchSelf() {
		return "!=";
	}

}
