package panda.el.opt.bit;

import panda.el.ElContext;
import panda.el.opt.TwoTernary;

/**
 * 左移
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class LeftShift extends TwoTernary {
	public int getPriority() {
		return 5;
	}

	public Object calculate(ElContext ec) {
		Integer lval = (Integer)getLeft(ec);
		Integer rval = (Integer)getRight(ec);
		return lval << rval;
	}

	public String operator() {
		return "<<";
	}

}
