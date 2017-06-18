package panda.el.opt.logic;

import panda.el.ElContext;
import panda.el.opt.TwoOpt;
import panda.lang.Classes;

/**
 * Great Than or Equal
 */
public class GTEOpt extends TwoOpt {

	public int getPriority() {
		return 6;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object calculate(ElContext ec) {
		Comparable lval = (Comparable)getLeft(ec);
		Comparable rval = (Comparable)getRight(ec);

		if (lval == rval) {
			return true;
		}
		if (lval == null || rval == null) {
			return false;
		}
		if (Classes.isFloatLike(lval.getClass()) || Classes.isFloatLike(rval.getClass())) {
			return ((Number)lval).doubleValue() >= ((Number)rval).doubleValue();
		}
		if (Classes.isIntLike(lval.getClass()) || Classes.isIntLike(rval.getClass())) {
			return ((Number)lval).longValue() >= ((Number)rval).longValue();
		}
		return lval.compareTo(rval) >= 0;
	}

	public String operator() {
		return ">=";
	}

}
