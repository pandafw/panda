package panda.el;

import java.util.Queue;

import panda.el.arithmetic.RPN;
import panda.el.arithmetic.ShuntingYard;

public class El {
	private RPN rpn;
	private CharSequence expr;

	/**
	 * compile
	 */
	public El(CharSequence cs) {
		expr = cs;
		ShuntingYard sy = new ShuntingYard();
		Queue<Object> que = sy.parseToRPN(cs.toString());
		rpn = new RPN(que);
	}

	/**
	 * 解析预编译后的EL表达式
	 */
	public Object eval() {
		ElContext ec = new ElContext(null);
		return rpn.calculate(ec);
	}

	public Object eval(Object context) {
		ElContext ec = new ElContext(context);
		return rpn.calculate(ec);
	}

	public String toString() {
		return expr.toString();
	}

	public static El parse(String expr) {
		return new El(expr);
	}
	
	/**
	 * 对参数代表的表达式进行运算
	 */
	public static Object eval(String expr) {
		// 逆波兰表示法（Reverse Polish notation，RPN，或逆波兰记法）
		return eval(null, expr);
	}

	public static Object eval(Object context, String expr) {
		return new El(expr).eval(context);
	}

	/**
	 * 说明: 1. 操作符优先级参考<Java运算符优先级参考图表>, 但不完全遵守,比如"()" 2. 使用Queue 的原因是,调用peek()方法不会读取串中的数据.
	 * 因为我希望达到的效果是,我只读取我需要的,我不需要的数据我不读出来.
	 */

	// @ JKTODO 删除原来的EL包,并修改当前为EL
	// @ JKTODO 自己实现一个QUEUE接口, 主要是实现队列,头部检测,头部第几个元素检测
}
