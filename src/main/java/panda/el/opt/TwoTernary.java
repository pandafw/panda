package panda.el.opt;

import java.util.Queue;

import panda.el.ElContext;

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
}
