package panda.el.opt;

import java.util.Queue;

import panda.el.ELContext;
import panda.el.ELException;

/**
 * '}'
 */
public class RBraceOp extends Op {
	public static final RBraceOp INSTANCE = new RBraceOp();
	
	private RBraceOp() {
	}

	public int getPriority() {
		return 100;
	}

	public String operator() {
		return "}";
	}

	public void wrap(Queue<Object> obj) {
	}

	public Object calculate(ELContext ec) {
		throw new ELException("calculate() is unsupported by '}'");
	}

}
