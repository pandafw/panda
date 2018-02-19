package panda.el.opt.logic;

import java.util.Queue;

import panda.el.ElContext;
import panda.el.opt.AbstractOpt;

public class NullableOpt extends AbstractOpt {

	private Object right;

	@Override
	public int getPriority() {
		return 7;
	}

	@Override
	public void wrap(Queue<Object> rpn) {
		right = rpn.poll();
	}

	@Override
	public Object calculate(ElContext ec) {
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
