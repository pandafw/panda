package panda.el.opt;

import java.util.Queue;

import panda.el.ElContext;
import panda.el.ElException;

/**
 * "("
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class LBracketOpt extends AbstractOpt {
	
	public static final LBracketOpt INSTANCE = new LBracketOpt();
	
	private LBracketOpt() {
	}

	public String operator() {
		return "(";
	}

	public int getPriority() {
		return 100;
	}

	public void wrap(Queue<Object> obj) {
		throw new ElException("wrap() is unsupported by '('");
	}

	public Object calculate(ElContext ec) {
		throw new ElException("calculate() is unsupported by '('");
	}
}
