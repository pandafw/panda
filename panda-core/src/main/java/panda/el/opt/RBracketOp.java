package panda.el.opt;

import java.util.Queue;

import panda.el.ELContext;
import panda.el.ELException;

/**
 * ']'
 */
public class RBracketOp extends Op {
	public static final RBracketOp INSTANCE = new RBracketOp();
	
	private RBracketOp() {
	}

	public int getPriority() {
		return 100;
	}

	public String operator() {
		return ")";
	}

	public void wrap(Queue<Object> obj) {
		throw new ELException("wrap() is unsupported by ']'");
	}

	public Object calculate(ELContext ec) {
		throw new ELException("calculate() is unsupported by ']'");
	}

}
