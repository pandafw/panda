package panda.el.opt.object;

import java.util.Queue;

import panda.el.ElContext;
import panda.el.ElException;
import panda.el.opt.AbstractOpt;

/**
 * 方法执行 以方法体右括号做为边界
 */
public class InvokeMethodOpt extends AbstractOpt {
	private Object left;

	public int getPriority() {
		return 1;
	}

	public void wrap(Queue<Object> operand) {
		left = operand.poll();
	}

	public Object calculate(ElContext ec) {
		if (left instanceof MethodOpt) {
			return ((MethodOpt)left).calculate(ec);
		}
		throw new ElException("Invalid left method operator: " + left);
	}

	public String operator() {
		return "method invoke";
	}
}
