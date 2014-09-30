package panda.el.opt.logic;

import panda.el.ElContext;
import panda.el.opt.TwoTernary;

/**
 * 小于等于
 */
public class LTEOpt extends TwoTernary {
	public int getPriority() {
		return 6;
	}

	public Object calculate(ElContext ec) {
		Number lval = (Number)getLeft(ec);
		Number rval = (Number)getRight(ec);
		if (rval instanceof Double || lval instanceof Double) {
			return lval.doubleValue() <= rval.doubleValue();
		}
		if (rval instanceof Float || lval instanceof Float) {
			return lval.floatValue() <= rval.floatValue();
		}
		if (rval instanceof Long || lval instanceof Long) {
			return lval.longValue() <= rval.longValue();
		}
		return lval.intValue() <= rval.intValue();
	}

	public String operator() {
		return "<=";
	}
}
