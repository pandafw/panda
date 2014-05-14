package panda.el.opt.object;

import java.util.Queue;

import panda.el.ElContext;
import panda.el.opt.AbstractOpt;

/**
 * 方法执行 以方法体右括号做为边界
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class InvokeMethodOpt extends AbstractOpt {
	private Object left;

	public int getPriority() {
		return 1;
	}

	public Object calculate(ElContext ec) {
		if (left instanceof MethodOpt) {
			return ((MethodOpt)left).calculate(ec);
		}
		return null;
	}

	public String operator() {
		return "method invoke";
	}

	public void wrap(Queue<Object> operand) {
		left = operand.poll();
	}
}
