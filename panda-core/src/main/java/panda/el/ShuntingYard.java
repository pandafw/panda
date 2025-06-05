package panda.el;

import java.util.LinkedList;
import java.util.List;

import panda.el.opt.LBraceOp;
import panda.el.opt.LBracketOp;
import panda.el.opt.LParenthesisOp;
import panda.el.opt.RBraceOp;
import panda.el.opt.RBracketOp;
import panda.el.opt.RParenthesisOp;
import panda.el.opt.logic.LogicQuestion;
import panda.el.opt.logic.LogicQuestionSelect;
import panda.el.parse.Parser;

/**
 * Shunting yard算法是一个用于将中缀表达式转换为后缀表达式的经典算法，由艾兹格·迪杰斯特拉引入，因其操作类似于火车编组场而得名。<br>
 * @see https://en.wikipedia.org/wiki/Shunting_yard_algorithm
 */
public class ShuntingYard {
	private LinkedList<Operator> ops;
	private LinkedList<Object> rpn;

	/**
	 * 转换操作符. 根据 ShuntingYard 算法进行操作
	 * 
	 * @param op
	 */
	private void addOperator(Operator op) {
		// '(' or '[' or '{'
		if (op instanceof LParenthesisOp || op instanceof LBracketOp || op instanceof LBraceOp) {
			ops.addFirst(op);
			return;
		}

		// ')'
		if (op instanceof RParenthesisOp) {
			while ((op = ops.poll()) != null) {
				if (op instanceof LParenthesisOp) {
					return;
				}
				rpn.add(op);
			}
			throw new IllegalArgumentException("Failed to find '(' for ')'");
		}

		// ']'
		if (op instanceof RBracketOp) {
			while ((op = ops.poll()) != null) {
				if (op instanceof LBracketOp) {
					return;
				}
				rpn.add(op);
			}
			throw new IllegalArgumentException("Failed to find '[' for ']'");
		}

		// '}'
		if (op instanceof RBraceOp) {
			while ((op = ops.poll()) != null) {
				if (op instanceof LBraceOp) {
					return;
				}
				rpn.add(op);
			}
			throw new IllegalArgumentException("Failed to find '{' for '}'");
		}

		// 空,直接添加进操作符队列
		if (ops.isEmpty()) {
			ops.addFirst(op);
			return;
		}

		// 符号队列top元素优先级大于当前,则直接添加到
		if (ops.peek().getPriority() > op.getPriority()) {
			ops.addFirst(op);
			return;
		}
		
		// 一般情况,即优先级小于栈顶,那么直接弹出来,添加到逆波兰表达式中
		while (!ops.isEmpty() && ops.peek().getPriority() <= op.getPriority()) {
			// 三元表达式嵌套的特殊处理
			if (ops.peek() instanceof LogicQuestion) {
				if (op instanceof LogicQuestion) {
					break;
				}
				if (op instanceof LogicQuestionSelect) {
					rpn.add(ops.poll());
					break;
				}
			}
			rpn.add(ops.poll());
		}
		ops.addFirst(op);
	}

	/**
	 * 转换成 逆波兰表示法（Reverse Polish notation，RPN，或逆波兰记法）
	 * 
	 * @param exp expression
	 * @return object queue
	 */
	public List<Object> parseToRPN(CharSequence exp) {
		rpn = new LinkedList<Object>();
		ops = new LinkedList<Operator>();

		Parser parser = new Parser(exp);
		List<Object> items = parser.parse();
		for (Object item : items) {
			if (item instanceof Operator) {
				addOperator((Operator)item);
				continue;
			}
			rpn.add(item);
		}
		while (!ops.isEmpty()) {
			rpn.add(ops.poll());
		}

		return rpn;
	}
}
