package panda.el.opt.logic;

import java.util.Queue;

import panda.el.ElContext;
import panda.el.opt.AbstractOpt;
import panda.lang.Objects;

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
		if (rval == null) {
			return true;
		}
		if (rval instanceof Boolean) {
			return !(Boolean)rval;
		}
		return Objects.isEmpty(rval);
	}

	public String operator() {
		return "!";
	}
}
