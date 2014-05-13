package panda.el.opt.arithmetic;

import panda.el.ElContext;
import panda.el.opt.TwoTernary;

/**
 * 除
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class DivOpt extends TwoTernary {

	public int getPriority() {
		return 3;
	}

	public Object calculate(ElContext ec) {
		Number lval = (Number)calculateItem(ec, this.left);
		Number rval = (Number)calculateItem(ec, this.right);
		if (rval instanceof Double || lval instanceof Double) {
			return lval.doubleValue() / rval.doubleValue();
		}
		if (rval instanceof Float || lval instanceof Float) {
			return lval.floatValue() / rval.floatValue();
		}
		if (rval instanceof Long || lval instanceof Long) {
			return lval.longValue() / rval.longValue();
		}
		return lval.intValue() / rval.intValue();
	}

	public String fetchSelf() {
		return "/";
	}

}
