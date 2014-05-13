package panda.el.opt.object;

import java.util.ArrayList;
import java.util.List;

import panda.el.ElContext;
import panda.el.opt.TwoTernary;

/**
 * "," 逗号操作符,将左右两边的数据组织成一个数据
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class CommaOpt extends TwoTernary {
	public int getPriority() {
		return 99;
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
			objs.add(calculateItem(ec, left));
		}
		objs.add(calculateItem(ec, right));
		return objs;
	}

	public String fetchSelf() {
		return ",";
	}

}
