package panda.el.opt.arithmetic;

import panda.el.ElContext;
import panda.el.opt.TwoTernary;

/**
 * "+"
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class PlusOpt extends TwoTernary {
	public int getPriority() {
		return 4;
	}

	public String fetchSelf() {
		return "+";
	}

	public Object calculate(ElContext ec) {
		Object lval = calculateItem(ec, this.left);
		Object rval = calculateItem(ec, this.right);

		if (lval instanceof String || rval instanceof String) {
			return lval.toString() + rval.toString();
		}

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

}
