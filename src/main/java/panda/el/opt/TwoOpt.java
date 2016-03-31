package panda.el.opt;

import java.util.Queue;

import panda.el.ElContext;
import panda.el.ElException;
import panda.el.Operator;
import panda.el.obj.ElObj;

/**
 * 二元运算,只是提取了公共部分
 */
public abstract class TwoOpt extends AbstractOpt {
	protected Object right;
	protected Object left;

	public void wrap(Queue<Object> rpn) {
		right = rpn.poll();
		left = rpn.poll();
	}

	public Object getRight(ElContext ec) {
		return calculateItem(ec, right);
	}

	public Object getLeft(ElContext ec) {
		return calculateItem(ec, left);
	}

	/**
	 * 取得变得的值
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
	
	protected boolean isReturnNull(ElContext ec, Object lval, Object rval) {
		if (lval == null) {
			if (ec.isStrict()) {
				throw new ElException(getClass().getSimpleName() + " left object is NULL: " + left);
			}
			return true;
		}
		if (rval == null) {
			if (ec.isStrict()) {
				throw new ElException("right object is NULL: " + right);
			}
			return true;
		}
		
		return false;
	}
}
