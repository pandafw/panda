package panda.el.arithmetic;

import java.util.LinkedList;
import java.util.Queue;

import panda.el.opt.LArrayOpt;
import panda.el.opt.LBracketOpt;
import panda.el.opt.Operator;
import panda.el.opt.RArrayOpt;
import panda.el.opt.RBracketOpt;
import panda.el.opt.logic.QuestionOpt;
import panda.el.opt.logic.QuestionSelectOpt;
import panda.el.parse.Converter;

/**
 * Shunting yard算法是一个用于将中缀表达式转换为后缀表达式的经典算法，由艾兹格·迪杰斯特拉引入，因其操作类似于火车编组场而得名。<br>
 * 参考: <a href='http://zh.wikipedia.org/wiki/Shunting_yard%E7%AE%97%E6%B3%95'>Shunting yard算法</a>
 */
public class ShuntingYard {
	private LinkedList<Operator> opts;
	private Queue<Object> rpn;

	/**
	 * 转换操作符. 根据 ShuntingYard 算法进行操作
	 * 
	 * @param current
	 */
	private void parseOperator(Operator current) {
		// '(' or '['
		if (current instanceof LBracketOpt || current instanceof LArrayOpt) {
			opts.addFirst(current);
			return;
		}

		// ')'
		if (current instanceof RBracketOpt) {
			Operator op;
			while ((op = opts.poll()) != null) {
				if (op instanceof LBracketOpt) {
					return;
				}
				rpn.add(op);
			}
			throw new IllegalArgumentException("Failed to find '(' for ')'");
		}

		// ']'
		if (current instanceof RArrayOpt) {
			Operator op;
			while ((op = opts.poll()) != null) {
				if (op instanceof LArrayOpt) {
					return;
				}
				rpn.add(op);
			}
			throw new IllegalArgumentException("Failed to find '[' for ']'");
		}

		// 空,直接添加进操作符队列
		if (opts.isEmpty()) {
			opts.addFirst(current);
			return;
		}

		// 符号队列top元素优先级大于当前,则直接添加到
		if (opts.peek().getPriority() > current.getPriority()) {
			opts.addFirst(current);
			return;
		}
		
		// 一般情况,即优先级小于栈顶,那么直接弹出来,添加到逆波兰表达式中
		while (!opts.isEmpty() && opts.peek().getPriority() <= current.getPriority()) {
			// 三元表达式嵌套的特殊处理
			if (opts.peek() instanceof QuestionOpt && current instanceof QuestionOpt) {
				break;
			}
			if (opts.peek() instanceof QuestionOpt && current instanceof QuestionSelectOpt) {
				rpn.add(opts.poll());
				break;
			}
			rpn.add(opts.poll());
		}
		opts.addFirst(current);
	}

	/**
	 * 转换成 逆波兰表示法（Reverse Polish notation，RPN，或逆波兰记法）
	 * 
	 * @param exp expression
	 * @return object queue
	 */
	public Queue<Object> parseToRPN(CharSequence exp) {
		rpn = new LinkedList<Object>();
		opts = new LinkedList<Operator>();

		Converter converter = new Converter(exp);
		converter.initItems();
		while (!converter.isEnd()) {
			Object item = converter.fetchItem();
			if (item instanceof Operator) {
				parseOperator((Operator)item);
				continue;
			}
			rpn.add(item);
		}
		while (!opts.isEmpty()) {
			rpn.add(opts.poll());
		}

		return rpn;
	}
}
