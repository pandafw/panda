package panda.el.opt.object;

import java.util.Queue;

import panda.el.ELContext;
import panda.el.ELException;
import panda.el.Operator;
import panda.el.opt.Op;

/**
 * ']',数组封装. 本身没做什么操作,只是对'[' ArrayGetOpt 做了一个封装而已
 */
public class ArrayEndOp extends Op {
	private Object left;

	public int getPriority() {
		return 1;
	}

	public void wrap(Queue<Object> operand) {
		left = operand.poll();
	}

	public Object calculate(ELContext ec) {
		if (left instanceof Operator) {
			return ((Operator)left).calculate(ec);
		}
		throw new ELException("Invalid left operator: " + left);
	}

	public String operator() {
		return "]";
	}
}
