package panda.el.opt.arithmetic;

import panda.el.ElContext;
import panda.el.opt.AbstractTwoOpt;

/**
 * "+"
 */
public class PlusOpt extends AbstractTwoOpt {
	public int getPriority() {
		return 4;
	}

	public String operator() {
		return "+";
	}

	public Object calculate(ElContext ec) {
		Object lval = getLeft(ec);
		Object rval = getRight(ec);

		if (lval == null && rval == null) {
			return null;
		}
		if (lval == null) {
			return rval;
		}
		if (rval == null) {
			return rval;
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
