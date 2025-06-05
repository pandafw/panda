package panda.el.opt;

import java.util.Queue;

import panda.el.ELContext;
import panda.el.ELException;

/**
 * "["
 */
public class LBracketOp extends Op {
	
	public static final LBracketOp INSTANCE = new LBracketOp();
	
	private LBracketOp() {
	}

	public String operator() {
		return "(";
	}

	public int getPriority() {
		return 100;
	}

	public void wrap(Queue<Object> obj) {
		throw new ELException("wrap() is unsupported by '['");
	}

	public Object calculate(ELContext ec) {
		throw new ELException("calculate() is unsupported by '['");
	}
}
