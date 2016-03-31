package panda.el.opt;

import java.util.Queue;

import panda.el.ElContext;
import panda.el.ElException;

/**
 * Single Operator
 */
public abstract class SingleOpt extends AbstractOpt {
	protected Object right;

	public void wrap(Queue<Object> operand) {
		right = operand.poll();
	}

	protected boolean isReturnNull(ElContext ec, Object rval) {
		if (rval == null) {
			if (ec.isStrict()) {
				throw new ElException("right object is NULL: " + right);
			}
			return true;
		}
		
		return false;
	}

}
