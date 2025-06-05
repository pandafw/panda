package panda.el.opt.object;

import java.util.Queue;

import panda.el.ELContext;
import panda.el.ELException;
import panda.el.opt.Op;

/**
 * 方法执行 以方法体右括号做为边界
 */
public class MethodEndOp extends Op {
	private Object left;

	public int getPriority() {
		return 1;
	}

	public void wrap(Queue<Object> operand) {
		left = operand.poll();
	}

	public Object calculate(ELContext ec) {
		if (left instanceof MethodInvokeOp) {
			return ((MethodInvokeOp)left).calculate(ec);
		}
		throw new ELException("Invalid left method operator: " + left);
	}

	public String operator() {
		return "method invoke";
	}
}
