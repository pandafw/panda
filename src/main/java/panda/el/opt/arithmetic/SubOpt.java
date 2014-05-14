package panda.el.opt.arithmetic;

import panda.el.ElContext;
import panda.el.opt.TwoTernary;

/**
 * "-"
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class SubOpt extends TwoTernary {

	public String operator() {
		return "-";
	}

	public int getPriority() {
		return 4;
	}

	public Object calculate(ElContext ec) {
		Number lval = (Number)getLeft(ec);
		Number rval = (Number)getRight(ec);
		if (rval instanceof Double || lval instanceof Double) {
			return lval.doubleValue() - rval.doubleValue();
		}
		if (rval instanceof Float || lval instanceof Float) {
			return lval.floatValue() - rval.floatValue();
		}
		if (rval instanceof Long || lval instanceof Long) {
			return lval.longValue() - rval.longValue();
		}
		return lval.intValue() - rval.intValue();
	}

}
