package panda.el.opt.object;

import java.util.List;

import panda.bean.Beans;
import panda.el.ElContext;
import panda.el.ElException;
import panda.el.opt.RunMethod;
import panda.el.opt.TwoTernary;
import panda.lang.Exceptions;
import panda.lang.reflect.Methods;

/**
 * 访问符:'.'
 */
public class AccessOpt extends TwoTernary implements RunMethod {
	
	public AccessOpt() {
	}

	public AccessOpt(Object left, Object right) {
		this.left = left;
		this.right = right;
	}

	public int getPriority() {
		return 1;
	}

	public Object calculate(ElContext ec) {
		Object obj = getLeftVar(ec);
		if (obj == null) {
			if (ec.raiseNullCall()) {
				throw new ElException("obj is NULL, can't call obj." + right);
			}
			return null;
		}
		return Beans.getProperty(obj, right.toString());
	}

	public Object run(ElContext ec, List<?> param) {
		Object obj = getLeftVar(ec);
		if (obj == null) {
			throw new NullPointerException();
		}

		Object[] arg = param.toArray();
		try {
			return Methods.invokeMethod(obj, right.toString(), arg);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public String operator() {
		return ".";
	}
}
