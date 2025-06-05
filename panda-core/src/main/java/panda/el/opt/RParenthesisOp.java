package panda.el.opt;

import java.util.Queue;

import panda.el.ELContext;
import panda.el.ELException;

/**
 * ')'
 */
public class RParenthesisOp extends Op {
	public static final RParenthesisOp INSTANCE = new RParenthesisOp();
	
	private RParenthesisOp() {
	}

	public int getPriority() {
		return 100;
	}

	public String operator() {
		return ")";
	}

	public void wrap(Queue<Object> obj) {
		throw new ELException("wrap() is unsupported by ')'");
	}

	public Object calculate(ELContext ec) {
		throw new ELException("calculate() is unsupported by ')'");
	}

}
