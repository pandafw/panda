package panda.el.opt.object;

import java.util.ArrayList;
import java.util.List;

import panda.el.ElContext;
import panda.el.opt.TwoOpt;

/**
 * "," 逗号操作符,将左右两边的数据组织成一个数据
 */
public class CommaOpt extends TwoOpt {
	public int getPriority() {
		return 90;
	}

	@SuppressWarnings("unchecked")
	public Object calculate(ElContext ec) {
		List<Object> objs = new ArrayList<Object>();
		if (left instanceof CommaOpt) {
			List<Object> tem = (List<Object>)((CommaOpt)left).calculate(ec);
			for (Object t : tem) {
				objs.add(t);
			}
		}
		else {
			objs.add(getLeft(ec));
		}
		objs.add(getRight(ec));
		return objs;
	}

	public String operator() {
		return ",";
	}

}
