package panda.el.opt;

import java.util.Queue;

import panda.el.ElContext;
import panda.el.Operator;
import panda.el.obj.ElObj;

/**
 * 二元运算,只是提取了公共部分
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public abstract class TwoTernary extends AbstractOpt {
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
		if (left instanceof Operator) {
			return ((Operator)left).calculate(ec);
		}
		if (left instanceof ElObj) {
			return ((ElObj)left).getObj(ec);
		}
		return left;
	}
}
