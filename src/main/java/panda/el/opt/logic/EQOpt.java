package panda.el.opt.logic;

import panda.el.ElContext;
import panda.el.opt.TwoTernary;

/**
 * 等于
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class EQOpt extends TwoTernary {
	public int getPriority() {
		return 7;
	}

	public Object calculate(ElContext ec) {
		Object lval = calculateItem(ec, this.left);
		Object rval = calculateItem(ec, this.right);
		if (lval == rval) {
			return true;
		}
		return lval.equals(rval);
	}

	public String fetchSelf() {
		return "==";
	}
}
