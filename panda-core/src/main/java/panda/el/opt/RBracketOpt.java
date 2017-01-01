package panda.el.opt;

import java.util.Queue;

import panda.el.ElContext;
import panda.el.ElException;

/**
 * 右括号')'
 */
public class RBracketOpt extends AbstractOpt {
	public static final RBracketOpt INSTANCE = new RBracketOpt();
	
	private RBracketOpt() {
	}

	public int getPriority() {
		return 100;
	}

	public String operator() {
		return ")";
	}

	public void wrap(Queue<Object> obj) {
		throw new ElException("wrap() is unsupported by ')'");
	}

	public Object calculate(ElContext ec) {
		throw new ElException("calculate() is unsupported by ')'");
	}

}
