package panda.el.arithmetic;

import java.util.LinkedList;
import java.util.Queue;

import panda.el.ElContext;
import panda.el.Operator;
import panda.el.obj.ElObj;

/**
 * 逆波兰表示法（Reverse Polish
 * notation，RPN，或逆波兰记法），是一种是由波兰数学家扬·武卡谢维奇1920年引入的数学表达式方式，在逆波兰记法中，所有操作符置于操作数的后面，因此也被称为后缀表示法。<br/>
 * 参考:<a href="http://zh.wikipedia.org/wiki/%E9%80%86%E6%B3%A2%E5%85%B0%E8%A1%A8%E7%A4%BA%E6%B3%95">
 * 逆波兰表达式</a>
 */
public class RPN {
	// 预编译后的对象
	private LinkedList<Object> el;

	public RPN() {
	}

	/**
	 * 进行EL的预编译
	 * @param rpn the RPN Queue
	 */
	public RPN(Queue<Object> rpn) {
		compile(rpn);
	}

	/**
	 * 执行已经预编译的EL
	 * 
	 * @param ec the ElContext
	 * @return the calculated value
	 */
	public Object calculate(ElContext ec) {
		return calculate(ec, el);
	}

	/**
	 * 根据逆波兰表达式进行计算
	 * 
	 * @param ec the ElContext
	 * @param rpn the RPN Queue
	 * @return the calculated value
	 */
	public Object calculate(ElContext ec, Queue<Object> rpn) {
		LinkedList<Object> operand = OperatorTree(rpn);
		return calculate(ec, operand);
	}

	/**
	 * 计算
	 */
	private Object calculate(ElContext ec, LinkedList<Object> el2) {
		Object obj = el2.peek();
		if (obj instanceof Operator) {
			return ((Operator)obj).calculate(ec);
		}
		if (obj instanceof ElObj) {
			return ((ElObj)obj).getObj(ec);
		}
		return obj;
	}

	/**
	 * 预先编译
	 * @param rpn the RPN Queue
	 */
	public void compile(Queue<Object> rpn) {
		el = OperatorTree(rpn);
	}

	/**
	 * 转换成操作树
	 */
	private LinkedList<Object> OperatorTree(Queue<Object> rpn) {
		LinkedList<Object> operand = new LinkedList<Object>();
		while (!rpn.isEmpty()) {
			Object obj = rpn.poll();
			if (obj instanceof Operator) {
				Operator opt = (Operator)obj;
				opt.wrap(operand);
				operand.addFirst(opt);
				continue;
			}
			operand.addFirst(obj);
		}
		return operand;
	}
}
