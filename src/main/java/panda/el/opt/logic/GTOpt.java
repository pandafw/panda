package panda.el.opt.logic;

import panda.el.ElContext;
import panda.el.opt.TwoOpt;
import panda.lang.Classes;

/**
 * 大于
 */
public class GTOpt extends TwoOpt {
	public int getPriority() {
		return 6;
	}

	@SuppressWarnings("unchecked")
	public Object calculate(ElContext ec) {
		Comparable lval = (Comparable)getLeft(ec);
		Comparable rval = (Comparable)getRight(ec);

		if (lval == null || rval == null) {
			return false;
		}
		if (Classes.isFloatLike(lval.getClass()) || Classes.isFloatLike(rval.getClass())) {
			return ((Number)lval).doubleValue() > ((Number)rval).doubleValue();
		}
		if (Classes.isIntLike(lval.getClass()) || Classes.isIntLike(rval.getClass())) {
			return ((Number)lval).longValue() > ((Number)rval).longValue();
		}
		return lval.compareTo(rval) > 0;
	}

	public String operator() {
		return ">";
	}

}
