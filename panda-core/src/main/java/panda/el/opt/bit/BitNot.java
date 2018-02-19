package panda.el.opt.bit;

import panda.el.ELContext;
import panda.el.opt.AbstractSingleOpt;

/**
 * Bit Not: ~
 */
public class BitNot extends AbstractSingleOpt {
	public int getPriority() {
		return 2;
	}

	public Object calculate(ELContext ec) {
		Integer rval = (Integer)this.calculateItem(ec, right);
		if (isReturnNull(ec, rval)) {
			return null;
		}
		return ~rval;
	}

	public String operator() {
		return "~";
	}
}
