package panda.el.opt.math;

import panda.el.ELContext;
import panda.el.opt.DoubleOp;

/**
 * "+"
 */
public class MathAdd extends DoubleOp {
	public int getPriority() {
		return 4;
	}

	public String operator() {
		return "+";
	}

	public Object calculate(ELContext ec) {
		Object lval = calcLeft(ec);
		Object rval = calcRight(ec);

		if (lval == null && rval == null) {
			return null;
		}
		if (lval == null) {
			return rval;
		}
		if (rval == null) {
			return lval;
		}

		if (lval instanceof Number && rval instanceof Number) {
			Number nlval = (Number)lval;
			Number nrval = (Number)rval;
	
			if (nrval instanceof Double || nlval instanceof Double) {
				return nlval.doubleValue() + nrval.doubleValue();
			}
			if (nrval instanceof Float || nlval instanceof Float) {
				return nlval.floatValue() + nrval.floatValue();
			}
			if (nrval instanceof Long || nlval instanceof Long) {
				return nlval.longValue() + nrval.longValue();
			}
			return nlval.intValue() + nrval.intValue();
		}

		return String.valueOf(lval) + String.valueOf(rval);
	}

}
