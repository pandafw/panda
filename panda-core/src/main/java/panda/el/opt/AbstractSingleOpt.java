package panda.el.opt;

import java.util.Queue;

import panda.el.ELContext;
import panda.el.ELException;

/**
 * Abstract Single Operand Operator
 */
public abstract class AbstractSingleOpt extends AbstractOpt {
	protected Object right;

	public void wrap(Queue<Object> operand) {
		right = operand.poll();
	}

	protected boolean isReturnNull(ELContext ec, Object rval) {
		if (rval == null) {
			if (ec.strict()) {
				throw new ELException("right object is NULL: " + right);
			}
			return true;
		}
		
		return false;
	}

}
