package panda.el.opt.object;

import java.lang.reflect.Array;
import java.util.Map;

import panda.el.ElContext;
import panda.el.opt.TwoTernary;

/**
 * 数组读取 将'['做为读取操作符使用,它读取两个参数,一个是数组本身,一个是下标 多维数组,则是先读出一维,然后再读取下一维度的数据
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class ArrayOpt extends TwoTernary {
	public int getPriority() {
		return 1;
	}

	public Object calculate(ElContext ec) {
		Object lval = getLeft(ec);
		Object rval = getRight(ec);

		// @ JKTODO 这里要不要与, AccessOpt 里面相同的代码合并呢?
		if (lval instanceof Map) {
			Map<?, ?> om = (Map<?, ?>)lval;
			if (om.containsKey(right.toString())) {
				return om.get(right.toString());
			}
		}

		return Array.get(lval, (Integer)rval);
	}

	public String operator() {
		return "[";
	}
}
