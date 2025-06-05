package panda.el.opt;

import java.util.Queue;

import panda.el.ELContext;
import panda.el.ELException;

/**
 * "{"
 */
public class LBraceOp extends Op {
	public static final LBraceOp INSTANCE = new LBraceOp();
	
	private LBraceOp() {
	}

	public String operator() {
		return "{";
	}

	public int getPriority() {
		return 100;
	}

	public void wrap(Queue<Object> obj) {
	}

	public Object calculate(ELContext ec) {
		throw new ELException("calculate() is unsupported by '{'");
	}
}
