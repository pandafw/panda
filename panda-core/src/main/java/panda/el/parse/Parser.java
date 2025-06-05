package panda.el.parse;

import java.util.LinkedList;
import java.util.List;

import panda.el.ELException;
import panda.el.ELObj;
import panda.el.opt.LBraceOp;
import panda.el.opt.LBracketOp;
import panda.el.opt.LParenthesisOp;
import panda.el.opt.RBraceOp;
import panda.el.opt.RBracketOp;
import panda.el.opt.RParenthesisOp;
import panda.el.opt.math.MathAdd;
import panda.el.opt.math.MathDiv;
import panda.el.opt.math.MathMod;
import panda.el.opt.math.MathMul;
import panda.el.opt.math.MathNegate;
import panda.el.opt.math.MathSub;
import panda.el.opt.object.ArrayEndOp;
import panda.el.opt.object.ArrayGetOp;
import panda.el.opt.object.ArrayMakeOp;
import panda.el.opt.object.CommaOp;
import panda.el.opt.object.MethodEndOp;
import panda.el.opt.object.MethodInvokeOp;

public class Parser {
	private static final Parse[] parses = new Parse[] {
		new OperatorParse(),
		new StringParse(),
		new IdentifierParse(),
		new NumberParse()
	};

	private CharQueue exp;

	private LinkedList<MethodInvokeOp> methods;
	private LinkedList<Object> items;

	public Parser(CharSequence exp) {
		this.exp = new CharQueue(exp);
		skipSpace();
	}

	public List<Object> parse() {
		methods = new LinkedList<MethodInvokeOp>();
		items = new LinkedList<Object>();

		while (!exp.isEmpty()) {
			parseItem();
		}

		return items;
	}

	private void parseItem() {
		for (Parse parse : parses) {
			Object obj = parse.fetchItem(exp);
			if (obj != null) {
				skipSpace();
				addItem(obj);
				return;
			}
		}
	}

	private void addItem(Object item) {
		if (methods.peek() != null) {
			MethodInvokeOp opt = methods.peek();
			if (opt.params <= 0) {
				if (!(item instanceof CommaOp) && !(item instanceof RParenthesisOp)) {
					opt.params++;
				}
			} else {
				if (item instanceof CommaOp) {
					opt.params++;
				}
			}
		}

		Object prev = items.peekLast();
		
		// '('
		if (item instanceof LParenthesisOp) {
			if (prev instanceof ELObj) {
				MethodInvokeOp prem = new MethodInvokeOp();
				methods.addFirst(prem);
				items.add(prem);
			} else {
				methods.addFirst(null);
			}
			items.add(item);
			return;
		}

		// ')'
		if (item instanceof RParenthesisOp) {
			items.add(item);
			if (methods.poll() != null) {
				items.add(new MethodEndOp());
			}
			return;
		}

		// '['
		if (item instanceof LBracketOp) {
			items.add(new ArrayGetOp());
			items.add(item);
			return;
		}

		// ']'
		if (item instanceof RBracketOp) {
			items.add(item);
			items.add(new ArrayEndOp());
			return;
		}

		// '{'
		if (item instanceof LBraceOp) {
			MethodInvokeOp prem = new MethodInvokeOp();
			methods.addFirst(prem);
			items.add(new ArrayMakeOp());
			items.add(prem);
			items.add(LParenthesisOp.INSTANCE);
			return;
		}

		// '}'
		if (item instanceof RBraceOp) {
			if (methods.poll() == null) {
				throw new ELException("missing opening brace '{'");
			}
			items.add(RParenthesisOp.INSTANCE);
			items.add(new MethodEndOp());
			return;
		}

		// 转换负号'-'
		if (item instanceof MathSub && isNegative(prev)) {
			items.add(new MathNegate());
			return;
		}

		items.add(item);
		return;
	}

	public static boolean isNegative(Object prev) {
		if (prev == null) {
			return true;
		}
		if (prev instanceof LParenthesisOp) {
			return true;
		}
		if (prev instanceof LBracketOp) {
			return true;
		}
		if (prev instanceof LBraceOp) {
			return true;
		}
		if (prev instanceof MathAdd) {
			return true;
		}
		if (prev instanceof MathMul) {
			return true;
		}
		if (prev instanceof MathDiv) {
			return true;
		}
		if (prev instanceof MathMod) {
			return true;
		}
		if (prev instanceof MathSub) {
			return true;
		}
		return false;
	}

	/**
	 * 跳过空格,并返回是否跳过空格(是否存在空格)
	 */
	private boolean skipSpace() {
		boolean space = false;
		while (!exp.isEmpty() && Character.isWhitespace(exp.peek())) {
			space = true;
			exp.poll();
		}
		return space;
	}
}
