package panda.el.opt;

import java.util.Queue;

import panda.el.ElContext;
import panda.el.ElException;
import panda.el.Operator;
import panda.el.obj.ElObj;

/**
 * Abstract 2 Operands Operator
 */
public abstract class AbstractTwoOpt extends AbstractOpt {
	protected Object right;
	protected Object left;

	public void wrap(Queue<Object> rpn) {
		right = rpn.poll();
		left = rpn.poll();
	}

	/**
	 * calculate right object
	 * @param ec EL context
	 * @return the right calculated result
	 */
	public Object getRight(ElContext ec) {
		return calculateItem(ec, right);
	}

	/**
	 * calculate left object
	 * @param ec EL context
	 * @return the left calculated result
	 */
	public Object getLeft(ElContext ec) {
		return calculateItem(ec, left);
	}

	/**
	 * get left variable
	 * @param ec EL context
	 * @return left variable
	 */
	protected Object getLeftVar(ElContext ec) {
		if (left == null) {
			return ec.context();
		}
		
		if (left instanceof Operator) {
			return ((Operator)left).calculate(ec);
		}
		if (left instanceof ElObj) {
			return ((ElObj)left).getObj(ec);
		}
		return left;
	}

	/**
	 * should return null or not, or throw exception if in strict mode
	 * @param ec EL context
	 * @param lval left var
	 * @param rval right var
	 * @return weather to return null
	 */
	protected boolean isReturnNull(ElContext ec, Object lval, Object rval) {
		if (lval == null) {
			if (ec.strict()) {
				throw new ElException(getClass().getSimpleName() + " left object is NULL: " + left);
			}
			return true;
		}
		if (rval == null) {
			if (ec.strict()) {
				throw new ElException("right object is NULL: " + right);
			}
			return true;
		}
		
		return false;
	}
}
