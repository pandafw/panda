package panda.el.parse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import panda.el.ELException;
import panda.el.obj.ELObj;
import panda.el.opt.LArrayOpt;
import panda.el.opt.LBracketOpt;
import panda.el.opt.RArrayOpt;
import panda.el.opt.RBracketOpt;
import panda.el.opt.arithmetic.DivOpt;
import panda.el.opt.arithmetic.ModOpt;
import panda.el.opt.arithmetic.MulOpt;
import panda.el.opt.arithmetic.NegateOpt;
import panda.el.opt.arithmetic.PlusOpt;
import panda.el.opt.arithmetic.SubOpt;
import panda.el.opt.object.CommaOpt;
import panda.el.opt.object.InvokeMethodOpt;
import panda.el.opt.object.MakeArrayOpt;
import panda.el.opt.object.MethodOpt;

/**
 * 转换器,也就是用来将字符串转换成队列. 这个类的名字不知道取什么好...
 */
public class Converter {
	private final List<Parse> parses = new ArrayList<Parse>();

	// 表达式字符队列
	private CharQueue exp;

	// 表达式项
	private LinkedList<Object> itemCache;

	// 方法栈
	private LinkedList<MethodOpt> methods = new LinkedList<MethodOpt>();

	// 上一个数据
	private Object prev = null;

	public Converter(CharQueue exp) {
		this.exp = exp;
		itemCache = new LinkedList<Object>();
		skipSpace();
		initParse();
	}

	public Converter(CharSequence exp) {
		this(new CharQueue(exp));
	}

	/**
	 * initialize
	 */
	private void initParse() {
		parses.add(new OptParse());
		parses.add(new StringParse());
		parses.add(new IdentifierParse());
		parses.add(new NumberParse());
	}

	/**
	 * 重新设置解析器
	 * @param val the Parse list
	 */
	public void setParse(List<Parse> val) {
		parses.addAll(val);
	}

	/**
	 * 初始化EL项
	 */
	public void initItems() {
		while (!exp.isEmpty()) {
			Object obj = parseItem();
			// 处理数组的情况
			if (obj.getClass().isArray()) {
				for (Object o : (Object[])obj) {
					itemCache.add(o);
				}
				continue;
			}
			itemCache.add(obj);
		}
	}

	/**
	 * 解析数据
	 */
	private Object parseItem() {
		Object obj = Parse.NULL;
		for (Parse parse : parses) {
			obj = parse.fetchItem(exp);
			if (obj != Parse.NULL) {
				skipSpace();
				return parseItem(obj);
			}
		}
		throw new ELException("Failed to parse!");
	}

	/**
	 * 转换数据,主要是转换负号,方法执行
	 */
	private Object parseItem(Object item) {
		// 处理参数个数
		if (methods.peek() != null) {
			MethodOpt opt = methods.peek();
			if (opt.getSize() <= 0) {
				if (!(item instanceof CommaOpt) && !(item instanceof RBracketOpt)) {
					opt.setSize(1);
				}
			}
			else {
				if (item instanceof CommaOpt) {
					opt.setSize(opt.getSize() + 1);
				}
			}
		}

		// '('
		if (item instanceof LBracketOpt) {
			if (prev instanceof ELObj) {
				MethodOpt prem = new MethodOpt();
				item = new Object[] { prem, item };
				methods.addFirst(prem);
			}
			else {
				methods.addFirst(null);
			}
		}

		// ')'
		if (item instanceof RBracketOpt) {
			if (methods.poll() != null) {
				item = new Object[] { item, new InvokeMethodOpt() };
			}
		}

		// '{'
		if (item instanceof LArrayOpt) {
			MethodOpt prem = new MethodOpt();
			item = new Object[] { new MakeArrayOpt(), prem, LBracketOpt.INSTANCE };
			methods.addFirst(prem);
		}

		// '}'
		if (item instanceof RArrayOpt) {
			if (methods.poll() != null) {
				item = new Object[] { RBracketOpt.INSTANCE, new InvokeMethodOpt() };
			}
		}
		
		// 转换负号'-'
		if (item instanceof SubOpt && isNegative(prev)) {
			item = new NegateOpt();
		}
		prev = item;
		return item;
	}

	public static boolean isNegative(Object prev) {
		if (prev == null) {
			return true;
		}
		if (prev instanceof LBracketOpt) {
			return true;
		}
		if (prev instanceof LArrayOpt) {
			return true;
		}
		if (prev instanceof PlusOpt) {
			return true;
		}
		if (prev instanceof MulOpt) {
			return true;
		}
		if (prev instanceof DivOpt) {
			return true;
		}
		if (prev instanceof ModOpt) {
			return true;
		}
		if (prev instanceof SubOpt) {
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

	/**
	 * 取得一个有效数据
	 * @return the fetched item
	 */
	public Object fetchItem() {
		return itemCache.poll();
	}

	/**
	 * 是否结束
	 * @return true if the cache is empty
	 */
	public boolean isEnd() {
		return itemCache.isEmpty();
	}
}
