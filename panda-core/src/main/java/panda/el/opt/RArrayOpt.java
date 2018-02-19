package panda.el.opt;

import java.util.Queue;

import panda.el.ELContext;
import panda.el.ELException;

/**
 * 右括号'}'
 */
public class RArrayOpt extends AbstractOpt {
	public static final RArrayOpt INSTANCE = new RArrayOpt();
	
	private RArrayOpt() {
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
