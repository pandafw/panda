package panda.el.opt.logic;

import panda.el.ElContext;
import panda.el.opt.TwoTernary;
import panda.lang.Classes;

/**
 * 小于
 */
public class LTOpt extends TwoTernary {

	public int getPriority() {
		return 6;
	}

	public String operator() {
		return "<";
	}

	@SuppressWarnings("unchecked")
	public Object calculate(ElContext ec) {
		Comparable lval = (Comparable)getLeft(ec);
		Comparable rval = (Comparable)getRight(ec);

		if (Classes.isFloatLike(lval.getClass()) || Classes.isFloatLike(rval.getClass())) {
			return ((Number)lval).doubleValue() < ((Number)rval).doubleValue();
		}
		if (Classes.isIntLike(lval.getClass()) || Classes.isIntLike(rval.getClass())) {
			return ((Number)lval).longValue() < ((Number)rval).longValue();
		}
		return lval.compareTo(rval) < 0;
	}

}
