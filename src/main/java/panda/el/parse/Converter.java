package panda.el.parse;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import panda.el.ElException;
import panda.el.Parse;
import panda.el.obj.ElObj;
import panda.el.obj.FieldObj;
import panda.el.obj.IdentifierObj;
import panda.el.obj.MethodObj;
import panda.el.opt.LArrayOpt;
import panda.el.opt.LBracketOpt;
import panda.el.opt.RArrayOpt;
import panda.el.opt.RBracketOpt;
import panda.el.opt.RunMethod;
import panda.el.opt.arithmetic.NegativeOpt;
import panda.el.opt.arithmetic.SubOpt;
import panda.el.opt.object.CommaOpt;
import panda.el.opt.object.InvokeMethodOpt;
import panda.el.opt.object.MakeArrayOpt;
import panda.el.opt.object.MethodOpt;

/**
 * 转换器,也就是用来将字符串转换成队列. 这个类的名字不知道取什么好...
 * 
 * @author juqkai(juqkai@gmail.com)
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

	public Converter(CharQueue reader) {
		this.exp = reader;
		itemCache = new LinkedList<Object>();
		skipSpace();
		initParse();
	}

	public Converter(String val) {
		this(new StringReader(val));
	}

	public Converter(Reader reader) {
		this(new CharQueueDefault(reader));
	}

	/**
	 * 初始化解析器
	 */
	private void initParse() {
		parses.add(new OptParse());
		parses.add(new StringParse());
		parses.add(new IdentifierParse());
		parses.add(new ValParse());
	}

	/**
	 * 重新设置解析器
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
		itemCache = clearUp(itemCache);
	}

	/**
	 * 清理转换后的结果, 主要做一些标识性的转换
	 * 
	 * @param rpn
	 */
	private LinkedList<Object> clearUp(LinkedList<Object> rpn) {
		LinkedList<Object> dest = new LinkedList<Object>();
		while (!rpn.isEmpty()) {
			if (!(rpn.getFirst() instanceof ElObj)) {
				dest.add(rpn.removeFirst());
				continue;
			}
			
			ElObj obj = (ElObj)rpn.removeFirst();
			// 方法对象
			if (!rpn.isEmpty() && rpn.getFirst() instanceof MethodOpt) {
				dest.add(new MethodObj(obj.getVal()));
				continue;
			}
			// 属性对象
			if (dest.size() > 0 && dest.getLast() instanceof RunMethod 
					&& rpn.size() > 0
					&& rpn.getFirst() instanceof RunMethod) {
				dest.add(new FieldObj(obj.getVal()));
				continue;
			}
			// //普通的对象
			// if(!(dest.getLast() instanceof AccessOpt) && !(rpn.peekFirst()
			// instanceof MethodOpt)){
			// continue;
			// }
			dest.add(new IdentifierObj(obj.getVal()));
		}
		return dest;
	}

	/**
	 * 解析数据
	 */
	private Object parseItem() {
		Object obj = Parse.nullobj;
		for (Parse parse : parses) {
			obj = parse.fetchItem(exp);
			if (obj != Parse.nullobj) {
				skipSpace();
				return parseItem(obj);
			}
		}
		throw new ElException("Failed to parse!");
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
			if (prev instanceof ElObj) {
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
		if (item instanceof SubOpt && NegativeOpt.isNegetive(prev)) {
			item = new NegativeOpt();
		}
		prev = item;
		return item;
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
	 */
	public Object fetchItem() {
		return itemCache.poll();
	}

	/**
	 * 是否结束
	 */
	public boolean isEnd() {
		return itemCache.isEmpty();
	}
}
