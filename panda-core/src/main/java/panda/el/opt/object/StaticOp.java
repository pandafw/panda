package panda.el.opt.object;

import java.util.List;

import panda.el.ELContext;
import panda.el.ELException;
import panda.el.RunMethod;
import panda.el.opt.DoubleOp;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.reflect.Fields;
import panda.lang.reflect.Methods;

/**
 * operator: '@'
 */
public class StaticOp extends DoubleOp implements RunMethod {
	public int getPriority() {
		return 1;
	}

	@SuppressWarnings("rawtypes")
	private Class getClass(ELContext ec) {
		Object obj = getLeftVar(ec);
		if (obj == null) {
			throw new ELException("obj is NULL, can't call obj@" + right);
		}
		
		Class clz;
		if (obj instanceof Class) {
			clz = (Class)obj;
		}
		else if (obj instanceof String) {
			String c = (String)obj;
			if (Strings.isEmpty(c)) {
				throw new ELException("obj is EMPTY, can't call ''@" + right);
			}

			if (Character.isUpperCase(c.charAt(0))) {
				c = "java.lang." + c;
			}
			
			clz = Classes.load(c);
		}
		else {
			clz = obj.getClass();
		}
		return clz;
	}
	
	public Object calculate(ELContext ec) {
		Class<?> clz = getClass(ec);
		try {
			return Fields.readStaticField(clz, right.toString(), false);
		}
		catch (IllegalAccessException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public Object invoke(ELContext ec, List<?> param) {
		Class<?> clz = getClass(ec);

		Object[] arg = param.toArray();
		try {
			return Methods.invokeStaticMethod(clz, right.toString(), arg);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public String operator() {
		return "@";
	}
}
