package panda.el.opt.object;

import java.util.List;

import panda.bean.Beans;
import panda.el.ELContext;
import panda.el.ELException;
import panda.el.RunMethod;
import panda.el.opt.DoubleOp;
import panda.lang.Exceptions;
import panda.lang.reflect.Methods;

/**
 * 访问符:'.'
 */
public class AccessOp extends DoubleOp implements RunMethod {
	
	public AccessOp() {
	}

	public AccessOp(Object left, Object right) {
		this.left = left;
		this.right = right;
	}

	public int getPriority() {
		return 1;
	}

	public Object calculate(ELContext ec) {
		Object obj = getLeftVar(ec);
		if (obj == null) {
			if (ec.strict()) {
				throw new ELException("obj is NULL, can't call obj." + right);
			}
			return null;
		}
		return Beans.getProperty(obj, right.toString());
	}

	public Object invoke(ELContext ec, List<?> param) {
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
