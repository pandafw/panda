package panda.el.opt;

import java.util.Queue;

import panda.el.ElContext;
import panda.el.ElException;

/**
 * Abstract Single Operand Operator
 */
public abstract class AbstractSingleOpt extends AbstractOpt {
	protected Object right;

	public void wrap(Queue<Object> operand) {
		right = operand.poll();
	}

	protected boolean isReturnNull(ElContext ec, Object rval) {
		if (rval == null) {
			if (ec.strict()) {
				throw new ElException("right object is NULL: " + right);
			}
			return true;
		}
		
		return false;
	}

}
