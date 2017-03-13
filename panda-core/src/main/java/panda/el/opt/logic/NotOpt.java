package panda.el.opt.logic;

import java.util.Queue;

import panda.el.El;
import panda.el.ElContext;
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

	public Object calculate(ElContext ec) {
		Object rval = calculateItem(ec, right);
		return El.isFalse(rval);
	}

	public String operator() {
		return "!";
	}
}
