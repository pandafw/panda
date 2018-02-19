package panda.el.opt;

import java.util.Queue;

import panda.el.ELContext;
import panda.el.ELException;

/**
 * "{"
 */
public class LArrayOpt extends AbstractOpt {
	public static final LArrayOpt INSTANCE = new LArrayOpt();
	
	private LArrayOpt() {
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
		throw new ELException("calculate() is unsupported by '}'");
	}
}
