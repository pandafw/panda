package panda.el.opt.logic;

import panda.el.ELContext;
import panda.el.opt.DoubleOp;
import panda.lang.Classes;

/**
 * Less Than
 */
public class LogicLt extends DoubleOp {

	public int getPriority() {
		return 6;
	}

	public String operator() {
		return "<";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object calculate(ELContext ec) {
		Comparable lval = (Comparable)calcLeft(ec);
		Comparable rval = (Comparable)calcRight(ec);

		if (lval == null || rval == null) {
			return false;
		}
		if (Classes.isFloatLike(lval.getClass()) || Classes.isFloatLike(rval.getClass())) {
			return ((Number)lval).doubleValue() < ((Number)rval).doubleValue();
		}
		if (Classes.isIntLike(lval.getClass()) || Classes.isIntLike(rval.getClass())) {
			return ((Number)lval).longValue() < ((Number)rval).longValue();
		}
		return lval.compareTo(rval) < 0;
	}

}
