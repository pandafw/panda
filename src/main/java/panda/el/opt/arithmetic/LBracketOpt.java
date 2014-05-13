package panda.el.opt.arithmetic;

import java.util.Queue;

import panda.el.ElContext;
import panda.el.ElException;
import panda.el.opt.AbstractOpt;

/**
 * "("
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class LBracketOpt extends AbstractOpt {
	public String fetchSelf() {
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
