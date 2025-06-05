package panda.el;

import java.util.LinkedList;
import java.util.List;

/**
 * 逆波兰表示法（Reverse Polish
 * notation，RPN，或逆波兰记法），是一种是由波兰数学家扬·武卡谢维奇1920年引入的数学表达式方式，在逆波兰记法中，所有操作符置于操作数的后面，因此也被称为后缀表示法。<br>
 * 参考:<a href="http://zh.wikipedia.org/wiki/%E9%80%86%E6%B3%A2%E5%85%B0%E8%A1%A8%E7%A4%BA%E6%B3%95">逆波兰表达式</a>
 */
public class RPN {
	// 预编译后的对象
	private LinkedList<Object> ops;

	public RPN() {
	}

	/**
	 * 进行EL的预编译
	 * @param rpn the RPN Queue
	 */
	public RPN(List<Object> rpn) {
		ops = operatorTree(rpn);
	}

	/**
	 * 执行已经预编译的EL
	 * 
	 * @param ec the ElContext
	 * @return the calculated value
	 */
	public Object calculate(ELContext ec) {
		return calculate(ec, ops);
	}

	/**
	 * 计算
	 */
	private static Object calculate(ELContext ec, LinkedList<Object> ops) {
		Object obj = ops.peek();
		if (obj instanceof Operator) {
			return ((Operator)obj).calculate(ec);
		}
		if (obj instanceof ELObj) {
			return ((ELObj)obj).getObj(ec);
		}
		return obj;
	}

	/**
	 * 转换成操作树
	 */
	private static LinkedList<Object> operatorTree(List<Object> rpn) {
		LinkedList<Object> operand = new LinkedList<Object>();
		for (Object obj : rpn) {
			if (obj instanceof Operator) {
				Operator opt = (Operator)obj;
				opt.wrap(operand);
			}
			operand.addFirst(obj);
		}
		return operand;
	}
}
