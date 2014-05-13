package panda.el.opt.logic;

import panda.el.ElContext;
import panda.el.opt.TwoTernary;

/**
 * 小于
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class LTOpt extends TwoTernary {

	public int getPriority() {
		return 6;
	}

	public String fetchSelf() {
		return "<";
	}

	public Object calculate(ElContext ec) {
		Number lval = (Number)calculateItem(ec, this.left);
		Number rval = (Number)calculateItem(ec, this.right);
		if (rval instanceof Double || lval instanceof Double) {
			return lval.doubleValue() < rval.doubleValue();
		}
		if (rval instanceof Float || lval instanceof Float) {
			return lval.floatValue() < rval.floatValue();
		}
		if (rval instanceof Long || lval instanceof Long) {
			return lval.longValue() < rval.longValue();
		}
		return lval.intValue() < rval.intValue();
	}

}
