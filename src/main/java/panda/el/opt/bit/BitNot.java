package panda.el.opt.bit;

import java.util.Queue;

import panda.el.ElContext;
import panda.el.opt.AbstractOpt;

/**
 * 非
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class BitNot extends AbstractOpt {
	private Object right;

	public int getPriority() {
		return 2;
	}

	public void wrap(Queue<Object> operand) {
		right = operand.poll();
	}

	public Object calculate(ElContext ec) {
		Integer rval = (Integer)this.calculateItem(ec, right);
		return ~rval;
	}

	public String operator() {
		return "~";
	}
}
