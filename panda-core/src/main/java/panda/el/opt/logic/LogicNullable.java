package panda.el.opt.logic;

import panda.el.ELContext;
import panda.el.opt.SingleOp;

public class LogicNullable extends SingleOp {

	@Override
	public int getPriority() {
		return 7;
	}

	@Override
	public Object calculate(ELContext ec) {
		try {
			return calculateItem(ec, right);
		}
		catch (Throwable e) {
			return null;
		}
	}

	@Override
	public String operator() {
		return "!!";
	}

}
