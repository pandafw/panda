package panda.el.opt.object;

import java.lang.reflect.Array;

import panda.bean.Beans;
import panda.el.ELContext;
import panda.el.ELException;
import panda.el.opt.AbstractTwoOpt;

/**
 * 数组读取 将'['做为读取操作符使用,它读取两个参数,一个是数组本身,一个是下标 多维数组,则是先读出一维,然后再读取下一维度的数据
 */
public class ArrayOpt extends AbstractTwoOpt {
	public int getPriority() {
		return 1;
	}

	public Object calculate(ELContext ec) {
		Object lval = getLeft(ec);
		Object rval = getRight(ec);

		if (lval == null) {
			if (ec.strict()) {
				throw new ELException("obj is NULL, can't call obj['" + rval + "']");
			}
			return null;
		}

		if (lval.getClass().isArray()) {
			return Array.get(lval, (Integer)rval);
		}
		return Beans.getProperty(lval, rval.toString());
	}

	public String operator() {
		return "[";
	}
}
