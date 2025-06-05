package panda.el.opt.object;

import java.util.ArrayList;
import java.util.List;

import panda.el.ELContext;
import panda.el.opt.DoubleOp;

/**
 * "," 逗号操作符,将左右两边的数据组织成一个数据
 */
public class CommaOp extends DoubleOp {
	public int getPriority() {
		return 90;
	}

	@SuppressWarnings("unchecked")
	public Object calculate(ELContext ec) {
		List<Object> objs = new ArrayList<Object>();
		if (left instanceof CommaOp) {
			List<Object> tem = (List<Object>)((CommaOp)left).calculate(ec);
			for (Object t : tem) {
				objs.add(t);
			}
		} else {
			objs.add(calcLeft(ec));
		}
		objs.add(calcRight(ec));
		return objs;
	}

	public String operator() {
		return ",";
	}

}
