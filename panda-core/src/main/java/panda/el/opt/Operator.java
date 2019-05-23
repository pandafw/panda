package panda.el.opt;

import java.util.Queue;

import panda.el.ELContext;

/**
 * 操作符
 */
public interface Operator {

	/**
	 * @return the priority of the operator
	 */
	public int getPriority();

	/**
	 * 打包数据. 每个操作符都有相应的操作数,这个方法目的在于,根据操作符自身的需求,从operand中读取相应的操作数
	 * 
	 * @param operand 操作数
	 */
	public void wrap(Queue<Object> operand);

	/**
	 * @param ec the ElContext
	 * @return the calculated value
	 */
	public Object calculate(ELContext ec);

}
