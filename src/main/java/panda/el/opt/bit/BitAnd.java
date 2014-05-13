package panda.el.opt.bit;

import panda.el.ElContext;
import panda.el.opt.TwoTernary;

/**
 * 与
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class BitAnd extends TwoTernary {
	public int getPriority() {
		return 8;
	}

	public Object calculate(ElContext ec) {
		Integer lval = (Integer)calculateItem(ec, left);
		Integer rval = (Integer)calculateItem(ec, right);
		return lval & rval;
	}

	public String fetchSelf() {
		return "&";
	}

}
