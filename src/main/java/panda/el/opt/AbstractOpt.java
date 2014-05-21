package panda.el.opt;

import panda.el.ElContext;
import panda.el.ElException;
import panda.el.Operator;
import panda.el.obj.ElObj;

/**
 * 操作符抽象类
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public abstract class AbstractOpt implements Operator {
	/**
	 * 操作符对象自身的符号
	 */
	public abstract String operator();

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (obj.equals(operator())) {
			return true;
		}
		return super.equals(obj);
	}

	public String toString() {
		return String.valueOf(operator());
	}

	/**
	 * 计算子项
	 */
	protected Object calculateItem(ElContext ec, Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Number) {
			return obj;
		}
		if (obj instanceof Boolean) {
			return obj;
		}
		if (obj instanceof String) {
			return obj;
		}
		if (obj instanceof ElObj) {
			return ((ElObj)obj).getObj(ec);
		}
		if (obj instanceof Operator) {
			return ((Operator)obj).calculate(ec);
		}
		throw new ElException("Unknown calculate value: " + obj);

	}
}
