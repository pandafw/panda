package panda.el.opt.logic;

import java.util.Queue;

import panda.el.ELContext;
import panda.el.opt.AbstractOpt;

/**
 * Not(!)
 */
public class NotOpt extends AbstractOpt {
	private Object right;

	public int getPriority() {
		return 7;
	}

	public void wrap(Queue<Object> rpn) {
		right = rpn.poll();
	}

	public Object calculate(ELContext ec) {
		Object rval = calculateItem(ec, right);
		return Logics.isFalse(rval);
	}

	public String operator() {
		return "!";
	}
}
