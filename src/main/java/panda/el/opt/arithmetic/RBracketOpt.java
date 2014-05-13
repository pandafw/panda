package panda.el.opt.arithmetic;

import java.util.Queue;

import panda.el.ElContext;
import panda.el.ElException;
import panda.el.opt.AbstractOpt;

/**
 * 右括号')'
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class RBracketOpt extends AbstractOpt {

	public int getPriority() {
		return 100;
	}

	public String fetchSelf() {
		return ")";
	}

	public void wrap(Queue<Object> obj) {
		throw new ElException("wrap() is unsupported by ')'");
	}

	public Object calculate(ElContext ec) {
		throw new ElException("calculate() is unsupported by ')'");
	}

}
