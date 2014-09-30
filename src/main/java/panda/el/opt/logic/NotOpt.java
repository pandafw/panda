package panda.el.opt.logic;

import java.util.Queue;

import panda.el.ElContext;
import panda.el.ElException;
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
		if (rval instanceof Boolean) {
			return !(Boolean)rval;
		}
		throw new ElException("'!'操作符操作失败!");
	}

	public String operator() {
		return "!";
	}
}
