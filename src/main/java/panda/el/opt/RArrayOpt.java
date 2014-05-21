package panda.el.opt;

import java.util.Queue;

import panda.el.ElContext;
import panda.el.ElException;

/**
 * 右括号'}'
 * 
 * @author juqkai(juqkai@gmail.com)
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

	public Object calculate(ElContext ec) {
		throw new ElException("calculate() is unsupported by '}'");
	}

}
