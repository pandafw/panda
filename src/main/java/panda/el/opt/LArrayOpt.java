package panda.el.opt;

import java.util.Queue;

import panda.el.ElContext;
import panda.el.ElException;

/**
 * "{"
 * 
 * @author juqkai(juqkai@gmail.com)
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

	public Object calculate(ElContext ec) {
		throw new ElException("calculate() is unsupported by '}'");
	}
}
